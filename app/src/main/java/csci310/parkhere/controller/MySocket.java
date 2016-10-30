package csci310.parkhere.controller;

import java.io.Serializable;
import java.net.Socket;

/**
 * Created by ivylinlaw on 10/29/16.
 */
public class MySocket extends Socket implements Serializable {
    private static final long serialVersionUID = 12391901233917283L;

    MySocket(String hostname, int portNumber) throws java.net.UnknownHostException, java.io.IOException
    {
        super(hostname, portNumber);
    }
}
