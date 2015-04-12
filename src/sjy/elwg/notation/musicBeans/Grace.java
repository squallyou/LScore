package sjy.elwg.notation.musicBeans;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import sjy.elwg.notation.NoteCanvas;

/**
 * 倚音类，继承自Note
 * @author sjy
 *
 */
public class Grace extends Note implements Gracable{
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -5065254488949589867L;
	
	/**
	 * 是否含有斜线，也即是否是短倚音
	 */
	private boolean hasSlash;
	/**
	 * 该倚音所属的音符
	 */
	private AbstractNote note;
	
	
	
	/**
	 * 空构造函数
	 */
	public Grace(){
		super();
		adjSize();
		repaint();
	}
	
	/**
	 * 空构造函数
	 * @param hasSlash
	 */
	public Grace(boolean hasSlash){
		super();
		this.hasSlash = hasSlash;
		adjSize();
		repaint();
	}
	
	/**
	 * 带附点倚音构造函数
	 * @param duration
	 * @param pitch
	 * @param dotNum
	 * @param hasSlash
	 */
	public Grace(int duration, int pitch, int dotNum, boolean hasSlash){
		super(duration, pitch, dotNum);
		this.hasSlash = hasSlash;
		adjSize();
		repaint();
	}
	
	/**
	 * 不带附点倚音构造函数
	 * @param duration
	 * @param pitch
	 * @param hasSlash
	 */
	public Grace(int duration, int pitch, boolean hasSlash){
		super(duration, pitch);
		this.hasSlash = hasSlash;
		adjSize();
		repaint();
	}
	
	public void adjSize(){
		setSize(GRACE_WIDTH, GRACE_HEIGHT);
	}
	
	public void paintComponent(Graphics gg){
//		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg;	
		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
	    renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
	    g.setRenderingHints(renderHints);
		g.setFont(NoteCanvas.MCORE_FONT.deriveFont(25f));
		if(selected) 
			g.setColor(Color.blue);
		else 
			g.setColor(Color.black);
		
		g.drawString("\uE12D", 0, 3);
	}
	
	/**
	 * 放置音符
	 */
	public void locateNote(int x){
		Measure measure = note.getMeasure();
		setLocation(x, measure.getY() + 4*NoteCanvas.LINE_GAP - 
				NoteCanvas.LINE_GAP*pitch/2 - getHeight()/2);
		refreshPosLines();
	}

	/**
	 * 是否是短倚音
	 * @return
	 */
	public boolean isHasSlash() {
		return hasSlash;
	}

	/**
	 * 设置短倚音
	 * @param hasSlash
	 */
	public void setHasSlash(boolean hasSlash) {
		this.hasSlash = hasSlash;
	}

	/**
	 * 获得所属的音符
	 * @return
	 */
	public AbstractNote getNote() {
		return note;
	}

	/**
	 * 设置音符
	 * @param note
	 */
	public void setNote(AbstractNote note) {
		this.note = note;
	}
	
	/**
	 * 被选中
	 */
	public void beSelected(){
		super.beSelected();
		requestFocusInWindow();
	}

}
