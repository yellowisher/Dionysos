package LobbyServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;

import RoomInfo.RoomInfo;


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

				ByteArrayOutputStream baos = new ByteArrayOutputStream(6400);
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(roomMap);
				byte[] sendData = baos.toByteArray();

				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getSocketAddress());
				listenSocket.send(sendPacket);
				
				System.out.println("Sent room list to client");
				
			} catch (IOException e) {
				listenSocket.close();
				System.out.println("Request listener closing");
			}
		}
	}
}