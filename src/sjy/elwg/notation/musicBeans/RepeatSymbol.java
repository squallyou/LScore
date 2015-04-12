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

/**
 * 用文字或者符号表示的反复，包括segno,coda,Fine,D.C.,D.C. al Fine
 * D.s. al Fine,D.C. al Coda,D.S. al Coda,D.S.,To Coda
 * 
 */
public class RepeatSymbol extends NoteSymbol implements MouseListener,MouseMotionListener,Selectable{

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 与该符号相关联的小节组
	 */
	protected MeasurePart measurePart;
	/**
	 * 鼠标事件相对屏幕的坐标
	 */
	protected Point screenPoint = new Point(); 
	
	//重复记号的类型
	private String type;
	
	public RepeatSymbol(String type) {
		super(type);
		// TODO Auto-generated constructor stub
		this.type = type;
		setOpaque(false);
		setLayout(null);
		addMouseListener(this);
		addMouseMotionListener(this);
		repaint();
	}
	
	

	public void paintComponent(Graphics gg){
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg;	
		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g.setRenderingHints(renderHints);
		if(selected) g.setColor(Color.blue);
		else g.setColor(Color.black);
		if(symbolType.equalsIgnoreCase("coda")){
			g.setFont(NoteCanvas.MUSICAL_FONT.deriveFont(30f));
			g.drawString("\uf0DE", 0, 7);
		}else if(symbolType.equalsIgnoreCase("segno")){
			g.setFont(NoteCanvas.MUSICAL_FONT.deriveFont(25f));
			g.drawString("\uf025", 0, 8);
		}else if(symbolType.equalsIgnoreCase("dc")){
			g.setFont(NoteCanvas.FREESERIF.deriveFont(15f));
			g.drawString("D.C.", 0, 10);
		}else if(symbolType.equalsIgnoreCase("ds")){
			g.setFont(NoteCanvas.FREESERIF.deriveFont(15f));
			g.drawString("D.S.", 0, 10);	
		}else if(symbolType.equalsIgnoreCase("fine")){
			g.setFont(NoteCanvas.FREESERIF.deriveFont(15f));
			g.drawString("Fine",0, 10);
		}else if(symbolType.equalsIgnoreCase("toCoda")){
			g.setFont(NoteCanvas.FREESERIF.deriveFont(15f));
			g.drawString("To Coda", 0, 10);
		}else if(symbolType.equalsIgnoreCase("codaLetter")){
			g.setFont(NoteCanvas.FREESERIF.deriveFont(15f));
			g.drawString("Coda", 0, 10);
		}else if(symbolType.equalsIgnoreCase("dcCoda")){
			g.setFont(NoteCanvas.FREESERIF.deriveFont(15f));
			g.drawString("D.C.al Coda", 0, 10);
		}else if(symbolType.equalsIgnoreCase("dcFine")){
			g.setFont(NoteCanvas.FREESERIF.deriveFont(15f));
			g.drawString("D.C.al Fine",0, 10);
		}else if(symbolType.equalsIgnoreCase("dsCoda")){
			g.setFont(NoteCanvas.FREESERIF.deriveFont(15f));
			g.drawString("D.S.al Coda", 0, 10);
		}else if(symbolType.equalsIgnoreCase("dsFine")){
			g.setFont(NoteCanvas.FREESERIF.deriveFont(15f));
			g.drawString("D.S.al Fine",0, 10);
		}
	}
	@Override
	public void adjustSize() {
		// TODO Auto-generated method stub
		if(symbolType.equalsIgnoreCase("coda"))
			setSize(15,15);
		if(symbolType.equalsIgnoreCase("segno"))
			setSize(15,15);
		if(symbolType.equalsIgnoreCase("dc"))
			setSize(30,15);
		if(symbolType.equalsIgnoreCase("ds"))
			setSize(30,15);
		if(symbolType.equalsIgnoreCase("fine"))
			setSize(40,15);
		if(symbolType.equalsIgnoreCase("toCoda"))
			setSize(55,15);
		if(symbolType.equalsIgnoreCase("dcCoda"))
			setSize(80,15);
		if(symbolType.equalsIgnoreCase("dcFine"))
			setSize(70,15);
		if(symbolType.equalsIgnoreCase("dsCoda"))
			setSize(80,15);
		if(symbolType.equalsIgnoreCase("dsFine"))
			setSize(70,15);
		if(symbolType.equalsIgnoreCase("codaLetter"))
			setSize(35,15);
	 
	}
	
	/**
	 * 根据类型返回其乐理内容
	 * @param str
	 * @return
	 */
	public static String getContentbyType(String symbolType){
		String result = null;
		if(symbolType.equalsIgnoreCase("coda"))
			result = "coda";
		if(symbolType.equalsIgnoreCase("segno"))
			result = "segno";
		if(symbolType.equalsIgnoreCase("dc"))
			result = "D.C.";
		if(symbolType.equalsIgnoreCase("ds"))
			result = "D.S.";
		if(symbolType.equalsIgnoreCase("fine"))
			result = "Fine";
		if(symbolType.equalsIgnoreCase("toCoda"))
			result = "To Coda";
		if(symbolType.equalsIgnoreCase("dcCoda"))
			result = "D.C. al Coda";
		if(symbolType.equalsIgnoreCase("dcFine"))
			result = "D.C. al Fine";
		if(symbolType.equalsIgnoreCase("dsCoda"))
			result = "D.S. al Coda";
		if(symbolType.equalsIgnoreCase("dsFine"))
			result = "D.S. al Fine";
		return result;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public void beSelected() {
		// TODO Auto-generated method stub
		if(!selected){
			selected = true;
			repaint();
		}
	}

	@Override
	public void cancleSelected() {
		// TODO Auto-generated method stub
		if(selected){
			selected = false;
			repaint();
		}
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
		screenPoint.setLocation((int)e.getXOnScreen(), (int)e.getYOnScreen());
	}



	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	public void setMeasure(MeasurePart measurePart) {
		// TODO Auto-generated method stub
		this.measurePart = measurePart;
	}



	public MeasurePart getMeasurePart() {
		return measurePart;
	}



	public void setMeasurePart(MeasurePart measurePart) {
		this.measurePart = measurePart;
	}

}
