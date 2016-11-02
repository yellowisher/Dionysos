package VirtualPiano;

import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;

class WhiteKey extends Key {
	static ImageIcon keyUpImage = new ImageIcon("Resource/Image/Piano/Key_White.png");
	static ImageIcon keyDownImage = new ImageIcon("Resource/Image/Piano/Key_White_Blue.png");

	public WhiteKey(Clip clip) {
		super(keyUpImage);
		this.clip = clip;
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