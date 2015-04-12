package sjy.elwg.notation.musicBeans.symbolLines;

import javax.swing.JPanel;

/**
 * 包括连音线在内的线条符号的父类.
 * 继承自该类的子类具有的特点：可被切分，即符号可能由于换行等情况而被一分为二.
 * @author sjy
 *
 */
public abstract class AbstractLine extends JPanel implements Splitable{
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -7290983329762122391L;
	/**
	 * 分别为编辑，选择，普通的状态值.
	 */
	public static final int EDIT_STATUS = 0;
	public static final int SELECT_STATUS = 1;
	public static final int NORMAL_STATUS = 2;
	/**
	 * 所有线条符号的名字
	 */
	public static final int TIE = 0;
	public static final int SLUR = 1;
	public static final int VIB = 2;
	public static final int OCTAVE_UP = 3;
	public static final int OCTAVE_DOWN = 4;
	public static final int CRE = 5;
	public static final int DIM = 6;
	public static final int PEDAL = 7;
	public static final int DIMC = 8;
	public static final int CRESC = 9;
	public static final int REPEATENDING = 10;
	
	
	/**
	 * 当前状态值
	 */
	protected int status = NORMAL_STATUS;
	
	/**
	 * 切分后符号的前一部分，没有切分或者为切分后的第一部分 则为null.
	 */
	protected AbstractLine preSymbolLine;
	
	/**
	 * 切分后符号的下一部分，没有切分或者为切分后的最后一部分 则为null.
	 */
	protected AbstractLine nextSymbolLine;
	
	/**
	 * 构造函数
	 */
	public AbstractLine(){
		super();
		setOpaque(false);
		setLayout(null);
	}
	
	/**
	 * 调整形状与大小
	 */
	public abstract void reShape();
	
	/**
	 * 抽象方法，根据宽度调整符号大小.
	 * 宽度参数是符号所依赖的两音符(或者空)之间的标准宽度，如果符号被用户手动改变大小，
	 * 则子类重写该方法时需要将大小该变量考虑进去.
	 * @param x
	 */
	public abstract void reSize(int x);
	
	/**
	 * 抽象方法,放置线条符号
	 * @param line 行
	 * @param measureIndex 谱表ID
	 */
	public abstract void reLocate();

	/**
	 * 获得前一个符号
	 * @return
	 */
	public AbstractLine getPreSymbolLine() {
		return preSymbolLine;
	}

	/**
	 * 设置前一个符号
	 * @param preSymbolLine
	 */
	public void setPreSymbolLine(AbstractLine preSymbolLine) {
		this.preSymbolLine = preSymbolLine;
	}

	/**
	 * 获得后一个符号
	 * @return
	 */
	public AbstractLine getNextSymbolLine() {
		return nextSymbolLine;
	}

	/**
	 * 设置后一个符号
	 * @param nextSymbolLine
	 */
	public void setNextSymbolLine(AbstractLine nextSymbolLine) {
		this.nextSymbolLine = nextSymbolLine;
	}

	/**
	 * 获得当前状态
	 * @return
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * 设置当前状态
	 * @param status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

}
