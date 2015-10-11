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

        Simulator simulate = new Simulator(numCashiers, meanIntArrival, varIntArrival, meanService, varService);

        while (simulate.getClock() < timeLimit)
            simulate.process();
    }
}