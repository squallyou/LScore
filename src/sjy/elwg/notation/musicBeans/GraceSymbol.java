package sjy.elwg.notation.musicBeans;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import sjy.elwg.notation.NoteCanvas;
import sjy.elwg.notation.musicBeans.symbolLines.NoteSymbol;

public class GraceSymbol extends NoteSymbol implements MouseListener,MouseMotionListener,Selectable{
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -4601589678915452508L;
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

	
	public GraceSymbol(String type){
		super(type);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void adjustSize(){
		if(symbolType.equalsIgnoreCase("inverted-mordent") || symbolType.equalsIgnoreCase("mordent") ||
				symbolType.equalsIgnoreCase("turn") || symbolType.equalsIgnoreCase("inverted-turn") ){
			setSize(20, 10);
		}
		else if(symbolType.equalsIgnoreCase("trill-natural")|| symbolType.equalsIgnoreCase("trill-sharp")
				|| symbolType.equalsIgnoreCase("trill-flat")|| symbolType.equalsIgnoreCase("trill-mark")){
			setSize(20, 30);
		}
	}
	
	public void paintComponent(Graphics gg){
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg;	
		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g.setRenderingHints(renderHints);
//		g.setFont(new Font("Mscore", Font.PLAIN, 35));
		g.setFont(NoteCanvas.MCORE_FONT.deriveFont(30f));
		if(selected) g.setColor(Color.BLUE);
		else g.setColor(Color.black);
		
		if(symbolType.equalsIgnoreCase("inverted-mordent")){
			System.out.println("inverted-mordent");
			g.drawString("\uE183", 9, getHeight()/2);
		}else if(symbolType.equalsIgnoreCase("mordent")){
			System.out.println("mordent");
			g.drawString("\uE184", 9, getHeight()/2); 	
		}else if(symbolType.equalsIgnoreCase("turn")){
			System.out.println("turn");
			g.drawString("\uE170", 9, getHeight()/2);		
		}else if(symbolType.equalsIgnoreCase("inverted-turn")){
			System.out.println("inverted-turn");
			g.drawString("\uE16F",9, getHeight()/2);
		}else if(symbolType.equalsIgnoreCase("trill-mark")){

			g.drawString("\uE171", 9, 13);
		}else if(symbolType.equalsIgnoreCase("trill-natural")){
			g.setFont(NoteCanvas.MCORE_FONT.deriveFont(20f));
			g.drawString("\uE113", 9, 6);
			g.setFont(NoteCanvas.MCORE_FONT.deriveFont(30f));
			g.drawString("\uE171", 9, 26);
		}else if(symbolType.equalsIgnoreCase("trill-sharp")){
			g.setFont(NoteCanvas.MCORE_FONT.deriveFont(20f));
			g.drawString("\uE10E", 7, 5);
			g.setFont(NoteCanvas.MCORE_FONT.deriveFont(30f));
			g.drawString("\uE171", 9, 25);
		}else if(symbolType.equalsIgnoreCase("trill-flat")){
			g.setFont(NoteCanvas.MCORE_FONT.deriveFont(20f));
			g.drawString("\uE114", 8, 6);
			g.setFont(NoteCanvas.MCORE_FONT.deriveFont(30f));
			g.drawString("\uE171", 9, 25);
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
	
	/**
	 * 返回音符
	 */
	public AbstractNote getNote() {
		return note;
	}
	
	/**
	 * 设置音符
	 */
	public void setNote(AbstractNote note) {
		this.note = note;
	}

}
