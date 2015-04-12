package sjy.elwg.notation.musicBeans.symbolLines;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JComponent;

import sjy.elwg.notation.NoteCanvas;
import sjy.elwg.notation.musicBeans.AbstractNote;
import sjy.elwg.notation.musicBeans.MeasurePart;
import sjy.elwg.notation.musicBeans.NoteLine;
import sjy.elwg.notation.musicBeans.Page;
import sjy.elwg.notation.musicBeans.Selectable;
import sjy.elwg.utility.MusicMath;

/**
 * 以音符为基本单位的线条符号的父类，即该类中的线条符号是以一个音符作为起点、一个音符作为终点.
 * 由于所关联的音符的属性差别，该类不包括连音线. 与连音线一样继承自AbstractSymbolLine.
 * @author jingyuan.sun
 *
 */
public abstract class SymbolLine extends AbstractLine implements Selectable, MouseListener, MouseMotionListener{
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -6855117194689288517L;
	/**
	 * 起始音符
	 */
	protected AbstractNote startNote;
	/**
	 * 结束音符
	 */
	protected AbstractNote endNote;
	/**
	 * 鼠标事件相对屏幕的坐标
	 */
	protected Point screenPoint = new Point();
	/**
	 * 被用户所拖动过的x距离和y距离
	 * 在进行符号放置时，其位置是默认位置与拖动位置之和
	 */
	protected int draggedX = 0;
	protected int draggedY = 0;
	
