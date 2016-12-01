package LobbyClient;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.EtchedBorder;

import RoomInfo.RoomInfo;

public class ClientRoomRenderer extends JPanel implements ListCellRenderer<RoomInfo> {
	static ImageIcon lockIcon = new ImageIcon("Resource/Image/Icon_Lock.png");
	static ImageIcon emptyIcon = new ImageIcon("Resource/Image/Icon_Empty.png");
	static Font font = new Font("Arial Unicode MS", Font.BOLD, 22);

	JLabel roomName = new JLabel();
	JLabel numUser = new JLabel();

	public ClientRoomRenderer() {
		setLayout(new BorderLayout());
		setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		add(roomName, BorderLayout.WEST);
		add(numUser, BorderLayout.EAST);

		roomName.setFont(font);
		numUser.setFont(font);

		setOpaque(true);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends RoomInfo> list, RoomInfo roomInfo, int index, boolean isSelected, boolean cellHasFocus) {
		if (roomInfo.password.equals("")) roomName.setIcon(emptyIcon);
		else roomName.setIcon(lockIcon);

		roomName.setText(roomInfo.roomName);
		numUser.setText(roomInfo.numUser + "/3 ");

		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		}
		else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		return this;
	}
}