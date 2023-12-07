package Client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SoundControlFrame extends JFrame {

	private JPanel contentPane;
	Operator o;

	public SoundControlFrame(Operator _o) {
		o = _o;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 212, 494);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		Float stone = o.fontopt.stonevolume;
		Float music = o.fontopt.musicvolume;

		JSlider stoneslider = new JSlider();
		stoneslider.setOrientation(SwingConstants.VERTICAL);
		stoneslider.setBounds(12, 45, 62, 355);
		stoneslider.setMajorTickSpacing(10);
		stoneslider.setPaintTicks(true);
		stoneslider.setValue((int) (o.fontopt.stonevolume * 100));

		stoneslider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int svolumeValue = stoneslider.getValue();
				float svolume = svolumeValue / 100.0f;
				o.fontopt.stonevolume = svolume;
			}
		});
		contentPane.add(stoneslider);
		JSlider musicslider = new JSlider();
		musicslider.setOrientation(SwingConstants.VERTICAL);
		musicslider.setBounds(120, 45, 62, 355);
		musicslider.setMajorTickSpacing(10);
		musicslider.setPaintTicks(true);
		musicslider.setValue((int) (o.fontopt.musicvolume * 100));

		musicslider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int mvolumeValue = musicslider.getValue();
				float mvolume = mvolumeValue / 100.0f;
				o.fontopt.musicvolume = mvolume;
			}
		});
		contentPane.add(musicslider);

		JLabel lblNewLabel = new JLabel("효과음");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(12, 10, 62, 26);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("배경음악");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(120, 9, 62, 26);
		contentPane.add(lblNewLabel_1);

		JButton btnNewButton = new JButton("변경");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				o.wr.sendServer("CMUSIC//" + o.nick + "//" + o.fontopt.musicvolume + "//" + o.fontopt.stonevolume);
				o.sp.setmusicSound();
				dispose();
			}
		});
		btnNewButton.setBounds(12, 410, 75, 37);
		contentPane.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("취소");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				o.fontopt.stonevolume = stone;
				o.fontopt.musicvolume = music;
				dispose();
			}
		});
		btnNewButton_1.setBounds(107, 410, 75, 37);
		contentPane.add(btnNewButton_1);
	}
}
