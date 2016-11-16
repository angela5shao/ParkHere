package csci310.parkhere.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import csci310.parkhere.R;

/**
 * Created by angela02pd2014 on 11/16/16.
 */

public class CustomSearchListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final Integer[] imgid;

    public CustomSearchListAdapter(Activity context, String[] itemname, Integer[] imgid) {
        super(context, R.layout.mylist, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null,true);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView itemNameView = (TextView) rowView.findViewById(R.id.Itemname);

        imageView.setImageResource(imgid[position]);
        itemNameView.setText(itemname[position]);
        return rowView;
    };
}
