package Client;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;

public class OperatorFrame extends JFrame {

	private JPanel contentPane;
	Operator o = null;
	Socket sock;
	OutputStream os;
	DataOutputStream dos;
	InputStream is;
	DataInputStream dis;
	private JTable table;
	private JScrollPane scrollPane;
	private int selectedRow = -1;
	private int selectedColumn = -1;
	private JFileChooser fileChooser;
	private File file;
	JLabel imageL;
	private JButton btnNewButton_1;
	String[] columnNames = { "ID", "Name", "Nickname", "Password", "Sex", "Email", "Zip", "Address", "Birthday", "img",
			"Win", "Lose" };
	private JTextField textField;
	private JTextField namet;
	private JTextField nickt;
	private JTextField idt;
	private JPasswordField passwordt;
	private JTextField zipt;
	private JTextField addt;
	private JTextField addplust;
	private JTextField emailt;
	private JTextField emailt2;
	JComboBox comboBox;
	JComboBox<String> daysComboBox;
	JComboBox<String> monthsComboBox;
	JComboBox<String> yearComboBox;
	String[][] userData;
	JRadioButton brb;
	JRadioButton wrb;
	String sex;
	boolean passhide = true;
	private JLabel lblNewLabel_9;
	private JLabel lblNewLabel_10;
	private JLabel lblNewLabel_11;
	private JLabel lblNewLabel_12;
	private JLabel lblNewLabel_13;
	private JButton btnNewButton_7;

