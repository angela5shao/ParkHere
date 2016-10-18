package csci310.parkhere.resource;

import java.io.Serializable;

/**
 * Created by angela02pd2014 on 10/18/16.
 */

public class Review implements Serializable {

    private static final long serialVersionUID = 9876543;

    public long reviewID;
    public long forUserID;
    public long spaceID;
    public float spotRating;
    public String spotComment;
    public float provOrRentRating;
    public String proOrRentComment;

    public Review() {
        reviewID = -1;
        forUserID = -1;
        spaceID = -1;
        spotRating = -1;
        spotComment = "";
        provOrRentRating = -1;
        proOrRentComment = "";
    }

    public Review(long inReviewID, long inForUserID, long inSpaceID, float inSpotRating,
                    String inSpotComment, float inUserRating, String inUserComment) {
        reviewID = inReviewID;
        forUserID = inForUserID;
        spaceID = inSpaceID;
        spotRating = inSpotRating;
        spotComment = inSpotComment;
        provOrRentRating = inUserRating;
        proOrRentComment = inUserComment;
    }
}
