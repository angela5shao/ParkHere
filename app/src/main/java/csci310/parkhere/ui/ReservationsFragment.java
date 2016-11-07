package csci310.parkhere.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;
import resource.ParkingSpot;
import resource.Reservation;
import resource.Time;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReservationsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReservationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservationsFragment extends Fragment implements AdapterView.OnItemClickListener  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ArrayAdapter passedListAdapter, futureListAdapter;
    ListView _futureList, _passedList;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reservations, container, false);
        _futureList = (ListView) v.findViewById(R.id.futureList);
        _passedList = (ListView) v.findViewById(R.id.passedList);

        ClientController controller = ClientController.getInstance();
        ArrayList<Reservation> original_reservations = controller.reservations;


        Calendar cal = Calendar.getInstance();
//        String timeStamp = new SimpleDateFormat("d-M-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
//        System.out.println(timeStamp);
        System.out.println(cal.getTime());
        Time currentTime = new Time(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH),cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));


        ArrayList<Reservation> reservations = sortReservationListByStartTime(original_reservations);
        ArrayList<Reservation> passedReservations = new ArrayList<Reservation>();
        ArrayList<Reservation> futureReservations = new ArrayList<Reservation>();
//         ArrayList<Reservation> allReservations = new ArrayList<Reservation>();
        for(int i = 0; i<reservations.size();i++){
            if(currentTime.compareTo(reservations.get(i).getReserveTimeInterval().endTime)>=0){
                passedReservations.add(reservations.get(i));
            }
            else{
                futureReservations.add(reservations.get(i));
            }
        }

        ArrayList<String> futureListString = new ArrayList<String>();
        ArrayList<String> passedListString = new ArrayList<String>();
        for(int i = 0 ; i < futureReservations.size(); i++) {
            Log.d("FETCHRESERVATIONLIST", "add into future list");
            ParkingSpot spotInlist = futureReservations.get(i).getSpot();
            Time startTIme = futureReservations.get(i).getReserveTimeInterval().startTime;
            Time endTime = futureReservations.get(i).getReserveTimeInterval().endTime;

            futureListString.add(spotInlist.getStreetAddr() + " Time: " + startTIme.toString() + "-" + endTime.toString());
        }
        for(int i = 0 ; i < passedReservations.size(); i++) {
            Log.d("FETCHRESERVATIONLIST","add into passed list");
            ParkingSpot spotInlist = passedReservations.get(i).getSpot();
            Time startTIme = passedReservations.get(i).getReserveTimeInterval().startTime;
            Time endTime = passedReservations.get(i).getReserveTimeInterval().endTime;

            passedListString.add(spotInlist.getStreetAddr() + " Time: " + startTIme.toString() + "-" + endTime.toString());
        }

        futureListAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, futureListString);
        _futureList.setAdapter(futureListAdapter);
        _futureList.setOnItemClickListener(this);

        passedListAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, passedListString);
        _passedList.setAdapter(passedListAdapter);
        _passedList.setOnItemClickListener(this);

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
        void onReservationSelected(int resPosition, boolean ifCanCancel);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        boolean ifCanCancel = true;
        if(view.getId()==R.id.passedList) ifCanCancel = false;
        Log.d("view.getId() = ", getContext().getString(view.getId()));
        mListener.onReservationSelected(position, ifCanCancel);
        System.out.println("CLICKED on Reservation: " + position);
    }

//    public void refresh(){
//        ClientController clientcontroller = ClientController.getInstance();
//        mAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, clientcontroller.reservations);
//    }


    public ArrayList<Reservation> sortReservationListByStartTime(ArrayList<Reservation> reservationlist) {
        if(reservationlist != null) {
            // ArrayList<ParkingSpot> searchResultList
            Collections.sort(reservationlist, new Comparator<Reservation>() {
                public int compare(Reservation r1, Reservation r2) {
                    return r1.getReserveTimeInterval().startTime.compareTo(r2.getReserveTimeInterval().startTime);
                }
            });
        }
        return reservationlist;
    }
}
