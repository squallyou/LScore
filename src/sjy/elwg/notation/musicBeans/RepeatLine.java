package sjy.elwg.notation.musicBeans;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class RepeatLine extends JPanel implements Selectable{
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 2720936694846549815L;

	/**
	 * 是否被选中
	 */
	protected boolean selected;
	
	/**
	 * 重复记号的标号
	 */
	private JTextField number;
		
	/**
	 * 对应的小节
	 */
	private MeasurePart measurePart;
	
	/**
	 * 是否被选择
	 */
	private boolean isSelected;
		
	public RepeatLine(){
		super();
		setSize(30,20);
		isSelected = false;
		setLayout(null);
		setOpaque(false);
		setLayout(null);
		
		number = new JTextField("1.");
	//	System.out.println(number.getText());
		number.setSize(20, 10);
		number.setBorder(null);
		add(number);
		number.setLocation(7, 17);				
		repaint();
		
	}
	
	public JTextField getNumber() {
		return number;
	}

	public void setNumber(JTextField number) {
		this.number = number;
	}

	public void adjustSize(){
		setSize(this.getMeasurePart().getWidth()+2,30);
	}
	
	public void paintComponent(Graphics gg){
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg;	
		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g.setRenderingHints(renderHints);
        
        if(isSelected){
			g.setColor(Color.blue);
		}else{
			g.setColor(Color.black);
		}
        
        g.drawLine(0, 25, 0, 12);
        int x = this.getMeasurePart().getWidth();
        g.drawLine(0, 12, x, 12);
        g.drawLine(x, 12, x, 25);
       // g.drawString("yes", 5, 5);

	}
		
	public MeasurePart getMeasurePart() {
		return measurePart;
	}

	public void setMeasurePart(MeasurePart measurePart) {
		this.measurePart = measurePart;
	}

	/**
	 * 返回是否被选择
	 * @return
	 */
	public boolean isSelected() {
		return isSelected;
	}

	/**
	 * 设置被选择
	 * @param isSelected
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	@Override
	public void beSelected() {
		// TODO Auto-generated method stub
		isSelected = true;
		repaint();
	}

	@Override
	public void cancleSelected() {
		// TODO Auto-generated method stub
		isSelected = false;
		repaint();
	}

}
