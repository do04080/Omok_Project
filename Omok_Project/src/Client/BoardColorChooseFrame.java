package Client;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class BoardColorChooseFrame extends JFrame {

	private JPanel contentPane;
	Color blackstonecolor;
	Color whitestonecolor;
	Color boardcolor;
	Color linecolor;
	MapSize size;
	Map map;
	DrawBoard d;
	Operator o;

	public BoardColorChooseFrame(Operator _o) {
		o = _o;
		blackstonecolor = o.fontopt.blackstonecolor;
		whitestonecolor = o.fontopt.whitestonecolor;
		boardcolor = o.fontopt.boardcolor;
		linecolor = o.fontopt.linecolor;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 986, 697);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		panel.setBounds(12, 10, 645, 645);
		contentPane.add(panel);

		size = new MapSize();
		map = new Map(size);
		d = new DrawBoard(o, size, map);
		d.setBounds(0, 0, 1306, 814);
		d.setPreferredSize(new Dimension(size.getCell() * size.getSize(), size.getCell() * size.getSize()));
		panel.add(d);
		d.setLayout(null);
		for (int x = 12; x < 17; x++) {
			int y = 15;
			map.setMap(y, x);
		}
		map.changeCheck();
		for (int x = 12; x < 17; x++) {
			int y = 16;
			map.setMap(y, x);
		}
		d.repaint();

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(blackstonecolor);
		panel_1.setBounds(740, 60, 89, 29);
		panel_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				Color color = colorSelet();
				if (color != o.fontopt.whitestonecolor) {
					o.fontopt.blackstonecolor = color;
					panel_1.setBackground(o.fontopt.blackstonecolor);
					d.repaint();
				} else {

				}

			}
		});
		contentPane.add(panel_1);

		JPanel panel_1_1 = new JPanel();
		panel_1_1.setBackground(whitestonecolor);
		panel_1_1.setBounds(740, 170, 89, 29);
		panel_1_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				Color color = colorSelet();
				if (color != o.fontopt.blackstonecolor) {
					o.fontopt.whitestonecolor = color;
					panel_1_1.setBackground(o.fontopt.whitestonecolor);
					d.repaint();
				} else {

				}

			}
		});
		contentPane.add(panel_1_1);

		JPanel panel_1_2 = new JPanel();
		panel_1_2.setBackground(o.fontopt.boardcolor);
		panel_1_2.setBounds(740, 280, 89, 29);
		panel_1_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				Color color = colorSelet();
				if (color != o.fontopt.linecolor) {
					o.fontopt.boardcolor = color;
					panel_1_2.setBackground(o.fontopt.boardcolor);
					d.repaint();
				} else {

				}

			}
		});
		contentPane.add(panel_1_2);

		JPanel panel_1_3 = new JPanel();
		panel_1_3.setBackground(linecolor);
		panel_1_3.setBounds(740, 400, 89, 29);
		panel_1_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				Color color = colorSelet();
				if (color != o.fontopt.boardcolor) {
					o.fontopt.linecolor = color;
					panel_1_3.setBackground(o.fontopt.linecolor);
					d.repaint();
				} else {

				}

			}
		});
		contentPane.add(panel_1_3);

		JLabel lblNewLabel = new JLabel("흑색돌");
		lblNewLabel.setBounds(740, 30, 89, 29);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("백색돌");
		lblNewLabel_1.setBounds(740, 141, 89, 29);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("보드색");
		lblNewLabel_2.setBounds(740, 251, 89, 29);
		contentPane.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("라인색");
		lblNewLabel_3.setBounds(740, 370, 89, 29);
		contentPane.add(lblNewLabel_3);

		JButton btnNewButton = new JButton("적용하기");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				o.gui.d.repaint();
				blackstonecolor = o.fontopt.blackstonecolor;
				whitestonecolor = o.fontopt.whitestonecolor;
				boardcolor = o.fontopt.boardcolor;
				linecolor = o.fontopt.linecolor;
				o.wr.sendServer("BCHANGE//" + o.nick + "//" + o.fontopt.colorToString(blackstonecolor) + "//"
						+ o.fontopt.colorToString(whitestonecolor) + "//" + o.fontopt.colorToString(boardcolor) + "//"
						+ o.fontopt.colorToString(linecolor) + "//");
			}
		});
		btnNewButton.setBounds(692, 576, 103, 46);
		contentPane.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("취소하기");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				o.fontopt.blackstonecolor = blackstonecolor;
				o.fontopt.whitestonecolor = whitestonecolor;
				o.fontopt.boardcolor = boardcolor;
				o.fontopt.linecolor = linecolor;
				dispose();
			}
		});
		btnNewButton_1.setBounds(843, 576, 103, 46);
		contentPane.add(btnNewButton_1);

		JButton btnNewButton_2 = new JButton("초기화");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				o.fontopt.blackstonecolor = blackstonecolor;
				o.fontopt.whitestonecolor = whitestonecolor;
				o.fontopt.boardcolor = boardcolor;
				o.fontopt.linecolor = linecolor;
				panel_1.setBackground(o.fontopt.blackstonecolor);
				panel_1_1.setBackground(o.fontopt.whitestonecolor);
				panel_1_2.setBackground(o.fontopt.boardcolor);
				panel_1_3.setBackground(o.fontopt.linecolor);

				d.repaint();
			}
		});
		btnNewButton_2.setBounds(692, 482, 103, 29);
		contentPane.add(btnNewButton_2);

		JButton btnNewButton_2_1 = new JButton("기본 설정");
		btnNewButton_2_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				o.fontopt.blackstonecolor = Color.black;
				o.fontopt.whitestonecolor = Color.white;
				o.fontopt.boardcolor = o.fontopt.stringToColor("#cea73d");
				o.fontopt.linecolor = Color.black;
				panel_1.setBackground(o.fontopt.blackstonecolor);
				panel_1_1.setBackground(o.fontopt.whitestonecolor);
				panel_1_2.setBackground(o.fontopt.boardcolor);
				panel_1_3.setBackground(o.fontopt.linecolor);
				d.repaint();
			}
		});
		btnNewButton_2_1.setBounds(843, 482, 103, 29);
		contentPane.add(btnNewButton_2_1);
	}

	Color colorSelet() {
		Color selectedColor = JColorChooser.showDialog(contentPane, "Choose a Color", Color.BLACK);
		return selectedColor;
	}
}
