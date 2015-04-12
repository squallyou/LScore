package sjy.elwg.notation;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class InstrumentDialog extends JDialog implements ActionListener{
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 8390190872587846802L;
	/**
	 * 窗口宽度与高度
	 */
	public static final int DIALOG_WIDTH = 320;
	public static final int DIALOG_HEIGHT = 300;
	public static final int LIST_WIDTH = 120;
	public static final int LIST_HEIGHT = 180;
	
	/**
	 * 确定取消按钮
	 */
	private JButton btYes;
	private JButton btNo;
	
	/**
	 * 乐器列表
	 */
	private JList instrumentList;
	
	/**
	 * 乐器名称与缩写的输入框
	 */
	private JTextField fname;
	private JTextField fabbre;
	
	/**
	 * 乐器名称与缩写的说明
	 */
	private JLabel lname;
	private JLabel labbre;
	
	/**
	 * 用户点击确定还是取消
	 */
	private boolean yes = false;
	
	
	/**
	 * 构造函数
	 * @param o 由外界所传递进来的对象
	 */
	public InstrumentDialog(){
		super();
		setModal(true);
		setLayout(null);
		setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
		setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
		initComponents();
		setVisible(true);
	}
	
	/**
	 * 初始化组件
	 */
	public void initComponents(){
		btYes = new JButton("确定");
		btNo = new JButton("取消");
		btYes.setSize(60, 20);
		btYes.setPreferredSize(new Dimension(60, 20));
		btNo.setSize(60, 20);
		btNo.setPreferredSize(new Dimension(60, 20));
		btYes.addActionListener(this);
		btNo.addActionListener(this);
		
		String[] data = {"钢琴", "贝斯", "小提琴", "中提琴", "大提琴", "吉他", "鼓"};
		instrumentList = new JList(data);
		instrumentList.setSelectedIndex(0);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView(instrumentList);
		instrumentList.setPreferredSize(new Dimension(LIST_WIDTH, LIST_HEIGHT));
		scrollPane.setSize(LIST_WIDTH + 15, LIST_HEIGHT + 15);
		scrollPane.setPreferredSize(new Dimension(LIST_WIDTH + 15, LIST_HEIGHT + 15));
		
		fname = new JTextField();
		fabbre = new JTextField();
		lname = new JLabel("名称");
		labbre = new JLabel("缩写");
		fname.setSize(LIST_WIDTH - 40, 15);
		fname.setPreferredSize(new Dimension(LIST_WIDTH - 40, 15));
		fabbre.setSize(LIST_WIDTH - 40, 15);
		fabbre.setPreferredSize(new Dimension(LIST_WIDTH - 40, 15));
		lname.setSize(30, 15);
		lname.setPreferredSize(new Dimension(30, 15));
		labbre.setSize(30, 15);
		labbre.setPreferredSize(new Dimension(30, 15));
		
		getContentPane().add(btYes);
		getContentPane().add(btNo);
		getContentPane().add(scrollPane);
		getContentPane().add(fname);
		getContentPane().add(fabbre);
		getContentPane().add(lname);
		getContentPane().add(labbre);
		
		scrollPane.setLocation(10, 10);
		lname.setLocation(scrollPane.getX() + scrollPane.getWidth() + 10, DIALOG_HEIGHT/2);
		labbre.setLocation(lname.getX(), lname.getY() + lname.getHeight()+ 5);
		fname.setLocation(lname.getX() + lname.getWidth() + 5, lname.getY());
		fabbre.setLocation(fname.getX(), labbre.getY());
		btYes.setLocation(DIALOG_WIDTH/3, DIALOG_HEIGHT - 70);
		btNo.setLocation(btYes.getX() + btYes.getWidth() + 30, btYes.getY());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btYes){
			yes = true;
			dispose();
		}
		else if(e.getSource() == btNo){
			dispose();
		}
	}
	
	/**
	 * 返回用户所选的参数
	 * 如果用户点击取消，则返回null
	 * @return
	 */
	public HashMap<String, String> getParameters(){
		if(yes){
			HashMap<String, String> result = new HashMap<String, String>();
			result.put("instrument", (String)getSelectedInstrument());
			result.put("name", fname.getText());
			result.put("abbre", fabbre.getText());
			return result;
		}
		else 
			return null;
	}
	
	/**
	 * 返回所选择的乐器
	 * @return
	 */
	private String getSelectedInstrument(){
		String result = null;
		String name = (String)instrumentList.getSelectedValue();
		if(name.equals("钢琴"))
			result = "1";
		else if(name.equals("贝斯"))
			result = "33";
		else if(name.equals("小提琴"))
			result = "41";
		else if(name.equals("中提琴"))
			result = "42";
		else if(name.equals("大提琴"))
			result = "43";
		else if(name.equals("吉他"))
			result = "25";
		else if(name.equals("鼓"))
			result = "117";
		
		return result;
	}

}
