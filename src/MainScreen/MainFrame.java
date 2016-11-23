package MainScreen;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

import LobbyClient.LobbyServerInfo;
import RoomScreen.Layout.RoomPanel;

public class MainFrame extends JFrame {
	static final int WIDTH = 920;
	static final int HEIGHT = 756;

	CardLayout panelHolder = new CardLayout();
	public RoomPanel roomPanel = new RoomPanel();
	
	public static MainFrame instance;

	MainFrame() {
		instance = this;
		setTitle("Dionysos");
		getContentPane().setPreferredSize(new Dimension(WIDTH, HEIGHT));

		// Setting for CardLayout
		getContentPane().setLayout(panelHolder);
		getContentPane().add("Title", new TitlePanel(WIDTH, HEIGHT, this));
		getContentPane().add("Room", roomPanel);

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
		mainFrame.changePanel("Title");
	}

}
