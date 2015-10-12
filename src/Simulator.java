import java.util.PriorityQueue;
import java.util.Random;

/**
 * @author Joseph Edmunds
 *         Created: 10/11/2015.
 */
public class Simulator {

    private int numCashiers;
    private int meanIntArrival, varIntArrival;
    private int meanService, varService;
    private int clock = 0;
    private int maxQueueLength = 0;
    private int customerCount = 0;
    private int currWaitTime, maxWaitTime = 0, totalWaitTime = 0;
    private int peopleRemaining;
    private int totalServiceTime;
    private int interArrivalTime;
    private double avgInterArrivalTime;
    private double avgServiceTime;
    private double idleTime;
    private double avgWaitTime;
    private Random rand;

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
        this.rand = new Random();
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

        temp = evQueue.remove();
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

            if (tellers[holdQueue].teller.size() == 1) {
                EventItem departure = new EventItem(clock + temp.service_time, temp.service_time, holdQueue);
                evQueue.add(departure);
            }

            EventItem arrival = new EventItem(uniform(meanIntArrival, varIntArrival, rand), uniform(meanService, varService, rand), -1);
            interArrivalTime += temp.time_of_day - clock;
            totalServiceTime += temp.service_time;
            for (int k = 0; k < numCashiers; k++) {
                if (tellers[k].teller.size() > maxQueueLength)
                    maxQueueLength = tellers[k].teller.size();
            }
        } else if (temp.type_of_event >= 0) {
            customerCount++;
            currWaitTime = clock - (temp.time_of_day - temp.service_time);
            if (currWaitTime > maxWaitTime) {
                maxWaitTime = currWaitTime;
            }
            totalWaitTime += currWaitTime;
            tellers[temp.type_of_event].teller.poll();
            if (tellers[temp.type_of_event].teller.isEmpty()) {
                EventItem departure = new EventItem(clock + tellers[temp.type_of_event].teller.peek().service_time, temp.service_time, temp.type_of_event);
                evQueue.add(departure);
            }
        } else {
            //will have output stats
        }
    } //end process

    /**
     * Function that returns a uniformly random integer in the range of mean +/- variant
     *
     * @param mean    The average
     * @param variant The amount of variance possible from the average
     * @param rand    The random generator
     * @return The uniformly random integer
     */
    public int uniform(int mean, int variant, Random rand) {
        int small = mean - variant;
        int range = 2 * variant + 1;
        return small + rand.nextInt(range);
    }
}