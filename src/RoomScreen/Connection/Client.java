package RoomScreen.Connection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import Instrument.VirtualDrum.VirtualDrum;
import Instrument.VirtualPiano.VirtualPiano;
import MainScreen.MainFrame;
import RoomScreen.Layout.*;
import RoomScreen.Manager.*;

public class Client extends Thread {
	private static final int STATE_NOTHING = 0;
	private static final int STATE_PIANO = 1;
	private static final int STATE_GUITAR = 2;
	private static final int STATE_DRUM = 3;
	int currentState;

	BufferedReader in;
	PrintWriter out;
	Main frame;
	ConnectInfo info;
	JTextField textField;

	JTextPane messageArea;

	JList<String> userList;
	DefaultListModel<String> listModel = new DefaultListModel<String>();
	String name = null;
	PlayManager pm = new PlayManager();

	JButton pianoBtn, guitarBtn, drumBtn;

	public Client(Main f, ConnectInfo info) {
		currentState = STATE_NOTHING;
		this.frame = f;
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
			/**
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
		//Recording
		out.println(msg);
	}

	public String getUserName() {
		return name;
	}

	private void initChoiceContent() {
		pianoBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (currentState == STATE_PIANO) return;
				currentState = STATE_PIANO;

				pianoBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 255, 51), 3));
				guitarBtn.setBorder(null);
				drumBtn.setBorder(null);
				out.println("CHOICE Piano");

				JPanel jp = (JPanel) frame.getJpInstru();
				jp.removeAll();
				VirtualPiano vpPanel = new VirtualPiano(jp, frame);
				jp.add(vpPanel);
				vpPanel.requestFocus();
				frame.setFocusDest(vpPanel);
				frame.repaint();
			}
		});
		guitarBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (currentState == STATE_GUITAR) return;
				currentState = STATE_GUITAR;

				guitarBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 255, 51), 3));
				pianoBtn.setBorder(null);
				drumBtn.setBorder(null);
				out.println("CHOICE Guitar");

				JPanel jp = (JPanel) frame.getJpInstru();
				//jp.removeAll();

				frame.repaint();
			}
		});
		drumBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (currentState == STATE_DRUM) return;
				currentState = STATE_DRUM;

				drumBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 255, 51), 3));
				pianoBtn.setBorder(null);
				guitarBtn.setBorder(null);
				out.println("CHOICE Drum");

				JPanel jp = (JPanel) frame.getJpInstru();
				jp.removeAll();
				VirtualDrum vdPanel = new VirtualDrum(jp, frame);
				jp.add(vdPanel);
				vdPanel.requestFocus();
				frame.setFocusDest(vdPanel);
				frame.repaint();
			}
		});
	}

	/**
	 * Prompt for and return the desired screen name.
	 */
	private String getNick() {
		System.out.print("CALLED");
		return JOptionPane.showInputDialog(frame, "Choose a screen name:", "Screen name selection", JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * Connects to the server then enters the processing loop.
	 */

	public void run() {
		try {
			System.out.println("Client Running");

			// Make connection and initialize streams
			System.out.println("Client try to connect to " + info.getIp() + ":" + info.getPort());
			Socket socket = new Socket(info.getIp(), info.getPort());

			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);

			// Process all messages from server, according to the protocol.

			while (true) {
				String line = in.readLine();
				System.out.println("GET " + line);

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
					name = getNick(); // id input msg.
					if (name == null || name.equals("")) System.exit(0);
					out.println(name);
				}
				else if (line.startsWith("NAMEACCEPTED")) {
					textField.setEditable(true);

				}
				else if (line.startsWith("BROADCAST")) {
					//server broadcasts room information.
					frame.appendStr(line.substring(18) + "\n", "BLUE");

				}

				else if (line.startsWith("MESSAGE")) {
					frame.appendStr(line.substring(8) + "\n", null);
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
					String note = line.substring(3, 6);

					switch (line.charAt(0)) {
						case 'P' :
							//if recording
							if(line.charAt(1)=='D')pm.play("Piano", note);
							break;
						case 'G' :
						case 'D' :
							
							if(line.charAt(1)=='H')pm.play("Drum", note);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			MainFrame.instance.changePanel("Title");
		}
	}
}