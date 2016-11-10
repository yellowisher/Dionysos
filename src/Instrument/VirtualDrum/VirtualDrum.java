package Instrument.VirtualDrum;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import RoomScreen.Connection.Client;
import RoomScreen.Layout.Main;

public class VirtualDrum extends JPanel implements KeyListener {
	JPanel parent;
	Client client;
	Main main;
	final int N_DRUM = 8;
	DrumKey[] drums = new DrumKey[N_DRUM];
	HashMap<Integer, Key> keyMap = new HashMap<Integer, Key>();
	public VirtualDrum(JPanel parent, Main main) {
		this.parent = parent;
		this.main = main;
		initComponents();
		for (int i = 0; i < N_DRUM; i++) {
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
		System.out.println("생성완료");
		jLabel1.setIcon(icon);
		jLabel1.setOpaque(true);
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGap(216, 216, 216).addComponent(jLabel1).addContainerGap(223, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addContainerGap(20, Short.MAX_VALUE)));
	}// </editor-fold>                        

	@Override
	public void keyPressed(KeyEvent e) {
		Key key = keyMap.get(e.getKeyCode());
		if (key == null) return;

		int check = key.keyDown();
		if (check == 1) {
			this.client = main.getClient();
			client.sendMessage("DH_" + key.getNoteName());
		}
		//INSTRU,CODE
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
