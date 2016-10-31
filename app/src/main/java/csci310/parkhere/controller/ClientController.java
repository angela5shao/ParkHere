package csci310.parkhere.controller;

import android.util.Log;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import resource.CarType;
import resource.ParkingSpot;
import resource.Reservation;
import resource.Review;
import resource.Time;
import resource.TimeInterval;
import resource.User;


public class ClientController {

    private static final long serialVersionUID = 1239123098533917283L;

    private User user;
    private ArrayList<ParkingSpot> parkingSpots;
    private ArrayList<Reservation> reservations;
    private ArrayList<Review> reviews;
    public ClientCommunicator clientCommunicator;

    private static ClientController instance;

    public ClientController() { // private constructor

        user = null;
        parkingSpots = null;
        reservations = null;
        reviews = null;
        clientCommunicator = new ClientCommunicator();

        instance = this;

    }

    public static ClientController getInstance() {
        if(instance == null) {
            instance = new ClientController();
        }
        return instance;
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

    public User getProfile(long userID) {
        return null;
    }

    public boolean editProfile(String name, String email, String pw, String license, String plateNum) {
        return false;
    }

    // TODO: Functions for provider
//    public ArrayList<>

    public ArrayList<ParkingSpot> getSpaces(long userID) {
        return null;
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

    public Reservation getReservationDetail(long resID) {
        return null;
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
        ParkingSpot.Location current_location = new ParkingSpot.Location((int)location.latitude, (int)location.longitude);
        entry.put("LOCATION", current_location);
        entry.put("TIMEINTERVAL", timeInterval);
        entry.put("CARTYPE", carType);
        entry.put("DISTANCE", Integer.parseInt(distance.replaceAll("[\\D]", "")));
        clientCommunicator.send("SEARCH", entry);
    }
    public ArrayList<ParkingSpot> search(String address, int dist, CarType type, TimeInterval interval, int length) {
        return null;
    }

    public boolean book(long spaceID, long userID, TimeInterval interval) {
        return false;
    }

    public void loadPay(String method) {

    }
}
