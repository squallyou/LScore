package sjy.elwg.notation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

public class PerformanceDialog extends JDialog implements ActionListener {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -8849023450204487770L;

	/**
	 * 表情术语面板，包含的记号有affetuoso热情,agitato激情地,animato活波快速地,brillante华丽、灿烂地,con brio朝气蓬勃,cantabile如歌地,deciso果断地,dolce柔美、柔和地,dolento哀怨、悲伤地,
	 * espressivo表情丰富地,energico精力充沛地,furioso狂怒地,giocoso愉快嬉戏地,grave庄重地,grazioso优雅、优美地,maestoso庄严、宏伟地,
	 * misterioso神秘地,passionato热情洋溢地,sostenuto持续地,con sporito热烈有精神,tranquillo安静地
	 * 
	 */
	private String type = "none";
	
	/**
	 * 按钮宽高
	 */
	private static int WIDTH = 70;
	private static int HEIGHT = 25;
	
	
	/**
	 * 窗口大小
	 */
	private static final int DIALOG_WIDTH = 545;
	private static final int DIALOG_HEIGHT = 135;
	
	ImageIcon iconAffetuoso = new ImageIcon(("pic/affetuoso.png"));
	ImageIcon iconAgitato = new ImageIcon(("pic/agitato.png"));
	ImageIcon iconAnimato = new ImageIcon(("pic/animato.png"));
	ImageIcon iconBrillante = new ImageIcon(("pic/brillante.png"));
	ImageIcon iconConBrio = new ImageIcon(("pic/conBrio.png"));
	ImageIcon iconCantabile = new ImageIcon(("pic/cantabile.png"));
	ImageIcon iconDeciso = new ImageIcon(("pic/deciso.png"));
	ImageIcon iconDolce = new ImageIcon(("pic/dolce.png"));
	ImageIcon iconDolento = new ImageIcon(("pic/dolento.png"));
	ImageIcon iconEspressivo = new ImageIcon(("pic/espressivo.png"));
	ImageIcon iconEnergico = new ImageIcon(("pic/energico.png"));
	ImageIcon iconFurioso = new ImageIcon(("pic/furioso.png"));
	ImageIcon iconGiocoso = new ImageIcon(("pic/giocoso.png"));
	ImageIcon iconGrave = new ImageIcon(("pic/grave.png"));
	ImageIcon iconGrazioso = new ImageIcon(("pic/grazioso.png"));
	ImageIcon iconMaestoso = new ImageIcon(("pic/maestoso.png"));
	ImageIcon iconMisterioso = new ImageIcon(("pic/misterioso.png"));
	ImageIcon iconPassionato = new ImageIcon(("pic/passionato.png"));
	ImageIcon iconSostenuto = new ImageIcon(("pic/sostenuto.png"));
	ImageIcon iconConSpirito = new ImageIcon(("pic/conSpirito.png"));
	ImageIcon iconTranquillo = new ImageIcon(("pic/tranquillo.png"));
	
	/**
	 * 按钮
	 */
	private JButton btAffetuoso = new JButton(iconAffetuoso);
	private JButton btAgitato = new JButton(iconAgitato);
	private JButton btAnimato = new JButton(iconAnimato);
	private JButton btBrillante = new JButton(iconBrillante);
	private JButton btConBrio = new JButton(iconConBrio);
	private JButton btCantabile = new JButton(iconCantabile);
	private JButton btDeciso = new JButton(iconDeciso);
	private JButton btDolce = new JButton(iconDolce);
	private JButton btDolento = new JButton(iconDolento);
	private JButton btEspressivo = new JButton(iconEspressivo);
	private JButton btEnergico = new JButton(iconEnergico);
	private JButton btFurioso = new JButton(iconFurioso);
	private JButton btGiocoso = new JButton(iconGiocoso);
	private JButton btGrave = new JButton(iconGrave);
	private JButton btGrazioso = new JButton(iconGrazioso);
	private JButton btMaestoso = new JButton(iconMaestoso);
	private JButton btMisterioso = new JButton(iconMisterioso);
	private JButton btPassionato = new JButton(iconPassionato);
	private JButton btSostenuto = new JButton(iconSostenuto);
	private JButton btConSpirito = new JButton(iconConSpirito);
	private JButton btTranquillo = new JButton(iconTranquillo);
	
	
	
	
	
	/**
	 * 构造函数
	 */
	public PerformanceDialog(){
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
		btAffetuoso.addActionListener(l);
		btAgitato.addActionListener(l);
		btAnimato.addActionListener(l);
		btBrillante.addActionListener(l);
		btConBrio.addActionListener(l);
		btCantabile.addActionListener(l);
		btDeciso.addActionListener(l);
		btDolce.addActionListener(l);
		btDolento.addActionListener(l);
		btEspressivo.addActionListener(l);
		btEnergico.addActionListener(l);
		btFurioso.addActionListener(l);
		btGiocoso.addActionListener(l);
		btGrave.addActionListener(l);
		btGrazioso.addActionListener(l);
		btMaestoso.addActionListener(l);
		btMisterioso.addActionListener(l);
		btPassionato.addActionListener(l);
		btSostenuto.addActionListener(l);
		btConSpirito.addActionListener(l);
		btTranquillo.addActionListener(l);
	}

