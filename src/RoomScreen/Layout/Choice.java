package RoomScreen.Layout;

import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Choice extends JPanel {
	JPanel parent;
	public Choice(JPanel parent) {
		this.parent = parent;
		init();
		initComponents();
		setImage();
	}

	private void init() {
	}

	private void setImage() {
		ImageIcon icon;
		Image image, newImg;
		icon = new ImageIcon("Resource/Image/piano.jpg");
		image = icon.getImage();
		newImg = image.getScaledInstance(120, 120, java.awt.Image.SCALE_SMOOTH);
		pianoBtn.setIcon(new ImageIcon(newImg));
		pianoBtn.setContentAreaFilled(false);

		icon = new ImageIcon("Resource/Image/guitar.jpg");
		image = icon.getImage();
		newImg = image.getScaledInstance(120, 120, java.awt.Image.SCALE_SMOOTH);
		guitarBtn.setIcon(new ImageIcon(newImg));
		guitarBtn.setContentAreaFilled(false);

		icon = new ImageIcon("Resource/Image/drum.jpg");
		image = icon.getImage();
		newImg = image.getScaledInstance(120, 120, java.awt.Image.SCALE_SMOOTH);
		drumBtn.setIcon(new ImageIcon(newImg));
		drumBtn.setContentAreaFilled(false);
	}

	private void initComponents() {
		jPanel2 = new javax.swing.JPanel();
		jLabel4 = new javax.swing.JLabel();
		pianoBtn = new javax.swing.JButton();
		guitarBtn = new javax.swing.JButton();
		drumBtn = new javax.swing.JButton();

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 100, Short.MAX_VALUE));
		jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 100, Short.MAX_VALUE));

		jLabel4.setBackground(new java.awt.Color(0, 153, 153));
		jLabel4.setText("jLabel1");

		setBackground(new java.awt.Color(255, 204, 102));
		setPreferredSize(new java.awt.Dimension(621, 150));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGap(18, 18, 18)
						.addComponent(pianoBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(guitarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(drumBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(177, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup().addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
								.addComponent(drumBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
								.addComponent(guitarBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(pianoBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addContainerGap()));
	}
	public JButton getPianoBtn() {
		return pianoBtn;
	}
	public JButton getGuitarBtn() {
		return guitarBtn;
	}
	public JButton getDrumBtn() {
		return drumBtn;
	}

	// Variables declaration - do not modify                     
	private javax.swing.JButton drumBtn;
	private javax.swing.JButton guitarBtn;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JButton pianoBtn;
	// End of variables declaration       
}
