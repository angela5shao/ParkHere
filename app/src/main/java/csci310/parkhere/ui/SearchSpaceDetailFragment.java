package csci310.parkhere.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;
import resource.ParkingSpot;
import resource.Time;
import resource.TimeInterval;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchSpaceDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchSpaceDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchSpaceDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View mView;

    // TODO: Rename and change types of parameters
    private int mPosition;
    private String mParam2;

    private ParkingSpot mParkingSpot;

//    private String mProvidername;
//    private ArrayList<TimeInterval> mTimeIntervals;
//    private String mAddress;
//    private String mDescription;
////    private String mZipcode;
//    private Integer mCarType;
//    private boolean mStatus;
//    private int mCancelPolicy;
//    private double mPrice;

    private OnFragmentInteractionListener mListener;

    Button _searchspacedetail_reservebutton;

    public SearchSpaceDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchSpaceDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchSpaceDetailFragment newInstance(int param1, String param2) {
        SearchSpaceDetailFragment fragment = new SearchSpaceDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_search_space_detail, container, false);

        _searchspacedetail_reservebutton=(Button)mView.findViewById(R.id.searchspacedetail_reservebutton);
        _searchspacedetail_reservebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PaymentActivity.class);
                startActivityForResult(intent, 11);
            }
        });

        // Get ParkingSpot from controller
        ClientController controller = ClientController.getInstance();
        mParkingSpot = controller.searchResults.searchResultList.get(mPosition);

        // Populate fields with data
        ((TextView) mView.findViewById(R.id.searchspacedetail_address)).setText(mParkingSpot.getStreetAddr());
        ((TextView) mView.findViewById(R.id.searchspacedetail_price)).setText(new Double(mParkingSpot.search_price).toString());
//        ((TextView) mView.findViewById(R.id.searchspacedetail_rating)).setText(new Double(mParkingSpot.rating).toString());
        Time sTime = mParkingSpot.getTimeIntervalList().get(0).startTime;
        Time eTime = mParkingSpot.getTimeIntervalList().get(0).endTime;
        ((TextView) mView.findViewById(R.id.searchspacedetail_starttime)).setText(sTime.toString());
        ((TextView) mView.findViewById(R.id.searchspacedetail_endtime)).setText(eTime.toString());
        ((TextView) mView.findViewById(R.id.searchspacedetail_cartype)).setText(new Integer(mParkingSpot.getCartype()).toString());
        ((TextView) mView.findViewById(R.id.searchspacedetail_cancelpolicy)).setText(new Integer(mParkingSpot.cancelpolicy).toString());

        return mView;
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
}
