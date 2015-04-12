package sjy.elwg.notation;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import sjy.elwg.notation.action.AddMeasureAction;
import sjy.elwg.notation.action.ChangeMeaClefAction;
import sjy.elwg.notation.action.ChangeMeaKeyAction;
import sjy.elwg.notation.action.DelMeasureAction;
import sjy.elwg.notation.musicBeans.AbstractNote;
import sjy.elwg.notation.musicBeans.Annotation;
import sjy.elwg.notation.musicBeans.Barline;
import sjy.elwg.notation.musicBeans.Beam;
import sjy.elwg.notation.musicBeans.ChordGrace;
import sjy.elwg.notation.musicBeans.ChordNote;
import sjy.elwg.notation.musicBeans.FreeAddedText;
import sjy.elwg.notation.musicBeans.Gracable;
import sjy.elwg.notation.musicBeans.Grace;
import sjy.elwg.notation.musicBeans.GraceSymbol;
import sjy.elwg.notation.musicBeans.LineMarker;
import sjy.elwg.notation.musicBeans.Lyrics;
import sjy.elwg.notation.musicBeans.Measure;
import sjy.elwg.notation.musicBeans.MeasurePart;
import sjy.elwg.notation.musicBeans.Note;
import sjy.elwg.notation.musicBeans.NoteLine;
import sjy.elwg.notation.musicBeans.Page;
import sjy.elwg.notation.musicBeans.RepeatSymbol;
import sjy.elwg.notation.musicBeans.Score;
import sjy.elwg.notation.musicBeans.Selectable;
import sjy.elwg.notation.musicBeans.SharpOrFlat;
import sjy.elwg.notation.musicBeans.Stem;
import sjy.elwg.notation.musicBeans.Tail;
import sjy.elwg.notation.musicBeans.TempoText;
import sjy.elwg.notation.musicBeans.Time;
import sjy.elwg.notation.musicBeans.TremoloBeam;
import sjy.elwg.notation.musicBeans.Tuplet;
import sjy.elwg.notation.musicBeans.UIClef;
import sjy.elwg.notation.musicBeans.UIKey;
import sjy.elwg.notation.musicBeans.symbolLines.AbstractLine;
import sjy.elwg.notation.musicBeans.symbolLines.Bracket;
import sjy.elwg.notation.musicBeans.symbolLines.Breath;
import sjy.elwg.notation.musicBeans.symbolLines.Dynamic;
import sjy.elwg.notation.musicBeans.symbolLines.LineFactory;
import sjy.elwg.notation.musicBeans.symbolLines.Ornament;
import sjy.elwg.notation.musicBeans.symbolLines.Pedal;
import sjy.elwg.notation.musicBeans.symbolLines.PerformanceSymbol;
import sjy.elwg.notation.musicBeans.symbolLines.RepeatEnding;
import sjy.elwg.notation.musicBeans.symbolLines.RepeatLine;
import sjy.elwg.notation.musicBeans.symbolLines.Slur;
import sjy.elwg.notation.musicBeans.symbolLines.SymbolLine;
import sjy.elwg.notation.musicBeans.symbolLines.Tie;
import sjy.elwg.utility.ActionHistoryController;
import sjy.elwg.utility.ConstantInterface;
import sjy.elwg.utility.Controller;
import sjy.elwg.utility.MeaPartStrategy;
import sjy.elwg.utility.MusicFont;
import sjy.elwg.utility.MusicMath;
import sjy.elwg.utility.NoteLineStrategy;
import sjy.elwg.utility.PageStrategy;

/**
 * 画板类，乐谱所在的容器。负责乐谱的呈现、排版等功能，是程序最重要的类之一.
 * 注意：所有与重绘有关的方法，都是按规则对对象进行了重新排列，而并非对象的消除与重新生成.(符杠、符杆除外)
 * @author jingyuan.sun
 *
 */
public class NoteCanvas extends JLayeredPane implements MouseMotionListener, MouseListener, ActionListener, KeyListener, Printable, ConstantInterface{
	
	/**
	 * 默认序列号
	 */
	private static final long serialVersionUID = 2L;
    /**
     * 谱表内相邻线间距
     */
	public static final int LINE_GAP = 10;
	/**
	 * 两音符的最小间隔
	 */
	public static final int X_MIN_DIST = 5;
	/**
	 * 符杆长度
	 */
	public static final int STEM_LENGTH = 35;

	/**
	 * 全局字体
	 */
	public static Font MCORE_FONT = MusicFont.getMScore();
	public static Font MUSICAL_FONT = MusicFont.getMusicSymbol();
	public static Font MSCORE1_FONT = MusicFont.getMScore1();
	public static Font FREESERIF = MusicFont.getFreeSerif();
	public static Font GARBRIOLA = MusicFont.getGabriola();
	
	/**
	 * 左右拖动的光标
	 */
	private Cursor moveCursor;
	/**
	 * 正常光标
	 */
	private Cursor defaultCursor;
	/**
	 * 上下拖动的光标
	 */
	private Cursor upDownCursor; 
	/**
	 * 乐谱
	 */
	private Score score;
	/**
	 * 鼠标全屏坐标
	 */
	private Point screenPoint;
	/**
	 * 画板控制器
	 */
	private Controller controller;
	/**
	 * 虚线
	 */
	private DashedLine dashedLine;
	/**
	 * 顶部面板
	 */
	private TopPanel topPanel;
	/**
	 * 底部面板
	 */
	private BottomPanel bottomPanel;
	/**
	 * 符号面板
	 */
	private SymbolPanel symbolPanel;

	
	/**
	 * 当前乐谱中被选择的对象集合
	 */
	private ArrayList<Selectable> selectedObjects;   //Selectable 是一个接口
	//构造一个包含指定 Selectable 的元素的列表，这些元素是按照该 Selectable 的迭代器返回它们的顺序排列的。
	
	/**
	 * 画板上面的透明层
	 */
	private Veil veil;
	/**	
	 * 编辑时用于指示编辑位置的音符	   		
	 */
	private Note editNote;
	/**
	 * 当前画板的模式，有效值为"view"和"edit",分别表示视图状态与编辑状态
	 */
	private String mode = "view";
	/**
	 * 当前声部，当前支持两个声部，因此有效值为0和1.
	 */
	private int voice = 0;
	/**
	 * 将乐谱音符按照时间先后顺序排列的串行音符序列，通常用于播放
	 */
	private List<List<MeasurePart.NListWithMeaIndex>> noteSequence;
	/**
	 * 当前正在播放的音符在串型音符序列中的位置
	 */
	private int playingNoteIndex;
	/**
	 * 用于指示播放进度的线条
	 */
	private JPanel pointer;
	
	/**
	 * 当前画板的两个可连接对象的坐标.
	 */
	private Point connBeginPoint = new Point();
	private Point connEndPoint = new Point();

	/**
	 * 操作控制器
	 */
	private ActionHistoryController actionController = ActionHistoryController.getInstance();
	

	
	/**
	 * 构造函数
	 * @param topPanel
	 * @param bottomPanel
	 */
	public NoteCanvas(TopPanel topPanel, BottomPanel bottomPanel, SymbolPanel symbolPanel){
		super();
		this.topPanel = topPanel;
		this.bottomPanel = bottomPanel;
		this.symbolPanel = symbolPanel;
		this.symbolPanel.addFunctionListener(this);
		this.symbolPanel.addMeasureSymbolListener(this);
		this.symbolPanel.addToolListener(this);
	//	this.symbolPanel.addStaffListener(this);
		setBackground(Color.LIGHT_GRAY);
		setSize(Page.PAGE_WIDTH, Page.PAGE_HEIGHT);
		setLayout(null);
		setOpaque(true);
		screenPoint = new Point();
		selectedObjects = new ArrayList<Selectable>();
		controller = new Controller(this);
		moveCursor = Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
		defaultCursor = Cursor.getDefaultCursor();
		upDownCursor = Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
		dashedLine = new DashedLine();
		dashedLine.setVisible(false);
		
		veil = new Veil(this);
		veil.setSize(getWidth(), getHeight());
		add(veil, JLayeredPane.PALETTE_LAYER);
		veil.setOpaque(false);
		veil.setVisible(false);
		veil.addMouseListener(this);
		veil.addMouseMotionListener(this);
//		addKeyListener(this);
		
		editNote = new Note(64, 0);
		editNote.setSelected(true);
		editNote.setVoice(this.voice);
		editNote.repaint();
		editNote.setVisible(false);
		
		score = new Score();
	}
	
	/**
	 * 初始化乐谱
	 */
	public void initScore(){
		InputStream stream = null;
		try{
			stream = new FileInputStream("config/example.xml");
		}catch(Exception e){
			e.printStackTrace();
		}
		importXML(stream);
	}
	
	/**
	 * 由输入数据流生成乐谱
	 * @param stream
	 */
	public void importXML(InputStream stream){
		XMLParser parser = new XMLParser(stream, controller);//parser 解析器
		parser.readFromXML();
		int pageNow = parser.getScore().getPageList().size();
		int pageBefore = 0;
		if(score != null)
			pageBefore = score.getPageList().size();
		pageBefore = pageBefore <= 0 ? 1 : pageBefore;
		getBottomPanel().getButtonGroup().refreshPageButton(pageBefore, pageNow);
		disposeAll();
		score = parser.getScore();
		rearangePages(0);
		for(int i = 0; i < score.getPageList().size(); i++){
			Page page = score.getPageList().get(i);
			add(page);
			MeaPartStrategy mpst = new MeaPartStrategy(X_MIN_DIST, false);
			NoteLineStrategy nls = new NoteLineStrategy(mpst, false);
			PageStrategy ps = new PageStrategy(nls, true);
			redrawPage(page, ps);
		}
		redrawSymbolLinesInScore();
		redrawRepeatLineInScore();
		redrawFreeTextInScore();
		updateUI();
	}
	
	/**
	 * 创建空白乐谱
	 * @param scoreType 乐谱类型
	 */
	public void newScore(int scoreType, boolean hasTitle){
		if(scoreType == Score.SCORE_NORMAL){
			score.setScoreType(Score.SCORE_NORMAL);
			disposeAll();
			Page page = new Page();
			score.getPageList().add(page);
			page.setScore(score);
			page.addMouseListener(this);
			NoteLine line = new NoteLine();
			page.getNoteLines().add(line);
			line.setPage(page);
			if(hasTitle){
				page.generateTitleBox();
			}
			for(int i = 0; i < 4; i++){
				MeasurePart meaPart = new MeasurePart();
				Barline barline = null;
				if(i == 3){
					barline = new Barline("light-heavy");
				}else{
					barline = new Barline("regular");
				}
				meaPart.setBarline(barline);
				barline.setMeaPart(meaPart);
				page.add(barline);
				barline.addMouseListener(this);
				barline.addMouseMotionListener(this);
				
				line.getMeaPartList().add(meaPart);
				meaPart.setNoteLine(line);
				for(int j = 0; j < 2; j++){
					Measure measure = new Measure("g/2", 0, 4, 4);
					if(j == 0){
						measure.setInstrument("0");
					}
					meaPart.addMeasure(measure);
					page.add(measure);
					measure.addMouseListener(this);
					measure.addMouseMotionListener(this);
					Note note = new Note(MusicMath.getMeasureDuration(measure), true);
					measure.addNote(note, 0);
					page.add(note);
					note.addMouseListener(this);
					note.addMouseMotionListener(this);
				}
			}
			line.generateBrackets();
			line.generateFrontBarlineLine();
			line.generateMarkers();
			line.addMarkerListener(this);
		}
		else if(scoreType == Score.SCORE_UNLMTED){
			score.setScoreType(Score.SCORE_UNLMTED);
			disposeAll();
			Page page = new Page();
			score.getPageList().add(page);
			page.setScore(score);
			page.addMouseListener(this);
			NoteLine line = new NoteLine();
			page.getNoteLines().add(line);
			line.setPage(page);
			for(int i = 0; i < 1; i++){
				MeasurePart meaPart = new MeasurePart();
				line.getMeaPartList().add(meaPart);
				meaPart.setNoteLine(line);
				Measure measure = new Measure("g/2", 0);
				measure.setInstrument("0");
				meaPart.addMeasure(measure);
				page.add(measure);
				measure.addMouseListener(this);
				measure.addMouseMotionListener(this);
				
				Barline bl = new Barline(meaPart,"regular");
				page.add(bl);
				bl.addMouseListener(this);
				bl.addMouseMotionListener(this);
			}
			line.generateBrackets();
			line.generateFrontBarlineLine();
			line.generateMarkers();
			line.addMarkerListener(this);
		}
		rearangePages(0);
		for(int i = 0; i < score.getPageList().size(); i++){
			Page page = score.getPageList().get(i);
			add(page);
			MeaPartStrategy mpst = new MeaPartStrategy(X_MIN_DIST, false);
			NoteLineStrategy nls = new NoteLineStrategy(mpst, false);
			PageStrategy ps = new PageStrategy(nls, true);
			redrawPage(page, ps);
		}
	}
	
	/**
	 * 画板内部类
	 * 拖动每行最后一个小节线时出现的虚线
	 *
	 */
	private class DashedLine extends JPanel{
		
		/**
		 * 序列号
		 */
		private static final long serialVersionUID = 11L;

		public DashedLine(){
			super();
			setSize(2, Measure.MEASURE_HEIGHT);
			repaint();
		}
		
		public void paintComponent(Graphics g){
			g.setColor(Color.DARK_GRAY);
			int y = 0;
			while(y < getHeight()){
				g.drawLine(0, y, 0, y + 2);
				y += 4;
			}
		}
	}
	
	/**
	 * 以最紧密的方法重绘小节组
	 * 注意：这里的重绘仅仅重新对音符进行排列，而并未删除对象
	 * 
	 * @param measurePart 待重绘小节组
	 */
	public void redrawTightMeasurePart(MeasurePart measurePart){
		MeaPartStrategy mpst = new MeaPartStrategy(X_MIN_DIST, true);
		redrawMeasurePart(measurePart, mpst);
	}
	
	/**
	 * 以指定的策略重绘小节组
	 * @param measurePart 待绘制小节组
	 * @param mpst 小节组重绘策略
	 */
	public void redrawMeasurePart(MeasurePart measurePart, MeaPartStrategy mpst){
		
		int dist = mpst.getNoteDist();
		boolean isTemp = mpst.isTemp();
		
		int x = measurePart.getX();
		int y = measurePart.getY();
		int meaNum = measurePart.getMeasureNum();
		//各个小节前一个音符
		ArrayList<MeasurePart.NListWithMeaIndex> preNotes = new ArrayList<MeasurePart.NListWithMeaIndex>();
		for(int i = 0; i <meaNum; i++){
			preNotes.add(null);
		}
		
		//谱号，调号，拍号的x坐标.
		int clefx = x;
		int keyx = clefx;
		int timex = keyx;
		//音符开始位置x坐标.
		int notex = timex;
		for(int i = 0, n = measurePart.getMeasureNum(); i < n; i++){
			Measure measure = measurePart.getMeasure(i);
			if(measure.getUiClef() != null){
				clefx = x + UIClef.CLEF_GAP;
				keyx = clefx + measure.getUiClef().getWidth();
				timex = timex > keyx ? timex : keyx;
				notex = timex ;
			}
			if(measure.getUiKey() != null){
                keyx = keyx + UIKey.KEY_GAP;
                timex = timex > keyx ? timex : keyx + measure.getUiKey().getWidth();
				notex = timex;
			}
			if(measure.getUiTime() != null){
				notex = timex + measure.getUiTime().getWidth();
			}
		}
		
		List<List<MeasurePart.NListWithMeaIndex>> noteStamp = measurePart.getNotesByTimeSlot();  //..........!!!!!!!!
		
		//各个时间槽中音符的横坐标，构建时向其中添加一个初始值，即小节组的x值
		ArrayList<Integer> xlocations = new ArrayList<Integer>();
		xlocations.add(notex);
		//当前时间槽的横坐标
		int tempx = notex;
		//各个小节前一个音符的坐标
		int[] prex = new int[meaNum];
		for(int i = 0; i < noteStamp.size(); i++){
			List<MeasurePart.NListWithMeaIndex> tslot = noteStamp.get(i);
			//由同一小节的前后两个音符的间距所限定的当前时间槽内音符的最小x值
			int[] shortestDistX = new int[tslot.size()];
			for(int j = 0; j < tslot.size(); j++){
				MeasurePart.NListWithMeaIndex mslot = tslot.get(j);
				int meaIndex = mslot.getMeaIndex();
				int shortDist = MusicMath.shortestDist(preNotes.get(meaIndex), mslot, true);
				shortestDistX[j] = preNotes.get(meaIndex)==null ? 
						notex+shortDist : prex[meaIndex]+shortDist;
				preNotes.set(meaIndex, mslot);
			}
			int maxShortX = MusicMath.maxValue(shortestDistX);
			
			if(i == 0){
				if(maxShortX > xlocations.get(i) + dist){
					tempx = maxShortX;
				}else{
					tempx = xlocations.get(i) + dist;
				}
			}
			else{
				if(maxShortX > xlocations.get(i) + Note.NORMAL_HEAD_WIDTH + dist){
					tempx = maxShortX;
				}else{
					tempx = xlocations.get(i) + Note.NORMAL_HEAD_WIDTH + dist;
				}
			}
			
			for(int j = 0; j < tslot.size(); j++){
				int meaIndex = tslot.get(j).getMeaIndex();
				prex[meaIndex] = tempx;
			}
			xlocations.add(tempx);
		}
		//删掉xlocations中的首个初始值
		xlocations.remove(0);
		
		//设置小节的位置,并放置谱号，调号和拍号
		int yy = y;
		//measurePart.getRepeatLine().setLocation(x, y-30);
		NoteLine line = measurePart.getNoteLine();
		for(int i = 0; i < measurePart.getMeasureNum(); i++){
			Measure measure = measurePart.getMeasure(i);
			measure.setLocation(x, yy);
			measure.locateClef(clefx);
			measure.locateKey(keyx);
			measure.locateTime(timex);
			yy += Measure.MEASURE_HEIGHT + line.getMeasureGaps().get(i);
		}
		
		//放置音符
		int lastShortDist = Note.NORMAL_HEAD_WIDTH + dist;
		for(int i = 0; i< noteStamp.size(); i++){
			List<MeasurePart.NListWithMeaIndex> tslot = noteStamp.get(i);
			for(int j = 0; j < tslot.size(); j++){
				MeasurePart.NListWithMeaIndex mn = tslot.get(j);
				mn.locateNotes(xlocations.get(i));
				//如果是最后一个时间槽
				if(i == noteStamp.size()-1){
					int shortDist = MusicMath.shortestDist(mn, null, true);
					if(shortDist > lastShortDist)
						lastShortDist = shortDist;
				}
			}
		}
		
		int maxAttrWidth = measurePart.maxAttrWidth();
		
		int lastX = x + maxAttrWidth;
		if(xlocations.size() > 0)
			lastX = xlocations.get(xlocations.size()-1) + lastShortDist;
		measurePart.setWidth(lastX - x);
		if(measurePart.getWidth() < Note.NORMAL_HEAD_WIDTH + maxAttrWidth)
			measurePart.setWidth(Note.NORMAL_HEAD_WIDTH + maxAttrWidth);
		
		for (int j = 0; j < measurePart.getMeasureNum(); j++) {
			Measure measure = measurePart.getMeasure(j);
			measure.setSize(measurePart.getWidth(), Measure.MEASURE_HEIGHT);
		}
		Barline barline = measurePart.getBarline();

	//	barline.setType("light-heavy");
//		if(score.getPageList().indexOf(page) == score.getPageList().size() - 1){
//			if(page.getNoteLines().indexOf(noteLine) == page.getNoteLines().size() - 1){
//				if(noteLine.getMeaPartList().indexOf(measurePart) == noteLine.getMeaPartList().size() - 1){
//				//	System.out.println("yes");
//					barline.setType("light-heavy");
//				}
//			}
//		}
		
		barline.adjustSize();
		if(!isTemp){
			barline.setBarlineLocation(x + measurePart.getX(), y);
//			if(barline.getType().equalsIgnoreCase("regular")){
//				barline.setLocation(x + measurePart.getX(), y);
//			}else if(barline.getType().equalsIgnoreCase("forward")){
//				barline.setLocation(x + measurePart.getX()-10, y);
//			}else{
//				barline.setLocation(x + measurePart.getX()-3, y);
//			}
			
		}
		
	}
	
