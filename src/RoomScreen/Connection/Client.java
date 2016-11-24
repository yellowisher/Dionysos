package RoomScreen.Connection;

import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.Action;
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
import RoomScreen.Layout.*;
import RoomScreen.Manager.*;

public class Client extends Thread {
	private static final int STATE_NOTHING = 0;
	private static final int STATE_PIANO = 1;
	private static final int STATE_GUITAR = 2;
	private static final int STATE_DRUM = 3;
	int currentState;

	RoomInfo info;
	BufferedReader in = null;
	PrintWriter out;
	RoomPanel frame;
	JTextField textField;
	JTextPane messageArea;
	Client instance;

	JList<String> userList;
	DefaultListModel<String> listModel = new DefaultListModel<String>();
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

	private void getChoiceContent() {
		JPanel j = frame.getJpChoice();
		Choice cho = (Choice) j.getComponent(0);
		pianoBtn = cho.getPianoBtn();
		guitarBtn = cho.getGuitarBtn();
		drumBtn = cho.getDrumBtn();
	}

	public void sendMessage(String msg) {
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
				if (currentState == STATE_PIANO) return;
				currentState = STATE_PIANO;

				pianoBtn.setBorder(selected);
				guitarBtn.setBorder(notSelected);
				drumBtn.setBorder(notSelected);
				out.println("CHOICE Piano");

				JPanel jp = (JPanel) frame.getJpInstru();
				jp.removeAll();
				VirtualPiano vpPanel = new VirtualPiano(instance);
				jp.add(vpPanel);
				vpPanel.requestFocus();
				frame.setFocusDest(vpPanel);
				frame.repaint();
			}
		});
		guitarBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (currentState == STATE_GUITAR) return;
				currentState = STATE_GUITAR;

				guitarBtn.setBorder(selected);
				pianoBtn.setBorder(notSelected);
				drumBtn.setBorder(notSelected);
				out.println("CHOICE Guitar");

				JPanel jp = (JPanel) frame.getJpInstru();
				jp.removeAll();
				VirtualGuitar vpPanel = new VirtualGuitar(instance);
				jp.add(vpPanel);
				vpPanel.requestFocus();
				frame.setFocusDest(vpPanel);
				frame.repaint();
			}
		});
		drumBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (currentState == STATE_DRUM) return;
				currentState = STATE_DRUM;

				drumBtn.setBorder(selected);
				pianoBtn.setBorder(notSelected);
				guitarBtn.setBorder(notSelected);
				out.println("CHOICE Drum");

				JPanel jp = (JPanel) frame.getJpInstru();
				jp.removeAll();
				VirtualDrum vdPanel = new VirtualDrum(instance);
				jp.add(vdPanel);
				vdPanel.requestFocus();
				frame.setFocusDest(vdPanel);
				frame.repaint();
			}
		});
	}

	private String getNick() {
		return JOptionPane.showInputDialog(frame, "Choose a screen name:", "Screen name selection", JOptionPane.PLAIN_MESSAGE);
	}

	public void run() {
		try {
			// Make connection and initialize streams
			System.out.println("Client : Client try to connect to " + info.IPAdress + ":" + info.port);

			dialog = new ConnDialog(MainFrame.instance);
			Socket socket = new Socket(info.IPAdress, info.port);
			dialog.dispose();
			//Socket socket = new Socket(info.getIp(), info.getPort());

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
					if (frame.isRecording) {
						frame.recorder.record(line);
					}
					String note = line.substring(3, 6);

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

		} catch (Exception e) {
			e.printStackTrace();
			MainFrame.instance.changePanel("Title");
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