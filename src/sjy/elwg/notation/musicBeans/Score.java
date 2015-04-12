package sjy.elwg.notation.musicBeans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sjy.elwg.utility.MusicMath;

/**
 * 乐谱
 * @author sjy
 *
 */
public class Score implements Equalable{
	
	/**
	 * 乐谱模式，分别为普通模式与无限制模式。
	 * 在无限制模式下，小节不具有拍号的概念，即小节内可以盛放任意个音符
	 */
	public static int SCORE_NORMAL = 0;
	public static int SCORE_UNLMTED = 1;
	
	
	/**
	 * 页面集合
	 */
	private ArrayList<Page> pageList;
	
	/**
	 * 被加亮的对象.
	 * 该Map的key值是ID，value值是一个list，其中保存了与当前ID所对应的可选择对象
	 * 该实例变量通常是用来静态调用的，即当乐谱不可编辑时调用。当乐谱可编辑时，可
	 * 能会修改该Map中对象，从而达不到预期结果
	 */
	private HashMap<Integer, ArrayList<Selectable>> highlightedObjts;
	
	/**
	 * 乐谱类型
	 */
	private int scoreType = SCORE_NORMAL;
	
	/**
	 * 乐谱是否含有标题与作者
	 */
	private boolean hasTitle = false;
	
	/**
	 * 乐谱中所添加的自由文本
	 */
	private List<FreeAddedText> addedTexts = new ArrayList<FreeAddedText>();
	
	
	/**
	 * 构造函数
	 */
	public Score(){
		pageList = new ArrayList<Page>();
		highlightedObjts = new HashMap<Integer, ArrayList<Selectable>>();
	}

	/**
	 * 获得页面
	 * @return
	 */
	public ArrayList<Page> getPageList() {
		return pageList;
	}

	/**
	 * 获得高亮对象Map
	 * @return
	 */
	public HashMap<Integer, ArrayList<Selectable>> getHighlightedObjts() {
		return highlightedObjts;
	}

    /**
	 * 返回乐谱类型
	 * @return
	 */
	public int getScoreType() {
		return scoreType;
	}
	
    /**
	 * 设置乐谱类型
	 * @param mode
	 */
	public void setScoreType(int scoreType) {
		this.scoreType = scoreType;
	}

	/**
	 * 是否含有标题
	 * @return
	 */
	public boolean isHasTitle() {
		return hasTitle;
	}

	/**
	 * 设置是否含有标题
	 * @param hasTitle
	 */
	public void setHasTitle(boolean hasTitle) {
		this.hasTitle = hasTitle;
	}
	
	/**
	 * 将乐谱转化为一个小节组序列
	 * @return
	 */
	public ArrayList<MeasurePart> toMeaPartList(){
		ArrayList<MeasurePart> list = new ArrayList<MeasurePart>();
		for(int i = 0; i < pageList.size(); i++){
			Page page = pageList.get(i);
			for(int j = 0; j < page.getNoteLines().size(); j++){
				NoteLine line = page.getNoteLines().get(j);
				for(int k = 0; k < line.getMeaPartList().size(); k++){
					MeasurePart part = line.getMeaPartList().get(k);
					list.add(part);
				}
			}
		}
		return list;
	}

	@Override
	public boolean equalsWith(Object o) {
		// TODO Auto-generated method stub
		if(!(o instanceof Score)){
			return false;
		}
		Score score = (Score)o;
		ArrayList<MeasurePart> parts1 = score.toMeaPartList();
		ArrayList<MeasurePart> parts2 = toMeaPartList();
		if(parts1.size() != parts2.size()){
			System.out.println("score数目不一致");
			return false;
		}
		int num = parts1.size();
		MeasurePart[] list1 = new MeasurePart[num];
		MeasurePart[] list2 = new MeasurePart[num];
		
		//打印匹配过程
		for(int i = 0; i < parts1.size(); i++){
			MeasurePart part = parts1.get(i);
			for(int j = 0; j < part.getMeasureNum(); j++){
				Measure measure = part.getMeasure(j);
				for(int v = 0, vn = measure.getVoiceNum(); v < vn; v++){
					for(int k = 0; k < measure.getNoteNum(v); k++){
						AbstractNote anote = measure.getNote(k, v);
						if(!(anote instanceof Note))
							continue;
						Note note = (Note)anote;
						System.out.print(note.getPitch() + " ");
					}
				}
			}
		}
		for(int i = 0; i < parts2.size(); i++){
			MeasurePart part = parts1.get(i);
			for(int j = 0; j < part.getMeasureNum(); j++){
				Measure measure = part.getMeasure(j);
				for(int v = 0, vn = measure.getVoiceNum(); v < vn; v++){
					for(int k = 0; k < measure.getNoteNum(v); k++){
						AbstractNote anote = measure.getNote(k, v);
						if(!(anote instanceof Note))
							continue;
						Note note = (Note)anote;
						System.out.print(note.getPitch() + " ");
					}
				}
			}
		}
		
		return MusicMath.equals(parts1.toArray(list1), parts2.toArray(list2));
	}

	public List<FreeAddedText> getAddedTexts() {
		return addedTexts;
	}
	
	public void addFreeText(FreeAddedText txt){
		addedTexts.add(txt);
	}
	
	public void removeFreeText(FreeAddedText txt){
		addedTexts.remove(txt);
	}
}
