package csci310.parkhere.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;
import resource.MyEntry;
import resource.NetworkPackage;
import resource.ParkingSpot;
import resource.Time;
import resource.TimeInterval;
import resource.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchSpaceDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchSpaceDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchSpaceDetailFragment extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "position";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    View mView;

    // TODO: Rename and change types of parameters
    private int mPosition;
    private String mParam2;
    private String mParam3;
    private String mParam4;
    private String mParam5;

    private ParkingSpot mParkingSpot;
    private OnFragmentInteractionListener mListener;

    Button _searchspacedetail_reservebutton;




    SupportMapFragment mMapView;
    private GoogleMap googleMap;
    CameraPosition cameraPosition;


    public SearchSpaceDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchSpaceDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchSpaceDetailFragment newInstance(String param1, String param2) {
        SearchSpaceDetailFragment fragment = new SearchSpaceDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            m = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
        mPosition = getArguments().getInt(ARG_PARAM1);
        mParam2 = getArguments().getString(ARG_PARAM2);
        mParam3 = getArguments().getString(ARG_PARAM3);
        mParam4 = getArguments().getString(ARG_PARAM4);
        mParam5 = getArguments().getString(ARG_PARAM5);
    }
}

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        mView = inflater.inflate(R.layout.fragment_search_space_detail, container, false);
//
//        _searchspacedetail_reservebutton=(Button)mView.findViewById(R.id.searchspacedetail_reservebutton);
//        _searchspacedetail_reservebutton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                RenterReserveTask RRT = new RenterReserveTask(parkingSpotID, userID, timeInterval);
//                RRT.execute((Void) null);
//                Intent intent = new Intent(getContext(), PaymentActivity.class);
//                startActivityForResult(intent, 11);
//            }
//        });
//
//        return mView;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_search_space_detail, container, false);

        _searchspacedetail_reservebutton=(Button)mView.findViewById(R.id.searchspacedetail_reservebutton);
        _searchspacedetail_reservebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {




                System.out.println(mParam2+ " "+mParam3+" "+mParam4+" "+mParam5);
                String[] startDate1 = mParam2.split("\\D");
                String[] endDate1 = mParam4.split("\\D");
                String startDateStr = startDate1[1] + "-" + startDate1[0] + "-" +startDate1[2];
                String endDateStr = endDate1[1] + "-" + endDate1[0] + "-" +endDate1[2];

                Time startTime = new Time(startDateStr+" "+mParam3 + "-0");
                Time endTime = new Time(endDateStr+" "+mParam5+"-0");
                TimeInterval timeInterval = new TimeInterval(startTime,endTime);
                RenterReserveTask RRT = new RenterReserveTask(mParkingSpot.getParkingSpotID(), timeInterval,  ClientController.getInstance().getUser().userID);
                RRT.execute((Void) null);



            }
        });

        // Get ParkingSpot from controller
        ClientController controller = ClientController.getInstance();
        mParkingSpot = controller.searchResults.searchResultList.get(mPosition);


        mMapView = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        mMapView.onCreate(savedInstanceState);


        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);


        Log.d("MAPPOSITION", String.valueOf(mParkingSpot.getLat()) +" "+ String.valueOf(mParkingSpot.getLon()));
        Log.d("MAPPOSITION", "googlemap = null?" + String.valueOf(googleMap == null));

//        cameraPosition = new CameraPosition.Builder()
//                .target(new LatLng(mParkingSpot.getLat(), mParkingSpot.getLon())).zoom(12).build();
//
//        if(googleMap != null) {
//            googleMap.addMarker(new MarkerOptions()
//                    .position(new LatLng(mParkingSpot.getLat(), mParkingSpot.getLon())));
//            googleMap.animateCamera(CameraUpdateFactory
//                    .newCameraPosition(cameraPosition));
//        }

        // Populate fields with data
        ((TextView) mView.findViewById(R.id.searchspacedetail_address)).setText(mParkingSpot.getStreetAddr());
        ((TextView) mView.findViewById(R.id.searchspacedetail_price)).setText(new Double(mParkingSpot.search_price).toString());
//        ((TextView) mView.findViewById(R.id.searchspacedetail_rating)).setText(new Double(mParkingSpot.rating).toString());
        TextView _provider = (TextView) mView.findViewById(R.id.searchspacedetail_providername);
        _provider.setText(new Double(mParkingSpot.search_price).toString());
        _provider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublicProfileFragment publicProfileFragment = new PublicProfileFragment();
                Bundle args = new Bundle();
