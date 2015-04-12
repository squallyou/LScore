package sjy.elwg.notation.musicBeans;

import java.util.ArrayList;
import java.util.List;

import javax.rmi.CORBA.Tie;
import javax.swing.JComponent;
import javax.swing.JPanel;

import sjy.elwg.notation.musicBeans.symbolLines.Breath;
import sjy.elwg.notation.musicBeans.symbolLines.Dynamic;
import sjy.elwg.notation.musicBeans.symbolLines.NoteSymbol;
import sjy.elwg.notation.musicBeans.symbolLines.Ornament;
import sjy.elwg.notation.musicBeans.symbolLines.Pedal;
import sjy.elwg.notation.musicBeans.symbolLines.PerformanceSymbol;
import sjy.elwg.notation.musicBeans.symbolLines.SymbolLine;
import sjy.elwg.utility.Controller;
import sjy.elwg.utility.MusicMath;

/**
 * 音符的父类
 * 子类有Note（普通音符）与和弦（ChordNote）
 * @author jingyuan.sun
 *
 */
public abstract class AbstractNote extends JPanel implements Selectable, Equalable{
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 14L;
	/**
	 * 音符时长. 全音符为256， 2分为128， 4分为64， 依次类推...
	 */
	protected int duration = 64;
	/**
	 * 附点个数
	 */
	protected int dotNum;
	/**
	 * 音符符杠类型. 有"begin", "continue", "none", "default"四种.
	 * 默认对于时长大于等于四分音符时长的音符，以及对于休止符，值为"none".
	 * 其他音符的默认值为"default".   "begin"和"continue"仅在用户手动设置时产生.
	 */
	protected String beamType = "default";
	/**
	 * 所在小节内的声部.
	 * 音符在被添加进小节时，该变量被设定.
	 * 目前本软件针对单谱表支持两个声部，因此有效值是0或者1
	 */
	protected int voice = -1;
	/**
	 * 音符所在小节
	 */
	protected Measure measure;
	/**
	 * 音符符杠
	 */
	protected Beam beam;
	/**
	 * 音符符柄
	 */
	protected Stem stem;
	/**
	 * 音符符尾
	 */
	protected Tail tail;
	/**
	 * 歌词
	 */
	protected ArrayList<Lyrics> lyrics;
	/**
	 * 与该音符有关的线条符号集合
	 */
	protected ArrayList<SymbolLine> symbolLines;
	/**
	 * 该音符的音符符号集合.
	 */
	protected ArrayList<NoteSymbol> ornaments;
	/**
	 * 音符相关的力度记号
	 */
	protected ArrayList<NoteSymbol> dynamics;
	/**
	 * 音符相关的装饰音记号，不包括倚音
	 */
	protected ArrayList<NoteSymbol> graceSymbols;
	/**
	 * 表情术语
	 */
	protected ArrayList<NoteSymbol> performanceSymbols;
	
	
	/**
	 * 在符干上的颤音记号
	 */
	protected TremoloBeam tremoloBeam;
	
	/**
	 * 呼吸记号
	 */
	protected Breath breath;
	
	/**
	 * 踏板记号
	 */
	protected Pedal pedal;
	
	/**
	 * 颤音记号
	 */
	protected Tremolo tremolo;
	/**
	 * tie符号
	 */
	protected ArrayList<Tie> ties;
	/**
	 * 连音号
	 */
	protected Tuplet tuplet;
	/**
	 * 音符左边的倚音
	 */
	protected List<AbstractNote> leftGraces;
	/**
	 * 音符右边的倚音
	 */
	protected List<AbstractNote> rightGraces;
	/**
	 * 速度记号
	 */
	protected TempoText tempoText;


	/**
	 * 构造函数
	 */
	public AbstractNote(int duration){
		super();
		this.duration = duration;
		symbolLines = new ArrayList<SymbolLine>();
		ornaments = new ArrayList<NoteSymbol>();
		dynamics = new ArrayList<NoteSymbol>();
		performanceSymbols = new ArrayList<NoteSymbol>();
		lyrics = new ArrayList<Lyrics>();
		leftGraces = new ArrayList<AbstractNote>();
		rightGraces = new ArrayList<AbstractNote>();
		graceSymbols = new ArrayList<NoteSymbol>();
	//	breath = new Breath();
	//	pedal = new Pedal(type)
	//	tremoloBeam = new TremoloBeam("no");
	}
	
