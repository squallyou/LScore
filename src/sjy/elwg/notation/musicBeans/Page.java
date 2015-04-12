package sjy.elwg.notation.musicBeans;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import sjy.elwg.notation.NoteCanvas;
import sjy.elwg.notation.musicBeans.symbolLines.NoteSymbol;
import sjy.elwg.notation.musicBeans.symbolLines.SymbolLine;
import sjy.elwg.notation.musicBeans.symbolLines.Tie;
import sjy.elwg.utility.ConstantInterface;
import sjy.elwg.utility.Controller;
/**
 * 页
 * @author jingyuan.sun
 *
 */
public class Page extends JLayeredPane implements MouseListener, ConstantInterface{
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 8L;
	
	public static final int TITLE_HEIGHT = 100;
	
	/**
	 * 页中包含的行
	 */
	private ArrayList<NoteLine> noteLines;
	/**
	 * 页面所属的乐谱
	 */
	private Score score;
	/**
	 * 首页所具有的标题框,包含乐谱标题与作者
	 */
	private TitleBox titleBox;
	
	/**
	 * 行初始y值
	 */
	private int noteLineYStart = NoteCanvas.yStart;
	
	public Page(){
		super();
		noteLines = new ArrayList<NoteLine>();
		setSize(PAGE_WIDTH, PAGE_HEIGHT);
		setBackground(Color.white);
		setLayout(null);
		setOpaque(true);
		addMouseListener(this);
	}

	/**
	 * 获得行
	 * @return
	 */
	public ArrayList<NoteLine> getNoteLines() {
		return noteLines;
	}
	
	/**
	 * 获得页所在的乐谱
	 * @return
	 */
	public Score getScore() {
		return score;
	}

	/**
	 * 设置乐谱
	 * @param score
	 */
	public void setScore(Score score) {
		this.score = score;
	}
	
	/**
	 * 生成标题块.
	 * 只有第一页会调用该方法
	 */
	public void generateTitleBox(){
		if(titleBox == null){
			titleBox = new TitleBox();
			add(titleBox);
			titleBox.setLocation((getWidth() - titleBox.getWidth())/2, 30);
			//add listener
			NoteCanvas canvas = (NoteCanvas)getParent();
			titleBox.titlePanel.addMouseListener(canvas);
			titleBox.authorPanel.addMouseListener(canvas);
		}
		noteLineYStart = NoteCanvas.yStart + titleBox.getHeight() ;
	}
	
	/**
	 * 返回本页剩余的空白高度
	 * @return
	 */
	public int getBlankHeight(){
		int accum = noteLineYStart;
		for(int i = 0, n = noteLines.size(); i < n; i++){
			NoteLine line = noteLines.get(i);
			accum += line.getHeight() + line.getLineGap();
		}
		return PAGE_HEIGHT - accum + NoteLine.NOTELINE_GAP;
	}
	
	/**
	 * 重写添加组件方法，对和弦音符特殊照顾
	 */
	public Component add(Component component){
		if(component instanceof ChordNote){
			ChordNote cnote = (ChordNote)component;
			for(int i = 0, n = cnote.getNoteNum(); i < n; i++){
				Note nnote = cnote.getNote(i);
				super.add(nnote, PALETTE_LAYER );
			}
			return cnote.getNote(0);
		}
		else if(component instanceof Measure || component instanceof Tuplet){
			super.add(component, DEFAULT_LAYER);
			return component;
		}
		else if(component instanceof Annotation){
			super.add(component, DRAG_LAYER);
			return component;
		}
		else{
			super.add(component, PALETTE_LAYER );
			return component;
		}
	}
	
	/**
	 * 获得该页第一行的其实y坐标
	 * @return
	 */
	public int getNoteLineYStart() {
		return noteLineYStart;
	}

	/**
	 * 设置页其实y坐标
	 * @param noteLineYStart
	 */
	public void setNoteLineYStart(int noteLineYStart) {
		this.noteLineYStart = noteLineYStart;
	}
	
