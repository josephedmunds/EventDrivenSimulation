/**
 * @author Joseph Edmunds
 * Created: 10/6/2015.
 */
public class EventItem implements Comparable<EventItem>{
    public int time_of_day;
    public int service_time;
    public int type_of_event; //-1 is arrival, 0..9 is departure from that teller

    EventItem(int time, int serviceTime, int type) {
        time_of_day = time;
        service_time = serviceTime;
        type_of_event = type;
    }

    @Override
    public int compareTo(EventItem o) {
        if (this.time_of_day < o.time_of_day)
            return -1;
        else if (this.time_of_day == o.time_of_day)
            return 0;
        else
            return 1;
    }
}
