package sjy.elwg.notation.musicBeans.symbolLines;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import sjy.elwg.notation.NoteCanvas;
import sjy.elwg.notation.musicBeans.AbstractNote;
import sjy.elwg.notation.musicBeans.Note;
import sjy.elwg.utility.Controller;

public class Vibrato extends SymbolLine{

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -362112727911053620L;
	
	/**
	 * 高度
	 */
	public static final int VIB_HEIGHT = 23;
	
	/**
	 * 构造函数
	 * @param note 起始音符
	 */
	public Vibrato(AbstractNote note){
		
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
	public Vibrato(AbstractNote snote , AbstractNote enote){
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
		return new Vibrato(null);
	}

	@Override
	public void reLocate() {
		// TODO Auto-generated method stub
		Controller.locateLine(this);
	}

	@Override
	public void reSize(int x) {
		// TODO Auto-generated method stub
		setSize(x, VIB_HEIGHT);
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
        
//        g.setFont(new Font("mscore", Font.PLAIN, 35));
        g.setFont(NoteCanvas.MCORE_FONT.deriveFont(35f));
   	    g.drawString("\ue171", Note.NORMAL_HEAD_WIDTH, getHeight());
   	    int x = 30;
		while (x < getWidth()) {
			g.drawString("\ue183", x, getHeight() / 2);
			x += 1.5 * Note.NORMAL_HEAD_WIDTH;
		}
	}

}
