package Omok;

import java.awt.Container;


import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class Gui extends JFrame{
	private Container c;
	MapSize size = new MapSize();
	  private JTextArea chatArea;
	    private JTextField messageField;
	
	public Gui(String title) {
		setTitle(title);
		c= getContentPane();
		setBounds(200,20,650,700);
		c.setLayout(null);
		Map map=new Map(size);
		DrawBoard d = new DrawBoard(size,map);
		setContentPane(d);
		addMouseListener(new MouseEventHandler(map, size, d, this));
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		
		
	}
	public void showPopUP(String message) {
		JOptionPane.showMessageDialog(this, message,"",JOptionPane.INFORMATION_MESSAGE);;
		dispose();
		new Gui("오목");
	}

}
