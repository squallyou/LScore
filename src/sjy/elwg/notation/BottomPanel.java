package sjy.elwg.notation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * 窗口底部面板
 * @author sjy
 *
 */
public class BottomPanel extends JPanel implements ActionListener{

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 13L;
	
	/**
	 * 画板滚动窗口
	 */
	private JScrollPane scrollPane;
	
	/**
	 * 放置翻页按钮的面板
	 */
	private PageButtonGroupPanel buttonGroup;
	/**
	 * 播放按钮
	 */
	private JButton btPlay;
	/**
	 * 停止按钮
	 */
	private JButton btStop;
	
	private JLabel noteLabel;
	/**
	 * 速度栏
	 */
	private JTextField tempoField;
	
	/**
	 * 画板
	 */
	private NoteCanvas canvas;
	/**
	 * MIDI播放器
	 */
	private MIDIPlayer player;
	
	/**
	 * 构造函数
	 */
	public BottomPanel(){
		super();
		initComponents();
		locateComponents();
		setSize(1050, 25);
		setLayout(null);
	}
	
	/**
	 * 初始化组件
	 */
	public void initComponents(){
		
		buttonGroup = new PageButtonGroupPanel();
		
		btPlay = new JButton(){
			private static final long serialVersionUID = 6483239994629441083L;
			public void paintComponent(Graphics gg){
				super.paintComponent(gg);
				Graphics2D g = (Graphics2D) gg;	
				RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		        g.setRenderingHints(renderHints);
		        g.setColor(Color.red);
		        g.drawLine(5, 5, getWidth()-5, getHeight()/2);
		        g.drawLine(getWidth()-5, getHeight()/2, 5, getHeight()-5);
		        g.drawLine(5, getHeight()-5, 5, 5);
			}
		};
		btStop = new JButton(){
			private static final long serialVersionUID = -4552726189287496211L;
			public void paintComponent(Graphics gg){
				super.paintComponent(gg);
				Graphics2D g = (Graphics2D) gg;	
				RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		        g.setRenderingHints(renderHints);
		        g.setColor(Color.red);
		        g.drawRect(5, 5, getWidth()-10, getHeight()-10);
			}
		};
		noteLabel = new JLabel("tempo:");
		tempoField = new JTextField("120");
		
		add(buttonGroup);
		add(btPlay);
		add(btStop);
		add(noteLabel);
		add(tempoField);
		
		btPlay.addActionListener(this);
		btStop.addActionListener(this);
	}
	
	/**
	 * 放置组件位置
	 */
	public void locateComponents(){
		int xx = (getWidth() - buttonGroup.getWidth()) / 2 - 50;
		buttonGroup.setLocation(xx, 0);
		
		int x = 3;
		int y = 0;
		btPlay.setLocation(x, y);
		btPlay.setSize(30, 20);
		x += btPlay.getWidth()+1;
		btStop.setLocation(x, y);
		btStop.setSize(30, 20);
		x += btStop.getWidth()+10;
		noteLabel.setLocation(x, y);
		noteLabel.setSize(50, 20);
		x += noteLabel.getWidth()+10;
		tempoField.setLocation(x, y);
		tempoField.setSize(50, 20);
	}
	
	public void setSize(int x, int y){
		super.setSize(x, y);
		locateComponents();
	}

	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	public void setScrollPane(JScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}

	public PageButtonGroupPanel getButtonGroup(){
		return buttonGroup;
	}

	public NoteCanvas getCanvas() {
		return canvas;
	}

	public void setCanvas(NoteCanvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btPlay){
			if(player != null)
				player.interrupt();
			player = new MIDIPlayer(canvas);
			int tempo = Integer.parseInt(tempoField.getText());
			player.setQuarterNoteTick(tempo);
			player.start();
		}
		else if(e.getSource() == btStop){
			if(player != null)
				player.interrupt();
			canvas.stopPlaying();
		}
	}

}
