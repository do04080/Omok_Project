package Omok;

import java.io.InputStreamReader;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Vector;
import java.io.*;

class CCUser extends Thread{
	Server server;
	Socket socket;
	
	/* 각 객체를 Vector로 관리 */
	Vector<CCUser> auser;	//연결된 모든 클라이언트
	Vector<CCUser> wuser;	//대기실에 있는 클라이언트
	Vector<Room> room;		//생성된 Room
	
	DBTable db = new DBTable();
	
	/* 메시지 송수신을 위한 필드 */
	OutputStream os;
	DataOutputStream dos;
	InputStream is;
	DataInputStream dis;
	
	String msg;			//수신 메시지를 저장할 필드
	String nickname;	//클라이언트의 닉네임을 저장할 필드
	
	Room myRoom;		//입장한 방 객체를 저장할 필드
	
	/* 각 메시지를 구분하기 위한 태그 */
	final String loginTag = "LOGIN";	//로그인
	final String joinTag = "JOIN";		//회원가입
	final String overTag = "OVER";		//중복확인
	final String viewTag = "VIEW";		//회원정보조회
	final String changeTag = "CHANGE";	//회원정보변경
	final String rankTag = "RANK";		//전적조회(전체회원)
	final String croomTag = "CROOM";	//방생성
	final String vroomTag = "VROOM";	//방목록
	final String uroomTag = "UROOM";	//방유저
	final String eroomTag = "EROOM";	//방입장
	final String cuserTag = "CUSER";	//접속유저
	final String searchTag = "SEARCH";	//전적조회(한명)
	final String pexitTag = "PEXIT";	//프로그램종료
	final String rexitTag = "REXIT";	//방퇴장
	final String omokTag = "OMOK";		//오목
	final String winTag = "WIN";		//승리
	final String loseTag = "LOSE";		//패배
	final String recordTag = "RECORD";	//전적업데이트
	final String msgTag = "MSG";        //메세지 송수신
	
