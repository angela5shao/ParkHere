package csci310.parkhere.controller;

import java.util.ArrayList;

import csci310.parkhere.resource.ParkingSpot;
import csci310.parkhere.resource.Reservation;
import csci310.parkhere.resource.Review;
import csci310.parkhere.resource.User;

/**
 * Created by angela02pd2014 on 10/16/16.
 */

public class ClientController {
    private User user;
    private ArrayList<ParkingSpot> parkingSpots;
    private ArrayList<Reservation> reservations;
    private ArrayList<Review> reviews;

    public ClientController() {
        user = null;
        parkingSpots = null;
        reservations = null;
        reviews = null;
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
    public long login(String username, String pw) {
        return 0;
    }

    public long register(String username, String pw) {
        return 0;
    }

    public User getProfile(long userID) {
        return null;
    }

    public boolean editProfile(String name, String email, String pw, String license, String plateNum) {
        return false;
    }

    // TODO: Functions for provider
//    public ArrayList<>

    // TODO: Functions for renter
}
