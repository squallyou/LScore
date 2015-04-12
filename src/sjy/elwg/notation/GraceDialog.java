package sjy.elwg.notation;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

public class GraceDialog extends JDialog implements ActionListener{
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 7408210062356264538L;
	/**
	 * 窗口大小
	 */
	private static final int DIALOG_WIDTH = 70;
	private static final int DIALOG_HEIGHT = 70;
	
	/**
	 * 倚音类型
	 */
	int duration = 0;
	boolean hasSlash = false;
	String direction = null;
	
	ImageIcon iconG8s = new ImageIcon("pic/grace1.png");
	ImageIcon iconG8 = new ImageIcon(("pic/grace2.png"));
	ImageIcon iconG4 = new ImageIcon(("pic/grace3.png"));
	ImageIcon iconG16 = new ImageIcon(("pic/grace16.png"));
	ImageIcon iconG32 = new ImageIcon(("pic/grace5.png"));

	
	/**
	 * 按钮
	 */
	private JButton btGrace8s = new JButton(iconG8s);
	private JButton btGrace8 = new JButton(iconG8);
	private JButton btGrace4 = new JButton(iconG4);
	private JButton btGrace16 = new JButton(iconG16);
	private JButton btGrace32 = new JButton(iconG32);

	
	
	public GraceDialog(){
		super();
		setLayout(null);
		setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
		initComponents();	
		setModal(true);
		addFunctionListener(this);
	}
	

	void addFunctionListener(ActionListener l) {
		// TODO Auto-generated method stub
		btGrace8s.addActionListener(l);
		btGrace8.addActionListener(l);
		btGrace4.addActionListener(l);
		btGrace16.addActionListener(l);
		btGrace32.addActionListener(l);
	}

	/**
	 * 初始化按钮
	 */
	public void initComponents(){
	
		setLayout(null);
		add(btGrace8s);
		add(btGrace8);
		add(btGrace4);
		add(btGrace16);
		add(btGrace32);


		
		int x = 9; int y = 5;
		btGrace8s.setBounds(x, y, 16, 25);
		x += 20;
		btGrace8.setBounds(x, y, 16, 25);
		x += 20;
		btGrace4.setBounds(x, y, 16, 25);
		x += 20;
		btGrace16.setBounds(x, y, 16, 25);
		x += 20;
		btGrace32.setBounds(x, y, 16, 25);

	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getSource() == btGrace8s){	
			duration = 32;
			hasSlash = true;
			direction = "left";

			dispose();
		}else if(e.getSource() == btGrace8){
			duration = 32;
			hasSlash = false;
			direction = "left";
			
			dispose();
		}else if(e.getSource() == btGrace4){
			duration = 64;
			hasSlash = false;
			direction = "left";
			
			dispose();
		}else if(e.getSource() == btGrace16){
			duration = 16;
			hasSlash = false;
			direction = "left";
			
			dispose();
		}else if(e.getSource() == btGrace32){
			duration = 8;
			hasSlash = false;
			direction = "left";
			
			dispose();
		}
	}


	public int getDuration() {
		return duration;
	}


	public void setDuration(int duration) {
		this.duration = duration;
	}


	public boolean isHasSlash() {
		return hasSlash;
	}


	public void setHasSlash(boolean hasSlash) {
		this.hasSlash = hasSlash;
	}


	public String getDirection() {
		return direction;
	}


	public void setDirection(String direction) {
		this.direction = direction;
	}




}
