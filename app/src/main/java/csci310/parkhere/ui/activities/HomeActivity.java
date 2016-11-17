package csci310.parkhere.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;

public class HomeActivity extends Activity {
    Button _loginButton, _registerButton, _guestButton;
    ClientController clientController;
    Intent myIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_ui);

        _loginButton=(Button)findViewById(R.id.loginButton);
        _registerButton=(Button)findViewById(R.id.registerButton);
        _guestButton=(Button)findViewById(R.id.guestButton);


        ServerConnectTask sct = new ServerConnectTask();
        sct.execute((Void) null);




        Log.d("HOMEAC", "Oncreate");




        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clientController == null)
                {
                    ServerConnectTask sct = new ServerConnectTask();
                    sct.execute((Void) null);
                    return;
                }
                myIntent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(myIntent);
            }
        });

        _registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clientController == null)
                {
                    ServerConnectTask sct = new ServerConnectTask();
                    sct.execute((Void) null);
                    return;
                }
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


    private class ServerConnectTask extends AsyncTask<Void, Void, Boolean> {

        ServerConnectTask(){

        }
        @Override
        protected void onPreExecute(){
            //Display a progress dialog

        }
        @Override
        protected Boolean doInBackground(Void... params ){
            clientController = ClientController.getInstance();
//            Looper.prepare();
            Log.d("SERVERCONNECTTASK", "DOINBACKGROUND");

            if(clientController == null)
            {
                return false;
            }
            else
            {
                clientController.setCurrentActivity(HomeActivity.this);
            }


            return true;

        }
        @Override
        protected void onPostExecute(Boolean success) {
            Context c = getBaseContext();

            if(!success)
                Toast.makeText(c, "Server Connection Failed", Toast.LENGTH_SHORT).show();
        }

    }


}
