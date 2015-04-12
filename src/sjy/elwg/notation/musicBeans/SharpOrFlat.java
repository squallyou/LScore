package sjy.elwg.notation.musicBeans;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import sjy.elwg.notation.NoteCanvas;

/**
 * 升降调号
 * @author jinyuan.sun
 *
 */
public class SharpOrFlat extends JPanel implements Selectable{
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 5958074630251405095L;

	/**
	 * 升降号类型：有效值有：sharp, flat, natural, double-sharp, double-flat, sharp-sharp, flat-flat.
	 */
	private String type;
	
	/**
	 * 是否被选择
	 */
	private boolean selected;
	
	/**
	 * 所属的音符
	 */
	private Note note;
	
	public SharpOrFlat(String type){
		super();
		this.type = type;
		setLayout(null);
		setOpaque(false);
		setSize(Note.NORMAL_HEAD_WIDTH, 3*Note.NORMAL_HEAD_WIDTH);
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
		if(type.equalsIgnoreCase("sharp")){
			g.drawString("\uE10E", 0, 2 * NoteCanvas.LINE_GAP);
		}else if(type.equalsIgnoreCase("flat")){
			g.drawString("\uE114", 0, 2 * NoteCanvas.LINE_GAP);
		}else if(type.equalsIgnoreCase("double-sharp")){
//			g.setFont(new Font("musicalSymbols", Font.PLAIN, 30));
			g.setFont(NoteCanvas.MUSICAL_FONT.deriveFont(30f));
			g.drawString("\uf0DC", 0, 2 * NoteCanvas.LINE_GAP);
//			g.setFont(new Font("mscore", Font.PLAIN, 35));
			g.setFont(NoteCanvas.MCORE_FONT.deriveFont(35f));
		}else if(type.equalsIgnoreCase("double-flat")){
			g.drawString("\uE11A", 0, 2 * NoteCanvas.LINE_GAP);
		}else if(type.equalsIgnoreCase("natural")){
			g.drawString("\uE113", 0, 2 * NoteCanvas.LINE_GAP);
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public Note getNote() {
		return note;
	}

	public void setNote(Note note) {
		this.note = note;
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
	
}
