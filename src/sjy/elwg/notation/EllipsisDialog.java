package sjy.elwg.notation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

public class EllipsisDialog extends JDialog implements ActionListener{
	

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -1054697358039249268L;
	/**
	 * 窗口大小
	 */
	private static final int DIALOG_WIDTH = 100;
	private static final int DIALOG_HEIGHT = 75;
	
	/**
	 * 演奏符号类型
	 */
	private String type = "none";
	
	ImageIcon iconTremoloBeam1 = new ImageIcon(("pic/TremoloBeam1.png"));
	ImageIcon iconTremoloBeam2 = new ImageIcon(("pic/TremoloBeam2.png"));
	ImageIcon iconTremoloBeam3 = new ImageIcon(("pic/TremoloBeam3.png"));
	
	/**
	 * 按钮
	 */
	private JButton btTremoloBeam1 = new JButton(iconTremoloBeam1);
	private JButton btTremoloBeam2 = new JButton(iconTremoloBeam2);
	private JButton btTremoloBeam3 = new JButton(iconTremoloBeam3);
	
	
	public EllipsisDialog(){
		super();
		setLayout(null);
		setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
		initComponents();	
		setModal(true);
		addFunctionListener(this);
	}
	

	void addFunctionListener(ActionListener l) {
		// TODO Auto-generated method stub
		btTremoloBeam1.addActionListener(l);
		btTremoloBeam2.addActionListener(l);
		btTremoloBeam3.addActionListener(l);
	}

	/**
	 * 初始化按钮
	 */
	public void initComponents(){
	
		setLayout(null);

		add(btTremoloBeam1);
		add(btTremoloBeam2);
		add(btTremoloBeam3);
		
		int x = 8; int y = 5;
		btTremoloBeam1.setBounds(x, y, 20, 25);	
		x += 20;
		btTremoloBeam2.setBounds(x, y, 20, 25);	
		x += 20;
		btTremoloBeam3.setBounds(x, y, 20, 25);
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getSource() == btTremoloBeam1){
			type = "tremoloBeam1";
			dispose();
		}else if(e.getSource() == btTremoloBeam2){
			type = "tremoloBeam2";
			dispose();
		}else if(e.getSource() == btTremoloBeam3){
			type = "tremoloBeam3";
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
