package sjy.elwg.notation;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JRadioButton;


public class KeyDialog extends JDialog implements ActionListener{

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 711504499068647979L;
	
	/**
	 * 窗口大小
	 */
	private static final int DIALOG_WIDTH = 385;
	private static final int DIALOG_HEIGHT = 225;
	/**
	 * 按钮大小
	 */
	private static final int BT_WIDTH = 50;
	private static final int BT_HEIGHT = 50;
	

	/**
	 * 按钮图标
	 */
	ImageIcon iconNatural = new ImageIcon(("pic/naturalClef.png"));
	ImageIcon iconOneFlat = new ImageIcon(("pic/oneflat.png"));
	ImageIcon iconTwoFlat = new ImageIcon(("pic/twoflat.png"));
	ImageIcon iconThreeFlat= new ImageIcon(("pic/threeflat.png"));
	ImageIcon iconFourFlat = new ImageIcon(("pic/fourflat.png"));
	ImageIcon iconFiveFlat = new ImageIcon(("pic/fiveflat.png"));
	ImageIcon iconSixFlat = new ImageIcon(("pic/sixflat.png"));
	ImageIcon iconSevenFlat = new ImageIcon(("pic/sevenflat.png"));
	ImageIcon iconOneSharp = new ImageIcon(("pic/onesharp.png"));
	ImageIcon iconTwoSharp= new ImageIcon(("pic/twosharp.png"));
	ImageIcon iconThreeSharp = new ImageIcon(("pic/threesharp.png"));
	ImageIcon iconFourSharp = new ImageIcon(("pic/foursharp.png"));
	ImageIcon iconFiveSharp= new ImageIcon(("pic/fivesharp.png"));
	ImageIcon iconSixSharp = new ImageIcon(("pic/sixsharp.png"));
	ImageIcon iconSevenSharp = new ImageIcon(("pic/sevensharp.png"));
	
	/**
	 * 按钮
	 */
//	private JButton btYes = new JButton("确定");
//	private JButton btCancle = new JButton("取消");
	
	private JButton bt1s = new JButton(iconOneSharp);
	private JButton bt2s = new JButton(iconTwoSharp);
	private JButton bt3s = new JButton(iconThreeSharp);
	private JButton bt4s = new JButton(iconFourSharp);
	private JButton bt5s = new JButton(iconFiveSharp);
	private JButton bt6s = new JButton(iconSixSharp);
	private JButton bt7s = new JButton(iconSevenSharp);
	private JButton bt1f = new JButton(iconOneFlat);
	private JButton bt2f = new JButton(iconTwoFlat);
	private JButton bt3f = new JButton(iconThreeFlat);
	private JButton bt4f = new JButton(iconFourFlat);
	private JButton bt5f = new JButton(iconFiveFlat);
	private JButton bt6f = new JButton(iconSixFlat);
	private JButton bt7f = new JButton(iconSevenFlat);
	private JButton bt0 = new JButton(iconNatural);
	


	/**
	 * 当前所选择的调号值
	 */
	private int key = 0;
	
	/**
	 * 构造函数
	 */
	public KeyDialog(){
		super();
		setLayout(null);
		setModal(true);
		setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
		initComponents();
//		setAlwaysOnTop(true);
		//setVisible(true);
	}
	

	


