package csci310.parkhere.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.BraintreePaymentActivity;
import com.braintreepayments.api.PaymentRequest;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.models.PaymentMethodNonce;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;

public class PaymentActivity extends Activity {
    int BRAINTREE_REQUEST_CODE = 11;
    int BRAINTREE_PAYPAL_REQUEST_CODE = 12;

    private String clientToken;
    BraintreeFragment braintreeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        try {
            braintreeFragment = BraintreeFragment.newInstance(this, clientToken);
            // mBraintreeFragment is ready to use!
        } catch (InvalidArgumentException e) {
            // There was an issue with your authorization string.
        }

        PaymentRequest paymentRequest = new PaymentRequest()
                .clientToken(clientToken);

//        try {
//            PaymentButton paymentButton = PaymentButton.newInstance(this, R.id.payment_button_container, paymentRequest);
//            paymentButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    PayPal.authorizeAccount(braintreeFragment);
//                }
//            });
//        } catch (InvalidArgumentException e) {
//            e.printStackTrace();
//        }

//        CardBuilder cardBuilder = new CardBuilder()
//                .cardNumber("4111111111111111")
//                .expirationDate("09/2018");
//
//        Card.tokenize(braintreeFragment, cardBuilder);
    }

    // For BrainTree payment
    public void onBraintreeSubmit(View v) {
//        Intent intent = new Intent(context, BraintreePaymentActivity.class);
//        .putExtra(BraintreePaymentActivity.EXTRA_CLIENT_TOKEN, clientToken);

        System.out.println("MDZZ!!!");

        // TODO: request client token
        clientToken = "eyJ2ZXJzaW9uIjoyLCJhdXRob3JpemF0aW9uRmluZ2VycHJpbnQiOiJjZTM0OTdiMGMzMzQyZjBjNjBjYWZmMWUxNjIzOWNhZGIzZTU3OWU0Zjc5MDMyNjZmZDEzMWVhYjE2MDNkM2U1fGNyZWF0ZWRfYXQ9MjAxNi0xMS0xNVQwNToxMjoyOC4yOTcwNTM4MzgrMDAwMFx1MDAyNm1lcmNoYW50X2lkPXNyZjczdDVmYzcyNHc0cmhcdTAwMjZwdWJsaWNfa2V5PXRxdjhwcTZoaHl4N2J6cXIiLCJjb25maWdVcmwiOiJodHRwczovL2FwaS5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tOjQ0My9tZXJjaGFudHMvc3JmNzN0NWZjNzI0dzRyaC9jbGllbnRfYXBpL3YxL2NvbmZpZ3VyYXRpb24iLCJjaGFsbGVuZ2VzIjpbXSwiZW52aXJvbm1lbnQiOiJzYW5kYm94IiwiY2xpZW50QXBpVXJsIjoiaHR0cHM6Ly9hcGkuc2FuZGJveC5icmFpbnRyZWVnYXRld2F5LmNvbTo0NDMvbWVyY2hhbnRzL3NyZjczdDVmYzcyNHc0cmgvY2xpZW50X2FwaSIsImFzc2V0c1VybCI6Imh0dHBzOi8vYXNzZXRzLmJyYWludHJlZWdhdGV3YXkuY29tIiwiYXV0aFVybCI6Imh0dHBzOi8vYXV0aC52ZW5tby5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tIiwiYW5hbHl0aWNzIjp7InVybCI6Imh0dHBzOi8vY2xpZW50LWFuYWx5dGljcy5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tL3NyZjczdDVmYzcyNHc0cmgifSwidGhyZWVEU2VjdXJlRW5hYmxlZCI6dHJ1ZSwicGF5cGFsRW5hYmxlZCI6dHJ1ZSwicGF5cGFsIjp7ImRpc3BsYXlOYW1lIjoiUGFya0hlcmUiLCJjbGllbnRJZCI6bnVsbCwicHJpdmFjeVVybCI6Imh0dHA6Ly9leGFtcGxlLmNvbS9wcCIsInVzZXJBZ3JlZW1lbnRVcmwiOiJodHRwOi8vZXhhbXBsZS5jb20vdG9zIiwiYmFzZVVybCI6Imh0dHBzOi8vYXNzZXRzLmJyYWludHJlZWdhdGV3YXkuY29tIiwiYXNzZXRzVXJsIjoiaHR0cHM6Ly9jaGVja291dC5wYXlwYWwuY29tIiwiZGlyZWN0QmFzZVVybCI6bnVsbCwiYWxsb3dIdHRwIjp0cnVlLCJlbnZpcm9ubWVudE5vTmV0d29yayI6dHJ1ZSwiZW52aXJvbm1lbnQiOiJvZmZsaW5lIiwidW52ZXR0ZWRNZXJjaGFudCI6ZmFsc2UsImJyYWludHJlZUNsaWVudElkIjoibWFzdGVyY2xpZW50MyIsImJpbGxpbmdBZ3JlZW1lbnRzRW5hYmxlZCI6dHJ1ZSwibWVyY2hhbnRBY2NvdW50SWQiOiJwYXJraGVyZSIsImN1cnJlbmN5SXNvQ29kZSI6IlVTRCJ9LCJjb2luYmFzZUVuYWJsZWQiOmZhbHNlLCJtZXJjaGFudElkIjoic3JmNzN0NWZjNzI0dzRyaCIsInZlbm1vIjoib2ZmIn0";
        PaymentRequest paymentRequest = new PaymentRequest()
                .clientToken(clientToken);

        startActivityForResult(paymentRequest.getIntent(this), BRAINTREE_REQUEST_CODE);
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

//    void postNonceToServer(String nonce) {
//        AsyncHttpClient client = new AsyncHttpClient();
//        RequestParams params = new RequestParams();
//        params.put("payment_method_nonce", nonce);
//        client.post("http://your-server/checkout", params,
//                new AsyncHttpResponseHandler() {
//                    // Your implementation here
//                }
//        );
//    }

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
                    String paymentMethodNonce = data
                            .getStringExtra(BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE);

                    // Send paymeny success to server
                    Log.d("PAYMENT", " success and send to server!");
                    ClientController clientController = ClientController.getInstance();
                    clientController.setCurrentActivity(this);

                    Intent myintent = getIntent();
                    Long resID = myintent.getLongExtra("RESERVATIONID", 0);
                    Long providerID = myintent.getLongExtra("PROVIDERID", 0);
                    String price = myintent.getStringExtra("PRICE");

                    clientController.postPaymentNonceToServer(paymentMethodNonce, resID, providerID, price);
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
    }
}
