package sjy.elwg.notation;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import sjy.elwg.notation.musicBeans.Note;
import sjy.elwg.notation.musicBeans.Selectable;


/**
 * @author tequila
 *
 */
public class SymbolPanel extends JPanel implements ActionListener{
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -1727740841082299674L;
	
	/**
	 * 大小按钮大小
	 */
	private static final int SHORT_WIDTH0 = 20;
	private static final int SHORT_HEIGHT0 = 25;
	
	/**
	 * 面板所关联的画板
	 */
	private NoteCanvas canvas;

	private String symbolType;

	
	/**
	 * 每个标签的面板
	 */
	//音符面板
	private JPanel notePanel;
	//小节面板
	private JPanel measurePanel;
	//工具面板
	private JPanel toolPanel;


	//放置五个面板的选项卡
	private JTabbedPane pane;
	
	
	/**
	 * 当前所按下的音符时长
	 */
	private int curDuration = 64;
	/**
	 * 当前是否是休止符
	 */
	private boolean isRest;
	/**
	 * 当前被选择的附点个数
	 */
	private int dotNum = 0;
	
	/**
	 * 被选择的音符按钮的边界与颜色
	 */
	private Border selectedBorder = BorderFactory.createLineBorder(Color.RED);
	private Color selectedColor = Color.PINK;
	/**
	 * 模式按钮的边界
	 */
	private Border lowBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
	private Border highBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED);
	
	///////////////符号面板图标/////////////////
	ImageIcon iconWhole = new ImageIcon(("pic/whole.png"));
	ImageIcon icon2th = new ImageIcon(("pic/half.png"));
	ImageIcon icon4th = new ImageIcon(("pic/4th.png"));
	ImageIcon icon4th1 = new ImageIcon(("pic/4th.png"));
	ImageIcon icon8th = new ImageIcon(("pic/8th.png"));
	ImageIcon icon16th = new ImageIcon(("pic/16th.png"));
	ImageIcon icon32th = new ImageIcon(("pic/32th.png"));
	ImageIcon icon64th = new ImageIcon(("pic/64th.png"));
	ImageIcon iconRest = new ImageIcon(("pic/rest.png"));
	ImageIcon iconDot = new ImageIcon(("pic/onedot.png"));
	ImageIcon iconDoubleDot = new ImageIcon(("pic/doubleDot.png"));
	ImageIcon iconSharp = new ImageIcon(("pic/sharp.png"));
	ImageIcon iconFlat = new ImageIcon(("pic/flat.png"));
	ImageIcon iconDoubleSharp = new ImageIcon(("pic/doubleSharp.png"));
	ImageIcon iconDoubleFlat = new ImageIcon(("pic/doubleFlat.png"));
	ImageIcon iconNatural = new ImageIcon(("pic/natural.png"));
	ImageIcon iconTie = new ImageIcon(("pic/tie.png"));
	ImageIcon iconBracket = new ImageIcon(("pic/bracket.png"));
	ImageIcon iconTuplet = new ImageIcon(("pic/tuplet.png"));
	
	///////////////符号面板图标/////////////////
	ImageIcon iconKey = new ImageIcon(("pic/key.png"));
	ImageIcon iconTime = new ImageIcon(("pic/time.png"));
	ImageIcon iconClef = new ImageIcon(("pic/clef.png"));
	ImageIcon iconBarline = new ImageIcon(("pic/barline.png"));
	ImageIcon iconMeasureMark = new ImageIcon(("pic/measureMark.png"));
	ImageIcon iconAddMeasure = new ImageIcon(("pic/addMeasure.png"));
	ImageIcon iconDeleteMeasure = new ImageIcon(("pic/deleteMeasure.png"));
	
	///////////////工具面板图标/////////////////
	ImageIcon iconGraceSymbol = new ImageIcon(("pic/graceSymbol.png"));
	ImageIcon iconOrnament = new ImageIcon(("pic/ornament.png"));
	ImageIcon iconEllipsis = new ImageIcon(("pic/ellipsis.png"));
	ImageIcon iconDynamics = new ImageIcon(("pic/dynamics.png"));
	ImageIcon iconPerformance = new ImageIcon(("pic/performance.png"));
	ImageIcon iconTempo = new ImageIcon(("pic/tempo.png"));
	ImageIcon iconSymbolLine = new ImageIcon(("pic/symbolLine.png"));
	
    ///////////////模式按钮、删除按钮图标、回撤、前进按钮/////////////////
	ImageIcon iconMode = new ImageIcon(("pic/mode.png"));
	ImageIcon iconDelete = new ImageIcon(("pic/delete.png"));
	ImageIcon iconBack = new ImageIcon(("pic/backButton.png"));
	ImageIcon iconForward = new ImageIcon(("pic/forwardButton.png"));
	

	//音符面板按钮
	private JButton btFulln = new JButton(iconWhole);
	private JButton btHalfn = new JButton(icon2th);
	private JButton btQuartern = new JButton(icon4th);
	private JButton bt8thn = new JButton(icon8th);
	private JButton bt16thn = new JButton(icon16th);
	private JButton bt32thn = new JButton(icon32th);
	private JButton bt64thn = new JButton(icon64th);	
	private JButton btRest = new JButton(iconRest);
	private JButton btNatural = new JButton(iconNatural);
	private JButton btSharp = new JButton(iconSharp);
	private JButton btFlat = new JButton(iconFlat);
	private JButton btDoubleSharp = new JButton(iconDoubleSharp);
	private JButton btDoubleFlat = new JButton(iconDoubleFlat);
	private JButton btDot1 = new JButton(iconDot);
	private JButton btDot2 = new JButton(iconDoubleDot);
	private JButton btBracket = new JButton(iconBracket);
	private JButton btTie = new JButton(iconTie);
	private JButton btTuplet = new JButton(iconTuplet);
	
	//小节面板按钮
	private JButton btKey = new JButton(iconKey);
	private JButton btTime = new JButton(iconTime);
	private JButton btClef = new JButton(iconClef);
	private JButton btBarline = new JButton(iconBarline);
	private JButton btMeasureMark = new JButton(iconMeasureMark);
	private JButton btAddMeasure = new JButton(iconAddMeasure);
	private JButton btDeleteMeasure = new JButton(iconDeleteMeasure);
	
	
	//模式选择按钮
	private JButton btMode = new JButton(iconMode);

	//删除按钮
	private JButton btDelete = new JButton(iconDelete);
	//private JMenuBar menuBar;
	
	//回撤按钮
	private JButton btBack = new JButton(iconBack);
	
	//前进按钮
	private JButton btForward = new JButton(iconForward);
	
	//声部按钮
	private JButton btVoice0 = new JButton();
	private JButton btVoice1 = new JButton();
	
	//工具面板按钮
	private JButton btGraceSymbol = new JButton(iconGraceSymbol);
	private JButton btOrnament = new JButton(iconOrnament);
	private JButton btEllipsis = new JButton(iconEllipsis);
	private JButton btDynamics = new JButton(iconDynamics);
	private JButton btPerformance = new JButton(iconPerformance);
	private JButton btTempo = new JButton(iconTempo);
	private JButton btSymbolLine = new JButton(iconSymbolLine);
	private JButton btText = new JButton("T");

	
	/**
	 * 默认音符按钮的边界与颜色
	 */
	private Border defaultBorder = btFulln.getBorder();
	private Color defaultColor = btFulln.getBackground();

	
	/**
	 * 构造函数
	 */
	public SymbolPanel(Map<String, Integer> map){
		super();
		setSize(136,190);
		
		//模式按钮（鼠标按钮）外观设置
		btMode.setBorder(this.highBorder);
		btMode.addActionListener(this);
		btMode.setSize(30, 25);
		btMode.setLocation(5, getHeight()-btMode.getHeight()-8);
		add(btMode);

		//删除按钮外观设置
	//	btDelete.setBorder(this.highBorder);
		btDelete.addActionListener(this);
		btDelete.setSize(30, 25);
		btDelete.setLocation(37, getHeight()-btDelete.getHeight()-8);
		add(btDelete);
		
		//回撤按钮外观设置
		btBack.addActionListener(this);
		btBack.setSize(30, 25);
		btBack.setLocation(69, getHeight()-btDelete.getHeight()-8);
		add(btBack);
		

		//前进按钮外观设置
		btForward.addActionListener(this);
		btForward.setSize(30, 25);
		btForward.setLocation(101, getHeight()-btDelete.getHeight()-8);
		add(btForward);
		
		btVoice0.addActionListener(this);
		btVoice1.addActionListener(this);
		btVoice0.setSize(20, 10);
		btVoice1.setSize(20, 10);
	//	btVoice0.setLocation(70, getHeight()-btVoice0.getHeight()*2-4);
	//	btVoice1.setLocation(70, getHeight()-btVoice1.getHeight()-2);
		btVoice0.setBackground(Color.blue);
		btVoice1.setBackground(Color.green);
		btVoice0.setBorder(selectedBorder);
//		add(btVoice0);
//		add(btVoice1);

		pane = new JTabbedPane(JTabbedPane.TOP);
		UserTabbedPaneUI paneUI = new UserTabbedPaneUI();
		pane.setUI(paneUI);
		pane.setLayout(null);
		pane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT );
		pane.setBorder(BorderFactory.createLineBorder(Color.PINK));
		
		notePanel = new JPanel();
		measurePanel = new JPanel();
		toolPanel = new JPanel();

		pane.addTab("音符",notePanel);
		pane.addTab("小节",measurePanel);
		pane.addTab("工具", toolPanel);

		add(pane);
		pane.setLocation(4, 2);
		pane.setSize(getWidth()-8, 150);
		pane.repaint();
		
		setLayout(null);
		setVisible(true);
		initPanels(map);
		//setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.LIGHT_GRAY, Color.GRAY));
		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		addFunctionListener(this);
		determineCurDuration();
	}
	
	public void initPanels(Map<String, Integer> map){
		
		//面板配置对应
		if(map.get("whole-note")!= null && map.get("whole-note") == 0)
			btFulln.setEnabled(false);
		if(map.get("half-note")!= null && map.get("half-note") == 0)
			btHalfn.setEnabled(false);
		if(map.get("quarter-note")!= null && map.get("quarter-note") == 0)
			btQuartern.setEnabled(false);
		if(map.get("note-8th")!= null && map.get("note-8th") == 0)
			bt8thn.setEnabled(false);
		if(map.get("note-16th")!= null && map.get("note-16th") == 0)
			bt16thn.setEnabled(false);
		if(map.get("note-32th")!= null && map.get("note-32th") == 0)
			bt32thn.setEnabled(false);
		if(map.get("note-64th")!= null && map.get("note-64th") == 0)
			bt64thn.setEnabled(false);
		if(map.get("rest")!= null && map.get("rest") == 0)
			btRest.setEnabled(false);
		if(map.get("single-dot")!= null && map.get("single-dot") == 0)
			btDot1.setEnabled(false);
		if(map.get("double-dot")!= null && map.get("double-dot") == 0)
			btDot2.setEnabled(false);
		if(map.get("sharp")!= null && map.get("sharp") == 0)
			btSharp.setEnabled(false);
		if(map.get("double-sharp")!= null && map.get("double-sharp") == 0)
			btDoubleSharp.setEnabled(false);
		if(map.get("flat")!= null && map.get("flat") == 0)
			btFlat.setEnabled(false);
		if(map.get("double-flat")!= null && map.get("double-flat") == 0)
			btDoubleFlat.setEnabled(false);
		if(map.get("natural")!= null && map.get("natural") == 0)
			btNatural.setEnabled(false);
		if(map.get("tie")!= null && map.get("tie") == 0)
			btTie.setEnabled(false);
		if(map.get("tuplet")!= null && map.get("tuplet") == 0)
			btTuplet.setEnabled(false);
		if(map.get("stress")!= null && map.get("stress") == 0)
			btBracket.setEnabled(false);
		
		//添加属于音乐面板的按钮，设置位置
		notePanel.setLayout(null);
		//notePanel.setBackground(Color.WHITE);	
		notePanel.add(btFulln);
		btFulln.setBackground(Color.white);
		notePanel.add(btHalfn);
		btHalfn.setBackground(Color.white);
		notePanel.add(btQuartern);
		btQuartern.setBackground(Color.white);
		notePanel.add(bt8thn);
		bt8thn.setBackground(Color.white);
		notePanel.add(bt16thn);
		bt16thn.setBackground(Color.white);
		notePanel.add(bt32thn);
		bt32thn.setBackground(Color.white);
		notePanel.add(bt64thn);
		bt64thn.setBackground(Color.white);
		notePanel.add(btRest);
		btRest.setBackground(Color.white);
		notePanel.add(btDot1);
		btDot1.setBackground(Color.white);
		notePanel.add(btDot2);
		btDot2.setBackground(Color.white);
		
		notePanel.add(btNatural);
		btNatural.setBackground(Color.white);		
		notePanel.add(btSharp);
		btSharp.setBackground(Color.white);		
		notePanel.add(btDoubleSharp);
		btDoubleSharp.setBackground(Color.white);		
		notePanel.add(btFlat);
		btFlat.setBackground(Color.white);		
		notePanel.add(btDot1);
		btDoubleFlat.setBackground(Color.white);		
		notePanel.add(btDoubleFlat);
		btDoubleFlat.setBackground(Color.white);	
		
		//notePanel.add(btBracket);
		btBracket.setBackground(Color.white);
		
		notePanel.add(btTie);
		btTie.setBackground(Color.white);
		
		notePanel.add(btTuplet);
		btTuplet.setBackground(Color.LIGHT_GRAY);
		
		notePanel.add(btVoice0);
		notePanel.add(btVoice1);

		
		int x = 3; int y = 5;
		btFulln.setBounds(x, y, SHORT_WIDTH0, SHORT_HEIGHT0);
		x += SHORT_WIDTH0+4;
		btHalfn.setBounds(x, y, SHORT_WIDTH0, SHORT_HEIGHT0);
		x += SHORT_WIDTH0+4;
		btQuartern.setBounds(x, y, SHORT_WIDTH0, SHORT_HEIGHT0);
		x += SHORT_WIDTH0+4;
		bt8thn.setBounds(x, y, SHORT_WIDTH0, SHORT_HEIGHT0);
		x += SHORT_WIDTH0+4;		
		bt16thn.setBounds(x, y, SHORT_WIDTH0, SHORT_HEIGHT0);
		x += SHORT_WIDTH0+4;
		x =3; y+=SHORT_HEIGHT0+3 ;
		bt32thn.setBounds(x, y, SHORT_WIDTH0, SHORT_HEIGHT0);
		x += SHORT_WIDTH0+4;
		bt64thn.setBounds(x, y, SHORT_WIDTH0, SHORT_HEIGHT0);
		x += SHORT_WIDTH0+4;
		btRest.setBounds(x, y, SHORT_WIDTH0, SHORT_HEIGHT0);
		x += SHORT_WIDTH0+4;
		btDot1.setBounds(x, y, SHORT_WIDTH0, SHORT_HEIGHT0);
		x += SHORT_WIDTH0+4;		
		btDot2.setBounds(x, y, SHORT_WIDTH0, SHORT_HEIGHT0);
		x += SHORT_WIDTH0+4;
		x=3; y = SHORT_HEIGHT0 *2+12;
		btNatural.setBounds(x, y, SHORT_WIDTH0, SHORT_HEIGHT0);
		x += SHORT_WIDTH0+4;
		btSharp.setBounds(x, y, SHORT_WIDTH0, SHORT_HEIGHT0);
		x += SHORT_WIDTH0+4;
		btDoubleSharp.setBounds(x, y, SHORT_WIDTH0, SHORT_HEIGHT0);
		x += SHORT_WIDTH0+4;
		btFlat.setBounds(x, y, SHORT_WIDTH0, SHORT_HEIGHT0);
		x += SHORT_WIDTH0+4;
		btDoubleFlat.setBounds(x, y, SHORT_WIDTH0, SHORT_HEIGHT0);
		x += SHORT_WIDTH0+4;
		x=3; y = SHORT_HEIGHT0 *3+15;
	//	btBracket.setBounds(x, y, SHORT_WIDTH0, SHORT_HEIGHT0);
	//	x += SHORT_WIDTH0+4;
		btTie.setBounds(x, y, SHORT_WIDTH0, SHORT_HEIGHT0);
		x += SHORT_WIDTH0+4;
		btTuplet.setBounds(x, y, SHORT_WIDTH0*2+4, SHORT_HEIGHT0);
		x += SHORT_WIDTH0 * 3 + 3;
		
		btVoice0.setBounds(x, y, SHORT_WIDTH0 * 3 / 2, SHORT_HEIGHT0 / 2);
		y += SHORT_HEIGHT0 / 2 + 3;
		btVoice1.setBounds(x, y, SHORT_WIDTH0 * 3 / 2 , SHORT_HEIGHT0 / 2);
		
		

		
		//小节面板符号的添加，位置的设置
		measurePanel.setLayout(null);
	//	measurePanel.setBackground(Color.WHITE);
		
		measurePanel.add(btKey);
		measurePanel.add(btTime);
		measurePanel.add(btClef);
		measurePanel.add(btBarline);
		measurePanel.add(btMeasureMark);
		measurePanel.add(btAddMeasure);
		measurePanel.add(btDeleteMeasure);
			
		x = 2; y = 6;
		btKey.setBounds(x, y, 38, 30);
		x += 40;
		btTime.setBounds(x, y, 38, 30);
		x += 40;
		btClef.setBounds(x, y, 38, 30);
		x = 2; y = 43;
		btBarline.setBounds(x, y, 58, 30);
		x += 60;
		btMeasureMark.setBounds(x, y, 58, 30);
		x = 2; y = 82;
		btAddMeasure.setBounds(x, y, 58, 30);
		x += 60;
		btDeleteMeasure.setBounds(x, y, 58, 30);
				
		//工具面板符号的添加，位置的设置
		toolPanel.setLayout(null);
		toolPanel.add(btGraceSymbol);
		toolPanel.add(btOrnament);
		toolPanel.add(btEllipsis);
		toolPanel.add(btDynamics);
		toolPanel.add(btPerformance);
		toolPanel.add(btTempo);
		toolPanel.add(btSymbolLine);
		toolPanel.add(btText);
		
		
		x = 2; y = 5;
		btGraceSymbol.setBounds(x, y, 58, 25);
		x += 60;
		btOrnament.setBounds(x, y, 58, 25);
		x = 2; y = 33;
		btEllipsis.setBounds(x, y, 58, 25);
		x += 60;
		btDynamics.setBounds(x, y, 58, 25);
		x = 2; y = 60;
		btPerformance.setBounds(x, y, 58, 25);
		x += 60;
		btTempo.setBounds(x, y, 58, 25);
		x = 2; y = 88;
		btSymbolLine.setBounds(x, y, 58, 25);
		x += 60;
		btText.setBounds(x, y, 58, 25);
		btText.setBackground(Color.LIGHT_GRAY);
	}
	
	
	/**
	 * 自定义标签面板UI，继承自BasicTabbedPaneUI
	 * @author sjy
	 *
	 */
	class UserTabbedPaneUI extends BasicTabbedPaneUI{
		public UserTabbedPaneUI(){
			super();
		}
		
		public int calculateTabHeight(int tabPlacement, int tabIndex, int fontSize){
			return fontSize;
		}
		
		public int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics){
			int width = metrics.getHeight();
			return (int)(1.8*width);
		}
		
		public void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x , int y, int w, int h, boolean isSelected){
			super.paintTabBorder(g, tabPlacement, tabIndex, x, y, w, h, isSelected);
			if(isSelected){
				g.drawLine(x-1, y, x-1, y+h);
				g.drawLine(x-1, y, x-1+w, y);
				g.drawLine(x-1+w, y, x-1+w, y+h);
			}
		}
	}
	
	/**
	 * 返回当前被选择的音符
	 * @return
	 */
	public JButton getCurrentNoteButton(){
		switch(curDuration){
		case 256: return btFulln;
		case 128: return btHalfn;
		case 64: return btQuartern;
		case 32: return bt8thn;
		case 16: return bt16thn;
		case 8: return bt32thn;
		case 4: return bt64thn;
		default: return bt64thn;
		}
	}
	
	/**
	 * 重现休止符被按下按钮特征
	 */
	public void reselectRest(){
		btRest.setBorder(selectedBorder);
		btRest.setBackground(selectedColor);
	}
	
	/**
	 * 取消休止符被按下的特征
	 */
	public void cancleRest(){
		btRest.setBorder(defaultBorder);
		btRest.setBackground(defaultColor);
	}
	
	/**
	 * 消除音符按钮的边界
	 */
	public void cancleNoteBorder(){
		JButton button = getCurrentNoteButton();
		button.setBorder(defaultBorder);
		button.setBackground(defaultColor);
	}
	
	/**
	 * 重现音符按钮边界
	 */
	public void reselectNoteBorder(){
		JButton button = getCurrentNoteButton();
		button.setBorder(selectedBorder);
		button.setBackground(selectedColor);
	}
	
	/**
	 * 取消附点音符的选择状态
	 */
	public void cancleDotBorder(){
		btDot1.setBorder(defaultBorder);
		btDot2.setBorder(defaultBorder);
		btDot1.setBackground(defaultColor);
	    btDot2.setBackground(defaultColor);
	}
	
	/**
	 * 重现附点音符的选择状态
	 */
	public void reselectDotBorder(){
		if(dotNum == 1){
			btDot1.setBorder(selectedBorder);
			btDot1.setBackground(selectedColor);
		}
		else if(dotNum == 2){
			btDot2.setBorder(selectedBorder);
	        btDot2.setBackground(selectedColor);
		}
		else if(dotNum == 0){
			cancleDotBorder();
		}
	}
	
	/**
	 * 点击事件
	 */
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//点击模式按钮后更改编辑模式，更改按钮的弹起和按下
		if(e.getSource() == btMode){
			if(btMode.getBorder() == highBorder){
				canvas.editMode();
				btMode.setBorder(lowBorder);
			}
			else if(btMode.getBorder() == lowBorder){
				canvas.viewMode();
				btMode.setBorder(highBorder);
			}
		}
		//多声部按钮
		else if(e.getSource() == btVoice0 || e.getSource() == btVoice1){
			int voice = e.getSource() == btVoice0 ? 0 : 1;
			canvas.switchVoice(voice);
			if(voice == 0){
				btVoice0.setBorder(selectedBorder);
				btVoice1.setBorder(defaultBorder);
			}else if(voice == 1){
				btVoice0.setBorder(defaultBorder);
				btVoice1.setBorder(selectedBorder);
			}
		}

		if(e.getSource() == btFulln){
			cancleNoteBorder();
			curDuration = 256;
			reselectNoteBorder();
			canvas.noteChanged();
		}else if(e.getSource() == btHalfn){
			cancleNoteBorder();
			curDuration = 128;
			reselectNoteBorder();
			canvas.noteChanged();
		}else if(e.getSource() == btQuartern){
			cancleNoteBorder();
			curDuration = 64;
			reselectNoteBorder();
			canvas.noteChanged();
		}else if(e.getSource() == bt8thn){
			cancleNoteBorder();
			curDuration = 32;
			reselectNoteBorder();
			canvas.noteChanged();
		}else if(e.getSource() == bt16thn){
			cancleNoteBorder();
			curDuration = 16;
			reselectNoteBorder();
			canvas.noteChanged();
		}else if(e.getSource() == bt32thn){
			cancleNoteBorder();
			curDuration = 8;
			reselectNoteBorder();
			canvas.noteChanged();
		}else if(e.getSource() == bt64thn){
			cancleNoteBorder();
			curDuration = 4;
			reselectNoteBorder();
			canvas.noteChanged();
		}else if(e.getSource() == btRest){
			if(canvas.getMode().equalsIgnoreCase("view"))
				return;
			if(isRest == false){
				btRest.setBorder(selectedBorder);
				btRest.setBackground(selectedColor);
				isRest = true;
			}else{
				btRest.setBorder(defaultBorder);
				btRest.setBackground(defaultColor);
				isRest = false;
			}
		}else if(e.getSource() == btDot1){
			cancleDotBorder();
			dotNum = dotNum == 1 ? 0 : 1;
			reselectDotBorder();
			canvas.dotChanged();
		}else if(e.getSource() == btDot2){
			cancleDotBorder();
			dotNum = dotNum == 2 ? 0 : 2;
			reselectDotBorder();
			canvas.dotChanged();
		}

		else if(e.getSource() == btTie){
			canvas.tieChanged();
		}
		else if(e.getSource() == btDelete){
			canvas.deleteSomething();
		}

		else if(e.getSource() == btSharp){
			canvas.sharpOrFlatChanged("sharp");
		}else if(e.getSource() == btFlat){
			canvas.sharpOrFlatChanged("flat");
		}else if(e.getSource() == btNatural){
			canvas.sharpOrFlatChanged("natural");
		}else if(e.getSource() == btDoubleSharp){
			canvas.sharpOrFlatChanged("double-sharp");
		}else if(e.getSource() == btDoubleFlat){
			canvas.sharpOrFlatChanged("double-flat");
		}
		
		else if(e.getSource() == btBack){
			canvas.getController().undo();
		}else if(e.getSource() == btForward){
			canvas.getController().redo();
		}
	}	
	
	/**
	 * 根据被选择的对象刷新面板按钮状态
	 * @param s 被选择的对象，通常是音符
	 */
	public void refreshButtonStatus(Selectable s){
		if(s instanceof Note){
			Note note = (Note)s;
			cancleNoteBorder();
			curDuration = note.getDuration();
			reselectNoteBorder();
			if(note.isRest()){
				btRest.setBorder(selectedBorder);
				btRest.setBackground(selectedColor);
			}
			else {
				btRest.setBorder(defaultBorder);
				btRest.setBackground(defaultColor);
			}
			dotNum = note.getDotNum();
			cancleDotBorder();
			reselectDotBorder();
		}
	}
	
	public String getSymbols() {
		return symbolType;
	}

	public void setSymbols(String symbols) {
		this.symbolType = symbols;
	}

	
	public void addSharpOrFlatListener(ActionListener listener){
		btSharp.addActionListener(listener);
		btFlat.addActionListener(listener);
		btDoubleSharp.addActionListener(listener);
		btDoubleFlat.addActionListener(listener);
		btNatural.addActionListener(listener);
	}
	
	public void removeSharpOrFlatListener(ActionListener listener){
		btSharp.removeActionListener(listener);
		btFlat.removeActionListener(listener);
		btDoubleSharp.removeActionListener(listener);
		btDoubleFlat.removeActionListener(listener);
		btNatural.removeActionListener(listener);
	}

	public JButton getBtTie() {
		return btTie;
	}

	
	public void addTieListener(ActionListener listener){
		btTie.addActionListener(listener);
	}
	
	public void removeTieListener(ActionListener listener){
		btTie.removeActionListener(listener);
	}
	
	public void addNoteListener(ActionListener listener){
		btFulln.addActionListener(listener);
		btQuartern.addActionListener(listener);
		btHalfn.addActionListener(listener);
		bt8thn.addActionListener(listener);
		bt16thn.addActionListener(listener);
		bt32thn.addActionListener(listener);
		bt64thn.addActionListener(listener);
		btTuplet.addActionListener(listener);
		btTie.addActionListener(listener);
	}
	
	public void removeNoteListener(ActionListener listener){
		btFulln.removeActionListener(listener);
		btQuartern.removeActionListener(listener);
		btHalfn.removeActionListener(listener);
		bt8thn.removeActionListener(listener);
		bt16thn.removeActionListener(listener);
		bt32thn.removeActionListener(listener);
		bt64thn.removeActionListener(listener);
		btTuplet.removeActionListener(listener);
		btTie.addActionListener(listener);
	}
	public void addMeasureSymbolListener(ActionListener listener){
		btKey.addActionListener(listener);
		btTime.addActionListener(listener);
		btClef.addActionListener(listener);
		btBarline.addActionListener(listener);
		btMeasureMark.addActionListener(listener);
		btAddMeasure.addActionListener(listener);
		btDeleteMeasure.addActionListener(listener);
	}
	public void removeMeasureSymbolListener(ActionListener listener){
		btKey.removeActionListener(listener);
		btTime.removeActionListener(listener);
		btKey.removeActionListener(listener);
		btBarline.removeActionListener(listener);
		btMeasureMark.removeActionListener(listener);
		btAddMeasure.removeActionListener(listener);
		btDeleteMeasure.addActionListener(listener);
	}
	
	public void addToolListener(ActionListener listener){		
		btGraceSymbol.addActionListener(listener);
		btOrnament.addActionListener(listener);
		btEllipsis.addActionListener(listener);
		btDynamics.addActionListener(listener);
		btPerformance.addActionListener(listener);
		btTempo.addActionListener(listener);
		btSymbolLine.addActionListener(listener);
		btText.addActionListener(listener);
	}
	
	public void removeToolListener(ActionListener listener){
		btGraceSymbol.removeActionListener(listener);
		btOrnament.removeActionListener(listener);
		btEllipsis.removeActionListener(listener);
		btDynamics.removeActionListener(listener);
		btPerformance.removeActionListener(listener);
		btTempo.removeActionListener(listener);
		btSymbolLine.removeActionListener(listener);
		btText.removeActionListener(listener);
	}
	public void addSymbolsListener(ActionListener listener){
		btTuplet.addActionListener(listener);
	}
	
	public void removeSymbolsListener(ActionListener listener){	
		btTuplet.removeActionListener(listener);
	}
	
	public JButton getBtTuplet() {
		return btTuplet;
	}

	public void setBtTuplet(JButton btTuplet) {
		this.btTuplet = btTuplet;
	}

	//添加符杠连接符号监听器
	public void addNoteConnectionListener(ActionListener listener){
//		btStart.addActionListener(listener);
//		btContinue.addActionListener(listener);
//		btEnd.addActionListener(listener);
//		btNone.addActionListener(listener);
	}
	//删除符杠连接符号监听器
	public void removeNoteConnectionListener(ActionListener listener){
//		btStart.removeActionListener(listener);
//		btContinue.removeActionListener(listener);
//		btEnd.removeActionListener(listener);
//		btNone.removeActionListener(listener);
	}
	
	public void addStrengthListener(ActionListener listener){
//		p.addActionListener(listener);
//		pp.addActionListener(listener);
//		ppp.addActionListener(listener);
//		mp.addActionListener(listener);
//		f.addActionListener(listener);
//		ff.addActionListener(listener);
//		fff.addActionListener(listener);
//		mf.addActionListener(listener);
//		cresc.addActionListener(listener);
//		dim.addActionListener(listener);
	}
	
	public void removeStrengthListener(ActionListener listener){
//		p.removeActionListener(listener);
//		pp.removeActionListener(listener);
//		ppp.removeActionListener(listener);
//		mp.removeActionListener(listener);
//		f.removeActionListener(listener);
//		ff.removeActionListener(listener);
//		fff.removeActionListener(listener);
//		mf.removeActionListener(listener);
//		cresc.removeActionListener(listener);
//		dim.removeActionListener(listener);
	}
	
	public void addDotListener(ActionListener listener){
		btDot1.addActionListener(listener);
		btDot2.addActionListener(listener);
	}
	
	public void addDeleteListener(ActionListener listener){
		btDelete.addActionListener(listener);
	}
	
	public void addBeamListener(ActionListener listener){
//		btStart.addActionListener(listener);
//		btContinue.addActionListener(listener);
//		btEnd.addActionListener(listener);
//		btNone.addActionListener(listener);
	}
	
	public void addOtherListener(ActionListener listener){
		btTuplet.addActionListener(listener);
//		btRest.addActionListener(listener);
//		btGraceNote.addActionListener(listener);
////		btGrace8s.addActionListener(listener);
////		btGrace4.addActionListener(listener);
////		btGrace16.addActionListener(listener);
////		btGrace32.addActionListener(listener);
//		btTempo.addActionListener(listener);
	}


	public int getCurDuration() {
		return curDuration;
	}

	public void setCurDuration(int curDuration) {
		this.curDuration = curDuration;
	}

	public boolean isRest() {
		return isRest;
	}

	public void setRest(boolean isRest) {
		this.isRest = isRest;
	}

	public int getDotNum() {
		return dotNum;
	}

	public void setDotNum(int dotNum) {
		this.dotNum = dotNum;
	}

	public JButton getBtFulln() {
		return btFulln;
	}

	public JButton getBtHalfn() {
		return btHalfn;
	}

	public JButton getBtQuartern() {
		return btQuartern;
	}

	public JButton getBt8thn() {
		return bt8thn;
	}

	public JButton getBt16thn() {
		return bt16thn;
	}

	public JButton getBt32thn() {
		return bt32thn;
	}

	public JButton getBt64thn() {
		return bt64thn;
	}

	public JButton getBtRest() {
		return btRest;
	}

	public JButton getBtDot1() {
		return btDot1;
	}

	public JButton getBtDot2() {
		return btDot2;
	}

	public JButton getBtSharp() {
		return btSharp;
	}

	public JButton getBtFlat() {
		return btFlat;
	}

	public JButton getBtDoubleSharp() {
		return btDoubleSharp;
	}

	public JButton getBtDoubleFlat() {
		return btDoubleFlat;
	}

	public JButton getBtNatural() {
		return btNatural;
	}

	public JButton getBtDelete() {
		return btDelete;
	}

	public NoteCanvas getCanvas() {
		return canvas;
	}

	public void setCanvas(NoteCanvas canvas) {
		this.canvas = canvas;
	}

	/**
	 * 为悬浮面板添加功能侦听器
	 * 所谓功能侦听器，即该侦听器侦听面板的所有功能按钮。
	 * 通常侦听器对象是画板。
	 * @param l
	 */
	public void addFunctionListener(ActionListener l){
		this.addNoteListener(l);
		this.addSharpOrFlatListener(l);
		this.addDotListener(l);
		this.addDeleteListener(l);
	}
	
	/**
	 * 为乐谱菜单侦听器
	 * @param l
	 */
