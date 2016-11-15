package Instrument.VirtualGuitar;

import Instrument.Key;

public class HitKey extends Key{

	public HitKey() {
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
