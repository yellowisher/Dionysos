package RoomScreen.Connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

import org.bitlet.weupnp.GatewayDevice;
import org.xml.sax.SAXException;

import LobbyClient.LANListener;
import RoomInfo.RoomInfo;
import RoomScreen.Layout.RoomPanel;

public class Server extends Thread {
	private static int numUser;

	private static Hashtable<String, PrintWriter> writers = new Hashtable<String, PrintWriter>();
	private ServerSocket listener;
	private Timer surviveReporter;

	// If there is no reference to socket, GC gonna free it!
	@SuppressWarnings("unused")
	private Socket lobbySocket;
	private static ObjectOutputStream lobbyWriter;
	private static BufferedReader lobbyReader;

	private GatewayDevice device;
	private LANListener lanListener;
	private int port;
	private String roomName;

	public Server(GatewayDevice device, ServerSocket listener, Socket socket, LANListener lanListener, String roomName) throws Exception {
		this.device = device;
		this.listener = listener;
		this.lanListener = lanListener;
		this.roomName = roomName;
		RoomPanel.instance.server = this;

		// lanListener is null; When is not LAN mode, start reporting(pint) to lobby server
		// if it fails 4 times in a row, lobby server remove it from room list
		if (lanListener == null) {
			lobbySocket = socket;
			lobbyWriter = new ObjectOutputStream(socket.getOutputStream());
			lobbyReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			port = socket.getLocalPort();

			surviveReporter = new Timer();
			surviveReporter.schedule(new Reporter(), RoomInfo.PING_DELAY, RoomInfo.PING_DELAY);
		}
		numUser = 0;
	}

	public void closeServer() {
	}

	// Listener for accept client join
	@Override
	public void run() {
		System.out.println("Room server start running");

		try {
			while (true) {
				
				// Client listener is null; when using TCP hole punching
				// Start listening to lobby server, hole punching request
				if (listener == null) {
					String line = lobbyReader.readLine();
					if (line != null && line.startsWith("CONN")) {
						TCPHolePuncher puncher = new TCPHolePuncher(TCPHolePuncher.TYPE_HOST, roomName);
					
						// Wait for TCPHolePuncher finish connect
						puncher.start();
						puncher.join();

						Socket socket = puncher.connectedSocket;
						if (socket != null) {
							// Connection done by TCP hole punching;
							new Handler(socket, lanListener).start();
						}
					}
				}
				// Client listener is not null; listen for client connections
				else {
					new Handler(listener.accept(), lanListener).start();
				}
			}
		} catch (Exception e) {
			try {
				if (device != null) device.deletePortMapping(port, "TCP");
				if (lanListener != null) listener.close();
				else lanListener.endListen();
			} catch (IOException | SAXException e1) {
				e1.printStackTrace();
			}
		}
	}

	class Reporter extends TimerTask {
		@Override
		public void run() {
			try {
				lobbyWriter.writeObject("Ping");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static class Handler extends Thread {
		private String name;
		private Socket socket;
		private BufferedReader in;
		private PrintWriter out;
		private LANListener lanListener;

		public Handler(Socket socket, LANListener lanListener) {
			System.out.println("Server : Client with port " + socket.getPort() + " try to connect...");
			this.socket = socket;
			this.lanListener = lanListener;
		}

		public void run() {
			try {
				System.out.println("Server : Client with port " + socket.getPort() + " connected!");

				// Create character streams for the socket.
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);

				if (numUser == 3) {
					out.println("FULL");
					throw new RoomIsFullException("Room is full");
				}
				out.println("ENTER");

				String line;
				while (true) {
					line = in.readLine();
					if (line != null && line.startsWith("OK")) {
						numUser++;
						if (lanListener == null) lobbyWriter.writeObject(new String("JOIN"));
						else lanListener.clientJoin();
						break;
					}
				}

				while (true) {
					out.println("SUBMITNAME");
					name = in.readLine();

					synchronized (writers) {
						if (!writers.containsKey(name)) {
							out.println("NAMEACCEPTED " + name);
							writers.put(name, out);
							break;
						}
					}
				}

				for (PrintWriter writer : writers.values()) {
					writer.println("BROADCAST " + "[Notice] [" + name + "] joined the room.");
					writer.println("USER_DEL_ALL");

					for (String name : writers.keySet()) {
						writer.println("USER_ADD " + name);
					}
				}

				while (true) {
					String input = in.readLine();
					if (input == null) {
						return;
					}
					else if (input.startsWith("CHOICE")) {
						String instrument = input.substring(7);
						for (PrintWriter writer : writers.values()) {
							writer.println("BROADCAST " + "[Notice] [" + name + "] chose the " + instrument + ".");
						}
					}
					else if (input.startsWith("MSG")) {
						for (PrintWriter writer : writers.values()) {
							writer.println("MESSAGE" + name + ": " + input.substring(3));
						}
					}
					else {
						for (PrintWriter writer : writers.values()) {
							writer.println(input);
						}
					}
				}
			} catch (RoomIsFullException e) {
				// Exception for denial join; user didn't join yet
			} catch (IOException e) {
				// Exception for lost connection
				numUser--;

				if (name != null) {
					System.out.println("REMOVE");
					writers.remove(name);

					for (PrintWriter writer : writers.values()) {
						writer.println("BROADCAST " + "[Notice] [" + name + "] exited the room.");
						writer.println("USER_DEL_ALL");

						for (String name : writers.keySet()) {
							writer.println("USER_ADD " + name);
						}
					}
				}

				try {
					if (lanListener == null) lobbyWriter.writeObject("LEFT");
					else lanListener.clientLeft();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
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