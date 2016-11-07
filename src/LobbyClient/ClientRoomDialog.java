package LobbyClient;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import RoomInfo.RoomInfo;

public class ClientRoomDialog extends JDialog {
	static InetAddress sAddress;
	static final int sPort = 9322;

	DefaultListModel<RoomInfo> listModel;

	public ClientRoomDialog(JFrame parent) {
		super(parent, true);

		try {
			sAddress = InetAddress.getByName("localhost");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		setSize(500, 400);
		setLocation(parent.getLocation().x + (parent.getWidth() - getWidth()) / 2, parent.getLocation().y + (parent.getHeight() - getHeight()) / 2);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		add(panel);

		listModel = new DefaultListModel<>();
		JList<RoomInfo> roomList = new JList<>(listModel);
		roomList.setCellRenderer(new ClientRoomRenderer());
		panel.add(new JScrollPane(roomList));

		JButton refreshBtn = new JButton("Refresh");
		refreshBtn.addActionListener(new ButtonListener());
		panel.add(refreshBtn, BorderLayout.SOUTH);

		if (refresh()) setVisible(true);
	}

	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (refresh()) invalidate();
		}
	}

	public boolean refresh() {
		listModel.clear();
		HashMap<String, RoomInfo> map = null;
		byte[] data = new byte[6400];
		byte[] send = new byte[4];

		send[0] = 'G';
		DatagramSocket socket = null;

		try {
			socket = new DatagramSocket();
			DatagramPacket request = new DatagramPacket(send, send.length, sAddress, sPort);
			DatagramPacket receivePacket = new DatagramPacket(data, data.length);
			socket.send(request);
			socket.setSoTimeout(1500);

			while (true) {
				socket.receive(receivePacket);

				if (receivePacket != null) {
					ByteArrayInputStream baos = new ByteArrayInputStream(data);
					ObjectInputStream oos = new ObjectInputStream(baos);

					map = (HashMap<String, RoomInfo>) oos.readObject();

					if (map.isEmpty()) {
						// no room
					}
					else {
						for (RoomInfo roomInfo : map.values()) {
							listModel.addElement(roomInfo);
						}
					}
					return true;
				}
			}
		} catch (SocketTimeoutException e) {
			JOptionPane.showMessageDialog(
					this, 
					"Cannot connect to lobby server", 
					"Lobby server error", 
					JOptionPane.ERROR_MESSAGE);
			socket.close();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		//setVisible(true);
	}
}
