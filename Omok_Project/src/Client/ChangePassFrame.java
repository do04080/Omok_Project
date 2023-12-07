package Client;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ChangePassFrame extends JFrame {

	private JPanel contentPane;
	JPasswordField pw;
	JPasswordField pwchk;
	int pck = 0;
	int passtotal = 0;
	private JLabel pwchkL_1;
	private JLabel pwchkL_2;
	private JButton btnNewButton;
	Operator o;

	public ChangePassFrame(Operator _o, String nick) {
		o = _o;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 591, 281);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		pw = new JPasswordField();
		pw.setToolTipText("a-z,A-Z,0-9,!-/ ,8~20자");
		pw.setBounds(132, 28, 296, 43);
		pw.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				// 텍스트가 삽입될 때 실행할 코드
				pwcheck();
				pwneed();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				// 텍스트가 삭제될 때 실행할 코드
				pwcheck();
				pwneed();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub

			}
		});
		pwchk = new JPasswordField();
		pwchk.setBounds(132, 103, 296, 43);
		pwchk.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				// 텍스트가 삽입될 때 실행할 코드
				pwcheck();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				// 텍스트가 삭제될 때 실행할 코드
				pwcheck();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
			}
		});
		contentPane.add(pw);
		contentPane.add(pwchk);

		JLabel pwdL = new JLabel("비밀번호");
		pwdL.setHorizontalAlignment(SwingConstants.CENTER);
		pwdL.setFont(new Font("굴림", Font.BOLD, 17));
		pwdL.setBounds(26, 27, 72, 43);
		contentPane.add(pwdL);

		JLabel pwchkL = new JLabel("비밀번호 확인");
		pwchkL.setForeground(new Color(0, 0, 0));
		pwchkL.setHorizontalAlignment(SwingConstants.CENTER);
		pwchkL.setFont(new Font("굴림", Font.BOLD, 17));
		pwchkL.setBounds(12, 102, 117, 43);
		contentPane.add(pwchkL);

		pwchkL_1 = new JLabel("20자까지 사용가능");
		pwchkL_1.setVisible(false);
		pwchkL_1.setHorizontalAlignment(SwingConstants.CENTER);
		pwchkL_1.setFont(new Font("바탕체", Font.BOLD, 15));
		pwchkL_1.setBounds(132, 149, 296, 43);
		contentPane.add(pwchkL_1);

		pwchkL_2 = new JLabel("0~9,특수문자,대문자,소문자 구분해 작성해주세요");

		pwchkL_2.setVisible(false);
		pwchkL_2.setHorizontalAlignment(SwingConstants.CENTER);
		pwchkL_2.setForeground(Color.BLACK);
		pwchkL_2.setFont(new Font("바탕체", Font.BOLD, 15));
		pwchkL_2.setBounds(74, 65, 400, 43);
		contentPane.add(pwchkL_2);

		JLabel lblNewLabel_4 = new JLabel("20자까지 사용가능");
		lblNewLabel_4.setFont(new Font("굴림", Font.BOLD, 15));
		lblNewLabel_4.setBounds(440, 27, 138, 42);
		contentPane.add(lblNewLabel_4);

		setContentPane(contentPane);
		contentPane.setLayout(null);

		btnNewButton = new JButton("New button");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changepass(nick);
			}
		});
		btnNewButton.setBounds(203, 202, 125, 32);
		contentPane.add(btnNewButton);
	}

	public void pwneed() {

		String upass = "";
		boolean hasUppercase = false;
		boolean hasLowercase = false;
		int hasSpecialChar = 0;
		int passlength = 0;

		for (int i = 0; i < pw.getPassword().length; i++) {
			upass = upass + pw.getPassword()[i];
		}
		for (char c : upass.toCharArray()) {
			if (Character.isUpperCase(c)) {
				hasUppercase = true;
			} else if (Character.isLowerCase(c)) {
				hasLowercase = true;
			} else if ("!@#$%^&*()_+-=[]{};':\",.<>?/".contains(String.valueOf(c))) {
				hasSpecialChar = 1;
			}
		}
		if (upass.length() >= 8 && upass.length() <= 12) {
			passlength = 1;
		} else if (upass.length() >= 12) {
			passlength = 2;
		}
		int uplowcase = 0;
		if (hasUppercase && hasLowercase) {
			uplowcase = 1;
		} else {
			uplowcase = 0;
		}
		passtotal = passlength + hasSpecialChar + uplowcase;
		if (upass != "") {
			pwchkL_2.setVisible(true);
			pwSafeImg(passtotal);
		} else {
			pwchkL_2.setVisible(false);

		}
	}

	public void pwcheck() {
		String upass = "";
		String upasschk = "";

		for (int i = 0; i < pw.getPassword().length; i++) {
			upass = upass + pw.getPassword()[i];
		}
		for (int i = 0; i < pwchk.getPassword().length; i++) {
			upasschk = upasschk + pwchk.getPassword()[i];
		}
		System.out.println(upass + "|" + upasschk);
		if (upasschk != "") {
			pwchkL_1.setVisible(true);
			if (upass.equals(upasschk)) {
				pwchkL_1.setText("비밀번호가 일치합니다");
				pwchkL_1.setForeground(Color.GREEN);
				pck = 1;
			} else {
				pwchkL_1.setText("비밀번호가 일치하지 않습니다");
				pwchkL_1.setForeground(Color.RED);
				pck = 0;
			}
		} else if (upasschk == "") {
			pwchkL_1.setVisible(false);
			pck = 0;
		}
	}

	public void pwSafeImg(int i) {
		ImageIcon imageIcon = new ImageIcon("C:\\Users\\도석환\\eclipse-workspace\\Omok_Project\\safety\\" + i + ".png");
		Image image = imageIcon.getImage().getScaledInstance(pwchkL_2.getWidth(), pwchkL_2.getHeight(),
				Image.SCALE_SMOOTH);
		pwchkL_2.setIcon(new ImageIcon(image));
	}

	void changepass(String msg) {
		String[] m = msg.split("@@");
		String nick = m[0];
		String pass = m[1];
		String upass = "";
		for (int i = 0; i < pw.getPassword().length; i++) {
			upass = upass + pw.getPassword()[i];
		}
		if (upass.equals("")) {
			JOptionPane.showMessageDialog(null, "비밀번호를 입력해주세요", "비밀번호 변경 실패", JOptionPane.ERROR_MESSAGE);
		} else if (pck != 1) {
			JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다", "비밀번호 변경 실패", JOptionPane.ERROR_MESSAGE);
		} else if (passtotal < 2) {
			JOptionPane.showMessageDialog(null, "사용할 수 없는 비밀번호입니다", "비밀번호 변경 실패", JOptionPane.ERROR_MESSAGE);
		} else if (pass.equals(upass)) {
			JOptionPane.showMessageDialog(null, "이전에 사용한 비밀번호 입니다.", "비밀번호 변경 실패", JOptionPane.ERROR_MESSAGE);
		} else {
			o.sendServer("PASSWORD//" + nick + "//" + upass);
		}

	}
}
