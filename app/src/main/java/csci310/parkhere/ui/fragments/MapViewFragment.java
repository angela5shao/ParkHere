package csci310.parkhere.ui.fragments;

import android.content.Context;
import android.net.Uri;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import csci310.parkhere.R;
import csci310.parkhere.ui.activities.OnInfoWindowElemTouchListener;
import csci310.parkhere.ui.activities.RenterActivity;
import csci310.parkhere.ui.layout.MapWrapperLayout;
import resource.ParkingSpot;

public class MapViewFragment extends Fragment implements OnMapReadyCallback {
    private double searchLat, searchLon;
    private ArrayList<ParkingSpot> _spots;

    Button _ListviewSwitch;
    private ViewGroup infoWindow;
    private TextView infoTitle;
    private TextView infoSnippet;
    private Button infoButton;
    private OnInfoWindowElemTouchListener infoButtonListener;

    SupportMapFragment mMapView;
    private GoogleMap googleMap;
    CameraPosition cameraPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            searchLat = getArguments().getDouble("SEARCH_LAT");
            searchLon = getArguments().getDouble("SEARCH_LON");

            Log.d("MapViewFrag", " SEARCH_LAT = "+searchLat);
            Log.d("MapViewFrag", " SEARCH_LON = "+searchLon);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mapview, container, false);

        _ListviewSwitch = (Button) v.findViewById(R.id.ListviewSwitch);
        _ListviewSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Switch to ListView
                DisplaySearchFragment displaySearchFragment = new DisplaySearchFragment();
                Bundle args = new Bundle();
//                args.putString("USERNAME", mParam1);
//                args.putString("PASSWORD", mParam2);
//                args.putString("USERLICENSE", mParam3);
//                args.putString("USERPLATE", mParam4);
//                args.putString("PHONE", mParam5);
                displaySearchFragment.setArguments(args);
                ((RenterActivity) getActivity()).switchToListViewFrag(displaySearchFragment);
            }
        });

        mMapView = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map));
        mMapView.onCreate(savedInstanceState);

        final MapWrapperLayout mapWrapperLayout = (MapWrapperLayout)v.findViewById(R.id.map_relative_layout);
        mapWrapperLayout.init(googleMap, getPixelsFromDp(getContext(), 39 + 20));

        this.infoWindow = (ViewGroup)inflater.inflate(R.layout.info_window, null);
        this.infoTitle = (TextView)infoWindow.findViewById(R.id.title);
        this.infoSnippet = (TextView)infoWindow.findViewById(R.id.snippet);
        this.infoButton = (Button)infoWindow.findViewById(R.id.button);

        this.infoButtonListener = new OnInfoWindowElemTouchListener(infoButton){
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                // TODO: Here we can perform some action triggered after clicking the button

                Toast.makeText(getActivity(), marker.getTitle() + "'s button clicked!", Toast.LENGTH_SHORT).show();
            }
        };
        this.infoButton.setOnTouchListener(infoButtonListener);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);

        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Setting up the infoWindow with current's marker info
                infoTitle.setText(marker.getTitle());
                infoSnippet.setText(marker.getSnippet());
                infoButtonListener.setMarker(marker);

                // We must call this to set the current marker and infoWindow references
                // to the MapWrapperLayout
                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                return infoWindow;
            }
        });

//        addMarkers(_spots);

//        if ( ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED ) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
//                    LocationService.MY_PERMISSION_ACCESS_COURSE_LOCATION);
//        }
//
//        mMapView.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap mMap) {
//                googleMap = mMap;
//
//                // For showing a move to my location button
//                googleMap.setMyLocationEnabled(true);
//
//                // For dropping a marker at a point on the Map
//                LatLng sydney = new LatLng(-34, 151);
//                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));
//
//                // For zooming automatically to the location of the marker
//                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
//                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//            }
//        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap inGoogleMap) {

        Log.d("ONMAPREADY", "inGoogleMap");

        googleMap = inGoogleMap;

        cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(searchLat, searchLon)).zoom(12).build();

        addMarkers(_spots);

        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }

    public void addMarkers(ArrayList<ParkingSpot> spots) {
        if(googleMap != null) {
            for (ParkingSpot spot : spots) {

                googleMap.addMarker(new MarkerOptions()
                        .title(Double.toString(spot.search_price))
                        .snippet(spot.getStreetAddr())
                        .position(new LatLng(spot.getLat(), spot.getLon())));
            }
        }
    }

    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
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
}
