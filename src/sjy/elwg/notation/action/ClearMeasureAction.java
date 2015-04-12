package sjy.elwg.notation.action;

import java.util.ArrayList;
import java.util.List;

import sjy.elwg.notation.musicBeans.AbstractNote;
import sjy.elwg.notation.musicBeans.Measure;
import sjy.elwg.notation.musicBeans.NoteLine;
import sjy.elwg.notation.musicBeans.Page;
import sjy.elwg.utility.Controller;

/**
 * 清空小节操作
 * @author jingyuan.sun
 *
 */
public class ClearMeasureAction extends Action{
	
	private Measure measure;
	
	private List<List<AbstractNote>> voiceList;

	public ClearMeasureAction(Controller controller, Measure measure) {
		super(controller);
		this.measure = measure;
		voiceList = new ArrayList<List<AbstractNote>>();
		addNotes();
	}
	
	private void addNotes(){
		for(int v = 0; v < measure.getVoiceNum(); v++){
			List<AbstractNote> list = new ArrayList<AbstractNote>();
			voiceList.add(list);
			for(int i = 0; i < measure.getNoteNum(v); i++){
				list.add(measure.getNote(i, v));
			}
		}
	}

	@Override
	public void redo() {
		// TODO Auto-generated method stub
		controller.clearMeasure(measure);
	}

	@Override
	public void undo() {
		// TODO Auto-generated method stub
		Page page = (Page)measure.getParent();
		if(!measure.isUnlimited()){
			AbstractNote note = measure.getNote(0, 0);
			page.remove(note);
			measure.removeNote(0, 0);
		}
		for(int v = 0; v < voiceList.size(); v++){
			List<AbstractNote> noteList = voiceList.get(v);
			for(int i = 0; i < noteList.size(); i++){
				AbstractNote note = noteList.get(i);
				measure.addNote(note, v);
				//page.add(note);
				page.addNoteAndSymbols(note, true);
			}
		}
		NoteLine line = measure.getMeasurePart().getNoteLine();
		controller.getCanvas().redrawLine(line);
	}

	public Measure getMeasure() {
		return measure;
	}

	public void setMeasure(Measure measure) {
		this.measure = measure;
	}

}
