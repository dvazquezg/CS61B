package hw2;
import edu.princeton.cs.introcs.StdRandom;

public class PercolationStats {
    /**
     * Performs T independent experiments on an N-by-N grid to estimate
     * the percolation threshold
     * @param N side size of grid N * N
     * @param T Number of experiments
     * @param pf percolation factory
     */
    private final int N;
    private final int T;
    private double[] experimentThresholds;
    private int percThreshold;


    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("N and T must be positive integers.");
        }
        this.N = N;
        this.T = T;
        experimentThresholds = new double[T];
        monteCarloSimulation(pf); // Start Simulation
    }

    /**
     * Runs the MonteCarlo Simulation by performing T experiments and
     * storing each experiment threshold into an array
     * @param pf Percolation Factory object
     */
    private void monteCarloSimulation(PercolationFactory pf) {
        Percolation perc;
        // loop to run each experiment
        for (int i = 0; i < T; i++) {
            perc = pf.make(N); // reset all sites
            experimentThresholds[i] = expThreshold(perc);
        }
    }

    /**
     * Runs a single experiment. It keeps opening random locations until
     * the system percolates.
     * @param perc Percolation Factory object
     * @return The percolation threshold for the experiment
     */
    private double expThreshold(Percolation perc) {
        while (!perc.percolates()) {
            perc.open(StdRandom.uniform(N), StdRandom.uniform(N));
        }
        return (double) perc.numberOfOpenSites() / (N * N);
    }

    /**
     * Samples mean of percolation threshold
     * @return mean
     */
    public double mean() {
        double sum = 0.0;
        for (double xi: experimentThresholds) {
            sum += xi;
        }
        return sum / T;
    }

    /**
     * Samples standard deviation of percolation threshold
     * @return standard deviation
     */
    public double stddev() {
        if (T < 2) {
            return Double.NaN; // stddev is undefined whe there is less than 2 elements
        }
        double  mean = mean();
        double sumOfSquares = 0.0;
        for (double xi: experimentThresholds) {
            sumOfSquares += Math.pow(xi - mean, 2);
        }
        double stdDevSquared = sumOfSquares / (T - 1);
        return Math.sqrt(stdDevSquared);
    }

    /**
     * Lows endpoint of 95% confidence interval
     * @return confidence
     */
    public double confidenceLow() {
        double  mean = mean();
        double  stddev = stddev();
        return (mean - (1.96 * stddev) / Math.sqrt(T));
    }

    /**
     * High endpoint of 95% confidence interval
     * @return
     */
    public double confidenceHigh() {
        double  mean = mean();
        double  stddev = stddev();
        return (mean + (1.96 * stddev) / Math.sqrt(T));
    }
}
