package csci310.parkhere.controller;

/**
 * Created by angela02pd2014 on 10/16/16.
 */

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;

import resource.NetworkPackage;

public class ClientCommunicator extends Thread{

    private static final long serialVersionUID = 12391823917283L;

    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    public ClientCommunicator(){
        try {
            socket = new Socket("104.236.143.142", 61129);
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();

            ois = new ObjectInputStream(socket.getInputStream());

            start();
        }
        catch (IOException ioe) {
           ioe.printStackTrace();
        }
    }

    public void run()
    {
        try {
            while(true)
            {
                sleep(1000);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void send(String command, HashMap<String, Serializable> entry) throws IOException {
        Log.v("send to server: ", command);
        NetworkPackage NP = new NetworkPackage();
        NP.addEntry(command, entry);

        if(oos == null)
            Log.d("oos","oos is null");

        oos.writeObject(NP);
        oos.flush();
    }
}
