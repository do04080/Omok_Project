package Client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JList;
import java.awt.Font;
import java.awt.Image;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.awt.event.ActionEvent;
import javax.swing.JSplitPane;

public class InfoChangeFrame extends JFrame {

	private JPanel contentPane;
	private JFileChooser chfileChooser;
	private File file;
	JList<String> infoList;
	JLabel profileLabel;
	DefaultListModel<String> infoListModel = null;
	Operator o = null;
	Socket sock;
	DataOutputStream dos;
	DataInputStream dis;
	JList<String> list;
	String newValue;

	public InfoChangeFrame(Operator _o, Socket socket) {
		o = _o;
		sock = socket;

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 596, 366);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		infoList = new JList();
		infoList.setFont(new Font("굴림", Font.PLAIN, 14));
		infoListModel = new DefaultListModel<>();
		for (String info : o.userinfo) {
			infoListModel.addElement(info);
		}

		JButton changeBtn = new JButton("비밀번호 변경");
		changeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				o.wr.sendServer("CPASSWORD//" + o.nick);
			}
		});

		changeBtn.setFont(new Font("굴림", Font.PLAIN, 15));
		changeBtn.setBounds(35, 264, 221, 43);
		contentPane.add(changeBtn);

		profileLabel = new JLabel("New label");
		profileLabel.setBounds(25, 25, 236, 229);
		ImageIcon imageIcon = new ImageIcon(o.myimage);
		Image img = imageIcon.getImage().getScaledInstance(profileLabel.getWidth(), profileLabel.getHeight(),
				Image.SCALE_SMOOTH);
		profileLabel.setIcon(new ImageIcon(img));
		contentPane.add(profileLabel);

		chfileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("이미지 파일", "jpg", "jpeg", "png", "gif", "bmp");
		chfileChooser.setFileFilter(filter);

		JButton profileChange = new JButton("프로필 이미지 변경");
		profileChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = chfileChooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					file = chfileChooser.getSelectedFile();
					displayImage(file);
					long fileSize = file.length(); // 파일 크기
					String fileName = file.getName();
					String ext = fileName.substring(fileName.lastIndexOf("."));
					o.wr.sendServer("CHANGE//" + o.nick + "//img//" + o.nick + ext + "//" + fileSize);
					try {
						dos = new DataOutputStream(sock.getOutputStream());
						// 이미지 데이터 전송
						FileInputStream fileInputStream = new FileInputStream(file);
						byte[] buffer = new byte[4096];
						int bytesRead;
						while ((bytesRead = fileInputStream.read(buffer)) != -1) {
							dos.write(buffer, 0, bytesRead);
						}
						fileInputStream.close();
					} catch (IOException a) {
						a.printStackTrace();

					}
				}
			}
		});
		profileChange.setFont(new Font("굴림", Font.PLAIN, 15));
		profileChange.setBounds(337, 128, 221, 43);
		contentPane.add(profileChange);

		JButton cancel = new JButton("창 닫기");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		cancel.setFont(new Font("굴림", Font.PLAIN, 18));
		cancel.setBounds(337, 264, 221, 43);
		contentPane.add(cancel);
	}

	public void updateInfoList(String[] newInfoList) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				infoListModel.clear(); // 목록에서 기존 항목 지우기

				for (String info : newInfoList) {
					infoListModel.addElement(info); // 새로운 ZIP 코드를 목록에 추가하기
				}
			}
		});
	}

	public static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?"); // 정수 및 부동 소수점 숫자 체크
	}

	

	private void displayImage(File file) {
		if (file != null) {
			ImageIcon imageIcon = new ImageIcon(file.getAbsolutePath());
			Image image = imageIcon.getImage().getScaledInstance(profileLabel.getWidth(), profileLabel.getHeight(),
					Image.SCALE_SMOOTH);
			profileLabel.setIcon(new ImageIcon(image));
		}
	}
}
