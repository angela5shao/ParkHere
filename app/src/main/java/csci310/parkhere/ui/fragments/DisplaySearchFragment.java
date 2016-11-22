package csci310.parkhere.ui.fragments;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;
import csci310.parkhere.ui.activities.RenterActivity;
import csci310.parkhere.ui.adapters.CustomSearchListAdapter;
import csci310.parkhere.ui.helpers.DiplayListViewHelper;
import resource.MyEntry;
import resource.NetworkPackage;
import resource.ParkingSpot;
import resource.SearchResults;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DisplaySearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DisplaySearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DisplaySearchFragment extends Fragment implements AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static LatLng mSearchLoc;
    private static String mStartDate;
    private static String mStartTime;
    private static String mEndDate;
    private static String mEndTime;

    private OnFragmentInteractionListener mListener;

    Button _MapviewSwitch;
    Spinner _sortOptionSpinner;

    ListView _searchresultList;
    ArrayList<ParkingSpot> _spots;
    String[] _searchDescriptions;
    Uri[] uriArr;


    public DisplaySearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DisplaySearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DisplaySearchFragment newInstance(String param1, String param2) {
        DisplaySearchFragment fragment = new DisplaySearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_display_search, container, false);

        _MapviewSwitch = (Button) v.findViewById(R.id.MapviewSwitch);
        _MapviewSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Switch to MapView
                MapViewFragment mapViewFragment = new MapViewFragment();
                Bundle args = new Bundle();

                //*********************************************************
                args.putDouble("SEARCH_LAT", mSearchLoc.latitude);
                args.putDouble("SEARCH_LON", mSearchLoc.longitude);
                args.putString("START_DATE", mStartDate);
                args.putString("START_TIME", mStartTime);
                args.putString("END_DATE", mEndDate);
                args.putString("END_TIME", mEndTime);
                //*********************************************************

                mapViewFragment.setArguments(args);
                ((RenterActivity) getActivity()).switchToMapViewFrag(mapViewFragment);
            }
        });

        _sortOptionSpinner = (Spinner) v.findViewById(R.id.sortOptionSpinner);
        _sortOptionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ClientController controller = ClientController.getInstance();
                // Sort by distance
                if(position == 0) {
                    SearchResults newResult = controller.sortSearchResultByDist();
                    setSearchResultListview(newResult);
                }
                // Sort by price
                else if(position == 1) {
                    SearchResults newResult = controller.sortSearchResultByPrice();
                    setSearchResultListview(newResult);
                }
                // Sort by parking spot rating
                else if(position == 2) {
                    SearchResults newResult = controller.sortSearchResultBySpotRating();
                    setSearchResultListview(newResult);
                }
                // Sort by provider rating
                else if(position == 3){
                    SearchResults newResult = controller.sortSearchResultByProviderRating();
                    setSearchResultListview(newResult);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //
            }
        });

        _searchresultList = (ListView) v.findViewById(R.id.searchresultList);

        // Get list of search results
        ClientController controller = ClientController.getInstance();
        SearchResults result = controller.searchResults;

        _searchresultList.setOnItemClickListener(this);
        setSearchResultListview(result);

//        String[] resultList = new String[result.searchResultList.size()];
//        for(int i = 0; i < result.searchResultList.size(); i++) {
//            resultList[i] = result.searchResultList.get(i).getDescription();
//        }

//        _searchresultList.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, resultList));
//        DiplayListViewHelper.getListViewSize(_searchresultList);


