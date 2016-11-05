package csci310.parkhere.controller;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import resource.CarType;
import resource.NetworkPackage;
import resource.ParkingSpot;
import resource.Reservation;
import resource.Review;
import resource.SearchResults;
import resource.Time;
import resource.TimeInterval;
import resource.User;


public class ClientController {

    private static final long serialVersionUID = 1239123098533917283L;

    private User user;
    public ArrayList<ParkingSpot> parkingSpots;
    public ArrayList<Reservation> reservations;
    public ArrayList<Review> reviews;
    public ClientCommunicator clientCommunicator;

    private static ClientController instance;


    private static Activity currentActivity;
    private static Fragment currentFragment;

    public int currentIndexofSpaces;

    public LatLng currLocation;
    public SearchResults searchResults;

    public boolean registerFailed;
    public boolean loginFailed;
    public boolean toDispaySearch;

    public boolean providerToshowSpaces;
    public boolean providerToshowSpacesDetail;
    //new
    private boolean received;
    private NetworkPackage NP;
    //

    private ClientController() throws IOException { // private constructor

        user = null;
        parkingSpots = new ArrayList<>();
        reservations = new ArrayList<>();
        reviews = new ArrayList<>();
        clientCommunicator = new ClientCommunicator(this);

        instance = this;

        registerFailed = false;
        loginFailed = false;
        toDispaySearch = false;
        providerToshowSpaces = false;
        providerToshowSpacesDetail = false;
        searchResults = null;
        currentIndexofSpaces = -1;
        //new
        received = false;
        NP = null;
        //
    }

    public void setCurrentActivity(Activity ac)
    {
        currentActivity = ac;
    }
    public void setCurrentFragment(Fragment fr) { currentFragment = fr;}

    public static ClientController getInstance(){

        if(instance == null) {
            try {
                instance = new ClientController();
            } catch (IOException e) {
                e.printStackTrace();
                return null;

            }
        }
        return instance;
    }

    public static void resetController()
    {
//            instance = null;
    }

    // Getters
    public User getUser() { return user;}
    public ArrayList<ParkingSpot> getSpots() { return parkingSpots; }
    public ArrayList<Reservation> getReservations() { return reservations; }
    public ArrayList<Review> getReviews() { return reviews; }

    // Setters
    public void setUser(User u) { user = u; }
    public void setSpots(ArrayList<ParkingSpot> spots) { parkingSpots = spots; }
    public void setReservations(ArrayList<Reservation> res) { reservations = res; }
    public void setReviews(ArrayList<Review> rev) { reviews = rev; }

    // TODO: Functions for login, signup
    public void login(String username, String pw) throws IOException {

        Log.d("LOGIN","Try to Login");
        cancelReceived();
        HashMap<String, Serializable> entry = new HashMap<>();
        entry.put("USERNAME", username);
        entry.put("PASSWORD", pw);
        clientCommunicator.send("LOGIN", entry);
    }

    public void register(String username, String pw, String phone, String license, String plate, String usertype, String name) throws IOException {
        HashMap<String, Serializable> entry = new HashMap<>();
        entry.put("USERNAME", username);
        entry.put("PASSWORD", pw);
        entry.put("NAME", name);
        entry.put("PHONE", Long.parseLong(phone));
        entry.put("LICENSE", license);
        entry.put("PLATE", plate);
        boolean usertype_bool;
        if(usertype=="renter"){
            usertype_bool = true;
        } else{
            usertype_bool = false;
        }
        entry.put("USERTYPE", usertype_bool);
        clientCommunicator.send("REGISTER", entry);
    }

    //new Functions
    public void updateReceived(NetworkPackage NP){
        this.NP = NP;
        received = true;
    }

    public void cancelReceived(){
        received = false;
        NP = null;
    }

