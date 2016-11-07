package LobbyServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.HashMap;

public class RequestListener extends Thread {
	private static final int PORT = 9322;

	private DatagramSocket listenSocket;
	private HashMap<String, RoomInfo> roomMap;
	byte[] readBuffer = new byte[4];

	public RequestListener(HashMap<String, RoomInfo> roomMap) throws SocketException {
		this.roomMap = roomMap;
		listenSocket = new DatagramSocket(PORT);
	}

	public void run() {
		while (true) {
			try {
				DatagramPacket receivePacket = new DatagramPacket(readBuffer, readBuffer.length);
				listenSocket.receive(receivePacket);

				SocketAddress address = receivePacket.getSocketAddress();

				ByteArrayOutputStream baos = new ByteArrayOutputStream(6400);
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(roomMap);
				byte[] sendData = baos.toByteArray();

				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address);
				listenSocket.send(sendPacket);
			} catch (IOException e) {
				listenSocket.close();
				e.printStackTrace();
			}
		}
	}
}