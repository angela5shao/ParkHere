package csci310.parkhere.resource;

import java.io.Serializable;
import java.util.ArrayList;

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

    public static class Location implements Serializable {
        /**
		 * 
		 */
		private static final long serialVersionUID = 127970804711014803L;
		double lg;
        double lt;
        public Location(int longitude, int latitude){
            lg = longitude;
            lt = latitude;
        }
    }
}
