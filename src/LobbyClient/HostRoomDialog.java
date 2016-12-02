package LobbyClient;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import MainScreen.MainFrame;
import RoomInfo.RoomInfo;
import RoomScreen.Connection.Client;
import RoomScreen.Connection.Server;
import RoomScreen.Connection.TCPHolePuncher;

import org.bitlet.weupnp.GatewayDevice;
import org.bitlet.weupnp.GatewayDiscover;

public class HostRoomDialog extends JDialog {
	Socket socket;
	JTextField roomNameField = new JTextField(20);
	JTextField passwordField = new JTextField(14);
	Font font = new Font("Arial", Font.BOLD, 32);
	boolean isLocal;
	JRadioButton none, UPnP, punch;

	// Constructor setting UI
	public HostRoomDialog(MainFrame parent, boolean isLocal) {
		super(parent, true);
		this.isLocal = isLocal;

		setLayout(new BorderLayout());
		setTitle("Creating room");
		setSize(500, 190);
		setLocation(parent.getLocation().x + (parent.getWidth() - getWidth()) / 2, parent.getLocation().y + (parent.getHeight() - getHeight()) / 2);

		JLabel nameLabel = new JLabel("Room name : ");
		nameLabel.setFont(font);
		nameLabel.setHorizontalAlignment(JLabel.CENTER);

		JPanel namePanel = new JPanel();
		namePanel.add(nameLabel);
		namePanel.add(roomNameField);

		JLabel passLabel = new JLabel("Password : ");
		passLabel.setFont(font);
		passLabel.setHorizontalAlignment(JLabel.CENTER);

		JPanel passPanel = new JPanel();
		passPanel.add(passLabel);
		passPanel.add(passwordField);

		// If it is not LAN room, (when is online room), show several options
		// (None, UPnP, TCP hole punching)
		if (!isLocal) {
			JPanel optionPanel = new JPanel();
			optionPanel.setLayout(new GridLayout(3, 1));

			none = new JRadioButton("None");
			UPnP = new JRadioButton("UPnP");
			punch = new JRadioButton("Hole Punch");

			none.setToolTipText("No option: If you are using NAT, client might not join the room");
			UPnP.setToolTipText("Try UPnP: Try to port mapping by UPnP");
			punch.setToolTipText("TCP hole punching: nTry to hole punching");

			none.setSelected(true);

			ButtonGroup optionGroup = new ButtonGroup();
			optionGroup.add(none);
			optionGroup.add(UPnP);
			optionGroup.add(punch);

			optionPanel.add(none);
			optionPanel.add(UPnP);
			optionPanel.add(punch);

			passPanel.add(optionPanel);
		}

		JButton button = new JButton("Create!");
		button.addActionListener(new ButtonListener(parent));

		add(namePanel, BorderLayout.NORTH);
		add(passPanel, BorderLayout.CENTER);
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
				 * have to listen itself
				 */
				if (isLocal) {
					roomInfo.IPAdress = InetAddress.getLocalHost().getHostAddress();
					lanListener = new LANListener(roomInfo);
					lanListener.start();
				}
				else {
					// If user created online room; try to UPnP and/or hole punching
					boolean doUPnP = false, doPunch = false;
					if (UPnP.isSelected()) doUPnP = true;
					else if (punch.isSelected()) doPunch = true;

					System.out.println("UPNP: " + UPnP + " Punch " + punch);

					LobbyServerInfo.init();
					socket = new Socket();
					socket.connect(new InetSocketAddress(LobbyServerInfo.IPAddress, LobbyServerInfo.hostPort), 1500);
					BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

					if (doPunch) {
						roomInfo.holePunch = true;
						listener.close();
						listener = null;
					}
					oos.writeObject(roomInfo);

					String line = reader.readLine();
					if (line != null) {

						// Online mode also have to check for duplicated name
						if (line.equals("EXIST")) {
							JOptionPane.showMessageDialog(parent, "Already exist name!", "Name exist!", JOptionPane.ERROR_MESSAGE);
							listener.close();
							throw new AlreadyExistException();
						}
						else {
							String[] externalAddr = line.split("/");
							System.out.println("External : " + externalAddr[0] + ":" + externalAddr[1]);

							if (doUPnP) {
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
									MainFrame.instance.setTitle("Dionysos [UPNP FAILED]");
								}
								else {
									System.out.println("Mapping " + port);
									MainFrame.instance.setTitle("Dionysos [UPNP SUCCEED]");
								}
							}
						}
					}
				}

				new Server(device, listener, socket, lanListener, roomInfo.roomName).start();
				parent.changePanel("Room");
				dispose();

				Thread.sleep(1000);
				
				// Host connect to its room via local address (faster)
				roomInfo.IPAdress = InetAddress.getLocalHost().getHostAddress();
				new Client(roomInfo).start();

			} catch (Exception exception) {
				if (exception instanceof AlreadyExistException) {
					JOptionPane.showMessageDialog(MainFrame.instance, "Already exist name!", "Name error", JOptionPane.ERROR_MESSAGE);
				}
				else if (exception instanceof BindException) {
					JOptionPane.showMessageDialog(MainFrame.instance, "You already created LAN room!", "Binding error", JOptionPane.ERROR_MESSAGE);
				}
				else {
					JOptionPane.showMessageDialog(MainFrame.instance, "Cannot connect to lobby server!", "Connection error", JOptionPane.ERROR_MESSAGE);
				}
				dispose();
			}
		}
	}

	class AlreadyExistException extends Exception {
		public AlreadyExistException() {
			super();
		}
	}
}
