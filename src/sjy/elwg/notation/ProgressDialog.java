package sjy.elwg.notation;

import java.awt.Rectangle;

import javax.swing.JDialog;
import javax.swing.JProgressBar;

public class ProgressDialog extends JDialog{
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 9114669846195416260L;
	/**
	 * 进度条
	 */
	private JProgressBar progressBar;
	
	/**
	 * 空构造函数
	 */
	public ProgressDialog(){
		super();
		progressBar = new JProgressBar();
		add(progressBar);
		
		getContentPane().setLayout(null);
		setSize(500, 50);
		progressBar.setSize(500, 10);
		progressBar.setLocation(0, 0);
		
		setAlwaysOnTop(true);
		setVisible(true);
	}
	
	/**
	 * 构造函数
	 * @param min 最小值
	 * @param max 最大值
	 */
	public ProgressDialog(int min, int max){
		super();
		progressBar = new JProgressBar(min, max);
		add(progressBar);
		
		getContentPane().setLayout(null);
		setSize(500, 55);
		progressBar.setSize(500, 10);
		progressBar.setLocation(0, 0);
		
		setAlwaysOnTop(true);
		setVisible(true);
	}
	
	/**
	 * 设置当前值
	 * @param n
	 */
	public void setValue(int n){
		progressBar.setValue(n);
	}
	
	/**
	 * 获得当前值
	 * @return
	 */
	public int getValue(){
		return progressBar.getValue();
	}
	
	/**
	 * 获得最小值
	 * @return
	 */
	public int getMin(){
		return progressBar.getMinimum();
	}
	
	/**
	 * 设置最小值
	 * @param n
	 */
	public void setMin(int n){
		progressBar.setMinimum(n);
	}
	
	/**
	 * 获得最大值
	 * @return
	 */
	public int getMax(){
		return progressBar.getMaximum();
	}
	
	/**
	 * 设置最小值
	 * @param n
	 */
	public void setMax(int n){
		progressBar.setMaximum(n);
	}
	
	public void refresh(){
		int x = progressBar.getWidth();
		int y = progressBar.getHeight();
		progressBar.paintImmediately(new Rectangle(x, y));
		//progressBar.updateUI();
	}

}