	/**
	 * 抽象方法
	 * 按给定的横坐标放置音符
	 * @param x
	 */
	public abstract void locateNote(int x);
	
	/**
	 * 升降号宽度.
	 * 如果没有则返回0
	 * @return
	 */
	public abstract int sharpOrFlatWidth();
	
	/**
	 * 浮点宽度
	 * 如果没有则返回0
	 * @return
	 */
	public abstract int dotWidth();
	
	/**
	 * 音高是否绝对高于另一个音符
	 * 所谓绝对高于，是指该音符的每一个子音符都高于另一个音符的任意一个子音符
	 * @param note
	 * @return
	 */
	public abstract boolean absHigherThan(AbstractNote note);
	
	/**
	 * 音高是否绝对低于另一个音符
	 * 所谓绝对低于，是指该音符的每一个子音符都低于另一个音符的任意一个子音符
	 * @param note
	 * @return
	 */
	public abstract boolean absLowerThan(AbstractNote note);
	
	/**
	 * 放置音符符号
	 * 子类通常需要重写该方法
	 */
	public void locateNoteSymbols(){
		//倚音
		if(!leftGraces.isEmpty()){
			int x = getX();
			AbstractNote curNote = this;
			for(int i = leftGraces.size()-1; i >= 0; i--){
				AbstractNote grace = leftGraces.get(i);
				x -= MusicMath.shortestDistNeeded(grace, curNote, false);
				grace.locateNote(x);
				curNote = grace;
			}
			Controller.deleteBeamAndStem(leftGraces);
			Controller.drawBeamAndStem((JComponent)getHighestNote().getParent(), leftGraces);
			for(int i = 0; i < leftGraces.size(); i++){
				AbstractNote grace = leftGraces.get(i);
				grace.locateNoteSymbols();
			}
		}
		//速度记号
		if(tempoText != null){
			int y = measure.getY() - tempoText.getHeight() - 5;
			y = getHighestNote().getY() <= y ? getHighestNote().getY() - 10 : y;
			tempoText.setLocation(getX()+getWidth()/2-tempoText.getWidth()/2, y);
		}
	}
	

	/**
	 * 抽象方法
	 * 返回音高最低的音符.
	 * 如果是普通音符，返回本身；如果和弦，返回音高最低的音符
	 * @return
	 */
	public abstract Note getLowestNote();
	
	/**
	 * 抽象方法
	 * 返回音高最高的音符.
	 * 如果是普通音符，返回本身；如果和弦，返回音高最高的音符
	 * @return
	 */
	public abstract Note getHighestNote();
	
	/**
	 * 返回具有指定音高的音符
	 * 如果是普通音符，且音符的音高与指定值相同，则返回音符本身
	 * 如果是和弦音符，且和弦中包含指定音高的音符，则返回该特定的音符
	 * 否则返回0.
	 * @param pitch
	 * @return
	 */
	public abstract Note getNoteWithPitch(int pitch);
	
	/**
	 * 抽象方法
	 * 生成附点实体
	 */
	public abstract void generateUIDot();
	
	public abstract boolean isRest();
	
	
	/**
	 * 返回考虑符杆时音符的y坐标，即音符y与符杆y的最小值
	 * @return
	 */
	public int getYWithStem(){
		if(stem == null)
			return getHighestNote().getY();
		else{
			int ny = getHighestNote().getY();
			int sy = stem.getY();
			return ny > sy ? sy : ny;
		}
	}
	
