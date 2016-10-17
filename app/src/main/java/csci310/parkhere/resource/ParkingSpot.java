package csci310.parkhere.resource;

import java.io.Serializable;

public class ParkingSpot implements Serializable {
    private static final long serialVersionUID = 1033583913841696111L;
    public long parkingSpotID;
    public User owner;
}
