package Instrument.VirtualPiano;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.border.Border;

import RoomScreen.Connection.Client;
import RoomScreen.Layout.Main;

// Good luck dong-jae!
public class VirtualPiano extends JPanel implements KeyListener {
	JPanel parent;
	Client client;
	Main main;
	final int N_BLACKKEY = 15;
	final int N_WHITEKEY = 22;
	BlackKey[] blackKeys = new BlackKey[N_BLACKKEY];
	WhiteKey[] whiteKeys = new WhiteKey[N_WHITEKEY];
	HashMap<Integer, Key> keyMap = new HashMap<Integer, Key>();

	public VirtualPiano(JPanel parent, Main main) {
		this.parent = parent;
		this.main = main;
		setBackground(new Color(247, 145, 0));
		setLayout(null);

		init();
		int keyWidth = BlackKey.WIDTH;
		int keyHeight = BlackKey.HEIGHT;

		for (int i = 0; i < N_BLACKKEY; i++) {
			// Skips black keys
			int g;
			blackKeys[i] = new BlackKey();
			if (i < 5) {
				g = i + ((i + 1) / 3);
				blackKeys[i].setBounds(50 + (int) (40 * g), 33, keyWidth, keyHeight);
			}
			else if (i < 10) {
				g = (i - 5) + (((i - 5) + 1) / 3);
				blackKeys[i].setBounds(280 + 50 + (int) (40 * g), 33, keyWidth, keyHeight);
			}
			else if (i < 15) {
				g = (i - 10) + (((i - 10) + 1) / 3);
				blackKeys[i].setBounds(280 * 2 + 50 + (int) (40 * g), 33, keyWidth, keyHeight);
			}
			add(blackKeys[i]);
		}

		keyWidth = WhiteKey.WIDTH;
		keyHeight = WhiteKey.HEIGHT;

		for (int i = 0; i < N_WHITEKEY; i++) {
			whiteKeys[i] = new WhiteKey();
			whiteKeys[i].setBounds(20 + 40 * i, 33, keyWidth, keyHeight - 80);
			add(whiteKeys[i]);
		}

		//@preOn
		keyMap.put(KeyEvent.VK_Z,					whiteKeys[0].setNoteName("C_0"));
		keyMap.put(KeyEvent.VK_X,					whiteKeys[1].setNoteName("D_0"));
		keyMap.put(KeyEvent.VK_C,					whiteKeys[2].setNoteName("E_0"));
		keyMap.put(KeyEvent.VK_V,					whiteKeys[3].setNoteName("F_0"));
		keyMap.put(KeyEvent.VK_B,					whiteKeys[4].setNoteName("G_0"));
		keyMap.put(KeyEvent.VK_N,					whiteKeys[5].setNoteName("A_0"));
		keyMap.put(KeyEvent.VK_M,					whiteKeys[6].setNoteName("B_0"));
		keyMap.put(KeyEvent.VK_COMMA,				whiteKeys[7].setNoteName("C_1"));
		keyMap.put(KeyEvent.VK_PERIOD,				whiteKeys[8].setNoteName("D_1"));
		keyMap.put(KeyEvent.VK_SLASH,				whiteKeys[9].setNoteName("E_1"));
		keyMap.put(KeyEvent.VK_Q,					whiteKeys[10].setNoteName("F_1"));
		keyMap.put(KeyEvent.VK_W,					whiteKeys[11].setNoteName("G_1"));
		keyMap.put(KeyEvent.VK_E,					whiteKeys[12].setNoteName("A_1"));
		keyMap.put(KeyEvent.VK_R,					whiteKeys[13].setNoteName("B_1"));
		keyMap.put(KeyEvent.VK_T,					whiteKeys[14].setNoteName("C_2"));
		keyMap.put(KeyEvent.VK_Y,					whiteKeys[15].setNoteName("D_2"));
		keyMap.put(KeyEvent.VK_U,					whiteKeys[16].setNoteName("E_2"));
		keyMap.put(KeyEvent.VK_I,					whiteKeys[17].setNoteName("F_2"));
		keyMap.put(KeyEvent.VK_O,					whiteKeys[18].setNoteName("G_2"));
		keyMap.put(KeyEvent.VK_P,					whiteKeys[19].setNoteName("A_2"));
		keyMap.put(KeyEvent.VK_OPEN_BRACKET,		whiteKeys[20].setNoteName("B_2"));
		keyMap.put(KeyEvent.VK_CLOSE_BRACKET,		whiteKeys[21].setNoteName("C_3"));

		keyMap.put(KeyEvent.VK_S,					blackKeys[0].setNoteName("C#0"));
		keyMap.put(KeyEvent.VK_D,					blackKeys[1].setNoteName("D#0"));
		keyMap.put(KeyEvent.VK_G,					blackKeys[2].setNoteName("F#0"));
		keyMap.put(KeyEvent.VK_H,					blackKeys[3].setNoteName("G#0"));
		keyMap.put(KeyEvent.VK_J,					blackKeys[4].setNoteName("A#0"));
		keyMap.put(KeyEvent.VK_L,					blackKeys[5].setNoteName("C#1"));
		keyMap.put(KeyEvent.VK_SEMICOLON,			blackKeys[6].setNoteName("D#1"));
		keyMap.put(KeyEvent.VK_2,         			blackKeys[7].setNoteName("F#1"));
		keyMap.put(KeyEvent.VK_3,	      			blackKeys[8].setNoteName("G#1"));
		keyMap.put(KeyEvent.VK_4,	      			blackKeys[9].setNoteName("A#1"));
		keyMap.put(KeyEvent.VK_6,	      			blackKeys[10].setNoteName("C#2"));
		keyMap.put(KeyEvent.VK_7,	      			blackKeys[11].setNoteName("D#2"));
		keyMap.put(KeyEvent.VK_9,	     		  	blackKeys[12].setNoteName("F#2"));
		keyMap.put(KeyEvent.VK_0,	      			blackKeys[13].setNoteName("G#2"));
		keyMap.put(KeyEvent.VK_MINUS,	  		 	blackKeys[14].setNoteName("A#2"));
		//@preOff
		
		addKeyListener(this);
		setFocusable(true);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		Key key = keyMap.get(e.getKeyCode());
		if (key == null) return;

		int check = key.keyDown();
		if (check == 1) {
			this.client = main.getClient();
			client.sendMessage("PD_" + key.getNoteName());
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		Key key = keyMap.get(e.getKeyCode());
		if (key == null) return;
		this.client = main.getClient();
		client.sendMessage("PU_" + key.getNoteName());
		key.keyUp();
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	private void init() {
		setPreferredSize(new Dimension(920, 350));
		setSize(new Dimension(920, 350));
	}
}
