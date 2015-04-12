package sjy.elwg.notation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 * 添加房子记号的对话框，供用于选择房子记号的标号
 * @author tequila
 *
 */
public class RepeatEndingDialog extends JDialog implements ActionListener{
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1166202490561117504L;
	/**
	 * 窗口大小
	 */
	private static final int DIALOG_WIDTH = 167;
	private static final int DIALOG_HEIGHT = 140;
	
	/**
	 * 按钮宽度，高度
	 */
	private static final int BT_WIDTH = 20;
	private static final int BT_HEIGHT = 20;
	
	ImageIcon iconRepeatEnding1 = new ImageIcon(("pic/ending1.png"));
	ImageIcon iconRepeatEnding2 = new ImageIcon(("pic/ending2.png"));
	ImageIcon iconRepeatEnding3 = new ImageIcon(("pic/ending3.png"));
	ImageIcon iconRepeatEnding4 = new ImageIcon(("pic/ending4.png"));
	ImageIcon iconRepeatEndingAdded = new ImageIcon(("pic/addRepeatEnding.png"));
	
	/**
	 * 标号类型选择框
	 */
//	private JCheckBox checkBox2 = new JCheckBox("自定义标号");
	

	/**
	 * 常用标号按钮
	 */
	private JButton bt1 = new JButton(iconRepeatEnding1);
	private JButton bt2 = new JButton(iconRepeatEnding2);
	private JButton bt3 = new JButton(iconRepeatEnding3);
	private JButton bt4 = new JButton(iconRepeatEnding4);
	
	/**
	 * 添加按钮
	 */
	private JButton btAdd = new JButton("添加");
	
	private int number = 0;
	private int[] numbers = new int[3];
	
	/**
	 * 放置拍号的面板
	 */
	private JPanel panel1;
	private JPanel panel2;
	
	private JCheckBox checkBox;
	
	/**
	 *其他拍号输入框 
	 */
	private JTextField bt11 = new JTextField("0");
	private JTextField bt12 = new JTextField("0");
	private JTextField bt13 = new JTextField("0");
	
	/**
	 * 构造函数
	 */
	public RepeatEndingDialog(){
		super();
		setLayout(null);
		
		setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
		initComponents();
		setModal(true);
	}
	