	/**
	 * 绘制无节拍限制类型的小节组
	 * 该方法按照小节组原有宽度进行绘制.
	 * 如果宽度够用，返回true, 否则返回false
	 * @param measurePart
	 * @return
	 */
	private boolean redrawRetainedUnlimitedMeaPart(MeasurePart measurePart){
		boolean result = false;
		//小节组原有宽度
		int formerWidth = measurePart.getWidth();
		//先紧密排列
		redrawMeasurePart(measurePart, new MeaPartStrategy(2*LINE_GAP, false));
		//再调节宽度
		int newWidth = measurePart.getWidth();
		//宽度够用
		if(newWidth <= formerWidth){
			measurePart.setWidth(formerWidth);
			for(int i = 0; i < measurePart.getMeasureNum(); i++){
				Measure measure = measurePart.getMeasure(i);
				measure.setSize(formerWidth, measure.getHeight());
				measure.locateAnnotations();
				measurePart.locateSymbols();
				//符杠符杆
				deleteMeasureBeamAndStem(measurePart, measure);
				measure.drawBeamAndStem();
				//放置音符符号
				for(int v = 0, vn = measure.getVoiceNum(); v < vn; v++){
					for(int j = 0, jn = measure.getNoteNum(v); j < jn; j++){
						AbstractNote note = measure.getNote(j, v);
						note.locateNoteSymbols();
					}
				}
			}
		}
		else{
			result = false;
		}
		
		//小节线
		measurePart.getBarline().setBarlineLocation(measurePart.getX()+measurePart.getWidth(), measurePart.getY());
		return result;
	}
	
	/**
	 * 重绘小节组. 其与包含策略的redrawMeasurePart()的区别在于该方法尽量保持小节组原有的宽度，只有当
	 * 小节组宽度不够的时候才会被动将小节组宽度撑宽.
	 * 当小节组宽度够用时返回true,否则返回false.
	 * 
	 * @param measurePart 小节组
	 */
	public boolean redrawMeaPartWithRetainedInfo(MeasurePart measurePart){
		if(score.getScoreType() == Score.SCORE_UNLMTED){
			return redrawRetainedUnlimitedMeaPart(measurePart);
		}
		
		boolean result = true;
		//设置小节小节的位置
		int x = measurePart.getX();
		int y = measurePart.getY();
		//小节组原有宽度
		int formerWidth = measurePart.getWidth();
		//先紧密排列
		redrawTightMeasurePart(measurePart);
		//再调节宽度
		int newWidth = measurePart.getWidth();
		//宽度够用
		if(newWidth < formerWidth){
			
			//最大谱、调、拍号宽度
			int maxAttrWidth = measurePart.maxAttrWidth();
			//音符缩放比例
			double noteRatio = (double)(formerWidth - maxAttrWidth) / (newWidth - maxAttrWidth);
			//System.out.println("");
			//System.out.println("formerWidth = " + formerWidth);
			//System.out.println("maxAttrWidth = " + maxAttrWidth);
			//System.out.println("newWidth = " + newWidth);
			//System.out.println("noteRatio = " + noteRatio);
			//缩放音符坐标
			measurePart.setWidth(formerWidth);
			for(int i = 0; i < measurePart.getMeasureNum(); i++){
				Measure measure = measurePart.getMeasure(i);
				int meaAttrWidth = measure.getAttrWidth(); //小节符号宽度
				measure.setSize(formerWidth, Measure.MEASURE_HEIGHT);
				List<MeasurePart.NListWithMeaIndex> slots = measure.getMeasureSlots();
				for(int k = 0, kn = slots.size(); k < kn; k++){
					MeasurePart.NListWithMeaIndex slot = slots.get(k);
					int sx = slot.getLeftNote() == null ? slot.getList().get(0).getX() : slot.getLeftNote().getX();
					int newx = x + maxAttrWidth + 
				     (int)((sx + Note.NORMAL_HEAD_WIDTH/2 - x - maxAttrWidth)*noteRatio) - Note.NORMAL_HEAD_WIDTH/2;
					slot.locateNotes(newx);
				}
				//修正全休止符位置，使其放在小节中间
				for(int v = 0, vn = measure.getVoiceNum(); v < vn; v++){
					if(measure.getNoteNum(v) == 1){
						AbstractNote note = measure.getNote(0, v);
						if(note instanceof Note && ((Note)note).isRest() && ((Note)note).isFull()){
							int xx = measurePart.getX() + meaAttrWidth + (measurePart.getWidth() - meaAttrWidth)/2 - note.getWidth()/2;
							note.locateNote(xx);
						}
					}
				}
			}
		}
		else{
			result = false;
		}
		Barline barline = measurePart.getBarline();
		barline.setBarlineLocation(x + measurePart.getWidth(), y);
		
		//barline.setLocation(x + measurePart.getWidth(), y);
		//重绘符杆、符杠等
		for(int i = 0, n = measurePart.getMeasureNum(); i < n; i++){
			Measure measure = measurePart.getMeasure(i);
			deleteMeasureBeamAndStem(measurePart, measure);
			measure.drawBeamAndStem();
			//放置小节注释
			measure.locateAnnotations();
			measurePart.locateSymbols();
			//放置音符符号
			for(int v = 0, vn = measure.getVoiceNum(); v < vn; v++){
				for(int j = 0, jn = measure.getNoteNum(v); j < jn; j++){
					AbstractNote note = measure.getNote(j, v);
					note.locateNoteSymbols();
				}
			}
		}
		return result;
	}
	
	/**
	 * 删除小节的符杠符柄符尾
	 * @param measurePart 小节所在小节组
	 * @param measure 待删除小节
	 */
	public void deleteMeasureBeamAndStem(MeasurePart measurePart, Measure measure){
		Page page = measurePart.getNoteLine().getPage();
		for(int v = 0, vn = measure.getVoiceNum(); v < vn; v++){
			for(int i = 0, n = measure.getNoteNum(v); i < n; i++){
				AbstractNote note = measure.getNote(i, v);
				if(note.getBeam() != null){
					Beam beam = note.getBeam();
					for(int j = 0, jn = beam.getUiNoteList().size(); j < jn; j++){
						AbstractNote nnote = beam.getUiNoteList().get(j);
						nnote.setBeam(null);
					}
					beam.getUiNoteList().clear();
					page.remove(beam);
				}
				if(note.getStem() != null){
					Stem stem = note.getStem();
					note.setStem(null);
					page.remove(stem);
				}
				if(note.getTail() != null){
					Tail tail = note.getTail();
					note.setTail(null);
					page.remove(tail);
				}
			}
		}
	}
	
	/**
	 * 重绘无限制节拍的行
	 * 每行只允许包含一个小节组,且在小节内音符以最紧密方式从左向右排列
	 * @param line
	 * @return
	 */
	public boolean redrawUnlimitedLine(NoteLine line){
		boolean result = false;
		
		//每行只允许一个小节组
		if(line.getMeaPartList().size() > 1){
			NoteLine nxtLine = MusicMath.nxtLine(line);
			if(nxtLine == null){
				nxtLine = new NoteLine();
				controller.addLine(nxtLine);
			}
			for(int i = line.getMeaPartList().size() - 1; i >= 1; i--){
				MeasurePart meaPart = line.getMeaPartList().get(i);
				nxtLine.getMeaPartList().add(0, meaPart);
				meaPart.setNoteLine(nxtLine);
			}
			while(line.getMeaPartList().size() > 1){
				MeasurePart mPart = line.getMeaPartList().get(1);
				line.getMeaPartList().remove(mPart);
			}
			result = true;
		}
		
		//刷新谱、调、拍号.
		controller.genMeaAttrInLine(line);
		
		MeasurePart meaPart = line.getMeaPartList().get(0);
		meaPart.setX(line.getX());
		meaPart.setY(line.getY());
		
		//紧密排列
//		redrawTightMeasurePart(meaPart);
		redrawMeasurePart(meaPart, new MeaPartStrategy(LINE_GAP*2, false));
		//拉伸小节，但不包括音符
		meaPart.setWidth(lineWidth);
		for(int i = 0; i < meaPart.getMeasureNum(); i++){
			Measure measure = meaPart.getMeasure(i);
			measure.setSize(meaPart.getWidth(), Measure.MEASURE_HEIGHT);
			
			//画符杠符柄等
			deleteMeasureBeamAndStem(meaPart, measure);
			measure.drawBeamAndStem();
			
			//放置音符符号
			for(int v = 0, vn = measure.getVoiceNum(); v < vn; v++){
				for(int k = 0, kn = measure.getNoteNum(v); k < kn; k++){
					AbstractNote note = measure.getNote(k, v);
					note.locateNoteSymbols();
				}
			}
			
			//小节标注
			measure.locateAnnotations();
			measure.getMeasurePart().locateSymbols();
		}
		meaPart.getBarline().setBarlineLocation(meaPart.getX()+meaPart.getWidth(), meaPart.getY());
		
//		if(meaPart.getBarline().getType().equalsIgnoreCase("regular")){
//			meaPart.getBarline().setLocation(meaPart.getX()+meaPart.getWidth(), meaPart.getY());
//		}else if(meaPart.getBarline().getType().equalsIgnoreCase("forward")){
//			meaPart.getBarline().setLocation(meaPart.getX()+meaPart.getWidth()-10, meaPart.getY());
//		}
//		else {
//			meaPart.getBarline().setLocation(meaPart.getX()+meaPart.getWidth()-3, meaPart.getY());
//		}

		redrawSymbolLinesInLine(line);
		redrawRepeatLineInLine(line);
		
		line.locateBrackets();
		line.locateFrontBarline();
		line.locateMarkers();
		return result;
	}
	
	/**
	 * 以默认策略重绘某一行.
	 * 其对该行的元素尽可能的容纳，因而以最紧密的方式绘制每个小节组.最终再将元素按比例伸缩到整行.
	 * 容纳不下的元素放置到下一行
	 * 默认策略不自动吸收下一行
	 * @param line
	 * @return 是否影响到下一行,是则返回true, 否则返回false
	 */
	public boolean redrawLine(NoteLine line){
		MeaPartStrategy mpst = new MeaPartStrategy(X_MIN_DIST, false);
		NoteLineStrategy nls = new NoteLineStrategy(mpst, false);
		return redrawLineWithStrategy(line, nls);
	}
	
	/**
	 * 按照指定的策略重绘某行
	 * @param line 待会的行
	 * @param nls 行绘制策略
	 * @return 是否影响到下一行,是则返回true, 否则返回false
	 */
	public boolean redrawLineWithStrategy(NoteLine line, NoteLineStrategy nls){
		boolean result = false;
		
		//如果是无限制节拍类型
		if(score.getScoreType() == Score.SCORE_UNLMTED){
			return redrawUnlimitedLine(line);
		}
		
		MeaPartStrategy mpst = nls.getMpst();
		boolean isDrawBack = nls.isDrawBack();
		
		
		//刷新谱、调、拍号.
		controller.genMeaAttrInLine(line);
		
		Page page = line.getPage();
		
		//本行的所有调号、谱号等宽度和
		int attrWidth = 0;
		
		int x = line.getX();
		int y = line.getY();
		LoopFor:
		for(int i = 0; i < line.getMeaPartList().size(); i++){
			MeasurePart meaPart = line.getMeaPartList().get(i);
			meaPart.setX(x);
			meaPart.setY(y);
			//紧密排列小节组音符
			redrawMeasurePart(meaPart, mpst);
			x = x + meaPart.getWidth();
			attrWidth += meaPart.maxAttrWidth();
			
			//从小节组i开始，该行已经容不下，移进下一行
			if(x > xStart + lineWidth){
				NoteLine nxtLine = MusicMath.nxtLine(line);
				if(nxtLine == null){
					nxtLine = new NoteLine();
					controller.addLine(nxtLine);
				}
				//移入下一行
				for(int j = line.getMeaPartList().size()-1; j >= i; j--){
					//System.out.println(i);
					MeasurePart mPart = line.getMeaPartList().get(j);
					//下一行不在同一页，移动UI对象
					if(nxtLine.getPage() != page){
						controller.addUIEntityToPage(mPart, nxtLine.getPage());
					}
					nxtLine.getMeaPartList().add(0, mPart);
					line.getMeaPartList().remove(mPart);
					mPart.setNoteLine(nxtLine);
					//新生成的行,则生成各种辅助符号
					if(nxtLine.getMeaPartList().size() == 1){
						nxtLine.generateBrackets();
						nxtLine.generateFrontBarlineLine();
						nxtLine.generateMarkers();
						nxtLine.addMarkerListener(this);
					}
				}
				x -= meaPart.getWidth();
				result = true;
				break LoopFor;
			}
		}
		
		//过于稀释，将下一行的部分音符吸回来
		if(isDrawBack){
			//本行剩下的宽度
			int restx = xStart + lineWidth - x;
			int nxtAccumx = 0;
			NoteLine nextLine = MusicMath.nxtLine(line);
			Loop: 
			while(line.getMeaPartList().size() <= 5 && nextLine != null){
				for(int i = 0; i < nextLine.getMeaPartList().size(); i++){
					MeasurePart measurePart = nextLine.getMeaPartList().get(i);
					nxtAccumx = Controller.shortestMeaPartWidth(measurePart, mpst.getNoteDist());
					//nxtAccumx = nxtAccumx > (20 * LINE_GAP) ? nxtAccumx : (20 * LINE_GAP);
					
					if(nxtAccumx <= restx){
						//不在同一页，移动UI
						if(nextLine.getPage() != page){
							controller.addUIEntityToPage(measurePart, page);
						}
						controller.removeMeasurePartFromLine(measurePart);
						line.getMeaPartList().add(measurePart);
						measurePart.setNoteLine(line);
						
						restx = restx - nxtAccumx;
						result = true;
					}
					else{
						//有从下一行吸回小节组
						if(result){
							NoteLineStrategy nnls = new NoteLineStrategy(nls);
							nnls.setDrawBack(false);
							redrawLineWithStrategy(line, nnls);
							return true;
					    }else{
					    	break Loop;
					    }
					}
				}
				nextLine = MusicMath.nxtLine(line);
			}
			//终止不是由于宽度不够，而是由于到乐谱末尾
			if(result){
				NoteLineStrategy nnls = new NoteLineStrategy(nls);
				nnls.setDrawBack(false);
				redrawLineWithStrategy(line, nnls);
				return true;
		    }
		}
		
		//将音符按比例扩散到整行
		if(x - xStart < lineWidth){
			
			//小节组的音符部分的宽度缩放比例
			double ratio = (double)(lineWidth - attrWidth) / (x - xStart - attrWidth);
			x = xStart;
			//个小节组累积宽度
			int accumWidth = 0;
			for(int i = 0, ni = line.getMeaPartList().size(); i < ni; i++){
				MeasurePart meaPart = line.getMeaPartList().get(i);
				int meaAttrWidth = meaPart.maxAttrWidth();
				
				int formerx = meaPart.getX();
				meaPart.setX(x);
				if(i != ni - 1){
					meaPart.setWidth((int)((meaPart.getWidth() - meaAttrWidth) * ratio + meaAttrWidth));
				}else{
					meaPart.setWidth(lineWidth - accumWidth);
				}
				meaPart.locateSymbols();
				accumWidth += meaPart.getWidth();
				
				for(int j = 0, nj = meaPart.getMeasureNum(); j < nj; j++){
					Measure measure = meaPart.getMeasure(j);
					
					//谱号，调号，拍号相对小节起始的距离
					int clef_deltax = 0;
					int key_deltax = 0;
					int time_deltax = 0;
					if(measure.getUiClef() != null){
						clef_deltax = measure.getUiClef().getX() - measure.getX();
					}
					if(measure.getUiKey() != null){
						key_deltax = measure.getUiKey().getX() - measure.getX();
					}
					if(measure.getUiTime() != null){
						time_deltax = measure.getUiTime().getX() - measure.getX();
					}
					
					//调制小节位置和大小
					measure.setLocation(meaPart.getX(), measure.getY());
					measure.setSize(meaPart.getWidth(), measure.getHeight());
					
					//调整谱号，调号，拍号位置
					if(measure.getUiClef() != null){
						measure.getUiClef().setLocation(measure.getX() + clef_deltax, measure.getUiClef().getY());
					}
					if(measure.getUiKey() != null){
						measure.getUiKey().setLocation(measure.getX() + key_deltax, measure.getUiKey().getY());
					}
					if(measure.getUiTime() != null){
						measure.getUiTime().setLocation(measure.getX() + time_deltax, measure.getUiTime().getY());
					}
					
					//调整小节音符位置
					List<MeasurePart.NListWithMeaIndex> slots = measure.getMeasureSlots();
					for(int k = 0, kn = slots.size(); k < kn; k++){
						MeasurePart.NListWithMeaIndex slot = slots.get(k);
						int sx = slot.getLeftNote() == null ? slot.getList().get(0).getX() : slot.getLeftNote().getX();
						sx = sx + meaPart.getX() - formerx;
						int newx = x + meaAttrWidth + 
					     (int)((sx + Note.NORMAL_HEAD_WIDTH/2 - x - meaAttrWidth)*ratio) - Note.NORMAL_HEAD_WIDTH/2;
						slot.locateNotes(newx);
					}
					//修正全休止符位置，使其放在小节中间
					for(int v = 0, vn = measure.getVoiceNum(); v < vn; v++){
						if(measure.getNoteNum(v) == 1){
							AbstractNote note = measure.getNote(0, v);
							if(note instanceof Note && ((Note)note).isRest() && ((Note)note).isFull()){
								int xx = meaPart.getX() + meaAttrWidth + (meaPart.getWidth() - meaAttrWidth)/2 - note.getWidth()/2;
								note.locateNote(xx);
							}
						}
					}
					
					//画符杠符柄等
					deleteMeasureBeamAndStem(meaPart, measure);
					measure.drawBeamAndStem();
					//小节标注
					measure.locateAnnotations();
					meaPart.locateSymbols();
					
					//放置音符符号
					for (int v = 0, vn = measure.getVoiceNum(); v < vn; v++) {
						for (int k = 0, kn = measure.getNoteNum(v); k < kn; k++) {
							AbstractNote note = measure.getNote(k, v);
							note.locateNoteSymbols();
						}
					}
					
				}
				
				x += meaPart.getWidth();
				Barline barline = meaPart.getBarline();
		
				barline.setBarlineLocation(x, y);
//				if(barline.getType().equalsIgnoreCase("regular")){
//					barline.setLocation(x , y);
//				}else if(barline.getType().equalsIgnoreCase("forward")){
//					barline.setLocation(x-10 , y);
//				}else {
//					barline.setLocation(x-3, y);
//				}
			}
		}
		redrawSymbolLinesInLine(line);
		redrawRepeatLineInLine(line);
		
		line.locateBrackets();
		line.locateFrontBarline();
		line.locateMarkers();
		
		revalidate();
		updateUI();
		
		return result;
	}
	
