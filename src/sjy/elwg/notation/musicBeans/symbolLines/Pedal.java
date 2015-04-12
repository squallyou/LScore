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

public class Pedal extends NoteSymbol implements MouseListener,MouseMotionListener,Selectable{
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 3906008363477387913L;

	/**
	 * 鼠标事件相对屏幕的坐标
	 */
	protected Point screenPoint = new Point(); 
	/**
	 * 构造函数
	 * @param type
	 */
	public Pedal(String type){
		super(type);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void paintComponent(Graphics gg){
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg;	
		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g.setRenderingHints(renderHints);
		if(selected) g.setColor(Color.blue);
		else g.setColor(Color.black);	
		if(symbolType.equalsIgnoreCase("start")){
			g.setFont(NoteCanvas.MCORE_FONT.deriveFont(30f));
			g.drawString("\ue1ac", 0, getHeight());
			g.setFont(NoteCanvas.FREESERIF.deriveFont(25f));
			g.drawString(".", 23, getHeight());
		}else if(symbolType.equalsIgnoreCase("stop")){
			g.setFont(NoteCanvas.MCORE_FONT.deriveFont(30f));
			g.drawString("\ue1a6", 0, getHeight());
		}
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

	@Override
	public void adjustSize() {
		// TODO Auto-generated method stub
		setSize(30, 30);
	}

}