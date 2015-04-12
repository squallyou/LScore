package sjy.elwg.notation.action;

import java.util.ArrayList;
import java.util.List;

import sjy.elwg.notation.musicBeans.AbstractNote;
import sjy.elwg.notation.musicBeans.Measure;
import sjy.elwg.notation.musicBeans.NoteLine;
import sjy.elwg.notation.musicBeans.Page;
import sjy.elwg.utility.Controller;

/**
 * 改变旋律操作.指涉及到音符的改变、拆分、合并的操作
 * @author jingyuan.sun
 *
 */
public class ChangeRythmAction extends Action{

	/**
	 * 操作之前的音符
	 */
	private List<AbstractNote> preNotes = new ArrayList<AbstractNote>();
	
	/**
	 * 操作之后的音符
	 */
	private List<AbstractNote> curNotes = new ArrayList<AbstractNote>();
	
	private Measure measure;
	
	private int voice;
	
	
	public ChangeRythmAction(Measure measure, int voice, Controller controller) {
		super(controller);
		this.measure = measure;
		this.voice = voice;
	}
	
	@Override
	public void redo() {
		// TODO Auto-generated method stub
		NoteLine line = measure.getMeasurePart().getNoteLine();
		Page page = line.getPage();
		int index = preNotes.isEmpty() ? 0 : measure.noteIndex(preNotes.get(0));
		if(index == -1){
			System.err.println("Redo ChangeRythm Error!");
			return;
		}
		removeNotes(preNotes);
		addNotes(curNotes, index, voice, measure, page);
		controller.getCanvas().redrawLine(line);
	}

	@Override
	public void undo() {
		// TODO Auto-generated method stub
		if(curNotes.isEmpty())
			return;
		NoteLine line = measure.getMeasurePart().getNoteLine();
		Page page = line.getPage();
		int index = measure.noteIndex(curNotes.get(0));
		if(index == -1){
			System.err.println("Undo ChangeRythm Error!");
			return;
		}
		removeNotes(curNotes);
		addNotes(preNotes, index, voice, measure, page);
		controller.getCanvas().redrawLine(line);
	}
	
	private void removeNotes(List<AbstractNote> list){
		if(list.isEmpty()){
			return;
		}
		NoteLine line = measure.getMeasurePart().getNoteLine();
		Page page = line.getPage();
		for(int i = 0; i < list.size(); i++){
			AbstractNote note = list.get(i);
			measure.removeNote(note);
			page.deleteNote(note, false);
			//note.removeAllSymbols(false);
		}
	}
	
	private void addNotes(List<AbstractNote> list, int index, int voice, Measure measure, Page page){
		if(list.isEmpty()){
			return;
		}
		for(int i = 0; i < list.size(); i++){
			AbstractNote note = list.get(i);
			measure.addNote(index++, note, voice);
			page.add(note);
		}
	}

	public List<AbstractNote> getPreNotes() {
		return preNotes;
	}

	public List<AbstractNote> getCurNotes() {
		return curNotes;
	}
	
	public void addPreNote(AbstractNote note){
		preNotes.add(note);
	}
	
	public void addCurNote(AbstractNote note){
		curNotes.add(note);
	}

}
