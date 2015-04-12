package sjy.elwg.notation.musicBeans;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

import sjy.elwg.notation.NoteCanvas;
import sjy.elwg.notation.musicBeans.symbolLines.NoteSymbol;
import sjy.elwg.notation.musicBeans.symbolLines.RepeatLine;
import sjy.elwg.utility.MusicMath;

/**
 * 小节组,包含若干个小节
 * @author jingyuan.sun
 *
 */
public class MeasurePart implements Selectable,Equalable{
	
	/**
	 * 小节数组
	 */
	private ArrayList<Measure> measureList;
	/**
	 * 所在的行
	 */
	private NoteLine noteLine;
	/**
	 * 小节线
	 */
	private Barline barline;
	/**
	 * 反复记号
	 */
	protected ArrayList<NoteSymbol> repeatSymbol;
	
	/**
	 * 反复记号
	 */
	protected ArrayList<RepeatLine> repeatLines;
	/**
	 * X坐标值
	 */
	private int x;
	/**
	 * y坐标值
	 */
	private int y;
	/**
	 * 宽度
	 */
	private int width;
	/**
	 * 被选择时出现的框
	 */
	private JPanel selector;
	
	/**
	 * 构造函数
	 */
	public MeasurePart(){
		measureList = new ArrayList<Measure>();
		repeatSymbol = new ArrayList<NoteSymbol>();
		repeatLines = new ArrayList<RepeatLine>();
	}
	
	/********  对measureList的方法进行封装，不允许外界直接访问measureList  *********/
	
	/**
	 * 添加小节
	 */
	public void addMeasure(Measure measure){
		measureList.add(measure);
		measure.setMeasurePart(this);
	}
	
	/**
	 * 添加小节
	 * @param index
	 * @param measure
	 */
	public void addMeasure(int index, Measure measure){
		measureList.add(index, measure);
		measure.setMeasurePart(this);
	}
	
	/**
	 * 删除小节
	 * @param measure
	 */
	public void removeMeasure(Measure measure){
		measure.setMeasurePart(null);
		measureList.remove(measure);
	}
	
	/**
	 * 获得小节
	 * @param index
	 * @return
	 */
	public Measure getMeasure(int index){
		return measureList.get(index);
	}
	
	/**
	 * 小节在小节组中的位置
	 * @param measure
	 * @return
	 */
	public int measureIndex(Measure measure){
		return measureList.indexOf(measure);
	}
	
	/**
	 * 小节个数
	 * @return
	 */
	public int getMeasureNum(){
		return measureList.size();
	}
	
	/*******************************************************/

	/**
	 * 获得小节线
	 */
	public Barline getBarline() {
		return barline;
	}

	/**
	 * 设置小节线
	 * @param barline
	 */
	public void setBarline(Barline barline) {
		this.barline = barline;
	}
	
	/**
	 * 返回X坐标值,即小节组中第一个小节的X坐标
	 * @return
	 */
	public int getX() {
		return x;
	}

	/**
	 * 设置X坐标值
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * 返回Y坐标值
	 * @return
	 */
	public int getY() {
		return y;
	}

	/**
	 * 设置Y坐标值
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * 记录音符小节序列的实体类,充当结构体
	 *
	 */
	public class NoteWithMeaIndex{
		private AbstractNote note;
		private int meaIndex;
		public NoteWithMeaIndex(AbstractNote note, int noteIndex){
			this.note = note;
			this.meaIndex = noteIndex;
		}
		public AbstractNote getNote() {
			return note;
		}
		public int getMeaIndex() {
			return meaIndex;
		}
	}
	
