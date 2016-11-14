package RoomScreen.Connection;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import MainScreen.MainFrame;

public class ConnDialog extends JDialog {
	public ConnDialog(MainFrame frame) {
		super(frame, "Connecting", false);

		JLabel label = new JLabel("Connecting to host...");
		label.setFont(new Font("Arial", Font.BOLD, 36));
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);

		getContentPane().setBackground(new Color(127, 55, 43));
		getRootPane().setBorder(new LineBorder(new Color(43, 127, 93), 10));

		add(label);
		setSize(500, 150);
		setUndecorated(true);
		setResizable(false);
		setLocation(frame.getLocation().x + (frame.getWidth() - getWidth()) / 2, frame.getLocation().y + (frame.getHeight() - getHeight()) / 2);
		setVisible(true);
	}
}
