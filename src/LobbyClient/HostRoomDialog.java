package LobbyClient;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.naming.ldap.ExtendedRequest;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import MainScreen.MainFrame;
import RoomInfo.RoomInfo;
import RoomScreen.Connection.ConnectInfo;
import RoomScreen.Layout.Main;

import org.bitlet.weupnp.GatewayDevice;
import org.bitlet.weupnp.GatewayDiscover;
import org.bitlet.weupnp.PortMappingEntry;

public class HostRoomDialog extends JDialog {
	Socket socket;
	JTextField roomNameField = new JTextField(20);
	JTextField passwordField = new JTextField(10);
	JCheckBox localOnly;
	Font font = new Font("Arial", Font.BOLD, 32);

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
		localOnly = new JCheckBox("Local only");
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

			//			if(isLocal) {
			//				roomInfo.IPAdress=InetAddress.getLocalHost().getHostAddress(),
			//			}
			try {
				socket = new Socket(LobbyServerInfo.IPAddress, LobbyServerInfo.hostPort);

				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

				ServerSocket listener = new ServerSocket(0);
				int pt = listener.getLocalPort();

				roomInfo.port = pt;
				oos.writeObject(roomInfo);

				while (true) {
					String line = reader.readLine();
					if (line != null) {

						if (line.equals("EXIST")) {
							JOptionPane.showMessageDialog(parent, "Already exist name!", "Name exist!", JOptionPane.ERROR_MESSAGE);
							listener.close();
							return;
						}
						else {
							String[] externalAddr = line.split("/");
							System.out.println("External : " + externalAddr[0] + ":" + externalAddr[1]);

							ConnectInfo info = new ConnectInfo(externalAddr[0], roomInfo.port, ConnectInfo.CREATE);

							// Setup UPNP
							GatewayDiscover discover = new GatewayDiscover();
							discover.discover();
							GatewayDevice device = discover.getValidGateway();

							PortMappingEntry portMapping = new PortMappingEntry();
							if (!device.addPortMapping(pt, pt, device.getLocalAddress().getHostAddress(), "TCP", "Dionysos!")) {
								System.out.println("FAILED!");
								return;
							}

							parent.roomPanel.createRoom(info, device, listener, socket);
							
							parent.changePanel("Room");
							dispose();
							parent.roomPanel.joinRoom(info);
						}
						return;
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
}
