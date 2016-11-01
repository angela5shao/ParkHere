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
import com.braintreepayments.api.PayPal;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.braintreepayments.api.models.VenmoAccountNonce;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;
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
    String mAuthorization = "clientToken";

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
        spacesFragment = new SpacesFragment();
        privateProfileFragment = new PrivateProfileFragment();
        addSpaceFragment = new AddSpaceFragment();

//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.fragContainer, spacesFragment).commit();

//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.fragContainer, addSpaceFragment).commit();

        spaceDetailFragment = new SpaceDetailFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragContainer, spaceDetailFragment).commit();

        // Initialize BraintreeFragment
        try {
            // TODO mAuthorization should be either a client token or tokenization key
            mBraintreeFragment = BraintreeFragment.newInstance(this, mAuthorization);
            // mBraintreeFragment is ready to use!
        } catch (InvalidArgumentException e) {
            // There was an issue with your authorization string.
        }

        _spaceLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Clicked on spaces tab item");
                try {
//                    fragmentTransaction.replace(R.id.fragContainer, spacesFragment)
//                        .commit();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragContainer, spacesFragment).commit();
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
                    String deviceData = data.getStringExtra(BraintreePaymentActivity.EXTRA_DEVICE_DATA);

                    String nonce = paymentMethodNonce.getNonce();
                    // Send the nonce to your server

                    if(paymentMethodNonce instanceof VenmoAccountNonce) {
                        VenmoAccountNonce venmoAccountNonce = (VenmoAccountNonce) paymentMethodNonce;
                        String venmoUsername = venmoAccountNonce.getUsername();
                    }
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

    public void onSpaceSelected(long spaceID) {
        System.out.println("ProviderActivity onSpaceSelected for: " + spaceID);
        SpaceDetailFragment spaceDetailFragment = new SpaceDetailFragment();
        Bundle args = new Bundle();
        args.putLong("param1", spaceID);
        spaceDetailFragment.setArguments(args);

        try {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragContainer, spaceDetailFragment).commit();
            System.out.println("onSpaceSelected, replaced with sppaceDetailFragment");
        } catch (Exception e) {
            System.out.println("Spaces tab item exception");
        }
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
            PayPal.authorizeAccount(mBraintreeFragment);
        } catch (Exception e) {
            System.out.println("Reservation item exception");
        }

//        SearchSpaceDetailFragment searchSpaceDetailFragment = new SearchSpaceDetailFragment();
//        Bundle args = new Bundle();
//        args.putLong("param1", reservationID);
//        searchSpaceDetailFragment.setArguments(args);
//
//        try {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragContainer, searchSpaceDetailFragment).commit();
//            System.out.println("onReservationSelected, replaced with reservationDetailFragment");
//            PayPal.authorizeAccount(mBraintreeFragment);
//        } catch (Exception e) {
//            System.out.println("Reservation item exception");
//        }
    }

    // For BrainTree payment
//    public void onBraintreeSubmit(View v) {
////        ClientTokenRequest clientTokenRequest = new ClientTokenRequest()
////                .customerId(aCustomerId);
////        String clientToken = gateway.clientToken().generate(clientTokenRequest);
//
//        // TODO: request client token
//        String clientToken = "eyJ2ZXJzaW9uIjoyLCJhdXRob3JpemF0aW9uRmluZ2VycHJpbnQiOiI0Y2ZjNmI4MWVlZjQ1YTA5YjM1ODQ2OWUyYWViZjI0Mjk1ZjE0MTJiZWI0M2Q3MDUxODBmMTI3NTNhNjk2M2NjfGNyZWF0ZWRfYXQ9MjAxNi0xMC0zMVQwNDo0OTo1Mi40MjA0NzgzNDMrMDAwMFx1MDAyNm1lcmNoYW50X2lkPTM0OHBrOWNnZjNiZ3l3MmJcdTAwMjZwdWJsaWNfa2V5PTJuMjQ3ZHY4OWJxOXZtcHIiLCJjb25maWdVcmwiOiJodHRwczovL2FwaS5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tOjQ0My9tZXJjaGFudHMvMzQ4cGs5Y2dmM2JneXcyYi9jbGllbnRfYXBpL3YxL2NvbmZpZ3VyYXRpb24iLCJjaGFsbGVuZ2VzIjpbXSwiZW52aXJvbm1lbnQiOiJzYW5kYm94IiwiY2xpZW50QXBpVXJsIjoiaHR0cHM6Ly9hcGkuc2FuZGJveC5icmFpbnRyZWVnYXRld2F5LmNvbTo0NDMvbWVyY2hhbnRzLzM0OHBrOWNnZjNiZ3l3MmIvY2xpZW50X2FwaSIsImFzc2V0c1VybCI6Imh0dHBzOi8vYXNzZXRzLmJyYWludHJlZWdhdGV3YXkuY29tIiwiYXV0aFVybCI6Imh0dHBzOi8vYXV0aC52ZW5tby5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tIiwiYW5hbHl0aWNzIjp7InVybCI6Imh0dHBzOi8vY2xpZW50LWFuYWx5dGljcy5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tLzM0OHBrOWNnZjNiZ3l3MmIifSwidGhyZWVEU2VjdXJlRW5hYmxlZCI6dHJ1ZSwicGF5cGFsRW5hYmxlZCI6dHJ1ZSwicGF5cGFsIjp7ImRpc3BsYXlOYW1lIjoiQWNtZSBXaWRnZXRzLCBMdGQuIChTYW5kYm94KSIsImNsaWVudElkIjpudWxsLCJwcml2YWN5VXJsIjoiaHR0cDovL2V4YW1wbGUuY29tL3BwIiwidXNlckFncmVlbWVudFVybCI6Imh0dHA6Ly9leGFtcGxlLmNvbS90b3MiLCJiYXNlVXJsIjoiaHR0cHM6Ly9hc3NldHMuYnJhaW50cmVlZ2F0ZXdheS5jb20iLCJhc3NldHNVcmwiOiJodHRwczovL2NoZWNrb3V0LnBheXBhbC5jb20iLCJkaXJlY3RCYXNlVXJsIjpudWxsLCJhbGxvd0h0dHAiOnRydWUsImVudmlyb25tZW50Tm9OZXR3b3JrIjp0cnVlLCJlbnZpcm9ubWVudCI6Im9mZmxpbmUiLCJ1bnZldHRlZE1lcmNoYW50IjpmYWxzZSwiYnJhaW50cmVlQ2xpZW50SWQiOiJtYXN0ZXJjbGllbnQzIiwiYmlsbGluZ0FncmVlbWVudHNFbmFibGVkIjp0cnVlLCJtZXJjaGFudEFjY291bnRJZCI6ImFjbWV3aWRnZXRzbHRkc2FuZGJveCIsImN1cnJlbmN5SXNvQ29kZSI6IlVTRCJ9LCJjb2luYmFzZUVuYWJsZWQiOmZhbHNlLCJtZXJjaGFudElkIjoiMzQ4cGs5Y2dmM2JneXcyYiIsInZlbm1vIjoib2ZmIn0";
//        PaymentRequest paymentRequest = new PaymentRequest()
//                .clientToken(clientToken);
//        startActivityForResult(paymentRequest.getIntent(this), 1);
//
//    }

    public void onAddSpace() {
        System.out.println("SpacesFragment onAddSpace() called");
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

    public void onAddSpaceClick(View v) {
        System.out.println("SpacesFragment onAddSpace() called");
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
}
