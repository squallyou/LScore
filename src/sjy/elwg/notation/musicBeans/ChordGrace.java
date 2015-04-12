package sjy.elwg.notation.musicBeans;

public class ChordGrace extends ChordNote{

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 26979727663461315L;
	
	/**
	 * 是否具有斜线，即是否是短倚音
	 */
	private boolean hasSlash;
	
	/**
	 * 倚音所属的音符
	 */
	private AbstractNote note;
	
	/**
	 * 构造函数
	 * @param note
	 */
	public ChordGrace(Note note, boolean hasSlash) {
		super(note);
		this.hasSlash = hasSlash;
		// TODO Auto-generated constructor stub
	}

	/**
	 * 是否是短倚音
	 * @return
	 */
	public boolean isHasSlash() {
		return hasSlash;
	}

	/**
	 * 设置短倚音
	 * @param hasSlash
	 */
	public void setHasSlash(boolean hasSlash) {
		this.hasSlash = hasSlash;
	}

	/**
	 * 获得所属音符
	 * @return
	 */
	public AbstractNote getNote() {
		return note;
	}

	/**
	 * 设置所属音符
	 * @param note
	 */
	public void setNote(AbstractNote note) {
		this.note = note;
	}

}
