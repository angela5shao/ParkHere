package csci310.parkhere.ui.fragments;

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
import csci310.parkhere.ui.adapters.CustomReservationListAdapter;
import resource.ParkingSpot;
import resource.Reservation;
import resource.Review;
import resource.Time;
import resource.TimeInterval;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RenterReservationsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RenterReservationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RenterReservationsFragment extends Fragment implements AdapterView.OnItemClickListener  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ArrayAdapter futureListAdapter;
    private CustomReservationListAdapter passedListAdapter;
    ListView _futureList, _passedList;


    ArrayList<Reservation> passedReservations;
    ArrayList<Reservation> futureReservations;

    public RenterReservationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RenterReservationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RenterReservationsFragment newInstance(String param1, String param2) {
        RenterReservationsFragment fragment = new RenterReservationsFragment();
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
        View v = inflater.inflate(R.layout.fragment_renter_reservations, container, false);
        _futureList = (ListView) v.findViewById(R.id.futureList);
        _passedList = (ListView) v.findViewById(R.id.passedList);

        ClientController controller = ClientController.getInstance();

        ArrayList<Reservation> original_reservations = controller.renterReservations;


        Calendar cal = Calendar.getInstance();
//        String timeStamp = new SimpleDateFormat("d-M-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
//        System.out.println(timeStamp);
        System.out.println(cal.getTime());
        Time currentTime = new Time(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH),cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));


        ArrayList<Reservation> reservations = sortReservationListByStartTime(original_reservations);
        passedReservations = new ArrayList<Reservation>();
        futureReservations = new ArrayList<Reservation>();
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
            Time startTime = futureReservations.get(i).getReserveTimeInterval().startTime;
            Time endTime = futureReservations.get(i).getReserveTimeInterval().endTime;


            Time displayStartTime = new Time(startTime.year,startTime.month,startTime.dayOfMonth,startTime.hourOfDay,startTime.minute,startTime.second);
            Time displayEndTime = new Time(endTime.year,endTime.month,endTime.dayOfMonth,endTime.hourOfDay,endTime.minute,endTime.second);

            displayStartTime.month+=1;
            displayEndTime.month+=1;

            futureListString.add(spotInlist.getStreetAddr() + "\n Time: " + displayStartTime.toString() + " ~ " + displayEndTime.toString());

        }
        for(int i = 0 ; i < passedReservations.size(); i++) {
            Log.d("FETCHRESERVATIONLIST","add into passed list");
            ParkingSpot spotInlist = passedReservations.get(i).getSpot();
            Time startTime = passedReservations.get(i).getReserveTimeInterval().startTime;
            Time endTime = passedReservations.get(i).getReserveTimeInterval().endTime;

            Time displayStartTime = new Time(startTime.year,startTime.month,startTime.dayOfMonth,startTime.hourOfDay,startTime.minute,startTime.second);
            Time displayEndTime = new Time(endTime.year,endTime.month,endTime.dayOfMonth,endTime.hourOfDay,endTime.minute,endTime.second);

            displayStartTime.month+=1;
            displayEndTime.month+=1;

            passedListString.add(spotInlist.getStreetAddr() + "\n Time: " + displayStartTime.toString() + " ~ " + displayEndTime.toString());
        }


        futureListAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, futureListString);
        _futureList.setAdapter(futureListAdapter);
        _futureList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    Log.d("ONITEMCLICKED", "FUTRUELIST");
                    ClientController controller = ClientController.getInstance();


                    ArrayList<Reservation> resList = controller.getRenterReservations();

                    int posInControllerList = -1;

                    long resID = futureReservations.get(position).getReservationID();

                    for(int i = 0; i < resList.size(); i++ )
                    {
                        if(resList.get(i).getReservationID() == resID)
                        {
                            posInControllerList = i;
                        }
                    }

                if(posInControllerList != -1)
                {
                    mListener.onReservationSelected(posInControllerList, true);
                    System.out.println("CLICKED on Reservation: " + position);
                }
                else
                {
                    Log.d("CANCELRESERVATION", "cannot find corresponded reservation");
                }
            }
        });


//        {
//            passedReservations = new ArrayList<>();
//            ParkingSpot parkingSpot1 = new ParkingSpot(1, null, 34, 34, "2831 Ellendale Pl, Los Angeles", "Really good", "90007", 0,0);
//            Time start =  new Time(2016, 2, 29, 4, 30, 0);
//			Time end = new Time(2016, 2 ,29, 5, 0,0);
//            TimeInterval ti  = new TimeInterval(start,end);
//            Reservation reservation = new Reservation(1,1,1,parkingSpot1,ti,1,true);
//            Reservation reservation1 = new Reservation(1,1,1,parkingSpot1,ti,1,true);
//            reservation.review =null;
//            reservation1.review = new Review(1l, "ha");
//            passedReservations.add(reservation);
//            passedReservations.add(reservation1);
//            passedReservations.add(reservation);
//        }

        passedListAdapter = new CustomReservationListAdapter(getActivity(),passedReservations);
        _passedList.setAdapter(passedListAdapter);
        _passedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                boolean ifCanCancel = false;

                ClientController controller = ClientController.getInstance();


                ArrayList<Reservation> resList = controller.getRenterReservations();

                int posInControllerList = -1;
//
//                Log.d("ONITEMCLICKED", "viewid: " + String.valueOf(view.getId()));
//                Log.d("ONITEMCLICKED", "pass id: " + String.valueOf(R.id.passedList));
//                Log.d("ONITEMCLICKED", "future id: " + String.valueOf(R.id.futureList));


//                if(view.getId() == R.id.passedList)
                {
                    Log.d("ONITEMCLICKED", "PASSEDLIST");
                    long resID = passedReservations.get(position).getReservationID();

                    for(int i = 0; i < resList.size(); i++ )
                    {
                        if(resList.get(i).getReservationID() == resID)
                        {
                            posInControllerList = i;
                        }
                    }
                }
//                else if(view.getId() == R.id.futureList)
//                {
//                    Log.d("ONITEMCLICKED", "FUTRUELIST");
//
//                    long resID = futureReservations.get(position).getReservationID();
//
//                    for(int i = 0; i < resList.size(); i++ )
//                    {
//                        if(resList.get(i).getReservationID() == resID)
//                        {
//                            posInControllerList = i;
//                        }
//                    }
//                }




                if(posInControllerList != -1)
                {
                    mListener.onReservationSelected(posInControllerList, false);
                    System.out.println("CLICKED on Reservation: " + position);
                }
                else
                {
                    Log.d("CANCELRESERVATION", "cannot find corresponded reservation");
                }

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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void onReservationSelected(int resPosition, boolean ifNotPasses);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

    }

//    public void refresh(){
//        ClientController clientcontroller = ClientController.getInstance();
//        mAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, clientcontroller.renterReservations);
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
