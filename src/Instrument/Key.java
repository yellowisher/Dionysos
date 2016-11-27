package Instrument;

import javax.swing.JLabel;

public abstract class Key extends JLabel {
	protected String noteName;
	protected boolean isPressed = false;
	
	public Key(){}

	// Maybe change return type to String to return protocol like "PU_C#0"?
	public abstract boolean keyDown();
	public abstract void keyUp();

	public Key setNoteName(String noteName) {
		this.noteName = noteName;
		return this;
	}
	
	public String getNoteName(){
		return noteName;
	}
}