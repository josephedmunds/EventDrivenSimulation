import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;

/**
 * @author Joseph Edmunds
 *         Created: 10/11/2015.
 */
public class Simulator {

    private int numCashiers;
    private int meanIntArrival;
    private int varIntArrival;
    private int meanService;
    private int varService;
    private int interArrivalTime;
    private int totalServiceTime;
    private int clock = 0;

    private PriorityQueue<EventItem> evQueue;
    private Teller tellers[];

    public Simulator(int numCashiers, int meanIntArrival, int varIntArrival, int meanService, int varService) {
        this.numCashiers = numCashiers;
        this.meanIntArrival = meanIntArrival;
        this.varIntArrival = varIntArrival;
        this.meanService = meanService;
        this.varService = varService;

        evQueue = new PriorityQueue<>();
        tellers = new Teller[numCashiers];
        Random rand = new Random();
        EventItem event = new EventItem(uniform(meanIntArrival, varIntArrival, rand), uniform(meanService, varService, rand), -1);
        evQueue.add(event);
    }


    public int getClock() {
        return clock;
    }

    public void setClock(int clock) {
        this.clock = clock;
    }

    public void process() {
        EventItem temp;

        temp = evQueue.poll();
        for (int i = 0; i < numCashiers; i++) {
            if (tellers[i].teller.peek() == null)
                tellers[i].totalIdleTime += temp.time_of_day - clock;
        }
        clock = temp.time_of_day;

        if (temp.type_of_event == -1) {
            int holdQueue = 0;
            for (int j = 0; j < numCashiers - 1; j++) {
                if (tellers[j].teller.size() < tellers[j + 1].teller.size()) {
                    holdQueue = j;
                }
            }
            tellers[holdQueue].teller.add(temp);
        } else {

        }
    }


    /**
     * Function that returns a uniformly random integer in the range of mean +/- variant
     *
     * @param mean The average
     * @param variant The amount of variance possible from the average
     * @param rand The random generator
     * @return The uniformly random integer
     */
    public int uniform(int mean, int variant, Random rand) {
        int small = mean - variant;
        int range = 2 * variant + 1;
        return small + rand.nextInt(range);
    }
}
