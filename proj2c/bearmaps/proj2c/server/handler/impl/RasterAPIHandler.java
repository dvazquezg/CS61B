package bearmaps.proj2c.server.handler.impl;

import bearmaps.proj2c.AugmentedStreetMapGraph;
import bearmaps.proj2c.server.handler.APIRouteHandler;
import spark.Request;
import spark.Response;
import bearmaps.proj2c.utils.Constants;

import javax.imageio.ImageIO;
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
 * @author rahul, Josh Hug, _________
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
        System.out.println("yo, wanna know the parameters given by the web browser? They are:");
        System.out.println(requestParams);
        Map<String, Object> results = new HashMap<>();
        System.out.println("Since you haven't implemented RasterAPIHandler.processRequest, nothing is displayed in "
                + "your browser.");
        // get individual parameters
        double ullon = requestParams.get("ullon");
        double ullat = requestParams.get("ullat");
        double lrlon = requestParams.get("lrlon");
        double lrlat = requestParams.get("lrlat");
        double w_res = requestParams.get("w");
        double h_res = requestParams.get("h");

        for (Map.Entry<String, Double> a : requestParams.entrySet()) {
            System.out.println(a.getKey() + ": " + a.getValue());
        }

        int depth = getDepth(ullon, lrlon, w_res);
        System.out.println("Required depth: " + depth);
        //double ullon, double lrlon, double ullat, double lrlat, double depth
        double[] raster_bounds = getRasterBounds(ullon, lrlon, ullat, lrlat, depth);
        String[][] images = get_images(ullon, lrlon, ullat, lrlat, w_res, h_res, depth);

        results.put("render_grid", images);
        results.put("raster_ul_lon", raster_bounds[0]);
        results.put("raster_ul_lat", raster_bounds[1]);
        results.put("raster_lr_lon", raster_bounds[2]);
        results.put("raster_lr_lat", raster_bounds[3]);
        results.put("depth", depth);
        results.put("query_success", true);

        return results;
    }

    public int getCol(double lon, int depth){
        double w_delta = (Constants.ROOT_LRLON - Constants.ROOT_ULLON) / Math.pow(2, depth);

        int col = 0;
        System.out.println("lon " + lon);
        while(lrlon(col, depth) < lon) {
            col++;
            //System.out.println(lrlon(col, depth) + " -> col: " + col);
        }
        return col;
    }

    public int getRow(double lat, int depth) {
        double h_delta = (Constants.ROOT_ULLAT - Constants.ROOT_LRLAT) / Math.pow(2, depth);
        int row = 0;
        while(lrlat(row, depth) > lat) {
            row++;
        }
        return row;
    }

    /*
    public int getLCol(double lrlon, int depth){
        double w_delta = (Constants.ROOT_LRLON - Constants.ROOT_ULLON) / Math.pow(2, depth);

        int lcol = 0;
        while(lrlon(lcol, depth) < lrlon) {
            lcol++;
        }
        return lcol;
    }*/

    public double ullon(int col, int depth){
        double w_delta = (Constants.ROOT_LRLON - Constants.ROOT_ULLON) / Math.pow(2, depth);

        if (col == 0){
            return Constants.ROOT_ULLON;
        }

        double ullon = Constants.ROOT_ULLON;
        for (int c = 1; c <= col; c++) {
            ullon += w_delta;

        }
        return ullon;
    }

    public double lrlon(int col, int depth){
        double w_delta = (Constants.ROOT_LRLON - Constants.ROOT_ULLON) / Math.pow(2, depth);
        return ullon(col, depth) + w_delta;
    }

    public double ullat(int row, int depth){
        double h_delta = (Constants.ROOT_ULLAT - Constants.ROOT_LRLAT) / Math.pow(2, depth);
        if (row == 0){
            return Constants.ROOT_ULLAT;
        }

        double ullat = Constants.ROOT_ULLAT;
        for (int c = 1; c <= row; c++) {
            ullat -= h_delta;

        }
        return ullat;
    }

    public double lrlat(int row, int depth){
        double h_delta = (Constants.ROOT_ULLAT - Constants.ROOT_LRLAT) / Math.pow(2, depth);
        return ullat(row, depth) - h_delta;
    }



    private String[][] get_images(double ullon, double lrlon, double ullat, double lrlat, double w, double h, int depth) {
        double max_width = (Constants.ROOT_LRLON - Constants.ROOT_ULLON); // max
        double root_ullon_to_ullon = Math.abs(ullon - Constants.ROOT_ULLON);
        double root_ullon_to_lrlon = Math.abs(lrlon - Constants.ROOT_ULLON);
        int left_col = (int) (root_ullon_to_ullon * (Math.pow(2, depth)) / max_width); // floor div
        int right_col = (int) (root_ullon_to_lrlon * (Math.pow(2, depth)) / max_width); // floor div
        //int right_col = left_col + ((int) (w / Constants.TILE_SIZE)) - 1;
        System.out.println("left_col: " + left_col + " | right_col: "+ right_col);
        System.out.println("Right col: " + find_RightCol(left_col, ullon, lrlon, depth));

        double max_heigth = (Constants.ROOT_ULLAT - Constants.ROOT_LRLAT); // max
        double root_ullat_to_ullat = (Constants.ROOT_ULLAT - ullat);
        double root_lrlat_to_ullat = (Constants.ROOT_ULLAT - lrlat);
        int top_row = (int) (root_ullat_to_ullat * (Math.pow(2, depth)) / max_heigth);
        int bottom_row = (int) (root_lrlat_to_ullat * (Math.pow(2, depth)) / max_heigth);
        //int bottom_row = top_row + ((int) (h / Constants.TILE_SIZE));

        System.out.println("top_row: " + top_row + " | bottom_row: "+ bottom_row);

        int rows = (bottom_row - top_row) + 1;
        int cols = (right_col - left_col) + 1;

        String[][] images = new String[rows][cols];
        System.out.println("" + (rows) + "x" + (cols));
        for(int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                images[row][col] = "d" + depth + "_x" + (left_col + col) + "_y" + (top_row + row) + ".png";
                System.out.print(images[row][col] + " ");
            }
            System.out.println();
        }

        return images;
    }

    private int find_RightCol(int left_col, double ullon, double lrlon, int depth){
        double blockWidth = (Constants.ROOT_LRLON - Constants.ROOT_ULLON) / Math.pow(2, depth);
        double root_ullon_to_ullon = Math.abs(ullon - Constants.ROOT_ULLON);
        double dist_left_right = Math.abs(lrlon - ullon);
        double currlon = blockWidth; // right edge of end of col

        int right_col = left_col;
        while(currlon < dist_left_right) {
            right_col++;
            currlon +=blockWidth;
        }
        return right_col;
    }

    /**
     * Returns an array containing the corner bounds of the raster area
     * @return
     */
    private double[] getRasterBounds(double ullon, double lrlon, double ullat, double lrlat, double depth) {
        double blockWidth = (Constants.ROOT_LRLON - Constants.ROOT_ULLON) / Math.pow(2, depth);
        double blockHeight = (Constants.ROOT_ULLAT - Constants.ROOT_LRLAT) / Math.pow(2, depth);
        System.out.println("blockwidth: " + blockWidth);
        System.out.println("blockHeight: " + blockHeight);
        double alpha_ullon = Math.floor(ullon / blockWidth); // blockWidth factor for ullon
        double alpha_lrlon = Math.ceil(lrlon / blockWidth); // blockWidth factor for lrlon
        double beta_ullat = Math.ceil(ullat / blockHeight); // blockHeight factor for ullat
        double beta_lrlat = Math.floor(lrlat / blockHeight); // blockHeight factor for lrlat

        double raster_ul_lon = alpha_ullon * blockWidth;
        double raster_lr_lon = alpha_lrlon * blockWidth;
        double raster_ul_lat = beta_ullat * blockHeight;
        double raster_lr_lat = beta_lrlat * blockHeight;

        System.out.println("alpha_ullon: " + alpha_ullon);
        System.out.println("raster_ul_lon: " + raster_ul_lon);
        System.out.println("raster_ul_lat: " + raster_ul_lat);
        System.out.println("raster_lr_lon: " + raster_lr_lon);
        System.out.println("raster_lr_lat: " + raster_lr_lat);
        /*
        -122.24212646484375;
        0.0005421337
        5.421337009124394E-4
        0.000542133701

        -122.24006652832031;

        37.87701580361881;

        37.87538940251607;*/

        return new double[]{raster_ul_lon, raster_ul_lat, raster_lr_lon, raster_lr_lat};
    }



    /**
     * Calculates the longitudinal distance per pixel (LonDPP)
     * LonDPP = (lower right longitude − upper left longitude) / width of the image (or box) in pixels
     * @param ullon upper left longitude
     * @param lrlon lower right longitude
     * @param w_res screen width resolution
     * @return LonDPP
     */
    private double getLonDPP(double ullon, double lrlon, double w_res) {
        return (lrlon - ullon) / w_res;
    }

    /**
     * Calculates the appropriate resolution depth level
     * @param ullon upper left longitude
     * @param lrlon lower right longitude
     * @param w_res screen width resolution
     * @return
     */
    private int getDepth(double ullon, double lrlon, double w_res) {
        double targetLonDPP = getLonDPP(ullon, lrlon, w_res);
        double maxLonDPP = getLonDPP(Constants.ROOT_ULLON, Constants.ROOT_LRLON, Constants.TILE_SIZE);
        double currentLonDPP = maxLonDPP;
        int maxDepth = 7;
        int targetDepth = 0;

        while (currentLonDPP > targetLonDPP && targetDepth < maxDepth){
            //root_lrlon = (root_ullon + root_lrlon) / 2; // midpoint
            //currentLonDPP = getLonDPP(root_ullon, root_lrlon, Constants.TILE_SIZE);
            targetDepth += 1;
            currentLonDPP = maxLonDPP / (Math.pow(2, targetDepth));

            System.out.println("target: " + targetLonDPP + " current: " + currentLonDPP + "level: " + targetDepth);
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
