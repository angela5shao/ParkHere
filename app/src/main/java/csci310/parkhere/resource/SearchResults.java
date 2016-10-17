package csci310.parkhere.resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by ivylinlaw on 10/16/16.
 */
public class SearchResults implements Serializable {

    private static final long serialVersionUID = -8780184924166565894L;

    public ArrayList<ParkingSpot> searchResultList;
    public GregorianCalendar searchTime;


    public SearchResults() {
        searchResultList = new ArrayList<ParkingSpot>();
        searchTime = new GregorianCalendar();
    }

    public SearchResults(ArrayList<ParkingSpot> inSearchResultList, GregorianCalendar inSearchTime) {
        searchResultList = new ArrayList<ParkingSpot>(inSearchResultList);
        searchTime = new GregorianCalendar(inSearchTime.get(Calendar.YEAR),
                inSearchTime.get(Calendar.MONTH),
                inSearchTime.get(Calendar.DAY_OF_MONTH),
                inSearchTime.get(Calendar.HOUR_OF_DAY),
                inSearchTime.get(Calendar.MINUTE),
                inSearchTime.get(Calendar.SECOND));
    }
}