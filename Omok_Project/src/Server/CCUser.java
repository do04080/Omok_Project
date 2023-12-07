package Server;

import java.net.Socket;
import java.net.SocketException;
import java.nio.file.*;
import java.util.Random;
import java.util.Vector;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

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
	String status;      //유저의 위치
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
	final String wroommsgTag = "WMSG";        //메세지 송수신
	
	final String waittag = "WAIT"; 
	final String objtag = "OBJ"; 
	final String playertag = "PLAYER"; 
	
	
	String originalFilePath  ="C:\\Users\\도석환\\eclipse-workspace\\Omok_Project\\img\\";
	String gameFilePath  ="C:\\Users\\도석환\\eclipse-workspace\\Omok_Project\\screenshot\\";
	
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
					if(!mm.equals("null")&&userStatus(mm).equals("")) {	//로그인 성공
						nickname = mm;		//로그인한 사용자의 닉네임을 필드에 저장
						status = waittag;
						auser.add(this);	//모든 접속 인원에 추가
						wuser.add(this);	//대기실 접속 인원에 추가
						File file = openFile(nickname);
						long fileSize = file.length();  // 파일 크기		
						
						dos.flush();
						dos.writeUTF(loginTag + "//OKAY//"+db.searchRank(nickname)+"//"+fileSize+"//"+db.getFriendList(nickname)+"//"+db.getFontOption(nickname));
						sendImage(file);
						
						sendWait(connectedUser());//대기실 접속 유저에 모든 접속 인원을 전송
						
						
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
					/*"JOIN//" + uid + "//" + upass+ "//" +unm+"//" +unnm+"//"+sex+"//" +uzip+"//"
		        +uadd+"//" +uem+"//"+birth+"//"+ fileSize+ "//" + ext*/
					
					if(db.joinCheck(m[1], m[2], m[3], m[4], m[5],m[6],m[7],m[8],m[9],m[4]+m[11])) {	//회원가입 성공
						db.insertFontOption(m[4]);
						long fileSize = Long.parseLong(m[10]);  // 파일 크기
					    byte[] fileData = new byte[(int) fileSize];
					    dis.readFully(fileData);

					    // 파일을 서버에 저장
					    File receivedFile = new File(originalFilePath + m[4]+m[11]);
					    FileOutputStream fileOutputStream = new FileOutputStream(receivedFile);
					    fileOutputStream.write(fileData);
					    fileOutputStream.close();
						dos.writeUTF(joinTag + "//OKAY");
					}
					
					else {	//회원가입 실패
						dos.writeUTF(joinTag + "//FAIL");
					}
				}  //회원가입 if문
				
				/* 중복확인 */
				else if(m[0].equals(overTag)) {
					if(!db.overCheck(m[1], m[2])) {	//사용 가능
						
						dos.writeUTF(overTag + "//"+"OKAY//" +m[1]);
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
				
				else if(m[0].equals("OCHANGE")) {
					String originFilename = db.imgInfo(m[1]);
					db.updateFriendshipsForNicknameChange(m[1], m[5]);
					db.updateUsername(m[1],m[5]);
					 File fileToDelete = new File(originalFilePath+originFilename);
					 fileToDelete.delete();
					long fileSize = Long.parseLong(m[11]);  // 파일 크기
					byte[] fileData = new byte[(int) fileSize];
				        dis.readFully(fileData); 
					File receivedFile = new File(originalFilePath + m[12]);
					FileOutputStream fileOutputStream = new FileOutputStream(receivedFile);
					fileOutputStream.write(fileData);
					fileOutputStream.close();
					
					if(db.optChange(m[1], m[2], m[3], m[4], m[5],m[6],m[7],m[8],m[9],m[10],m[12])) {
						
						dos.writeUTF(changeTag + "//OKAY//"+db.OPTInfo());
					}else {
						dos.writeUTF(changeTag + "//FAIL");
					}
						
				}
				/* 회원정보 변경 */
				else if(m[0].equals(changeTag)) {
					String originFilename = db.imgInfo(m[1]);
					db.updateFriendshipsForNicknameChange(m[1], m[3]);
					db.updateUsername(m[1],m[3]);
					System.out.println(m[1]+ m[2]+ m[3]);
					
					if(db.changeInfo(m[1], m[2], m[3])) {	//변경 성공
						if(m[2].equals("img")) {
						long fileSize = Long.parseLong(m[4]);  // 파일 크기
						byte[] fileData = new byte[(int) fileSize];
					        dis.readFully(fileData); 
					        
					        // 파일을 서버에 저장
					        File receivedFile = new File(originalFilePath + m[3]);
						FileOutputStream fileOutputStream = new FileOutputStream(receivedFile);
						fileOutputStream.write(fileData);
						fileOutputStream.close();
						}if(m[2].equals("Nickname")) {											    
							String newFileName = db.imgInfo(m[3]);
					        try {
					            // 원본 파일 경로를 나타내는 Path 객체 생성
					            Path originalPath = Paths.get(originalFilePath+originFilename);
					            // 변경할 파일 경로를 나타내는 Path 객체 생성
					            Path newPath = originalPath.resolveSibling(newFileName);
					            // 파일 이름 변경
					            Files.move(originalPath, newPath);
						} catch (Exception e) {
				            e.printStackTrace();
				        }
						}
						dos.writeUTF(changeTag + "//OKAY//"+db.OPTInfo());
					
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
					myRoom.acount++;			//방의 인원수 하나 추가
					myRoom.pcount++;			//방의 플레이어 인원수 하나 추가
					
					room.add(myRoom);		//room 배열에 myRoom을 추가
					
					myRoom.ccu.add(this);	//myRoom의 접속인원에 클라이언트 추가
					myRoom.player.add(this); //myRoom의 플레이어에 클라이언트 추가
					wuser.remove(this);		//대기실 접속 인원에서 클라이언트 삭제
					
					dos.writeUTF(croomTag + "//OKAY//"+m[1]+"//PLAYER//"+myRoom.title);
					System.out.println("[Server] "+ nickname + " : 방 '" + m[1] + "' 생성");
					
					sendWait(roomInfo());	//대기실 접속 인원에 방 목록을 전송
					sendRoom(roomUser());	//방에 입장한 인원에 방 인원 목록을 전송
				}  //방 생성 if문
				/* 방 입장 */
				else if(m[0].equals(eroomTag)) {
					for(int i=0; i<room.size(); i++) {	//생성된 방의 개수만큼 반복
						Room r = room.get(i);
						if(r.title.equals(m[1])) {	//방 제목이 같고
							
							if(r.pcount < 2) {			//방 인원수가 2명보다 적을 때 입장 성공
								myRoom = room.get(i);	//myRoom에 두 조건이 맞는 i번째 room을 초기화
								myRoom.acount++;			//방의 인원수 하나 추가
								myRoom.pcount++;			//방의 플레이어 인원수 하나 추가
								
								wuser.remove(this);		//대기실 접속 인원에서 클라이언트 삭제
								myRoom.player.add(this);//myRoom의 플레이어에 클라이언트 추가
								myRoom.ccu.add(this);	//myRoom의 접속 인원에 클라이언트 추가
							
								sendWait(roomInfo());	//대기실 접속 인원에 방 목록을 전송
								sendRoom(roomUser());	//방에 입장한 인원에 방 인원 목록을 전송
								
								status = playertag;
								dos.writeUTF(eroomTag + "//OKAY//"+m[1]+"//"+myRoom.rmsg+"//"+myRoom.romok+"//PLAYER//"+myRoom.title);
								sendRoom("OUSER//"+objUser());
								System.out.println("[Server] " + nickname + " : 방 '" + m[1] + "' 입장");
							}
							
							else {	//방 인원수가 2명 이상이므로 입장 실패
								myRoom = room.get(i);	//myRoom에 두 조건이 맞는 i번째 room을 초기화
								myRoom.acount++;			//방의 인원수 하나 추가
								myRoom.ocount++;			//방의 관전자 인원수 하나 추가
								
								wuser.remove(this);		//대기실 접속 인원에서 클라이언트 삭제
								myRoom.obj.add(this);   //myRoom의 관전자에 클라이언트 추가
								myRoom.ccu.add(this);	//myRoom의 접속 인원에 클라이언트 추가
								
								status = objtag;
								dos.writeUTF(eroomTag + "//OKAY//"+m[1]+"//"+myRoom.rmsg+"//"+myRoom.romok+"//OBJ//"+myRoom.title);
								sendRoom("OUSER//"+objUser());
								System.out.println("[Server] " + nickname + " : 방 '" + m[1] + "' 입장");
								System.out.println("[Server] 인원 초과. 관전자");
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
					File file = openFile(m[1]);
					long filesize = file.length();
					if(!mm.equals("null")) {	//조회 성공
						dos.writeUTF(searchTag + "//" + mm+"//"+filesize);	//태그와 조회한 내용을 같이 전송
						sendImage(file);
					}
					
					else {	//조회 실패
						dos.writeUTF(searchTag + "//FAIL");
					}
				} //전적 조회 if문
				
				/* 프로그램 종료 */
				else if(m[0].equals(pexitTag)) {
					auser.remove(this);		//전체 접속 인원에서 클라이언트 삭제
					wuser.remove(this);		//대기실 접속 인원에서 클라이언트 삭제
					dos.writeUTF("EXIT");
					sendWait(connectedUser());	//대기실 접속 인원에 전체 접속 인원을 전송
					
				} //프로그램 종료 if문
				
				/* 방 퇴장 */
				else if(m[0].equals(rexitTag)) {
					myRoom.ccu.remove(this);	//myRoom의 접속 인원에서 클라이언트 삭제
					myRoom.acount--;				//myRoom의 인원수 하나 삭제
					if(m[1].equals("PLAYER")) {
						myRoom.player.remove(this);
						myRoom.pcount--;
						
						sendRimg();
						
					}if(m[1].equals("OBJ")) {
						myRoom.obj.remove(this);
						myRoom.ocount--;
						sendRoom("OUSER//"+objUser());
					}
					status = waittag;
					dos.writeUTF("REXIT");
					wuser.add(this);			//대기실 접속 인원에 클라이언트 추가
					System.out.println("[Server] " + nickname + " : 방 '" + myRoom.title + "' 퇴장");
					
					if(myRoom.acount==0) {	//myRoom의 인원수가 0이면 myRoom을 room 배열에서 삭제
						room.remove(myRoom);
					}
					
					if(room.size() != 0) {	//생성된 room의 개수가 0이 아니면 방에 입장한 인원에 방 인원 목록을 전송
						sendRoom(roomUser());
						
					}
					
					sendWait(roomInfo());		//대기실 접속 인원에 방 목록을 전송
					sendWait(connectedUser());	//대기실 접속 인원에 전체 접속 인원을 전송
				} //방 퇴장 if문
				/* 오목 */
				else if(m[0].equals("")) {
					for(int i=0; i<myRoom.ccu.size(); i++) {	//myRoom의 인원수만큼 반복
						
						if(!myRoom.ccu.get(i).nickname.equals(nickname)) {	//방 접속 인원 중 클라이언트와 다른 닉네임의 클라이언트에게만 전송
							myRoom.ccu.get(i).dos.writeUTF(omokTag + "//" + m[1] + "//" + m[2] + "//" + m[3]);
						}
					}
				}  //오목 if문
				
				/* 승리 및 전적 업데이트 */
				else if(m[0].equals(winTag)) {
					System.out.println("[Server] " + nickname + " 승리");
					myRoom.playing=false;
					if(db.winRecord(nickname)) {	//전적 업데이트가 성공하면 업데이트 성공을 전송
						sendRoom(recordTag + "//OKAY//"+db.searchRank(nickname));
					} else {						//전적 업데이트가 실패하면 업데이트 실패를 전송
						sendRoom(recordTag + "//FAIL//");
					}
				}  //승리 및 전적 업데이트 if문
				
				/* 패배, 기권 및 전적 업데이트 */
				else if(m[0].equals(loseTag)) {
					System.out.println(myRoom.pcount);
					if(myRoom.pcount==1) {	//기권을 했는데 방 접속 인원이 1명일 때 전적 미반영을 전송
						dos.writeUTF(recordTag + "//NO");
					}
					else if(myRoom.pcount==2) {	//기권 및 패배를 했을 때 방 접속 인원이 2명일 때
						System.out.println("lose실행");
						myRoom.playing=false;
						LocalDateTime currentTime = LocalDateTime.now();
						String time = db.formatTimestamp(currentTime);
						db.insertRecord(myRoom.black,myRoom.white,myRoom.romok,m[2],time);
						long fileSize = Long.parseLong(m[3]);  // 파일 크기
						byte[] fileData = new byte[(int) fileSize];
					        dis.readFully(fileData); 
					        
					        // 파일을 서버에 저장
					        File receivedFile = new File(gameFilePath + time+".png");
						FileOutputStream fileOutputStream = new FileOutputStream(receivedFile);
						fileOutputStream.write(fileData);
						fileOutputStream.close();
						myRoom.romok = ".";
						if(m[1].equals("BTN")) {
							for(int i=0; i<myRoom.player.size(); i++) {
								if(!myRoom.player.get(i).nickname.equals(nickname)) {
									sendRoom("BTN//"+myRoom.player.get(i).nickname);
									if(db.winRecord(myRoom.player.get(i).nickname)) {
										
										sendRoom(recordTag + "//OKAY//"+db.searchRank(myRoom.player.get(i).nickname));
									}
								}
							}
									if(db.loseRecord(nickname)) {	//전적 업데이트가 성공하면 업데이트 성공을 전송
										sendRoom(recordTag + "//OKAY//"+db.searchRank(nickname));
								}
									else {						//전적 업데이트가 실패하면 업데이트 실패를 전송
										sendRoom(recordTag + "//FAIL//");
								}
								
							
						}else {
						if(db.loseRecord(nickname)) {	//전적 업데이트가 성공하면 업데이트 성공을 전송
							sendRoom(recordTag + "//OKAY//"+db.searchRank(nickname));
						} else {						//전적 업데이트가 실패하면 업데이트 실패를 전송
							sendRoom(recordTag + "//FAIL//");
						}
						}
						
					}
				}  //패배, 기권 및 전적 업데이트 if문
				else if(m[0].equals(wroommsgTag)) {
					db.saveChatHistory(nickname,m[1]);
					sendWait(wroommsgTag+"//"+nickname +"//"+ m[1]);
					
				}//채팅 msg
				else if(m[0].equals("CMSG")) {
					db.saveChatHistory(nickname,m[1]);
					sendRoom("CMSG"+"//"+nickname +"//"+ m[1]);
					myRoom.recordMsg(nickname+":"+m[1]+"@@");
					
				}//채팅 msg
				else if(m[0].equals("ZIP")) {
					String msg = db.zipInfo(m[1]);
					if(msg.equals("")) {
						dos.writeUTF("ZIP//FAIL");
					}else {
					dos.writeUTF("ZIP//OKAY//"+msg);
					}
				}//주소 검색
				else if(m[0].equals("INFO")) {
					String msg = db.changeInfo(m[1]);
					if(msg.equals("")) {
						dos.writeUTF("INFO//FAIL");
					}else {
					dos.writeUTF("INFO//OKAY//"+msg);
					}
				}//회원 정보 보내기
				else if(m[0].equals("OPT")) {
					String msg = db.OPTInfo();
					if(msg.equals("")) {
						dos.writeUTF("OPT//FAIL");
					}else {
					dos.writeUTF("OPT//OKAY//"+msg);
					}
					
				}
				else if(m[0].equals("IMG")) {
					File file = openFile(m[1]);
					long fileSize = file.length();  // 파일 크기		
					System.out.println(m[1]);
					dos.flush();
					this.dos.writeUTF("IMG" + "//OKAY//"+fileSize);
					sendImage(file);
				}
				else if(m[0].equals("RIMG")) {
					sendRimg();
					
				}
				else if(m[0].equals("OMOK")) {
					sendRoom("OMOK//"+m[1]);
					myRoom.recordOmok(m[1]);
				}
				else if(m[0].equals("START")) {
					System.out.println(myRoom.pcount);
					if(myRoom.pcount==2&&!myRoom.playing) {
					Random random = new Random();
			        // 0 또는 1을 랜덤으로 생성
			        int randomValue = random.nextInt(2);
			        // a에는 1 또는 -1을, b에는 a의 반대값을 할당
			        int a = (randomValue == 0) ? 1 : -1;
			        int b = -a;
			        this.sendPlayer("START//OKAY//"+ playUser(),a,b);
			        //방에 순서를 통지
			        if(a==1) {
			        	sendRoom("SMSG//["+myRoom.player.get(0).nickname+"]님이 흑입니다\n["
			        			+myRoom.player.get(1).nickname+ "]님이 백입니다\n//1//-1");
			        	myRoom.black=myRoom.player.get(0).nickname;
			        	myRoom.white=myRoom.player.get(1).nickname;
			        }else if(a==-1) {
			        	sendRoom("SMSG//["+myRoom.player.get(0).nickname+"]님이 백입니다\n["
			        			+myRoom.player.get(1).nickname+ "]님이 흑입니다\n//-1//1");
			        	myRoom.white=myRoom.player.get(0).nickname;
			        	myRoom.black=myRoom.player.get(1).nickname;
			        }
			        myRoom.playing =true;
					}else if(myRoom.playing){
						sendRoom("START//FAIL//PLAYING");
					}else {
						sendRoom("START//FAIL//PLAYING");
					}
			        
				}
				else if(m[0].equals("MOVE")) {
					if(m[1].equals("PLAYER")) {
						myRoom.player.remove(this);
						myRoom.pcount--;
						
						myRoom.obj.add(this);
						myRoom.ocount++;
						
						status = objtag;
						dos.writeUTF("MOVE//OBJ");
						sendRoom("OUSER//"+objUser());
						sendRimg();
					
					}else if(m[1].equals("OBJ")) {
						if(myRoom.pcount!=2) {
						myRoom.obj.remove(this);
						myRoom.ocount--;
						
						myRoom.player.add(this);
						myRoom.pcount++;
						
						status = playertag;
						dos.writeUTF("MOVE//PLAYER");
						sendRoom("OUSER//"+objUser());
						sendRimg();
					}else {
						dos.writeUTF("MOVE//FAIL");
						}
						}
					}
				else if(m[0].equals("DELETE")) {
					if(db.deleteDB(m[1])) {
						dos.writeUTF("DELETE//OKAY//"+db.OPTInfo());
					}else {
						dos.writeUTF("DELETE//FAIL");
					}
				}
				else if(m[0].equals("WHISPER")) {
					db.saveChatHistory(nickname,m[2]);
					for(int i=0; i<auser.size(); i++) {
						if(auser.get(i).nickname.equals(m[1])) {
							auser.get(i).dos.writeUTF("WHISPER//"+nickname+"//"+m[2]);
							dos.writeUTF("WHISPER//"+nickname+"//"+m[2]+"//"+m[1]);
						}
					}
				}
				else if(m[0].equals("FRIENDPLUS")) {
					sendUser(m[1],"FRIENDPLUS//"+nickname+"//");
				}
				
				else if(m[0].equals("FRIEND")) {
					if(m[1].equals("OKAY")) {
						if(db.addFriend(m[2], m[3])) {
						sendUser(m[2],"FRIEND//OKAY//"+m[3]+"//"+db.getFriendList(m[2]));
						dos.writeUTF("FRIEND//OKAY//"+m[2]+"//"+db.getFriendList(m[3]));
						}
					}else {
						sendUser(m[1],"FRIEND//FAIL//"+m[2]);
					}
				}
				else if(m[0].equals("FIND")) {
					if(m[1].equals("ID")) {
					   String id = db.findId(m[2], m[3]);
						if(id.equals("")) {
							dos.writeUTF("FIND//ID//FAIL");
						}else {
							dos.writeUTF("FIND//ID//OKAY//"+id);
						}
					}
					if(m[1].equals("PASSWORD")) {
						String nk = db.findPassword(m[2], m[3], m[4]);
						if(!nk.equals("")) {
							dos.writeUTF("FIND//PASSWORD//OKAY//"+nk);
						}else {
							dos.writeUTF("FIND//PASSWORD//FAIL");
						}
					}
				}
				if(m[0].equals("CPASSWORD")) {
					String nk = db.findPassword(m[1]);
					if(!nk.equals("")) {
						dos.writeUTF("FIND//PASSWORD//OKAY//"+nk);
					}else {
						dos.writeUTF("FIND//PASSWORD//FAIL");
					}
				}
				else if(m[0].equals("PASSWORD")) {
					if(db.changePassword(m[1], m[2])) {
						dos.writeUTF("PASSWORD//OKAY");
					}else {
						dos.writeUTF("PASSWORD//FAIL");
					}
				}else if(m[0].equals("INVITE")){
					String stat = userStatus(m[1]);
					if(stat.equals("")) {
						dos.writeUTF("INVITE//UNCONNECTED//"+m[1]);
					}if(stat.equals(waittag)||stat.equals(objtag)) {
						sendUser(m[1],"INVITE//MESSAGE//"+m[2]+"//"+m[3]);
					}if(stat.equals(playertag)) {
						if(roomStatus(m[1])) {
							dos.writeUTF("INVITE//PLAYING//"+m[1]);
						}else {
							sendUser(m[1],"INVITE//MESSAGE//"+m[2]+"//"+m[3]);
						}
					}
				}
				else if(m[0].equals("OPTION")) {
					db.updateFontOptions(m[1], m[2], m[3], m[4], m[5], m[6], m[7]);
				}
				else if(m[0].equals("GIBO")) {
					dos.writeUTF("GIBO//"+db.retrieveConcatenatedData()+"//"+filesSize(db.omokplaytime()));
					for(int i=0; i<db.omokplaytime().length;i++)
					{
						sendImage(openFiles(db.omokplaytime()[i]));
					}
				}
				else if(m[0].equals("WIMSG")) {
					long fileSize = Long.parseLong(m[1]);  // 파일 크기
					System.out.println(m[1]);
				    byte[] fileData = new byte[(int) fileSize];
				    dis.readFully(fileData);
				    BufferedImage img = ImageIO.read(new ByteArrayInputStream(fileData));
				    sendWaitImage(img);
				}
				else if(m[0].equals("BCHANGE")) {
					db.updateboardOptions(m[1], m[2], m[3], m[4], m[5]);
				}
				else if(m[0].equals("CMUSIC")) {
					db.updatemusicOptions(m[1], m[2], m[3]);
				}
				else if(m[0].equals("RIMSG")) {
					long fileSize = Long.parseLong(m[1]);  // 파일 크기
					System.out.println(m[1]);
				    byte[] fileData = new byte[(int) fileSize];
				    dis.readFully(fileData);
				    BufferedImage img = ImageIO.read(new ByteArrayInputStream(fileData));
				    sendRoomImage(img);
				}
				else if(m[0].equals("RATE")) {
					sendrecord(m[1]);
				} else if(m[0].equals("CHAT")) {
					System.out.println(m[1]);
					dos.writeUTF("CHAT//"+db.getChatHistoryByNick(m[1]));
				}else if(m[0].equals("EEXIT")) {
					System.out.println("에러엑시트 실행");
					myRoom.playing=false;
					LocalDateTime currentTime = LocalDateTime.now();
					String time = db.formatTimestamp(currentTime);
					db.insertRecord(myRoom.black,myRoom.white,myRoom.romok,m[1],time);
					long fileSize = Long.parseLong(m[2]);  // 파일 크기
					byte[] fileData = new byte[(int) fileSize];
				        dis.readFully(fileData); 
				        
				        // 파일을 서버에 저장
				        File receivedFile = new File(gameFilePath + time+".png");
					FileOutputStream fileOutputStream = new FileOutputStream(receivedFile);
					fileOutputStream.write(fileData);
					fileOutputStream.close();
					myRoom.romok = ".";
					
				}
			}  //while문
		} catch (Exception e) {
		    // 예외 발생 시 클라이언트와의 연결 종료 및 예외 메시지 출력
		    e.printStackTrace();
		    server.alluser.remove(this);
		    server.waituser.remove(this);
		    if (myRoom != null) {
		    	if(myRoom.playing) {
		    		myRoom.playing= false;
		    		if(myRoom.black.equals(this.nickname)) {
		    			db.winRecord(myRoom.white);
		    			db.loseRecord(this.nickname);
		    			sendRoom("BTN//"+myRoom.white);
		    			sendRoom(recordTag + "//OKAY//"+db.searchRank(myRoom.white));
		    			
		    		}
		    		if(myRoom.white.equals(this.nickname)) {
		    			db.winRecord(myRoom.black);
		    			db.loseRecord(this.nickname);
		    			sendRoom("BTN//"+myRoom.black);
		    			sendRoom(recordTag + "//OKAY//"+db.searchRank(myRoom.black));
		    			
		    		}
		    		
		    	}
		    	synchronized (myRoom.ccu) {
		    	    myRoom.ccu.remove(this);
		    	    myRoom.acount--;
		    	    if (myRoom.player.contains(this)) {
		    	    	myRoom.player.remove(this);
		    	    	myRoom.pcount--;
		            } else if (myRoom.obj.contains(this)) {
		            	myRoom.obj.remove(this);
		            	myRoom.ocount--;
		            }
		    	}
		        
		        System.out.println("유저전체수"+myRoom.acount);
		        for(int i=0; i<myRoom.ccu.size();i++) {
		        	System.out.println(myRoom.ccu.get(i).nickname);
		        	if(myRoom.ccu.get(i).nickname.equals(this.nickname)) {
		        		myRoom.ccu.remove(i);
		        		
		        	}
		        }
		        System.out.println(this.nickname);
		        sendRoom("EEXIT");
		        
		        sendRimg();
		        sendRoom("OUSER//"+objUser());
		    }
		    sendWait(connectedUser());//대기실 접속 유저에 모든 접속 인원을 전송
		}
	}  //run()
	/* 현재 존재하는 방의 목록을 조회하는 메소드 */
	String roomInfo() {
		String msg = vroomTag + "//";
		
		for(int i=0; i<room.size(); i++) {
			if(room.get(i).acount==0) {
				System.out.println("룸삭제 테스트");
				room.remove(i);
			}else {
			msg = msg + room.get(i).title + ":" + room.get(i).acount + "@";
			}
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
	/*방의 플레이어를 조회화는 메소드*/
	String playUser() {
		String msg = "";
		
		for(int i=0; i<myRoom.player.size(); i++) {
			msg = msg + myRoom.player.get(i).nickname + "@";
		}
		return msg;
	}
	String objUser() {
		String msg = "";
		
		for(int i=0; i<myRoom.obj.size(); i++) {
				msg = msg + myRoom.obj.get(i).nickname + "@";
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
		for(int i=0; i<auser.size(); i++) {
			try {
				auser.get(i).dos.writeUTF(m);
				
				System.out.println("서버문자보냄");
			} catch(IOException e) {
				auser.remove(i--);
			}
		}
	}
	
	void sendRoom(File file) {
		for(int i=0; i<myRoom.ccu.size(); i++) {
			try {
				FileInputStream fileInputStream = new FileInputStream(file);
			    byte[] buffer = new byte[4096];
			    int bytesRead;

			    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
					myRoom.ccu.get(i).dos.write(buffer, 0, bytesRead);
			    }
			    fileInputStream.close();
			} catch(IOException e) {
				myRoom.ccu.remove(i--);
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
	/* 방에 입장한 모든 플레이어에게만 순서를 전송하는 메소드*/
	void sendPlayer(String m,int a, int b) {
		for(int i=0; i<myRoom.player.size(); i++) {
			try {
				myRoom.player.get(i).dos.writeUTF(m+"//"+a);
				a=b;
			} catch(IOException e) {
				myRoom.ccu.remove(i--);
			}
		}
	}
	void sendRimg() {
		if(myRoom.pcount==0) {
			sendRoom("RIMG" + "//OKAY//"+myRoom.pcount+"//"+0+"//"+0+"//"+0+"//"+0);
		}else {
		String[] nnm = playUser().split("@");
		String user1 = db.searchRank(nnm[0]);
		String user2 = "";
		File file1 = openFile(nnm[0]);
		long file1Size = file1.length();  // 파일 크기	
		File file2 = null;
		long file2Size = 0;
		if(myRoom.pcount==2) {
			user2 = db.searchRank(nnm[1]);
			file2 = openFile(nnm[1]);
			file2Size = file2.length();  // 파일 크기	
		}
		sendRoom("RIMG" + "//OKAY//"+myRoom.pcount+"//"+file1Size+"//"+file2Size+"//"+user1+"//"+user2);
		sendRoom(file1);
		if(myRoom.pcount==2) {
		sendRoom(file2);
		}
		}
	}
	void sendrecord(String nick) {
		String user = db.searchRank(nick);
		File file = openFile(nick);
		long filesize = file.length();
		try {
		dos.writeUTF("RATE//"+user+"//"+filesize);
		sendImage(file);
		}catch(IOException e){
		}
		}
		
	File openFile(String m) {
		File file = new File(originalFilePath+db.imgInfo(m));
		return file;
	}
	File openFiles(String m) {
		File file = new File(gameFilePath+m+".png");
		return file;
	}
	void sendUser(String user,String msg) {
		System.out.println("user와 친구추가");
		for(int i=0; i<auser.size(); i++) {
			if(auser.get(i).nickname.equals(user)) {
				try {
				auser.get(i).dos.writeUTF(msg);
			} catch(IOException e) {
				auser.remove(i--);
			}
		}
	}
	}
	String filesSize(String[] m) {
	    StringBuilder files = new StringBuilder();
	    for (int i = 0; i < m.length; i++) {
	        File file = new File(gameFilePath + m[i] + ".png");
	        files.append(file.length()).append("@");
	    }
	    return files.toString();
	}
	void sendImage(File file) {
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
		    byte[] buffer = new byte[4096];
		    int bytesRead;
		    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
		        dos.write(buffer, 0, bytesRead);
		    }
		    fileInputStream.close();
		} catch(IOException e) {
		}
		
	}
	void sendWaitImage(BufferedImage image) {
		for(int i=0; i<wuser.size(); i++) {
        try {
            // BufferedImage를 바이트 배열로 변환
            ByteArrayOutputStream baos = new ByteArrayOutputStream(8192);
            ImageIO.write(image, "png", baos);
            baos.flush();
            byte[] imageBytes = baos.toByteArray();
            baos.close();
            // 이미지 데이터를 전송
            wuser.get(i).dos.writeUTF("WIMSG//"+nickname+"//"+imageBytes.length);
            wuser.get(i).dos.write(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
		}
    }
	void sendRoomImage(BufferedImage image) {
		for(int i=0; i<myRoom.ccu.size(); i++) {
        try {
            // BufferedImage를 바이트 배열로 변환
            ByteArrayOutputStream baos = new ByteArrayOutputStream(8192);
            ImageIO.write(image, "png", baos);
            baos.flush();
            byte[] imageBytes = baos.toByteArray();
            baos.close();
            // 이미지 데이터를 전송
            myRoom.ccu.get(i).dos.writeUTF("RIMSG//"+nickname+"//"+imageBytes.length);
            myRoom.ccu.get(i).dos.write(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
		}
    }
	void sendRoomImage(BufferedImage image,String nick) {
		for(int i=0; i<myRoom.ccu.size(); i++) {
        try {
            // BufferedImage를 바이트 배열로 변환
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            baos.flush();
            byte[] imageBytes = baos.toByteArray();
            baos.close();
            // 이미지 데이터를 전송
            myRoom.ccu.get(i).dos.write(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
		}
    }
	String userStatus(String nick) {
		String stat="";
		for(int i=0; i<auser.size(); i++) {
			
				if(auser.get(i).nickname.equals(nick)) {
					stat = auser.get(i).status;
					}
		}
		return stat;
	}
	boolean roomStatus(String nick) {
		boolean stat = false;
		for(int i=0; i<auser.size(); i++) {
			
			if(auser.get(i).nickname.equals(nick)) {
				stat = auser.get(i).myRoom.playing;
				}
	}
		return stat;
	}
}  //CCUser 클래스


	
	

