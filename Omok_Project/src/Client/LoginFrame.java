package Client;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.io.OutputStream;

import java.net.*;

public class LoginFrame extends JFrame {
	JPanel basePanel = new JPanel(null);

	JLabel idL = new JLabel("아이디");
	JLabel pwL = new JLabel("비밀번호");

	JTextField id = new JTextField();
	JPasswordField pw = new JPasswordField();

	JButton loginBtn = new JButton("로그인");
	JButton joinBtn = new JButton("회원가입");
	JButton exitBtn = new JButton("프로그램 종료");

	Operator o = null;
	Socket sock;

	OutputStream os;
	DataOutputStream dos;
	InputStream is;
	DataInputStream dis;
	final String okayTag = "OKAY";
	int read = 0;
	JFrame frame;

	LoginFrame(Operator _o, Socket socket) {
		o = _o;
		sock = socket;
		setTitle("로그인");

		idL.setBounds(40, 158, 50, 30);
		pwL.setBounds(40, 198, 50, 30);

		id.setBounds(102, 159, 166, 30);
		pw.setBounds(102, 199, 166, 30);

		loginBtn.setBounds(301, 158, 75, 63);
		joinBtn.setBounds(52, 285, 135, 25);
		exitBtn.setBounds(241, 285, 135, 25);

		setContentPane(basePanel);

		basePanel.add(idL);
		basePanel.add(id);
		basePanel.add(pwL);
		basePanel.add(pw);

		basePanel.add(loginBtn);
		basePanel.add(joinBtn);
		basePanel.add(exitBtn);

		JButton imageButton = new JButton();
		imageButton.setBounds(367, 10, 42, 41);
		ImageIcon imageIcon = new ImageIcon("C:\\Users\\도석환\\eclipse-workspace\\Omok_Project\\다운로드 (1).png");
		Image image = imageIcon.getImage().getScaledInstance(imageButton.getWidth(), imageButton.getHeight(),
				Image.SCALE_SMOOTH);
		imageButton.setIcon(new ImageIcon(image));

		imageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				optFrame();
			}
		});
		basePanel.add(imageButton);

		basePanel.setPreferredSize(new Dimension(317, 219));

		JButton idsearchbtn = new JButton("");
		idsearchbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				o.sif = new SearchIdFrame(o, 0);
				o.sif.setVisible(true);
			}
		});
		idsearchbtn.setBounds(159, 239, 81, 16);
		ImageIcon isbtn = new ImageIcon("C:\\Users\\도석환\\eclipse-workspace\\Omok_Project\\idsearch.png");
		Image isbtnimage = isbtn.getImage().getScaledInstance(idsearchbtn.getWidth(), idsearchbtn.getHeight(),
				Image.SCALE_SMOOTH);
		idsearchbtn.setIcon(new ImageIcon(isbtnimage));
		basePanel.add(idsearchbtn);

		ButtonListener bl = new ButtonListener();
		loginBtn.addActionListener(bl);
		exitBtn.addActionListener(bl);
		joinBtn.addActionListener(bl);

		pw.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendlogin();
			}
		});
		JButton pwsearchbtn = new JButton("");
		pwsearchbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				o.sif = new SearchIdFrame(o, 1);
				o.sif.setVisible(true);
			}
		});
		pwsearchbtn.setBounds(241, 239, 81, 16);
		ImageIcon pwbtn = new ImageIcon("C:\\Users\\도석환\\eclipse-workspace\\Omok_Project\\pwsearch.png");
		Image pwbtnimage = pwbtn.getImage().getScaledInstance(pwsearchbtn.getWidth(), pwsearchbtn.getHeight(),
				Image.SCALE_SMOOTH);
		pwsearchbtn.setIcon(new ImageIcon(pwbtnimage));
		basePanel.add(pwsearchbtn);

		JLabel background = new JLabel();
		background.setBounds(0, 0, 435, 545);
		basePanel.add(background);

		// Load the image and set it as the icon for the background JLabel
		ImageIcon backgroundImage = new ImageIcon("C:\\Users\\도석환\\eclipse-workspace\\Omok_Project\\login.png");
		Image bimage = backgroundImage.getImage().getScaledInstance(background.getWidth(), background.getHeight(),
				Image.SCALE_SMOOTH);
		background.setIcon(new ImageIcon(bimage));

		setSize(435, 367);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	class ButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			JButton b = (JButton) a.getSource();

			String uid = id.getText();
			String upass = new String(pw.getPassword());

			if (b.getText().equals("프로그램 종료")) {
				System.out.println("프로그램 종료");
				System.exit(0);
			} else if (b.getText().equals("회원가입")) {
				o.jf = new JoinFrame(o, sock);
				o.jf.setVisible(true);
			} else if (b.getText().equals("로그인")) {
				sendlogin();
			}
		}
	}

	public void sendlogin() {
		String uid = id.getText();
		String upass = new String(pw.getPassword());

		if (uid.equals("") || upass.equals("")) {
			JOptionPane.showMessageDialog(null, "아이디와 비밀번호 모두 입력해주세요", "로그인 실패", JOptionPane.ERROR_MESSAGE);
			System.out.println("로그인 실패 > 로그인 정보 미입력");
		} else {
			try {
				dos = new DataOutputStream(sock.getOutputStream());
				String msg = ("LOGIN//" + uid + "//" + upass);
				dos.writeUTF(msg);
				dos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void optFrame() {
		frame = new JFrame("관리자 로그인");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(300, 150);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

		JLabel label = new JLabel("관리자 암호를 입력하세요");
		JTextField textField = new JTextField(20);
		JButton submitButton = new JButton("확인");
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				String password = textField.getText().trim();
				if (password.equals("Admin")) {
					try {
						dos = new DataOutputStream(sock.getOutputStream());
						String msg = ("OPT//");
						dos.writeUTF(msg);
						dos.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null, "암호가 틀렸습니다");
					frame.dispose();
				}
			}
		});
		frame.getContentPane().add(label);
		frame.getContentPane().add(textField);
		frame.getContentPane().add(submitButton);

		frame.setVisible(true);
	}

	public void loginSign(boolean okay) {
		if (okay) {
			System.out.println("로그인 성공");
			JOptionPane.showMessageDialog(null, "로그인에 성공하였습니다");
			dispose();
		} else {
			JOptionPane.showMessageDialog(null, "아이디와 비밀번호를 확인해주세요", "로그인 실패", JOptionPane.ERROR_MESSAGE);
			System.out.println("로그인 실패 > 로그인 정보 불일치");
		}
	}
}
