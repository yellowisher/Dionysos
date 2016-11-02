package MainScreen;

import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JPanel;

import VirtualPiano.VirtualPiano;
import a.VirtualNothing;

public class RoomPanel extends JPanel {
	VirtualInst inst;

	public RoomPanel(int frameWidth, int frameHeight) {
		this.setLayout(null);

		inst = new VirtualInst();
		add(inst);
	}

	class VirtualInst extends JPanel {
		CardLayout instHolder = new CardLayout();

		public VirtualInst() {
			setLayout(instHolder);
			setBounds(0, 150, 800, 350);

			add("Nothing", new VirtualNothing());
			add("Piano", new VirtualNothing());
			// add("Drum", new VirtualDrum());
			// add("Guitar", new VirtualGuitar());

			changeInst("Piano");
		}

		public void changeInst(String inst) {
			instHolder.show(this, inst);
		}
	}
}
