package csci310.parkhere.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import csci310.parkhere.R;

/**
 * Created by angela02pd2014 on 10/31/16.
 */

public class CustomSpacesListAdapter extends ArrayAdapter {
    private final Activity context;
    private final ArrayList<String> itemname;
//    private final ArrayList<String> rentername;
//    private final Integer[] imgid;

    public CustomSpacesListAdapter(Activity context, ArrayList<String> itemname) {
        super(context, R.layout.fragment_reservations, itemname);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.itemname = itemname;
//        this.imgid = imgid;
//        this.rentername = renters;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.fragment_reservations, null, true);

//        TextView txtTitle = (TextView) rowView.findViewById(R.id.reservations_address);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
//        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);

//        txtTitle.setText(itemname.get(position));
//        imageView.setImageResource(imgid[position]);
//        extratxt.setText("Reserved by: "+rentername.get(position));
        return rowView;
    };
}
