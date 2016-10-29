package csci310.parkhere.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import csci310.parkhere.R;

/**
 * Created by ivylinlaw on 10/29/16.
 */
public class RegisterRenterActivity extends Activity {
    Button _nextButton;
    EditText _liscenseIdText, _liscensePlateNumText;
    String name, email, password, phonenum;

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

        _nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String licenseID = _liscenseIdText.getText().toString();
                String licensePlate = _liscensePlateNumText.getText().toString();

                //
            }
        });

//        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
//                R.style.AppTheme);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Registering...");
//        progressDialog.show();
//
//        // TODO: Implement your own authentication logic here.
//
//        final View curr_v = v;
//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    //                    private View v;
//                    public void run() {
//                        // On complete call either onLoginSuccess or onLoginFailed
//                        onLoginSuccess(curr_v);
//                        // onLoginFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
    }
}
