package csci310.parkhere.ui;

import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.imanoweb.calendarview.CalendarListener;
import com.imanoweb.calendarview.CustomCalendarView;
import com.imanoweb.calendarview.DayDecorator;
import com.imanoweb.calendarview.DayView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;
import csci310.parkhere.resource.CalendarUtils;
import resource.ParkingSpot;
import resource.Time;
import resource.TimeInterval;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SpaceDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SpaceDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpaceDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;

    public ParkingSpot thisParkingSpot;

    TextView _spacedetail_address;
    CustomCalendarView calendarView;
    final String disabledDateColor = "#c3c3c3";
    final String selectedDateColor = "#3e50b4";
    final String postedDateColor = "#3e50b4";

    Calendar currentCalendar;
    List<DayDecorator> decorators = new ArrayList<>();
    int curr_year, curr_month, curr_day, curr_hour, curr_minute;

    ArrayList<TimeInterval> currSpaceTimeIntervals = new ArrayList<TimeInterval>();
    // Store currSpaceTimeIntervals in GregorianCalendar in startTime, endTime order
    ArrayList<GregorianCalendar> currSpaceTimeIntervalsGC = new ArrayList<GregorianCalendar>();

    ArrayList<TimeInterval> postedSpaceTimeIntervals = new ArrayList<TimeInterval>();
    // Store currSpaceTimeIntervals in GregorianCalendar in startTime, endTime order
    ArrayList<GregorianCalendar> postedSpaceTimeIntervalsGC = new ArrayList<GregorianCalendar>();

    Date selectedStartDate;
    Date selectedEndDate;

//    //*******************************************************************************
//    // FOR TESTING DELETE LATER!!!
//    // TimeInterval(Time start, Time end)
//    // Time (int year, int month, int dayOfMonth, int hourOfDay, int minute, int second)
//    Time start1 = new Time (2016, 9, 20, 0, 0, 0);
//    Time end1 = new Time (2016, 9, 25, 0, 0, 0);
//    TimeInterval interval1 = new TimeInterval(start1, end1);
//    //*******************************************************************************

//    // Keep track of current selected time
//    TwoEntryQueue<Time> currSelectedTime = new TwoEntryQueue<Time>();

    Button _btn_add_space, _btn_start_time, _btn_end_time, _btn_add_confirm;
    LinearLayout _addTimeForSpaceLayout;
    EditText _in_start_date, _in_start_time, _in_end_date, _in_end_time, _in_price;
    ListView _timeList;

    private OnFragmentInteractionListener mListener;

    public SpaceDetailFragment() {
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
    public static SpaceDetailFragment newInstance(int param1, String param2) {
        SpaceDetailFragment fragment = new SpaceDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_space_detail, container, false);


        _spacedetail_address = (TextView)v.findViewById(R.id.spacedetail_address);

        _addTimeForSpaceLayout = (LinearLayout) v.findViewById(R.id.addTimeForSpaceLayout);

        _btn_add_space=(Button)v.findViewById(R.id.btn_add_space);
        _btn_add_space.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                _addTimeForSpaceLayout.setVisibility(View.VISIBLE);
            }
        });

        _in_start_date=(EditText)v.findViewById(R.id.in_start_date);
        _in_start_time=(EditText)v.findViewById(R.id.in_start_time);
        _in_end_date=(EditText)v.findViewById(R.id.in_end_date);
        _in_end_time=(EditText)v.findViewById(R.id.in_end_time);
        _in_price=(EditText)v.findViewById(R.id.in_price);
        _btn_start_time=(Button)v.findViewById(R.id.btn_start_time);
        _btn_start_time.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                curr_hour = c.get(Calendar.HOUR_OF_DAY);
                curr_minute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                Log.d("Selected start time is ", hourOfDay + ":" + minute);
                                _in_start_time.setText(hourOfDay+"-"+minute);
                            }
                        }, curr_hour, curr_minute, false);
                timePickerDialog.show();
            }
        });
        _btn_end_time=(Button)v.findViewById(R.id.btn_end_time);
        _btn_end_time.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                curr_hour = c.get(Calendar.HOUR_OF_DAY);
                curr_minute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                Log.d("Selected start time is ", hourOfDay + ":" + minute);
                                _in_end_time.setText(hourOfDay + "-" + minute);
                            }
                        }, curr_hour, curr_minute, false);
                timePickerDialog.show();
            }
        });

        //Initialize CustomCalendarView from layout
        calendarView = (CustomCalendarView) v.findViewById(R.id.calendar_view);
        //Initialize calendar with date
        currentCalendar = Calendar.getInstance(Locale.getDefault());
        //Show monday as first date of week
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);
        //Show/hide overflow days of a month
        calendarView.setShowOverflowDate(false);
        //call refreshCalendar to update calendar the view
        calendarView.refreshCalendar(currentCalendar);

        selectedStartDate = null;
        selectedEndDate = null;


//        ********************************
        ClientController controller = ClientController.getInstance();
        controller.requestMyParkingSpotList();
        thisParkingSpot = controller.parkingSpots.get(0);

        controller.requestSpotTimeInterval(thisParkingSpot);
        updatePostedSpaceTimeIntervalsGC(thisParkingSpot.getTimeIntervalList());
