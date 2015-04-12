package sjy.elwg.notation.action;

import java.util.ArrayList;
import java.util.List;

import sjy.elwg.notation.musicBeans.AbstractNote;
import sjy.elwg.notation.musicBeans.Measure;
import sjy.elwg.notation.musicBeans.MeasurePart;
import sjy.elwg.notation.musicBeans.NoteLine;
import sjy.elwg.notation.musicBeans.Note;
import sjy.elwg.notation.musicBeans.Page;
import sjy.elwg.notation.musicBeans.Score;
import sjy.elwg.utility.Controller;

/**
 * Note delete action. 
 * This action turns a normal note into a rest in normal mode, or just remove a note from the measure in abnormal mode.
 * @author jingyuan.sun
 * 
 */
public class DelNoteAction extends Action{

	private Note note;

	private int pitch; //pitch of the note. We store this value, 'cause it would be set to 0 after deletion.

	private Measure measure;

	public DelNoteAction(Controller controller){
		super(controller);
	}

	public DelNoteAction(Note note, Controller controller){
		super(controller);
		this.note = note;
		this.measure = note.getMeasure();
		this.pitch = note.getPitch();
	}
	
	public void redo(){
		// the full-rest of the second voice in the measure
		if(note.getVoice() > 0 && !note.isHidden()){
			note.setHidden(true);
			note.repaint();	
		}	
		//normal mode.
		else if(controller.getCanvas().getScore().getScoreType() == Score.SCORE_NORMAL){
			NoteLine line = measure.getMeasurePart().getNoteLine();
			Page page = line.getPage();
			this.pitch = note.getPitch();
			note.setPitch(0);
			note.setRest(true);
			note.repaint();
			page.deleteNote(note, false);
			page.add(note);
			controller.getCanvas().redrawLine(line);
		}
		//abnormal mode
		else{
			NoteLine line = measure.getMeasurePart().getNoteLine();
			Page page = line.getPage();
			page.deleteNote(note, false);
			measure.removeNote(note);
			controller.getCanvas().redrawLine(line);
		}
	}

	public void undo(){
		// the full-rest of the second voice in the measure
		if(note.getVoice() > 0 && !note.isHidden()){
			note.setHidden(false);
			note.repaint();	
		}	
		//normal mode.
		else if(controller.getCanvas().getScore().getScoreType() == Score.SCORE_NORMAL){
			NoteLine line = measure.getMeasurePart().getNoteLine();
			Page page = line.getPage();
			note.setPitch(pitch);
			note.setRest(false);
			note.repaint();
			page.addNoteAndSymbols(note, false);
			controller.getCanvas().redrawLine(line);
		}
		//abnormal mode
		else{
			NoteLine line = measure.getMeasurePart().getNoteLine();
			Page page = line.getPage();
			page.addNoteAndSymbols(note, false);
			measure.addNote(note, note.getVoice());
			controller.getCanvas().redrawLine(line);
		}
	}

	public void setPitch(int pitch){
		this.pitch = pitch;
	}

	public int getPitch(){
		return this.pitch;
	}

}
