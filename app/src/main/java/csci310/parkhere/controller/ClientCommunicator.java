package csci310.parkhere.controller;

import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

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
    public ClientCommunicator(ClientController controller) throws IOException {
        this.controller = controller;
        ServerConnAsync sca = new ServerConnAsync();
        sca.execute();
    }


    private class ServerConnAsync extends AsyncTask<Void, Void, Boolean> {



        ServerConnAsync() {


        }
        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                socket = new Socket("ec2-35-164-64-84.us-west-2.compute.amazonaws.com", 61129);
                oos = new ObjectOutputStream(socket.getOutputStream());
                oos.flush();
                ois = new ObjectInputStream(socket.getInputStream());
                controller.startReceiving();
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
                start();
            }

        }
    }




    public void run()
    {
        Looper.prepare();

        try {
            while(true) {
                if(ois==null) {
                    controller.stopReceiving();
                }
                else{
                    try {
                        socket = new Socket("ec2-35-164-64-84.us-west-2.compute.amazonaws.com", 61129);
                        oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.flush();

                        ois = new ObjectInputStream(socket.getInputStream());
                        controller.startReceiving();
                    } catch (IOException e) {
                    }
                }
                NetworkPackage obj = (NetworkPackage)ois.readObject();
                System.out.println("do receive the networkpackage");
                Log.d("NetworkPackage", "NP is null?" + String.valueOf(obj == null));

                if(obj == null)
                    continue;

                //controller.updateActivity();
                controller.updateReceived(obj);

//                oos.flush();
            }
        } catch (OptionalDataException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    public void send(String command, Serializable entry) throws IOException {
        Log.v("send to server: ", command);
        NetworkPackage NP = new NetworkPackage();
        NP.addEntry(command, entry);

        if(oos == null){
            Log.d("oos","oos is null");
//            Toast.makeText(controller.getCurrentActivity().getBaseContext(), "Lost the Network Connection", Toast.LENGTH_SHORT).show();
            return;
        }
        oos.writeObject(NP);
        oos.flush();
    }


    public void sendPackage(NetworkPackage np) throws IOException {
        Log.d("SENDPACAGE", "send once");
        if(oos == null){
            Log.d("oos","oos is null");
            controller.stopReceiving();
//            Toast.makeText(controller.getCurrentActivity().getBaseContext(), "Lost the Network Connection", Toast.LENGTH_SHORT).show();
            return;
        }
        oos.writeObject(np);
        oos.flush();
    }
}

