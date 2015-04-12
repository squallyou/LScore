package sjy.elwg.notation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

public class LineSymbolDialog extends JDialog implements ActionListener{
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -777348936471249281L;
	
	/**
	 * 窗口大小
	 */
	private static final int DIALOG_WITDH = 195;
	private static final int DIALOG_HEIGHT = 128;

	/**
	 *线条类型
	 */
	int number;
	
	
	ImageIcon iconCrescendo = new ImageIcon(("pic/crescendo1.png"));
	ImageIcon iconDiminuendo = new ImageIcon(("pic/diminuendo.png"));
	ImageIcon iconCresc = new ImageIcon(("pic/cresc.png"));
	ImageIcon iconDime= new ImageIcon(("pic/dim.png"));
	ImageIcon icon8va = new ImageIcon(("pic/8va1.png"));
	ImageIcon icon8vb = new ImageIcon(("pic/8vb.png"));
	ImageIcon iconSlur = new ImageIcon(("pic/slur.png"));

	
	/**
	 * 按钮
	 */
	private JButton btCrescendo = new JButton(iconCrescendo);
	private JButton btDiminuendo = new JButton(iconDiminuendo);
	private JButton btCresc = new JButton(iconCresc);
	private JButton btDim = new JButton(iconDime);
	private JButton bt8va = new JButton(icon8va);
	private JButton bt8vb = new JButton(icon8vb);
	private JButton btSlur = new JButton(iconSlur);

	
	public LineSymbolDialog(){
		super();
		setLayout(null);
		setSize(DIALOG_WITDH, DIALOG_HEIGHT);
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
		bt8va.addActionListener(l);
		bt8vb.addActionListener(l);
		btSlur.addActionListener(l);
	}

	/**
	 * 初始化按钮
	 */
	public void initComponents(){
	
		setLayout(null);

		setLayout(null);
		add(btCrescendo);
		add(btDiminuendo);
		add(btCresc);
		add(btDim);
		add(bt8va);
		add(bt8vb);
		add(btSlur);



		
		int x = 10; int y = 5;
		btCrescendo.setBounds(x, y, 45, 25);
		x += 50;
		btDiminuendo.setBounds(x, y, 45, 25);
		x += 50;
		bt8va.setBounds(x, y, 60, 25);
		x = 10; y += 28;
		btCresc.setBounds(x, y, 45, 25);
		x += 50;
		btDim.setBounds(x, y, 45, 25);
		x += 50;
		bt8vb.setBounds(x, y, 60, 25);
		x = 10; y += 28;
		btSlur.setBounds(x, y, 45, 25);
		
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
		}else if(e.getSource() == bt8va){	
			number = 3;

			dispose();
		}else if(e.getSource() == bt8vb){
			number = 4;			
			dispose();
		}else if(e.getSource() == btSlur){
			number = 1;			
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
