package Omok;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatFrame extends JFrame {
    private JTextArea chatArea;
    private JTextField messageField;
    
	Socket sock;
	OutputStream os;
	DataOutputStream dos;
	InputStream is;
	DataInputStream dis;
	
    public ChatFrame(Socket socket) {
        // 프레임 설정
    	this.sock= socket;
        setTitle("채팅 프로그램");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 채팅창 설정
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        // 메시지 입력 필드 설정
        messageField = new JTextField();
        messageField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        add(messageField, BorderLayout.SOUTH);

        // 전송 버튼 설정
        JButton sendButton = new JButton("전송");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        add(sendButton, BorderLayout.EAST);
}
    

    private void sendMessage() {
        String message = messageField.getText();
     
			    
        if (!message.isEmpty()) {
        	
        	try {
				 dos = new DataOutputStream(sock.getOutputStream());
				 dis = new DataInputStream(sock.getInputStream());
	   		        // 서버로 로그인 요청 메시지 전송
				  String msg = ("MSG//" + message);
			        dos.writeUTF(msg);
			        messageField.setText("");
			        msg = dis.readUTF();
			        String[] m = msg.split("//");
	   		       
	   		        if(m[0].equals("MSG")) {
	   		        	String nick = m[1];
	   		        	String cmsg = m[2];
	   		        	chatArea.append(nick +" : " + cmsg + "\n");
	   		        	
	   		        	}
			        // 서버로 로그인 요청 메시지 전송
			      
			       
			        
				 } catch (UnknownHostException e) {
				        e.printStackTrace();
				    } catch (IOException e) {
				        e.printStackTrace();
				    }
        }
    }
}