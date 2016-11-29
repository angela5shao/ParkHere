package csci310.parkhere.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;
import csci310.parkhere.ui.activities.ProviderActivity;
import csci310.parkhere.ui.activities.RenterActivity;
import resource.MyEntry;
import resource.NetworkPackage;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PrivateProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PrivateProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrivateProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "USERNAME";
    private static final String ARG_PARAM2 = "PASSWORD";
    private static final String ARG_PARAM3 = "USERLICENSE";
    private static final String ARG_PARAM4 = "USERPLATE";
    private static final String ARG_PARAM5 = "PHONE";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;
    private String mParam4;
    private String mParam5;

    private OnFragmentInteractionListener mListener;
    private ClientController clientController = ClientController.getInstance();

    ImageView _privatProfileImage, _editLogo;
    TextView _usernameText, _pwText, _licenseIDText, _licenseplateText, _phoneText;

    public PrivateProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PrivateProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PrivateProfileFragment newInstance(String param1, String param2, String param3, String param4, String param5) {
        PrivateProfileFragment fragment = new PrivateProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        args.putString(ARG_PARAM5, param5);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
            mParam4 = getArguments().getString(ARG_PARAM4);
            mParam5 = getArguments().getString(ARG_PARAM5);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_private_profile, container, false);




        _privatProfileImage = (ImageView) v.findViewById(R.id.privatProfileImage);
        _editLogo = (ImageView) v.findViewById(R.id.editLogo);
        _usernameText = (TextView) v.findViewById(R.id.usernameText);
        _pwText = (TextView) v.findViewById(R.id.pwText);
        _licenseIDText = (TextView) v.findViewById(R.id.licenseIDText);
        _licenseplateText = (TextView) v.findViewById(R.id.licenseplateText);
        _phoneText = (TextView) v.findViewById(R.id.phoneText);

//        Bitmap bm = BitmapFactory.decodeResource(getResources(),
//                R.mipmap.ic_default_profile_pic);
//        _privatProfileImage.setImageBitmap(bm);

        // Set profile picture
        getProfilePic gpc = new getProfilePic(clientController.getUser().userID);
        String encodedPic = "";
        try {
            encodedPic = gpc.execute((Void) null).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
//        byte[] imageAsBytes = Base64.decode(encodedPic.getBytes(), Base64.DEFAULT);
//        Bitmap bp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        Glide.with(getContext()).load(encodedPic).into(_privatProfileImage);

//        _privatProfileImage.setImageBitmap(bp);




        _editLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfileFragment editProfileFragment = new EditProfileFragment();
                Bundle args = new Bundle();
                args.putString("USERNAME", mParam1 );
                args.putString("PASSWORD", mParam2);
                args.putString("USERLICENSE", mParam3);
                args.putString("USERPLATE", mParam4);
                args.putString("PHONE", mParam5);
                editProfileFragment.setArguments(args);
                Activity ac = getActivity();
                if(ac instanceof RenterActivity)
                    ((RenterActivity) getActivity()).switchToEditProfileFrag(editProfileFragment);
                else if(ac instanceof ProviderActivity)
                    ((ProviderActivity) getActivity()).switchToEditProfileFrag(editProfileFragment);
            }
        });

//        ClientController controller = ClientController.getInstance();
//
//        User user = controller.getUser();
//        if(user != null)
//        {
//            updateUserInfo(user.userName, "", user.userLicense, user.userPlate);
//        } else{
//            //TO DO: add message pop-up that should log in or register
//        }
        _usernameText.setText(mParam1);
        _pwText.setText(mParam2);
        _licenseIDText.setText(mParam3);
        _licenseplateText.setText(mParam4);
        _phoneText.setText(mParam5);
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

//    public void updateUserInfo(String inUsername, String inPw, String inLicenseID, String inLicensePlate) {
//        _usernameText.setText(inUsername);
//        _pwText.setText(inPw);
//        _licenseIDText.setText(inLicenseID);
//        _licenseplateText.setText(inLicensePlate);
//    }

    // return edit ImageView for parent fragmaent
//    public ImageView getEditLogo() {
//        ImageView _editLogo = (ImageView) getActivity().findViewById(R.id.editLogo);
//        return _editLogo;
//    }

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

    private class getProfilePic extends AsyncTask<Void, Void, String> {
        private final long userID;

        getProfilePic(long userID){
            this.userID = userID;
        }
        @Override
        protected void onPreExecute(){
        }
        @Override
        protected String doInBackground(Void... params ){

//            clientController.getProfilePic(mUsername);
            clientController.getProfilePic(userID);

            NetworkPackage NP = clientController.checkReceived();
            MyEntry<String, Serializable> entry = NP.getCommand();

            String key = entry.getKey();
            Object value = entry.getValue();
            String encodedPic = null;
            if(key.equals("PROFILEPICIMAGE")){
                encodedPic = (String)value;
            }
            return encodedPic;
        }
        @Override
        protected void onPostExecute(String profilePic) {
            // TODO set profile image
//            _profilePic.setImageBitmap();
        }

    }
}
