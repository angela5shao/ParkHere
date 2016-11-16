package csci310.parkhere.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;
import csci310.parkhere.resource.CarType;
import resource.MyEntry;
import resource.NetworkPackage;
import resource.ParkingSpot;
import resource.TimeInterval;
import resource.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SpaceEditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SpaceEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpaceEditFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private ParkingSpot thisParkingSpot;

    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 0;
    private int RESULT_LOAD_IMAGE = 1;

    private Button mDoneButton, mUploadPicButton, mEditAddressButton;
    private EditText mAddressText, mDescriptionText;
    private ImageView mSpacePic;
    private SubsamplingScaleImageView mSpacePic2;
    private Spinner mCartypeSpinner, mCancelPolicySpinner;
    private String encodedImage;

    private LatLng mCurrLocation;

    private OnFragmentInteractionListener mListener;

    public SpaceEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SpaceEditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SpaceEditFragment newInstance(String param1, String param2) {
        SpaceEditFragment fragment = new SpaceEditFragment();
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
            thisParkingSpot = (ParkingSpot) getArguments().getSerializable("spot");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_space_edit, container, false);

        mAddressText = (EditText) v.findViewById(R.id.address_text);
        mAddressText.setText(thisParkingSpot.getStreetAddr());
        mDescriptionText = (EditText) v.findViewById(R.id.description_text);
        mDescriptionText.setText(thisParkingSpot.getDescription());
        mSpacePic = (ImageView) v.findViewById(R.id.parkingSpotImage);
        // TODO: Set image of parking spot
        mSpacePic2 = (SubsamplingScaleImageView) v.findViewById(R.id.imageView);

        mCartypeSpinner = (Spinner)v.findViewById(R.id.editCartype_spinner);
        mCartypeSpinner.setSelection(thisParkingSpot.getCartype());
        mCancelPolicySpinner = (Spinner)v.findViewById(R.id.editCancelPolicy_spinner);
        mCancelPolicySpinner.setSelection(thisParkingSpot.cancelpolicy);

        mDoneButton = (Button)v.findViewById(R.id.editSpaceSave_btn);
        mUploadPicButton = (Button)v.findViewById(R.id.spacePicUpload_btn);
        mEditAddressButton = (Button)v.findViewById(R.id.changeAddress_btn);

        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAddressText.getText().length() == 0) {
                    Toast.makeText(getContext(), "Please enter address.", Toast.LENGTH_SHORT).show();
                    return;
                }

                EditSpaceTask editProfileTask = new EditSpaceTask(mAddressText.getText().toString(),
                        mDescriptionText.getText().toString(),
                        mCartypeSpinner.getSelectedItem().toString(),
                        mCancelPolicySpinner.getSelectedItemPosition(),
                        encodedImage //ImageSource.uri(picturePath)
                );
                editProfileTask.execute((Void)null);
            }
        });

        mUploadPicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });


        mEditAddressButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().setCountry("US").build();

                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .setFilter(typeFilter)
                                    .build(getActivity());

                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

                } catch (GooglePlayServicesRepairableException e) {
                } catch (GooglePlayServicesNotAvailableException e) {
                }
            }

        });

        // Inflate the layout for this fragment
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        Log.d("ONACTIVITYRESULT", "START");
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getContext(), data);
                mAddressText.setText(place.getAddress());
                mCurrLocation = place.getLatLng();
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getContext(), data);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        else if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContext().getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

//            Bitmap bmp = intent.getExtras().get("data");
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
//            byte[] byteArray = stream.toByteArray();

            Bitmap bitmap = null;
            try {
//                BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(myStream, false);
//                bitmap = decoder.decodeRegion(new Rect(10, 10, 50, 50), null);
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
            } catch (IOException e) {

            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

//            ImageView imageView = (ImageView) findViewById(R.id.imgView);
//            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

//            Bitmap d = new BitmapDrawable(ctx.getResources() , w.photo.getAbsolutePath()).getBitmap();
//            int nh = (int) ( d.getHeight() * (512.0 / d.getWidth()) );
//            Bitmap scaled = Bitmap.createScaledBitmap(d, 512, nh, true);
//            iv.setImageBitmap(scaled);

//            mSpacePic2.setImage(ImageSource.resource(R.drawable.monkey));
//// ... or ...
//            mSpacePic2.setImage(ImageSource.asset("map.png"))
//// ... or ...
//            mSpacePic2.setImage(ImageSource.uri("/sdcard/DCIM/DSCM00123.JPG"));
//            mSpacePic2.setImage(ImageSource.bitmap(bitmap));
            mSpacePic2.setImage(ImageSource.uri(picturePath));

//            mSpacePic.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            System.out.println("- - - SpaceEditFrag: setImage - -");
        }
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

    private class EditSpaceTask extends AsyncTask<Void, Void, ParkingSpot> {
        String address;
        String description;
        String cartype;
        int cancelpolicy;
        String encodedImage;

        EditSpaceTask(String addr, String description, String cartype, int inCancelPolicy, String encodedImage){
            this.address = addr;
            this.description = description;
            this.cartype = cartype;
            this.cancelpolicy = inCancelPolicy;
            this.encodedImage = encodedImage;
        }

        @Override
        protected ParkingSpot doInBackground(Void... params ){
            ClientController clientController = ClientController.getInstance();
            clientController.editParkingSpot(address, description, cartype, cancelpolicy, encodedImage);
            NetworkPackage NP = clientController.checkReceived();
            MyEntry<String, Serializable> entry = NP.getCommand();
            String key = entry.getKey();
            Object value = entry.getValue();
            if(key.equals("EDITPARKINGSPOT")){
                ParkingSpot spot = (ParkingSpot)value;
                return spot;
            }
            return null;
        }

        @Override
        protected void onPostExecute(ParkingSpot spot) {

            if(spot!=null){
                ((ProviderActivity)getActivity()).onEditSpace(spot);
            } else{
                Toast.makeText(getContext(), "Error on edit space! Please try again.", Toast.LENGTH_SHORT).show();
                // back to parking spot detail
            }
        }
    }

}
