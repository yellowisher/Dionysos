package Instrument.VirtualDrum;

import javax.sound.sampled.Clip;
import javax.swing.JLabel;

abstract class Key extends JLabel {
	protected Clip clip;
	protected String noteName;
	protected boolean isPressed = false;
	
	Key(){}
//	Key(ImageIcon icon) {
//		super(icon);
//	}

	// Maybe change return type to String to return protocol like "PU_C#0"?
	abstract int keyDown();
	abstract void keyUp();

	Key setNoteName(String noteName) {
		this.noteName = noteName;
		// Set clip here
		return this;
	}
	
	protected String getNoteName(){
		return noteName;
	}
	
//	protected void play(){
//		File file = new File("Resource/Audio/Piano/"+getNoteName()+".wav");
//		try{
//		clip = AudioSystem.getClip();
//		clip.open(AudioSystem.getAudioInputStream(file));
//		clip.start();
//		}
//		catch(Exception e){}
//	}
	
	
}