package sjy.elwg.notation;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 * 菜单所在的顶部面板.
 * @author jingyuan.sun
 *
 */
public class TopPanel extends JPanel{

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 12L;
	
	/**
	 * 盛放菜单按钮
	 */
	private JMenuBar menuBar;
	
	JMenu menuFile = new JMenu("文件");
	JMenu menuStaff = new JMenu("乐谱");
	JMenu menuLayout = new JMenu("页面");
	
	JMenuItem menuAutoLayout = new JMenuItem("自动布局");
	JMenuItem menuNew = new JMenuItem("新建乐谱");
	JMenuItem menuImport = new JMenuItem("导入");
	JMenu menuExport = new JMenu("导出");
	JMenuItem menuXML = new JMenuItem("MusicXML文档");
	JMenuItem menuPicture = new JMenuItem("图片");
	JMenuItem menuPrint = new JMenuItem("打印");
	
	JMenuItem addPartDown = new JMenuItem("在之前添加乐器");
	JMenuItem addPartUp = new JMenuItem("在之后添加乐器");
	JMenuItem removeStaff = new JMenuItem("删除声部");
	JMenuItem changeTime = new JMenuItem("改变拍号");
	JMenuItem changeKey = new JMenuItem("改变调号");
	JMenuItem changeClef = new JMenuItem("改变谱号");
	JMenu addTuplet = new JMenu("创建连音符");
	JMenuItem addMea = new JMenuItem("添加小节");
	JMenuItem delMea = new JMenuItem("移除小节");
	
	public TopPanel(){
		super();
		setLayout(null);
		initComponents();
		setSize(80, 25);
	}
	
	public void initComponents(){
		menuBar = new JMenuBar();
		menuBar.setSize(120, 25);
		
		menuLayout.add(menuAutoLayout);
		
		menuExport.add(menuXML);
		menuExport.add(menuPicture);
		menuFile.add(menuNew);
		menuFile.add(menuImport);
		menuFile.add(menuExport);
		menuFile.add(menuPrint);
		
		menuStaff.add(addPartDown);
		menuStaff.add(addPartUp);
		menuStaff.add(removeStaff);
		menuStaff.addSeparator();
		menuStaff.add(changeTime);
		menuStaff.add(changeKey);
		menuStaff.add(changeClef);
		menuStaff.addSeparator();
		menuStaff.add(addTuplet);
		menuStaff.addSeparator();
		menuStaff.add(addMea);
		menuStaff.add(delMea);
		
		menuBar.add(menuFile);
		menuBar.add(menuStaff);
		menuBar.add(menuLayout);
		add(menuBar);
		
		int x = 0;
		int y = 5;
		menuFile.setLocation(x, y);
		x += menuFile.getWidth();
		menuLayout.setLocation(x, y);
	}
	
	/**
	 * 设置大小
	 */
	public void setSize(int x, int y){
		super.setSize(x, y);
//		menuBar.setLocation(getWidth() - menuBar.getWidth() - 10, getHeight() - menuBar.getHeight());
		menuBar.setLocation(0, getHeight() - menuBar.getHeight());
	}
	
	/**
	 * 添加五线谱菜单侦听器
	 * @param l
	 */
	public void addStaffListener(ActionListener l){
		addPartDown.addActionListener(l);
		addPartUp.addActionListener(l);
		removeStaff.addActionListener(l);
		changeTime.addActionListener(l);
		changeKey.addActionListener(l);
		changeClef.addActionListener(l);
		addMea.addActionListener(l);
		delMea.addActionListener(l);
	}
	
	/**
	 * 添加文件侦听器
	 * @param listener
	 */
	public void addFileListener(ActionListener listener){
		menuNew.addActionListener(listener);
		menuImport.addActionListener(listener);
		menuXML.addActionListener(listener);
		menuPicture.addActionListener(listener);
		menuPrint.addActionListener(listener);
	}
	
	/**
	 * 添加布局菜单侦听器
	 * @param listener
	 */
	public void addLayoutListenern(ActionListener listener){
		menuAutoLayout.addActionListener(listener);
	}
	
	public void addFunctionListener(ActionListener l){
		this.addStaffListener(l);
		this.addFileListener(l);
		this.addLayoutListenern(l);
	}

	public JMenu getMenuFile() {
		return menuFile;
	}

	public JMenu getMenuStaff() {
		return menuStaff;
	}

	public JMenu getMenuLayout() {
		return menuLayout;
	}

	public JMenuItem getMenuAutoLayout() {
		return menuAutoLayout;
	}

	public JMenuItem getMenuNew() {
		return menuNew;
	}

	public JMenuItem getMenuImport() {
		return menuImport;
	}

	public JMenuItem getMenuPrint(){
		return menuPrint;
	}

	public JMenu getMenuExport() {
		return menuExport;
	}

	public JMenuItem getMenuXML() {
		return menuXML;
	}

	public JMenuItem getMenuPicture() {
		return menuPicture;
	}

	public JMenuItem getAddPartDown() {
		return addPartDown;
	}

	public JMenuItem getAddPartUp() {
		return addPartUp;
	}

	public JMenuItem getRemoveStaff() {
		return removeStaff;
	}

	public JMenuItem getChangeTime() {
		return changeTime;
	}

	public JMenuItem getChangeKey() {
		return changeKey;
	}

	public JMenuItem getChangeClef() {
		return changeClef;
	}

	public JMenu getAddTuplet() {
		return addTuplet;
	}

	public JMenuItem getAddMea() {
		return addMea;
	}

	public JMenuItem getDelMea() {
		return delMea;
	}

}
