package sjy.elwg.notation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

public class BarlineDialog extends JDialog implements ActionListener{

	/**
	 * 小节线修改面板，小节线有不同的属性，这里暂时定为7个，分别为regular,heavy,light-light,
	 * light-heavy,backward,forward
	 * 
	 */
	private String type = "regular";

	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -4734349311645410340L;

	
	/**
	 * 窗口大小
	 */
	private static final int DIALOG_WIDTH = 170;
	private static final int DIALOG_HEIGHT = 75;



	ImageIcon iconBarline1 = new ImageIcon(("pic/regular.png"));
	ImageIcon iconBarline2 = new ImageIcon(("pic/heavy.png"));
	ImageIcon iconBarline3 = new ImageIcon(("pic/light-light.png"));
	ImageIcon iconBarline4 = new ImageIcon(("pic/light-heavy.png"));
	ImageIcon iconBarline5 = new ImageIcon(("pic/backward.png"));
	ImageIcon iconBarline6 = new ImageIcon(("pic/forward.png"));
	ImageIcon iconBarline7 = new ImageIcon(("pic/forward-backward.png"));
	/**
	 * 按钮
	 */
	private JButton btBarline1 = new JButton(iconBarline1);
	private JButton btBarline2 = new JButton(iconBarline2);
	private JButton btBarline3 = new JButton(iconBarline3);
	private JButton btBarline4 = new JButton(iconBarline4);
	private JButton btBarline5 = new JButton(iconBarline5);
	private JButton btBarline6 = new JButton(iconBarline6);
	private JButton btBarline7 = new JButton(iconBarline7);

	
	/**
	 * 构造函数
	 */
	public BarlineDialog(){
		super();
		setLayout(null);
		setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
		initComponents();	
		setModal(true);
		addFunctionListener(this);
		//此处setVisible的代码的位置很特殊
	//	setVisible(true);
	
	}

	void addFunctionListener(ActionListener l) {
		// TODO Auto-generated method stub
		btBarline1.addActionListener(l);
		btBarline2.addActionListener(l);
		btBarline3.addActionListener(l);
		btBarline4.addActionListener(l);
		btBarline5.addActionListener(l);
		btBarline6.addActionListener(l);
		btBarline7.addActionListener(l);
	}

	/**
	 * 初始化按钮
	 */
	public void initComponents(){
	
		setLayout(null);
		add(btBarline1);
		add(btBarline2);
		add(btBarline3);
		add(btBarline4);
		add(btBarline5);
		add(btBarline6);
		add(btBarline7);

		
		int x = 5; int y = 5;
		btBarline1.setBounds(x, y, 15, 30);
		x += 20;
		btBarline2.setBounds(x, y, 15, 30);
		x += 20;
		btBarline3.setBounds(x, y, 15, 30);
		x += 20;
		btBarline4.setBounds(x, y, 15, 30);
		x += 20;
		btBarline5.setBounds(x, y, 15, 30);
		x += 20;
		btBarline6.setBounds(x, y, 15, 30);
		x += 20;
		btBarline7.setBounds(x, y, 25, 30);
	
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getSource() == btBarline1){	
			type = "regular";
			dispose();
		}else if(e.getSource() == btBarline2){
			type = "heavy";
			dispose();
		}else if(e.getSource() == btBarline3){
			type = "light-light";
			dispose();
		}else if(e.getSource() == btBarline4){
			type = "light-heavy";
			dispose();
		}else if(e.getSource() == btBarline5){
			type = "backward";
			dispose();
		}else if(e.getSource() == btBarline6){
			type = "forward";
			dispose();
		}else if(e.getSource() == btBarline7){
			type = "backward-forward";
			dispose();
		}
	}

	public String getSymbolType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
