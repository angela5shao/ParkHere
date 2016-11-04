package csci310.parkhere.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;
import resource.ParkingSpot;
import resource.Reservation;
import resource.SearchResults;
import resource.User;


/**
 * Created by ivylinlaw on 10/17/16.
 */
public class RenterActivity extends AppCompatActivity implements SearchFragment.OnFragmentInteractionListener,
        PrivateProfileFragment.OnFragmentInteractionListener, EditProfileFragment.OnFragmentInteractionListener,
        DisplaySearchFragment.OnFragmentInteractionListener, ReservationsFragment.OnFragmentInteractionListener,
        SearchSpaceDetailFragment.OnFragmentInteractionListener, ReservationDetailFragment.OnFragmentInteractionListener {

    int PAYMENT_REQUEST_CODE = 11;

    LinearLayout _resLink, _searchLink;
    ImageView _profilePic;
    ImageView _editLogo;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    Fragment searchFragment, privateProfileFragment, editProfileFragment, displaySearchFragment,
            reservationsFragment, searchSpaceDetailFragment, reservationDetailFragment;

    ClientController clientController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.renter_ui);

        clientController = ClientController.getInstance();
        clientController.setCurrentActivity(this);

        Toolbar renterToolbar = (Toolbar) findViewById(R.id.renterTabbar);
        setSupportActionBar(renterToolbar);

        fm = getSupportFragmentManager();
        fragmentTransaction = fm.beginTransaction();

        searchFragment = new SearchFragment();
        privateProfileFragment = new PrivateProfileFragment();
        editProfileFragment = new EditProfileFragment();
        displaySearchFragment = new DisplaySearchFragment();
        reservationsFragment = new ReservationsFragment();
        searchSpaceDetailFragment = new SearchSpaceDetailFragment();

        _resLink = (LinearLayout)findViewById(R.id.resLink);
        _searchLink = (LinearLayout)findViewById(R.id.searchLink);
        _profilePic = (ImageView) findViewById(R.id.profilePic);



        //*****************************************************************
        reservationDetailFragment = new ReservationDetailFragment();
        fragmentTransaction.add(R.id.fragContainer, searchFragment);
        fragmentTransaction.commit();

        //*****************************************************************

        _resLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragContainer, reservationsFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        _searchLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragContainer, searchFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        _profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = fm.beginTransaction();

                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragContainer);
                User user = clientController.getUser();
                if(user == null)
                    Log.d("PROFILE", "user is null");

                if (fragment instanceof PrivateProfileFragment && user != null) {
                    Log.d("@@@@@@@@@@@@@@ ", user.userName);
                    Log.d("@@@@@@@@@@@@@@ ", user.userLicense);
                    Log.d("@@@@@@@@@@@@@@ ", user.userPlate);
                    ((PrivateProfileFragment) fragment).updateUserInfo(user.userName, "", user.userLicense, user.userPlate);
                }

                fragmentTransaction.replace(R.id.fragContainer, privateProfileFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.renter_menu_ui, menu);

        return true;
    }

    public void returnToReservationsFragment() {
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragContainer, reservationsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
//        switch (item.getItemId()) {
//            case R.id.ProviderSwitch:
////                openSearch();
//                return true;
//            case R.id.action_compose:
//                composeMessage();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);

            if(item.getItemId() == R.id.ProviderSwitch) {
                Intent intent = new Intent(this, ProviderActivity.class);
                startActivityForResult(intent, 0);
                clientController.getUser().userType = false;
                return true;
            }
            else if(item.getItemId() == R.id.LogOut) {
                Intent intent = new Intent(this, HomeActivity.class);
                startActivityForResult(intent, 0);
                ClientController.resetController();
                return true;
            }
            else {
                return super.onOptionsItemSelected(item);
            }

    }