	/**
	 * 获得音符的真实时长（包括附点、连音符）
	 * @return
	 */
	public float getRealDuration(){
		if(tuplet == null)
			return (float)getDurationWithDot();
		else{
			int mod = tuplet.getModification();
			int nor = tuplet.getNormal();
			return (float)getDurationWithDot() * nor / mod;
		}
	}
	

	
	/**
	 * 在逻辑上删除音符所拥有的所有符号
	 * @param withTup 是否包括连音号
	 */
    public void removeAllSymbols(boolean withTup){
    	//表演记号
		for(NoteSymbol ns : ornaments){
			ns.setNote(null);
		}
		ornaments.clear();
		
		//力度符号
		for(NoteSymbol ns : dynamics){
			ns.setNote(null);
		}
		dynamics.clear();
		
		//表情术语
		for(NoteSymbol ns : performanceSymbols ){
			ns.setNote(null);
		}
		performanceSymbols.clear();
		//歌词
		for(Lyrics ly : lyrics){
			ly.setNote(null);
		}
		lyrics.clear();
		
		//颤音记号
		
		if(tremoloBeam != null){
			tremoloBeam.setNote(null);
			tremoloBeam = null;
		}
		
		//装饰符号
		
		if(graceSymbols != null){
			for(NoteSymbol ns : graceSymbols){
				ns.setNote(null);
			}
			graceSymbols.clear();
		}
		
		//线条符号
		for(int i = symbolLines.size() - 1; i >= 0; i--){
			SymbolLine sl = symbolLines.get(i);
			//之前的片段链表
			SymbolLine preLine = (SymbolLine)sl.getPreSymbolLine();
			//之后的片段链表
			SymbolLine nxtLine = (SymbolLine)sl.getNextSymbolLine();
			
			//起始音符
			AbstractNote startNote = sl.getStartNote();
			AbstractNote endNote = sl.getEndNote();
			if(startNote == this){
				if(startNote != null){
					startNote.getSymbolLines().remove(sl);
					sl.setStartNote(null);
				}
				if(endNote != null){
					endNote.getSymbolLines().remove(sl);
					sl.setEndNote(null);
				}
				while(preLine != null){
					AbstractNote snote = preLine.getStartNote();
					AbstractNote enote = preLine.getEndNote();
					
				
						if(snote != null){
							snote.getSymbolLines().remove(preLine);
							preLine.setStartNote(null);
						}
						if(enote != null){
							enote.getSymbolLines().remove(preLine);
							preLine.setEndNote(null);
						}
					

					SymbolLine templine = (SymbolLine)preLine.getPreSymbolLine();
					if(templine != null){
						preLine.setPreSymbolLine(null);
						templine.setNextSymbolLine(null);
					}
					preLine = templine;
				}
				while(nxtLine != null){
					AbstractNote snote = nxtLine.getStartNote();
					AbstractNote enote = nxtLine.getEndNote();
			
						if(snote != null){
							snote.getSymbolLines().remove(nxtLine);
							nxtLine.setStartNote(null);
						}
						if(enote != null){
							enote.getSymbolLines().remove(nxtLine);
							nxtLine.setEndNote(null);
						}
					

					SymbolLine templine = (SymbolLine)nxtLine.getNextSymbolLine();
					if(templine != null){
						nxtLine.setNextSymbolLine(null);
						templine.setPreSymbolLine(null);
					}
					nxtLine = templine;
				}
			}



		}
		
		//连音号
		if(withTup && tuplet != null){
			for(int i = 0; i < tuplet.getNoteList().size(); i++){
				AbstractNote note = tuplet.getNoteList().get(i);
				if(note != this){
					tuplet.getNoteList().remove(note);
					note.setTuplet(null);
					note.removeAllSymbols(false);
				}
			}
		}
    }
	
	/**
	 * 音符提前产生的音符符号.
	 * @param nsl
	 */
    public void addNoteSymbol(NoteSymbol nsl){
    	if(nsl instanceof Ornament){
    		ornaments.add(nsl);
    	}else if(nsl instanceof Dynamic){
    		dynamics.add(nsl);
    	}else if(nsl instanceof PerformanceSymbol){
    		performanceSymbols.add(nsl);
    	}else if(nsl instanceof TremoloBeam){
    		tremoloBeam = (TremoloBeam)nsl;
    	}else if(nsl instanceof GraceSymbol){
    		graceSymbols.add(nsl);
    	}else if(nsl instanceof TempoText){
    		tempoText = (TempoText)nsl;
    	}else if(nsl instanceof Breath){
    		breath = (Breath)nsl;
    	}else if(nsl instanceof Pedal){
    		pedal = (Pedal)nsl;
    	}
    	nsl.setNote(this);
    }
    
    /**
     * 是否含有由字符串参数指定的装饰音/演奏记号
     * @param str 字符串参数
     * @return
     */
    public boolean hasOrnament(String str){
    	for(NoteSymbol nsl : ornaments){
    		if(nsl.getSymbolType().equalsIgnoreCase(str))
    			return true;
    	}
    	return false;
    }
    
	
    /**
     * 获得时长
     * @return
     */
	public int getDuration() {
		return duration;
	}

