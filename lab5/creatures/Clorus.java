package creatures;

import huglife.*;

import java.awt.Color;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

/**
 * An implementation of a motile non-pacifist and photosynthesizer-creature eater.
 *
 * @author Daniel Vazquez
 */
public class Clorus extends Creature {

    /**
     * red color.
     */
    private int r;
    /**
     * green color.
     */
    private int g;
    /**
     * blue color.
     */
    private int b;

    /**
     * creates clorus with energy equal to E.
     */
    public Clorus(double e) {
        super("clorus");
        r = 0;
        g = 0;
        b = 0;
        energy = e;
    }

    /**
     * creates a clorus with energy equal to 1.
     */
    public Clorus() {
        this(1);
    }

    /**
     * Cloruses are non-pacifist, non-color changing creatures
     */
    public Color color() {
        r = 34;
        g = 0;
        b = 231;
        return color(r, g, b);
    }

    /**
     * Cloruses are savage Plip eaters.
     * They take all the plip's energy once it eat it.
     */
    public void attack(Creature c) {
        energy += c.energy(); // take energy
    }

    /**
     * cloruses should lose 0.03 units of energy when moving.
     */
    public void move() {
        double newEnergy = energy - 0.03;
        energy = (newEnergy > 0) ? newEnergy : 0;
    }


    /**
     * cloruses lose 0.01 energy when staying due to not eating
     */
    public void stay() {
        double newEnergy = energy - 0.01;
        energy = (newEnergy > 0) ? newEnergy : 0;
    }

    /**
     * cloruses and their offspring each get 50% of the energy, with none
     * lost to the process. Now that's efficiency! Returns a baby
     * clorus.
     */
    public Clorus replicate() {
        energy = energy * 0.5;
        return new Clorus(energy);
    }

    /**
     * cloruses take exactly the following actions based on NEIGHBORS:
     * 1. If no empty adjacent spaces, STAY.
     * 2. Otherwise, if energy >= 1, REPLICATE towards an empty direction
     * chosen at random.
     * 3. Otherwise, if any Cloruses, MOVE with 50% probability,
     * towards an empty direction chosen at random.
     * 4. Otherwise, if nothing else, STAY
     * <p>
     * Returns an object of type Action. See Action.java for the
     * scoop on how Actions work. See SampleCreature.chooseAction()
     * for an example to follow.
     */
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        // Rule 1
        Deque<Direction> emptyNeighbors = new ArrayDeque<>();
        Deque<Direction> plipNeighbors = new ArrayDeque<>();

        // loop over the 4 directions
        for (Direction key : neighbors.keySet()) {
            Occupant being = neighbors.get(key);
            if (being.name().equals("empty")) {
                emptyNeighbors.add(key);
            } else if (being.name().equals("plip")) {
                plipNeighbors.add(key);
            }
        }

        if (emptyNeighbors.size() == 0) { // if 4 directions are occupied
            return new Action(Action.ActionType.STAY);
        }

        // Rule 2

        if (plipNeighbors.size() > 0) { // if there is a plip nearby
            Direction targetPlip = HugLifeUtils.randomEntry(plipNeighbors);
            return new Action(Action.ActionType.ATTACK, targetPlip);
        }

        // Rule 3
        if (energy >= 1.0 && emptyNeighbors.size() > 0) {
            Direction dirReplicate = HugLifeUtils.randomEntry(emptyNeighbors);
            return new Action(Action.ActionType.REPLICATE, dirReplicate);
        }


        // Rule 4
        Direction dirMove = HugLifeUtils.randomEntry(emptyNeighbors);
        return new Action(Action.ActionType.MOVE, dirMove);
    }
}
