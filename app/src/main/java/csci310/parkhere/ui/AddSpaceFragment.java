package csci310.parkhere.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.util.Calendar;

import csci310.parkhere.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddSpaceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddSpaceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddSpaceFragment extends Fragment {
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private long mParam1;
    private String mParam2;

    Button _btn_add_address, btnStartDatePicker, btnStartTimePicker, btnEndDatePicker, btnEndTimePicker, _btn_confirm;
    EditText txtStartDate, txtStartTime, txtEndDate, txtEndTime, _in_descrip;
    TextView _addressText;

    private int startYear, startMonth, startDay, startHour, startMinute,
            endYear, endMonth, endDay, endHour, endMinute;
    private Calendar startDate = Calendar.getInstance();
    private Calendar endDate = Calendar.getInstance();
    private String description;

    private OnFragmentInteractionListener mListener;

    public AddSpaceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddSpaceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddSpaceFragment newInstance(long param1, String param2) {
        AddSpaceFragment fragment = new AddSpaceFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getLong(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_space, container, false);

        _addressText = (TextView)v.findViewById(R.id.addressText);

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

        _in_descrip = (EditText)v.findViewById(R.id.in_descrip);
        description = _in_descrip.getText().toString();

        _btn_confirm=(Button)v.findViewById(R.id.btn_confirm);
        _btn_confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//                ClientController clientController = ClientController.getInstance();
//                try {
//                    clientController.addSpace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
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
    }
}
