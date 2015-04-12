package sjy.elwg.notation.musicBeans;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import sjy.elwg.notation.FontChooser;

/**
 * 可自由进行编辑，拖拽，更改字体等操作的area文本;与之类似见FreeTextField.
 * 例如自由标注文本
 * @author jingyuan.sun
 *
 */
public abstract class FreeTextArea extends JTextArea implements Editable, DocumentListener,MouseMotionListener,MouseListener,Selectable{
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -6275980176134532122L;

	/**
	 * 默认字体
	 */
	public static Font LYRIC_FONT = new Font("宋体", Font.PLAIN, 20);
	/**
	 * 歌词边框高度
	 */
	protected int fontHeight = LYRIC_FONT.getSize() + 4;
	
	/**
	 * 当前状态值
	 */
	protected int mode = VIEW_MODE;
	
	/**
	 * 被用户所拖动过的x距离和y距离
	 * 在进行符号放置时，其位置是默认位置与拖动位置之和
	 */
	protected int draggedX = 0;
	protected int draggedY = 0;


	/**
	 * 鼠标事件相对屏幕的坐标
	 */
	protected Point screenPoint = new Point(); 
	
	/**
	 * 编辑模式下文本的边框
	 */
	protected Border border = BorderFactory.createLineBorder(Color.BLUE);
	
	/**
	 * 默认颜色
	 */
	protected Color defaultColor = Color.BLACK;
	
	/**
	 * 构造函数
	 *
	 */
	public FreeTextArea(){
		super();
		setFont(LYRIC_FONT);		
		reSize();
		editMode();
		mode = EDIT_MODE;
		getDocument().addDocumentListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	/**
	 * 构造函数
	 * @param str
	 */
	public FreeTextArea(String str){
		super(str);
		setFont(LYRIC_FONT);
		setOpaque(false);
		setText(str);
		reSize();
		viewMode();
		getDocument().addDocumentListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	/**
	 * 根据文字输入
	 */
	public void reSize(){	
		int height = getLineCount() * (getFont().getSize() + 4);
		//所有行的最大宽度
		int maxWidth = 0;
		int offset = 0;
		for(int i = 0; i < getLineCount(); i++){
			try {
				String subStr = getText().substring(offset, getLineEndOffset(i));
				int tempWidth = getFontMetrics(getFont()).stringWidth(subStr);
				if(tempWidth > maxWidth)
					maxWidth = tempWidth;
				offset = getLineEndOffset(i);
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		height = height < 10 ? 10 : height;
		if(maxWidth == 0)
			setSize(5, (height + 1));
		else
			setSize((int)(maxWidth + 3), (height + 1));
	}



	public void changedUpdate(DocumentEvent e) {
		reSize();
	}

	public void insertUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		reSize();
	}

	public void removeUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		reSize();
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

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getButton() == MouseEvent.BUTTON3){
			TextPopMenu tm = new TextPopMenu();
			tm.show((Component)(e.getSource()), e.getX(), e.getY());
		}
		else if (e.getSource() == this && e.getClickCount() == 2) {
			editMode();
			requestFocus();
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

	public int getDraggedX() {
		return draggedX;
	}

	public void setDraggedX(int draggedX) {
		this.draggedX = draggedX;
	}

	public int getDraggedY() {
		return draggedY;
	}

	public void setDraggedY(int draggedY) {
		this.draggedY = draggedY;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	public void beSelected(){
		setForeground(Color.blue);
		repaint();
	}
	
	public void cancleSelected(){
		viewMode();
		setForeground(defaultColor);
		repaint();
	}

	@Override
	public void editMode() {
		// TODO Auto-generated method stub
		mode = EDIT_MODE;
		reSize();
		setDragEnabled(false);
		setEditable(true);
		setOpaque(true);
		setCaretColor(Color.blue);
		setFocusable(true);
		setForeground(Color.blue);
		setBorder(border);
		revalidate();
	}

	@Override
	public void viewMode() {
		// TODO Auto-generated method stub
		mode =VIEW_MODE;
		setDragEnabled(true);
		setEditable(false);
		setOpaque(false);
		setFocusable(false);
		setBorder(null);
		revalidate();
	}

	public Color getDefaultColor(){
		return defaultColor;
	}
	
	public void setDefaultColor(Color color){
		this.defaultColor = color;
	}

	
	/**
	 * 弹出菜单 
	 *
	 */
	class TextPopMenu extends JPopupMenu{
		private static final long serialVersionUID = 41816163850165945L;
		private JMenuItem menuFont;
		private JMenuItem menuColor;
		public TextPopMenu(){
			super();
			install();
		}
		private void install(){
			menuFont = new JMenuItem("字体");
			this.add(menuFont);
			menuColor = new JMenuItem("颜色");
			this.add(menuColor);
			addMouseListener(FreeTextArea.this);
			menuFont.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Font font = FontChooser.showDialog(null, null, FreeTextArea.this.getFont());
					FreeTextArea.this.setFont(font);
					FreeTextArea.this.revalidate();
				}
			});
			menuColor.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Color color = JColorChooser.showDialog(FreeTextArea.this, "Color", Color.BLACK);
					FreeTextArea.this.setForeground(color);
					FreeTextArea.this.defaultColor = color;
					FreeTextArea.this.repaint();
				}
			});
		}
	}
}
