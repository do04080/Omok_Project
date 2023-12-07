package Client;

public class Map {

	private short[][] map;// 맵의 배열 1일때 흑,-1일때 백, 0일때 돌이 안놓여짐(
	private final short BLACK = 1;
	private final short WHITE = -1;
	private final short REMOVER = 0;
	private boolean checkBNW = true;// 흑백차례확인
	public int orderchk = 1;

	public Map(MapSize ms) {
		map = new short[ms.getSize()][];
		for (int i = 0; i < map.length; i++) {
			map[i] = new short[ms.getSize()];
		}
	}

	public void resetBoard() {
		// 보드를 초기 상태로 리셋하는 메서드입니다.

		// 맵의 각 위치를 순회하면서 초기값(0)으로 설정합니다.
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				map[i][j] = 0;
			}
		}

		// 차례를 초기화합니다.
		checkBNW = true;

		// 추가로 필요한 경우 다른 초기화 작업도 수행할 수 있습니다.
		// 예를 들어, 다른 변수들을 초기화하거나 추가적인 초기 설정을 할 수 있습니다.
	}

	public short getBlack() {
		return BLACK;
	}

	public short getWhite() {
		return WHITE;
	}

	public short getXY(int y, int x) {
		return map[y][x];
	}

	public boolean getCheck() {
		return checkBNW;
	}

	public void changeCheck() {
		if (checkBNW) {
			orderchk = -1;
			checkBNW = false;
		} else {
			orderchk = 1;
			checkBNW = true;
		}
	}

	public boolean isGameOver(short[][] board) {
		// 승자가 있는지 확인
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] != 0) {
					if (winCheck(j, i)) {
						return true; // 게임 종료, 플레이어가 이겼습니다
					}
				}
			}
		}

		// 비겼는지 확인 (빈 셀이 없으면 비긴 것으로 간주)
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == 0) {
					return false; // 빈 셀이 있으면 게임 계속
				}
			}
		}

		return true; // 게임 종료, 비긴 것으로 간주
	}

	public void setMap(int y, int x) {
		// checkBNW를 확인해 true일 때 map에 BLACK, false일 때 WHITE저장
		if (checkBNW) {
			map[y][x] = BLACK;
		} else {
			map[y][x] = WHITE;
		}
	}

	public void removeMap(int y, int x) {
		map[y][x] = REMOVER;
	}

	// 승리확인
	public boolean winCheck(int x, int y) {
		if (winCheckL(x, y) || winCheckLD(x, y) || winCheckLU(x, y) || winCheckR(x, y)

				|| winCheckRD(x, y) || winCheckRU(x, y) || winCheckUp(x, y) || winCheckDown(x, y)

				|| winCheckOneDown(x, y) || winCheckOneL(x, y) || winCheckOneLD(x, y) || winCheckOneLU(x, y)

				|| winCheckOneR(x, y) || winCheckOneRD(x, y) || winCheckOneUp(x, y) || winCheckOneRU(x, y)

				|| winCheckCenterLU(x, y) || winCheckCenterRL(x, y) || winCheckCenterRU(x, y)
				|| winCheckCenterUD(x, y)) {
			return true;
		} else {

			return false;
		}
	}

	// 위쪽
	public boolean winCheckUp(int x, int y) {
		try {
			for (int i = y; i < y + 5; i++) {
				if (map[y][x] != map[i][x])
					return false;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
		return true;
	}

	public boolean winCheckDown(int x, int y) {
		try {
			for (int i = y; i > y - 5; i--) {
				if (map[y][x] != map[i][x])
					return false;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
		return true;
	}

	public boolean winCheckRU(int x, int y) {
		try {
			for (int i = y, z = x; i > y - 5; i--, z++) {
				if (map[y][x] != map[i][z])
					return false;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
		return true;
	}

	public boolean winCheckLU(int x, int y) {
		try {
			for (int i = y, z = x; i > y - 5; i--, z--) {
				if (map[y][x] != map[i][z])
					return false;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
		return true;
	}

	public boolean winCheckR(int x, int y) {
		try {
			for (int z = x; z < x + 5; z++) {
				if (map[y][x] != map[y][z])
					return false;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
		return true;
	}

	public boolean winCheckL(int x, int y) {
		try {
			for (int i = y, z = x; z > x - 5; z--) {
				if (map[y][x] != map[i][z] || i > 19 || z > 19 || i < 0 || z < 0)
					return false;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
		return true;
	}

	public boolean winCheckRD(int x, int y) {
		try {
			for (int i = y, z = x; i < y + 5; i++, z++) {
				if (map[y][x] != map[i][z])
					return false;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
		return true;
	}

	public boolean winCheckLD(int x, int y) {
		try {
			for (int i = y, z = x; i < y + 5; i++, z--) {
				if (map[y][x] != map[i][z])
					return false;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
		return true;
	}

	// 한칸 위쪽
	public boolean winCheckOneUp(int x, int y) {
		try {
			for (int i = y - 1; i < y + 4; i++) {
				if (map[y][x] != map[i][x])
					return false;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
		return true;
	}

	// 한칸 아래쪽
	public boolean winCheckOneDown(int x, int y) {
		try {
			for (int i = y + 1; i > y - 4; i--) {
				if (map[y][x] != map[i][x])
					return false;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
		return true;
	}

	// 한칸 오른쪽 위 대각선

	public boolean winCheckOneRU(int x, int y) {
		try {
			for (int i = y + 1, z = x - 1; i > y - 4; i--, z++) {
				if (map[y][x] != map[i][z])
					return false;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
		return true;
	}

	// 한칸 왼쪽 위 대각선

	public boolean winCheckOneLU(int x, int y) {
		try {
			for (int i = y + 1, z = x + 1; i > y - 4; i--, z--) {
				if (map[y][x] != map[i][z])
					return false;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
		return true;
	}

	// 한칸 오른쪽
	public boolean winCheckOneR(int x, int y) {
		try {
			for (int z = x - 1; z < x + 4; z++) {
				if (map[y][x] != map[y][z])
					return false;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
		return true;
	}

	// 한칸 왼쪽
	public boolean winCheckOneL(int x, int y) {
		try {
			for (int z = x + 1; z > x - 4; z--) {
				if (map[y][x] != map[y][z])
					return false;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
		return true;
	}

	// 한칸 오른쪽 아래 대각선
	public boolean winCheckOneRD(int x, int y) {
		try {
			for (int i = y - 1, z = x - 1; i < y + 4; i++, z++) {
				if (map[y][x] != map[i][z] || i > 19 || z > 19 || i < 0 || z < 0)
					return false;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
		return true;
	}

	// 한칸 왼쪽 아래 대각선
	public boolean winCheckOneLD(int x, int y) {
		try {
			for (int i = y - 1, z = x + 1; i < y + 4; i++, z--) {
				if (map[y][x] != map[i][z])
					return false;
			}
		} catch (ArrayIndexOutOfBoundsException e) {

			return false;
		}
		return true;
	}

	// 센터 업다운

	public boolean winCheckCenterUD(int x, int y) {
		try {
			for (int i = y - 2; i < y + 3; i++) {
				if (map[y][x] != map[i][x])
					return false;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
		return true;
	}

	// 센터 라이트 레프트

	public boolean winCheckCenterRL(int x, int y) {
		try {
			for (int z = x - 2; z < x + 3; z++) {
				if (map[y][x] != map[y][z])
					return false;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
		return true;
	}

	// 센터 라이트 대각선

	public boolean winCheckCenterRU(int x, int y) {
		try {
			for (int i = y + 2, z = x - 2; i > y - 3; i--, z++) {
				if (map[y][x] != map[i][z])
					return false;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
		return true;
	}

	// 센터 레프트 대각선
	public boolean winCheckCenterLU(int x, int y) {
		try {
			for (int i = y + 2, z = x + 2; i > y - 4; i--, z--) {
				if (map[y][x] != map[i][z])

					return false;
			}
		} catch (ArrayIndexOutOfBoundsException e) {

			return false;
		}
		return true;
	}

	public int[] findForbiddenPattern() {
		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[y].length; x++) {
				if (map[y][x] == 0) {
					// 빈 공간에 돌을 놓아보고 금지된 패턴인지 확인
					map[y][x] = getCheck() ? getBlack() : getWhite();
					if (containsForbiddenPattern(x, y)) {
						// 금지된 패턴이라면 해당 좌표를 반환하고 원래대로 돌 상태를 복구
						map[y][x] = 0;
						return new int[] { y, x };
					}
					// 원래대로 돌 상태를 복구
					map[y][x] = 0;
				}
			}
		}
		// 금지된 패턴이 없으면 null 반환
		return null;
	}

	public boolean containsForbiddenPattern(int x, int y) {
		// 33나 44와 같은 금지된 패턴을 확인하고 발견되면 true를 반환합니다.
		if (contains33Pattern(x, y) || contains44Pattern(x, y)) {
			return true;
		}
		return false;
	}

	private boolean contains33Pattern(int x, int y) {
		// 33 패턴 확인: X X - X
		try {
			if (map[y][x] == map[y][x + 1] && map[y][x] == map[y][x + 2] && map[y][x] == map[y - 1][x]) {
				return true;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// 예외 처리
		}
		return false;
	}

	private boolean contains44Pattern(int x, int y) {
		// 44 패턴 확인: X X - -
		try {
			if (map[y][x] == map[y][x + 1] && map[y][x] == map[y][x + 2] && map[y][x] == map[y][x + 3]) {
				return true;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// 예외 처리
		}
		return false;
	}

	public short[][] getBoard() {
		return map;
	}

}