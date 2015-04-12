package sjy.elwg.notation;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;


public class ClefDialog extends JDialog implements ActionListener{
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 5822711597877496859L;

	/**
	 * 窗口大小
	 */
	private static final int DIALOG_WIDTH = 305;
	private static final int DIALOG_HEIGHT = 240;
	/**
	 * 按钮大小
	 */
	private static final int BT_WIDTH = 30;
	private static final int BT_HEIGHT = 60;
	

	/**
	 * 按钮图标
	 */
	ImageIcon iconC3 = new ImageIcon(("pic/c3.png"));
	ImageIcon iconF4 = new ImageIcon(("pic/f4.png"));
	ImageIcon iconG2 = new ImageIcon(("pic/g2.png"));
	ImageIcon iconC1 = new ImageIcon(("pic/c1.png"));
	ImageIcon iconC2 = new ImageIcon(("pic/c2.png"));
	ImageIcon iconC4 = new ImageIcon(("pic/c4.png"));
	ImageIcon iconC5 = new ImageIcon(("pic/c5.png"));
	ImageIcon iconF3 = new ImageIcon(("pic/f3.png"));
	ImageIcon iconF5 = new ImageIcon(("pic/f5.png"));
	ImageIcon iconF41d = new ImageIcon(("pic/f41d.png"));
	ImageIcon iconF41u = new ImageIcon(("pic/f41u.png"));
	ImageIcon iconF42d = new ImageIcon(("pic/f42d.png"));
	ImageIcon iconF42u = new ImageIcon(("pic/f42u.png"));
	ImageIcon iconG1 = new ImageIcon(("pic/g1.png"));
	ImageIcon iconG21u = new ImageIcon(("pic/g21u.png"));
	ImageIcon iconG21d = new ImageIcon(("pic/g21d.png"));
	ImageIcon iconG22u = new ImageIcon(("pic/g22u.png"));
	ImageIcon iconf41u = new ImageIcon(("pic/f41u.png"));
	
	

	
	/**
	 * 按钮
	 */

	
	private JButton btC3 = new JButton(iconC3);
	private JButton btF4 = new JButton(iconF4);
	private JButton btG2 = new JButton(iconG2);
	private JButton btC1 = new JButton(iconC1);
	private JButton btC2 = new JButton(iconC2);
	private JButton btC4 = new JButton(iconC4);
	private JButton btC5 = new JButton(iconC5);
	private JButton btF3 = new JButton(iconF3);
	private JButton btF5 = new JButton(iconF5);
	private JButton btF41d = new JButton(iconF41d);
	private JButton btF41u = new JButton(iconF41u);
	private JButton btF42d = new JButton(iconF42d);
	private JButton btF42u = new JButton(iconF42u);
	private JButton btG1 = new JButton(iconG1);
	private JButton btG21u = new JButton(iconG21u);
	private JButton btG21d = new JButton(iconG21d);
	private JButton btG22u = new JButton(iconG22u);
	private JButton btf41u = new JButton(iconf41u);

	
	/**
	 * 当前所选择的拍号的属性
	 */
	private String clef = "g/2";
	
	public String getClef() {
		return clef;
	}




	public void setClef(String clef) {
		this.clef = clef;
	}




