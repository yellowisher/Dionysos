package RoomScreen.Layout;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.text.Style;
import javax.swing.GroupLayout;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;
import javax.swing.text.StyledDocument;

import RoomScreen.Connection.Client;
import RoomScreen.Connection.Server;
import WAVMaker.WAVMaker;

import Recorder.Recorder;

import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 *
 * @author DevSim
 */
public class RoomPanel extends JPanel {

	//private javax.swing.JTextArea msgArea;
	public JTextPane msgArea;

	public CardLayout instHolder;
	private JPanel focusDest;
	public StyledDocument document;

	public Client client;
	public Server server;

	public boolean isRecording;
	public Recorder recorder;
	private JFileChooser fileChooser;
	private Font buttonFont = new Font("Arial", Font.PLAIN, 20);
	private int seconds = 0;
	private Timer timer;

	public static RoomPanel instance;
	/*
	 * Creates new form Main2
	 */
	public RoomPanel() {
		instance = this;

		msgArea = new JTextPane();
		document = (StyledDocument) msgArea.getDocument();

		Style init = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		Style aStyle = document.addStyle("BLUE", init);
		StyleConstants.setForeground(aStyle, Color.blue);

		look_feel();
		initComponents();
		addPanel();

		isRecording = false;
		fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter("wav", "wav"));
		fileChooser.setMultiSelectionEnabled(false);

