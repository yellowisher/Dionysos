package Instrument.VirtualDrum;

import Instrument.Key;

public class DrumKey extends Key {

	public DrumKey() {
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
