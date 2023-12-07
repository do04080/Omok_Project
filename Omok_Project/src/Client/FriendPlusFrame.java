package Client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.awt.event.ActionEvent;

public class FriendPlusFrame extends JFrame {

	private JPanel contentPane;

	FriendPlusFrame(int i, Operator o, String nick, String roomname) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 542, 198);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("[" + nick + "]님이 친구 요청을 하셨습니다.");
		if (i == 0) {
			lblNewLabel.setText("[" + nick + "]님이 친구 요청을 하셨습니다.");
		} else if (i == 1) {
			lblNewLabel.setText("[" + nick + "]님이 [" + roomname + "]으로 초대를 보내셨습니다.");
		}
		lblNewLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(12, 25, 506, 51);
		contentPane.add(lblNewLabel);

		JButton btnNewButton = new JButton("수락");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (i == 0) {
					o.wr.sendServer("FRIEND//OKAY//" + nick + "//" + o.nick);
				}
				if (i == 1) {
					if (o.mystat.equals("WAIT")) {
						o.wr.sendServer("EROOM//" + roomname);
					}
					if (o.mystat.equals("OBJ") || o.mystat.equals("PLAYER")) {
						o.wr.sendServer("REXIT//" + o.mystat);
						o.wr.sendServer("EROOM//" + roomname);
					}
				}
				dispose();
			}
		});
		btnNewButton.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		btnNewButton.setBounds(54, 105, 163, 44);
		contentPane.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("거절");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				o.wr.sendServer("FRIEND//FAIL//" + nick);
				dispose();
			}
		});
		btnNewButton_1.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		btnNewButton_1.setBounds(308, 105, 163, 44);
		contentPane.add(btnNewButton_1);
	}
}
