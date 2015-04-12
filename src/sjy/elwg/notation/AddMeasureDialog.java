package sjy.elwg.notation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class AddMeasureDialog extends JDialog implements ActionListener{

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -2796002169639595637L;
	
	/**
	 * 窗口大小
	 */
	private static final int DIALOG_WIDTH = 200;
	private static final int DIALOG_HEIGHT = 120;
	/**
	 * 按钮大小
	 */
	private static final int BT_WIDTH = 45;
	private static final int BT_HEIGHT = 22;
	
	/**
	 * 按钮图标
	 */
	ImageIcon iconConfirm = new ImageIcon(("pic/confirm.png"));
	ImageIcon iconCancel = new ImageIcon(("pic/cancel.png"));
	
	/**
	 * 确定与取消按钮
	 */
	private JButton btYes = new JButton(iconConfirm);
	private JButton btCancle = new JButton(iconCancel);
	
	/**
	 * 用户填写栏
	 */
	private JTextField textField = new JTextField("3");
	
	private int measureNum = -1;
	
	
	/**
	 * 构造函数
	 */
	public AddMeasureDialog(){
		super();
		setLayout(null);
		setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
		initComponents();
		setModal(true);
		setVisible(true);
	}
	
	/**
	 * 初始化组件
	 */
	public void initComponents(){
		JLabel lb = new JLabel("小节个数:");
		lb.setSize(60, 20);
		textField.setSize(30, 20);
		btYes.setSize(BT_WIDTH, BT_HEIGHT);
		btCancle.setSize(BT_WIDTH, BT_HEIGHT);
		
		lb.setLocation(50, 10);
		textField.setLocation(lb.getX() + lb.getWidth(), lb.getY());
		btYes.setLocation(getWidth()/5, 50);
		btCancle.setLocation(btYes.getX() + btYes.getWidth() + 20, btYes.getY());
		
		add(lb);
		add(btYes);
		add(btCancle);
		add(textField);
		
		btYes.addActionListener(this);
		btCancle.addActionListener(this);
	}

	/**
	 * 获得添加的小节个数
	 * @return
	 */
	public int getMeasureNum() {
		return measureNum;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//点击确定
		if(e.getSource() == btYes){
			int num = -1;
			try{
				num = Integer.parseInt(textField.getText());
			}catch(NumberFormatException ex){
				ex.printStackTrace();
				measureNum = -1;
				dispose();
				return;
			}
			measureNum = num;
			dispose();
		}
		//点击取消
		else if(e.getSource() == btCancle){
			measureNum = -1;
			dispose();
		}
	}

}
