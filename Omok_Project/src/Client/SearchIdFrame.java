package Client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SearchIdFrame extends JFrame {

	private JPanel contentPane;
	private JTextField name;
	private JTextField email;
	private JTextField id;

	public SearchIdFrame(Operator o, int i) {
		setVisible(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		if (i == 0) {
			setBounds(100, 100, 408, 230);
		} else if (i == 1) {
			setBounds(100, 100, 408, 270);
		}
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		name = new JTextField();
		name.setBounds(76, 24, 273, 33);
		contentPane.add(name);
		name.setColumns(10);

		email = new JTextField();
		email.setColumns(10);
		email.setBounds(76, 83, 273, 33);
		contentPane.add(email);

		id = new JTextField();
		id.setColumns(10);
		id.setBounds(76, 141, 273, 33);
		contentPane.add(id);
		if (i == 1) {
			id.setVisible(true);
		} else {
			id.setVisible(false);
		}
		JLabel lblNewLabel = new JLabel("이름");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(0, 24, 76, 33);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("이메일");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(0, 83, 76, 33);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("아이디");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(0, 141, 76, 33);
		if (i == 1) {
			lblNewLabel_2.setVisible(true);
		} else {
			lblNewLabel_2.setVisible(false);
		}
		contentPane.add(lblNewLabel_2);

		JButton search = new JButton("검색");
		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String _i = id.getText();
				String _e = email.getText();
				String _n = name.getText();
				
				if (i == 0) {
					if(_e.equals("")||_n.equals("")) {
						showMessage("모든 정보를 입력해주세요");
					}else {
					o.sendServer("FIND//ID//" + _n + "//" + _e);
					}
				} else if (i == 1) {
					if(_e.equals("")||_n.equals("")||_i.equals("")) {
						showMessage("모든 정보를 입력해주세요");
					}else {
					o.sendServer("FIND//PASSWORD//" + _n + "//" + _e + "//" + _i);
					}
				}
			}
		});
		if (i == 1) {
			search.setBounds(117, 184, 157, 38);
		} else {
			search.setBounds(114, 138, 157, 38);

		}

		contentPane.add(search);
	}

	public void showID(String id) {
		String message = "[" + id + "] 회원님의 아이디 입니다.";
		JOptionPane.showMessageDialog(null, message, "알림", JOptionPane.INFORMATION_MESSAGE);
		dispose();
	}

	public void showMessage(String m) {
		String message = m;
		JOptionPane.showMessageDialog(null, message, "알림", JOptionPane.INFORMATION_MESSAGE);
	}
}