	/**
	 * 设置时长
	 * @param duration
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * 获得附点个数
	 * @return
	 */
	public int getDotNum() {
		return dotNum;
	}

	/**
	 * 设置附点个数
	 * @param dotNum
	 */
	public void setDotNum(int dotNum) {
		this.dotNum = dotNum;
	}

	/**
	 * 获得符杠类型
	 * @return
	 */
	public String getBeamType() {
		return beamType;
	}

	/**
	 * 设置符杠类型
	 * @param beamType
	 */
	public void setBeamType(String beamType) {
		this.beamType = beamType;
	}

	/**
	 * 获得所属的小节
	 * @return
	 */
	public Measure getMeasure() {
		return measure;
	}

	/**
	 * 设置小节
	 * @param measure
	 */
	public void setMeasure(Measure measure) {
		this.measure = measure;
	}

	/**
	 * 获得符杠实体
	 * @return
	 */
	public Beam getBeam() {
		return beam;
	}

	/**
	 * 设置符杠
	 * @param beam
	 */
	public void setBeam(Beam beam) {
		this.beam = beam;
	}

	/**
	 * 获得符杆
	 * @return
	 */
	public Stem getStem() {
		return stem;
	}
	/**
	 * 获得附点
	 * @return
	 */

	/**
	 * 设置符杆
	 * @param stem
	 */
	public void setStem(Stem stem) {
		this.stem = stem;
	}
	
