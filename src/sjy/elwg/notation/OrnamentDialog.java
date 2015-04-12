package sjy.elwg.notation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

public class OrnamentDialog extends JDialog implements ActionListener{
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 2647829265264720382L;
	/**
	 * 窗口大小
	 */
	private static final int DIALOG_WIDTH = 100;
	private static final int DIALOG_HEIGHT = 135;
	
	/**
	 * 演奏符号类型
	 */
	private String symboleType = "none";
	
	ImageIcon iconMarcato = new ImageIcon(("pic/marcato.png"));
	ImageIcon iconMartellatoDown = new ImageIcon(("pic/martellatoDown.png"));
	ImageIcon iconMartellatoUp = new ImageIcon(("pic/martellatoUp.png"));
	ImageIcon iconStaccato = new ImageIcon(("pic/staccato.png"));
	ImageIcon iconTenuto = new ImageIcon(("pic/tenuto.png"));
	ImageIcon iconStaccatissimoDown = new ImageIcon(("pic/staccatissimoDown.png"));
	ImageIcon iconStaccatissimoUp = new ImageIcon(("pic/staccatissimoUp.png"));
	ImageIcon iconStaccatoTenutoUp = new ImageIcon(("pic/staccatoTenutoUp.png"));
	ImageIcon iconStaccatoTenutoDown = new ImageIcon(("pic/staccatoTenutoDown.png"));
	ImageIcon iconFermata = new ImageIcon(("pic/fermata.png"));
	ImageIcon iconPedalStart = new ImageIcon(("pic/pedalstart.png"));
	ImageIcon iconPedalEnd = new ImageIcon(("pic/pedalend.png"));
	ImageIcon iconBreath = new ImageIcon(("pic/breath.png"));
//	ImageIcon iconTremoloBeam1 = new ImageIcon(("pic/TremoloBeam1.png"));
//	ImageIcon iconTremoloBeam2 = new ImageIcon(("pic/TremoloBeam2.png"));
//	ImageIcon iconTremoloBeam3 = new ImageIcon(("pic/TremoloBeam3.png"));
	
	/**
	 * 按钮
	 */
	private JButton btMarcato = new JButton(iconMarcato);
	private JButton btMartellatoDown = new JButton(iconMartellatoDown);
	private JButton btMartellatoUp = new JButton(iconMartellatoUp);
	private JButton btStaccato = new JButton(iconStaccato);
	private JButton btTenuto = new JButton(iconTenuto);
	private JButton btStaccatissimoDown = new JButton(iconStaccatissimoDown);
	private JButton btStaccatissimoUp = new JButton(iconStaccatissimoUp);
	private JButton btStaccatoTenutoUp = new JButton(iconStaccatoTenutoUp);
	private JButton btStaccatoTenutoDown = new JButton(iconStaccatoTenutoDown);
	private JButton btFermata = new JButton(iconFermata);
	private JButton btPedalStart = new JButton(iconPedalStart);
	private JButton btPedalEnd = new JButton(iconPedalEnd);
	private JButton btBreath = new JButton(iconBreath);
//	private JButton btTremoloBeam1 = new JButton(iconTremoloBeam1);
//	private JButton btTremoloBeam2 = new JButton(iconTremoloBeam2);
//	private JButton btTremoloBeam3 = new JButton(iconTremoloBeam3);
	
	
	public OrnamentDialog(){
		super();
		setLayout(null);
		setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
		initComponents();	
		setModal(true);
		addFunctionListener(this);
	}
	

	void addFunctionListener(ActionListener l) {
		// TODO Auto-generated method stub
		btMarcato.addActionListener(l);
		btMartellatoDown.addActionListener(l);
		btMartellatoUp.addActionListener(l);
		btStaccato.addActionListener(l);
		btTenuto.addActionListener(l);
		btStaccatissimoDown.addActionListener(l);
		btStaccatissimoUp.addActionListener(l);
		btStaccatoTenutoUp.addActionListener(l);
		btStaccatoTenutoDown.addActionListener(l);
		btFermata.addActionListener(l);
		btPedalStart.addActionListener(l);
		btPedalEnd.addActionListener(l);
		btBreath.addActionListener(l);
//		btTremoloBeam1.addActionListener(l);
//		btTremoloBeam2.addActionListener(l);
//		btTremoloBeam3.addActionListener(l);
	}

