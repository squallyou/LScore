package sjy.elwg.notation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

public class TupletDialog extends JDialog implements ActionListener{
	/**
	 * –Ú¡–∫≈
	 */
	private static final long serialVersionUID = 2165591347010431251L;
	private static final int DIALOG_WIDTH = 215;
	private static final int DIALOG_HEIGHT = 115;
	
	/**
	 * º∏¡¨“Ù
	 */
	int number;
	
	ImageIcon iconTup3 = new ImageIcon(("pic/tup3.png"));
	ImageIcon iconTup4 = new ImageIcon(("pic/tup4.png"));
	ImageIcon iconTup5 = new ImageIcon(("pic/tup5.png"));
	ImageIcon iconTup6 = new ImageIcon(("pic/tup6.png"));
	ImageIcon iconTup7 = new ImageIcon(("pic/tup7.png"));
	ImageIcon iconTup8 = new ImageIcon(("pic/tup8.png"));
	
	
	
	private JButton btTup3 = new JButton(iconTup3);
	private JButton btTup4 = new JButton(iconTup4);
	private JButton btTup5 = new JButton(iconTup5);
	private JButton btTup6 = new JButton(iconTup6);
	private JButton btTup7 = new JButton(iconTup7);
	private JButton btTup8 = new JButton(iconTup8);
	
	public TupletDialog(){
		super();
		setLayout(null);
		setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
		initComponents();	
		setModal(true);
		addFunctionListener(this);
	}
	
	
	void addFunctionListener(ActionListener l) {
		// TODO Auto-generated method stub
		btTup3.addActionListener(l);
		btTup4.addActionListener(l);
		btTup5.addActionListener(l);
		btTup6.addActionListener(l);
		btTup7.addActionListener(l);
		btTup8.addActionListener(l);
	}

	private void initComponents() {
		// TODO Auto-generated method stub
		setLayout(null);
		add(btTup3);
		add(btTup4);
		add(btTup5);
		add(btTup6);
		add(btTup7);
		add(btTup8);
		
		int x = 5;int y = 5;
		btTup3.setBounds(x, y, 60, 30);
		x += 65;
		btTup4.setBounds(x, y, 60, 30);
		x += 65;
		btTup5.setBounds(x, y, 60, 30);
		x = 5;y = 40;
		btTup6.setBounds(x, y, 60, 30);
		x += 65;
		btTup7.setBounds(x, y, 60, 30);
		x += 65;
		btTup8.setBounds(x, y, 60, 30);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btTup3){
			number = 3;
			dispose();
		}else if(e.getSource() == btTup4){
			number = 4;
			dispose();
		}else if(e.getSource() == btTup5){
			number = 5;
			dispose();
		}else if(e.getSource() == btTup6){
			number = 6;
			dispose();
		}else if(e.getSource() == btTup7){
			number = 7;
			dispose();
		}else if(e.getSource() == btTup8){
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