	/**
	 * 若具有符杆，符杆是否朝上（符杆在上符头在下）
	 */
	public boolean isStemUp(){
		if(getStem() != null){
			if(getStem().getY() < getY()){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	/**
	 * 获得符尾
	 * @return
	 */
	public Tail getTail() {
		return tail;
	}

	/**
	 * 设置符尾
	 * @param tail
	 */
	public void setTail(Tail tail) {
		this.tail = tail;
	}
	
	/**
	 * 获得线条符号集合
	 * @return
	 */
	public ArrayList<SymbolLine> getSymbolLines() {
		return symbolLines;
	}

	/**
	 * 获得装饰符号集合
	 * @return
	 */
	public ArrayList<NoteSymbol> getOrnaments() {
		return ornaments;
	}
	/**
	 * 获得tie符号
	 */	
	public ArrayList<Tie> getTies() {
		return ties;
	}
	/**
	 * 设置tie符号
	 */
	public void setTies(ArrayList<Tie> ties) {
		this.ties = ties;
	}

	/**
	 * 获得力度记号等类似符号
	 * @return
	 */
//	public ArrayList<NoteSymbol> getDynamics() {
//		return dynamics;
//	}

	/**
	 * 获得歌词
	 * @return
	 */
//	public ArrayList<Lyrics> getLyrics() {
//		return lyrics;
//	}
	
	/**
	 * 获得连音号
	 */
	public Tuplet getTuplet() {
		return tuplet;
	}

	/**
	 * 设置连音号
	 * @param tuplet
	 */
	public void setTuplet(Tuplet tuplet) {
		this.tuplet = tuplet;
	}
	
	/**
	 * 设置在符杆上的颤音
	 * @param tuplet
	 */
	public void setTremoloBeam(TremoloBeam tB) {
		this.tremoloBeam = tB;
	}
	
	/**
	 * 设置颤音
	 * @param tuplet
	 */	
	public void setTremolo(Tremolo t) {
		this.tremolo = t;
	}
	
	


	/**
	 * 获得左倚音
	 * @return
	 */
	public List<AbstractNote> getLeftGraces() {
		return leftGraces;
	}

	/**
	 * 获得右倚音
	 * @return
	 */
	public List<AbstractNote> getRightGraces() {
		return rightGraces;
	}

	/**************************对音符符号、力度记号、歌词等进行封装****************************/
	
	public void addOrnament(NoteSymbol ns){
		ornaments.add(ns);
		ns.setNote(this);
	}
	
	public void addDynamics(NoteSymbol ns){
		dynamics.add(ns);
		ns.setNote(this);
	}
	
	public void addPerformanceSymbols(NoteSymbol ns){
		performanceSymbols.add(ns);
		ns.setNote(this);
	}
	
	public void addBreath(Breath breath){
		this.breath = breath;
		breath.setNote(this);
	}
	
	public void addPedal(Pedal pedal){
		this.pedal = pedal;
	}
	
	public void addTremoloBeam(TremoloBeam tB){
		this.tremoloBeam = tB;
	}
	
	public void addGraceSymbol(NoteSymbol gs){
		graceSymbols.add(gs);
		gs.setNote(this);
	}
	
	public void addLyrics(Lyrics l){
		lyrics.add(l);
		if(l != null)
			l.setNote(this);
	}
	
	public void removeOrnament(NoteSymbol ns){
		ornaments.remove(ns);
		ns.setNote(null);
	}
	
	public void removeDynamics(NoteSymbol ns){
		dynamics.remove(ns);
		ns.setNote(null);
	}
	
	public void removePerformanceSymbols(NoteSymbol ns){
		performanceSymbols.remove(ns);
		ns.setNote(null);
	}
	
	public void removeTremoloBeam(){
		if(tremoloBeam != null){
			tremoloBeam.setNote(null);
			tremoloBeam = null;
		}
	}
	
	public void removeBreath(){
		if(breath != null){
			breath.setNote(null);
			breath = null;
		}
	}
	
	public void removePedal(){
		if(pedal != null){
			pedal.setNote(null);
			pedal = null;
		}
	}
	
	public void removeGraceSymbol(NoteSymbol ns){
		graceSymbols.remove(ns);
		ns.setNote(null);
	}
	
	public void removeLyrics(Lyrics l){
		//最后一个歌词
		int index = lyrics.indexOf(l);
		if(index == lyrics.size() - 1)
			lyrics.remove(l);
		else
			lyrics.set(index, null);
		l.setNote(null);
	}
	
	public int getOrnamentsNum(){
		return ornaments.size();
	}
	
	public int getDynamicsNum(){
		return dynamics.size();
	}
	
	public int getPerformanceSymbolsNum(){
		return performanceSymbols.size();
	}
	
	public int getGraceSymbolNum(){
		return graceSymbols.size();
	}
	
	
	public int getLyricsNum(){
		return lyrics.size();
	}
	
	public NoteSymbol getOrnament(int index){
		return ornaments.get(index);
	}
	
	public TremoloBeam getTremoloBeam(){
		return tremoloBeam;
	}
	
	public NoteSymbol getDynamics(int index){
		return dynamics.get(index);
	}
	
	public NoteSymbol getPerformanceSymbols(int index){
		return performanceSymbols.get(index);
	}
	
	public NoteSymbol getGraceSymbols(int index){
		return graceSymbols.get(index);
	}
	
	public Lyrics getLyrics(int index){
		return lyrics.get(index);
	}
	
	public void clearOrnaments(){
		ornaments.clear();
	}
	
	public void clearDynamics(){
		dynamics.clear();
	}
	
	public void clearPerformanceSymbol(){
		performanceSymbols.clear();
	}
	
	public void clearLyrics(){
		lyrics.clear();
	}
	
	public void setLyrics(int index, Lyrics ly){
		lyrics.set(index, ly);
	}
	
	/******************************************************************************************/

	
    /********************************对倚音的序列的操作进行封装*******************************/
	
	/**
	 * 添加左倚音
	 */
	public void addLeftGrace(AbstractNote g){
		leftGraces.add(g);
		g.setMeasure(measure);
		if(g instanceof Grace){
			((Grace)g).setNote(this);
			((Grace)g).determineRealPitch();
		}
		if(g instanceof ChordGrace){
			ChordGrace cgrace = (ChordGrace)g;
			cgrace.setNote(this);
			for(int i = 0; i < cgrace.getNoteNum(); i++){
				Note nnote = cgrace.getNote(i);
				nnote.determineRealPitch();
			}
		}
	}
	
	/**
	 * 添加左倚音
	 * @param index
	 * @param g
	 */
	public void addLeftGrace(int index, AbstractNote g){
		leftGraces.add(index, g);
		g.setMeasure(measure);
		if(g instanceof Grace){
			((Grace)g).setNote(this);
		}
		if(g instanceof ChordGrace){
			ChordGrace cgrace = (ChordGrace)g;
			cgrace.setNote(this);
			for(int i = 0; i < cgrace.getNoteNum(); i++){
				Note nnote = cgrace.getNote(i);
				nnote.determineRealPitch();
			}
		}
	}
	
	/**
	 * 添加右倚音
	 */
	public void addRightGrace(AbstractNote g){
		rightGraces.add(g);
		g.setMeasure(measure);
		if(g instanceof Grace){
			((Grace)g).setNote(this);
			((Grace)g).determineRealPitch();
		}
		if(g instanceof ChordGrace){
			ChordGrace cgrace = (ChordGrace)g;
			cgrace.setNote(this);
			for(int i = 0; i < cgrace.getNoteNum(); i++){
				Note nnote = cgrace.getNote(i);
				nnote.determineRealPitch();
			}
		}
		
	}
	
	/**
	 * 添加右倚音
	 * @param index
	 * @param g
	 */
	public void addRightGrace(int index, AbstractNote g){
		rightGraces.add(index, g);
		g.setMeasure(measure);
		if(g instanceof Grace){
			((Grace)g).setNote(this);
		}
		if(g instanceof ChordGrace){
			ChordGrace cgrace = (ChordGrace)g;
			cgrace.setNote(this);
			for(int i = 0; i < cgrace.getNoteNum(); i++){
				Note nnote = cgrace.getNote(i);
				nnote.determineRealPitch();
			}
		}
	}
	
	/**
	 * 删除倚音
	 * @param g
	 */
	public void removeGrace(AbstractNote g){
		if(leftGraces.contains(g)){
			leftGraces.remove(g);
			if(g instanceof Grace)
				((Grace)g).setNote(null);
			else if(g instanceof ChordGrace)
				((ChordGrace)g).setNote(null);
			g.setMeasure(null);
			return;
		}
		else if(rightGraces.contains(g)){
			rightGraces.remove(g);
			if(g instanceof Grace)
				((Grace)g).setNote(null);
			else if(g instanceof ChordGrace)
				((ChordGrace)g).setNote(null);
			g.setMeasure(null);
			return;
		}
	}
	
	/******************************************************************************************/
	
	
	/**
	 * 加上附点之后的音符时长
	 * @return
	 */
	public int getDurationWithDot(){
		int dur = duration;
		if(dotNum ==1) 
			return dur + dur/2;
		else if(dotNum ==2)
			return dur + dur/2 + dur/4;
		else
			return duration;
	}
	
	/**
	 * 返回歌词所占的最长宽度,如果没有歌词，则返回0
	 * @return
	 */
	public int maxLyricWidth(){
		int max = 0;
		for(int i = 0; i < lyrics.size(); i++){
			Lyrics ly = lyrics.get(i);
			if(ly != null && ly.getWidth() > max)
				max = ly.getWidth();
		}
		return max;
	}
	
	/**
	 * 返回音符所包含的倚音所占的宽度
	 * @param direction 倚音位置，有效值有："left","right".分别代表左倚音和右倚音
	 * @return
	 */
	public int shortestGraceWidth(String direction){
		List<AbstractNote> list = null;
		if(direction.equalsIgnoreCase("left"))
			list = leftGraces;
		else if(direction.equalsIgnoreCase("right"))
			list = rightGraces;
		if(list != null){
			int result = 0;
			for(int i = 0; i < list.size()-1; i++){
				AbstractNote grace = list.get(i);
				result += MusicMath.shortestDistNeeded(grace, list.get(i+1), false);
			}
			AbstractNote firstGrace = list.get(0);
			AbstractNote lastGrace = list.get(list.size()-1);
			result += firstGrace.sharpOrFlatWidth();
			result += lastGrace.dotWidth();
			return result;
		}
		return 0;
	}

	/**
	 * 获得速度记号
	 * @return
	 */
	public TempoText getTempoText() {
		return tempoText;
	}

	/**
	 * 设置速度记号
	 * @param tempoText
	 */
	public void setTempoText(TempoText tempoText) {
		this.tempoText = tempoText;
	}

	/**
	 * 获得呼吸记号
	 * @return
	 */
	public Breath getBreath() {
		return breath;
	}

	/**
	 * 设置呼吸记号
	 * @param breath
	 */
	public void setBreath(Breath breath) {
		this.breath = breath;
	}

	public Pedal getPedal() {
		return pedal;
	}

	public void setPedal(Pedal pedal) {
		this.pedal = pedal;
	}

	public int getVoice() {
		return voice;
	}

	public void setVoice(int voice) {
		this.voice = voice;
	}

}
