package sjy.elwg.notation.musicBeans;

/**
 * 可编辑接口
 * 乐谱所有可编辑对象都实现该接口.该接口要求所实现的子类指定进入不同状态所执行的操作.
 * @author jingyuan.sun
 *
 */
public interface Editable {
	
	/**
	 * 状态常量,具有视图状态和编辑状态
	 */
	public static final int VIEW_MODE = 0;
	public static final int EDIT_MODE = 1;
	
	/**
	 * 进入编辑状态
	 */
	public void editMode();
	
	/**
	 * 进入观察状态
	 */
	public void viewMode();

}
