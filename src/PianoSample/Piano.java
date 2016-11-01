package PianoSample;

import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import Recorder.Recorder;

public class Piano {

	public static void main(String[] args) throws Exception {
		AudioInputStream ais = AudioSystem.getAudioInputStream(new File("Resource/Audio/Piano/D#0.wav"));
		Clip c = AudioSystem.getClip();
		c.open(ais);
		Scanner kb=new Scanner(System.in);
		
		Recorder recorder=new Recorder();
		long startTime = System.currentTimeMillis();
		
		escape:
		while(true){
			switch(kb.nextInt()){
			case 1:
				c.stop();
				c.flush();
				c.setFramePosition(0);
				c.start();
				recorder.record("Piano,C");
				break;
			case 2:
				break escape;
			}
		}
		recorder.stop((int) (System.currentTimeMillis() - startTime));
		recorder.saveAs("WAHAA.txt");

	}

}
