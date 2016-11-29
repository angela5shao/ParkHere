package csci310.parkhere.ui.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.BraintreePaymentActivity;
import com.braintreepayments.api.PaymentRequest;
import com.braintreepayments.api.models.PaymentMethodNonce;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;
import resource.MyEntry;
import resource.NetworkPackage;

public class PaymentActivity extends Activity {
    int BRAINTREE_REQUEST_CODE = 11;
    int BRAINTREE_PAYPAL_REQUEST_CODE = 12;

    private String clientToken;
    BraintreeFragment braintreeFragment;

    private long resID, providerID;
    private String price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Intent intent = getIntent();
        resID = intent.getLongExtra("RESERVATIONID", 0);
        providerID = intent.getLongExtra("PROVIDERID", 0);
        price = intent.getStringExtra("PRICE");

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
//        Intent intent = new Intent(context, BraintreePaymentActivity.class);
//        .putExtra(BraintreePaymentActivity.EXTRA_CLIENT_TOKEN, clientToken);

        System.out.println("MDZZ!!!");

        // TODO: request client token
        GetClientToken GCT = new GetClientToken();
        GCT.execute((Void) null);
//        clientToken = "eyJ2ZXJzaW9uIjoyLCJhdXRob3JpemF0aW9uRmluZ2VycHJpbnQiOiJjZTM0OTdiMGMzMzQyZjBjNjBjYWZmMWUxNjIzOWNhZGIzZTU3OWU0Zjc5MDMyNjZmZDEzMWVhYjE2MDNkM2U1fGNyZWF0ZWRfYXQ9MjAxNi0xMS0xNVQwNToxMjoyOC4yOTcwNTM4MzgrMDAwMFx1MDAyNm1lcmNoYW50X2lkPXNyZjczdDVmYzcyNHc0cmhcdTAwMjZwdWJsaWNfa2V5PXRxdjhwcTZoaHl4N2J6cXIiLCJjb25maWdVcmwiOiJodHRwczovL2FwaS5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tOjQ0My9tZXJjaGFudHMvc3JmNzN0NWZjNzI0dzRyaC9jbGllbnRfYXBpL3YxL2NvbmZpZ3VyYXRpb24iLCJjaGFsbGVuZ2VzIjpbXSwiZW52aXJvbm1lbnQiOiJzYW5kYm94IiwiY2xpZW50QXBpVXJsIjoiaHR0cHM6Ly9hcGkuc2FuZGJveC5icmFpbnRyZWVnYXRld2F5LmNvbTo0NDMvbWVyY2hhbnRzL3NyZjczdDVmYzcyNHc0cmgvY2xpZW50X2FwaSIsImFzc2V0c1VybCI6Imh0dHBzOi8vYXNzZXRzLmJyYWludHJlZWdhdGV3YXkuY29tIiwiYXV0aFVybCI6Imh0dHBzOi8vYXV0aC52ZW5tby5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tIiwiYW5hbHl0aWNzIjp7InVybCI6Imh0dHBzOi8vY2xpZW50LWFuYWx5dGljcy5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tL3NyZjczdDVmYzcyNHc0cmgifSwidGhyZWVEU2VjdXJlRW5hYmxlZCI6dHJ1ZSwicGF5cGFsRW5hYmxlZCI6dHJ1ZSwicGF5cGFsIjp7ImRpc3BsYXlOYW1lIjoiUGFya0hlcmUiLCJjbGllbnRJZCI6bnVsbCwicHJpdmFjeVVybCI6Imh0dHA6Ly9leGFtcGxlLmNvbS9wcCIsInVzZXJBZ3JlZW1lbnRVcmwiOiJodHRwOi8vZXhhbXBsZS5jb20vdG9zIiwiYmFzZVVybCI6Imh0dHBzOi8vYXNzZXRzLmJyYWludHJlZWdhdGV3YXkuY29tIiwiYXNzZXRzVXJsIjoiaHR0cHM6Ly9jaGVja291dC5wYXlwYWwuY29tIiwiZGlyZWN0QmFzZVVybCI6bnVsbCwiYWxsb3dIdHRwIjp0cnVlLCJlbnZpcm9ubWVudE5vTmV0d29yayI6dHJ1ZSwiZW52aXJvbm1lbnQiOiJvZmZsaW5lIiwidW52ZXR0ZWRNZXJjaGFudCI6ZmFsc2UsImJyYWludHJlZUNsaWVudElkIjoibWFzdGVyY2xpZW50MyIsImJpbGxpbmdBZ3JlZW1lbnRzRW5hYmxlZCI6dHJ1ZSwibWVyY2hhbnRBY2NvdW50SWQiOiJwYXJraGVyZSIsImN1cnJlbmN5SXNvQ29kZSI6IlVTRCJ9LCJjb2luYmFzZUVuYWJsZWQiOmZhbHNlLCJtZXJjaGFudElkIjoic3JmNzN0NWZjNzI0dzRyaCIsInZlbm1vIjoib2ZmIn0";
//        PaymentRequest paymentRequest = new PaymentRequest()
//                .clientToken(clientToken);
//
//        startActivityForResult(paymentRequest.getIntent(this), BRAINTREE_REQUEST_CODE);
    }

    public void onBraintreeSubmitPaypal(View v) {
//        ClientTokenRequest clientTokenRequest = new ClientTokenRequest()
//                .customerId(aCustomerId);
//        String clientToken = gateway.clientToken().generate(clientTokenRequest);

        // TODO: request client token
        clientToken = "eyJ2ZXJzaW9uIjoyLCJhdXRob3JpemF0aW9uRmluZ2VycHJpbnQiOiJjZTM0OTdiMGMzMzQyZjBjNjBjYWZmMWUxNjIzOWNhZGIzZTU3OWU0Zjc5MDMyNjZmZDEzMWVhYjE2MDNkM2U1fGNyZWF0ZWRfYXQ9MjAxNi0xMS0xNVQwNToxMjoyOC4yOTcwNTM4MzgrMDAwMFx1MDAyNm1lcmNoYW50X2lkPXNyZjczdDVmYzcyNHc0cmhcdTAwMjZwdWJsaWNfa2V5PXRxdjhwcTZoaHl4N2J6cXIiLCJjb25maWdVcmwiOiJodHRwczovL2FwaS5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tOjQ0My9tZXJjaGFudHMvc3JmNzN0NWZjNzI0dzRyaC9jbGllbnRfYXBpL3YxL2NvbmZpZ3VyYXRpb24iLCJjaGFsbGVuZ2VzIjpbXSwiZW52aXJvbm1lbnQiOiJzYW5kYm94IiwiY2xpZW50QXBpVXJsIjoiaHR0cHM6Ly9hcGkuc2FuZGJveC5icmFpbnRyZWVnYXRld2F5LmNvbTo0NDMvbWVyY2hhbnRzL3NyZjczdDVmYzcyNHc0cmgvY2xpZW50X2FwaSIsImFzc2V0c1VybCI6Imh0dHBzOi8vYXNzZXRzLmJyYWludHJlZWdhdGV3YXkuY29tIiwiYXV0aFVybCI6Imh0dHBzOi8vYXV0aC52ZW5tby5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tIiwiYW5hbHl0aWNzIjp7InVybCI6Imh0dHBzOi8vY2xpZW50LWFuYWx5dGljcy5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tL3NyZjczdDVmYzcyNHc0cmgifSwidGhyZWVEU2VjdXJlRW5hYmxlZCI6dHJ1ZSwicGF5cGFsRW5hYmxlZCI6dHJ1ZSwicGF5cGFsIjp7ImRpc3BsYXlOYW1lIjoiUGFya0hlcmUiLCJjbGllbnRJZCI6bnVsbCwicHJpdmFjeVVybCI6Imh0dHA6Ly9leGFtcGxlLmNvbS9wcCIsInVzZXJBZ3JlZW1lbnRVcmwiOiJodHRwOi8vZXhhbXBsZS5jb20vdG9zIiwiYmFzZVVybCI6Imh0dHBzOi8vYXNzZXRzLmJyYWludHJlZWdhdGV3YXkuY29tIiwiYXNzZXRzVXJsIjoiaHR0cHM6Ly9jaGVja291dC5wYXlwYWwuY29tIiwiZGlyZWN0QmFzZVVybCI6bnVsbCwiYWxsb3dIdHRwIjp0cnVlLCJlbnZpcm9ubWVudE5vTmV0d29yayI6dHJ1ZSwiZW52aXJvbm1lbnQiOiJvZmZsaW5lIiwidW52ZXR0ZWRNZXJjaGFudCI6ZmFsc2UsImJyYWludHJlZUNsaWVudElkIjoibWFzdGVyY2xpZW50MyIsImJpbGxpbmdBZ3JlZW1lbnRzRW5hYmxlZCI6dHJ1ZSwibWVyY2hhbnRBY2NvdW50SWQiOiJwYXJraGVyZSIsImN1cnJlbmN5SXNvQ29kZSI6IlVTRCJ9LCJjb2luYmFzZUVuYWJsZWQiOmZhbHNlLCJtZXJjaGFudElkIjoic3JmNzN0NWZjNzI0dzRyaCIsInZlbm1vIjoib2ZmIn0";
        PaymentRequest paymentRequest = new PaymentRequest()
                .clientToken(clientToken);
        startActivityForResult(paymentRequest.getIntent(this), BRAINTREE_PAYPAL_REQUEST_CODE);
    }

    public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
        // Send this nonce to your server
        String nonce = paymentMethodNonce.getNonce();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BRAINTREE_REQUEST_CODE) {
            switch (resultCode) {
                case BraintreePaymentActivity.RESULT_OK:
                    PaymentMethodNonce paymentMethodNonce = data.getParcelableExtra(
                            BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE
                    );

                    String nonce = paymentMethodNonce.getNonce();

                    // Send paymeny success to server
                    Log.d("PAYMENT", " success and send to server!");
                    ClientController clientController = ClientController.getInstance();
                    clientController.setCurrentActivity(this);

                    clientController.postPaymentNonceToServer(nonce, resID, providerID, "35");//price);
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

        if (requestCode == BRAINTREE_PAYPAL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK)
            {
                Log.d("BT onActivityResult ", "Activity.RESULT_OK");
                PaymentMethodNonce paymentMethodNonce = data.getParcelableExtra(
                        BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE
                );
                String nonce = paymentMethodNonce.getNonce();
                // Send the nonce to your server.

                Intent intent = new Intent(getBaseContext(), RenterActivity.class);
                startActivity(intent);
            }
            else {
                Log.d("VT onActivityResult ", "!Activity.RESULT_OK");
            }
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        Date date = new Date();
        System.out.println("END PAYMENT: " + dateFormat.format(date) + " ********************");
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
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
            Date date = new Date();
            System.out.println("START GetClientToken: " + dateFormat.format(date) + " ********************");

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
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
            Date date = new Date();
            System.out.println("FINISHED GetClientToken: " + dateFormat.format(date) + " ********************");

            if(clientToken != null) {
                Log.d("PaymentActivity ", "clientToken != null");
                progressDialog.dismiss();

                Log.d("clientToken ", "= "+clientToken);
                PaymentRequest paymentRequest = new PaymentRequest()
                        .amount("$35")
                        .clientToken(clientToken);
                startActivityForResult(paymentRequest.getIntent(PaymentActivity.this), BRAINTREE_REQUEST_CODE);

            } else{
                progressDialog.dismiss();
                Toast.makeText(getBaseContext(), "Payment request token failed!", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
