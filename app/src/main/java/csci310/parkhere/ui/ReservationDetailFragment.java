package csci310.parkhere.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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

import java.io.Serializable;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;
import resource.MyEntry;
import resource.NetworkPackage;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReservationDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReservationDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservationDetailFragment extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    SupportMapFragment mMapView;
    private GoogleMap googleMap;
    CameraPosition cameraPosition;

    TextView _spacedetail_address, _start_time_label, _end_time_label, _renter_username_label;
    Button _btn_review, _btn_cancel;

    // latitude and longitude (default as USC)
    private double curr_lat = 34.0224;
    private double curr_long = 118.2851;
    private String address = "[address]";
    private String start_time = "[start time]";
    private String end_time = "[end time]";
    private String renter_username = "[renter username]";
    private long res_id = 0;

    public ReservationDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReservationDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReservationDetailFragment newInstance(int param1, String param2) {
        ReservationDetailFragment fragment = new ReservationDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get data from parent activity/fragment
        Bundle b = getArguments();
        if (b != null) {
            curr_lat = b.getDouble("LAT");
            curr_long = b.getDouble("LONG");
            address = b.getString("ADDRESS");
            start_time = b.getString("START_TIME");
            end_time = b.getString("END_TIME");
            renter_username = b.getString("RENTER");
            res_id = b.getLong("RES_ID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reservation_detail, container, false);

        _spacedetail_address=(TextView)v.findViewById(R.id.spacedetail_address);
        _start_time_label=(TextView)v.findViewById(R.id.start_time_label);
        _end_time_label=(TextView)v.findViewById(R.id.end_time_label);
        _renter_username_label=(TextView)v.findViewById(R.id.renter_username_label);
        _btn_review=(Button)v.findViewById(R.id.btn_review);
        _btn_cancel=(Button)v.findViewById(R.id.btn_cancel);

        _spacedetail_address.setText(address);
        _start_time_label.setText(start_time);
        _end_time_label.setText(end_time);
        _renter_username_label.setText(renter_username);

        mMapView = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);

//        // create marker
//        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude))
//                .title("Hello Maps").icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_pin));
//        // adding marker
//        googleMap.addMarker(marker);

        cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(curr_lat, curr_long)).zoom(12).build();

        _btn_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create custom dialog object
                final Dialog reviewDialog = new Dialog(getActivity());
                reviewDialog.setContentView(R.layout.dialog_review);
                reviewDialog.setTitle("Review");

                final RatingBar _ratingBar=(RatingBar)reviewDialog.findViewById(R.id.ratingBar);
//                Drawable drawable = _ratingBar.getProgressDrawable();
//                drawable.setColorFilter(Color.parseColor("#FFCC00"), PorterDuff.Mode.SRC_ATOP);
                final EditText _commentDialog=(EditText)reviewDialog.findViewById(R.id.commentDialog);
                Button _btn_confirm=(Button)reviewDialog.findViewById(R.id.btn_confirm);
                _btn_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Send to controller
                        AddReviewTask ART = new AddReviewTask(_ratingBar.getRating(),
                                                    _commentDialog.getText().toString());
                        ART.execute((Void) null);

                        // Close dialog
                        reviewDialog.dismiss();
                    }
                });
                reviewDialog.show();
            }
        });

        _btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call Client Controller for cancelling reservaiton
                RenterCancelTask RCT = new RenterCancelTask(res_id);
                RCT.execute((Void) null);
            }
        });

        return v;
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
        void returnToReservationsFragment();
    }

    @Override
    public void onMapReady(GoogleMap inGoogleMap) {
        googleMap = inGoogleMap;
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(curr_lat, curr_long)));
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }

    // Update reservation information
    public void setReservation(String in_address, String in_start_time, String in_end_time,
                               String in_renter_username, double in_lat, double in_long) {
        address = in_address;
        start_time = in_start_time;
        end_time = in_end_time;
        renter_username = in_renter_username;
        _spacedetail_address.setText(address);
        _start_time_label.setText(start_time);
        _end_time_label.setText(end_time);
        _renter_username_label.setText(renter_username);

        curr_lat = in_lat;
        curr_long = in_long;
        cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(curr_lat, curr_long)).zoom(12).build();
        if(googleMap != null) {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(curr_lat, curr_long)));
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        }
    }

    private class RenterCancelTask extends AsyncTask<Void, Void, Long> {
        long resID;

        RenterCancelTask(long resID){
            this.resID = resID;
        }

        @Override
        protected Long doInBackground(Void... params ){
            ClientController clientController = ClientController.getInstance();
            clientController.RenterCancel(resID);
            NetworkPackage NP = clientController.checkReceived();
            MyEntry<String, Serializable> entry = NP.getCommand();
            String key = entry.getKey();
            Object value = entry.getValue();
            if(key.equals("CANCELRESERVATION")){
                long reservationID = (long) value;
                return reservationID;
            } else if(key.equals("CANCELRESERVATIONFAIL")){
                Log.d("ReservationDetail ","CANCELRESERVATIONFAIL");
                return (long)-1;
            }
            Log.d("ReservationDetail ","END OF doInBackground");
            return (long)-1;
        }

        @Override
        protected void onPostExecute(Long resID) {
            if(resID >= 0){
                ClientController clientcontroller = ClientController.getInstance();
                for(int i = 0; i<clientcontroller.reservations.size(); i++){
                    if(clientcontroller.reservations.get(i).getReservationID()==resID){
                        clientcontroller.reservations.remove(i);
                    }
                }
                mListener.returnToReservationsFragment();
            } else{
                Toast.makeText(getContext(), "Error on cancel reservation!", Toast.LENGTH_SHORT).show();
                // back to reservation detail
            }
        }
    }

    private class AddReviewTask extends AsyncTask<Void, Void, Boolean> {
        private int mRating;
        private String mComment;
        ProgressDialog progressDialog;

        AddReviewTask(float inRating, String inComment){
            mRating = (int) inRating;
            mComment = inComment;

            Log.d("Reservation Detail: ", "AddReviewTask.mRating = "+inRating);
        }

        @Override
        protected void onPreExecute(){
            //Display a progress dialog
            progressDialog = new ProgressDialog(getActivity(),
                    R.style.AppTheme);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Adding...");
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params ){
            ClientController clientController = ClientController.getInstance();
            clientController.submitReview(res_id, mRating, mComment);
            NetworkPackage NP = clientController.checkReceived();
            MyEntry<String, Serializable> entry = NP.getCommand();
            String key = entry.getKey();
            if(key.equals("ADDREVIEW")){
                return true;
            }
            else if(key.equals("ADDREVIEWFAIL")){
                Log.d("Reservation Detail ","ADDREVIEWFAIL");
                return false;
            }
            Log.d("Reservation Detail ","doInBackground END");
            return false;
        }

        @Override
        protected void onPostExecute(Boolean ifAdded) {
            if(ifAdded){
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Review added!", Toast.LENGTH_SHORT).show();
            }
            else{
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Review cannot be added! Please try agian.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
