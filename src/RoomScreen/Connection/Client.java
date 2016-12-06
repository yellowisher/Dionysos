package RoomScreen.Connection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Instrument.VirtualDrum.VirtualDrum;
import Instrument.VirtualGuitar.VirtualGuitar;
import Instrument.VirtualPiano.VirtualPiano;
import MainScreen.MainFrame;
import RoomInfo.RoomInfo;
import RoomScreen.Layout.Choice;
import RoomScreen.Layout.RoomPanel;
import RoomScreen.Manager.PlayManager;

public class Client extends Thread {
	private static final int STATE_NOTHING = -1;
	private static final int STATE_PIANO = 0;
	private static final int STATE_GUITAR = 1;
	private static final int STATE_DRUM = 2;
	int currentState;

	RoomInfo info;
	BufferedReader in = null;
	PrintWriter out;
	RoomPanel frame;
	JTextField textField;
	JTextPane messageArea;
	static Client instance;
	Socket socket;
	boolean isExit = false;

	JList<String> userList;
	public DefaultListModel<String> listModel = new DefaultListModel<String>();
	String name = null;
	PlayManager pm = new PlayManager();

	JButton pianoBtn, guitarBtn, drumBtn;
	ConnDialog dialog;

	public Client(RoomInfo info) {
		currentState = STATE_NOTHING;
		instance = this;
		frame = RoomPanel.instance;
		this.info = info;
		getChoiceContent();
		initChoiceContent();
		textField = frame.gettxtField();
		messageArea = frame.getMsgArea();
		userList = frame.getUserList();
		// Layout GUI
		textField.setEditable(false);
		messageArea.setEditable(false);
		RoomPanel.instance.client = this;

		frame.getJpInstru().removeAll();
		// Add Listeners
		textField.addActionListener(new ActionListener() {
			/*
			 * Responds to pressing the enter key in the textfield by sending
			 * the contents of the text field to the server. Then clear the text
			 * area in preparation for the next message.
			 */
			public void actionPerformed(ActionEvent e) {
				String msg = textField.getText();
				String[] split = msg.split(" ");
				out.println("MSG" + msg);
				textField.setText("");
			}
		});
	}

	public void leftRoom() {
		isExit = true;
		try {
			socket.close();
			System.out.println("Closed!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		MainFrame.instance.changePanel("title");
	}

	private void getChoiceContent() {
		JPanel j = frame.getJpChoice();
		Choice cho = (Choice) j.getComponent(0);
		pianoBtn = cho.getPianoBtn();
		guitarBtn = cho.getGuitarBtn();
		drumBtn = cho.getDrumBtn();
	}

	public void sendMessage(String msg) {
		System.out.println("Send : " + msg);
		out.println(msg);
	}

	public String getUserName() {
		return name;
	}

	private LineBorder selected = (LineBorder) BorderFactory.createLineBorder(new java.awt.Color(51, 255, 51), 3);
	private EmptyBorder notSelected = (EmptyBorder) BorderFactory.createEmptyBorder(3, 3, 3, 3);

	private void initChoiceContent() {
		pianoBtn.setBorder(notSelected);
		guitarBtn.setBorder(notSelected);
		drumBtn.setBorder(notSelected);
		frame.repaint();

		pianoBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				changeInst(STATE_PIANO);
			}
		});
		guitarBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				changeInst(STATE_GUITAR);
			}
		});
		drumBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				changeInst(STATE_DRUM);
			}
		});
	}

	private void changeInst(int newState) {
		if (currentState == newState) return;
		currentState = newState;

		guitarBtn.setBorder(notSelected);
		pianoBtn.setBorder(notSelected);
		drumBtn.setBorder(notSelected);
		JPanel inst = null;

		switch (currentState) {
			case STATE_PIANO :
				pianoBtn.setBorder(selected);
				out.println("CHOICE Piano");
				inst = new VirtualPiano(instance);
				break;

			case STATE_GUITAR :
				guitarBtn.setBorder(selected);
				out.println("CHOICE Guitar");
				inst = new VirtualGuitar(instance);
				break;

			case STATE_DRUM :
				drumBtn.setBorder(selected);
				out.println("CHOICE Drum");
				inst = new VirtualDrum(instance);
				break;
		}

		JPanel jp = (JPanel) frame.getJpInstru();
		jp.removeAll();
		jp.add(inst);
		inst.requestFocus();
		frame.setFocusDest(inst);
		frame.repaint();
	}

	private String getNick() {
		return JOptionPane.showInputDialog(frame, "Choose a screen name:", "Screen name selection", JOptionPane.PLAIN_MESSAGE);
	}

	public void run() {
		try {
			// Make connection and initialize streams
			System.out.println("Client : Client try to connect to " + info.IPAdress + ":" + info.port);

			dialog = new ConnDialog(MainFrame.instance);
			socket = null;
			if (info.holePunch) {
				TCPHolePuncher puncher = new TCPHolePuncher(TCPHolePuncher.TYPE_CLIENT, info.roomName);
				// Wait for TCPHolePuncher finishes connect
				puncher.start();
				puncher.join();

				socket = puncher.connectedSocket;
			}
			if (socket == null || !info.holePunch) socket = new Socket(info.IPAdress, info.port);
			dialog.dispose();

			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);

			// Process all messages from server, according to the protocol.
			while (true) {
				String line = in.readLine();

				if (line.startsWith("FULL")) {
					socket.close();
					JOptionPane.showMessageDialog(MainFrame.instance, "Room is full!", "Cannot join", JOptionPane.ERROR_MESSAGE);
					MainFrame.instance.changePanel("Title");
					return;
				}
				else if (line.startsWith("ENTER")) {
					out.println("OK");
					break;
				}
			}

			while (true) {
				String line = in.readLine();
				if (line.startsWith("SUBMITNAME")) {

					while (true) {
						name = getNick(); // id input msg.
						if (name == null || name.equals("")) continue;
						break;
					}
					out.println(name);

				}
				else if (line.startsWith("NAMEACCEPTED")) {
					textField.setEditable(true);

				}
				else if (line.startsWith("BROADCAST")) {
					//server broadcasts room information.
					frame.appendStr(line.substring(19) + "\n", "BLUE");
				}

				else if (line.startsWith("MESSAGE")) {
					frame.appendStr(line.substring(7) + "\n", null);
				}
				else if (line.startsWith("USER_DEL_ALL")) {
					listModel.removeAllElements();
					userList.setModel(listModel);
				}
				else if (line.startsWith("USER_ADD")) {
					listModel.addElement(line.substring(9));
					userList.setModel(listModel);
				}
				else {

					String[] inputs = line.split("/");
					for (String input : inputs) {
						if (frame.isRecording) frame.recorder.record(input);
						String note = input.substring(3, 6);

						switch (line.charAt(0)) {
							case 'P' :
								if (line.charAt(1) == 'D') pm.play("Piano", note);
								break;
							case 'G' :
								if (line.charAt(1) == 'H') pm.play("Guitar", note);
								break;
							case 'D' :
								if (line.charAt(1) == 'H') pm.play("Drum", note);
								break;
						}
					}
				}
			}

		} catch (Exception e) {
			MainFrame.instance.changePanel("Title");
			if (isExit) return;
			e.printStackTrace();
			if (name == null) {
				dialog.dispose();
				JOptionPane.showMessageDialog(frame, "Cannot connect to host!", "Connection failed", JOptionPane.ERROR_MESSAGE);
			}
			else {
				JOptionPane.showMessageDialog(frame, "Diconnected from host", "Disconnected", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}