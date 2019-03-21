package bearmaps;

import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug. Demonstrates how you can use either
 * System.currentTimeMillis or the Princeton Stopwatch
 * class to time code.
 */
public class TimingTestHeap {
    public static void main(String[] args) {
        /*
        long start = System.currentTimeMillis();
        int sum = 0;
        for (int i = 0; i < 100000; i += 1) {
            for (int j = 0; j < 10000; j += 1) {
                sum = sum + i + j;
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Total time elapsed: " + (end - start)/1000.0 +  " seconds.");
        */
        Stopwatch sw = new Stopwatch();

        ArrayHeapMinPQ<Integer> heapTest = new ArrayHeapMinPQ<>();
        NaiveMinPQ<Integer> naiveTest = new NaiveMinPQ<>();
        for (int i = 0; i < 10000000; i += 1) {
            heapTest.add(i, 1 + (Math.random() * (100000 - 1)));
            //naiveTest.add(i, 1);
        }
        System.out.println("Total time elapsed: " + sw.elapsedTime()
                +  " seconds. for 20,000,000 insertions");

    }
}
