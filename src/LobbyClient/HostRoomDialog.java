package LobbyClient;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import MainScreen.MainFrame;
import RoomInfo.RoomInfo;
import RoomScreen.Connection.Client;
import RoomScreen.Connection.Server;

import org.bitlet.weupnp.GatewayDevice;
import org.bitlet.weupnp.GatewayDiscover;

public class HostRoomDialog extends JDialog {
	Socket socket;
	JTextField roomNameField = new JTextField(20);
	JTextField passwordField = new JTextField(10);
	JCheckBox localOnly;
	Font font = new Font("Arial", Font.BOLD, 32);

	// Constructor setting UI
	public HostRoomDialog(MainFrame parent) {
		super(parent, true);

		setLayout(new BorderLayout());
		setTitle("Creating room");
		setSize(500, 180);
		setLocation(parent.getLocation().x + (parent.getWidth() - getWidth()) / 2, parent.getLocation().y + (parent.getHeight() - getHeight()) / 2);

		JLabel l1 = new JLabel("Room name : ");
		l1.setFont(font);
		l1.setHorizontalAlignment(JLabel.CENTER);
		JPanel p1 = new JPanel();
		p1.add(l1);
		p1.add(roomNameField);

		JLabel l2 = new JLabel("Password : ");
		l2.setFont(font);
		l2.setHorizontalAlignment(JLabel.CENTER);
		JPanel p2 = new JPanel();
		p2.add(l2);
		p2.add(passwordField);
		localOnly = new JCheckBox("LAN Only");
		localOnly.setFont(new Font("Arial", Font.PLAIN, 20));
		p2.add(localOnly);

		JButton button = new JButton("Create!");
		button.addActionListener(new ButtonListener(parent));

		add(p1, BorderLayout.NORTH);
		add(p2, BorderLayout.CENTER);
		add(button, BorderLayout.SOUTH);
		setVisible(true);
	}

	class ButtonListener implements ActionListener {
		MainFrame parent;

		public ButtonListener(MainFrame parent) {
			this.parent = parent;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String roomName = roomNameField.getText();
			String password = passwordField.getText();

			if (roomName.equals("")) {
				JOptionPane.showMessageDialog(parent, "Input name!!");
				return;
			}

			RoomInfo roomInfo = new RoomInfo(roomName, password);
			boolean isLocal = localOnly.isSelected();

			try {
				GatewayDevice device = null;
				GatewayDiscover discover = null;
				LANListener lanListener = null;

				ServerSocket listener = new ServerSocket(0);
				int port = listener.getLocalPort();
				roomInfo.port = port;

				/*
				 * If user created LAN room; start UDP listening server that
				 * listen for local user request. In online mode, lobby server
				 * do this, but in LAN mode, there is no lobby server so host
				 * have to listen
				 */
				if (isLocal) {
					roomInfo.IPAdress = InetAddress.getLocalHost().getHostAddress();
					lanListener = new LANListener(roomInfo);
					lanListener.start();
				}
				else {
					// If user created online room; try to port mapping by UPNP
					LobbyServerInfo.init();
					socket = new Socket();
					socket.connect(new InetSocketAddress(LobbyServerInfo.IPAddress, LobbyServerInfo.hostPort), 1500);
					BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

					oos.writeObject(roomInfo);

					String line = reader.readLine();
					if (line != null) {

						// Online mode also have to check for duplicated name
						if (line.equals("EXIST")) {
							JOptionPane.showMessageDialog(parent, "Already exist name!", "Name exist!", JOptionPane.ERROR_MESSAGE);
							listener.close();
							return;
						}
						else {
							String[] externalAddr = line.split("/");
							System.out.println("External : " + externalAddr[0] + ":" + externalAddr[1]);

							// Setup UPNP
							discover = new GatewayDiscover();
							discover.discover();
							device = discover.getValidGateway();

							// We have to check for already mapped?
							//PortMappingEntry portMapping = new PortMappingEntry();

							if (device == null) {
								System.out.println("Cannot find gateway router!");
								MainFrame.instance.setTitle("Dionysos [UPNP FAILED]");
							}
							else if (!device.addPortMapping(port, port, device.getLocalAddress().getHostAddress(), "TCP", "Dionysos!")) {
								System.out.println("Mapping failed!");
								listener.close();
								return;
							}
							else {
								System.out.println("Mapping " + port);
							}
						}
					}
				}

				new Server(device, listener, socket, lanListener).start();
				parent.changePanel("Room");
				dispose();

				// Host connect via local address (faster)
				roomInfo.IPAdress = InetAddress.getLocalHost().getHostAddress();
				new Client(roomInfo).start();
			} catch (Exception e1) {
				dispose();
				JOptionPane.showMessageDialog(MainFrame.instance, "Cannot connect to lobby server!", "Connection error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