	/**
	 * 构造函数
	 */
	public ClefDialog(){
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
		btC3.setSize(BT_WIDTH, BT_HEIGHT);
		btF4.setSize(BT_WIDTH, BT_HEIGHT);
		btG2.setSize(BT_WIDTH, BT_HEIGHT);
		btC1.setSize(BT_WIDTH, BT_HEIGHT);
		btC2.setSize(BT_WIDTH, BT_HEIGHT);
		btC4.setSize(BT_WIDTH, BT_HEIGHT);
		btC5.setSize(BT_WIDTH, BT_HEIGHT);
		btF3.setSize(BT_WIDTH, BT_HEIGHT);
		btF5.setSize(BT_WIDTH, BT_HEIGHT);
		btF41d.setSize(BT_WIDTH, BT_HEIGHT);
		btF41u.setSize(BT_WIDTH, BT_HEIGHT);
		btF42d.setSize(BT_WIDTH, BT_HEIGHT);
		btF42u.setSize(BT_WIDTH, BT_HEIGHT);
		btG1.setSize(BT_WIDTH, BT_HEIGHT);
		btG21u.setSize(BT_WIDTH, BT_HEIGHT);
		btG21d.setSize(BT_WIDTH, BT_HEIGHT);
		btG22u.setSize(BT_WIDTH, BT_HEIGHT);
		btf41u.setSize(BT_WIDTH, BT_HEIGHT);

	
		
		int x = 10; 
		int y = 5;
		btG2.setLocation(x, y);
		x += BT_WIDTH + 10;
		btG21u.setLocation(x, y);
		x += BT_WIDTH + 10;
		btG22u.setLocation(x, y);
		x += BT_WIDTH + 10;
		btG21d.setLocation(x, y);
		x += BT_WIDTH + 10;
		btG1.setLocation(x, y);
		x = 10; 
		y += 65;
		btF4.setLocation(x, y);
		x += BT_WIDTH + 10;
		btF41u.setLocation(x, y);
		x += BT_WIDTH + 10;
		btF42u.setLocation(x, y);
		x += BT_WIDTH + 10;
		btF41d.setLocation(x, y);
		x += BT_WIDTH + 10;
		btF42d.setLocation(x, y);
		x += BT_WIDTH + 10;
		btF3.setLocation(x, y);
		x += BT_WIDTH + 10;
		btF5.setLocation(x, y);
		x = 10; 
		y += 65;
		btC3.setLocation(x, y);
		x += BT_WIDTH + 10;
		btC1.setLocation(x, y);
		x += BT_WIDTH + 10;
		btC2.setLocation(x, y);
		x += BT_WIDTH + 10;
		btC4.setLocation(x, y);
		x += BT_WIDTH + 10;
		btC5.setLocation(x, y);
		x += BT_WIDTH + 10;

	
		
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(btG2);
		bg.add(btG21u);
		bg.add(btG22u);
		bg.add(btG21d);
		bg.add(btG1);
	//	bg.add(btC3);
	//	bg.add(btG2);
		bg.add(btF4);
		bg.add(btF41u);
		bg.add(btF42u);
		bg.add(btF41d);
		bg.add(btF42d);
		bg.add(btF3);
		bg.add(btF5);
		bg.add(btC3);
		bg.add(btC1);
		bg.add(btC2);
		bg.add(btC4);
		bg.add(btC5);

		
		
		

		add(btG2);
		add(btG21u);
		add(btG22u);
		add(btG21d);
		add(btG1);
	//	add(btC3);
	//	add(btG2);
		add(btF4);
		add(btF41u);
		add(btF42u);
		add(btF41d);
		add(btF42d);
		add(btF3);
		add(btF5);
		add(btC3);
		add(btC1);
		add(btC2);
		add(btC4);
		add(btC5);
		


		
		btG2.addActionListener(this);
		btG21u.addActionListener(this);
		btG22u.addActionListener(this);
		btG21d.addActionListener(this);
		btG1.addActionListener(this);
	//	btC3.addActionListener(this);
	//	btG2.addActionListener(this);
		btF4.addActionListener(this);
		btF41u.addActionListener(this);
		btF42u.addActionListener(this);
		btF41d.addActionListener(this);
		btF42d.addActionListener(this);
		btF3.addActionListener(this);
		btF5.addActionListener(this);
		btC3.addActionListener(this);
		btC1.addActionListener(this);
		btC2.addActionListener(this);
		btC4.addActionListener(this);
		btC5.addActionListener(this);

		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btG2 ){
			clef = "g/2";
			dispose();
		}else if(e.getSource() == btG21u ){
			clef = "g1u/2";
			dispose();
		}else if(e.getSource() == btG22u ){
			clef = "g2u/2";
			dispose();
		}else if(e.getSource() == btG21d ){
			clef = "g1d/2";
			dispose();
		}
		else if(e.getSource() == btG1 ){
			clef = "g/1";
			dispose();
		}

		
		else if(e.getSource() == btF4 ){
			clef = "f/4";
			dispose();
		}else if(e.getSource() == btF41u ){
			clef = "f1u/4";
			dispose();
		}
		else if(e.getSource() == btF42u ){
			clef = "f2u/4";
			dispose();
		}
		else if(e.getSource() == btF41d ){
			clef = "f1d/4";
			dispose();
		}
		else if(e.getSource() == btF42d ){
			clef = "f2d/4";
			dispose();
		}
		else if(e.getSource() == btF3 ){
			clef = "f/3";
			dispose();
		}
		else if(e.getSource() == btF5 ){
			clef = "f/5";
			dispose();
		}
		else if(e.getSource() == btC3 ){
			clef = "c/3";
			dispose();
		}
		else if(e.getSource() == btC1 ){
			clef = "c/1";
			dispose();
		}
		else if(e.getSource() == btC2 ){
			clef = "c/2";
			dispose();
		}
		
		else if(e.getSource() == btC4 ){
			clef = "c/4";
			dispose();
		}
		
		else if(e.getSource() == btC5 ){
			clef = "c/5";
			dispose();
		}
		
		
		
		

	}





}
