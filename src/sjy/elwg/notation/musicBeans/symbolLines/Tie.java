package sjy.elwg.notation.musicBeans.symbolLines;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import sjy.elwg.notation.NoteCanvas;
import sjy.elwg.notation.musicBeans.MeasurePart;
import sjy.elwg.notation.musicBeans.Note;
import sjy.elwg.notation.musicBeans.NoteLine;
import sjy.elwg.notation.musicBeans.Page;
import sjy.elwg.notation.musicBeans.Selectable;
import sjy.elwg.notation.musicBeans.Stem;
import sjy.elwg.utility.Controller;
import sjy.elwg.utility.MusicMath;

/**
 * 连音线
 * @author sjy
 *
 */
public class Tie extends AbstractLine implements MouseListener,Selectable{
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 7834196443431064912L;
	/**
	 * 其实音符
	 */
	private Note startNote;
	/**
	 * 结束音符
	 */
	private Note endNote;
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
	 * 连音线的弧度方向，有效值"up"和"down".
	 * "up"表示弧形是圆形的上面部分，或者放置在音符的上面所呈现的弧度方向。 "down"则相反.
	 */
	protected String upOrDown = "up";
	
	
	/**
	 * 空构造函数
	 */
	public Tie(){
		super();
		addMouseListener(this);
			
	}
	
	/**
	 * 构造函数
	 * 通常用于XML导入时
	 * @param note
	 */
	public Tie(Note note){
		super();
		startNote = note;
		if(startNote != null)
			startNote.addTie(this);
		addMouseListener(this);
	}
	
	/**
	 * 构造函数
	 * 通常用于XML导入时
	 * @param note
	 * @param direction
	 */
	public Tie(Note note, String direction){
		super();
		startNote = note;
		upOrDown = direction;
		repaint();
		if(startNote != null)
			startNote.addTie(this);
		addMouseListener(this);
	}
	
	/**
	 * 构造函数
	 * 通常用于用户操作添加符号
	 * @param startNote
	 * @param endNote
	 */
	public Tie(Note startNote, Note endNote){
		super();
		this.startNote = startNote;
		this.endNote = endNote;
		startNote.addTie(this);
		endNote.addTie(this);
		reShape();
		addMouseListener(this);
	}

	@Override
	public void reLocate() {
		// TODO Auto-generated method stub
		Controller.locateLine(this);
	}

	@Override
	public void reSize(int x) {
		// TODO Auto-generated method stub
		setSize(x, Slur.SLUR_HEIGHT);
	}

	@Override
	public void reShape() {
		// TODO Auto-generated method stub
		/*
		 * 起始音符与结束音符均不为空
		 */
		if(startNote != null && endNote != null){
			//方向判断
			determineUpOrDown();
			
			NoteLine startLine = startNote.getMeasure().getMeasurePart().getNoteLine();
			NoteLine endLine = endNote.getMeasure().getMeasurePart().getNoteLine();
			
			//两音符在同一行
			if(startLine == endLine){
				reSize(endNote.getX() - startNote.getX());
				Page page = startLine.getPage();
				if(getParent() != page) 
					page.add(this);
				//如果之前有切分，将后面的部分删除
				Tie nxtSlur = (Tie)nextSymbolLine;
				nextSymbolLine = null;
				while(nxtSlur != null){
					nxtSlur.setPreSymbolLine(null);
					if(nxtSlur.getParent() != null)
						((JPanel)nxtSlur.getParent()).remove(nxtSlur);
					if(nxtSlur.getEndNote() != null){
						nxtSlur.getEndNote().removeTie(nxtSlur);
					}
					nxtSlur = (Tie)nxtSlur.getNextSymbolLine();
				}
				
				reLocate();
			}
			//两音符不在同一行，通常这是刚导入数据的情况，或者正在编辑符号.
			else{
				int lineDiff = MusicMath.NoteLineDiffs(startLine, endLine);
				ArrayList<AbstractLine> pieces = split(lineDiff + 1);
				NoteLine nxtLine = startLine;
				for(int i = 0, n = pieces.size(); i < n; i++){
					Tie sl = (Tie)pieces.get(i);
					sl.reShape();
					sl.reLocate();
					if(sl.getParent() != nxtLine.getPage())
						nxtLine.getPage().add(sl);
					nxtLine = MusicMath.nxtLine(nxtLine);
				}
			}
		}
		
		/*
		 * 起始音符不为空，结束音符为空，是切分后的第一部分
		 */
		else if(startNote != null && endNote == null){
			//方向判断
			determineUpOrDown();
			
			//与之后的部分在同一行,则进行合并
			Tie nxtTie = nextSymbolLine == null ? null : (Tie)nextSymbolLine;
			NoteLine line = startNote.getMeasure().getMeasurePart().getNoteLine();
			if(nxtTie != null &&  nxtTie.getEndNote() != null){
				NoteLine nxtLine = nxtTie.getEndNote().getMeasure().getMeasurePart().getNoteLine();
				if(nxtLine == line){
					nextSymbolLine = null;
					nxtTie.setPreSymbolLine(null);
					line.getPage().remove(nxtTie);
					endNote = nxtTie.getEndNote();
					endNote.removeTie(nxtTie);
					endNote.addTie(this);
					
					reSize(endNote.getX() - startNote.getX());
					reLocate();
					return;
				}
			}
			reSize(NoteCanvas.lineWidth + NoteCanvas.xStart - startNote.getX());
			reLocate();
		}
		
		/*
		 * 起始音符为空，结束音符不为空，是切分后的最后一部分 
		 */
		else if(startNote == null && endNote != null){
			NoteLine line = endNote.getMeasure().getMeasurePart().getNoteLine();
			MeasurePart firstPart = line.getMeaPartList().get(0);
			reSize(endNote.getX() + endNote.getWidth()/2 - NoteCanvas.xStart - firstPart.maxAttrWidth());
			reLocate();
		}
		
	}