	/**
	 * 从页面上删除音符实体及符杠符柄和其他各种符号实体
	 * @param note 待删除音符
	 * @param withTup 是否删除连音号
	 */
	public void deleteNote(AbstractNote note, boolean withTup) {
		
		// 删除符杠、符柄、符尾
		if (note.getBeam() != null) {
			remove(note.getBeam());
		}
		if (note.getStem() != null) {
			remove(note.getStem());
		}
		if (note.getTail() != null) {
			remove(note.getTail());
		}
		
		//删除修饰符号、力度符号、歌词
		if(note.getOrnamentsNum() > 0){
			for(int i = 0, n = note.getOrnamentsNum(); i < n; i++){
				NoteSymbol ns = note.getOrnament(i);
				remove(ns);
			}
		}
		if(note.getDynamicsNum() > 0){
			for(int i = 0, n = note.getDynamicsNum(); i < n; i++){
				NoteSymbol ns = note.getDynamics(i);
				remove(ns);
			}
		}
		if(note.getPerformanceSymbolsNum() > 0){
			for(int i = 0, n = note.getPerformanceSymbolsNum(); i < n; i++){
				NoteSymbol ns = note.getPerformanceSymbols(i);
				remove(ns);
			}
		}
		if(note.getLyricsNum() > 0){
			for(int i = 0, n = note.getLyricsNum(); i < n; i++){
				Lyrics ly = note.getLyrics(i);
				remove(ly);
			}
		}
		
		//颤音
		if(note.getTremoloBeam() != null){
			remove(note.getTremoloBeam());
		}
		
		//踏板
		if(note.getPedal() != null){
			remove(note.getPedal());
		}
		
		//呼吸记号
		if(note.getBreath() != null){
			remove(note.getBreath());
		}
		
		//倚音
		if(!note.getLeftGraces().isEmpty()){
			Controller.deleteBeamAndStem(note.getLeftGraces());
			for(int i = 0, n = note.getLeftGraces().size(); i < n; i++){
				AbstractNote an = note.getLeftGraces().get(i);
				deleteNote(an, true);
			}
		}
		if(!note.getRightGraces().isEmpty()){
			Controller.deleteBeamAndStem(note.getRightGraces());
			for(int i = 0, n = note.getRightGraces().size(); i < n; i++){
				AbstractNote an = note.getRightGraces().get(i);
				deleteNote(an, true);
			}
		}
		
		//删除线条符号
		if(note.getSymbolLines().size() > 0){
			for(SymbolLine sl : note.getSymbolLines()){
				if(sl.getStartNote() == note){
					remove(sl);
					SymbolLine pre = (SymbolLine)sl.getPreSymbolLine();
					while(pre != null){
						JComponent parent = (JComponent)pre.getParent();
						parent.remove(pre);
						pre = (SymbolLine)pre.getPreSymbolLine();
					}
					SymbolLine nxt = (SymbolLine)sl.getNextSymbolLine();
					while(nxt != null){
						JComponent parent = (JComponent)nxt.getParent();
						parent.remove(nxt);
						nxt = (SymbolLine)nxt.getNextSymbolLine();
					}
				}
				
			}
		}
		
		//连音线
		if(note instanceof Note && ((Note)note).getTieNum() > 0){
			Note nnote = (Note)note;
			for(int i = 0; i < nnote.getTieNum(); i++){
				Tie tie = nnote.getTie(i);
				remove(tie);
				if(tie.getPreSymbolLine() != null){
					Tie preTie = (Tie)tie.getPreSymbolLine();
					JComponent parent = (JComponent)preTie.getPreSymbolLine();
					parent.remove(preTie);
				}
				if(tie.getNextSymbolLine() != null){
					Tie nxtTie = (Tie)tie.getNextSymbolLine();
					JComponent parent = (JComponent)nxtTie.getPreSymbolLine();
					parent.remove(nxtTie);
				}
			}
		}
		
		// 删除音符、附点、升降号，以及位置短线
		if (note instanceof Note) {
			Note nnote = (Note)note;
			remove(nnote);
			if(nnote.getUiDot() != null)
				remove(nnote.getUiDot());
			if(nnote.getPositionLines().size() != 0){
				for(JPanel l : nnote.getPositionLines()){
					remove(l);
				}
			}
			if(nnote.getSharpOrFlat() != null)
				remove(nnote.getSharpOrFlat());
		}
		else if (note instanceof ChordNote) {
			ChordNote cnote = (ChordNote) note;
			for (int i = 0; i < cnote.getNoteNum(); i++) {
				Note nnote = cnote.getNote(i);
				remove(nnote);
				if(nnote.getUiDot() != null)
					remove(nnote.getUiDot());
				if(nnote.getPositionLines().size() != 0){
					for(JPanel l : nnote.getPositionLines()){
						remove(l);
					}
				}
				if(nnote.getSharpOrFlat() != null)
					remove(nnote.getSharpOrFlat());
			}
		}
		
		//连音号
		if(withTup && note.getTuplet() != null){
			remove(note.getTuplet());
			for(AbstractNote nnote : note.getTuplet().getNoteList()){
				if(nnote != note)
					deleteNote(nnote, false);
			}
		}
		
	
		
	}
	
	
	/**
	 * 向页面添加音符UI实体，以及音符相关的所有符号
	 */
	public void addNoteAndSymbols(AbstractNote note, boolean withTup){
		
		//符杠等
		if(note.getBeam() != null){
			add(note.getBeam());
		}
		if(note.getStem() != null){
			add(note.getStem());
		}
		if(note.getTail() != null){
			add(note.getTail());
		}

		//修饰符号，力度符号，歌词
		if(note.getOrnamentsNum() > 0){
			for(int i =0, n = note.getOrnamentsNum(); i < n; i++){
				NoteSymbol ns = note.getOrnament(i);
				add(ns);	
			}
		}
		if(note.getDynamicsNum() > 0){
			for(int i = 0, n = note.getDynamicsNum(); i < n; i++){
				NoteSymbol ns = note.getDynamics(i);
				add(ns);
			}
		}
		if(note.getPerformanceSymbolsNum() > 0){
			for(int i = 0, n = note.getPerformanceSymbolsNum(); i < n; i++){
				NoteSymbol ns = note.getPerformanceSymbols(i);
				add(ns);
			}
		}
		if(note.getLyricsNum() > 0){
			for(int i = 0, n = note.getLyricsNum(); i < n; i++){
				Lyrics ly = note.getLyrics(i);
				add(ly);
			}
		}

		//颤音
		if(note.getTremoloBeam() != null){
			add(note.getTremoloBeam());
		}
		
		//踏板
		if(note.getPedal() != null){
			add(note.getPedal());
		}
		
		//呼吸记号
		if(note.getBreath() != null){
			add(note.getBreath());
		}
		
		//倚音
		if(!note.getLeftGraces().isEmpty()){
			Controller.deleteBeamAndStem(note.getLeftGraces());
			for(int i = 0, n = note.getLeftGraces().size(); i < n; i++){
				AbstractNote an = note.getLeftGraces().get(i);
				addNoteAndSymbols(an, true);
			}
		}
		if(!note.getRightGraces().isEmpty()){
			Controller.deleteBeamAndStem(note.getRightGraces());
			for(int i = 0, n = note.getRightGraces().size(); i < n; i++){
				AbstractNote an = note.getRightGraces().get(i);
				add(an, true);
			}
		}
		
		//线条符号
		if(note.getSymbolLines().size() > 0){
			for(SymbolLine sl : note.getSymbolLines()){
				if(sl.getStartNote() == note){
					add(sl);
					SymbolLine pre = (SymbolLine)sl.getPreSymbolLine();
					while(pre != null){
						add(pre);
						pre = (SymbolLine)pre.getPreSymbolLine();
					}
					SymbolLine nxt = (SymbolLine)sl.getNextSymbolLine();
					while(nxt != null){
						add(nxt);
						nxt = (SymbolLine)nxt.getNextSymbolLine();
					}
				}
				
			}
		}
		
		//连音线
		if(note instanceof Note && ((Note)note).getTieNum() > 0){
			Note nnote = (Note)note;
			for(int i = 0; i < nnote.getTieNum(); i++){
				Tie tie = nnote.getTie(i);
				add(tie);
				if(tie.getPreSymbolLine() != null){
					Tie preTie = (Tie)tie.getPreSymbolLine();
					add(preTie);
				}
				if(tie.getNextSymbolLine() != null){
					Tie nxtTie = (Tie)tie.getNextSymbolLine();
					add(nxtTie);
				}
			}
		}
		
		// 删除音符、附点、升降号，以及位置短线
		if (note instanceof Note) {
			Note nnote = (Note)note;
			add(nnote);
			if(nnote.getUiDot() != null)
				add(nnote.getUiDot());
			if(nnote.getPositionLines().size() != 0){
				for(JPanel l : nnote.getPositionLines()){
					add(l);
				}
			}
			if(nnote.getSharpOrFlat() != null)
				add(nnote.getSharpOrFlat());
		}
		else if (note instanceof ChordNote) {
			ChordNote cnote = (ChordNote) note;
			for (int i = 0; i < cnote.getNoteNum(); i++) {
				Note nnote = cnote.getNote(i);
				add(nnote);
				if(nnote.getUiDot() != null)
					add(nnote.getUiDot());
				if(nnote.getPositionLines().size() != 0){
					for(JPanel l : nnote.getPositionLines()){
						add(l);
					}
				}
				if(nnote.getSharpOrFlat() != null)
					add(nnote.getSharpOrFlat());
			}
		}
		
		//连音号
		if(withTup && note.getTuplet() != null){
			add(note.getTuplet());
			for(AbstractNote nnote : note.getTuplet().getNoteList()){
				if(nnote != note)
					addNoteAndSymbols(nnote, false);
			}
		}
	}	


