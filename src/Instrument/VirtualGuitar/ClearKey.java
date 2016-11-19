package Instrument.VirtualGuitar;

import Instrument.Key;

public class ClearKey extends Key{

	public ClearKey() {
		super();
	}

	@Override
	public boolean keyDown() {
		if (isPressed) return false;
		isPressed = true;
		return true;
	}

	@Override
	public void keyUp() {
		isPressed = false;
	}
}