//    public void displaySearchResult(SearchResults results) {
//        if(results == null)
//            return;
//
//
//        ArrayList<ParkingSpot> spotList = results.searchResultList;
//
//        String[] searchResults = new String[spotList.size()];
//        for(int i = 0; i < spotList.size(); i++)
//        {
//            searchResults[i] = spotList.get(i).getDescription();
//        }
//
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragContainer);
//        if (fragment instanceof DisplaySearchFragment) {
//            ((DisplaySearchFragment) fragment).setSearchResultListview(searchResults);
//        }
//
//        fragmentTransaction = fm.beginTransaction();
//        fragmentTransaction.replace(R.id.fragContainer, displaySearchFragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
//    }

    public void displaySearchResult(SearchResults results, String startDate, String startTime, String endDate, String endTime) {
        if(results == null)
            return;


        ArrayList<ParkingSpot> spotList = results.searchResultList;

        String[] searchResults = new String[spotList.size()];
        for(int i = 0; i < spotList.size(); i++)
        {
            searchResults[i] = spotList.get(i).getDescription();
        }
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragContainer);
        if (fragment instanceof DisplaySearchFragment) {
            ((DisplaySearchFragment) fragment).setSearchResultListview(searchResults, startDate, startTime, endDate, endTime);
        }

        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragContainer, displaySearchFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    public void switchToEditProfileFrag() {
        fragmentTransaction = fm.beginTransaction();

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragContainer);
        User user = clientController.getUser();
        if (fragment instanceof EditProfileFragment && user != null) {
            Log.d("############## ", user.userName);
            Log.d("############## ", user.userLicense);
            Log.d("############## ", user.userPlate);
            ((EditProfileFragment) fragment).updateUserInfo(user.userName, "", user.userLicense, user.userPlate);
        }


        fragmentTransaction.replace(R.id.fragContainer, editProfileFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

//    public void updateUserInfo(String inUsername, String inPw, String inLicenseID, String inLicensePlate) {
//        // STILL NEED TO ADD PROFILE PIC
//        privateProfileFragment.updateUserInfo(inUsername, inPw, inLicenseID, inLicensePlate);
//        editProfileFragment.updateUserInfo(inUsername, inPw, inLicenseID, inLicensePlate);
//    }

    public void onReservationSelected(int resPosition) {
        System.out.println("RenterActivity onReservationSelected for: " + resPosition);
        if (clientController.reservations.size() == 0) {
            System.out.println("RenterActivity: error - no reservations to select");
            return;
        }
        Reservation selectedRes = clientController.reservations.get(resPosition);
        if (selectedRes == null) {
            System.out.println("Selected parking spot is null");
            return;
        }
        ReservationDetailFragment resDetailfragment = new ReservationDetailFragment();
        Bundle args = new Bundle();
        args.putDouble("LAT", selectedRes.getSpot().getLat());
        args.putDouble("LONG", selectedRes.getSpot().getLon());
        args.putString("ADDRESS", selectedRes.getSpot().getStreetAddr());
        args.putString("START_TIME", selectedRes.getReserveTimeInterval().endTime.toString());
        args.putString("END_TIME", selectedRes.getReserveTimeInterval().endTime.toString());
        args.putString("RENTER", Long.toString(selectedRes.getSpot().getOwner()));

//        resDetailfragment.setReservation(selectedRes.getSpot().getStreetAddr(),
//                                            selectedRes.getReserveTimeInterval().endTime.toString(),
//                                            selectedRes.getReserveTimeInterval().endTime.toString(),
//                                            Long.toString(selectedRes.getSpot().getOwner()),
//                                            selectedRes.getSpot().getLat(),
//                                            selectedRes.getSpot().getLon());

        try {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragContainer, resDetailfragment).commit();
        } catch (Exception e) {
            System.out.println("RenterActivity onReservationSelected exception");
        }
    }

    // Implements DisplaySearchFragment
    public void onSearchSpaceSelected(int position, String startDate, String startTime, String endDate, String endTime) {
        // Pass position in searchResultList to searchSpaceDetailFragment
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putString("param2", startDate);
        bundle.putString("param3", startTime);
        bundle.putString("param4", endDate);
        bundle.putString("param5", endTime);
        searchSpaceDetailFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragContainer, searchSpaceDetailFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