	/**
	 *向页面添加小节实体及各种符号
	 */
	public void addMeasurePartAndSymbols(MeasurePart measurePart){
		for(int i = 0; i < measurePart.getMeasureNum(); i++){
			Measure mea = measurePart.getMeasure(i);
			//音符及音符符号
			for(int j = 0; j < mea.getVoiceNum(); j++){
				for(int k = 0; k < mea.getNoteNum(j); k++){
					AbstractNote note = mea.getNote(k, j);
					addNoteAndSymbols(note, true);
				}
			}
			add(mea);
			//barline
			add(measurePart.getBarline());
			//UITime, UIClef, UIKey
			if(mea.getUiTime() != null)
				add(mea.getUiTime());
			if(mea.getUiClef() != null)
				add(mea.getUiClef());
			if(mea.getUiKey() != null)
				add(mea.getUiKey());
		}
	}


	

	/**************************************************************************/
	
	/**
	 * 乐谱标题框，包括标题与作者
	 */
	public class TitleBox extends JPanel{
		
		/**
		 * 序列号
		 */
		private static final long serialVersionUID = -287081669597517767L;
		/**
		 * 标题与作者
		 */
		TitlePanel titlePanel;
		TitlePanel authorPanel;
		
		public TitleBox(){
			super();
			setOpaque(false);
			setLayout(null);
			setSize(PAGE_WIDTH  * 4 / 5, TITLE_HEIGHT);
			initComponents();
		}
		
