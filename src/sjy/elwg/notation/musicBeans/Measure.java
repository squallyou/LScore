package sjy.elwg.notation.musicBeans;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

import sjy.elwg.notation.NoteCanvas;
import sjy.elwg.utility.Controller;
import sjy.elwg.utility.MusicMath;

/**
 * 小节.包含谱、调、拍号等属性.
 * 由于乐谱结构没有区分乐器与声部概念，因此对于乐器的区分通过每个小节的乐器名字来区分.乐器缩写即MIDI乐器作为辅助.
 * 每个小节组中的多个小节，可能属于不同的乐器.因此为小节增加乐器等相关字段。
 * 对于两个小节属于同于一个乐器的情况，例如钢琴的两个声部，我们做如下处理：对第一个声部设置乐器名称，而第二个声部
 * 则为null. 如果声部没有名字，则为"none"，但不能为null，否则将其视为另外一个乐器的非第一声部.
 * 
 * 小节分为两种：正常小节与无限制节拍小节. 他们的唯一不同在与其无限制节拍小节没有拍号的概念，即小节内可以盛放的音
 * 符个数没有限制。该类型的小节通常用于做题模式.
 * 
 * 一个小节最多支持两个声部(voices)，一个主声部和一个副声部，分别为声部1和声部2。主声部一定不为空，副声部可以为空
 * （多数情况下都为空）.如果主副声部同时存在，其符杆方向相反，默认主声部朝上，副声部朝下。
 * 
 * 向小节添加删除音符，调用:measure.addNote(note)或measure.remove(note)
 * 
 * @author jingyuan.sun
 *
 */
public class Measure extends JPanel implements Selectable,Equalable{
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 6L;
	/**
	 * 小节高度
	 */
	public static final int MEASURE_HEIGHT = 41;
	/**
	 * 单个小节支持的声部上限
	 */
	public static final int VOICE_NUM = 2;
	/**
	 * 主声部音符序列
	 */
	private ArrayList<AbstractNote> noteList;
	/**
	 * 副声部音符序列
	 */
	private V2NoteList v2NoteList;
	/**
	 * 所属的小节组
	 */
	private MeasurePart measurePart;
	
	/**
	 * 谱号类型.有效值有："g/2", "f/4",分别表示高音和低音谱号
	 */
	private String clefType;
	
	/**
	 * 调号值，用来表示调号类型。数值范围从-7到7.正数表示升号，负数表示降号.0表示普通调式
	 */
	private int keyValue;
	
	/**
	 * 拍号类型
	 */
	private Time time;
	
	/**
	 * 谱号，调号与拍号的UI实体.
	 * 注意：UI实体并不一定与小节相应的属性值一一对应.
	 */
	private UIClef uiClef;
	private UIKey uiKey;
	private UITime uiTime;
	
	/**
	 * 声部名字、缩写、MIDI乐器.
	 * MIDI乐器是MIDI规范中所规定的乐器频道值，通常是阿拉伯数字，这里采取字符串形式.
	 */
	private String partName;
	private String partAbrre;
	private String instrument;
	
	/**
	 * 是否被选择
	 */
	private boolean selected;
	
	/**
	 * 小节被选择时的方框
	 */
	private JPanel selector;
	
	/**
	 * 标注集合
	 */
	private ArrayList<Annotation> annotations;
	
	
	/**
	 * 普通小节构造函数
	 */
	public Measure(String clefType, int keyValue, Time time){
		super();
		this.clefType = clefType;
		this.keyValue = keyValue;
		this.time = new Time(time.getBeats(), time.getBeatType());
		noteList = new ArrayList<AbstractNote>();
		v2NoteList = new V2NoteList();
		annotations = new ArrayList<Annotation>();
		setOpaque(false);
		setBackground(Color.yellow);
		setLayout(null);
	}
	
