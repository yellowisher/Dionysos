package LobbyClient;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import RoomInfo.RoomInfo;

public class HostDemo extends JFrame {

	static final String serverIP = "127.0.0.1";
	static final int serverPort = 9392;

	Socket socket;

	JTextField name, password;

	public HostDemo() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLayout(new GridLayout(3, 2));
		setSize(500, 400);

		JPanel p1 = new JPanel();
		p1.add(new JLabel("Room name"));
		name = new JTextField(20);
		p1.add(name);

		JPanel p2 = new JPanel();
		p2.add(new JLabel("Password"));
		password = new JTextField(20);
		p2.add(password);

		JButton button = new JButton("Create");
		button.addActionListener(new Listener(this));

		add(p1);
		add(p2);
		add(button);

		setVisible(true);
	}

	class Listener implements ActionListener {
		JFrame frame;
		public Listener(JFrame f) {
			frame = f;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String roomName = name.getText();

			if (roomName.equals("")) {
				JOptionPane.showMessageDialog(frame, "Input name!!");
				return;
			}

			String roomPassword = password.getText();
			RoomInfo roomInfo = new RoomInfo(roomName, roomPassword);

			try {
				socket = new Socket(serverIP, serverPort);
				System.out.println("Connected!");

				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

				oos.writeObject(roomInfo);
				System.out.println("Send!");

				while (true) {
					String line = reader.readLine();
					if (line != null) {

						System.out.println(line);
						if (line.equals("CREATED")) frame.setTitle("CREATED");
						else frame.setTitle("FAILED");
						break;
					}
				}

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		new HostDemo();
	}
}
