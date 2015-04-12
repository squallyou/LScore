package sjy.elwg.notation.action;

import sjy.elwg.notation.musicBeans.Measure;
import sjy.elwg.notation.musicBeans.MeasurePart;
import sjy.elwg.notation.musicBeans.NoteLine;
import sjy.elwg.notation.musicBeans.Page;
import sjy.elwg.utility.Controller;
import sjy.elwg.utility.MusicMath;
import sjy.elwg.notation.NoteCanvas;

/**
 * 改变调号操作
 * @author jingyuan.sun
 *
 */
public class ChangeMeaKeyAction extends Action{

	private int oldKey; //老调号

	private int newKey; //新调号

	private MeasurePart measurePart; //改变的起始小节组

	private int meaIndex; //小节在小节组里边的index

	public ChangeMeaKeyAction(Controller controller){
		super(controller);
	}

	public ChangeMeaKeyAction(int oldKey, int newKey, MeasurePart measurePart, int meaIndex, Controller controller){
		super(controller);
		this.oldKey = oldKey;
		this.newKey = newKey;
		this.meaIndex = meaIndex;
		this.measurePart = measurePart;
	}

	public void redo(){
		controller.changeKey(measurePart, meaIndex, newKey);
		//重绘
		NoteCanvas canvas = controller.getCanvas();
		NoteLine line = measurePart.getNoteLine();
		while(line != null){
			canvas.redrawLine(line);
			line = MusicMath.nxtLine(line);
		}
	}

	public void undo(){
		controller.changeKey(measurePart, meaIndex, oldKey);
		//重绘
		NoteCanvas canvas = controller.getCanvas();
		NoteLine line = measurePart.getNoteLine();
		while(line != null){
			canvas.redrawLine(line);
			line = MusicMath.nxtLine(line);
		}
	}

}
