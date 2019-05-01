package byow.Core;

import static byow.Core.Constants.Direction;

public class Step {
    char charStep;
    Direction dirStep;

    public Step(char charStep, Direction dirStep) {
        this.charStep = charStep;
        this.dirStep = dirStep;
    }

    public char getCharStep() {
        return charStep;
    }

    public Direction getDirStep() {
        return dirStep;
    }

    public void setCharStep(char charStep) {
        this.charStep = charStep;
    }

    public void setDirStep(Direction dirStep) {
        this.dirStep = dirStep;
    }
}
