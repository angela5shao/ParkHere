package resources;

import java.io.Serializable;

public class Payment implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8925128605152859161L;
	public long renterID;
    public long providerID;
    public long reservationID;

    public Payment(long reID, long prID, long resvID) {
        renterID = reID;
        providerID = prID;
        reservationID = resvID;
    }
}
