package sjy.elwg.notation.musicBeans;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import sjy.elwg.notation.musicBeans.symbolLines.NoteSymbol;

public class TremoloBeam  extends NoteSymbol implements Selectable, Gracable{
	
	/**
	 * 大小常量
	 */
	public static int WIDTH = 10;
	public static int HEIGHT1 = 7;
	public static int HEIGHT2 = 15;
	public static int HEIGHT3 = 18;
	
	/**
	 * 大小类型，见Gracable
	 */
	private int type = NORMAL;
	/**
	 * 与之相关联的音符
	 */
	private AbstractNote note;
	

	
	/**
	 * 构造函数
	 * @param symbolType
	 */
	public TremoloBeam(String symbolType){
		super(symbolType);
		adjustSize();
		repaint();
	}
	
	/**
	 * 构造函数
	 * @param symbolType
	 * @param type
	 */
	public TremoloBeam(String symbolType, int type) {
		super(symbolType);
		this.type = type;
		adjustSize();
		repaint();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void paintComponent(Graphics gg){
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg;	
		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g.setRenderingHints(renderHints);
		if(selected) g.setColor(Color.blue);
		else g.setColor(Color.black);
		if(symbolType.equalsIgnoreCase("tremoloBeam1")){
			int[] X = {0,getWidth(),getWidth(),0};
			int[] Y = {getHeight(),getHeight()/2,0,getHeight()/2+1};	
			g.fillPolygon(X, Y, 4);
		}else if(symbolType.equalsIgnoreCase("tremoloBeam2")){
			int[] X1 = {0,10,10,0};
			int[] Y1 = {7,3,0,4};	
			g.fillPolygon(X1, Y1, 4);
			int[] Y2 = {12,8,5,9};
			g.fillPolygon(X1, Y2, 4);
		}else if(symbolType.equalsIgnoreCase("tremoloBeam3")){
			int[] X1 = {0,10,10,0};
			int[] Y1 = {7,3,0,4};	
			g.fillPolygon(X1, Y1, 4);
			
			int[] X2 = {0,10,10,0};
			int[] Y2 = {12,8,5,9};
			g.fillPolygon(X2, Y2, 4);
			
			int[] X3 = {0,10,10,0};
			int[] Y3 = {17,13,10,14};
			g.fillPolygon(X3, Y3, 4);
		}

	}
	@Override
	public void adjustSize() {
		// TODO Auto-generated method stub
		if(type == NORMAL){
			if(symbolType.equalsIgnoreCase("tremoloBeam1"))
				setSize(WIDTH,HEIGHT1);
			if(symbolType.equalsIgnoreCase("tremoloBeam2"))
				setSize(WIDTH,HEIGHT2);
			if(symbolType.equalsIgnoreCase("tremoloBeam3"))
				setSize(WIDTH,HEIGHT3);
		}
		else if(type == GRACE){
			if(symbolType.equalsIgnoreCase("tremoloBeam1"))
				setSize(TREMOLO_WIDTH,TREMOLO1_HEIGHT);
			if(symbolType.equalsIgnoreCase("tremoloBeam2"))
				setSize(TREMOLO_WIDTH,TREMOLO1_HEIGHT);
			if(symbolType.equalsIgnoreCase("tremoloBeam3"))
				setSize(TREMOLO_WIDTH,TREMOLO1_HEIGHT);
		}
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
