package bearmaps.test;

import bearmaps.proj2c.AugmentedStreetMapGraph;
import org.apache.commons.math3.analysis.function.Constant;
import org.junit.Before;
import org.junit.Test;
import bearmaps.proj2c.server.handler.impl.RasterAPIHandler;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.StringJoiner;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import bearmaps.proj2c.utils.Constants;

/** Test of the rastering part of the assignment.*/
public class TestRasterAPIHandler {
    private static final double DOUBLE_THRESHOLD = 0.000000001;
    private static DecimalFormat df2 = new DecimalFormat(".#########");
    private static final String PARAMS_FILE = "../library-sp19/data/proj2c_test_inputs/raster_params.txt";
    private static final String RESULTS_FILE = "../library-sp19/data/proj2c_test_inputs/raster_results.txt";
    private static final int NUM_TESTS = 8;
    private static RasterAPIHandler rasterer;


    @Before
    public void setUp() throws Exception {
        rasterer = new RasterAPIHandler();
    }

    @Test
    public void testProcessRequests() throws Exception {
        List<Map<String, Double>> testParams = paramsFromFile();
        List<Map<String, Object>> expectedResults = resultsFromFile();

        for (int i = 0; i < NUM_TESTS; i++) {
            System.out.println(String.format("Running test: %d", i));
            Map<String, Double> params = testParams.get(i);
            Map<String, Object> actual = rasterer.processRequest(params, null);
            Map<String, Object> expected = expectedResults.get(i);
            String msg = "Your results did not match the expected results for input "
                         + mapToString(params) + ".\n";
            checkParamsMap(msg, expected, actual);
        }
    }

