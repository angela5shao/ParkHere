//package csci310.parkhere.ui;
//
//
//import android.app.Activity;
//import android.app.DatePickerDialog;
//import android.app.ProgressDialog;
//import android.app.TimePickerDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.TimePicker;
//import android.widget.Toast;
//
//import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
//import com.google.android.gms.common.GooglePlayServicesRepairableException;
//import com.google.android.gms.common.api.Status;
//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.ui.PlaceAutocomplete;
//import com.google.android.gms.maps.model.LatLng;
//
//import java.io.IOException;
//import java.io.Serializable;
//import java.util.Calendar;
//
//import csci310.parkhere.R;
//import csci310.parkhere.controller.ClientController;
//import resource.MyEntry;
//import resource.NetworkPackage;
//import resource.ParkingSpot;
//import resource.SearchResults;
//
///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link SearchFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link SearchFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class SearchFragment extends Fragment {
//    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//    private SearchSpaceTask SSTask;
//
//    private static final String TAG = "!!!!!!!!!!!!!!! "; // EDIT/DELETE LATER!
//    Button _btn_add_address, btnStartDatePicker, btnStartTimePicker, btnEndDatePicker, btnEndTimePicker, _btn_confirm;
//    EditText txtStartDate, txtStartTime, txtEndDate, txtEndTime;
//    TextView _addressText;
//    LinearLayout _addressSearchLayout, _latlongSearchLayout;
//    Spinner  _distSpinner, _cartypeSpinner;
//
//    private int startYear, startMonth, startDay, startHour, startMinute,
//                    endYear, endMonth, endDay, endHour, endMinute;
//
//    private Calendar startDate = Calendar.getInstance();
//    private Calendar endDate = Calendar.getInstance();
//    LatLng curr_location;
//    String curr_cartype, curr_dist;
//
//    private OnFragmentInteractionListener mListener;
//
//    public SearchFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment SearchFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static SearchFragment newInstance(String param1, String param2) {
//        SearchFragment fragment = new SearchFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//
//        setHasOptionsMenu(true);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.fragment_search, container, false);
//
//        _addressText = (TextView)v.findViewById(R.id.addressText);
//        _addressSearchLayout = (LinearLayout)v.findViewById(R.id.addressSearchLayout);
//        _latlongSearchLayout = (LinearLayout)v.findViewById(R.id.latlongSearchLayout);
//
//        Spinner _usertypeSpinner = (Spinner)v.findViewById(R.id.usertypeSpinner);
//        _usertypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                if(pos == 1) {
//                    _addressSearchLayout.setVisibility(View.GONE);
//                    _latlongSearchLayout.setVisibility(View.VISIBLE);
//                }
//                else {
//                    _latlongSearchLayout.setVisibility(View.GONE);
//                    _addressSearchLayout.setVisibility(View.VISIBLE);
//                }
//            }
//
//            public void onNothingSelected(AdapterView<?> parent) { }
//        });
//
//        _btn_add_address = (Button)v.findViewById(R.id.btn_add_address);
//        _btn_add_address.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                try {
//                    Intent intent =
//                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
//                                    .build(getActivity());
//                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
//                } catch (GooglePlayServicesRepairableException e) {
//                    // TODO: Handle the error.
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    // TODO: Handle the error.
//                }
//            }
//        });
//        // Inflate the layout for this fragment
//
//        btnStartDatePicker=(Button)v.findViewById(R.id.btn_start_date);
//        btnStartTimePicker=(Button)v.findViewById(R.id.btn_start_time);
//        txtStartDate=(EditText)v.findViewById(R.id.in_start_date);
//        txtStartTime=(EditText)v.findViewById(R.id.in_start_time);
//
//        btnEndDatePicker=(Button)v.findViewById(R.id.btn_end_date);
//        btnEndTimePicker=(Button)v.findViewById(R.id.btn_end_time);
//        txtEndDate=(EditText)v.findViewById(R.id.in_end_date);
//        txtEndTime=(EditText)v.findViewById(R.id.in_end_time);
//
//        btnStartDatePicker.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                // Get Current Date
//                final Calendar c = Calendar.getInstance();
//                startYear = c.get(Calendar.YEAR);
//                startMonth = c.get(Calendar.MONTH);
//                startDay = c.get(Calendar.DAY_OF_MONTH);
//
//
//                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(),
//                        new DatePickerDialog.OnDateSetListener() {
//
//                            @Override
//                            public void onDateSet(DatePicker view, int year,
//                                                  int monthOfYear, int dayOfMonth) {
//
//                                txtStartDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//                                startYear = year;
//                                startMonth = monthOfYear;
//                                startDay = dayOfMonth;
//                                startDate.set(year, monthOfYear, dayOfMonth);
//
//                            }
//                        }, startYear, startMonth, startDay);
//                datePickerDialog.show();
//            }
//        });
//        btnStartTimePicker.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                // Get Current Time
//                final Calendar c = Calendar.getInstance();
//                startHour = c.get(Calendar.HOUR_OF_DAY);
//                startMinute = c.get(Calendar.MINUTE);
//
//                // Launch Time Picker Dialog
//                TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(),
//                        new TimePickerDialog.OnTimeSetListener() {
//
//                            @Override
//                            public void onTimeSet(TimePicker view, int hourOfDay,
//                                                  int minute) {
//
//                                txtStartTime.setText(hourOfDay + ":" + minute);
//                                startHour = hourOfDay;
//                                startMinute = minute;
//                            }
//                        }, startHour, startMinute, false);
//                timePickerDialog.show();
//            }
//        });
//
//        btnEndDatePicker.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                // Get Current Date
//                final Calendar c = Calendar.getInstance();
//                endYear = c.get(Calendar.YEAR);
//                endMonth = c.get(Calendar.MONTH);
//                endDay = c.get(Calendar.DAY_OF_MONTH);
//
//
//                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(),
//                        new DatePickerDialog.OnDateSetListener() {
//
//                            @Override
//                            public void onDateSet(DatePicker view, int year,
//                                                  int monthOfYear, int dayOfMonth) {
//
//                                txtEndDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//                                endYear = year;
//                                endMonth = monthOfYear;
//                                endDay = dayOfMonth;
//                                endDate.set(year, monthOfYear, dayOfMonth);
//
//                            }
//                        }, endYear, endMonth, endDay);
//                datePickerDialog.getDatePicker().setMinDate(startDate.getTimeInMillis());
//                datePickerDialog.show();
//            }
//        });
//        btnEndTimePicker.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                // Get Current Time
//                final Calendar c = Calendar.getInstance();
//                endHour = c.get(Calendar.HOUR_OF_DAY);
//                endMinute = c.get(Calendar.MINUTE);
//
//                // Launch Time Picker Dialog
//                TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(),
//                        new TimePickerDialog.OnTimeSetListener() {
//
//                            @Override
//                            public void onTimeSet(TimePicker view, int hourOfDay,
//                                                  int minute) {
//
//                                txtEndTime.setText(hourOfDay + ":" + minute);
//                            }
//                        }, endHour, endMinute, false);
//                timePickerDialog.show();
//            }
//        });
//
//        _distSpinner = (Spinner)v.findViewById(R.id.distSpinner);
//        _cartypeSpinner = (Spinner)v.findViewById(R.id.cartypeSpinner);
//
//        _btn_confirm=(Button)v.findViewById(R.id.btn_confirm);
//        _btn_confirm.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                curr_dist = _distSpinner.getSelectedItem().toString();
//                curr_cartype = _cartypeSpinner.getSelectedItem().toString();
//
//                ClientController clientController = ClientController.getInstance();
//                SSTask = new SearchSpaceTask(curr_location, startMonth+"-"+startDay+"-"+startYear, startHour+"-"+startMinute,
//                        endMonth+"-"+endDay+"-"+endYear, endHour+"-"+endMinute, curr_cartype, curr_dist);
//                SSTask.execute((Void)null);
//            }
//        });
//
//        return v;
//    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p/>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
////        switch (item.getItemId()) {
////            case R.id.action_save : {
////                Log.i(TAG, "Save from fragment");
////                return true;
////            }
////        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.d("AUTOCOMPLETE", "SEARCH");
//        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
//            if (resultCode == Activity.RESULT_OK) {
//                Place place = PlaceAutocomplete.getPlace(getContext(), data);
//                Log.i(TAG, "Place: " + place.getName());
//                Log.i(TAG, "Place LatLng: " + place.getLatLng().toString());
//
//                _addressText.setText(place.getAddress());
//                curr_location = place.getLatLng();
//
//
//            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
//                Status status = PlaceAutocomplete.getStatus(getContext(), data);
//                // TODO: Handle the error.
//                Log.i(TAG, status.getStatusMessage());
//
//            } else if (resultCode == Activity.RESULT_CANCELED) {
//                // The user canceled the operation.
//            }
//        }
//    }
//    private class SearchSpaceTask extends AsyncTask<Void, Void, SearchResults> {
////        private final String mAddressText;
////        private final String mDescrip;
////        private final String mCarType;
////        private final int mCancelPolicy;
//        private final LatLng mlocation;
//        private final String mStartDate;
//        private final String mStartTime;
//        private final String mEndDate;
//        private final String mEndTime;
//        private final String mCarType;
//        private final String mDistance;
//
//        SearchSpaceTask(LatLng location, String startDate, String startTime, String endDate, String endTime, String carType, String distance){
//            mlocation = location;
//            mStartDate = startDate;
//            mStartTime = startTime;
//            mEndDate = endDate;
//            mEndTime = endTime;
//            mCarType = carType;
//            mDistance = distance;
//            doInBackground((Void) null);
//
//            System.out.println(mCarType);
//        }
////        @Override
////        protected void onPreExecute(){
////            //Display a progress dialog
////            progressDialog = new ProgressDialog(getContext(),
////                    R.style.AppTheme);
////            progressDialog.setIndeterminate(true);
////            progressDialog.setMessage("Adding...");
////            progressDialog.show();
////        }
//        @Override
//        protected SearchResults doInBackground(Void... params){
//            ClientController clientController = ClientController.getInstance();
//            try {
//
//                Log.d("SEARCH", mStartDate.toString()  + " " + mEndDate.toString());
//                clientController.search(mlocation, mStartDate, mStartTime, mEndDate,mEndTime, mCarType, mDistance);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            NetworkPackage NP = clientController.checkReceived();
//            MyEntry<String, Serializable> entry = NP.getCommand();
//            String key = entry.getKey();
//            Object value = entry.getValue();
//
//            if(key.equals("SEARCH_RESULT")) {
//                SearchResults result = (SearchResults) value;
////                Log.d("Results", result.searchResultList.get(0).getStreetAddr());
//                clientController.toDispaySearch = true;
//                //controller.updateActivity();
//                clientController.searchResults = result;
//                Log.d("SEARCH_RESULT", "Size " + String.valueOf(result.searchResultList.size()));
//                return result;
//            }
//            else {
//                return null;
//            }
//        }
//        @Override
//        protected void onPostExecute(SearchResults result) {
//                if(result!=null) {
//                    ((RenterActivity) getActivity()).displaySearchResult(result);
//                }
//                else{
//                    Toast.makeText(getContext(), "Error to find space! Please try again.", Toast.LENGTH_SHORT).show();
//                    // back to add space?
//                }
//
//        }
//    }
//}

