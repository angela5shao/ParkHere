package controller;

import resources.User;

public class Main
{

    public static void main(String[] args)
    {
        // TODO Auto-generated method stub
        User u = new User(0, "gg", "usc.edu", 0);
        ClientCommunicator cc = new ClientCommunicator();
        cc.sendObject(u);
    }

}