	/**
	 * 某一时刻不同声部音符的组合类.
	 * @author sjy
	 *
	 */
	public class NListWithMeaIndex{
		private List<AbstractNote> list;
		private int meaIndex;
		public NListWithMeaIndex(List<AbstractNote> list, int index){
			this.list = list;
			this.meaIndex = index;
		}
		public List<AbstractNote> getList(){
			return list;
		}
		public int getMeaIndex(){
			return meaIndex;
		}
		/**
		 * 两个声部的音符是否交叉
		 * 如果交叉，在音符排列时这两个音符将并排排列，而不是上下排列
		 * @return
		 */
		public boolean isInteracted(){
			if(list.size() < 2)  //音符少于两个
				return false;
			if((list.get(0) instanceof Note && ((Note)list.get(0)).isRest()) ||   //有一个是休止符
					(list.get(1) instanceof Note && ((Note)list.get(1)).isRest()))
				return false;
			Measure measure = measureList.get(meaIndex);
			AbstractNote note1 = list.get(0);
			AbstractNote note2 = list.get(1);
			if(note1.absHigherThan(note2) && ((note1.getVoice() == 0 && measure.isVoice2Down()) || 
					(note1.getVoice() == 1 && !measure.isVoice2Down())))
				return false;
			if(note2.absHigherThan(note1) && ((note2.getVoice() == 0 && measure.isVoice2Down()) || 
					(note2.getVoice() == 1 && !measure.isVoice2Down())))
				return false;
			return true;
		}
		/**
		 * 如果交叉，返回交叉时放置在右边的音符，即符杆朝下的音符
		 * 如果没有交叉，返回null
		 * @return
		 */
		public AbstractNote getRightNote(){
			if(!isInteracted())
				return null;
			Measure measure = measureList.get(meaIndex);
			int voiceIndex = -1;
			voiceIndex = measure.isVoice2Down() ? 1 : 0;
			for(int i = 0; i < list.size(); i++){
				AbstractNote note = list.get(i);
				if(note.getVoice() == voiceIndex)
					return note;
			}
			return null;
		}
		/**
		 * 如果交叉，返回交叉时放置在左边的音符，即符杆朝上的音符
		 * 如果没有交叉，返回null
		 * @return
		 */
		public AbstractNote getLeftNote(){
			if(!isInteracted())
				return null;
			Measure measure = measureList.get(meaIndex);
			int voiceIndex = -1;
			voiceIndex = measure.isVoice2Down() ? 0 : 1;
			for(int i = 0; i < list.size(); i++){
				AbstractNote note = list.get(i);
				if(note.getVoice() == voiceIndex)
					return note;
			}
			return null;
		}
		/**
		 * 放置各个音符序列
		 * @param x
		 */
		public void locateNotes(int x){
			if(!isInteracted()){
				for(AbstractNote n : list){
					n.locateNote(x);
				}
			}else{
				Measure measure = measureList.get(meaIndex);
				if(measure.isVoice2Down()){
					for(AbstractNote n : list){
						if(n.getVoice() == 1){
							int xx = n.getWidth() > Note.NORMAL_HEAD_WIDTH ? x : x + Note.NORMAL_HEAD_WIDTH + 1;
							n.locateNote(xx);
						}else{
							n.locateNote(x);
						}
					}
				}else{
					for(AbstractNote n : list){
						if(n.getVoice() == 0){
							int xx = n.getWidth() > Note.NORMAL_HEAD_WIDTH ? x : x + Note.NORMAL_HEAD_WIDTH + 1;
							n.locateNote(xx);
						}else{
							n.locateNote(x);
						}
					}
				}
			}
		}
		/**
		 * 返回音符序列所占的宽度
		 * @return
		 */
		public int getWidth(){
			int w = isInteracted() ? Note.NORMAL_HEAD_WIDTH * 2 + 1 : Note.NORMAL_HEAD_WIDTH;
			return w;
		}
	}
	
	
	/**
	 * 返回小节组按照时间槽排列的音符序列
	 * @return
	 */
	public List<List<NoteWithMeaIndex>> getNoteListByTimeSlot(){
		int meaNum = measureList.size();
		//各小节的累积时长
		int[] accumDur = new int[meaNum];
		float[] faccumDur = new float[meaNum];
		//各小节当前时间戳序号
		int[] slotIndex = new int[meaNum];
		
		List<List<List<AbstractNote>>> meaSlots = new ArrayList<List<List<AbstractNote>>>();  //将各个小节的时间戳序列合并为一个
		for(int i = 0; i < meaNum; i++){
			Measure measure = measureList.get(i);
			meaSlots.add(measure.getNotesByTimeSlot());
		}
		
		List<List<NoteWithMeaIndex>> result = new ArrayList<List<NoteWithMeaIndex>>();
		Loop:
		while(true){
			List<NoteWithMeaIndex> timeSlot = new ArrayList<NoteWithMeaIndex>();
			int minDur = MusicMath.minValue(accumDur);
			for(int i = 0;i < meaNum; i++){
				if(accumDur[i] == minDur){
					if(slotIndex[i] >= meaSlots.get(i).size())
						break Loop;
					List<AbstractNote> l = meaSlots.get(i).get(slotIndex[i]);
					for(int j = 0; j < l.size(); j++){
						NoteWithMeaIndex nw = new NoteWithMeaIndex(l.get(j), i);
						timeSlot.add(nw);
					}
					faccumDur[i] += l.get(0).getRealDuration();
					slotIndex[i]++;
					accumDur[i] = Math.round(faccumDur[i]);
				}
			}
			result.add(timeSlot);
		}
		return result;
	}
	
