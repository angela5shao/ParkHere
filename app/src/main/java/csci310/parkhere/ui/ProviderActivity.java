package csci310.parkhere.ui;

import android.app.Activity;
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

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.BraintreePaymentActivity;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.braintreepayments.api.models.VenmoAccountNonce;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;
import resource.MyEntry;
import resource.NetworkPackage;
import resource.ParkingSpot;
import resource.Time;
import resource.TimeInterval;
import resource.User;

/**
 * Created by ivylinlaw on 10/17/16.
 */
public class ProviderActivity extends AppCompatActivity implements SpacesFragment.OnFragmentInteractionListener,
        SpaceDetailFragment.OnFragmentInteractionListener, PrivateProfileFragment.OnFragmentInteractionListener,
        ReservationDetailFragment.OnFragmentInteractionListener, AddSpaceFragment.OnFragmentInteractionListener,
        EditProfileFragment.OnFragmentInteractionListener{

    LinearLayout _spaceLink;
    ImageView _profilePic;

    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    Fragment spacesFragment, privateProfileFragment, addSpaceFragment, spaceDetailFragment;
    BraintreeFragment mBraintreeFragment;
    String mAuthorization = "clientToken";

    ClientController clientController;
    requestParkingSpotListTask RPTask = null;
    requestSpotTimeIntervalTask RSTTask = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_ui);

        clientController = ClientController.getInstance();
        clientController.setCurrentActivity(this);

        Toolbar providerrToolbar = (Toolbar) findViewById(R.id.providerTabbar);
        setSupportActionBar(providerrToolbar);
//
        _spaceLink = (LinearLayout)findViewById(R.id.spaceLink);
        _profilePic = (ImageView)findViewById(R.id.profilePic);
//
        fm = getSupportFragmentManager();
        fragmentTransaction = fm.beginTransaction();


        (new requestParkingSpotListTask()).execute();

//        spacesFragment = new SpacesFragment();

//        fragmentTransaction.add(R.id.fragContainer, spacesFragment).commit();

//        ArrayList<ParkingSpot> parkingSpots = clientController.getSpaces(clientController.getUser().userID);

//        // TODO: Fix this; want to call setParkingSpot on spacesFragment
//        Fragment spcfragment = getSupportFragmentManager().findFragmentById(R.id.fragContainer);
//        if (spcfragment instanceof SpacesFragment) {
//            parkingSpots.add(new ParkingSpot(clientController.getUser().userID, null, 0, 0, "Tuscany 101, 10 Figueroa", "", "90007", 0x0001));
//            ((SpacesFragment) spcfragment).setParkingSpots(parkingSpots);
//        }
////        spacesFragment.setParkingSpots(parkingSpots);

        privateProfileFragment = new PrivateProfileFragment();
        addSpaceFragment = new AddSpaceFragment();

        spaceDetailFragment = new SpaceDetailFragment();

        // Initialize BraintreeFragment
        try {
            // TODO mAuthorization should be either a client token or tokenization key
            mBraintreeFragment = BraintreeFragment.newInstance(this, mAuthorization);
            // mBraintreeFragment is ready to use!
        } catch (InvalidArgumentException e) {
            // There was an issue with your authorization string.
        }

        _spaceLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Clicked on spaces tab item");
                try {
//                    fragmentTransaction.replace(R.id.fragContainer, spacesFragment)
//                        .commit();
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.fragContainer, spacesFragment).commit();
                    RPTask = new requestParkingSpotListTask();
                    RPTask.execute((Void) null);
                } catch (Exception e) {
                    System.out.println("Spaces tab item exception");
                }
            }
        });
        _profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                System.out.println("Clicked on profile tab item");
