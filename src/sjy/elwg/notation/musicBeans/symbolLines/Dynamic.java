package sjy.elwg.notation.musicBeans.symbolLines;

import java.awt.Color;
import java.awt.Font;
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
public class Dynamic extends NoteSymbol implements MouseListener,MouseMotionListener,Selectable{
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1782438855027858506L;
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
	/**
	 * 构造函数
	 * @param type
	 */
	public Dynamic(String type){
		super(type);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void adjustSize(){
		if(symbolType.equalsIgnoreCase("p") || symbolType.equalsIgnoreCase("f")){
			setSize(20, 22);
		}else if(symbolType.equalsIgnoreCase("pp") || symbolType.equalsIgnoreCase("ff") ||symbolType.equalsIgnoreCase("fp") ||
				symbolType.equalsIgnoreCase("fz") ||symbolType.equalsIgnoreCase("sf") ||
				symbolType.equalsIgnoreCase("mp") || symbolType.equalsIgnoreCase("mf")){
			setSize(30, 22);
		}else if(symbolType.equalsIgnoreCase("ppp") || symbolType.equalsIgnoreCase("fff")|| symbolType.equalsIgnoreCase("sfz")){
			setSize(40, 22);
		}else if(symbolType.equalsIgnoreCase("pppp") || symbolType.equalsIgnoreCase("ffff")|| 
				symbolType.equalsIgnoreCase("sffz")){
			setSize(60, 22);
		}else if(symbolType.equalsIgnoreCase("subitoP")){
			setSize(80, 22);
		}
	}
	
	public void paintComponent(Graphics gg){
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg;	
		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g.setRenderingHints(renderHints);
//		g.setFont(new Font("Mscore", Font.PLAIN, 18));
		g.setFont(NoteCanvas.MCORE_FONT.deriveFont(18f));
		if(selected) g.setColor(Color.blue);
		else g.setColor(Color.black);
		
		if(symbolType.equalsIgnoreCase("p")){
			g.drawString("\u0070",2, getHeight()/2);
		}else if(symbolType.equalsIgnoreCase("pp")){
			g.drawString("\u0070\u0070", 2,  getHeight()/2);
		}else if(symbolType.equalsIgnoreCase("ppp")){
			g.drawString("\u0070\u0070\u0070", 2,getHeight()/2);
		}else if(symbolType.equalsIgnoreCase("pppp")){
			g.drawString("\u0070\u0070\u0070\u0070", 2,getHeight()/2);
			
		}else if(symbolType.equalsIgnoreCase("mp")){
			g.drawString("\u006D\u0070", 2, getHeight()/2);
		}else if(symbolType.equalsIgnoreCase("f")){
			g.drawString("\u0066",2, getHeight()-5);
		}else if(symbolType.equalsIgnoreCase("ff")){
			g.drawString("\u0066\u0066", 2, getHeight()-5);
		}else if(symbolType.equalsIgnoreCase("fff")){
			g.drawString("\u0066\u0066\u0066", 2,getHeight()-5);
		}else if(symbolType.equalsIgnoreCase("ffff")){
			g.drawString("\u0066\u0066\u0066\u0066", 2,getHeight()-5);	
		}else if(symbolType.equalsIgnoreCase("fp")){
			g.drawString("\u0066\u007A", 2, getHeight()-5);
		}else if(symbolType.equalsIgnoreCase("fz")){
			g.drawString("\u006D\u0070", 2, getHeight()-5);
		}else if(symbolType.equalsIgnoreCase("mf")){
			g.drawString("\u006D\u0066", 2, getHeight()-5);
			
	
		}else if(symbolType.equalsIgnoreCase("sf")){
			g.drawString("\u0073\u0066", 2, getHeight()-5);
			System.out.println("yes");
		}else if(symbolType.equalsIgnoreCase("sfz")){
			g.drawString("\u0073\u0066\u007A", 2, getHeight()-5);
		}else if(symbolType.equalsIgnoreCase("sffz")){
			g.drawString("\u0073\u0066\u0066\u007A", 2, getHeight()-5);
		}else if(symbolType.equalsIgnoreCase("subitoP")){
			Font font  = new Font("SansSerif",Font.ITALIC,14);
			g.setFont(font);
			g.drawString("subito", 2, getHeight()/2+1);
			g.setFont(NoteCanvas.MCORE_FONT.deriveFont(18f));
			g.drawString("\u0070",44, getHeight()/2-1);
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
//		selected = true;
//		repaint();
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
    	setLocation(curX + deltax, curY + deltay);
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
}
