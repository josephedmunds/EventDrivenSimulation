import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Joseph Edmunds
 *         Created: 10/11/2015.
 */
public class Teller {
    Queue<EventItem> teller;
    public int totalIdleTime;

    public Teller() {
        teller = new LinkedList<>();
    }
}