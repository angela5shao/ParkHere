package csci310.parkhere.ui.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.braintreepayments.api.BraintreePaymentActivity;
import com.braintreepayments.api.PaymentRequest;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.PaymentMethodNonce;

import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;
import resource.MyEntry;
import resource.NetworkPackage;
import resource.Time;
import resource.TimeInterval;

public class PaymentActivity extends Activity implements PaymentMethodNonceCreatedListener {
    int BRAINTREE_REQUEST_CODE = 11;

//    private String clientToken;
//    BraintreeFragment braintreeFragment;

    private long userID;
    private long parkingSpotID;
    private TimeInterval timeInterval;
    private long resID = -1;
    private long providerID;
    private String price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Intent intent = getIntent();
        userID = intent.getLongExtra("USERID", 0);
        parkingSpotID = intent.getLongExtra("PARKINGSPOTID", 0);
        String start = intent.getStringExtra("TIMEINTERVALSTART");
        String end = intent.getStringExtra("TIMEINTERVALEND");
        Log.d("Start and end: ", start+ " "+end);
        timeInterval = new TimeInterval(new Time(start), new Time(end));
        providerID = intent.getLongExtra("PROVIDERID", 0);
        PriceRetrieveTask PRT = new PriceRetrieveTask(parkingSpotID, timeInterval, providerID, userID);
        try {
            price = PRT.execute((Void)null).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
//        try {
//            braintreeFragment = BraintreeFragment.newInstance(this, clientToken);
//            // mBraintreeFragment is ready to use!
//        } catch (InvalidArgumentException e) {
//            // There was an issue with your authorization string.
//        }
//
//        PaymentRequest paymentRequest = new PaymentRequest()
//                .clientToken(clientToken);
    }

    // For BrainTree payment
    public void onBraintreeSubmit(View v) {
        GetClientToken GCT = new GetClientToken();
        GCT.execute((Void) null);
    }