	/**
	 * 初始化按钮
	 */
	public void initComponents(){
	
		setLayout(null);
		add(btMarcato);
		add(btMartellatoDown);
		add(btMartellatoUp);
		add(btStaccato);
		add(btTenuto);
		add(btStaccatissimoDown);
		add(btStaccatissimoUp);
		add(btStaccatoTenutoUp);
		add(btStaccatoTenutoDown);
		add(btFermata);
		add(btPedalStart);
		add(btPedalEnd);
		add(btBreath);
//		add(btTremoloBeam1);
//		add(btTremoloBeam2);
//		add(btTremoloBeam3);
		
		int x = 8; int y = 5;
		btMarcato.setBounds(x, y, 20, 25);
		x += 20;
		btMartellatoDown.setBounds(x, y, 20, 25);
		x += 20;
		btMartellatoUp.setBounds(x, y, 20, 25);
		x += 20;
		btStaccato.setBounds(x, y, 20, 25);
		x += 20;
		btTenuto.setBounds(x, y, 20, 25);
		x = 8; y += 30;
		btStaccatissimoDown.setBounds(x, y, 20, 25);
		x += 20;
		btStaccatissimoUp.setBounds(x, y, 20, 25);
		x += 20;
		btStaccatoTenutoUp.setBounds(x, y, 20, 25);
		x += 20;
		btStaccatoTenutoDown.setBounds(x, y, 20, 25);
		x += 20;
		btFermata.setBounds(x, y, 20, 25);
		x = 8; y+= 30;
		btPedalStart.setBounds(x, y, 20, 25);
		x += 20;
		btPedalEnd.setBounds(x, y, 20, 25);
		x += 20;
		btBreath.setBounds(x, y, 20, 25);	
		x += 20;
//		btTremoloBeam1.setBounds(x, y, 20, 25);	
//		x += 20;
//		btTremoloBeam2.setBounds(x, y, 20, 25);	
//		x = 8; y+= 30;
//		btTremoloBeam3.setBounds(x, y, 20, 25);
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getSource() == btMarcato){	
			symboleType = "accent";
			dispose();
		}else if(e.getSource() == btMartellatoDown){
			symboleType = "strongAccentDown";
			dispose();
		}else if(e.getSource() == btMartellatoUp){
			symboleType = "strongAccentUp";
			dispose();
		}else if(e.getSource() == btStaccato){
			symboleType = "staccato";
			dispose();
		}else if(e.getSource() == btTenuto){
			symboleType = "tenuto";
			dispose();
		}else if(e.getSource() == btStaccatissimoDown){
			symboleType = "staccatissimoDown";
			dispose();
		}else if(e.getSource() == btStaccatissimoUp){
			symboleType = "staccatissimoUp";
			dispose();
		}else if(e.getSource() == btStaccatoTenutoUp){
			symboleType = "staccatoTenutoUp";
			dispose();
		}else if(e.getSource() == btStaccatoTenutoDown){
			symboleType = "staccatoTenutoDown";
			dispose();
		}else if(e.getSource() == btFermata){
			symboleType = "fermata";
			dispose();
		}else if(e.getSource() == btPedalStart){
			symboleType = "pedalStart";
			dispose();
		}else if(e.getSource() == btPedalEnd){
			symboleType = "pedalEnd";
			dispose();
		}else if(e.getSource() == btBreath){
			symboleType = "breath";
			dispose();
		}
//		else if(e.getSource() == btTremoloBeam1){
//			type = "tremoloBeam1";
//			dispose();
//		}else if(e.getSource() == btTremoloBeam2){
//			type = "tremoloBeam2";
//			dispose();
//		}else if(e.getSource() == btTremoloBeam3){
//			type = "tremoloBeam3";
//			dispose();
//		}
	}


	public String getSymbolType() {
		return symboleType;
	}

	public void setType(String type) {
		this.symboleType = type;
	}

}
