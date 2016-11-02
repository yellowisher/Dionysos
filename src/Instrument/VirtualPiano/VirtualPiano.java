package Instrument.VirtualPiano;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import javax.swing.JPanel;

// Good luck dong-jae!
public class VirtualPiano extends JPanel implements KeyListener {

	BlackKey[] blackKeys = new BlackKey[7];
	WhiteKey[] whiteKeys = new WhiteKey[10];
	HashMap<Integer, Key> keyMap = new HashMap<Integer, Key>();

	public VirtualPiano() {
		setBackground(new Color(247, 145, 0));
		setLayout(null);

		int keyWidth = BlackKey.keyUpImage.getIconWidth();
		int keyHeight = BlackKey.keyUpImage.getIconHeight();

		for (int i = 0; i < 7; i++) {
			// Skips black keys
			int g = i + ((i + 1) / 3);
			blackKeys[i] = new BlackKey(null);
			blackKeys[i].setBounds(89 + (int) (73 * g), 33, keyWidth, keyHeight);
			add(blackKeys[i]);
		}

		keyWidth = WhiteKey.keyUpImage.getIconWidth();
		keyHeight = WhiteKey.keyUpImage.getIconHeight();

		for (int i = 0; i < 10; i++) {
			whiteKeys[i] = new WhiteKey(null);
			whiteKeys[i].setBounds(33 + 73 * i, 33, keyWidth, keyHeight);
			add(whiteKeys[i]);
		}

		//@preOn
		keyMap.put(KeyEvent.VK_Z,			whiteKeys[0]);
		keyMap.put(KeyEvent.VK_X,			whiteKeys[1]);
		keyMap.put(KeyEvent.VK_C,			whiteKeys[2]);
		keyMap.put(KeyEvent.VK_V,			whiteKeys[3]);
		keyMap.put(KeyEvent.VK_B,			whiteKeys[4]);
		keyMap.put(KeyEvent.VK_N,			whiteKeys[5]);
		keyMap.put(KeyEvent.VK_M,			whiteKeys[6]);
		keyMap.put(KeyEvent.VK_COMMA,		whiteKeys[7]);
		keyMap.put(KeyEvent.VK_PERIOD,		whiteKeys[8]);
		keyMap.put(KeyEvent.VK_SLASH,		whiteKeys[9]);
		
		keyMap.put(KeyEvent.VK_S,			blackKeys[0]);
		keyMap.put(KeyEvent.VK_D,			blackKeys[1]);
		keyMap.put(KeyEvent.VK_G,			blackKeys[2]);
		keyMap.put(KeyEvent.VK_H,			blackKeys[3]);
		keyMap.put(KeyEvent.VK_J,			blackKeys[4]);
		keyMap.put(KeyEvent.VK_L,			blackKeys[5]);
		keyMap.put(KeyEvent.VK_SEMICOLON,	blackKeys[6]);
		//@preOff

		addKeyListener(this);
		setFocusable(true);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println(e.getKeyChar());

		Key key = keyMap.get(e.getKeyCode());
		if (key == null) return;

		key.keyDown();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		Key key = keyMap.get(e.getKeyCode());
		if (key == null) return;

		key.keyUp();
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}
}
