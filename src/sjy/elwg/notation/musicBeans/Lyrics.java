package sjy.elwg.notation.musicBeans;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.event.DocumentListener;


/**
 * 歌词
 * @author jingyuan.sun
 *
 */
public class Lyrics extends FreeTextField implements Editable, DocumentListener,MouseMotionListener,MouseListener,Selectable{
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -6275980176199532122L;
	
	/**
	 * 歌词所属的音符
	 */
	private AbstractNote note;
	
	/**
	 * 构造函数
	 *
	 */
	public Lyrics(){
		super();
	}
	
	/**
	 * 构造函数
	 * @param str
	 */
	public Lyrics(String str){
		super(str);
	}
	
	/**
	 * 根据文字输入
	 */
	public void reSize(){	
		if(getText() == null || getText().equalsIgnoreCase("")){
			setSize(Note.NORMAL_HEAD_WIDTH/2, LYRIC_FONT.getSize() + 4);
		}
		else{
			int width = getFontMetrics(getFont()).stringWidth(getText())+5;
			int height = getFont().getSize() + 4;
			setSize(width , height);
		}
	}

	/**
	 * 返回歌词所在的音符 
	 * @return
	 */
	public AbstractNote getNote() {
		return note;
	}

	/**
	 * 设置音符
	 * @param note
	 */
	public void setNote(AbstractNote note) {
		this.note = note;
	}

}
