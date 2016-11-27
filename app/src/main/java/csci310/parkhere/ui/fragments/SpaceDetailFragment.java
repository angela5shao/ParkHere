package csci310.parkhere.ui.fragments;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.imanoweb.calendarview.CalendarListener;
import com.imanoweb.calendarview.CustomCalendarView;
import com.imanoweb.calendarview.DayDecorator;
import com.imanoweb.calendarview.DayView;

import junit.framework.Assert;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;
import csci310.parkhere.resource.CalendarUtils;
import csci310.parkhere.ui.activities.ProviderActivity;
import csci310.parkhere.ui.helpers.DiplayListViewHelper;
import resource.MyEntry;
import resource.NetworkPackage;
import resource.ParkingSpot;
import resource.Review;
import resource.Time;
import resource.TimeInterval;
import resource.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SpaceDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SpaceDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpaceDetailFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int mParam1;
    private String mParam2;

    public ParkingSpot thisParkingSpot;
    public ArrayList<TimeInterval> list;
    TextView _spacedetail_address;
    CustomCalendarView calendarView;

    final String disabledDateColor = "#c3c3c3";
    final String selectedDateColor = "#3e50b4";
    final String postedDateColor = "#009688";

    Calendar currentCalendar;
    List<DayDecorator> decorators = new ArrayList<>();
    int curr_year, curr_month, curr_day, curr_hour, curr_minute;
    long curr_selected_time_id;
    String address = "";

    ArrayList<TimeInterval> currSpaceTimeIntervals = new ArrayList<TimeInterval>();
    // Store currSpaceTimeIntervals in GregorianCalendar in startTime, endTime order
    ArrayList<GregorianCalendar> currSpaceTimeIntervalsGC = new ArrayList<GregorianCalendar>();

    ArrayList<TimeInterval> postedSpaceTimeIntervals = new ArrayList<TimeInterval>();
    // Store currSpaceTimeIntervals in GregorianCalendar in startTime, endTime order
    ArrayList<GregorianCalendar> postedSpaceTimeIntervalsGC = new ArrayList<GregorianCalendar>();

    Date currSelectedDate;
    Date selectedStartDate;
    Date selectedEndDate;

    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    Time inputedStartTime;
    Time inputedEndTime;
    Time inputedEditStartTime;
    Time inputedEditEndTime;


    private ArrayList<String> mImagesURLs;
    private GalleryPagerAdapter mImageAdapter;


    ViewPager _pager;
    LinearLayout _thumbnails;

    ListView spotReviewList;
    ArrayAdapter spotReivewListAdapter;
    ArrayList<String> spotReviewStringList;

