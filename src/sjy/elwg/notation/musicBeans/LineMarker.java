package sjy.elwg.notation.musicBeans;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;

import javax.swing.JPanel;
/**
 * 乐谱行的水平标识符,用于Y轴方向拖拽谱表
 * @author jingyuan.sun
 *
 */
public class LineMarker extends JPanel{
	
	/**
	 * 默认序列号
	 */
	private static final long serialVersionUID = 1L;
	
	private NoteLine line;

	public LineMarker(){
		super();
		setSize(10, 6);
		repaint();
	}
	
	public void paintComponent(Graphics gg){
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg;	
		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g.setRenderingHints(renderHints);
        g.setColor(Color.red);
		Polygon poly = new Polygon();
		poly.addPoint(0, 0);
		poly.addPoint(0, getHeight());
		poly.addPoint(getWidth(), getHeight()/2);
		g.fillPolygon(poly);
	}

	public NoteLine getLine() {
		return line;
	}

	public void setLine(NoteLine line) {
		this.line = line;
	}

}
