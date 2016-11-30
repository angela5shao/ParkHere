package csci310.parkhere.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;
import csci310.parkhere.ui.activities.GuestActivity;
import csci310.parkhere.ui.activities.OnInfoWindowElemTouchListener;
import csci310.parkhere.ui.activities.RenterActivity;
import csci310.parkhere.ui.layout.MapWrapperLayout;
import resource.ParkingSpot;
import resource.SearchResults;

public class MapViewFragment extends Fragment implements OnMapReadyCallback {
    // in case of several markers with the same location
    private static final float COORDINATE_OFFSET = 0.0001f;
    // Visualize freq circle radius
    private double mapCircleRadius = 40.0;
    private float curr_zoom = 16;
    private boolean isShowCircle = false;
    // Store all added circles
    private List<Circle> mapCircles = new ArrayList<Circle>();
    // HashMap of marker identifier and its location as a string
    private HashMap<String, String> markerLocation = new HashMap<String, String>();
    // HashMap of parking spot ID and its amount of times of booking
    private HashMap<Long, Integer> spotBookingAmount = new HashMap<Long, Integer>();

    private double searchLat, searchLon;
    private String startDate, startTime, endDate, endTime;
    private ArrayList<ParkingSpot> _spots;

    Button _ListviewSwitch, _popButton;
    private ViewGroup infoWindow;
    private TextView infoTitle;
    private TextView infoSnippet;
//    private Button infoButton;
    private OnInfoWindowElemTouchListener infoListener;

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

            spotBookingAmount = controller.searchResultsFreq;
            Log.d("MapViewFrag", " spotBookingAmount.size = "+spotBookingAmount.size());

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
                Activity activity = getActivity();
                if(activity instanceof RenterActivity) {
                    ((RenterActivity) getActivity()).displaySearchResult(results, new LatLng(searchLat, searchLon),
                            startDate, startTime, endDate, endTime);
                }else if(activity instanceof GuestActivity)
                {
                    ((GuestActivity)getActivity()).displaySearchResult(results, new LatLng(searchLat, searchLon),
                        startDate, startTime, endDate, endTime);
                }
            }
        });

        mMapView = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
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
//        this.infoButton = (Button)infoWindow.findViewById(R.id.button);

        this.infoListener = new OnInfoWindowElemTouchListener(infoWindow) { //infoButton){
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                // TODO: Here we can perform some action triggered after clicking the button
                Log.d("MapViewFrag ", "marker get @ pos "+((int) marker.getTag()));
                ((RenterActivity) getActivity()).onSearchSpaceSelected((int) marker.getTag(), startDate, startTime, endDate, endTime);

//                Toast.makeText(getActivity(), marker.getTitle() + "'s button clicked!", Toast.LENGTH_SHORT).show();
            }
        };
        this.infoWindow.setOnTouchListener(infoListener);
