package sjy.elwg.notation;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

public class GraceSymbolDialog extends JDialog implements ActionListener{
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 5586683756569608888L;
	/**
	 * 窗口大小
	 */
	private static final int DIALOG_WIDTH = 90;
	private static final int DIALOG_HEIGHT = 155;
	/**
	 *添加的是装饰符号还是倚音,0为装饰符号，1为倚音
	 */
	int attributes;
	

	/**
	 * 装饰符号类型
	 */
	String type = null;
	int number;
	
	/**
	 * 倚音类型
	 */
	int duration = 0;
	boolean hasSlash = false;
	String direction = null;
	
	ImageIcon iconVibrato = new ImageIcon(("pic/vibrato.png"));
	ImageIcon iconMordent = new ImageIcon(("pic/mordent.png"));
	ImageIcon iconTurn = new ImageIcon(("pic/turn.png"));
	ImageIcon iconInvertedTurn= new ImageIcon(("pic/inverted-turn.png"));
	ImageIcon iconThrill = new ImageIcon(("pic/thrill.png"));
	ImageIcon iconThrillFlat = new ImageIcon(("pic/thrill-flat.png"));
	ImageIcon iconThrillSharp= new ImageIcon(("pic/thrill-sharp.png"));
	ImageIcon iconThrillNatural = new ImageIcon(("pic/thrill-natural.png"));
	ImageIcon iconThrillVibrato = new ImageIcon(("pic/thrill-vibrato.png"));
	
	ImageIcon iconG8s = new ImageIcon(("pic/grace1.png"));
	ImageIcon iconG8 = new ImageIcon(("pic/grace2.png"));
	ImageIcon iconG4 = new ImageIcon(("pic/grace3.png"));
	ImageIcon iconG16 = new ImageIcon(("pic/grace16.png"));
	ImageIcon iconG32 = new ImageIcon(("pic/grace5.png"));

	


	/**
	 * 按钮
	 */
	private JButton btVibrato = new JButton(iconVibrato);
	private JButton btMordent = new JButton(iconMordent);
	private JButton btTurn = new JButton(iconTurn);
	private JButton btInvertedTurn = new JButton(iconInvertedTurn);
	private JButton btThrill = new JButton(iconThrill);
	private JButton btThrillFlat = new JButton(iconThrillFlat);
	private JButton btThrillSharp = new JButton(iconThrillSharp);
	private JButton btThrillNatural = new JButton(iconThrillNatural);
	private JButton btThrillVibrato = new JButton(iconThrillVibrato);
	
	private JButton btGrace8s = new JButton(iconG8s);
	private JButton btGrace8 = new JButton(iconG8);
	private JButton btGrace4 = new JButton(iconG4);
	private JButton btGrace16 = new JButton(iconG16);
	private JButton btGrace32 = new JButton(iconG32);


	
	
	public GraceSymbolDialog(){
		super();
		setLayout(null);
		setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
		initComponents();	
		setModal(true);
		addFunctionListener(this);
	}
	

	void addFunctionListener(ActionListener l) {
		// TODO Auto-generated method stub
		btVibrato.addActionListener(l);
		btMordent.addActionListener(l);
		btTurn.addActionListener(l);
		btInvertedTurn.addActionListener(l);
		btThrill.addActionListener(l);
		btThrillFlat.addActionListener(l);
		btThrillSharp.addActionListener(l);
		btThrillNatural.addActionListener(l);
		btThrillVibrato.addActionListener(l);
		
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
		add(btVibrato);
		add(btMordent);
		add(btTurn);
		add(btInvertedTurn);
		add(btThrill);
		add(btThrillFlat);
		add(btThrillSharp);
		add(btThrillNatural);
		add(btThrillVibrato);
		add(btGrace8s);
		add(btGrace8);
		add(btGrace4);
		add(btGrace16);
		add(btGrace32);


		
		int x = 10; int y = 5;
		btVibrato.setBounds(x, y, 20, 25);
		x += 25;
		btMordent.setBounds(x, y, 20, 25);
		x += 25;
		btTurn.setBounds(x, y, 20, 25);
		x += 25;
		btInvertedTurn.setBounds(x, y, 20, 25);
		x = 10; y += 27;
		btThrill.setBounds(x, y, 20, 26);
		x += 25;
		btThrillFlat.setBounds(x, y, 20, 26);
		x += 25;
		btThrillSharp.setBounds(x, y, 20, 26);
		x += 25;
		btThrillNatural.setBounds(x, y, 20, 26);
		x = 10; y += 28;
		btThrillVibrato.setBounds(x, y, 45, 25);
		x = 10; y += 28;
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
		
		if(e.getSource() == btVibrato){	
			attributes = 0;
			type = "inverted-mordent";

			dispose();
		}else if(e.getSource() == btMordent){
			attributes = 0;
			type = "mordent";
			
			dispose();
		}else if(e.getSource() == btTurn){
			attributes = 0;
			type = "turn";
			
			dispose();
		}else if(e.getSource() == btInvertedTurn){
			attributes = 0;
			type = "inverted-turn";
			
			dispose();
		}else if(e.getSource() == btThrill){
			attributes = 0;
			type = "trill-mark";
			
			dispose();
		}else if(e.getSource() == btThrillFlat){
			attributes = 0;
			type = "trill-flat";
			
			dispose();
		}else if(e.getSource() == btThrillSharp){
			attributes = 0;
			type = "trill-sharp";
			
			dispose();
		}else if(e.getSource() == btThrillNatural){
			attributes = 0;
			type = "trill-natural";
			
			dispose();
		}else if(e.getSource() == btThrillVibrato){
			attributes = 0;
			number = 2;			
			dispose();
		}

		
		else if(e.getSource() == btGrace8s){	
			attributes = 1;
			duration = 32;
			hasSlash = true;
			direction = "left";

			dispose();
		}else if(e.getSource() == btGrace8){
			attributes = 1;
			duration = 32;
			hasSlash = false;
			direction = "left";
			
			dispose();
		}else if(e.getSource() == btGrace4){
			attributes = 1;
			duration = 64;
			hasSlash = false;
			direction = "left";
			
			dispose();
		}else if(e.getSource() == btGrace16){
			attributes = 1;
			duration = 16;
			hasSlash = false;
			direction = "left";
			
			dispose();
		}else if(e.getSource() == btGrace32){
			attributes = 1;
			duration = 8;
			hasSlash = false;
			direction = "left";
			
			dispose();
		}
	}
	
	


	public int getNumber() {
		return number;
	}


	public void setNumber(int number) {
		this.number = number;
	}


	public String getSymbolType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public int getAttributes() {
		return attributes;
	}


	public void setAttributes(int attributes) {
		this.attributes = attributes;
	}

}
