package sjy.elwg.notation.musicBeans;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import sjy.elwg.notation.NoteCanvas;
/**
 * 谱号
 * @author sjy
 *
 */
public class UIClef extends JPanel{
	
    /**
	 * 序列号
	 */
	private static final long serialVersionUID = -1185461229574551193L;

	/**
     * 放置谱号时与前一个UI对象的间距
     */
	public static final int CLEF_GAP = 5;
	
	/**
	 * 有效值有： "g/2", "g1u/2","g2u/2","g1d/2","g/1","f/4","f1u/4","f2u/4","f1d/4"
	 * "f2d/4", "f/3","f/5","c/3","c/1"	,"c/2","c/4","c/5","percussion"
	 * 
	 */
	private String clefType;
	
	/**
	 * 是否被选中
	 */
	private boolean selected;
	
	/**
	 * 构造函数
	 * @param type
	 */
	public UIClef(String type){
		super();
		this.clefType = type;
		selected = false;
		adjustSize();
		setOpaque(false);
	}
	
	/**
	 * 调整大小
	 */
	public void adjustSize(){
		if(clefType.equalsIgnoreCase("g/2")||clefType.equalsIgnoreCase("g/1")) 
				setSize(2*NoteCanvas.LINE_GAP+5, 80);
		else if(clefType.equalsIgnoreCase("f/4")||clefType.equalsIgnoreCase("f/3")||clefType.equalsIgnoreCase("f/5")) 
				setSize(2*NoteCanvas.LINE_GAP+5, 3*Note.NORMAL_HEAD_WIDTH);
		else if(clefType.equalsIgnoreCase("c/3")||clefType.equalsIgnoreCase("c/1")||clefType.equalsIgnoreCase("c/2")
				||clefType.equalsIgnoreCase("c/4")||clefType.equalsIgnoreCase("c/5"))
				setSize(2*NoteCanvas.LINE_GAP+5, 4*NoteCanvas.LINE_GAP);
		else if(clefType.equalsIgnoreCase("g1u/2")||clefType.equalsIgnoreCase("g2u/2")||clefType.equalsIgnoreCase("g1d/2")) 
				setSize(2*NoteCanvas.LINE_GAP+5, 90);
		else if(clefType.equalsIgnoreCase("f1u/4")||clefType.equalsIgnoreCase("f2u/4")) 
				setSize(2*NoteCanvas.LINE_GAP+5, 3*Note.NORMAL_HEAD_WIDTH+15);
		else if(clefType.equalsIgnoreCase("f1d/4")||clefType.equalsIgnoreCase("f2d/4")) 
				setSize(2*NoteCanvas.LINE_GAP+5, 5*Note.NORMAL_HEAD_WIDTH+10);
		else if(clefType.equalsIgnoreCase("percussion")){
			setSize(2*NoteCanvas.LINE_GAP, 2*NoteCanvas.LINE_GAP);
		}
	}
	
	/**
	 * 获得谱号类型
	 * @return
	 */
	public String getClefType() {
		return clefType;
	}

	/**
	 * 设置谱号类型
	 * @param clefType
	 */
	public void setClefType(String clefType) {
		this.clefType = clefType;
		adjustSize();
	}

	/**
	 * 是否被选中
	 * @return
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * 设置被选中
	 * @param selected
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	/**
	 * 放置的时候坐标相对小节y值的offset.
	 * @return
	 */
	public int getPositionOffset(){
		if(clefType.equalsIgnoreCase("g/2")||clefType.equalsIgnoreCase("g1d/2")) 
			return (int)(-11*1.5);
		else if(clefType.equalsIgnoreCase("g1u/2")||clefType.equalsIgnoreCase("g2u/2")) 
			return (int)(-11*2.5);
		else if(clefType.equalsIgnoreCase("g/1")) return (int)(-11*0.5);
		else if(clefType.equalsIgnoreCase("f/4") || clefType.equalsIgnoreCase("f1d/4")||
				clefType.equalsIgnoreCase("f2d/4"))
			return(int)(-11*0.1);
		else if(clefType.equalsIgnoreCase("f1u/4") || clefType.equalsIgnoreCase("f2u/4")) 
			return(int)(-11*1.1);
		else if(clefType.equalsIgnoreCase("f/3")) 
			return(int)(11*0.9);
		else if(clefType.equalsIgnoreCase("f/5")) 
			return(int)(-11*1.0);
		else if(clefType.equalsIgnoreCase("c/3")) 
			return(int)(11*0.1);
		else if(clefType.equalsIgnoreCase("c/1")) 
			return(int)(11*1.9);
		else if(clefType.equalsIgnoreCase("c/2")) 
			return(int)(11*1.0);
		else if(clefType.equalsIgnoreCase("c/4")) 
			return(int)(-11*0.9);
		else if(clefType.equalsIgnoreCase("c/5")) 
			return(int)(-11*1.9);
		else if(clefType.equals("percussion"))
			return NoteCanvas.LINE_GAP;
		return 0;
	}

