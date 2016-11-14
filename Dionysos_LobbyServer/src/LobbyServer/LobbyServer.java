package LobbyServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ClientInfoStatus;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import RoomInfo.RoomInfo;

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
		private Timer disconChecker;

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

					if (room.IPAdress == null) room.IPAdress = socket.getInetAddress().getHostAddress();
					writer.println(room.IPAdress + "/" + socket.getPort());
					roomMap.put(room.roomName, room);
				}

				disconChecker = new Timer();
				DisconChecker checker = new DisconChecker();
				disconChecker.schedule(checker, RoomInfo.PING_DELAY, RoomInfo.PING_DELAY);

				String msg;
				reader = new ObjectInputStream(socket.getInputStream());
				while (true) {
					msg = (String) reader.readObject();
					if (msg != null) {
						checker.reset();

						if (msg.equals("Ping")) {
							System.out.println("Ping from " + room.roomName);
						}
						else {
							System.out.println("Client in " + room.roomName + " has " + msg);
							if (msg.equals("JOIN")) {
								room.numUser++;
							}
							else if (msg.equals("LEFT")) {
								room.numUser--;
							}
						}
					}
				}
			} catch (Exception e) {
				disconChecker.cancel();
				try {
					System.out.println("A room is closed");
					roomMap.remove(room.roomName);
					socket.close();
				} catch (IOException e1) {

				}
			}
		}

		class DisconChecker extends TimerTask {
			int count = 0;

			@Override
			public void run() {
				System.out.println((count + 1));

				if (++count == 4) {
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			void reset() {
				count = 0;
			}
		}
	}
}