//	public void addStaffListener(ActionListener l){
//		JMenu staff = menuBar.getMenu(0);
//		Controller.addMenuListener(staff, l);
//	}
	
	/**
	 * 按照优先级获取当前被选音符类型
	 * 优先级:4 > 全 > 半 > 8 > 16 > 32 > 64
	 */
	public void determineCurDuration(){
		if(btQuartern.isEnabled())
			curDuration = 64;
		else if(btFulln.isEnabled())
			curDuration = 256;
		else if(btHalfn.isEnabled())
			curDuration = 128;
		else if(bt8thn.isEnabled())
			curDuration = 32;
		else if(bt16thn.isEnabled())
			curDuration = 16;
		else if(bt32thn.isEnabled())
			curDuration = 8;
		else if(bt64thn.isEnabled())
			curDuration = 4;
	}
	
	/**
	 * 设置编辑按钮按下去
	 */
	public void setEditPressed(){
		btMode.setBorder(lowBorder);
	}

	public JButton getBtMeasureMark() {
		return btMeasureMark;
	}

	public void setBtMeasureMark(JButton btMeasureMark) {
		this.btMeasureMark = btMeasureMark;
	}

	public JButton getBtBracket() {
		return btBracket;
	}

	public void setBtBracket(JButton btBracket) {
		this.btBracket = btBracket;
	}

	public JButton getBtKey() {
		return btKey;
	}

	public void setBtKey(JButton btKey) {
		this.btKey = btKey;
	}

	public JButton getBtTime() {
		return btTime;
	}

	public void setBtClef(JButton btClef) {
		this.btClef = btClef;
	}
	
	public JButton getBtClef() {
		return btClef;
	}

	public void setBtTime(JButton btTime) {
		this.btTime = btTime;
	}

	public JButton getBtBarline() {
		return btBarline;
	}

	public void setBtBarline(JButton btBarline) {
		this.btBarline = btBarline;
	}

	public JButton getBtAddMeasure() {
		return btAddMeasure;
	}

	public void setBtAddMeasure(JButton btAddMeasure) {
		this.btAddMeasure = btAddMeasure;
	}

	public JButton getBtDeleteMeasure() {
		return btDeleteMeasure;
	}

	public void setBtDeleteMeasure(JButton btDeleteMeasure) {
		this.btDeleteMeasure = btDeleteMeasure;
	}

	public JButton getBtGraceSymbol() {
		return btGraceSymbol;
	}

	public void setBtGraceSymbol(JButton btGraceSymbol) {
		this.btGraceSymbol = btGraceSymbol;
	}

	public JButton getBtOrnament() {
		return btOrnament;
	}

	public void setBtOrnament(JButton btOrnament) {
		this.btOrnament = btOrnament;
	}

	public JButton getBtEllipsis() {
		return btEllipsis;
	}

	public void setBtEllipsis(JButton btEllipsis) {
		this.btEllipsis = btEllipsis;
	}

	public JButton getBtDynamics() {
		return btDynamics;
	}

	public void setBtDynamics(JButton btDynamics) {
		this.btDynamics = btDynamics;
	}

	public JButton getBtPerformance() {
		return btPerformance;
	}

	public void setBtPerformance(JButton btPeromance) {
		this.btPerformance = btPeromance;
	}

	public JButton getBtTempo() {
		return btTempo;
	}

	public void setBtTempo(JButton btTempo) {
		this.btTempo = btTempo;
	}

	public JButton getBtSymbolLine() {
		return btSymbolLine;
	}

	public void setBtSymbolLine(JButton btSymbolLine) {
		this.btSymbolLine = btSymbolLine;
	}

	public JTabbedPane getPane() {
		return pane;
	}

	public JButton getBtText() {
		return btText;
	}
	
}