package csci310.parkhere.ui.fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;
import csci310.parkhere.ui.activities.RenterActivity;
import resource.MyEntry;
import resource.NetworkPackage;
import resource.SearchResults;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SearchSpaceTask SSTask;

    private static final String TAG = "!!!!!!!!!!!!!!! "; // EDIT/DELETE LATER!
    Button _btn_add_address, btnStartDatePicker, btnStartTimePicker, btnEndDatePicker, btnEndTimePicker, _btn_confirm;
    EditText _in_lat, _in_long, txtStartDate, txtStartTime, txtEndDate, txtEndTime;
    TextView _addressText;
    LinearLayout _addressSearchLayout, _latlongSearchLayout;
    Spinner  _distSpinner, _cartypeSpinner;

    private int startYear, startMonth, startDay, startHour, startMinute,
            endYear, endMonth, endDay, endHour, endMinute = -1;

    private Calendar startDate = Calendar.getInstance();
    private Calendar endDate = Calendar.getInstance();
    LatLng curr_location;// = new LatLng(37.8719, 122.2585);
    String curr_cartype, curr_dist;

    private OnFragmentInteractionListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        _addressText = (TextView)v.findViewById(R.id.addressText);
        _addressSearchLayout = (LinearLayout)v.findViewById(R.id.addressSearchLayout);
        _latlongSearchLayout = (LinearLayout)v.findViewById(R.id.latlongSearchLayout);
        _in_lat = (EditText)v.findViewById(R.id.in_lat);
        _in_long = (EditText)v.findViewById(R.id.in_long);

        Spinner _usertypeSpinner = (Spinner)v.findViewById(R.id.usertypeSpinner);
        _usertypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if(pos == 1) {
                    _addressSearchLayout.setVisibility(View.GONE);
                    _latlongSearchLayout.setVisibility(View.VISIBLE);
                }
                else {
                    _latlongSearchLayout.setVisibility(View.GONE);
                    _addressSearchLayout.setVisibility(View.VISIBLE);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) { }
        });

        _btn_add_address = (Button)v.findViewById(R.id.btn_add_address);
        _btn_add_address.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().setCountry("US").build();

                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .setFilter(typeFilter)
                                    .build(getActivity());
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });
        // Inflate the layout for this fragment

        btnStartDatePicker=(Button)v.findViewById(R.id.btn_start_date);
        btnStartTimePicker=(Button)v.findViewById(R.id.btn_start_time);
        txtStartDate=(EditText)v.findViewById(R.id.in_start_date);
        txtStartTime=(EditText)v.findViewById(R.id.in_start_time);

        btnEndDatePicker=(Button)v.findViewById(R.id.btn_end_date);
        btnEndTimePicker=(Button)v.findViewById(R.id.btn_end_time);
        txtEndDate=(EditText)v.findViewById(R.id.in_end_date);
        txtEndTime=(EditText)v.findViewById(R.id.in_end_time);

        btnStartDatePicker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                startYear = c.get(Calendar.YEAR);
                startMonth = c.get(Calendar.MONTH);
                startDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                txtStartDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                startYear = year;
                                startMonth = monthOfYear;
                                startDay = dayOfMonth;
                                startDate.set(year, monthOfYear, dayOfMonth);

                            }
                        }, startYear, startMonth, startDay);
                datePickerDialog.show();
            }
        });
        btnStartTimePicker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                startHour = c.get(Calendar.HOUR_OF_DAY);
                startMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txtStartTime.setText(hourOfDay + ":" + minute);
                                startHour = hourOfDay;
                                startMinute = minute;
                            }
                        }, startHour, startMinute, false);
                timePickerDialog.show();
            }
        });

        btnEndDatePicker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                endYear = c.get(Calendar.YEAR);
                endMonth = c.get(Calendar.MONTH);
                endDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                txtEndDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                endYear = year;
                                endMonth = monthOfYear;
                                endDay = dayOfMonth;
                                endDate.set(year, monthOfYear, dayOfMonth);

                            }
                        }, endYear, endMonth, endDay);
                datePickerDialog.getDatePicker().setMinDate(startDate.getTimeInMillis());
                datePickerDialog.show();
            }
        });
        btnEndTimePicker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                endHour = c.get(Calendar.HOUR_OF_DAY);
                endMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txtEndTime.setText(hourOfDay + ":" + minute);
                                endHour = hourOfDay;
                                endMinute = minute;
                            }
                        }, endHour, endMinute, false);
                timePickerDialog.show();
            }
        });

        _distSpinner = (Spinner)v.findViewById(R.id.distSpinner);
        _cartypeSpinner = (Spinner)v.findViewById(R.id.cartypeSpinner);

        _btn_confirm=(Button)v.findViewById(R.id.btn_confirm);
        _btn_confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                curr_dist = _distSpinner.getSelectedItem().toString();
                curr_cartype = _cartypeSpinner.getSelectedItem().toString();

                if(_latlongSearchLayout.getVisibility()==View.VISIBLE) {
                    Double mLat = Double.parseDouble(_in_lat.getText().toString());
                    Double mLong = Double.parseDouble(_in_long.getText().toString());
                    curr_location = new LatLng(mLat, mLong);
                }


                if (startHour == -1 && startMinute == -1 && endHour == -1 && endMinute == -1) {
                    startHour = 0; startMinute = 0;
                    endHour = 23; endMinute = 59;
                    Log.d("TESTTEST", String.valueOf(startHour));
                }

                if(curr_location == null || startMonth == -1 || startDay == -1 || startYear == -1 || startHour == -1 || startMinute == -1
                        || endMonth == -1 || endDay == -1 || endYear == -1 || endHour == -1 || endMinute == -1){
                    Toast.makeText(getContext(), "Please input valid search info! Please try again.", Toast.LENGTH_SHORT).show();
                    return;
                }

                ClientController clientController = ClientController.getInstance();
                // SearchSpaceTask(LatLng location, String startDate, String startTime, String endDate, String endTime, String carType, String distance){

                SSTask = new SearchSpaceTask(curr_location,
                        startMonth+"-"+startDay+"-"+startYear,
                        startHour+"-"+startMinute,
                        endMonth+"-"+endDay+"-"+endYear,
                        endHour+"-"+endMinute,
                        curr_cartype,
                        curr_dist);
                SSTask.execute((Void)null);
                System.out.println("SearchFrag: btn_confirm clicked");
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_save : {
//                Log.i(TAG, "Save from fragment");
//                return true;
//            }
//        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("AUTOCOMPLETE", "SEARCH");
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getContext(), data);
                Log.i(TAG, "Place: " + place.getName());
                Log.i(TAG, "Place LatLng: " + place.getLatLng().toString());

                _addressText.setText(place.getAddress());
                curr_location = place.getLatLng();


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getContext(), data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
    private class SearchSpaceTask extends AsyncTask<Void, Void, SearchResults> {
        //        private final String mAddressText;
//        private final String mDescrip;
//        private final String mCarType;
//        private final int mCancelPolicy;
        private final LatLng mlocation;
        private final String mStartDate;
        private final String mStartTime;
        private final String mEndDate;
        private final String mEndTime;
        private final String mCarType;
        private final String mDistance;

        SearchSpaceTask(LatLng location, String startDate, String startTime, String endDate, String endTime, String carType, String distance){
            mlocation = location;
            mStartDate = startDate;
            mStartTime = startTime;
            mEndDate = endDate;
            Log.d("FINALBATTLE", endTime);
            mEndTime = endTime;
            mCarType = carType;
            mDistance = distance;
//            doInBackground((Void) null);

            System.out.println(mCarType);
            Log.d("@@@@@@ ", "mlocation.latitude = "+mlocation.latitude);
            Log.d("@@@@@@ ", "mlocation.longitude = "+mlocation.longitude);
        }
        //        @Override
//        protected void onPreExecute(){
//            //Display a progress dialog
//            progressDialog = new ProgressDialog(getContext(),
//                    R.style.AppTheme);
//            progressDialog.setIndeterminate(true);
//            progressDialog.setMessage("Adding...");
//            progressDialog.show();
//        }
        @Override
        protected SearchResults doInBackground(Void... params){
            ClientController clientController = ClientController.getInstance();
            try {

                Log.d("SEARCH", mStartDate.toString()  + " " + mEndDate.toString()+" "+mEndTime.toString());
                clientController.search(mlocation, mStartDate, mStartTime, mEndDate, mEndTime, mCarType, mDistance);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("SearchFrag doInBackground 0");
            NetworkPackage NP = clientController.checkReceived();
            System.out.println("SearchFrag doInBackground 5");
            MyEntry<String, Serializable> entry = NP.getCommand();
            System.out.println("SearchFrag doInBackground 10");
            String key = entry.getKey();
            Object value = entry.getValue();

            System.out.println("SearchFrag doInBackground 15");

            if(key.equals("SEARCH_RESULT")) {
                HashMap<String, Serializable> map = (HashMap<String, Serializable>)value;

                SearchResults result = (SearchResults) map.get("SEARCHRESULT");
                HashMap<Long, Integer> result_freq = (HashMap<Long, Integer>) map.get("FREQ");

//                Log.d("Results", result.searchResultList.get(0).getStreetAddr());
                clientController.toDispaySearch = true;
                //controller.updateActivity();
                clientController.searchResults = result;
                Log.d("SEARCH_RESULT", "Size " + String.valueOf(result.searchResultList.size()));

                // Update HashMap<Long, Integer> of <parking spot ID, amount of bookings>
                clientController.searchResultsFreq = result_freq;
                Log.d("SEARCH_RESULT_FREQ", "Size " + String.valueOf(result_freq.size()));

                return result;
            }
            else {
                return null;
            }
        }
        @Override
        protected void onPostExecute(SearchResults result) {
            if(result!=null) {
                Log.d("SEARCH_RESULT", "onPostExecute");
                ((RenterActivity) getActivity()).displaySearchResult(result, curr_location, mStartDate, mStartTime, mEndDate, mEndTime);
            }
            else{
                Toast.makeText(getContext(), "Error to find space! Please try again.", Toast.LENGTH_SHORT).show();
                // back to add space?
            }

        }
    }
}