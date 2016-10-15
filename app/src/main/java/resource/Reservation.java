package resource;

import java.io.Serializable;

/**
 * Created by angela02pd2014 on 10/14/16.
 */

public class Reservation implements Serializable {

    private static final long serialVersionUID = 456789; // How to generate in Android Studio?
    public long reservationID;
    public ParkingSpot spot;
    public TimeInterval reserveTimeInterval;
    public User renter;
    public double totalFee;
    public boolean paid;

    public Reservation(long rID, ParkingSpot s, TimeInterval time, User r, double fee, boolean p) {
        reservationID = rID;
        spot = s;
        reserveTimeInterval = time;
        renter = r;
        totalFee = fee;
        paid = p;
    }
}
