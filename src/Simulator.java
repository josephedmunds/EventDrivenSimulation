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
    private int peopleRemaining = 0;
    private int totalServiceTime;
    private int interArrivalTime;
    private double avgInterArrivalTime;
    private double avgServiceTime;
    private double IdleTime;
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
        peopleRemaining = 0;
        totalServiceTime = 0;
        avgInterArrivalTime = 0;
        avgServiceTime = 0;
        IdleTime = 0;
        avgWaitTime = 0;

        evQueue = new PriorityQueue<>();
        tellers = new Teller[numCashiers];
        for (int i = 0; i < numCashiers; i++) {
            tellers[i] = new Teller();
        }
        this.rand = new Random();
        EventItem event = new EventItem(uniform(meanIntArrival, varIntArrival, rand), uniform(meanService, varService, rand), -1);
        EventItem stats = new EventItem(500, 0, -2);
        evQueue.add(event);
        evQueue.add(stats);
        interArrivalTime = event.time_of_day;
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
            if (tellers[i].teller.size() == 0)
                tellers[i].totalIdleTime += temp.time_of_day - clock;
        }
        clock = temp.time_of_day;

        if (temp.type_of_event == -1) {
            //System.out.println("Heyo");
            tellers[shortLine()].teller.add(temp);

            if (tellers[shortLine()].teller.size() == 1) {
                EventItem departure = new EventItem(clock + temp.service_time, temp.service_time, shortLine());
                evQueue.add(departure);
            }

            EventItem arrival = new EventItem(clock + uniform(meanIntArrival, varIntArrival, rand), uniform(meanService, varService, rand), -1);
            evQueue.add(arrival);
            interArrivalTime += temp.time_of_day - clock;
            totalServiceTime += temp.service_time;
            for (int k = 0; k < numCashiers; k++) {
                if (tellers[k].teller.size() > maxQueueLength)
                    maxQueueLength = tellers[k].teller.size();
            }
        } else if (temp.type_of_event >= 0) {
            //System.out.println("ayy lmao");
            customerCount++;
            currWaitTime = clock - (temp.time_of_day - temp.service_time);
            if (currWaitTime > maxWaitTime) {
                maxWaitTime = currWaitTime;
            }
            totalWaitTime += currWaitTime;
            tellers[temp.type_of_event].teller.remove();
            if (!tellers[temp.type_of_event].teller.isEmpty()) {
                EventItem departure = new EventItem(clock + temp.service_time, temp.service_time, temp.type_of_event);
                evQueue.add(departure);
            }
        } else {
            System.out.printf("Number of Customers Served: %d\n", customerCount);
            System.out.printf("Average Inter-Arrival Time: %.2f\n", avgInterArrivalTime);
            System.out.printf("Average Service Time: %.2f\n", avgServiceTime);
            System.out.printf("Average Wait Time: %.2f\n", avgWaitTime);
            //for (int i = 0; i < numCashiers; i++) {
            //    System.out.printf("Percent Idle Time of Cashier " + i + ": %f", );
            //}
            for (int i = 0; i < numCashiers; i++) {
                System.out.printf("Idle Time of Cashier " + i + ": %d\n", tellers[i].totalIdleTime);
            }
            System.out.printf("Maximum Customer Wait: %d\n", maxWaitTime);
            System.out.printf("Maximum Queue Length: %d\n", maxQueueLength);
            if (clock == 2000)
                for (int j = 0; j < numCashiers; j++) {
                    peopleRemaining += tellers[j].teller.size();
                }
            System.out.printf("Total people unserved: " + peopleRemaining);
            System.out.println("\n\n");
            EventItem stats = new EventItem(clock + 500, 0, -2);
            evQueue.add(stats);
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

    public int shortLine() {
        int holdQueue = 0;
        for (int j = 0; j < numCashiers - 1; j++) {
            if (tellers[j].teller.size() < tellers[j + 1].teller.size()) {
                holdQueue = j;
            }
        }
        return holdQueue;
    }
}
