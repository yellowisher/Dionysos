package Instrument.VirtualGuitar;

import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import Instrument.Key;

public class CombineKey extends Key {
	ImageIcon keyUpImage;
	ImageIcon keyDownImage;

	ArrayList<GuitarKey> storedKey;
	public boolean isStored;

	public CombineKey() {
		super();
		isStored = false;
		storedKey = new ArrayList<GuitarKey>();
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
		isPressed = false;
		setIcon(keyUpImage);
	}

	public void reset() {
		isStored = false;
		storedKey.clear();
	}
	
	@SuppressWarnings("unchecked")
	public void setStoredKey(ArrayList<GuitarKey> storedKey) {
		isStored = true;
		this.storedKey = (ArrayList<GuitarKey>) storedKey.clone();
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<GuitarKey> getStoredKey() {
		return (ArrayList<GuitarKey>) storedKey.clone();
	}
}
