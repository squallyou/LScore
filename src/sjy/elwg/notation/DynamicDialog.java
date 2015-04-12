package sjy.elwg.notation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

public class DynamicDialog extends JDialog implements ActionListener {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -5905237857981559759L;

	/**
	 * 力度记号面板，包含的记号有f,ff,fff,ffff,p,pp,ppp,pppp,fp,fz
	 * mf,mp,sf,sffz,sfz,subitoP;
	 * 
	 */
	private String type = "none";
	
	/**
	 * 窗口大小
	 */
	private static final int DIALOG_WIDTH = 162;
	private static final int DIALOG_HEIGHT = 169;
	
	ImageIcon btP = new ImageIcon(("pic/p.png"));
	ImageIcon btPp = new ImageIcon(("pic/pp.png"));
	ImageIcon btPpp = new ImageIcon(("pic/ppp.png"));
	ImageIcon btPppp = new ImageIcon(("pic/pppp.png"));
	ImageIcon btF = new ImageIcon(("pic/f.png"));
	ImageIcon btFf = new ImageIcon(("pic/ff.png"));
	ImageIcon btFff = new ImageIcon(("pic/fff.png"));
	ImageIcon btFfff = new ImageIcon(("pic/ffff.png"));
	ImageIcon btMf = new ImageIcon(("pic/mf.png"));
	ImageIcon btMp = new ImageIcon(("pic/mp.png"));
	ImageIcon btFp = new ImageIcon(("pic/fp.png"));
	ImageIcon btFz = new ImageIcon(("pic/fz.png"));
	ImageIcon btSf = new ImageIcon(("pic/sf.png"));
	ImageIcon btSfz = new ImageIcon(("pic/sfz.png"));
	ImageIcon btSffz = new ImageIcon(("pic/sffz.png"));
	ImageIcon btSubitoP = new ImageIcon(("pic/subitoP.png"));
	
	/**
	 * 按钮
	 */
	private JButton p = new JButton(btP);
	private JButton pp = new JButton(btPp);
	private JButton ppp = new JButton(btPpp);
	private JButton pppp = new JButton(btPppp);
	private JButton f = new JButton(btF);
	private JButton ff = new JButton(btFf);
	private JButton fff = new JButton(btFff);
	private JButton ffff = new JButton(btFfff);
	private JButton mf = new JButton(btMf);
	private JButton mp = new JButton(btMp);
	private JButton fp = new JButton(btFp);
	private JButton fz = new JButton(btFz);
	private JButton sf = new JButton(btSf);
	private JButton sfz = new JButton(btSfz);
	private JButton sffz = new JButton(btSffz);
	private JButton subitoP = new JButton(btSubitoP);
	
	
	
	
	/**
	 * 构造函数
	 */
	public DynamicDialog(){
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
		p.addActionListener(l);
		pp.addActionListener(l);
		ppp.addActionListener(l);
		pppp.addActionListener(l);
		f.addActionListener(l);
		ff.addActionListener(l);
		fff.addActionListener(l);
		ffff.addActionListener(l);
		mf.addActionListener(l);
		mp.addActionListener(l);
		fp.addActionListener(l);
		fz.addActionListener(l);
		sf.addActionListener(l);
		sfz.addActionListener(l);
		sffz.addActionListener(l);
		subitoP.addActionListener(l);
	}

	/**
	 * 初始化按钮
	 */
	public void initComponents(){
	
		setLayout(null);
		add(p);
		add(pp);
		add(ppp);
		add(pppp);
		add(f);
		add(ff);
		add(fff);
		add(ffff);
		add(mf);
		add(mp);
		add(fp);
		add(fz);
		add(sf);
		add(sfz);
		add(sffz);
		add(subitoP);

		
		int x = 5; int y = 5;
		p.setBounds(x, y, 17, 30);
		x += 17;
		pp.setBounds(x, y, 30, 30);
		x += 30;
		ppp.setBounds(x, y, 40, 30);
		x += 40;
		pppp.setBounds(x, y, 50, 30);
		x = 5; y += 30;
		f.setBounds(x, y, 17, 30);
		x += 17;
		ff.setBounds(x, y, 30, 30);
		x += 30;
		fff.setBounds(x, y, 40, 30);
		x += 40;
		ffff.setBounds(x, y, 50, 30);
		x = 5; y += 30;
		mf.setBounds(x, y, 30, 30);
		x += 30;
		mp.setBounds(x, y, 30, 30);
		x += 30;
		fp.setBounds(x, y, 26, 30);
		x += 26;
		fz.setBounds(x, y, 26, 30);
		x += 26;
		sf.setBounds(x, y, 25, 30);
		x = 5; y += 30;
		sfz.setBounds(x, y, 33, 30);
		x += 33;
		sffz.setBounds(x, y, 48, 30);
		x += 48;
		subitoP.setBounds(x, y, 56, 30);
	
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getSource() == p){	
			type = "p";
			dispose();
		}else if(e.getSource() == pp){
			type = "pp";
			dispose();
		}else if(e.getSource() == ppp){
			type = "ppp";
			dispose();
		}else if(e.getSource() == pppp){
			type = "pppp";
			dispose();
		}else if(e.getSource() == f){
			type = "f";
			dispose();
		}else if(e.getSource() == ff){
			type = "ff";
			dispose();
		}else if(e.getSource() == fff){
			type = "fff";
			dispose();
		}
		else if(e.getSource() == ffff){
			type = "ffff";
			dispose();
		}
		else if(e.getSource() == mp){
			type = "mp";
			dispose();
		}
		else if(e.getSource() == mf){
			type = "mf";
			dispose();
		}
		else if(e.getSource() == fp){
			type = "fp";
			dispose();
		}
		
		else if(e.getSource() == fz){
			type = "fz";
			dispose();
		}
		else if(e.getSource() == sf){
			type = "sf";
			dispose();
		}
		else if(e.getSource() == sffz){
			type = "sffz";
			dispose();
		}
		else if(e.getSource() == sfz){
			type = "sfz";
			dispose();
		}
		else if(e.getSource() == subitoP){
			type = "subitoP";
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