	/**
	 * 初始化按钮
	 */
	public void initComponents(){
		bt1s.setSize(BT_WIDTH, BT_HEIGHT);
		bt2s.setSize(BT_WIDTH, BT_HEIGHT);
		bt3s.setSize(BT_WIDTH, BT_HEIGHT);
		bt4s.setSize(BT_WIDTH, BT_HEIGHT);
		bt5s.setSize(BT_WIDTH, BT_HEIGHT);
		bt6s.setSize(BT_WIDTH, BT_HEIGHT);
		bt7s.setSize(BT_WIDTH, BT_HEIGHT);
		bt1f.setSize(BT_WIDTH, BT_HEIGHT);
		bt2f.setSize(BT_WIDTH, BT_HEIGHT);
		bt3f.setSize(BT_WIDTH, BT_HEIGHT);
		bt4f.setSize(BT_WIDTH, BT_HEIGHT);
		bt5f.setSize(BT_WIDTH, BT_HEIGHT);
		bt6f.setSize(BT_WIDTH, BT_HEIGHT);
		bt7f.setSize(BT_WIDTH, BT_HEIGHT);
		bt0.setSize(BT_WIDTH, BT_HEIGHT);
		
		int x = 20; 
		int y = 5;
		bt0.setLocation(x, y);
		x += BT_WIDTH + 20;
		bt1s.setLocation(x, y);
		x += BT_WIDTH + 20;
		bt2s.setLocation(x, y);
		x += BT_WIDTH + 20;
		bt3s.setLocation(x, y);
		x += BT_WIDTH + 20;
		bt4s.setLocation(x, y);
		
		x = 20; y = 66;
		bt5s.setLocation(x, y);
		x += BT_WIDTH + 20;
		bt6s.setLocation(x, y);
		x += BT_WIDTH + 20;
		bt7s.setLocation(x, y);
		x += BT_WIDTH + 20;
		bt1f.setLocation(x, y);
		x += BT_WIDTH + 20;
		bt2f.setLocation(x, y);
		
		x = 20; y = 127;
		bt3f.setLocation(x, y);
		x += BT_WIDTH + 20;
		bt4f.setLocation(x, y);
		x += BT_WIDTH + 20;
		bt5f.setLocation(x, y);
		x += BT_WIDTH + 20;
		bt6f.setLocation(x, y);
		x += BT_WIDTH + 20;
		bt7f.setLocation(x, y);
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(bt0);
		bg.add(bt1f);
		bg.add(bt2f);
		bg.add(bt3f);
		bg.add(bt4f);
		bg.add(bt5f);
		bg.add(bt6f);
		bg.add(bt7f);
		bg.add(bt1s);
		bg.add(bt2s);
		bg.add(bt3s);
		bg.add(bt4s);
		bg.add(bt5s);
		bg.add(bt6s);
		bg.add(bt7s);
		
//		btYes.setSize(60 , 30);
//		btCancle.setSize(60, 30);
//		btYes.setLocation(380, 40);
//		btCancle.setLocation(380, 105);
		
		add(bt0);
		add(bt1f);
		add(bt2f);
		add(bt3f);
		add(bt4f);
		add(bt5f);
		add(bt6f);
		add(bt7f);
		add(bt1s);
		add(bt2s);
		add(bt3s);
		add(bt4s);
		add(bt5s);
		add(bt6s);
		add(bt7s);
//		add(btYes);
//		add(btCancle);
		
		bt0.addActionListener(this);
		bt1f.addActionListener(this);
		bt2f.addActionListener(this);
		bt3f.addActionListener(this);
		bt4f.addActionListener(this);
		bt5f.addActionListener(this);
		bt6f.addActionListener(this);
		bt7f.addActionListener(this);
		bt1s.addActionListener(this);
		bt2s.addActionListener(this);
		bt3s.addActionListener(this);
		bt4s.addActionListener(this);
		bt5s.addActionListener(this);
		bt6s.addActionListener(this);
		bt7s.addActionListener(this);
//		btYes.addActionListener(this);
//		btCancle.addActionListener(this);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == bt0 ){
			key = 0;
			dispose();
		}else if(e.getSource() == bt1s ){
			key = 1;
			dispose();
		}else if(e.getSource() == bt2s ){
			key = 2;
			dispose();
		}else if(e.getSource() == bt3s ){
			key = 3;
			dispose();
		}else if(e.getSource() == bt4s ){
			key = 4;
			dispose();
		}else if(e.getSource() == bt5s ){
			key = 5;
			dispose();
		}else if(e.getSource() == bt6s ){
			key = 6;
			dispose();
		}else if(e.getSource() == bt7s ){
			key = 7;
			dispose();
		}else if(e.getSource() == bt1f ){
			key = -1;
			dispose();
		}else if(e.getSource() == bt2f ){
			key = -2;
			dispose();
		}else if(e.getSource() == bt3f ){
			key = -3;
			dispose();
		}else if(e.getSource() == bt4f ){
			key = -4;
			dispose();
		}else if(e.getSource() == bt5f ){
			key = -5;
			dispose();
		}else if(e.getSource() == bt6f ){
			key = -6;
			dispose();
		}else if(e.getSource() == bt7f ){
			key = -7;
			dispose();
		}
		
		
		
		
		if(e.getSource() instanceof JRadioButton){
			JRadioButton jrb = (JRadioButton)e.getSource();
			int key = Integer.parseInt(jrb.getText());
			this.key = key;
		}
//		else if(e.getSource() == btYes){
//			dispose();
//		}
//		else if(e.getSource() == btCancle){
//			this.key = 100;
//			dispose();
//		}
		
//		if(e.getSource() == bt0){
//			cancleKeyButtonBorder();
//
//			reselectKeyButtonBorder();
//		}
	}

	/**
	 * 获得选择的调号值
	 * @return
	 */
	public int getKey() {
		return key;
	}

	/**
	 * 设置调号值
	 * @param key
	 */
	public void setKey(int key) {
		this.key = key;
	}

}
