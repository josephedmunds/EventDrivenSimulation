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
    /*    Scanner scanMan = new Scanner(System.in);
        System.out.printf("Enter the number of cashiers: ");
        int numCashiers = scanMan.nextInt();
        System.out.printf("Enter the mean inter-arrival time: ");
        int meanIntArrival = scanMan.nextInt();
        System.out.printf("Enter the inter-arrival time variance: ");
        int varIntArrival = scanMan.nextInt();
        System.out.printf("Enter the mean service time time: ");
        int meanService = scanMan.nextInt();
        System.out.printf("Enter the service time variance: ");
        int varService = scanMan.nextInt();
        System.out.printf("Enter the time limit: ");
        int timeLimit = scanMan.nextInt();

        Simulator simulate = new Simulator(numCashiers, meanIntArrival, varIntArrival, meanService, varService);
        */

        Simulator simulate = new Simulator(2,3,2,8,3);
        int timeLimit = 2000;

        simulate.setClock(0);
        while (simulate.getClock() < timeLimit)
            simulate.process();
    }
}