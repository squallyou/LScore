package sjy.elwg.notation.action;

import sjy.elwg.notation.musicBeans.Measure;
import sjy.elwg.notation.musicBeans.MeasurePart;
import sjy.elwg.notation.musicBeans.NoteLine;
import sjy.elwg.notation.musicBeans.Page;
import sjy.elwg.utility.Controller;
import sjy.elwg.utility.MusicMath;
import sjy.elwg.notation.NoteCanvas;

public class ChangeMeaClefAction extends Action{

	private String oldClef; //old clef

	private String newClef; //new clef

	private MeasurePart measurePart; //measurePart from where to change

	private int meaIndex; //index of the measure within the measurePart

	public ChangeMeaClefAction(Controller controller){
		super(controller);
	}

	public ChangeMeaClefAction(String oldClef, String newClef, MeasurePart measurePart, int meaIndex, Controller controller){
		super(controller);
		this.oldClef = oldClef;
		this.newClef = newClef;
		this.meaIndex = meaIndex;
		this.measurePart = measurePart;
	}

	public void redo(){
		controller.changeClef(measurePart, meaIndex, newClef);
		//ÖØ»æ
		NoteCanvas canvas = controller.getCanvas();
		NoteLine line = measurePart.getNoteLine();
		while(line != null){
			canvas.redrawLine(line);
			line = MusicMath.nxtLine(line);
		}
	}

	public void undo(){
		controller.changeClef(measurePart, meaIndex, oldClef);
		//ÖØ»æ
		NoteCanvas canvas = controller.getCanvas();
		NoteLine line = measurePart.getNoteLine();
		while(line != null){
			canvas.redrawLine(line);
			line = MusicMath.nxtLine(line);
		}
	}

}
