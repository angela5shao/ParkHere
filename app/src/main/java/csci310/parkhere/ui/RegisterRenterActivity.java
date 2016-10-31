package csci310.parkhere.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;

/**
 * Created by ivylinlaw on 10/29/16.
 */
public class RegisterRenterActivity extends Activity {
    Button _nextButton;
    EditText _liscenseIdText, _liscensePlateNumText;
    String name, email, password, phonenum;
    ClientController clientController;
    private static final int REQUEST_SIGNUP = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_renter_ui);

        _nextButton=(Button)findViewById(R.id.nextButton);
        _liscenseIdText=(EditText)findViewById(R.id.liscenseIdText); // license ID
        _liscensePlateNumText=(EditText)findViewById(R.id.liscensePlateNumText); // license plate number

        Intent intent = getIntent();
        name = intent.getStringExtra("NAME");
        email = intent.getStringExtra("EMAIL");
        password = intent.getStringExtra("PASSWORD");
        phonenum = intent.getStringExtra("PHONE_NUM");

        //
        Log.v("@@@@@@@@@@@@ ", name);
        Log.v("@@@@@@@@@@@@ ", email);
        Log.v("@@@@@@@@@@@@ ", password);
        Log.v("@@@@@@@@@@@@ ", phonenum);
        //

//        clientController = (ClientController) intent.getSerializableExtra("CLIENT_CONTROLLER");
        clientController = ClientController.getInstance();
        clientController.setCurrentActivity(this);

        _nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(v);
            }
        });
    }

    private void register(View v) {
        String licenseID = _liscenseIdText.getText().toString();
        String licensePlate = _liscensePlateNumText.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(RegisterRenterActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Registering...");
        progressDialog.show();

        // TODO: Implement your own authentication logic here.
        try {
            clientController.register(email, password, phonenum, licenseID, licensePlate, "renter", name);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final View curr_v = v;
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
//                        onRegisterSuccess(curr_v);
                            // onRegisterFailed();

                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_SIGNUP) {
            if(resultCode == RESULT_OK) {
                Toast.makeText(getBaseContext(), "Fragment Got it: " + requestCode + ", " + resultCode, Toast.LENGTH_SHORT).show();

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onRegisterSuccess(View v) {
        Intent intent = new Intent(v.getContext(), RenterActivity.class);
        startActivityForResult(intent, 0);
    }

    public void onRegisterFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
    }

//    public boolean validate() {
//        boolean valid = true;
//
//        String email = _emailText.getText().toString();
//        String password = _passwordText.getText().toString();
//
//        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            _emailText.setError("enter a valid email address");
//            valid = false;
//        } else {
//            _emailText.setError(null);
//        }
//
//        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
//            _passwordText.setError("between 4 and 10 alphanumeric characters");
//            valid = false;
//        } else {
//            _passwordText.setError(null);
//        }
//
//        return valid;
//    }
}
