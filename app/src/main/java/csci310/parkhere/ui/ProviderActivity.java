package csci310.parkhere.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.BraintreePaymentActivity;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.models.PaymentMethodNonce;

import java.util.ArrayList;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;
import resource.ParkingSpot;
import resource.User;

/**
 * Created by ivylinlaw on 10/17/16.
 */
public class ProviderActivity extends AppCompatActivity implements SpacesFragment.OnFragmentInteractionListener,
        SpaceDetailFragment.OnFragmentInteractionListener, PrivateProfileFragment.OnFragmentInteractionListener,
        ReservationDetailFragment.OnFragmentInteractionListener, AddSpaceFragment.OnFragmentInteractionListener {

    LinearLayout _spaceLink;
    ImageView _profilePic;

    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    Fragment spacesFragment, privateProfileFragment, addSpaceFragment, spaceDetailFragment;
    BraintreeFragment mBraintreeFragment;

    ClientController clientController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_ui);

        clientController = ClientController.getInstance();
        clientController.setCurrentActivity(this);

        Toolbar providerrToolbar = (Toolbar) findViewById(R.id.providerTabbar);
        setSupportActionBar(providerrToolbar);
//
        _spaceLink = (LinearLayout)findViewById(R.id.spaceLink);
        _profilePic = (ImageView)findViewById(R.id.profilePic);
//
        fm = getSupportFragmentManager();
        fragmentTransaction = fm.beginTransaction();
//        spacesFragment = fm.findFragmentById(R.id.fragment_spaces);
        spacesFragment = new SpacesFragment();
        fragmentTransaction.add(R.id.fragContainer, spacesFragment).commit();
        ArrayList<ParkingSpot> parkingSpots = clientController.getSpaces(clientController.getUser().userID);

        // TODO: Fix this; want to call setParkingSpot on spacesFragment
        Fragment spcfragment = getSupportFragmentManager().findFragmentById(R.id.fragContainer);
        if (spcfragment instanceof SpacesFragment) {
            ((SpacesFragment) spcfragment).setParkingSpots(parkingSpots);
        }
//        spacesFragment.setParkingSpots(parkingSpots);

        privateProfileFragment = new PrivateProfileFragment();
        addSpaceFragment = new AddSpaceFragment();


        spaceDetailFragment = new SpaceDetailFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragContainer, spaceDetailFragment).commit();

        _spaceLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Clicked on spaces tab item");
                try {
//                    fragmentTransaction.replace(R.id.fragContainer, spacesFragment)
//                        .commit();
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.fragContainer, spacesFragment).commit();
                    clientController.requestMyParkingSpotList();
                    clientController.providerToshowSpaces = true;
                } catch (Exception e) {
                    System.out.println("Spaces tab item exception");
                }
            }
        });
        _profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                System.out.println("Clicked on profile tab item");
