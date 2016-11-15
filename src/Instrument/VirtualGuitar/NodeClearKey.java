package Instrument.VirtualGuitar;

import Instrument.Key;

public class NodeClearKey extends Key{

	public NodeClearKey() {
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
