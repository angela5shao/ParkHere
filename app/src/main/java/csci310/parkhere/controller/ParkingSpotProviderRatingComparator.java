package csci310.parkhere.controller;

import java.util.Comparator;

import resource.ParkingSpot;

/**
 * Created by angela02pd2014 on 11/3/16.
 */

public class ParkingSpotProviderRatingComparator implements Comparator {
    public int compare(Object obj1, Object obj2) {
        ParkingSpot spot1 = (ParkingSpot) obj1;
        ParkingSpot spot2 = (ParkingSpot) obj2;

        // TODO: return spot's provider's rating
//        return Double.compare(spot1.providerRating, spot2.providerRating);
        return Double.compare(spot1.search_price, spot2.search_price);
    }
}
