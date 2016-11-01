package csci310.parkhere.controller;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import csci310.parkhere.ui.LoginActivity;
import csci310.parkhere.ui.RegisterProviderActivity;
import csci310.parkhere.ui.RegisterRenterActivity;
import csci310.parkhere.ui.RenterActivity;
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
    private ArrayList<Reservation> reservations;
    private ArrayList<Review> reviews;
    public ClientCommunicator clientCommunicator;

    private static ClientController instance;


    private static Activity currentActivity;

    public SearchResults searchResults;

    public boolean registerFailed;
    public boolean loginFailed;
    public boolean toDispaySearch;




    private ClientController() { // private constructor

        user = null;
        parkingSpots = new ArrayList<>();
        reservations = new ArrayList<>();
        reviews = new ArrayList<>();
        clientCommunicator = new ClientCommunicator(this);

        instance = this;

        registerFailed = false;
        loginFailed = false;
        toDispaySearch = false;
        searchResults = null;
    }

    public void setCurrentActivity(Activity ac)
    {
        currentActivity = ac;
    }

    public static ClientController getInstance() {
        if(instance == null) {
            instance = new ClientController();
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


    public void updateActivity()
    {
        if(currentActivity instanceof RegisterRenterActivity)
        {
            RegisterRenterActivity rra = (RegisterRenterActivity)currentActivity;
            Log.d("UPDATEACTIVITY", "RegisterRenterActivity");

            if(user == null)
            {
                rra.onRegisterFailed(rra.getApplicationContext());
            }
            else
            {
                rra.onRegisterSuccess(rra.getApplicationContext());
            }
        }
        else if(currentActivity instanceof RegisterProviderActivity) {
            RegisterProviderActivity rpa = (RegisterProviderActivity)currentActivity;
            Log.d("UPDATEACTIVITY", "RegisterProviderActivity");

            if(user == null)
            {
                rpa.onRegisterFailed(rpa.getApplicationContext());
            }
            else
            {
                rpa.onRegisterSuccess(rpa.getApplicationContext());
            }
        }
        else if(currentActivity instanceof RenterActivity) {
            RenterActivity ra = (RenterActivity)currentActivity;
//            Log.d("UPDATEACTIVITY", "RenterActivity");

            if(toDispaySearch)
            {
                ra.displaySearchResult(searchResults);
                toDispaySearch = false;
            }

            if(user != null)
            {
//                ra.updateUserInfo(user.getUsername(), "", user.userLicense, user.userPlate);
            }
        }
        else if(currentActivity instanceof LoginActivity)
        {
            LoginActivity la = (LoginActivity)currentActivity;
            if(user == null)
            {
                la.onLoginFailed(la.getApplicationContext());
            }
            else
            {
                la.onLoginSuccess(la.getApplicationContext());
            }
        }
    }

    public User getProfile(long userID) {
        return null;
    }

    public boolean editProfile(String name, String email, String pw, String license, String plateNum) {
        return false;
    }

    // TODO: Functions for provider
//    public ArrayList<>

    public ArrayList<ParkingSpot> getSpaces(long userID) {
        return parkingSpots;
    }

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
    public ArrayList<Reservation> getReservations(long userID) {
        return null;
    }

    public boolean editReservation(long resID) {
        return false;
    }

    public boolean cancelReservation(long resID) {
        return false;
    }

    public void getReservationDetail(long resID) {
    }

    public void submitReview(Review rev) {

    }

    public void report(Reservation res) {

    }

    public void search(LatLng location, String startDate, String startTime, String endDate, String endTime, String carType, String distance) throws IOException {
        String[] time1 = startDate.split("-");
        String[] time11 = startTime.split("-");
        String[] time2 = endDate.split("-");
        String[] time22 = endTime.split("-");
        Log.d("time", time1[0] + " " + time1[1] + " "+time1[2]+ " "+ time11[0]+" "+time11[1]+ " "+time2[0] + " " + time2[1] + " "+time2[2]+ " "+ time22[0]+" "+time22[1]+ " " );
        Time inStartTime = new Time(Integer.parseInt(time1[2]),Integer.parseInt(time1[0]), Integer.parseInt(time1[1]), Integer.parseInt(time11[1]), Integer.parseInt(time11[0]),0);
        Time inEndTime = new Time(Integer.parseInt(time2[2]),Integer.parseInt(time2[0]), Integer.parseInt(time2[1]), Integer.parseInt(time22[1]), Integer.parseInt(time22[0]),0);
        TimeInterval timeInterval = new TimeInterval(inStartTime, inEndTime);
        HashMap<String, Serializable> entry = new HashMap<>();
        if(location == null)
        {
            Toast.makeText(currentActivity.getApplicationContext(), "Please input search address", Toast.LENGTH_SHORT).show();

            return;
        }


        ParkingSpot.Location current_location = new ParkingSpot.Location((int)location.latitude, (int)location.longitude);
        entry.put("LOCATION", current_location);
        entry.put("TIMEINTERVAL", timeInterval);
        entry.put("CARTYPE", carType);
        entry.put("DISTANCE", Integer.parseInt(distance.replaceAll("[\\D]", "")));
        clientCommunicator.send("SEARCH", entry);
    }


    public void addSpace(LatLng location, String streetAddress, String description)
    {
        if(location == null)
            return;

        ParkingSpot spot = new ParkingSpot(user.userID,null,location.latitude,location.longitude,streetAddress,description, "", 0 );
        try {
            clientCommunicator.send("ADD_PARKINGSPOT", spot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    public ArrayList<ParkingSpot> search(String address, int dist, CarType type, TimeInterval interval, int length) {
//        return null;
//    }

    public boolean book(long spaceID, long userID, TimeInterval interval) {
        return false;
    }

    public void loadPay(String method) {

    }


    public void getMyReservationList()
    {
        if(user == null)
            return;

        NetworkPackage NP = new NetworkPackage();
        NP.addEntry("FETCHRESERVATION", user.getID());
    }

    public void getMyParkingSpotList()
    {
        if(user == null)
            return;

        NetworkPackage NP = new NetworkPackage();
        NP.addEntry("FETCHPARKINGSPOT", user.getID());

    }

    public void getSpotTimeInterval(ParkingSpot spot)
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

}
