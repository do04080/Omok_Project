package Omok;

import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.*;

public class JoinFrame extends JFrame {
	
	
	/* Panel */
	JPanel panel = new JPanel();
	
	/* Label */
	JLabel idL = new JLabel("아이디");
	JLabel pwL = new JLabel("비밀번호");
	JLabel nnl = new JLabel("닉네임");
	JLabel nl = new JLabel("이름");
	JLabel eml = new JLabel("이메일");
	
	/* TextField */
	JTextField id = new JTextField();
	JPasswordField pw = new JPasswordField();
	JTextField nm = new JTextField();
	JTextField nnm = new JTextField();
	JTextField em = new JTextField();
	
	/* Button */
	JButton joinBtn = new JButton("가입하기");
	JButton cancelBtn = new JButton("가입취소");
	JButton overBtn1 = new JButton("id중복확인");
	JButton overBtn2 = new JButton("중복확인");
	
	Operator o = null;
	int ick = 0;
	int nck = 0;
	Socket sock;
	
	OutputStream os;
	DataOutputStream dos;
	InputStream is;
	DataInputStream dis;
	
	JoinFrame(Operator _o,Socket socket) {
		
		o = _o;
		sock = socket;
		setTitle("회원가입");
		
		/* Label 크기 작업 */
		idL.setPreferredSize(new Dimension(50, 30));
		pwL.setPreferredSize(new Dimension(50, 30));
		nnl.setPreferredSize(new Dimension(50, 30));
		nl.setPreferredSize(new Dimension(50, 30));
		eml.setPreferredSize(new Dimension(50, 30));
		
		/* TextField 크기 작업 */
		id.setPreferredSize(new Dimension(95, 30));
		pw.setPreferredSize(new Dimension(195, 30));
		nm.setPreferredSize(new Dimension(195, 30));
		nnm.setPreferredSize(new Dimension(95, 30));
		em.setPreferredSize(new Dimension(195, 30));
		
		
		/* Button 크기 작업 */
		joinBtn.setPreferredSize(new Dimension(95, 25));
		cancelBtn.setPreferredSize(new Dimension(95, 25));
		overBtn1.setPreferredSize(new Dimension(95, 30));
		overBtn2.setPreferredSize(new Dimension(95, 30));
		
		
		/* Panel 추가 작업 */
		setContentPane(panel);
		
		panel.add(nl);
		panel.add(nm);
		
		panel.add(idL);
		panel.add(id);
		panel.add(overBtn1);
		
		
		panel.add(pwL);
		panel.add(pw);
		
		panel.add(nnl);
		panel.add(nnm);
		panel.add(overBtn2);
		
		
		panel.add(eml);
		panel.add(em);
		
		panel.add(cancelBtn);
		panel.add(joinBtn);
		
		/* Button 이벤트 리스너 추가 */
		ButtonListener bl = new ButtonListener();
		
		overBtn1.addActionListener(bl);
		overBtn2.addActionListener(bl);
		cancelBtn.addActionListener(bl);
		joinBtn.addActionListener(bl);
		
		setSize(300, 250);
		setLocationRelativeTo(null);
		setResizable(false);
	}
	
