package Client;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.lang.model.element.Element;
import javax.print.attribute.AttributeSet;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.FileChooserUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.border.LineBorder;
import javax.swing.JProgressBar;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemColor;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

public class WaitRoomFrame extends JFrame {

	private JPanel contentPane;
	private JTextPane chatArea;
	private JTextField messageField;
	private JList<String> roomList;
	private boolean isFriendListMode = false; // 현재 모드를 추적하는 플래그
	Socket sock;
	DataOutputStream dos;
	DataInputStream dis;
	Operator o = null;
	String selectroom = null;
	JLabel profileImg;
	JLabel losewinLabel;
	JLabel nickLabel;
	StyledDocument doc;
	SimpleAttributeSet style;
	JList<String> userList;
	DefaultListModel<String> listModel = null;
	DefaultListModel<String> userListModel = null;
	DefaultListModel<String> friendListModel = null;
	JButton listchangeButton;
	JLabel listNameLabel;
	JFileChooser fileChooser;
	JLabel playlabel = null;
	
	public WaitRoomFrame(Operator opt, String nickname, Socket socket) {
		o = opt;
		sock = socket;
		setTitle("대기실");
		fileChooser = new JFileChooser();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 820, 763);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(204, 153, 102));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		listModel = new DefaultListModel<>();
		for (String room : o.roomList) {
			listModel.addElement(room);
		}
		roomList = new JList(listModel); // 방 목록 띄우기
		roomList.addContainerListener(new ContainerAdapter() {
			@Override
			public void componentAdded(ContainerEvent e) {
			}
		});
		roomList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) { // 이거 없으면 mouse 눌릴때, 뗄때 각각 한번씩 호출되서 총 두번 호출

				}
			}
		});
		roomList.setBorder(new LineBorder(new Color(0, 0, 0)));
		roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		roomList.setBounds(29, 39, 395, 305);
		contentPane.add(roomList);

		JButton btnNewButton = new JButton("방 입장하기");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (roomList.getSelectedValue() != null) {
					enterRoom();
				} else {
					JOptionPane.showMessageDialog(null, "방을 선택해주세요", "알림", JOptionPane.INFORMATION_MESSAGE);
				}

			}
		});
		btnNewButton.setBounds(447, 56, 130, 80);
		contentPane.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("전적조회");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedIndex = userList.getSelectedIndex();
				if (selectedIndex >= 0) {
					sendRate(userList.getSelectedValue());
				}
			}
		});
		btnNewButton_1.setBounds(447, 284, 130, 80);
		contentPane.add(btnNewButton_1);

		friendListModel = new DefaultListModel<>();
		userListModel = new DefaultListModel<>();
		for (String zip : o.zipList) {
			userListModel.addElement(zip);
		}
		userList = new JList<String>(userListModel);
		userList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int selectedIndex = userList.getSelectedIndex();
					if (selectedIndex >= 0) {
						sendRate(userList.getSelectedValue());

					}
				}

			}
		});
		userList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					int index = userList.locationToIndex(e.getPoint());
					if (index >= 0 && index < userList.getModel().getSize()) {
						userList.setSelectedIndex(index);
					}
					// 우클릭이 감지되면 팝업 메뉴를 표시합니다.
					if (userList.getSelectedValue() != null) {
						if (!userList.getSelectedValue().equals(o.nick)) {
							showPopupMenu(e.getComponent(), e.getX(), e.getY());
						}
					}
				}
			}
		});
		JScrollPane listScroll = new JScrollPane(userList);
		listScroll.setBounds(614, 45, 177, 295);
		contentPane.add(listScroll);

		listNameLabel = new JLabel("접속중인 유저");
		listNameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		listScroll.setColumnHeaderView(listNameLabel);

		JLabel lblNewLabel = new JLabel("방 목록");
		lblNewLabel.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		lblNewLabel.setBounds(40, 10, 88, 32);
		contentPane.add(lblNewLabel);

		messageField = new JTextField();
		messageField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessage();

			}
		});

		messageField.setBounds(29, 672, 297, 32);
		contentPane.add(messageField);

		chatArea = new JTextPane();
		chatArea.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		chatArea.setBackground(SystemColor.inactiveCaption);
		style = new SimpleAttributeSet();
		;
		chatArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(chatArea);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(29, 393, 395, 269);
		contentPane.add(scrollPane);

		JButton sendButton = new JButton("전송");
		sendButton.setBounds(329, 671, 95, 32);
		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});
		contentPane.add(sendButton);

		JLabel lblNewLabel_2 = new JLabel("채팅창");
		lblNewLabel_2.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		lblNewLabel_2.setBounds(39, 354, 88, 32);
		contentPane.add(lblNewLabel_2);

		nickLabel = new JLabel("nickname");
		nickLabel.setFont(new Font("굴림", Font.BOLD, 18));
		nickLabel.setBounds(614, 591, 123, 46);
		contentPane.add(nickLabel);

		JButton btnNewButton_3 = new JButton("기보 조회");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendServer("GIBO");
			}
		});
		btnNewButton_3.setBounds(447, 393, 131, 76);
		contentPane.add(btnNewButton_3);

		JButton btnNewButton_2 = new JButton("방 생성하기");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				o.rcf.setVisible(true);
			}
		});
		btnNewButton_2.setBounds(447, 164, 130, 86);
		contentPane.add(btnNewButton_2);

		JButton btnNewButton_4 = new JButton("회원 정보 변경");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				try {
					dos = new DataOutputStream(sock.getOutputStream());

					// 서버로 로그인 요청 메시지 전송
					System.out.println(o.nick);
					String message = ("INFO//" + o.nick);

					dos.writeUTF(message);
					dos.flush();
					o.ic.setVisible(true);

				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
		btnNewButton_4.setBounds(447, 495, 130, 73);
		contentPane.add(btnNewButton_4);

		losewinLabel = new JLabel(o.loseno + "승 " + o.winno + "패 ");
		losewinLabel.setFont(new Font("굴림", Font.BOLD, 18));
		losewinLabel.setBounds(614, 641, 123, 46);
		contentPane.add(losewinLabel);

		listchangeButton = new JButton("접속중인 친구 목록 보기");
		listchangeButton.setHorizontalAlignment(SwingConstants.LEADING);
		listchangeButton.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		listchangeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggleListMode();
			}
		});
		listchangeButton.setBounds(616, 345, 175, 23);
		contentPane.add(listchangeButton);

		JButton btnNewButton_5 = new JButton("");
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FontChooseFrame fcf = new FontChooseFrame(o);
				fcf.setVisible(true);
			}
		});
		btnNewButton_5.setBounds(394, 360, 30, 30);
		ImageIcon imageIcon = new ImageIcon("C:\\Users\\도석환\\eclipse-workspace\\Omok_Project\\wheel.png");
		Image image = imageIcon.getImage().getScaledInstance(btnNewButton_5.getWidth(), btnNewButton_5.getHeight(),
				Image.SCALE_SMOOTH);
		btnNewButton_5.setIcon(new ImageIcon(image));
		contentPane.add(btnNewButton_5);

		profileImg = new JLabel("New label");
		profileImg.setBounds(614, 372, 180, 209);
		contentPane.add(profileImg);

		FileNameExtensionFilter filter = new FileNameExtensionFilter("이미지 파일", "jpg", "jpeg", "png", "gif", "bmp");
		fileChooser.setFileFilter(filter);
		JButton btnNewButton_6 = new JButton("이미지 전송");
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileChooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();

					// 이미지 크기 전송
					sendServer("WIMSG//" + file.length());
					System.out.println(file.length());
					try {
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
		btnNewButton_6.setBounds(270, 358, 120, 30);
		contentPane.add(btnNewButton_6);

		JButton btnNewButton_4_1 = new JButton("게임 종료");
		btnNewButton_4_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendServer("PEXIT");
				dispose();
				
			}
		});
		btnNewButton_4_1.setBounds(447, 599, 130, 73);
		contentPane.add(btnNewButton_4_1);

		JLabel lblNewLabel_1 = new JLabel("New label");
		lblNewLabel_1.setBounds(614, 5, 40, 40);
		ImageIcon soundIcon = new ImageIcon("C:\\Users\\도석환\\eclipse-workspace\\Omok_Project\\soundcontrol.png");
		Image soundimage = soundIcon.getImage().getScaledInstance(lblNewLabel_1.getWidth(), lblNewLabel_1.getHeight(), Image.SCALE_SMOOTH);
		lblNewLabel_1.setIcon(new ImageIcon(soundimage));
		lblNewLabel_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) { // 이미지 레이블을 한 번 클릭했을 때
					SoundControlFrame scf = new SoundControlFrame(o);
					scf.setVisible(true);
				}
			}
		});
		contentPane.add(lblNewLabel_1);

		playlabel = new JLabel("New label");
		playlabel.setBounds(674, 5, 40, 40);
		ImageIcon playIcon = new ImageIcon("C:\\Users\\도석환\\eclipse-workspace\\Omok_Project\\pause.png");
		Image playimage = playIcon.getImage().getScaledInstance(playlabel.getWidth(), playlabel.getHeight(),
				Image.SCALE_SMOOTH);
		playlabel.setIcon(new ImageIcon(playimage));
		playlabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) { // 이미지 레이블을 한 번 클릭했을 때
					if (o.sp.musicClip != null && o.sp.musicClip.isRunning()) {
						o.sp.stopMusic();
						ImageIcon playIcon = new ImageIcon("C:\\Users\\도석환\\eclipse-workspace\\Omok_Project\\play.png");
						Image playimage = playIcon.getImage().getScaledInstance(playlabel.getWidth(),
								playlabel.getHeight(), Image.SCALE_SMOOTH);
						playlabel.setIcon(new ImageIcon(playimage));
					} else {
						o.sp.musicSound();
						ImageIcon playIcon = new ImageIcon(
								"C:\\Users\\도석환\\eclipse-workspace\\Omok_Project\\pause.png");
						Image playimage = playIcon.getImage().getScaledInstance(playlabel.getWidth(),
								playlabel.getHeight(), Image.SCALE_SMOOTH);
						playlabel.setIcon(new ImageIcon(playimage));

					}
				}
			}
		});
		contentPane.add(playlabel);

		profileImg.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) { // 이미지 레이블을 한 번 클릭했을 때

					if (o.myimage != null) {
						showImageInNewWindow(o.myimage);
					}
				}
			}
		});

	}

	public void updateRoomList(String[] newRoomList) {
		listModel.clear(); // 기존 목록을 지우고
		for (String room : newRoomList) {
			listModel.addElement(room); // 새로운 목록을 추가
		}
	}

	private void enterRoom() {
		String[] message = roomList.getSelectedValue().split(":");
		System.out.println(message[1]);
		if (message != null) {
			try {
				dos = new DataOutputStream(sock.getOutputStream());

				// 서버로 로그인 요청 메시지 전송

				String msg = ("EROOM//" + message[0]);

				dos.writeUTF(msg);
				dos.flush();
				messageField.setText("");

				// 서버로 로그인 요청 메시지 전송

			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void sendMessage() {
		String message = messageField.getText();

		if (!message.isEmpty()) {
			if (startsWithW(message)) {
				String nick = extractNickname(message);
				String msg = extractWithoutWAndNickname(message);
				System.out.println("추출메시지" + msg);

				if (!nick.equals(o.nick)) {
					sendServer("WHISPER//" + nick + "//" + msg);
				} else {
					JOptionPane.showMessageDialog(this, "본인에게는 귓속말을 보낼 수 없습니다", "경고", JOptionPane.INFORMATION_MESSAGE);
				}
			} else {
				try {
					dos = new DataOutputStream(sock.getOutputStream());
					// 서버로 로그인 요청 메시지 전송
					String msg = ("WMSG//" + message);
					System.out.println("문자보냄");
					dos.writeUTF(msg);
					dos.flush();
					messageField.setText("");

					// 서버로 로그인 요청 메시지 전송

				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void processChat(int i, String chatString) {
		new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				updateChatArea(i, chatString);
				return null;
			}
		}.execute();
	}

	private void updateChatArea(int i, String chatString) {
		// i가 0이면 그냥 채팅 1이면 귓속말
		String[] parts = chatString.split(":");
		if (parts.length == 2 || parts.length == 3) {
			String sender = parts[0].trim(); // 송신자 닉네임

			String message = parts[1].trim(); // 메시지 내용

			doc = (StyledDocument) chatArea.getDocument();
			SimpleAttributeSet style = new SimpleAttributeSet();
			StyleConstants.setFontSize(style, o.fontopt.fontsize); // 폰트 사이즈 설정
			StyleConstants.setFontFamily(style, o.fontopt.fontname); // 폰트 설정

			try {
				if (sender.equals(o.nick)) { // 내가 보낸 메시지
					StyleConstants.setAlignment(style, StyleConstants.ALIGN_RIGHT); // 오른쪽 정렬
					StyleConstants.setBackground(style, o.fontopt.mybackcolor); // 배경색 적용
					StyleConstants.setForeground(style, o.fontopt.mycolor); // 글자색 적용
					doc.setParagraphAttributes(doc.getLength(), 1, style, false);
					if (i == 0) {// 귓속말인지 확인 i==0이면 그냥 채팅 i==1이면 귓속말
						sender = "";
					} else if (i == 1) {
						sender = parts[2].trim() + "님에게 귓속말 : ";
					}
				} else { // 상대가 보낸 메시지
					StyleConstants.setAlignment(style, StyleConstants.ALIGN_LEFT);
					StyleConstants.setBackground(style, o.fontopt.yourbackcolor);
					StyleConstants.setForeground(style, o.fontopt.yourcolor);
					doc.setParagraphAttributes(doc.getLength(), 1, style, false);
					if (i == 0) {
						sender = sender + ": ";
					} else if (i == 1) {
						sender = sender + "님의 귓속말 : ";
					}
				}
				doc.insertString(doc.getLength(), sender + message + "\n", style);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
	}

	public void profileImg(BufferedImage image, String nick) {
		if (image != null) {
			ImageIcon imageIcon = new ImageIcon(image);
			Image img = imageIcon.getImage().getScaledInstance(profileImg.getWidth(), profileImg.getHeight(),
					Image.SCALE_SMOOTH);
			profileImg.setIcon(new ImageIcon(img));
			nickLabel.setText(nick);
			losewinLabel.setText(o.winno + "승 " + o.loseno + "패 ");
		}
	}

	public void showImageInNewWindow(BufferedImage file) {

		if (file != null) {
			ImageIcon originalImageIcon = new ImageIcon(file);

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

	private void viewInfo() {
		String msg = userList.getSelectedValue();
		if (msg != null) {

			try {
				dos = new DataOutputStream(sock.getOutputStream());

				// 서버로 로그인 요청 메시지 전송

				String message = ("SEARCH//" + msg);

				dos.writeUTF(message);
				dos.flush();

			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateUserList(String[] newUserList) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				userListModel.clear(); // 목록에서 기존 항목 지우기
				for (String user : newUserList) {
					userListModel.addElement(user); // 새로운 userlist 목록에 추가하기
				}
			}
		});
	}

	public void updateFriendList(String[] newUserList) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				friendListModel.clear(); // 목록에서 기존 항목 지우기
				for (String friend : newUserList) {
					friendListModel.addElement(friend); // 새로운 userlist 목록에 추가하기
				}
			}
		});
	}

	private void showPopupMenu(Component component, int x, int y) {
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem menufriend = new JMenuItem("친구 추가");
		JMenuItem menuwhisper = new JMenuItem("귓속말");
		JMenuItem menurate = new JMenuItem("전적보기");

		// 메뉴 아이템에 액션 리스너를 추가하여 클릭 이벤트를 처리합니다.
		menufriend.addActionListener(a -> {
			if (!containsString(o.friendList, userList.getSelectedValue())) {
				sendServer("FRIENDPLUS//" + userList.getSelectedValue());
				System.out.println(userList.getSelectedValue() + "에게 친구요청");
			} else {
				String message = "[" + userList.getSelectedValue() + "] 님과 이미 친구입니다.";
				JOptionPane.showMessageDialog(null, message, "알림", JOptionPane.INFORMATION_MESSAGE);
			}

		});
		menuwhisper.addActionListener(a -> {
			// 여기에 메뉴 아이템이 클릭되었을 때 수행할 동작을 추가합니다.
			messageField.setText("/W \"" + userList.getSelectedValue() + "\" ");
		});
		menurate.addActionListener(a -> {
			// 여기에 메뉴 아이템이 클릭되었을 때 수행할 동작을 추가합니다.
			sendRate(userList.getSelectedValue());
		});

		if (!isFriendListMode) {
			popupMenu.add(menufriend);
		}
		popupMenu.add(menuwhisper);
		popupMenu.add(menurate);

		// 메뉴를 표시합니다.
		popupMenu.show(component, x, y);
	}

	void sendServer(String msg) {
		try {
			dos = new DataOutputStream(sock.getOutputStream());
			dos.writeUTF(msg);
			dos.flush();
			messageField.setText("");

			// 서버로 친구추가 요청 메시지 전송
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static boolean startsWithW(String input) {
		return input.startsWith("/W");
	}

	private static String extractNickname(String input) {
		// /W 다음에 나오는 큰 따옴표의 위치를 찾습니다.
		int start = input.indexOf("\"");
		if (start != -1) {
			// 큰 따옴표의 다음 위치를 찾습니다.
			int end = input.indexOf("\"", start + 1);
			if (end != -1) {
				// 큰 따옴표로 둘러싸인 부분을 추출합니다.
				return input.substring(start + 1, end);
			}
		}
		// 큰 따옴표로 둘러싸인 부분이 없거나 오류가 있을 경우 빈 문자열을 반환합니다.
		return "";
	}

	private static String extractWithoutWAndNickname(String input) {
		// "/W" 다음에 나오는 큰 따옴표의 위치를 찾습니다.
		int start = input.indexOf("\"");
		if (start != -1) {
			// 큰 따옴표의 다음 위치를 찾습니다.
			int end = input.indexOf("\"", start + 1);
			if (end != -1) {
				// 큰 따옴표로 둘러싸인 부분과 그 뒤의 내용을 추출합니다.
				return input.substring(end + 1).trim();
			}
		}
		// "/W"나 큰 따옴표가 없거나 오류가 있을 경우 원래 문자열을 반환합니다.
		return input;
	}

	public void showFriendRequestAcceptedDialog(String nick) {
		String message = "[" + nick + "] 님과 친구가 되었습니다.";
		JOptionPane.showMessageDialog(null, message, "알림", JOptionPane.INFORMATION_MESSAGE);
	}

	private void toggleListMode() {
		if (isFriendListMode) {
			// 유저 목록 모드로 전환
			userList.setModel(userListModel);
			listNameLabel.setText("접속중인 유저");
			listchangeButton.setText("접속중인 친구 목록 보기");
		} else {
			// 친구 목록 모드로 전환
			userList.setModel(friendListModel);
			listNameLabel.setText("접속중인 친구");
			listchangeButton.setText("접속중인 유저 목록 보기");
		}

		isFriendListMode = !isFriendListMode; // 모드 전환
	}

	public static boolean containsString(String[] array, String target) {
		// 배열을 반복하여 문자열이 있는지 확인
		for (String element : array) {
			if (element.equals(target)) {
				return true; // 문자열이 배열에 있으면 true 반환
			}
		}
		return false; // 문자열이 배열에 없으면 false 반환
	}

	public void insertImageWithHyperlink(BufferedImage image, String nick, boolean flag) {
		String text = "이미지";
		try {
			// 이미지 크기 조절
			Image resizedImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
			// 스타일 지정
			SimpleAttributeSet attributeSet = new SimpleAttributeSet();
			StyleConstants.setIcon(attributeSet, new ImageIcon(resizedImage));
			// 텍스트판에 스타일 적용
			StyledDocument doc = chatArea.getStyledDocument();
			int offset;
			// 오른쪽 정렬 또는 왼쪽 정렬 설정
			if (flag) {
				// 이미지에 대한 하이퍼링크 추가
				offset = doc.getLength();
				doc.insertString(offset, text, attributeSet);

				SimpleAttributeSet alignRight = new SimpleAttributeSet();
				StyleConstants.setAlignment(alignRight, StyleConstants.ALIGN_RIGHT);
				doc.setParagraphAttributes(offset, text.length(), alignRight, false);

				doc.insertString(doc.getLength(), "\n", null);
			} else {
				// 닉네임 먼저 띄우고 줄 바꿈
				doc.insertString(doc.getLength(), nick + "\n", null);
				// 이미지에 대한 하이퍼링크 추가
				offset = doc.getLength();
				doc.insertString(offset, text, attributeSet);

				doc.insertString(doc.getLength(), "\n", null);
			}
			// 마우스 이벤트 리스너 등록
			chatArea.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int clickedOffset = chatArea.viewToModel2D(e.getPoint());
					int textLength = text.length();

					if (clickedOffset >= offset - textLength && clickedOffset < offset + textLength) {
						// 클릭된 영역이 이미지에 해당하는지 확인
						showImageInNewWindow(image); // 클릭 시 실행될 코드를 추가
					}
				}
			});
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	void sendRate(String nick) {
		sendServer("RATE//" + nick);
	}
}
