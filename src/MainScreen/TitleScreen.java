package MainScreen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
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

public class TitleScreen{
	static final int WIDTH = 800;
	static final int HEIGHT = 600;
	
	JFrame mainFrame = new JFrame("Dionysos");

	TitleScreen() {
		mainFrame.getContentPane().setPreferredSize(new Dimension(WIDTH, HEIGHT));
		mainFrame.setLayout(null);

		// Set frame location to center of screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		mainFrame.setLocation((screenSize.width - WIDTH) / 2, (screenSize.height - HEIGHT) / 2);

		// Show title lyra image
		ImageIcon titleImageIcon = new ImageIcon("Image/Lyra.png");
		JLabel titleImage = new JLabel(titleImageIcon);
		int imageWidth = titleImageIcon.getIconWidth();
		int imageHeight = titleImageIcon.getIconHeight();
		titleImage.setBounds((WIDTH - imageWidth) / 2, 10, imageWidth, imageHeight);
		mainFrame.add(titleImage);

		// Panel for join room
		JPanel joinRoom = new JPanel();
		joinRoom.setBounds(0, imageHeight + 10, WIDTH / 2, HEIGHT - imageHeight - 10);
		joinRoom.setBorder(new LineBorder(new Color(247, 145, 0), 2));
		joinRoom.setLayout(new GridBagLayout());
		mainFrame.add(joinRoom);

		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.BOTH;

		// Join room button
		JButton joinRoomBtn = new JButton("Join Room");
		joinRoomBtn.setFont(new Font("Arial", Font.PLAIN, 48));
		joinRoomBtn.setMargin(new Insets(20, 20, 20, 20));
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
		createRoom.setBounds(WIDTH / 2, imageHeight + 10, WIDTH / 2, HEIGHT - imageHeight - 10);
		createRoom.setBorder(new LineBorder(new Color(247, 145, 0), 2));
		createRoom.setLayout(new GridBagLayout());
		mainFrame.add(createRoom);

		// Create room button
		JButton createRoomBtn = new JButton("Create Room");
		createRoomBtn.setFont(new Font("Arial", Font.PLAIN, 48));
		createRoomBtn.setMargin(new Insets(20, 20, 20, 20));
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
		
		mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mainFrame.setVisible(true);
		mainFrame.pack();
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
        return JOptionPane.showInputDialog(
            mainFrame,
            "Enter IP address of room:",
            "Joining room",
            JOptionPane.PLAIN_MESSAGE);
    }

	public static void main(String[] args) {
		TitleScreen titleScreen = new TitleScreen();
	}
}
