package Client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JList;
import javax.swing.JTextField;

public class OperatorOptionFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textField;

	public OperatorOptionFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 454, 359);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnNewButton = new JButton("회원정보 관리");
		btnNewButton.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		btnNewButton.setBounds(262, 166, 165, 57);
		contentPane.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("채팅 정보 관리");
		btnNewButton_1.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		btnNewButton_1.setBounds(262, 246, 165, 57);
		contentPane.add(btnNewButton_1);

		JButton btnNewButton_1_1 = new JButton("접속중인 유저");
		btnNewButton_1_1.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		btnNewButton_1_1.setBounds(262, 10, 165, 57);
		contentPane.add(btnNewButton_1_1);

		JButton btnNewButton_1_1_1 = new JButton("게임 방 관리");
		btnNewButton_1_1_1.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		btnNewButton_1_1_1.setBounds(262, 88, 165, 57);
		contentPane.add(btnNewButton_1_1_1);

		JList list = new JList();
		list.setBounds(28, 82, 204, 221);
		contentPane.add(list);

		textField = new JTextField();
		textField.setBounds(28, 22, 156, 39);
		contentPane.add(textField);
		textField.setColumns(10);

		JButton btnNewButton_2 = new JButton("검색");
		btnNewButton_2.setBounds(196, 22, 56, 39);
		contentPane.add(btnNewButton_2);
	}
}
