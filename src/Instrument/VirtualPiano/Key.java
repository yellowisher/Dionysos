package Instrument.VirtualPiano;

import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

abstract class Key extends JLabel {
	protected Clip clip;
	protected boolean isPressed = false;

	Key(ImageIcon icon) {
		super(icon);
	}

	abstract void keyDown();
	abstract void keyUp();
}