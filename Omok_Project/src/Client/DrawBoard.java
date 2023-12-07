package Client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class DrawBoard extends JPanel {

	private MapSize size;
	private Map map;
	private final int STONE_SIZE = 28; // 돌 사이즈
	Operator o;
	Color blackstonecolor;
	Color whitestonecolor;
	Color boardcolor;
	Color linecolor;

	public DrawBoard(Operator _o, MapSize m, Map map) {
		o = _o;
		setBackground(o.fontopt.boardcolor);
		size = m;
		this.map = map;
		setPreferredSize(new Dimension(size.getCell() * size.getSize(), size.getCell() * size.getSize()));

	}

	@Override
	public void paintComponent(Graphics arg0) {
		super.paintComponent(arg0);
		setBackground(o.fontopt.boardcolor);
		arg0.setColor(o.fontopt.linecolor);
		board(arg0);
		drawStone(arg0);
	}

	public void board(Graphics arg0) {
		for (int i = 1; i <= size.getSize(); i++) {
			arg0.drawLine(size.getCell(), i * size.getCell(), size.getCell() * size.getSize(), i * size.getCell());
			arg0.drawLine(i * size.getCell(), size.getCell(), i * size.getCell(), size.getCell() * size.getSize());
		}
	}

	public void drawStone(Graphics arg0) {
		for (int y = 0; y < size.getSize(); y++) {
			for (int x = 0; x < size.getSize(); x++) {
				if (map.getXY(y, x) == map.getBlack())
					drawBlack(arg0, x, y);
				else if (map.getXY(y, x) == map.getWhite())
					drawWhite(arg0, x, y);
			}
		}
	}

	public void drawBlack(Graphics arg0, int x, int y) {
		arg0.setColor(Color.BLACK);

		// 외곽선 그리기
		arg0.drawOval((x + 1) * size.getCell() - 15, y * size.getCell() + 15, STONE_SIZE, STONE_SIZE);

		// 돌 내부를 채우기 위한 설정
		arg0.setColor(o.fontopt.blackstonecolor);

		// 돌 그리기
		arg0.fillOval((x + 1) * size.getCell() - 15, y * size.getCell() + 15, STONE_SIZE, STONE_SIZE);

	}

	public void drawWhite(Graphics arg0, int x, int y) {

		arg0.setColor(Color.WHITE);
		arg0.drawOval(x * size.getCell() + 15, y * size.getCell() + 15, STONE_SIZE, STONE_SIZE);

		// 돌 내부를 채우기 위한 설정
		arg0.setColor(o.fontopt.whitestonecolor);

		// 돌 그리기
		arg0.fillOval(x * size.getCell() + 15, y * size.getCell() + 15, STONE_SIZE, STONE_SIZE);
	}

	public BufferedImage captureImage() {
		blackstonecolor = o.fontopt.blackstonecolor;
		whitestonecolor = o.fontopt.whitestonecolor;
		boardcolor = o.fontopt.boardcolor;
		linecolor = o.fontopt.linecolor;
		o.fontopt.blackstonecolor = Color.black;
		o.fontopt.whitestonecolor = Color.white;
		o.fontopt.boardcolor = o.fontopt.stringToColor("#cea73d");
		o.fontopt.linecolor = Color.black;
		repaint();
		int captureWidth = 625; // 원하는 캡처 폭
		int captureHeight = 625; // 원하는 캡처 높이
		int oriWidth = getWidth();
		int oriHeight = getHeight();
		BufferedImage image = new BufferedImage(captureWidth, captureHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();

		// 원하는 부분만 캡처하도록 좌표를 조절합니다.
		int captureX = 0; // 시작 X 좌표
		int captureY = 0; // 시작 Y 좌표

		g.translate(-captureX, -captureY);

		// 원하는 부분만 캡처하도록 크기를 조절합니다.
		setBounds(captureX, captureY, captureWidth, captureHeight);

		paint(g); // DrawBoard의 내용을 이미지에 그림
		g.dispose();

		// 원래 크기로 다시 설정합니다.
		setBounds(0, 0, oriWidth, oriHeight);
		o.fontopt.blackstonecolor = blackstonecolor;
		o.fontopt.whitestonecolor = whitestonecolor;
		o.fontopt.boardcolor = boardcolor;
		o.fontopt.linecolor = linecolor;
		repaint();

		return image;
	}

}