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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
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
import javax.swing.border.EtchedBorder;

public class Gibo extends JFrame {
	private Container c;
	MapSize size;
	Map map;
	DrawBoard d;
	JButton exitBtn;
	private JButton btnNewButton;
	private JButton btnNewButton_1;
	private JButton btnNewButton_2;
	private JButton btnNewButton_3;
	int order = 0;
	String[] orderrecord;
	Operator o;

	public Gibo(Operator _o, String title, String[] record) {
		o = _o;

		orderrecord = record;
		size = new MapSize();
		setTitle(title);
		c = getContentPane();
		setBounds(200, 20, 630, 840);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		map = new Map(size);
		getContentPane().setLayout(null);
		d = new DrawBoard(o, size, map);
		d.setBounds(0, 0, 621, 802);
		d.setPreferredSize(new Dimension(size.getCell() * size.getSize(), size.getCell() * size.getSize()));

		// DrawBoard를 중앙에 배치
		c.add(d);
		d.setLayout(null);

		btnNewButton = new JButton("New button");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetBoard();
			}
		});
		btnNewButton.setOpaque(false);
		btnNewButton.setContentAreaFilled(false);
		btnNewButton.setBounds(52, 643, 95, 95);
		ImageIcon imageIcon = new ImageIcon("C:\\Users\\도석환\\eclipse-workspace\\Omok_Project\\화살표1.png");
		Image img = imageIcon.getImage().getScaledInstance(btnNewButton.getWidth(), btnNewButton.getHeight(),
				Image.SCALE_SMOOTH);
		btnNewButton.setIcon(new ImageIcon(img));
		d.add(btnNewButton);

		btnNewButton_1 = new JButton("New button");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (order == 0) {
					JOptionPane.showMessageDialog(null, "초기 상태입니다");
				} else {
					order--;
					String[] xy = orderrecord[order].split("@");
					int x = Integer.parseInt(xy[0]);
					int y = Integer.parseInt(xy[1]);
					removeomok(x, y);
				}
			}
		});
		btnNewButton_1.setOpaque(false);
		btnNewButton_1.setContentAreaFilled(false);
		btnNewButton_1.setBounds(174, 643, 95, 95);
		imageIcon = new ImageIcon("C:\\Users\\도석환\\eclipse-workspace\\Omok_Project\\화살표2.png");
		img = imageIcon.getImage().getScaledInstance(btnNewButton_1.getWidth(), btnNewButton_1.getHeight(),
				Image.SCALE_SMOOTH);
		btnNewButton_1.setIcon(new ImageIcon(img));
		d.add(btnNewButton_1);

		btnNewButton_2 = new JButton("New button");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (order == orderrecord.length) {
					JOptionPane.showMessageDialog(null, "기록이 여기까지입니다");
				} else {
					String[] xy = orderrecord[order].split("@");
					int x = Integer.parseInt(xy[0]);
					int y = Integer.parseInt(xy[1]);
					receiveomok(x, y);
					order++;
				}
			}
		});
		btnNewButton_2.setOpaque(false);
		btnNewButton_2.setContentAreaFilled(false);
		btnNewButton_2.setBounds(349, 635, 95, 95);
		imageIcon = new ImageIcon("C:\\Users\\도석환\\eclipse-workspace\\Omok_Project\\화살표3.png");
		img = imageIcon.getImage().getScaledInstance(btnNewButton_1.getWidth(), btnNewButton_1.getHeight(),
				Image.SCALE_SMOOTH);
		btnNewButton_2.setIcon(new ImageIcon(img));
		d.add(btnNewButton_2);

		btnNewButton_3 = new JButton("New button");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				recordOmok(orderrecord);
			}
		});
		btnNewButton_3.setOpaque(false);
		btnNewButton_3.setContentAreaFilled(false);
		btnNewButton_3.setBounds(481, 635, 95, 95);
		imageIcon = new ImageIcon("C:\\Users\\도석환\\eclipse-workspace\\Omok_Project\\화살표4.png");
		img = imageIcon.getImage().getScaledInstance(btnNewButton_1.getWidth(), btnNewButton_1.getHeight(),
				Image.SCALE_SMOOTH);
		btnNewButton_3.setIcon(new ImageIcon(img));
		d.add(btnNewButton_3);

		exitBtn = new JButton("방 나가기");
		exitBtn.setBounds(227, 740, 179, 37);
		d.add(exitBtn);
		exitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		exitBtn.setVisible(true);

	}

	public void showPopUP(String message) {
		JOptionPane.showMessageDialog(this, message, "", JOptionPane.INFORMATION_MESSAGE);
		;
	}

	void recordOmok(String[] m) {
		resetBoard();
		for (String romok : m) {
			String[] xy = romok.split("@");
			int x = Integer.parseInt(xy[0]);
			int y = Integer.parseInt(xy[1]);
			receiveomok(x, y);
		}
		order = orderrecord.length;
	}

	void receiveomok(int x, int y) {
		map.setMap(y, x);
		map.changeCheck();
		d.repaint();
		if (map.winCheck(x, y)) {
			if (map.getCheck() == true) {
				this.showPopUP("[백돌]이 승리");
			} else {
				this.showPopUP("[흑돌]이 승리");
			}

		}
	}

	void removeomok(int x, int y) {
		map.removeMap(y, x);
		map.changeCheck();
		d.repaint();

	}

	void resetBoard() {
		order = 0;
		map.resetBoard(); // 보드 초기화
		d.repaint(); // 화면 갱신
	}

}
