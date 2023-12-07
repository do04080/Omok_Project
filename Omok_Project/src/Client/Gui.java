package Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JList;
import javax.swing.JMenuItem;

import java.awt.SystemColor;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.border.EtchedBorder;

public class Gui extends JFrame {
	private Container c;
	MapSize size;
	JPanel panel;
	private JTextField messageField;
	private JLabel player1imgL;
	private JLabel player2imgL;
	private JLabel player1nickL;
	private JLabel player2nickL;
	private JLabel player1infoL;
	private JLabel player2infoL;
	private boolean isFriendListMode = false;
	JList<String> list;
	DefaultListModel<String> listModel = null;
	DefaultListModel<String> friendListModel = null;
	JTextPane chatArea;
	Socket sock;
	DataOutputStream dos;
	DataInputStream dis;
	Operator o = null;
	Map map;
	DrawBoard d;
	private JButton movebtn2_1;
	private JLabel listNameLabel;
	private JButton listchangeButton;
	JButton exitBtn;
	JButton sendBtn;
	private JButton btnNewButton;
	BufferedImage capturedImage;
	private JButton btnNewButton_2;

	public Gui(String title, Socket socket, Operator _o) {
		o = _o;
		sock = socket;
		size = new MapSize();
		setTitle(title);
		c = getContentPane();
		setBounds(200, 20, 1315, 851);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		map = new Map(size);
		getContentPane().setLayout(null);
		d = new DrawBoard(o, size, map);
		d.setBounds(0, 0, 1306, 814);
		d.setPreferredSize(new Dimension(size.getCell() * size.getSize(), size.getCell() * size.getSize()));

		// DrawBoard를 중앙에 배치
		c.add(d);
		addMouseListener(new MouseEventHandler(map, size, d, this, sock));
		d.setLayout(null);

		panel = new JPanel();
		panel.setBackground(new Color(204, 153, 102));
		panel.setBounds(626, 0, 702, 814);
		d.add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel_1 = new JLabel("New label");
		lblNewLabel_1.setBounds(12, 386, 40, 40);
		ImageIcon soundIcon = new ImageIcon("C:\\Users\\도석환\\eclipse-workspace\\Omok_Project\\soundcontrol.png");
		Image soundimage = soundIcon.getImage().getScaledInstance(lblNewLabel_1.getWidth(), lblNewLabel_1.getHeight(),
				Image.SCALE_SMOOTH);
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
		panel.add(lblNewLabel_1);

		JLabel lblNewLabel_1_1 = new JLabel("New label");
		lblNewLabel_1_1.setBounds(60, 386, 40, 40);
		ImageIcon playIcon;
		if (o.sp.musicClip != null && o.sp.musicClip.isRunning()) {
		playIcon = new ImageIcon("C:\\Users\\도석환\\eclipse-workspace\\Omok_Project\\pause.png");
		}else {
		playIcon = new ImageIcon("C:\\Users\\도석환\\eclipse-workspace\\Omok_Project\\play.png");
		}
		Image playimage = playIcon.getImage().getScaledInstance(lblNewLabel_1_1.getWidth(), lblNewLabel_1_1.getHeight(),
				Image.SCALE_SMOOTH);
		lblNewLabel_1_1.setIcon(new ImageIcon(playimage));
		lblNewLabel_1_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) { // 이미지 레이블을 한 번 클릭했을 때
					if (o.sp.musicClip != null && o.sp.musicClip.isRunning()) {
						o.sp.stopMusic();
						ImageIcon playIcon = new ImageIcon("C:\\Users\\도석환\\eclipse-workspace\\Omok_Project\\play.png");
						Image playimage = playIcon.getImage().getScaledInstance(lblNewLabel_1_1.getWidth(),
								lblNewLabel_1_1.getHeight(), Image.SCALE_SMOOTH);
						
						lblNewLabel_1_1.setIcon(new ImageIcon(playimage));
						playimage = playIcon.getImage().getScaledInstance(o.wr.playlabel.getWidth(),
								o.wr.playlabel.getHeight(), Image.SCALE_SMOOTH);
						o.wr.playlabel.setIcon(new ImageIcon(playimage));
					} else {
						o.sp.musicSound();
						ImageIcon playIcon = new ImageIcon(
								"C:\\Users\\도석환\\eclipse-workspace\\Omok_Project\\pause.png");
						Image playimage = playIcon.getImage().getScaledInstance(lblNewLabel_1_1.getWidth(),
								lblNewLabel_1_1.getHeight(), Image.SCALE_SMOOTH);
						lblNewLabel_1_1.setIcon(new ImageIcon(playimage));
						playimage = playIcon.getImage().getScaledInstance(o.wr.playlabel.getWidth(),
								o.wr.playlabel.getHeight(), Image.SCALE_SMOOTH);
						o.wr.playlabel.setIcon(new ImageIcon(playimage));

					}
				}
			}
		});
		panel.add(lblNewLabel_1_1);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 436, 460, 300);
		panel.add(scrollPane);
		panel.setVisible(true);

		chatArea = new JTextPane();
		scrollPane.setViewportView(chatArea);
		chatArea.setBackground(SystemColor.inactiveCaption);
		chatArea.setEditable(false);

		messageField = new JTextField();
		messageField.setBounds(12, 746, 371, 40);
		messageField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				sendMessage();

			}
		});
		panel.add(messageField);
		messageField.setColumns(10);

		sendBtn = new JButton("전송");
		sendBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});
		sendBtn.setBounds(395, 745, 77, 40);
		panel.add(sendBtn);

		player1imgL = new JLabel("");
		player1imgL.setBounds(10, 18, 203, 246);
		panel.add(player1imgL);

		player1nickL = new JLabel("New label");
		player1nickL.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		player1nickL.setBounds(12, 273, 161, 23);
		panel.add(player1nickL);

		player2imgL = new JLabel("");
		player2imgL.setBounds(247, 18, 203, 246);
		panel.add(player2imgL);

		player2nickL = new JLabel("New label");
		player2nickL.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		player2nickL.setBounds(247, 274, 161, 21);
		panel.add(player2nickL);

		exitBtn = new JButton("방 나가기");
		exitBtn.setBounds(484, 747, 179, 37);
		panel.add(exitBtn);
		exitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (o.omokorder != 0) {
					sendServer("LOSE//BTN//" + o.mycolor);
				}
				sendServer("REXIT//" + o.mystat);

			}
		});
		exitBtn.setVisible(true);

		listModel = new DefaultListModel<>();
		friendListModel = new DefaultListModel<>();
		list = new JList(listModel);
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int selectedIndex = list.getSelectedIndex();
					if (selectedIndex >= 0) {
						o.wr.sendRate(list.getSelectedValue());

					}
				}
				if (SwingUtilities.isRightMouseButton(e)) {
					int index = list.locationToIndex(e.getPoint());
					if (index >= 0 && index < list.getModel().getSize()) {
						list.setSelectedIndex(index);
					}
					// 우클릭이 감지되면 팝업 메뉴를 표시합니다.
					if (list.getSelectedValue() != null) {
						if (!list.getSelectedValue().equals(o.nick)) {
							showPopupMenu(e.getComponent(), e.getX(), e.getY());
						}
					}
				}
			}
		});
		JScrollPane listScroll = new JScrollPane(list);
		listScroll.setBounds(484, 70, 179, 641);
		panel.add(listScroll);

		listNameLabel = new JLabel("관전중인 유저 목록");
		listNameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		listScroll.setColumnHeaderView(listNameLabel);

		player1infoL = new JLabel("New label");
		player1infoL.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		player1infoL.setBounds(12, 309, 203, 21);
		panel.add(player1infoL);

		player2infoL = new JLabel("New label");
		player2infoL.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		player2infoL.setBounds(247, 305, 161, 21);
		panel.add(player2infoL);

		movebtn2_1 = new JButton("이동");
		movebtn2_1.setBounds(484, 18, 180, 40);
		panel.add(movebtn2_1);
		movebtn2_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (o.omokorder != 0) {
					sendServer("LOSE//BTN//" + o.mycolor);
				}
				sendServer("MOVE//" + o.mystat);
			}
		});
		movebtn2_1.setVisible(true);

		listchangeButton = new JButton("친구 목록 보기");
		listchangeButton.setBounds(484, 714, 179, 23);
		panel.add(listchangeButton);

		btnNewButton = new JButton("");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FontChooseFrame fcf = new FontChooseFrame(o);
				fcf.setVisible(true);
			}
		});
		btnNewButton.setBounds(442, 396, 30, 30);
		ImageIcon imageIcon = new ImageIcon("C:\\Users\\도석환\\eclipse-workspace\\Omok_Project\\wheel.png");
		Image image = imageIcon.getImage().getScaledInstance(btnNewButton.getWidth(), btnNewButton.getHeight(),
				Image.SCALE_SMOOTH);
		btnNewButton.setIcon(new ImageIcon(image));
		panel.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("이미지 전송");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = o.wr.fileChooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = o.wr.fileChooser.getSelectedFile();

					// 이미지 크기 전송
					sendServer("RIMSG//" + file.length());
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
		btnNewButton_1.setBounds(269, 396, 161, 30);
		panel.add(btnNewButton_1);

		listchangeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggleListMode();
			}
		});

		JButton startBtn = new JButton("게임 시작");
		startBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				if (o.mystat.equals("PLAYER")) {
					try {
						dos = new DataOutputStream(sock.getOutputStream());

						// 서버로 로그인 요청 메시지 전송
						String msg = ("START//");
						System.out.println("문자보냄");
						dos.writeUTF(msg);
						dos.flush();

					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null, "관전자 유저 입니다", "게임시작 실패", JOptionPane.ERROR_MESSAGE);
					System.out.println("게임시작 실패 > 관전자 유저");

				}

			}
		});
		startBtn.setBounds(117, 671, 122, 55);
		d.add(startBtn);

		JButton loseBtn = new JButton("기권하기");
		loseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (o.omokorder != 0) {
					sendServer("LOSE//BTN//" + o.mycolor);

				}
			}
		});
		loseBtn.setBounds(353, 671, 122, 55);
		d.add(loseBtn);

		btnNewButton_2 = new JButton("보드색 설정");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BoardColorChooseFrame bccf = new BoardColorChooseFrame(o);
				bccf.setVisible(true);
			}
		});
		btnNewButton_2.setBounds(449, 626, 161, 30);
		d.add(btnNewButton_2);

	}

	public void showPopUP(String message) {
		o.omokorder = 0;
		JOptionPane.showMessageDialog(this, message, "", JOptionPane.INFORMATION_MESSAGE);
		;
		map.resetBoard(); // 보드 초기화
		d.repaint(); // 화면 갱신
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
					String msg = ("CMSG//" + message);
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

	void recordOmok(String[] m) {
		for (String romok : m) {
			String[] xy = romok.split("@");
			int x = Integer.parseInt(xy[0]);
			int y = Integer.parseInt(xy[1]);
			receiveomok(x, y);
		}
	}

	void receiveomok(int x, int y) {
		map.setMap(y, x);
		map.changeCheck();
		o.sp.stoneSound();
		d.repaint();
		if (map.winCheck(x, y)) {
			if (map.getCheck() == true) {
				if (o.omokorder == -1) {
					sendServer("WIN");
				}
				if (o.omokorder == 1) {
					sendServer("LOSE//.//" + o.mycolor);

				}
				if (o.p1order.equals("-1")) {
					this.showPopUP("[" + o.p1nick + "]이 승리");
					System.out.println(o.p1order + o.p2order + "1");
				} else {
					this.showPopUP("[" + o.p2nick + "]이 승리");
					System.out.println(o.p1order + o.p2order + "2");
				}
			} else {

				if (o.omokorder == 1) {
					sendServer("WIN");
				}
				if (o.omokorder == -1) {
					sendServer("LOSE//.//" + o.mycolor);
				}
				if (o.p1order.equals("1")) {
					this.showPopUP("[" + o.p1nick + "]이 승리");
					System.out.println(o.p1order + o.p2order + "3");
				} else {
					this.showPopUP("[" + o.p2nick + "]이 승리");
					System.out.println(o.p1order + o.p2order + "4");
				}

			}
			o.omokorder = 0;
		}

	}

	void sendomok(int x, int y) {
		if (o.mystat.equals("PLAYER")) {
			if (o.omokorder == map.orderchk)
				try {
					dos = new DataOutputStream(sock.getOutputStream());

					String msg = ("OMOK//" + x + "@" + y);
					System.out.println("문자보냄");
					dos.writeUTF(msg);
					dos.flush();

				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public void profileImg(int i) {
		ImageIcon imageIcon;
		if (i == 1) {
			if (o.player1image != null) {
				imageIcon = new ImageIcon(o.player1image);
				ImageIcon image2Icon = new ImageIcon("C:\\Users\\도석환\\eclipse-workspace\\Omok_Project\\다운로드 (1).png");
				Image img2 = image2Icon.getImage().getScaledInstance(player2imgL.getWidth(), player2imgL.getHeight(),
						Image.SCALE_SMOOTH);
				player2imgL.setIcon(new ImageIcon(img2));
				Image img = imageIcon.getImage().getScaledInstance(player1imgL.getWidth(), player1imgL.getHeight(),
						Image.SCALE_SMOOTH);
				player1imgL.setIcon(new ImageIcon(img));
			}
		} else if (i == 2) {
			if (o.player2image != null) {
				imageIcon = new ImageIcon(o.player2image);
			} else {
				imageIcon = new ImageIcon("C:\\Users\\도석환\\eclipse-workspace\\Omok_Project\\다운로드 (1).png");
			}
			Image img = imageIcon.getImage().getScaledInstance(player2imgL.getWidth(), player2imgL.getHeight(),
					Image.SCALE_SMOOTH);
			player2imgL.setIcon(new ImageIcon(img));
		} else if (i == 0) {
			imageIcon = new ImageIcon("C:\\Users\\도석환\\eclipse-workspace\\Omok_Project\\다운로드 (1).png");
			Image img = imageIcon.getImage().getScaledInstance(player1imgL.getWidth(), player1imgL.getHeight(),
					Image.SCALE_SMOOTH);
			player1imgL.setIcon(new ImageIcon(img));
			player2imgL.setIcon(new ImageIcon(img));
		}

	}

	void sendServer(String m) {

		try {
			dos = new DataOutputStream(sock.getOutputStream());
			if (m.split("//")[0].equals("LOSE")) {
				capturedImage = d.captureImage(); // DrawBoard의 내용을 이미지로 캡처
				File tempFile = File.createTempFile("captured_image", ".png");
				ImageIO.write(capturedImage, "png", tempFile);

				// 파일 크기를 얻음
				long fileSizeInBytes = tempFile.length();
				m = m + "//" + Long.toString(fileSizeInBytes);
			}
			System.out.println("오목승패보냄");
			dos.writeUTF(m);
			if (m.split("//")[0].equals("LOSE")) {
				sendImage(capturedImage);
			}
			dos.flush();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	BufferedImage captureImage() {
		capturedImage = d.captureImage();
		return capturedImage;
	}

	void sendImage(BufferedImage image) {
		try {
			// BufferedImage를 바이트 배열로 변환
			ByteArrayOutputStream baos = new ByteArrayOutputStream(8192);
			ImageIO.write(image, "png", baos);
			baos.flush();
			byte[] imageBytes = baos.toByteArray();
			baos.close();
			// 이미지 데이터를 전송
			dos.write(imageBytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void profileUpdate(String i) {
		if (i.equals("0")) {
			player1nickL.setVisible(false);
			player1infoL.setVisible(false);
			player2nickL.setVisible(false);
			player2infoL.setVisible(false);
		}
		if (i.equals("1")) {
			player1nickL.setVisible(true);
			player1infoL.setVisible(true);
			player1nickL.setText(o.p1nick);
			player1infoL.setText(o.p1winno + "승 " + o.p1loseno + "패 ");
			player2nickL.setVisible(false);
			player2infoL.setVisible(false);
		}
		if (i.equals("2")) {
			player1nickL.setVisible(true);
			player1infoL.setVisible(true);
			player2nickL.setVisible(true);
			player2infoL.setVisible(true);
			player1nickL.setText(o.p1nick);
			player1infoL.setText(o.p1winno + "승 " + o.p1loseno + "패 ");
			player2nickL.setText(o.p2nick);
			player2infoL.setText(o.p2winno + "승 " + o.p2loseno + "패 ");
		}
	}

	public void updateObjList(String[] newUserList) {
		listModel.clear(); // 기존 목록을 지우고
		for (String obj : newUserList) {
			listModel.addElement(obj); // 새로운 목록을 추가
		}
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

	private void toggleListMode() {
		if (isFriendListMode) {
			// 유저 목록 모드로 전환
			list.setModel(listModel);
			listNameLabel.setText("관전중인 유저");
			listchangeButton.setText("접속중인 친구 목록 보기");
		} else {
			// 친구 목록 모드로 전환
			list.setModel(friendListModel);
			listNameLabel.setText("접속중인 친구");
			listchangeButton.setText("관전 유저 목록 보기");
		}

		isFriendListMode = !isFriendListMode; // 모드 전환
	}

	public void processChat(int i, String chatString) {
		// i가 0이면 그냥 채팅 1이면 귓속말
		String[] parts = chatString.split(":");
		if (parts.length == 2 || parts.length == 3) {
			String sender = parts[0].trim();

			String message = parts[1].trim();

			StyledDocument doc = (StyledDocument) chatArea.getDocument();
			SimpleAttributeSet style = new SimpleAttributeSet();
			StyleConstants.setFontSize(style, o.fontopt.fontsize);
			StyleConstants.setForeground(style, Color.BLACK);
			StyleConstants.setFontFamily(style, o.fontopt.fontname);

			try {
				if (sender.equals(o.nick)) {
					if (i == 0) {
						StyleConstants.setAlignment(style, StyleConstants.ALIGN_RIGHT);
						StyleConstants.setBackground(style, o.fontopt.mybackcolor);
						StyleConstants.setForeground(style, o.fontopt.mycolor);
						doc.setParagraphAttributes(doc.getLength(), 1, style, false);
						sender = "";
					} else if (i == 1) {
						StyleConstants.setAlignment(style, StyleConstants.ALIGN_RIGHT);
						StyleConstants.setBackground(style, o.fontopt.mybackcolor);
						StyleConstants.setForeground(style, o.fontopt.mycolor);
						doc.setParagraphAttributes(doc.getLength(), 1, style, false);

						sender = parts[2].trim() + "님에게 귓속말 : ";
					}
				} else {
					if (i == 0) {
						StyleConstants.setAlignment(style, StyleConstants.ALIGN_LEFT);
						StyleConstants.setBackground(style, o.fontopt.yourbackcolor);
						StyleConstants.setForeground(style, o.fontopt.yourcolor);
						doc.setParagraphAttributes(doc.getLength(), 1, style, false);
						sender = sender + ": ";
					} else if (i == 1) {
						StyleConstants.setAlignment(style, StyleConstants.ALIGN_LEFT);
						StyleConstants.setBackground(style, o.fontopt.yourbackcolor);
						StyleConstants.setForeground(style, o.fontopt.yourcolor);
						doc.setParagraphAttributes(doc.getLength(), 1, style, false);
						sender = sender + "님의 귓속말 : ";
					}
				}
				doc.insertString(doc.getLength(), sender + message + "\n", style);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
	}

	public void processChats(String[] chatStrings) {
		for (String chatString : chatStrings) {
			processChat(0, chatString);
		}
	}

	public void serverMessage(String message) {
		SimpleAttributeSet style = new SimpleAttributeSet();
		StyleConstants.setAlignment(style, StyleConstants.ALIGN_CENTER);
		StyleConstants.setBackground(style, new Color(0, 0, 128)); // 어두운 파랑색
		StyleConstants.setForeground(style, Color.GREEN);
		StyleConstants.setBold(style, true);
		StyleConstants.setFontSize(style, 18);

		StyledDocument doc = (StyledDocument) chatArea.getDocument();
		doc.setParagraphAttributes(doc.getLength(), 1, style, false);
		try {
			doc.insertString(doc.getLength(), message + "\n", style);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	private void showPopupMenu(Component component, int x, int y) {
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem menufriend = new JMenuItem("친구 추가");
		JMenuItem menuwhisper = new JMenuItem("귓속말");
		JMenuItem menuinvite = new JMenuItem("초대하기");
		JMenuItem menurate = new JMenuItem("전적보기");

		// 메뉴 아이템에 액션 리스너를 추가하여 클릭 이벤트를 처리합니다.
		menufriend.addActionListener(a -> {
			if (!containsString(o.friendList, list.getSelectedValue())) {
				sendServer("FRIENDPLUS//" + list.getSelectedValue());
				System.out.println(list.getSelectedValue() + "에게 친구요청");
			} else {
				String message = "[" + list.getSelectedValue() + "] 님과 이미 친구입니다.";
				JOptionPane.showMessageDialog(null, message, "알림", JOptionPane.INFORMATION_MESSAGE);
			}

		});
		menuwhisper.addActionListener(a -> {
			// 여기에 메뉴 아이템이 클릭되었을 때 수행할 동작을 추가합니다.
			messageField.setText("/W \"" + list.getSelectedValue() + "\" ");
		});
		menuinvite.addActionListener(a -> {
			// 여기에 메뉴 아이템이 클릭되었을 때 수행할 동작을 추가합니다.
			sendServer("INVITE//" + list.getSelectedValue() + "//" + o.nick + "//" + o.roomname);
		});
		menurate.addActionListener(a -> {
			// 여기에 메뉴 아이템이 클릭되었을 때 수행할 동작을 추가합니다.
			o.wr.sendRate(list.getSelectedValue());
		});

		if (!isFriendListMode) {
			popupMenu.add(menufriend);
		} else if (isFriendListMode) {
			popupMenu.add(menuinvite);
		}
		popupMenu.add(menuwhisper);
		popupMenu.add(menurate);

		// 메뉴를 표시합니다.
		popupMenu.show(component, x, y);
	}

	public boolean containsString(String[] array, String target) {
		// 배열을 반복하여 문자열이 있는지 확인
		for (String element : array) {
			if (element.equals(target)) {
				return true; // 문자열이 배열에 있으면 true 반환
			}
		}
		return false; // 문자열이 배열에 없으면 false 반환
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
						o.wr.showImageInNewWindow(image); // 클릭 시 실행될 코드를 추가
					}
				}
			});
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
}