	public ArrayList<AbstractLine> split(int num) {
		// TODO Auto-generated method stub
		ArrayList<AbstractLine> pieces = new ArrayList<AbstractLine>();
//		if(num != 2){
//			System.err.println("连音线只能被切分为两部分！不能为："+num);
//			return null;
//		}
		
		Note endNote = this.endNote;
		this.endNote = null;
		
		pieces.add(this);
		
		Tie sl = new Tie(null, this.upOrDown);
		sl.setEndNote(endNote);
		endNote.removeTie(this);
		endNote.addTie(sl);
		pieces.add(sl);
		nextSymbolLine = sl;
		sl.setPreSymbolLine(this);
		
		return pieces;
	}
	
	public void paintComponent(Graphics gg){
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg;	
		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g.setRenderingHints(renderHints);
        
        if(selected)
        	g.setColor(Color.blue);
        else 
        	g.setColor(Color.black);
        
        if(upOrDown.equalsIgnoreCase("up"))
    		g.drawArc(0, 2, getWidth(), (int)(getHeight()*1.5), 15, 150);
    	else if(upOrDown.equalsIgnoreCase("down"))
    		g.drawArc(0, -10, getWidth(), (int)(getHeight()*1.5-1), -15, -150);
	}
	
	public void setSize(int width, int height){
		if(width < 30){
			super.setSize(width, 11);
			repaint();
			return;
		}
		else if(width >= 30){
			super.setSize(width, 20);
			repaint();
			return;
		}
		super.setSize(width, (int)(width * 0.2));
		repaint();
	}

	/**
	 * 判断连音线弧度方向
	 */
	public void determineUpOrDown(){
		//方向判断
		Stem stem = startNote.getChordNote() == null ? 
				startNote.getStem() : startNote.getChordNote().getStem();
		if(stem != null && 
				stem.getY() < startNote.getHighestNote().getY()){
			upOrDown = "down";
			repaint();
		}else{
			upOrDown = "up";
			repaint();
		}
	}

	public Note getStartNote() {
		return startNote;
	}

	public void setStartNote(Note startNote) {
		this.startNote = startNote;
	}

	public Note getEndNote() {
		return endNote;
	}

	public void setEndNote(Note endNote) {
		this.endNote = endNote;
	}

	public String getUpOrDown() {
		return upOrDown;
	}

	public void setUpOrDown(String upOrDown) {
		this.upOrDown = upOrDown;
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
//	JComponent p = (JComponent)getStartNote().getParent().getParent();
//		
//		((NoteCanvas) p).cancleAllSelected();
//		selected = true;
//		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
//		selected = false;
//		repaint();
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




}
