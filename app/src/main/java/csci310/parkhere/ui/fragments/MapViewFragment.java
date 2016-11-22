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
import csci310.parkhere.controller.ClientController;
import csci310.parkhere.ui.activities.OnInfoWindowElemTouchListener;
import csci310.parkhere.ui.activities.RenterActivity;
import csci310.parkhere.ui.layout.MapWrapperLayout;
import resource.ParkingSpot;
import resource.SearchResults;

public class MapViewFragment extends Fragment implements OnMapReadyCallback {
    private double searchLat, searchLon;
    private String startDate, startTime, endDate, endTime;
    private ArrayList<ParkingSpot> _spots;

    Button _ListviewSwitch;
    private ViewGroup infoWindow;
    private TextView infoTitle;
    private TextView infoSnippet;
    private Button infoButton;
    private OnInfoWindowElemTouchListener infoButtonListener;

    MapWrapperLayout mapWrapperLayout;

    SupportMapFragment mMapView;
    private GoogleMap googleMap;
    CameraPosition cameraPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            searchLat = getArguments().getDouble("SEARCH_LAT");
            searchLon = getArguments().getDouble("SEARCH_LON");
            startDate = getArguments().getString("START_DATE");
            startTime = getArguments().getString("START_TIME");
            endDate = getArguments().getString("END_DATE");
            endTime = getArguments().getString("END_TIME");

            ClientController controller = ClientController.getInstance();
            _spots = controller.searchResults.searchResultList;

            Log.d("MapViewFrag", " SEARCH_LAT = "+searchLat);
            Log.d("MapViewFrag", " SEARCH_LON = "+searchLon);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_mapview, container, false);

        _ListviewSwitch = (Button) v.findViewById(R.id.ListviewSwitch);
        _ListviewSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Switch to ListView
                ClientController clientController = ClientController.getInstance();
                SearchResults results = clientController.searchResults;
                ((RenterActivity) getActivity()).displaySearchResult(results, new LatLng(searchLat, searchLon),
                        startDate, startTime, endDate, endTime);
            }
        });

        mMapView = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
//        if (mMapView == null) {
//            mMapView = SupportMapFragment.newInstance();
//            getChildFragmentManager().beginTransaction().replace(R.id.map, mMapView).commit();
//        }
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately
        try {
//            MapsInitializer.initialize(getActivity().getApplicationContext());
            MapsInitializer.initialize(v.getContext());

        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);

        mapWrapperLayout = (MapWrapperLayout) v.findViewById(R.id.map_relative_layout);

        this.infoWindow = (ViewGroup) inflater.inflate(R.layout.info_window, null);
        this.infoTitle = (TextView)infoWindow.findViewById(R.id.title);
        this.infoSnippet = (TextView)infoWindow.findViewById(R.id.snippet);
        this.infoButton = (Button)infoWindow.findViewById(R.id.button);

        this.infoButtonListener = new OnInfoWindowElemTouchListener(infoButton){
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                // TODO: Here we can perform some action triggered after clicking the button
                Log.d("MapViewFrag ", "marker get @ pos "+((int) marker.getTag()));
                ((RenterActivity) getActivity()).onSearchSpaceSelected((int) marker.getTag(), startDate, startTime, endDate, endTime);

//                Toast.makeText(getActivity(), marker.getTitle() + "'s button clicked!", Toast.LENGTH_SHORT).show();
            }
        };
        this.infoButton.setOnTouchListener(infoButtonListener);

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

        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

            mapWrapperLayout.init(googleMap, getPixelsFromDp(getContext(), 39 + 20));

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

        addMarkers(_spots);
    }

    public void addMarkers(ArrayList<ParkingSpot> spots) {
        if(googleMap != null) {
            for (int i=0; i<spots.size(); ++i) {
                Marker marker = googleMap.addMarker(new MarkerOptions()
                        .title(("$" + Double.toString(spots.get(i).search_price) + "/hr"))
                        .snippet(spots.get(i).getStreetAddr())
                        .position(new LatLng(spots.get(i).getLat(), spots.get(i).getLon())));
                marker.setTag(i);
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
