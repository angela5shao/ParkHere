package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ServerListener extends Thread
{
    // private Vector<AWSThread> awsThreads = new Vector<AWSThread>();
    private Vector<ServerCommunicator> commVec ;

    ServerSocket ss;

    public ServerListener() {
        commVec = new Vector<>();
        this.start();
    }

    public void run() {
        try {
            ss = new ServerSocket(61129);
            while (true) {
                Socket s = ss.accept();
                System.out.println("Accept");
                ServerCommunicator sc = new ServerCommunicator(s, this);
                commVec.add(sc);
                sc.start();

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException ioe) {
                    System.out.println(ioe.getMessage());
                }
            }
        }
    }
}