	/* Button 이벤트 리스너 */
	class ButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent d) {
			JButton b = (JButton)d.getSource();
			
			/* TextField에 입력된 회원 정보들을 변수에 초기화 */
			String uid = id.getText();
			String unm = nm.getText();
			String unnm = nnm.getText();
			String uem = em.getText();
			String upass = "";
			String att = "id";
			
			for(int i=0; i<pw.getPassword().length; i++) {
				upass = upass + pw.getPassword()[i];
			}
			
			/* 가입취소 버튼 이벤트 */
			if(b.getText().equals("가입취소")) {
				dispose();
			}
			/* 아이디 이름 중복확인 버튼 이벤트*/
			else if(b.getText().equals("id중복확인")) {
				 att = "id";
				 if(uid.equals("") ) {
					JOptionPane.showMessageDialog(null, "아이디를 입력해주세요", "중복확인 실패", JOptionPane.ERROR_MESSAGE);
					System.out.println("중복확인 실패 > 중복확인 정보 미입력");
				}else if(uid != null) {
					try {
						dos = new DataOutputStream(sock.getOutputStream());
						dos.writeUTF("OVER//"+att+"//"+uid);
						dos.flush();
					new ReceiveOver(sock).start();
						
					 } catch (UnknownHostException e) {
					        e.printStackTrace();
					    } catch (IOException e) {
					        e.printStackTrace();
					    }
				}
			 
				
			}
			/* 닉네임 이름 중복확인 버튼 이벤트*/
			else if(b.getText().equals("중복확인")) {
				 att = "nickname";
				 if(unnm.equals("")) {
					JOptionPane.showMessageDialog(null, "아이디를 입력해주세요", "중복확인 실패", JOptionPane.ERROR_MESSAGE);
					System.out.println("중복확인 실패 > 중복확인 정보 미입력");
				}else if(unnm != null) {
					try {
						dos = new DataOutputStream(sock.getOutputStream());
						dos.writeUTF("OVER//"+att+"//"+unnm);
						dos.flush();
						
						new ReceiveOver(sock).start();
						
					 } catch (UnknownHostException e) {
					        e.printStackTrace();
					    } catch (IOException e) {
					        e.printStackTrace();
					    }
					
				}
			 
				
			}
			/* 가입하기 버튼 이벤트 */
			else if(b.getText().equals("가입하기")) {
				if(uid.equals("") || upass.equals("")) {
					JOptionPane.showMessageDialog(null, "모든 정보를 기입해주세요", "회원가입 실패", JOptionPane.ERROR_MESSAGE);
					System.out.println("회원가입 실패 > 회원정보 미입력");
				}
				
				else if(!uid.equals("") && !upass.equals("")) {
					
					if(ick==1 && nck==1) {
						try {
						o.jf.dos = new DataOutputStream(o.jf.sock.getOutputStream());
				        // 서버로 로그인 요청 메시지 전송
				        String msg = ("JOIN//" + unm + "//" + unnm+ "//" +uid+"//" +upass+"//" +uem);
				        dos.writeUTF(msg);
				    	ick = 0;
						nck = 0;
						dispose();
					 } catch (UnknownHostException e) {
					        e.printStackTrace();
					    } catch (IOException e) {
					        e.printStackTrace();
					    }
					
					
					}
				}else {
					System.out.println("중복확인 실패 > 중복확인 하지않음");
					JOptionPane.showMessageDialog(null, "중복확인을 해주세요", "회원가입 실패", JOptionPane.ERROR_MESSAGE);
				}
				}
			
			}
		}
	public class ReceiveOver extends Thread{
		Socket socket;

		public ReceiveOver(Socket socket) {
			this.socket = socket;
			
		}
		public void run(){
		try {
			
		        dis = new DataInputStream(socket.getInputStream());
		        
		        String msg = dis.readUTF();
		        String[] m = msg.split("//");
		        if(m[1].equals("OKAY")) {
		        	
	        		String att = m[2];
		        	System.out.println("중복확인 성공");
					JOptionPane.showMessageDialog(null, "중복확인에 성공하였습니다");
					if(att.equals("nickname")) {
		        		nck=1;
		        		interrupt();
		        	}else if(att.equals("id")) {
		        		ick=1;
		        		interrupt();
		        	
		        
		        }else if(m[1].equals("FAIL")){
		        	System.out.println("중복확인 실패");
					JOptionPane.showMessageDialog(null, "중복확인에 실패하였습니다");
					interrupt();
	        		
	}
		        }


}catch (UnknownHostException e) {
    e.printStackTrace();
} catch (IOException e) {
    e.printStackTrace();
}
}
	}
}
		