//        _searchresultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
////                // ListView Clicked item index
////                int itemPosition     = position;
////                // ListView Clicked item value
////                String  itemValue    = (String) listView.getItemAtPosition(position);
////                // Show Alert
////                Toast.makeText(getApplicationContext(),
////                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
////                        .show();
//            }
//        });
        //***********************************************

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

    public void setSearchResultListview(SearchResults result) {
        Log.d("DisplaySearchFragment", "setSearchResultListview CALLED AFTER SORTING");
        _spots = result.searchResultList;

        if(_spots.isEmpty())
        {
            Toast.makeText(getContext(), "Cannot find space Please try again.", Toast.LENGTH_SHORT).show();
            return;
        }


        // Populate string array of descriptions to display
        _searchDescriptions = new String[result.searchResultList.size()];
        for(int i = 0; i < result.searchResultList.size(); i++) {
            ParkingSpot s = result.searchResultList.get(i);
            _searchDescriptions[i] = "Address: " + s.getStreetAddr() + "\n" + s.getDescription();
        }

        ArrayList<Long> idlist = new ArrayList<>();
        for(ParkingSpot p : result.searchResultList)
        {
            idlist.add(p.getParkingSpotID());
        }


        uriArr = new Uri[result.searchResultList.size()];
//        for(int i = 0; i < uriArr.length; i++)
//        {
//            uriArr[i] =  Uri.parse("http://sourcey.com/images/stock/salvador-dali-metamorphosis-of-narcissus.jpg");
//        }
////        var imageBitmap = GetImageBitmapFromUrl("http://xamarin.com/resources/design/home/devices.png");
//        // Setup list adapter using customSearchListAdapter
////        _searchresultList.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, resultList));
//        // TODO: pass array of image IDs (int)
//        CustomSearchListAdapter adapter = new CustomSearchListAdapter(getActivity(), _searchDescriptions, uriArr);
//        _searchresultList.setAdapter(adapter);
//        DiplayListViewHelper.getListViewSize(_searchresultList);


        LoadSpotThumbImageTask lstit = new LoadSpotThumbImageTask(idlist);
        lstit.execute();
    }
//


    private class LoadSpotThumbImageTask extends AsyncTask<Void, Void, ArrayList<String> > {
        private final ArrayList<Long> idList;
        private ProgressDialog progressDialog;


        LoadSpotThumbImageTask(ArrayList<Long> list){
            idList = list;
        }
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getContext(), R.style.AppTheme);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Searching...");
            progressDialog.show();
        }
        @Override
        protected ArrayList<String> doInBackground(Void... params ){
//            try {
//
            ClientController clientController = ClientController.getInstance();
            clientController.fetchThumbNailImg(idList);


            NetworkPackage NP = clientController.checkReceived();

            MyEntry<String, Serializable> entry = NP.getCommand();
            if(entry.getKey().equals("PARKINGSPOTTHUMBNAILURLS"))
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
            if(imagesURLs != null) {
                for(int i = 0; i < uriArr.length; i++)
                {

                    if(imagesURLs.get(i).length() > 0)
                    {
                        uriArr[i] = Uri.parse(imagesURLs.get(i));
                    }
                    else
                    {
                        uriArr[i] =  Uri.parse("http://sourcey.com/images/stock/salvador-dali-metamorphosis-of-narcissus.jpg");

                    }
                }
//        var imageBitmap = GetImageBitmapFromUrl("http://xamarin.com/resources/design/home/devices.png");
                // Setup list adapter using customSearchListAdapter
//        _searchresultList.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, resultList));
                // TODO: pass array of image IDs (int)
                CustomSearchListAdapter adapter = new CustomSearchListAdapter(getActivity(), _searchDescriptions, uriArr);
                _searchresultList.setAdapter(adapter);
                DiplayListViewHelper.getListViewSize(_searchresultList);
                progressDialog.dismiss();

            } else{
                // TODO: WHAT TO DISPLAY WHEN NO IMAGE
                //
                progressDialog.dismiss();

                Toast.makeText(getContext(), "Cannot find images", Toast.LENGTH_SHORT).show();

            }
        }

    }




    public void setSearchResultListview(String[] inSearchResults, LatLng inSearchLoc, String startDate, String startTime, String endDate, String endTime) {
//        _searchresultList.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, inSearchResults));
//        DiplayListViewHelper.getListViewSize(_searchresultList);
        Log.d("setSearchResultListview", startDate + " " + startTime + " " + endDate + " " + endTime);

        mSearchLoc = inSearchLoc;
        mStartDate = startDate;
        mStartTime = startTime;
        mEndDate = endDate;
        mEndTime = endTime;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void onSearchSpaceSelected(int position, String mStartDate, String mStartTime, String mEndDate, String mEndTime);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
//        String selectedItem= itemname[+position];
        Log.d("time debugging", mStartDate+" "+mStartTime+" "+mEndDate+" "+mEndTime);
        mListener.onSearchSpaceSelected(position, mStartDate, mStartTime, mEndDate, mEndTime);
    }
}