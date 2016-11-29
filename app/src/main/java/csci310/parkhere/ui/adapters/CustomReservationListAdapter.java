package csci310.parkhere.ui.adapters;

/**
 * Created by yubowang on 11/26/16.
 */
import csci310.parkhere.R;
import resource.ParkingSpot;
import resource.Reservation;
import resource.Time;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomReservationListAdapter extends ArrayAdapter<Reservation>{
    private final Context context;
    private final ArrayList<Reservation> ReservationsArrayList;

    public CustomReservationListAdapter(Context context, ArrayList<Reservation> ReservationsArrayList) {

        super(context, R.layout.target_item, ReservationsArrayList);
//        Log.d("Constructor", "I get called");

        this.context = context;
        this.ReservationsArrayList = ReservationsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater

        View rowView = null;
            rowView = inflater.inflate(R.layout.target_item, parent, false);
            // 3. Get icon,title & counter views from the rowView
            TextView titleView = (TextView) rowView.findViewById(R.id.item_title);
            TextView counterView = (TextView) rowView.findViewById(R.id.item_counter);

            // 4. Set the text for textView
            ParkingSpot spotInlist = ReservationsArrayList.get(position).getSpot();
            Time startTime = ReservationsArrayList.get(position).getReserveTimeInterval().startTime;
            Time endTime = ReservationsArrayList.get(position).getReserveTimeInterval().endTime;

            Time displayStartTime = new Time(startTime.year,startTime.month,startTime.dayOfMonth,startTime.hourOfDay,startTime.minute,startTime.second);
            Time displayEndTime = new Time(endTime.year,endTime.month,endTime.dayOfMonth,endTime.hourOfDay,endTime.minute,endTime.second);

            displayStartTime.month+=1;
            displayEndTime.month+=1;

            titleView.setText(spotInlist.getStreetAddr() + "\nTime: " + displayStartTime.toString() + " ~ " + displayEndTime.toString());
            if(ReservationsArrayList.get(position).review == null) {
            //make invisible
                counterView.setText("!");

            }
            else {
                counterView.setVisibility(View.GONE);

            }

        // 5. retrn rowView
        return rowView;
    }
}