    public NetworkPackage checkReceived(){
        while(received == false ){

        }
        received = false;
        return NP;
    }
    //new functions for the AsyncTask
//    public void updateActivity()
//    {
//        if(currentActivity instanceof RegisterRenterActivity)
//        {
//            RegisterRenterActivity rra = (RegisterRenterActivity)currentActivity;
//            Log.d("UPDATEACTIVITY", "RegisterRenterActivity");
//
//            if(user == null)
//            {
//                rra.onRegisterFailed(rra.getApplicationContext());
//            }
//            else
//            {
//                rra.onRegisterSuccess(rra.getApplicationContext());
//            }
//        }
//        else if(currentActivity instanceof RegisterProviderActivity) {
//            RegisterProviderActivity rpa = (RegisterProviderActivity)currentActivity;
//            Log.d("UPDATEACTIVITY", "RegisterProviderActivity");
//
//            if(user == null)
//            {
//                rpa.onRegisterFailed(rpa.getApplicationContext());
//            }
//            else
//            {
//                rpa.onRegisterSuccess(rpa.getApplicationContext());
//            }
//        }
//        else if(currentActivity instanceof RenterActivity) {
//            RenterActivity ra = (RenterActivity)currentActivity;
////            Log.d("UPDATEACTIVITY", "RenterActivity");
//
//            if(toDispaySearch)
//            {
//                ra.displaySearchResult(searchResults);
//                toDispaySearch = false;
//            }
//
//            if(user != null)
//            {
////                ra.updateUserInfo(user.getUsername(), "", user.userLicense, user.userPlate);
//            }
//        }
//        else if(currentActivity instanceof ProviderActivity) {
//            ProviderActivity ra = (ProviderActivity)currentActivity;
//            if(providerToshowSpaces)
//            {
//                ra.showSpaceFragment();
//                providerToshowSpaces = false;
//            }
//
//            else if(providerToshowSpacesDetail)
//            {
//                ra.showSpaceDetailFragment();
//                providerToshowSpacesDetail = false;
//            }
//
//
//        }
//        else if(currentActivity instanceof LoginActivity)
//        {
//            LoginActivity la = (LoginActivity)currentActivity;
//            if(user == null)
//            {
//                la.onLoginFailed(la.getApplicationContext());
//            }
//            else
//            {
//                la.onLoginSuccess(la.getApplicationContext());
//            }
//        }
//    }

    public User getProfile(long userID) {
        return null;
    }

    public boolean editProfile(String name, String email, String pw, String license, String plateNum) {
        return false;
    }

    // TODO: Functions for provider
//    public ArrayList<>

    public boolean addSpace(TimeInterval interval, String address, long userID) {
        return false;
    }

    public boolean editSpace(long spaceID, TimeInterval interval) {
        return false;
    }

    public void publishSpace(long spaceID) {

    }

    public void unpublishSpace(long spaceID) {

    }

    // TODO: Functions for renter

    public boolean editReservation(long resID) {
        return false;
    }

