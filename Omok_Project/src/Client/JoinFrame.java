package Client;

import java.awt.EventQueue;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DeflaterOutputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;

import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;

public class JoinFrame extends JFrame {

	private JPanel contentPane;

	Operator o = null;
	int ick = 0;
	int nck = 0;
	int pck = 0;
	int pnn = 0;
	Socket sock;

	OutputStream os;
	DataOutputStream dos;
	InputStream is;
	DataInputStream dis;
	String zipno = "";
	int passtotal;
	String zipname;
	private JTextField name;
	private JTextField id;
	private JPasswordField pw;
	private JPasswordField pwchk;
	private JTextField nnm;
	private JTextArea zip;
	private JTextField address;
	private JTextField email;
	private JButton imagesch;
	private JLabel pwchkL_1;
	private JLabel pwchkL_2;
	private JLabel imageL;
	private JLabel backgroundLabel;
	private JFileChooser fileChooser;
	private File file;
	JComboBox<String> daysComboBox;
	JComboBox<String> monthsComboBox;
	JComboBox<String> yearComboBox;
	boolean passhide = true;
	String sex = "남";
	JTextArea zip_1;
	private JTextField email2;

	JoinFrame(Operator _o, Socket socket) {

		o = _o;
		sock = socket;
		setTitle("회원가입");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 761, 857);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		backgroundLabel = new JLabel();
		backgroundLabel.setBounds(0, 0, 761, 857);

		// Set the background image
		ImageIcon backgroundImageIcon = new ImageIcon(
				"C:\\Users\\도석환\\eclipse-workspace\\Omok_Project\\img\\KakaoTalk_20231120_184002953.jpg");
		Image backgroundImage = backgroundImageIcon.getImage().getScaledInstance(761, 857, Image.SCALE_SMOOTH);
		backgroundLabel.setIcon(new ImageIcon(backgroundImage));

		name = new JTextField();
		name.setBounds(117, 31, 265, 43);
		contentPane.add(name);
		name.setColumns(10);

		id = new JTextField();
		id.setColumns(10);
		id.setBounds(117, 103, 265, 43);
		contentPane.add(id);

