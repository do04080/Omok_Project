package Server;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DBTable {

	Connection con = null;
	Statement stmt = null;
	String url = "jdbc:mysql://localhost/omok?serverTimezone=Asia/Seoul";
	String user = "root";
	String passwd = "0000";

	private static final String INSERT_QUERY_FONT = "INSERT INTO fontoption  (nickname) VALUES (?)";
	private static final String SELECT_QUERY_FONT = "SELECT * FROM fontoption WHERE nickname = ?";

	DBTable() {
		try { // 데이터베이스 연결은 try-catch문으로 예외를 잡아준다.
				// 데이터베이스와 연결한다.
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url, user, passwd);
			stmt = con.createStatement();
			System.out.println("[Server] MySQL 서버 연동 성공");

		} catch (Exception e) { // 데이터베이스 연결 및 테이블 생성에 예외가 발생했을 때 실패를 콘솔로 알린다.
			System.out.println("[Server] 데이터베이스 연결 혹은 테이블 생성에 문제 발생 > " + e.toString());
		}
	}

	String loginCheck(String _i, String _p) {
		String nickname = "null"; // 반환할 닉네임 변수를 "null"로 초기화.

		// 매개변수로 받은 id와 password값을 id와 pw값에 초기화한다.
		String id = _i;
		String pw = _p;

		try {
			// id와 일치하는 비밀번호와 닉네임이 있는지 조회한다.
			String checkingStr = "SELECT password, nickname FROM member WHERE id='" + id + "'";
			ResultSet result = stmt.executeQuery(checkingStr);

			int count = 0;
			while (result.next()) {
				// 조회한 비밀번호와 pw 값을 비교.
				if (pw.equals(result.getString("password"))) { // true일 경우 nickname에 조회한 닉네임에 반환하고 로그인 성공을 콘솔로 알린다.
					nickname = result.getString("nickname");
					System.out.println("[Server] 로그인 성공");

				}

				else { // false일 경우 nickname을 "null"로 초기화하고 로그인 실패를 콘솔로 알린다.
					nickname = "null";
					System.out.println("[Server] 로그인 실패");

				}
				count++;
			}
		} catch (Exception e) { // 조회에 실패했을 때 nickname을 "null"로 초기화. 실패를 콘솔로 알린다.
			nickname = "null";
			System.out.println("[Server] 로그인 실패 > " + e.toString());

		}
		return nickname;

	}

	// 회원가입을 수행하는 메소드. 회원가입에 성공하면 true, 실패하면 false를 반환한다.

	/*
	 * "JOIN//" + uid + "//" + upass+ "//" +unm+"//" +unnm+"//"+sex+"//" +uzip+"//"
	 * +uadd+"//" +uem+"//"+birth+"//"+ fileSize+ "//" + ext
	 */
	boolean joinCheck(String _i, String _p, String _n, String _nn, String _s, String _z, String _a, String _e,
			String _bir, String _img) {
		boolean flag = false; // 참거짓을 반환할 flag 변수. 초기값은 false

		// 매개변수로 받은 각 문자열들을 각 변수에 초기화한다.
		String na = _n;
		String nn = _nn;
		String id = _i;
		String pw = _p;
		String zip = _z;
		String add = _a;
		String em = _e;
		String sex = _s;
		String birth = _bir;
		String img = _img;
		System.out.println(na + nn + id + pw + sex + zip + add + em + birth + img);

		try {
			// member 테이블에 각 문자열들을 순서대로 업데이트하는 문장. 승, 패는 초기값을 숫자 0으로 한다.
			String insertStr = "INSERT INTO member VALUES('" + na + "', '" + nn + "', '" + id + "', '" + pw + "', '"
					+ sex + "','" + zip + "', '" + add + "', '" + em + "', '" + birth + "','" + img + "', 0, 0,0)";
			stmt.executeUpdate(insertStr);

			flag = true; // 업데이트문이 정상적으로 수행되면 flag를 true로 초기화하고 성공을 콘솔로 알린다.
			System.out.println("[Server] 회원가입 성공");
		} catch (Exception e) { // 회원가입 절차를 수행하지 못하면 flag를 false로 초기화하고 실패를 콘솔로 알린다.
			flag = false;
			System.out.println("[Server] 회원가입 실패 > " + e.toString());
		}

		return flag; // flag 반환
	}

	// 아이디나 닉네임이 중복되었는지 확인해주는 메소드. 중복 값이 존재하면 false, 존재하지 않으면 true를 반환한다.
	boolean overCheck(String _a, String _v) {
		boolean flag = false; // 참거짓을 반환할 flag 변수. 초기값은 false

		// att는 속성(아이디, 닉네임)을 구분하고, val은 확인할 값이 초기화.
		String att = _a;
		String val = _v;

		try {
			// member 테이블에 존재하는 아이디(혹은 닉네임)를 모두 찾는다.
			String selcectStr = "SELECT " + att + " FROM member";
			ResultSet result = stmt.executeQuery(selcectStr);

			int count = 0;
			while (result.next()) {
				// 조회한 아이디(혹은 닉네임)과 val을 비교.
				if (val.equals(result.getString(att))) { // val과 같은 것이 존재하면 flag를 true로 변경한다.
					flag = true;
					return flag;
				}

				else { // val과 같은 것이 존재하지 않으면 flag를 false로 변경한다.
					flag = false;
				}
				count++;
			}
			System.out.println("[Server] 중복 확인 성공"); // 정상적으로 수행되었을 때 성공을 콘솔로 알린다.
		} catch (Exception e) { // 정상적으로 수행하지 못하면 실패를 콘솔로 알린다.
			System.out.println("[Server] 중복 확인 실패 > " + e.toString());
		}

		return flag; // flag 반환
	}

	// 데이터베이스에 저장된 자신의 정보를 조회하는 메소드. 조회한 정보들을 String 형태로 반환한다.
	String viewInfo(String _nn) {
		String msg = "null"; // 반환할 문자열 변수를 "null"로 초기화.

		// 매개변수로 받은 닉네임을 nick에 초기화한다.
		String nick = _nn;

		try {
			// member 테이블에서 nick이라는 닉네임을 가진 회원의 이름과 이메일 정보를 조회한다.
			String viewStr = "SELECT name, email FROM member WHERE nickname='" + nick + "'";
			ResultSet result = stmt.executeQuery(viewStr);

			int count = 0;
			while (result.next()) {
				// msg에 "이름//닉네임//이메일" 형태로 초기화한다.
				msg = result.getString("name") + "//" + nick + "//" + result.getString("email");
				count++;
			}
			System.out.println("[Server] 회원정보 조회 성공"); // 정상적으로 수행되면 성공을 콘솔로 알린다.
		} catch (Exception e) { // 정상적으로 수행하지 못하면 실패를 콘솔로 알린다.
			System.out.println("[Server] 회원정보 조회 실패 > " + e.toString());
		}

		return msg; // msg 반환
	}

	// 닉네임을 기반으로 member 테이블에서 행을 삭제하는 메소드
	boolean deleteDB(String nick) {
		boolean flag = false; // 삭제가 성공했는지 여부를 나타내는 플래그

		try {
			// 닉네임을 기반으로 member 테이블에서 행을 삭제하는 SQL 문장
			String deleteStr = "DELETE FROM member WHERE nickname='" + nick + "'";

			// 삭제 문장을 실행
			int rowsDeleted = stmt.executeUpdate(deleteStr);

			// 삭제된 행이 있는지 확인
			if (rowsDeleted > 0) {
				flag = true; // 삭제 성공
				System.out.println("[Server] 회원 삭제 성공");
			} else {
				flag = false; // 삭제된 행이 없음
				System.out.println("[Server] 해당하는 닉네임을 가진 회원이 없습니다.");
			}
		} catch (Exception e) {
			flag = false; // 삭제 실패
			System.out.println("[Server] 회원 삭제 실패 > " + e.toString());
		}

		return flag;
	}

	public boolean optChange(String m, String _i, String _p, String _n, String _nn, String _s, String _z, String _a,
			String _e, String _bir, String _img) {
		boolean flag = false;
		// SQL 업데이트 쿼리 생성
		String sql = "UPDATE member SET ID=?, Name=?, Nickname=?, Password=?, Sex=?, Email=?, Zip=?, Address=?, Birthday=?, img=? WHERE Nickname=?";

		try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
			// 업데이트 쿼리의 매개변수 설정
			preparedStatement.setString(1, _i);
			preparedStatement.setString(2, _n);
			preparedStatement.setString(3, _nn);
			preparedStatement.setString(4, _p);
			preparedStatement.setString(5, _s);
			preparedStatement.setString(6, _e);
			preparedStatement.setString(7, _z);
			preparedStatement.setString(8, _a);
			preparedStatement.setString(9, _bir);
			preparedStatement.setString(10, _img);
			preparedStatement.setString(11, m);

			// 업데이트 쿼리 실행
			int rowsUpdated = preparedStatement.executeUpdate();

			// 업데이트가 성공했는지 확인
			if (rowsUpdated > 0) {
				System.out.println("업데이트 성공!");
				flag = true;
			} else {
				flag = false;
				System.out.println("지정된 닉네임에 해당하는 레코드가 없습니다.");
			}
		} catch (SQLException e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	// 회원정보를 변경을 수행하는 메소드. 변경에 성공하면 true, 실패하면 false를 반환한다.
	boolean changeInfo(String _nn, String _a, String _v) {
		boolean flag = false; // 참거짓을 반환할 flag 변수. 초기값은 false.

		// 매개변수로 받은 정보들을 초기화한다. att는 속성(이름, 이메일, 비밀번호) 구분용이고 val은 바꿀 값.
		String nick = _nn;
		String att = _a;
		String val = _v;
		String[] ext = null;
		String msg = "";
		try {
			// member 테이블에서 nick이라는 닉네임을 가진 회원의 att(이름, 이메일, 비밀번호)를 val로 변경한다.
			String changeStr = "UPDATE member SET " + att + "='" + val + "' WHERE nickname='" + nick + "'";
			stmt.executeUpdate(changeStr);
			if (att.equals("Nickname")) {
				changeStr = "SELECT img, nickname FROM member WHERE nickname='" + val + "'";
				ResultSet result = stmt.executeQuery(changeStr);
				int count = 0;

				while (result.next()) {
					msg = result.getString("img");
					count++;
				}
				System.out.println(msg);
				msg = msg.substring(msg.lastIndexOf("."));
				System.out.println(msg);
				changeStr = "UPDATE member SET img ='" + val + msg + "' WHERE nickname='" + val + "'";
				stmt.executeUpdate(changeStr);
			}

			flag = true; // 정상적으로 수행되면 flag를 true로 바꾸고 성공을 콘솔로 알린다.
			System.out.println("[Server] 회원정보 변경 성공");
		} catch (Exception e) { // 정상적으로 수행하지 못하면 flag를 false로 바꾸고 실패를 콘솔로 알린다.
			flag = false;
			System.out.println("[Server] 회원정보 변경 실패 > " + e.toString());
		}

		return flag; // flag 반환
	}

	// 전체 회원의 전적을 조회하는 메소드. 모든 회원의 전적을 String 형태로 반환한다.
	String viewRank() {
		String msg = ""; // 전적을 받을 문자열. 초기값은 ""로 한다.

		try {
			// member 테이블의 닉네임, 승, 패를 모두 조회한다.
			String viewStr = "SELECT nickname, win, lose FROM member";
			ResultSet result = stmt.executeQuery(viewStr);

			int count = 0;
			while (result.next()) {
				// 기존의 msg에 "닉네임 : n승 n패@" 형태의 문자열을 계속해서 추가한다.
				msg = msg + result.getString("nickname") + " : " + result.getInt("win") + "승 " + result.getInt("lose")
						+ "패@";
				count++;
			}
			System.out.println("[Server] 전적 조회 성공"); // 정상적으로 수행되면 성공을 콘솔로 알린다.
		} catch (Exception e) { // 정상적으로 수행하지 못하면 실패를 콘솔로 알린다.
			System.out.println("[Server] 전적 조회 실패 > " + e.toString());
		}

		return msg; // msg 반환
	}

	// 한 명의 회원의 전적을 조회하는 메소드. 해당 회원의 전적을 String 형태로 반환한다.
	String searchRank(String _nn) {
		String msg = "null"; // 전적을 받을 문자열. 초기값은 "null"로 한다.

		// 매개변수로 받은 닉네임을 초기화한다.
		String nick = _nn;

		try {
			// member 테이블에서 nick이라는 닉네임을 가진 회원의 승, 패를 조회한다.
			String searchStr = "SELECT win, lose FROM member WHERE nickname='" + nick + "'";
			ResultSet result = stmt.executeQuery(searchStr);

			int count = 0;
			while (result.next()) {
				// msg에 "닉네임 : n승 n패" 형태의 문자열을 초기화한다.
				msg = nick + "@" + result.getInt("win") + "@" + result.getInt("lose");
				count++;
			}
			System.out.println("[Server] 전적 조회 성공"); // 정상적으로 수행되면 성공을 콘솔로 알린다.
		} catch (Exception e) { // 정상적으로 수행하지 못하면 실패를 콘솔로 알린다.
			System.out.println("[Server] 전적 조회 실패 > " + e.toString());
		}

		return msg; // msg 반환
	}

	// 게임 승리 시 전적을 업데이트하는 메소드. 조회 및 업데이트에 성공하면 true, 실패하면 false를 반환한다.
	boolean winRecord(String _nn) {
		boolean flag = false; // 참거짓을 반환할 flag 변수. 초기값은 false.

		// 매개변수로 받은 닉네임과 조회한 승리 횟수를 저장할 변수. num의 초기값은 0.
		String nick = _nn;
		int num = 0;

		try {
			// member 테이블에서 nick이라는 닉네임을 가진 회원의 승리 횟수를 조회한다.
			String searchStr = "SELECT win FROM member WHERE nickname='" + nick + "'";
			ResultSet result = stmt.executeQuery(searchStr);

			int count = 0;
			while (result.next()) {
				// num에 조회한 승리 횟수를 초기화.
				num = result.getInt("win");
				count++;
			}
			num++; // 승리 횟수를 올림

			// member 테이블에서 nick이라는 닉네임을 가진 회원의 승리 횟수를 num으로 업데이트한다.
			String changeStr = "UPDATE member SET win=" + num + " WHERE nickname='" + nick + "'";
			stmt.executeUpdate(changeStr);
			flag = true; // 조회 및 업데이트 성공 시 flag를 true로 바꾸고 성공을 콘솔로 알린다.
			System.out.println("[Server] 전적 업데이트 성공");
		} catch (Exception e) { // 조회 및 업데이트 실패 시 flag를 false로 바꾸고 실패를 콘솔로 알린다.
			flag = false;
			System.out.println("[Server] 전적 업데이트 실패 > " + e.toString());
		}

		return flag; // flag 반환
	}

	// 게임 패배 시 전적을 업데이트하는 메소드. 조회 및 업데이트에 성공하면 true, 실패하면 false를 반환한다.
	boolean loseRecord(String _nn) {
		boolean flag = false; // 참거짓을 반환할 flag 변수. 초기값은 false.

		// 매개변수로 받은 닉네임과 조회한 패배 횟수를 저장할 변수. num의 초기값은 0.
		String nick = _nn;
		int num = 0;

		try {
			// member 테이블에서 nick이라는 닉네임을 가진 회원의 패배 횟수를 조회한다.
			String searchStr = "SELECT lose FROM member WHERE nickname='" + nick + "'";
			ResultSet result = stmt.executeQuery(searchStr);

			int count = 0;
			while (result.next()) {
				// num에 조회한 패배 횟수를 초기화.
				num = result.getInt("lose");
				count++;
			}
			num++; // 패배 횟수를 올림

			// member 테이블에서 nick이라는 닉네임을 가진 회원의 승리 횟수를 num으로 업데이트한다.
			String changeStr = "UPDATE member SET lose=" + num + " WHERE nickname='" + nick + "'";
			stmt.executeUpdate(changeStr);
			flag = true; // 조회 및 업데이트 성공 시 flag를 true로 바꾸고 성공을 콘솔로 알린다.
			System.out.println("[Server] 전적 업데이트 성공");
		} catch (Exception e) { // 조회 및 업데이트 실패 시 flag를 false로 바꾸고 실패를 콘솔로 알린다.
			flag = false;
			System.out.println("[Server] Error: > " + e.toString());
		}

		return flag; // flag 반환

	}

	String zipInfo(String _z) {
		String msg = ""; // 전적을 받을 문자열. 초기값은 ""로 한다.
		String doro = _z;
		try {
			// member 테이블의 닉네임, 승, 패를 모두 조회한다.
			String searchStr = "SELECT zipcode, sido, sigungu, eupmyun, doro, buildno1, buildno2, buildname FROM zipcode where doro like '%"
					+ doro + "%' LIMIT 200";
			ResultSet result = stmt.executeQuery(searchStr);

			int count = 0;
			while (result.next()) {
				// 기존의 msg에 "zip :주소@" 형태의 문자열을 계속해서 추가한다.
				msg = msg + result.getString("zipcode") + " : " + result.getString("sido") + " "
						+ result.getString("sigungu") + " " + result.getString("eupmyun") + " "
						+ result.getString("doro") + " " + result.getString("buildno1") + "-"
						+ result.getString("buildno2") + " " + result.getString("buildname") + "@";
				count++;
			}
			System.out.println("[Server] 우편 조회 성공"); // 정상적으로 수행되면 성공을 콘솔로 알린다.
		} catch (Exception e) { // 정상적으로 수행하지 못하면 실패를 콘솔로 알린다.
			System.out.println("[Server] 전적 조회 실패 > " + e.toString());
		}

		return msg; // msg 반환
	}

	String imgInfo(String _nn) {
		String msg = ""; // 전적을 받을 문자열. 초기값은 ""로 한다.
		String nick = _nn;
		try {
			// member 테이블의 닉네임, 승, 패를 모두 조회한다.
			String searchStr = "SELECT img FROM member WHERE nickname = '" + nick + "'";
			ResultSet result = stmt.executeQuery(searchStr);

			int count = 0;
			while (result.next()) {
				// 기존의 msg에 "zip :주소@" 형태의 문자열을 계속해서 추가한다.
				msg = result.getString("img");
				count++;
			}
			System.out.println("[Server] 이미지 조회 성공"); // 정상적으로 수행되면 성공을 콘솔로 알린다.
		} catch (Exception e) { // 정상적으로 수행하지 못하면 실패를 콘솔로 알린다.
			System.out.println("[Server] 전적 조회 실패 > " + e.toString());
		}

		return msg; // msg 반환
	}

	String changeInfo(String _nn) {
		String msg = "";
		String nick = _nn;
		try {
			// member 테이블의 닉네임, 승, 패를 모두 조회한다.
			String searchStr = "SELECT name,nickname,password,zip,address,email,img FROM member WHERE nickname = '"
					+ nick + "'";
			ResultSet result = stmt.executeQuery(searchStr);

			int count = 0;
			while (result.next()) {
				// 기존의 msg에 "zip :주소@" 형태의 문자열을 계속해서 추가한다.
				msg = result.getString("name") + "@" + nick + "@" + result.getString("password") + "@"
						+ result.getString("email") + "@" + result.getString("zip") + ":" + result.getString("address")
						+ "@" + result.getString("img");
				count++;
			}
			System.out.println("[Server] 정보 조회 성공"); // 정상적으로 수행되면 성공을 콘솔로 알린다.
		} catch (Exception e) { // 정상적으로 수행하지 못하면 실패를 콘솔로 알린다.
			System.out.println("[Server] 전적 조회 실패 > " + e.toString());
		}
		return msg;
	}

	String OPTInfo() {
		String msg = "";

		try {
			// member 테이블의 닉네임, 승, 패를 모두 조회한다.
			String searchStr = "SELECT * FROM member";
			ResultSet result = stmt.executeQuery(searchStr);

			int count = 0;
			while (result.next()) {
				// 기존의 msg에 "zip :주소@" 형태의 문자열을 계속해서 추가한다.
				msg = msg + result.getString("id") + "@@" + result.getString("name") + "@@"
						+ result.getString("nickname") + "@@" + result.getString("password") + "@@"
						+ result.getString("sex") + "@@" + result.getString("email") + "@@" + result.getString("zip")
						+ "@@" + result.getString("address") + "@@" + result.getString("birthday") + "@@"
						+ result.getString("img") + "@@" + result.getInt("win") + "@@" + result.getInt("lose") + "||";
				count++;
			}
			System.out.println("[Server] 정보전부 조회 성공"); // 정상적으로 수행되면 성공을 콘솔로 알린다.
		} catch (Exception e) { // 정상적으로 수행하지 못하면 실패를 콘솔로 알린다.
			System.out.println("[Server] 전적 조회 실패 > " + e.toString());
		}

		return msg;
	}

	public boolean addFriend(String user1, String user2) {
		boolean flag = false;
		String sql = "INSERT INTO friendships (user1, user2) VALUES (?, ?), (?, ?)";
		try (PreparedStatement statement = con.prepareStatement(sql)) {
			statement.setString(1, user1);
			statement.setString(2, user2);
			statement.setString(3, user2); // 역방향도 추가
			statement.setString(4, user1); // 역방향도 추가

			int rowsAffected = statement.executeUpdate();

			// Check if the query was successful
			if (rowsAffected > 0) {
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// 예외 처리 필요
			flag = false;
		}

		return flag;
	}

	// 친구 목록 조회 메소드
	public String getFriendList(String nick) {
		String sql = "SELECT user2 FROM friendships WHERE user1 = ? ";
		List<String> friendList = new ArrayList<>();

		try (PreparedStatement statement = con.prepareStatement(sql)) {
			statement.setString(1, nick);
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					friendList.add(resultSet.getString("user2"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// 예외 처리 필요
		}

		// 친구 목록을 문자열로 변환
		return String.join("@", friendList);
	}

	public boolean updateFriendshipsForNicknameChange(String oldNickname, String newNickname) {
		boolean flag = false;
		String updateSql = "UPDATE friendships SET user1 = ? WHERE user1 = ?";
		String updateSqlReverse = "UPDATE friendships SET user2 = ? WHERE user2 = ?";

		try (PreparedStatement statement = con.prepareStatement(updateSql);
				PreparedStatement statementReverse = con.prepareStatement(updateSqlReverse)) {

			statement.setString(1, newNickname);
			statement.setString(2, oldNickname);

			statementReverse.setString(1, newNickname);
			statementReverse.setString(2, oldNickname);

			int rowsAffected = statement.executeUpdate();
			int rowsAffectedReverse = statementReverse.executeUpdate();

			// Check if the query was successful
			if (rowsAffected > 0 || rowsAffectedReverse > 0) {
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// 예외 처리 필요
			flag = false;
		}

		return flag;
	}

	String findId(String nm, String eml) {
		String msg = ""; // 전적을 받을 문자열. 초기값은 ""로 한다.

		try {
			// member 테이블의 닉네임, 승, 패를 모두 조회한다.
			String viewStr = "SELECT id FROM member WHERE name='" + nm + "'and email ='" + eml + "'";
			ResultSet result = stmt.executeQuery(viewStr);

			int count = 0;
			while (result.next()) {
				// 기존의 msg에 "닉네임 : n승 n패@" 형태의 문자열을 계속해서 추가한다.
				msg = msg + result.getString("id");
				count++;
			}
			System.out.println("[Server] 승패 조회 성공"); // 정상적으로 수행되면 성공을 콘솔로 알린다.
		} catch (Exception e) { // 정상적으로 수행하지 못하면 실패를 콘솔로 알린다.
			System.out.println("[Server] 전적 조회 실패 > " + e.toString());
		}

		return msg; // msg 반환
	}

	String findPassword(String nm, String eml, String id) {
		String msg = ""; // 전적을 받을 문자열. 초기값은 ""로 한다.

		try {
			// member 테이블의 닉네임, 승, 패를 모두 조회한다.
			String viewStr = "SELECT nickname, password FROM member WHERE name='" + nm + "'and email ='" + eml + "'";
			ResultSet result = stmt.executeQuery(viewStr);

			int count = 0;
			while (result.next()) {
				// 기존의 msg에 "닉네임 : n승 n패@" 형태의 문자열을 계속해서 추가한다.
				msg = msg + result.getString("nickname") + "@@" + result.getString("password");
				count++;
			}
			System.out.println("[Server] 비밀번호 조회 성공"); // 정상적으로 수행되면 성공을 콘솔로 알린다.
		} catch (Exception e) { // 정상적으로 수행하지 못하면 실패를 콘솔로 알린다.
			System.out.println("[Server] 전적 조회 실패 > " + e.toString());
		}

		return msg; // msg 반환
	}
	String findPassword(String nm) {
		String msg = ""; // 전적을 받을 문자열. 초기값은 ""로 한다.

		try {
			// member 테이블의 닉네임, 승, 패를 모두 조회한다.
			String viewStr = "SELECT nickname, password FROM member WHERE nickname='" + nm + "'";
			ResultSet result = stmt.executeQuery(viewStr);

			int count = 0;
			while (result.next()) {
				// 기존의 msg에 "닉네임 : n승 n패@" 형태의 문자열을 계속해서 추가한다.
				msg = msg + result.getString("nickname") + "@@" + result.getString("password");
				count++;
			}
			System.out.println("[Server] 비밀번호 조회 성공"); // 정상적으로 수행되면 성공을 콘솔로 알린다.
		} catch (Exception e) { // 정상적으로 수행하지 못하면 실패를 콘솔로 알린다.
			System.out.println("[Server] 전적 조회 실패 > " + e.toString());
		}

		return msg; // msg 반환
	}
	

	boolean changePassword(String nm, String pw) {
		boolean flag = false; // 전적을 받을 문자열. 초기값은 ""로 한다.

		try {
			String changeStr = "UPDATE member SET password ='" + pw + "' WHERE nickname='" + nm + "'";
			stmt.executeUpdate(changeStr);
			flag = true; // 조회 및 업데이트 성공 시 flag를 true로 바꾸고 성공을 콘솔로 알린다.
			System.out.println("[Server] 비밀번호 변경 성공"); // 정상적으로 수행되면 성공을 콘솔로 알린다.
		} catch (Exception e) { // 정상적으로 수행하지 못하면 실패를 콘솔로 알린다.
			flag = false;
			System.out.println("[Server] 전적 조회 실패 > " + e.toString());
		}

		return flag; // msg 반환
	}

	public void saveChatHistory(String username, String message) {
		// 현재 시간 얻기
		LocalDateTime currentTime = LocalDateTime.now();

		// SQL 쿼리 준비
		String sql = "INSERT INTO chat (nickname, message, chat_time) VALUES (?, ?, ?)";
		try (PreparedStatement statement = con.prepareStatement(sql)) {
			// 파라미터 설정
			statement.setString(1, username);
			statement.setString(2, message);
			statement.setObject(3, currentTime);

			// 쿼리 실행
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getChatHistoryByNick(String nick) {
		StringBuilder result = new StringBuilder();

		String sql = "SELECT nickname, message, chat_time FROM chat WHERE nickname = ?";
		try (PreparedStatement statement = con.prepareStatement(sql)) {
			// 파라미터 설정
			statement.setString(1, nick);

			// 쿼리 실행 및 결과 가져오기
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					String nickname = resultSet.getString("nickname");
					String message = resultSet.getString("message");
					java.sql.Timestamp chatTime = resultSet.getTimestamp("chat_time");

					// 결과를 문자열로 추가
					result.append(nickname).append("$$").append(message).append("$$").append(formatTimestamp(chatTime))
							.append("&&");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result.toString();
	}

	public void updateUsername(String oldUsername, String newUsername) {
		// SQL 쿼리 준비
		String sql = "UPDATE chat SET nickname = ? WHERE nickname = ?";
		try (PreparedStatement statement = con.prepareStatement(sql)) {
			// 파라미터 설정
			statement.setString(1, newUsername);
			statement.setString(2, oldUsername);

			// 쿼리 실행
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static String formatTimestamp(java.sql.Timestamp timestamp) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년MM월dd일 HH시mm분ss");
		return dateFormat.format(timestamp);
	}

	public static String formatTimestamp(LocalDateTime ldt) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년MM월dd일 HH시mm분ss초");
		return ldt.format(formatter);
	}

	public void insertRecord(String nick1, String nick2, String romok, String winColor, String currentTime) {
		try {

			// SQL query to insert data into the record_omok table
			String sql = "INSERT INTO record_omok (black_user, white_user, romok, win_color,play_time) VALUES (?, ?, ?, ?,?)";

			try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
				preparedStatement.setString(1, nick1);
				preparedStatement.setString(2, nick2);
				preparedStatement.setString(3, romok);
				preparedStatement.setString(4, winColor);
				preparedStatement.setString(5, currentTime);

				preparedStatement.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();

		}
	}

	public String retrieveConcatenatedData() {
		StringBuilder concatenatedData = new StringBuilder();

		try {
			// SQL query to select all data from the record_omok table
			String selectSql = "SELECT CONCAT(black_user, '$', white_user, '$', romok, '$', win_color, '$', play_time, '&&') AS concatenated_data FROM record_omok";

			try (PreparedStatement selectStatement = con.prepareStatement(selectSql);
					ResultSet resultSet = selectStatement.executeQuery()) {

				while (resultSet.next()) {
					concatenatedData.append(resultSet.getString("concatenated_data"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return concatenatedData.toString();
	}

	String[] omokplaytime() {
		String msg = ""; // 전적을 받을 문자열. 초기값은 ""로 한다.

		try {
			// member 테이블의 닉네임, 승, 패를 모두 조회한다.
			String viewStr = "SELECT play_time FROM record_omok";
			ResultSet result = stmt.executeQuery(viewStr);

			int count = 0;
			while (result.next()) {
				// 기존의 msg에 "닉네임 : n승 n패@" 형태의 문자열을 계속해서 추가한다.
				msg = msg + result.getString("play_time") + "@";
				count++;
			}
			System.out.println("[Server] 플레이타임 조회 성공"); // 정상적으로 수행되면 성공을 콘솔로 알린다.
		} catch (Exception e) { // 정상적으로 수행하지 못하면 실패를 콘솔로 알린다.
			System.out.println("[Server] 전적 조회 실패 > " + e.toString());
		}

		return msg.split("@"); // msg 반환
	}

	public void insertFontOption(String nickname) {
		try {
			// Reuse the existing stmt prepared statement
			stmt = con.prepareStatement(INSERT_QUERY_FONT);
			((PreparedStatement) stmt).setString(1, nickname);
			((PreparedStatement) stmt).executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace(); // Handle the exception appropriately
		}
	}

	public String getFontOption(String nickname) {
		try {
			// Reuse the existing stmt prepared statement
			stmt = con.prepareStatement(SELECT_QUERY_FONT);
			((PreparedStatement) stmt).setString(1, nickname);
			ResultSet resultSet = ((PreparedStatement) stmt).executeQuery();

			if (resultSet.next()) {
				// 원하는 형태로 문자열 조합
				return resultSet.getString("mycolor") + "@" + resultSet.getString("mybackcolor") + "@"
						+ resultSet.getString("yourcolor") + "@" + resultSet.getString("yourbackcolor") + "@"
						+ resultSet.getString("fontsize") + "@" + resultSet.getString("font") + "@"
						+ resultSet.getString("blackstonecolor") + "@" + resultSet.getString("whitestonecolor") + "@"
						+ resultSet.getString("boardcolor") + "@" + resultSet.getString("linecolor") + "@"
						+ resultSet.getString("musicvolume") + "@" + resultSet.getString("stonevolume");
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Handle the exception appropriately
		}

		return null;
	}

	public void updateFontOptions(String nickname, String mycolor, String mybackcolor, String yourcolor,
			String yourbackcolor, String fontsize, String font) {
		try {
			String updateStr = "UPDATE fontoption SET mycolor=?, mybackcolor=?, yourcolor=?, yourbackcolor=?, fontsize=?, font=? WHERE nickname=?";
			stmt = con.prepareStatement(updateStr);
			((PreparedStatement) stmt).setString(1, mycolor);
			((PreparedStatement) stmt).setString(2, mybackcolor);
			((PreparedStatement) stmt).setString(3, yourcolor);
			((PreparedStatement) stmt).setString(4, yourbackcolor);
			((PreparedStatement) stmt).setString(5, fontsize);
			((PreparedStatement) stmt).setString(6, font);
			((PreparedStatement) stmt).setString(7, nickname);
			((PreparedStatement) stmt).executeUpdate();
			System.out.println("[Server] 폰트 옵션 업데이트 성공");
		} catch (SQLException e) {
			System.out.println("[Server] 폰트 옵션 업데이트 실패 > " + e.toString());
		}
	}

	public void updateboardOptions(String nickname, String blackstonecolor, String whitestonecolor, String boardcolor,
			String linecolor) {
		try {
			String updateStr = "UPDATE fontoption SET blackstonecolor=?, whitestonecolor=?, boardcolor=?, linecolor=? WHERE nickname=?";
			stmt = con.prepareStatement(updateStr);
			((PreparedStatement) stmt).setString(1, blackstonecolor);
			((PreparedStatement) stmt).setString(2, whitestonecolor);
			((PreparedStatement) stmt).setString(3, boardcolor);
			((PreparedStatement) stmt).setString(4, linecolor);
			((PreparedStatement) stmt).setString(5, nickname);
			((PreparedStatement) stmt).executeUpdate();
			System.out.println("[Server] 보드 옵션 업데이트 성공");
		} catch (SQLException e) {
			System.out.println("[Server] 보드 옵션 업데이트 실패 > " + e.toString());
		}
	}

	public void updatemusicOptions(String nickname, String musicvolume, String stonevolume) {
		try {
			String updateStr = "UPDATE fontoption SET musicvolume=?, stonevolume=? WHERE nickname=?";
			stmt = con.prepareStatement(updateStr);
			((PreparedStatement) stmt).setString(1, musicvolume);
			((PreparedStatement) stmt).setString(2, stonevolume);
			((PreparedStatement) stmt).setString(3, nickname);
			((PreparedStatement) stmt).executeUpdate();
			System.out.println("[Server] 보드 옵션 업데이트 성공");
		} catch (SQLException e) {
			System.out.println("[Server] 보드 옵션 업데이트 실패 > " + e.toString());
		}
	}

}
