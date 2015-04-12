package sjy.elwg.utility;

/**
 * 绘制某页时的策略类,包含策略实体参数
 * @author jingyuan.sun
 *
 */
public class PageStrategy {
	/**
	 * 逐行绘制所采用的行策略
	 */
	private NoteLineStrategy nls;
	/**
	 * 该页绘制完后若剩余空白，是否允许吸收下一页的部分元素
	 */
	private boolean isDrawBack;
	
	/**
	 * 构造函数
	 * @param nls
	 * @param isDrawBack
	 */
	public PageStrategy(NoteLineStrategy nls, boolean isDrawBack){
		this.nls = nls;
		this.isDrawBack = isDrawBack;
	}
	
	/**
	 * 构造函数
	 * @param ps
	 */
	public PageStrategy(PageStrategy ps){
		this.nls = ps.getNls();
		this.isDrawBack = ps.isDrawBack();
	}

	/**
	 * 获得行策略
	 * @return
	 */
	public NoteLineStrategy getNls() {
		return nls;
	}

	/**
	 * 设置行策略
	 * @param nls
	 */
	public void setNls(NoteLineStrategy nls) {
		this.nls = nls;
	}

	/**
	 * 获得是否允许吸收
	 * @return
	 */
	public boolean isDrawBack() {
		return isDrawBack;
	}

	/**
	 * 设置允许吸收
	 * @param isDrawBack
	 */
	public void setDrawBack(boolean isDrawBack) {
		this.isDrawBack = isDrawBack;
	}

}
