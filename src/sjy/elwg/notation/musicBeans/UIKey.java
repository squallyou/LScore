package sjy.elwg.notation.musicBeans;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import sjy.elwg.notation.NoteCanvas;
/**
 * 调号实体
 * 注意在小节中所存储的字符串型调号类型变量，与小节的实际调号实体的调号类型可能不一样，调号实体取决于小节在乐谱中的位置.
 * @author sjy
 *
 */
public class UIKey extends JPanel{
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -8113634949273674746L;

	/**
	 * 放置调号时与前一个UI对象的间距
	 */
	public static final int KEY_GAP = 3;
	
	/**
	 * 组成调号的单个符号的宽度
	 */
	public static final int SINGLEWIDTH = 8;
	
	/**
	 * 升号，降号以及恢复号的单个符号
	 */
	public static final String FONT_SHARP = "\uE10E";
	public static final String FONT_FLAT = "\uE114";
	public static final String FONT_NATURAL = "\uE113";
	
	/**
	 * 调号值. 取值范围从-7到7.不能为0.正数为升号，负数为降号.
	 */
	private int keyValue;
	
	/**
	 * 是否是恢复正常调号
	 */
	private boolean restoreNatural;
	
	/**
	 * 谱号类型.因为调号实体的形状与对应的谱号有关.
	 */
	private String clefType;
	
	/**
	 * 是否被选中
	 */
	private boolean selected;
	
	/**
	 * 构造函数
	 * @param keyValue
	 * @param clefType
	 */
	public UIKey(int keyValue, String clefType){
		super();
		this.keyValue = keyValue;
		this.clefType = clefType;
		selected = false;
		adjustSize();
		setOpaque(false);
		//setBackground(Color.red);
	}
	
	/**
	 * 调整大小
	 */
	public void adjustSize(){
		if(keyValue == 1 || keyValue == -1){
			setSize(NoteCanvas.LINE_GAP+2, NoteCanvas.LINE_GAP*3 + 5);
		}else if(keyValue == 2 || keyValue == -2){
			setSize(2*SINGLEWIDTH+2, NoteCanvas.LINE_GAP*4 + 5);
		}else if(keyValue == 3 || keyValue == -3){
			setSize(3*SINGLEWIDTH+2, NoteCanvas.LINE_GAP*5 + 5);
		}else if(keyValue == 4 || keyValue == -4){
			setSize(4*SINGLEWIDTH+2, NoteCanvas.LINE_GAP*5 + 5);
		}else if(keyValue == 5 || keyValue == -5){
			setSize(5*SINGLEWIDTH+2, NoteCanvas.LINE_GAP*6 + 5);
		}else if(keyValue == 6 || keyValue == -6){
			setSize(6*SINGLEWIDTH+2, NoteCanvas.LINE_GAP*6 + 5);
		}else if(keyValue == 7 || keyValue == -7){
			setSize(7*SINGLEWIDTH+2, NoteCanvas.LINE_GAP*6 + 5);
		}
	}
	
	/**
	 * 获得是否恢复正常调
	 * @return
	 */
	public boolean isRestoreNatural() {
		return restoreNatural;
	}

	/**
	 * 设置恢复正常调
	 * @param restoreNatural
	 */
	public void setRestoreNatural(boolean restoreNatural) {
		this.restoreNatural = restoreNatural;
	}

	/**
	 * 获得调号值，
	 * @return
	 */
	public int getKeyValue() {
		return keyValue;
	}

	/**
	 * 设置调号值
	 * @param keyValue
	 */
	public void setKeyValue(int keyValue) {
		this.keyValue = keyValue;
	}

	/**
	 * 获得谱号类型
	 * @return
	 */
	public String getClefType() {
		return clefType;
	}

	/**
	 * 设置谱号类型.
	 * @param clefType
	 */
	public void setClefType(String clefType) {
		this.clefType = clefType;
	}