	/**
	 * 普通小节普通拍号构造函数
	 * @param clefType
	 * @param keyValue
	 * @param beats
	 * @param beatType
	 */
	public Measure(String clefType, int keyValue, int beats,int beatType){
		super();
		this.clefType = clefType;
		this.keyValue = keyValue;
		this.time = new Time(beats, beatType);
		noteList = new ArrayList<AbstractNote>();
		v2NoteList = new V2NoteList();
		annotations = new ArrayList<Annotation>();
		setOpaque(false);
		setBackground(Color.yellow);
		setLayout(null);
	}
	/**
	 * 普通小节混合拍号构造函数
	 * @param clefType
	 * @param keyValue
	 * @param beat
	 * @param beatType
	 */
	public Measure(String clefType, int keyValue, int[] beat,int beatType){
		super();
		this.clefType = clefType;
		this.keyValue = keyValue;
		this.time = new Time(beat, beatType);
		noteList = new ArrayList<AbstractNote>();
		v2NoteList = new V2NoteList();
		annotations = new ArrayList<Annotation>();
		setOpaque(false);
		setBackground(Color.yellow);
		setLayout(null);
	}
	

	/**
	 * 无限制节拍小节构造函数
	 * @param clefType
	 * @param keyValue
	 */
	public Measure(String clefType, int keyValue){
		super();
		this.clefType = clefType;
		this.keyValue = keyValue;
		noteList = new ArrayList<AbstractNote>();
		v2NoteList = new V2NoteList();
		annotations = new ArrayList<Annotation>();
		setOpaque(false);
		setBackground(Color.yellow);
		setLayout(null);
	}
	
	/**
	 * 返回小节所在的小节组
	 * @return
	 */
	public MeasurePart getMeasurePart() {
		return measurePart;
	}

	/**
	 * 设置小节组
	 * @param measurePart
	 */
	public void setMeasurePart(MeasurePart measurePart) {
		this.measurePart = measurePart;
	}
	
	/**
	 * 返回小节的注释集合
	 * @return
	 */
	public ArrayList<Annotation> getAnnotations() {
		return annotations;
	}
	
	/**
	 * 向指定声部添加音符
	 * @param note
	 * @param voiceNum
	 */
	private void addVoiceNote(AbstractNote note, int voiceNum){
		if(voiceNum == 0){
			noteList.add(note);
			note.setVoice(0);
		}
		else if(voiceNum == 1){
			v2NoteList.list.add(note);
			note.setVoice(1);
		}
		else{
			System.err.println("无法添加音符,声部序号异常: " + voiceNum);
			return;
		}
		note.setMeasure(this);
		makeRealPitch(note);
//		System.out.println("note voice: " + voiceNum);
	}
	
	/**
	 * 向指定声部插入音符
	 * @param index
	 * @param note
	 * @param voiceNum
	 */
	private void addVoiceNote(int index, AbstractNote note, int voiceNum){
		if(voiceNum == 0){
			noteList.add(index, note);
			note.setVoice(0);
		}
		else if(voiceNum == 1){
			v2NoteList.list.add(index, note);
			note.setVoice(1);
		}
		else{
			System.err.println("无法添加音符,声部序号异常: " + voiceNum);
			return;
		}
		note.setMeasure(this);
		makeRealPitch(note);
	}
	
	/**
	 * 计算音符绝对音高.
	 * 注意：该方法仅在addVoiceNote()方法内部调用
	 * @param note
	 */
	private void makeRealPitch(AbstractNote note){
		if(note instanceof Note){
			Note nnote = (Note)note;
			if(!nnote.isRest())
				nnote.determineRealPitch();
		}
		else if(note instanceof ChordNote){
			ChordNote cnote = (ChordNote)note;
			for(int i = 0, n = cnote.getNoteNum(); i < n; i++){
				Note nnote = cnote.getNote(i);
				nnote.determineRealPitch();
			}
		}
	}
	
	/************  以下方法对noteList的方法进行了封装,不允许在measure以外直接获得noteList的引用  ****************/

	/**
	 * 向小节指定声部追加音符
	 * @param note
	 */
	public void addNote(AbstractNote note, int voice){
		addVoiceNote(note, voice);
	}
	
