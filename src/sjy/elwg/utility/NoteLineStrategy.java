package sjy.elwg.utility;

/**
 * 绘制某行时的实体策略类，包含绘制时的参数
 * @author jingyuan.sun
 *
 */
public class NoteLineStrategy {
	
	/**
	 * 小节组绘制策略
	 */
	private MeaPartStrategy mpst;
	/**
	 * 当绘制的行过于稀疏时，是否允许吸收下一行的部分小节组
	 */
	private boolean isDrawBack;
	
	/**
	 * 构造函数
	 * @param mpst 小节组绘制策略
	 * @param isDrawBack 是否允许吸收下一行
	 */
	public NoteLineStrategy(MeaPartStrategy mpst, boolean isDrawBack){
		this.mpst = mpst;
		this.isDrawBack = isDrawBack;
	}
	
	public NoteLineStrategy(NoteLineStrategy nls){
		this.mpst = new MeaPartStrategy(nls.getMpst());
		this.isDrawBack = nls.isDrawBack();
	}

	/**
	 * 获得小节组绘制策略
	 * @return
	 */
	public MeaPartStrategy getMpst() {
		return mpst;
	}

	/**
	 * 设置小节组策略
	 * @param mpst
	 */
	public void setMpst(MeaPartStrategy mpst) {
		this.mpst = mpst;
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
