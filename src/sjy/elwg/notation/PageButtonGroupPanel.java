package sjy.elwg.notation;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollBar;

import sjy.elwg.notation.musicBeans.Page;

/**
 * 盛放与页码、翻页相关的按钮的面板
 * @author wenxi.lu
 *
 */
public class PageButtonGroupPanel extends JPanel implements MouseListener,AdjustmentListener{
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 21L;
	
	/**
	 * 当前被选择的页面按钮
	 */
	private PageButtonPanel currentPBP;
	/**
	 * 上一页按钮
	 */
	private PageTurningPanel leftPanel;
	/**
	 * 下一页按钮
	 */
	private PageTurningPanel rightPanel;
	/**
	 * 页码按钮,最多5个
	 */
	private ArrayList<PageButtonPanel> buttonList;
	/**
	 * 当前页面
	 */
	private int curPageIndex;
   
	/**
	 * 构造函数
	 */
	public PageButtonGroupPanel(){
		super();
		buttonList = new ArrayList<PageButtonPanel>();
		setSize(220, 20);
		initComponents();
		locateComponents();
		//setOpaque(false);
		setLayout(null);
	}
	
	/**
	 * 初始化组件
	 */
	public void initComponents(){
		PageButtonPanel pbp = new PageButtonPanel(1);
		pbp.setMode("selected");
		pbp.repaint();
		currentPBP = pbp;
		add(pbp);
		buttonList.add(pbp);
		pbp.addMouseListener(this);
		
		leftPanel = new PageTurningPanel("left");
		rightPanel = new PageTurningPanel("right");
		add(leftPanel);
		add(rightPanel);
		leftPanel.addMouseListener(this);
		rightPanel.addMouseListener(this);
	}
	
	/**
	 * 放置组件位置
	 */
	public void locateComponents(){
		int x = (getWidth() - getPageButtonWidth()) / 2; 
		int y = 1;
		leftPanel.setLocation(x, y);
		x += leftPanel.getWidth() + 5;
		for(int i = 0; i < buttonList.size(); i++){
			PageButtonPanel pbp = buttonList.get(i);
			pbp.setLocation(x, y);
			x += pbp.getWidth() + 5;
		}
		rightPanel.setLocation(x, y);
	}
	
	/**
	 * 添加页码按钮
	 */
	public void addPageButton(){
		int num = buttonList.size();
		if(num < 5){
			PageButtonPanel pbp = new PageButtonPanel(num+1);
			add(pbp);
			buttonList.add(pbp);
			pbp.addMouseListener(this);
			locateComponents();
		}
	}
	
	/**
	 * 删除页码按钮
	 */
	public void removePageButton(){
		int num = buttonList.size();
		PageButtonPanel pbp = buttonList.get(num-1);
		pbp.removeMouseListener(this);
		remove(pbp);
		buttonList.remove(pbp);
		locateComponents();
	}
	
	/**
	 * 根据导入乐谱的页数更新按钮
	 */
	public void refreshPageButton(int pageBefore,int pageNow){
		int pB = pageBefore;
		int pN = pageNow;
		int num = buttonList.size();		  
		BottomPanel bottomPanel = (BottomPanel)getParent();
		JScrollBar scrollBar = bottomPanel.getScrollPane().getVerticalScrollBar();
		scrollBar.setValue((Page.PAGE_HEIGHT + NoteCanvas.PAGE_GAP) * (0));
		if(pN <= 5 && pB < pN){
			int addNum = pN - pB;
			    for(int i = 1; i <= addNum; i++){
			    	PageButtonPanel pbp = new PageButtonPanel(pB+i);
					add(pbp);
					buttonList.add(pbp);
					pbp.addMouseListener(this);
					buttonList.get(0).setMode("Selected");
					buttonList.get(0).repaint();
					for(int i1 = 1, n = buttonList.size(); i1 < n; i1++){	
						buttonList.get(i1).setMode("normal");
						buttonList.get(i1).repaint();
					}
					locateComponents();	
			    }				
			}
		else if(pB <= 5 && pN < pB){
				int deleteNum = pB - pN;
				for(int i = 1; i <= deleteNum; i++){
				PageButtonPanel pbp = buttonList.get(num-i);
				pbp.removeMouseListener(this);
				remove(pbp);
				buttonList.remove(pbp);
				buttonList.get(0).setMode("Selected");
				buttonList.get(0).repaint();
				for(int i1 = 1, n = buttonList.size(); i1 < n; i1++){	
					buttonList.get(i1).setMode("normal");
					buttonList.get(i1).repaint();
				}
				locateComponents();	
				}		
			}	
		else if(pN > 5 && pB < 5){
			    currentPBP = new PageButtonPanel(0);
				int addNum = 5 - pB;
				for(int i = 1; i <= addNum; i++){
		    	PageButtonPanel pbp = new PageButtonPanel(pB+i);
				add(pbp);
				buttonList.add(pbp);
				pbp.addMouseListener(this);
				locateComponents();	
		    }				
		}
		else if(pB >=5 && pN <5){
				int deleteNum = 5 - pN;
				for(int i = 1; i <= deleteNum; i++){
				PageButtonPanel pbp = buttonList.get(num-i);
				pbp.removeMouseListener(this);
				remove(pbp);
				buttonList.remove(pbp);
				locateComponents();	
			}		
		}
	}
	
