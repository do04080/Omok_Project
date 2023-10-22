package Omok;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class GameUser {
	
	private Room room; 		// 유저가 속한 룸이다.
	private Socket sock;	// 소켓 object
	private String nickName;	// 닉네임
	
	 public Socket getSock() {
	        return sock;
	    }

	    public void setSock(Socket sock) {
	        this.sock = sock;

    	}
	


}
