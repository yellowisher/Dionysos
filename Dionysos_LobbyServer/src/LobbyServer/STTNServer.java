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

/* 
 * STTN(Simple Traversal of TCP Through NAT) Server!
 * TCP version of STUN server
 * To TCP hole punching, host can client have to know each other's public/private end points
 * So this server do that role
 * Referenced document -> http://www.bford.info/pub/net/p2pnat/index.html
 */

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
				try {
					punchListener.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.out.println("Hole punch helper is closing");
			}
		}
	}

	class PunchHandler extends Thread {
		static final int TYPE_HOST = 0;
		static final int TYPE_CLIENT = 1;
		int type;
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

				// When client sent hole punching request message;
				// Tell host to punch a hole, wait for response from host
				if (type == TYPE_CLIENT) {
					// When host response arrives, fill this String
					sendStr = null;

					// Put this class(which is also Thread) waiting list
					// There could be several join request to same room at the same time,
					// Handler(this class) must be key in HashMap not room name
					waitingList.put(this, roomName);
					roomWriter.println("CONN");

					// Wait until host response or 3000ms, which considered as
					// host somehow failed to response (host closed room at perfect timing etc...)
					System.out.println("Client start waiting for host : " + roomName);
					synchronized (this) {
						try {
							wait(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					System.out.println("Client finished waiting!");

					if (sendStr == null) {
						System.out.println("Host didn't response to request!");
					}
					else {
						writer.println(sendStr);
						System.out.println("Sent punching data to client");
					}
				}
				else {
					// Got message from host;
					// Stop waiting client thread, send data to host
					for (PunchHandler handler : waitingList.keySet()) {
						if (waitingList.get(handler).equals(roomName)) {
							waitingList.remove(handler);

							// Send end points of each opponent 
							handler.sendStr = myStr;
							sendStr = handler.myStr;

							synchronized (handler) {
								handler.notify();
							}
							break;
						}
					}

					writer.println(sendStr);
					System.out.println("Sent punching data to host");
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
