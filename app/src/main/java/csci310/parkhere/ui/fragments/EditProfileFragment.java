package csci310.parkhere.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;
import csci310.parkhere.ui.activities.ProviderActivity;
import csci310.parkhere.ui.activities.RenterActivity;
import resource.MyEntry;
import resource.NetworkPackage;
import resource.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment {
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

    private int RESULT_LOAD_IMAGE = 87;
    private String encodedImage;

    private OnFragmentInteractionListener mListener;

    ImageView _privatProfileImage;
    Button _btn_upload_image, _btn_save;
    EditText _usernameText, _pwText, _licenseIDText, _licenseplateText, _phoneText;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2, String param3, String param4, String param5) {
        EditProfileFragment fragment = new EditProfileFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        final ClientController controller = ClientController.getInstance();

        _privatProfileImage = (ImageView) v.findViewById(R.id.privatProfileImage);

        _usernameText = (EditText) v.findViewById(R.id.edit_usernameText);
        _usernameText.setText(controller.getUser().userName);
        _usernameText.setFocusable(false);
        _usernameText.setEnabled(false);
        _usernameText.setCursorVisible(false);
        _usernameText.setKeyListener(null);
        _usernameText.setBackgroundColor(Color.TRANSPARENT);



        _pwText = (EditText) v.findViewById(R.id.edit_pwText);

        _licenseIDText = (EditText) v.findViewById(R.id.edit_licenseIDText);
        _licenseIDText.setText(controller.getUser().userLicense);

        _licenseplateText = (EditText) v.findViewById(R.id.edit_licenseplateText);
        _licenseplateText.setText(controller.getUser().userPlate);


        _phoneText = (EditText) v.findViewById(R.id.phoneText);
        _phoneText.setText(controller.getUser().userPhone);

        updateUserInfo(mParam1, mParam2, mParam3, mParam4, mParam5);
        _btn_upload_image = (Button) v.findViewById(R.id.btn_upload_image);
        _btn_upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        _btn_save = (Button) v.findViewById(R.id.btn_save);
        _btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_pwText.getText().length() < 10)
                {
                    Toast.makeText(getContext(), "Please input password longer than 10 digits", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(_licenseplateText.getText().length() == 0)
                {
                    _licenseplateText.setText("#######");
                }

                if(_phoneText.getText().length() != 10)
                {
                    Toast.makeText(getContext(), "Please input valid phone numebr", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(controller.getUser().userType && _licenseplateText.getText().toString().equals("#######"))
                {
                    Toast.makeText(getContext(), "Please input your licence plate to proceed in renter mode", Toast.LENGTH_SHORT).show();
                    return;
                }




                if(_licenseIDText.getText().length() == 0 || _phoneText.getText().length() == 0 )
                {
                    Toast.makeText(getContext(), "Please fill in all required field", Toast.LENGTH_SHORT).show();
                    return;
                }

                EditProfileTask editProfileTask = new EditProfileTask(_usernameText.getText().toString(),_pwText.getText().toString(), _licenseIDText.getText().toString(), _licenseplateText.getText().toString(), _phoneText.getText().toString());
                editProfileTask.execute((Void)null);

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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }




    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContext().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            _privatProfileImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            Log.d("!!!Edit Profile ", "picturePath = "+picturePath);

//            SubsamplingScaleImageView newImage = new SubsamplingScaleImageView(getContext());
//            newImage.setImage(ImageSource.uri(picturePath));

            Bitmap bitmap = null;
            try {
//                BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(myStream, false);
//                bitmap = decoder.decodeRegion(new Rect(10, 10, 50, 50), null);
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

//            _privatProfileImage.setImageBitmap(bitmap);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();

            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }
    }

    public void updateUserInfo(String inUsername, String inPw, String inLicenseID, String inLicensePlate, String inPhone) {
        _usernameText.setHint("Can not change");
        _pwText.setHint(inPw);
        _licenseIDText.setHint(inLicenseID);
        _licenseplateText.setHint(inLicensePlate);
        _phoneText.setHint(inPhone);
    }

    private class EditProfileTask extends AsyncTask<Void, Void, Boolean> {
        String username;
        String pwText;
        String licenseIdText;
        String licenseplateText;
        String phoneText;

        EditProfileTask(String username, String pwText, String licenseIdText, String licenseplateText, String phoneText){
            this.username = username;
            this.pwText = pwText;
            this.licenseIdText = licenseIdText;
            this.licenseplateText = licenseplateText;
            this.phoneText = phoneText;
            doInBackground((Void) null);
        }

//        @Override
//        protected void onPreExecute(){
//            clientController.providerToshowSpacesDetail = true;
//        }

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
                if(ac instanceof RenterActivity)
                    ((RenterActivity) getActivity()).switchToPrivateProfileFrag(privateProfileFragment);
                else if(ac instanceof ProviderActivity)
                    ((ProviderActivity) getActivity()).switchToPrivateProfileFrag(privateProfileFragment);
            } else{
                Toast.makeText(getContext(), "Error on edit profile! Please try again.", Toast.LENGTH_SHORT).show();
                // back to reservation detail
            }
        }
    }
}
