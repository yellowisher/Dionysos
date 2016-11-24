package MainScreen;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import LobbyClient.ClientRoomDialog;
import LobbyClient.HostRoomDialog;

public class TitlePanel extends JPanel {

	MainFrame frame;
	JCheckBox lanCheckBox = new JCheckBox("LAN");

	TitlePanel(int frameWidth, int frameHeight, MainFrame frame) {
		this.setLayout(null);
		this.frame = frame;

		// Show title lyra image
		ImageIcon titleImageIcon = new ImageIcon("Resource/Image/Lyra.png");
		JLabel titleImage = new JLabel(titleImageIcon);
		int imageWidth = titleImageIcon.getIconWidth();
		int imageHeight = titleImageIcon.getIconHeight();
		titleImage.setBounds((frameWidth - imageWidth) / 2, 10, imageWidth, imageHeight);
		add(titleImage);
		setBackground(new Color(255, 200, 200));

		// Panel for join room
		JPanel joinRoom = new JPanel();
		joinRoom.setBounds(0, imageHeight + 90, frameWidth / 2, frameHeight - imageHeight - 90);
		joinRoom.setBorder(new LineBorder(new Color(247, 145, 0), 4));
		joinRoom.setLayout(new GridBagLayout());
		joinRoom.setBackground(new Color(255, 200, 200));

		add(joinRoom);

		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.BOTH;

		// Join room button
		JButton joinRoomBtn = new JButton("Join Room");
		joinRoomBtn.setFont(new Font("Arial", Font.PLAIN, 48));
		joinRoomBtn.setMargin(new Insets(20, 20, 20, 20));
		joinRoomBtn.setFocusPainted(false);
		gc.gridx = 1;
		gc.gridy = 1;
		joinRoom.add(joinRoomBtn, gc);

		JPanel textPanel = new JPanel();
		gc.gridx = 1;
		gc.gridy = 2;
		joinRoom.add(textPanel, gc);

		// Join room text
		JLabel joinRoomText = new JLabel("Jump into friend's room!");
		joinRoomText.setFont(joinRoomText.getFont().deriveFont(16f));
		joinRoomText.setHorizontalAlignment(SwingConstants.CENTER);

		textPanel.setBackground(new Color(255, 200, 200));
		textPanel.add(joinRoomText);
		textPanel.add(lanCheckBox);

		// Panel for create room
		JPanel createRoom = new JPanel();
		createRoom.setBounds(frameWidth / 2, imageHeight + 90, frameWidth / 2, frameHeight - imageHeight - 90);
		createRoom.setBorder(new LineBorder(new Color(247, 145, 0), 4));
		createRoom.setLayout(new GridBagLayout());
		createRoom.setBackground(new Color(255, 200, 200));
		add(createRoom);

		// Create room button
		JButton createRoomBtn = new JButton("Create Room");
		createRoomBtn.setFont(new Font("Arial", Font.PLAIN, 48));
		createRoomBtn.setMargin(new Insets(20, 20, 20, 20));
		createRoomBtn.setFocusPainted(false);
		gc.gridx = 1;
		gc.gridy = 1;
		createRoom.add(createRoomBtn, gc);

		// Create room text
		JLabel createRoomText = new JLabel("...or Create your own room!");
		createRoomText.setFont(createRoomText.getFont().deriveFont(16f));
		createRoomText.setHorizontalAlignment(SwingConstants.CENTER);
		gc.gridx = 1;
		gc.gridy = 2;
		createRoom.add(createRoomText, gc);

		ButtonListener btnListener = new ButtonListener();
		joinRoomBtn.addActionListener(btnListener);
		createRoomBtn.addActionListener(btnListener);
	}

	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String type = e.getActionCommand();

			if (type.equals("Join Room")) {
				new ClientRoomDialog(frame, lanCheckBox.isSelected());
				// TODO : Try connect here
			}
			else if (type.equals("Create Room")) {
				new HostRoomDialog(frame);
				// TODO : Create room here
			}
		}
	}
}
