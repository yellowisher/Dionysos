package Instrument.VirtualDrum;

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
	DrumKey[] drums = new DrumKey[NUM_DRUM_KEY];
	HashMap<Integer, Key> keyMap = new HashMap<Integer, Key>();

	public VirtualDrum(Client client) {
		this.client = client;
		initComponents();
		
		for (int i = 0; i < NUM_DRUM_KEY; i++) {
			drums[i] = new DrumKey();
		}
		//@preOn
				keyMap.put(KeyEvent.VK_N,			drums[0].setNoteName("SNR"));
				keyMap.put(KeyEvent.VK_F,			drums[1].setNoteName("RID"));
				keyMap.put(KeyEvent.VK_J,			drums[2].setNoteName("MTM"));
				keyMap.put(KeyEvent.VK_G,			drums[3].setNoteName("LTM"));
				keyMap.put(KeyEvent.VK_SPACE,		drums[4].setNoteName("KIK"));
				keyMap.put(KeyEvent.VK_V,			drums[5].setNoteName("HIH"));
				keyMap.put(KeyEvent.VK_B,			drums[6].setNoteName("FTM"));
				keyMap.put(KeyEvent.VK_K,			drums[7].setNoteName("CRS"));
		//@preOff
		addKeyListener(this);
		setFocusable(true);
	}

	private void initComponents() {

		jLabel1 = new javax.swing.JLabel();

		setBackground(new java.awt.Color(21, 21, 21));
		setSize(920, 350);
		ImageIcon icon = new ImageIcon("Resource/Image/drum/drum_Panel.jpg");
		jLabel1.setIcon(icon);
		jLabel1.setOpaque(true);
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGap(216, 216, 216).addComponent(jLabel1).addContainerGap(223, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addContainerGap(20, Short.MAX_VALUE)));
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

	// Variables declaration - do not modify                     
	private javax.swing.JLabel jLabel1;
}