	/**
	 * 重绘某行，但是保留该行各小节宽度，各元素位置等其他信息。
	 * 实际上，该方法仅仅改变行中谱表Y方向坐标，因此通常在鼠标拖动行的水平标识符时调用。
	 * @param line
	 */
	public void redrawLineWithRetainedInfo(NoteLine line){
		for(int i = 0, n = line.getMeaPartList().size(); i < n; i++){
			MeasurePart meaPart = line.getMeaPartList().get(i);
			meaPart.locateSymbols();
			meaPart.setY(line.getY());
			redrawMeaPartWithRetainedInfo(meaPart);
		}
		line.locateFrontBarline();
		line.locateMarkers();
		line.locateBrackets();
		redrawSymbolLinesInLine(line);
		redrawRepeatLineInLine(line);
		updateUI();
		((JComponent)getParent()).updateUI();
	}
	
	/**
	 * 以默认策略重绘某一页
	 * 默认策略不会自动吸收下一行以及下一页
	 * @param page
	 */
	public boolean redrawPage(Page page){
		MeaPartStrategy mpst = new MeaPartStrategy(X_MIN_DIST, false);
		NoteLineStrategy nls = new NoteLineStrategy(mpst, false);
		PageStrategy ps = new PageStrategy(nls, false);
		return redrawPage(page, ps);
	}
	
	/**
	 * 重绘某一页.
	 * @param page 待绘制的页
	 * @param pst 页绘制策略
	 */
	public boolean redrawPage(Page page, PageStrategy pst){
		boolean result = false;
		
		NoteLineStrategy nls = pst.getNls();
		boolean isDrawBack = pst.isDrawBack();
		//本页剩余空间
		int blank = page.getHeight() - page.getNoteLineYStart();
		for(int i = 0; i < page.getNoteLines().size(); i++){
			NoteLine line = page.getNoteLines().get(i);
			//调整间隔（以防歌词）
			line.adjustGap(false);
			
			//本页空间不够，从本行开始移到下一页
			if(blank < line.getHeight() + line.getLineGap() && i != 0){
				
				for(int k = page.getNoteLines().size()-1; k >= i; k--){
					line = page.getNoteLines().get(k);
					
					Page nxtPage = MusicMath.nxtPage(score, page);
					//最后一页，生成新页面
					if(nxtPage == null){
						nxtPage = new Page();
						score.getPageList().add(nxtPage);
						nxtPage.setScore(score);
						add(nxtPage);
						rearangePages(score.getPageList().indexOf(nxtPage));
					}
					//转移UI
					for(int j = 0, n = line.getMeaPartList().size(); j < n; j++){
						MeasurePart meaPart = line.getMeaPartList().get(j);
						controller.addUIEntityToPage(meaPart, nxtPage);
					}
					for(Bracket bk : line.getBrackets()){
						if(bk != null)
							nxtPage.add(bk);
					}
					for(LineMarker mk : line.getMarkers()){
						nxtPage.add(mk);
					}
					nxtPage.add(line.getFrontBarline());
					
					controller.removeNoteLineFromPage(line);
					nxtPage.getNoteLines().add(0, line);
					line.setPage(nxtPage);
				}
				result = true;
			}
			//空间够，则绘制本行
			else{
				line.determineLocation();
				redrawLineWithStrategy(line, nls);
				blank = blank - line.getHeight() - line.getLineGap();
			}
		}
		//调整页面与画板大小
//		adjustSize(score);
		//本页过于稀疏时，允许吸收下一页元素
		if(isDrawBack){
			int restBlank = page.getBlankHeight();
			NoteLine curLine = page.getNoteLines().get(page.getNoteLines().size() - 1);
			NoteLine nxtLine = MusicMath.nxtLine(curLine);
			while(nxtLine != null && nxtLine.getHeight() + nxtLine.getLineGap()*2 < restBlank){
				controller.removeNoteLineFromPage(nxtLine);
				//UI实体
				for(int j = 0, jn = nxtLine.getMeaPartList().size(); j < jn; j++){
					MeasurePart meaPart = nxtLine.getMeaPartList().get(j);
					controller.addUIEntityToPage(meaPart, page);
				}
				for(Bracket bk : nxtLine.getBrackets()){
					if(bk != null)
						page.add(bk);
				}
				for(LineMarker mk : nxtLine.getMarkers()){
					page.add(mk);
				}
				page.add(nxtLine.getFrontBarline());
				
				page.getNoteLines().add(nxtLine);
				nxtLine.setPage(page);
				
				nxtLine.determineLocation();
				redrawLineWithStrategy(nxtLine, nls);
				restBlank = restBlank - nxtLine.getHeight() - nxtLine.getLineGap();
				
				nxtLine = MusicMath.nxtLine(nxtLine);
				result = true;
		    }   
		}
		return result;
	}
	
	/**
	 * 拖动小节线时，该小节线把这一行的小节组分为左后两个部分。该变量记录了在拖动之前，左半部分的小节组宽度总和
	 * 因此，该变量仅仅在拖动小节线时有意义. 
	 * 该变量在鼠标按下时初始化，在鼠标松开时清0.
	 */
	private int accumWidthBeforeDrag;
	
	/**
	 * 拖动小节线之前，该行的各个小节组所占对应部分（左半部分或后半部分）宽度的比例
	 */
	private ArrayList<Double> ratios = new ArrayList<Double>();
	
	/**
	 * 鼠标拖动
	 */
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
		/*
		 * 拖动小节线
		 */
		
		if(e.getSource() instanceof Barline){
			//改变光标
			setCursor(moveCursor);
			
			int xpos = e.getXOnScreen();
			
			Barline barline = (Barline)e.getSource();
			MeasurePart measurePart = barline.getMeaPart();
			NoteLine line = measurePart.getNoteLine();
			Page page = line.getPage();
			
			//第几个小节组
			int meaPartIndex = line.getMeaPartList().indexOf(measurePart);
			
			//缓存该拖动事件之前，该行各小节组的宽度
			ArrayList<Integer> formerWidths = new ArrayList<Integer>();
			for(int i = 0, n = line.getMeaPartList().size(); i < n; i++){
				formerWidths.add(line.getMeaPartList().get(i).getWidth());
			}
			
			int x = xStart;
			
			//相对于拖动前小节线的位置，一共拖动了多少。向右为正，向左为负
			int accumChange = xpos - (int)screenPoint.getX();
			
			//如果向左拖动最后一个小节线，一定条件下可以将下一行的第一个小节组“吸”上来
			if(meaPartIndex == line.getMeaPartList().size() - 1){
				if(accumChange < 0){
					NoteLine nxtLine = MusicMath.nxtLine(line);
					if(nxtLine != null){
						//虚线
						dashedLine.setVisible(true);
						if(dashedLine.getParent() != page)
							page.add(dashedLine);
						dashedLine.setSize(2, barline.getHeight());
						dashedLine.setLocation(barline.getX() + accumChange, line.getY());
						
						MeasurePart nxtPart = nxtLine.getMeaPartList().get(0);
						int nxtShortWidth = Controller.shortestMeaPartWidth(nxtPart);
						if(-1 * accumChange > nxtShortWidth ){
							int accumWidth = 0;
							for(int i = 0, n = line.getMeaPartList().size(); i < n; i++){
								MeasurePart part = line.getMeaPartList().get(i);
								accumWidth += Controller.shortestMeaPartWidth(part);
							}
							if(accumWidth + nxtShortWidth < lineWidth){
								//虚线隐藏
								dashedLine.setVisible(false);
								//下一行是否在新的一页
								boolean flag = false;
								//不在同一页，则要将UI对象移至该页
								if(nxtPart.getNoteLine().getPage() != page){
									controller.addUIEntityToPage(nxtPart, page);
									flag = true;
								}
								//吸收下一行的小节组后，下一行是否变为空
								boolean re = controller.removeMeasurePartFromLine(nxtPart);
								line.getMeaPartList().add(nxtPart);
								nxtPart.setNoteLine(line);
								if(re){
									redrawLine(line);
									if(flag){
										Page nxtPage = MusicMath.nxtPage(score, page);
										if(nxtPage != null)
											rearangeLinesInPage(nxtPage);
									}
									else
										rearangeLinesInPage(page);
								}
								else{
									redrawLine(line);
									redrawLine(nxtLine);
								}
							}
						}
					}
				}
				return;
			}
			
			int accumWidth = 0;
			//按照比例缩放前半部分小节组宽度
			for(int i = 0; i <= meaPartIndex; i++){
				MeasurePart meaPart = line.getMeaPartList().get(i);
				int newWidth = (int)(ratios.get(i) * (accumWidthBeforeDrag + accumChange));
				meaPart.setWidth(newWidth);
				meaPart.setX(x);
				boolean xx = redrawMeaPartWithRetainedInfo(meaPart);
				//前半部分小节组已经被挤压至极致
				if(!xx && i == meaPartIndex){
					int tempx = xStart;
					for(int j = 0; j <= meaPartIndex; j++){
						MeasurePart part = line.getMeaPartList().get(j);
						part.setWidth(formerWidths.get(j));
						part.setX(tempx);
						redrawMeaPartWithRetainedInfo(part);
						tempx += part.getWidth();
					}
					return;
				}
				x += meaPart.getWidth();
				accumWidth += meaPart.getWidth();
			}

			//缩放后半部分小节组宽度
			for(int i = meaPartIndex + 1; i < line.getMeaPartList().size(); i++){
				MeasurePart meaPart = line.getMeaPartList().get(i);
				//如果是最后一个小节组，则保证右端严格对齐
				if(i == line.getMeaPartList().size()-1){
					meaPart.setWidth(lineWidth - accumWidth);
				}
				else{
					int newWidth = (int)(ratios.get(i) * (lineWidth - accumWidthBeforeDrag - accumChange));
					meaPart.setWidth(newWidth);
				}
				meaPart.setX(x);
				redrawMeaPartWithRetainedInfo(meaPart);
				x += meaPart.getWidth();
				accumWidth += meaPart.getWidth();
			}
			
			//最右端的小节组已经被挤压出该行
			if(x > xStart + lineWidth + 1){
				MeasurePart lastMeaPart = line.getMeaPartList().get(line.getMeaPartList().size()-1);
				NoteLine nxtLine = MusicMath.nxtLine(line);
				if(nxtLine == null){
					nxtLine = new NoteLine();
					controller.addLine(nxtLine);
				}
				//下一行与本行不在同一页，移动UI对象
				if(lastMeaPart.getNoteLine().getPage() != nxtLine.getPage()){
					controller.addUIEntityToPage(lastMeaPart, nxtLine.getPage());
				}
				nxtLine.getMeaPartList().add(0, lastMeaPart);
				//该行是新生成的，则生成行标识符,括号,和前行线
				if(nxtLine.getMeaPartList().size() == 1){
					nxtLine.generateMarkers();
					nxtLine.addMarkerListener(this);
					nxtLine.generateBrackets();
					nxtLine.generateFrontBarlineLine();
				}
				line.getMeaPartList().remove(lastMeaPart);
				lastMeaPart.setNoteLine(nxtLine);
				redrawLine(line);
				boolean flag = redrawLine(nxtLine);
				while(flag){
					nxtLine = MusicMath.nxtLine(nxtLine);
					flag = redrawLine(nxtLine);
				}
				
				//调整页面大小
//				adjustSize(score);
				updateUI();
				return;
			}
			
			redrawSymbolLinesInLine(line);
			redrawRepeatLineInLine(line);
		}
		
		/*
		 * 拖动行标识符
		 */
		
		else if(e.getSource() instanceof LineMarker){
			setCursor(upDownCursor);
			
			LineMarker marker = (LineMarker)e.getSource();
			int x = e.getYOnScreen();
			int y = e.getYOnScreen();
			int deltay = y - (int)screenPoint.getY();
			NoteLine line = marker.getLine();
			Page page = line.getPage();
			int lineIndex = page.getNoteLines().indexOf(line);
			int meaIndex = line.getMarkers().indexOf(marker);
			//拖动的是本行的第一行谱表,并且该行不是对应页面的第一行
			if(meaIndex == 0 && lineIndex != 0){
				NoteLine preLine = page.getNoteLines().get(lineIndex - 1);
				int newGap = preLine.getLineGap()+deltay < NoteLine.NOTELINE_GAP ? NoteLine.NOTELINE_GAP :
					preLine.getLineGap()+deltay;
				preLine.setLineGap(newGap);
				boolean re = rearangeLinesInPage(page);
				while(re){
					Page nxtPage = MusicMath.nxtPage(score, page);
					re = rearangeLinesInPage(nxtPage);
				}
				screenPoint.setLocation(x, y);
			}//拖动的不是本行第一个谱表
			else if(meaIndex != 0){
				int formerMeaGap = line.getMeasureGaps().get(meaIndex - 1);
				line.getMeasureGaps().set(meaIndex - 1, formerMeaGap + deltay);
				boolean re = rearangeLinesInPage(page);
				while(re){
					Page nxtPage = MusicMath.nxtPage(score, page);
					re = rearangeLinesInPage(nxtPage);
				}
				screenPoint.setLocation(x, y);
			}//页面第一行的第一个谱表
			else if(meaIndex == 0 && lineIndex == 0){
				int pageIndex = score.getPageList().indexOf(page);
				if(pageIndex > 0 && deltay < 0 && -1 * deltay >= yStart / 2){
					Page prePage = score.getPageList().get(pageIndex - 1);
					if(prePage.getBlankHeight() > line.getHeight() + line.getLineGap()){
						//转移UI对象
						for(int i = 0, n = line.getMeaPartList().size(); i < n; i++){
							MeasurePart part = line.getMeaPartList().get(i);
							controller.addUIEntityToPage(part, prePage);
						}
						for(LineMarker mk : line.getMarkers()){
							prePage.add(mk);
						}
						
						for(Bracket bk : line.getBrackets()){
							if(bk != null)
								prePage.add(bk);
						}
						prePage.add(line.getFrontBarline());
						
						page.getNoteLines().remove(line);
						rearangeLinesInPage(page);
						
						prePage.getNoteLines().add(line);
						line.setPage(prePage);
						rearangeLinesInPage(prePage);
					}
				}
			}
		}
		
