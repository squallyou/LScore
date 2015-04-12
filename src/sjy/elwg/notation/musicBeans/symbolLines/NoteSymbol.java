package sjy.elwg.notation.musicBeans.symbolLines;

import javax.swing.JPanel;

import sjy.elwg.notation.musicBeans.AbstractNote;
import sjy.elwg.notation.musicBeans.Selectable;
/**
 * 抽象类，音符符号,包括各种音符的装饰符号，力度符号等.
 * 实际上装饰音、力度符号等符号特性相似，但考虑到使用习惯，将它们分为不同的类.
 * 该类作为这些子类的父类.
 * 之所以采用抽象类，是为了避免从父类直接创建符号对象.
 * @author jingyuan.sun
 *
 */
public abstract class NoteSymbol extends JPanel implements Selectable{
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -2626826446968113902L;
	/**
	 * 是否被选中
	 */
	protected boolean selected;
	/**
	 * 音符符号类型.
	 */
	protected String symbolType;
	
	/**
	 * 与该符号相关联的音符
	 */
	protected AbstractNote note;
	
	/**
	 * 被用户所拖动过的x距离和y距离
	 * 在进行符号放置时，其位置是默认位置与拖动位置之和
	 */
	protected int draggedX = 0;
	protected int draggedY = 0;
	
	/**
	 * 默认构造函数
	 * @param type
	 */
	public NoteSymbol(String type){
		super();
		this.symbolType = type;
		setOpaque(false);
		adjustSize();
//		repaint();
	}
	
	/**
    /**
	 * 抽象方法，调整大小
	 */
	public abstract void adjustSize();


	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getSymbolType() {
		return symbolType;
	}

	public void setSymbolType(String symbolType) {
		this.symbolType = symbolType;
	}

	public AbstractNote getNote() {
		return note;
	}

	public void setNote(AbstractNote note) {
		this.note = note;
	}

	public int getDraggedY() {
		// TODO Auto-generated method stub
		return draggedY;
	}
	
	public int getDraggedX() {
		return draggedX;
	}
	
}
