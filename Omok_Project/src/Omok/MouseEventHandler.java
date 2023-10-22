package Omok;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseEventHandler extends MouseAdapter {
	private Map map;
	private MapSize ms;
	private DrawBoard d;
	private Gui main;
	
	public MouseEventHandler(Map m, MapSize ms, DrawBoard d, Gui main) {
		map = m;
		this.ms = ms;
		this.d = d;
		this.main = main;
	}
	
	@Override
	public void mousePressed(MouseEvent arg0) {
		super.mousePressed(arg0);
		//입력된 x,y 좌표를 Cell(30)로 나눠 나온 값에 1,2를 빼 0~19사이로 맞춤
		int x =(int) Math.round(arg0.getX()/(double)ms.getCell())-1;
		int y =(int) Math.round(arg0.getY()/(double)ms.getCell())-2;
		if(x < 0 || x > 19 || y < 0 || y > 19) {
			return;
			
		}
		if(map.getXY(y, x) == map.getBlack()|| map.getXY(y, x)==map.getWhite()) {
			//이미 놓여진 자리일 경우 return
			return;
		}
		System.out.println(x+""+y);
		map.setMap(y, x);
		map.changeCheck();
		d.repaint();
		if(map.winCheck(x, y)) {
			if(map.getCheck()==true) {
				main.showPopUP("백돌이 승리");
			}else {
				main.showPopUP("흑돌이 승리");
			}
		}
		
	}
	
	
	
}
