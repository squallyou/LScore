package sjy.elwg.notation.musicBeans;

/**
 * 可选择接口.
 * 该接口定了对象被选择，已经被取消选择时所执行的操作。
 * 乐谱中所有可在物理上或逻辑上被选择的对象都应实现该接口.
 * @author jingyuan.sun
 *
 */
public interface Selectable {
	
	/**
	 * 当被选择时
	 */
	public void beSelected();
	
	/**
	 * 当被取消选择时
	 */
	public void cancleSelected();

}