	/**
	 * 与getNoteListByTimeSlot()功能类似。不同的是考虑小节所有声部
	 * @return
	 */
	public List<List<NListWithMeaIndex>> getNotesByTimeSlot(){
		int meaNum = measureList.size();
		//各小节的累积时长
		int[] accumDur = new int[meaNum];
		float[] faccumDur = new float[meaNum];
		//各小节当前时间戳序号
		int[] slotIndex = new int[meaNum];
		
		List<List<List<AbstractNote>>> meaSlots = new ArrayList<List<List<AbstractNote>>>();  //将各个小节的时间戳序列合并为一个
		for(int i = 0; i < meaNum; i++){
			Measure measure = measureList.get(i);
			meaSlots.add(measure.getNotesByTimeSlot());
		}
		
		List<List<NListWithMeaIndex>> result = new ArrayList<List<NListWithMeaIndex>>();
		Loop:
		while(true){
			List<NListWithMeaIndex> list = new ArrayList<NListWithMeaIndex>();
			int minDur = MusicMath.minValue(accumDur);
			for(int i = 0; i < meaNum; i++){
				if(accumDur[i] == minDur){
					if(slotIndex[i] >= meaSlots.get(i).size())
						break Loop;
					List<AbstractNote> l = meaSlots.get(i).get(slotIndex[i]);
					NListWithMeaIndex nwmi = new NListWithMeaIndex(l, i);
					list.add(nwmi);
					faccumDur[i] += MusicMath.minRealDuration(l);
					slotIndex[i]++;
					accumDur[i] = Math.round(faccumDur[i]);
				}
			}
			result.add(list);
		}
		return result;
	}

	/**
	 * 返回小节组宽度,与其中每个小节的宽度都相同
	 * @return
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * 设置小节组宽度
	 * @param width
	 */
	public void setWidth(int width) {
		this.width = width;
	}
    
	/**
	 * 返回小节组高度,其值为从第一个小节的第一根线到最后一个小节的最后一根线的高度差
	 * @return
	 */
	public int getHeight() {
		int height = 0;
		for(int i = 0, n = measureList.size() - 1; i < n; i++){
			height += Measure.MEASURE_HEIGHT + noteLine.getMeasureGaps().get(i);
		}
		height += Measure.MEASURE_HEIGHT;
		return height;
	}

	/**
	 * 返回小节组所在的行
	 * @return
	 */
	public NoteLine getNoteLine() {
		return noteLine;
	}

	/**
	 * 设置行
	 * @param noteLine
	 */
	public void setNoteLine(NoteLine noteLine) {
		this.noteLine = noteLine;
	}
	
	/**
	 * 返回小节组各个小节谱号、调号、拍号宽度之和的最大值
	 * @return
	 */
	public int maxAttrWidth(){
		int result = 0;
		for(int i = 0, n = measureList.size(); i < n; i++){
			Measure measure = measureList.get(i);
			if(measure.getAttrWidth() > result)
				result = measure.getAttrWidth();
		}
		return result;
	}
	
	/**
	 * 产生被选择框
	 */
	public void generateSelector(){
		if(selector == null){
			selector = new JPanel();
			selector.setOpaque(false);
			selector.setSize(getWidth() + NoteCanvas.LINE_GAP, getHeight() + 2 * NoteCanvas.LINE_GAP);
			Border bd = BorderFactory.createLineBorder(Color.blue, 2);
			selector.setBorder(bd);
			selector.setLocation(getX() - NoteCanvas.LINE_GAP/2, getY() - NoteCanvas.LINE_GAP);
			Measure fmeasure = measureList.get(0);
			((JLayeredPane)fmeasure.getParent()).add(selector, JLayeredPane.DRAG_LAYER);
		}
	}
	
	/**
	 * 删除被选择框
	 */
	public void removeSelector(){
		if(selector != null){
			Measure fmeasure = measureList.get(0);
			Page page = (Page)fmeasure.getParent();
			page.remove(selector);
			page.revalidate();
			page.updateUI();
			((JComponent)(page.getParent().getParent())).updateUI();
			selector = null;
		}
	}

