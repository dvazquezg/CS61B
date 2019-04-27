package byow.Core;

import java.util.ArrayList;
import java.util.Random;
import static byow.Core.Constants.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestRandom {

    public static String[] myKSet = {"^[Nn]\\d+[Ss]",
                                    "^[Nn]\\d+[sS][" + keySet1 + "]+",
                                    "^[Nn]\\d+[sS][" + keySet1  + "]+[:][Qq].*",
                                    "^[Ll][" + keySet1  + "]+",
                                    "^[Ll][" + keySet1  + "]+[:][Qq].*"
                                };

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

}
