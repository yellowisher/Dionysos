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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class TitlePanel extends JPanel {

	TitlePanel(int frameWidth, int frameHeight) {
		this.setLayout(null);

		// Show title lyra image
		ImageIcon titleImageIcon = new ImageIcon("Resource/Image/Lyra.png");
		JLabel titleImage = new JLabel(titleImageIcon);
		int imageWidth = titleImageIcon.getIconWidth();
		int imageHeight = titleImageIcon.getIconHeight();
		titleImage.setBounds((frameWidth - imageWidth) / 2, 10, imageWidth, imageHeight);
		add(titleImage);

		// Panel for join room
		JPanel joinRoom = new JPanel();
		joinRoom.setBounds(0, imageHeight + 10, frameWidth / 2, frameHeight - imageHeight - 10);
		joinRoom.setBorder(new LineBorder(new Color(247, 145, 0), 2));
		joinRoom.setLayout(new GridBagLayout());
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

		// Join room text
		JLabel joinRoomText = new JLabel("Jump into friend's room!");
		joinRoomText.setFont(joinRoomText.getFont().deriveFont(16f));
		joinRoomText.setHorizontalAlignment(SwingConstants.CENTER);
		gc.gridx = 1;
		gc.gridy = 2;
		joinRoom.add(joinRoomText, gc);

		// Panel for create room
		JPanel createRoom = new JPanel();
		createRoom.setBounds(frameWidth / 2, imageHeight + 10, frameWidth / 2, frameHeight - imageHeight - 10);
		createRoom.setBorder(new LineBorder(new Color(247, 145, 0), 2));
		createRoom.setLayout(new GridBagLayout());
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
				String address = getAddress();
				// TODO : Try connect here
			}
			else if (type.equals("Create Room")) {
				// TODO : Create room here
			}
		}
	}

	private String getAddress() {
		return JOptionPane.showInputDialog(this, "Enter IP address of room:", "Joining room", JOptionPane.PLAIN_MESSAGE);
	}
}
