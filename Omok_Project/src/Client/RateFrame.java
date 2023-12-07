package Client;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class RateFrame extends JFrame {

	private JPanel contentPane;

	public RateFrame(BufferedImage img, String user) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 647, 502);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		String[] info = user.split("@");

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("로딩중");
		lblNewLabel.setBounds(12, 10, 371, 445);
		ImageIcon profileicon = new ImageIcon(img);
		
		Image profile = profileicon.getImage().getScaledInstance(lblNewLabel.getWidth(), lblNewLabel.getHeight(),
				Image.SCALE_SMOOTH);
		lblNewLabel.setIcon(new ImageIcon(profile));
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel(info[0]);
		lblNewLabel_1.setFont(new Font("맑은 고딕", Font.PLAIN, 20));

		lblNewLabel_1.setBounds(436, 115, 185, 55);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_1_1 = new JLabel(info[1] + " 승 " + info[2] + " 패");
		lblNewLabel_1_1.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
		lblNewLabel_1_1.setBounds(436, 266, 185, 55);
		contentPane.add(lblNewLabel_1_1);
	}

}
