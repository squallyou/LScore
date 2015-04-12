package sjy.elwg.notation.action;

import sjy.elwg.notation.NoteCanvas;
import sjy.elwg.notation.musicBeans.AbstractNote;
import sjy.elwg.notation.musicBeans.ChordNote;
import sjy.elwg.notation.musicBeans.Measure;
import sjy.elwg.notation.musicBeans.Note;
import sjy.elwg.notation.musicBeans.NoteLine;
import sjy.elwg.utility.Controller;

/**
 * 增加和弦的操作.操作不影响其他时间戳的音符
 * @author jingyuan.sun
 *
 */
public class AddChordAction extends Action{
	
	private AbstractNote preNote; //操作之前的 音符
	
	private AbstractNote curNote; //操作之后的音符
	
	private Note note; //添加的音符

	public AddChordAction(Controller controller) {
		super(controller);
	}

	@Override
	public void redo() {
		// TODO Auto-generated method stub
		Measure measure = preNote.getMeasure();
		if(measure == null)
			return;
		NoteLine line = measure.getMeasurePart().getNoteLine();
		int noteIndex = measure.noteIndex(preNote);
		int voice = preNote.getVoice();
		curNote = controller.addChord(preNote, note, noteIndex, voice);
		NoteCanvas canvas = controller.getCanvas();
		canvas.redrawLine(line);
	}

	@Override
	public void undo() {
		// TODO Auto-generated method stub
		NoteCanvas canvas = controller.getCanvas();
		NoteLine line = curNote.getMeasure().getMeasurePart().getNoteLine();
		if(curNote instanceof ChordNote){
			ChordNote cnote = (ChordNote)curNote;
			controller.removeChord(cnote, note);
		}
		canvas.redrawLine(line);
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