	/**
	 * 在指定声部指定位置插入音符
	 * @param index
	 * @param note
	 */
	public void addNote(int index, AbstractNote note, int voice){
		addVoiceNote(index, note, voice);
	}
	
	/**
	 * 返回指定位置的音符
	 * @param index
	 * @return
	 */
	public AbstractNote getNote(int index, int voice){
		AbstractNote note = voice == 0 ? noteList.get(index) : v2NoteList.list.get(index);
		return note;
	}
	
	/**
	 * 删除音符
	 * @param index
	 */
	public void removeNote(int index, int voice){
		if(voice == 0){
			noteList.get(index).setMeasure(null);
			noteList.get(index).setVoice(-1);
			noteList.remove(index);
		}
		else if(voice == 1){
			v2NoteList.list.remove(index);
			v2NoteList.list.get(index).setVoice(-1);
			v2NoteList.list.get(index).setMeasure(null);
		}
	}
	
	/**
	 * 删除音符
	 * @param note
	 */
	public void removeNote(AbstractNote note){
		if(note.getVoice() == 0){
			note.setMeasure(null);
			noteList.remove(note);
			note.setVoice(-1);
		}else if(note.getVoice() == 1){
			note.setMeasure(null);
			v2NoteList.list.remove(note);
			note.setVoice(-1);
		}
	}
	
	/**
	 * 返回指定声部的音符个数
	 * 如果指定的声部不存在，则返回-1.
	 * @return
	 */
	public int getNoteNum(int voice){
		return noteNumInVoice(voice);
	}
	
	/**
	 * 两个声部音符的总数目
	 * @return
	 */
	public int totalNoteNum(){
		return noteList.size() + v2NoteList.list.size();
	}
	
	/**
	 * 返回指定声部的音符个数
	 * 如果指定声部异常，则返回-1.
	 * @param index
	 * @return
	 */
	private int noteNumInVoice(int index){
		return index == 0 ? noteList.size() : (index == 1 ? v2NoteList.list.size() : -1);
	}
	
	/**
	 * 返回小节内音符位置,不包含指定音符则返回-1.
	 * @param note
	 * @return
	 */
	public int noteIndex(AbstractNote note){
		if(note.getVoice() == 0 || noteList.contains(note))
			return noteList.indexOf(note);
		else if(note.getVoice() == 1 || v2NoteList.list.contains(note))
			return v2NoteList.list.indexOf(note);
		else 
			return -1;
	}
	
	/****************************************************************************************/
	
	/**
	 * 将小节多个声部的音符按照时间戳序列，每个序列包含最多voice个音符，其中voice为小节声部个数.
	 */
	public List<List<AbstractNote>> getNotesByTimeSlot(){
		List<List<AbstractNote>> result = new ArrayList<List<AbstractNote>>();
		//各声部的累积时长
		int voiceNum = v2NoteList.list.size() == 0 ? 1 : 2;
		int[] accumDur = new int[voiceNum];
		float[] faccumDur = new float[voiceNum];
		//各声部当前音符序号
		int noteIndex1 = 0;
		int noteIndex2 = 0;
		while(true){
			List<AbstractNote> slot = new ArrayList<AbstractNote>();
			int accum = MusicMath.minValue(accumDur);
			if(accumDur[0] == accum){
				if(noteIndex1 < noteList.size()){
					AbstractNote note = noteList.get(noteIndex1++);
					slot.add(note);
					faccumDur[0] += note.getRealDuration();
					accumDur[0] = Math.round(faccumDur[0]);
				}
			}
			if(v2NoteList.list.size() != 0 && accumDur[1] == accum){
				if(noteIndex2 < v2NoteList.list.size()){
					AbstractNote note = v2NoteList.list.get(noteIndex2++);
					slot.add(note);
					faccumDur[1] += note.getRealDuration();
					accumDur[1] = Math.round(faccumDur[1]);
				}
			}
			if(!slot.isEmpty())
				result.add(slot);
			else if(noteIndex1 < noteList.size()){
				AbstractNote note = noteList.get(noteIndex1++);
				slot.add(note);
				faccumDur[0] += note.getRealDuration();
				accumDur[0] = Math.round(faccumDur[0]);
				result.add(slot);
			}
			else{
				return result;
			}
		}
	}
	