	public void paintComponent(Graphics gg){
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg;	
		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g.setRenderingHints(renderHints);
//		g.setFont(new Font("mscore", Font.PLAIN, 35));
	//	g.setFont(NoteCanvas.MCORE_FONT.deriveFont(37f));
		if(selected){
			g.setColor(Color.red);
		}
		if(clefType.equalsIgnoreCase("g/2")) {
			g.setFont(NoteCanvas.MCORE_FONT.deriveFont(37f));
			g.drawString("\uE19E", 0, 46);
//			g.setFont(NoteCanvas.FREESERIF.deriveFont(10f));
//			g.drawString("8", 9, 12);
//			g.setFont(NoteCanvas.MCORE_FONT.deriveFont(37f));
//			g.drawString("\uE19E", 0,57);	
		}
		else if(clefType.equalsIgnoreCase("g1u/2")){
			g.setFont(NoteCanvas.FREESERIF.deriveFont(10f));
			g.drawString("8", 9, 10);
			g.setFont(NoteCanvas.MCORE_FONT.deriveFont(37f));
			g.drawString("\uE19E", 0,57);
		}else if(clefType.equalsIgnoreCase("g2u/2")){
			g.setFont(NoteCanvas.FREESERIF.deriveFont(10f));
			g.drawString("15", 7, 10);
			g.setFont(NoteCanvas.MCORE_FONT.deriveFont(37f));
			g.drawString("\uE19E", 0,57);
		}else if(clefType.equalsIgnoreCase("g1d/2")){
			g.setFont(NoteCanvas.FREESERIF.deriveFont(10f));
			g.drawString("8", 9, 78);
			g.setFont(NoteCanvas.MCORE_FONT.deriveFont(37f));
			g.drawString("\uE19E", 0,46);
		}else  if(clefType.equalsIgnoreCase("g/1")) {
			g.setFont(NoteCanvas.MCORE_FONT.deriveFont(37f));
			g.drawString("\uE19E", 0, 46);	
		}
		 else if(clefType.equalsIgnoreCase("f/4")){
//			g.setFont(NoteCanvas.FREESERIF.deriveFont(10f));
//			g.drawString("15", 5, 40);
//			g.setFont(NoteCanvas.MCORE_FONT.deriveFont(37f));
//			g.drawString("\uE19C", 0, Note.NORMAL_HEAD_WIDTH);
			g.setFont(NoteCanvas.MCORE_FONT.deriveFont(37f));
			g.drawString("\uE19C", 0, Note.NORMAL_HEAD_WIDTH);
		}else if(clefType.equalsIgnoreCase("f1u/4")){
			g.setFont(NoteCanvas.FREESERIF.deriveFont(10f));
			g.drawString("8", 7, 10);
			g.setFont(NoteCanvas.MCORE_FONT.deriveFont(37f));
			g.drawString("\uE19C", 0, Note.NORMAL_HEAD_WIDTH+11);
		}else if(clefType.equalsIgnoreCase("f2u/4")){
			g.setFont(NoteCanvas.FREESERIF.deriveFont(10f));
			g.drawString("15", 5, 10);
			g.setFont(NoteCanvas.MCORE_FONT.deriveFont(37f));
			g.drawString("\uE19C", 0, Note.NORMAL_HEAD_WIDTH+11);
		}else if(clefType.equalsIgnoreCase("f1d/4")){
			g.setFont(NoteCanvas.FREESERIF.deriveFont(10f));
			g.drawString("8", 6, 53);
			g.setFont(NoteCanvas.MCORE_FONT.deriveFont(37f));
			g.drawString("\uE19C", 0, Note.NORMAL_HEAD_WIDTH);
		}else if(clefType.equalsIgnoreCase("f2d/4")){
			g.setFont(NoteCanvas.FREESERIF.deriveFont(10f));
			g.drawString("15", 5, 53);
			g.setFont(NoteCanvas.MCORE_FONT.deriveFont(37f));
			g.drawString("\uE19C", 0, Note.NORMAL_HEAD_WIDTH);
		}else if(clefType.equalsIgnoreCase("f/3")){
			g.setFont(NoteCanvas.MCORE_FONT.deriveFont(37f));
			g.drawString("\uE19C", 0, Note.NORMAL_HEAD_WIDTH);
		}else if(clefType.equalsIgnoreCase("f/5")){
			g.setFont(NoteCanvas.MCORE_FONT.deriveFont(37f));
			g.drawString("\uE19C", 0, Note.NORMAL_HEAD_WIDTH);
		}
		else if(clefType.equalsIgnoreCase("c/1")||clefType.equalsIgnoreCase("c/2")||clefType.equalsIgnoreCase("c/3")
				||clefType.equalsIgnoreCase("c/4")||clefType.equalsIgnoreCase("c/5")){
			g.setFont(NoteCanvas.MCORE_FONT.deriveFont(37f));
			g.drawString("\uE19A", 0, 2*NoteCanvas.LINE_GAP);
		}
		else if(clefType.equals("percussion")){
			for(int i = 3; i < 8; i++){
				g.drawLine(i, 0, i, getHeight());
			}
			for(int i = 12; i < 17; i++){
				g.drawLine(i, 0, i, getHeight());
			}
		}
	}

}
