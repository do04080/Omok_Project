package Client;

import java.util.Arrays;
import UI.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;

public class Operator {

	LoginFrame mf = null;
	JoinFrame jf = null;

	RoomCreateFrame rcf = null;
	ZipFrame zf = null;
	Gui gui = null;
	InfoChangeFrame ic = null;
	Socket socket;
	OutputStream os;
	DataOutputStream dos;
	InputStream is;
	DataInputStream dis;
	String nick = null;
	String[] userinfo = new String[] {};
	WaitRoomFrame wr = null;
	SearchIdFrame sif;
	ChangePassFrame cpf;
	Option fontopt;
	OmokRecordFrame orf;
	SoundPlay sp;

	BufferedImage myimage;
	BufferedImage youimage;
	BufferedImage player1image;
	BufferedImage player2image;
	String p1order;
	String p2order;
	String p1nick;
	String p2nick;
	String p1winno;
	String p1loseno;
	String p2winno;
	String p2loseno;
	OperatorFrame opf;
	int zipinst;
	int overinst;
	String[] roomList = new String[] {}; // 방 목록
	String[] roomName = new String[] {}; // 방 제목
	String[] zipList = new String[] {};
	String[] userList = new String[] {};
	String[] friendList = new String[] {};
	String[] onlineFriendList = new String[] {};
	String winno;
	String loseno;
	String inputString = "id@name@nickname@password@email@address@number1@number2@number3||id2@name2@nickname2@password2@email2@address2@number1@number2@number3";
	String mystat = "WAIT"; // 플레이어 확인
	int omokorder = 0; // 오목 순서 1이면 흑 -1이면 백 0은 놓을 수 없음
	int inRoom = 0;
	long fileSize;
	Socket sock;
	String roomname;
	String mycolor;

	/* 각 메시지를 구분하기 위한 태그 */
	final String loginTag = "LOGIN"; // 로그인
	final String joinTag = "JOIN"; // 회원가입
	final String overTag = "OVER"; // 중복확인
	final String viewTag = "VIEW"; // 회원정보조회
	final String changeTag = "CHANGE"; // 회원정보변경
	final String rankTag = "RANK"; // 전적조회(전체회원)
	final String croomTag = "CROOM"; // 방생성
	final String vroomTag = "VROOM"; // 방목록
	final String uroomTag = "UROOM"; // 방유저
	final String eroomTag = "EROOM"; // 방입장
	final String cuserTag = "CUSER"; // 접속유저
	final String searchTag = "SEARCH"; // 전적조회(한명)
	final String pexitTag = "PEXIT"; // 프로그램종료
	final String rexitTag = "REXIT"; // 방퇴장
	final String omokTag = "OMOK"; // 오목
	final String winTag = "WIN"; // 승리
	final String loseTag = "LOSE"; // 패배
	final String recordTag = "RECORD"; // 전적업데이트
	final String wroommsgTag = "WMSG"; // 메세지 송수신

	public Operator(Socket socket) {
		sock = socket;

	}

