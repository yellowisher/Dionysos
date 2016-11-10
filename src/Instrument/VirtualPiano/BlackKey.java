package Instrument.VirtualPiano;

import java.awt.Image;

import javax.swing.ImageIcon;

class BlackKey extends Key {
	static final int WIDTH = 20;
	static final int HEIGHT = 185;
	//39,185
	static ImageIcon keyUpImage;
	static ImageIcon keyDownImage;
	
	public BlackKey() {
		super();
		resizeImage();
	}

	@Override
	int keyDown() {
		if (isPressed) return 0;
		isPressed = true;
		setIcon(keyDownImage);
		return 1;
	}

	@Override
	void keyUp() {
		isPressed = false;
		setIcon(keyUpImage);
	}
	
	void resizeImage(){
		ImageIcon icon = new ImageIcon("Resource/Image/Piano/Key_Black.png");
		Image image =icon.getImage();
		Image newImg = image.getScaledInstance(WIDTH, HEIGHT, java.awt.Image.SCALE_SMOOTH);
		keyUpImage = new ImageIcon(newImg);
		
		ImageIcon icon2 = new ImageIcon("Resource/Image/Piano/Key_Black_Blue.png");
		Image image2 =icon2.getImage();
		Image newImg2 = image2.getScaledInstance(WIDTH, HEIGHT, java.awt.Image.SCALE_SMOOTH);
		keyDownImage = new ImageIcon(newImg2);
		this.setIcon(keyUpImage);
	}
}

