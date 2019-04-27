package byow.Core;

import java.util.Random;

/**
 * Generates pseudo-random integer from the given range
 * The passed range in the random method is inclusive
 */
public class RandomGen {

    private Random RANDOM;

    /**
     * Initialize the random generator with the given seed
     * @param seed the seed from wich the number will be generated
     */
    public RandomGen(long seed) {
        this.RANDOM = new Random(seed);
    }

    /**
     * Generates a random number from the given range (inclusive)
     * @param min lower bound
     * @param max upper bound
     * @return the random number within range
     */
    public int random(int min, int max) {
        return RANDOM.nextInt((max - min) + 1) + min;
    }


}
