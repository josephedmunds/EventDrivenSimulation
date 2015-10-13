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
    private int lastArrival;
    private int maxQueueLength = 0;
    private int customerCount = 0;
    private int currWaitTime, maxWaitTime = 0, totalWaitTime = 0;
    private int peopleRemaining = 0;
    private int totalServiceTime;
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
        avgServiceTime = 0;
        IdleTime = 0;
        avgWaitTime = 0;
        lastArrival = 0;
        avgInterArrivalTime = 0;

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
            int hold = shortLine();
            tellers[shortLine()].teller.add(new EventItem(temp.time_of_day, temp.service_time, shortLine()));

            if (tellers[hold].teller.size() == 1) {
                EventItem departure = new EventItem(clock + temp.service_time, temp.service_time, hold);
                evQueue.add(departure);
            }
            EventItem arrival = new EventItem(clock + uniform(meanIntArrival, varIntArrival, rand), uniform(meanService, varService, rand), -1);
            evQueue.add(arrival);
            avgInterArrivalTime += temp.time_of_day - lastArrival;
            lastArrival = temp.time_of_day;
            for (int k = 0; k < numCashiers; k++) {
                if (tellers[k].teller.size() > maxQueueLength)
                    maxQueueLength = tellers[k].teller.size();
            }
        } else if (temp.type_of_event >= 0) {
            customerCount++;
            EventItem leaving = tellers[temp.type_of_event].teller.poll();

            totalServiceTime += leaving.service_time;
            currWaitTime =  clock - (temp.time_of_day - temp.service_time);
            if (currWaitTime > maxWaitTime) {
                maxWaitTime = currWaitTime;
            }
            totalWaitTime += currWaitTime;

            if (!tellers[temp.type_of_event].teller.isEmpty()) {
                EventItem departure = new EventItem(clock + temp.service_time, temp.service_time, temp.type_of_event);
                evQueue.add(departure);
            }
        } else {
            System.out.println(clock);
            System.out.printf("Number of items in event queue: %d\n", evQueue.size());
            for (int i = 0; i < numCashiers; i++) {
                System.out.printf("Number of customers in queue %d: %d\n", i, tellers[i].teller.size());
            }
            if (clock == 2000) {
                for (int j = 0; j < numCashiers; j++) {
                    peopleRemaining += tellers[j].teller.size();
                }
                System.out.printf("(1) Number of Customers Served: %d\n", customerCount);
                avgInterArrivalTime = 2000.0 / (customerCount + peopleRemaining);
                System.out.printf("(2) Average Inter-Arrival Time: %.2f\n", avgInterArrivalTime);
                avgServiceTime = (double) totalServiceTime / customerCount;
                System.out.printf("(3) Average Service Time: %.2f\n", avgServiceTime);
                avgWaitTime = (double) totalWaitTime / customerCount;
                System.out.printf("(4) Average Wait Time: %.2f\n", avgWaitTime);
                for (int i = 0; i < numCashiers; i++) {
                    System.out.printf("(5) Percent Idle Time of Cashier %d: %.2f%c\n", i, (double) tellers[i].totalIdleTime / 2000 * 100, '%');
                }
                System.out.printf("(6) Maximum Customer Wait: %d\n", maxWaitTime);
                System.out.printf("(7) Maximum Queue Length: %d\n", maxQueueLength);


                System.out.printf("(8) Total people unserved: " + peopleRemaining);
            }
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
        for (int j = 1; j < numCashiers; j++) {
            if (tellers[holdQueue].teller.size() > tellers[j].teller.size()) {
                holdQueue = j;
            }
        }
        return holdQueue;
    }
}