	public void beSelected() {
		// TODO Auto-generated method stub
		if(selector == null){
			generateSelector();
			for(Measure measure : measureList){
				for(int v = 0, vn = measure.getVoiceNum(); v < vn; v++){
					for(int i = 0, n = measure.getNoteNum(v+1); i < n; i++){
						measure.getNote(i, v).beSelected();
					}
				}
			}
		}
	}

	public void cancleSelected() {
		// TODO Auto-generated method stub
		removeSelector();
		for(Measure measure : measureList){
			for(int v = 0, vn = measure.getVoiceNum(); v < vn; v++){
				for(int i = 0, n = measure.getNoteNum(v+1); i < n; i++){
					measure.getNote(i, v).cancleSelected();
				}
			}
		}
	}
	
	/**
	 * 返回小节在小节组中处于第几个乐器.
	 * 如果该小节组不包含该小节，则返回-1
	 * @param measure
	 * @return
	 */
	public int getInstrumentIndex(Measure measure){
		int result = -1;
		for(int i = 0, n = measureList.size(); i < n; i++){
			Measure mea = measureList.get(i);
			if(mea.getInstrument() != null)
				result++;
			if(mea == measure)
				return result;
		}
		return -1;
	}
	
	/**
	 * 返回指定乐器的第一个声部在乐谱中的声部位置
	 * 如果指定的乐器超出了乐谱的乐器个数，则返回-1.
	 * @param instrIndex 乐器序号
	 * @return
	 */
	public int getMeaIndxByInstrIndx(int instrIndex){
		int instr = -1;
		for(int i = 0, n = measureList.size(); i < n; i++){
			Measure measure = measureList.get(i);
			if(measure.getInstrument() != null)
				instr++;
			if(instr == instrIndex)
				return i;
		}
		return -1;
	}
	
	/**
	 * 返回小节序列
	 * @return
	 */
	public ArrayList<Measure> getMeasureList(){
		return measureList;
	}

	@Override
	public boolean equalsWith(Object o) {
		// TODO Auto-generated method stub
		if(!(o instanceof MeasurePart))
			return false;
		MeasurePart meaPart = (MeasurePart)o;
		if(meaPart.getMeasureNum() != measureList.size())
			return false;
		int num = measureList.size();
		Measure[] list1 = new Measure[num];
		Measure[] list2 = new Measure[num];
		return MusicMath.equals(measureList.toArray(list1), meaPart.getMeasureList().toArray(list2));
	}

	public void addRepeatSymbol(RepeatSymbol rs) {
		// TODO Auto-generated method stub
		repeatSymbol.add(rs);
		rs.setMeasure(this);
	}

	public void removeRepeatSymbol(RepeatSymbol rs) {
		// TODO Auto-generated method stub
		repeatSymbol.remove(rs);
		rs.setMeasurePart(null);
	}

//	public void addRepeatEnding(RepeatEnding rl) {
//		// TODO Auto-generated method stub
//		RepeatEnding = rl;
//		rl.setMeasurePart(this);
//	}




	
	/**
	 * 放置小节组的各种符号
	 */
	public void locateSymbols() {
		// TODO Auto-generated method stub
//		if(repeatEnding != null){
//			RepeatEnding.adjustSize();
//			RepeatEnding.setLocation(getX(), getY() - RepeatEnding.getHeight());
//		}
//		if((RepeatEnding)repeatLine != null){
//			System.out.println("yes,has repeat line");
//			repeatLine.setLocation(getX(), getY() - repeatLine.getHeight());
//			repeatLine.setLocation(200, 50);
//		}
		if(repeatSymbol.size() != 0){
			for(int i = 0, n = repeatSymbol.size(); i < n; i++){
				NoteSymbol nsl = repeatSymbol.get(i);
				int dragX = nsl.getDraggedX();
				int dragY = nsl.getDraggedY();
				nsl.setLocation(getX()+dragX, getY()-nsl.getHeight()+dragY);
			}
		}
	}

	/**
	 * 获得重复线条记号
	 * @return
	 */
	public ArrayList<RepeatLine> getRepeatLines() {
		return repeatLines;
	}

	public void setRepeatLines(ArrayList<RepeatLine> repeatLines) {
		this.repeatLines = repeatLines;
	}

	/**
	 * 获得反复记号
	 * @return
	 */
	public ArrayList<NoteSymbol> getRepeatSymbol() {
		return repeatSymbol;
	}

}
