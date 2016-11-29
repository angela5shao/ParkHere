package csci310.parkhere.ui.activities;

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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.BraintreePaymentActivity;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.braintreepayments.api.models.VenmoAccountNonce;
import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;
import csci310.parkhere.ui.fragments.PrivateProfileFragment;
import csci310.parkhere.ui.fragments.ProviderReservationDetailFragment;
import csci310.parkhere.ui.fragments.ProviderReservationsFragment;
import csci310.parkhere.ui.fragments.SpaceDetailFragment;
import csci310.parkhere.ui.fragments.SpacesFragment;
import resource.MyEntry;
import resource.NetworkPackage;
import resource.ParkingSpot;
import resource.Reservation;
import resource.Time;
import resource.TimeInterval;
import resource.User;

/**
 * Created by ivylinlaw on 10/17/16.
 */
public class ProviderActivity extends AppCompatActivity implements SpacesFragment.OnFragmentInteractionListener,
        SpaceDetailFragment.OnFragmentInteractionListener, PrivateProfileFragment.OnFragmentInteractionListener,
        ProviderReservationDetailFragment.OnFragmentInteractionListener, ProviderReservationsFragment.OnFragmentInteractionListener {

    private final int EDIT_PROFILE_CODE = 3;

    LinearLayout _spaceLink, _resLink;
    CircularImageView _profilePic;

    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    Fragment spacesFragment, privateProfileFragment, spaceDetailFragment, reservationsFragment;
    BraintreeFragment mBraintreeFragment;
    String mAuthorization = "clientToken";

    ClientController clientController;
    requestParkingSpotListTask RPTask = null;
    requestSpotTimeIntervalTask RSTTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_ui);

        ClientController.getInstance().setCurrentActivity(this);


        clientController = ClientController.getInstance();
        clientController.setCurrentActivity(this);

        Toolbar providerrToolbar = (Toolbar) findViewById(R.id.providerTabbar);
        setSupportActionBar(providerrToolbar);
