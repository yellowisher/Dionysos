package Instrument.VirtualGuitar;

import javax.swing.JPanel;

import Instrument.Key;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import RoomScreen.Connection.Client;

//Good luck dong-jae!
public class VirtualGuitar extends JPanel implements KeyListener {
	static final int NUM_LINE = 4;
	static final int NUM_KEY = 10;
	static final int NUM_COMB = 9;

	static final int CODE_SIZE = 60;
	static final int COMB_SIZE = 80;

	Client client;

	HashMap<Integer, Key> keyMap = new HashMap<Integer, Key>();
	ArrayList<GuitarKey> storedKey;
	ArrayList<GuitarKey> pressedKey;
	boolean lastWasHit = false;

	public VirtualGuitar(Client client) {
		this.client = client;

		setBackground(new Color(13, 100, 222));
		setPreferredSize(new Dimension(920, 350));
		setSize(new Dimension(920, 350));
		setLayout(null);

		GuitarKey[][] keys = new GuitarKey[NUM_LINE][NUM_KEY];
		CombineKey[] combine = new CombineKey[NUM_COMB];
		storedKey = new ArrayList<GuitarKey>();
		pressedKey = new ArrayList<GuitarKey>();

		for (int l = 0; l < NUM_LINE; l++) {
			for (int k = 0; k < NUM_KEY; k++) {
				keys[l][k] = new GuitarKey();
				keys[l][k].setBounds(5 + k * 68, 5 + (NUM_LINE - l - 1) * 74, CODE_SIZE, CODE_SIZE);
				add(keys[l][k]);
			}
		}
		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) {
				combine[r * 3 + c] = new CombineKey();
				combine[r * 3 + c].setBounds(680 + c * 80, 27 + r * 80, COMB_SIZE, COMB_SIZE);
				add(combine[r * 3 + c]);
			}
		}

		ClearKey Back = new ClearKey();
		HitKey Hit = new HitKey();

		//@preOn
		keyMap.put(KeyEvent.VK_Z,			keys[0][0].setNoteName("E_0_1"));
		keyMap.put(KeyEvent.VK_X,			keys[0][1].setNoteName("F_0_1"));
		keyMap.put(KeyEvent.VK_C,			keys[0][2].setNoteName("F#0_1"));
		keyMap.put(KeyEvent.VK_V,			keys[0][3].setNoteName("G_0_1"));
		keyMap.put(KeyEvent.VK_B,			keys[0][4].setNoteName("G#0_1"));
		keyMap.put(KeyEvent.VK_N,			keys[0][5].setNoteName("A_0_1"));
		keyMap.put(KeyEvent.VK_M,			keys[0][6].setNoteName("A#0_1"));
		keyMap.put(KeyEvent.VK_COMMA,		keys[0][7].setNoteName("B_0_1"));
		keyMap.put(KeyEvent.VK_PERIOD,		keys[0][8].setNoteName("C_1_1"));
		keyMap.put(KeyEvent.VK_SLASH,		keys[0][9].setNoteName("C#1_1"));
				
		keyMap.put(KeyEvent.VK_A,			keys[1][0].setNoteName("A_0_2"));
		keyMap.put(KeyEvent.VK_S,			keys[1][1].setNoteName("A#0_2"));
		keyMap.put(KeyEvent.VK_D,			keys[1][2].setNoteName("B_0_2"));
		keyMap.put(KeyEvent.VK_F,			keys[1][3].setNoteName("C_1_2"));
		keyMap.put(KeyEvent.VK_G,			keys[1][4].setNoteName("C#1_2"));
		keyMap.put(KeyEvent.VK_H,			keys[1][5].setNoteName("D_1_2"));
		keyMap.put(KeyEvent.VK_J,			keys[1][6].setNoteName("D#1_2"));
		keyMap.put(KeyEvent.VK_K,			keys[1][7].setNoteName("E_1_2"));
		keyMap.put(KeyEvent.VK_L,			keys[1][8].setNoteName("F_1_2"));
		keyMap.put(KeyEvent.VK_SEMICOLON,	keys[1][9].setNoteName("F#1_2"));

		keyMap.put(KeyEvent.VK_W,			keys[2][0].setNoteName("D_1_3"));
		keyMap.put(KeyEvent.VK_E,			keys[2][1].setNoteName("D#1_3"));
		keyMap.put(KeyEvent.VK_R,			keys[2][2].setNoteName("E_1_3"));
		keyMap.put(KeyEvent.VK_T,			keys[2][3].setNoteName("F_1_3"));
		keyMap.put(KeyEvent.VK_Y,			keys[2][4].setNoteName("F#1_3"));
		keyMap.put(KeyEvent.VK_U,			keys[2][5].setNoteName("G_1_3"));
		keyMap.put(KeyEvent.VK_I,			keys[2][6].setNoteName("G#1_3"));
		keyMap.put(KeyEvent.VK_O,			keys[2][7].setNoteName("A_1_3"));
		keyMap.put(KeyEvent.VK_P,			keys[2][8].setNoteName("A#1_3"));
		keyMap.put(KeyEvent.VK_OPEN_BRACKET,keys[2][9].setNoteName("B_1_3"));

		keyMap.put(KeyEvent.VK_2,			keys[3][0].setNoteName("G_1_4"));
		keyMap.put(KeyEvent.VK_3,			keys[3][1].setNoteName("G#1_4"));
		keyMap.put(KeyEvent.VK_4,			keys[3][2].setNoteName("A_1_4"));
		keyMap.put(KeyEvent.VK_5,			keys[3][3].setNoteName("A#1_4"));
		keyMap.put(KeyEvent.VK_6,			keys[3][4].setNoteName("B_1_4"));
		keyMap.put(KeyEvent.VK_7,			keys[3][5].setNoteName("C_2_4"));
		keyMap.put(KeyEvent.VK_8,			keys[3][6].setNoteName("C#2_4"));
		keyMap.put(KeyEvent.VK_9,			keys[3][7].setNoteName("D_2_4"));
		keyMap.put(KeyEvent.VK_0,			keys[3][8].setNoteName("D#2_4"));
		keyMap.put(KeyEvent.VK_MINUS,		keys[3][9].setNoteName("E_2_4"));

		keyMap.put(KeyEvent.VK_NUMPAD7,		combine[0].setNoteName("c1"));
		keyMap.put(KeyEvent.VK_NUMPAD8,		combine[1].setNoteName("c2"));
		keyMap.put(KeyEvent.VK_NUMPAD9,		combine[2].setNoteName("c3"));
		keyMap.put(KeyEvent.VK_NUMPAD4,		combine[3].setNoteName("c4"));
		keyMap.put(KeyEvent.VK_NUMPAD5,		combine[4].setNoteName("c5"));
		keyMap.put(KeyEvent.VK_NUMPAD6,		combine[5].setNoteName("c6"));
		keyMap.put(KeyEvent.VK_NUMPAD1,		combine[6].setNoteName("c7"));
		keyMap.put(KeyEvent.VK_NUMPAD2,		combine[7].setNoteName("c8"));
		keyMap.put(KeyEvent.VK_NUMPAD3,		combine[8].setNoteName("c9"));

		keyMap.put(KeyEvent.VK_SPACE,		Hit.setNoteName("HIT"));
		keyMap.put(KeyEvent.VK_BACK_SPACE,	Back.setNoteName("CANCEL"));
		//@preOff

		addKeyListener(this);
		setFocusable(true);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		Key key = keyMap.get(e.getKeyCode());
		if (key == null) return;

		if (key.keyDown()) {
			String keyName = key.getNoteName();

			if (keyName.startsWith("HIT")) {
				for (Key aKey : storedKey) {
					aKey.keyDown();
					pressedKey.add((GuitarKey) aKey);
					client.sendMessage("GH_" + aKey.getNoteName().substring(0, 3));
					lastWasHit = true;
				}
			}
			else if (keyName.startsWith("CANCEL")) {
				storedKey.clear();
			}
			else if (keyName.startsWith("c")) {
				lastWasHit = false;
				if (e.isControlDown()) {
					if (!storedKey.isEmpty()) ((CombineKey) key).setStoredKey(storedKey);
				}
				else {
					if (((CombineKey) key).isStored) storedKey = ((CombineKey) key).getStoredKey();
				}
			}
			else {
				if (lastWasHit) storedKey.clear();
				if (!storedKey.contains(key)) storedKey.add((GuitarKey) key);
				lastWasHit = false;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		Key key = keyMap.get(e.getKeyCode());
		if (key == null) return;

		if (key.getNoteName().equals("HIT")) {
			for (Key aKey : pressedKey) {
				aKey.keyUp();
			}
		}
		key.keyUp();
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	// Variables declaration - do not modify                     
	private javax.swing.JLabel jLabel1;
}
