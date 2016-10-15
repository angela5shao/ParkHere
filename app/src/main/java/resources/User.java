package resources;

import java.io.Serializable;

/**
 * Created by angela02pd2014 on 10/14/16.
 */

public class User implements Serializable {

    private static final long serialVersionUID = -7856374444889271384L;
    public long userID;
    public String username;
    public String userDomain;
    public long userSessionKey;

    public User() // For guests?
    {
        userID = 0;
        username = "";
        userDomain = "";
        userSessionKey = -1;
    }

    public User(long UID, String uname, String udomain, long ukey)
    {
        userID = UID;
        username = uname;
        userDomain = udomain;
        userSessionKey = ukey;
    }
    
}