//                _firstnameText.setText();
//                _emailText.setText();
//                _phoneNumText.setText();
                args.putString("FIRSTNAME", );
                args.putString("EMAIL", );
                args.putString("PHONE_NUM", );
                args.putStringArrayList("REVIEWS", );
                publicProfileFragment.setArguments(args);
                Activity ac = getActivity();
                if(ac instanceof  RenterActivity)
                    ((RenterActivity) getActivity()).switchToPublicProfileFrag(publicProfileFragment);
            }
        });

        Time sTime = mParkingSpot.getTimeIntervalList().get(0).startTime;
        Time eTime = mParkingSpot.getTimeIntervalList().get(0).endTime;
        ((TextView) mView.findViewById(R.id.searchspacedetail_starttime)).setText(sTime.toString());
        ((TextView) mView.findViewById(R.id.searchspacedetail_endtime)).setText(eTime.toString());
        ((TextView) mView.findViewById(R.id.searchspacedetail_cartype)).setText(new Integer(mParkingSpot.getCartype()).toString());
        ((TextView) mView.findViewById(R.id.searchspacedetail_cancelpolicy)).setText(new Integer(mParkingSpot.cancelpolicy).toString());

        return mView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap inGoogleMap) {

        Log.d("ONMAPREADY", "inGoogleMap");

        googleMap = inGoogleMap;
        cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(mParkingSpot.getLat(), mParkingSpot.getLon())).zoom(12).build();
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(mParkingSpot.getLat(), mParkingSpot.getLon())));
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class RenterReserveTask extends AsyncTask<Void, Void, Boolean> {
        private final long parkingSpotID;
        private final TimeInterval timeInterval;
        private final long userID;

        private ProgressDialog progressDialog;

        RenterReserveTask(long parkingSpotID, TimeInterval timeinterval, long userID){
            this.parkingSpotID = parkingSpotID;
            this.timeInterval = timeinterval;
            this.userID = userID;
        }

        @Override
        protected void onPreExecute(){
            //Display a progress dialog
            progressDialog = new ProgressDialog(getContext(), R.style.AppTheme);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Booking...");
            progressDialog.show();
        }
        @Override
        protected Boolean doInBackground(Void... params ){
            // call client controller
            ClientController controller = ClientController.getInstance();
            controller.renterReserve(userID, parkingSpotID, timeInterval);
            Log.d("SEARCHRESERVE", "AFTERREQUEST");


            NetworkPackage NP = controller.checkReceived();
            MyEntry<String, Serializable> entry = NP.getCommand();
            String key = entry.getKey();
            Object value = entry.getValue();

            Log.d("SEARCHRESERVE", "key :" + key);

            if(key.equals("RESERVE")) {

                Intent intent = new Intent(getContext(), PaymentActivity.class);
                intent.putExtra("RESERVATIONID", (Long)value);

                startActivityForResult(intent, 11);
                return true;
            }
            else if(key.equals("RESERVEFAIL")) {
                Log.d("SEARCHRESERVE", "RESERVFAIL");
                return false;
            }
            else {
                Log.d("SEARCHRESERVE", "UNDEFINED ERROR");
                return false;
            }
        }
        @Override
        protected void onPostExecute(Boolean success) {
            if(success) {
                progressDialog.dismiss();


            } else{
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Book space failed! Please try agian.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class GetPublicProfileTask extends AsyncTask<Void, Void, ArrayList<String> >{
        private final String mUsername;
        private final String mPassword;
        private boolean authenticationStatus = true;

        UserLoginTask(String username, String password){
            mUsername = username;
            mPassword = password;
//            doInBackground((Void) null);
            System.out.println(mUsername);
            System.out.println(mPassword);
        }
        @Override
        protected void onPreExecute(){
            //Display a progress dialog
            progressDialog = new ProgressDialog(LoginActivity.this,
                    R.style.AppTheme);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();
        }
        @Override
        protected ArrayList<String> doInBackground(Void... params ){
            try {

                Log.d("LOGIN", "ASYNTASK LOGIN");
                clientController.login(email, password);
//                clientController.cancelReceived();
                NetworkPackage NP = clientController.checkReceived();

                if(NP == null)
                {
                    Log.d("DOINBACKGROUND", "null");
                }
                MyEntry<String, Serializable> entry = NP.getCommand();




                String key = entry.getKey();
                Object value = entry.getValue();
                if(key.equals("LF")){
                    return false;
                } else if(key.equals("LOGIN")){
                    User result = (User) value;
                    Log.d("LOGIN", result.userName);
                    clientController.setUser(result);

                    Log.d("Login", result.userPlate);


                    return true;
                } else{
                    return false;
                }
            } catch (IOException e) {
                return false;
            }
        }
        @Override
        protected void onPostExecute(ArrayList<String> inReview) {
            Context c = getBaseContext();
            if(success) {
                Log.d("LOGIN TEST 1", "yeah");
                progressDialog.dismiss();
                finish();
                if (clientController.getUser().userType) {
                    Intent myIntent = new Intent(c, RenterActivity.class);
                    startActivityForResult(myIntent, 0);
                } else {
                    Intent myIntent = new Intent(c, ProviderActivity.class);
                    startActivityForResult(myIntent, 0);
                }
            } else{
                progressDialog.dismiss();
                Intent intent = new Intent(c, HomeActivity.class);
                startActivityForResult(intent, 0);
            }
        }

    }
}
