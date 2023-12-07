package Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.io.*;
import java.net.*;


public class Server {
	ServerSocket ss = null;
	
	Vector<CCUser> alluser;		//연결된 모든 클라이언트
	Vector<CCUser> waituser;	//대기실에 있는 클라이언트
	Vector<Room> room;			//생성된 Room
	

	public static void main(String[] args) {
		Server server = new Server();
		
		server.alluser = new Vector<>();
		server.waituser = new Vector<>();
		server.room = new Vector<>();
		
		try {
			//서버 소켓 준비
			server.ss = new ServerSocket(1228);
			System.out.println("[Server] 서버 소켓 준비 완료");
			
			//클라이언트의 연결 요청을 상시 대기. 
			while(true) {
				Socket socket = server.ss.accept();
				CCUser c = new CCUser(socket, server);	//소켓과 서버를 넘겨 CCUser(접속한 유저 관리)객체 생성
				
				c.start();	//CCUser 스레드 시작
			}
		} catch(SocketException e) {	//각 오류를 콘솔로 알린다.
			System.out.println("[Server] 서버 소켓 오류 > " + e.toString());
		} catch(IOException e) {
			System.out.println("[Server] 입출력 오류 > " + e.toString());
		}
	}
}