package Client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;

public class RoomCreateFrame extends JFrame {

	private JPanel contentPane;
	private JTextField roomname;

	Operator o = null;
	Socket sock;

	OutputStream os;
	DataOutputStream dos;

	String[] roomnames;

	public RoomCreateFrame(Operator opt, Socket socket) {

		o = opt;
		sock = socket;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 457, 175);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		roomname = new JTextField();
		roomname.setBounds(162, 10, 258, 45);
		contentPane.add(roomname);

		JLabel lblNewLabel = new JLabel("방 이름");
		lblNewLabel.setBounds(45, 17, 105, 30);
		contentPane.add(lblNewLabel);

		JButton btnNewButton = new JButton("생성하기");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				String rm = roomname.getText();
				if (rm.equals("")) {
					JOptionPane.showMessageDialog(null, "방 제목을 입력해주세요", "방 생성 실패", JOptionPane.ERROR_MESSAGE);
					System.out.println("방생성 실패 > 방 제목 미입력");
				} else if (rm != null) {
					if (roomCheck(rm)) {
						try {
							dos = new DataOutputStream(sock.getOutputStream());
							// 서버로 로그인 요청 메시지 전송
							String msg = ("CROOM//" + rm);
							dos.writeUTF(msg);
							dos.flush();
						}

						catch (UnknownHostException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if (!roomCheck(rm)) {
						JOptionPane.showMessageDialog(null, "방 제목이 중복되었습니다", "방생성 실패", JOptionPane.ERROR_MESSAGE);
						System.out.println("방생성 실패 > 방 제목 중복");
					}
				}

			}
		});
		btnNewButton.setBounds(45, 84, 150, 44);
		contentPane.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("그만두기");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_1.setBounds(233, 83, 150, 45);
		contentPane.add(btnNewButton_1);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public boolean roomCheck(String rname) {
		String[] rns = o.roomList;
		if (rns != null) {
			for (int i = 0; i < rns.length; i++) {
				String[] rn = rns[i].split(":");
				if (rn[0].equals(rname)) {
					return false;
				}
			}
		}
		return true;
	}
}