	void sendServer(String m) {

		try {
			dos = new DataOutputStream(sock.getOutputStream());

			String msg = (m);
			System.out.println("문자보냄");
			dos.writeUTF(msg);
			dos.flush();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String[] findCommonElements(String[] array1, String[] array2) {
		// Set을 사용하여 중복을 피하면서 공통된 요소를 찾습니다.
		Set<String> set1 = new HashSet<>(Set.of(array1));
		Set<String> set2 = new HashSet<>(Set.of(array2));

		// 두 Set에 공통으로 존재하는 요소를 찾아 commonSet에 추가합니다.
		Set<String> commonSet = new HashSet<>(set1);
		commonSet.retainAll(set2);

		// 결과를 배열로 변환하여 반환합니다.
		return commonSet.toArray(new String[0]);
	}

	public static void main(String[] args) {

		try {
			Socket socket = new Socket("127.0.0.1", 1228);

			Operator opt = new Operator(socket);

			// 입력 스트림
			// 서버에서 보낸 데이터를 받음

			// 출력 스트림
			// 서버에 데이터를 송신

			new read_C(socket).start();

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
			Operator opt = new Operator(socket);
			opt.jf = new JoinFrame(opt, socket);
			opt.mf = new LoginFrame(opt, socket);
			opt.wr = new WaitRoomFrame(opt, opt.nick, socket);
			opt.rcf = new RoomCreateFrame(opt, socket);
			opt.zf = new ZipFrame(opt, socket);
			opt.opf = new OperatorFrame(opt, socket);
			opt.fontopt = new Option();
			opt.sp = new SoundPlay(opt);

			while (true) {

				// POINT) server가 write 하면 동작한다
				// readUTF는 상대가 입력하지 않으면 계속 대기한다(스캐너처럼)
				// while이 쓸데없이 계속 돌지 않는 이유다
				String msg = dis.readUTF();
				System.out.println();
				System.out.println("상대방 msg : " + msg);
				String[] m = msg.split("//");

				if (m[0].equals("LOGIN")) { // 서버에서 로그인관련 메시지
					if (m[1].equals("OKAY")) { // 성공

						String[] user = m[2].split("@");
						opt.nick = user[0];
						opt.winno = user[1];
						opt.loseno = user[2];
						opt.mystat = "WAIT";
						long fileSize = Long.parseLong(m[3]); // 파일 크기
						byte[] fileData = new byte[(int) fileSize];
						dis.readFully(fileData);
						opt.myimage = ImageIO.read(new ByteArrayInputStream(fileData));
						opt.wr.profileImg(opt.myimage, opt.nick);
						opt.wr.setVisible(true); // 채팅창 열기
						opt.mf.loginSign(true); // 로그인 성공
						if (m.length >= 5) {
							opt.friendList = m[4].split("@");
							opt.fontopt.splitOption(m[5].split("@"));
						}
						opt.ic = new InfoChangeFrame(opt, socket);
						opt.sp.musicSound();
					} else {
						opt.mf.loginSign(false);
					}
				}
				if (m[0].equals("WMSG")) {
					System.out.println("문자받음");
					String Mnick = m[1];
					String cmsg = m[2];
					opt.wr.processChat(0, Mnick + ":" + cmsg);
				}
				if (m[0].equals("JOIN")) {
					if (m[1].equals("OKAY")) {

						opt.jf.joinSign(true);
					} else {
						opt.jf.joinSign(false);

					}
				}
				if (m[0].equals("OVER")) {
					if (m[1].equals("OKAY")) {
						if (opt.overinst == 0) {
							opt.jf.overSign(true, m[2]);
						}
						if (opt.overinst == 1) {
							opt.opf.overSign(true, m[2]);
						}

					} else if (m[1].equals("FAIL")) {
						if (opt.overinst == 0) {
							opt.jf.overSign(false, "");
						}
					}
				}
				if (m[0].equals("VROOM")) {
					opt.roomList = new String[] {};
					if (m.length != 1) {
						opt.roomList = m[1].split("@"); // 방 목록
						opt.wr.updateRoomList(opt.roomList);
					} else
						opt.roomList = new String[] {}; // 방 목록
					opt.wr.updateRoomList(opt.roomList);
				}
				if (m[0].equals("CROOM")) {
					opt.rcf.dispose();
					opt.wr.setVisible(false);
					opt.mystat = m[3];
					opt.gui = new Gui(m[2] + " " + opt.nick, socket, opt);
					opt.gui.setVisible(true);
					opt.gui.sendServer("RIMG//");
					opt.roomname = m[4];
					opt.inRoom = 1;
					opt.gui.updateFriendList(opt.onlineFriendList);
				}
				if (m[0].equals("EROOM")) {
					if (m[1].equals("OKAY")) {
						opt.wr.setVisible(false);
						opt.gui = new Gui(m[2], socket, opt);
						opt.gui.setVisible(true);
						opt.gui.sendServer("RIMG//");
						opt.inRoom = 1;
						opt.gui.updateFriendList(opt.onlineFriendList);

						if (!m[3].equals(".")) {
							opt.gui.processChats(m[3].split("@@"));
						}
						if (!m[4].equals(".")) {

							opt.gui.recordOmok(m[4].split(":"));
						}

						opt.mystat = m[5];
						opt.roomname = m[6];
					}
				}
				if (m[0].equals("OUSER")) {
					if (m.length != 1) {
						if (!m[1].equals(".")) {
							opt.gui.updateObjList(m[1].split("@"));
						}
					} else {
						String[] m1 = { "" };
						opt.gui.updateObjList(m1);
					}
				}
				if (m[0].equals("CMSG")) {
					opt.gui.processChat(0, m[1] + ":" + m[2]);
				}
				if (m[0].equals("SMSG")) {
					opt.gui.serverMessage(m[1]);
					opt.p1order = m[2];
					opt.p2order = m[3];
				}
				if (m[0].equals("ZIP")) {
					if (m[1].equals("FAIL")) {
						opt.zf.zipFail();
					} else if (m[1].equals("OKAY")) {
						opt.zipList = new String[] {};
						opt.zipList = m[2].split("@");
						System.out.println("분류");// 방 목록
						opt.zf.updateZipList(opt.zipList);
						System.out.println("리스트 업데이트 끝");
					}
				}
				if (m[0].equals("CUSER")) {
					opt.userList = new String[] {};
					opt.userList = m[1].split("@");
					opt.onlineFriendList = opt.findCommonElements(opt.userList, opt.friendList);
					opt.wr.updateUserList(opt.userList);
					opt.wr.updateFriendList(opt.onlineFriendList);
					if (opt.gui != null) {
						opt.gui.updateFriendList(opt.onlineFriendList);
					}
				}
				if (m[0].equals("SEARCH")) {

					long fileSize = Long.parseLong(m[2]); // 파일 크기
					byte[] fileData = new byte[(int) fileSize];
					dis.readFully(fileData);
					opt.youimage = ImageIO.read(new ByteArrayInputStream(fileData));
					opt.wr.showImageInNewWindow(opt.youimage);
				}
				if (m[0].equals("INFO")) {
					if (m[1].equals("OKAY")) {
						String[] userinfodata = new String[] {};
						opt.userinfo = new String[6];
						userinfodata = m[2].split("@");
						opt.userinfo[0] = "이름 : " + userinfodata[0];
						opt.userinfo[1] = "닉네임 : " + userinfodata[1];
						opt.userinfo[2] = "비밀번호 : " + userinfodata[2];
						opt.userinfo[3] = "이메일 : " + userinfodata[3];
						opt.userinfo[4] = "우편번호 및 주소 ↓";
						opt.userinfo[5] = userinfodata[4];

						opt.ic.updateInfoList(opt.userinfo);
					}
				}
				if (m[0].equals("OPT")) {
					if (m[1].equals("OKAY")) {
						opt.mf.frame.dispose();
						opt.inputString = m[2];
						opt.opf.setVisible(true);
						opt.opf.updateTableWithNewData(opt.inputString);
					}
				}
				if (m[0].equals("DELETE")) {
					if (m[1].equals("OKAY")) {
						opt.inputString = m[2];
						opt.opf.updateTableWithNewData(opt.inputString);
					}
				}
				if (m[0].equals("CHANGE")) {
					if (m[1].equals("OKAY")) {
						opt.inputString = m[2];
						opt.opf.updateTableWithNewData(opt.inputString);

					}
					if (m[1].equals("FAIL")) {

					}
				}
				if (m[0].equals("IMG")) {
					if (m[1].equals("OKAY")) {
						opt.fileSize = Long.parseLong(m[2]); // 파일 크기
						byte[] fileData = new byte[(int) opt.fileSize];
						dis.readFully(fileData);
						opt.youimage = ImageIO.read(new ByteArrayInputStream(fileData));
						opt.opf.profileImg(opt.youimage);
					}
				}
				if (m[0].equals("RIMG")) {
					if (m[1].equals("OKAY")) {
						if (m[2].equals("0")) {
							opt.gui.profileImg(0);
							opt.gui.profileUpdate("0");
						} else {
							try {
								opt.player1image = null;
								opt.player2image = null;
								String[] user = m[5].split("@");
								opt.p1nick = user[0];
								opt.p1winno = user[1];
								opt.p1loseno = user[2];

								long file1Size = Long.parseLong(m[3]); // 파일 크기
								byte[] file1Data = new byte[(int) file1Size];
								dis.readFully(file1Data);
								opt.player1image = ImageIO.read(new ByteArrayInputStream(file1Data));
								opt.gui.profileImg(1);

								if (m[2].equals("2")) {
									long file2Size = Long.parseLong(m[4]); // 파일 크기
									byte[] file2Data = new byte[(int) file2Size];
									dis.readFully(file2Data);
									opt.player2image = ImageIO.read(new ByteArrayInputStream(file2Data));
									opt.gui.profileImg(2);
									String[] user2 = m[6].split("@");
									opt.p2nick = user2[0];
									opt.p2winno = user2[1];
									opt.p2loseno = user2[2];
								}
								opt.gui.profileUpdate(m[2]);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				} else if (m[0].equals("OMOK")) {
					String[] xy = m[1].split("@");
					int x = Integer.parseInt(xy[0]);
					int y = Integer.parseInt(xy[1]);
					opt.gui.receiveomok(x, y);
				} else if (m[0].equals("START")) {
					if (m[1].equals("OKAY")) {
						String[] nnm = m[2].split("@");
						if (Arrays.asList(nnm).contains(opt.nick)) {
							opt.omokorder = Integer.parseInt(m[3]);
						}
						if (opt.omokorder == -1) {
							opt.mycolor = "BLACK";
						} else if (opt.omokorder == 1) {
							opt.mycolor = "WHITE";
						}
						opt.gui.map.orderchk = 1;
					} else if (m[1].equals("FAIL")) {
						opt.gui.serverMessage("유저가 부족합니다");
					}
				} else if (m[0].equals("RECORD")) {
					if (m[1].equals("OKAY")) {
						String[] user = m[2].split("@");
						if (user[0].equals(opt.p1nick)) {
							opt.p1winno = user[1];
							opt.p1loseno = user[2];

						} else if (user[0].equals(opt.p2nick)) {
							opt.p2winno = user[1];
							opt.p2loseno = user[2];
						}
						opt.gui.profileUpdate("2");
					}
				} else if (m[0].equals("BTN")) {
					opt.gui.showPopUP("[" + m[1] + "]이 승리");
				} else if (m[0].equals("MOVE")) {
					opt.mystat = m[1];
				} else if (m[0].equals("REXIT")) {
					opt.mystat = "WAIT";
					opt.gui.dispose();

					opt.wr.setVisible(true);
					opt.inRoom = 0;
				} else if (m[0].equals("WHISPER")) {
					if (opt.inRoom == 0) {
						if (m[1].equals(opt.nick)) {
							opt.wr.processChat(1, m[1] + ":" + m[2] + ":" + m[3]);
							System.out.println("내가 보낸 메시지");
						} else {
							opt.wr.processChat(1, m[1] + ":" + m[2]);
						}
					} else if (opt.inRoom == 1) {
						if (m[1].equals(opt.nick)) {
							opt.gui.processChat(1, m[1] + ":" + m[2] + ":" + m[3]);
							System.out.println("내가 보낸 메시지");
						} else {
							opt.gui.processChat(1, m[1] + ":" + m[2]);
						}
					}
				} else if (m[0].equals("FRIENDPLUS")) {
					FriendPlusFrame fpf = new FriendPlusFrame(0, opt, m[1], "");
					fpf.setVisible(true);
				} else if (m[0].equals("FRIEND")) {
					if (m[1].equals("OKAY")) {
						opt.wr.showFriendRequestAcceptedDialog(m[2]);
						opt.friendList = m[3].split("@");
						opt.onlineFriendList = opt.findCommonElements(opt.userList, opt.friendList);
						opt.wr.updateFriendList(opt.onlineFriendList);
						if (opt.gui != null) {
							opt.gui.updateFriendList(opt.onlineFriendList);
						}
						System.out.println(m[3]);
					} else {
						opt.sif.showMessage(m[2] + "님에게 친구 신청이 거절 당했습니다");
					}
				} else if (m[0].equals("FIND")) {
					if (m[1].equals("ID")) {
						if (m[2].equals("OKAY")) {
							opt.sif.showID(m[3]);
						} else {
							opt.sif.showMessage("해당하는 ID가 없습니다");
						}
					}
					if (m[1].equals("PASSWORD")) {
						if (m[2].equals("OKAY")) {
							opt.cpf = new ChangePassFrame(opt, m[3]);
							opt.cpf.setVisible(true);
							if(opt.sif!=null) {
							opt.sif.dispose();
							}
						} else {
							opt.sif.showMessage("해당하는 유저정보가 없습니다. 정보를 다시 확인해주세요");
						}
					}
				} else if (m[0].equals("PASSWORD")) {
					opt.sif= new SearchIdFrame(opt,1);
					opt.sif.setVisible(false);
					if (m[1].equals("OKAY")) {
						opt.sif.showMessage("회원님의 비밀번호 변경을 성공했습니다");
						opt.cpf.dispose();

					} else {
						opt.sif.showMessage("회원님의 비밀번호 변경을 실패했습니다");
						opt.cpf.dispose();

					}
				} else if (m[0].equals("INVITE")) {
					if (m[1].equals("MESSAGE")) {
						FriendPlusFrame fpf = new FriendPlusFrame(1, opt, m[2], m[3]);
						fpf.setVisible(true);
					} else if (m[1].equals("UNCONNECTED")) {
						opt.sif.showMessage("[" + m[1] + "] 님이 접속하지 않았습니다.");
					} else if (m[1].equals("PLAYING")) {
						opt.sif.showMessage("[" + m[1] + "] 님이 게임중입니다.");
					} else if (m[1].equals("REJECT")) {
						opt.sif.showMessage("[" + m[1] + "] 님이 초대를 거절했습니다.");
					}
				} else if (m[0].equals("GIBO")) {
					String[] mdata = m[1].split("\\&\\&");
					String[] user1 = new String[mdata.length];
					String[] user2 = new String[mdata.length];
					String[] recordomok = new String[mdata.length];
					String[] wincolor = new String[mdata.length];
					String[] playtime = new String[mdata.length];

					for (int i = 0; i < mdata.length; i++) {
						String[] sdata = mdata[i].split("\\$");
						user1[i] = sdata[0];
						user2[i] = sdata[1];
						recordomok[i] = sdata[2];
						wincolor[i] = sdata[3];
						playtime[i] = sdata[4];
					}

					String[] filessize = m[2].split("@");
					BufferedImage[] omokimage = new BufferedImage[filessize.length];

					for (int i = 0; i < filessize.length; i++) {
						long fileSize = Long.parseLong(filessize[i]);
						byte[] fileData = new byte[(int) fileSize];
						dis.readFully(fileData);
						omokimage[i] = ImageIO.read(new ByteArrayInputStream(fileData));
					}
					opt.orf = new OmokRecordFrame(opt);
					opt.orf.addPanels(user1, user2, omokimage, recordomok, wincolor, playtime);
					opt.orf.setVisible(true);

				} else if (m[0].equals("WIMSG")) {
					long fileSize = Long.parseLong(m[2]); // 파일 크기
					System.out.println(fileSize);
					byte[] fileData = new byte[(int) fileSize];
					dis.readFully(fileData);
					BufferedImage img = ImageIO.read(new ByteArrayInputStream(fileData));
					opt.wr.insertImageWithHyperlink(img, m[1], m[1].equals(opt.nick));
				} else if (m[0].equals("RIMSG")) {
					long fileSize = Long.parseLong(m[2]); // 파일 크기
					System.out.println(fileSize);
					byte[] fileData = new byte[(int) fileSize];
					dis.readFully(fileData);
					BufferedImage img = ImageIO.read(new ByteArrayInputStream(fileData));
					opt.gui.insertImageWithHyperlink(img, m[1], m[1].equals(opt.nick));
				} else if (m[0].equals("RATE")) {
					long fileSize = Long.parseLong(m[2]); // 파일 크기
					byte[] fileData = new byte[(int) fileSize];
					dis.readFully(fileData);
					BufferedImage profile = ImageIO.read(new ByteArrayInputStream(fileData));
					RateFrame rf = new RateFrame(profile, m[1]);
					rf.setVisible(true);
				}else if(m[0].equals("CHAT")) {
					ChatRecordFrame crf = new ChatRecordFrame(m[1]);
					crf.setVisible(true);
				}else if(m[0].equals("EEXIT")) {
					if(opt.mystat.equals("PLAYER")) {
					BufferedImage capimg = opt.gui.captureImage();
					File tempFile = File.createTempFile("capimg", ".png");
					ImageIO.write(capimg, "png", tempFile);
					long fileSizeInBytes = tempFile.length();
					opt.wr.sendServer("EEXIT//"+opt.nick+"//"+fileSizeInBytes);
					opt.gui.sendImage(capimg);
					}
				}
				if (m[0].equals("EXIT")) {
					break;
				}
			}
		}

		catch (Exception e) {
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