		jpInstru.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				if (focusDest != null) focusDest.requestFocus();
			}
		});
	}
	public void clearAll() {
		if (client != null) {
			client.listModel.removeAllElements();
			userList.setModel(client.listModel);
		}
		msgArea.setText("");
	}

	public void setFocusDest(JPanel p) {
		focusDest = p;
	}

	public void setInstFocus() {
		if (focusDest != null) focusDest.requestFocus();
	}

	// Because JTextPane has no append() method, home made version of append()
	public void appendStr(String str, String type) throws BadLocationException {
		document = (StyledDocument) msgArea.getDocument();
		if (type != null) document.insertString(document.getLength(), str, document.getStyle("BLUE"));
		else document.insertString(document.getLength(), str, null);
	}

	/*
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	private void initComponents() {
		jpLogo = new javax.swing.JPanel();
		jPanel2 = new javax.swing.JPanel();
		recordBtn = new javax.swing.JButton();
		time = new javax.swing.JLabel();
		exitBtn = new javax.swing.JButton();
		jpInstru = new javax.swing.JPanel();
		jPanel4 = new javax.swing.JPanel();
		jScrollPane2 = new javax.swing.JScrollPane();
		txtField = new javax.swing.JTextField();
		sendBtn = new javax.swing.JButton();
		jScrollPane1 = new javax.swing.JScrollPane();
		userList = new javax.swing.JList<>();
		jpChoice = new javax.swing.JPanel();

		setSize(new Dimension(920, 770));

		jpLogo.setBackground(new java.awt.Color(255, 255, 204));

		javax.swing.GroupLayout jpLogoLayout = new javax.swing.GroupLayout(jpLogo);
		jpLogo.setLayout(jpLogoLayout);
		jpLogoLayout.setHorizontalGroup(jpLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 214, Short.MAX_VALUE));
		jpLogoLayout.setVerticalGroup(jpLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 190, Short.MAX_VALUE));

		jPanel2.setBackground(new java.awt.Color(204, 255, 204));
		jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Function"));
		jPanel2.setName(""); // NOI18N

		recordBtn.setFont(buttonFont);
		exitBtn.setFont(buttonFont);
		recordBtn.setText("Record");
		recordBtn.setName(""); // NOI18N
		exitBtn.setText("Exit");
		time.setText("Duration : 0:00");
		time.setFont(new Font("Arial", Font.PLAIN, 20));

		exitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				exitBtnActionPerformed(evt);
			}
		});

		recordBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				recordBtnActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout
				.createSequentialGroup().addContainerGap()
				.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(jPanel2Layout.createSequentialGroup().addComponent(recordBtn, GroupLayout.PREFERRED_SIZE, 220, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(exitBtn,
										javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addComponent(time))
				.addContainerGap(71, Short.MAX_VALUE)));
		jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel2Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addComponent(exitBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
								.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(recordBtn,
										javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(time).addContainerGap()));

		jpInstru.setBackground(new java.awt.Color(255, 204, 255));

		javax.swing.GroupLayout jpInstruLayout = new javax.swing.GroupLayout(jpInstru);
		jpInstru.setLayout(jpInstruLayout);

		jpInstruLayout.setHorizontalGroup(jpInstruLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 0, Short.MAX_VALUE));
		jpInstruLayout.setVerticalGroup(jpInstruLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 400, Short.MAX_VALUE));

		jPanel4.setBackground(new java.awt.Color(204, 204, 255));
		jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chatting"));

		msgArea.setEditable(false);

		DefaultCaret caret = (DefaultCaret) msgArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		jScrollPane2.setViewportView(msgArea);
		((DefaultCaret) msgArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		sendBtn.setText("Send");
		jScrollPane1.setViewportView(userList);

		//TODO: UI BUG FIX
		javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
		jPanel4.setLayout(jPanel4Layout);

		jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
				.addGroup(jPanel4Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane2, 250, 250, 250)
								.addGroup(jPanel4Layout.createSequentialGroup().addComponent(txtField, 188, 188, 188)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(sendBtn, 61, 61, 61).addGap(0, 0, 0))
								.addComponent(jScrollPane1, 250, 250, 250))
						.addContainerGap()));

		jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
				.addGroup(jPanel4Layout.createSequentialGroup().addComponent(jScrollPane1, 78, 78, 78)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane2, 280, 280, 280)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(txtField,
								javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(sendBtn))
						.addContainerGap()));

		jpChoice.setBackground(new java.awt.Color(255, 153, 102));
		jpChoice.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Instrument"));

		javax.swing.GroupLayout jpChoiceLayout = new javax.swing.GroupLayout(jpChoice);
		jpChoice.setLayout(jpChoiceLayout);
		jpChoiceLayout.setHorizontalGroup(jpChoiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 0, Short.MAX_VALUE));
		jpChoiceLayout.setVerticalGroup(jpChoiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 130, Short.MAX_VALUE));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(layout.createSequentialGroup()
										.addComponent(jpLogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, 399, GroupLayout.PREFERRED_SIZE))
										.addComponent(jpChoice, GroupLayout.PREFERRED_SIZE, 619, GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel4,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addComponent(jpInstru, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(layout
								.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
										.addComponent(jpLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jpChoice,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jpInstru, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
	}

	class DurationChanger extends TimerTask {
		@Override
		public void run() {
			int min = (++seconds) / 60;
			int sec = seconds % 60;
			time.setText("Duration : " + min + ":" + String.format("%02d", sec));
		}
	}

	private void addPanel() {
		//Logo
		Logo logoPanel = new Logo(jpLogo);
		jpLogo.setLayout(new BorderLayout());
		jpLogo.add(logoPanel);

		//Chocie
		Choice choicePanel = new Choice(jpChoice);
		jpChoice.setLayout(new BorderLayout());
		jpChoice.add(choicePanel);
	}

	private void exitBtnActionPerformed(ActionEvent evt) {
		if (server != null) server.closeServer();
		else client.leftRoom();
	}

	private void recordBtnActionPerformed(ActionEvent evt) {
		if (isRecording) {
			// Finish recording
			isRecording = false;
			recordBtn.setText("Record");
			recorder.stop();

			timer.cancel();
			seconds = 0;
			time.setText("Duration : 0:00");

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.KOREA);
			Date date = new Date();
			recorder.saveAs("History/" + formatter.format(date) + ".txt");

			try {
				WAVMaker maker = new WAVMaker(new FileInputStream("History/" + formatter.format(date) + ".txt"));
				maker.createWAV();
				if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
					maker.save(fileChooser.getSelectedFile().toString() + ".wav");
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			recorder = null;
		}
		else {
			// Start recording
			isRecording = true;
			recordBtn.setText("[STOP]");

			recorder = new Recorder();
			timer = new Timer();
			timer.schedule(new DurationChanger(), 1000, 1000);
		}
		setInstFocus();
	}
	private void look_feel() {
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(RoomPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(RoomPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(RoomPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(RoomPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
	}

	public JTextPane getMsgArea() {
		return msgArea;
	}
	public JTextField gettxtField() {
		return txtField;
	}
	public JList<String> getUserList() {
		return userList;
	}
	public JPanel getJpChoice() {
		return jpChoice;
	}
	public JPanel getJpInstru() {
		return jpInstru;
	}
	public void changeInst(String inst) {
		instHolder.show(jpInstru, inst);
	}

	// Variables declaration - do not modify                     
	public javax.swing.JButton exitBtn;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel4;
	public javax.swing.JScrollPane jScrollPane1;
	public javax.swing.JScrollPane jScrollPane2;

	private javax.swing.JPanel jpChoice;
	private javax.swing.JPanel jpInstru;
	private javax.swing.JPanel jpLogo;

	public javax.swing.JButton recordBtn;
	private javax.swing.JButton sendBtn;
	private javax.swing.JLabel time;
	public javax.swing.JTextField txtField;
	public javax.swing.JList<String> userList;
}
