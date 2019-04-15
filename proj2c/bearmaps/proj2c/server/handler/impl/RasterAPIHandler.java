package bearmaps.proj2c.server.handler.impl;

import bearmaps.proj2c.AugmentedStreetMapGraph;
import bearmaps.proj2c.server.handler.APIRouteHandler;
import spark.Request;
import spark.Response;
import bearmaps.proj2c.utils.Constants;

import javax.imageio.ImageIO;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static bearmaps.proj2c.utils.Constants.SEMANTIC_STREET_GRAPH;
import static bearmaps.proj2c.utils.Constants.ROUTE_LIST;

/**
 * Handles requests from the web browser for map images. These images
 * will be rastered into one large image to be displayed to the user.
 * @author rahul, Josh Hug, Daniel Vazquez
 */
public class RasterAPIHandler extends APIRouteHandler<Map<String, Double>, Map<String, Object>> {

    /**
     * Each raster request to the server will have the following parameters
     * as keys in the params map accessible by,
     * i.e., params.get("ullat") inside RasterAPIHandler.processRequest(). <br>
     * ullat : upper left corner latitude, <br> ullon : upper left corner longitude, <br>
     * lrlat : lower right corner latitude,<br> lrlon : lower right corner longitude <br>
     * w : user viewport window width in pixels,<br> h : user viewport height in pixels.
     **/
    private static final String[] REQUIRED_RASTER_REQUEST_PARAMS = {"ullat", "ullon", "lrlat",
            "lrlon", "w", "h"};

    /**
     * The result of rastering must be a map containing all of the
     * fields listed in the comments for RasterAPIHandler.processRequest.
     **/
    private static final String[] REQUIRED_RASTER_RESULT_PARAMS = {"render_grid", "raster_ul_lon",
            "raster_ul_lat", "raster_lr_lon", "raster_lr_lat", "depth", "query_success"};


