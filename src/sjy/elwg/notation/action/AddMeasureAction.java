package sjy.elwg.notation.action;

import sjy.elwg.notation.musicBeans.Measure;
import sjy.elwg.notation.musicBeans.MeasurePart;
import sjy.elwg.notation.musicBeans.NoteLine;
import sjy.elwg.notation.musicBeans.Note;
import sjy.elwg.notation.musicBeans.Page;
import sjy.elwg.notation.NoteCanvas;
import sjy.elwg.utility.Controller;
import sjy.elwg.utility.MusicMath;
import sjy.elwg.utility.MeaPartStrategy;
import sjy.elwg.utility.NoteLineStrategy;
import sjy.elwg.utility.PageStrategy;



/**
 * 增加小节的操作, 2012-11-26
 * @author jingyuan.sun
 */
public class AddMeasureAction extends Action{

	//在该小节后面添加小节
	private Measure measure;

	//新增小节数量
	private int number;

	public AddMeasureAction(Controller controller){
		super(controller);
	}

	public AddMeasureAction(Measure measure, int number, Controller controller){
		super(controller);
		this.measure = measure;
		this.number = number;
	}

	public void redo(){
		MeasurePart meaPart = measure.getMeasurePart();	
		NoteLine line = meaPart.getNoteLine();
		NoteCanvas canvas = controller.getCanvas();
		controller.addMeasure(meaPart, number);
		while(canvas.redrawLine(line)){
			line = MusicMath.nxtLine(line);
		}
	}

	public void undo(){
		MeasurePart measurePart = measure.getMeasurePart();	
		NoteCanvas canvas = controller.getCanvas();
		Page page = measurePart.getNoteLine().getPage();
		for(int i = 0; i < number; i++){
			MeasurePart part = MusicMath.nxtMeasurePart(measurePart);
			controller.deleteMeasure(part);		
		}
		//重绘
		MeaPartStrategy mpst = new MeaPartStrategy(NoteCanvas.X_MIN_DIST, false);
		NoteLineStrategy nls = new NoteLineStrategy(mpst, false);
		PageStrategy ps = new PageStrategy(nls, true);//允许吸收下一页
		while(canvas.redrawPage(page, ps)){
			page = MusicMath.nxtPage(canvas.getScore(), page);
		}
	}

}
