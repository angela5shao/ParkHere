package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import resources.User;


public class ServerCommunicator extends Thread
{
    private Socket				socket;
    private ObjectOutputStream	oos;
    private ObjectInputStream	ois;
    private PrintWriter			pw;
    private ServerListener		serverListener;


    public ServerCommunicator(Socket socket, ServerListener serverListener)
            throws IOException
    {
        this.socket = socket;
        this.serverListener = serverListener;
        this.oos = new ObjectOutputStream(socket.getOutputStream());
        oos.flush();
        this.ois = new ObjectInputStream(socket.getInputStream());
        this.pw = new PrintWriter(socket.getOutputStream());
    }

    public void run()
    {
        try{
            while(true)
            {
                Object obj = ois.readObject();
                if(obj instanceof User)
                {
                    User u = (User)obj;
                    System.out.println(u.userDomain);
                }

            }
        }
        catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }
}