	public OperatorFrame(Operator _o, Socket socket) {
		o = _o;
		sock = socket;
		setTitle("회원관리");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1602, 810);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.inactiveCaption);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("이미지 파일", "jpg", "jpeg", "png", "gif", "bmp");
		fileChooser.setFileFilter(filter);

		// 문자열을 행으로 나누기
		String[] userStrings = o.inputString.split("\\|\\|");

		// 각 행을 열로 나누고 2차원 배열 생성
		String[][] userData = new String[userStrings.length][];
		for (int i = 0; i < userStrings.length; i++) {
			userData[i] = userStrings[i].split("@@");
		}

		// NonEditableTableModel을 사용하여 JTable을 생성
		NonEditableTableModel model = new NonEditableTableModel(userData, columnNames);
		table = new JTable(model);

		// JScrollPane을 사용하여 스크롤 가능한 테이블 생성
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(34, 107, 798, 589);
		contentPane.setLayout(null);
		contentPane.add(scrollPane);

		// 테이블의 선택된 행이 변경될 때 이벤트 처리
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent a) {
				selectedRow = table.getSelectedRow();
				selectedColumn = table.getSelectedColumn();
				populateTextFieldsFromSelectedRow();

			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		});

		JButton btnNewButton = new JButton("검색");
		btnNewButton.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchTable();
			}
		});
		btnNewButton.setBounds(630, 35, 193, 45);
		contentPane.add(btnNewButton);

		textField = new JTextField();
		textField.setBounds(197, 35, 396, 45);
		contentPane.add(textField);
		textField.setColumns(10);

		String[] combooption = { "아이디", "이름", "닉네임", "성별", "이메일", "우편번호", "주소" };
		comboBox = new JComboBox(combooption);
		comboBox.setBounds(41, 34, 144, 46);
		contentPane.add(comboBox);

		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.activeCaption);
		panel.setBounds(876, 12, 667, 684);
		contentPane.add(panel);
		panel.setLayout(null);

		namet = new JTextField();
		namet.setBounds(91, 40, 277, 45);
		panel.add(namet);
		namet.setColumns(10);

		btnNewButton_1 = new JButton("이미지 변경");
		btnNewButton_1.setBounds(477, 514, 108, 45);
		panel.add(btnNewButton_1);

		imageL = new JLabel("");
		imageL.setHorizontalAlignment(SwingConstants.CENTER);
		imageL.setForeground(Color.BLACK);
		imageL.setBackground(new Color(255, 255, 255));
		imageL.setBounds(402, 263, 253, 241);
		panel.add(imageL);

		nickt = new JTextField();
		nickt.setColumns(10);
		nickt.setBounds(91, 114, 277, 45);
		panel.add(nickt);

		idt = new JTextField();
		idt.setColumns(10);
		idt.setBounds(91, 191, 277, 45);
		panel.add(idt);

		btnNewButton_7 = new JButton("");
		btnNewButton_7.setBounds(314, 263, 54, 45);
		btnNewButton_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (passhide) {
					passwordt.setEchoChar((char) 0);
					ImageIcon imageIcon = new ImageIcon(
							"C:\\Users\\도석환\\eclipse-workspace\\Omok_Project\\eyeclose.png");
					Image image = imageIcon.getImage().getScaledInstance(btnNewButton_7.getWidth(),
							btnNewButton_7.getHeight(), Image.SCALE_SMOOTH);
					btnNewButton_7.setIcon(new ImageIcon(image));
					passhide = !passhide;
				} else {
					passwordt.setEchoChar('*');
					ImageIcon imageIcon = new ImageIcon("C:\\Users\\도석환\\eclipse-workspace\\Omok_Project\\eyeopen.png");
					Image image = imageIcon.getImage().getScaledInstance(btnNewButton_7.getWidth(),
							btnNewButton_7.getHeight(), Image.SCALE_SMOOTH);
					btnNewButton_7.setIcon(new ImageIcon(image));
					passhide = !passhide;
				}
			}
		});
		ImageIcon imageIconpw = new ImageIcon("C:\\Users\\도석환\\eclipse-workspace\\Omok_Project\\eyeopen.png");
		Image imagepw = imageIconpw.getImage().getScaledInstance(btnNewButton_7.getWidth(), btnNewButton_7.getHeight(),
				Image.SCALE_SMOOTH);
		btnNewButton_7.setIcon(new ImageIcon(imagepw));
		panel.add(btnNewButton_7);

		passwordt = new JPasswordField();
		passwordt.setColumns(10);
		passwordt.setBounds(91, 263, 228, 45);
		panel.add(passwordt);

		zipt = new JTextField();
		zipt.setEditable(false);
		zipt.setColumns(10);
		zipt.setBounds(91, 353, 143, 45);
		panel.add(zipt);

		addt = new JTextField();
		addt.setEditable(false);
		addt.setColumns(10);
		addt.setBounds(91, 408, 277, 45);
		panel.add(addt);

		addplust = new JTextField();
		addplust.setColumns(10);
		addplust.setBounds(91, 477, 277, 45);
		panel.add(addplust);

		emailt = new JTextField();
		emailt.setColumns(10);
		emailt.setBounds(91, 628, 143, 45);
		panel.add(emailt);

		String[] domainOptions = { "선택", "naver.com", "daum.com", "google.com", "직접입력" };
		JComboBox<String> domainComboBox = new JComboBox<>(domainOptions);
		domainComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String domain = (String) domainComboBox.getSelectedItem();
				if (domain.equals("직접입력")) {
					emailt2.setText("");
					emailt2.setEditable(true);
				} else {
					emailt2.setText("");
					emailt2.setEditable(false);
					if (!domain.equals("선택")) {
						emailt2.setText((String) domainComboBox.getSelectedItem());
					}
				}
			}

		});
		domainComboBox.setBounds(441, 627, 144, 46);
		panel.add(domainComboBox);

		emailt2 = new JTextField();
		emailt2.setColumns(10);
		emailt2.setBounds(286, 628, 143, 45);
		panel.add(emailt2);

		int startYear = 1920;
		int endYear = Calendar.getInstance().get(Calendar.YEAR); // 현재 연도
		String[] yearOptions = new String[endYear - startYear + 2];
		yearOptions[0] = "선택"; // 선택 안 함 옵션 추가
		for (int i = startYear; i <= endYear; i++) {
			yearOptions[i - startYear + 1] = String.valueOf(i);
		}

		String[] monthOptions = new String[13];
		monthOptions[0] = "선택"; // 선택 안 함 옵션 추가
		for (int i = 1; i <= 12; i++) {
			monthOptions[i] = String.valueOf(i);
		}

		JLabel lblNewLabel = new JLabel("이름");
		lblNewLabel.setFont(new Font("굴림", Font.BOLD, 18));
		lblNewLabel.setBounds(12, 41, 78, 44);
		panel.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("닉네임");
		lblNewLabel_1.setFont(new Font("굴림", Font.BOLD, 18));
		lblNewLabel_1.setBounds(12, 114, 78, 44);
		panel.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("아이디");
		lblNewLabel_2.setFont(new Font("굴림", Font.BOLD, 18));
		lblNewLabel_2.setBounds(12, 192, 78, 44);
		panel.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("비밀번호");
		lblNewLabel_3.setFont(new Font("굴림", Font.BOLD, 18));
		lblNewLabel_3.setBounds(12, 264, 78, 44);
		panel.add(lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel("우편번호");
		lblNewLabel_4.setFont(new Font("굴림", Font.BOLD, 18));
		lblNewLabel_4.setBounds(12, 353, 78, 44);
		panel.add(lblNewLabel_4);

		JLabel lblNewLabel_5 = new JLabel("주소");
		lblNewLabel_5.setFont(new Font("굴림", Font.BOLD, 18));
		lblNewLabel_5.setBounds(12, 409, 78, 44);
		panel.add(lblNewLabel_5);

		JLabel lblNewLabel_6 = new JLabel("상세주소");
		lblNewLabel_6.setFont(new Font("굴림", Font.BOLD, 18));
		lblNewLabel_6.setBounds(12, 476, 78, 44);
		panel.add(lblNewLabel_6);

		JLabel lblNewLabel_7 = new JLabel("생년월일\r\n");
		lblNewLabel_7.setFont(new Font("굴림", Font.BOLD, 18));
		lblNewLabel_7.setBounds(12, 553, 78, 44);
		panel.add(lblNewLabel_7);

		JLabel lblNewLabel_8 = new JLabel("이메일\r\n");
		lblNewLabel_8.setFont(new Font("굴림", Font.BOLD, 18));
		lblNewLabel_8.setBounds(12, 628, 78, 44);
		panel.add(lblNewLabel_8);

		brb = new JRadioButton("남");
		brb.setBackground(SystemColor.activeCaption);
		brb.setFont(new Font("굴림", Font.BOLD, 18));
		brb.setBounds(529, 124, 54, 23);
		panel.add(brb);

		wrb = new JRadioButton("여");
		wrb.setBackground(SystemColor.activeCaption);
		wrb.setFont(new Font("굴림", Font.BOLD, 18));
		wrb.setBounds(577, 124, 54, 23);
		panel.add(wrb);
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

		JButton btnNewButton_6 = new JButton("우편번호 찾기");
		btnNewButton_6.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow() != -1) {
					o.zipinst = 2;
					o.zf = new ZipFrame(o, sock);
					o.zf.setVisible(true);
				}
			}
		});
		btnNewButton_6.setBounds(243, 353, 125, 45);
		panel.add(btnNewButton_6);
		yearComboBox = new JComboBox<String>(yearOptions);
		yearComboBox.setBounds(91, 554, 83, 45);
		panel.add(yearComboBox);
		monthsComboBox = new JComboBox<String>(monthOptions);
		monthsComboBox.setBounds(214, 554, 83, 45);
		panel.add(monthsComboBox);

		daysComboBox = new JComboBox<>();
		daysComboBox.setBounds(337, 554, 83, 45);
		panel.add(daysComboBox);

		lblNewLabel_9 = new JLabel("@");
		lblNewLabel_9.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_9.setFont(new Font("굴림", Font.BOLD, 18));
		lblNewLabel_9.setBounds(232, 627, 54, 45);
		panel.add(lblNewLabel_9);

		lblNewLabel_10 = new JLabel("년");
		lblNewLabel_10.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_10.setFont(new Font("굴림", Font.BOLD, 18));
		lblNewLabel_10.setBounds(168, 553, 54, 45);
		panel.add(lblNewLabel_10);

		lblNewLabel_11 = new JLabel("월");
		lblNewLabel_11.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_11.setFont(new Font("굴림", Font.BOLD, 18));
		lblNewLabel_11.setBounds(286, 553, 54, 45);
		panel.add(lblNewLabel_11);

		lblNewLabel_12 = new JLabel("일");
		lblNewLabel_12.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_12.setFont(new Font("굴림", Font.BOLD, 18));
		lblNewLabel_12.setBounds(411, 553, 54, 45);
		panel.add(lblNewLabel_12);

		lblNewLabel_13 = new JLabel("성별");
		lblNewLabel_13.setFont(new Font("굴림", Font.BOLD, 18));
		lblNewLabel_13.setBounds(470, 114, 78, 44);
		panel.add(lblNewLabel_13);

		daysComboBox.addItem("선택");
		monthsComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateDaysComboBox(daysComboBox, (String) monthsComboBox.getSelectedItem());
			}
		});

		JButton btnNewButton_2 = new JButton("전체 회원 검색");
		btnNewButton_2.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showAllRows();
			}
		});
		btnNewButton_2.setBounds(34, 718, 193, 45);
		contentPane.add(btnNewButton_2);

		JButton btnNewButton_3 = new JButton("삭제하기");
		btnNewButton_3.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow() != -1) {
					deleteSend();
				}
			}
		});
		btnNewButton_3.setBounds(681, 718, 193, 45);
		contentPane.add(btnNewButton_3);

		JButton btnNewButton_4 = new JButton("수정하기");
		btnNewButton_4.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow() != -1) {
					sendChange();
				}
			}
		});
		btnNewButton_4.setBounds(1017, 718, 193, 45);
		contentPane.add(btnNewButton_4);

		JButton btnNewButton_5 = new JButton("종료하기");
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_5.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		btnNewButton_5.setBounds(1350, 718, 193, 45);
		contentPane.add(btnNewButton_5);

		JButton btnNewButton_3_1 = new JButton("채팅내역");
		btnNewButton_3_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow() != -1) {
					o.wr.sendServer("CHAT//"+table.getValueAt(selectedRow, 2));
				}
			}
		});
		btnNewButton_3_1.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		btnNewButton_3_1.setBounds(328, 718, 193, 45);
		contentPane.add(btnNewButton_3_1);
		btnNewButton_1.setVisible(false);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileChooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					file = fileChooser.getSelectedFile();
					displayImage(file);
				}
			}
		});
	}

	public void updateTableWithNewData(String inputString) {
		// 문자열을 행으로 나누기
		String[] userStrings = inputString.split("\\|\\|");

		// 각 행을 열로 나누고 2차원 배열 생성
		userData = new String[userStrings.length][];
		for (int i = 0; i < userStrings.length; i++) {
			userData[i] = userStrings[i].split("@@");
		}

		String[] columnNames = { "ID", "Name", "Nickname", "Password", "Sex", "Email", "Zip", "Address", "Birthday",
				"img", "Win", "Lose" };

		// NonEditableTableModel을 사용하여 JTable을 업데이트
		NonEditableTableModel model = new NonEditableTableModel(userData, columnNames);
		table.setModel(model);
	}

	public void overSign(boolean sign, String att) {
		if (sign) {
			System.out.println("중복확인 성공");
			JOptionPane.showMessageDialog(null, "중복확인에 성공하였습니다");
		} else {
			System.out.println("중복확인 실패");
			JOptionPane.showMessageDialog(null, "중복확인에 실패하였습니다");
		}
	}

	public void overCheck(String att, String value) {
		try {
			o.overinst = 1;
			dos = new DataOutputStream(sock.getOutputStream());
			dos.writeUTF("OVER//" + att + "//" + value);
			dos.flush();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void displayImage(File file) {
		if (file != null) {
			ImageIcon imageIcon = new ImageIcon(file.getAbsolutePath());
			Image image = imageIcon.getImage().getScaledInstance(imageL.getWidth(), imageL.getHeight(),
					Image.SCALE_SMOOTH);
			imageL.setIcon(new ImageIcon(image));
		}
	}

	public void profileImg(BufferedImage image) {
		System.out.println("이미지실행");
		if (image != null) {
			ImageIcon imageIcon = new ImageIcon(image);
			Image img = imageIcon.getImage().getScaledInstance(imageL.getWidth(), imageL.getHeight(),
					Image.SCALE_SMOOTH);
			imageL.setIcon(new ImageIcon(img));
			imageL.setVisible(true);
			btnNewButton_1.setVisible(true);
		}
	}

	void plsImg() {
		try {

			dos = new DataOutputStream(sock.getOutputStream());
			dos.writeUTF("IMG//" + Objects.toString(table.getValueAt(selectedRow, 2)));
			dos.flush();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// NonEditableTableModel 정의
	class NonEditableTableModel extends DefaultTableModel {

		public NonEditableTableModel(Object[][] data, Object[] columnNames) {
			super(data, columnNames);
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return false; // 모든 셀을 수정 불가능하게 설정
		}
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

	private void populateTextFieldsFromSelectedRow() {
		if (selectedRow != -1) {
			idt.setText(Objects.toString(table.getValueAt(selectedRow, 0)));
			namet.setText(Objects.toString(table.getValueAt(selectedRow, 1)));
			nickt.setText(Objects.toString(table.getValueAt(selectedRow, 2)));
			plsImg();
			// Splitting the Birthday string
			String birthdayString = Objects.toString(table.getValueAt(selectedRow, 8));
			String[] birthdayParts = birthdayString.split(" ");
			String year = birthdayParts[0].replace("년", "");
			String month = birthdayParts[1].replace("월", "");
			String day = birthdayParts[2].replace("일", "");
			yearComboBox.setSelectedItem(year);
			monthsComboBox.setSelectedItem(month);
			daysComboBox.setSelectedItem(day);

			passwordt.setText(Objects.toString(table.getValueAt(selectedRow, 3)));

			String sexValue = Objects.toString(table.getValueAt(selectedRow, 4));
			if ("남".equals(sexValue)) {
				sex = "남";
				brb.setSelected(true);
				wrb.setSelected(false);
			} else if ("여".equals(sexValue)) {
				sex = "여";
				brb.setSelected(false);
				wrb.setSelected(true);
			}

			// Splitting the Address string
			String[] addressString = Objects.toString(table.getValueAt(selectedRow, 7)).split("\\s{3}");
			String add = addressString[0];
			String addp = addressString[1];
			String zipString = Objects.toString(table.getValueAt(selectedRow, 6));
			zipt.setText(zipString);
			addt.setText(add);
			addplust.setText(addp);

			// Splitting the Email string
			String emailString = Objects.toString(table.getValueAt(selectedRow, 5));
			String[] emailParts = emailString.split("@");
			emailt.setText(emailParts[0]);
			emailt2.setText(emailParts[1]);
		}
	}

	void sendChange() {
		String uid = idt.getText();
		String unm = namet.getText();
		String unnm = nickt.getText();
		String uem = emailt.getText() + "@" + emailt2.getText();
		String upass = new String(passwordt.getPassword()); // char[]를 String으로 변환하기 위해 String 생성자 사용
		String uzip = zipt.getText();
		String uadd = addt.getText() + addplust.getText(); // addt가 JTextField인 것을 가정합니다
		String selectedYear = (String) yearComboBox.getSelectedItem();
		String selectedMonth = (String) monthsComboBox.getSelectedItem();
		String selectedDay = (String) daysComboBox.getSelectedItem();
		String birth = selectedYear + "년 " + selectedMonth + "월 " + selectedDay + "일";

		long fileSize;
		String fileName;
		String ext;

		if (file != null) {
			fileSize = file.length();
			fileName = file.getName();
			ext = fileName.substring(fileName.lastIndexOf("."));
			fileName = unm + ext;
		} else {
			fileSize = o.fileSize;
			fileName = Objects.toString(table.getValueAt(selectedRow, 9));
			ext = fileName.substring(fileName.lastIndexOf(".") + 1);
		}
		try {
			dos = new DataOutputStream(sock.getOutputStream());

			System.out.println();
			dos.writeUTF("OCHANGE//" + Objects.toString(table.getValueAt(selectedRow, 2)) + "//" + uid + "//" + upass
					+ "//" + unm + "//" + unnm + "//" + sex + "//" + uzip + "//" + uadd + "//" + uem + "//" + birth
					+ "//" + fileSize + "//" + fileName);
			dos.flush();

			if (file != null) {
				try (FileInputStream fileInputStream = new FileInputStream(file)) {
					byte[] buffer = new byte[4096];
					int bytesRead;
					while ((bytesRead = fileInputStream.read(buffer)) != -1) {
						dos.write(buffer, 0, bytesRead);
					}
				}
			} else {
				byte[] imageBytes = convertToByteArray(o.youimage, ext);
				dos.write(imageBytes);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static byte[] convertToByteArray(BufferedImage image, String format) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, format, baos);
		return baos.toByteArray();
	}

	private void searchTable() {
		String searchCategory = (String) comboBox.getSelectedItem();
		String searchTerm = textField.getText().toLowerCase();

		// 테이블 데이터 초기화
		((DefaultTableModel) table.getModel()).setRowCount(0);

		// 검색어와 일치하는 결과만 테이블에 추가
		for (String[] row : userData) {
			String cellValue = "";
			switch (searchCategory) {

			case "아이디":
				cellValue = row[0].toLowerCase(); // 아이디 컬럼에서 검색
				break;
			case "이름":
				cellValue = row[1].toLowerCase(); // 이름 컬럼에서 검색
				break;
			case "닉네임":
				cellValue = row[2].toLowerCase(); // 닉네임 컬럼에서 검색
				break;
			case "성별":
				cellValue = row[4].toLowerCase(); // 성별 컬럼에서 검색
				break;
			case "우편번호":
				cellValue = row[6].toLowerCase(); // 이름 컬럼에서 검색
				break;
			case "주소":
				cellValue = row[7].toLowerCase(); // 이름 컬럼에서 검색
				break;
			case "이메일":
				cellValue = row[5].toLowerCase(); // 이름 컬럼에서 검색
				break;

			}

			if (cellValue.contains(searchTerm)) {
				// 검색어가 포함된 행 추가
				((DefaultTableModel) table.getModel()).addRow(Arrays.copyOf(row, row.length));
			}
		}
	}

	private void showAllRows() {
		// 테이블 데이터 초기화
		((DefaultTableModel) table.getModel()).setRowCount(0);

		// 원래의 데이터로 테이블 다시 채우기
		for (String[] row : userData) {
			((DefaultTableModel) table.getModel()).addRow(Arrays.copyOf(row, row.length));
		}
	}

	public void zipAppend(String add) {
		String[] m = add.split(":");
		zipt.setText(m[0]);
		addt.setText(m[1]);
		addplust.setText("");
	}

	public void deleteSend() {
		try {

			dos = new DataOutputStream(sock.getOutputStream());
			dos.writeUTF("DELETE//" + Objects.toString(table.getValueAt(selectedRow, 2)));
			dos.flush();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
