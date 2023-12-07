package Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class OmokRecordFrame extends JFrame {

	private JPanel contentPane;
	String originalFilePath = "C:\\Users\\도석환\\eclipse-workspace\\Omok_Project\\screenshot\\";
	private JTextField textField;
	JPanel showpanel;
	Operator o;

	public OmokRecordFrame(Operator _o) {
		o = _o;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 645, 845);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(53, 89, 532, 643);
		contentPane.add(scrollPane);

		showpanel = new JPanel();
		showpanel.setBackground(new Color(255, 255, 255));
		scrollPane.setViewportView(showpanel);
		showpanel.setLayout(new BoxLayout(showpanel, BoxLayout.Y_AXIS)); // BoxLayout으로 변경

		textField = new JTextField();
		textField.setBounds(53, 27, 433, 42);
		contentPane.add(textField);
		textField.setColumns(10);

		JButton btnNewButton = new JButton("검색");
		btnNewButton.setBounds(498, 27, 83, 42);
		contentPane.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("전체 기보 보기");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showAllPanels(showpanel);
			}
		});
		btnNewButton_1.setBounds(53, 756, 174, 42);
		contentPane.add(btnNewButton_1);

		JButton btnNewButton_1_1 = new JButton("종료하기");
		btnNewButton_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_1_1.setBounds(404, 756, 174, 42);
		contentPane.add(btnNewButton_1_1);

		// 예시 데이터

		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 필터 버튼 클릭 시 동작
				String filterUsername = textField.getText().trim();
				if (filterUsername.isEmpty()) {
					showAllPanels(showpanel);
				} else {
					filterPanels(showpanel, filterUsername);
				}
			}
		});
	}

	public void addPanels(String[] users1, String[] users2, BufferedImage[] images, String[] omokrecord,
			String[] wincolor, String[] playtime) {
		showpanel.removeAll(); // 기존 패널 초기화

		for (int i = users1.length - 1; i >= 0; i--) {
			RecordOmokPanel recordOmok = new RecordOmokPanel(users1[i], users2[i], images[i], omokrecord[i],
					wincolor[i], playtime[i], o);
			showpanel.add(recordOmok);
		}

		showpanel.revalidate();
		showpanel.repaint();
	}

	private void filterPanels(JPanel panel, String filterUsername) {
		Component[] components = panel.getComponents();
		for (Component component : components) {
			if (component instanceof RecordOmokPanel) {
				RecordOmokPanel recordOmok = (RecordOmokPanel) component;
				if (recordOmok.getUserName().equals(filterUsername)
						|| recordOmok.getUserName2().equals(filterUsername)) {
					recordOmok.setVisible(true);
				} else {
					recordOmok.setVisible(false);
				}
			}
		}
		panel.revalidate();
		panel.repaint();
	}

	private void showAllPanels(JPanel panel) {
		Component[] components = panel.getComponents();
		for (Component component : components) {
			if (component instanceof RecordOmokPanel) {
				RecordOmokPanel recordOmok = (RecordOmokPanel) component;
				recordOmok.setVisible(true);
			}
		}
		panel.revalidate();
		panel.repaint();
	}
}

class RecordOmokPanel extends JPanel {
	String userName1;
	String userName2;
	BufferedImage images;
	String recordomok;
	String wincolor;
	String playtime;
	Operator o;

	public RecordOmokPanel(String userName1, String userName2, BufferedImage images, String recordomok, String wincolor,
			String playtime, Operator _o) {
		this.userName1 = userName1;
		this.userName2 = userName2;
		this.images = images;
		this.recordomok = recordomok;
		this.wincolor = wincolor;
		this.playtime = playtime;
		this.o = _o;
		initialize();
	}

	private void initialize() {
		setPreferredSize(new java.awt.Dimension(500, 180));
		setBackground(Color.WHITE);
		setLayout(null);

		JLabel blacklabel = new JLabel("검정색");
		blacklabel.setBounds(12, 10, 116, 38);
		add(blacklabel);
		JLabel whitelabel = new JLabel("흰색");
		whitelabel.setBounds(128, 10, 116, 38);
		add(whitelabel);

		JLabel userLabel = new JLabel(userName1);
		userLabel.setBounds(12, 48, 116, 38);
		add(userLabel);
		JLabel userLabel2 = new JLabel(userName2);
		userLabel2.setBounds(128, 48, 116, 38);
		add(userLabel2);
		JLabel winlabel = new JLabel("우승자");
		winlabel.setBounds(270, 10, 116, 38);
		add(winlabel);

		JLabel wincolorlabel = new JLabel("");
		if (wincolor.equals("BLACK")) {
			wincolorlabel.setText(userName1);
		} else {
			wincolorlabel.setText(userName2);
		}
		wincolorlabel.setBounds(270, 48, 116, 38);
		add(wincolorlabel);

		JLabel playtimelabel = new JLabel(playtime);
		playtimelabel.setBounds(12, 140, 200, 38);
		add(playtimelabel);

		JLabel imageLabel = new JLabel("새 라벨");
		imageLabel.setBounds(340, 22, 154, 140);
		ImageIcon imageIcon = new ImageIcon(images);
		Image image = imageIcon.getImage().getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(),
				Image.SCALE_SMOOTH);
		imageLabel.setIcon(new ImageIcon(image));

		add(imageLabel);

		// 패널에 클릭 이벤트 핸들러 추가
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 클릭 이벤트 처리 로직을 여기에 추가
				Gibo gb = new Gibo(o, userName1 + " vs " + userName2, recordomok.split(":"));
				gb.setVisible(true);
			}
		});
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

	public String getUserName() {
		return userName1;
	}

	public String getUserName2() {
		return userName2;
	}
}