package csci310.parkhere.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientCommunicator;
import csci310.parkhere.controller.ClientController;

public class HomeActivity extends Activity {
    Button _loginButton, _registerButton, _guestButton;
    ClientController clientController;
    Intent myIntent;

    public static ClientCommunicator clientCommunicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_ui);

        _loginButton=(Button)findViewById(R.id.loginButton);
        _registerButton=(Button)findViewById(R.id.registerButton);
        _guestButton=(Button)findViewById(R.id.guestButton);

        Log.v("HomeActivity", "start");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);


        clientController = new ClientController();
        clientController.setCurrentActivity(this);


        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myIntent = new Intent(v.getContext(), LoginActivity.class);
                startActivityForResult(myIntent);
            }
        });

        _registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myIntent = new Intent(v.getContext(), RegisterMainActivity.class);
                startActivity(myIntent);
            }
        });

        _guestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });
    }
}
