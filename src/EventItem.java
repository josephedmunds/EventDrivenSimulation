/**
 * @author Joseph Edmunds
 * Created: 10/6/2015.
 */
public class EventItem {
    private int current_number;
    private int total_idle_time;
    private int max_length;

    public int time_of_day;
    public int service_time;
    public int type_of_event; //-1 is arrival, 0..9 is departure from that teller

    EventItem(){}

    EventItem(int time, int serviceTime, int type) {
        time_of_day = time;
        service_time = serviceTime;
        type_of_event = type;
    }
}
