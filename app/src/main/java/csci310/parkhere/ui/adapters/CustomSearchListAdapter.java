package csci310.parkhere.ui.adapters;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import csci310.parkhere.R;

/**
 * Created by angela02pd2014 on 11/16/16.
 */

public class CustomSearchListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final Uri[] imgUri;

    public CustomSearchListAdapter(Activity context, String[] itemname, Uri[] uri) {
        super(context, R.layout.mylist, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgUri = uri;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null,true);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.Itemname);

        txtTitle.setText(itemname[position]);
//        imageView.setImageResource(imgid[position]);
//        imageView.setImageURI(imgUri[position]);
        Glide.with(context).load(imgUri[position]).into(imageView);

        return rowView;
    };
}
