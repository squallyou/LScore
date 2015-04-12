package sjy.elwg.notation.musicBeans;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import sjy.elwg.notation.NoteCanvas;
import sjy.elwg.notation.musicBeans.symbolLines.NoteSymbol;

public class TempoText extends NoteSymbol implements DocumentListener, MouseMotionListener, MouseListener, Editable, Selectable{

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -1429531940642655294L;
	
	public static final int HEIGHT = 25;
	
	private JPanel symbolPanel;
	
	private EditField nameField;
	
	private JTextField tempoField;
	
	private int tempo = -1;
	
	private boolean displayTempo;

	/**
	 * 鼠标事件相对屏幕的坐标
	 */
	protected Point screenPoint = new Point(); 
	
	/**
	 * 编辑模式下歌词的边框
	 */
	private Border border = BorderFactory.createLineBorder(Color.BLUE);
	
	
	/**
	 * 构造函数
	 * @param name
	 * @param tempo
	 * @param displayTempo
	 */
	public TempoText(String name, int tempo, boolean displayTempo){
		super(name);
		this.tempo = tempo;
		this.displayTempo = displayTempo;
		initComponents(name);
		setOpaque(false);
		setLayout(null);
		nameField.getDocument().addDocumentListener(this);
		nameField.addMouseMotionListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		viewMode();
		resize();
		repaint();
	}
	
	/**
	 * 构造函数
	 * @param name
	 */
	public TempoText(String name){
		super(name);
		initComponents(name);
		setOpaque(false);
		setLayout(null);
		nameField.getDocument().addDocumentListener(this);
		nameField.addMouseMotionListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		viewMode();
		resize();
		repaint();
	}
	
	/**
	 * 初始化组件
	 */
	public void initComponents(String name){
		nameField = new EditField(name);
		adjustNameSize();
		nameField.setLocation(0, 0);
		add(nameField);
		
		if(displayTempo){
			symbolPanel = new JPanel(){
				private static final long serialVersionUID = -6315720988191274590L;

				public void paintComponent(Graphics gg){
					super.paintComponent(gg);
					Graphics2D g = (Graphics2D) gg;	
					RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
				    renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
				    g.setRenderingHints(renderHints);
				    g.setFont(NoteCanvas.MCORE_FONT.deriveFont(20f));
				    g.drawString("\uE12D", 2, getHeight()-10);
				    g.drawLine(symbolPanel.getWidth()-12, 0, symbolPanel.getWidth()-12, getHeight()-10);
				 //   g.drawLine(symbolPanel.getWidth(),getHeight()/2-2,symbolPanel.getWidth(),getHeight()/2-2);
				    g.setFont(NoteCanvas.FREESERIF.deriveFont(16f));
				    g.drawString("=", 12, getHeight()-10);
				}
			};
			tempoField = new JTextField(String.valueOf(tempo));
			tempoField.setOpaque(true);
			
			symbolPanel.setSize(20, HEIGHT);
			tempoField.setSize(30, HEIGHT);
			
			symbolPanel.setLocation(nameField.getX() + nameField.getWidth(), 0);
			tempoField.setLocation(symbolPanel.getX() + symbolPanel.getWidth(), 0);
			
			symbolPanel.setOpaque(false);
			tempoField.setOpaque(false);
			tempoField.setBorder(null);
			symbolPanel.repaint();
			tempoField.repaint();
			nameField.repaint();
			
			add(symbolPanel);
			add(tempoField);
			tempoField.addMouseListener(this);
		}
		
	}
	
	/**
	 * 调整文字宽度
	 */
	public void adjustNameSize(){
		if(nameField.getText().equalsIgnoreCase("")){
			nameField.setSize(3, HEIGHT);
		}else{
			int width = nameField.getFontMetrics(nameField.getFont()).stringWidth(nameField.getText())+5;
			nameField.setSize(width , HEIGHT);
		}
	}

	/**
	 * 调整整个组件大小
	 */
	public void resize(){
		int width = 0;
		if(nameField != null)
			width += nameField.getWidth();
		if(displayTempo)
			width += tempoField.getWidth() + symbolPanel.getWidth();
		setSize(width + 5, HEIGHT);
		relocate();
	}
	
