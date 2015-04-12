package sjy.elwg.notation.musicBeans;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import sjy.elwg.notation.NoteCanvas;

/**
 * 附点实体
 * @author sjy
 *
 */
public class UIDot extends JPanel{
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 6887314525369158735L;
	/**
	 * 附点个数
	 */
	private int dotNum;
	
	/**
	 * 构造函数
	 * @param dotNum
	 */
	public UIDot(int dotNum){
		super();
		this.dotNum = dotNum;
		adjustSize();
		setOpaque(false);
		setBackground(Color.red);
		repaint();
	}
	
	/**
	 * 调整大小
	 */
	public void adjustSize(){
		if(dotNum == 1){
			setSize(6, 12);
		}else if(dotNum ==2){
			setSize(12, 15);
		}
	}

	public void paintComponent(Graphics gg){
		Graphics2D g = (Graphics2D) gg;	
		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        	renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        	g.setRenderingHints(renderHints);
//		g.setFont(new Font("mscore1", Font.PLAIN, 30));
		g.setFont(NoteCanvas.MSCORE1_FONT.deriveFont(30f));
		if(dotNum == 1){
			g.drawString("\uE10A", 2, 10);
		}else if(dotNum == 2){
			g.drawString("\uE10A", 2, 10);
			g.drawString("\uE10A", 7, 10);
		}
	}

	/**
	 * 获得附点个数
	 * @return
	 */
	public int getDotNum() {
		return dotNum;
	}

	/**
	 * 设置附点个数 
	 * @param dotNum
	 */
	public void setDotNum(int dotNum) {
		this.dotNum = dotNum;
	}

}
