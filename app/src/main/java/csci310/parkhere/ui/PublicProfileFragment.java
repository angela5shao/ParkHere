package csci310.parkhere.ui;


/**
 * Created by yubowang on 11/6/16.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;
import resource.User;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PublicProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PublicProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PublicProfileFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "FIRSTNAME";
    private static final String ARG_PARAM2 = "PHONENUMBER";
    private static final String ARG_PARAM3 = "REVIEWSCORE";
    private static final String ARG_PARAM4 = "COMMENTS";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;
    private String [] mParam4;

    private OnFragmentInteractionListener mListener;

    ImageView _privatProfileImage, _editLogo;
    TextView _usernameText, _pwText, _licenseIDText, _licenseplateText;

    public PublicProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PublicProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PublicProfileFragment newInstance(String param1, String param2, String param3, String param4) {
        PublicProfileFragment fragment = new PublicProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);

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
            mParam4 = getArguments().getStringArray(ARG_PARAM4);
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

        Bitmap bm = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_default_profile_pic);
        Bitmap conv_bm = getRoundedBitmap(bm);
        _privatProfileImage.setImageBitmap(conv_bm);

        _editLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfileFragment editProfileFragment = new EditProfileFragment();
                Bundle args = new Bundle();
                args.putString("FIRSTNAME", mParam1 );
                args.putString("PHONENUMBER", mParam2);
                args.putString("REVIEWSCORE", mParam3);
                args.putStringArray("COMMENTS",mParam4);
//                args.putString("COMMENTS", mParam4);
                editProfileFragment.setArguments(args);
                Activity ac = getActivity();
                if(ac instanceof  RenterActivity)
                    ((RenterActivity) getActivity()).switchToEditProfileFrag();
                else if(ac instanceof  ProviderActivity)
                    ((ProviderActivity) getActivity()).switchToEditProfileFrag();
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

    public static Bitmap getRoundedBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
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
}
