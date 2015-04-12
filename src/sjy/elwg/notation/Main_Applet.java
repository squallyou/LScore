package sjy.elwg.notation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import sjy.elwg.notation.musicBeans.Page;
import sjy.elwg.utility.ConstantInterface;

/**
 * 
 * @author sunjingyuan (215522963@qq.com)
 * 			luwenxi		(110470164@qq.com)
 *
 */
public class Main_Applet extends JFrame implements ConstantInterface{
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 3L;
	/**
	 * 主面板
	 */
	private JPanel pane;
	/**
	 * 顶部面板
	 */
	private TopPanel topPanel;
	/**
	 * 底部面板
	 */
	private BottomPanel bottomPanel;
	/**
	 * 画板
	 */
	private NoteCanvas canvas;
	/**
	 * 滚轴窗口
	 */
	private JScrollPane scrollPane;
	/**
	 * 盛放滚轴窗口的中间面板
	 */
	private JLayeredPane centerPane;
	/**
	 * 悬浮符号面板
	 */
	private SymbolPanel symbolPanel;
	
	/**
	 * 拖动悬浮面板时用于记录位置的坐标
	 */
	private Point absPoint = new Point();
	
	
	public Main_Applet(){
		super("L$core");
		init();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	/**
	 * 调整符号面板的侦听器
	 * @author sjy
	 *
	 */
	class DragListener implements MouseMotionListener{

		@Override
		public void mouseDragged(MouseEvent e) {
			int absX = e.getXOnScreen();
			int absY = e.getYOnScreen();
			int deltax = absX - (int)absPoint.getX();
			int deltay = absY - (int)absPoint.getY();
			//当前面板应该所在位置
			int currentx = 0;
			int currenty = 0;
			//absPoint的更新值
			int currentAbsx = (int)absPoint.getX();
			int currentAbsy = (int)absPoint.getY();
			if(symbolPanel.getX()+deltax <= 0){
				currentx = 0;
			}else if(symbolPanel.getX()+deltax >= scrollPane.getWidth()-symbolPanel.getWidth()){
				currentx = scrollPane.getWidth()-symbolPanel.getWidth();
			}else{
				currentx = symbolPanel.getX() + deltax;
				currentAbsx = absX;
			}
			if(symbolPanel.getY()+deltay <= 0){
				currenty = 0;
			}else if(symbolPanel.getY()+deltay >= centerPane.getHeight()-symbolPanel.getHeight()){
				currenty = centerPane.getHeight()-symbolPanel.getHeight();
			}else{
				currenty = symbolPanel.getY() + deltay;
				currentAbsy = absY;
			}
            symbolPanel.setLocation(currentx, currenty);
            absPoint.setLocation(currentAbsx, currentAbsy);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			
		}
		
	}
	
	/**
	 * Applet已经被载入
	 */
	public void init(){
		setVisible(true);
		getContentPane().setLayout(null);
		
		pane = new JPanel();
		pane.setSize(getWidth()-1, getHeight()-1);
		pane.setLayout(null);
		pane.setLocation(1, 1);
		add(pane);
		
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream("config/configs.xml");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		Map<String, Integer> configs = XMLParser.getConfigs(inputStream);
		topPanel = new TopPanel();
		topPanel.setSize(pane.getWidth(), topPanel.getHeight());
		topPanel.setLocation(0, 0);
		
		bottomPanel = new BottomPanel();
		bottomPanel.setSize(pane.getWidth(), bottomPanel.getHeight());
		bottomPanel.setLocation(0, pane.getHeight() - bottomPanel.getHeight());
		
		symbolPanel = new SymbolPanel(configs);
		symbolPanel.setSize(135,185);
		
		/*拖拽符号面板使用*/
		DragListener dragger = new DragListener();
		symbolPanel.addMouseMotionListener(dragger);
		symbolPanel.getPane().addMouseMotionListener(dragger);
		MouseListener adapter = new MouseAdapter() {
			public void mousePressed(MouseEvent e){
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				absPoint.setLocation(x, y);
			}
		};
		symbolPanel.addMouseListener(adapter);
		symbolPanel.getPane().addMouseListener(adapter);
		/**/
		
		canvas = new NoteCanvas(topPanel, bottomPanel, symbolPanel);
		canvas.setPreferredSize(new Dimension(Page.PAGE_WIDTH , Page.PAGE_HEIGHT));
		canvas.setLocation(1, 1);
		symbolPanel.setCanvas(canvas);
		bottomPanel.setCanvas(canvas);
		topPanel.addFunctionListener(canvas);
		
		scrollPane = new JScrollPane(canvas);
		scrollPane.setSize(pane.getWidth(), pane.getHeight()-topPanel.getHeight()-bottomPanel.getHeight());
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);
		scrollPane.getVerticalScrollBar().addAdjustmentListener(bottomPanel.getButtonGroup());
		bottomPanel.setScrollPane(scrollPane);
		
		centerPane = new JLayeredPane();
		centerPane.setOpaque(false);
		centerPane.setLayout(null);
		centerPane.setLocation(0, topPanel.getHeight());
		centerPane.add(scrollPane);
		centerPane.add(symbolPanel, JLayeredPane.PALETTE_LAYER);
		
		pane.add(centerPane);
		pane.add(topPanel);
		pane.add(bottomPanel);
		pane.updateUI();
		
		canvas.initScore();
		
		setSize(DEFAULT_WIN_WIDTH, DEFAULT_WIN_HEIGHT);
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		if(getWidth() > screensize.getWidth())
			setSize((int)screensize.getWidth()-100, getHeight());
		if(getHeight() > screensize.getHeight())
			setSize(getWidth(), (int)screensize.getHeight()-100);
		
		((JComponent)getContentPane()).setBorder(BorderFactory.createLineBorder(Color.GRAY));
	}
	
	
	
	/**
	 * 重写方法，使得外框大小调整的时候组件跟着调整
	 */
	public void setSize(int x, int y){
		super.setSize(x, y);
		pane.setSize(getWidth(), getHeight()-35);
		pane.updateUI();
		topPanel.setSize(getWidth(), topPanel.getHeight());
		bottomPanel.setSize(getWidth(), bottomPanel.getHeight());
		bottomPanel.setLocation(0, getHeight() - bottomPanel.getHeight());
		bottomPanel.setLocation(0, pane.getHeight() - bottomPanel.getHeight());
		scrollPane.setSize(pane.getWidth()-15, pane.getHeight() - topPanel.getHeight()- bottomPanel.getHeight());
		centerPane.setSize(scrollPane.getWidth(), scrollPane.getHeight());
		canvas.setSize(scrollPane.getWidth()-10, scrollPane.getHeight()-5);
		symbolPanel.setLocation(getWidth()-30-symbolPanel.getWidth(), centerPane.getHeight()-symbolPanel.getHeight());
		System.out.println(pane.getHeight() + " " + topPanel.getHeight() + " " + scrollPane.getHeight() + " " + 
				bottomPanel.getHeight());
		System.out.println("canvas:" + canvas.getWidth() + "  " + canvas.getHeight());
	}
	
	
	public static void main(String args[]){
		new Main_Applet();
	}

}
