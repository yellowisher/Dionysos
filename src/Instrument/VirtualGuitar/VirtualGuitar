package Instrument.VirtualGuitar;

import javax.swing.JPanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import javax.swing.ImageIcon;
import RoomScreen.Connection.Client;
import RoomScreen.Layout.Main;
//Good luck dong-jae!
public class VirtualGuitar extends JPanel implements KeyListener{
	JPanel parent;
	Client client;
	Main main;
	final int N_Guitar = 10;
	final int N_Com = 9;
	
	int keySize = 60;
	int ComSize = 80;
	
	HashMap<Integer, Key> keyMap = new HashMap<Integer, Key>();
	
	String[] ComTemp = new String[10];
	int ComNum = 0;
	
	String[][] CO = new String[10][10];
	
	int C1=0;
	int C2=0;
	int C3=0;
	int C4=0;
	int C5=0;
	int C6=0;
	int C7=0;
	int C8=0;
	int C9=0;
	int hit=0;
	int[] Cstat = new int[9];


	
	public VirtualGuitar(JPanel parent, Main main) {
		this.parent = parent;
		this.main = main;
		
		
		GuitarKey[] line1 = new GuitarKey[N_Guitar];
		GuitarKey[] line2 = new GuitarKey[N_Guitar];
		GuitarKey[] line3 = new GuitarKey[N_Guitar];
		GuitarKey[] line4 = new GuitarKey[N_Guitar];
		GuitarKey[] Combine = new GuitarKey[9];
		
		for(int i=0;i<9;i++){
			Cstat[i]=0;
		}
		
		NodeClearKey Back = new NodeClearKey();
		HitKey Hit = new HitKey();
				
		for (int i = 0; i < N_Guitar; i++) {
			line1[i] = new GuitarKey();
		}
		for (int i = 0; i < N_Guitar; i++) {
			line2[i] = new GuitarKey();
		}
		for (int i = 0; i < N_Guitar; i++) {
			line3[i] = new GuitarKey();
		}
		for (int i = 0; i < N_Guitar; i++) {
			line4[i] = new GuitarKey();
		}
		for (int i = 0; i < 9; i++) {
			Combine[i] = new GuitarKey();
		}
			
		
		//@preOn
				keyMap.put(KeyEvent.VK_Z,			line1[0].setNoteName("E_0"));
				keyMap.put(KeyEvent.VK_X,			line1[1].setNoteName("F_0"));
				keyMap.put(KeyEvent.VK_C,			line1[2].setNoteName("F#0"));
				keyMap.put(KeyEvent.VK_V,			line1[3].setNoteName("G_0"));
				keyMap.put(KeyEvent.VK_B,			line1[4].setNoteName("G#0"));
				keyMap.put(KeyEvent.VK_N,			line1[5].setNoteName("A_0"));
				keyMap.put(KeyEvent.VK_M,			line1[6].setNoteName("A#0"));
				keyMap.put(KeyEvent.VK_COMMA,		line1[7].setNoteName("B_0"));
				keyMap.put(KeyEvent.VK_PERIOD,		line1[8].setNoteName("C_1"));
				keyMap.put(KeyEvent.VK_SLASH,		line1[9].setNoteName("C#1"));
				
				line1[0].setNoteNum(10);
				line1[1].setNoteNum(11);
				line1[2].setNoteNum(12);
				line1[3].setNoteNum(13);
				line1[4].setNoteNum(14);
				line1[5].setNoteNum(15);
				line1[6].setNoteNum(16);
				line1[7].setNoteNum(17);
				line1[8].setNoteNum(18);
				line1[9].setNoteNum(19);
				
				keyMap.put(KeyEvent.VK_A,			line2[0].setNoteName("A_0"));
				keyMap.put(KeyEvent.VK_S,			line2[1].setNoteName("A#0"));
				keyMap.put(KeyEvent.VK_D,			line2[2].setNoteName("B_0"));
				keyMap.put(KeyEvent.VK_F,			line2[3].setNoteName("C_1"));
				keyMap.put(KeyEvent.VK_G,			line2[4].setNoteName("C#1"));
				keyMap.put(KeyEvent.VK_H,			line2[5].setNoteName("D_1"));
				keyMap.put(KeyEvent.VK_J,			line2[6].setNoteName("D#1"));
				keyMap.put(KeyEvent.VK_K,			line2[7].setNoteName("E_1"));
				keyMap.put(KeyEvent.VK_L,			line2[8].setNoteName("F_1"));
				keyMap.put(KeyEvent.VK_SEMICOLON,	line2[9].setNoteName("F#1"));
				
				line2[0].setNoteNum(20);
				line2[1].setNoteNum(21);
				line2[2].setNoteNum(22);
				line2[3].setNoteNum(23);
				line2[4].setNoteNum(24);
				line2[5].setNoteNum(25);
				line2[6].setNoteNum(26);
				line2[7].setNoteNum(27);
				line2[8].setNoteNum(28);
				line2[9].setNoteNum(29);

				keyMap.put(KeyEvent.VK_W,			line3[0].setNoteName("D_1"));
				keyMap.put(KeyEvent.VK_E,			line3[1].setNoteName("D#1"));
				keyMap.put(KeyEvent.VK_R,			line3[2].setNoteName("E_1"));
				keyMap.put(KeyEvent.VK_T,			line3[3].setNoteName("F_1"));
				keyMap.put(KeyEvent.VK_Y,			line3[4].setNoteName("F#1"));
				keyMap.put(KeyEvent.VK_U,			line3[5].setNoteName("G_1"));
				keyMap.put(KeyEvent.VK_I,			line3[6].setNoteName("G#1"));
				keyMap.put(KeyEvent.VK_O,			line3[7].setNoteName("A_1"));
				keyMap.put(KeyEvent.VK_P,			line3[8].setNoteName("A#1"));
				keyMap.put(KeyEvent.VK_OPEN_BRACKET,line3[9].setNoteName("B_1"));

				line3[0].setNoteNum(30);
				line3[1].setNoteNum(31);
				line3[2].setNoteNum(32);
				line3[3].setNoteNum(33);
				line3[4].setNoteNum(34);
				line3[5].setNoteNum(35);
				line3[6].setNoteNum(36);
				line3[7].setNoteNum(37);
				line3[8].setNoteNum(38);
				line3[9].setNoteNum(39);

				keyMap.put(KeyEvent.VK_2,			line4[0].setNoteName("G_1"));
				keyMap.put(KeyEvent.VK_3,			line4[1].setNoteName("G#1"));
				keyMap.put(KeyEvent.VK_4,			line4[2].setNoteName("A_1"));
				keyMap.put(KeyEvent.VK_5,			line4[3].setNoteName("A#1"));
				keyMap.put(KeyEvent.VK_6,			line4[4].setNoteName("B_1"));
				keyMap.put(KeyEvent.VK_7,			line4[5].setNoteName("C_2"));
				keyMap.put(KeyEvent.VK_8,			line4[6].setNoteName("C#2"));
				keyMap.put(KeyEvent.VK_9,			line4[7].setNoteName("D_2"));
				keyMap.put(KeyEvent.VK_0,			line4[8].setNoteName("D#2"));
				keyMap.put(KeyEvent.VK_MINUS,		line4[9].setNoteName("E_2"));
				
				line4[0].setNoteNum(40);
				line4[1].setNoteNum(41);
				line4[2].setNoteNum(42);
				line4[3].setNoteNum(43);
				line4[4].setNoteNum(44);
				line4[5].setNoteNum(45);
				line4[6].setNoteNum(46);
				line4[7].setNoteNum(47);
				line4[8].setNoteNum(48);
				line4[9].setNoteNum(49);

				keyMap.put(KeyEvent.VK_NUMPAD1,		Combine[0].setNoteName("C1"));
				keyMap.put(KeyEvent.VK_NUMPAD2,		Combine[1].setNoteName("C2"));
				keyMap.put(KeyEvent.VK_NUMPAD3,		Combine[2].setNoteName("C3"));
				keyMap.put(KeyEvent.VK_NUMPAD4,		Combine[3].setNoteName("C4"));
				keyMap.put(KeyEvent.VK_NUMPAD5,		Combine[4].setNoteName("C5"));
				keyMap.put(KeyEvent.VK_NUMPAD6,		Combine[5].setNoteName("C6"));
				keyMap.put(KeyEvent.VK_NUMPAD7,		Combine[6].setNoteName("C7"));
				keyMap.put(KeyEvent.VK_NUMPAD8,		Combine[7].setNoteName("C8"));
				keyMap.put(KeyEvent.VK_NUMPAD9,		Combine[8].setNoteName("C9"));
	
				Combine[0].setNoteNum(0);
				Combine[1].setNoteNum(1);
				Combine[2].setNoteNum(2);
				Combine[3].setNoteNum(3);
				Combine[4].setNoteNum(4);
				Combine[5].setNoteNum(5);
				Combine[6].setNoteNum(6);
				Combine[7].setNoteNum(7);
				Combine[8].setNoteNum(8);
				Combine[9].setNoteNum(9);

				keyMap.put(KeyEvent.VK_SPACE,		Hit.setNoteName("HIT"));
				keyMap.put(KeyEvent.VK_BACK_SPACE,	Back.setNoteName("BACK"));
				
				
				line1[0].resizeImage(line1[0].getNoteName());
				line1[1].resizeImage(line1[1].getNoteName());
				line1[2].resizeImage(line1[2].getNoteName());
				line1[3].resizeImage(line1[3].getNoteName());
				line1[4].resizeImage(line1[4].getNoteName());
				line1[5].resizeImage(line1[5].getNoteName());
				line1[6].resizeImage(line1[6].getNoteName());
				line1[7].resizeImage(line1[7].getNoteName());
				line1[8].resizeImage(line1[8].getNoteName());
				line1[9].resizeImage(line1[9].getNoteName());

				line2[0].resizeImage(line2[0].getNoteName());
				line2[1].resizeImage(line2[1].getNoteName());
				line2[2].resizeImage(line2[2].getNoteName());
				line2[3].resizeImage(line2[3].getNoteName());
				line2[4].resizeImage(line2[4].getNoteName());
				line2[5].resizeImage(line2[5].getNoteName());
				line2[6].resizeImage(line2[6].getNoteName());
				line2[7].resizeImage(line2[7].getNoteName());
				line2[8].resizeImage(line2[8].getNoteName());
				line2[9].resizeImage(line2[9].getNoteName());

				line3[0].resizeImage(line3[0].getNoteName());
				line3[1].resizeImage(line3[1].getNoteName());
				line3[2].resizeImage(line3[2].getNoteName());
				line3[3].resizeImage(line3[3].getNoteName());
				line3[4].resizeImage(line3[4].getNoteName());
				line3[5].resizeImage(line3[5].getNoteName());
				line3[6].resizeImage(line3[6].getNoteName());
				line3[7].resizeImage(line3[7].getNoteName());
				line3[8].resizeImage(line3[8].getNoteName());
				line3[9].resizeImage(line3[9].getNoteName());

				line4[0].resizeImage(line4[0].getNoteName());
				line4[1].resizeImage(line4[1].getNoteName());
				line4[2].resizeImage(line4[2].getNoteName());
				line4[3].resizeImage(line4[3].getNoteName());
				line4[4].resizeImage(line4[4].getNoteName());
				line4[5].resizeImage(line4[5].getNoteName());
				line4[6].resizeImage(line4[6].getNoteName());
				line4[7].resizeImage(line4[7].getNoteName());
				line4[8].resizeImage(line4[8].getNoteName());
				line4[9].resizeImage(line4[9].getNoteName());


		//@preOff
		for(int i=0;i<N_Guitar*4;i++){
			if(i<10){
				
			}
		}
				
		
				
		addKeyListener(this);
		setFocusable(true);
	}                        