    @Override
    public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
        Log.d("BT PAYPAL ", "Activity.RESULT_OK -- onPaymentMethodNonceCreated");
        // Send this nonce to your server
        String nonce = paymentMethodNonce.getNonce();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BRAINTREE_REQUEST_CODE) {
            switch (resultCode) {
                case BraintreePaymentActivity.RESULT_OK:
                    Log.d("BT CREDIT CARD ", "Activity.RESULT_OK");

                    PaymentMethodNonce paymentMethodNonce = data.getParcelableExtra(
                            BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE
                    );

                    String nonce = paymentMethodNonce.getNonce();

                    RenterReserveTask RRT = new RenterReserveTask(parkingSpotID, timeInterval, providerID, userID);
                    try {
                        HashMap<String, Serializable> mapp = RRT.execute((Void)null).get();
                        resID = (long) mapp.get("RESERVATIONID");
                        Log.d("resID" , String.valueOf(resID));
                        price = String.valueOf(mapp.get("PRICE"));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    // Send payment success to server
                    Log.d("PAYMENT", " success and send to server - "+price);
                    ClientController clientController = ClientController.getInstance();
                    clientController.setCurrentActivity(this);
                    if(resID!=-1) {
                        Log.d("Payment Success", String.valueOf(resID));
                        if((paymentMethodNonce.getTypeLabel().equals("PayPal"))) {
                            clientController.postPaymentNonceToServer(nonce, resID, providerID, price, "PAYPAL");
                        }
                        else {
                            clientController.postPaymentNonceToServer(nonce, resID, providerID, price, "CREDITCARD");
                        }

                    }
                    Intent intent = new Intent(getBaseContext(), RenterActivity.class);
                    startActivity(intent);

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

    private class GetClientToken extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressDialog;

        GetClientToken(){ }
        @Override
        protected void onPreExecute(){
            //Display a progress dialog
            progressDialog = new ProgressDialog(PaymentActivity.this,
                    R.style.AppTheme);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Proceeding to payment...");
            progressDialog.show();
        }
        @Override
        protected String doInBackground(Void... params ){
            Log.d("PaymentActivity ", "GetClientToken doInBackground");
            ClientController clientController = ClientController.getInstance();
            clientController.requestPaymentToken();
//                clientController.cancelReceived();
            NetworkPackage NP = clientController.checkReceived();
            MyEntry<String, Serializable> entry = NP.getCommand();
            String key = entry.getKey();
            Object value = entry.getValue();
            if(key.equals("CLIENTTOKEN")){
                Log.d("PaymentActivity ", "CLIENTTOKEN = "+((String) value));
                return (String) value;
            }
            else{
                return null;
            }
        }
        @Override
        protected void onPostExecute(String clientToken) {
            if(clientToken != null) {
                Log.d("PaymentActivity ", "clientToken != null");
                progressDialog.dismiss();

                Log.d("clientToken ", "= " + clientToken);

                PaymentRequest paymentRequest = new PaymentRequest()
                        .amount("$" + price)
                        .clientToken(clientToken);
                startActivityForResult(paymentRequest.getIntent(PaymentActivity.this), BRAINTREE_REQUEST_CODE);
            } else{
                progressDialog.dismiss();
                Toast.makeText(getBaseContext(), "Payment request token failed!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private class RenterReserveTask extends AsyncTask<Void, Void, HashMap<String, Serializable>> {
        private final long parkingSpotID;
        private final TimeInterval timeInterval;
        private final long providerID;
        private final long userID;
        private long resID;
        private String price;


        private ProgressDialog progressDialog;

        RenterReserveTask(long parkingSpotID, TimeInterval timeinterval, long providerID, long userID){
            this.parkingSpotID = parkingSpotID;
            this.timeInterval = timeinterval;
            this.providerID = providerID;
            this.userID = userID;
        }

        @Override
        protected void onPreExecute(){
            //Display a progress dialog
//            progressDialog = new ProgressDialog(getBaseContext(), R.style.AppTheme);
//            progressDialog.setIndeterminate(true);
//            progressDialog.setMessage("Booking...");
//            progressDialog.show();
        }

        @Override
        protected HashMap<String, Serializable> doInBackground(Void... params ){
            // call client controller
            // call client controller
            ClientController controller = ClientController.getInstance();
            controller.renterReserve(userID, parkingSpotID, timeInterval);
            Log.d("SEARCHRESERVE", "AFTERREQUEST");
            NetworkPackage NP = controller.checkReceived();
            MyEntry<String, Serializable> entry = NP.getCommand();
            String key = entry.getKey();
            Object value = entry.getValue();

            if(key.equals("RESERVE")) {
                HashMap<String, Serializable> map = (HashMap<String, Serializable>) value;
                return map;
            }
            else if(key.equals("RESERVEFAIL")) {
                Log.d("SEARCHRESERVE", "RESERVFAIL");
                return null;
            }
            else {
                Log.d("SEARCHRESERVE", "UNDEFINED ERROR");
                return null;
            }
        }
        @Override
        protected void onPostExecute(HashMap<String, Serializable> map) {
            if(map==null){
//                progressDialog.dismiss();
                Toast.makeText(getBaseContext(), "Book space failed! Please try agian.", Toast.LENGTH_SHORT).show();
            }else {
                this.resID = (long) map.get("RESERVATIONID");
                this.price = String.valueOf(map.get("PRICE"));
                Log.d("testtest", String.valueOf(resID)+"  "+price);
//                progressDialog.dismiss();
            }
        }
    }


    private class PriceRetrieveTask extends AsyncTask<Void, Void, String> {
        private final long parkingSpotID;
        private final TimeInterval timeInterval;
        private final long providerID;
        private final long userID;
        private long resID;
        private String price;

        PriceRetrieveTask(long parkingSpotID, TimeInterval timeinterval, long providerID, long userID){
            this.parkingSpotID = parkingSpotID;
            this.timeInterval = timeinterval;
            this.providerID = providerID;
            this.userID = userID;
        }

        @Override
        protected void onPreExecute(){
            //Display a progress dialog
//            progressDialog = new ProgressDialog(getBaseContext(), R.style.AppTheme);
//            progressDialog.setIndeterminate(true);
//            progressDialog.setMessage("Booking...");
//            progressDialog.show();
        }
        @Override
        protected String doInBackground(Void... params ){
            // call client controller
            // call client controller
            ClientController controller = ClientController.getInstance();
            controller.getPrice(userID, parkingSpotID, timeInterval);
            Log.d("SEARCHRESERVE", "AFTERREQUEST");
            NetworkPackage NP = controller.checkReceived();
            MyEntry<String, Serializable> entry = NP.getCommand();
            String key = entry.getKey();
            Object value = entry.getValue();

            if(key.equals("PRICE")) {
                double pricee = (double)value;
                this.price = String.valueOf(pricee);
                return this.price;
            }
            else {
                return null;
            }
        }
//        @Override
//        protected void onPostExecute(String price) {
//            if(price==null){
////                progressDialog.dismiss();
//                price = price;
//            }else {
//                this.resID = (long) map.get("RESERVATIONID");
//                this.price = String.valueOf(map.get("PRICE"));
//                Log.d("testtest", String.valueOf(resID)+"  "+price);
////                progressDialog.dismiss();
//            }
//        }
    }
}
