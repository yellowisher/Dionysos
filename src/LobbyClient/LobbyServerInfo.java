package LobbyClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import MainScreen.MainFrame;
import RoomScreen.Layout.RoomPanel;

public class LobbyServerInfo {
	private static final String hostName = "m1rukim.iptime.org";
	//private static final String hostName = "localhost";

	public static InetAddress IPAddress;
	public static final int hostPort = 9392;
	public static final int clientPort = 9322;

	private static boolean inited = false;

	public static void init() throws UnknownHostException {
		if (inited) return;

		IPAddress = InetAddress.getByName(hostName);
		inited = true;
	}
}