	@Override
	public void keyPressed(KeyEvent e) {
		Key key = keyMap.get(e.getKeyCode());
		if (key == null) return;

		int check = key.keyDown();
		if (check == 1) {
			switch(key.getNoteName()){
			
			case "HIT":
				hit=1;
				this.client = main.getClient();
				for(int i=0;i<ComNum;i++){
					client.sendMessage(ComTemp[i]);
				}
				break;
				
			case "C1":
				if(Cstat[0]==0){
					if(ComNum==0)
						break;
					for(int i=0;i<ComNum;i++)
						CO[0][i] = ComTemp[i];
					C1 = ComNum;
					Cstat[0] = 1;
				}
				else{
					for(int i=0;i<C1;i++)
						ComTemp[i] = CO[0][i];
					ComNum  = C1;
				}
				break;
			case "C2":
				if(Cstat[1]==0){
					if(ComNum==0)
						break;
					for(int i=0;i<ComNum;i++)
						CO[1][i]=ComTemp[i];
					C2 = ComNum;
					Cstat[1] = 1;
				}
				else{
					for(int i=0;i<C2;i++)
						ComTemp[i] = CO[1][i];
					ComNum  = C2;
				}
				break;
			case "C3":
				if(Cstat[2]==0){
					if(ComNum==0)
						break;
					for(int i=0;i<ComNum;i++)
						CO[2][i]=ComTemp[i];
					C3 = ComNum;
					Cstat[2]=1;
				}
				else{
					for(int i=0;i<C3;i++)
						ComTemp[i] = CO[2][i];
					ComNum  = C3;
				}
				break;
			case "C4":
				if(Cstat[3]==0){
					if(ComNum==0)
						break;
					for(int i=0;i<ComNum;i++)
						CO[3][i]=ComTemp[i];
					C4 = ComNum;
					Cstat[3]=1;
				}
				else{
					for(int i=0;i<C4;i++)
						ComTemp[i] = CO[3][i];
					ComNum  = C4;
				}
				break;
			case "C5":
				if(Cstat[4]==0){
					if(ComNum==0)
						break;
					for(int i=0;i<ComNum;i++)
						CO[4][i]=ComTemp[i];
					C5 = ComNum;
					Cstat[4]=1;
				}
				else{
					for(int i=0;i<C5;i++)
						ComTemp[i] = CO[4][i];
					ComNum  = C5;
				}
				break;
			case "C6":
				if(Cstat[5]==0){
					if(ComNum==0)
						break;
					for(int i=0;i<ComNum;i++)
						CO[5][i]=ComTemp[i];
					C6 = ComNum;
					Cstat[5]=1;
				}
				else{
					for(int i=0;i<C6;i++)
						ComTemp[i] = CO[5][i];
					ComNum  = C6;
				}
				break;
			case "C7":
				if(Cstat[6]==0){
					if(ComNum==0)
						break;
					for(int i=0;i<ComNum;i++)
						CO[6][i]=ComTemp[i];
					C7 = ComNum;
					Cstat[6]=1;
				}
				else{
					for(int i=0;i<C7;i++)
						ComTemp[i] = CO[6][i];
					ComNum  = C7;
				}
				break;
			case "C8":
				if(Cstat[7]==0){
					if(ComNum==0)
						break;
					for(int i=0;i<ComNum;i++)
						CO[7][i]=ComTemp[i];
					C8 = ComNum;
					Cstat[7]=1;
				}
				else{
					for(int i=0;i<C8;i++)
						ComTemp[i] = CO[7][i];
					ComNum  = C8;
				}
				break;
			case "C9":
				if(Cstat[8]==0){
					if(ComNum==0)
						break;
					for(int i=0;i<ComNum;i++)
						CO[8][i]=ComTemp[i];
					C9 = ComNum;
					Cstat[8]=1;
				}
				else{
					for(int i=0;i<C9;i++)
						ComTemp[i] = CO[8][i];
					ComNum  = C9;
				}
				break;
			case "BACK":
				for(int i=0;i<ComNum;i++){
						ComTemp[i] = null;
					}
					ComNum=0;
				break;
			default:
				if(hit==0){
					ComTemp[ComNum] = "GH_" + key.getNoteName();
					ComNum++;
					}
				
				else{
					for(int i=0;i<ComNum;i++){
						ComTemp[i] = null;
					}
					hit=0;
					ComNum=0;
					ComTemp[ComNum] = "GH_" + key.getNoteName();
					ComNum++;
				}
			}
		}
			
		//INSTRU,CODE
	}

	@Override
	public void keyReleased(KeyEvent e) {
		Key key = keyMap.get(e.getKeyCode());
		if (key == null) return;

		key.keyUp();
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	// Variables declaration - do not modify                     
	private javax.swing.JLabel jLabel1;
}
