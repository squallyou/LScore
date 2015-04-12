package sjy.elwg.notation.musicBeans.symbolLines;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import sjy.elwg.notation.NoteCanvas;
import sjy.elwg.notation.musicBeans.Selectable;
/**
 * 力度记号
 * @author wenxi.lu
 *
 */
public class Breath extends NoteSymbol implements MouseListener, MouseMotionListener,Selectable{
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1782438855027858506L;
	/**
	 * 被用户所拖动过的x距离和y距离
	 * 在进行符号放置时，其位置是默认位置与拖动位置之和
	 */
	protected int draggedX = 0;
	protected int draggedY = 0;
	

	/**
	 * 鼠标事件相对屏幕的坐标
	 */
	protected Point screenPoint = new Point(); 
	
	
	
	/**
	 * 构造函数
	 * @param type
	 */
	public Breath(){
		super("breath");
		addMouseListener(this);
		addMouseMotionListener(this);
		setOpaque(false);
		setSize(20,20);
	}

	
	public void paintComponent(Graphics gg){
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg;	
		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g.setRenderingHints(renderHints);
		g.setFont(NoteCanvas.MCORE_FONT.deriveFont(35f));
		if(selected) g.setColor(Color.blue);
		else g.setColor(Color.black);
	    g.drawString("\uE17A",2, getHeight()/2);

	}

	public void beSelected() {
		// TODO Auto-generated method stub
		if(!selected){
			selected = true;
			repaint();
		}
	}

	public void cancleSelected() {
		// TODO Auto-generated method stub
		if(selected){
			selected = false;
			repaint();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		//更新坐标
		screenPoint.setLocation((int)e.getXOnScreen(), (int)e.getYOnScreen());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void adjustSize() {
		// TODO Auto-generated method stub
		setSize(20,20);
	}



	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		selected = true;
		int x = e.getXOnScreen();
    	int y = e.getYOnScreen();
    	
    	int deltax = x - (int)screenPoint.getX();
    	int deltay = y - (int)screenPoint.getY();
    	
    	screenPoint.setLocation(x, y);
    	draggedX += deltax;
    	draggedY += deltay;
    	
    	int curX = getX();
    	int curY = getY();
    	setLocation(curX + deltax, curY + deltay);
	}



	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