	/**
	 * 与翻页相关按钮所占的总宽度
	 * @return
	 */
	public int getPageButtonWidth(){
		int result = 0;
		result += leftPanel.getWidth() + rightPanel.getWidth() + 5;
		result += buttonList.size() * (buttonList.get(0).getWidth() + 5);
		return result;
	}
	
	public PageButtonPanel getCurrentPBP() {
		return currentPBP;
	}

	public void setCurrentPBP(PageButtonPanel currentPBP) {
		this.currentPBP = currentPBP;
	}

	public PageTurningPanel getLeftPanel() {
		return leftPanel;
	}

	public PageTurningPanel getRightPanel() {
		return rightPanel;
	}
	
	public int getCurPageIndex() {
		return curPageIndex;
	}

	public void setCurPageIndex(int curPageIndex) {
		this.curPageIndex = curPageIndex;
	}
	
	
	
 /********************************************************/
	

	/**
	 * 
	 * 用于快速换页的按钮类
	 * @author jingyuan.sun
	 * 
	 */
	public class PageButtonPanel extends JPanel{
		/**
		 * 
		 */
		private static final long serialVersionUID = 20L;
		/**
		 * 状态,有"selected", "entered", "normal"三个值
		 */
		private String mode = "none";
		/**
		 * 所指示的页数
		 */
		private int pageNum;
		
		public PageButtonPanel(int pageNum){
			super();
			this.pageNum = pageNum;
			setSize(20, 13);
			repaint();
		}
		
		public void paintComponent(Graphics gg){
			super.paintComponent(gg);
			Graphics2D g = (Graphics2D) gg;	
			RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
	        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
	        g.setRenderingHints(renderHints);
			if(mode.equalsIgnoreCase("selected")){
				//方框
				g.setColor(Color.GRAY);
				g.fillRect(0, 0, getWidth(), getHeight());
				g.setColor(Color.white);
				//数字
				String str = String.valueOf(pageNum);
				g.setFont(new Font("Arial", Font.PLAIN, 12));
				g.drawString(str, getWidth()/2-3, getHeight()-2);
			}
			else{
				if(mode.equalsIgnoreCase("entered"))
					g.setColor(Color.red);
				else
					g.setColor(Color.DARK_GRAY);
				//画矩形
				g.drawRect(0, 0, getWidth()-1, getHeight()-1);
				//数字
				String str = String.valueOf(pageNum);
				g.setFont(new Font("Arial", Font.PLAIN, 12));
				g.drawString(str, getWidth()/2-3, getHeight()-2);
			}
		}

		public String getMode() {
			return mode;
		}

		public void setMode(String mode) {
			this.mode = mode;
		}

		public int getPageNum() {
			return pageNum;
		}

		public void setPageNum(int pageNum) {
			this.pageNum = pageNum;
		}
	}
	
    /*******************************************************************/	
	
	/**
	 * 
	 * 用于切换页码范围的按钮类
	 * @author jingyuan.sun
	 * 
	 */
	public class PageTurningPanel extends JPanel{
		/**
		 * 序列号
		 */
		private static final long serialVersionUID = 22L;
		
		/**
		 * 箭头方法，有"left"和"right"两个值
		 */
		private String direction;
		
		private PageTurningPanel(String direction){
			super();
			this.direction = direction;
			setSize(35, 13);
			repaint();
			setOpaque(false);
			setLayout(null);
		}
		
