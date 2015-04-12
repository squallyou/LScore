package sjy.elwg.utility;

/**
 * 常量接口
 * 主要定义了软件窗口，页面尺寸等常量。
 * 注：不提唱使用常量接口，但是本系统暂时没有做对于窗口大小的自适应调整，为了手动调整时候的方便，
 *    这里专门放了一个常量接口便于大小调试时方便
 * @author root
 *
 */
public interface ConstantInterface {
	
	/*默认窗口宽度*/
	public static int DEFAULT_WIN_WIDTH = 1100; //1100
	
	/*默认窗口高度*/
	public static int DEFAULT_WIN_HEIGHT = 650; //650
	
	/*两个页面之间的间隔*/
	public static int PAGE_GAP = 50;
	
	/*页面音符的起始横坐标*/
	public static int xStart = 50;
	
	/*页面的起始纵坐标*/
	public static int yStart = 100;
	
	/*每行五线谱的标准宽度*/
	public static int lineWidth = 900;
	
	/*页面宽度和高度*/
	public static final int PAGE_WIDTH = 1000;
	public static final int PAGE_HEIGHT = 1500;
}
