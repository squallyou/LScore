package sjy.elwg.notation.musicBeans;

/**
 * 倚音接口（常量接口）
 * @author sjy
 *
 */
public interface Gracable {
	
	/**
	 * 类型常量
	 * 任何实现该接口的类均可以使用以下字段用来表明自身的大小类型.
	 * GRACE类型通常比NORMAL型小一些
	 */
	public static int GRACE = 111;
	public static int NORMAL = 112;
	
	/**
	 * 倚音的长宽常量
	 */
	public static int GRACE_WIDTH = 8;
	public static int GRACE_HEIGHT = 7;
	
	/**
	 * 颤音符号大小
	 */
	public static int TREMOLO_WIDTH = 8;
	public static int TREMOLO1_HEIGHT = 6;
	public static int TREMOLO2_HEIGHT = 13;
	public static int TREMOLO3_HEIGHT = 15;

}
