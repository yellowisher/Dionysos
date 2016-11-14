package Instrument.VirtualGuitar;

public class NodeClearKey extends Key{

	public NodeClearKey() {
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
}