	public void initComponents(){
		//灰色边框
		Border border = BorderFactory.createLineBorder(Color.GRAY,1);
		
		bt1.setSize(BT_WIDTH, BT_HEIGHT);
		bt2.setSize(BT_WIDTH, BT_HEIGHT);
		bt3.setSize(BT_WIDTH, BT_HEIGHT);
		bt4.setSize(BT_WIDTH, BT_HEIGHT);
		
		panel1 = new JPanel();
		panel1.setBounds(10, 5, 130, 35);
		panel1.setLayout(null);	
		panel1.setBorder(border);
		
		btAdd.setSize(BT_WIDTH, BT_HEIGHT);
			
		int x = 10;
		int y = 8; 
		bt1.setLocation(x, y);
		x += BT_WIDTH + 10;
		bt2.setLocation(x, y);
		x += BT_WIDTH + 10;
		bt3.setLocation(x, y);
		x += BT_WIDTH + 10;
		bt4.setLocation(x, y);
				
		ButtonGroup bg = new ButtonGroup();
		
		bg.add(bt1);
		bg.add(bt2);
		bg.add(bt3);
		bg.add(bt4);
		
		panel1.add(bt1);
		panel1.add(bt2);
		panel1.add(bt3);
		panel1.add(bt4);

		add(panel1);
		
		panel2 = new JPanel(){};
		
		JPanel panelPlus1 = new JPanel(){
			public void paintComponent(Graphics gg){
				Graphics2D g = (Graphics2D) gg;	
				RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
				renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
				g.setRenderingHints(renderHints);
//				g.setFont(NoteCanvas.MCORE_FONT.deriveFont(25f));
				g.drawString("+", 5, 13);
			//	g.drawString("+", 56, 38);
	       
				g.setColor(Color.gray);

			}
		};
		
		JPanel panelPlus2 = new JPanel(){
			public void paintComponent(Graphics gg){
				Graphics2D g = (Graphics2D) gg;	
				RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
				renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
				g.setRenderingHints(renderHints);
//				g.setFont(NoteCanvas.MCORE_FONT.deriveFont(25f));
				g.drawString("+", 5, 13);
			//	g.drawString("+", 56, 38);
	       
				g.setColor(Color.gray);
				}
		};
				
		panel2.setBounds(10, 45, 130, 50);
		panel2.setLayout(null);
		panel2.setBorder(border);
		
		checkBox = new JCheckBox("自定义标号");
		
		checkBox.setBounds(5, 1, 100, 20);
	
		
		bt11.setBounds(5, 25, 20, 20);
		bt12.setBounds(35, 25, 20, 20);
		bt13.setBounds(65, 25, 20, 20);
		
		bt11.setEnabled(false);
		bt12.setEnabled(false);
		bt13.setEnabled(false);
		
		panelPlus1.setBounds(21, 26, 20, 20);
		panelPlus2.setBounds(51, 26, 20, 20);
		
		
		btAdd = new JButton(iconRepeatEndingAdded);
		btAdd.setEnabled(false);
		
		btAdd.setBounds(95, 25, 30, 20);
		
		panel2.add(bt11);
		panel2.add(bt12);
		panel2.add(bt13);
		panel2.add(btAdd);
		
		panel2.add(checkBox);
		panel2.add(panelPlus1);
		panel2.add(panelPlus2);
		
		add(panel2);
		
		
		checkBox.addActionListener(this);
		bt1.addActionListener(this);
		bt2.addActionListener(this);
		bt3.addActionListener(this);
		bt4.addActionListener(this);
		btAdd.addActionListener(this);
		
		
//		checkBox = new JCheckBox("自定义标号"){
//			/**
//			 * 序列号
//			 */
//			private static final long serialVersionUID = 7205141718902949379L;
//
////			public void paintComponent(Graphics gg){
////				Graphics2D g = (Graphics2D) gg;	
////				RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
////		        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
////		        g.setRenderingHints(renderHints);
////		        g.setFont(getFont().deriveFont(Font.BOLD, 15f));
////		        g.drawString("其它:", 6, 20);
////		        g.setFont(NoteCanvas.MCORE_FONT.deriveFont(30f));
////		        g.drawString("+", 73, 23);
////		        g.drawString("+", 108, 23);
////		     //   g.drawString("+", 113, 45);        
////		        g.setColor(Color.gray);
////		        g.drawLine(15,52,148,52);
////		        g.drawLine(15,53,148,53);
////			}
//		};
//		checkBox.setBounds(0, 60, 160, 50);
//		checkBox.setLayout(null);
//		
//		bt11.setBounds(50, 5, 20, 25);
//		bt12.setBounds(85, 5, 20, 25);
//		bt13.setBounds(120, 5, 20, 25);
//		
//		checkBox.add(bt11);
//		checkBox.add(bt12);
//		checkBox.add(bt13);
//		add(checkBox);
		
//		JPanel panelShow = new JPanel(){
//			/**
//			 * 序列号
//			 */
//			private static final long serialVersionUID = -2649203903295425124L;
//
//			public void paintComponent(Graphics gg){
//				Graphics2D g = (Graphics2D) gg;	
//				RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
//		        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
//		        g.setRenderingHints(renderHints);
//		        g.setFont(getFont().deriveFont(Font.BOLD, 15f));
//		        g.drawString("选择拍号:", 6, 15);
//
//			}
//		};
//		panelShow.setBounds(0, 0, 75, 20);
//		add(panelShow);
		
		
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getSource() == checkBox && checkBox.isSelected() == true){
			setCommonRepeatUnEnabled();
			setUnCommonRepeatEnabled();
		}else if(checkBox.isSelected() == true && e.getSource() == btAdd){
			numbers[0] = Integer.parseInt(bt11.getText());
			numbers[1] = Integer.parseInt(bt12.getText());
			numbers[2] = Integer.parseInt(bt13.getText());
			dispose();
		}else{
			setCommonRepeatEnabled();
			setUnCommonRepeatUnEnabled();
			if(e.getSource() == bt1){
				number = 1;
				dispose();
			}else if(e.getSource() == bt2){
				number = 2;
				dispose();
			}else if(e.getSource() == bt3){
				number = 3;
				dispose();
			}else if(e.getSource() == bt4){
				number = 4;
				dispose();
			}
		}
		
		
	}
	


	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int[] getNumbers() {
		return numbers;
	}

	public void setNumbers(int[] numbers) {
		this.numbers = numbers;
	}

	/**
	 * 设置普通标号可用
	 */
	public void setCommonRepeatEnabled(){
		bt1.setEnabled(true);
		bt2.setEnabled(true);
		bt3.setEnabled(true);
		bt4.setEnabled(true);
		
	}
	/**
	 * 设置普通标号不可用
	 */
	public void setCommonRepeatUnEnabled(){
		bt1.setEnabled(false);
		bt2.setEnabled(false);
		bt3.setEnabled(false);
		bt4.setEnabled(false);	
	}
	
	public void setUnCommonRepeatEnabled(){
		bt11.setEnabled(true);
		bt12.setEnabled(true);
		bt13.setEnabled(true);
		btAdd.setEnabled(true);
	}
	
	public void setUnCommonRepeatUnEnabled(){
		bt11.setEnabled(false);
		bt12.setEnabled(false);
		bt13.setEnabled(false);
		btAdd.setEnabled(false);
	}
	
	
}
