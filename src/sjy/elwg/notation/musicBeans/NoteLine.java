package sjy.elwg.notation.musicBeans;

import java.awt.Container;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.EventListener;

import javax.swing.JPanel;

import sjy.elwg.notation.NoteCanvas;
import sjy.elwg.notation.musicBeans.symbolLines.Bracket;
/**
 * 行
 * @author jingyuan.sun
 *
 */
public class NoteLine {
	
	/**
	 * 两个谱表之间的默认距离
	 */
	public static final int MEASURE_GAP = 60;
	
	/**
	 * 与下一行之间的默认距离
	 */
	public static final int NOTELINE_GAP = 110;
	
	/**
	 * 最大谱表个数，为10
	 */
	public static final int MAX_STAVES = 10;
	
	/**
	 * 小节组
	 */
	private ArrayList<MeasurePart> meaPartList;
	/**
	 * 行开头的竖线
	 */
	private Barline frontBarline;
	/**
	 * 行内声部括号集合.个数与声部相同，没有括号的声部为null.
	 */
	private ArrayList<Bracket> brackets;
	
	private Page page;
	
	private int x;
	
	private int y;
	
	/**
	 * 该行各谱表的水平标识符, 数量与谱表个数，即小节组小节个数相等
	 */
	private ArrayList<LineMarker> markers;
	
	/**
	 * 该行各谱表间距.
	 */
	private ArrayList<Integer> measureGaps;
	
	/**
	 * 与下一行之间的间距
	 */
	private int lineGap = NOTELINE_GAP;
	
	/**
	 * 构造函数
	 */
	public NoteLine(){
		meaPartList = new ArrayList<MeasurePart>();
		markers = new ArrayList<LineMarker>();
		measureGaps = new ArrayList<Integer>();
		for(int i = 0; i < MAX_STAVES; i++){
			measureGaps.add(MEASURE_GAP);
		}
		brackets = new ArrayList<Bracket>();
		for(int i = 0; i < MAX_STAVES; i++){
			brackets.add(null);
		}
	}
	
	/**
	 * 放置水平标识符
	 */
	public void locateMarkers(){
		int yy = y;
		for(int i = 0, n = markers.size(); i < n; i++){
			LineMarker marker = markers.get(i);
			marker.setLocation(0, yy);
			yy += measureGaps.get(i) + Measure.MEASURE_HEIGHT;
		}
	}
	
	/**
	 * 产生水平标识符
	 * 标识符的数目与行中声部数目相同，即行中任一个小节组的小节数目相同
	 * 因此产生标识符的前提是行内不能为空.
	 */
	public void generateMarkers(){
		NoteCanvas canvas = (NoteCanvas)page.getParent();
		MeasurePart firstPart = meaPartList.get(0);
		int meaNum = firstPart.getMeasureNum();
		if(markers.size() < meaNum){
			for(int i = 0, n = meaNum - markers.size(); i < n; i++){
				LineMarker marker = new LineMarker();
				markers.add(marker);
				marker.setLine(this);
				page.add(marker);
				marker.addMouseListener(canvas);
				marker.addMouseMotionListener(canvas);
			}
		}
	}
	
	/**
	 * 为该行所有的行标识符添加鼠标侦听时间
	 * @param listener
	 */
	public void addMarkerListener(EventListener listener){
		for(int i = 0, n = markers.size(); i < n; i++){
			LineMarker marker = markers.get(i);
			MouseListener lsn = (MouseListener)listener;
			marker.addMouseListener(lsn);
			MouseMotionListener mlsn = (MouseMotionListener)listener;
			marker.addMouseMotionListener(mlsn);
		}
	}
	
	/**
	 * 删除水平标识符
	 */
	public void deleteMarkers(){
		if(markers.size() == 0)
			return;
		LineMarker firstMarker = markers.get(0);
		Container container = firstMarker.getParent();
		for(int i = 0, n = markers.size(); i < n; i++){
			LineMarker marker = markers.get(i);
			container.remove(marker);
		}
		JPanel panel = (JPanel)firstMarker;
		panel.revalidate();
		panel.updateUI();
		markers.clear();
	}

	/**
	 * 获得小节组集合
	 * @return
	 */
	public ArrayList<MeasurePart> getMeaPartList() {
		return meaPartList;
	}

