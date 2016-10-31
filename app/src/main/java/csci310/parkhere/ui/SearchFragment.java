package csci310.parkhere.ui;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.Calendar;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;

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

    private static final String TAG = "!!!!!!!!!!!!!!! "; // EDIT/DELETE LATER!
    Button _btn_add_address, btnStartDatePicker, btnStartTimePicker, btnEndDatePicker, btnEndTimePicker, _btn_confirm;
    EditText txtStartDate, txtStartTime, txtEndDate, txtEndTime;
    TextView _addressText;
    LinearLayout _addressSearchLayout, _latlongSearchLayout;
    Spinner  _distSpinner, _cartypeSpinner;

    private int startYear, startMonth, startDay, startHour, startMinute,
                    endYear, endMonth, endDay, endHour, endMinute;

    private Calendar startDate = Calendar.getInstance();
    private Calendar endDate = Calendar.getInstance();
    LatLng curr_location;
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
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
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

                ClientController clientController = ClientController.getInstance();
                try {
                    clientController.search(curr_location, startMonth+"-"+startDay+"-"+startYear, startHour+"-"+startMinute,
                            endMonth+"-"+endDay+"-"+endYear, endHour+"-"+endMinute, curr_cartype, curr_dist);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
}
