package csci310.parkhere.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.BraintreePaymentActivity;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.models.PaymentMethodNonce;

import csci310.parkhere.R;
import resource.ParkingSpot;

/**
 * Created by ivylinlaw on 10/17/16.
 */
public class ProviderActivity extends FragmentActivity implements SpacesFragment.OnFragmentInteractionListener,
        SpaceDetailFragment.OnFragmentInteractionListener, PrivateProfileFragment.OnFragmentInteractionListener,
        ReservationDetailFragment.OnFragmentInteractionListener, AddSpaceFragment.OnFragmentInteractionListener {

    TextView _spaceLink;
    ImageView _profilePic;

    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    Fragment spacesFragment;
    Fragment privateProfileFragment;
    BraintreeFragment mBraintreeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_ui);

        Toolbar providerrToolbar = (Toolbar) findViewById(R.id.providerTabbar);
        setActionBar(providerrToolbar);
//
        _spaceLink = (TextView)findViewById(R.id.spaceLink);
        _profilePic = (ImageView)findViewById(R.id.profilePic);
//
        fm = getSupportFragmentManager();
        fragmentTransaction = fm.beginTransaction();
//        spacesFragment = fm.findFragmentById(R.id.fragment_spaces);
        spacesFragment = new SpacesFragment();
        privateProfileFragment = new PrivateProfileFragment();

//        fragmentTransaction.add(R.id.fragContainer, spacesFragment).commit();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragContainer, spacesFragment).commit();

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
                System.out.println("Clicked on profile tab item");
                try {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragContainer, privateProfileFragment).commit();
                } catch (Exception e) {
                    System.out.println("Profile tab item exception");
                }
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
        switch (item.getItemId()) {
//            case R.id.action_search:
//                openSearch();
//                return true;
//            case R.id.action_compose:
//                composeMessage();
//                return true;
            default:
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
        } catch (Exception e) {
            System.out.println("Reservation item exception");
        }
    }

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
