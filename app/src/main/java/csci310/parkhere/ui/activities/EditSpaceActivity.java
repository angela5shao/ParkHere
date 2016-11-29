package csci310.parkhere.ui.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;
import csci310.parkhere.ui.adapters.PhotoAdapter;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import resource.MyEntry;
import resource.NetworkPackage;
import resource.ParkingSpot;

public class EditSpaceActivity extends AppCompatActivity {
    private ParkingSpot thisParkingSpot;

    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 0;

    private PhotoAdapter photoAdapter;
    private ArrayList<String> selectedPhotos = new ArrayList<>();

    private Button mDoneButton, mUploadPicButton, mEditAddressButton;
    private EditText mAddressText, mDescriptionText;
    private Spinner mCartypeSpinner, mCancelPolicySpinner;

    private LatLng mCurrLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_edit_space);
        ClientController.getInstance().setCurrentActivity(this);


        Intent intent = getIntent();
        thisParkingSpot = (ParkingSpot) intent.getSerializableExtra("SPOT");
        mCurrLocation = new LatLng(thisParkingSpot.getLat(), thisParkingSpot.getLon());
        //

        mAddressText = (EditText) findViewById(R.id.address_text);
        mAddressText.setText(thisParkingSpot.getStreetAddr());
        mDescriptionText = (EditText) findViewById(R.id.description_text);
        mDescriptionText.setText(thisParkingSpot.getDescription());

        mCartypeSpinner = (Spinner) findViewById(R.id.editCartype_spinner);
        mCartypeSpinner.setSelection(thisParkingSpot.getCartype());
        mCancelPolicySpinner = (Spinner) findViewById(R.id.editCancelPolicy_spinner);
        mCancelPolicySpinner.setSelection(thisParkingSpot.cancelpolicy);


        mDoneButton = (Button) findViewById(R.id.editSpaceSave_btn);
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mAddressText.getText().length() == 0) {
                    Toast.makeText(getBaseContext(), "Please enter address.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d("SpaceEdit", "mDoneButton ***** ***** ***");


                EditSpaceTask editProfileTask = new EditSpaceTask(
                        mCurrLocation,
                        mAddressText.getText().toString(),
                        mDescriptionText.getText().toString(),
                        mCartypeSpinner.getSelectedItemPosition(),
                        mCancelPolicySpinner.getSelectedItemPosition(),
                        selectedPhotos
                );

                editProfileTask.execute((Void) null);
            }
        });

        mUploadPicButton = (Button) findViewById(R.id.spacePicUpload_btn);
        mUploadPicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(i, RESULT_LOAD_IMAGE);
                PhotoPicker.builder()
                        .setPhotoCount(6)
                        .setShowCamera(true)
                        .setSelected(selectedPhotos)
                        .start(EditSpaceActivity.this);
            }
        });
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        photoAdapter = new PhotoAdapter(getBaseContext(), selectedPhotos);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));
        recyclerView.setAdapter(photoAdapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getBaseContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        PhotoPreview.builder()
                                .setPhotos(selectedPhotos)
                                .setCurrentItem(position)
                                .start(EditSpaceActivity.this);
                    }
                })
        );

        mEditAddressButton = (Button) findViewById(R.id.changeAddress_btn);
        mEditAddressButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().setCountry("US").build();

                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .setFilter(typeFilter)
                            .build(EditSpaceActivity.this);

                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

                } catch (GooglePlayServicesRepairableException e) {
                } catch (GooglePlayServicesNotAvailableException e) {
                }
            }

        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                mAddressText.setText(place.getAddress());
                mCurrLocation = place.getLatLng();
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getBaseContext(), data);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private class EditSpaceTask extends AsyncTask<Void, Void, ParkingSpot> {
        //        Vector<String> encodedImages;
        private ParkingSpot ps;
        private ProgressDialog progressDialog;
        private final ArrayList<String> encodeImages = new ArrayList<String>();

        EditSpaceTask(LatLng mCurrLocation, String addr, String description, int cartype, int inCancelPolicy, ArrayList<String> inImagePaths) {
            ps = thisParkingSpot;
            ps.setDescription(description);
            ps.setStreetAddr(addr);
            ps.setCartype(cartype);
            ps.setLat(mCurrLocation.latitude);
            ps.setLon(mCurrLocation.longitude);
            ps.cancelpolicy = inCancelPolicy;

            for(String s : inImagePaths) {
                try {
                    Log.d("@@@EDITSPACE", "inImagePaths - "+s);
                    encodeImages.add(readEncodeImage(s));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(EditSpaceActivity.this, R.style.AppTheme);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Editing...");
            progressDialog.show();
        }

        @Override
        protected ParkingSpot doInBackground(Void... params ) {
            ClientController clientController = ClientController.getInstance();
    //            clientController.editParkingSpot(address, description, cartype, cancelpolicy, encodedImages);
            clientController.editParkingSpot(ps);
            NetworkPackage NP = clientController.checkReceived();
            MyEntry<String, Serializable> entry = NP.getCommand();
            String key = entry.getKey();


            Log.d("EditSpaceTask", key);

            if (key.equals("EDITSPACE")) {
                clientController.deleteOldParkingSpotImages(ps);
                NetworkPackage response = clientController.checkReceived();

                if(response.getCommand().getKey().equals("DELETEIMAGESSUCCESS")) {
                    clientController.sendImagesToServer(encodeImages, "PARKINGSPACEIMAGE", thisParkingSpot.getParkingSpotID());
                }

                Log.d("SpaceEdit", "doInBackground 1");
                return ps;
            }
            Log.d("SpaceEdit", "doInBackground 2");
            return null;
        }

        @Override
        protected void onPostExecute(ParkingSpot spot) {
            Log.d("EditSpaceTask", "onPostExecute!!!!! - - - - - ");
            progressDialog.dismiss();
            if(spot!=null){
                finish();

//                ((ProviderActivity)getActivity()).onEditSpace(spot);

            } else{
                Toast.makeText(getBaseContext(), "Error on edit space! Please try again.", Toast.LENGTH_SHORT).show();
                // back to parking spot detail
            }
        }

        public String readEncodeImage(String filepath) throws IOException {
            File imagefile = new File(filepath);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(imagefile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

//        Bitmap bm = BitmapFactory.decodeFile(imagefile);
            Bitmap bm = BitmapFactory.decodeStream(fis);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100 , baos);
            byte[] b = baos.toByteArray();
            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            Log.d("@@@EDITSPACE", "encodedImage = " + encodedImage);
            return encodedImage;
        }
    }
}