//    //*******************************************************************************
//    // FOR TESTING DELETE LATER!!!
//    // TimeInterval(Time start, Time end)
//    // Time (int year, int month, int dayOfMonth, int hourOfDay, int minute, int second)
//    Time start1 = new Time (2016, 9, 20, 0, 0, 0);
//    Time end1 = new Time (2016, 9, 25, 0, 0, 0);
//    TimeInterval interval1 = new TimeInterval(start1, end1);
//    //*******************************************************************************

    Button _btn_add_time, _btn_start_time, _btn_end_time, _btn_add_confirm, _btn_delete_time,
            _btn_editSpace, _btn_editTimeConfirm, _btn_deleteSpace,
            _btn_editStartTime, _btn_editEndTime;
    LinearLayout _addTimeForSpaceLayout, _editTimeForSpaceLayout;
    EditText _in_start_date, _in_start_time, _in_end_date, _in_end_time, _in_price,
            _edit_start_date, _edit_end_date, _edit_start_time, _edit_end_time, _edit_price;
    ListView _timeList;

    ProgressDialog progressDialog;
    AddTimeForSpaceTask AddSpaceTask = null;
    DeleteTimeForSpaceTask DeleteTimeTask = null;

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

        ArrayList<Integer> inStartYear=null, inStartMonth=null, inStartDay=null, inStartHour=null, inStartMin=null;
        ArrayList<Integer> inEndYear=null, inEndMonth=null, inEndDay=null, inEndHour=null, inEndMin=null;
        // Get data from parent activity/fragment
        Bundle b = getArguments();
        if (b != null) {
            address = b.getString("ADDRESS");

            inStartYear = new ArrayList<Integer>(b.getIntegerArrayList("START_YEARS"));
            inStartMonth = new ArrayList<Integer>(b.getIntegerArrayList("START_MONTHS"));
            inStartDay = new ArrayList<Integer>(b.getIntegerArrayList("START_DAYS"));
            inStartHour = new ArrayList<Integer>(b.getIntegerArrayList("START_HOURS"));
            inStartMin = new ArrayList<Integer>(b.getIntegerArrayList("START_MINS"));

            inEndYear = new ArrayList<Integer>(b.getIntegerArrayList("END_YEARS"));
            inEndMonth = new ArrayList<Integer>(b.getIntegerArrayList("END_MONTHS"));
            inEndDay = new ArrayList<Integer>(b.getIntegerArrayList("END_DAYS"));
            inEndHour = new ArrayList<Integer>(b.getIntegerArrayList("END_HOURS"));
            inEndMin = new ArrayList<Integer>(b.getIntegerArrayList("END_MINS"));


            mImagesURLs = getArguments().getStringArrayList("spot_images");
        }
        updatePostedSpaceTimeIntervalsGC(inStartYear, inStartMonth, inStartDay, inStartHour, inStartMin,
                inEndYear, inEndMonth, inEndDay, inEndHour, inEndMin);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_space_detail, container, false);


        spotReviewList = (ListView) v.findViewById(R.id.spot_review_list);


        spotReviewStringList = new ArrayList<>();

        spotReivewListAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,  spotReviewStringList);
        spotReviewList.setAdapter(spotReivewListAdapter);

        RequestReviewTask rrt = new RequestReviewTask(thisParkingSpot.getParkingSpotID());
        rrt.execute();



        //Display parking spot images from URLs
        _pager = (ViewPager) v.findViewById(R.id.pager);
        _thumbnails = (LinearLayout) v.findViewById(R.id.thumbnails);
        //Assert.assertNotNull() methods checks that the object is null or not.
        //      If it is null then it throws an AssertionError.
        Assert.assertNotNull(mImagesURLs);
        mImageAdapter = new GalleryPagerAdapter(getContext());
        _pager.setAdapter(mImageAdapter);
        _pager.setOffscreenPageLimit(6); // how many images to load into memory





        _spacedetail_address = (TextView)v.findViewById(R.id.spacedetail_address);
        _spacedetail_address.setText(address);

        _addTimeForSpaceLayout = (LinearLayout) v.findViewById(R.id.addTimeForSpaceLayout);
        _editTimeForSpaceLayout = (LinearLayout) v.findViewById(R.id.editTimeForSpaceLayout);

        _btn_add_time=(Button)v.findViewById(R.id.btn_add_time);
        _btn_add_time.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(_addTimeForSpaceLayout.getVisibility()==View.GONE) {
                    _addTimeForSpaceLayout.setVisibility(View.VISIBLE);
                } else {
                    _addTimeForSpaceLayout.setVisibility(View.GONE);
                }
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
        _btn_editSpace = (Button)v.findViewById(R.id.editSpace_btn);
        _btn_editSpace.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mListener.openSpaceEditFragment(thisParkingSpot);
            }
        });

        _btn_deleteSpace = (Button)v.findViewById(R.id.deleteSpace_btn);
        _btn_deleteSpace.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DeleteSpaceTask dst = new DeleteSpaceTask(thisParkingSpot);
                dst.execute((Void) null);
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


        // Handling custom calendar events
        calendarView.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {
                currSelectedDate = date;
                GetPostedTimeOnDateTask GPTODTask = new GetPostedTimeOnDateTask(currSelectedDate);
                GPTODTask.execute((Void) null);


                // Check if date is not pasted first
//                if (!CalendarUtils.isPastDay(date)) {
                    if (selectedStartDate == null) {
                        selectedStartDate = date;

//                        _in_start_date.setText(dateFormat.format(selectedStartDate));
//                        _edit_start_date.setText(dateFormat.format(selectedStartDate));

                        // Build string with incremented month (because month selected on calendar is -1 of actual month)
                        String updatedStartDate = Integer.toString(selectedStartDate.getMonth()+1)+"/"+selectedStartDate.getDate()+"/"+Integer.toString(selectedStartDate.getYear()+1900);

                        _in_start_date.setText(updatedStartDate);
                        _edit_start_date.setText(updatedStartDate);

                        decorators.clear();
                        decorators.add(new DisabledColorDecorator());
                        decorators.add(new PostedColorDecorator());
                        calendarView.setDecorators(decorators);
                        currentCalendar.setTime(selectedStartDate);
                        calendarView.refreshCalendar(currentCalendar);
                        currSpaceTimeIntervals.clear();
                        currSpaceTimeIntervalsGC.clear();
                        return;
                    }

                    if (selectedStartDate.compareTo(date) >= 0) {
                        selectedStartDate = date;

                        // Build string with incremented month (because month selected on calendar is -1 of actual month)
                        String updatedStartDate = Integer.toString(selectedStartDate.getMonth()+1)+"/"+selectedStartDate.getDate()+"/"+Integer.toString(selectedStartDate.getYear()+1900);

                        _in_start_date.setText(updatedStartDate);//dateFormat.format(updatedStartDate));
                        _edit_start_date.setText(updatedStartDate);//dateFormat.format(selectedStartDate));
                    } else {
                        selectedEndDate = date;

                        // Build string with incremented month (because month selected on calendar is -1 of actual month)
                        String updatedEndDate = Integer.toString(selectedEndDate.getMonth()+1)+"/"+selectedEndDate.getDate()+"/"+Integer.toString(selectedEndDate.getYear()+1900);

                        _in_end_date.setText(updatedEndDate);//dateFormat.format(selectedEndDate));
                        _edit_end_date.setText(updatedEndDate);//dateFormat.format(selectedEndDate));

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(selectedStartDate);
                        Time timeStart = new Time(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));

                        cal.setTime(selectedEndDate);
                        Time timeEnd = new Time(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));

    //                    Log.d("time", selectedStartDate.toString() + " " + selectedEndDate.toString());
