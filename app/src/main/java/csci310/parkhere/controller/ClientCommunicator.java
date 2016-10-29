package csci310.parkhere.controller;

/**
 * Created by angela02pd2014 on 10/16/16.
 */

public class ClientCommunicator extends Thread{
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    public ClientCommunicator(){
        try {
            socket = new Socket(hostname, 61129);
//            System.out.println("Connected!");
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            start();
        } catch (IOException ioe) {
            System.out.println("IOE in Client constructor: " + ioe.getMessage());
        }
    }
    public void send(String command, HashMap<String, Object> entry){
        oos.writeObject();
    }
}
