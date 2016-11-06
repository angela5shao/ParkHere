package csci310.parkhere.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;
import resource.ParkingSpot;
import resource.Reservation;
import resource.Time;
import resource.TimeInterval;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReservationsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReservationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservationsFragment extends ListFragment implements AdapterView.OnItemClickListener  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ArrayAdapter mAdapter;

    public ReservationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReservationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReservationsFragment newInstance(String param1, String param2) {
        ReservationsFragment fragment = new ReservationsFragment();
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

//        Bundle
//        curr_lat = b.getDouble("LAT");
//        curr_long = b.getDouble("LONG");
//        address = b.getString("ADDRESS");
//        start_time = b.getString("START_TIME");
//        end_time = b.getString("END_TIME");
//        renter_username = b.getString("RENTER");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reservations, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            setText(bundle.getString("link"));
//        }
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Planets, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

        ClientController controller = ClientController.getInstance();
        ArrayList<Reservation> reservations = controller.reservations;
        ParkingSpot spot = new ParkingSpot(controller.getUser().userID, null, 0, 0, "Tuscany 101, 10 Figueroa", "", "90007", 0x0001,0);
        Time start =  new Time(2016, 2, 29, 4, 30, 0);
			Time end = new Time(2016, 2 ,29, 5, 0,0);
			Time start1 =  new Time(2016, 12, 29, 4, 0, 0);
			Time end1 = new Time(2016,12,29, 5, 0,0);
			TimeInterval timeInterval1 = new TimeInterval(start, end);
			TimeInterval timeInterval2 = new TimeInterval(start1, end1);
        reservations.add(new Reservation(0123, controller.getUser().userID, 789, spot, timeInterval1, 50.00, false));
        reservations.add(new Reservation(0123, controller.getUser().userID, 789, spot, timeInterval2, 75.00, false));

        ArrayList<String> listString = new ArrayList<>();
        for(int i = 0 ; i < reservations.size(); i++)
        {
            Log.d("FETCHRESERVATIONLIST","add into list");
            ParkingSpot spotInlist = reservations.get(i).getSpot();
            Time startTIme = reservations.get(i).getReserveTimeInterval().startTime;
            Time endTime = reservations.get(i).getReserveTimeInterval().endTime;

            listString.add(spotInlist.getStreetAddr() + " Time: " + startTIme.toString() + "-" + endTime.toString());
        }


        mAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listString);
        setListAdapter(mAdapter);
        getListView().setOnItemClickListener(this);
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
        void onReservationSelected(int resPosition);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        mListener.onReservationSelected(position);
        System.out.println("CLICKED on Reservation: " + position);
    }

    public void refresh(){
        ClientController clientcontroller = ClientController.getInstance();
        mAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, clientcontroller.reservations);
    }

}
