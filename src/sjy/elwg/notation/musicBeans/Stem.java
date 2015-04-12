package sjy.elwg.notation.musicBeans;

import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * 符杆
 * @author sjy
 *
 */
public class Stem extends JPanel {
	
	/**
	 * 符杆的类型常量
	 */
	public static final int NORMAL = 10;
	public static final int GRACE = 11;
	/**
	 * 高度常量
	 */
	public static final int NORMAL_HEIGHT = 35;
	public static final int GRACE_HEIGHT = 22;
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 9L;
	
	/**
	 * 类型
	 */
	private int type;
	
	/**
	 * 符杆宽度
	 */
	private int stemWidth = 2;
	
	/**
	 * 空构造函数
	 */
	public Stem(){
		super();
		type = NORMAL;
		setSize(stemWidth, NORMAL_HEIGHT);
		setOpaque(false);
		setLayout(null);
		repaint();
	}
	
	/**
	 * 构造函数
	 * @param type 符杆类型
	 */
	public Stem(int type){
		super();
		this.type = type;
		adjustHeight();
		setOpaque(false);
		setLayout(null);
		repaint();
	}
	
	/**
	 * 调整高度
	 */
	public void adjustHeight(){
		if(type == NORMAL)
			setSize(2, NORMAL_HEIGHT);
		else if(type == GRACE)
			setSize(2, GRACE_HEIGHT);
	}
	
	public void paintComponent(Graphics g){
		g.drawLine(0, 0, 0, getHeight());
	}
	
	/**
	 * 返回当前符杆的默认长度
	 * @return
	 */
	public int getDefaultHeight(){
		if(type == GRACE)
			return GRACE_HEIGHT;
		else if(type == NORMAL)
			return NORMAL_HEIGHT;
		return -1;
	}

}