//                        Log.d("Month", String.valueOf(cal.get(Calendar.MONTH)) + " " + selectedEndDate.toString());
//                        Log.d("time", timeStart.toString() + " " + timeEnd.toString());
                        inputedStartTime = timeStart;
                        inputedEndTime = timeEnd;


                        TimeInterval timeInterval = new TimeInterval(timeStart, timeEnd);
                        currSpaceTimeIntervals.add(timeInterval);
                        updateCurrSpaceTimeIntervalsGC(currSpaceTimeIntervals);

                        decorators.add(new SelectedIntervalColorDecorator());
                        calendarView.setDecorators(decorators);
                        currentCalendar.setTime(selectedEndDate);
                        calendarView.refreshCalendar(currentCalendar);

                        selectedStartDate = null;
                        selectedEndDate = null;
                    }
//                }

//                if (!currSpaceTimeIntervals.isEmpty()) {
//                    Log.d("btn_add_space", ".setClickable(true)");
//                    _btn_add_time.setClickable(true);
//                } else {
//                    Log.d("btn_add_space", ".setClickable(false)");
//                    _btn_add_time.setClickable(false);
//                }
            }

            @Override
            public void onMonthChanged(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
                Log.d("Selected month is ", df.format(date));
            }
        });

        //adding calendar day decorators
        decorators.add(new SelectedColorDecorator());
        decorators.add(new DisabledColorDecorator());
        decorators.add(new PostedColorDecorator());
        calendarView.setDecorators(decorators);
        calendarView.refreshCalendar(currentCalendar);

        _btn_add_confirm=(Button)v.findViewById(R.id.btn_add_confirm);
        _btn_add_confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                boolean valid = true;
                ArrayList<TimeInterval> intervals = thisParkingSpot.getTimeIntervalList();
                for(int i = 0; i < intervals.size(); i++)
                {
                    Time start = intervals.get(i).startTime;
                    Time end = intervals.get(i).endTime;
                    if((start.compareTo(inputedStartTime) <= 0 && end.compareTo(inputedEndTime) >=0) ||
                            (start.compareTo(inputedStartTime) >= 0 && start.compareTo(inputedEndTime) <=0) ||
                            (end.compareTo(inputedStartTime) >= 0 && end.compareTo(inputedEndTime) <=0) ||
                            (start.compareTo(inputedStartTime) >= 0 && end.compareTo(inputedEndTime) <=0))
                    {
                        valid = false;
                        Log.d("ADDTIME", "Try to add: " + inputedStartTime + " " + inputedEndTime);
                        Log.d("ADDTIME", "Conflict: " + start + " " + end);
                        Log.d("ADDTIME", "Not valid time interval");
                    }
                }

                if(!valid) {
                    Toast.makeText(getContext(), "Please input valid time interval.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Check that rate is provided
                if(_in_price.getText().toString().length() == 0) {
                    Toast.makeText(getContext(), "Please input price (per hour).", Toast.LENGTH_SHORT).show();
                    return;
                }

                AddSpaceTask = new AddTimeForSpaceTask(_in_price.getText().toString());
                AddSpaceTask.execute((Void) null);
            }
        });

        _timeList = (ListView) v.findViewById(R.id.timeList);
        _timeList.setClickable(true);

        _timeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                curr_selected_time_id = (long)position;

                // Prepopulate edit start & end dates/times with existing dates/times
                TimeInterval t = list.get((int) curr_selected_time_id);
                String startDate = Integer.toString(t.startTime.month+1) + "/" + t.startTime.dayOfMonth + "/" + t.startTime.year;
                String startTime = t.startTime.hourOfDay + "-" + t.startTime.minute;
                String endDate = Integer.toString(t.endTime.month+1) + "/" + t.endTime.dayOfMonth + "/" + t.endTime.year;
                String endTime = t.endTime.hourOfDay + "-" + t.endTime.minute;

                _edit_start_date.setText(startDate);
                _edit_end_date.setText(endDate);
                _edit_start_time.setText(startTime);
                _edit_end_time.setText(endTime);

                // Prepopulate with current price
                _edit_price.setText(Double.toString(thisParkingSpot.search_price));

                if(_editTimeForSpaceLayout.getVisibility()==View.GONE) {
                    _btn_delete_time.setVisibility(View.VISIBLE);
                    _editTimeForSpaceLayout.setVisibility(View.VISIBLE);
                }
                else {
                    _btn_delete_time.setVisibility(View.GONE);
                    _editTimeForSpaceLayout.setVisibility(View.GONE);
                }
            }
        });
        
        _btn_delete_time = (Button) v.findViewById(R.id.btn_delete_time);
        _btn_delete_time.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DeleteTimeTask = new DeleteTimeForSpaceTask(curr_selected_time_id);
                DeleteTimeTask.execute((Void) null);
            }
        });

        _edit_start_date = (EditText)v.findViewById(R.id.edit_start_date);
        _edit_end_date = (EditText)v.findViewById(R.id.edit_end_date);
        _edit_start_time = (EditText)v.findViewById(R.id.edit_start_time);
        _edit_end_time = (EditText)v.findViewById(R.id.edit_end_time);
        _edit_price = (EditText)v.findViewById(R.id.editPrice_text);

        _edit_price = (EditText)v.findViewById(R.id.editPrice_text);

        _btn_editStartTime=(Button)v.findViewById(R.id.editStartTime_btn);
        _btn_editStartTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                curr_hour = c.get(Calendar.HOUR_OF_DAY);
                curr_minute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                _edit_start_time.setText(hourOfDay+"-"+minute);
                            }
                        }, curr_hour, curr_minute, false);
                timePickerDialog.show();
            }
        });
        _btn_editEndTime=(Button)v.findViewById(R.id.editEndTime_btn);
        _btn_editEndTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                curr_hour = c.get(Calendar.HOUR_OF_DAY);
                curr_minute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                _edit_end_time.setText(hourOfDay + "-" + minute);
                            }
                        }, curr_hour, curr_minute, false);
                timePickerDialog.show();
            }
        });

        _btn_editTimeConfirm = (Button) v.findViewById(R.id.editTimeConfirm_btn);
        _btn_editTimeConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//                ArrayList<TimeInterval> intervals = thisParkingSpot.getTimeIntervalList();
