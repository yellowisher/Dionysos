package LobbyClient;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import RoomInfo.RoomInfo;

public class LANListener extends Thread {
	public static final int PORT = 7712;
	byte[] readBuffer = new byte[4];
	RoomInfo roomInfo;
	DatagramSocket listenSocket;

	public LANListener(RoomInfo roomInfo) throws SocketException {
		this.roomInfo = roomInfo;
		listenSocket = new DatagramSocket(PORT);
	}

	public void clientLeft() {
		roomInfo.numUser--;
	}

	public void clientJoin() {
		roomInfo.numUser++;
	}

	public void endListen() {
		listenSocket.close();
	}

	@Override
	public void run() {
		
		while (true) {
			DatagramPacket packet = new DatagramPacket(readBuffer, readBuffer.length);

			try {
				listenSocket.receive(packet);
				ByteArrayOutputStream baos = new ByteArrayOutputStream(6400);
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(roomInfo);
				byte[] sendData = baos.toByteArray();

				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getSocketAddress());
				listenSocket.send(sendPacket);
			} catch (Exception e) {
				listenSocket.close();
				e.printStackTrace();
			}
		}
	}
}