//                try {
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.fragContainer, privateProfileFragment).commit();
//                } catch (Exception e) {
//                    System.out.println("Profile tab item exception");
//                }
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
//                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragContainer);
//                User user = clientController.getUser();
//                if(user == null)
//                    Log.d("PROFILE", "user is null");
//
//                if (fragment instanceof PrivateProfileFragment && user != null) {
//                    Log.d("@@@@@@@@@@@@@@ ", user.userName);
//                    Log.d("@@@@@@@@@@@@@@ ", user.userLicense);
//                    Log.d("@@@@@@@@@@@@@@ ", user.userPlate);
//                    ((PrivateProfileFragment) fragment).updateUserInfo(user.userName, "", user.userLicense, user.userPlate);
//                }
                fragmentTransaction.replace(R.id.fragContainer, privateProfileFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    public void showSpaceFragment()
    {
        Log.d("show or not", "true");
        spacesFragment = new SpacesFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragContainer, spacesFragment).commit();
    }

    public void switchToEditProfileFrag() {
        fragmentTransaction = fm.beginTransaction();
        User user = clientController.getUser();
        EditProfileFragment editProfileFragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putString("USERNAME", user.userName);
        args.putString("PASSWORD", "******");
        args.putString("USERLICENSE", user.userLicense);
        args.putString("USERPLATE", user.userPlate);
        editProfileFragment.setArguments(args);
        fragmentTransaction.replace(R.id.fragContainer, editProfileFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void showSpaceDetailFragment()
    {
        // TODO: Get ParkingSpot given position in list
        if (clientController.parkingSpots.size() == 0) {
            System.out.println("ProviderActivity: no spaces to select");
            return;
        }
        ParkingSpot spotSelected = clientController.parkingSpots.get(clientController.currentIndexofSpaces);
        if (spotSelected == null) {
            System.out.println("Selected parking spot is null");
            return;
        }

        spaceDetailFragment = new SpaceDetailFragment();
        System.out.print("Show space detail " + spotSelected.getDescription());


        try {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragContainer, spaceDetailFragment).commit();
        } catch (Exception e) {
            System.out.println("Spaces tab item exception");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.provider_menu_ui, menu);
        return true;
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    PaymentMethodNonce paymentMethodNonce = data.getParcelableExtra(
                            BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE
                    );
                    String deviceData = data.getStringExtra(BraintreePaymentActivity.EXTRA_DEVICE_DATA);

                    String nonce = paymentMethodNonce.getNonce();
                    // Send the nonce to your server

                    if(paymentMethodNonce instanceof VenmoAccountNonce) {
                        VenmoAccountNonce venmoAccountNonce = (VenmoAccountNonce) paymentMethodNonce;
                        String venmoUsername = venmoAccountNonce.getUsername();
                    }
                    break;
                case BraintreePaymentActivity.BRAINTREE_RESULT_DEVELOPER_ERROR:
                case BraintreePaymentActivity.BRAINTREE_RESULT_SERVER_ERROR:
                case BraintreePaymentActivity.BRAINTREE_RESULT_SERVER_UNAVAILABLE:
                    // handle errors here, a throwable may be available in
                    // data.getSerializableExtra(BraintreePaymentActivity.EXTRA_ERROR_MESSAGE)
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
//        switch (item.getItemId()) {
////            case R.id.action_search:
////                openSearch();
////                return true;
////            case R.id.action_compose:
////                composeMessage();
////                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }

        if(item.getItemId() == R.id.RenterSwitch)
        {
            Intent intent = new Intent(this, RenterActivity.class);
            startActivityForResult(intent, 0);
            clientController.getUser().userType = true;

            Log.d("SWITCH","Switch To Renter");
            return true;
        }
        else if(item.getItemId() == R.id.LogOut)
        {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivityForResult(intent, 0);
            ClientController.resetController();
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }

    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    @Override
    public void returnToReservationsFragment() {

    }

    public void onSpaceSelected(int spacePositionInList) {
        if(spacePositionInList < 0 || clientController.parkingSpots == null ||spacePositionInList >= clientController.parkingSpots.size())
            return;

        System.out.println("ProviderActivity onSpaceSelected for: " + spacePositionInList);

        clientController.currentIndexofSpaces = spacePositionInList;

        RSTTask = new requestSpotTimeIntervalTask(clientController.parkingSpots.get(spacePositionInList));
        RSTTask.execute((Void) null);

    }

    public void onEditSpace(long spaceID) {
    }

    // Called by SpacesFragment's "add" button
    public void onAddSpaceClick(View v) {
        System.out.println("ProviderActivity onAddSpaceClick() called");
        AddSpaceFragment addSpaceFragment = new AddSpaceFragment();
        Bundle args = new Bundle();
        args.putLong("param1", 012345);
        addSpaceFragment.setArguments(args);

        try {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragContainer, addSpaceFragment).commit();
            System.out.println("onReservationSelected, replaced with reservationDetailFragment");
        } catch (Exception e) {
            System.out.println("Reservation item exception");
        }
    }

    public void returnToSpaces() {
        clientController.requestMyParkingSpotList();
        clientController.providerToshowSpaces = true;


        try {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragContainer, spacesFragment).commit();

            Fragment spcfragment = getSupportFragmentManager().findFragmentById(R.id.fragContainer);
            if (spcfragment instanceof SpacesFragment) {
                ((SpacesFragment) spcfragment).refresh();
            }

        } catch (Exception e) {
            System.out.println("Spaces tab item exception");
        }

    }

    private class requestParkingSpotListTask extends AsyncTask<Void, Void, ArrayList<ParkingSpot>> {

        requestParkingSpotListTask(){
//            doInBackground((Void) null);

        }
        @Override
        protected ArrayList<ParkingSpot> doInBackground(Void... params ){
            clientController.requestMyParkingSpotList();
            NetworkPackage NP = clientController.checkReceived();
            MyEntry<String, Serializable> entry = NP.getCommand();
            String key = entry.getKey();
            Object value = entry.getValue();
            if(key.equals("RESPONSEPARKINGSPOT")){
                Log.d("ResponsePakringSpot", "yes");
                ArrayList<ParkingSpot> myParkingSpot = (ArrayList<ParkingSpot>)value;
                Log.d("ResponsePakringSpot", "yes"+ myParkingSpot.size());

                for(int i = 0; i < myParkingSpot.size(); i++)
                    Log.d("Receive parkingSpot", String.valueOf(myParkingSpot.get(i).getParkingSpotID()));

                return myParkingSpot;
            }
            return null;
        }
        @Override
        protected void onPostExecute(ArrayList<ParkingSpot> list) {
            clientController.providerToshowSpaces = true;
            clientController.parkingSpots = list;

            spacesFragment = new SpacesFragment();

            Log.d("requestParkingSpots", "onPostExecute");
            showSpaceFragment();
        }

    }

    private class requestSpotTimeIntervalTask extends AsyncTask<Void, Void, ArrayList<TimeInterval>>{
        private ParkingSpot parkingSpot;
        private ArrayList<TimeInterval> myTimeIntervals;

        requestSpotTimeIntervalTask(ParkingSpot parkingSpot){
            this.parkingSpot = parkingSpot;
//            doInBackground((Void) null);
        }

        @Override
        protected void onPreExecute(){
            clientController.providerToshowSpacesDetail = true;
        }

        @Override
        protected ArrayList<TimeInterval> doInBackground(Void... params ){
            clientController.requestSpotTimeInterval(parkingSpot);
            NetworkPackage NP = clientController.checkReceived();
            MyEntry<String, Serializable> entry = NP.getCommand();
            String key = entry.getKey();
            Object value = entry.getValue();
            if(key.equals("RESPONSEINTERVAL")){
                HashMap<String, Serializable> map = (HashMap<String, Serializable>) value;
                myTimeIntervals = (ArrayList<TimeInterval>) map.get("TIMEINTERVAL");
                Long spotID = (Long)map.get("PARKINGSPOTID");
                clientController.setSpotTimeInterval(spotID,myTimeIntervals);
                return myTimeIntervals;
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<TimeInterval> myTimeIntervals) {
            ArrayList<Integer> inStartYear = new ArrayList<Integer>();
            ArrayList<Integer> inStartMonth = new ArrayList<Integer>();
            ArrayList<Integer> inStartDay = new ArrayList<Integer>();
            ArrayList<Integer> inStartHour = new ArrayList<Integer>();
            ArrayList<Integer> inStartMin = new ArrayList<Integer>();
            ArrayList<Integer> inEndYear = new ArrayList<Integer>();
            ArrayList<Integer> inEndMonth = new ArrayList<Integer>();
            ArrayList<Integer> inEndDay = new ArrayList<Integer>();
            ArrayList<Integer> inEndHour = new ArrayList<Integer>();
            ArrayList<Integer> inEndMin = new ArrayList<Integer>();
            for (TimeInterval timeInterv: myTimeIntervals) {
                Time startTime = timeInterv.startTime;
                Time endTime = timeInterv.endTime;

                inStartYear.add(startTime.year);
                inStartMonth.add(startTime.month);
                inStartDay.add(startTime.dayOfMonth);
                inStartHour.add(startTime.hourOfDay);
                inStartMin.add(startTime.minute);
                inEndYear.add(endTime.year);
                inEndMonth.add(endTime.month);
                inEndDay.add(endTime.dayOfMonth);
                inEndHour.add(endTime.hourOfDay);
                inEndMin.add(endTime.minute);
            }

            Bundle args = new Bundle();
            args.putString("ADDRESS", parkingSpot.getStreetAddr());
            args.putIntegerArrayList("START_YEARS", inStartYear);
            args.putIntegerArrayList("START_MONTHS", inStartMonth);
            args.putIntegerArrayList("START_DAYS", inStartDay);
            args.putIntegerArrayList("START_HOURS", inStartHour);
            args.putIntegerArrayList("START_MINS", inStartMin);
            args.putIntegerArrayList("END_YEARS", inEndYear);
            args.putIntegerArrayList("END_MONTHS", inEndMonth);
            args.putIntegerArrayList("END_DAYS", inEndDay);
            args.putIntegerArrayList("END_HOURS", inEndHour);
            args.putIntegerArrayList("END_MINS", inEndMin);

            spaceDetailFragment = new SpaceDetailFragment();
            ((SpaceDetailFragment)spaceDetailFragment).thisParkingSpot = parkingSpot;
            spaceDetailFragment.setArguments(args);
            fm.beginTransaction().add(R.id.fragContainer, spaceDetailFragment).commit();
        }
    }
}
