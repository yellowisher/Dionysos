package Instrument.VirtualGuitar;

import javax.swing.ImageIcon;
import Instrument.Key;

public class GuitarKey extends Key {
	ImageIcon keyUpImage;
	ImageIcon keyDownImage;

	public GuitarKey() {
		super();
	}

	@Override
	public Key setNoteName(String noteName) {
		this.noteName = noteName;
		keyUpImage = new ImageIcon("Resource/Image/Guitar/KeyUp/" + noteName.substring(0, 2) + ".png");
		keyDownImage = new ImageIcon("Resource/Image/Guitar/KeyDown/" + noteName.substring(0, 2) + ".png");
		setIcon(keyUpImage);
		return this;
	}

	@Override
	public boolean keyDown() {
		if (isPressed) return false;
		isPressed = true;
		setIcon(keyDownImage);
		return true;
	}

	@Override
	public void keyUp() {
		isPressed = false;
		setIcon(keyUpImage);
	}
}
