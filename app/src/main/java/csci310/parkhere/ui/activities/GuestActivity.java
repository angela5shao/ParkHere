package csci310.parkhere.ui.activities;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;
import csci310.parkhere.ui.fragments.DisplaySearchFragment;
import csci310.parkhere.ui.fragments.MapViewFragment;
import csci310.parkhere.ui.fragments.RenterReservationDetailFragment;
import csci310.parkhere.ui.fragments.SearchFragment;
import csci310.parkhere.ui.fragments.SearchSpaceDetailFragment;
import resource.MyEntry;
import resource.NetworkPackage;
import resource.ParkingSpot;
import resource.SearchResults;
/**
 * Created by ivylinlaw on 10/17/16.
 */
public class GuestActivity extends AppCompatActivity implements SearchFragment.OnFragmentInteractionListener,
        DisplaySearchFragment.OnFragmentInteractionListener {


    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    Fragment searchFragment, privateProfileFragment, editProfileFragment, displaySearchFragment,
            reservationsFragment, searchSpaceDetailFragment, reservationDetailFragment;

    ClientController clientController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        clientController = ClientController.getInstance();
        clientController.setCurrentActivity(this);
        setContentView(R.layout.activity_guest);
        fm = getSupportFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        searchFragment = new SearchFragment();
        fragmentTransaction.add(R.id.fragContainer, searchFragment);
        fragmentTransaction.commit();

    }


    public void displaySearchResult(SearchResults results, LatLng loc, String startDate, String startTime, String endDate, String endTime) {
        if (results == null)
            return;

        ArrayList<ParkingSpot> spotList = results.searchResultList;

        String[] searchResults = new String[spotList.size()];
        for (int i = 0; i < spotList.size(); i++) {
            searchResults[i] = spotList.get(i).getDescription();
        }


        Log.d("SEARCH_RESULT", "To displaySearchFragment");

        displaySearchFragment = new DisplaySearchFragment();

        ((DisplaySearchFragment) displaySearchFragment).setSearchResultListview(searchResults, loc, startDate, startTime, endDate, endTime);

        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragContainer, displaySearchFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void onFragmentInteraction(Uri uri) {
        //you can leave it empty
    }


    private class LoadSpotImageTask extends AsyncTask<Void, Void, ArrayList<String> >{
        private final long mSpotID;
        private final Bundle bundle;

        LoadSpotImageTask(long spotID, Bundle bundle){
            mSpotID = spotID;
            this.bundle = bundle;
            System.out.println(mSpotID);
        }
        @Override
        protected void onPreExecute() { }
        @Override
        protected ArrayList<String> doInBackground(Void... params ){
//            try {
//
            ClientController clientController = ClientController.getInstance();
            clientController.getParkingSpotImages("PARKINGSPOT", mSpotID);

            NetworkPackage NP = clientController.checkReceived();

            MyEntry<String, Serializable> entry = NP.getCommand();
            if(entry.getKey().equals("PARKINGSPOTIMAGESURLS"))
            {
                ArrayList<String> urls = (ArrayList<String>) entry.getValue();


                for(String url : urls)
                {
                    Log.d("FETCHIMAGE", url);
                }
                return urls;
            }

            return null;

        }
        @Override
        protected void onPostExecute(ArrayList<String> imagesURLs) {

        }

    }

    public void switchToListViewFrag(DisplaySearchFragment displaySearchFragment) {
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragContainer, displaySearchFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void switchToMapViewFrag(MapViewFragment mapViewFragment) {
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragContainer, mapViewFragment);
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
    public void loadImages(Bundle bundle, ArrayList<String> inputURL) {


        bundle.putStringArrayList("spot_images", inputURL);
    }
    //************************************************************************


    @Override
    public void onSearchSpaceSelected(int position, String startDate, String startTime, String endDate, String endTime) {


    }


}
