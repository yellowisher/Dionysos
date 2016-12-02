package LobbyServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import RoomInfo.RoomInfo;

// STTN(Simple Traversal of TCP Through NAT) Server!
// TCP version of STUN server
public class STTNServer extends Thread {
	private static final int PORT = 9999;

	private HashMap<String, RoomInfo> roomMap;
	private HashMap<String, PrintWriter> writerMap;
	private HashMap<PunchHandler, String> waitingList = new HashMap<PunchHandler, String>();

	private ServerSocket punchListener;

	public STTNServer(HashMap<String, RoomInfo> roomMap, HashMap<String, PrintWriter> writerMap) throws IOException {
		this.roomMap = roomMap;
		this.writerMap = writerMap;
		punchListener = new ServerSocket(PORT);
	}

	@Override
	public void run() {
		while (true) {
			try {
				new PunchHandler(punchListener.accept()).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class PunchHandler extends Thread {
		static final int TYPE_HOST = 0;
		static final int TYPE_CLIENT = 1;
		int type;
		boolean waiting;
		String myStr, sendStr;

		Socket socket;
		BufferedReader reader;
		PrintWriter writer;

		PunchHandler(Socket socket) throws IOException {
			this.socket = socket;

			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream(), true);
		}

		@Override
		public void run() {
			try {
				String[] info = reader.readLine().split("/");
				type = info[0].equals("H") ? TYPE_HOST : TYPE_CLIENT;
				String roomName = info[1];
				String privateAddress = info[2];
				int privatePort = Integer.parseInt(info[3]);

				String publicAddress = socket.getInetAddress().getHostAddress();
				int publicPort = socket.getPort();

				myStr = privateAddress + "/" + privatePort + "/" + publicAddress + "/" + publicPort;

				RoomInfo targetRoom = roomMap.get(roomName);
				PrintWriter roomWriter = writerMap.get(roomName);

				if (targetRoom == null) return;

				// When client send hole punching request message;
				// Send host to hole punch, wait for host
				if (type == TYPE_CLIENT) {
					waitingList.put(this, roomName);
					roomWriter.println("CONN");

					// Busy wait for host send data
					// Maybe better idea?
					System.out.println("Client wait for host : " + roomName);
					waiting = true;
					while (waiting) {
						;
					}

					System.out.println("Client waiting finished!");
					writer.println(sendStr);
					System.out.println("Sent data for punching to client");
				}
				else {
					// Got message from host;
					// Stop waiting client thread, send data to host
					for (PunchHandler handler : waitingList.keySet()) {
						if (waitingList.get(handler).equals(roomName)) {
							waitingList.remove(handler);
							// Send oppenent's end points
							handler.sendStr = myStr;
							sendStr = handler.myStr;
							handler.waiting = false;
							break;
						}
					}

					writer.println(sendStr);
					System.out.println("Sent data for punching to host");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
