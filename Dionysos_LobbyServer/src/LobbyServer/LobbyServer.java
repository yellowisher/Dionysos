package LobbyServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class LobbyServer {
	private static final int PORT = 9392;

	private static HashMap<String, RoomInfo> roomMap = new HashMap<String, RoomInfo>();

	public static void main(String[] args) throws Exception {
		System.out.println("Lobby server is running!");
		ServerSocket hostListener = new ServerSocket(PORT);

		try {
			// Start request listening thread
			new RequestListener(roomMap).start();

			while (true) {

				// Start host listening thread
				new HostHandler(hostListener.accept()).start();
			}
		} finally {
			hostListener.close();
		}
	}

	private static class HostHandler extends Thread {
		private RoomInfo room;
		private Socket socket;
		private ObjectInputStream reader;
		private PrintWriter writer;

		HostHandler(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try {
				reader = new ObjectInputStream(socket.getInputStream());
				writer = new PrintWriter(socket.getOutputStream(), true);

				room = (RoomInfo) reader.readObject();

				synchronized (roomMap) {
					if (roomMap.containsKey(room.roomName)) {
						writer.println("EXIST");
						socket.close();
						return;
					}

					writer.println("CREATED");
					room.IPAdress = socket.getInetAddress().getHostAddress();
					room.port = socket.getPort();
					roomMap.put(room.roomName, room);
				}

				while (true) {
					String msg = (String) reader.readObject();
					
					//System.out.println(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {

					//System.out.println("CLOSE!");

					socket.close();
					roomMap.remove(room.roomName);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}