package Instrument.VirtualDrum;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import RoomScreen.Connection.Client;
import RoomScreen.Layout.RoomPanel;
import Instrument.Key;

public class VirtualDrum extends JPanel implements KeyListener {
	Client client;
	final int NUM_DRUM_KEY = 8;
	Key[] drumKeys = new Key[NUM_DRUM_KEY];
	HashMap<Integer, Key> keyMap = new HashMap<Integer, Key>();

	public VirtualDrum(Client client) {
		this.client = client;
		
		setBackground(new Color(125, 187, 23));
		setSize(new Dimension(920, 350));
		setLayout(null);

		drumKeys[0] = new DrumKey(DrumKey.TYPE_METAL, 151, 152);
		drumKeys[0].setBounds(186, 0, 166, 167);

		drumKeys[1] = new DrumKey(DrumKey.TYPE_METAL, 133, 133);
		drumKeys[1].setBounds(186, 118, 146, 146);

		drumKeys[2] = new DrumKey(DrumKey.TYPE_METAL, 158, 158);
		drumKeys[2].setBounds(549, 0, 174, 174);

		drumKeys[3] = new DrumKey(DrumKey.TYPE_NORMAL, 139, 140);
		drumKeys[3].setBounds(470, 0, 153, 154);

		drumKeys[4] = new DrumKey(DrumKey.TYPE_NORMAL, 125, 126);
		drumKeys[4].setBounds(294, 0, 137, 138);

		drumKeys[5] = new DrumKey(DrumKey.TYPE_NORMAL, 157, 158);
		drumKeys[5].setBounds(246, 127, 172, 173);

		drumKeys[6] = new DrumKey(DrumKey.TYPE_NORMAL, 167, 169);
		drumKeys[6].setBounds(532, 114, 183, 185);

		drumKeys[7] = new DrumKey(DrumKey.TYPE_KICK, -1, -1);
		drumKeys[7].setBounds(337, 25, 222, 187);

		for (int i = 0; i < NUM_DRUM_KEY; i++)
			add(drumKeys[i]);
		//@preOn
		keyMap.put(KeyEvent.VK_F,			drumKeys[0].setNoteName("RID"));
		keyMap.put(KeyEvent.VK_V,			drumKeys[1].setNoteName("HIH"));
		keyMap.put(KeyEvent.VK_K,			drumKeys[2].setNoteName("CRS"));
		keyMap.put(KeyEvent.VK_J,			drumKeys[3].setNoteName("MTM"));
		keyMap.put(KeyEvent.VK_G,			drumKeys[4].setNoteName("LTM"));
		keyMap.put(KeyEvent.VK_B,			drumKeys[5].setNoteName("FTM"));
		keyMap.put(KeyEvent.VK_N,			drumKeys[6].setNoteName("SNR"));
		keyMap.put(KeyEvent.VK_SPACE,		drumKeys[7].setNoteName("KIK"));
		//@preOff
		addKeyListener(this);
		setFocusable(true);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		Key key = keyMap.get(e.getKeyCode());
		if (key == null) return;

		if (key.keyDown()) {
			client.sendMessage("DH_" + key.getNoteName());
		}
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
