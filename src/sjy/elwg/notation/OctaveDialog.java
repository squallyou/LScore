package sjy.elwg.notation;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

public class OctaveDialog extends JDialog implements ActionListener{
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1994457778616743076L;
	/**
	 * 窗口大小
	 */
	private static final int DIALOG_WIDTH = 160;
	private static final int DIALOG_HEIGHT = 73;
	
	/**
	 * 类型
	 */

	int number;
	
	ImageIcon icon8va = new ImageIcon(("pic/8va1.png"));
	ImageIcon icon8vb = new ImageIcon(("pic/8vb.png"));


	
	/**
	 * 按钮
	 */
	private JButton bt8va = new JButton(icon8va);
	private JButton bt8vb = new JButton(icon8vb);



	
	public OctaveDialog(){
		super();
		setLayout(null);
		setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
		initComponents();	
		setModal(true);
		addFunctionListener(this);
	}
	

	void addFunctionListener(ActionListener l) {
		// TODO Auto-generated method stub
		bt8va.addActionListener(l);
		bt8vb.addActionListener(l);
	}

	/**
	 * 初始化按钮
	 */
	public void initComponents(){
	
		setLayout(null);
		add(bt8va);
		add(bt8vb);



		
		int x = 10; int y = 5;
		bt8va.setBounds(x, y, 60, 25);
		x += 65;
		bt8vb.setBounds(x, y, 60, 25);
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getSource() == bt8va){	
			number = 3;

			dispose();
		}else if(e.getSource() == bt8vb){
			number = 4;
			
			dispose();
		}
	}

	public int getNumber() {
		return number;
	}


	public void setNumber(int number) {
		this.number = number;
	}



}