	/**
	 * 重新放置组件
	 */
	public void relocate(){
		if(nameField != null)
			nameField.setLocation(0, 0);
		if(displayTempo){
			symbolPanel.setLocation(nameField.getX() + nameField.getWidth(), 0);
			tempoField.setLocation(symbolPanel.getX() + symbolPanel.getWidth(), 0);
		}
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		adjustNameSize();
		resize();
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		adjustNameSize();
		resize();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		adjustNameSize();
		resize();
	}

	@Override
	public void editMode() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void viewMode() {
		// TODO Auto-generated method stub
		if(nameField.isEditable()){
			nameField.setEditable(false);
		}
		if(tempoField != null && tempoField.isEditable()){
			tempoField.setEditable(false);
		}
		nameField.setBorder(null);
		if(tempoField != null)
			tempoField.setBorder(null);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if((e.getSource() == nameField || e.getSource() == this) && e.getClickCount() == 2){
			nameField.setEditable(true);
			nameField.setBorder(border);
		}
		if(e.getSource() == tempoField && e.getClickCount() == 2){
			tempoField.setEditable(true);
			nameField.setBorder(border);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		//观察状态下被选中
		screenPoint.setLocation((int)e.getXOnScreen(), (int)e.getYOnScreen());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beSelected() {
		// TODO Auto-generated method stub
		nameField.setForeground(Color.BLUE);
		tempoField.setForeground(Color.blue);
		nameField.repaint();
		tempoField.repaint();
	}

	@Override
	public void cancleSelected() {
		// TODO Auto-generated method stub
		nameField.setForeground(Color.BLACK);
		
		nameField.repaint();
		if(tempoField != null){
			tempoField.setForeground(Color.BLACK);
			tempoField.repaint();
		}
		viewMode();
	}
	
	public void addMouseListener(MouseListener l){
		super.addMouseListener(l);
		nameField.addMouseListener(l);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		int x = e.getXOnScreen();
    	int y = e.getYOnScreen();
    	
    	int deltax = x - (int)screenPoint.getX();
    	int deltay = y - (int)screenPoint.getY();
    	
    	screenPoint.setLocation(x, y);
    	draggedX += deltax;
    	draggedY += deltay;
    	
    	int curX = getX();
    	int curY = getY();
    	setLocation(curX + deltax, curY + deltay);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	/********************************************************************************************************************/
	
	/**
	 * 内部类，可编辑区域类
	 * @author Administrator
	 *
	 */
	public class EditField extends JTextField implements Selectable, MouseMotionListener, MouseListener{

		/**
		 * 序列号
		 */
		private static final long serialVersionUID = 3545379070277606870L;
		
		private EditField(String name){
			super(name);
			setOpaque(false);
			setBorder(null);
		}

		@Override
		public void beSelected() {
			// TODO Auto-generated method stub
			if(!isEditable()){
				setForeground(Color.blue);
				repaint();
			}
		}

		@Override
		public void cancleSelected() {
			// TODO Auto-generated method stub
			viewMode();
			setForeground(Color.black);
			repaint();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			if(isEditable()){
				int x = e.getXOnScreen();
		    	int y = e.getYOnScreen();
		    	
		    	int deltax = x - (int)screenPoint.getX();
		    	int deltay = y - (int)screenPoint.getY();
		    	
		    	screenPoint.setLocation(x, y);
		    	draggedX += deltax;
		    	draggedY += deltay;
		    	
		    	int curX = getX();
		    	int curY = getY();
		    	setLocation(curX + deltax, curY + deltay);
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			//观察状态下被选中
			if(isEditable())
				screenPoint.setLocation((int)e.getXOnScreen(), (int)e.getYOnScreen());
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}

	@Override
	public void adjustSize() {
		// TODO Auto-generated method stub
		resize();
	}
	
	/**
	 * 返回速度记号内容
	 * @return
	 */
	public String getText(){
		return nameField.getText();
	}
	
	/**
	 * 返回速度
	 * @return
	 */
	public int getTempo(){
		return tempo;
	}
	
}