	/**
	 * 跟getNotesByTimeSlot()功能类似，只是包含的元素类型有所变化
	 * @return
	 */
	public List<MeasurePart.NListWithMeaIndex> getMeasureSlots(){
		List<MeasurePart.NListWithMeaIndex> result = new ArrayList<MeasurePart.NListWithMeaIndex>();
		int meaIndex = measurePart.getMeasureList().indexOf(this);
		//各声部的累积时长
		int voiceNum = v2NoteList.list.size() == 0 ? 1 : 2;
		int[] accumDur = new int[voiceNum];
		float[] faccumDur = new float[voiceNum];
		//各声部当前音符序号
		int noteIndex1 = 0;
		int noteIndex2 = 0;
		while(true){
			List<AbstractNote> slot = new ArrayList<AbstractNote>();
			int accum = MusicMath.minValue(accumDur);
			if(accumDur[0] == accum){
				if(noteIndex1 < noteList.size()){
					AbstractNote note = noteList.get(noteIndex1++);
					slot.add(note);
					faccumDur[0] += note.getRealDuration();
					accumDur[0] = Math.round(faccumDur[0]);
				}
			}
			if(v2NoteList.list.size() != 0 && accumDur[1] == accum){
				if(noteIndex2 < v2NoteList.list.size()){
					AbstractNote note = v2NoteList.list.get(noteIndex2++);
					slot.add(note);
					faccumDur[1] += note.getRealDuration();
					accumDur[1] = Math.round(faccumDur[1]);
				}
			}
			if(!slot.isEmpty()){
				MeasurePart.NListWithMeaIndex mn = measurePart.new NListWithMeaIndex(slot, meaIndex);
				result.add(mn);
			}
			else if(noteIndex1 < noteList.size()){
				AbstractNote note = noteList.get(noteIndex1++);;
				slot.add(note);
				faccumDur[1] += note.getRealDuration();
				accumDur[1] = Math.round(faccumDur[1]);
				MeasurePart.NListWithMeaIndex mn = measurePart.new NListWithMeaIndex(slot, meaIndex);
				result.add(mn);
			}else{
				return result;
			}
		}
	}
	
	
	/**
	 * 画图
	 */
	public void paintComponent(Graphics g){
		for(int i = 0; i < 5; i++){
			g.drawLine(0, i * NoteCanvas.LINE_GAP, getWidth(), i * NoteCanvas.LINE_GAP);
		}
	}
	
	/**
	 * 放置小节谱号
	 * @param x
	 */
	public void locateClef(int x){
		if(uiClef != null){
			uiClef.setLocation(x, getY() + uiClef.getPositionOffset());
		}
	}
	

	
	/**
	 * 放置小节拍号
	 * @param x
	 */
	public void locateTime(int x){
		if(uiTime != null){
			uiTime.setLocation(x, getY() + uiTime.getPositionOffset());
		}
	}
	
	/**
	 * 放置小节调号
	 * @param x
	 */
	public void locateKey(int x){
		if(uiKey != null){
			uiKey.setLocation(x, getY() + uiKey.getPositionOffset());
		}
	}

	/**
	 * 返回谱号类型
	 * @return
	 */
	public String getClefType() {
		return clefType;
	}

	/**
	 * 设置谱号类型
	 * @param clefType
	 */
	public void setClefType(String clefType) {
		this.clefType = clefType;
	}

	/**
	 * 返回调号类型
	 * @return
	 */
	public int getKeyValue() {
		return keyValue;
	}

	/**
	 * 设置调号类型
	 * @param keyValue
	 */
	public void setKeyValue(int keyValue) {
		this.keyValue = keyValue;
	}

	/**
	 * 返回拍号实体
	 * @return
	 */
	public Time getTime() {
		return time;
	}

