package byow.Core;

import java.util.ArrayList;
import static byow.Core.Constants.*;

/**
 * Takes a string and analyses if it follows pattern for tile engine
 * @author Daniel Vazquez
 */
public class ArgumentAnalyzer {
    private long seed;
    private Action action;
    private ArrayList<Direction> steps;
    private String strSteps;
    private boolean saveState = false;
    private boolean success = false;


    public ArgumentAnalyzer(String argument) {
        if (argument == null || argument.equals("")) {
            success = false;
            return;
        }
        steps = new ArrayList<>();
        process(argument.toUpperCase()); // IMPORTANT: uppercase the argument!
    }

    /**
     * Validates and process given argument according to the following
     * specification: N-SEED-STEPS(:Q) or L-STEPS(:Q)
     * COMMAND: 'N' = New or 'L' = Load, etc (check list of commands)
     * SEED: sequence of digits using 0 through 9 (if entered N)
     * STEPS: The sequence of instructions (after digits or special command)
     * SAVE: find a q or Q after colon ':'
     * Example of valid arguments:
     * N999SDDDWWWDDD, N25SDDWD:Q, LWWWDDD, LWWW:Q, L:Q, L:q3dg%#^45'
     * @param argument assumes non-null, non-empty, UpperCase string
     */
    private void process(String argument) {
        StringInputDevice tokenizer = new StringInputDevice(argument);
        char cmd = tokenizer.getNextKey();

        switch (cmd) {
            case 'N':
                action = Action.NEW;
                success = newGameArgs(tokenizer);
                break;
            case 'L':
                action = Action.LOAD;
                success = loadGameArgs(tokenizer);
                break;
            default:
                success = false;
                break;
        }
    }


    /**
     * Gets a sequence of digits until next non-digit character is found
     * @param tokenizer tokenizer (assumes string starts with number)
     * @return true if extraction was successful, false otherwise
     */
    private boolean getSeed(StringInputDevice tokenizer) {
        String seedStr = "";
        while (tokenizer.possibleNextInput()) {
            char nextChar = tokenizer.getNextKey();
            if (Character.isDigit(nextChar)) {
                seedStr += nextChar;
            } else if (nextChar == 'S') { // check if end of seed
                break;
            } else {
                return false;
            }
        }
        // check that seedStr is not empty
        if (!seedStr.equals("")) {
            this.seed = Long.parseLong(seedStr);
            return true;
        }
        return false;
    }

    private boolean getSteps(StringInputDevice tokenizer) {
        strSteps = "";
        while (tokenizer.possibleNextInput()) {
            char nextChar = tokenizer.getNextKey();
            switch (nextChar) {
                case 'W':
                    strSteps += 'W';
                    steps.add(Direction.NORTH);
                    break;
                case 'A':
                    strSteps += 'A';
                    steps.add(Direction.WEST);
                    break;
                case 'S':
                    strSteps += 'S';
                    steps.add(Direction.SOUTH);
                    break;
                case 'D':
                    strSteps += 'D';
                    steps.add(Direction.EAST);
                    break;
                case ':':
                    return savingCommand(tokenizer);
                default:
                    steps = null;
                    return false;
            }
        }
        return true;
    }

    private boolean savingCommand(StringInputDevice tokenizer) {
        if (tokenizer.possibleNextInput()) {
            char nextChar = tokenizer.getNextKey();
            if (nextChar == 'Q') {
                saveState = true;
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public boolean success() {
        return success;
    }

    public long getSeed() {
        return seed;
    }

    public boolean saveState() {
        return saveState;
    }

    public boolean hasSteps() {
        return steps.size() > 0;
    }

    public Action getAction() {
        return action;
    }

    public ArrayList<Direction> getSteps() {
        return steps;
    }

    public String getStrSteps() {
        return strSteps;
    }

    private boolean newGameArgs(StringInputDevice tokenizer) {
        return getSeed(tokenizer) && getSteps(tokenizer);
    }

    private boolean loadGameArgs(StringInputDevice tokenizer) {
        return getSteps(tokenizer);
    }
}
