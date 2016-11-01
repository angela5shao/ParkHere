package csci310.parkhere.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;
import resource.Reservation;

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
    private Reservation mReservation;

    TextView _spacedetail_address, _start_time_label, _end_time_label, _renter_username_label;

    //*****************************************************************
    // latitude and longitude
    // NEED TO UPDATE
    private double curr_lat = 13.0294278;
    private double curr_long = 80.24667829999999;
    //*****************************************************************

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
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
                .target(new LatLng(13.0294278, 80.24667829999999)).zoom(12).build();

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
    }

    @Override
    public void onMapReady(GoogleMap inGoogleMap) {
        googleMap = inGoogleMap;
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(curr_lat, curr_long)));
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }

    public void setReservation(Reservation r) { mReservation = r; System.out.println("ResDetailFrag: setRes"); }
}