	/**
	 * 设置拍号实体
	 * @param time
	 */
	public void setTime(Time time) {
		this.time = time;
	}

	/**
	 * 返回谱号实体
	 * @return
	 */
	public UIClef getUiClef() {
		return uiClef;
	}

	/**
	 * 设置谱号实体
	 * @param uiClef
	 */
	public void setUiClef(UIClef uiClef) {
		this.uiClef = uiClef;
	}

	/**
	 * 获得调号实体
	 * @return
	 */
	public UIKey getUiKey() {
		return uiKey;
	}

	/**
	 * 设置调号实体
	 * @param uiKey
	 */
	public void setUiKey(UIKey uiKey) {
		this.uiKey = uiKey;
	}

	/**
	 * 返回拍号实体
	 * @return
	 */
	public UITime getUiTime() {
		return uiTime;
	}

	/**
	 * 设置拍号实体
	 * @param uiTime
	 */
	public void setUiTime(UITime uiTime) {
		this.uiTime = uiTime;
	}

	/**
	 * 返回小节所在的乐器名称
	 * @return
	 */
	public String getPartName() {
		return partName;
	}

	/**
	 * 设置小节所在的乐器名称0
	 * @param partName
	 */
	public void setPartName(String partName) {
		this.partName = partName;
	}

	/**
	 * 返回乐器缩写
	 * @return
	 */
	public String getPartAbrre() {
		return partAbrre;
	}

	/**
	 * 设置乐器缩写
	 * @param partAbrre
	 */
	public void setPartAbrre(String partAbrre) {
		this.partAbrre = partAbrre;
	}

	/**
	 * 返回MIDI乐器
	 * @return
	 */
	public String getInstrument() {
		return instrument;
	}

	/**
	 * 设置MIDI乐器
	 * @param instrument
	 */
	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	/**
	 * 返回小节被选择时的选择框
	 * @return
	 */
	public JPanel getSelector() {
		return selector;
	}

	/**
	 * 设置小节选择框
	 * @param selector
	 */
	public void setSelector(JPanel selector) {
		this.selector = selector;
	}

	/**
	 * 返回小节是否被选中
	 * @return
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * 设置小节选中
	 * @param selected
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * 返回谱号，调号和拍号的宽度之和.
	 * @return
	 */
	public int getAttrWidth(){
		//谱号，调号，拍号的间隔
		int result = 0;
		int deltax = uiClef == null ? 0 : uiClef.getWidth() + UIClef.CLEF_GAP;
		result += deltax;
		deltax = uiKey == null ? 0 : uiKey.getWidth() + UIKey.KEY_GAP;
		result += deltax;
		deltax = uiTime == null ? 0 : uiTime.getWidth() + UITime.TIME_GAP;
		result += deltax;
		
		return result;
	}
	
	/**
	 * 生成被选择时的方框
	 */
	public void generateSelector(){
		if(selector == null){
			selector = new JPanel();
			selector.setOpaque(false);
			selector.setSize(getWidth() + NoteCanvas.LINE_GAP, getHeight() + 2 * NoteCanvas.LINE_GAP);
			Border bd = BorderFactory.createLineBorder(Color.blue, 2);
			selector.setBorder(bd);
			selector.setLocation(getX() - NoteCanvas.LINE_GAP/2, getY() - NoteCanvas.LINE_GAP);
			((JLayeredPane)getParent()).add(selector, JLayeredPane.DRAG_LAYER);
		}
	}
	
	/**
	 * 去掉选择器
	 */
	public void removeSelector(){
		if(selector != null && getParent() != null){
			Page page = (Page)getParent();
			page.remove(selector);
			page.revalidate();
			page.updateUI();
			((JComponent)(page.getParent().getParent())).updateUI();
			selector = null;
		}
		selector = null;
	}

	public void cancleSelected() {
		// TODO Auto-generated method stub
		selected = false;
		removeSelector();
		for(AbstractNote note : noteList){
			note.cancleSelected();
		}
		for(AbstractNote note : v2NoteList.list){
			note.cancleSelected();
		}
	}