    @Override
    protected Map<String, Double> parseRequestParams(Request request) {
        return getRequestParams(request, REQUIRED_RASTER_REQUEST_PARAMS);
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param requestParams Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @param response : Not used by this function. You may ignore.
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image;
     *                    can also be interpreted as the length of the numbers in the image
     *                    string. <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    @Override
    public Map<String, Object> processRequest(Map<String, Double> requestParams, Response response) {
        Map<String, Object> results = new HashMap<>();
        //System.out.println(requestParams);
        // get individual parameters
        double ullon = requestParams.get("ullon");
        double ullat = requestParams.get("ullat");
        double lrlon = requestParams.get("lrlon");
        double lrlat = requestParams.get("lrlat");
        double wres = requestParams.get("w");
        double hres = requestParams.get("h");
        /*
        System.out.println("------------ INPUT ------------ ");
        for (Map.Entry<String, Double> a : requestParams.entrySet()) {
            System.out.println(a.getKey() + ": " + a.getValue());
        }
        System.out.println("------------ END INPUT ------------ ");
        */

        //getting required data

        // check that coordinates are in order
        if (lrlon < ullon || lrlat > ullat) {
            return queryFail();
        }


        int depth = getDepth(ullon, lrlon, wres); // depth level
        int ulRow = getRow(ullat, depth); // y_cord first tile
        int ulCol = getCol(ullon, depth); // x_cord first tile
        int lrRow = getRow(lrlat, depth); // y_cord last tile
        int lrCol = getCol(lrlon, depth); // x_cord last tile
        double rasterUllon = ullon(ulCol, depth); // lon first tile (upper left corner)
        double rasterUllat = ullat(ulRow, depth); // lat first tile (upper left corner)
        double rasterLrlon = lrlon(lrCol, depth); // lon last tile (lower right corner)
        double rasterLrlat = lrlat(lrRow, depth); // lat last tile (lower right corner)
        String[][] images = getImages(ulRow, ulCol,  lrRow, lrCol, depth);

        // filling response Map
        results.put("render_grid", images);
        results.put("raster_ul_lon", rasterUllon);
        results.put("raster_ul_lat", rasterUllat);
        results.put("raster_lr_lon", rasterLrlon);
        results.put("raster_lr_lat", rasterLrlat);
        results.put("depth", depth);
        results.put("query_success", true);

        /*
        System.out.println("------------ OUTPUT ------------ ");
        for (Map.Entry<String, Object> a : results.entrySet()) {
            System.out.println(a.getKey() + ": " + a.getValue());
        }
        System.out.println("------------ END OUTPUT ------------ ");
        */

        return results;
    }

    private int getCol(double lon, int depth) {
        // if lon is larger than ROOT_LRLON, then return largest possible column value
        if (lon > Constants.ROOT_LRLON) {
            return (int) Math.pow(2, depth) - 1;
        }
        // if lon is smaller than ROOT_ULLON, then return smallest possible column value
        if(lon < Constants.ROOT_ULLON) {
            return 0;
        }

        int col = 0;
        while (lrlon(col, depth) < lon) {
            col++;
        }
        return col;
    }

    private int getRow(double lat, int depth) {
        // if lat is larger than ROOT_ULLAT, then return smallest possible row available
        if (lat > Constants.ROOT_ULLAT) {
            return 0;
        }

        // if lat is smaller than ROOT_LRLAT, then return largest possible row available
        if (lat < Constants.ROOT_LRLAT) {
            return (int) Math.pow(2, depth) - 1;
        }

        int row = 0;
        while (lrlat(row, depth) > lat) {
            row++;
        }
        return row;
    }

    private double ullon(int col, int depth) {
        double wDelta = (Constants.ROOT_LRLON - Constants.ROOT_ULLON) / Math.pow(2, depth);
        if (col == 0) {
            return Constants.ROOT_ULLON;
        }

        double ullon = Constants.ROOT_ULLON; // start at the leftmost (negative) position
        for (int c = 1; c <= col; c++) {
            ullon += wDelta; // increase by w_delta

        }
        return ullon;
    }

    private double lrlon(int col, int depth) {
        double wDelta = (Constants.ROOT_LRLON - Constants.ROOT_ULLON) / Math.pow(2, depth);
        return ullon(col, depth) + wDelta;
    }

    private double ullat(int row, int depth) {
        double hDelta = (Constants.ROOT_ULLAT - Constants.ROOT_LRLAT) / Math.pow(2, depth);
        if (row == 0) {
            return Constants.ROOT_ULLAT;
        }

        double ullat = Constants.ROOT_ULLAT; // start at the topmost (positive) position
        for (int c = 1; c <= row; c++) {
            ullat -= hDelta; // decrease by h_delta at given depth

        }
        return ullat;
    }

    private double lrlat(int row, int depth) {
        double hDelta = (Constants.ROOT_ULLAT - Constants.ROOT_LRLAT) / Math.pow(2, depth);
        return ullat(row, depth) - hDelta; // just add h_delta to ullat
    }

    /**
     * Generates an grid of image tiles names to be used in the front-end rastering
     * @param ulRow the upper left tile row-coordinate
     * @param ulCol the upper left tile col-coordinate
     * @param lrRow the lower right tile row-coordinate
     * @param lrCol the lower right tile col-coordinate
     * @param depth the depth (resolution)
     * @return grid of image names
     */
    private String[][] getImages(int ulRow, int ulCol, int lrRow, int lrCol, int depth) {
        int rows = (lrRow - ulRow) + 1;
        int cols = (lrCol - ulCol) + 1;

        String[][] images = new String[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                images[row][col] = "d" + depth + "_x" + (ulCol + col)
                        + "_y" + (ulRow + row) + ".png";
            }
        }
        return images;
    }


    /**
     * Calculates the longitudinal distance per pixel (LonDPP)
     * LonDPP = (lower right longitude − upper left longitude) / width of
     * the image (or box) in pixels
     * @param ullon upper left longitude
     * @param lrlon lower right longitude
     * @param wres screen width resolution
     * @return LonDPP
     */
    private double getLonDPP(double ullon, double lrlon, double wres) {
        return (lrlon - ullon) / wres;
    }

    /**
     * Calculates the appropriate resolution depth level
     * @param ullon upper left longitude
     * @param lrlon lower right longitude
     * @param wres screen width resolution
     * @return
     */
    private int getDepth(double ullon, double lrlon, double wres) {
        double targetLDPP = getLonDPP(ullon, lrlon, wres);
        double maxLDPP = getLonDPP(Constants.ROOT_ULLON, Constants.ROOT_LRLON, Constants.TILE_SIZE);
        double currentLonDPP = maxLDPP; // lowest resolution (a pixel represents lost of feet)
        int maxDepth = 7;
        int targetDepth = 0;

        while (currentLonDPP > targetLDPP && targetDepth < maxDepth) {
            targetDepth += 1;
            currentLonDPP = maxLDPP / (Math.pow(2, targetDepth)); // increase resolution
        }
        return targetDepth;
    }

