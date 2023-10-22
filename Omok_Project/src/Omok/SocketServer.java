package Omok;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
	try {
		int socketPost = 1234;
		ServerSocket socket = new ServerSocket(socketPost);
		Socket socketUser = null;
		System.out.println("socket : " + socketPost + "으로 서버가 열렸습니다");
		
		while(true) {
			socketUser = socket.accept();
			System.out.println("Client가 접속함" + socketUser.getInetAddress()); 
		}
	}catch(Exception e) {
		e.printStackTrace();
	}
}
}