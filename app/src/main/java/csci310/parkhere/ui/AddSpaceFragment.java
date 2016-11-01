package csci310.parkhere.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddSpaceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddSpaceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddSpaceFragment extends Fragment {
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private long mParam1;
    private String mParam2;

    Button _btn_add_address, _btn_upload_image, _btn_confirm;
    Spinner _cartypeSpinner, _cancelPolicySpinner;
    EditText  _in_descrip;
    TextView _addressText;

    private String description, curr_cartype;


    public  LatLng curr_location;

    private OnFragmentInteractionListener mListener;

    public AddSpaceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddSpaceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddSpaceFragment newInstance(long param1, String param2) {
        AddSpaceFragment fragment = new AddSpaceFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getLong(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_space, container, false);

        _addressText = (TextView)v.findViewById(R.id.addressText);
        _cartypeSpinner = (Spinner)v.findViewById(R.id.cartypeSpinner);
        _cancelPolicySpinner = (Spinner)v.findViewById(R.id.cancelPolicySpinner);

        curr_cartype = _cartypeSpinner.getSelectedItem().toString();

        _btn_add_address = (Button)v.findViewById(R.id.btn_add_address);
        _btn_add_address.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    Log.d("AUTOCOMPLETE", "ONCLICK");
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(getActivity());
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                    Log.d("AUTOCOMPLETE", "ONCLICK LAST");

                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }

        });
        // Inflate the layout for this fragment

        _in_descrip = (EditText)v.findViewById(R.id.in_descrip);
        description = _in_descrip.getText().toString();

        _btn_confirm=(Button)v.findViewById(R.id.btn_confirm);
        _btn_confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ClientController clientController = ClientController.getInstance();
                clientController.addSpace(curr_location,_addressText.getText().toString(), _in_descrip.getText().toString() );

            }
        });

        return v;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("ONACTIVITYRESULT", "START");
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getContext(), data);

                Log.d("GOOGLE MAP PLACE", "Here");
                _addressText.setText(place.getAddress());
                curr_location = place.getLatLng();
                Log.d("GOOGLE MAP PLACE", "After " + (curr_location == null));


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getContext(), data);
                // TODO: Handle the error.
//                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
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
}
