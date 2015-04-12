package sjy.elwg.notation;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

public class CrescDimDialog extends JDialog implements ActionListener{
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -2919697504127661463L;
	/**
	 * 窗口大小
	 */
	private static final int DIALOG_WIDTH = 90;
	private static final int DIALOG_HEIGHT = 100;
	
	/**
	 * 类型
	 */

	int number;
	
	ImageIcon iconCrescendo = new ImageIcon(("pic/crescendo1.png"));
	ImageIcon iconDiminuendo = new ImageIcon(("pic/diminuendo.png"));
	ImageIcon iconCresc = new ImageIcon(("pic/cresc.png"));
	ImageIcon iconDime= new ImageIcon(("pic/dim.png"));

	
	/**
	 * 按钮
	 */
	private JButton btCrescendo = new JButton(iconCrescendo);
	private JButton btDiminuendo = new JButton(iconDiminuendo);
	private JButton btCresc = new JButton(iconCresc);
	private JButton btDim = new JButton(iconDime);


	
	public CrescDimDialog(){
		super();
		setLayout(null);
		setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
		initComponents();	
		setModal(true);
		addFunctionListener(this);
	}
	

	void addFunctionListener(ActionListener l) {
		// TODO Auto-generated method stub
		btCrescendo.addActionListener(l);
		btDiminuendo.addActionListener(l);
		btCresc.addActionListener(l);
		btDim.addActionListener(l);
	}

	/**
	 * 初始化按钮
	 */
	public void initComponents(){
	
		setLayout(null);
		add(btCrescendo);
		add(btDiminuendo);
		add(btCresc);
		add(btDim);



		
		int x = 10; int y = 5;
		btCrescendo.setBounds(x, y, 45, 25);
		x += 50;
		btDiminuendo.setBounds(x, y, 45, 25);
		x = 10; y += 28;
		btCresc.setBounds(x, y, 45, 25);
		x += 50;
		btDim.setBounds(x, y, 45, 25);
		

	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getSource() == btCrescendo){	
			number = 5;

			dispose();
		}else if(e.getSource() == btDiminuendo){
			number = 6;
			
			dispose();
		}else if(e.getSource() == btCresc){
			number = 9;
			
			dispose();
		}else if(e.getSource() == btDim){
			number = 8;
			
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