	/**
	 * 初始化按钮
	 */
	public void initComponents(){
	
		setLayout(null);
		add(btAffetuoso);
		add(btAgitato);
		add(btAnimato);
		add(btBrillante);
		add(btConBrio);
		add(btCantabile);
		add(btDeciso);
		add(btDolce);
		add(btDolento);
		add(btEspressivo);
		add(btEnergico);
		add(btFurioso);
		add(btGiocoso);
		add(btGrave);
		add(btGrazioso);
		add(btMaestoso);
		add(btMisterioso);
		add(btPassionato);
		add(btSostenuto);
		add(btConSpirito);
		add(btTranquillo);

		
		int x = 5; int y = 5;
		btAffetuoso.setBounds(x, y, WIDTH, HEIGHT);
		x += 75;
		btAgitato.setBounds(x, y, WIDTH, HEIGHT);
		x += 75;
		btAnimato.setBounds(x, y, WIDTH, HEIGHT);
		x += 75;
		btBrillante.setBounds(x, y, WIDTH, HEIGHT);
		x += 75;
		btConBrio.setBounds(x, y, WIDTH, HEIGHT);
		x += 75;
		btCantabile.setBounds(x, y,WIDTH, HEIGHT);
		x += 75;
		btDeciso.setBounds(x, y, WIDTH, HEIGHT);
		x = 5; y += 30;
		btDolce.setBounds(x, y,WIDTH, HEIGHT);
		x += 75;
		btDolento.setBounds(x, y,WIDTH, HEIGHT);
		x += 75;
		btEspressivo.setBounds(x, y, WIDTH, HEIGHT);
		x += 75;
		btEnergico.setBounds(x, y,WIDTH, HEIGHT);
		x += 75;
		btFurioso.setBounds(x, y, WIDTH, HEIGHT);
		x += 75;
		btGiocoso.setBounds(x, y, WIDTH, HEIGHT);
		x += 75;
		btGrave.setBounds(x, y, WIDTH, HEIGHT);
		x = 5; y += 30;
		btGrazioso.setBounds(x, y,WIDTH, HEIGHT);
		x += 75;
		btMaestoso.setBounds(x, y, WIDTH, HEIGHT);
		x += 75;
		btMisterioso.setBounds(x, y, WIDTH, HEIGHT);
		x += 75;
		btPassionato.setBounds(x, y,WIDTH, HEIGHT);
		x += 75;
		btSostenuto.setBounds(x, y, WIDTH, HEIGHT);
		x += 75;
		btConSpirito.setBounds(x, y, WIDTH, HEIGHT);
		x += 75;
		btTranquillo.setBounds(x, y,WIDTH, HEIGHT);
	
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getSource() == btAffetuoso){	
			type = "affetuoso";
			dispose();
		}else if(e.getSource() == btAgitato){
			type = "agitato";
			dispose();
		}else if(e.getSource() == btAnimato){
			type = "animato";
			dispose();
		}else if(e.getSource() == btBrillante){
			type = "brillante";
			dispose();
		}else if(e.getSource() == btConBrio){
			type = "conBrio";
			dispose();
		}else if(e.getSource() == btCantabile){
			type = "cantabile";
			dispose();
		}else if(e.getSource() == btDeciso){
			type = "deciso";
			dispose();
		}
		else if(e.getSource() == btDolce){
			type = "dolce";
			dispose();
		}
		else if(e.getSource() == btDolento){
			type = "dolento";
			dispose();
		}
		else if(e.getSource() == btEspressivo){
			type = "esporessivo";
			dispose();
		}
		else if(e.getSource() == btEnergico){
			type = "energico";
			dispose();
		}
		
		else if(e.getSource() == btFurioso){
			type = "furioso";
			dispose();
		}
		else if(e.getSource() == btGiocoso){
			type = "giocoso";
			dispose();
		}
		else if(e.getSource() == btGrave){
			type = "grave";
			dispose();
		}
		else if(e.getSource() == btGrazioso){
			type = "grazioso";
			dispose();
		}
		else if(e.getSource() == btMaestoso){
			type = "maestoso";
			dispose();
		}
		else if(e.getSource() == btMisterioso){
			type = "misterioso";
			dispose();
		}
		else if(e.getSource() == btPassionato){
			type = "passionato";
			dispose();
		}
		else if(e.getSource() == btSostenuto){
			type = "sostenuto";
			dispose();
		}
		else if(e.getSource() == btConSpirito){
			type = "conSpirito";
			dispose();
		}
		else if(e.getSource() == btTranquillo){
			type = "tranquillo";
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
