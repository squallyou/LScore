package sjy.elwg.notation.musicBeans.symbolLines;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;

import sjy.elwg.notation.musicBeans.AbstractNote;
import sjy.elwg.notation.musicBeans.Note;
import sjy.elwg.utility.Controller;

public class Cresc extends SymbolLine{


	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -6485712855623807280L;
	/**
	 * 高度
	 */
	public static final int CRESC_HEIGHT = 23;
	/**
	 * 默认宽度
	 */
	public static final int  CRESC_WIDTH= 40;
	
	
	/**
	 * 根据起始音符构造符号
	 * @param note
	 */
	public Cresc(AbstractNote note){
		super();
		startNote = note;
		if(note != null)
			note.getSymbolLines().add(this);
	}
	
	/**
	 * 根据起始音符与结束音符构造符号
	 * @param snote
	 * @param enote
	 */
	public Cresc(AbstractNote snote , AbstractNote enote){
		super();
		startNote = snote;
		endNote = enote;
		if(snote != null)
			snote.getSymbolLines().add(this);
		if(enote != null)
			enote.getSymbolLines().add(this);
		reShape();
	//	setSize(30,DIMC_HEIGHT);
	}

	@Override
	public SymbolLine getSymbolLineInstance() {
		// TODO Auto-generated method stub
		return new Dimc(null);
	}

	@Override
	public void reLocate() {
		// TODO Auto-generated method stub
		Controller.locateLine(this);
	}

	@Override
	public void reSize(int x) {
		if(x < CRESC_WIDTH){
			setSize(CRESC_WIDTH,CRESC_HEIGHT);
		}else{
				setSize(x, CRESC_HEIGHT);
		}
	
	}
	
	public void paintComponent(Graphics gg){
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg;	
		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g.setRenderingHints(renderHints);
        
        if(status == EDIT_STATUS)
        	g.setColor(Color.green);
        else if(status == SELECT_STATUS)
        	g.setColor(Color.blue);
        else 
        	g.setColor(Color.black);
        g.setFont(new Font("arial", Font.ITALIC, 15));
		g.drawString("crescendo", 0, Note.HEAD_HEIGHT);
//		int x = 35;
//		if(status == EDIT_STATUS){
//			if (x < getWidth()) {
//				while(x < getWidth()){
//					g.drawLine(x, Note.HEAD_HEIGHT, x+5,Note.HEAD_HEIGHT);
//					x += 10;
//				}
//				g.drawLine(x, Note.HEAD_HEIGHT, x+2, Note.HEAD_HEIGHT);
//				g.drawLine(x+4, Note.HEAD_HEIGHT, x+6, Note.HEAD_HEIGHT);
//			}
//			int[] ax = new int[4];
//     		int[] ay = new int[4];
//     		Polygon a;
//     		
//     		ax[0] = getWidth()-5;
//     		ax[1] = getWidth()-1;
//     		ax[2] = getWidth()-1;
//     		ax[3] = getWidth()-5;
//     		
//     		ay[0] = getHeight()/2+3;
//     		ay[1] = getHeight()/2+3;
//     		ay[2] = getHeight()/2+7;
//     		ay[3] = getHeight()/2+7;
//     		
//     		a = new Polygon(ax,ay,4);
//     		g.drawPolygon(a);
//		}else{
//			if (x < getWidth()) {
//				while(x < getWidth()){
//					g.drawLine(x, Note.HEAD_HEIGHT, x+5,Note.HEAD_HEIGHT);
//					x += 10;
//				}
//				g.drawLine(x, Note.HEAD_HEIGHT, x+2, Note.HEAD_HEIGHT);
//				g.drawLine(x+4, Note.HEAD_HEIGHT, x+6, Note.HEAD_HEIGHT);
//			}
//		}

	}

    public void mouseDragged(MouseEvent e){
   
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
}
