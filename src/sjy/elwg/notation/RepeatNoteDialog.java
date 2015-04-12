package sjy.elwg.notation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

public class RepeatNoteDialog extends JDialog implements ActionListener{

	/**
	 * 用文字或者符号表示的反复的选择面板，反复记号包括segno,coda,Fine,D.C.,D.C. al Fine
	 * D.s. al Fine,D.C. al Coda,D.S. al Coda,D.S.,To Coda
	 * 
	 */
	private String repeat = "none";

	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -4734349311645410340L;

	
	/**
	 * 窗口大小
	 */
	private static final int DIALOG_WIDTH = 185;
	private static final int DIALOG_HEIGHT = 163;



	ImageIcon iconSegno = new ImageIcon(("pic/Segno.png"));
	ImageIcon iconCoda = new ImageIcon(("pic/Coda.png"));
	ImageIcon iconFine = new ImageIcon(("pic/Fine.png"));
	ImageIcon iconDC = new ImageIcon(("pic/DC.png"));
	ImageIcon iconDS = new ImageIcon(("pic/DS.png"));
	ImageIcon iconDCFine = new ImageIcon(("pic/DCFine.png"));
	ImageIcon iconDSFine = new ImageIcon(("pic/DSFine.png"));
	ImageIcon iconDCCoda = new ImageIcon(("pic/DCCoda.png"));
	ImageIcon iconDSCoda = new ImageIcon(("pic/DSCoda.png"));
	ImageIcon iconToCoda = new ImageIcon(("pic/ToCoda.png"));
	ImageIcon iconCodaLetter = new ImageIcon(("pic/CodaLetter.png"));
	ImageIcon iconRepeatLine = new ImageIcon(("pic/RepeatLine.png"));
	
	/**
	 * 按钮
	 */
	private JButton btSegno = new JButton(iconSegno);
	private JButton btCoda = new JButton(iconCoda);
	private JButton btFine = new JButton(iconFine);
	private JButton btDC = new JButton(iconDC);
	private JButton btDS = new JButton(iconDS);
	private JButton btDCFine = new JButton(iconDCFine);
	private JButton btDSFine = new JButton(iconDSFine);
	private JButton btDCCoda = new JButton(iconDCCoda);
	private JButton btDSCoda = new JButton(iconDSCoda);
	private JButton btToCoda = new JButton(iconToCoda);
	private JButton btCodaLetter = new JButton(iconCodaLetter);
	private JButton btRepeatLine = new JButton(iconRepeatLine);
	
	/**
	 * 构造函数
	 */
	public RepeatNoteDialog(){
		super();
		setLayout(null);
		setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
		initComponents();	
		setModal(true);
		addFunctionListener(this);
	//	setLocationRelativeTo(null);
		//此处setVisible的代码的位置很特殊
	//	setVisible(true);
	
	}

	void addFunctionListener(ActionListener l) {
		// TODO Auto-generated method stub
		btCoda.addActionListener(l);
		btDC.addActionListener(l);
		btDCCoda.addActionListener(l);
		btDCFine.addActionListener(l);
		btDS.addActionListener(l);
		btDSCoda.addActionListener(l);
		btDSFine.addActionListener(l);
		btFine.addActionListener(l);
		btSegno.addActionListener(l);
		btToCoda.addActionListener(l);
		btCodaLetter.addActionListener(l);
		btRepeatLine.addActionListener(l);
	}

	/**
	 * 初始化按钮
	 */
	public void initComponents(){
	
		setLayout(null);
		add(btCoda);
		add(btDC);
		add(btDCCoda);
		add(btDCFine);
		add(btDS);
		add(btDSCoda);
		add(btDSFine);
		add(btFine);
		add(btSegno);
		add(btToCoda);
		add(btCodaLetter);
		add(btRepeatLine);
		
		int x = 5; int y = 5;
		btCoda.setBounds(x, y, 20, 25);
		x += 29;
		btSegno.setBounds(x, y, 20, 25);
		x += 29;
		btDC.setBounds(x, y, 25, 25);
		x += 35;
		btDS.setBounds(x, y, 25, 25);
		x += 35;
		btFine.setBounds(x, y, 30, 25);		
		x = 5; y = 35;
		btToCoda.setBounds(x, y, 50, 25);
		x += 55;
		btCodaLetter.setBounds(x, y, 35, 25);
		x += 40;
		btRepeatLine.setBounds(x, y, 63, 25);
		x = 5; y = 65;
		btDCCoda.setBounds(x, y, 80, 25);
		x += 80;
		btDCFine.setBounds(x, y, 80, 25);
		x = 5; y = 95;
		btDSCoda.setBounds(x, y, 80, 25);
		x += 80;
		btDSFine.setBounds(x, y, 80, 25);	
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getSource() == btCoda){	
			repeat = "coda";
			dispose();
		}else if(e.getSource() == btSegno){
			repeat = "segno";
			dispose();
		}else if(e.getSource() == btDC){
			setRepeat("dc");
			dispose();
		}else if(e.getSource() == btDS){
			setRepeat("ds");
			dispose();
		}else if(e.getSource() == btFine){
			setRepeat("fine");
			dispose();
		}else if(e.getSource() == btToCoda){
			setRepeat("toCoda");
			dispose();
		}else if(e.getSource() == btCodaLetter){
			setRepeat("codaLetter");
			dispose();
		}else if(e.getSource() == btDCCoda){
			setRepeat("dcCoda");
			dispose();
		}else if(e.getSource() == btDCFine){
			setRepeat("dcFine");
			dispose();
		}else if(e.getSource() == btDSCoda){
			setRepeat("dsCoda");
			dispose();
		}else if(e.getSource() == btDSFine){
			setRepeat("dsFine");
			dispose();
		}else if(e.getSource() == btRepeatLine){
			setRepeat("repeatLine");
			dispose();
		}
	}

	public String getRepeat() {
		return repeat;
	}

	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}


}
