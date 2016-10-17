package csci310.parkhere.resource;

import java.io.Serializable;

public class Payment implements Serializable {

    private static final long serialVersionUID = 45666;
    public long renterID;
    public long providerID;
    public long reservationID;

    public Payment(long reID, long prID, long resvID) {
        renterID = reID;
        providerID = prID;
        reservationID = resvID;
    }
}
