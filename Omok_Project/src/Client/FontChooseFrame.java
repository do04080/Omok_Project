package Client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JColorChooser;

import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.JComboBox;
import java.awt.SystemColor;
import java.awt.BorderLayout;

public class FontChooseFrame extends JFrame {

	private JPanel contentPane;
	Color mycolor;
	Color mybackcolor;
	Color yourcolor;
	Color yourbackcolor;
	String fontname;
	int fontsize;
	JLabel lblNewLabel;
	JPanel labelBackgroundPanel;
	JPanel panel;
	boolean mine = true;

	FontChooseFrame(Operator o) {
		mycolor = o.fontopt.mycolor;
		mybackcolor = o.fontopt.mybackcolor;
		yourcolor = o.fontopt.yourcolor;
		yourbackcolor = o.fontopt.yourbackcolor;
		fontname = o.fontopt.fontname;
		fontsize = o.fontopt.fontsize;

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 627, 356);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.inactiveCaptionBorder);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JSlider fontSizeSlider = new JSlider(SwingConstants.HORIZONTAL, 10, 25, 12);
		fontSizeSlider.setValue(fontsize);
		fontSizeSlider.setBounds(106, 209, 223, 46);
		fontSizeSlider.setMajorTickSpacing(5);
		fontSizeSlider.setMinorTickSpacing(1);
		fontSizeSlider.setPaintTicks(true);
		fontSizeSlider.setPaintLabels(true);
		fontSizeSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				if (!source.getValueIsAdjusting()) {
					fontsize = source.getValue();
					lblNewLabel.setFont(new Font(fontname, Font.PLAIN, fontsize));
					int textWidth = lblNewLabel.getFontMetrics(lblNewLabel.getFont())
							.stringWidth(lblNewLabel.getText());
					int textHeight = lblNewLabel.getFontMetrics(lblNewLabel.getFont()).getHeight();
					lblNewLabel.setBounds(0, 0, textWidth, textHeight);
					labelBackgroundPanel.setBounds((panel.getWidth() - textWidth - 5) / 2,
							(panel.getHeight() - textHeight - 5) / 2, textWidth + 5, textHeight + 5);

					// 여기에 다른 작업을 수행하면 됩니다.
				}
			}
		});
		contentPane.add(fontSizeSlider);
		panel = new JPanel();
		panel.setBackground(SystemColor.activeCaption);
		panel.setBounds(119, 20, 400, 168);
		contentPane.add(panel);

		lblNewLabel = new JLabel("AaZz가나다라마바사!~@");
		lblNewLabel.setForeground(mycolor);
		lblNewLabel.setFont(new Font(fontname, Font.PLAIN, fontsize));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);

		// JLabel의 크기에 맞게 Panel 크기를 동적으로 조정
		int textWidth = lblNewLabel.getFontMetrics(lblNewLabel.getFont()).stringWidth(lblNewLabel.getText());
		int textHeight = lblNewLabel.getFontMetrics(lblNewLabel.getFont()).getHeight();
		panel.setLayout(null);

		lblNewLabel.setBounds(0, 0, textWidth, textHeight);

		// JLabel을 이 새로운 JPanel에 추가
		labelBackgroundPanel = new JPanel();
		labelBackgroundPanel.setBounds((panel.getWidth() - textWidth - 5) / 2, (panel.getHeight() - textHeight - 5) / 2,
				textWidth + 5, textHeight + 5);
		labelBackgroundPanel.setBackground(mybackcolor);
		labelBackgroundPanel.add(lblNewLabel);

		panel.add(labelBackgroundPanel);
		JPanel panel_1 = new JPanel();
		panel_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (mine) {
					mycolor = colorSelet();
					panel_1.setBackground(mycolor);
					lblNewLabel.setForeground(mycolor);
				} else {
					yourcolor = colorSelet();
					panel_1.setBackground(yourcolor);
					lblNewLabel.setForeground(yourcolor);
				}
			}
		});
		panel_1.setBackground(mycolor);
		panel_1.setBounds(531, 45, 63, 25);
		contentPane.add(panel_1);

		JPanel panel_1_1 = new JPanel();
		panel_1_1.setBackground(mybackcolor);
		panel_1_1.setBounds(531, 116, 63, 25);
		panel_1_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (mine) {
					mybackcolor = colorSelet();
					panel_1_1.setBackground(mybackcolor);
					labelBackgroundPanel.setBackground(mybackcolor);
				} else {
					yourbackcolor = colorSelet();
					panel_1_1.setBackground(yourbackcolor);
					labelBackgroundPanel.setBackground(yourbackcolor);
				}
			}
		});
		contentPane.add(panel_1_1);

		JLabel lblNewLabel_1 = new JLabel("글꼴색");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		lblNewLabel_1.setBounds(524, 74, 77, 30);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_1_1 = new JLabel("배경색");
		lblNewLabel_1_1.setBackground(new Color(255, 255, 225));
		lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		lblNewLabel_1_1.setBounds(524, 144, 77, 30);
		contentPane.add(lblNewLabel_1_1);

		// createKoreanFontComboBox();
		JComboBox comboBox = createKoreanFontComboBox();
		comboBox.setBounds(372, 209, 147, 39);
		comboBox.setSelectedItem(fontname);
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fontname = (String) comboBox.getSelectedItem();
				lblNewLabel.setFont(new Font(fontname, Font.PLAIN, fontsize));
			}

		});
		contentPane.add(comboBox);

		JButton btnNewButton = new JButton("변경하기");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				o.wr.sendServer("OPTION//" + o.nick + "//" + o.fontopt.colorToString(mycolor) + "//"
						+ o.fontopt.colorToString(mybackcolor) + "//" + o.fontopt.colorToString(yourcolor) + "//"
						+ o.fontopt.colorToString(yourbackcolor) + "//" + fontsize + "//" + fontname);
				o.fontopt.mycolor = mycolor;
				o.fontopt.mybackcolor = mybackcolor;
				o.fontopt.yourcolor = yourcolor;
				o.fontopt.yourbackcolor = yourbackcolor;
				o.fontopt.fontname = fontname;
				o.fontopt.fontsize = fontsize;
				dispose();
			}
		});
		btnNewButton.setBounds(77, 265, 195, 43);
		contentPane.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("취소하기");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_1.setBounds(361, 265, 195, 43);
		contentPane.add(btnNewButton_1);

		JButton btnNewButton_2_1 = new JButton("상대 메시지");
		btnNewButton_2_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mine = false;
				panel_1.setBackground(yourcolor);
				panel_1_1.setBackground(yourbackcolor);
				lblNewLabel.setForeground(yourcolor);
				labelBackgroundPanel.setBackground(yourbackcolor);
			}
		});
		btnNewButton_2_1.setBounds(12, 116, 104, 39);
		contentPane.add(btnNewButton_2_1);

		JButton btnNewButton_2_1_1 = new JButton("내 메시지");
		btnNewButton_2_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mine = true;
				panel_1.setBackground(mycolor);
				panel_1_1.setBackground(mybackcolor);
				lblNewLabel.setForeground(mycolor);
				labelBackgroundPanel.setBackground(mybackcolor);
			}
		});
		btnNewButton_2_1_1.setBounds(12, 45, 104, 39);
		contentPane.add(btnNewButton_2_1_1);
		setVisible(true);
	}

	Color colorSelet() {
		Color selectedColor = JColorChooser.showDialog(contentPane, "Choose a Color", Color.BLACK);
		return selectedColor;
	}

	private static JComboBox<String> createKoreanFontComboBox() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fontNames = ge.getAvailableFontFamilyNames();

		// 한글이 포함된 글꼴만 필터링
		String[] koreanFonts = Arrays.stream(fontNames).filter(fontName -> fontName.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*"))
				.toArray(String[]::new);

		JComboBox<String> comboBox = new JComboBox<>(koreanFonts);
		return comboBox;
	}
}