//
        _resLink = (LinearLayout)findViewById(R.id.ProviderResLink);
        _spaceLink = (LinearLayout)findViewById(R.id.spaceLink);

        _profilePic = (CircularImageView)findViewById(R.id.profilePic);
        // Set Border
        _profilePic.setBorderColor(R.color.colorLightBackground);
        _profilePic.setBorderWidth(10);

        String encodedPic = clientController.encodedProfilePic;
        if (encodedPic == "") {
            getProfilePic gpc = new getProfilePic(clientController.getUser().userID);
            try {
                encodedPic = gpc.execute((Void) null).get();
                clientController.encodedProfilePic = encodedPic;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        Glide.with(this)
                .load(encodedPic)
                .override(48, 48)
                .into(_profilePic);




//        getProfilePic gpc = new getProfilePic(clientController.getUser().userID);
//        String encodedPic = "";
//        try {
//            encodedPic = gpc.execute((Void) null).get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        Glide.with(this).load(encodedPic).into(_profilePic);



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
//        addSpaceFragment = new AddSpaceFragment();

        spaceDetailFragment = new SpaceDetailFragment();

        // Initialize BraintreeFragment
        try {
            // TODO mAuthorization should be either a client token or tokenization key
            mBraintreeFragment = BraintreeFragment.newInstance(this, mAuthorization);
            // mBraintreeFragment is ready to use!
        } catch (InvalidArgumentException e) {
            // There was an issue with your authorization string.
        }

        _resLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestRenterReservationsTask rrtrt = new RequestRenterReservationsTask();
                rrtrt.execute();
            }
        });

        _spaceLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Clicked on spaces tab item");
                try {

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
        (new requestParkingSpotListTask()).execute();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragContainer, spacesFragment).commit();
    }

    public void switchToEditProfileFrag() {
        Intent intent = new Intent(getBaseContext(), EditProfileActivity.class);
        startActivityForResult(intent, EDIT_PROFILE_CODE);
    }

    public void switchToPrivateProfileFrag(PrivateProfileFragment privateProfileFragment){
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragContainer, privateProfileFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

//    public void showSpaceDetailFragment()
//    {
//        // TODO: Get ParkingSpot given position in list
//        if (clientController.parkingSpots.size() == 0) {
//            System.out.println("ProviderActivity: no spaces to select");
//            return;
//        }
//        ParkingSpot spotSelected = clientController.parkingSpots.get(clientController.currentIndexofSpaces);
//        if (spotSelected == null) {
//            System.out.println("Selected parking spot is null");
//            return;
//        }
//
//        spaceDetailFragment = new SpaceDetailFragment();
//        System.out.print("Show space detail " + spotSelected.getDescription());
//
//
//        try {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragContainer, spaceDetailFragment).commit();
//        } catch (Exception e) {
//            System.out.println("Spaces tab item exception");
//        }
//    }

    public void showSpaceDetailFragment(ParkingSpot ps){

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

        if (requestCode == EDIT_PROFILE_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                //
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
            clientController.logout(false);
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
    public void onReservationSelected(int resPosition, boolean ifNotPassed) {

        System.out.println("ProviderActivity onReservationSelected for: " + resPosition);
        if (clientController.providerReservations.size() == 0) {
            System.out.println("ProviderActivity: error - no providerReservations to select");
            return;
        }
        Reservation selectedRes = clientController.providerReservations.get(resPosition);
        if (selectedRes == null) {
            System.out.println("Selected parking spot is null");
            return;
        }
        ProviderReservationDetailFragment resDetailfragment = new ProviderReservationDetailFragment();
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
        args.putLong("RENTER", selectedRes.getRenterID());
        args.putLong("RES_ID", selectedRes.getReservationID());

//        if(selectedRes.review==null && !ifNotPassed) {
//            args.putBoolean("IF_CANREVIEW", true);
//        }
//        else {
//            args.putBoolean("IF_CANREVIEW", false);
//        }
//        args.putBoolean("IF_CANCANCEL", ifNotPassed);
//        args.putBoolean("IF_ISPAID", selectedRes.isPaid());
        resDetailfragment.setArguments(args);

        try {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragContainer, resDetailfragment).commit();
        } catch (Exception e) {
            System.out.println("ProviderActivity onReservationSelected exception");
        }
    }

    public void openSpaceEditFragment(ParkingSpot spot) {
        Intent intent = new Intent(getBaseContext(), EditSpaceActivity.class);
        intent.putExtra("SPOT", spot);
        startActivityForResult(intent, 87);

//        SpaceEditFragment editSpaceFrag = new SpaceEditFragment();
//
//        Bundle args = new Bundle();
//        args.putSerializable("spot", spot);
//        editSpaceFrag.setArguments(args);
//
//        // TODO: pass parkingspot
//
//        fragmentTransaction = fm.beginTransaction();
//        fragmentTransaction.replace(R.id.fragContainer, editSpaceFrag);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
    }

    @Override
    public void returnToReservationsFragment() {
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragContainer, reservationsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void onSpaceSelected(int spacePositionInList) {
        if(spacePositionInList < 0 || clientController.parkingSpots == null ||spacePositionInList >= clientController.parkingSpots.size())
            return;

        System.out.println("ProviderActivity onSpaceSelected for: " + spacePositionInList);

        clientController.currentIndexofSpaces = spacePositionInList;
        RSTTask = new requestSpotTimeIntervalTask(clientController.parkingSpots.get(spacePositionInList));
        RSTTask.execute((Void) null);

    }

    public void onEditSpace(ParkingSpot ps) {
        requestSpotTimeIntervalTask rstit = new requestSpotTimeIntervalTask(ps);
        rstit.execute();
    }

    // Called by SpacesFragment's "add" button
    public void onAddSpaceClick(View v) {
        Intent intent = new Intent(v.getContext(), AddSpaceActivity.class);
        startActivity(intent);
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

    public class requestParkingSpotListTask extends AsyncTask<Void, Void, ArrayList<ParkingSpot>> {

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
            if(list == null) {
                Toast.makeText(getBaseContext(), "Error get parkingspots Please try again.", Toast.LENGTH_SHORT).show();
                return;
            }


            clientController.providerToshowSpaces = true;
            clientController.parkingSpots = list;

            spacesFragment = new SpacesFragment();
            fragmentTransaction.replace(R.id.fragContainer, spacesFragment);
            fragmentTransaction.addToBackStack(null);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragContainer, spacesFragment).commit();



            Log.d("requestParkingSpots", "onPostExecute");
//            showSpaceFragment();
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

            Log.d("requesTimeInterval: Key", key);


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


            if(myTimeIntervals != null)
            {
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

                LoadSpotImageTask slit =  new LoadSpotImageTask(parkingSpot.getParkingSpotID(), args);
                slit.execute();

            }
            else{
                Toast.makeText(getBaseContext(), "Error in getting parking spot info, try again", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void loadImages(Bundle bundle, ArrayList<String> inputURL) {

        bundle.putStringArrayList("spot_images", inputURL);
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

            ClientController clientController = ClientController.getInstance();
            clientController.getParkingSpotImages("PARKINGSPOT", mSpotID);

            NetworkPackage NP = clientController.checkReceived();

            MyEntry<String, Serializable> entry = NP.getCommand();

            ArrayList<String> images = (ArrayList<String>) entry.getValue() ;

            ArrayList<String> urls = new ArrayList<>();


            Log.d("LoadSpotImageTask", "Get images back");

            if(entry.getKey().equals("PARKINGSPOTIMAGESURLS"))
            {
//                ArrayList<String> urls = (ArrayList<String>) entry.getValue() ;

                Calendar cal = Calendar.getInstance();

                for(String url : images)
                {
                    Log.d("FETCHIMAGE", url);
                    urls.add(url+"?" + String.valueOf(cal.getTimeInMillis()));
                }
                return urls;
            }

            return null;

        }
        @Override
        protected void onPostExecute(ArrayList<String> imagesURLs) {
            if(imagesURLs != null) {
                Log.d("LoadSpotImageTask", " post execute imagesURLs != null");

                ArrayList<String> mImagesURLs = new ArrayList<String>(imagesURLs);
                loadImages(this.bundle, mImagesURLs);



                spaceDetailFragment.setArguments(bundle);
                fm.beginTransaction().add(R.id.fragContainer, spaceDetailFragment).commit();


            } else{
                // TODO: WHAT TO DISPLAY WHEN NO IMAGE
                //
                Toast.makeText(getBaseContext(), "Cannot find images", Toast.LENGTH_SHORT).show();
                spaceDetailFragment.setArguments(bundle);
                fm.beginTransaction().add(R.id.fragContainer, spaceDetailFragment).commit();

            }
        }

    }

    private class RequestRenterReservationsTask extends AsyncTask<Void, Void, ArrayList<Reservation>> {
        RequestRenterReservationsTask() { }
        @Override
        protected ArrayList<Reservation> doInBackground(Void... params) {
            clientController.requestMyProviderReservationList();
            NetworkPackage NP = clientController.checkReceived();
            MyEntry<String, Serializable> entry = NP.getCommand();
            String key = entry.getKey();
            Object value = entry.getValue();
            if (key.equals("PROVIDERRESERVATION")) {
                ArrayList<Reservation> list = (ArrayList<Reservation>) value;
                Log.d("FETCHRESERVATION", "listsize: " + String.valueOf(list.size()));

                return list;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Reservation> list) {
            if (list != null) {
                clientController.providerReservations = list;
                reservationsFragment = new ProviderReservationsFragment();
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragContainer, reservationsFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            } else {
                Toast.makeText(getBaseContext(), "Error on get providerReservations! Please try again.", Toast.LENGTH_SHORT).show();
            }


        }


    }

    private class getProfilePic extends AsyncTask<Void, Void, String>{
        private final long userID;

        getProfilePic(long userID){
            this.userID = userID;
        }
        @Override
        protected void onPreExecute(){
        }
        @Override
        protected String doInBackground(Void... params ){

//            clientController.getProfilePic(mUsername);
            clientController.getProfilePic(userID);

            NetworkPackage NP = clientController.checkReceived();
            MyEntry<String, Serializable> entry = NP.getCommand();

            String key = entry.getKey();
            Object value = entry.getValue();
            String encodedPic = null;
            if(key.equals("PROFILEPICIMAGE")){
                encodedPic = (String)value;
            }
            return encodedPic;
        }
        @Override
        protected void onPostExecute(String profilePic) {
            // TODO set profile image
//            _profilePic.setImageBitmap();
        }

    }

    public void updateBarImage()
    {
            String encodedPic  = null;

            getProfilePic gpc = new getProfilePic(clientController.getUser().userID);
            try {
                encodedPic = gpc.execute((Void) null).get();
                clientController.encodedProfilePic = encodedPic;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        if(encodedPic != null)
        {
            Glide.with(this)
                    .load(encodedPic)
                    .override(48, 48)
                    .into(_profilePic);
        }

    }
}