	public void beSelected() {
		// TODO Auto-generated method stub
		selected = true;
		if(selector == null){
			generateSelector();
		}
		for(AbstractNote note : noteList){
			note.beSelected();
		}
		for(AbstractNote note : v2NoteList.list){
			note.beSelected();
		}
	}
	
	 /**
	  * 删除小节谱号、拍号等实体
	  */
	public void deleteMeaAttr(){
		if(uiClef != null){
			JComponent parent = (JComponent)uiClef.getParent();
			parent.remove(uiClef);
		}
		if(uiKey != null){
			JComponent parent = (JComponent)uiKey.getParent();
			parent.remove(uiKey);
		}
		if(uiTime != null){
			JComponent parent = (JComponent)uiTime.getParent();
			parent.remove(uiTime);
		}
	}


	/**
	 * 放置注释
	 */
	public void locateAnnotations(){
		for(Annotation an : annotations){
			if(an.getRelatedObjts().get(0) == this){
				an.setLocation(getX() + NoteCanvas.LINE_GAP + an.getDraggedX(),
						getY() - an.getHeight() - 2*NoteCanvas.LINE_GAP + an.getDraggedY());
			}
		}
	}
	
	/**
	 * 小节是否是无限制节拍的
	 * @return
	 */
	public boolean isUnlimited(){
		return time == null;
	}
	
	/**
	 * 返回音符序列
	 * @return
	 */
	public ArrayList<AbstractNote> getNoteList(){
		return noteList;
	}

	@Override
	public boolean equalsWith(Object o) {
		// TODO Auto-generated method stub
		if(!(o instanceof Measure))
			return false;
		Measure measure = (Measure)o;
		if(measure.getNoteNum(0) != noteList.size() || measure.getNoteNum(1) != v2NoteList.list.size())
			return false;
		int noteNum = noteList.size();
		AbstractNote[] list1 = new AbstractNote[noteNum];
		AbstractNote[] list2 = new AbstractNote[noteNum];
		
		return MusicMath.equals(noteList.toArray(list1), measure.getNoteList().toArray(list2));
	}
	
	/**
	 * 副声部是否在下面
	 * 该方法决定着声部符杆的绘制，因此主要在绘制符杠时调用
	 * @return
	 */
	public boolean isVoice2Down(){
		return v2NoteList.direction.equals("down");
	}
	
	/**
	 * 返回音符所在声部
	 * @param note
	 * @return
	 */
	public int getVoice(AbstractNote note){
		if(note.getVoice() != -1)
			return note.getVoice();
		return noteList.contains(note) ? 1 : (v2NoteList.list.contains(note) ? 2 : -1);
	}
	
	/**
	 * 返回小节声部个数
	 * 当且仅当第二声部为空时，返回1；否则返回2
	 * @return
	 */
	public int getVoiceNum(){
		int n = v2NoteList.list.size() == 0 ? 1 : 2;
		return n;
	}
	
	/**
	 * 副声部音符的封装类
	 * @author sjy
	 *
	 */
	class V2NoteList{
		/**
		 * 副声部音符位置，有效值"up"和"down".
		 */
		String direction = "down";
		/**
		 * 音符序列
		 */
		List<AbstractNote> list = new ArrayList<AbstractNote>();
	}
	
	/**
	 * 画符杠符杆符柄
	 */
	public void drawBeamAndStem(){
		JComponent page = (JComponent)getParent();
		if(getVoiceNum() == 1)
			Controller.drawBeamAndStem(page, noteList);
		else{
			if(v2NoteList.direction.equalsIgnoreCase("down")){
				Controller.drawBeamAndStem(page, noteList, "up");
				Controller.drawBeamAndStem(page, v2NoteList.list, "down");
			}
			else if(v2NoteList.direction.equalsIgnoreCase("up")){
				Controller.drawBeamAndStem(page, noteList, "down");
				Controller.drawBeamAndStem(page, v2NoteList.list, "up");
			}
		}
	}

}
