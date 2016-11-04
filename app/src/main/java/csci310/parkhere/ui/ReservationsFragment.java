package csci310.parkhere.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;
import resource.Reservation;


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

    String [] resultList;
    ListView _myreservationList;
    ArrayList<Reservation> _reservationList;
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

    }


    public void setReservationListview(String[] reservationResults)
    {
        _myreservationList.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, reservationResults));
        DiplayListViewHelper.getListViewSize(_myreservationList);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        if (bundle != null) {
            // setText(bundle.getString("link"));
            resultList = bundle.getStringArray("RESULT_LIST");
        }

        View v = inflater.inflate(R.layout.fragment_reservations, container, false);
        _myreservationList = (ListView) v.findViewById(android.R.id.list);

        ClientController controller = ClientController.getInstance();
        _reservationList = controller.getReservations();

        resultList = new String[_reservationList.size()];

        for(int i = 0; i < _reservationList.size(); i++)
        {
            resultList[i] = _reservationList.get(i).getSpot().getStreetAddr();
        }

        setReservationListview(resultList);

        _myreservationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//                // ListView Clicked item index
//                int itemPosition     = position;
//                // ListView Clicked item value
//                String  itemValue    = (String) listView.getItemAtPosition(position);
//                // Show Alert
//                Toast.makeText(getApplicationContext(),
//                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
//                        .show();
            }

        });
        //***********************************************

        return v;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            setText(bundle.getString("link"));
//        }
//        System.out.println("first");
//        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Planets, android.R.layout.simple_list_item_1);
        //       setListAdapter(adapter);
//        getListView().setOnItemClickListener(this);

        //      ClientController controller = ClientController.getInstance();
        //    ArrayList<Reservation> reservations = controller.reservations;
        //  ParkingSpot spot = new ParkingSpot(controller.getUser().userID, null, 0, 0, "Tuscany 101, 10 Figueroa", "", "90007", 0x0001);
        //reservations.add(new Reservation(0123, controller.getUser().userID, 789, spot, null, 50.00, false));
        //reservations.add(new Reservation(0123, controller.getUser().userID, 789, spot, null, 75.00, false));

//        mAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, resultList);
//        setListAdapter(mAdapter);
//        getListView().setOnItemClickListener(this);
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
}
