package Instrument.VirtualGuitar;

import java.awt.Image;

import javax.swing.ImageIcon;

public class GuitarKey extends Key{
	static ImageIcon keyUpImage;
	static ImageIcon keyDownImage;
	public GuitarKey() {
		super();
	}

	@Override
	int keyDown() {
		if (isPressed) return 0;
		isPressed = true;
//		setIcon(keyDownImage);
		// Play clip here
//		play();
		return 1;
	}

	@Override
	void keyUp() {
		isPressed = false;
//		setIcon(keyUpImage);
		// Fade out clip here
	}
	
	void resizeImage(String t){
		String key = t.substring(0,1);
		ImageIcon icon = new ImageIcon("Resource/Image/Guitar/KeyUp/" + key + ".png");
		Image image = icon.getImage();
		keyUpImage = new ImageIcon(image);

		ImageIcon icon2 = new ImageIcon("Resource/Image/Guitar/KeyDown/" + key + ".png");
		Image image2 = icon.getImage();
		keyDownImage = new ImageIcon(image);
		keyDownImage = new ImageIcon(image2);
		
		this.setIcon(keyUpImage);
	}
}
