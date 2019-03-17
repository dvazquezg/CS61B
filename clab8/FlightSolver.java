import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Solver for the Flight problem (#9) from CS 61B Spring 2018 Midterm 2.
 * Assumes valid input, i.e. all Flight start times are >= end times.
 * If a flight starts at the same time as a flight's end time, they are
 * considered to be in the air at the same time.
 */
public class FlightSolver {
    PriorityQueue<Flight> endTimes;
    PriorityQueue<Flight> startTimes;
    int maxPassengersOnAir;

    public FlightSolver(ArrayList<Flight> flights) {
        // Constructors using lambda functions
        Comparator<Flight> smallerEndtime = (Flight f1, Flight f2) -> {
            int diff = f1.endTime() - f2.endTime();
            return diff;
        };
        Comparator<Flight> smallerStartime = (Flight f1, Flight f2) -> {
            int diff = f1.startTime() - f2.startTime();
            return diff;
        };
        // initialize priority queues
        endTimes = new PriorityQueue<>(smallerEndtime);
        startTimes = new PriorityQueue<>(smallerStartime);

        for (Flight f: flights) {
            startTimes.add(f);
            endTimes.add(f);
        }
        maxPassengersOnAir = 0;

    }

    public int solve() {
        //every time that a plane takes off add number of passengers
        //every time that a plane lands subtract number of passengers
        int passengersOnAir = 0;
        while (startTimes.size() != 0 && endTimes.size() != 0) {
            if (startTimes.peek().startTime() <= endTimes.peek().endTime()) {
                passengersOnAir += startTimes.remove().passengers();
            } else {
                passengersOnAir -= endTimes.remove().passengers();
            }
            if (passengersOnAir > maxPassengersOnAir) {
                maxPassengersOnAir = passengersOnAir;
            }
        }
        return maxPassengersOnAir;
    }

}