//        ********************************

        System.out.println("SpaceDetailFragment for spaceID: " + thisParkingSpot.getParkingSpotID());


        //Handling custom calendar events
        calendarView.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {
                if (selectedStartDate == null) {
                    selectedStartDate = date;
                    _in_start_date.setText(selectedStartDate.toString());

                    decorators.clear();
                    decorators.add(new DisabledColorDecorator());
                    decorators.add(new PostedColorDecorator());
                    calendarView.setDecorators(decorators);
                    calendarView.refreshCalendar(currentCalendar);
                    currSpaceTimeIntervals.clear();
                    currSpaceTimeIntervalsGC.clear();
                    return;
                }

                if (selectedStartDate.compareTo(date) >= 0) {
                    selectedStartDate = date;
                    _in_start_date.setText(selectedStartDate.toString());
                } else {
                    selectedEndDate = date;
                    _in_end_date.setText(selectedEndDate.toString());

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(selectedStartDate);
                    Time timeStart = new Time(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));

                    cal.setTime(selectedEndDate);
                    Time timeEnd = new Time(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));

                    selectedStartDate = null;
                    selectedEndDate = null;

                    TimeInterval timeInterval = new TimeInterval(timeStart, timeEnd);
                    currSpaceTimeIntervals.add(timeInterval);
                    updateCurrSpaceTimeIntervalsGC(currSpaceTimeIntervals);


                    decorators.add(new SelectedColorDecorator());
                    calendarView.setDecorators(decorators);
                    calendarView.refreshCalendar(currentCalendar);
                }

                if(!currSpaceTimeIntervals.isEmpty()) {
                    Log.d("btn_add_space", ".setClickable(true)");
                    _btn_add_space.setClickable(true);
                }
                else {
                    Log.d("btn_add_space", ".setClickable(false)");
                    _btn_add_space.setClickable(false);
                }
            }

            @Override
            public void onMonthChanged(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
                Log.d("Selected month is ", df.format(date));
            }
        });

        //adding calendar day decorators
        decorators.add(new DisabledColorDecorator());
        decorators.add(new PostedColorDecorator());
        calendarView.setDecorators(decorators);
        calendarView.refreshCalendar(currentCalendar);

        _btn_add_confirm=(Button)v.findViewById(R.id.btn_add_confirm);
        _btn_add_confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

               String price =  _in_price.getText().toString();
                // call client controller


                _addTimeForSpaceLayout.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Add space requested", Toast.LENGTH_SHORT).show();
            }
        });

        _timeList = (ListView) v.findViewById(R.id.timeList);

        return v;
    }

    private class DisabledColorDecorator implements DayDecorator {
        @Override
        public void decorate(DayView dayView) {
            if (CalendarUtils.isPastDay(dayView.getDate())) {
                int color = Color.parseColor(disabledDateColor);
                dayView.setBackgroundColor(color);
            }
        }
    }

    private class PostedColorDecorator implements DayDecorator {
        @Override
        public void decorate(DayView dayView) {
            for (int i=0; i<postedSpaceTimeIntervalsGC.size(); i+=2) {
                Date startDate = new Date(postedSpaceTimeIntervalsGC.get(i).getTimeInMillis());
                Date endDate = new Date(postedSpaceTimeIntervalsGC.get(i+1).getTimeInMillis());
                if (CalendarUtils.isBetweenDay(dayView.getDate(), startDate, endDate)) {
                    int color = Color.parseColor(postedDateColor);
                    dayView.setTextColor(color);
                }
            }
        }
    }

    private class SelectedColorDecorator implements DayDecorator {
        @Override
        public void decorate(DayView dayView) {
            for (int i=0; i<currSpaceTimeIntervalsGC.size(); i+=2) {
                Date startDate = new Date(currSpaceTimeIntervalsGC.get(i).getTimeInMillis());
                Date endDate = new Date(currSpaceTimeIntervalsGC.get(i+1).getTimeInMillis());
                if (CalendarUtils.isBetweenDay(dayView.getDate(), startDate, endDate)) {
                    int color = Color.parseColor(selectedDateColor);
                    dayView.setBackgroundColor(color);
                    dayView.setTextColor(Color.parseColor("#FFFFFF"));
                }
            }
        }
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

    // update ListView timeList
    public void setTimeListview(String[] inTimeList) {
        _timeList.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, inTimeList));
        DiplayListViewHelper.getListViewSize(_timeList);
    }

    //GregorianCalendar(int year, int month, int dayOfMonth, int hourOfDay, int minute)
    public void updatePostedSpaceTimeIntervalsGC(ArrayList<TimeInterval> inPostedSpaceTimeIntervals) {
        postedSpaceTimeIntervalsGC = new ArrayList<GregorianCalendar>();
        for (TimeInterval timeInterv: inPostedSpaceTimeIntervals) {
            Time startTime = timeInterv.startTime;
            Time endTime = timeInterv.endTime;
            postedSpaceTimeIntervalsGC.add(new GregorianCalendar(startTime.year, startTime.month,
                    startTime.dayOfMonth, startTime.hourOfDay, startTime.minute));
            postedSpaceTimeIntervalsGC.add(new GregorianCalendar(endTime.year, endTime.month,
                    endTime.dayOfMonth, endTime.hourOfDay, endTime.minute));
        }
    }

    //GregorianCalendar(int year, int month, int dayOfMonth, int hourOfDay, int minute)
    public void updateCurrSpaceTimeIntervalsGC(ArrayList<TimeInterval> currSpaceTimeIntervals) {
        currSpaceTimeIntervalsGC = new ArrayList<GregorianCalendar>();
        for (TimeInterval timeInterv: currSpaceTimeIntervals) {
            Time startTime = timeInterv.startTime;
            Time endTime = timeInterv.endTime;
            currSpaceTimeIntervalsGC.add(new GregorianCalendar(startTime.year, startTime.month,
                                            startTime.dayOfMonth, startTime.hourOfDay, startTime.minute));
            currSpaceTimeIntervalsGC.add(new GregorianCalendar(endTime.year, endTime.month,
                                            endTime.dayOfMonth, endTime.hourOfDay, endTime.minute));
        }
    }

    public void setThisParkingSpot(ParkingSpot s)
    {
        thisParkingSpot =s;
    }

}
