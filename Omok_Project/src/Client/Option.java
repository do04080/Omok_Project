package Client;

import java.awt.Color;

public class Option {
	Color mycolor; // 내 메시지 글자 색
	Color mybackcolor; // 내 메시지 글자 배경색
	Color yourcolor; // 상대 메시지 글자 색
	Color yourbackcolor; // 상대 메시지 글자 배경색
	Color blackstonecolor;
	Color whitestonecolor;
	Color boardcolor;
	Color linecolor;
	int fontsize; // 글자 크기
	String fontname; // 글자 폰트
	float musicvolume;
	float stonevolume;

	Option() {
		mycolor = null;
		mybackcolor = null;
		yourcolor = null;
		yourbackcolor = null;
		blackstonecolor = null;
		whitestonecolor = null;
		fontsize = 12;
		fontname = null;
	}

	/* 색을 문자열로 치환 */
	public String colorToString(Color color) {
		return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
	}

	/* 문자열을 색으로 치환 */
	public Color stringToColor(String colorString) {
		return Color.decode(colorString);
	}

	/* 서버에 저장되어 있는 내 옵션을 가져와서 저장하는 메소드 */
	public void splitOption(String[] optdata) {
		mycolor = stringToColor(optdata[0]);
		mybackcolor = stringToColor(optdata[1]);
		yourcolor = stringToColor(optdata[2]);
		yourbackcolor = stringToColor(optdata[3]);
		fontsize = Integer.parseInt(optdata[4]);
		fontname = optdata[5];
		blackstonecolor = stringToColor(optdata[6]);
		whitestonecolor = stringToColor(optdata[7]);
		boardcolor = stringToColor(optdata[8]);
		linecolor = stringToColor(optdata[9]);
		musicvolume = Float.parseFloat(optdata[10]);
		stonevolume = Float.parseFloat(optdata[11]);

	}
}
