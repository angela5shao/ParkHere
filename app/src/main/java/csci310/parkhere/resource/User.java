package resource;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = -7856374444889271384L;

    public long userID;
    public String username;
    public String userDomain;
    public long userSessionKey;



    public User()
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