	CCUser(Socket _s, Server _ss) {
		this.socket = _s;
		this.server = _ss;
		
		auser = server.alluser;
		wuser = server.waituser;
		room = server.room;		
	}  //CCUser()
	public void run() {
		try {
			System.out.println("[Server] 클라이언트 접속 > " + this.socket.toString());
			
			os = this.socket.getOutputStream();
			dos = new DataOutputStream(os);
			is = this.socket.getInputStream();
			dis = new DataInputStream(is);
			
			while(true) {
				msg = dis.readUTF();	//메시지 수신을 상시 대기한다.
				
				String[] m = msg.split("//");	//msg를 "//"로 나누어 m[]배열에 차례로 집어넣는다.
				
				// 수신받은 문자열들의 첫 번째 배열(m[0])은 모두 태그 문자. 각 기능을 분리한다.
				/* 로그인 */
				if(m[0].equals(loginTag)) {
					String mm = db.loginCheck(m[1], m[2]);
					if(!mm.equals("null")) {	//로그인 성공
						nickname = mm;		//로그인한 사용자의 닉네임을 필드에 저장
						
						auser.add(this);	//모든 접속 인원에 추가
						wuser.add(this);	//대기실 접속 인원에 추가
						
						dos.writeUTF(loginTag + "//OKAY");
						

						this.dos.writeUTF(loginTag + "//OKAY");
						
						
						msg = dis.readUTF();
						
						if(msg.equals(loginTag)) {
						sendWait(connectedUser());//대기실 접속 유저에 모든 접속 인원을 전송
						}
						
						if(room.size() > 0) {	//생성된 방의 개수가 0 이상일 때
							sendWait(roomInfo());	//대기실 접속 인원에 방 목록을 전송
						}
						
					}
					
					else {	//로그인 실패
						dos.writeUTF(loginTag + "//FAIL");
					}
				}  //로그인 if문
				
				/* 회원가입 */
				else if(m[0].equals(joinTag)) {
					if(db.joinCheck(m[1], m[2], m[3], m[4], m[5])) {	//회원가입 성공
						dos.writeUTF(joinTag + "//OKAY");
					}
					
					else {	//회원가입 실패
						dos.writeUTF(joinTag + "//FAIL");
					}
				}  //회원가입 if문
				
				/* 중복확인 */
				else if(m[0].equals(overTag)) {
					if(!db.overCheck(m[1], m[2])) {	//사용 가능
						dos.writeUTF(overTag + "//OKAY//" +m[1]);
					}
					
					else {	//사용 불가능
						dos.writeUTF(overTag + "//FAIL");
					}
				}  //중복확인 if문
				
				/* 회원정보 조회 */
				else if(m[0].equals(viewTag)) {
					if(!db.viewInfo(nickname).equals("null")) {	//조회 성공
						dos.writeUTF(viewTag + "//" + db.viewInfo(nickname));	//태그와 조회한 내용을 같이 전송
					}
					
					else {	//조회 실패
						dos.writeUTF(viewTag + "//FAIL");
					}
				}  //회원정보 조회 if문
				
				/* 회원정보 변경 */
				else if(m[0].equals(changeTag)) {
					if(db.changeInfo(nickname, m[1], m[2])) {	//변경 성공
						dos.writeUTF(changeTag + "//OKAY");
					}
					
					else {	//변경 실패
						dos.writeUTF(changeTag + "//FAIL");
					}
				}  //회원정보 변경 if문
				
				/* 전체 전적 조회 */
				else if(m[0].equals(rankTag)) {
					if(!db.viewRank().equals("")) {	//조회 성공
						dos.writeUTF(rankTag + "//" + db.viewRank());	//태그와 조회한 내용을 같이 전송
					}
					
					else {	//조회 실패
						dos.writeUTF(rankTag + "//FAIL");
					}
				}  //전체 전적 조회 if문

				/* 방 생성 */
				else if(m[0].equals(croomTag)) {
					myRoom = new Room();	//새로운 Room 객체 생성 후 myRoom에 초기화
					myRoom.title = m[1];	//방 제목을 m[1]로 설정
					myRoom.count++;			//방의 인원수 하나 추가
					
					room.add(myRoom);		//room 배열에 myRoom을 추가
					
					myRoom.ccu.add(this);	//myRoom의 접속인원에 클라이언트 추가
					wuser.remove(this);		//대기실 접속 인원에서 클라이언트 삭제
					
					dos.writeUTF(croomTag + "//OKAY");
					System.out.println("[Server] "+ nickname + " : 방 '" + m[1] + "' 생성");
					
					sendWait(roomInfo());	//대기실 접속 인원에 방 목록을 전송
					sendRoom(roomUser());	//방에 입장한 인원에 방 인원 목록을 전송
				}  //방 생성 if문
				/* 방 입장 */
				else if(m[0].equals(eroomTag)) {
					for(int i=0; i<room.size(); i++) {	//생성된 방의 개수만큼 반복
						Room r = room.get(i);
						if(r.title.equals(m[1])) {	//방 제목이 같고
							
							if(r.count < 2) {			//방 인원수가 2명보다 적을 때 입장 성공
								myRoom = room.get(i);	//myRoom에 두 조건이 맞는 i번째 room을 초기화
								myRoom.count++;			//방의 인원수 하나 추가
								
								wuser.remove(this);		//대기실 접속 인원에서 클라이언트 삭제
								myRoom.ccu.add(this);	//myRoom의 접속 인원에 클라이언트 추가
								
								sendWait(roomInfo());	//대기실 접속 인원에 방 목록을 전송
								sendRoom(roomUser());	//방에 입장한 인원에 방 인원 목록을 전송
								
								dos.writeUTF(eroomTag + "//OKAY");
								System.out.println("[Server] " + nickname + " : 방 '" + m[1] + "' 입장");
							}
							
							else {	//방 인원수가 2명 이상이므로 입장 실패
								dos.writeUTF(eroomTag + "//FAIL");
								System.out.println("[Server] 인원 초과. 입장 불가능");
							}
						}
						
						else {	//같은 방 제목이 없으니 입장 실패
							dos.writeUTF(eroomTag + "//FAIL");
							System.out.println("[Server] " + nickname + " : 방 '" + m[1] + "' 입장 오류");
						}
					}
				} //방 입장 if문
				
				/* 전적 조회 */
				else if(m[0].equals(searchTag)) {
					String mm = db.searchRank(m[1]);
					
					if(!mm.equals("null")) {	//조회 성공
						dos.writeUTF(searchTag + "//" + mm);	//태그와 조회한 내용을 같이 전송
					}
					
					else {	//조회 실패
						dos.writeUTF(searchTag + "//FAIL");
					}
				} //전적 조회 if문
				
				/* 프로그램 종료 */
				else if(m[0].equals(pexitTag)) {
					auser.remove(this);		//전체 접속 인원에서 클라이언트 삭제
					wuser.remove(this);		//대기실 접속 인원에서 클라이언트 삭제
					
					sendWait(connectedUser());	//대기실 접속 인원에 전체 접속 인원을 전송
				} //프로그램 종료 if문
				
				/* 방 퇴장 */
				else if(m[0].equals(rexitTag)) {
					myRoom.ccu.remove(this);	//myRoom의 접속 인원에서 클라이언트 삭제
					myRoom.count--;				//myRoom의 인원수 하나 삭제
					wuser.add(this);			//대기실 접속 인원에 클라이언트 추가
					
					System.out.println("[Server] " + nickname + " : 방 '" + myRoom.title + "' 퇴장");
					
					if(myRoom.count==0) {	//myRoom의 인원수가 0이면 myRoom을 room 배열에서 삭제
						room.remove(myRoom);
					}
					
					if(room.size() != 0) {	//생성된 room의 개수가 0이 아니면 방에 입장한 인원에 방 인원 목록을 전송
						sendRoom(roomUser());
						
					}
					
					sendWait(roomInfo());		//대기실 접속 인원에 방 목록을 전송
					sendWait(connectedUser());	//대기실 접속 인원에 전체 접속 인원을 전송
				} //방 퇴장 if문
				/* 오목 */
				else if(m[0].equals(omokTag)) {
					for(int i=0; i<myRoom.ccu.size(); i++) {	//myRoom의 인원수만큼 반복
						
						if(!myRoom.ccu.get(i).nickname.equals(nickname)) {	//방 접속 인원 중 클라이언트와 다른 닉네임의 클라이언트에게만 전송
							myRoom.ccu.get(i).dos.writeUTF(omokTag + "//" + m[1] + "//" + m[2] + "//" + m[3]);
						}
					}
				}  //오목 if문
				
				/* 승리 및 전적 업데이트 */
				else if(m[0].equals(winTag)) {
					System.out.println("[Server] " + nickname + " 승리");
					
					if(db.winRecord(nickname)) {	//전적 업데이트가 성공하면 업데이트 성공을 전송
						dos.writeUTF(recordTag + "//OKAY");
					} else {						//전적 업데이트가 실패하면 업데이트 실패를 전송
						dos.writeUTF(recordTag + "//FAIL");
					}
					
					for(int i=0; i<myRoom.ccu.size(); i++) {	//myRoom의 인원수만큼 반복
						
						/* 방 접속 인원 중 클라이언트와 다른 닉네임의 클라이언트일때만 */
						if(!myRoom.ccu.get(i).nickname.equals(nickname)) {
							myRoom.ccu.get(i).dos.writeUTF(loseTag + "//");
							
							if(db.loseRecord(myRoom.ccu.get(i).nickname)) {	//전적 업데이트가 성공하면 업데이트 성공을 전송
								myRoom.ccu.get(i).dos.writeUTF(recordTag + "//OKAY");
							} else {										//전적 업데이트가 실패하면 업데이트 실패를 전송
								myRoom.ccu.get(i).dos.writeUTF(recordTag + "//FAIL");
							}
						}
					}
				}  //승리 및 전적 업데이트 if문
				
				/* 패배, 기권 및 전적 업데이트 */
				else if(m[0].equals(loseTag)) {
					if(myRoom.count==1) {	//기권을 했는데 방 접속 인원이 1명일 때 전적 미반영을 전송
						dos.writeUTF(recordTag + "//NO");
					}
					
					else if(myRoom.count==2) {	//기권 및 패배를 했을 때 방 접속 인원이 2명일 때
						dos.writeUTF(loseTag + "//");
						
						if(db.loseRecord(nickname)) {	//전적 업데이트가 성공하면 업데이트 성공을 전송
							dos.writeUTF(recordTag + "//OKAY");
						} else {						//전적 업데이트가 실패하면 업데이트 실패를 전송
							dos.writeUTF(recordTag + "//FAIL");
						}
						
						for(int i=0; i<myRoom.ccu.size(); i++) {	//myRoom의 인원수만큼 반복
							
							/* 방 접속 인원 중 클라이언트와 다른 닉네임의 클라이언트일때만 */
							if(!myRoom.ccu.get(i).nickname.equals(nickname)) {
								myRoom.ccu.get(i).dos.writeUTF(winTag + "//");
								
								if(db.winRecord(myRoom.ccu.get(i).nickname)) {	//전적 업데이트가 성공하면 업데이트 성공을 전송
									myRoom.ccu.get(i).dos.writeUTF(recordTag + "//OKAY");
								} else {										//전적 업데이트가 실패하면 업데이트 실패를 전송
									myRoom.ccu.get(i).dos.writeUTF(recordTag + "//FAIL");
								}
							}
						}
					}
				}  //패배, 기권 및 전적 업데이트 if문
				else if(m[0].equals(msgTag)) {
					dos.writeUTF(nickname + m[1]);
				}//채팅 msg
			}  //while문
		} catch(IOException e) {
			System.out.println("[Server] 입출력 오류 > " + e.toString());
		}
	}  //run()
	/* 현재 존재하는 방의 목록을 조회하는 메소드 */
	String roomInfo() {
		String msg = vroomTag + "//";
		
		for(int i=0; i<room.size(); i++) {
			msg = msg + room.get(i).title + " : " + room.get(i).count + "@";
		}
		return msg;
	}
	
