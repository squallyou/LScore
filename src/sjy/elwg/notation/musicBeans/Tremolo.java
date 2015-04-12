package sjy.elwg.notation.musicBeans;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import sjy.elwg.notation.musicBeans.symbolLines.NoteSymbol;

public class Tremolo  extends NoteSymbol implements Selectable{
	
	private String type;
	
	private ArrayList<AbstractNote> noteList;
	/**
	 * 加入颤音修正之后对应的外表时长.
	 */
	private int duration;
	/**
	 * 连音所占的实际时长
	 */
	private int realDuration;

	public Tremolo(AbstractNote note,int duration,String type){
		super(type);
		// TODO Auto-generated constructor stub
		noteList = new ArrayList<AbstractNote>();
		noteList.add(note);
		this.duration = duration;
		note.setTremolo(this);
		realDuration = note.getDuration() / 2;
		setOpaque(true);
		}

	public ArrayList<AbstractNote> getNoteList() {
		return noteList;
	}
	public void setNoteList(ArrayList<AbstractNote> noteList) {
		this.noteList = noteList;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public int getRealDuration() {
		return realDuration;
	}
	public void setRealDuration(int realDuration) {
		this.realDuration = realDuration;
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
		if(symbolType.equalsIgnoreCase("tremolo1")){
			int[] X = {0,12,12,0};
			int[] Y = {7,3,0,4};	
			g.fillPolygon(X, Y, 4);
		}else if(symbolType.equalsIgnoreCase("tremolo2")){
			int[] X1 = {0,12,12,0};
			int[] Y1 = {7,3,0,4};	
			g.fillPolygon(X1, Y1, 4);
			
//			int[] X2 = {0,12,12,0};
			int[] Y2 = {12,8,5,9};
			g.fillPolygon(X1, Y2, 4);
		}else if(symbolType.equalsIgnoreCase("tremolo3")){
			int[] X1 = {0,12,12,0};
			int[] Y1 = {7,3,0,4};	
			g.fillPolygon(X1, Y1, 4);
			
			int[] X2 = {0,12,12,0};
			int[] Y2 = {12,8,5,9};
			g.fillPolygon(X2, Y2, 4);
			
			int[] X3 = {0,12,12,0};
			int[] Y3 = {17,13,10,14};
			g.fillPolygon(X3, Y3, 4);
		}

	}
	@Override
	public void adjustSize() {
		// TODO Auto-generated method stub
		if(symbolType.equalsIgnoreCase("tremolo1"))
			setSize(12,7);
		if(symbolType.equalsIgnoreCase("tremolo2"))
			setSize(12,15);
		if(symbolType.equalsIgnoreCase("tremolo3"))
			setSize(12,18);
		
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

}
