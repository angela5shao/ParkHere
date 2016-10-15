package resources;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by angela02pd2014 on 10/14/16.
 */

public class ParkingSpot implements Serializable {

    private static final long serialVersionUID = 1033583913841696111L;
    public long parkingSpotID;
    public User owner;
    public ArrayList<TimeInterval> availableTimeslots;
    public Location spotAddress;
    public ParkingSpot(User provider, ArrayList<TimeInterval> times, Location address) {
        owner = provider;
        availableTimeslots = times;
        spotAddress = address;
    }

    public class Location {
        double lg;
        double lt;
        Location(int longitude, int latitude){
            lg = longitude;
            lt = latitude;
        }
    }
}
