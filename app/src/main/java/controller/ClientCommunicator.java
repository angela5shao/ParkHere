package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientCommunicator extends Thread
{

    private Socket s;
    private ObjectOutputStream	oos;
    private ObjectInputStream	ois;

    public ClientCommunicator()
    {
        boolean socketReady = initializeVariables();

        if(socketReady)
            start();
    }


    public boolean initializeVariables()
    {
        try
        {
            this.s =  new Socket("104.236.143.142", 61129);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            this.oos = new ObjectOutputStream(s.getOutputStream());
            oos.flush();
            this.ois = new ObjectInputStream(s.getInputStream());

        } catch (IOException ioe) {
            System.out.println("IOE in ChatThread constructor: " + ioe.getMessage());
            return false;
        }

        return true;
    }

    public void sendObject(Object obj)
    {
        try
        {
            oos.writeObject(obj);
            oos.flush();
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    public void run()
    {
        Object obj = null;
        try
        {
            while(true)
            {
                obj = ois.readObject();

            }
        }
        catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

}
