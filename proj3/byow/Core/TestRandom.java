package byow.Core;

import java.util.ArrayList;
import static byow.Core.Constants.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestRandom {

    private static String[] myKSet = {"^[Nn]\\d+[Ss]",
                                      "^[Nn]\\d+[sS][" + KEYSET1 + "]+",
                                      "^[Nn]\\d+[sS][" + KEYSET1  + "]+[:][Qq].*",
                                      "^[Ll][" + KEYSET1  + "]+",
                                      "^[Ll][" + KEYSET1  + "]+[:][Qq].*"};

    @Test
    public void matchingTest() {
        RandomGen crazyNUmber = new RandomGen(3415);
        System.out.println(crazyNUmber.random(1, 10));
        System.out.println(crazyNUmber.random(11, 13));
        System.out.println(crazyNUmber.random(20, 21));
        System.out.println("Enum: " + Constants.Direction.EAST);
        System.out.println("Casting directions: " + Constants.Direction.values());
        String testStr1 = "N3344sAWDDssawdsAWDS:q";
        String testStr2 = "N3344sAWDDssawdsAWDS:q@#$%$TRGASDFsdfgsfdfwgead32453wq34ga';";

        System.out.println("Matches: " + testStr1.matches(myKSet[2]));
        System.out.println("Matches: " + testStr2.matches(myKSet[2]));
    }

    @Test
    public void validArgumentTest() {
        ArrayList<String> validArgs = new ArrayList<>();
        validArgs.add("n1234s");            // 0
        validArgs.add("N6775S");            // 1
        validArgs.add("N0976s");            // 2
        validArgs.add("n65445sdWWSAS");     // 3
        validArgs.add("LWASDSWD");          // 4
        validArgs.add("lwasdswwss");        // 5
        validArgs.add("N23342SSdwaa:Q");    // 6
        validArgs.add("lwsWdsa:q");         // 7
        validArgs.add("l:q");               // 8
        validArgs.add("N242SSdaw:Q:q:z:q"); // 9
        validArgs.add("L:Q");               // 10
        validArgs.add("N0976s:Q");          // 11
        validArgs.add("L");                 // 12
        validArgs.add("lwasdswwss:q");      // 13
        validArgs.add("n123ssswws");        // 14

        for (String str : validArgs) {
            ArgumentAnalyzer analyzer = new ArgumentAnalyzer(str);
            assertTrue(analyzer.success());
            System.out.print("Raw arg: " + str);
            System.out.print(", valid?: " + analyzer.success());
            System.out.print(", Action: " + analyzer.getAction());
            System.out.print(", Seed: " + analyzer.getSeed());
            System.out.print(", Has steps?: " + analyzer.hasSteps());
            System.out.print(", Steps: " + analyzer.getSteps().toString());
            System.out.println(", Save?: " + analyzer.saveState());
        }
    }

    @Test
    public void invalidArgumentTest() {
        ArrayList<String> validArgs = new ArrayList<>();
        validArgs.add("X1234s");            // 0
        validArgs.add("N12m34S");           // 1
        validArgs.add("N1234srtgh");        // 2
        validArgs.add("Nas1234sdWWSAS");    // 3
        validArgs.add("LYUASDSWD");         // 4
        validArgs.add("l33wasdswwss");      // 5
        validArgs.add("N23342SSdwaa:");     // 6
        validArgs.add("lwsWdsa:w");         // 7
        validArgs.add("lq");                // 9
        validArgs.add("L::");               // 9
        validArgs.add("N");                 // 10
        validArgs.add("");                  // 11
        validArgs.add("daniel");            // 12
        validArgs.add(" ");                 // 13
        validArgs.add("N233 42SSdwaa:Q");   // 14

        for (String str : validArgs) {
            ArgumentAnalyzer analyzer = new ArgumentAnalyzer(str);
            assertFalse(analyzer.success());
            System.out.println("Raw arg: " + str + "| valid?: " + analyzer.success());
        }
    }

    @Test
    public void overLapOutOfBoundsTest() {

        // Create the rooms array with a random size.
        ArrayList<Room> rooms = new ArrayList<>();

        Room testRoom1 = new Room(10, 10, 5, 5);
        Room testRoom2 = new Room(6, 14, 5, 5);
        Room testRoom3 = new Room(14, 6, 5, 5);
        Room testRoom4 = new Room(6, 6, 5, 5);
        Room testRoom5 = new Room(14, 14, 5, 5);
        Room testRoom6 = new Room(20, 20, 5, 5);
        Room testOutofBounds = new Room(0, 0, 80, 30);
        System.out.println(testRoom1);
        System.out.println(testRoom2);
        System.out.println(testRoom3);
        System.out.println(testRoom4);
        System.out.println(testRoom5);
        System.out.println(testRoom6);
        //rooms.add(testOutofBounds);
        rooms.add(testRoom1);
        rooms.add(testRoom2);
        rooms.add(testRoom3);
        rooms.add(testRoom4);
        rooms.add(testRoom5);
        rooms.add(testRoom6);

        boolean overlaps = GridCreator.overlaps(testRoom1, testRoom5);
        boolean outOfBounds = GridCreator.isOutOfBounds(testOutofBounds);
        System.out.println("Overlaps? " + overlaps);
        System.out.println("Out of bounds? " + outOfBounds);

        assertTrue(overlaps);
        assertTrue(outOfBounds);

    }

}