	/* 클라이언트가 입장한 방의 인원을 조회하는 메소드 */
	String roomUser() {
		String msg = uroomTag + "//";
		
		for(int i=0; i<myRoom.ccu.size(); i++) {
			msg = msg + myRoom.ccu.get(i).nickname + "@";
		}
		return msg;
	}
	
	/* 접속한 모든 회원 목록을 조회하는 메소드 */
	String connectedUser() {
		String msg = cuserTag + "//";
		
		for(int i=0; i<auser.size(); i++) {
			msg = msg + auser.get(i).nickname + "@";
		}
		return msg;
	}
	
	/* 대기실에 있는 모든 회원에게 메시지 전송하는 메소드 */
	void sendWait(String m) {
		for(int i=0; i<wuser.size(); i++) {
			try {
				wuser.get(i).dos.writeUTF(m);
			} catch(IOException e) {
				wuser.remove(i--);
			}
		}
	}
	
	/* 방에 입장한 모든 회원에게 메시지 전송하는 메소드 */
	void sendRoom(String m) {
		for(int i=0; i<myRoom.ccu.size(); i++) {
			try {
				myRoom.ccu.get(i).dos.writeUTF(m);
			} catch(IOException e) {
				myRoom.ccu.remove(i--);
			}
		}
	}
}  //CCUser 클래스


	
	