	/**
	 * 构造函数
	 */
	public SymbolLine(){
		super();
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	/**
	 * 根据起始音符与结束音符，调整大小与位置
	 */
	public void reShape(){
		repaint();
		
		/*
		 * 起始音符与结束音符均不为空
		 */
		if(startNote != null && endNote != null){
			
			NoteLine startLine = startNote.getMeasure().getMeasurePart().getNoteLine();
			NoteLine endLine = endNote.getMeasure().getMeasurePart().getNoteLine();
			
			//两音符在同一行
			if(startLine == endLine){
				reSize(endNote.getX() - startNote.getX());
				Page page = startLine.getPage();
				if(getParent() != page) 
					page.add(this);
				//如果之前有切分，将后面的部分删除
				SymbolLine nxtSlur = (SymbolLine)nextSymbolLine;
				nextSymbolLine = null;
				while(nxtSlur != null){
					nxtSlur.setPreSymbolLine(null);
					if(nxtSlur.getParent() != null)
						((JComponent)nxtSlur.getParent()).remove(nxtSlur);
					if(nxtSlur.getEndNote() != null){
						nxtSlur.getEndNote().getSymbolLines().remove(nxtSlur);
					} 
					
					nxtSlur = (SymbolLine)nxtSlur.getNextSymbolLine();
				}
				reLocate();
			}
			//两音符不在同一行，通常这是刚导入数据的情况，或者正在编辑符号.
			else{
				int lineDiff = MusicMath.NoteLineDiffs(startLine, endLine);
				ArrayList<AbstractLine> pieces = split(lineDiff + 1);
				NoteLine nxtLine = startLine;
				for(int i = 0, n = pieces.size(); i < n; i++){
					SymbolLine sl = (SymbolLine)pieces.get(i);
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
			
			//与之后的部分在同一行,则进行合并
			SymbolLine nxtTie = nextSymbolLine == null ? null : (SymbolLine)nextSymbolLine;
			NoteLine line = startNote.getMeasure().getMeasurePart().getNoteLine();
			if(nxtTie != null &&  nxtTie.getEndNote() != null){
				NoteLine nxtLine = nxtTie.getEndNote().getMeasure().getMeasurePart().getNoteLine();
				if(nxtLine == line){
					nextSymbolLine = null;
					nxtTie.setPreSymbolLine(null);
					line.getPage().remove(nxtTie);
					endNote = nxtTie.getEndNote();
					endNote.getSymbolLines().remove(nxtTie);
					endNote.getSymbolLines().add(this);
				}
			}
			
			reSize(NoteCanvas.lineWidth + NoteCanvas.xStart - startNote.getX());
			reLocate();
		}
		
		/*
		 * 起始音符为空，结束音符不为空，是切分后的最后一部分 
		 */
		else if(startNote == null && endNote != null){
			NoteLine startLine = null;
			if(preSymbolLine != null){
				NoteLine preLine = (NoteLine)MusicMath.getNoteLineBySymbolLine((SymbolLine)preSymbolLine).get(0);
				startLine = MusicMath.nxtLine(preLine);
			}
			NoteLine line = endNote.getMeasure().getMeasurePart().getNoteLine();
			//正常情况
			if(startLine == line){
				MeasurePart firstPart = line.getMeaPartList().get(0);
				reSize(endNote.getX() + endNote.getWidth()/2 - NoteCanvas.xStart - firstPart.maxAttrWidth());
				//如果之前有切分，将后面的部分删除
				SymbolLine nxtSlur = (SymbolLine)nextSymbolLine;
				nextSymbolLine = null;
				while(nxtSlur != null){
					nxtSlur.setPreSymbolLine(null);
					if(nxtSlur.getParent() != null)
						nxtSlur.getParent().remove(nxtSlur);
					if(nxtSlur.getEndNote() != null){
						nxtSlur.getEndNote().getSymbolLines().remove(nxtSlur);
					}
					nxtSlur = (SymbolLine)nxtSlur.getNextSymbolLine();
				}
				reLocate();
			}
			//需要切分
			else{
				ArrayList<AbstractLine> pieces = split(2);
				NoteLine nxtLine = startLine;
				for(int i = 0, n = pieces.size(); i < n; i++){
					SymbolLine sl = (SymbolLine)pieces.get(i);
					sl.reShape();
					sl.reLocate();
					if(sl.getParent() != nxtLine.getPage())
						nxtLine.getPage().add(sl);
					nxtLine = MusicMath.nxtLine(nxtLine);
				}
			}
		}
		
		/*
		 * 起始音符与结束音符均为空，是切分后的中间部分
		 */
		else{
			NoteLine curLine = (NoteLine)MusicMath.getNoteLineBySymbolLine(this).get(0);
			int attrWidth = curLine.getMeaPartList().get(0).maxAttrWidth(); 
			reSize(NoteCanvas.lineWidth - attrWidth);
			reLocate();
		}
	}
	
	/**
	 * 返回线条符号的一个新实例.
	 * 具体返回的对象类型由子类决定.
	 * @return
	 */
	public abstract SymbolLine getSymbolLineInstance();
	
	/**
	 * 设置线条的结束音符
	 * @param note
	 */
	public void shiftEndNote(AbstractNote note){
		//删除原来结束音符对该符号的引用
		if(this.endNote != null && this.endNote.getSymbolLines().size() > 0){
			this.endNote.getSymbolLines().remove(this);
		}
		//更新结束音符
		this.endNote = note;
		//添加新结束音符的引用
		endNote.getSymbolLines().add(this);
		reShape();
	}
	
	/**
	 * 对线条符号进行切分.
	 */
	public ArrayList<AbstractLine> split(int num){
		if(num < 2)
			return null;
		
		AbstractNote endNote = this.endNote;
		this.endNote = null;
		
		ArrayList<AbstractLine> pieces = new ArrayList<AbstractLine>();
		pieces.add(this);
		
		for(int i = 1; i < num; i++){
			SymbolLine sbl = getSymbolLineInstance();
			if(i < num - 1)
				sbl.setEndNote(null);
			else {
				sbl.setEndNote(endNote);
				if(endNote != null){
					endNote.getSymbolLines().remove(this);
					endNote.getSymbolLines().add(sbl);
				}
			}
			pieces.add(sbl);
			sbl.setPreSymbolLine(pieces.get(i - 1));
			pieces.get(i - 1).setNextSymbolLine(sbl);
			sbl.repaint();//设置完音符之后，重绘一次
		}
		
		return pieces;
	}
	

	public AbstractNote getStartNote() {
		return startNote;
	}

	public void setStartNote(AbstractNote startNote) {
		this.startNote = startNote;
	}

	public AbstractNote getEndNote() {
		return endNote;
	}

	public void setEndNote(AbstractNote endNote) {
		this.endNote = endNote;
	}
	
	public int getDraggedX() {
		return draggedX;
	}

	public void setDraggedX(int draggedX) {
		this.draggedX = draggedX;
	}

	public int getDraggedY() {
		return draggedY;
	}

	public void setDraggedY(int draggedY) {
		this.draggedY = draggedY;
	}

	public void beSelected(){
		if(status == NORMAL_STATUS){
			status = SELECT_STATUS;
			repaint();
		}
	}
	
	public void cancleSelected(){
		if(status != NORMAL_STATUS){
			status = NORMAL_STATUS;
			repaint();
		}
	}
	
	public void mouseClicked(MouseEvent e){
		
	}
	
    public void mousePressed(MouseEvent e){
		screenPoint.setLocation((int)e.getXOnScreen(), (int)e.getYOnScreen());
	}
    
    public void mouseEntered(MouseEvent e){
		
	}
    
    public void mouseExited(MouseEvent e){
		
	}
    
    public void mouseReleased(MouseEvent e){
    	
    }
    
    public void mouseMoved(MouseEvent e){
    	
    }
    
    public void mouseDragged(MouseEvent e){
    	if(status == SELECT_STATUS){
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
	else if(status == EDIT_STATUS){
    		int x = e.getXOnScreen();
        	int y = e.getYOnScreen();
        	int deltax = x - (int)screenPoint.getX();
        	screenPoint.setLocation(x, y);	

		setSize(getWidth() + deltax, getHeight());
		repaint();
    	}

    }

}
