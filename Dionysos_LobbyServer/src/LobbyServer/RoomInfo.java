package LobbyServer;

import java.io.Serializable;

public class RoomInfo implements Serializable {
	String roomName;
	String IPAdress;
	int port;
	String password;
	int numUser;

	RoomInfo(String roomName, String password) {
		this.roomName = roomName;
		this.password = password;
		numUser = 1;
	}
}