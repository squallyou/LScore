package sjy.elwg.notation.musicBeans.symbolLines;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;

import sjy.elwg.notation.musicBeans.AbstractNote;
import sjy.elwg.utility.Controller;

public class Cre extends SymbolLine{

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -534787190668141074L;
	/**
	 * 高度
	 */
	public static final int CRE_HEIGHT = 12;
	
	
	/**
	 * 构造函数
	 * @param note 起始音符
	 */
	public Cre(AbstractNote note){
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
	public Cre(AbstractNote snote , AbstractNote enote){
		super();
		startNote = snote;
		endNote = enote;
		if(snote != null)
			snote.getSymbolLines().add(this);
		if(enote != null)
			enote.getSymbolLines().add(this);
		reShape();
	}

	@Override
	public SymbolLine getSymbolLineInstance() {
		// TODO Auto-generated method stub
		return new Cre(null);
	}

	@Override
	public void reLocate() {
		// TODO Auto-generated method stub
		Controller.locateLine(this);
	}

	@Override
	public void reSize(int x) {
		// TODO Auto-generated method stub
		setSize(x, CRE_HEIGHT);
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
        if(status == EDIT_STATUS){
        	
            g.drawLine(0, getHeight()/2, getWidth(), 0);
        	g.drawLine(0, getHeight()/2, getWidth(), getHeight());
        	
        	int[] ax = new int[4];
			int[] ay = new int[4];
			Polygon a;
			
			ax[0] = getWidth()-5;
			ax[1] = getWidth()-1;
			ax[2] = getWidth()-1;
			ax[3] = getWidth()-5;
			
			ay[0] = getHeight()/2-2;
			ay[1] = getHeight()/2-2;
			ay[2] = getHeight()/2+2;
			ay[3] = getHeight()/2+2;
			
			a = new Polygon(ax,ay,4);
			g.drawPolygon(a);
        //	g.drawOval(getWidth()-5, getHeight()/2-2, 4, 4);
        	
        }else{
            g.drawLine(0, getHeight()/2, getWidth(), 0);
        	g.drawLine(0, getHeight()/2, getWidth(), getHeight()-1);
        }

	}

}
