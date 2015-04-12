package sjy.elwg.notation.musicBeans;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Tuplet extends JPanel{
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 424574999595747856L;
	/**
	 * 在不倾斜时的基本高度
	 */
	public static final int TUP_HEIGHT = 15;
	
	
	/**
	 * 连音包含的音符序列
	 */
	private ArrayList<AbstractNote> noteList;
	/**
	 * 连音修正之后值，比例分子.如3连音（3:2），modification值为3, normal为2.
	 */
	private int modification;
	/**
	 * 连音修正之前的正常值，比例分母
	 */
	private int normal;
	/**
	 * 连音所占的实际时长
	 */
	private int realDuration;
	/**
	 * 连音号的倾斜方式，有效值有:"left", "right",分别代表着左边高、右边高
	 */
	private String highNode = "right";
	
	
	/**
	 * 构造函数
	 * @param note
	 */
	public Tuplet(AbstractNote note, int modification, int normal){
		noteList = new ArrayList<AbstractNote>();
		noteList.add(note);
		this.modification = modification;
		this.normal = normal;
		note.setTuplet(this);
		realDuration = note.getDuration() * normal;
		setOpaque(true);
	}
	
	/**
	 * 调整形状与大小
	 */
	public void adjustSize(){
		if(noteList.size() > 1){
			AbstractNote fnote = noteList.get(0);
			AbstractNote lnote = noteList.get(noteList.size()-1);
			int width = lnote.getX() - fnote.getX() + Note.NORMAL_HEAD_WIDTH;
			int ly = fnote.getYWithStem();
			int fy = lnote.getYWithStem();
			if(ly >= fy){
				highNode = "right";
				setSize(width, ly - fy + TUP_HEIGHT);
			}
			else if(ly < fy){
				highNode = "left";
				setSize(width, fy - ly + TUP_HEIGHT);
			}
		}
	}
	
	public void paintComponent(Graphics gg){
		Graphics2D g = (Graphics2D) gg;	
		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g.setRenderingHints(renderHints);
        g.setFont(new Font("mscore", Font.PLAIN, 13));
        char char0 = '\u0030';
        for(int i = 0; i < modification; i++){
        	char0++;
        }
        char[] charArray = new char[1];
        charArray[0] = char0;
        String strMod = new String(charArray);
        
        if(highNode.equalsIgnoreCase("right")){
        	g.drawLine(1, getHeight(), 1, getHeight() - TUP_HEIGHT/2);
        	g.drawLine(getWidth()-1, TUP_HEIGHT/2, getWidth()-1, TUP_HEIGHT);
        	//g.drawLine(1, getHeight()-TUP_HEIGHT/2, getWidth()-1, TUP_HEIGHT/2);
        	g.drawString(strMod, getWidth()/2 - 2, getHeight()/2+5);
        	double k = ((double)getHeight() - TUP_HEIGHT)/getWidth();
        	int x1 = getWidth()/2 - 7;
        	int x2 = getWidth()/2 + 9;
        	int y1 = getHeight() - TUP_HEIGHT/2 - (int)((x1-1)*k);
        	int y2 = getHeight() - TUP_HEIGHT/2 - (int)((x1-1)*k);
        	g.drawLine(1, getHeight()-TUP_HEIGHT/2, x1, y1);
        	g.drawLine(x2, y2-1, getWidth()-1, TUP_HEIGHT/2);
        }
        else if(highNode.equalsIgnoreCase("left")){
        	g.drawLine(1, TUP_HEIGHT, 1, TUP_HEIGHT/2);
        	g.drawLine(getWidth()-1, getHeight(), getWidth()-1, getHeight()-TUP_HEIGHT/2);
        	//g.drawLine(1, TUP_HEIGHT/2, getWidth()-1, getHeight()-TUP_HEIGHT/2);
        	g.drawString(strMod, getWidth()/2 - 2, getHeight()/2+4);
        	double k = ((double)getHeight() - TUP_HEIGHT)/getWidth();
        	int x1 = getWidth()/2 - 7;
        	int x2 = getWidth()/2 + 9;
        	int y1 = getHeight() - TUP_HEIGHT/2 - (int)((getWidth()-x1+1)*k);
        	int y2 = getHeight() - TUP_HEIGHT/2 - (int)((getWidth()-x2+1)*k);
        	g.drawLine(1, TUP_HEIGHT/2-1, x1, y1);
        	g.drawLine(x2, y2, getWidth()-1, getHeight()-TUP_HEIGHT/2);
        }
	}
	
	/**
	 * 放置位置
	 */
	public void locateTuplet(){
		if(noteList.size() > 1){
			AbstractNote fnote = noteList.get(0);
			AbstractNote lnote = noteList.get(noteList.size()-1);
			if(highNode.equalsIgnoreCase("left")){
				int rightY = lnote.getYWithStem();
				setLocation(fnote.getX(), rightY - getHeight() - 2);
			}
			else{
				int leftY = fnote.getYWithStem();
				setLocation(fnote.getX(), leftY - getHeight() -2);
			}
		}
	}

	public int getModification() {
		return modification;
	}

	public void setModification(int modification) {
		this.modification = modification;
	}

	public int getNormal() {
		return normal;
	}

	public void setNormal(int normal) {
		this.normal = normal;
	}

	public String getHighNode() {
		return highNode;
	}

	public void setHighNode(String highNode) {
		this.highNode = highNode;
	}

	public ArrayList<AbstractNote> getNoteList() {
		return noteList;
	}

	public int getRealDuration() {
		return realDuration;
	}
	
}
