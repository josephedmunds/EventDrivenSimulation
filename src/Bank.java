import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

/**
 * @author Joseph Edmunds
 *         Created: 10/6/2015.
 */
public class Bank {
    public static void main(String[] args) {
        System.out.printf("Enter the following values with a space between each:\n ");
        System.out.printf("\t*Number of Cashiers\n\t*Mean inter-arrival time\n\t*Variance inter-arrival time\n\t");
        System.out.printf("*Mean customer service time\n\t*Variance customer service time\n\t*Time Limit\n");

        Scanner scanMan = new Scanner(System.in);
        int numCashiers = scanMan.nextInt();
        int meanIntArrival = scanMan.nextInt();
        int varIntArrival = scanMan.nextInt();
        int meanService = scanMan.nextInt();
        int varService = scanMan.nextInt();
        int timeLimit = scanMan.nextInt();

        Random rand = new Random();
        int clock = 0;

        PriorityQueue<EventItem> evQueue = new PriorityQueue<>();
        Teller tellers[] = new Teller[numCashiers];
        EventItem temp;
        EventItem event = new EventItem(uniform(meanIntArrival, varIntArrival, rand), uniform(meanService, varService, rand), -1);
        evQueue.add(event);
        int interArrivalTIme = 0;
        int totalServiceTime = 0;

        while (clock < timeLimit) {
            temp = evQueue.poll();
            for (int i = 0; i < numCashiers; i++) {
                if (tellers[i].teller.peek() == null)
                    tellers[i].totalIdleTime += temp.time_of_day - clock;
            }
            clock = temp.time_of_day;

            if (event.type_of_event == -1) {
                int holdQueue = 0;
                for (int j = 0; j < numCashiers-1; j++) {
                    if (tellers[j].teller.size() < tellers[j+1].teller.size()) {
                        holdQueue = j;
                    }
                }

                tellers[holdQueue].teller.add(temp);
            } else {

            }
        }

    }

    /**
     * Function that returns a uniformly random integer in the range of mean +/- variant
     *
     * @param mean
     * @param variant
     * @param rand
     * @return
     */
    public static int uniform(int mean, int variant, Random rand) {
        int small = mean - variant;
        int range = 2 * variant + 1;
        return small + rand.nextInt(range);
    }
}