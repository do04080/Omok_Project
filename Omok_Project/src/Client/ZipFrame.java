package Client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JSeparator;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.Font;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;

public class ZipFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JDialog waitingDialog;
	Operator o = null;
	Socket sock;
	DataOutputStream dos;
	DataInputStream dis;
	DefaultListModel<String> listModel = null;
	JList<String> list;

	public ZipFrame(Operator _o, Socket socket) {

		o = _o;
		sock = socket;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 597, 556);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		listModel = new DefaultListModel<>();
		for (String zip : o.zipList) {
			listModel.addElement(zip);
		}
		list = new JList<String>(listModel);
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int selectedIndex = list.getSelectedIndex();
					if (selectedIndex >= 0) {
						String selectedValue = listModel.getElementAt(selectedIndex);
						if (o.zipinst == 0) {
							o.jf.zipAppend(selectedValue);
						} else if (o.zipinst == 1) {
							o.ic.zipAppend(selectedValue);
						} else if (o.zipinst == 2) {
							o.opf.zipAppend(selectedValue);
						}
						dispose();
					}
				}
			}
		});

		JScrollPane listScroll = new JScrollPane(list);
		listScroll.setBounds(43, 73, 495, 436);
		contentPane.add(listScroll);

		textField = new JTextField();
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendZip();
			}
		});
		textField.setToolTipText("도로명 주소를 입력해주세요");
		textField.setBounds(43, 24, 398, 39);
		contentPane.add(textField);
		textField.setColumns(10);

		JButton btnNewButton = new JButton("찾기");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendZip();
			}
		});
		btnNewButton.setFont(new Font("굴림", Font.BOLD, 15));
		btnNewButton.setBounds(453, 24, 85, 39);
		contentPane.add(btnNewButton);
		setVisible(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}

	void zipFail() {
		System.out.println("우편번호 조회 실패");
		JOptionPane.showMessageDialog(null, "우편번호 조회에 실패하였습니다");
		closeWaitingDialog(); // zipFail() 메서드에서 waitingDialog를 닫음
	}

	void sendZip() {
		String message = textField.getText();
		if (!message.isEmpty()) {
			try {
				dos = new DataOutputStream(sock.getOutputStream());

				// 서버로 로그인 요청 메시지 전송
				String msg = ("ZIP//" + message);
				System.out.println("문자 보냄");
				dos.writeUTF(msg);
				dos.flush();
				showWaitingDialog();

			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				zipFail(); // IOException이 발생하면 zipFail() 호출하여 waitingDialog를 닫음
			}
		}
	}

	private void showWaitingDialog() {
		waitingDialog = new JDialog(this, "잠시만 기다려주세요", true);
		JLabel waitingLabel = new JLabel("서버 응답을 기다리는 중...");
		waitingDialog.add(waitingLabel);
		waitingDialog.setSize(200, 100);
		waitingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		waitingDialog.setLocationRelativeTo(this);
		SwingUtilities.invokeLater(() -> waitingDialog.setVisible(true));
	}

	private void closeWaitingDialog() {
		if (waitingDialog != null && waitingDialog.isShowing()) {
			waitingDialog.dispose();
		}
	}

	public void updateZipList(String[] newZipList) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				listModel.clear(); // 목록에서 기존 항목 지우기
				if (waitingDialog != null) {
					waitingDialog.dispose();
				}
				for (String zip : newZipList) {
					listModel.addElement(zip); // 새로운 ZIP 코드를 목록에 추가하기
				}
			}
		});
	}
}