//        this.infoButton.setOnTouchListener(infoListener);

        _popButton = (Button) v.findViewById(R.id.popButton);
        _popButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Visualize on map
                if(false) {
                    // Remove visuals
                }
                else if (_spots != null) {
                    isShowCircle = !isShowCircle;

                    MapLoadCirclesTask MLCT = new MapLoadCirclesTask(googleMap, _spots, spotBookingAmount);
                    MLCT.execute((Void) null);
                }
            }
        });

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
        if(mMapView != null) mMapView.onDestroy();
        super.onDestroy();
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
                .target(new LatLng(searchLat, searchLon)).zoom(curr_zoom).build();

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
                infoListener.setMarker(marker);

                // We must call this to set the current marker and infoWindow references
                // to the MapWrapperLayout
                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                return infoWindow;
            }
        });

        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {

                CameraPosition cameraPosition = googleMap.getCameraPosition();
                Log.d("MapView Frag ", "new mapCircleRadius with cameraPosition.zoom = " + (double) cameraPosition.zoom);
                mapCircleRadius = (mapCircleRadius / curr_zoom) * (double) cameraPosition.zoom;
                Log.d("MapView Frag ", "new mapCircleRadius = " + mapCircleRadius);

                if(cameraPosition.zoom != curr_zoom) {
                    //***************************************************************************************
                    MapLoadCirclesTask MLCT = new MapLoadCirclesTask(googleMap, _spots, spotBookingAmount);
                    MLCT.execute((Void) null);
                    //***************************************************************************************
                }

                curr_zoom = cameraPosition.zoom;
            }
        });

        addMarkers(_spots);
    }

    public void addMarkers(ArrayList<ParkingSpot> spots) {
        if(googleMap != null) {
            markerLocation.clear();
            for (int i=0; i<spots.size(); ++i) {
                Double[] coord = coordinateForMarker(i, spots.get(i).getLat(), spots.get(i).getLon());
                Marker marker = googleMap.addMarker(new MarkerOptions()
                        .title(("$" + Double.toString(spots.get(i).search_price) + "/hr"))
                        .snippet(spots.get(i).getStreetAddr())
                        .position(new LatLng(coord[0], coord[1])));
                marker.setTag(i);
            }
        }
    }

    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    // Source: http://stackoverflow.com/questions/19704124/how-to-handle-multiple-markers-on-google-maps-with-same-location
    // Check if any marker is displayed on given coordinate. If yes then decide
    // another appropriate coordinate to display this marker. It returns an
    // array with latitude(at index 0) and longitude(at index 1).
    private Double[] coordinateForMarker(int id, double latitude, double longitude) {
        Double[] location = new Double[2];

        for (int i = 0; i <= _spots.size(); i++) {
            if (mapAlreadyHasMarkerForLocation((latitude + i
                    * COORDINATE_OFFSET)
                    + "," + (longitude + i * COORDINATE_OFFSET))) {

                // If i = 0 then below if condition is same as upper one. Hence, no need to execute below if condition.
                if (i == 0)
                    continue;

                if (mapAlreadyHasMarkerForLocation((latitude - i
                        * COORDINATE_OFFSET)
                        + "," + (longitude - i * COORDINATE_OFFSET))) {

                    continue;

                } else {
                    location[0] = latitude - (i * COORDINATE_OFFSET);
                    location[1] = longitude - (i * COORDINATE_OFFSET);
                    break;
                }

            } else {
                location[0] = latitude + (i * COORDINATE_OFFSET);
                location[1] = longitude + (i * COORDINATE_OFFSET);
                break;
            }
        }

        // keep track of Hashmap
        markerLocation.put(id + "", location[0] + "," + location[1]);

        return location;
    }

    // Return whether marker with same location is already on map
    private boolean mapAlreadyHasMarkerForLocation(String location) {
        return (markerLocation.containsValue(location));
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

    private class MapLoadCirclesTask extends AsyncTask<Void, Void, HashMap<ParkingSpot, Integer> > {
        private GoogleMap map;
        private ArrayList<ParkingSpot> spots;
        private HashMap<Long, Integer> freqs;

        MapLoadCirclesTask(GoogleMap map, ArrayList<ParkingSpot> spots, HashMap<Long, Integer> freqs){
            this.map = map;
            this.spots = spots;
            this.freqs = freqs;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Clear draws on map
            for (Circle circle : mapCircles) {
                circle.remove();
            }
            mapCircles.clear();
        }

        @Override
        protected HashMap<ParkingSpot, Integer> doInBackground(Void... params){
            int total_bookings = 0;
            for(int bookings : freqs.values()) {
                total_bookings += bookings;
            }
            Log.d("MapLoadCirclesTask ", "total_bookings = " + total_bookings);

            if(total_bookings == 0) return null;

            HashMap<ParkingSpot, Integer> spotsWithFreq = new HashMap<ParkingSpot, Integer>();
//            String alpha = "ff";
            int bgColor = ContextCompat.getColor(getContext(), R.color.colorAccent);
            String red = Integer.toHexString(Color.red(bgColor));
            String green = Integer.toHexString(Color.green(bgColor));
            String blue = Integer.toHexString(Color.blue(bgColor));

            for(ParkingSpot spot : spots) {
                int amount_of_bookings = freqs.get(spot.getParkingSpotID());
                Log.d("MapLoadCirclesTask ", "amount_of_bookings for " + spot.getParkingSpotID() + " = " + amount_of_bookings);

                // Fill color of the circle
                // 0x represents, this is an hexadecimal code
                // 55 represents percentage of transparency. For 100% transparency, specify 00.
                // For 0% transparency ( ie, opaque ) , specify ff
                // The remaining 6 characters(00ff00) specify the fill color
                // eg. 0x5500ff00
                int tp = (amount_of_bookings * 100)/total_bookings;
                Log.d("MapLoadCirclesTask ", "transparency for " + spot.getParkingSpotID() + " = " + tp);

//                if(tp == 100) alpha = "00";
//                else if(tp >0 && tp < 10) alpha = "0" + tp;
//                else if(tp != 0) alpha = Integer.toString(tp);

                String alpha = Integer.toHexString(tp);
                Log.d("MapLoadCirclesTask ", "alpha for " + spot.getParkingSpotID() + " = " + alpha);

                String hex = alpha + red + green + blue;
                Log.d("MapLoadCirclesTask ", "hex for " + spot.getParkingSpotID() + " = " + hex);

                int color = Integer.parseInt(hex, 16);

                spotsWithFreq.put(spot, color);
            }

            return spotsWithFreq;
        }

        @Override
        protected void onPostExecute(HashMap<ParkingSpot, Integer> results) {
            if (results != null && isShowCircle == true) {
                for (ParkingSpot spot : results.keySet()) {
                    Log.d("MapLoadCirclesTask ", spot.getParkingSpotID() + "- results.get(spot) = " + results.get(spot));
                    Log.d("MapLoadCirclesTask ", "mapCircleRadius = " + mapCircleRadius);

                    float radius;
                    if(Color.alpha(results.get(spot)) == 100) radius = (float) mapCircleRadius;
                    else {
                        radius = (float)( mapCircleRadius / ( 100 - Color.alpha(results.get(spot)) ) ) * 50;
                    }
                    Log.d("MapLoadCirclesTask ", "Color.alpha(results.get(spot)) = " + Color.alpha(results.get(spot)));
                    Log.d("MapLoadCirclesTask ", "radius = " + radius);
                    Log.d("MapLoadCirclesTask ", " CIRCLE spot.getLat() = " + spot.getLat() + "spot.getLon()" + spot.getLon());

                    Circle circle = map.addCircle(new CircleOptions()
                            .center(new LatLng(spot.getLat(), spot.getLon()))
                            .radius(radius)
                            .strokeColor(Color.TRANSPARENT)
                            .fillColor(results.get(spot)));
                    mapCircles.add(circle);
                }
            }
        }
    }
}
