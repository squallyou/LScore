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
 * 装饰音，演奏记号 实体类
 * @author jingyuan.sun
 *
 */
public class Ornament extends NoteSymbol implements MouseListener,MouseMotionListener,Selectable{
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 3921934281438812004L;
	/**
	 * 是否被选择
	 */
	private boolean selected;
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * 鼠标事件相对屏幕的坐标
	 */
	protected Point screenPoint = new Point(); 
//	/**
//	 * 被用户所拖动过的x距离和y距离
//	 * 在进行符号放置时，其位置是默认位置与拖动位置之和
//	 */
//	protected int draggedX = 0;
//	protected int draggedY = 0;
	
	public Ornament(String type){
		super(type);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void adjustSize(){
		if(symbolType.equalsIgnoreCase("accent") || symbolType.equalsIgnoreCase("staccato") ||
				symbolType.equalsIgnoreCase("tenuto") || symbolType.equalsIgnoreCase("strongAccentUp") || symbolType.equalsIgnoreCase("staccatissimoUp")
				|| symbolType.equalsIgnoreCase("staccatissimoDown")|| symbolType.equalsIgnoreCase("strongAccentDown")){
			setSize(10, 10);
		}
		else if(symbolType.equalsIgnoreCase("staccatoTenutoDown")|| symbolType.equalsIgnoreCase("staccatoTenutoUp")
				|| symbolType.equalsIgnoreCase("fermata")){
			setSize(20, 20);
		}
	}
	
	public void paintComponent(Graphics gg){
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg;	
		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g.setRenderingHints(renderHints);
//		g.setFont(new Font("Mscore", Font.PLAIN, 35));
		g.setFont(NoteCanvas.MCORE_FONT.deriveFont(35f));
		if(selected) g.setColor(Color.BLUE);
		else g.setColor(Color.black);
		
		if(symbolType.equalsIgnoreCase("accent")){
			g.drawString("\uE161", 2, 5);
		}else if(symbolType.equalsIgnoreCase("staccato")){
			g.drawString("\uE163", 5, 5);
		}else if(symbolType.equalsIgnoreCase("tenuto")){
			g.drawString("\uE166", 5, 5);		
		}else if(symbolType.equalsIgnoreCase("strongAccentUp")){
			g.drawString("\uE169", 5, 10);
		}else if(symbolType.equalsIgnoreCase("staccatissimoUp")){
			g.drawString("\uE164", 5, 7);
		}else if(symbolType.equalsIgnoreCase("staccatissimoDown")){
			g.drawString("\uE165", getWidth()/2, 2);
		}else if(symbolType.equalsIgnoreCase("strongAccentDown")){
			g.drawString("\uE16A", getWidth()/2, 0);
		}else if(symbolType.equalsIgnoreCase("staccatoTenutoUp")){
			g.drawString("\uE166", getWidth()/2 - 5, getHeight()-10);
			g.drawString("\uE163", getWidth()/2 - 5, getHeight()-4);
		}else if(symbolType.equalsIgnoreCase("staccatoTenutoDown")){
			g.drawString("\uE163", getWidth()/2 - 5, getHeight()-8);
			g.drawString("\uE166", getWidth()/2 - 5, getHeight()-3);
		}else if(symbolType.equalsIgnoreCase("fermata")){
			g.drawString("\uE158", getWidth()/2, getHeight());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==this){
			if(!selected)
			selected = true;
			repaint();
		}			
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
		//改变颜色
		selected = true;
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		selected = true;
		repaint();
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
    	setLocation(curX, curY + deltay);
	}

	public int getDraggedY() {
		return draggedY;
	}

	public void setDraggedY(int draggedY) {
		this.draggedY = draggedY;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
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

}
