package csci310.parkhere.ui;

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
import android.widget.TextView;

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
import csci310.parkhere.resource.CalendarUtils;
import csci310.parkhere.resource.TwoEntryQueue;
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
    private String mParam1;
    private String mParam2;

    TextView _spacedetail_address;
    CustomCalendarView calendarView;
    final String postedDateColor = "#c3c3c3";
    final String selectedDateColor = "#3E50B4";

    Calendar currentCalendar;
    List<DayDecorator> decorators = new ArrayList<>();
    int curr_year, curr_month, curr_day, curr_hour, curr_minute;
    ArrayList<TimeInterval> currSpaceTimeIntervals = new ArrayList<TimeInterval>();

    //*******************************************************************************
    // FOR TESTING DELETE LATER!!!
    // TimeInterval(Time start, Time end)
    // Time (int year, int month, int dayOfMonth, int hourOfDay, int minute, int second)
    Time start1 = new Time (2016, 9, 20, 0, 0, 0);
    Time end1 = new Time (2016, 9, 25, 0, 0, 0);
    TimeInterval interval1 = new TimeInterval(start1, end1);
    //*******************************************************************************

    // Store currSpaceTimeIntervals in GregorianCalendar in startTime, endTime order
    ArrayList<GregorianCalendar> currSpaceTimeIntervalsGC = new ArrayList<GregorianCalendar>();
    // Keep track of current selected time
    TwoEntryQueue<Time> currSelectedTime = new TwoEntryQueue<Time>();

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
    public static SpaceDetailFragment newInstance(String param1, String param2) {
        SpaceDetailFragment fragment = new SpaceDetailFragment();
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
        View v = inflater.inflate(R.layout.fragment_space_detail, container, false);

        //*******************************************************************************
        // FOR TESTING DELETE LATER!!!
        currSpaceTimeIntervals.add(interval1);
        updateCurrSpaceTimeIntervalsGC(currSpaceTimeIntervals);
        //*******************************************************************************

        _spacedetail_address = (TextView)v.findViewById(R.id.spacedetail_address);

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

        //Handling custom calendar events
        calendarView.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {
                for (int i=0; i<currSpaceTimeIntervalsGC.size(); i+=2) {
                    Date startDate = new Date(currSpaceTimeIntervalsGC.get(i).getTimeInMillis());
                    Date endDate = new Date(currSpaceTimeIntervalsGC.get(i + 1).getTimeInMillis());
                    if (!CalendarUtils.isBetweenDay(date, startDate, endDate)) {
                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                        Log.d("Selected date is ", df.format(date));
                        //                    selectedDateTv.setText("Selected date is " + df.format(date));

                        // Get selected Date in year, month, day
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        curr_year = cal.get(Calendar.YEAR);
                        curr_month = cal.get(Calendar.MONTH);
                        curr_day = cal.get(Calendar.DAY_OF_MONTH);

                        // Get Current Time
                        final Calendar c = Calendar.getInstance();
                        curr_hour = c.get(Calendar.HOUR_OF_DAY);
                        curr_minute = c.get(Calendar.MINUTE);

                                decorators.add(new DisabledColorDecorator());
        calendarView.setDecorators(decorators);
        calendarView.refreshCalendar(currentCalendar);

                        // Launch Time Picker Dialog
//                        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
//                                new TimePickerDialog.OnTimeSetListener() {
//                                    @Override
//                                    public void onTimeSet(TimePicker view, int hourOfDay,
//                                                          int minute) {
//                                        Log.d("Selected time is ", hourOfDay + ":" + minute);
//                                        curr_hour = hourOfDay;
//                                        curr_minute = minute;
//                                        Log.d("Updated time is ", curr_hour + ":" + curr_minute);
//
//                                        // Add to TwoEntryQueue<Time> currSelectedTime
//                                        currSelectedTime.add(new Time(curr_year,curr_month,curr_day,curr_hour,curr_minute,0));
//                                    }
//                                }, curr_hour, curr_minute, false);
//                        timePickerDialog.show();
                    }
                }
            }

            @Override
            public void onMonthChanged(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
                Log.d("Selected month is ", df.format(date));
//                Toast.makeText(CalendarDayDecoratorActivity.this, df.format(date), Toast.LENGTH_SHORT).show();
            }
        });


        //adding calendar day decorators
//        decorators.add(new DisabledColorDecorator());
//        calendarView.setDecorators(decorators);
//        calendarView.refreshCalendar(currentCalendar);

        return v;
    }

    private class DisabledColorDecorator implements DayDecorator {
        @Override
        public void decorate(DayView dayView) {
            for (int i=0; i<currSpaceTimeIntervalsGC.size(); i+=2) {
                Date startDate = new Date(currSpaceTimeIntervalsGC.get(i).getTimeInMillis());
                Date endDate = new Date(currSpaceTimeIntervalsGC.get(i+1).getTimeInMillis());
                if (CalendarUtils.isBetweenDay(dayView.getDate(), startDate, endDate)) {
                    int color = Color.parseColor(postedDateColor);
                    dayView.setBackgroundColor(color);
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

    // Add TimeInterval to currSpaceTimeIntervals
    public void addToCurrSpaceTimeIntervals(TimeInterval newTimeInterval) {
        currSpaceTimeIntervals.add(newTimeInterval);
        updateCurrSpaceTimeIntervalsGC(currSpaceTimeIntervals);
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
}
