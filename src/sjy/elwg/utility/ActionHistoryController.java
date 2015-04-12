package sjy.elwg.utility;

import sjy.elwg.notation.action.Action;

/**
 * 用户的操作历史管理器
 * 用于撤销/回撤操作
 * 
 * @author jingyuan.sun
 *
 */
public class ActionHistoryController {
	
	private LmtStack<Action> undoStack = new LmtStack<Action>();
	
	private LmtStack<Action> redoStack = new LmtStack<Action>();
	
	private volatile static ActionHistoryController controller;
	
	public static ActionHistoryController getInstance(){
		if(controller == null){
			synchronized(ActionHistoryController.class){
				if(controller == null){
					controller = new ActionHistoryController();
				}
			}
		}
		return controller;
	}
	
	/**
	 * 将操作压栈
	 * @param action
	 */
	public void pushAction(Action action){
		undoStack.push(action);
	}
	
	/**
	 * 撤销
	 */
	public void undo(){
		Action a = undoStack.poll();
		if(a != null){
			redoStack.push(a);
			a.undo();
		}
	}
	
	/**
	 * 回撤
	 */
	public void redo(){
		Action a = redoStack.poll();
		if(a != null){
			undoStack.push(a);
			a.redo();
		}
	}

}
