package csci310.parkhere.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import csci310.parkhere.R;

/**
 * Created by ivylinlaw on 10/15/16.
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    Button _loginButton;
    EditText email, password;

    TextView _signupLink, _forgotPwLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_ui);

        _loginButton=(Button)findViewById(R.id.loginButton);
        _signupLink=(TextView)findViewById(R.id.signupLink);
        _forgotPwLink=(TextView)findViewById(R.id.forgotPwLink);

        // Log in
        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(v);
//                if(email.getText().toString().equals("admin") &&
//                        password.getText().toString().equals("admin")) {
//                    Toast.makeText(getApplicationContext(), "Redirecting...", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    Toast.makeText(getApplicationContext(), "Wrong Credentials",Toast.LENGTH_SHORT).show();
//                }
            }
        });

        //Sign up
        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });

        //Forgot password
        _forgotPwLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });
    }

    public void login(View v) {
        Log.d(TAG, "Login");

        email=(EditText)findViewById(R.id.emailText);
        password=(EditText)findViewById(R.id.passwordText);

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        // TODO: Implement your own authentication logic here.

        final View curr_v = v;
        new android.os.Handler().postDelayed(
                new Runnable() {
//                    private View v;
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess(curr_v);
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public boolean validate() {
        //
        return true;
    }

    public void onLoginSuccess(View v) {
        _loginButton.setEnabled(true);
        finish();

        Intent myIntent = new Intent(v.getContext(), RenterActivity.class);
        startActivityForResult(myIntent, 0);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}