//                for(int i = 0; i < intervals.size(); i++) {
//                    Time start = intervals.get(i).startTime;
//                    Time end = intervals.get(i).endTime;
//                    if((start.compareTo(inputedStartTime) <= 0 && end.compareTo(inputedEndTime) >=0) ||
//                            (start.compareTo(inputedStartTime) >= 0 && start.compareTo(inputedEndTime) <=0) ||
//                            (end.compareTo(inputedStartTime) >= 0 && end.compareTo(inputedEndTime) <=0) ||
//                            (start.compareTo(inputedStartTime) >= 0 && end.compareTo(inputedEndTime) <=0)) {
//                        Toast.makeText(getContext(), "Please input valid time interval", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                }
                // Check that rate is provided
                if(_edit_price.getText().toString().length() == 0) {
                    Toast.makeText(getContext(), "Please input price (per hour).", Toast.LENGTH_SHORT).show();
                    return;
                }

//                EditTimeTask editTimeTask = new EditTimeTask(_edit_price.getText().toString(), 1);
                // Convert revised month in date (to send back to server, -1 the month)
                String[] sDate = _edit_start_date.getText().toString().split("/");
                String[] eDate = _edit_end_date.getText().toString().split("/");
                EditTimeTask editTimeTask = new EditTimeTask(

                        sDate[1]+"/"+Integer.toString(Integer.valueOf(sDate[0])-1)+"/"+sDate[2],//_edit_start_date.getText().toString(),
//                        sDate[1]+"/"+sDate[0]+"/"+sDate[2],
                        _edit_start_time.getText().toString()+"-0",
                        eDate[1]+"/"+Integer.toString(Integer.valueOf(eDate[0])-1)+"/"+eDate[2],
//                        eDate[1]+"/"+eDate[0]+"/"+eDate[2],//_edit_end_date.getText().toString(),
                        _edit_end_time.getText().toString()+"-0",
                        _edit_price.getText().toString(),
                        list.get((int) curr_selected_time_id).TimeIntervalID);
                editTimeTask.execute((Void)null);
                Log.d("SpaceDetail", "edit_start_date: " + sDate[1]+"/"+sDate[0]+"/"+sDate[2] +"* * * * * * * * ***** *");
                Log.d("SpaceDetail", "edit_start_date: " + eDate[1]+"/"+eDate[0]+"/"+eDate[2] +"* * * * * * * * ***** *");
            }
        });

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
            int color = Color.parseColor(postedDateColor);
            for (int i=0; i<postedSpaceTimeIntervalsGC.size(); i+=2) {
                Date startDate = new Date(postedSpaceTimeIntervalsGC.get(i).getTimeInMillis());
                Date endDate = new Date(postedSpaceTimeIntervalsGC.get(i+1).getTimeInMillis());
                if (CalendarUtils.isBetweenDay(dayView.getDate(), startDate, endDate)) {
                    dayView.setTextColor(color);
                }
            }
        }
    }

    private class SelectedColorDecorator implements DayDecorator {
        @Override
        public void decorate(DayView dayView) {
            int color = Color.parseColor(selectedDateColor);
            if(currSelectedDate != null){
                if(currSelectedDate.equals(dayView.getDate())) {
                    dayView.setBackgroundColor(color);
                    dayView.setTextColor(Color.parseColor("#FFFFFF"));
                }
            }
        }
    }

    private class SelectedIntervalColorDecorator implements DayDecorator {
        @Override
        public void decorate(DayView dayView) {
            int color = Color.parseColor(selectedDateColor);
            for (int i=0; i<currSpaceTimeIntervalsGC.size(); i+=2) {
                Date startDate = new Date(currSpaceTimeIntervalsGC.get(i).getTimeInMillis());
                Date endDate = new Date(currSpaceTimeIntervalsGC.get(i+1).getTimeInMillis());

                if (_addTimeForSpaceLayout.getVisibility()==View.VISIBLE
                        && CalendarUtils.isBetweenDay(dayView.getDate(), startDate, endDate)) {
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
        void onFragmentInteraction(Uri uri);
        void openSpaceEditFragment(ParkingSpot spot);
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
        Log.d("SpaceDetailFragment ", "setTimeListview CALLED - ");
        _timeList.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, inTimeList));
        DiplayListViewHelper.getListViewSize(_timeList);

        _btn_delete_time.setVisibility(View.GONE);
    }

    //GregorianCalendar(int year, int month, int dayOfMonth, int hourOfDay, int minute)
    public void updatePostedSpaceTimeIntervalsGC(ArrayList<Integer> inStartYear, ArrayList<Integer> inStartMonth,
                                                 ArrayList<Integer> inStartDay, ArrayList<Integer> inStartHour,
                                                 ArrayList<Integer> inStartMin, ArrayList<Integer> inEndYear,
                                                 ArrayList<Integer> inEndMonth, ArrayList<Integer> inEndDay,
                                                 ArrayList<Integer> inEndHour, ArrayList<Integer> inEndMin) {
        postedSpaceTimeIntervalsGC = new ArrayList<GregorianCalendar>();
        for(int i=0; i<inStartYear.size(); ++i) {
            postedSpaceTimeIntervalsGC.add(new GregorianCalendar(inStartYear.get(i), inStartMonth.get(i),
                    inStartDay.get(i), inStartHour.get(i), inStartMin.get(i)));
            postedSpaceTimeIntervalsGC.add(new GregorianCalendar(inEndYear.get(i), inEndMonth.get(i),
                    inEndDay.get(i), inEndHour.get(i), inEndMin.get(i)));
        }
    }

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






    private class AddTimeForSpaceTask extends AsyncTask<Void, Void, Boolean> {
        private final String mPrice;

        AddTimeForSpaceTask(String price){
            mPrice = price;
        }
        @Override
        protected void onPreExecute(){
            //Display a progress dialog
            progressDialog = new ProgressDialog(getContext(), R.style.AppTheme);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Adding...");
            progressDialog.show();

            _addTimeForSpaceLayout.setVisibility(View.GONE);
        }
        @Override
        protected Boolean doInBackground(Void... params){
            // call client controller
            ClientController controller = ClientController.getInstance();

//            System.out.println("BEFORE REQ Start:"+ inputedStartTime);
//            System.out.println("BEFORE REQ End:" + inputedEndTime);
//            Log.d("ADDSPACE", String.valueOf(thisParkingSpot.getParkingSpotID()));

            controller.requestAddTime(thisParkingSpot, inputedStartTime, inputedEndTime, Integer.valueOf(mPrice));

            NetworkPackage NP = controller.checkReceived();
            MyEntry<String, Serializable> entry = NP.getCommand();
            String key = entry.getKey();
            Object value = entry.getValue();
            if(key.equals("ADDTIME")) {
                return true;
            }
            else {
                return false;
            }
        }
        @Override
        protected void onPostExecute(Boolean success) {
            if(success) {
                // Back to SpacesFragment
                progressDialog.dismiss();
                ((ProviderActivity)getActivity()).showSpaceFragment();
                Log.d("ADDTIME", "finish add time");
                Toast.makeText(getContext(), "Time added successfully!", Toast.LENGTH_SHORT).show();

                // Back to SpacesFragment
//                ((ProviderActivity)getActivity()).showSpaceFragment();
                progressDialog.dismiss();

            } else{
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Add time failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }





    private class DeleteTimeForSpaceTask extends AsyncTask<Void, Void, Boolean> {
        private final long mTimeID;

        DeleteTimeForSpaceTask(long time_id){
            mTimeID = time_id;
//            doInBackground((Void) null);
            System.out.println(mTimeID);
        }
        @Override
        protected void onPreExecute(){
            //Display a progress dialog
            progressDialog = new ProgressDialog(getContext(), R.style.AppTheme);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Deleting...");
            progressDialog.show();
        }
        @Override
        protected Boolean doInBackground(Void... params ){
            // call client controller
            ClientController controller = ClientController.getInstance();
//            curr_selected_time_id = controller.;
            System.out.println("DELETE TIME ID: " + curr_selected_time_id);
            controller.ProviderCancel(list.get((int)curr_selected_time_id).TimeIntervalID);

            NetworkPackage NP = controller.checkReceived();
            MyEntry<String, Serializable> entry = NP.getCommand();
            String key = entry.getKey();
            Object value = entry.getValue();
            if(key.equals("CANCELTIME")) {
                Log.d("CANCELTIME ", "received");
                return true;
            }
            else if(key.equals("CANCELTIMEFAIL")) {
                Log.d("CANCELTIMEFAIL ", "received");
                return false;
            }
            else {
                return false;
            }
        }
        @Override
        protected void onPostExecute(Boolean success) {
            if(success) {
                progressDialog.dismiss();
                _btn_delete_time.setVisibility(View.GONE);
                Log.d("DELETETIME", "finish delete time");
                Toast.makeText(getContext(), "Time is deleted successfully!", Toast.LENGTH_SHORT).show();

                // Back to SpacesFragment
                ((ProviderActivity)getActivity()).showSpaceFragment();
            } else{
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Delete time failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }





    private class GetPostedTimeOnDateTask extends AsyncTask<Void, Void, ArrayList<TimeInterval> >{
        private Date mDate;

        GetPostedTimeOnDateTask(Date date){
            mDate = date;
            Log.d("SpaceDetailFragment ", "GetPostedTimeOnDateTask CALLED - "+mDate.toString());
        }
        @Override
        protected void onPreExecute(){
            //
        }
        @Override
        protected ArrayList<TimeInterval> doInBackground(Void... params){
            //dd-mm-yyyy
            Calendar mCal = Calendar.getInstance();
            mCal.setTime(mDate);
            String date = mCal.get(Calendar.DAY_OF_MONTH)+"-"+mCal.get(Calendar.MONTH)+"-"+mCal.get(Calendar.YEAR);

            ClientController controller = ClientController.getInstance();
            Log.d("SpaceDetailFragment ", "GetPostedTimeOnDateTask doInBackground - "+thisParkingSpot.getParkingSpotID());
            //requestSpotTimeIntervalWithDate(long spotID, String date)
            return controller.requestSpotTimeIntervalWithDate(thisParkingSpot.getParkingSpotID(), date);
        }
        @Override
        protected void onPostExecute(ArrayList<TimeInterval> inTimeInterval) {
            if(inTimeInterval != null) {
                Log.d("SpaceDetailFragment ",
                        "GetPostedTimeOnDateTask onPostExecute inTimeInterval !NULL - "+inTimeInterval.size());
                list = inTimeInterval;
                Log.d("SpaceDetail", "* * * * * * list is assigned, size= "+inTimeInterval.size()+" * * * * * * *");
                String[] timeList = new String[inTimeInterval.size()];
                for(int i=0; i<inTimeInterval.size(); ++i) {
//                    String timeIntervalString = inTimeInterval.get(i).startTime.toString()
//                                + " ~ "
//                                + inTimeInterval.get(i).endTime.toString();
                    TimeInterval t = inTimeInterval.get(i);
                    String timeIntervalString = Integer.toString(t.startTime.month+1)+"/"+t.startTime.dayOfMonth+"/"+t.startTime.year
                            + " ~"
                            + Integer.toString(t.endTime.month+1)+"/"+t.endTime.dayOfMonth+"/"+t.endTime.year;

                    timeList[i] = timeIntervalString;
                }

                Log.d("SpaceDetailFragment ", "GetPostedTimeOnDateTask onPostExecute setTimeListview(timeList) CALLED");
                setTimeListview(timeList);
            }
        }

    }




    private class EditTimeTask extends AsyncTask<Void, Void, Boolean>{
        private final String mPrice;
        private final long timeSlotID;
        private Time start;
        private Time end;

        EditTimeTask(String startDate, String startTime, String endDate, String endTime, String price, long timeSlotID){
            System.out.println(startDate+" "+startTime);
            System.out.println(endDate+" "+endTime);

            start = new Time(startDate+" "+startTime);
            end = new Time(endDate+" "+endTime);
            mPrice = price;
            this.timeSlotID = timeSlotID;
        }

        @Override
        protected void onPreExecute(){
            //Display a progress dialog
            progressDialog = new ProgressDialog(getContext(), R.style.AppTheme);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Editing...");
            progressDialog.show();
        }
        @Override
        protected Boolean doInBackground(Void... params){
            // call client controller
            ClientController controller = ClientController.getInstance();

            controller.requestEditTime(timeSlotID, start, end, Integer.valueOf(mPrice));

            NetworkPackage NP = controller.checkReceived();
            MyEntry<String, Serializable> entry = NP.getCommand();
            String key = entry.getKey();
            Object value = entry.getValue();
            if(key.equals("EDITTIME")) {
                return true;
            }
            else {
                return false;
            }
        }
        @Override
        protected void onPostExecute(Boolean success) {
            if(success) {
                // Back to SpacesFragment
                ((ProviderActivity)getActivity()).showSpaceFragment();
                Toast.makeText(getContext(), "Time is edited successfully!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            } else{
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Time edit failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }




    private class DeleteSpaceTask extends AsyncTask<Void, Void, Boolean>{
        private long parkingSpaceID;
        DeleteSpaceTask(ParkingSpot parkingSpot){
            parkingSpaceID = parkingSpot.getParkingSpotID();
        }

        @Override
        protected void onPreExecute(){
            //Display a progress dialog
            progressDialog = new ProgressDialog(getContext(), R.style.AppTheme);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Deleting...");
            progressDialog.show();
        }
        @Override
        protected Boolean doInBackground(Void... params){
            // call client controller
            ClientController controller = ClientController.getInstance();

            controller.deleteSpace(parkingSpaceID);

            NetworkPackage NP = controller.checkReceived();
            MyEntry<String, Serializable> entry = NP.getCommand();
            String key = entry.getKey();
            Object value = entry.getValue();
            if(key.equals("DELETESPACE")) {
                return true;
            }
            else {
                return false;
            }
        }
        @Override
        protected void onPostExecute(Boolean success) {
            if(success) {
                // Back to SpacesFragment
                ((ProviderActivity)getActivity()).showSpaceFragment();
                Toast.makeText(getContext(), "Deleted time!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            } else{
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Delete time failed! Please try agian.", Toast.LENGTH_SHORT).show();
            }
        }
    }




    private class RequestReviewTask extends AsyncTask<Void, Void, ArrayList<Review>> {
        long parkingSpotID;
        ArrayList<User> userlist;


        RequestReviewTask(long parkingSpotID) {
            this.parkingSpotID = parkingSpotID;
        }
        @Override
        protected ArrayList<Review> doInBackground(Void... params) {

            ClientController clientController = ClientController.getInstance();


            clientController.requestParkingSpotReview(parkingSpotID);
            NetworkPackage NP = clientController.checkReceived();
            MyEntry<String, Serializable> entry = NP.getCommand();
            String key = entry.getKey();
            Object value = entry.getValue();
            if (key.equals("REVIEWFORPARKINGSPOT")) {
                HashMap<String, Serializable> map = (HashMap<String, Serializable>)value;

                ArrayList<Review> list = (ArrayList<Review>) map.get("REVIEWS");
                Log.d("FETCHREVIEWLIST", "listsize: " + String.valueOf(list.size()));
                userlist = (ArrayList<User>) map.get("USERS");



                return list;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Review> list) {


            if(list != null && userlist != null)
            {
                spotReviewStringList.clear();

//
                for(int i = 0; i < list.size(); i++)
                {
                    Log.d("FETCHREVIEW", list.get(i).comment);
                    spotReviewStringList.add(userlist.get(i).Fname + ":\n" + "Rating: " + String.valueOf(list.get(i).spotRating) + "\n" + "Comment: " + list.get(i).comment);

                }

//                for(Review r : list)
//                {
//                    Log.d("FETCHREVIEW", r.comment);
//                    spotReviewStringList.add("Rating: " + String.valueOf(r.spotRating) + "\n" + "Comment: " + r.comment);
//                }


                spotReivewListAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(spotReviewList);


            }


        }
    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewPager.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    // For mImageAdapter - Image horizontal scroll
    //      Source: https://github.com/sourcey/imagegallerydemo
    class GalleryPagerAdapter extends PagerAdapter {
        Context _context;
        LayoutInflater _inflater;

        public GalleryPagerAdapter(Context context) {
            _context = context;
            _inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mImagesURLs.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = _inflater.inflate(R.layout.pager_gallery_item, container, false);
            container.addView(itemView);

            // Get the border size to show around each image
            int borderSize = _thumbnails.getPaddingTop();

            // Get the size of the actual thumbnail image
            int thumbnailSize = ((FrameLayout.LayoutParams)
                    _pager.getLayoutParams()).bottomMargin - (borderSize);

            // Set the thumbnail layout parameters. Adjust as required
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(thumbnailSize, thumbnailSize);
            params.setMargins(0, 0, borderSize, 0);

            final ImageView thumbView = new ImageView(_context);
            thumbView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            thumbView.setLayoutParams(params);
            thumbView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Search Space Detail ", "- Thumbnail clicked @"+position);

                    // Set the pager position when thumbnail clicked
                    _pager.setCurrentItem(position);
                }
            });
            _thumbnails.addView(thumbView);

            // Source: https://github.com/davemorrissey/subsampling-scale-image-view
            final SubsamplingScaleImageView imageView =
                    (SubsamplingScaleImageView) itemView.findViewById(R.id.image);

            // Asynchronously load the image and set the thumbnail and pager view
            Glide.with(_context)
                    .load(mImagesURLs.get(position))
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                            imageView.setImage(ImageSource.bitmap(bitmap));
                            thumbView.setImageBitmap(bitmap);
                        }
                    });

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }


}
