package csci310.parkhere.resource;

import java.io.Serializable;

/**
 * Created by angela02pd2014 on 10/18/16.
 */

public class Review implements Serializable {

    private static final long serialVersionUID = 9876543;
    public long reviewID;
    public long forUserID;
    public int spotRating;
    public String spotComment;
    public int provOrRentRating;
    public String proOrRentComment;

}
