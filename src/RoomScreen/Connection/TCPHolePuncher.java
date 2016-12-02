package RoomScreen.Connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import LobbyClient.LobbyServerInfo;

public class TCPHolePuncher extends Thread {
	public static final int TYPE_HOST = 0;
	public static final int TYPE_CLIENT = 1;
	int type;

	String roomName;
	public Socket connectedSocket = null;

	public TCPHolePuncher(int type) {
		this.type = type;
	}
	public TCPHolePuncher(int type, String roomName) {
		this.type = type;
		this.roomName = roomName;
	}

	@Override
	public void run() {
		int port = 0;
		InetSocketAddress oppPrivatePoint, oppPublicPoint;

		// TCP hole punching Step #1
		// Connect to TCP hole punching helper server
		// Send IP address, port number of this host to server,
		// read oppenent's private/public IP and port from server.
		try {
			Socket punchSocket = new Socket();

			// We have to use same port number furthermore; set reuse address
			punchSocket.setReuseAddress(true);

			punchSocket.bind(new InetSocketAddress(0));
			port = punchSocket.getLocalPort();
			punchSocket.connect(new InetSocketAddress(LobbyServerInfo.IPAddress, LobbyServerInfo.punchPort));
			System.out.println("Connected to punch server!");

			BufferedReader reader = new BufferedReader(new InputStreamReader(punchSocket.getInputStream()));
			PrintWriter writer = new PrintWriter(punchSocket.getOutputStream(), true);

			String str = (type == TYPE_HOST ? "H/" : "C/") + roomName + "/";
			str += InetAddress.getLocalHost().getHostAddress() + "/" + port;
			writer.println(str);
			String[] info = reader.readLine().split("/");

			
			oppPrivatePoint = new InetSocketAddress(info[0], Integer.parseInt(info[1]));
			oppPublicPoint = new InetSocketAddress(info[2], Integer.parseInt(info[3]));

			System.out.println("Got oppenent's infomation");
			System.out.println("private "+info[0]+":"+info[1]);
			System.out.println("public "+info[2]+":"+info[3]);
			
			punchSocket.close();
		} catch (IOException e) {
			System.out.println("Failed to connect lobby server");
			e.printStackTrace();
			return;
		}

		// TCP hole punching Step #2
		// We just got oppenent's private/public end point.
		// Client tries to connect via private end point first
		if (type == TYPE_CLIENT) {
			Socket LANSocket = null, realSocket = null;
			try {
				// Wait for host listening;
				Thread.sleep(300);

				LANSocket = new Socket();
				LANSocket.setReuseAddress(true);
				LANSocket.bind(new InetSocketAddress(port));

				// Connecting to host who is in the same NAT would be very fast
				// so set connection timeout only for 1 second
				LANSocket.connect(oppPrivatePoint, 1000);

				// Reach here in case of connection to private end point succeed
				// Just keep that connection; abort hole punching
				connectedSocket = LANSocket;
				return;
			} catch (Exception e) {
				// Failed connecting to private end point
				// Wait 0.5 second for host to open listening socket
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

				// Connect to "real" ServerSocket that might be punched 
				try {
					realSocket = new Socket();
					realSocket.setReuseAddress(true);
					realSocket.bind(new InetSocketAddress(port));

					realSocket.connect(oppPublicPoint, 3000);

					connectedSocket = realSocket;
					return;
				} catch (IOException ioe) {
					System.out.println("Failed to hole punching");
					ioe.printStackTrace();
				}
			}
		}
		// Host just listen for client's trial
		else {
			ServerSocket LANSocket = null, publicSocket = null;
			Socket dummySocket = null;
			try {
				LANSocket = new ServerSocket();

				// Timeout of client local connection is only 1 second
				// And client will wait for extra 0.3 second, so set accept only for 1.5 second
				LANSocket.setSoTimeout(1500);
				LANSocket.setReuseAddress(true);
				LANSocket.bind(new InetSocketAddress(port));

				connectedSocket = LANSocket.accept();
				// Client connected via private end point; abort hole punching
				try {
					LANSocket.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
				return;
			} catch (Exception e) {
				try {
					try {
						LANSocket.close();
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}

					// Client failed connection to private end point
					// Send dummy packet (which would not reach to client) for
					// punch a hole on NAT
					dummySocket = new Socket();
					dummySocket.setReuseAddress(true);
					dummySocket.bind(new InetSocketAddress(port));
					dummySocket.connect(oppPublicPoint, 1);

					// Should never reach here
					System.out.println("WTF?");
				} catch (Exception e1) {
					try {
						try {
							dummySocket.close();
						} catch (IOException ioe) {
							ioe.printStackTrace();
						}

						// Finally, we made hole for client, start listening
						publicSocket = new ServerSocket();
						publicSocket.setSoTimeout(5000);
						publicSocket.setReuseAddress(true);
						publicSocket.bind(new InetSocketAddress(port));
						connectedSocket = publicSocket.accept();
						System.out.println("TCP hole punching succeed");
					} catch (Exception e2) {
						// Failed to hole punching
						System.out.println("TCP hole punching failed");
					} finally {
						try {
							publicSocket.close();
						} catch (IOException e2) {
							e2.printStackTrace();
						}
					}
				}
			}
		}
	}
}