package csci310.parkhere.resource;

import java.io.Serializable;

public class NetworkPackage implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7386596359060429547L;
	private MyEntry<String, Serializable> entry;
	private User user;
	
	
	public NetworkPackage() {
		user = null;
		entry = null;
	}
	
	public NetworkPackage(User u) {
		user = u;
		entry = null;
	}
	
	public void addEntry(String command, Serializable obj) {
		entry = new MyEntry<String, Serializable>(command, obj);
	}
	
	public MyEntry<String, Serializable> getCommand(){
		return entry;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
