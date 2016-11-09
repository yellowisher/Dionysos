package RoomScreen.Connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

import org.bitlet.weupnp.GatewayDevice;
import org.xml.sax.SAXException;

import LobbyClient.LobbyServerInfo;

public class Server extends Thread {
	private static HashSet<String> names = new HashSet<String>();
	private static int numUser;

	private static Hashtable<String, PrintWriter> writers = new Hashtable<String, PrintWriter>();
	private static ServerSocket listener;
	private Socket lobbySocket;

	private static ObjectOutputStream lobbyWriter;

	private GatewayDevice device;
	private int port;

	public Server(ConnectInfo info, GatewayDevice device, ServerSocket listener, Socket socket) throws Exception {
		this.device = device;
		this.listener = listener;
		lobbySocket = socket;
		port = info.port;

		lobbyWriter = new ObjectOutputStream(socket.getOutputStream());

		System.out.println("The chat server is running.");

		System.out.println("Listening port : " + info.port);
		numUser = 0;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		System.out.println("Room server start running : " + listener.getLocalPort());
		try {
			while (true) {
				new Handler(listener.accept()).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (device != null) device.deletePortMapping(port, "TCP");
				listener.close();
			} catch (IOException | SAXException e) {
				e.printStackTrace();
			}
		}
	}

	private static class Handler extends Thread {
		private String name;
		private Socket socket;
		private BufferedReader in;
		private PrintWriter out;
		private String key; // for iterator
		private boolean leftCuzFull = false;

		public Handler(Socket socket) {
			System.out.println("Client with port " + socket.getPort() + " try to connect...");
			this.socket = socket;
		}

		public void run() {
			try {
				System.out.println("Client with port " + socket.getPort() + " connected!");

				// Create character streams for the socket.
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);

				if (numUser == 3) {
					out.println("FULL");
					leftCuzFull = true;
					return;
				}
				out.println("ENTER");
				System.out.println("ENTER sent! current user: " + (numUser + 1));

				String line;
				while (true) {
					line = in.readLine();
					if (line != null) System.out.println("Server recv something!");
					if (line != null && line.startsWith("OK")) {
						numUser++;
						lobbyWriter.writeObject(new String("JOIN"));
						break;
					}
				}

				while (true) {
					out.println("SUBMITNAME");
					name = in.readLine();

					synchronized (names) {
						if (!names.contains(name)) {
							names.add(name);
							break;
						}
					}
				}

				out.println("NAMEACCEPTED " + name);
				writers.put(name, out);
				Iterator<String> itr = names.iterator();
				while (itr.hasNext()) {
					key = itr.next();
					PrintWriter writer = writers.get(key);
					writer.println("BROADCAST " + "[Notice] [" + name + "] joined the room.");
					writer.println("USER_DEL_ALL");
					Iterator<String> itrr = names.iterator();
					while (itrr.hasNext()) {
						String n = itrr.next();
						writer.println("USER_ADD " + n);
					}
				}

				while (true) {
					String input = in.readLine();
					//System.out.println("[" + name + "] : " + input);
					if (input == null) {
						return;
					}
					else if (input.startsWith("CHOICE")) {
						String instrument = input.substring(7);
						Iterator<String> itr2 = names.iterator();
						while (itr2.hasNext()) {
							key = itr2.next();
							PrintWriter writer = writers.get(key);
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
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				// This client is going down!  Remove its name and its print
				// writer from the sets, and close its socket.
				if (leftCuzFull) {
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}

				numUser--;
				try {
					lobbyWriter.writeObject("LEFT");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				if (name != null && names.contains(name)) {
					names.remove(name);
				}
				else return;
				if (out != null) {
					writers.remove(out);
					Iterator<String> itr3 = names.iterator();

					while (itr3.hasNext()) {
						key = itr3.next();
						PrintWriter writer = writers.get(key);
						writer.println("BROADCAST " + "[Notice] [" + name + "] exited the room.");
						writer.println("USER_DEL_ALL");
						Iterator<String> itr4 = names.iterator();
						while (itr4.hasNext()) {
							String n = itr4.next();
							writer.println("USER_ADD " + n);
						}
						System.out.println("Disconnected - " + name);
					}

				}
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
	}
}