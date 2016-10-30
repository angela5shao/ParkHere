package csci310.parkhere.controller;

/**
 * Created by angela02pd2014 on 10/16/16.
 */

import android.util.Log;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import csci310.parkhere.resource.NetworkPackage;

public class ClientCommunicator extends Thread implements Serializable{

    private static final long serialVersionUID = 12391823917283L;

    private MySocket socket;
    private MyObjectInputStream ois;
    private ObjectOutputStream oos;
    public ClientCommunicator(){
//        Log.d("&&&&&&&&&&&&&&&&& ", "waiting for the somthing wrong3");
        try {
////            Log.d("&&&&&&&&&&&&&&&&& ", "waiting for the somthing wrong4");
            socket = new MySocket("104.236.143.142", 61129);
////            Log.d("&&&&&&&&&&&&&&&&& ", "waiting for the somthing wrong5");
            ois = new MyObjectInputStream(socket.getInputStream());
            oos = new MyObjectOutputStream(socket.getOutputStream());
            start();
        }
        catch (IOException ioe) {
            Log.d("&&&&&&&&&&&&&&&&& ", "waiting for the somthing wrong6");
        }
    }
    public void send(String command, HashMap<String, Serializable> entry) throws IOException {
        Log.v("send to server: ", command);
        NetworkPackage NP = new NetworkPackage();
        NP.addEntry(command, entry);
        oos.writeObject(NP);
    }
}
