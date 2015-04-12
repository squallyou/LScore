package sjy.elwg.notation.action;

import sjy.elwg.utility.Controller;

/**
 * 所有对于乐谱的操作的父类
 * 严格来讲，并不是所有操作，而是需要提供撤销/回撤功能的操作
 * @author jingyuan.sun
 *
 */
public abstract class Action {
	
	protected Controller controller;
	
	public Action(Controller controller){
		this.controller = controller;
	}
	
	/**
	 * 撤销
	 */
	public abstract void undo();
	
	/**
	 * 回撤
	 */
	public abstract void redo();

}
