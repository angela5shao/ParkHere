package csci310.parkhere.controller;

import android.os.StrictMode;
import android.util.Log;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import csci310.parkhere.resource.CarType;
import csci310.parkhere.resource.ParkingSpot;
import csci310.parkhere.resource.Reservation;
import csci310.parkhere.resource.Review;
import csci310.parkhere.resource.TimeInterval;
import csci310.parkhere.resource.User;

public class ClientController implements Serializable {

    private static final long serialVersionUID = 1239123098533917283L;

    private User user;
    private ArrayList<ParkingSpot> parkingSpots;
    private ArrayList<Reservation> reservations;
    private ArrayList<Review> reviews;
    private HashMap<String, Serializable> entry;
//    private static ClientController ourInstance = new ClientController();
    private ClientCommunicator ClientCommunicator;

    public ClientController() { // private constructor
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        Log.d("&&&&&&&&&&&&&&&&& ", "waiting for the somthing wrong0");
        Log.d("new Tag", "a new tag");
        user = null;
        parkingSpots = null;
        reservations = null;
        reviews = null;
        entry = new HashMap<>();
        Log.d("&&&&&&&&&&&&&&&&& ", "waiting for the somthing wrong1");
        ClientCommunicator = new ClientCommunicator();
        Log.d("&&&&&&&&&&&&&&&&& ", "waiting for the somthing else wrong1");
    }

//    public static ClientController getInstance() {
//        return ourInstance;
//    }

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
    public long login(String username, String pw) {
        return 0;
    }

    public void register(String username, String pw, String phone, String license, String plate, String usertype, String name) throws IOException {
        Log.v("############ ", username);
        Log.v("############ ", pw);
        Log.v("############ ", phone);
        Log.v("############ ", license);
        Log.v("############ ", plate);
        Log.v("############ ", usertype);
        Log.v("############ ", name);

        entry.put("USERNAME", username);
        entry.put("PASSWORD", pw);
        entry.put("NAME", name);
        entry.put("PHONE", Integer.parseInt(phone));
        entry.put("LICENSE", license);
        entry.put("PLATE", plate);
        boolean usertype_bool;
        if(usertype=="renter"){
            usertype_bool = true;
        } else{
            usertype_bool = false;
        }
        entry.put("USERTYPE", usertype_bool);
        ClientCommunicator.send("REGISTER", entry);
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

    public ArrayList<ParkingSpot> search(String address, int dist, CarType type, TimeInterval interval, int length) {
        return null;
    }

    public boolean book(long spaceID, long userID, TimeInterval interval) {
        return false;
    }

    public void loadPay(String method) {

    }
}