//                try {
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.fragContainer, privateProfileFragment).commit();
//                } catch (Exception e) {
//                    System.out.println("Profile tab item exception");
//                }
                fragmentTransaction = fm.beginTransaction();

                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragContainer);
                User user = clientController.getUser();
                if(user == null)
                    Log.d("PROFILE", "user is null");

                if (fragment instanceof PrivateProfileFragment && user != null) {
                    Log.d("@@@@@@@@@@@@@@ ", user.userName);
                    Log.d("@@@@@@@@@@@@@@ ", user.userLicense);
                    Log.d("@@@@@@@@@@@@@@ ", user.userPlate);
                    ((PrivateProfileFragment) fragment).updateUserInfo(user.userName, "", user.userLicense, user.userPlate);
                }

                fragmentTransaction.replace(R.id.fragContainer, privateProfileFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        // Initialize BraintreeFragment
        try {
            // TODO mAuthorization should be either a client token or tokenization key
            String mAuthorization = "client token";
            mBraintreeFragment = BraintreeFragment.newInstance(this, mAuthorization);
            // mBraintreeFragment is ready to use!
        } catch (InvalidArgumentException e) {
            // There was an issue with your authorization string.
        }
    }

    public void showSpaceFragment()
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragContainer, spacesFragment).commit();
    }


    public void showSpaceDetailFragment()
    {
        // TODO: Get ParkingSpot given position in list
        if (clientController.parkingSpots.size() == 0) {
            System.out.println("ProviderActivity: no spaces to select");
            return;
        }
        ParkingSpot spotSelected = clientController.parkingSpots.get(clientController.currentIndexofSpaces);
        if (spotSelected == null) {
            System.out.println("Selected parking spot is null");
            return;
        }
        SpaceDetailFragment spaceDetailFragment = new SpaceDetailFragment();
        spaceDetailFragment.setThisParkingSpot(spotSelected);


        System.out.print("Show space detail " + spotSelected.getDescription());

//        Bundle args = new Bundle();
//        args.putInt("param1", spacePositionInList);
//        spaceDetailFragment.setArguments(args);

        try {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragContainer, spaceDetailFragment).commit();
        } catch (Exception e) {
            System.out.println("Spaces tab item exception");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.provider_menu_ui, menu);
        return true;
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 1) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    PaymentMethodNonce paymentMethodNonce = data.getParcelableExtra(
                            BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE
                    );
                    String nonce = paymentMethodNonce.getNonce();
                    // Send the nonce to your server
                    break;
                case BraintreePaymentActivity.BRAINTREE_RESULT_DEVELOPER_ERROR:
                case BraintreePaymentActivity.BRAINTREE_RESULT_SERVER_ERROR:
                case BraintreePaymentActivity.BRAINTREE_RESULT_SERVER_UNAVAILABLE:
                    // handle errors here, a throwable may be available in
                    // data.getSerializableExtra(BraintreePaymentActivity.EXTRA_ERROR_MESSAGE)
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
//        switch (item.getItemId()) {
////            case R.id.action_search:
////                openSearch();
////                return true;
////            case R.id.action_compose:
////                composeMessage();
////                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }

        if(item.getItemId() == R.id.RenterSwitch)
        {
            Intent intent = new Intent(this, RenterActivity.class);
            startActivityForResult(intent, 0);
            clientController.getUser().userType = true;

            Log.d("SWITCH","Switch To Renter");
            return true;
        }
        else if(item.getItemId() == R.id.LogOut)
        {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivityForResult(intent, 0);
            ClientController.resetController();
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }

    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    public void onSpaceSelected(int spacePositionInList) {
        if(spacePositionInList < 0 || clientController.parkingSpots == null ||spacePositionInList >= clientController.parkingSpots.size())
            return;

        System.out.println("ProviderActivity onSpaceSelected for: " + spacePositionInList);

        clientController.providerToshowSpacesDetail = true;

        clientController.currentIndexofSpaces = spacePositionInList;
        clientController.requestSpotTimeInterval(clientController.parkingSpots.get(spacePositionInList));


    }

    public void onEditSpace(long spaceID) {

    }

    public void onReservationSelected(long reservationID) {
        System.out.println("ProviderActivity onReservationSelected for: " + reservationID);
        ReservationDetailFragment reservationDetailFragment = new ReservationDetailFragment();
        Bundle args = new Bundle();
        args.putLong("param1", reservationID);
        reservationDetailFragment.setArguments(args);

        try {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragContainer, reservationDetailFragment).commit();
            System.out.println("onReservationSelected, replaced with reservationDetailFragment");
        } catch (Exception e) {
            System.out.println("Reservation item exception");
        }
    }

    // Called by SpacesFragment's "add" button
    public void onAddSpaceClick(View v) {
        System.out.println("ProviderActivity onAddSpaceClick() called");
        AddSpaceFragment addSpaceFragment = new AddSpaceFragment();
        Bundle args = new Bundle();
        args.putLong("param1", 012345);
        addSpaceFragment.setArguments(args);

        try {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragContainer, addSpaceFragment).commit();
            System.out.println("onReservationSelected, replaced with reservationDetailFragment");
        } catch (Exception e) {
            System.out.println("Reservation item exception");
        }
    }

    public void returnToSpaces() {
        clientController.requestMyParkingSpotList();
        clientController.providerToshowSpaces = true;


        Fragment spcfragment = getSupportFragmentManager().findFragmentById(R.id.fragContainer);
        if (spcfragment instanceof SpacesFragment) {
            ((SpacesFragment) spcfragment).refresh();
        }

        try {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragContainer, spacesFragment).commit();
        } catch (Exception e) {
            System.out.println("Spaces tab item exception");
        }

    }
}
