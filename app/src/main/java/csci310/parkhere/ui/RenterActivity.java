package csci310.parkhere.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;
import resource.MyEntry;
import resource.NetworkPackage;
import resource.ParkingSpot;
import resource.Reservation;
import resource.SearchResults;
import resource.Time;
import resource.User;

/**
 * Created by ivylinlaw on 10/17/16.
 */
public class RenterActivity extends AppCompatActivity implements SearchFragment.OnFragmentInteractionListener,
        PrivateProfileFragment.OnFragmentInteractionListener, EditProfileFragment.OnFragmentInteractionListener,
        DisplaySearchFragment.OnFragmentInteractionListener, ReservationsFragment.OnFragmentInteractionListener,
        SearchSpaceDetailFragment.OnFragmentInteractionListener, ReservationDetailFragment.OnFragmentInteractionListener,
        PublicProfileFragment.OnFragmentInteractionListener {

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
//        displaySearchFragment = new DisplaySearchFragment();
//        reservationsFragment = new ReservationsFragment();
//        searchSpaceDetailFragment = new SearchSpaceDetailFragment();

        _resLink = (LinearLayout) findViewById(R.id.resLink);
        _searchLink = (LinearLayout) findViewById(R.id.searchLink);
        _profilePic = (ImageView) findViewById(R.id.profilePic);


        //*****************************************************************
        reservationDetailFragment = new ReservationDetailFragment();
        fragmentTransaction.add(R.id.fragContainer, searchFragment);
        fragmentTransaction.commit();

        //*****************************************************************

        _resLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (clientController.getUser().userPlate.equals("#######"))
                {
                    Toast.makeText(getBaseContext(), "Please fill in licence plate info before proceed", Toast.LENGTH_SHORT).show();
                    return;
                }

                RequestReservationsTask RRT = new RequestReservationsTask();
                RRT.execute((Void) null);
            }
        });


        _searchLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clientController.getUser().userPlate.equals("#######"))
                {
                    Toast.makeText(getBaseContext(), "Please fill in licence plate info before proceed", Toast.LENGTH_SHORT).show();
                    return;
                }


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

                privateProfileFragment = new PrivateProfileFragment();
                User user = clientController.getUser();
                if(user == null){
                    Log.d("PROFILE", "user is null");
                }
                else {
                    Bundle args = new Bundle();
                    args.putString("USERNAME", user.userName);
                    args.putString("PASSWORD", "");
                    args.putString("USERLICENSE",user.userLicense);
                    args.putString("USERPLATE", user.userPlate);
                    args.putString("PHONE", user.userPhone);
                    privateProfileFragment.setArguments(args);
                }
                fragmentTransaction.replace(R.id.fragContainer, privateProfileFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        if(clientController.getUser().userPlate.equals("#######"))
        {
            fragmentTransaction = fm.beginTransaction();

            privateProfileFragment = new PrivateProfileFragment();
            User user = clientController.getUser();
            if(user == null){
                Log.d("PROFILE", "user is null");
            }
            else {
                Bundle args = new Bundle();
                args.putString("USERNAME", user.userName);
                args.putString("PASSWORD", "");
                args.putString("USERLICENSE",user.userLicense);
                args.putString("USERPLATE", user.userPlate);
                privateProfileFragment.setArguments(args);
            }
            fragmentTransaction.replace(R.id.fragContainer, privateProfileFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
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

        if (item.getItemId() == R.id.ProviderSwitch) {
            Intent intent = new Intent(this, ProviderActivity.class);
            startActivityForResult(intent, 0);
            clientController.getUser().userType = false;
            return true;
        } else if (item.getItemId() == R.id.LogOut) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivityForResult(intent, 0);
            clientController.logout(true);
            ClientController.resetController();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }


    public void displaySearchResult(SearchResults results, String startDate, String startTime, String endDate, String endTime) {
        if (results == null)
            return;

        ArrayList<ParkingSpot> spotList = results.searchResultList;

        String[] searchResults = new String[spotList.size()];
        for (int i = 0; i < spotList.size(); i++) {
            searchResults[i] = spotList.get(i).getDescription();
        }


        Log.d("SEARCH_RESULT", "To displaySearchFragment");

        displaySearchFragment = new DisplaySearchFragment();

        ((DisplaySearchFragment) displaySearchFragment).setSearchResultListview(searchResults, startDate, startTime, endDate, endTime);

        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragContainer, displaySearchFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void onFragmentInteraction(Uri uri) {
        //you can leave it empty
    }

    @Override
    public void onSearchSpaceSelected(int position, String startDate, String startTime, String endDate, String endTime) {
        // Pass position in searchResultList to searchSpaceDetailFragment
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putString("param2", startDate);
        bundle.putString("param3", startTime);
        bundle.putString("param4", endDate);
        bundle.putString("param5", endTime);

        loadImages(bundle);

        Log.d("ONSEARCHSPACESELECTED", startDate + " " + startTime + " " + endDate + " " + endTime);

        fragmentTransaction = fm.beginTransaction();

        searchSpaceDetailFragment = new SearchSpaceDetailFragment();
        searchSpaceDetailFragment.setArguments(bundle);


        fragmentTransaction.replace(R.id.fragContainer, searchSpaceDetailFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void switchToEditProfileFrag(EditProfileFragment editProfileFragment) {
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragContainer, editProfileFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void switchToPrivateProfileFrag(PrivateProfileFragment privateProfileFragment){
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragContainer, privateProfileFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void switchToPublicProfileFrag(PublicProfileFragment publicProfileFragment) {
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragContainer, publicProfileFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

//    public void updateUserInfo(String inUsername, String inPw, String inLicenseID, String inLicensePlate) {
//        // STILL NEED TO ADD PROFILE PIC
//        privateProfileFragment.updateUserInfo(inUsername, inPw, inLicenseID, inLicensePlate);
//        editProfileFragment.updateUserInfo(inUsername, inPw, inLicenseID, inLicensePlate);
//    }

    //************************************************************************
    // FOR TESTING - load images to search space detail fragment
    public void loadImages(Bundle bundle) {
        ArrayList<String> images = new ArrayList<String>();
        images.add("http://sourcey.com/images/stock/salvador-dali-metamorphosis-of-narcissus.jpg");
        images.add("http://sourcey.com/images/stock/salvador-dali-the-dream.jpg");
        images.add("http://sourcey.com/images/stock/salvador-dali-persistence-of-memory.jpg");
        images.add("http://sourcey.com/images/stock/simpsons-persistence-of-memory.jpg");
        images.add("http://sourcey.com/images/stock/salvador-dali-the-great-masturbator.jpg");

        bundle.putStringArrayList("spot_images", images);
    }
    //************************************************************************

    public void onReservationSelected(int resPosition, boolean ifNotPassed) {
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

        Time startTime = selectedRes.getReserveTimeInterval().startTime;
        Time endTime = selectedRes.getReserveTimeInterval().endTime;

        Time displayStartTime = new Time(startTime.year,startTime.month,startTime.dayOfMonth,startTime.hourOfDay,startTime.minute,startTime.second);
        Time displayEndTime = new Time(endTime.year,endTime.month,endTime.dayOfMonth,endTime.hourOfDay,endTime.minute,endTime.second);

        displayStartTime.month+=1;
        displayEndTime.month+=1;


        args.putString("START_TIME", displayStartTime.toString());
        args.putString("END_TIME", displayEndTime.toString());
        args.putString("RENTER", Long.toString(selectedRes.getSpot().getOwner()));
        args.putLong("RES_ID", selectedRes.getReservationID());

        if(selectedRes.review==null && !ifNotPassed) {
            args.putBoolean("IF_CANREVIEW", true);
        }
        else {
            args.putBoolean("IF_CANREVIEW", false);
        }
        args.putBoolean("IF_CANCANCEL", ifNotPassed);
        resDetailfragment.setArguments(args);

        try {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragContainer, resDetailfragment).commit();
        } catch (Exception e) {
            System.out.println("RenterActivity onReservationSelected exception");
        }
    }

    private class RequestReservationsTask extends AsyncTask<Void, Void, ArrayList<Reservation>> {
        RequestReservationsTask() { }
        @Override
        protected ArrayList<Reservation> doInBackground(Void... params) {
            clientController.requestMyReservationList();
            NetworkPackage NP = clientController.checkReceived();
            MyEntry<String, Serializable> entry = NP.getCommand();
            String key = entry.getKey();
            Object value = entry.getValue();
            if (key.equals("RESERVATIONLIST")) {
                ArrayList<Reservation> list = (ArrayList<Reservation>) value;
                Log.d("FETCHRESERVATIONLIST", "listsize: " + String.valueOf(list.size()));

                return list;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Reservation> list) {
            if (list != null) {
                clientController.reservations = list;
                reservationsFragment = new ReservationsFragment();
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragContainer, reservationsFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            } else {
                Toast.makeText(getBaseContext(), "Error on get reservations! Please try again.", Toast.LENGTH_SHORT).show();
            }


        }
    }
}
