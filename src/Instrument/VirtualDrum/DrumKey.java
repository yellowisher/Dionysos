package Instrument.VirtualDrum;

import java.awt.Image;

import javax.swing.ImageIcon;

import Instrument.Key;

public class DrumKey extends Key {
	public static final int TYPE_NORMAL = 0;
	public static final int TYPE_METAL = 1;
	public static final int TYPE_KICK = 2;

	//@preOn
	static Image[] images = {
			new ImageIcon("Resource/Image/Drum/normal.png").getImage(),
			new ImageIcon("Resource/Image/Drum/metal.png").getImage(),
			new ImageIcon("Resource/Image/Drum/kick.png").getImage(),			
	};
	//@preOff

	private ImageIcon keyUpImage;
	private ImageIcon keyDownImage;
	private int xDelta;

	public DrumKey(int type, int width, int height) {
		super();

		if (height == -1) {
			keyUpImage = new ImageIcon(images[type]);
			keyDownImage = new ImageIcon(images[type].getScaledInstance(222, 187, Image.SCALE_SMOOTH));
			xDelta = 10;
		}
		else {
			keyUpImage = new ImageIcon(images[type].getScaledInstance(width, height, Image.SCALE_SMOOTH));
			keyDownImage = new ImageIcon(images[type].getScaledInstance((int) (width * 1.1), (int) (height * 1.1), Image.SCALE_SMOOTH));
			xDelta = ((int) (width * 0.1)) / 2;
		}
		setIcon(keyUpImage);
	}

	/*
	 * Visual effect when hit drum
	 * set size * 1.1 and change its position little bit
	 * to make it looks like its scaling position is center
	 */
	@Override
	public boolean keyDown() {
		if (isPressed) return false;
		isPressed = true;
		setLocation((int) (getLocation().getX() - xDelta), (int) (getLocation().getY()));
		setIcon(keyDownImage);
		return true;
	}

	@Override
	public void keyUp() {
		if (!isPressed) return;
		isPressed = false;
		setLocation((int) (getLocation().getX() + xDelta), (int) (getLocation().getY()));
		setIcon(keyUpImage);
	}
}