    @Override
    protected Object buildJsonResponse(Map<String, Object> result) {
        boolean rasterSuccess = validateRasteredImgParams(result);

        if (rasterSuccess) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            writeImagesToOutputStream(result, os);
            String encodedImage = Base64.getEncoder().encodeToString(os.toByteArray());
            result.put("b64_encoded_image_data", encodedImage);
        }
        return super.buildJsonResponse(result);
    }

    private Map<String, Object> queryFail() {
        Map<String, Object> results = new HashMap<>();

        results.put("render_grid", null);
        results.put("raster_ul_lon", 0);
        results.put("raster_ul_lat", 0);
        results.put("raster_lr_lon", 0);
        results.put("raster_lr_lat", 0);
        results.put("depth", 0);
        results.put("query_success", false);

        /*
        results.put("render_grid",  new String[][]{{"d0_x0_y0.png"}});
        results.put("raster_ul_lon", Constants.ROOT_ULLON);
        results.put("raster_ul_lat", Constants.ROOT_ULLAT);
        results.put("raster_lr_lon", Constants.ROOT_LRLON);
        results.put("raster_lr_lat", Constants.ROOT_LRLAT);
        results.put("depth", 0);
        results.put("query_success", true);
        */

        return results;
    }

    /**
     * Validates that Rasterer has returned a result that can be rendered.
     * @param rip : Parameters provided by the rasterer
     */
    private boolean validateRasteredImgParams(Map<String, Object> rip) {
        for (String p : REQUIRED_RASTER_RESULT_PARAMS) {
            if (!rip.containsKey(p)) {
                System.out.println("Your rastering result is missing the " + p + " field.");
                return false;
            }
        }
        if (rip.containsKey("query_success")) {
            boolean success = (boolean) rip.get("query_success");
            if (!success) {
                System.out.println("query_success was reported as a failure");
                return false;
            }
        }
        return true;
    }

    /**
     * Writes the images corresponding to rasteredImgParams to the output stream.
     * In Spring 2016, students had to do this on their own, but in 2017,
     * we made this into provided code since it was just a bit too low level.
     */
    private  void writeImagesToOutputStream(Map<String, Object> rasteredImageParams,
                                                  ByteArrayOutputStream os) {
        String[][] renderGrid = (String[][]) rasteredImageParams.get("render_grid");
        int numVertTiles = renderGrid.length;
        int numHorizTiles = renderGrid[0].length;

        BufferedImage img = new BufferedImage(numHorizTiles * Constants.TILE_SIZE,
                numVertTiles * Constants.TILE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics graphic = img.getGraphics();
        int x = 0, y = 0;

        for (int r = 0; r < numVertTiles; r += 1) {
            for (int c = 0; c < numHorizTiles; c += 1) {
                graphic.drawImage(getImage(Constants.IMG_ROOT + renderGrid[r][c]), x, y, null);
                x += Constants.TILE_SIZE;
                if (x >= img.getWidth()) {
                    x = 0;
                    y += Constants.TILE_SIZE;
                }
            }
        }

        /* If there is a route, draw it. */
        double ullon = (double) rasteredImageParams.get("raster_ul_lon"); //tiles.get(0).ulp;
        double ullat = (double) rasteredImageParams.get("raster_ul_lat"); //tiles.get(0).ulp;
        double lrlon = (double) rasteredImageParams.get("raster_lr_lon"); //tiles.get(0).ulp;
        double lrlat = (double) rasteredImageParams.get("raster_lr_lat"); //tiles.get(0).ulp;

        final double wdpp = (lrlon - ullon) / img.getWidth();
        final double hdpp = (ullat - lrlat) / img.getHeight();
        AugmentedStreetMapGraph graph = SEMANTIC_STREET_GRAPH;
        List<Long> route = ROUTE_LIST;

        if (route != null && !route.isEmpty()) {
            Graphics2D g2d = (Graphics2D) graphic;
            g2d.setColor(Constants.ROUTE_STROKE_COLOR);
            g2d.setStroke(new BasicStroke(Constants.ROUTE_STROKE_WIDTH_PX,
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            route.stream().reduce((v, w) -> {
                g2d.drawLine((int) ((graph.lon(v) - ullon) * (1 / wdpp)),
                        (int) ((ullat - graph.lat(v)) * (1 / hdpp)),
                        (int) ((graph.lon(w) - ullon) * (1 / wdpp)),
                        (int) ((ullat - graph.lat(w)) * (1 / hdpp)));
                return w;
            });
        }

        rasteredImageParams.put("raster_width", img.getWidth());
        rasteredImageParams.put("raster_height", img.getHeight());

        try {
            ImageIO.write(img, "png", os);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private BufferedImage getImage(String imgPath) {
        BufferedImage tileImg = null;
        if (tileImg == null) {
            try {
                File in = new File(imgPath);
                tileImg = ImageIO.read(in);
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        return tileImg;
    }
}
