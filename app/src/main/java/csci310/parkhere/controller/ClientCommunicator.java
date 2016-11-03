package csci310.parkhere.controller;

/**
 * Created by angela02pd2014 on 10/16/16.
 */

import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import resource.MyEntry;
import resource.NetworkPackage;
import resource.ParkingSpot;
import resource.SearchResults;
import resource.Time;
import resource.TimeInterval;
import resource.User;

public class ClientCommunicator extends Thread{

    private static final long serialVersionUID = 12391823917283L;

    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private ClientController controller;

    int counter;

    public ClientCommunicator(ClientController controller){
        counter = 0;
        this.controller = controller;
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
        Looper.prepare();

        try {
            while(true) {
                Object obj = ois.readObject();
                counter++;
                Log.d("Counter", String.valueOf(counter));
                System.out.println("do receive the networkpackage");
                if (obj instanceof NetworkPackage) {
                    NetworkPackage np = (NetworkPackage) obj;
                    MyEntry<String, Serializable> entry = np.getCommand();
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    System.out.println("Command Key: " + key);
                    if(key.equals("RF")){
                        Log.d("CONTROLLER", "Set flag true");
                        controller.registerFailed = true;
                    } else if(key.equals("LF")){
                        controller.loginFailed = true;

                    } else if(key.equals("LOF")){

                    } else if(key.equals("LOGIN")){
                        User result = (User) value;
                        Log.d("LOGIN", result.userName);
                        controller.setUser(result);
                    } else if(key.equals("REGISTER")){
                        User result = (User) value;

                        controller.setUser(result);

                    } else if(key.equals("LOGOUT")){

                    } else if(key.equals("SEARCH_RESULT")){
                        SearchResults result = (SearchResults)value;
                        Log.d("Results", result.searchResultList.get(0).getStreetAddr());
                        controller.toDispaySearch = true;
                        controller.updateActivity();
                        controller.searchResults = result;
                        Log.d("SEARCH_RESULT", "Size "+ String.valueOf(result.searchResultList.size()));
                    } else if(key.equals("ADDSPACE")) {
                        ParkingSpot spot = (ParkingSpot)value;
                        Log.d("Result","add space " + String.valueOf(spot.getParkingSpotID()));

                        controller.parkingSpots.add(spot);
//                        controller.requestMyParkingSpotList();
//                        controller.providerToshowSpaces = true;

                    } else if(key.equals("RESPONSEPARKINGSPOT"))
                    {
                        ArrayList<ParkingSpot> myParkingSpot = (ArrayList<ParkingSpot>)value;
                        controller.parkingSpots = myParkingSpot;
                        Log.d("RESPONSEPARKINGSPOT","Receive list of parkingspot" + myParkingSpot.size());

                    } else if(key.equals("RESPONSEINTERVAL"))
                    {
                        HashMap<String, Serializable> map = (HashMap<String, Serializable>) value;
                        ArrayList<TimeInterval> intervals = (ArrayList<TimeInterval>) map.get("TIMEINTERVAL");
                        Long spotID = (Long)map.get("PARKINGSPOTID");
                        controller.setSpotTimeInterval(spotID,intervals);
                        Log.d("RESPONSEINTERVAL","Receive list of interval" + intervals.size());
                    }
                    controller.updateActivity();
                }
                oos.flush();
            }
        } catch (OptionalDataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void send(String command, Serializable entry) throws IOException {
        Log.v("send to server: ", command);
        NetworkPackage NP = new NetworkPackage();
        NP.addEntry(command, entry);

        if(oos == null)
            Log.d("oos","oos is null");
        oos.writeObject(NP);
        oos.flush();
    }


    public void sendPackage(NetworkPackage np) throws IOException {
        oos.writeObject(np);
        oos.flush();
    }
}
