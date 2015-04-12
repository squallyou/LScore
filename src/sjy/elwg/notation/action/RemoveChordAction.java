package sjy.elwg.notation.action;

import sjy.elwg.notation.NoteCanvas;
import sjy.elwg.notation.musicBeans.AbstractNote;
import sjy.elwg.notation.musicBeans.ChordNote;
import sjy.elwg.notation.musicBeans.Measure;
import sjy.elwg.notation.musicBeans.Note;
import sjy.elwg.notation.musicBeans.NoteLine;
import sjy.elwg.utility.Controller;

/**
 * 删除和弦操作
 * @author jingyuan.sun
 *
 */
public class RemoveChordAction extends Action{
	
	private AbstractNote preNote; //操作之前的 音符
	
	private AbstractNote curNote; //操作之后的音符
	
	private Note note; //删除的音符

	public RemoveChordAction(Controller controller) {
		super(controller);
	}

	@Override
	public void redo() {
		// TODO Auto-generated method stub
		Measure measure = preNote.getMeasure();
		NoteLine line = measure.getMeasurePart().getNoteLine();
		if(preNote instanceof ChordNote){
			ChordNote cnote = (ChordNote)preNote;
			controller.removeChord(cnote, note);
		}
		NoteCanvas canvas = controller.getCanvas();
		canvas.redrawLine(line);
	}

	@Override
	public void undo() {
		// TODO Auto-generated method stub
		if(preNote instanceof ChordNote){
			Measure measure = curNote.getMeasure();
			if(measure == null)
				return;
			int noteIndex = measure.noteIndex(curNote);
			int voice = curNote.getVoice();
			preNote = controller.addChord(curNote, note, noteIndex, voice);
			NoteLine line = measure.getMeasurePart().getNoteLine();
			NoteCanvas canvas = controller.getCanvas();
			canvas.redrawLine(line);
		}
	}

	public AbstractNote getPreNote() {
		return preNote;
	}

	public void setPreNote(AbstractNote preNote) {
		this.preNote = preNote;
	}

	public AbstractNote getCurNote() {
		return curNote;
	}

	public void setCurNote(AbstractNote curNote) {
		this.curNote = curNote;
	}

	public Note getNote() {
		return note;
	}

	public void setNote(Note note) {
		this.note = note;
	}

}
