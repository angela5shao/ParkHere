package csci310.parkhere.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import resource.MyEntry;
import resource.NetworkPackage;
import resource.ParkingSpot;

public class AddSpaceActivity extends AppCompatActivity {
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 100;

    Button _btn_add_address, _btn_upload_image, _btn_confirm;
    Spinner _cartypeSpinner, _cancelPolicySpinner;
    EditText _in_descrip;
    TextView _addressText;

    private PhotoAdapter photoAdapter;
    private ArrayList<String> selectedPhotos = new ArrayList<>();

    ProviderAddSpaceTask addSpaceTask = null;

    public LatLng curr_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_space);

        _addressText = (TextView)findViewById(R.id.addressText);
        _cartypeSpinner = (Spinner)findViewById(R.id.cartypeSpinner);
        _cancelPolicySpinner = (Spinner)findViewById(R.id.cancelPolicySpinner);

        _btn_add_address = (Button)findViewById(R.id.btn_add_address);
        _btn_add_address.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().setCountry("US").build();

                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .setFilter(typeFilter)
                                    .build(AddSpaceActivity.this);

                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }

        });
        // Inflate the layout for this fragment

        _in_descrip = (EditText)findViewById(R.id.in_descrip);

        _btn_upload_image =(Button)findViewById(R.id.btn_upload_image);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        photoAdapter = new PhotoAdapter(getBaseContext(), selectedPhotos);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));
        recyclerView.setAdapter(photoAdapter);
//        _imageGridview = (GridView)v.findViewById(R.id.gridview);
//        ImageAdapter adapter=new ImageAdapter(getContext(), imagesPathList);
//        _imageGridview.setAdapter(adapter);

        _btn_upload_image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                PhotoPicker.builder()
                        .setPhotoCount(6)
                        .setShowCamera(true)
                        .setSelected(selectedPhotos)
                        .start(AddSpaceActivity.this);
            }
        });

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getBaseContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        PhotoPreview.builder()
                                .setPhotos(selectedPhotos)
                                .setCurrentItem(position)
                                .start(AddSpaceActivity.this);
                    }
                })
        );

        _btn_confirm = (Button)findViewById(R.id.btn_confirm);
        _btn_confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d("AddSpace", " onClick!");

                addSpaceTask = new ProviderAddSpaceTask(_addressText.getText().toString(),
                        _in_descrip.getText().toString(),
                        _cartypeSpinner.getSelectedItem().toString(),
                        _cancelPolicySpinner.getSelectedItemPosition());
                addSpaceTask.execute((Void) null);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("ONACTIVITYRESULT", "START");
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                List<String> photos = null;
                if (data != null) {
                    photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);

                    for (String s : photos) {
                        Log.d("!!!!!!!!!!!", "data.getStringArrayListExtra = " + s);
                    }
                }
                selectedPhotos.clear();

                if (photos != null) {

                    selectedPhotos.addAll(photos);
                }
                photoAdapter.notifyDataSetChanged();
            }
        }

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getBaseContext(), data);

                Log.i("AddSpaceFragment ", "Place: " + place.getName());
                Log.i("AddSpaceFragment ", "Place LatLng: " + place.getLatLng().toString());

                if(place != null) {
                    _addressText.setText(place.getAddress());
                    curr_location = place.getLatLng();
                }

                Log.d("GOOGLE MAP PLACE", "After " + (curr_location == null));


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getBaseContext(), data);
                // TODO: Handle the error.
//                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private class ProviderAddSpaceTask extends AsyncTask<Void, Void, Boolean> {
        private final String mAddressText;
        private final String mDescrip;
        private final String mCarType;
        private final int mCancelPolicy;

        ProgressDialog progressDialog;

        ProviderAddSpaceTask(String inAddressText, String inDescrip, String inCarType, int inCancelPolicy) {
            Log.d("@@@ADDSPACE", "add space construct");

            mAddressText = inAddressText;
            mDescrip = inDescrip;
            mCarType = inCarType;
            mCancelPolicy = inCancelPolicy;

            System.out.println(mAddressText);
            System.out.println(mDescrip);
            System.out.println(mCarType);
            System.out.println(mCancelPolicy);
        }

        @Override
        protected void onPreExecute() {
            Log.d("@@@ADDSPACE", "add space pre execute");

            //Display a progress dialog
            progressDialog = ProgressDialog.show(
                    AddSpaceActivity.this, "",
                    "Adding", true);
//            progressDialog.setIndeterminate(true);
//            progressDialog.setMessage("Adding...");
//            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.d("@@@ADDSPACE", "add space");
            if(curr_location == null) {
                Log.d("@@@ADDSPACE", "doInBackground curr_location == null");
            }
            else {
                Log.d("@@@ADDSPACE", "curr_location == "+curr_location.toString());
            }

            ClientController clientController = ClientController.getInstance();
            clientController.addSpace(curr_location, mAddressText, mDescrip, mCarType, mCancelPolicy);

            NetworkPackage NP = clientController.checkReceived();
            MyEntry<String, Serializable> entry = NP.getCommand();
            String key = entry.getKey();
            Object value = entry.getValue();

            if (key.equals("ADDSPACE")) {
                ParkingSpot spot = (ParkingSpot) value;
                Log.d("Result", "add space " + String.valueOf(spot.getParkingSpotID()));

                clientController.parkingSpots.add(spot);

                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Log.d("ONPOSTEXECUTE", "success");
                progressDialog.dismiss();
                finish();
            } else {
                progressDialog.dismiss();
                Toast.makeText(getBaseContext(), "Error on add space! Please try again.", Toast.LENGTH_SHORT).show();
                // back to add space?
            }
        }
    }
}