	/**
	 * 获得所在的页
	 * @return
	 */
	public Page getPage() {
		return page;
	}

	/**
	 * 设置页
	 * @param page
	 */
	public void setPage(Page page) {
		this.page = page;
	}

	/**
	 * 获得x坐标
	 * @return
	 */
	public int getX() {
		return x;
	}

	/**
	 * 设置x坐标
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * 获得y坐标
	 * @return
	 */
	public int getY() {
		return y;
	}

	/**
	 * 设置y坐标
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * 获得该行与下一行之间的间隔
	 * @return
	 */
	public int getLineGap() {
		return lineGap;
	}

	/**
	 * 设置该行与下一行的行间隔
	 * @param lineGap
	 */
	public void setLineGap(int lineGap) {
		this.lineGap = lineGap;
	}

	/**
	 * 获得水平标识符
	 * @return
	 */
	public ArrayList<LineMarker> getMarkers() {
		return markers;
	}

	/**
	 * 行内谱表间隔数组
	 * @return
	 */
	public ArrayList<Integer> getMeasureGaps() {
		return measureGaps;
	}

	/**
	 * 获得括号集合
	 * @return
	 */
	public ArrayList<Bracket> getBrackets() {
		return brackets;
	}

	/**
	 * 设定在页中的位置
	 */
	public void determineLocation(){
		int lineIndex = page.getNoteLines().indexOf(this);
		if(lineIndex == 0){
			setX(NoteCanvas.xStart);
			setY(page.getNoteLineYStart());
		}else{
			NoteLine preLine = page.getNoteLines().get(lineIndex - 1);
			setX(NoteCanvas.xStart);
			setY(preLine.getY() + preLine.getHeight() + preLine.getLineGap());
		}
	}
	
	/**
	 * 返回行所占的高度
	 * 注意：该高度指从该行第一行谱表顶端到最后一行谱表底端的高度，不包括该行结束之后与下一行之间的空白
	 * @return
	 */
	public int getHeight(){
		int result = 0;
		int meaNum = meaPartList.get(0).getMeasureNum();
		for(int i = 0; i < meaNum - 1; i++){
			result += Measure.MEASURE_HEIGHT + measureGaps.get(i);
		}
		result += Measure.MEASURE_HEIGHT;
		return result;
	}
	
	/**
	 * 返回该行的最小高度.即采用默认谱表间距的行高
	 * @return
	 */
	public int getMinHeight(){
		int meaNum = meaPartList.get(0).getMeasureNum();
		return meaNum * Measure.MEASURE_HEIGHT + (meaNum-1) * MEASURE_GAP;
	}
	
	/**
	 * 返回该行所有小节组时间槽的数目总和
	 * @return
	 */
	public int getTimeSlotNum(){
		int result = 0;
		for(MeasurePart meaPart : meaPartList){
			int num = meaPart.getNoteListByTimeSlot().size();
			result += num;
		}
		return result;
	}
	
	/**
	 * 产生行起始竖线
	 */
	public void generateFrontBarlineLine(){
		if(frontBarline == null){
			frontBarline = new Barline("regular");
		}
		
		if(frontBarline.getParent() != page){
			page.add(frontBarline);
		}
	}
	
	/**
	 * 放置行起始线
	 */
	public void locateFrontBarline(){
		
		int height = 0;
		int meaNum = meaPartList.get(0).getMeasureNum();
		
		if(frontBarline != null){
			for(int i = 0, n = meaNum; i < n; i++){
				height += measureGaps.get(i) + Measure.MEASURE_HEIGHT;
			}
			height -= measureGaps.get(meaNum - 1);
			frontBarline.setSize(2, height);
			frontBarline.setLocation(x, y);
		}
		
	}
	
	/**
	 * 返回乐器个数
	 * @return
	 */
	public int getPartNum(){
		MeasurePart fmeaPart = meaPartList.get(0);
		int result = 0;
		for(int i = 0; i < fmeaPart.getMeasureNum(); i++){
			Measure measure = fmeaPart.getMeasure(i);
			if(measure.getInstrument() != null)
				result++;
		}
		return result;
	}
	
