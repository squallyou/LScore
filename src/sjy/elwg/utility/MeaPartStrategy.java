package sjy.elwg.utility;

/**
 * 该类是绘制小节组时的策略实体类，包含绘制小节组的必要参数.
 * @author jingyuan.sun
 *
 */
public class MeaPartStrategy {
	
	/**
	 * 音符间的间距
	 */
	private int noteDist;
	/**
	 * 是否是临时排列.该变量主要影响重绘小节组时小节线的放置.如果是临时排列，则绘制结束时不放置小节线，否则则放置.
	 * 通常设为true.
	 */
	private boolean isTemp;
	
	/**
	 * 构造函数
	 * @param noteDist 音符间距
	 * @param isTemp 是否是临时
	 */
	public MeaPartStrategy(int noteDist, boolean isTemp){
		this.noteDist = noteDist;
		this.isTemp = isTemp;
	}
	
	public MeaPartStrategy(MeaPartStrategy mps){
		this.noteDist = mps.getNoteDist();
		this.isTemp = mps.isTemp();
	}

	/**
	 * 获得音符间距
	 * @return
	 */
	public int getNoteDist() {
		return noteDist;
	}

	/**
	 * 设置音符间距
	 * @param noteDist
	 */
	public void setNoteDist(int noteDist) {
		this.noteDist = noteDist;
	}

	/**
	 * 获得是否是临时排列
	 * @return
	 */
	public boolean isTemp() {
		return isTemp;
	}

	/**
	 * 设置是否是临时排列
	 * @param isTemp
	 */
	public void setTemp(boolean isTemp) {
		this.isTemp = isTemp;
	}

}
