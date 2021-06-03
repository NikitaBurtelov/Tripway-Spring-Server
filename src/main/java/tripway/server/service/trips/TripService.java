package tripway.server.service.trips;


import org.apache.catalina.User;

/**
 * @author Nikita Burtelov
 */
public interface TripService {
    void getTripId(long id);

    void saveTrip(User user);

    void removeTrip(User user);

    void removeTrip(long id);

    void updateTrip(User user);

    void updateTrip(long id);
}