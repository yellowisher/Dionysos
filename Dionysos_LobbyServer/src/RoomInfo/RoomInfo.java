package RoomInfo;

import java.io.Serializable;

public class RoomInfo implements Serializable {
	private static final long serialVersionUID = 322L;
	public static final int PING_DELAY = 30000;
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