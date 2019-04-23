package byow.Core;
import static byow.Core.Constants.*;

public class Utils {
    public static boolean validDirection(String test) {
        for (Direction c : Direction.values()) {
            if (c.name().equalsIgnoreCase(test)) {
                return true;
            }
        }
        return false;
    }
    /*
    public static boolean validCommand(char test) {
        for (Command c : Command.values()) {
            if (c.name().equalsIgnoreCase(String.valueOf(test))) {
                return true;
            }
        }
        return false;
    }

    public static boolean validKeySet1(char test) {
        for (Direction c : Direction.values()) {
            if (c.name().equalsIgnoreCase(String.valueOf(test))) {
                return true;
            }
        }
        return false;
    }

    public static Command getCommand(char cmd) {
        if (validCommand(cmd)){
            return Command.valueOf(String.valueOf(cmd));
        }
        return null;
    }
    */
}
