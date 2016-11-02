package VirtualPiano;

import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;

class BlackKey extends Key {
	static ImageIcon keyUpImage = new ImageIcon("Resource/Image/Piano/Key_Black.png");
	static ImageIcon keyDownImage = new ImageIcon("Resource/Image/Piano/Key_Black_Blue.png");

	public BlackKey(Clip clip) {
		super(keyUpImage);
		this.clip = clip;
	}

	@Override
	void keyDown() {
		setIcon(keyDownImage);
		// Play clip here
	}

	@Override
	void keyUp() {
		setIcon(keyUpImage);
		// Fade out clip here
	}
}
