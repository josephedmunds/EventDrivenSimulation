import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

/**
 * @Author Joseph Edmunds
 * Created: 10/6/2015.
 */
public class Teller {
    public static void main(String[] args){

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
        PriorityQueue<Integer> eventQueue = new PriorityQueue<>();
        EventItem arrival = new EventItem(clock, uniform(meanService,varService,rand), -1);

        while (clock < timeLimit){
            break;
        }

    }

    public static int uniform(int mean, int variant, Random rand) {
        int small = mean - variant;
        int range = 2 * variant + 1;
        return small + rand.nextInt(range);
    }
}