		/**
		 * 生成组件
		 */
		public void initComponents(){
			titlePanel = new TitlePanel("标题");
			authorPanel = new TitlePanel("作者");
			titlePanel.setFont(TitlePanel.TITLE_FONT.deriveFont(40f));
			titlePanel.setForeground(Color.LIGHT_GRAY);
			titlePanel.reSize();
			
			authorPanel.setFont(TitlePanel.TITLE_FONT.deriveFont(30f));
			authorPanel.setForeground(Color.LIGHT_GRAY);
			authorPanel.reSize();
			
			this.add(titlePanel);
			this.add(authorPanel);
			titlePanel.setLocation((getWidth() - titlePanel.getWidth()) / 2, 2);
			authorPanel.setLocation(getWidth() - authorPanel.getWidth()-2, getHeight() - authorPanel.getHeight()-2);
		}
		
		/**
		 * 重置组件位置
		 */
		public void reLocateComponents(){
			titlePanel.setLocation((getWidth() - titlePanel.getWidth()) / 2, 2);
			authorPanel.setLocation(getWidth() - authorPanel.getWidth()-2, getHeight() - authorPanel.getHeight()-2);
		}
		
		public void paintComponent(Graphics gg){
			gg.setColor(Color.LIGHT_GRAY);
			gg.drawRect(0, 0, getWidth()-1, getHeight()-1);
		}
		
		public void setSize(int x, int y){
			super.setSize(x, y);
			repaint();
		}
		
		/**
		 * 设置标题
		 * @param str
		 */
		public void setTitle(String str){
			if(str == null || str.equalsIgnoreCase("")){
				return;
			}
			titlePanel.setText(str);
			titlePanel.setForeground(Color.black);
			titlePanel.reSize();
		}
		
		/**
		 * 设置作者
		 * @param str
		 */
		public void setComposer(String str){
			if(str == null || str.equalsIgnoreCase("")){
				return;
			}
			authorPanel.setText(str);
			authorPanel.setForeground(Color.black);
			authorPanel.reSize();
		}
		
	}

	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == this){
			requestFocusInWindow();
		}
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public TitleBox getTitleBox() {
		return titleBox;
	}

	public void setTitleBox(TitleBox titleBox) {
		this.titleBox = titleBox;
	}

}
