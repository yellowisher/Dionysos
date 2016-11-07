package Instrument.VirtualPiano;

import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

abstract class Key extends JLabel {
	protected Clip clip;
	protected String noteName;
	protected boolean isPressed = false;

	Key(ImageIcon icon) {
		super(icon);
	}

	// Maybe change return type to String to return protocol like "PU_C#0"?
	abstract void keyDown();
	abstract void keyUp();

	Key setNoteName(String noteName) {
		this.noteName = noteName;
		// Set clip here
		return this;
	}
}