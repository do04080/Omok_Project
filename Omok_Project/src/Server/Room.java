package Server;

import java.util.Vector;

public class Room {
	Vector<CCUser> ccu; // 방전체 인원
	Vector<CCUser> obj; // 관전자
	Vector<CCUser> player; // 플레이어
	String title; // 방제목

	int pcount = 0; // 플레이어 수
	int acount = 0; // 방전체 인원수
	int ocount = 0; // 관전자 수
	boolean playing = false; // 게임중인지 판단
	String black = "";
	String white = "";
	String rmsg = "."; // 채팅내역
	String romok = "."; // 오목내역

	Room() { // Room 객체 생성 시 접속(입장)한 클라이언트 객체에 대한 정보를 Room에 저장한다.
		ccu = new Vector<>();
		obj = new Vector<>();
		player = new Vector<>();
	}

	void recordMsg(String msg) {
		if (rmsg.equals(".")) {
			rmsg = "";
		}
		rmsg = rmsg + msg;
	}

	void recordOmok(String msg) {
		if (romok.equals(".")) {
			romok = "";
		}
		romok += msg + ":";
	}

	public void removeUser(CCUser user) {
			System.out.println("유저삭제 실행");
			if (ccu.contains(user)) {
	            ccu.remove(user);
	            if (player.contains(user)) {
	                player.remove(user);
	                pcount--;
	            } else if (obj.contains(user)) {
	                obj.remove(user);
	                ocount--;
	            }
	            acount--;	    }
	}
}
