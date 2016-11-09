package LobbyClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LobbyServerInfo {
	private static final String hostName = "m1rukim.iptime.org";
	//private static final String hostName = "localhost";
	

	public static InetAddress IPAddress;
	public static final int hostPort = 9392;
	public static final int clientPort = 9322;

	public static void init() {
		try {
			IPAddress = InetAddress.getByName(hostName);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