	/**
	 * 获得是否被选择
	 * @return
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * 设置被选择
	 * @param selected
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void paintComponent(Graphics gg){
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg;	
		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g.setRenderingHints(renderHints);
//		g.setFont(new Font("mscore", Font.PLAIN, 35));
		g.setFont(NoteCanvas.MCORE_FONT.deriveFont(35f));
		if(selected) g.setColor(Color.red);
		else g.setColor(Color.black);
		
		String fontToUse = null;
		if(keyValue > 0){
			if(restoreNatural) fontToUse = FONT_NATURAL;
			else fontToUse = FONT_SHARP;
		}else if(keyValue < 0){
			if(restoreNatural) fontToUse = FONT_NATURAL;
			else fontToUse = FONT_FLAT;
		}
		if(clefType.equalsIgnoreCase("g/2") || clefType.equalsIgnoreCase("f/4") || clefType.equalsIgnoreCase("c/3")){
			if(keyValue == 1){
				g.drawString(fontToUse, 1, 15);
			}else if(keyValue == 2){
				g.drawString(fontToUse, 1, 15);
				g.drawString(fontToUse, SINGLEWIDTH, 30);
			}else if(keyValue == 3){
				g.drawString(fontToUse, 1, 20);
				g.drawString(fontToUse, SINGLEWIDTH, 35);
				g.drawString(fontToUse, 2*SINGLEWIDTH, 15);
			}else if(keyValue == 4){
				g.drawString(fontToUse, 1, 20);
				g.drawString(fontToUse, SINGLEWIDTH, 35);
				g.drawString(fontToUse, 2*SINGLEWIDTH, 15);
				g.drawString(fontToUse, 3*SINGLEWIDTH, 30);
			}else if(keyValue == 5){
				g.drawString(fontToUse, 1, 20);
				g.drawString(fontToUse, SINGLEWIDTH, 35);
				g.drawString(fontToUse, 2*SINGLEWIDTH, 15);
				g.drawString(fontToUse, 3*SINGLEWIDTH, 30);
				g.drawString(fontToUse, 4*SINGLEWIDTH, 45);
			}else if(keyValue == 6){
				g.drawString(fontToUse, 1, 20);
				g.drawString(fontToUse, SINGLEWIDTH, 35);
				g.drawString(fontToUse, 2*SINGLEWIDTH, 15);
				g.drawString(fontToUse, 3*SINGLEWIDTH, 30);
				g.drawString(fontToUse, 4*SINGLEWIDTH, 45);
				g.drawString(fontToUse, 5*SINGLEWIDTH, 25);
			}else if(keyValue == 7){
				g.drawString(fontToUse, 1, 20);
				g.drawString(fontToUse, SINGLEWIDTH, 35);
				g.drawString(fontToUse, 2*SINGLEWIDTH, 15);
				g.drawString(fontToUse, 3*SINGLEWIDTH, 30);
				g.drawString(fontToUse, 4*SINGLEWIDTH, 45);
				g.drawString(fontToUse, 5*SINGLEWIDTH, 25);
				g.drawString(fontToUse, 6*SINGLEWIDTH, 40);
			}
			if(keyValue == -1){
				g.drawString(fontToUse, 1, 25);
			}else if(keyValue == -2){
				g.drawString(fontToUse, 1, 40);
				g.drawString(fontToUse, SINGLEWIDTH, 25);
			}else if(keyValue == -3){
				g.drawString(fontToUse, 1, 40);
				g.drawString(fontToUse, SINGLEWIDTH, 25);
				g.drawString(fontToUse, 2*SINGLEWIDTH, 45);
			}else if(keyValue == -4){
				g.drawString(fontToUse, 1, 40);
				g.drawString(fontToUse, SINGLEWIDTH, 25);
				g.drawString(fontToUse, 2*SINGLEWIDTH, 45);
				g.drawString(fontToUse, 3*SINGLEWIDTH, 30);
			}else if(keyValue == -5){
				g.drawString(fontToUse, 1, 40);
				g.drawString(fontToUse, SINGLEWIDTH, 25);
				g.drawString(fontToUse, 2*SINGLEWIDTH, 45);
				g.drawString(fontToUse, 3*SINGLEWIDTH, 30);
				g.drawString(fontToUse, 4*SINGLEWIDTH, 50);
			}else if(keyValue == -6){
				g.drawString(fontToUse, 1, 40);
				g.drawString(fontToUse, SINGLEWIDTH, 25);
				g.drawString(fontToUse, 2*SINGLEWIDTH, 45);
				g.drawString(fontToUse, 3*SINGLEWIDTH, 30);
				g.drawString(fontToUse, 4*SINGLEWIDTH, 50);
				g.drawString(fontToUse, 5*SINGLEWIDTH, 35);
			}else if(keyValue == -7){
				g.drawString(fontToUse, 1, 40);
				g.drawString(fontToUse, SINGLEWIDTH, 25);
				g.drawString(fontToUse, 2*SINGLEWIDTH, 45);
				g.drawString(fontToUse, 3*SINGLEWIDTH, 30);
				g.drawString(fontToUse, 4*SINGLEWIDTH, 50);
				g.drawString(fontToUse, 5*SINGLEWIDTH, 35);
				g.drawString(fontToUse, 6*SINGLEWIDTH, 55);
			}
		}
	}
	
	/**
	 * 根据调号与谱号类型，返回放置调号时调号的y坐标与调号所在小节y坐标的差值,即调号相对小节的y坐标值
	 * @return
	 */
	public int getPositionOffset(){
		if(keyValue == 0){
			System.err.println("调号实体的keyValue值不能为0");
			return 0;
		}
		if(clefType.equalsIgnoreCase("g/2")){
			if(keyValue == 1 || keyValue == 2){
				return -15;
			}else if(keyValue > 0){
				return -20;
			}else if(keyValue == -1){
				return -5;
			}else if(keyValue < 0){
				return -20;
			}
		}else if(clefType.equalsIgnoreCase("f/4")){
			if(keyValue == 1 || keyValue == 2){
				return -5;
			}else if(keyValue > 0){
				return -10;
			}else if(keyValue == -1){
				return 5;
			}else if(keyValue < 0){
				return -10;
			}
		}else if(clefType.equalsIgnoreCase("c/3")){
			if(keyValue == 1 || keyValue == 2){
				return -10;
			}else if(keyValue > 0){
				return -15;
			}else if(keyValue == -1){
				return 0;
			}else if(keyValue < 0){
				return -15;
			}
		}
		return 0;
	}

}
