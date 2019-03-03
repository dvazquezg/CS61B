package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Simulates percolation
 * @author Daniel Vazquez Guevara
 */
public class Percolation {

    private WeightedQuickUnionUF uSetFull;
    private WeightedQuickUnionUF uSetPerc;
    private boolean[][] grid;
    private final int topVirtualSite;
    private final int bottomVirtualSite;
    private final int N;
    private int openSites;

    /**
     * initializes a N-by-N grid, with all sites initially blocked
     * @param N side of grid
     */
    public Percolation(int N) {
        if (N < 0) {
            throw new IllegalArgumentException("Grid size must be greater than zero");
        }
        this.N = N;
        openSites = 0;
        // generate number of virtual spots
        topVirtualSite = N * N; // note that grid elements go from 0 to N * N - 1
        bottomVirtualSite = N * N + 1;
        uSetFull = new WeightedQuickUnionUF(N * N + 1); // allocate one virtual cell
        uSetPerc = new WeightedQuickUnionUF(N * N + 2);// allocate two virtual cells
        grid = new boolean[N][N]; // state grid (all false)

    }
    public int conv2DTo1D(int row, int col) {
        validateCell(row, col);
        return (row * N) + col;
    }

    /**
     * Opens a site (row, col) if it is not open already and
     * connect adjacent open sites
     * @param row coordinate
     * @param col coordinate
     */
    public void open(int row, int col) {
        validateCell(row, col);
        if (!grid[row][col]) {
            grid[row][col] = true; // open location
            connectNeighbors(row, col); // connect with neighbors
            openSites++;
        }
    }

    /**
     * Connects given site's coordinate to open orthogonally
     * adjacent neighbors. Also make sure that top and bottom
     * locations get connected to virtual places
     * @param row
     * @param col
     */
    private void connectNeighbors(int row, int col) {
        // Neighbors: Top, Bottom, Left, Right
        int[] rows = {0, 0, -1, 1};
        int[] cols = {-1, 1, 0, 0};
        connectToVirtual(row, col); // connect to virtual location if applicable
        for (int i = 0; i < 4; i++) {
            int rowN = row + rows[i]; // neighbor row
            int colN = col + cols[i]; // neighbor col
            if(validNeighbor(rowN, colN)) {
                uSetPerc.union(conv2DTo1D(row, col), conv2DTo1D(rowN, colN));
                uSetFull.union(conv2DTo1D(row, col), conv2DTo1D(rowN, colN));
            }
        }

    }

    /**
     * Checks if given coordinate is within the top or bottom row
     * if so, it connect to corresponding virtual location
     * @param row
     * @param col
     */
    private void connectToVirtual(int row, int col) {
        if (row == 0){
            // connect top row location to top virtual location
            uSetPerc.union(conv2DTo1D(row, col), topVirtualSite);
            uSetFull.union(conv2DTo1D(row, col), topVirtualSite);
        }
        if (row == N - 1) {
            // connect bottom row location to bottom virtual location
            uSetPerc.union(conv2DTo1D(row, col), bottomVirtualSite);
        }
    }

    /**
     * Checks if given coordinate is within grid, if so it checks if
     * the cell is open (meaning that it can be connected)
     * @param row
     * @param col
     * @return true if coordinate is a valid location, else false
     */
    private boolean validNeighbor(int row, int col) {
        if (row < 0 || row > (N - 1) || col < 0 || col > (N - 1)) {
            return false;
        }
        return isOpen(row, col);
    }

    /**
     * Checks if given site (row, col) is open
     * @param row coordinate
     * @param col coordinate
     * @return true if open, false otherwise
     */
    public boolean isOpen(int row, int col) {
        validateCell(row, col);
        return grid[row][col];
    }

    /**
     * Checks if the given site (row, col) is full
     * @param row coordinate
     * @param col coordinate
     * @return true if full, false otherwise
     */
    public boolean isFull(int row, int col) {
        validateCell(row, col);
        return uSetFull.connected(conv2DTo1D(row, col), topVirtualSite);
    }

    /**
     * Returns the number of open sites
     * @return number of open sites
     */
    public int numberOfOpenSites() {
        return openSites;
    }

    /**
     * Checks if the system percolate
     * @return true if the system percolates, false otherwise
     */
    public boolean percolates() {
        return uSetPerc.connected(topVirtualSite, bottomVirtualSite);
    }

    /**
     * Checks of coordinates are within the grid. if not it
     * throws an exception
     * @param row
     * @param col
     */
    public void validateCell(int row, int col) {
        if (row < 0 || row > (N - 1) || col < 0 || col > (N - 1)) {
            throw new IndexOutOfBoundsException("Invalid position");
        }
    }

    /**
     * use for unit testing (not required, but keep this here for the autograder)
     * @param args
     */
    public static void main(String[] args) {
//        Percolation tgrid = new Percolation(5);
//        System.out.println(tgrid.conv2DTo1D(2, 4));
//        System.out.println(tgrid.conv2DTo1D(3, 4));
//        System.out.println(tgrid.conv2DTo1D(0, 0));
//        System.out.println(tgrid.conv2DTo1D(4, 4));
    }
}