    private List<Map<String, Double>> paramsFromFile() throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(PARAMS_FILE), Charset.defaultCharset());
        List<Map<String, Double>> testParams = new ArrayList<>();
        int lineIdx = 2; // ignore comment lines
        for (int i = 0; i < NUM_TESTS; i++) {
            Map<String, Double> params = new HashMap<>();
            params.put("ullon", Double.parseDouble(lines.get(lineIdx)));
            params.put("ullat", Double.parseDouble(lines.get(lineIdx + 1)));
            params.put("lrlon", Double.parseDouble(lines.get(lineIdx + 2)));
            params.put("lrlat", Double.parseDouble(lines.get(lineIdx + 3)));
            params.put("w", Double.parseDouble(lines.get(lineIdx + 4)));
            params.put("h", Double.parseDouble(lines.get(lineIdx + 5)));
            testParams.add(params);
            lineIdx += 6;
        }
        return testParams;
    }

    private List<Map<String, Object>> resultsFromFile() throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(RESULTS_FILE), Charset.defaultCharset());
        List<Map<String, Object>> expected = new ArrayList<>();
        int lineIdx = 4; // ignore comment lines
        for (int i = 0; i < NUM_TESTS; i++) {
            Map<String, Object> results = new HashMap<>();
            results.put("raster_ul_lon", Double.parseDouble(lines.get(lineIdx)));
            results.put("raster_ul_lat", Double.parseDouble(lines.get(lineIdx + 1)));
            results.put("raster_lr_lon", Double.parseDouble(lines.get(lineIdx + 2)));
            results.put("raster_lr_lat", Double.parseDouble(lines.get(lineIdx + 3)));
            results.put("depth", Integer.parseInt(lines.get(lineIdx + 4)));
            results.put("query_success", Boolean.parseBoolean(lines.get(lineIdx + 5)));
            lineIdx += 6;
            String[] dimensions = lines.get(lineIdx).split(" ");
            int rows = Integer.parseInt(dimensions[0]);
            int cols = Integer.parseInt(dimensions[1]);
            lineIdx += 1;
            String[][] grid = new String[rows][cols];
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    grid[r][c] = lines.get(lineIdx);
                    lineIdx++;
                }
            }
            results.put("render_grid", grid);
            expected.add(results);
        }
        return expected;
    }

    private void checkParamsMap(String err, Map<String, Object> expected,
                                            Map<String, Object> actual) {
        for (String key : expected.keySet()) {
            assertTrue(err + "Your results map is missing "
                       + key, actual.containsKey(key));
            Object o1 = expected.get(key);
            Object o2 = actual.get(key);

            if (o1 instanceof Double) {
                String errMsg = genDiffErrMsg(err, expected, actual);
                assertTrue(errMsg, Math.abs((Double) o1 - (Double) o2) < DOUBLE_THRESHOLD);
            } else if (o1 instanceof String[][]) {
                String errMsg = genDiffErrMsg(err, expected, actual);
                assertArrayEquals(errMsg, (String[][]) o1, (String[][]) o2);
            } else {
                String errMsg = genDiffErrMsg(err, expected, actual);
                assertEquals(errMsg, o1, o2);
            }
        }
    }

    /** Generates an actual/expected message from a base message, an actual map,
     *  and an expected map.
     */
    private String genDiffErrMsg(String basemsg, Map<String, Object> expected,
                                 Map<String, Object> actual) {
        return basemsg + "Expected: " + mapToString(expected) + ", but got\n"
                       + "Actual  : " + mapToString(actual);
    }

    /** Converts a Rasterer input or output map to its string representation. */
    private String mapToString(Map<String, ?> m) {
        StringJoiner sj = new StringJoiner(", ", "{", "}");

        List<String> keys = new ArrayList<>();
        keys.addAll(m.keySet());
        Collections.sort(keys);

        for (String k : keys) {

            StringBuilder sb = new StringBuilder();
            sb.append(k);
            sb.append("=");
            Object v = m.get(k);

            if (v instanceof String[][]) {
                sb.append(Arrays.deepToString((String[][]) v));
            } else if (v instanceof Double) {
                sb.append(df2.format(v));
            } else {
                sb.append(v.toString());
            }
            String thisEntry = sb.toString();

            sj.add(thisEntry);
        }

        return sj.toString();
    }

    /*
    @Test
    public void TestHTMLTest(){
        RasterAPIHandler d = new RasterAPIHandler();
        //d7_x84_y28 <- first tile
        //d7_x86_y30 <- last tile
        int col = 86;
        int row = 30;
        int depth = 7;
        double ullon=-122.24163047377972;
        double lrlon=-122.24053369025242;
        double ullat=37.87655856892288;
        double lrlat=37.87548268822065;
        double w=892.0;
        double h=875.0;
        System.out.println("tile_ullon: " + d.ullon(col , depth));
        System.out.println("tile_ullat: " + d.ullat(row, depth));
        System.out.println("tile_lrlon: " + d.lrlon(col, depth));
        System.out.println("tile_lrlat: " + d.lrlat(row, depth));
        System.out.println("ulCol: " + d.getCol(ullon, depth));
        System.out.println("ulRow: " + d.getRow(ullat, depth));
        System.out.println("lrCol: " + d.getCol(lrlon, depth));
        System.out.println("lrRow: " + d.getRow(lrlat, depth));
    }

    @Test
    public void TwelveImagesTest(){
        RasterAPIHandler d = new RasterAPIHandler();
        //d2_x0_y1 <- first tile
        //d2_x3_y3 <- last tile
        int col = 3;
        int row = 3;
        int depth = 2;
        //raster_ul_lon=-122.2998046875, depth=2, raster_lr_lon=-122.2119140625, raster_lr_lat=37.82280243352756,
        // raster_ul_lat=37.87484726881516
        // tile_ullat =  37.87484726881516

        //raster_lr_lat=37.82280243352756
        // tile_lrlat = 37.82280243352756

        double lrlon=-122.2104604264636;
        double ullon=-122.30410170759153;
        double ullat=37.870213571328854;
        double lrlat=37.8318576119893;
        double w=1091.0;
        double h=566.0;

        System.out.println("tile_ullon: " + d.ullon(col , depth));
        System.out.println("tile_ullat: " + d.ullat(row, depth));
        System.out.println("tile_lrlon: " + d.lrlon(col, depth));
        System.out.println("tile_lrlat: " + d.lrlat(row, depth));
        System.out.println("ulCol: " + d.getCol(ullon, depth));
        System.out.println("lrCol: " + d.getCol(lrlon, depth));
        System.out.println("ulRow: " + d.getRow(ullat, depth));
        System.out.println("lrRow: " + d.getRow(lrlat, depth));

    }

    @Test
    public void TestAG() {
        RasterAPIHandler d = new RasterAPIHandler();

        double lrlon=854.4210921174251;
        double ullon=987.1836865949767;
        double w=454.61779318520195;
        double h=827.6077574243783;
        double ullat=481.2997641696711;
        double lrlat=85.15802634796614;

        Map<String, Object> results = new HashMap<>();

        //getting required data
        boolean outBounds = d.isOutOfBounds(ullon, ullat, lrlon, lrlat); // check if data is out of bounds
        System.out.println("Out of bounds: " + outBounds);
        int depth = outBounds ? 0 : d.getDepth(ullon, lrlon, w); // depth level
        int ulRow = outBounds ? 0 : d.getRow(ullat, depth); // y_cord first tile
        int ulCol = outBounds ? 0 : d.getCol(ullon, depth); // x_cord first tile
        int lrRow = outBounds ? 0 : d.getRow(lrlat, depth); // y_cord last tile
        int lrCol = outBounds ? 0 : d.getCol(lrlon, depth); // x_cord last tile
        double rasterUllon = d.ullon(ulCol, depth); // lon first tile (upper left corner)
        double rasterUllat = d.ullat(ulRow, depth); // lat first tile (upper left corner)
        double rasterLrlon = d.lrlon(lrCol, depth); // lon last tile (lower right corner)
        double rasterLrlat = d.lrlat(lrRow, depth); // lat last tile (lower right corner)
        String[][] images = d.getImages(ulRow, ulCol,  lrRow, lrCol, depth);

        // filling response Map
        results.put("render_grid", images);
        results.put("raster_ul_lon", rasterUllon);
        results.put("raster_ul_lat", rasterUllat);
        results.put("raster_lr_lon", rasterLrlon);
        results.put("raster_lr_lat", rasterLrlat);
        results.put("depth", depth);
        results.put("query_success", true);


        System.out.println("------------ OUTPUT ------------ ");
        for (Map.Entry<String, Object> a : results.entrySet()) {
            System.out.println(a.getKey() + ": " + a.getValue());
        }
        for (int row = 0; row < images.length; row++) {
            for (int col = 0; col < images[0].length; col++) {
                System.out.println(images[row][col]);
            }
        }
        System.out.println("------------ END OUTPUT ------------ ");

    }*/
    @Test
    public void retrieve(){
        AugmentedStreetMapGraph g = new AugmentedStreetMapGraph(Constants.OSM_DB_PATH);
        List<String> places = null;
        places = g.getLocationsByPrefix("p");

        int count = 0;
        if (places != null) {
            for (String str : places) {
                System.out.println(str);
                count++;
            }
        }

        System.out.println("Count : " + count);
        //System.out.println("Result: '" + result + "'");

        //System.out.println("cleaned: '" + g.cleanString("Peet's Coffee & Tea") + "'");
        System.out.println("original: '" + g.getLocationsByPrefix("Peet's Coffee & Tea") + "'");
        //System.out.println("cleaned: '" + g.cleanString("Peets Coffee & Tea") + "'");
        System.out.println("original: '" + g.getLocationsByPrefix("Peets Coffee & Tea") + "'");


        //AugmentedStreetMapGraph.getLocations()

    }
}
