package Instrument.VirtualPiano;

import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;

class BlackKey extends Key {
	static ImageIcon keyUpImage = new ImageIcon("Resource/Image/Piano/Key_Black.png");
	static ImageIcon keyDownImage = new ImageIcon("Resource/Image/Piano/Key_Black_Blue.png");

	public BlackKey() {
		super(keyUpImage);
	}

	@Override
	void keyDown() {
		if (isPressed) return;
		isPressed = true;
		setIcon(keyDownImage);
		// Play clip here
	}

	@Override
	void keyUp() {
		isPressed = false;
		setIcon(keyUpImage);
		// Fade out clip here
	}
}
