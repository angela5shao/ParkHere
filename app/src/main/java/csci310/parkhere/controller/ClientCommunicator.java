package csci310.parkhere.controller;

import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.net.Socket;

import resource.NetworkPackage;


/**
 * Created by YanbinLyu on 11/2/16.
 */

public class ClientCommunicator extends Thread {
    private static final long serialVersionUID = 12391823917283L;
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private ClientController controller;


    public boolean connecting;
    public ClientCommunicator(ClientController controller) {
        this.controller = controller;
        connecting = false;
        ServerConnAsync sca = new ServerConnAsync();
        sca.execute();
    }


    public void connect() throws IOException {
        socket = new Socket("ec2-35-164-64-84.us-west-2.compute.amazonaws.com", 61129);
        oos = new ObjectOutputStream(socket.getOutputStream());
        oos.flush();
        ois = new ObjectInputStream(socket.getInputStream());
    }


    private class ServerConnAsync extends AsyncTask<Void, Void, Boolean> {



        ServerConnAsync() {


        }



        @Override
        protected Boolean doInBackground(Void... params) {

            try {

                connect();

                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }



        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);

            if(success)
            {
                connecting = true;
                start();
            }

        }
    }




    public void run()
    {
        Looper.prepare();

        try {
            while(true) {
                NetworkPackage obj = (NetworkPackage)ois.readObject();

                System.out.println("do receive the networkpackage");
                Log.d("NetworkPackage", "NP is null?" + String.valueOf(obj == null));

                if(obj == null)
                    continue;

                if(obj.getCommand().getKey().equals("EXCEPTION"))
                {
                    Log.d("Server GG:", "GG");
                    System.err.println(obj.getCommand().getValue());
                    controller.resetConnection();

                    socket.close();

                    break;
                }

                //controller.updateActivity();
                controller.updateReceived(obj);
            }
        } catch (OptionalDataException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            controller.resetConnection();
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    public void send(String command, Serializable entry) {
        if(!connecting)
        {
            controller.resetConnection();
            return;
        }
        Log.v("send to server: ", command);

        NetworkPackage NP = new NetworkPackage();
        NP.addEntry(command, entry);


        try {
            oos.writeObject(NP);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            connecting = false;
            controller.resetConnection();
        }
    }


    public void sendPackage(NetworkPackage np)  {
        Log.d("SENDPACAGE", "send once");

        if(!connecting)
        {
            controller.resetConnection();
            return;
        }


        try {
            oos.writeObject(np);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            connecting = false;
            controller.resetConnection();
        }
    }
}