		JButton btnNewButton = new JButton("");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (passhide) {
					pw.setEchoChar((char) 0);
					pwchk.setEchoChar((char) 0);
					ImageIcon imageIcon = new ImageIcon(
							"C:\\Users\\도석환\\eclipse-workspace\\Omok_Project\\eyeclose.png");
					Image image = imageIcon.getImage().getScaledInstance(btnNewButton.getWidth(),
							btnNewButton.getHeight(), Image.SCALE_SMOOTH);
					btnNewButton.setIcon(new ImageIcon(image));
					passhide = !passhide;
				} else {
					pw.setEchoChar('*');
					pwchk.setEchoChar('*');
					ImageIcon imageIcon = new ImageIcon("C:\\Users\\도석환\\eclipse-workspace\\Omok_Project\\eyeopen.png");
					Image image = imageIcon.getImage().getScaledInstance(btnNewButton.getWidth(),
							btnNewButton.getHeight(), Image.SCALE_SMOOTH);
					btnNewButton.setIcon(new ImageIcon(image));
					passhide = !passhide;
				}
			}
		});
		btnNewButton.setBounds(339, 179, 43, 43);
		ImageIcon imageIconpw = new ImageIcon("C:\\Users\\도석환\\eclipse-workspace\\Omok_Project\\eyeopen.png");
		Image imagepw = imageIconpw.getImage().getScaledInstance(btnNewButton.getWidth(), btnNewButton.getHeight(),
				Image.SCALE_SMOOTH);
		btnNewButton.setIcon(new ImageIcon(imagepw));
		contentPane.add(btnNewButton);

		pw = new JPasswordField();
		pw.setToolTipText("a-z,A-Z,0-9,!-/ ,8~20자");
		pw.setBounds(117, 179, 225, 43);
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
		contentPane.add(pw);

		pwchk = new JPasswordField();
		pwchk.setBounds(117, 251, 265, 43);
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
		contentPane.add(pwchk);

		JButton idchk = new JButton("중복확인");
		idchk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				String att = "id";
				String uid = id.getText();
				if (uid.equals("")) {
					JOptionPane.showMessageDialog(null, "아이디를 입력해주세요", "중복확인 실패", JOptionPane.ERROR_MESSAGE);
					System.out.println("중복확인 실패 > 중복확인 정보 미입력");
				} else if (uid != null) {
					overCheck(att, uid);
				}
			}
		});
		idchk.setBounds(388, 104, 95, 40);
		contentPane.add(idchk);

		JButton nnmchk = new JButton("중복확인");
		nnmchk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				String att = "nickname";
				String unnm = nnm.getText();
				if (unnm.equals("")) {
					JOptionPane.showMessageDialog(null, "닉네임을 입력해주세요", "중복확인 실패", JOptionPane.ERROR_MESSAGE);
					System.out.println("중복확인 실패 > 중복확인 정보 미입력");
				} else if (unnm != null) {
					overCheck(att, unnm);
				}
			}
		});
		nnmchk.setBounds(388, 328, 95, 43);
		contentPane.add(nnmchk);

		fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("이미지 파일", "jpg", "jpeg", "png", "gif", "bmp");
		fileChooser.setFileFilter(filter);

		nnm = new JTextField();
		nnm.setColumns(10);
		nnm.setBounds(117, 328, 265, 43);
		contentPane.add(nnm);

		JScrollPane zipScroll = new JScrollPane(zip);
		zipScroll.setBounds(117, 389, 144, 55);
		contentPane.add(zipScroll);

		zip = new JTextArea();
		zipScroll.setViewportView(zip);
		zip.setEditable(false);
		zip.setColumns(10);

		JButton zipsearch = new JButton("우편번호 찾기");
		zipsearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				o.zipinst = 0;
				o.zf.setVisible(true);
			}
		});
		zipsearch.setBounds(273, 401, 109, 43);
		contentPane.add(zipsearch);

		address = new JTextField();
		address.setColumns(10);
		address.setBounds(117, 529, 265, 43);
		contentPane.add(address);

		email = new JTextField();
		email.setToolTipText("aaa@aaa.aaa");
		email.setColumns(10);
		email.setBounds(117, 596, 225, 43);
		contentPane.add(email);

		imagesch = new JButton("이미지 업로드");
		imagesch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileChooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					file = fileChooser.getSelectedFile();
					displayImage(file);
				}
			}
		});
		imagesch.setBounds(571, 381, 109, 43);
		contentPane.add(imagesch);

		JLabel nameL = new JLabel("이름");
		nameL.setHorizontalAlignment(SwingConstants.CENTER);
		nameL.setFont(new Font("굴림", Font.BOLD, 17));
		nameL.setBounds(33, 30, 72, 43);
		contentPane.add(nameL);

		JLabel idL = new JLabel("아이디");
		idL.setHorizontalAlignment(SwingConstants.CENTER);
		idL.setFont(new Font("굴림", Font.BOLD, 17));
		idL.setBounds(33, 102, 72, 43);
		contentPane.add(idL);

		JLabel pwdL = new JLabel("비밀번호");
		pwdL.setHorizontalAlignment(SwingConstants.CENTER);
		pwdL.setFont(new Font("굴림", Font.BOLD, 17));
		pwdL.setBounds(33, 178, 72, 43);
		contentPane.add(pwdL);

		JLabel pwchkL = new JLabel("비밀번호 확인");
		pwchkL.setForeground(new Color(0, 0, 0));
		pwchkL.setHorizontalAlignment(SwingConstants.CENTER);
		pwchkL.setFont(new Font("굴림", Font.BOLD, 17));
		pwchkL.setBounds(0, 250, 117, 43);
		contentPane.add(pwchkL);

		JLabel nnmL = new JLabel("닉네임");
		nnmL.setHorizontalAlignment(SwingConstants.CENTER);
		nnmL.setFont(new Font("굴림", Font.BOLD, 17));
		nnmL.setBounds(33, 328, 72, 43);
		contentPane.add(nnmL);

		JLabel addL = new JLabel("우편번호");
		addL.setHorizontalAlignment(SwingConstants.CENTER);
		addL.setFont(new Font("굴림", Font.BOLD, 17));
		addL.setBounds(33, 401, 72, 43);
		contentPane.add(addL);

		JLabel add2L = new JLabel("상세주소");
		add2L.setHorizontalAlignment(SwingConstants.CENTER);
		add2L.setFont(new Font("굴림", Font.BOLD, 17));
		add2L.setBounds(33, 528, 72, 43);
		contentPane.add(add2L);

		JLabel emL = new JLabel("이메일");
		emL.setHorizontalAlignment(SwingConstants.CENTER);
		emL.setFont(new Font("굴림", Font.BOLD, 17));
		emL.setBounds(33, 595, 72, 43);
		contentPane.add(emL);

		imageL = new JLabel("이미지");

		imageL.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) { // 이미지 레이블을 한 번 클릭했을 때

					if (file != null) {
						showImageInNewWindow(file);
					}
				}
			}
		});
		imageL.setBackground(Color.WHITE);
		imageL.setHorizontalAlignment(SwingConstants.CENTER);
		imageL.setBounds(526, 30, 204, 315);
		ImageIcon imageIcon = new ImageIcon("C:\\Users\\도석환\\eclipse-workspace\\Omok_Project\\다운로드 (1).png");
		Image image = imageIcon.getImage().getScaledInstance(imageL.getWidth(), imageL.getHeight(), Image.SCALE_SMOOTH);
		imageL.setIcon(new ImageIcon(image));
		contentPane.add(imageL);

		JButton joinbtn = new JButton("가입하기");
		joinbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (file != null) {
						joinBtn();
					} else {
						System.out.println("회원가입 실패 > 프로필 등록 하지않음");
						JOptionPane.showMessageDialog(null, "프로필 사진을 등록 해주세요", "회원가입 실패", JOptionPane.ERROR_MESSAGE);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		joinbtn.setFont(new Font("굴림", Font.BOLD, 18));
		joinbtn.setBounds(445, 731, 109, 55);
		contentPane.add(joinbtn);

		JButton closebtn = new JButton("가입취소");
		closebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		closebtn.setFont(new Font("굴림", Font.BOLD, 18));
		closebtn.setBounds(597, 731, 109, 55);
		contentPane.add(closebtn);

		pwchkL_1 = new JLabel("20자까지 사용가능");
		pwchkL_1.setVisible(false);
		pwchkL_1.setHorizontalAlignment(SwingConstants.CENTER);
		pwchkL_1.setFont(new Font("바탕체", Font.BOLD, 15));
		pwchkL_1.setBounds(117, 291, 265, 43);
		contentPane.add(pwchkL_1);

		pwchkL_2 = new JLabel("0~9,특수문자,대문자,소문자 구분해 작성해주세요");

		pwchkL_2.setVisible(false);
		pwchkL_2.setHorizontalAlignment(SwingConstants.CENTER);
		pwchkL_2.setForeground(Color.BLACK);
		pwchkL_2.setFont(new Font("바탕체", Font.BOLD, 15));
		pwchkL_2.setBounds(83, 217, 400, 43);
		contentPane.add(pwchkL_2);

		zip_1 = new JTextArea();
		zip_1.setEditable(false);
		zip_1.setColumns(10);
		zip_1.setBounds(117, 454, 265, 53);
		contentPane.add(zip_1);

		JLabel addL_1 = new JLabel("주소");
		addL_1.setHorizontalAlignment(SwingConstants.CENTER);
		addL_1.setFont(new Font("굴림", Font.BOLD, 17));
		addL_1.setBounds(33, 459, 72, 43);
		contentPane.add(addL_1);

		String[] domainOptions = { "선택", "naver.com", "daum.com", "google.com", "직접입력" };
		JComboBox<String> domainComboBox = new JComboBox<>(domainOptions);
		domainComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String domain = (String) domainComboBox.getSelectedItem();
				if (domain.equals("직접입력")) {
					email2.setText("");
					email2.setEditable(true);
				} else {
					email2.setText("");
					email2.setEditable(false);
					if (!domain.equals("선택")) {
						email2.setText((String) domainComboBox.getSelectedItem());
					}
				}
			}

		});
		domainComboBox.setBounds(539, 595, 167, 45);
		contentPane.add(domainComboBox);

		JLabel lblNewLabel = new JLabel("@");
		lblNewLabel.setFont(new Font("굴림", Font.BOLD, 18));
		lblNewLabel.setBounds(354, 595, 57, 43);
		contentPane.add(lblNewLabel);

		JLabel emL_1 = new JLabel("생년월일");
		emL_1.setHorizontalAlignment(SwingConstants.CENTER);
		emL_1.setFont(new Font("굴림", Font.BOLD, 17));
		emL_1.setBounds(33, 659, 72, 43);
		contentPane.add(emL_1);

		int startYear = 1920;
		int endYear = Calendar.getInstance().get(Calendar.YEAR); // 현재 연도
		String[] yearOptions = new String[endYear - startYear + 2];
		yearOptions[0] = "선택"; // 선택 안 함 옵션 추가
		for (int i = startYear; i <= endYear; i++) {
			yearOptions[i - startYear + 1] = String.valueOf(i);
		}
		yearComboBox = new JComboBox<String>(yearOptions);
		yearComboBox.setBounds(117, 659, 83, 45);
		contentPane.add(yearComboBox);

		String[] monthOptions = new String[13];
		monthOptions[0] = "선택"; // 선택 안 함 옵션 추가
		for (int i = 1; i <= 12; i++) {
			monthOptions[i] = String.valueOf(i);
		}
		monthsComboBox = new JComboBox<String>(monthOptions);
		monthsComboBox.setBounds(248, 657, 83, 45);
		contentPane.add(monthsComboBox);

		daysComboBox = new JComboBox<>();
		daysComboBox.addItem("선택");
		daysComboBox.setBounds(379, 657, 83, 45);
		contentPane.add(daysComboBox);

		JLabel lblNewLabel_1 = new JLabel("년");
		lblNewLabel_1.setFont(new Font("굴림", Font.BOLD, 18));
		lblNewLabel_1.setBounds(212, 659, 57, 43);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("월");
		lblNewLabel_2.setFont(new Font("굴림", Font.BOLD, 18));
		lblNewLabel_2.setBounds(343, 659, 57, 43);
		contentPane.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("일");
		lblNewLabel_3.setFont(new Font("굴림", Font.BOLD, 18));
		lblNewLabel_3.setBounds(474, 659, 57, 43);
		contentPane.add(lblNewLabel_3);
		monthsComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateDaysComboBox(daysComboBox, (String) monthsComboBox.getSelectedItem());
			}
		});

		email2 = new JTextField();
		email2.setEditable(false);
		email2.setBounds(392, 598, 138, 40);
		contentPane.add(email2);
		email2.setColumns(10);

		JRadioButton brb = new JRadioButton("남");
		brb.setSelected(true);
		brb.setBounds(445, 470, 37, 23);
		contentPane.add(brb);

		JRadioButton wrb = new JRadioButton("여");
		wrb.setBounds(497, 470, 43, 23);
		contentPane.add(wrb);
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(brb);
		buttonGroup.add(wrb);
		brb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sex = "남";
			}
		});

		wrb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sex = "여";
			}
		});

		JLabel lblNewLabel_4 = new JLabel("20자까지 사용가능");
		lblNewLabel_4.setFont(new Font("굴림", Font.BOLD, 15));
		lblNewLabel_4.setBounds(386, 180, 138, 42);
		contentPane.add(lblNewLabel_4);

		JLabel addL_1_1 = new JLabel("성별");
		addL_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		addL_1_1.setFont(new Font("굴림", Font.BOLD, 17));
		addL_1_1.setBounds(379, 459, 72, 43);
		contentPane.add(addL_1_1);

		contentPane.add(backgroundLabel);
	}

	public void joinBtn() throws IOException {
		String uid = id.getText();
		String unm = name.getText();
		String unnm = nnm.getText();
		String uem = email.getText() + "@" + email2.getText();
		String upass = "";
		String uzip = zipno;
		String uadd = zip_1.getText() + "   " + address.getText();
		long fileSize = file.length(); // 파일 크기
		String fileName = file.getName();
		String ext = fileName.substring(fileName.lastIndexOf("."));
		String selectedYear = (String) yearComboBox.getSelectedItem();
		String selectedMonth = (String) monthsComboBox.getSelectedItem();
		String selectedDay = (String) daysComboBox.getSelectedItem();
		String birth = selectedYear + "년 " + selectedMonth + "월 " + selectedDay + "일";

		// 파일을 서버에 저장

		// 파일 내용을 읽고 인코딩합니다.

		for (int i = 0; i < pw.getPassword().length; i++) {
			upass = upass + pw.getPassword()[i];
		}
		if (uid.equals("") || upass.equals("") || uzip.equals("") || file == null) {
			JOptionPane.showMessageDialog(null, "모든 정보를 기입해주세요", "회원가입 실패", JOptionPane.ERROR_MESSAGE);
			System.out.println("회원가입 실패 > 회원정보 미입력");
		}

		else if (!uid.equals("") && !upass.equals("") && !uzip.equals("") && fileName != null) {

			if (ick == 1 && nck == 1 && pck == 1 && passtotal > 2) {
				try {
					dos = new DataOutputStream(sock.getOutputStream());
					// 서버로 회원가입 요청 메시지 전송
					String msg = ("JOIN//" + uid + "//" + upass + "//" + unm + "//" + unnm + "//" + sex + "//" + uzip
							+ "//" + uadd + "//" + uem + "//" + birth + "//" + fileSize + "//" + ext);
					dos.writeUTF(msg);
					dos.flush();
					// 이미지 파일 전송
					FileInputStream fileInputStream = new FileInputStream(file);
					byte[] buffer = new byte[4096];
					int bytesRead;

					while ((bytesRead = fileInputStream.read(buffer)) != -1) {
						dos.write(buffer, 0, bytesRead);
					}
					fileInputStream.close();

				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			} else if (ick != 1 || nck != 1) {
				System.out.println("회원가입 실패 > 중복확인 하지않음");
				JOptionPane.showMessageDialog(null, "중복확인을 해주세요", "회원가입 실패", JOptionPane.ERROR_MESSAGE);
			} else if (pck != 1) {
				System.out.println("회원가입 실패 > 비밀번호가 일치하지 않음");
				JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다", "회원가입 실패", JOptionPane.ERROR_MESSAGE);
			} else if (pnn <= 2) {
				System.out.println("회원가입 실패 > 사용할 수 없는 비밀번호");
				JOptionPane.showMessageDialog(null, "사용할 수 없는 비밀번호입니다", "회원가입 실패", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	public void overCheck(String att, String value) {
		try {
			o.overinst = 0;
			dos = new DataOutputStream(sock.getOutputStream());
			dos.writeUTF("OVER//" + att + "//" + value);
			dos.flush();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public void overSign(boolean sign, String att) {
		if (sign) {
			System.out.println("중복확인 성공");
			JOptionPane.showMessageDialog(null, "중복확인에 성공하였습니다");

			if (att.equals("nickname")) {
				nck = 1;
				nnm.setEditable(false);
			} else if (att.equals("id")) {
				id.setEditable(false);
				ick = 1;
			}
		} else {
			System.out.println("중복확인 실패");
			JOptionPane.showMessageDialog(null, "중복확인에 실패하였습니다");

		}
	}

	public void joinSign(boolean sign) {

		if (sign) {
			System.out.println("회원가입 성공");
			JOptionPane.showMessageDialog(null, "회원가입에 성공하였습니다");
			ick = 0;
			nck = 0;
			pck = 0;
			name.setText("");
			id.setText("");
			pw.setText("");
			pwchk.setText("");
			nnm.setText("");
			zip.setText("");
			address.setText("");
			email.setText("");
			dispose();
		} else {
			System.out.println("회원가입 실패");
			JOptionPane.showMessageDialog(null, "회원가입에 실패하였습니다");

		}
	}

	private void displayImage(File file) {
		if (file != null) {
			ImageIcon imageIcon = new ImageIcon(file.getAbsolutePath());
			Image image = imageIcon.getImage().getScaledInstance(imageL.getWidth(), imageL.getHeight(),
					Image.SCALE_SMOOTH);
			imageL.setIcon(new ImageIcon(image));
		}
	}

	private void showImageInNewWindow(File file) {

		if (file != null) {
			ImageIcon originalImageIcon = new ImageIcon(file.getAbsolutePath());

			// 원본 이미지의 크기를 확인
			int originalWidth = originalImageIcon.getIconWidth() / 2;
			int originalHeight = originalImageIcon.getIconHeight() / 2;

			// 새 창 생성
			JFrame frame = new JFrame("원본 이미지");
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			// 이미지 레이블을 생성하고 원본 이미지 아이콘을 설정
			JLabel originalImageLabel = new JLabel(originalImageIcon);
			frame.getContentPane().add(originalImageLabel);

			// 창 크기를 원본 이미지의 크기에 맞게 조정
			frame.setSize(originalWidth, originalHeight);
			int width = frame.getWidth();
			int height = frame.getHeight();
			Image scaledImage = originalImageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);

			// ImageIcon 객체를 새로 생성하여 이미지를 변경
			ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
			originalImageLabel.setIcon(scaledImageIcon);

			frame.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					// 창 크기가 변경될 때 이미지 크기를 조절
					int width = frame.getWidth();
					int height = frame.getHeight();
					Image scaledImage = originalImageIcon.getImage().getScaledInstance(width, height,
							Image.SCALE_SMOOTH);

					// ImageIcon 객체를 새로 생성하여 이미지를 변경
					ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
					originalImageLabel.setIcon(scaledImageIcon);

				}
			});

			// 창을 가운데 정렬
			frame.setLocationRelativeTo(null);

			frame.setVisible(true);

		}

	}

	public void zipAppend(String add) {
		String[] m = add.split(":");
		zip.append(m[0]);
		zip_1.append(m[1]);
		zipno = m[0];
		zipname = m[1];
	}

	public void pwSafeImg(int i) {
		ImageIcon imageIcon = new ImageIcon("C:\\Users\\도석환\\eclipse-workspace\\Omok_Project\\safety\\" + i + ".png");
		Image image = imageIcon.getImage().getScaledInstance(pwchkL_2.getWidth(), pwchkL_2.getHeight(),
				Image.SCALE_SMOOTH);
		pwchkL_2.setIcon(new ImageIcon(image));
	}

	private void updateDaysComboBox(JComboBox<String> daysComboBox, String selectedMonth) {
		daysComboBox.removeAllItems();
		daysComboBox.addItem("선택");

		if (!selectedMonth.equals("선택")) {
			int month = Integer.parseInt(selectedMonth);
			int daysInMonth = getDaysInMonth(month);
			for (int i = 1; i <= daysInMonth; i++) {
				daysComboBox.addItem(String.valueOf(i));
			}
		}
	}

	private static int getDaysInMonth(int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, month - 1); // Calendar의 월은 0부터 시작하므로 1을 빼줌
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
}
