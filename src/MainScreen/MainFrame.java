package MainScreen;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainFrame extends JFrame {
	static final int WIDTH = 800;
	static final int HEIGHT = 600;

	CardLayout panelHolder = new CardLayout();

	MainFrame() {
		setTitle("Dionysos");
		getContentPane().setPreferredSize(new Dimension(WIDTH, HEIGHT));

		// Setting for CardLayout
		getContentPane().setLayout(panelHolder);
		getContentPane().add("Title", new TitlePanel(WIDTH, HEIGHT));
		getContentPane().add("Room", new RoomPanel(WIDTH, HEIGHT));

		// Set frame position to center of screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - WIDTH) / 2, (screenSize.height - HEIGHT) / 2);

		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
	}

	public void changePanel(String name) {
		panelHolder.show(getContentPane(), name);
	}

	public static void main(String[] args) {
		MainFrame mainFrame = new MainFrame();
		mainFrame.panelHolder.next(mainFrame.getContentPane());
		mainFrame.setVisible(true);
		mainFrame.changePanel("Piano");
	}

}
