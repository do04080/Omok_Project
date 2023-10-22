package Omok;
import java.util.Vector;
import java.io.*;
import java.net.*;
public class Operator {

	DBTable db = null;
	LoginFrame mf = null;
	JoinFrame jf = null;
	Server server = null;
	GameUser user = null;
	ChatFrame chat = null;
	Gui gui = null;
	Socket socket;
	CCUser ccu = null;
	OutputStream os;
	DataOutputStream dos;
	InputStream is;
	DataInputStream dis;
	public Operator(Socket socket) {
		Socket sock = socket;
		
	}
	
	
	public static void main(String[] args) {
		
		
		 try {
	            Socket socket = new Socket("127.0.0.1", 1228);

	            Operator opt = new Operator(socket);
	    		opt.server = new Server();
	    		opt.user = new GameUser();
	    		opt.chat = new ChatFrame(socket);
	    		opt.db = new DBTable();
	    		opt.jf = new JoinFrame(opt,socket);
	    		opt.mf = new LoginFrame(opt,socket);
	    		
	    		
	    		
				
	    		
	            // 입력 스트림
	            // 서버에서 보낸 데이터를 받음
	            BufferedReader in = new BufferedReader(new InputStreamReader(
	                    socket.getInputStream()));

	            // 출력 스트림
	            // 서버에 데이터를 송신
	            
	            
	            
	            new read_C(socket).start();
	            System.out.println("서버로 부터의 응답 : ");
	            
	        
	   		
	    		
	            // 서버 접속 끊기
	         

	        } catch (UnknownHostException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		 
		

	}
	
}
class read_C extends Thread {
    Socket socket;

    public read_C(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(socket.getInputStream());
            while (true) {
                
                // POINT) server가 write 하면 동작한다
                // readUTF는 상대가 입력하지 않으면 계속 대기한다(스캐너처럼)
                // while이 쓸데없이 계속 돌지 않는 이유다
                String msg = dis.readUTF();
                    System.out.println();
                    System.out.println("상대방 msg : " + msg);
                if (msg.equals("exit"))
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                dis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

