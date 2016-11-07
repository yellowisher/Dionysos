package RoomInfo;

import java.io.Serializable;

public class RoomInfo implements Serializable {
	public String roomName;
	public String IPAdress;
	public int port;
	public String password;
	public int numUser;

	public RoomInfo(String roomName, String password) {
		this.roomName = roomName;
		this.password = password;
		numUser = 1;
	}
}