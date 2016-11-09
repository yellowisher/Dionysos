package RoomScreen.Layout;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Logo extends JPanel{
	private ImageIcon logoImg;
	JPanel parent;
	public Logo(JPanel parent){
		this.parent = parent;
		init();
	}
	
	private void init(){
		this.setOpaque(false);
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(10, 10, 10, 10));
		Dimension ds = parent.getPreferredSize();
		int parentWidth = (int)ds.getWidth();
		int parentHeight = (int)ds.getHeight();
		
		setPreferredSize(new Dimension(parentWidth,parentHeight));
		
		//load image and resize image.
		ImageIcon logoImg = new ImageIcon("Resource/Image/Logo.png");
		Image image = logoImg.getImage();
	
		Image newImg = image.getScaledInstance(parentWidth-20, 
				logoImg.getIconHeight() * parentWidth/logoImg.getIconWidth()-20, 
				java.awt.Image.SCALE_SMOOTH);
		
		JLabel pImg = new JLabel(new ImageIcon(newImg));

		//pImg.setBounds(0, 0, parentWidth, parentHeight);
		this.add(pImg,BorderLayout.CENTER);
	}
}