		public void paintComponent(Graphics gg){
			super.paintComponent(gg);
			Graphics2D g = (Graphics2D) gg;	
			RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
	        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
	        g.setRenderingHints(renderHints);
	        g.setColor(Color.blue);
	        
	        if(direction.equalsIgnoreCase("left")){
	        	String str = "上一页";
				g.drawString(str, 0, getHeight()-2);
	        }
	        else if(direction.equalsIgnoreCase("right")){
	        	String str = "下一页";
				g.drawString(str, 0, getHeight()-2);
	        }
		}

		public String getDirection() {
			return direction;
		}

		public void setDirection(String direction) {
			this.direction = direction;
		}
		
	}

	/**************************************************************/
	
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		//页码选择按钮
		if(e.getSource() instanceof PageButtonPanel){
			PageButtonPanel pbp = (PageButtonPanel)e.getSource();
			if(!pbp.getMode().equalsIgnoreCase("selected")){
				pbp.setMode("entered");
				pbp.repaint();
			}
		}
		if(e.getSource() instanceof PageTurningPanel){
			PageTurningPanel pbp = (PageTurningPanel)e.getSource();
			pbp.setBackground(Color.gray);
			pbp.repaint();
		}
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		//页码选择按钮
		if(e.getSource() instanceof PageButtonPanel){
			PageButtonPanel pbp = (PageButtonPanel)e.getSource();
			if(!pbp.getMode().equalsIgnoreCase("selected")){
				pbp.setMode("normal");
				pbp.repaint();
			}
		}
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		//点击的是页码选择按钮
		if(e.getSource() instanceof PageButtonPanel){
			PageButtonPanel pbp = (PageButtonPanel)e.getSource();
			if(!pbp.getMode().equalsIgnoreCase("selected")){
				currentPBP.setMode("normal");
				currentPBP.repaint();
				currentPBP = pbp;
				pbp.setMode("selected");
				pbp.repaint();
			}
			BottomPanel bottomPanel = (BottomPanel)getParent();
			JScrollBar scrollBar = bottomPanel.getScrollPane().getVerticalScrollBar();
			scrollBar.setValue((Page.PAGE_HEIGHT + NoteCanvas.PAGE_GAP) * (pbp.getPageNum() - 1));
		}
		//点击的是左页码按钮
		else if(e.getSource() == leftPanel){
			BottomPanel bottomPanel = (BottomPanel)getParent();
			int pageNumAll = bottomPanel.getCanvas().getScore().getPageList().size();
			if(pageNumAll > 5){
				int lastPageNum = buttonList.get(2).getPageNum();
				if(lastPageNum > 3 && lastPageNum <= pageNumAll){
					for (int i = 0; i < 5; i++) {
						int num = lastPageNum - 3;
						buttonList.get(i).setPageNum(num + i);
						buttonList.get(i).repaint();
					}
				}
				else if(lastPageNum == 3 ){
					for (int i = 0; i < 5; i++) {
						buttonList.get(i).setPageNum(i + 1);
						buttonList.get(i).repaint();
					}
					if (buttonList.get(2).getMode() == "Selected") {
						buttonList.get(2).setMode("normal");
						buttonList.get(2).repaint();
						buttonList.get(1).setMode("Selected");
						buttonList.get(1).repaint();
					} else if (buttonList.get(1).getMode() == "Selected") {
						buttonList.get(1).setMode("normal");
						buttonList.get(1).repaint();
						buttonList.get(0).setMode("Selected");
						buttonList.get(0).repaint();
					}
				}
			}
			if(pageNumAll <= 5){
				int num = pageNumAll;
				for(int i = 0; i < pageNumAll; i++){
					buttonList.get(i).setPageNum(i+1);
					if(buttonList.get(i).getMode() == "Selected"){
						num = i;
					}			
				}
				if(num >= 1){
					if(buttonList.get(num).getMode() == "Selected"){
						buttonList.get(num).setMode("normal");
						buttonList.get(num).repaint();
						buttonList.get(num - 1).setMode("Selected");
						buttonList.get(num - 1).repaint();
					}
				}
			}
			JScrollBar scrollBar = bottomPanel.getScrollPane().getVerticalScrollBar();
			int lengthValue = scrollBar.getValue();
			scrollBar.setValue(lengthValue-(Page.PAGE_HEIGHT + NoteCanvas.PAGE_GAP) );
		}
		//点击的是右页码按钮
		else if(e.getSource() == rightPanel){
			BottomPanel bottomPanel = (BottomPanel)getParent();
			int pageNumAll = bottomPanel.getCanvas().getScore().getPageList().size();
			if(pageNumAll <= 5){
				int num = pageNumAll;
				for(int i = 0; i < pageNumAll; i++){
					buttonList.get(i).setPageNum(i+1);
					if(buttonList.get(i).getMode() == "Selected"){
						num = i;
					}			
				}
				if(num >= 1 && num < pageNumAll - 1){
					if(buttonList.get(num).getMode() == "Selected"){
						buttonList.get(num).setMode("normal");
						buttonList.get(num).repaint();
						buttonList.get(num + 1).setMode("Selected");
						buttonList.get(num + 1).repaint();
					}
				}
			}
			if (pageNumAll > 5) {
				int pageNum = buttonList.get(2).getPageNum();
				if (pageNum > 3) {
					if (pageNumAll - pageNum > 2) {
						for (int i = 0; i < 5; i++) {
							int num = pageNum - 1;
							buttonList.get(i).setPageNum(num + i);
							buttonList.get(i).repaint();
						}
					} else if (pageNumAll - pageNum == 2) {
						for (int i = 0; i < 5; i++) {
							int num = pageNumAll - 4;
							buttonList.get(i).setPageNum(num + i);
							buttonList.get(i).repaint();
						}
						if (buttonList.get(2).getMode() == "Selected") {
							buttonList.get(2).setMode("normal");
							buttonList.get(2).repaint();
							buttonList.get(3).setMode("Selected");
							buttonList.get(3).repaint();
						} else if (buttonList.get(3).getMode() == "Selected") {
							buttonList.get(3).setMode("normal");
							buttonList.get(3).repaint();
							buttonList.get(4).setMode("Selected");
							buttonList.get(4).repaint();
						}
					}
				}
			}
			
			JScrollBar scrollBar = bottomPanel.getScrollPane().getVerticalScrollBar();
			int lengthValue = scrollBar.getValue();
			scrollBar.setValue(lengthValue+(Page.PAGE_HEIGHT + NoteCanvas.PAGE_GAP) );
		}
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void adjustmentValueChanged(AdjustmentEvent e) {
		// TODO Auto-generated method stub
		BottomPanel bottomPanel = (BottomPanel)getParent();
		int pageLength = NoteCanvas.PAGE_GAP + Page.PAGE_HEIGHT;
		//总页数
		int pageNumAll = bottomPanel.getCanvas().getScore().getPageList().size();
		//当前页面
		int pageNumber = e.getValue() / pageLength + 1;
		if (pageNumAll <= 5) {
			//按钮不够，先生成按钮
			while(buttonList.size() < pageNumAll){
				addPageButton();
			}
			for (int i = 0; i < pageNumAll; i++) {
				buttonList.get(i).setPageNum(i + 1);
				buttonList.get(i).setMode("normal");
				if (buttonList.get(i).getPageNum() == pageNumber) {
					buttonList.get(i).setMode("Selected");
					curPageIndex = buttonList.get(i).getPageNum();
				}
				buttonList.get(i).repaint();
			}
		}
		else if (pageNumAll > 5) {
			//按钮不够，先生成按钮
			while(buttonList.size() < 5){
				addPageButton();
			}
			if (pageNumber <= 3) {
				for (int i = 0; i < 5; i++) {
					buttonList.get(i).setPageNum(i + 1);
					buttonList.get(i).setMode("normal");
					buttonList.get(i).repaint();
					if (buttonList.get(i).getPageNum() == pageNumber) {
						buttonList.get(i).setMode("Selected");
						curPageIndex = buttonList.get(i).getPageNum();
					}
					buttonList.get(i).repaint();
				}
			} else {
				if ((pageNumAll - pageNumber) <= 2) {
					for (int i = 0; i < 5; i++) {
						buttonList.get(i).setPageNum(pageNumAll - 4 + i);
						buttonList.get(i).setMode("normal");
						buttonList.get(i).repaint();
						if (buttonList.get(i).getPageNum() == pageNumber) {
							buttonList.get(i).setMode("Selected");
							curPageIndex = buttonList.get(i).getPageNum();
						}
						buttonList.get(i).repaint();
					}
				} else {
					for (int i = 0; i < 5; i++) {
						buttonList.get(i).setPageNum(pageNumber - 2 + i);
						buttonList.get(i).setMode("normal");
						buttonList.get(i).repaint();
						if (buttonList.get(i).getPageNum() == pageNumber) {
							buttonList.get(i).setMode("Selected");
							curPageIndex = buttonList.get(i).getPageNum();
						}
						buttonList.get(i).repaint();
					}
				}
			}

		}
	}

}
