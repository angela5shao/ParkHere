package csci310.parkhere.ui.fragments;

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
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;
import resource.ParkingSpot;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SpacesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SpacesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpacesFragment extends ListFragment implements AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ListView spacesListView;
    private Button addSpaceButton;
    private ArrayList<ParkingSpot> mParkingSpots;
    private ArrayAdapter mAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnSpaceSelectedListener mCallback;
    private OnFragmentInteractionListener mListener;

    public SpacesFragment() {
        // Required empty public constructor
//        spacesListView = (ListView)rootView().findViewById(R.id.spaces_list);
//        addSpaceButton = (Button)getView().findViewById(R.id.spaces_addbutton);
    }

    public void setParkingSpots(ArrayList<ParkingSpot> spots) {
        mParkingSpots = spots;
        System.out.println("SETTING SPACES in SpacesFragment");
    }

    public void refresh() { // Called by ProviderActivity after adding a new spot
        System.out.println("REFRESH SpacesFragment from controller");
        ClientController controller = ClientController.getInstance();

        if(!controller.providerToshowSpaces){
            ArrayList<ParkingSpot> spaces = controller.parkingSpots;
            mAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, spaces);
        }

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SpacesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SpacesFragment newInstance(String param1, long param2) {
        SpacesFragment fragment = new SpacesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putLong(ARG_PARAM2, param2);
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
//        addSpaceButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mListener.onAddSpace();
//            }
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_spaces, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            setText(bundle.getString("link"));
//        }

        // Get & update list of my spaces
        ClientController controller = ClientController.getInstance();


//        if(!controller.providerToshowSpaces)
        {
            controller.setCurrentFragment(this);
            ArrayList<ParkingSpot> spaces = controller.parkingSpots;
//            mAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, spaces);



            ArrayList<String> spacesAddr = new ArrayList<String>();

            for (int i=0; i<spaces.size(); i++) {
                spacesAddr.add(spaces.get(i).getStreetAddr());
                System.out.println("SpacesFrag: create string array with: " + spaces.get(i).getStreetAddr());
            }

            mAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, spacesAddr);
            setListAdapter(mAdapter);
            getListView().setOnItemClickListener(this);
            System.out.println("GET "+spaces.size()+" SPACES in SpacesFragment");
        }
//        ArrayList<ParkingSpot> spaces = controller.parkingSpots;

//        System.out.println("GET SPACES in SpacesFragment");

////        mAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.Planets, android.R.layout.simple_list_item_1);
//        mAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, spacesAddr) {
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                View view = super.getView(position, convertView, parent);
//                TextView text = (TextView) view.findViewById(android.R.id.text1);
//                text.setTextColor(Color.BLACK);
//                return view;
//            }
//        }
//        ;
//        setListAdapter(mAdapter);
//        getListView().setOnItemClickListener(this);
//        System.out.println("GET "+spaces.size()+" SPACES in SpacesFragment");
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
//        try {
//            mCallback = (OnSpaceSelectedListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString()
//                    + " must implement OnSpaceSpectedListener");
//        }
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
        void onSpaceSelected(int spacePositionInList);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        mListener.onSpaceSelected(position);
        System.out.println("CLICKED on Item: " + position);

    }
}