    public void ProviderCancel(long resID) {
        NetworkPackage NP = new NetworkPackage();
        NP.addEntry("PROVIDERCANCEL", resID);
        try {
            clientCommunicator.sendPackage(NP);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void RenterCancel (long resID) {
        NetworkPackage NP = new NetworkPackage();
        NP.addEntry("RENTERCANCEL", resID);
        try {
            clientCommunicator.sendPackage(NP);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Reservation getReservationDetail(int position) {
        return reservations.get(position);
    }

    public void submitReview(Review rev) {

    }

    public void report(Reservation res) {

    }

    public void search(LatLng location, String startDate, String startTime, String endDate, String endTime, String carType, String distance) throws IOException {
        currLocation = location;
        String[] time1 = startDate.split("-");
        String[] time11 = startTime.split("-");
        String[] time2 = endDate.split("-");
        String[] time22 = endTime.split("-");
        Log.d("time", time1[0] + " " + time1[1] + " "+time1[2]+ " "+ time11[0]+" "+time11[1]+ " "+time2[0] + " " + time2[1] + " "+time2[2]+ " "+ time22[0]+" "+time22[1]+ " " );
        Time inStartTime = new Time(Integer.parseInt(time1[2]),Integer.parseInt(time1[0]), Integer.parseInt(time1[1]), Integer.parseInt(time11[1]), Integer.parseInt(time11[0]),0);
        Time inEndTime = new Time(Integer.parseInt(time2[2]),Integer.parseInt(time2[0]), Integer.parseInt(time2[1]), Integer.parseInt(time22[1]), Integer.parseInt(time22[0]),0);
//        inStartTime.month -=;
//        inEndTime.month -=1;


        TimeInterval timeInterval = new TimeInterval(inStartTime, inEndTime);
        HashMap<String, Serializable> entry = new HashMap<>();
        if(location == null)
        {
            Toast.makeText(currentActivity.getApplicationContext(), "Please input search address", Toast.LENGTH_SHORT).show();

            return;
        }


        ParkingSpot.Location current_location = new ParkingSpot.Location(location.longitude, location.latitude);
        entry.put("LOCATION", current_location);
        entry.put("TIMEINTERVAL", timeInterval);
        entry.put("CARTYPE", carType);
        entry.put("DISTANCE", Integer.parseInt(distance.replaceAll("[\\D]", "")));
        clientCommunicator.send("SEARCH", entry);
    }

    public SearchResults sortSearchResultByPrice() {
        if(searchResults != null) {
            // ArrayList<ParkingSpot> searchResultList
            Collections.sort(searchResults.searchResultList, new Comparator<ParkingSpot>() {
                public int compare(ParkingSpot p1, ParkingSpot p2) {
                    return Double.compare(p1.search_price, p2.search_price);
                }
            });
        }
        return searchResults;
    }

    public SearchResults sortSearchResultByDist() {
        if(searchResults != null) {
            Collections.sort(searchResults.searchResultList, new Comparator<ParkingSpot>() {
                public int compare(ParkingSpot p1, ParkingSpot p2) {
                    double p1LatDiff = p1.getLat()-currLocation.latitude;
                    double p1LongDiff = p1.getLon()-currLocation.longitude;
                    double p1Dist = Math.sqrt(p1LatDiff*p1LatDiff + p1LongDiff*p1LongDiff);

                    double p2LatDiff = p2.getLat()-currLocation.latitude;
                    double p2LongDiff = p2.getLon()-currLocation.longitude;
                    double p2Dist = Math.sqrt(p2LatDiff*p2LatDiff + p2LongDiff*p2LongDiff);

                    return Double.compare(p1Dist, p2Dist);
                }
            });
        }
        return searchResults;
    }

    public SearchResults sortSearchResultBySpotRating() {
        if(searchResults != null) {
            Collections.sort(searchResults.searchResultList, new Comparator<ParkingSpot>() {
                public int compare(ParkingSpot p1, ParkingSpot p2) {
                    return Double.compare(p1.review, p2.review);
                }
            });
        }
        return searchResults;
    }

    public SearchResults sortSearchResultByProviderRating() {
        if(searchResults != null) {
//            Collections.sort(searchResults.searchResultList, new Comparator<ParkingSpot>() {
//                public int compare(ParkingSpot p1, ParkingSpot p2) {
//                    return Double.compare(p1.providerReview, p2.providerReview);
//                }
//            });
        }
        return searchResults;
    }

    public void addSpace(LatLng location, String streetAddress, String description, String carType, int cancelPolicy)
    {
        if(location == null)
            return;

        int mCarType = new CarType(carType).findNum();
        ParkingSpot spot = new ParkingSpot(user.userID,null,location.latitude,location.longitude,streetAddress,description, "",mCarType,cancelPolicy);
        //public ParkingSpot(long userID, ArrayList<TimeInterval> time, double lat, double lon, String streetAddr, String description, String zipcode, int cartype) {
        try {
            clientCommunicator.send("ADD_PARKINGSPOT", spot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public boolean book(long spaceID, long userID, TimeInterval interval) {
        return false;
    }

    public void postPaymentNonceToServer(String paymentMethodNonce)
    {
        try {
            clientCommunicator.send("PAYMENT_SUCCESS", paymentMethodNonce);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public void requestMyReservationList()
    {
        if(user == null)
            return;

        NetworkPackage NP = new NetworkPackage();
        NP.addEntry("FETCHRESERVATION", user.userID);
        try {
            clientCommunicator.sendPackage(NP);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void requestMyParkingSpotList()
    {
        if(user == null)
            return;

        NetworkPackage NP = new NetworkPackage();
        NP.addEntry("FETCHPARKINGSPOT", user.userID);
        try {
            clientCommunicator.sendPackage(NP);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void requestSpotTimeInterval(ParkingSpot spot)
    {
        if(user == null && spot == null)
        {
            return;
        }

        NetworkPackage NP = new NetworkPackage();
        NP.addEntry("FETCHTIMEINTERVAL", spot.getParkingSpotID());
        try {
            clientCommunicator.sendPackage(NP);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void requestSpotTimeIntervalWithDate(ParkingSpot spot, String Date){
        if(user == null && spot == null)
        {
            return;
        }
        NetworkPackage NP = new NetworkPackage();
        HashMap<String, Serializable> map = new HashMap<String, Serializable>();
        map.put("PARKINGSPOTID", spot.getParkingSpotID());
        map.put("DATE", Date);
        NP.addEntry("FETCHTIMEINTERVALWITHDATE", map);
        try {
            clientCommunicator.sendPackage(NP);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSpotTimeInterval(long spotID, ArrayList<TimeInterval> intervals)
    {
        for(int i = 0; i < parkingSpots.size(); i++)
        {
            ParkingSpot spot = parkingSpots.get(i);
            if(spot.getParkingSpotID() == spotID)
            {
                spot.setTimeIntervalList(intervals);
            }
        }
    }

    public void requestAddTime(ParkingSpot spot, Time startTime, Time endTime, double price)
    {
//        startTime.month +=1;
//        endTime.month += 1;

        TimeInterval timeInterval = new TimeInterval(startTime, endTime);


        NetworkPackage NP = new NetworkPackage();
        HashMap<String, Serializable> map = new HashMap<>();
        map.put("PARKINGSPOTID", spot.getParkingSpotID());
        map.put("TIMEINTERVAL", timeInterval);



        System.out.println("Start:"+timeInterval.startTime);
        System.out.println("End:" + timeInterval.endTime);
        map.put("PRICE", price);
        NP.addEntry("ADDTIME", map);
        try {
            clientCommunicator.sendPackage(NP);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout(boolean userType) {
        NetworkPackage NP = new NetworkPackage();
        NP.addEntry("LOGOUT", userType);
        try {
            clientCommunicator.sendPackage(NP);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void renterReserve(long userID, long parkingSpotID, TimeInterval timeInterval){
        NetworkPackage NP = new NetworkPackage();
        HashMap<String, Serializable> map = new HashMap<String, Serializable>();
        map.put("PARKINGSPOTID", parkingSpotID);
        map.put("RENTERID", userID);
        map.put("TIMEINTERVAL", timeInterval);
        NP.addEntry("RESERVE", map);
        try {
            clientCommunicator.sendPackage(NP);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void EditProfile(String username, String password, String userLicense, String userPlate){
        NetworkPackage NP = new NetworkPackage();
        HashMap<String, Serializable> map = new HashMap<String, Serializable>();
        map.put("USERNAME", username);
        map.put("PASSWORD", password);
        map.put("USERLICENSE", userLicense);
        map.put("USERPLATE", userPlate);
        NP.addEntry("EDITPROFILE", map);
        try {
            clientCommunicator.sendPackage(NP);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
