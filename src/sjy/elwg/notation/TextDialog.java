package sjy.elwg.notation;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;

/**
 * 文本选择窗口
 * @author jingyuan.sun
 *
 */
public class TextDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = -5523261703385735824L;
	
	/**
	 * 窗口大小
	 */
	private static final int DIALOG_WIDTH = 300;
	private static final int DIALOG_HEIGHT = 50;
	
	private JButton btLyric;
	private JButton btAnnt;
	private JButton btFree;
	
	/** 用户所选择的文本类型，有效值有：lyric, annotation, free */
	private static String textType = "none"; 
	
	private TextDialog(Frame frame){
		super();
		setModal(true);
		setLayout(null);
		setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
		initComponents();
		setVisible(true);
		addFunctionListener(this);
		setLocationRelativeTo(null);
	}
	
	/**
	 * 外部调用方法
	 * @param frame
	 * @param s
	 * @return
	 */
	public static String showDialog(Frame frame, String s){
		TextDialog td = new TextDialog(frame);
		td.setTitle(s);
		String type = textType;
		td.dispose();
		return type;
	}
	
	private void initComponents(){
		btLyric = new JButton("歌词");
		btAnnt = new JButton("标注");
		btFree = new JButton("自由文本");
		btLyric.setBackground(Color.WHITE);
		btAnnt.setBackground(Color.WHITE);
		btFree.setBackground(Color.WHITE);
		add(btAnnt);
		add(btFree);
		add(btLyric);
		int x = 10, y = 5;
		btLyric.setBounds(x, y, 60, 25);
		x += 70;
		btFree.setBounds(x, y, 90, 25);
		x += 100;
		btAnnt.setBounds(x, y, 60, 25);
		addFunctionListener(this);
	}
	
	public void addFunctionListener(ActionListener l){
		btLyric.addActionListener(l);
		btAnnt.addActionListener(l);
		btFree.addActionListener(l);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btAnnt){
			textType = "annotation";
			setVisible(false);
		}
		else if(e.getSource() == btLyric){
			textType = "lyric";
			setVisible(false);
		}
		else if(e.getSource() == btFree){
			textType = "free";
			setVisible(false);
		}
	}

}
