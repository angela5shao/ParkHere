package csci310.parkhere.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;
import resource.MyEntry;
import resource.NetworkPackage;
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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 0;

    private Button mDoneButton, mUploadPicButton, mEditAddressButton;
    private EditText mAddressText, mDescriptionText;
    private ImageView mSpacePic;
    private Spinner mCartypeSpinner, mCancelPolicySpinner;

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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_space_edit, container, false);

        mAddressText = (EditText) v.findViewById(R.id.address_text);
        mDescriptionText = (EditText) v.findViewById(R.id.description_text);
        mSpacePic = (ImageView) v.findViewById(R.id.parkingSpotImage);
        mCartypeSpinner = (Spinner)v.findViewById(R.id.editCartype_spinner);
        mCancelPolicySpinner = (Spinner)v.findViewById(R.id.editCancelPolicy_spinner);

        mDoneButton = (Button)v.findViewById(R.id.editSpaceSave_btn);
        mUploadPicButton = (Button)v.findViewById(R.id.spacePicUpload_btn);
        mEditAddressButton = (Button)v.findViewById(R.id.changeAddress_btn);

        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(_pwText.getText().length() < 10) {
//                    Toast.makeText(getContext(), "Please input password longer than 10 digits", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                // Convert into TimeInterval
                TimeInterval time = null;

                EditSpaceTask editProfileTask = new EditSpaceTask(mAddressText.getText().toString(),
                        mDescriptionText.getText().toString(),
                        mCartypeSpinner.getSelectedItem().toString(),
                        mCancelPolicySpinner.getSelectedItem().toString(),
                        mSpacePic,
                        time);
                editProfileTask.execute((Void)null);

            }
        });

        mUploadPicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // TODO
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

    private class EditSpaceTask extends AsyncTask<Void, Void, Boolean> {
        String address;
        String description;
        String cartype;
        String cancelpolicy;
        ImageView picture;
        TimeInterval timeInterval;

        EditSpaceTask(String addr, String description, String cartype, String cancelpolicy, ImageView pic, TimeInterval time){
            this.address = addr;
            this.description = description;
            this.cartype = cartype;
            this.cancelpolicy = cancelpolicy;
            this.picture = pic;
            this.timeInterval = time;
            doInBackground((Void) null);
        }

        @Override
        protected Boolean doInBackground(Void... params ){
            ClientController clientController = ClientController.getInstance();
            clientController.editProfile(username, pwText, licenseIdText, licenseplateText, phoneText);
            NetworkPackage NP = clientController.checkReceived();
            MyEntry<String, Serializable> entry = NP.getCommand();
            String key = entry.getKey();
            Object value = entry.getValue();
            if(key.equals("EDITPROFILE")){
                User user = (User) value;
                clientController.setUser(user);
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            if(result){
                PrivateProfileFragment privateProfileFragment = new PrivateProfileFragment();
                Bundle args = new Bundle();
                args.putString("USERNAME", _usernameText.getText().toString() );
                args.putString("PASSWORD", "");
                args.putString("USERLICENSE", _licenseIDText.getText().toString());
                args.putString("USERPLATE", _licenseplateText.getText().toString());
                args.putString("PHONE", _phoneText.getText().toString());
                privateProfileFragment.setArguments(args);
                Activity ac = getActivity();
                if(ac instanceof  RenterActivity)
                    ((RenterActivity) getActivity()).switchToPrivateProfileFrag(privateProfileFragment);
                else if(ac instanceof  ProviderActivity)
                    ((ProviderActivity) getActivity()).switchToPrivateProfileFrag(privateProfileFragment);
            } else{
                Toast.makeText(getContext(), "Error on edit profile! Please try again.", Toast.LENGTH_SHORT).show();
                // back to reservation detail
            }
        }
    }

}