	/**
	 * 乐器的声部个数（即谱表个数）
	 * @param partIndex
	 * @return
	 */
	public int getStavesInPart(int partIndex){
		if(partIndex >= getPartNum()){
			return 0;
		}
		MeasurePart fmeaPart = meaPartList.get(0);
		int num = 0;
		int result = 1;
		for(int i = 0; i < fmeaPart.getMeasureNum(); i++){
			Measure measure = fmeaPart.getMeasure(i);
			if(measure.getInstrument() != null)
				num++;
			else if(num == partIndex+1){
				result++;
			}
		}
		return result;
	}
	
	/**
	 * 返回该行中某个谱表的歌词段落的最大值,即某个音符所具有的最多歌词数量.
	 * @param meaIndex 谱表ID
	 * @return
	 */
	public int getMaxLyricNum(int meaIndex){
		int max = 0;
		for(int i = 0, n = meaPartList.size(); i < n; i++){
			MeasurePart meaPart = meaPartList.get(i);
			Measure measure = meaPart.getMeasure(meaIndex);
			for(int v = 0, vn = measure.getVoiceNum(); v < vn; v++){
				for(int j = 0, jn = measure.getNoteNum(v); j < jn; j++){
					AbstractNote anote = measure.getNote(j, v);
					if(anote.getLyricsNum() > max)
						max = anote.getLyricsNum();
				}
			}
		}
		return max;
	}
	
	/**
	 * 指定谱表ID，调整行间隔
	 * @param meaIndex 表示一行的第几个谱子
	 * @param flag 是否当行间距比较大时，是否允许自动收缩
	 */
	public void adjustGap(int meaIndex, boolean flag){
		int meaNum = meaPartList.get(0).getMeasureNum();
		int maxLyric = getMaxLyricNum(meaIndex);
		int deltay = maxLyric * (Lyrics.LYRIC_FONT.getSize() + 3);
		if(meaIndex == meaNum - 1){
			lineGap = lineGap < deltay + NoteCanvas.LINE_GAP ? deltay + NoteCanvas.LINE_GAP : lineGap;
		}else{
			int formerGap = measureGaps.get(meaIndex);
			int newGap;
			if(!flag)
				newGap = formerGap >= MEASURE_GAP + deltay ? formerGap : MEASURE_GAP + deltay;
			else
				newGap = MEASURE_GAP + deltay;
			measureGaps.set(meaIndex, newGap);
		}
	}
	
	/**
	 * 对每个谱表都调整间隔
	 * @param flag 是否当行间距比较大时，是否允许自动收缩
	 */
	public void adjustGap(boolean flag){
		int meaNum = meaPartList.get(0).getMeasureNum();
		
		for(int i = 0; i < meaNum; i++){
			adjustGap(i, flag);
		}
	}
	
	/**
	 * 放置括号
	 */
	public void locateBrackets(){
		MeasurePart fmeaPart = meaPartList.get(0);
		
		int instrIndex = -1;
		for(int i = 0, n = fmeaPart.getMeasureNum(); i < n; i++){
			Measure measure = fmeaPart.getMeasure(i);
			if(measure.getInstrument() != null){
				instrIndex++;
				Bracket bk = brackets.get(instrIndex);
				if(bk != null){
					int num = getStavesInPart(instrIndex);
					int hheight = 0;
					for(int j = 0; j < num - 1; j++){
						hheight += Measure.MEASURE_HEIGHT + measureGaps.get(i + j);
					}
					hheight += Measure.MEASURE_HEIGHT;
					bk.setSize(bk.getWidth(), hheight);
					bk.setLocation(x - bk.getWidth(), measure.getY());
				}
			}
		}
	}
	
	/**
	 * 生成括号
	 */
	public void generateBrackets(){
		deleteBrackets();
		int partNum = getPartNum();
		for(int i = 0; i < partNum; i++){
			int staffNum = getStavesInPart(i);
			if(staffNum > 1){
				Bracket bk = new Bracket();
				getBrackets().add(bk);
				getPage().add(bk);
			}else{
				getBrackets().add(null);
			}
		}
	}
	
	/**
	 * 删除括号
	 */
	public void deleteBrackets(){
		if(getBrackets().size() != 0){
			for(Bracket bk : getBrackets()){
				if(bk != null)
					getPage().remove(bk);
			}
			getBrackets().clear();
		}
	}
	
	/**
	 * 删除行前线
	 */
	public void deleteFrontLine(){
		if(frontBarline != null){
			getPage().remove(frontBarline);
		}
	}

	public Barline getFrontBarline() {
		return frontBarline;
	}

}
