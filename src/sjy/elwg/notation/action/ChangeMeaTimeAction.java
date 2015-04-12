package sjy.elwg.notation.action;

import java.util.ArrayList;
import java.util.List;

import sjy.elwg.notation.musicBeans.AbstractNote;
import sjy.elwg.notation.musicBeans.Measure;
import sjy.elwg.notation.musicBeans.NoteLine;
import sjy.elwg.notation.musicBeans.Page;
import sjy.elwg.notation.musicBeans.Time;
import sjy.elwg.utility.Controller;

/**
 * 改变单个小节组的拍号操作
 * @author jingyuan.sun
 *
 */
public class ChangeMeaTimeAction extends Action{
	
	private Measure measure;
	
	private Time oldTime;
	
	private Time newTime;
	
	private List<List<AbstractNote>> preVoiceList = new ArrayList<List<AbstractNote>>();
	
	private List<List<AbstractNote>> curVoiceList = new ArrayList<List<AbstractNote>>();

	public ChangeMeaTimeAction(Controller controller) {
		super(controller);
	}
	
	public ChangeMeaTimeAction(Controller controller, Measure measure, Time oldTime, Time newTime){
		super(controller);
		this.measure = measure;
		this.oldTime = oldTime;
		this.newTime = newTime;
	}

	public void setPreVoiceList(List<List<AbstractNote>> preVoiceList) {
		this.preVoiceList = preVoiceList;
	}

	public void setCurVoiceList(List<List<AbstractNote>> curVoiceList) {
		this.curVoiceList = curVoiceList;
	}

	@Override
	public void redo() {
		// TODO Auto-generated method stub
		NoteLine line = measure.getMeasurePart().getNoteLine();
		Page page = line.getPage();
		for(int v = 0; v < preVoiceList.size(); v++){
			List<AbstractNote> preList = preVoiceList.get(v);
			List<AbstractNote> curList = curVoiceList.get(v);
			for(int i = 0; i < preList.size(); i++){
				AbstractNote note = preList.get(i);
				page.deleteNote(note, true);
				measure.removeNote(note);
				//note.removeAllSymbols(true);
				System.out.println("SL of note: " + note.getSymbolLines().size());
			}
			for(int i = 0; i < curList.size(); i++){
				AbstractNote note = curList.get(i);
				//page.add(note);
				page.addNoteAndSymbols(note, false);
				measure.addNote(note, v);
			}
		}
		measure.setTime(newTime);
	}

	@Override
	public void undo() {
		// TODO Auto-generated method stub
		NoteLine line = measure.getMeasurePart().getNoteLine();
		Page page = line.getPage();
		for(int v = 0; v < preVoiceList.size(); v++){
			List<AbstractNote> preList = preVoiceList.get(v);
			List<AbstractNote> curList = curVoiceList.get(v);
			for(int i = 0; i < curList.size(); i++){
				AbstractNote note = curList.get(i);
				page.deleteNote(note, true);
				measure.removeNote(note);
				//note.removeAllSymbols(true);
			}
			for(int i = 0; i < preList.size(); i++){
				AbstractNote note = preList.get(i);
				//page.add(note);
				page.addNoteAndSymbols(note, false);
				System.out.println("SL of note: " + note.getSymbolLines().size());
				measure.addNote(note, v);
			}
		}
		measure.setTime(oldTime);
	}

	public Measure getMeasure() {
		return measure;
	}

	public void setMeasure(Measure measure) {
		this.measure = measure;
	}

	public List<List<AbstractNote>> getPreVoiceList() {
		return preVoiceList;
	}

	public List<List<AbstractNote>> getCurVoiceList() {
		return curVoiceList;
	}

	public Time getOldTime() {
		return oldTime;
	}

	public void setOldTime(Time oldTime) {
		this.oldTime = oldTime;
	}

	public Time getNewTime() {
		return newTime;
	}

	public void setNewTime(Time newTime) {
		this.newTime = newTime;
	}
	
	public void addPreNoteList(List<AbstractNote> list){
		preVoiceList.add(list);
	}
	
	public void addCurNoteList(List<AbstractNote> list){
		curVoiceList.add(list);
	}

}
