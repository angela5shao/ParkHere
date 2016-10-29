package resource;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = -7856374444889271384L;

    public long userID;
    public String username;
    public String userDomain;



    public User()
    {
        userID = 0;
        username = "";
        userDomain = "";
    }

    public User(long UID, String uname, String udomain)
    {
        userID = UID;
        username = uname;
        userDomain = udomain;
    }
    
    public long getID(){
    	return userID;
    }
    public String getUsername(){
    	return username;
    }
    public String getUserDomain(){
    	return userDomain;
    }
}
