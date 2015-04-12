package sjy.elwg.notation.musicBeans.symbolLines;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import sjy.elwg.notation.NoteCanvas;
import sjy.elwg.notation.musicBeans.Measure;
/**
 * 每行用来表示同一声部的括弧
 * @author sjy
 *
 */
public class Bracket extends JPanel{
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -5132298675158443786L;
	private Font font = NoteCanvas.MUSICAL_FONT.deriveFont(35f);
//	private Font font = new Font("musicalSymbols", Font.PLAIN, 35);
	
	public Bracket(){
		super();
		setLayout(null);
		setOpaque(false);
		setSize(12, 2*Measure.MEASURE_HEIGHT);
		repaint();
	}
	
	public void paintComponent(Graphics gg){
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg;	
		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g.setRenderingHints(renderHints);
        g.setFont(font);
		double ratio = (double)getHeight()/(4*font.getSize());
		g.scale(1.0, ratio);
		g.drawString("\uf0a7", 0, font.getSize());
		g.drawString("\uf0ea", 0, 3 * font.getSize());
	}
	
	public void setSize(int x, int y){
		super.setSize(x, y);
		repaint();
	}

}
