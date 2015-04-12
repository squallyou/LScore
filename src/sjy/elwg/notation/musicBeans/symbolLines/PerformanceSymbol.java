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
 * 表情术语
 * @author wenxi.lu
 *
 */
public class PerformanceSymbol extends NoteSymbol implements MouseListener,MouseMotionListener,Selectable{
	

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -2959721511611039924L;

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
	public PerformanceSymbol(String type){
		super(type);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void adjustSize(){
		setSize(70, 35);
	}
	
	public void paintComponent(Graphics gg){
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg;	
		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        	renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        	g.setRenderingHints(renderHints);
//     //   Font font  = new Font("SansSerif",Font.ITALIC,14);
    		g.setFont(NoteCanvas.GARBRIOLA.deriveFont(Font.ITALIC,21));
////        
		if(selected) g.setColor(Color.blue);
		else g.setColor(Color.black);

		if(symbolType.equalsIgnoreCase("affetuoso")){
			g.drawString("affetuoso", 2, getHeight()/2+1);
		}else if(symbolType.equalsIgnoreCase("agitato")){
			g.drawString("agitato", 2, getHeight()/2+1);
		}else if(symbolType.equalsIgnoreCase("animato")){
			g.drawString("animato", 2, getHeight()/2+1);
		}else if(symbolType.equalsIgnoreCase("brillante")){
			g.drawString("brillante", 2, getHeight()/2+1);
			
		}else if(symbolType.equalsIgnoreCase("conBrio")){
			g.drawString("con brio", 2, getHeight()/2+1);
		}else if(symbolType.equalsIgnoreCase("cantabile")){
			g.drawString("cantabile", 2, getHeight()/2+1);
		}else if(symbolType.equalsIgnoreCase("deciso")){
			g.drawString("deciso", 2, getHeight()/2+1);
		}else if(symbolType.equalsIgnoreCase("dolce")){
			g.drawString("dolce", 2, getHeight()/2+1);
		}else if(symbolType.equalsIgnoreCase("dolento")){
			g.drawString("dolento", 2, getHeight()/2+1);
		}else if(symbolType.equalsIgnoreCase("espressivo")){
			g.drawString("espressivo", 2, getHeight()/2+1);
		}else if(symbolType.equalsIgnoreCase("energico")){
			g.drawString("energico", 2, getHeight()/2+1);
		}else if(symbolType.equalsIgnoreCase("furioso")){
			g.drawString("furioso", 2, getHeight()/2+1);
			
	
		}else if(symbolType.equalsIgnoreCase("giocoso")){
			g.drawString("giocoso", 2, getHeight()/2+1);
		}else if(symbolType.equalsIgnoreCase("grave")){
			g.drawString("grave", 2, getHeight()/2+1);
		}else if(symbolType.equalsIgnoreCase("grazioso")){
			g.drawString("grazioso", 2, getHeight()/2+1);
		}else if(symbolType.equalsIgnoreCase("maestoso")){
			g.drawString("maestoso", 2, getHeight()/2+1);
		}else if(symbolType.equalsIgnoreCase("misterioso")){
			g.drawString("misterioso", 2, getHeight()/2+1);
		}else if(symbolType.equalsIgnoreCase("passionato")){
			g.drawString("passionato", 2, getHeight()/2+1);
		}else if(symbolType.equalsIgnoreCase("sostenuto")){
			g.drawString("sostenuto", 2, getHeight()/2+1);
		}else if(symbolType.equalsIgnoreCase("conSpirito")){
			g.drawString("con spirito", 2, getHeight()/2+1);
		}else if(symbolType.equalsIgnoreCase("tranquillo")){
			g.drawString("tranquillo", 2, getHeight()/2+1);
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