		/*
		 * 拖动音符
		 */
		
		else if(e.getSource() instanceof Note){
			Note note = (Note)e.getSource();
			int deltay = (int)e.getYOnScreen() - (int)screenPoint.getY();
			if(deltay >= LINE_GAP/2 || deltay <= -LINE_GAP/2){
				int stepNum = Math.round(deltay * 2 / LINE_GAP);
				int pitch = note.getPitch() - stepNum;
				note.setPitch(pitch);
				screenPoint.setLocation(screenPoint.getX(), screenPoint.getY() + stepNum * LINE_GAP / 2);
				
				Measure measure = null;
				if(note instanceof Grace){
					Grace grace = (Grace)note;
					measure = grace.getChordNote() == null ? grace.getMeasure() : ((ChordGrace)grace.getChordNote()).getMeasure();
				}
				else
					measure = note.getChordNote() == null ? note.getMeasure() : note.getChordNote().getMeasure();
				NoteLine line = measure.getMeasurePart().getNoteLine();
				redrawLineWithRetainedInfo(line);
				note.determineRealPitch();
			}
		}
		
		/*
		 * 拖动标注
		 */
		
		else if(e.getSource() instanceof Annotation || e.getSource() instanceof Annotation.ImagePanel){
			Annotation at = e.getSource() instanceof Annotation ? (Annotation)e.getSource() : 
				((Annotation.ImagePanel)e.getSource()).getAt();
		    JComponent page = (JComponent)at.getParent();
		    connBeginPoint.setLocation(at.getX(), at.getY() + page.getY());
		    if(!veil.isVisible())
		    	veil.setVisible(true);
		    veil.repaint();
		}
	}
	
	/**
	 * 从某页开始调整页面排列
	 * @param pageIndex
	 */
	public void rearangePages(int pageIndex){
		int y = 0;
		if(pageIndex != 0){
			Page prepage = score.getPageList().get(pageIndex-1);
			y = prepage.getY() + Page.PAGE_HEIGHT + PAGE_GAP;
		}
		for(int i = pageIndex, n = score.getPageList().size(); i < n; i++){
			Page page = score.getPageList().get(i);
			page.setLocation(0, y);
			y += Page.PAGE_HEIGHT + PAGE_GAP;
		}
		//调整画板大小
//		adjustSize(score);
		//调整画板大小
		if(getHeight() != y){
			setPreferredSize(new Dimension(getWidth(), y));
			setSize(getWidth(), y);
			veil.setSize(getWidth(), y);
			updateUI();
		}
	}
	
	/**
	 * 调整页面中的行. 其实是对页面的重绘
	 * @param page
	 * @return 如果影响到下一页的行，则返回true, 否则返回false.
	 */
	public boolean rearangeLinesInPage(Page page){
		//页面为空
		if(page.getNoteLines().size() == 0){
			int pageIndex = score.getPageList().indexOf(page);
			score.getPageList().remove(page);
			remove(page); //这里两个remove是不一样的意思，第一个是移除列表里首次出现的指定元素，第二个是从容器中移除组件
			if(getParent() instanceof JComponent){
				((JComponent)getParent()).updateUI();
			}
			updateUI();
			rearangePages(pageIndex);
			//删除页码按钮
			if(score.getPageList().size() < 5){
				bottomPanel.getButtonGroup().removePageButton();
			}
			return false;
		}
		else{
			for(int i = 0; i < page.getNoteLines().size(); i++){
				NoteLine line = page.getNoteLines().get(i);
				line.determineLocation();
				
				//超出本页范围,将剩下的行放到下一页
				if(line.getY() + line.getHeight() + line.getLineGap() > Page.PAGE_HEIGHT){
					//第一行就盛不下，则置之不理
					if(i == 0){
						redrawLineWithRetainedInfo(line);
						return false;
					}
					
					Page nxtPage = MusicMath.nxtPage(score, page);
					if(nxtPage == null){
						nxtPage = new Page();
						score.getPageList().add(nxtPage);
						nxtPage.addMouseListener(this);
						nxtPage.setScore(score);
						page.setScore(score);
						add(nxtPage);
						rearangePages(score.getPageList().indexOf(nxtPage));
						//增加相应的页码按钮
						if(score.getPageList().size() <= 5){
							bottomPanel.getButtonGroup().addPageButton();
						}
					}
					for(int j = page.getNoteLines().size() - 1; j >= i; j--){
						NoteLine moveline = page.getNoteLines().get(j);
						page.getNoteLines().remove(moveline);
						nxtPage.getNoteLines().add(0, moveline);
						moveline.setPage(nxtPage);
						//转移UI对象
						for(int k = 0; k < moveline.getMeaPartList().size(); k++){
							MeasurePart part = moveline.getMeaPartList().get(k);
							controller.addUIEntityToPage(part, nxtPage);
						}
						for(LineMarker mk : moveline.getMarkers()){
							nxtPage.add(mk);
						}
						for(Bracket bk : moveline.getBrackets()){
							if(bk != null) 
								nxtPage.add(bk);
						}
						nxtPage.add(moveline.getFrontBarline());
					}
					updateUI();
					if(getParent() instanceof JComponent){
						((JComponent)getParent()).updateUI();
					}
					//将本页最后一行的行间距设为默认值
					NoteLine lastLine = page.getNoteLines().get(i-1);
					lastLine.setLineGap(NoteLine.NOTELINE_GAP);
					return true;
				}
				
				//没有超出范围则简单重绘即可
				redrawLineWithRetainedInfo(line);
			}
			return false;
		}
	}
	
	/**
	 * 以指定的音符间距调整乐谱
	 * 该方法通常是为了进行自动布局，因此允许自动稀疏的行吸收下一行对象
	 * @param dist
	 */
	public void rearangeScore(int dist){
		int pageBefore = score.getPageList().size();
		//页数大于20，生成进度条
		ProgressDialog pm = null;
		ProgressThread thread = null;
		if(pageBefore > 20){
			pm = new ProgressDialog(0, pageBefore);
			Dimension dm = getToolkit().getScreenSize();//获取屏幕大小
			pm.setLocation(((int)dm.getWidth() - pm.getWidth())/2, ((int)dm.getHeight() - pm.getHeight())/2);
			thread = new ProgressThread(pm);
			thread.start();//  执行该线程
		}
		for(int i = 0; i < score.getPageList().size(); i++){
			if(pm != null){
				thread.setN(i+1);
			}
			
			Page page = score.getPageList().get(i);
			MeaPartStrategy mpst = new MeaPartStrategy(dist, false);
			NoteLineStrategy nls = new NoteLineStrategy(mpst, true);
			PageStrategy ps = new PageStrategy(nls, true);
			
			redrawPage(page, ps);
			System.out.println("自动布局到第：" + (i+1) + "页");
		}
		if(pm != null)
			pm.dispose();
		
		rearangePages(0);
	}
	
	/**
	 * 消除当前乐谱
	 */
	public void disposeAll(){
		if(score == null)
			return;
		
		//解除UI实体
		for(Page page : score.getPageList()){
			page.removeAll();
			remove(page);
		}
		for(Component comp : getComponents()){ //注释直接放在canvas上的，需要区别对待
			if(comp instanceof Annotation){
				remove(comp);
			}
		}
		revalidate();
		
		//消除对象关系
		for(Page page : score.getPageList()){//:是for一种增强循环的用法，对所有list里的page进行遍历
			page.setScore(null);
			for(NoteLine line : page.getNoteLines()){
				line.setPage(null);
				for(MeasurePart measurePart : line.getMeaPartList()){
					measurePart.setNoteLine(null);
					for(int i = 0, n = measurePart.getMeasureNum(); i < n; i++){
						Measure measure = measurePart.getMeasure(i);
						measure.setMeasurePart(null);
						page.remove(measure);
						for(int v = 0, vn = measure.getVoiceNum(); v < vn; v++){
							for(int j = measure.getNoteNum(v)-1, jn = 0; j >= jn; j--){
								AbstractNote note = measure.getNote(j, v);
								if(note instanceof Note){
									page.remove(note);
									measure.removeNote(note);
								}
								else if(note instanceof ChordNote){
									ChordNote cnote = (ChordNote)note;
									for(int k = cnote.getNoteNum()-1; k >= 0; k--){
										page.remove(cnote.getNote(k));
									    cnote.getNote(k).setChordNote(null);
									}
									cnote.clearNoteList();
								}
							}
						}
					}
				}
			}
		}
		score.getPageList().clear();
	}

	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == veil){
			int x = e.getX();
			int y = e.getY();
			int pageIndex = controller.getEditingPageIndex(x, y);
			Page page = score.getPageList().get(pageIndex);
			if(editNote.getParent() != page)
				page.add(editNote, JLayeredPane.PALETTE_LAYER);
			AbstractNote note = controller.getEditingNote(x, y, voice);
			
			//局部y坐标
			int yy = y - (Page.PAGE_HEIGHT + NoteCanvas.PAGE_GAP) * pageIndex;
			int lineIndex = controller.getEditingLineIndex(page, x, yy);
			NoteLine line = page.getNoteLines().get(lineIndex);
			int meaPartIndex = controller.getEditingMeaPartIndex(line, x, yy);
			MeasurePart meaPart = line.getMeaPartList().get(meaPartIndex);
			int meaIndex = controller.getEditingMeasureIndex(meaPart, x, yy);
			Measure measure = meaPart.getMeasure(meaIndex);
			
			int pitch = Math.round((measure.getY() + Measure.MEASURE_HEIGHT - yy) * 2 / LINE_GAP); 
			int notey = measure.getY() + measure.getHeight()-1 - (pitch) * LINE_GAP / 2 - editNote.getHeight()/2;
			
			if(note == null){
				if(measure.getNoteNum(voice) == 0)
					editNote.setLocation(measure.getX() + measure.getAttrWidth() + LINE_GAP, notey);
				else{
					AbstractNote lnote = measure.getNote(measure.getNoteNum(voice)-1, voice);
					editNote.setLocation(lnote.getX() + lnote.getWidth() + 2*LINE_GAP, notey);
				}
			}
			else
				editNote.setLocation(note.getX(), notey);
			
			editNote.refreshPosLines(measure);
			((JComponent)getParent()).revalidate();
			((JComponent)getParent()).updateUI();
		}
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		//选中小节组
		if(e.getSource() instanceof Measure && e.getClickCount() == 2){
			Measure measure = (Measure)e.getSource();
			MeasurePart measurePart = measure.getMeasurePart();
			for(Selectable s : selectedObjects){
				s.cancleSelected();
			}
			selectedObjects.add(measurePart);
			measurePart.beSelected();
		}
		//双击线条符号，使其进入编辑状态
		else if(e.getSource() instanceof SymbolLine && e.getClickCount() == 2){
			SymbolLine sl = (SymbolLine)e.getSource();
			sl.setStatus(AbstractLine.EDIT_STATUS);
			sl.repaint();
			sl.requestFocus();
		}
		//双击房子记号，使其进入编辑装填
		else if(e.getSource() instanceof RepeatLine && e.getClickCount() == 2){
			RepeatLine rl = (RepeatLine)e.getSource();
			rl.setStatus(AbstractLine.EDIT_STATUS);
			rl.repaint();
			rl.requestFocus();
		}
		//双击连音线，使其进入编辑状态
		else if(e.getSource() instanceof Tie && e.getClickCount() == 2){
			Tie sl = (Tie)e.getSource();
			sl.setStatus(AbstractLine.EDIT_STATUS);
			sl.repaint();
			sl.requestFocus();
		}
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() instanceof Barline){
			setCursor(moveCursor);
		}
		else if(e.getSource() instanceof LineMarker){
			setCursor(upDownCursor);
		}
		else if(e.getSource() == veil){
			int x = e.getX();
			int y = e.getY();
			int pageIndex = controller.getEditingPageIndex(x, y);
			Page page = score.getPageList().get(pageIndex);
			if(editNote.getParent() != page)
				page.add(editNote, JLayeredPane.PALETTE_LAYER);
			AbstractNote note = controller.getEditingNote(x, y, voice);
			
			//局部y坐标
			int yy = y - (Page.PAGE_HEIGHT + NoteCanvas.PAGE_GAP) * pageIndex;
			int lineIndex = controller.getEditingLineIndex(page, x, yy);
			NoteLine line = page.getNoteLines().get(lineIndex);
			int meaPartIndex = controller.getEditingMeaPartIndex(line, x, yy);
			MeasurePart meaPart = line.getMeaPartList().get(meaPartIndex);
			int meaIndex = controller.getEditingMeasureIndex(meaPart, x, yy);
			Measure measure = meaPart.getMeasure(meaIndex);
			
			int pitch = Math.round((measure.getY() + Measure.MEASURE_HEIGHT - yy) * 2 / LINE_GAP); 
			int notey = measure.getY() + measure.getHeight()-1 - (pitch) * LINE_GAP / 2 - editNote.getHeight()/2;
			
			//向无限制节拍小节的末尾添加音符
			if(note == null){
				if(measure.getNoteNum(voice) == 0)
					editNote.setLocation(measure.getX() + measure.getAttrWidth() + 2*LINE_GAP, notey);
				else{
					AbstractNote lnote = measure.getNote(measure.getNoteNum(voice)-1, voice);
					editNote.setLocation(lnote.getX() + lnote.getWidth() + 2*LINE_GAP, notey);
				}
			}
			//正常情况
			else
				editNote.setLocation(note.getX(), notey);
		}
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() instanceof Barline || e.getSource() instanceof LineMarker){
			setCursor(defaultCursor);
		}
	}
	
	/**
	 * 返回编辑时产生的新音符
	 * @param measure 点击的小节
	 * @param y 点击点在所在页内的Y坐标
	 * @return
	 */
	public Note getClickedNewNote(Measure measure, int y){
		Note newNote = null;
		
		if(symbolPanel.isRest()){
			newNote = new Note(symbolPanel.getCurDuration(), false);
		}
		else{
			int pitch = Math.round((measure.getY() + Measure.MEASURE_HEIGHT -y) * 2 / LINE_GAP);
			newNote = new Note(symbolPanel.getCurDuration(), pitch);
//			symbolPanel.addSymbolsListener(newNote);
		}
		newNote.setDotNum(symbolPanel.getDotNum());
		newNote.generateUIDot();
		return newNote;
		
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		//veil面板，编辑音符
		if(e.getSource() == veil && mode.equalsIgnoreCase("edit")){
			int voice = this.voice;
			int x = e.getX();
			int y = e.getY();
			int pageIndex = controller.getEditingPageIndex(x, y);
			Page page = score.getPageList().get(pageIndex);
			//局部y坐标
			int yy = y - (Page.PAGE_HEIGHT + NoteCanvas.PAGE_GAP) * pageIndex;
			int lineIndex = controller.getEditingLineIndex(page, x, yy);
			NoteLine line = page.getNoteLines().get(lineIndex);
			int meaPartIndex = controller.getEditingMeaPartIndex(line, x, yy);
			MeasurePart meaPart = line.getMeaPartList().get(meaPartIndex);
			int meaIndex = controller.getEditingMeasureIndex(meaPart, x, yy);
			Measure measure = meaPart.getMeasure(meaIndex);
			int noteIndex = controller.getEditingNoteIndex(measure, x, yy, voice);
			
			Note newNote = getClickedNewNote(measure, yy);
			controller.changeMeasureRythm(measure, noteIndex, newNote, voice);
			
			NoteLine nxtLine = line;
			while (redrawLine(nxtLine)) {
				nxtLine = MusicMath.nxtLine(nxtLine);
			}
			
		}
		
		//小节线
		if(e.getSource() instanceof Barline){
			screenPoint.setLocation(e.getXOnScreen(), e.getYOnScreen());
			accumWidthBeforeDrag = 0;
			Barline barline = (Barline)e.getSource();
			MeasurePart measurePart = barline.getMeaPart();
			NoteLine line = measurePart.getNoteLine();
			int meaPartIndex = line.getMeaPartList().indexOf(measurePart);
			for(int i = 0; i <= meaPartIndex; i++){
				MeasurePart part = line.getMeaPartList().get(i);
				accumWidthBeforeDrag += part.getWidth();
			}
			for(int i = 0; i <= meaPartIndex; i++){
				MeasurePart part = line.getMeaPartList().get(i);
				double ratio = (double)part.getWidth() / accumWidthBeforeDrag;
				ratios.add(ratio);
			}
			for(int i = meaPartIndex + 1; i < line.getMeaPartList().size(); i++){
				MeasurePart part = line.getMeaPartList().get(i);
				double ratio = (double)part.getWidth() / (lineWidth - accumWidthBeforeDrag);
				ratios.add(ratio);
			}
		}
		
		//水平标识符、音符等可拖动对象
		else if(e.getSource() instanceof LineMarker || e.getSource() instanceof Note){
			screenPoint.setLocation(e.getXOnScreen(), e.getYOnScreen());
		}
		
		//画板自身或页面
		else if(e.getSource() == this || e.getSource() instanceof Page){
			for(int i = 0; i < selectedObjects.size(); i++){
				if(selectedObjects.get(i) instanceof Lyrics){
					Lyrics ly = (Lyrics)selectedObjects.get(i);
					NoteLine line = ly.getNote().getMeasure().getMeasurePart().getNoteLine();
					while(redrawLine(line)){
						line = MusicMath.nxtLine(line);
					}
				}
			}
			//取消所有选择
			cancleAllSelected();
			//符号面板取消选择
			symbolPanel.cancleNoteBorder();
			symbolPanel.cancleRest();
			symbolPanel.cancleDotBorder();
		}
		
		//标注对象
		else if(e.getSource() instanceof Annotation || e.getSource() instanceof Annotation.ImagePanel){
			Annotation at = e.getSource() instanceof Annotation ? (Annotation)e.getSource() : 
				((Annotation.ImagePanel)e.getSource()).getAt();
			JComponent page = (JComponent)at.getParent();
			connBeginPoint.setLocation(at.getX(), at.getY() + page.getY());
			JComponent comp = (JComponent)at.getRelatedObjts().get(0);
			connEndPoint.setLocation(comp.getX(), comp.getY());
			veil.setVisible(true);
			veil.repaint();
		}
		
		//可选择对象
		if(e.getSource() instanceof Selectable){
			Selectable s = (Selectable)e.getSource();
			if(selectedObjects.contains(s))
				return;
			cancleAllSelected();
			selectedObjects.add(s);
			s.beSelected();
			symbolPanel.refreshButtonStatus(s);
		}
		
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() instanceof Barline || e.getSource() instanceof LineMarker){
			ratios.clear();
			accumWidthBeforeDrag = 0;
			if(dashedLine.isVisible())
				dashedLine.setVisible(false);
			setCursor(defaultCursor);
		}
		else if(e.getSource() instanceof Annotation || e.getSource() instanceof Annotation.ImagePanel){
			connBeginPoint.setLocation(0, 0);
			connEndPoint.setLocation(0, 0);
			veil.setVisible(false);
			veil.repaint();
		}
	}

	/**
	 * 获得乐谱
	 * @return
	 */
	public Score getScore() {
		return score;
	}
	
	/**
	 * 设置乐谱
	 * @param score
	 */
	public void setScore(Score score){
		this.score = score;
	}
	
	/**
	 * 获得当前声部
	 * @return
	 */
	public int getVoice() {
		return voice;
	}

	/**
	 * 设置当前声部
	 * @param voice
	 */
	public void setVoice(int voice) {
		this.voice = voice;
	}

	/**
	 * 返回控制器
	 * @return
	 */
	public Controller getController(){
		return controller;
	}

	/**
	 * 获得底部面板
	 * @return
	 */
	public BottomPanel getBottomPanel() {
		return bottomPanel;
	}
	

	/**
	 * 获得悬浮符号面板
	 * @return
	 */
	public SymbolPanel getSymbolPanel(){
		return symbolPanel;
	}
	
	/**
	 * 进入编辑状态
	 */
	public void editMode(){
		mode = "edit";
		veil.setVisible(true);
		editNote.setVisible(true);
		symbolPanel.reselectNoteBorder();
		symbolPanel.setEditPressed();
		//取消被选择对象的状态
		this.cancleAllSelected();
	}
	
	/**
	 * 进入观察状态
	 */
	public void viewMode(){
		mode = "view";
		veil.setVisible(false);
		editNote.setVisible(false);
		editNote.deletePosLines();
		symbolPanel.cancleNoteBorder();
		symbolPanel.cancleDotBorder();
		symbolPanel.cancleRest();
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == symbolPanel.getBtBarline()){
			if(mode.equalsIgnoreCase("edit"))
				return;
			if(selectedObjects.size() == 1 && selectedObjects.get(0) instanceof Barline){				
				BarlineDialog bd = new BarlineDialog();
				bd.setLocationRelativeTo(this);
				bd.setVisible(true);
				String type = bd.getSymbolType();
				Barline barline = (Barline)selectedObjects.get(0);
				barline.setType(type);
				barline.adjustSize();
				barline.setBarlineLocation(barline.getMeaPart().getX()+barline.getMeaPart().getWidth(),barline.getMeaPart().getY());
				System.out.println(barline.getType());
				JComponent pr = (JComponent) this.getParent();				
				pr.revalidate();  //支持推迟的自动布局
				pr.updateUI();
			}			
		}
		
		if(e.getSource() == symbolPanel.getBtMeasureMark()){
			
			if(selectedObjects.size() == 0 || score.getScoreType() == Score.SCORE_UNLMTED){
				return;
			}
			else if(selectedObjects.size() == 1 && selectedObjects.get(0) instanceof Measure){
					RepeatNoteDialog rnd = new RepeatNoteDialog();
					rnd.setLocationRelativeTo(this);
					rnd.setVisible(true);
					String type = rnd.getRepeat();
					
					if(!type.equalsIgnoreCase("none")){
						if(!type.equalsIgnoreCase("repeatLine")){
							RepeatSymbol rs = new RepeatSymbol(type);	
							Measure measure = (Measure)selectedObjects.get(0);
							MeasurePart meaPart = measure.getMeasurePart();
							
							meaPart.addRepeatSymbol(rs);
							measure.getParent().add(rs);
							meaPart.locateSymbols();
							rs.addMouseListener(this);
						}else if(type.equalsIgnoreCase("repeatLine")){
							//房子记号

							RepeatEndingDialog red = new RepeatEndingDialog();							
							red.setLocationRelativeTo(this);
							red.setVisible(true);
							
							int number = red.getNumber();
					        int[] numbers = red.getNumbers();

							if (selectedObjects.size() == 0 || score.getScoreType() == Score.SCORE_UNLMTED) {
								return;
							} else if (selectedObjects.size() == 1 && selectedObjects.get(0) instanceof Measure) {
								Measure measure = (Measure)selectedObjects.get(0);			
								MeasurePart mPart = measure.getMeasurePart();
								RepeatEnding rl = null;
								if(number != 0){
									rl = new RepeatEnding(mPart,number);
									rl.setStartMeasurePart(mPart);
									rl.setEndMeasurePart(mPart);
									mPart.getRepeatLines().add(rl); 
									measure.getParent().add(rl);
									rl.reShape();
									rl.requestFocus();
									rl.addMouseListener(this);
									rl.addKeyListener(this);
									cancleAllSelected();
									rl.setStatus(AbstractLine.EDIT_STATUS);
									rl.repaint();
									selectedObjects.add(rl);
								}else if(number == 0){
									if(numbers[0] != 0 || numbers[1] != 0 || numbers[2] != 0){
										rl = new RepeatEnding(mPart, numbers);
										rl.setEndMeasurePart(mPart);
										mPart.getRepeatLines().add(rl);
										measure.getParent().add(rl);
										rl.reShape();
										rl.requestFocus();
										rl.addMouseListener(this);
										rl.addKeyListener(this);
										cancleAllSelected();
										rl.setStatus(AbstractLine.EDIT_STATUS);
										rl.repaint();
										selectedObjects.add(rl);
									}
									
								}else{
									return;
								}		
							}
						}
					}
				}
		}
	
		if (e.getSource() == symbolPanel.getBtGraceSymbol()) {
			if (mode.equalsIgnoreCase("edit"))
				return;
			else if (selectedObjects.size() == 1
					&& selectedObjects.get(0) instanceof Note) {

				GraceSymbolDialog gsd = new GraceSymbolDialog();
				gsd.setLocationRelativeTo(this);
				// od.setLocation(this.getSymbolPanel().getX()-od.getWidth(),
				// this.getSymbolPanel().getY()+this.getSymbolPanel().getHeight()/2);
				gsd.setVisible(true);
				int attributes = gsd.getAttributes();

				Note note = (Note) selectedObjects.get(0);
				if (attributes == 0) {
					String type = gsd.getSymbolType();
					int number = gsd.getNumber();
					if (type != null) {
						AbstractNote cNote = note.getChordNote() == null ? note
								: note.getChordNote();
						if (note.isRest())
							return;
						if (note.hasOrnament(type))
							return;
						GraceSymbol gs = new GraceSymbol(type);
						gs.addMouseListener(this);
						cNote.addGraceSymbol(gs);
						note.getParent().add(gs);
						cNote.locateNoteSymbols();
					}
					// 颤音vibrate
					else if (number == 2) {
						symbolLineAdded(2);
					}
				} else if (attributes == 1) {
					int duration = gsd.getDuration();
					boolean hasSlash = gsd.hasSlash;
					String direction = gsd.getDirection();
					// 不能为倚音或者休止符添加倚音
					if (note instanceof Grace || note.isRest())
						return;
					Grace grace = new Grace(duration, note.getPitch(), hasSlash);
					AbstractNote anote = note.getChordNote() == null ? note
							: note.getChordNote();
					if (direction != null) {
						if (direction.equalsIgnoreCase("left"))
							anote.addLeftGrace(grace);
						else if (direction.equalsIgnoreCase("right"))
							anote.addRightGrace(grace);
						else
							return;
						((JComponent) note.getParent()).add(grace);
						grace.addMouseListener(this);
						grace.addMouseMotionListener(this);
						grace.addKeyListener(this);

						NoteLine line = note.getMeasure().getMeasurePart()
								.getNoteLine();
						while (redrawLine(line)) {
							line = MusicMath.nxtLine(line);
						}
					}
				}
			}
		}
	 
		if(e.getSource() == symbolPanel.getBtOrnament()){
		if(mode.equalsIgnoreCase("edit"))
			return;
		else if(selectedObjects.size() == 1 && selectedObjects.get(0) instanceof Note){
			
			OrnamentDialog od = new OrnamentDialog();
			od.setLocationRelativeTo(this);
			od.setVisible(true);
			String type = od.getSymbolType();
			System.out.println("oooo: " + type);
			if(!type.equalsIgnoreCase("none")){
				Note note = (Note)selectedObjects.get(0);
				AbstractNote cNote = note.getChordNote() == null ? note : note.getChordNote();
				if(note.isRest())
					return;
				if(note.hasOrnament(type))
					return;
				if(type == "accent"|| type == "strongAccentDown"|| type == "strongAccentUp"|| type == "staccato"
					|| type == "tenuto"|| type == "staccatissimoDown"|| type == "staccatissimoUp"|| type == "staccatoTenutoUp"
						|| type == "staccatoTenutoDown"|| type == "fermata"){
					Ornament o1 = new Ornament(type);
					o1.addMouseListener(this);
			        cNote.addOrnament(o1);
			        note.getParent().add(o1);       
			        cNote.locateNoteSymbols();   
			        
				}else if(type == "pedalStart"){
					pedalAdded("start");
				}else if(type == "pedalEnd"){
					pedalAdded("stop");
				}else if(type == "breath"){
					breathAdded();
				}else if(type == "tremoloBeam1"){
					tremoloBeamAdded("tremoloBeam1");
				}else if(type == "tremoloBeam2"){
					tremoloBeamAdded("tremoloBeam2");
				}else if(type == "tremoloBeam3"){
					tremoloBeamAdded("tremoloBeam3");
				}
			 			        
			}
		}
	}
		
		if(e.getSource() == symbolPanel.getBtEllipsis()){
			if(mode.equalsIgnoreCase("edit"))
				return;
			else if(selectedObjects.size() == 1 && selectedObjects.get(0) instanceof Note){
				
				EllipsisDialog ed = new EllipsisDialog();
				ed.setLocationRelativeTo(this);
				ed.setVisible(true);
				String type = ed.getSymbolType();
		
				
				if(!type.equalsIgnoreCase("none")){
					Note note = (Note)selectedObjects.get(0);
				//	AbstractNote cNote = note.getChordNote() == null ? note : note.getChordNote();
					if(note.isRest())
						return;
					if(type == "tremoloBeam1"){
						tremoloBeamAdded("tremoloBeam1");
					}else if(type == "tremoloBeam2"){
						tremoloBeamAdded("tremoloBeam2");
					}else if(type == "tremoloBeam3"){
						tremoloBeamAdded("tremoloBeam3");
					}
				 			        
				}
			}
		}
		if(e.getSource() == symbolPanel.getBtDynamics()){
			if(mode.equalsIgnoreCase("edit"))
				return;
			else if(selectedObjects.size() == 1 && selectedObjects.get(0) instanceof Note){
				DynamicDialog dd = new DynamicDialog();
				dd.setLocationRelativeTo(this);
				dd.setVisible(true);
				String type = dd.getSymbolType();
				
				if(!type.equalsIgnoreCase("none")){
					Note note = (Note)selectedObjects.get(0);
					AbstractNote cNote = note.getChordNote() == null ? note : note.getChordNote();
					Dynamic d1 = new Dynamic(type);
					d1.addMouseListener(this);
			        cNote.addDynamics(d1);
			        note.getParent().add(d1);       
			        cNote.locateNoteSymbols();
				}
				
			}
		}
		
		if(e.getSource() == symbolPanel.getBtTempo()){
			if((selectedObjects.size() == 1 && selectedObjects.get(0) instanceof Note)){
				TempoDialog td = new TempoDialog();
				td.setLocationRelativeTo(this);
				td.setVisible(true);
				String name = td.getText();
				if(name != null){
					int tempo = td.getNumber();
					boolean displayTempo = td.displayTempo();
					tempoAdded(name, tempo, displayTempo);
				}
			}
		}
		
		
		if(e.getSource() == symbolPanel.getBtSymbolLine()){
			if(mode.equalsIgnoreCase("edit"))
				return;
			else if(selectedObjects.size() == 1 && selectedObjects.get(0) instanceof Note){		
				LineSymbolDialog lsd = new LineSymbolDialog();
				lsd.setLocationRelativeTo(this);
				lsd.setVisible(true);
				int number = lsd.getNumber();
				if(number != 0){
				symbolLineAdded(number);
			}
			}
		}
		
		if(e.getSource() == symbolPanel.getBtKey()){
			if(selectedObjects.size() == 0){
				return;
			}
			else if(selectedObjects.size() == 1 && (selectedObjects.get(0) instanceof Note || selectedObjects.get(0) instanceof Measure)){
				KeyDialog kd = new KeyDialog();
				kd.setLocationRelativeTo(this);
				kd.setVisible(true);
				int key = kd.getKey();
				//用户选择取消
				if(key > 7)
					return;
				
				Measure measure = null;
				if(selectedObjects.get(0) instanceof Note){
					Note note = (Note)selectedObjects.get(0);
					measure = note.getChordNote() == null ? note.getMeasure() : 
						note.getChordNote().getMeasure();
				}
				else if(selectedObjects.get(0) instanceof Measure){
					measure = ((Measure)selectedObjects.get(0));
				}
				MeasurePart measurePart = measure.getMeasurePart();
				int meaIndex = measurePart.measureIndex(measure);
				//action
				ChangeMeaKeyAction action = new ChangeMeaKeyAction(measure.getKeyValue(), key, measurePart, meaIndex, controller);
				actionController.pushAction(action);

				controller.changeKey(measurePart, meaIndex, key);
				//重绘
				NoteLine line = measurePart.getNoteLine();
				while(line != null){
					redrawLine(line);
					line = MusicMath.nxtLine(line);
				}
			}
		}
	 	if(e.getSource() == symbolPanel.getBtTime()){	 
			if(selectedObjects.size() == 0 || score.getScoreType() == Score.SCORE_UNLMTED){
				return;
			}
			else if(selectedObjects.size() == 1 && (selectedObjects.get(0) instanceof Note || selectedObjects.get(0) instanceof Measure)){
				TimeDialog td = new TimeDialog();
				td.setLocationRelativeTo(this);
				td.setVisible(true);
				int beats = td.getBeats();
				int beatType = td.getBeatType();
				int[] beat = td.getBeat();
				
				//用户选择的取消

				if(beats == -1){
					if(beatType == -1){
						return;
					}else{
						MeasurePart smeaPart = null;
						if(selectedObjects.get(0) instanceof Note){
							Note note = (Note)selectedObjects.get(0);
							smeaPart = note.getChordNote() == null ? note.getMeasure().getMeasurePart() : 
								note.getChordNote().getMeasure().getMeasurePart();
						}
						else if(selectedObjects.get(0) instanceof Measure){
							smeaPart = ((Measure)selectedObjects.get(0)).getMeasurePart();
						}
						controller.changeTime(smeaPart, new Time(beat, beatType));
						NoteLine line = smeaPart.getNoteLine();
						while(line != null){
							redrawLine(line);
							line = MusicMath.nxtLine(line);
						}
					}
				}else{		
				
					MeasurePart smeaPart = null;
					if(selectedObjects.get(0) instanceof Note){
						Note note = (Note)selectedObjects.get(0);
						smeaPart = note.getChordNote() == null ? note.getMeasure().getMeasurePart() : 
							note.getChordNote().getMeasure().getMeasurePart();
					}
					else if(selectedObjects.get(0) instanceof Measure){
						smeaPart = ((Measure)selectedObjects.get(0)).getMeasurePart();
					}
					controller.changeTime(smeaPart, new Time(beats, beatType));
					NoteLine line = smeaPart.getNoteLine();
					while(line != null){
						redrawLine(line);
						line = MusicMath.nxtLine(line);
					}
				}
			}
		}
		
		if(e.getSource() == symbolPanel.getBtClef()){
			if(selectedObjects.size() == 0){
				return;
			}
			else if(selectedObjects.size() == 1 && (selectedObjects.get(0) instanceof Note || selectedObjects.get(0) instanceof Measure)){
				ClefDialog cd = new ClefDialog();
				cd.setLocationRelativeTo(this);
				cd.setVisible(true);
				String clefType = cd.getClef();
				//用户选择的取消
				if(clefType == null)
					return;
				
				Measure measure = null;
				if(selectedObjects.get(0) instanceof Note){
					Note note = (Note)selectedObjects.get(0);
					measure = note.getChordNote() == null ? note.getMeasure() : 
						note.getChordNote().getMeasure();
				}
				else if(selectedObjects.get(0) instanceof Measure){
					measure = ((Measure)selectedObjects.get(0));
				}
				MeasurePart measurePart = measure.getMeasurePart();
				int meaIndex = measurePart.measureIndex(measure);
				//action
				ChangeMeaClefAction action = new ChangeMeaClefAction(measure.getClefType(), clefType, measurePart, meaIndex, controller);
				actionController.pushAction(action);

				controller.changeClef(measurePart, meaIndex, clefType);
				//重绘
				NoteLine line = measurePart.getNoteLine();
				while(line != null){
					redrawLine(line);
					line = MusicMath.nxtLine(line);
				}
			}
		}
		
		if(e.getSource() == symbolPanel.getBtPerformance()){
			
			if(mode.equalsIgnoreCase("edit"))
				return;
			else if(selectedObjects.size() == 1 && selectedObjects.get(0) instanceof Note){
				PerformanceDialog pd = new PerformanceDialog();
				pd.setLocationRelativeTo(this);
				//dd.setLocation(this.getSymbolPanel().getX()-dd.getWidth(), this.getSymbolPanel().getY()+this.getSymbolPanel().getHeight()/2);
				pd.setVisible(true);
				String type = pd.getSymbolType();
//				System.out.println(type);
				
				if(!type.equalsIgnoreCase("none")){
					Note note = (Note)selectedObjects.get(0);
					AbstractNote cNote = note.getChordNote() == null ? note : note.getChordNote();
					PerformanceSymbol ps = new PerformanceSymbol(type);
					ps.addMouseListener(this);
//			        	cNote.addOrnament(ps);
					cNote.addPerformanceSymbols(ps);
			        	note.getParent().add(ps);       
			        	cNote.locateNoteSymbols();
				}
			}
		}
		
		else if(e.getSource() == symbolPanel.getBtText()){
			if(mode.equalsIgnoreCase("edit"))
				return;
			String txtType = TextDialog.showDialog(null, "");
			if(txtType != null && txtType.equals("free")){
				tryAddFreeText();
			}
			else if(txtType != null && txtType.equals("lyric")){
				lyricAdded();
			}
			else if(txtType != null && txtType.equals("annotation")){
				annotationAdded();
			}
		}
		
		if(e.getSource() == symbolPanel.getBtTuplet()){
			tryAddTuplet();
		}
		if(e.getSource() == topPanel.getMenuImport()){
			tryImportXML();
		}
		else if(e.getSource() == topPanel.getMenuNew()){
			tryCreateNewScore();
		}
		else if(e.getSource() == topPanel.getMenuXML()){
			tryExportXML();
		}
		else if (e.getSource() == topPanel.getMenuPicture()) {
			tryExportPicture();
		}
		else if(e.getSource() == topPanel.getMenuLayout()){
			rearangeScore((int)(1 * LINE_GAP));
		}
		else if(e.getSource() == topPanel.getAddPartUp()){
			tryAddInstrument("up");
		}
		else if(e.getSource() == topPanel.getAddPartDown()){
			tryAddInstrument("down");
		}
		else if(e.getSource() == topPanel.getRemoveStaff()){
			tryRemoveVoice();
		}
		else if(e.getSource() == topPanel.getAddMea() || e.getSource() == symbolPanel.getBtAddMeasure()){
			tryAddMeasure();
		}
		else if(e.getSource() == topPanel.getDelMea() || e.getSource() == symbolPanel.getBtDeleteMeasure()){
			tryRemoveMeasure();
		}
		else if(e.getSource() == topPanel.getMenuAutoLayout()){
			rearangeScore((int)(1 * LINE_GAP));
		}
		else if(e.getSource() == topPanel.getMenuPrint()){
			PrinterJob pb = PrinterJob.getPrinterJob();
			pb.setPrintable(this);
			if(pb.printDialog()){
				try{
					pb.print();
				}catch(PrinterException ee){
					ee.printStackTrace();
				}
			}
		}
	}
	
	private void tryAddFreeText(){
		FreeAddedText txt = new FreeAddedText();
		int pageIndex = getCurPageIndex();
		Page page = score.getPageList().get(pageIndex - 1);
		page.add(txt);
		txt.setLocation(page.getWidth()/2, getPageOffSet()+getWindowHeight()/2);
		txt.requestFocus();
		txt.editMode();
		page.revalidate();
		txt.addMouseListener(this);
		cancleAllSelected();
		selectedObjects.add(txt);
		score.addFreeText(txt);
	}
	
	private void tryAddTuplet(){
		if(mode.equalsIgnoreCase("edit"))
			return;
		else if(selectedObjects.size() == 1 && selectedObjects.get(0) instanceof Note){
			TupletDialog td = new TupletDialog();
			td.setLocationRelativeTo(this);
			td.setVisible(true);
			Note note = (Note)selectedObjects.get(0);
			int num = td.getNumber();
			switch(num){
			case 3:
				controller.addTuplet(note, 3, 2);
				break;
			case 4:
				controller.addTuplet(note, 4, 3);
				break;
			case 5:
				controller.addTuplet(note, 5, 4);
				break;
			case 6:
				controller.addTuplet(note, 6, 5);
				break;
			case 7:
				controller.addTuplet(note, 7, 6);
				break;
			case 8:
				controller.addTuplet(note, 8, 7);
				break;
			}
			//重绘
			NoteLine line = note.getChordNote() == null ? note.getMeasure().getMeasurePart().getNoteLine() :
				note.getChordNote().getMeasure().getMeasurePart().getNoteLine();
			while(redrawLine(line)){
				line = MusicMath.nxtLine(line);
			}
		}
	}
	
	private void tryCreateNewScore(){
		NewScoreDialog nsd = new NewScoreDialog();
		nsd.setLocationRelativeTo(this);
		nsd.setVisible(true);
		int scoreType = nsd.getScoreType();
		boolean hasTitle = nsd.hasTitle();
		//点击取消
		if(scoreType == -1)
			return;
		newScore(scoreType, hasTitle);
	}
	
	private void tryImportXML(){
		FileDialog fd = new FileDialog(new JFrame(), "XML文档", FileDialog.LOAD);
	    fd.setVisible(true);
	    int pageBefore =score.getPageList().size();
		String sfile;
		if((sfile = fd.getFile()) != null){
			String dir = fd.getDirectory();
			File file = new File(dir+sfile);
			XMLParser parser = new XMLParser(file, controller);
			parser.readFromXML();
			
			int pageNow = parser.getScore().getPageList().size();
			if(parser.getScore().getPageList().size()>20){				
				Object[] options = { "继续", "取消" };
				int response = JOptionPane.showOptionDialog(null, "乐谱页面过多，导入需要一些时间，是否继续？", "Warning", 
				JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
				null, options, options[0]);
				if(response==0){
					getBottomPanel().getButtonGroup().refreshPageButton(pageBefore,pageNow);
					disposeAll();
					score = parser.getScore();
					rearangePages(0);
					
					//页数大于20，生成进度条
					ProgressDialog pm = null;
					ProgressThread thread = null;
					if(score.getPageList().size() > 20){
						pm = new ProgressDialog(0, score.getPageList().size());
						Dimension dm = getToolkit().getScreenSize();
						pm.setLocation(((int)dm.getWidth() - pm.getWidth())/2, ((int)dm.getHeight() - pm.getHeight())/2);
						thread = new ProgressThread(pm);
						thread.start();
					}
					for(int i = 0; i < score.getPageList().size(); i++){
						if(pm != null){
							thread.setN(i+1);
						}
						Page page = score.getPageList().get(i);
						add(page);
						MeaPartStrategy mpst = new MeaPartStrategy(X_MIN_DIST, false);
						NoteLineStrategy nls = new NoteLineStrategy(mpst, false);
						PageStrategy ps = new PageStrategy(nls, true);
						redrawPage(page, ps);
					//	System.out.println("导入第" + (i+1) + "页");
					}
					pm.dispose();
					
					redrawSymbolLinesInScore();
					redrawRepeatLineInScore();
					redrawFreeTextInScore();
					updateUI();						
				}
				
			}else{
				getBottomPanel().getButtonGroup().refreshPageButton(pageBefore,pageNow);
				disposeAll();
				score = parser.getScore();
				rearangePages(0);
				for(int i = 0; i < score.getPageList().size(); i++){
					Page page = score.getPageList().get(i);
					if(page.getParent() != null){
						page.getParent().remove(page);
					}
					add(page, JLayeredPane.DEFAULT_LAYER);
					MeaPartStrategy mpst = new MeaPartStrategy(X_MIN_DIST, false);
					NoteLineStrategy nls = new NoteLineStrategy(mpst, false);
					PageStrategy ps = new PageStrategy(nls, true);
					redrawPage(page, ps);
				}
				redrawSymbolLinesInScore();
				redrawRepeatLineInScore();
				redrawFreeTextInScore();
				updateUI();
			}
		}
	}
	
	private void tryExportXML(){
		System.out.println("EXPORT");
		FileDialog fd = new FileDialog(new JFrame(), "XML文档", FileDialog.SAVE);
		//FileDialog（）创建一个指定标题的文件对话框窗口，用于加载或保存文件
	    fd.setVisible(true);
	    String sfile = null;
	    if((sfile = fd.getFile()) != null){
	    	String dir = fd.getDirectory();
			File file = new File(dir+sfile);
			XMLParser parser = new XMLParser(score);
			parser.writeDocumentToFile(parser.makeXML(), file);
	    }
	}
	
	private void tryExportPicture(){
		FileDialog fd = new FileDialog(new JFrame(), "图片", FileDialog.SAVE);
		fd.setVisible(true);
		String sfile;
		if ((sfile = fd.getFile()) != null) {
			String dir = fd.getDirectory();//将此文件对话框窗口的目录设置为指定目录
			String[] subs = sfile.split("\\.");
			if(subs.length > 0){
				String append = subs[subs.length - 1];
				if(!append.equalsIgnoreCase("jpg")){
					sfile = sfile +".jpg";
				}
			}else{
				sfile = sfile + ".jpg";
			}
			
			File file = new File(dir + sfile);
        		ImageHandler.saveScoreToPictures(file, score);
		}
	}
	
	private void tryAddInstrument(String upOrDown){
		if(selectedObjects.size() == 0){
			return;
		}
		else if(selectedObjects.size() == 1 && (selectedObjects.get(0) instanceof Note || selectedObjects.get(0) instanceof Measure)){
			HashMap<String, String> pa;
			InstrumentDialog id = new InstrumentDialog();
			pa = id.getParameters();
			if(pa == null){
				return;
			}
			Measure smeasure = null;
			if(selectedObjects.get(0) instanceof Note){
				Note note = (Note)selectedObjects.get(0);
				smeasure = note.getChordNote() == null ? note.getMeasure() : note.getChordNote().getMeasure();
			}else{
				smeasure = (Measure)selectedObjects.get(0);
			}
			MeasurePart smeaPart = smeasure.getMeasurePart();
			//被选对象所在的小节处于第几个乐器
			int instrIndex = smeaPart.getInstrumentIndex(smeasure);
			int newInstrument = Integer.parseInt(pa.get("instrument"));
			controller.addInstrument(instrIndex, newInstrument, upOrDown);
		}
		//重新布局
		for(int i = 0; i < score.getPageList().size(); i++){
			Page page = score.getPageList().get(i);
			redrawPage(page);
		}
	}
	
	private void tryRemoveMeasure(){
		if(selectedObjects.size() == 1 && selectedObjects.get(0) instanceof Measure){
			Measure measure = (Measure)selectedObjects.get(0);
			cancleAllSelected();
			MeasurePart measurePart = measure.getMeasurePart();
			Page page = measurePart.getNoteLine().getPage();
			DelMeasureAction action = new DelMeasureAction(MusicMath.preMeasurePart(measurePart), measurePart, controller);
			actionController.pushAction(action);
			boolean flag = controller.deleteMeasure(measurePart, false);
			if(flag){
				MeaPartStrategy mpst = new MeaPartStrategy(X_MIN_DIST, false);
				NoteLineStrategy nls = new NoteLineStrategy(mpst, false);
				//允许吸收下一页
				PageStrategy ps = new PageStrategy(nls, true);
				while(redrawPage(page, ps)){
					page = MusicMath.nxtPage(score, page);
				}
			}
			else{
				redrawLine(measurePart.getNoteLine());
			}
		}
	}
	
	private void tryAddMeasure(){
		if(selectedObjects.size() == 1 && selectedObjects.get(0) instanceof Measure){
			Measure measure = (Measure)selectedObjects.get(0);
			cancleAllSelected();
			AddMeasureDialog amd = new AddMeasureDialog();
			int meaNum = amd.getMeasureNum();
			//用户点击取消，或者直接关闭对话框
			if(meaNum == -1 || meaNum < 1)
				return;
			//点击确定
			AddMeasureAction action = new AddMeasureAction(measure, meaNum, controller);
			actionController.pushAction(action);
			MeasurePart meaPart = measure.getMeasurePart();
			controller.addMeasure(meaPart, meaNum);
			NoteLine line = meaPart.getNoteLine();
			while(redrawLine(line)){
				line = MusicMath.nxtLine(line);
			}
//			adjustSize(score);
		}
	}
	
	private void tryRemoveVoice(){
		if(selectedObjects.size() == 0){
			return;
		}
		else if(selectedObjects.size() == 1 && (selectedObjects.get(0) instanceof Note || selectedObjects.get(0) instanceof Measure)){
			Measure smeasure = null;
			if(selectedObjects.get(0) instanceof Note){
				Note note = (Note)selectedObjects.get(0);
				smeasure = note.getChordNote() == null ? note.getMeasure() : note.getChordNote().getMeasure();
			}else{
				smeasure = (Measure)selectedObjects.get(0);
			}
			cancleAllSelected();
			
			MeasurePart meaPart = smeasure.getMeasurePart();
			int measureIndex = meaPart.measureIndex(smeasure);
			controller.removePart(measureIndex);
			
			//重新布局
			for(int i = 0; i < score.getPageList().size(); i++){
				Page page = score.getPageList().get(i);
				
				MeaPartStrategy mpst = new MeaPartStrategy(X_MIN_DIST, false);
				NoteLineStrategy nls = new NoteLineStrategy(mpst, false);
				PageStrategy ps = new PageStrategy(nls, true);
				
				redrawPage(page, ps);
			}
		}
	}
	
	
	/*************************************************************************/
	class ProgressThread extends Thread{
		private int n = 0;
	    private ProgressDialog pd;
		public ProgressThread(ProgressDialog pd){
			this.pd = pd;
		}
		public void run(){
			while(pd.getValue() <= pd.getMax()){
				pd.setValue(n);
				pd.refresh();
				try{
					Thread.sleep(20);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		public void setN(int n){
			this.n = n;
		}
	}
	/*************************************************************************/
	
	
	/**
	 * 悬浮面板的附点按钮被按下
	 * 在音符被选中时，改变所选音符的附点
	 * 该方法通常是由悬浮面板进行调用
	 * @return 
	 */
	public void dotChanged(){
		//只有视图模式才有效
		if(mode.equalsIgnoreCase("edit"))
			return;
		if(selectedObjects.size() == 1 && selectedObjects.get(0) instanceof Note){ 
			
			/*
			 * size()返回此列表中的元素数,get(int index)返回此列表中指定位置上的元素
			 *instanceof 它的作用是测试它左边的对象是否是它右边的类的实例，返回boolean类型的数据,
			 *可以用在继承中的子类的实例是否为父类的实现。
			*/
			
			Note note = (Note)selectedObjects.get(0);
			boolean flag = false;
			int dotNum = symbolPanel.getDotNum();    //getDotNum()返回dotNum。
			//System.out.println("dotNum =" + dotNum );
			
			//添加附点
			if(dotNum != 0){       //dotNum附点个数
				flag = controller.addDotForNote(note, dotNum);
			}//删除附点
			else
				flag = controller.removeDotFromNote(note);
			
			if(flag){
				NoteLine line = note.getMeasure().getMeasurePart().getNoteLine();
				while(redrawLine(line)){
						line = MusicMath.nxtLine(line);
				}
			}
			
		}
	}
	
	/**
	 * 悬浮面板的音符按钮被按下
	 * 在音符被选中时，改变被选中音符的类型
	 * 改变成功时返回true, 否则返回false.
	 * @return
	 */
	public boolean noteChanged(){
		//只有视图模式才有效
		if(mode.equalsIgnoreCase("edit"))
			return false;
		if(selectedObjects.size() == 1 && selectedObjects.get(0) instanceof Note){
			Note note = (Note)selectedObjects.get(0);
			int voice = note.getVoice();
			
			int newDuration = symbolPanel.getCurDuration();
			int dotNum = note.getDotNum();
			Measure measure = null;
			int noteIndex = 0;
			if(note.getChordNote() == null){
				measure = note.getMeasure();
				noteIndex = measure.noteIndex(note);
			}else{
				measure = note.getChordNote().getMeasure();
				noteIndex = measure.noteIndex(note.getChordNote());
			}
			
			//休止符或音符
			Note newNote = note.isRest() ? new Note(newDuration, dotNum, false) :
				new Note(newDuration, note.getPitch(), dotNum);
			boolean flag = controller.changeMeasureRythm(measure, noteIndex, newNote, voice);
			
			if(flag){
				NoteLine line = measure.getMeasurePart().getNoteLine();
				while(redrawLine(line)){
					line = MusicMath.nxtLine(line);
				}
			}
			return flag;
		}
		else if(selectedObjects.isEmpty()){
			editMode();
			return false;
		}
		else 
			return false;
	}
	
	/**
	 * 符号面板的符杠类型按钮被按下，改变被选中音符的符杠类型
	 * @param type 类型参数，有效值:begin, continue, none
	 */
	public void beamChanged(String type){
		if(mode.equalsIgnoreCase("edit"))
			return;
		
		if(selectedObjects.size() == 1 && selectedObjects.get(0) instanceof Note){
			Note note = (Note)selectedObjects.get(0);
			AbstractNote anote = note.getChordNote() == null ? note : note.getChordNote();
			Measure measure = anote.getMeasure();
			
			//不可能具有符杠
			if(note.isRest() || note.getDuration() >= 64)
				return;
			
			anote.setBeamType(type);
			deleteMeasureBeamAndStem(measure.getMeasurePart(), measure);
			measure.drawBeamAndStem();
		}
	}
	
	/**
	 * 符号面板上的线条符号按钮被按下,为被选音符生成连句线
	 * 通常由符号面板调用
	 * @param type
	 */
	public void symbolLineAdded(int type){
		if(mode.equalsIgnoreCase("edit"))
			return;
		if(selectedObjects.size() == 1 && selectedObjects.get(0) instanceof Note){
	
			Note note = (Note)selectedObjects.get(0);
			AbstractNote curNote = note.getChordNote() == null ? note : note.getChordNote();
			AbstractNote nxtNote = MusicMath.nxtNote(curNote);
			
			//当前已经是最后一个音符
			if(nxtNote == null)
				return;
			
			SymbolLine sl = LineFactory.generateSymbolLine(type);
			sl.setStartNote(curNote);
			sl.setEndNote(nxtNote);
			curNote.getSymbolLines().add(sl);
			nxtNote.getSymbolLines().add(sl);
			//如果是连句号等符号，判断初始形状
			if(sl instanceof Slur){
				Slur slur = (Slur)sl;
				slur.determineUpOrDown();
				slur.repaint();
			}
			sl.reShape();
			sl.requestFocus();
			
			//为符合添加侦听器
			SymbolLine nxtsl = sl;
			while(nxtsl != null){
				nxtsl.addMouseListener(this);
				nxtsl.addKeyListener(this);
				nxtsl = (SymbolLine)nxtsl.getNextSymbolLine();
			}
			//将被选择的焦点转移到生成的线条符号上面
			cancleAllSelected();
			sl.setStatus(AbstractLine.EDIT_STATUS);
			sl.repaint();
			selectedObjects.add(sl);
		}
	}
	
	/**
	 * 删除按钮被按下，删除当前被选择对象
	 */
	public void deleteSomething(){
		for(int i = 0, n = selectedObjects.size(); i < n; i++){
			Selectable s = selectedObjects.get(i);
			s.cancleSelected();
			controller.deleteObject(s);
		}
		selectedObjects.clear();
		revalidate();
		updateUI();
	}
	
	/**
	 * 符号面板上的连音线按钮被按下，为被选音符生成连音线
	 * 通常由符号面板调用
	 */
	public void tieChanged(){
		if(mode.equalsIgnoreCase("edit"))
			return;
		if(selectedObjects.size() == 1 && selectedObjects.get(0) instanceof Note){
			Note note = (Note)selectedObjects.get(0);
			//起始不能是休止符
			if(note.isRest())
				return;
			AbstractNote curNote = note.getChordNote() == null ? note : note.getChordNote();
			AbstractNote nxtNote = MusicMath.nxtNote(curNote);
			Note nnote = nxtNote.getNoteWithPitch(note.getPitch());
			
			//下一个音符存在与之相同音高的音符
			if(nnote != null){
				Tie tie = new Tie(note, nnote);
			//	tie.determineUpOrDown();
				tie.reShape();
				tie.addMouseListener(this);
				Tie nxttie = (Tie)tie.getNextSymbolLine();
				if(nxttie != null)
					nxttie.addMouseListener(this);
			}
		}
	}
	
	/**
	 * 连音符按钮被按下
	 */
	public void tupletAdded(int modification, int normal){
		if(mode.equalsIgnoreCase("edit"))
			return;
		if(selectedObjects.size() == 1 && selectedObjects.get(0) instanceof Note){
			Note note = (Note)selectedObjects.get(0);
			controller.addTuplet(note, modification, normal);
			
			//重绘
			NoteLine line = note.getChordNote() == null ? note.getMeasure().getMeasurePart().getNoteLine() :
				note.getChordNote().getMeasure().getMeasurePart().getNoteLine();
			while(redrawLine(line)){
				line = MusicMath.nxtLine(line);
			}
		}
	}
	
	/**
	 * 取消当前所有被选择对象被选择状态
	 */
	public void cancleAllSelected(){
		if(!selectedObjects.isEmpty()){
			for(Selectable s : selectedObjects){
				s.cancleSelected();
			}
			selectedObjects.clear();
		}
	}
	
	/**
	 * 重绘乐谱中的线条符号(包括连音线)，仅在导入数据时调用.
	 */
	public void redrawSymbolLinesInScore(){
		for(Page page : score.getPageList()){
			for(NoteLine line : page.getNoteLines()){
				redrawSymbolLinesInLine(line);
			}
		}
	}
	
	/**
	 * 重绘行内的线条符号，包括连音线.
	 * （重绘行内所有取决于起始音符位置的符号，包括线条符号、连音号、连音线）
	 * @param line
	 */
	public void redrawSymbolLinesInLine(NoteLine line){
		for(MeasurePart measurePart : line.getMeaPartList()){
			for(int i = 0, n = measurePart.getMeasureNum(); i < n; i++){
				Measure measure = measurePart.getMeasure(i);
				for(int v = 0, vn = measure.getVoiceNum(); v < vn; v++){
					for(int j = 0, nj = measure.getNoteNum(v); j < nj; j++){
						AbstractNote note = measure.getNote(j, v);
						//各种线条符号
						if(!note.getSymbolLines().isEmpty()){
							for(SymbolLine sl : note.getSymbolLines()){
								if(sl instanceof Slur){
									((Slur)sl).determineUpOrDown();
									sl.repaint();
								}
								sl.reShape();
							}
						}
						//连音线
						if(note instanceof Note && ((Note)note).getTieNum() != 0){
							for(int k = 0; k < ((Note)note).getTieNum(); k++){
								Tie tie = ((Note)note).getTie(k);
								tie.reShape();
							}
						}else if(note instanceof ChordNote){
							for(int k = 0; k < ((ChordNote)note).getNoteNum(); k++){
								Note nnote = ((ChordNote)note).getNote(k);
								for(int m = 0; m < nnote.getTieNum(); m++){
									nnote.getTie(m).reShape();
								}
							}
						}
						//连音号
						if(note.getTuplet() != null){
							Tuplet tup = note.getTuplet();
							tup.adjustSize();
							tup.repaint();
							tup.locateTuplet();
						}
					}
				}
			}
		}
	}
	
	/**
	 * 重绘乐谱中和小节相关的线条记号，在导入数据时调用
	 */
	public void redrawRepeatLineInScore(){
		for(Page page : score.getPageList()){
			for(NoteLine line : page.getNoteLines()){
				redrawRepeatLineInLine(line);
			}
		}
	}
	
	/**
	 * 绘制自由文本
	 */
	public void redrawFreeTextInScore(){
		for(FreeAddedText txt : score.getAddedTexts()){
			Page page = null;
			if(txt.getPageIndex() < score.getPageList().size())
				page = score.getPageList().get(txt.getPageIndex());
			else 
				page = score.getPageList().get(score.getPageList().size()-1);
			page.add(txt);
			txt.cancleSelected();
			page.revalidate();
		}
	}
	
	/**
	 * 重绘乐谱中和小节相关的线条记号（例如房子记号）
	 * @param line
	 */
	public void redrawRepeatLineInLine(NoteLine line){
		
		for(int i = 0; i < line.getMeaPartList().size(); i++){
	
			if(!line.getMeaPartList().get(i).getRepeatLines().isEmpty()){
				
				for(int j = 0; j < line.getMeaPartList().get(i).getRepeatLines().size(); j++){
					RepeatLine rl = line.getMeaPartList().get(i).getRepeatLines().get(j);
					rl.reShape();
				}
//				for(RepeatLine rl : line.getMeaPartList().get(i).getRepeatLines()){
//					rl.reShape();
//				}
			}
		}

	}
	
	
	
	

	/**
	 * 获得被选择的对象集合
	 * @return
	 */
	public ArrayList<Selectable> getSelectedObjects() {
		return selectedObjects;
	}

	/**
	 * 获得连接线起点
	 * @return
	 */
	public Point getConnBeginPoint() {
		return connBeginPoint;
	}

	/**
	 * 设置连接线起点
	 * @param connBeginPoint
	 */
	public void setConnBeginPoint(Point connBeginPoint) {
		this.connBeginPoint = connBeginPoint;
	}

	/**
	 * 获得连接线终点
	 * @return
	 */
	public Point getConnEndPoint() {
		return connEndPoint;
	}

	/**
	 * 设置连接线终点
	 * @param connEndPoint
	 */
	public void setConnEndPoint(Point connEndPoint) {
		this.connEndPoint = connEndPoint;
	}

	/**
	 * 获得画板顶层的玻璃面板
	 * @return
	 */
	public Veil getVeil() {
		return veil;
	}

	/**
	 * 返回顶层面板
	 * @return
	 */
	public TopPanel getTopPanel() {
		return topPanel;
	}

	/**
	 * 获得模式
	 * @return
	 */
	public String getMode() {
		return mode;
	}

	/**
	 * 设置模式
	 * @param mode
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}

	/**
	 * 获得当前播放位置
	 * @return
	 */
	public int getPlayingNoteIndex() {
		return playingNoteIndex;
	}
	
	/**
	 * 获得串行音符序列
	 * @return
	 */
	public List<List<MeasurePart.NListWithMeaIndex>> getNoteSequence() {
		return noteSequence;
	}
	
	/**
	 * 清空串行音符序列
	 */
	public void clearNoteSequence(){
		noteSequence.clear();
	}

	/**
	 * 设置当前播放位置
	 * @param playingNoteIndex
	 */
	public void setPlayingNoteIndex(int playingNoteIndex) {
		this.playingNoteIndex = playingNoteIndex;
	}

	/**
	 * 返回播放指针
	 * @return
	 */
	public JPanel getPointer() {
		return pointer;
	}

	/**
	 * 设置播放指针
	 * @param pointer
	 */
	public void setPointer(JPanel pointer) {
		this.pointer = pointer;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
		/*
		 * 该变线条符号范围
		 */
		if(selectedObjects.size() == 1 && selectedObjects.get(0) instanceof SymbolLine){
			SymbolLine sl = (SymbolLine)selectedObjects.get(0);
			if(sl.getStatus() == AbstractLine.EDIT_STATUS && 
					(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_LEFT)){
				//当前线条符号没有结束音符，则返回
				if(sl.getEndNote() == null)
					return;
				
				//向右移动
				if(e.getKeyCode() == KeyEvent.VK_RIGHT){
					AbstractNote nxtEndNote = MusicMath.nxtNote(sl.getEndNote(), true);
					if(nxtEndNote != null){
						sl.shiftEndNote(nxtEndNote);
					}
					//如果线条符号进行了切分，则转移被选择对象，使之变为切分后的最后一部分
					if(sl.getNextSymbolLine() != null){
						SymbolLine nsl = (SymbolLine)sl.getNextSymbolLine();
						cancleAllSelected();
						nsl.setStatus(AbstractLine.EDIT_STATUS);
						nsl.repaint();
						selectedObjects.add(nsl);
						//为新的片段添加侦听器
						nsl.addMouseListener(this);
						nsl.addKeyListener(this);
					}
				}
					
				//向左移动
				else if(e.getKeyCode() == KeyEvent.VK_LEFT){
					AbstractNote preNote = MusicMath.preNote(sl.getEndNote(), true);
					if(preNote == sl.getStartNote())
						return;
					AbstractNote oldEndNote = sl.getEndNote();
					//前一个音符不在同一行
					if(!MusicMath.inSameLine(oldEndNote, preNote)){
						//删除当前线条
						SymbolLine pre = (SymbolLine)sl.getPreSymbolLine();
						pre.shiftEndNote(preNote);
						JComponent parent = (JComponent)getParent();
						parent.updateUI();
						
					    //转移被选择对象
						cancleAllSelected();
						selectedObjects.add(pre);
						pre.setStatus(AbstractLine.EDIT_STATUS);
						pre.repaint();
					}
					//仍是当前符号
					else{
						if(preNote != null)
							sl.shiftEndNote(preNote);
					}
				}
			}
		}
		
		else if(selectedObjects.size() == 1 && selectedObjects.get(0) instanceof RepeatLine ){
			RepeatLine rl = (RepeatLine)selectedObjects.get(0);
			if(rl.getStatus() == AbstractLine.EDIT_STATUS && (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_LEFT)){
				if(rl.getEndMeasurePart() == null)
					return;
				
				//向右移动
				if(e.getKeyCode() == KeyEvent.VK_RIGHT){
					MeasurePart nxtMeasurePart = MusicMath.nxtMeasurePart(rl.getEndMeasurePart());
					if(nxtMeasurePart != null){
						rl.shiftEndMeasure(nxtMeasurePart);
					}
					
					//如果线条进行了切分，则转移被选择对象，使之变为切分后的最后一部分
					if(rl.getNextSymbolLine() != null){
						RepeatLine repeatLine = (RepeatLine) rl.getNextSymbolLine();
						cancleAllSelected();
						repeatLine.setStatus(AbstractLine.EDIT_STATUS);
						repeatLine.repaint();
						selectedObjects.add(repeatLine);
						//为新片段添加侦听器
						repeatLine.addMouseListener(this);
						repeatLine.addKeyListener(this);
					}
				}
				
				//向左移动
				if(e.getKeyCode() == KeyEvent.VK_LEFT){
					MeasurePart preMeasurePart = MusicMath.preMeasurePart(rl.getEndMeasurePart());
//					if(preMeasurePart == rl.getStartMeasurePart())
//						return;
					if(rl.getStartMeasurePart() == rl.getEndMeasurePart())
						return;
					MeasurePart oldMeasurePart = rl.getEndMeasurePart();
					//前一个小节不在同一行
					if(!MusicMath.measureInSameLine(oldMeasurePart, preMeasurePart)){
						
						//删除当前线条
						RepeatLine pre = (RepeatLine)rl.getPreSymbolLine();
						pre.shiftEndMeasure(preMeasurePart);
						JComponent parent = (JComponent)getParent();
						parent.updateUI();
						
						//转移被选择对象
						cancleAllSelected();
						selectedObjects.add(pre);
						pre.setStatus(AbstractLine.EDIT_STATUS);
						pre.repaint();
						
						
					}
					//仍是当前符号
					else{
						if(preMeasurePart != null)
							rl.shiftEndMeasure(preMeasurePart);
					}
						
					
						
				}
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		/*
		 * 为倚音添加和弦
		 */
		if(selectedObjects.size() == 1 && selectedObjects.get(0) instanceof Grace){
			Grace grace = (Grace)selectedObjects.get(0);
			if(e.isShiftDown() && (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN)){
				
				int newPitch = e.getKeyCode() == KeyEvent.VK_UP ? grace.getPitch()+2 : grace.getPitch()-2;
				Grace newGrace = new Grace(grace.getDuration(), newPitch, grace.isHasSlash());
				Page page = (Page)grace.getParent();
				page.add(newGrace);
				newGrace.addMouseListener(this);
				newGrace.addMouseMotionListener(this);
				newGrace.addKeyListener(this);
				
				Measure measure = null;
				if(grace.getChordNote() == null){
					AbstractNote note = grace.getNote();
					ChordGrace cg = new ChordGrace(grace, grace.isHasSlash());	
					cg.addNote(grace);
					cg.addNote(newGrace);
					
					page.deleteNote(grace, false);
					grace.removeAllSymbols(false);
					page.add(grace);
					
					boolean left = grace.getX() < note.getX();
					if(left){
						int graceIndex = note.getLeftGraces().indexOf(grace);
						note.removeGrace(grace);
						note.addLeftGrace(graceIndex, cg);
					}
					else{
						int graceIndex = note.getRightGraces().indexOf(grace);
						note.removeGrace(grace);
						note.addRightGrace(graceIndex, cg);
					}
					measure = note.getMeasure();
				}
				
				else{
					ChordGrace cg = (ChordGrace)grace.getChordNote();
					cg.addNote(newGrace);
					measure = cg.getMeasure();
				}
				
				NoteLine line = measure.getMeasurePart().getNoteLine();
				while(redrawLine(line)){
					line = MusicMath.nxtLine(line);
				}
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
    public void ornamentAdded(){
    	// TODO Auto-generated method stub
    	String ornament = symbolPanel.getSymbols();
		if(mode.equalsIgnoreCase("edit"))
			return;
		if(selectedObjects.size() == 1 && selectedObjects.get(0) instanceof Note){
			Note note = (Note)selectedObjects.get(0);
			AbstractNote cNote = note.getChordNote() == null ? note : note.getChordNote();
			
			//起始不能是休止符
			if(note.isRest())
				return;
			if(note.hasOrnament(ornament))
				return;
			Ornament o1 = new Ornament(ornament);
			o1.addMouseListener(this);
	        cNote.addOrnament(o1);
	        note.getParent().add(o1);       
	        cNote.locateNoteSymbols();
		}	
    }	
    
	/**
	 * 返回当前被选择的小节组，或者被选择的对象所在的小节组
	 * 主要用于判断播放时候的起始小节组
	 * @return
	 */
	public MeasurePart getSelectedMeasurePart(){
		if(selectedObjects.size() == 1){
			Selectable s = selectedObjects.get(0);
			if(s instanceof Note){
				Note note = (Note)s;
				Measure measure = note.getChordNote() == null ? note.getMeasure() : note.getChordNote().getMeasure();
				return measure.getMeasurePart();
			}
			else if(s instanceof Measure){
				Measure measure = (Measure)s;
				return measure.getMeasurePart();
			}
			else if(s instanceof MeasurePart){
				return (MeasurePart)s;
			}
		}
		return null;
	}
	
	/**
	 * 返回当前被选择的对象所处的声部位置，即所处的小节在其对应的小节组中的位置。
	 * 如果被选择的对象是小节组，则返回其最后一个小节的位置.
	 * 主要用于谱表的添加、删除
	 * @return
	 */
	public int getSelectedMeaIndex(){
		if(selectedObjects.size() == 1){
			Selectable s = selectedObjects.get(0);
			if(s instanceof Note){
				Note note = (Note)s;
				Measure measure = note.getChordNote() == null ? note.getMeasure() : note.getChordNote().getMeasure();
				MeasurePart meaPart = measure.getMeasurePart();
				return meaPart.measureIndex(measure);
			}
			else if(s instanceof Measure){
				Measure measure = (Measure)s;
				MeasurePart meaPart = measure.getMeasurePart();
				return meaPart.measureIndex(measure);
			}
			else if(s instanceof MeasurePart){
				return ((MeasurePart)s).getMeasureNum()-1;
			}
		}
		return -1;
	}
	
	/**
	 * 将乐谱的音符按时间顺序转换为串型序列
	 * 如果乐谱中有音符、小节被选中，则从被选择的对象所在的小节开始转换.
	 * @return
	 */
	public void genPlayingNoteSequence(){
		List<List<MeasurePart.NListWithMeaIndex>> result = new ArrayList<List<MeasurePart.NListWithMeaIndex>>();
		//用来表示是否已经开始转换
		boolean flag = false;
		MeasurePart beginPart = getSelectedMeasurePart();
		for(int i = 0, in = score.getPageList().size(); i < in; i++){
			Page page = score.getPageList().get(i);
			for(int j = 0, jn = page.getNoteLines().size(); j < jn; j++){
				NoteLine line = page.getNoteLines().get(j);
				for(int k = 0, kn = line.getMeaPartList().size(); k < kn; k++){
					MeasurePart meaPart = line.getMeaPartList().get(k);
					if(!flag){
						if(beginPart == null || beginPart == meaPart)
							flag = true;
					}
					if(flag){
						List<List<MeasurePart.NListWithMeaIndex>> list = meaPart.getNotesByTimeSlot();
						result.addAll(list);
					}
				}
			}
		}
		noteSequence = result;
	}
	
	/**
	 * 播放下一个音符
	 */
	public void shiftPlayingNote(){
		cancleAllSelected();
		
		//播放到结尾
		if(playingNoteIndex >= noteSequence.size()){
			stopPlaying();
			System.out.println("停止播放");
			return;
		}
		
		//第一个音符
		if(pointer == null){
			NoteLine fline = score.getPageList().get(0).getNoteLines().get(0);
			int h = fline.getHeight() + LINE_GAP;
			if(pointer == null){
				pointer = new JPanel(){
					private static final long serialVersionUID = -7763843106508387693L;
					public void paintComponent(Graphics gg){
						gg.setColor(Color.blue);
						gg.drawLine(0, 0, 0, getHeight());
						gg.drawLine(1, 0, 1, getHeight());
					}
				};
				pointer.setSize(3, h);
				pointer.repaint();
				add(pointer, JLayeredPane.PALETTE_LAYER);
			}
		}
		List<MeasurePart.NListWithMeaIndex> list = noteSequence.get(playingNoteIndex);
		
		//当前时间槽所在的小节组，以及其x坐标
		MeasurePart meaPart = null;
		int x = -1;
		for(MeasurePart.NListWithMeaIndex mpw : list){
			for(AbstractNote note : mpw.getList()){
				note.beSelected();
				selectedObjects.add(note);
				if(meaPart == null)
					meaPart = note.getMeasure().getMeasurePart();
				if(x == -1)
					x = note.getX();
			}
		}
		Page page = meaPart.getNoteLine().getPage();
		int pageIndex = score.getPageList().indexOf(page);
		int y = (Page.PAGE_HEIGHT + PAGE_GAP) * pageIndex + meaPart.getY() - LINE_GAP/2;
		pointer.setLocation(x, y);
		playingNoteIndex++;
		
	}
	
	/**
	 * 停止播放
	 */
	public void stopPlaying(){
		cancleAllSelected();
		if(pointer != null)
			remove(pointer);
		pointer = null;
		playingNoteIndex = 0;
		revalidate();
		((JComponent)getParent()).updateUI();
	}
	
	/**
	 * 切换声部
	 * @param voice
	 */
	public void switchVoice(int voice){
		this.voice = voice;
		editNote.setVoice(voice);
		editNote.repaint();
	}

	/**
	 * 符号面板力度符号按钮被按下，添加力度符号
	 */
	public void dynamicAdded() {
		// TODO Auto-generated method stub
		String dynamic = symbolPanel.getSymbols();
		if(mode.equalsIgnoreCase("edit"))
			return;
		if(selectedObjects.size() == 1 && selectedObjects.get(0) instanceof Note){
			Note note = (Note)selectedObjects.get(0);
			AbstractNote cNote = note.getChordNote() == null ? note : note.getChordNote();
			Dynamic d1 = new Dynamic(dynamic);
			d1.addMouseListener(this);
	        	cNote.addDynamics(d1);
	        	note.getParent().add(d1);       
	        	cNote.locateNoteSymbols();
		}
	}

	/**
	 * 符号面板歌词按钮被按下，添加歌词
	 */
	public void lyricAdded() {
		// TODO Auto-generated method stub
		if(mode.equalsIgnoreCase("edit"))
			return;
		if(selectedObjects.size() == 1 && selectedObjects.get(0) instanceof Note){
			Note note = (Note)selectedObjects.get(0);
			AbstractNote cNote = note.getChordNote() == null ? note : note.getChordNote();
			Lyrics lyric = new Lyrics();
			lyric.editMode();
			lyric.addMouseListener(this);
	        	cNote.addLyrics(lyric);
	        	note.getParent().add(lyric);
	        	cNote.locateNoteSymbols();
			lyric.requestFocus();
			cancleAllSelected();
			selectedObjects.add(lyric);
		}
	}
	
	/**
	 * 符号面板注释按钮被按下，添加注释
	 */
	public void annotationAdded(){
		if(mode.equalsIgnoreCase("edit"))
			return;
		if(selectedObjects.size() > 0){
			Annotation ant = new Annotation();
			JComponent comp = (JComponent)selectedObjects.get(0);
		    Page page = (Page)comp.getParent();
		    for(int i = 0; i < selectedObjects.size(); i++){
		    	Selectable s = selectedObjects.get(i);
		    	ant.getRelatedObjts().add(s);
		    	if(s instanceof Note){
		    		Note note = (Note)s;
		    		note.getAnnotations().add(ant);
		    		note.locateNoteSymbols();
		    		if(ant.getParent() != page)
		    			page.add(ant);
		    		ant.addMouseListener(this);
		    		ant.getImagePanel().addMouseListener(this);
		    		ant.addMouseMotionListener(this);
		    		ant.getImagePanel().addMouseMotionListener(this);
		    		if(i == 0){
		    			connEndPoint.setLocation(note.getX(), note.getY());
		    		}
		    	}
		    	else if(s instanceof Measure){
		    		Measure measure = (Measure)s;
		    		measure.getAnnotations().add(ant);
		    		measure.locateAnnotations();
		    		if(ant.getParent() != page)
		    			page.add(ant);
		    		ant.addMouseListener(this);
		    		ant.getImagePanel().addMouseListener(this);
		    		ant.addMouseMotionListener(this);
		    		ant.getImagePanel().addMouseMotionListener(this);
		    		if(i == 0){
		    			connEndPoint.setLocation(measure.getX(), measure.getY() + page.getY());
		    		}
		    	}
		    }
		    cancleAllSelected();
		}
	}

	/**
	 * 添加升降号
	 * 符号面板的升降号按钮被按下
	 * @param string
	 */
	public void sharpOrFlatChanged(String string) {
		// TODO Auto-generated method stub
		if(mode.equalsIgnoreCase("edit"))
			return;
		if(selectedObjects.size() > 0 && selectedObjects.get(0) instanceof Note){
			Note note = (Note)selectedObjects.get(0);
			if(note.getSharpOrFlat() != null)
				return;
			SharpOrFlat sof = new SharpOrFlat(string);
			sof.addMouseListener(this);
	        note.setSharpOrFlat(sof);
	        sof.setNote(note);
	        note.resetAlter();
	        note.getParent().add(sof); 
	        Measure measure = null;
	        if(note instanceof Grace){
	        	ChordGrace cg = (ChordGrace)((Grace)note).getChordNote();
	        	measure = cg == null ? ((Grace)note).getNote().getMeasure() : cg.getNote().getMeasure();
	        }else
	        	measure = note.getChordNote() == null ? note.getMeasure() : note.getChordNote().getMeasure();
	        NoteLine line = measure.getMeasurePart().getNoteLine();
	        while(redrawLine(line)){
	        	line = MusicMath.nxtLine(line);
	        }
		}
	}
	
	/**
	 * 调整页面大小
	 */
//	public void adjustSize(Score score){
//		Page page = score.getPageList().get(0);
//		int lineNum = page.getNoteLines().size();
//		NoteLine lastLine = page.getNoteLines().get(lineNum-1);
//		if(lastLine.getY() + lastLine.getHeight() + lastLine.getLineGap()/2 > getHeight()){
//			page.setSize(page.getWidth(), lastLine.getY() + lastLine.getHeight() + lastLine.getLineGap()/2);
//			setSize(getWidth()-10, lastLine.getY() + lastLine.getHeight() + lastLine.getLineGap()/2);
//			setPreferredSize(new Dimension(getWidth()-10, lastLine.getY() + lastLine.getHeight() + lastLine.getLineGap()/2));
//			veil.setSize(getWidth(), lastLine.getY() + lastLine.getHeight() + lastLine.getLineGap()/2);
//		}
//	}
	
	/**
	 * 去掉页面
	 */
	public void removePages(){
		for(int i = 0; i < score.getPageList().size(); i++){
			Page page = score.getPageList().get(i);
			remove(page);
		}
	}
	
	/**
	 * 添加倚音
	 * @param duration 倚音时长类型
	 * @param hasSlash 是否短倚音
	 * @param direction 左倚音或者右倚音
	 */
	public void graceAdded(int duration, boolean hasSlash, String direction){
		if(mode.equalsIgnoreCase("edit"))
			return;
		if(selectedObjects.size() > 0 && selectedObjects.get(0) instanceof Note){
			Note note = (Note)selectedObjects.get(0);
			//不能为倚音或者休止符添加倚音
			if(note instanceof Grace || note.isRest())
				return;
			Grace grace = new Grace(duration, note.getPitch(), hasSlash);
			AbstractNote anote = note.getChordNote() == null ? note : note.getChordNote();
			if(direction.equalsIgnoreCase("left"))
				anote.addLeftGrace(grace);
			else if(direction.equalsIgnoreCase("right"))
				anote.addRightGrace(grace);
			((JComponent)note.getParent()).add(grace);
			grace.addMouseListener(this);
			grace.addMouseMotionListener(this);
			grace.addKeyListener(this);
			
			NoteLine line = note.getMeasure().getMeasurePart().getNoteLine();
			while(redrawLine(line)){
				line = MusicMath.nxtLine(line);
			}
		}
	}

	/**
	 * 添加页面
	 */
	public void addPages(){
		for(int i = 0; i < score.getPageList().size(); i++){
			Page page = score.getPageList().get(i);
			add(page);
		}
	}

	public void tremoloBeamAdded(String string) {
		// TODO Auto-generated method stub
		String tremoloBeam = string;
		if(mode.equalsIgnoreCase("edit"))
			return;
		if(selectedObjects.size() == 1 && selectedObjects.get(0) instanceof Note){
			Note note = (Note)selectedObjects.get(0);
			AbstractNote cNote = note.getChordNote() == null ? note : note.getChordNote();
			TremoloBeam tB = new TremoloBeam(tremoloBeam, Gracable.NORMAL);
			if(cNote.getTremoloBeam() == null){
				tB.addMouseListener(this);
		        cNote.addTremoloBeam(tB);
		        tB.setNote(cNote);
		        note.getParent().add(tB);       
		        cNote.locateNoteSymbols();
			}else{			
				TremoloBeam tBB =  cNote.getTremoloBeam();
				tBB.getParent().remove(tBB);
				cNote.removeTremoloBeam();
				JComponent pr = (JComponent) this.getParent();
				pr.revalidate();
				pr.updateUI();
				
				tB.addMouseListener(this);
		        cNote.addTremoloBeam(tB);
		        tB.setNote(cNote);
		        note.getParent().add(tB);       
		        cNote.locateNoteSymbols();				
			}
		}	
	}

//	public void tremoloAdded(String string) {
//		// TODO Auto-generated method stub
//		if(mode.equalsIgnoreCase("edit"))
//			return;
//		if(selectedObjects.size() == 1 && selectedObjects.get(0) instanceof Note){
//				
//		}	
//	}

//	public void repeatLineAdded() {
//		// TODO Auto-generated method stub
//		if (selectedObjects.size() == 0 || score.getScoreType() == Score.SCORE_UNLMTED) {
//			return;
//		} else if (selectedObjects.size() == 1 && selectedObjects.get(0) instanceof Measure) {
//			
//			Measure measure = (Measure)selectedObjects.get(0);			
//			MeasurePart mPart = measure.getMeasurePart();
//			
//		//	RepeatLine rl = LineFactory.generateRepeatLine(10);
//			RepeatEnding rl = (RepeatEnding) LineFactory.generateRepeatLine(10);
//			rl.setStartMeasurePart(mPart);
//			rl.setEndMeasurePart(mPart);
//			mPart.getRepeatLines().add(rl);
//			measure.getParent().add(rl);
//			//rl.setNumber("5");
//		//	rl.setNumber(3);
//					    
//			rl.reShape();
//			rl.requestFocus();
//			rl.addMouseListener(this);
//			rl.addKeyListener(this);
//			cancleAllSelected();
//			rl.setStatus(AbstractLine.EDIT_STATUS);
//			rl.repaint();
//			selectedObjects.add(rl);		
//		}
//	}

	public void breathAdded() {
		// TODO Auto-generated method stub
		if(mode.equalsIgnoreCase("edit"))
			return;
		else if(selectedObjects.size() == 1 && selectedObjects.get(0) instanceof Note){
			Note note = (Note) selectedObjects.get(0);
			AbstractNote cNote = note.getChordNote() == null ? note : note
					.getChordNote();
			Breath breath = new Breath();
			breath.addMouseListener(this);
			cNote.addBreath(breath);
			note.getParent().add(breath);
			breath.setNote(cNote);
			// breath.setLocation(400, 40);
			cNote.locateNoteSymbols();
		}
	}
	/**
	 * 速度记号
	 */
	public void tempoAdded(String name, int tempo, boolean displayTempo){
		if (selectedObjects.size() == 0) {
			return;
		} else if (selectedObjects.size() == 1 && selectedObjects.get(0) instanceof Note) {
			Note note = (Note)selectedObjects.get(0);
			AbstractNote anote = note.getChordNote() == null ? note : note.getChordNote();
			TempoText tt = new TempoText(name, tempo, displayTempo);
			note.getParent().add(tt);
			anote.setTempoText(tt);
			tt.setNote(anote);
			anote.locateNoteSymbols();
			tt.addMouseListener(this);
		}
	}

	public void pedalAdded(String string) {
		// TODO Auto-generated method stub
		if(mode.equalsIgnoreCase("edit"))
			return;
		else if (selectedObjects.size() == 1 && selectedObjects.get(0) instanceof Note) {
			String type = string;
			Note note = (Note) selectedObjects.get(0);
			AbstractNote cNote = note.getChordNote() == null ? note : note
					.getChordNote();
			Pedal pedal = new Pedal(type);
			pedal.addMouseListener(this);
			cNote.addPedal(pedal);
			note.getParent().add(pedal);
			pedal.setNote(cNote);
			cNote.locateNoteSymbols();
		}
	}
	
	/**
	 * 返回当前页面index
	 * @return
	 */
	public int getCurPageIndex(){
		return bottomPanel.getButtonGroup().getCurPageIndex();
	}
	
	/**
	 * 返回当前页面距离页面顶部的offset值
	 * @return
	 */
	public int getPageOffSet(){
		if(getParent().getParent() instanceof JScrollPane){
			JScrollPane pane = (JScrollPane)(getParent().getParent());
			int value = pane.getVerticalScrollBar().getValue();
			return value-(getCurPageIndex()-1)*(Page.PAGE_HEIGHT+NoteCanvas.PAGE_GAP);
		}
		return -1;
	}
	
	/**
	 * 返回容器高度
	 * @return
	 */
	public int getWindowHeight(){
		if(getParent().getParent() instanceof JScrollPane){
			JScrollPane pane = (JScrollPane)(getParent().getParent());
			return pane.getHeight();
		}
		return -1;
	}

	public int print(Graphics g, PageFormat pf, int index){
		if(index >= score.getPageList().size())
			return NO_SUCH_PAGE;
		Page page = score.getPageList().get(index);
		Graphics2D g2 = (Graphics2D)g;
		g2.translate(pf.getImageableX(), pf.getImageableY());
		page.paint(g2);
		return PAGE_EXISTS;
	}
	
}
