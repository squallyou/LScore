package sjy.elwg.notation.musicBeans;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JPanel;

import sjy.elwg.notation.NoteCanvas;
import sjy.elwg.utility.Controller;
import sjy.elwg.utility.MusicMath;

/**
 * 符杠
 * @author jingyuan.sun
 *
 */
public class Beam extends JPanel {
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 5L;
	/**
	 * 与符杠关联的音符
	 */
	private ArrayList<AbstractNote> uiNoteList;
	/**
	 * 符杠在音符的位置，上或者下
	 */
	private String upOrDown;
	/**
	 * 符杠倾斜方式，左高，右高，或者水平
	 */
	private String highNode;
	/**
	 * 符杠层数，取决于最短音符
	 */
	private int beamNum;
	
	private int noteWidth = 12;
	/**
	 * 符杠倾斜斜率
	 */
	private double ratio = 0.15;
	private int beamWidth = 4;
	private int beamGap = 2;
	
	/**
	 * 构造函数
	 * @param notes
	 */
	public Beam(ArrayList<AbstractNote> notes){
		super();
		int num = notes.size();
		uiNoteList = new ArrayList<AbstractNote>();
		for(int i = 0; i < notes.size(); i++){
			AbstractNote anote = notes.get(i);
			uiNoteList.add(anote);
			anote.setBeam(this);
			if(i != 0 && i < num - 1){
				anote.setBeamType("continue");
			}
		}
		beamNum = 1;
		noteWidth = notes.get(0).getHighestNote().getWidth()-1;
		determineShape();
		adjustSize();
		setOpaque(false);
		setLayout(null);
		repaint();
	}
	
	/**
	 * 构造函数
	 * @param notes
	 * @param upOrDown 符杠朝向
	 */
	public Beam(ArrayList<AbstractNote> notes, String upOrDown){
		super();
		int num = notes.size();
		uiNoteList = new ArrayList<AbstractNote>();
		for(int i = 0; i < notes.size(); i++){
			AbstractNote anote = notes.get(i);
			uiNoteList.add(anote);
			anote.setBeam(this);
			if(i != 0 && i < num - 1){
				anote.setBeamType("continue");
			}
		}
		beamNum = 1;
		noteWidth = notes.get(0).getHighestNote().getWidth()-1;
		this.upOrDown = upOrDown;
		determineHighNode();
		adjustSize();
		setOpaque(false);
		setLayout(null);
		repaint();
	}
	
	/**
	 * 判断音符序列的倾斜方式与样式
	 */
	public void determineShape(){
		determineUpOrDown();
		determineHighNode();
	}
	
	/**
	 * 决定符杠的朝向
	 */
	private void determineUpOrDown(){
		upOrDown = Controller.beamUpOrDown(uiNoteList);
	}
	
	/**
	 * 决定符杠的倾斜方式
	 */
	private void determineHighNode(){
		highNode = Controller.highBeamNode(uiNoteList);
	}

	/**
	 * 获得符杠斜率
	 * @return
	 */
	public double getRatio() {
		return ratio;
	}

	/**
	 * 设置斜率
	 * @param ratio
	 */
	public void setRatio(double ratio) {
		this.ratio = ratio;
	}

	/**
	 * 返回符杠位置
	 * @return
	 */
	public String getUpOrDown() {
		return upOrDown;
	}

	/**
	 * 设置符杠位置
	 * @param upOrDown
	 */
	public void setUpOrDown(String upOrDown) {
		this.upOrDown = upOrDown;
	}

	/**
	 * 获得倾斜方式
	 * @return
	 */
	public String getHighNode() {
		return highNode;
	}

	/**
	 * 设置倾斜方式
	 * @param highNode
	 */
	public void setHighNode(String highNode) {
		this.highNode = highNode;
	}

	/**
	 * 根据音符信息调整大小
	 */
	public void adjustSize(){
		//如果符杠水平
		int div = MusicMath.shortestDur(uiNoteList);
		switch(div){
		case 256/8: beamNum = 1; break;
		case 256/16: beamNum = 2; break;
		case 256/32: beamNum = 3; break;
		case 256/64: beamNum = 4; break;
		}
		int width = 0;
		for(int i = uiNoteList.size()-1; i >= 0; i--){
			if(uiNoteList.get(i) instanceof Note){
			width = uiNoteList.get(i).getX() - uiNoteList.get(0).getX()+10;
			break;	
			}else if(uiNoteList.get(i) instanceof ChordNote){
				if(uiNoteList.get(i).getWidth() == ((ChordNote)uiNoteList.get(i)).getNote(0).getWidth()){
					width = uiNoteList.get(i).getX() - uiNoteList.get(0).getX()+10;
					break;		
				}else{
					width = uiNoteList.get(i).getX() + ((ChordNote) uiNoteList.get(i)).getNote(0).getWidth()- uiNoteList.get(0).getX();
					break;		
				}
			}
			
		}
		int height = beamNum * beamWidth + (beamNum - 1) * beamGap;
		//如果符杠不水平，增加高度
		if(!highNode.equalsIgnoreCase("flat")) height += width * ratio;
		setSize(width, height);
	}
	
	/**
	 * 获得音符序列
	 * @return
	 */
	public ArrayList<AbstractNote> getUiNoteList() {
		return uiNoteList;
	}

	public void paintComponent(Graphics gg){
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg;	
		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g.setRenderingHints(renderHints);
        
		if(highNode.equalsIgnoreCase("flat")){
			ratio = 0.0;
		}
		//符杠在音符上面
		if(upOrDown.equalsIgnoreCase("up")){
			//右边高
			if(highNode.equalsIgnoreCase("right") || highNode.equalsIgnoreCase("flat")){
				int width = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(0).getX();
				Polygon p = new Polygon();
				p.addPoint(0, (int)(width*ratio));
				p.addPoint(0, (int)(width*ratio)+4);
				p.addPoint(width, 4);
				p.addPoint(width, 0);
				g.fillPolygon(p); 
				//第一条符杠,8分符杠
				/***************************************************/
				ArrayList<AbstractNote> list = new ArrayList<AbstractNote>();
				for(int i = 0; i < uiNoteList.size(); i++){
					if(uiNoteList.get(i).getDuration() <= 16){
						list.add(uiNoteList.get(i));
					}else{
						//如果不止一个音符
						if(!list.isEmpty() && list.size()!= 1){
							int startIndex = uiNoteList.indexOf(list.get(0));
							int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
							Polygon poly = new Polygon();
							int x = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
							int y = (int)(deltax*ratio) + beamWidth + beamGap;
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
							deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
							y = (int)(deltax*ratio) + beamWidth + beamGap;
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}//只有一个音符
						else if(!list.isEmpty()){
							int index = uiNoteList.indexOf(list.get(0));
							int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(index).getX();
							int y = (int)(deltax*ratio) + beamWidth + beamGap;
							Polygon poly = new Polygon();
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = x + noteWidth;
							y = y - (int)(noteWidth*ratio);
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}
					}
				}
				//处理最后一组音符
				if(!list.isEmpty() && list.size()!= 1){
					int startIndex = uiNoteList.indexOf(list.get(0));
					int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
					Polygon poly = new Polygon();
					int x = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
					int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
					int y = (int)(deltax*ratio) + beamWidth + beamGap;
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
					deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
					y = (int)(deltax*ratio) + beamWidth + beamGap;
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}//只有最后一个音符
				else if(!list.isEmpty()){
					int index = uiNoteList.indexOf(list.get(0));
					int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
					int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(index).getX();
					int y = (int)(deltax*ratio) + beamWidth + beamGap;
					Polygon poly = new Polygon();
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = x - noteWidth;
					y = y + (int)(noteWidth*ratio);
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}
				//第二条符杠，16分音符
				/**************************************************************/
				for(int i = 0; i < uiNoteList.size(); i++){
					if(uiNoteList.get(i).getDuration() <= 256/32){
						list.add(uiNoteList.get(i));
					}else{
						//如果不止一个音符
						if(!list.isEmpty() && list.size()!= 1){
							int startIndex = uiNoteList.indexOf(list.get(0));
							int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
							Polygon poly = new Polygon();
							int x = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
							int y = (int)(deltax*ratio) + 2*beamWidth + 2*beamGap;
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
							deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
							y = (int)(deltax*ratio) + 2*beamWidth + 2*beamGap;
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}//只有一个音符
						else if(!list.isEmpty()){
							int index = uiNoteList.indexOf(list.get(0));
							int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(index).getX();
							int y = (int)(deltax*ratio) + 2*beamWidth + 2*beamGap;
							Polygon poly = new Polygon();
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = x + noteWidth;
							y = y - (int)(noteWidth*ratio);
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}
					}
				}
				//处理最后一组音符
				if(!list.isEmpty() && list.size()!= 1){
					int startIndex = uiNoteList.indexOf(list.get(0));
					int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
					Polygon poly = new Polygon();
					int x = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
					int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
					int y = (int)(deltax*ratio) + 2*beamWidth + 2*beamGap;
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
					deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
					y = (int)(deltax*ratio) + 2*beamWidth + 2*beamGap;
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}//只有最后一个音符
				else if(!list.isEmpty()){
					int index = uiNoteList.indexOf(list.get(0));
					int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
					int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(index).getX();
					int y = (int)(deltax*ratio) + 2*beamWidth + 2*beamGap;
					Polygon poly = new Polygon();
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = x - noteWidth;
					y = y + (int)(noteWidth*ratio);
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}
				//第三条符杠,32分音符
				/*******************************************************/
				for(int i = 0; i < uiNoteList.size(); i++){
					if(uiNoteList.get(i).getDuration() <= 256/64){
						list.add(uiNoteList.get(i));
					}else{
						//如果不止一个音符
						if(!list.isEmpty() && list.size()!= 1){
							int startIndex = uiNoteList.indexOf(list.get(0));
							int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
							Polygon poly = new Polygon();
							int x = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
							int y = (int)(deltax*ratio) + 3*beamWidth + 3*beamGap;
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
							deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
							y = (int)(deltax*ratio) + 3*beamWidth + 3*beamGap;
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}//只有一个音符
						else if(!list.isEmpty()){
							int index = uiNoteList.indexOf(list.get(0));
							int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(index).getX();
							int y = (int)(deltax*ratio) + 3*beamWidth + 3*beamGap;
							Polygon poly = new Polygon();
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = x + noteWidth;
							y = y - (int)(noteWidth*ratio);
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}
					}
				}
				//处理最后一组音符
				if(!list.isEmpty() && list.size()!= 1){
					int startIndex = uiNoteList.indexOf(list.get(0));
					int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
					Polygon poly = new Polygon();
					int x = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
					int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
					int y = (int)(deltax*ratio) + 3*beamWidth + 3*beamGap;
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
					deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
					y = (int)(deltax*ratio) + 3*beamWidth + 3*beamGap;
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}//只有最后一个音符
				else if(!list.isEmpty()){
					int index = uiNoteList.indexOf(list.get(0));
					int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
					int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(index).getX();
					int y = (int)(deltax*ratio) + 3*beamWidth + 3*beamGap;
					Polygon poly = new Polygon();
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = x - noteWidth;
					y = y + (int)(noteWidth*ratio);
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}
				//第四条符杠，64分音符
			}
			//////////////////////////////////////////////////////////////////////////////////////////////////////////
			//左边高
			else{
				int width = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(0).getX();
				Polygon p = new Polygon();
				p.addPoint(width, (int)(width*ratio));
				p.addPoint(width, (int)(width*ratio)+4);
				p.addPoint(0, 4);
				p.addPoint(0, 0);
				g.fillPolygon(p); 
				//第一条符杠,8分符杠
				/***************************************************/
				ArrayList<AbstractNote> list = new ArrayList<AbstractNote>();
				for(int i = 0; i < uiNoteList.size(); i++){
					if(uiNoteList.get(i).getDuration() <= 16){
						list.add(uiNoteList.get(i));
					}else{
						//如果不止一个音符
						if(!list.isEmpty() && list.size()!= 1){
							int startIndex = uiNoteList.indexOf(list.get(0));
							int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
							Polygon poly = new Polygon();
							int x = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
							int y = (int)(x*ratio) + beamWidth + beamGap;
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
							y = (int)((x)*ratio) + beamWidth + beamGap;
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}//只有一个音符
						else if(!list.isEmpty()){
							int index = uiNoteList.indexOf(list.get(0));
							int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
							int y = (int)(x*ratio) + beamWidth + beamGap;
							Polygon poly = new Polygon();
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = x + noteWidth;
							y = y +(int)(noteWidth*ratio);//lwx
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}
					}
				}
				//处理最后一组音符
				if(!list.isEmpty() && list.size()!= 1){
					int startIndex = uiNoteList.indexOf(list.get(0));
					int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
					Polygon poly = new Polygon();
					int x = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
					int y = (int)(x*ratio) + beamWidth + beamGap;
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
					y = (int)(x*ratio) + beamWidth + beamGap;
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}//只有最后一个音符
				else if(!list.isEmpty()){
					int index = uiNoteList.indexOf(list.get(0));
					int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
					int y = (int)(x*ratio) + beamWidth + beamGap;
					Polygon poly = new Polygon();
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = x - noteWidth;
					y = y - (int)(noteWidth*ratio);
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}
				//第二条符杠，16分音符
				/**************************************************************/
				for(int i = 0; i < uiNoteList.size(); i++){
					if(uiNoteList.get(i).getDuration() <= 256/32){
						list.add(uiNoteList.get(i));
					}else{
						//如果不止一个音符
						if(!list.isEmpty() && list.size()!= 1){
							int startIndex = uiNoteList.indexOf(list.get(0));
							int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
							Polygon poly = new Polygon();
							int x = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
							int y = (int)(x*ratio) + 2*beamWidth + 2*beamGap;
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
							y = (int)(x*ratio) + 2*beamWidth + 2*beamGap;
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}//只有一个音符
						else if(!list.isEmpty()){
							int index = uiNoteList.indexOf(list.get(0));
							int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
							int y = (int)(x*ratio) + 2*beamWidth + 2*beamGap;
							Polygon poly = new Polygon();
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = x + noteWidth+50;
							y = y + (int)(noteWidth*ratio);//lwx
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}
					}
				}
				//处理最后一组音符
				if(!list.isEmpty() && list.size()!= 1){
					int startIndex = uiNoteList.indexOf(list.get(0));
					int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
					Polygon poly = new Polygon();
					int x = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
					int y = (int)(x*ratio) + 2*beamWidth + 2*beamGap;
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
					y = (int)(x*ratio) + 2*beamWidth + 2*beamGap;
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}//只有最后一个音符
				else if(!list.isEmpty()){
					int index = uiNoteList.indexOf(list.get(0));
					int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
					int y = (int)(x*ratio) + 2*beamWidth + 2*beamGap;
					Polygon poly = new Polygon();
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = x - noteWidth;
					y = y -(int)(noteWidth*ratio); //lwx
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}
				//第三条符杠,32分音符
				/*******************************************************/
				for(int i = 0; i < uiNoteList.size(); i++){
					if(uiNoteList.get(i).getDuration() <= 256/64){
						list.add(uiNoteList.get(i));
					}else{
						//如果不止一个音符
						if(!list.isEmpty() && list.size()!= 1){
							int startIndex = uiNoteList.indexOf(list.get(0));
							int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
							Polygon poly = new Polygon();
							int x = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
							int y = (int)(x*ratio) + 3*beamWidth + 3*beamGap;
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
							y = (int)(x*ratio) + 3*beamWidth + 3*beamGap;
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}//只有一个音符
						else if(!list.isEmpty()){
							int index = uiNoteList.indexOf(list.get(0));
							int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
							int y = (int)(x*ratio) + 3*beamWidth + 3*beamGap;
							Polygon poly = new Polygon();
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = x + noteWidth;
							y = y + (int)(noteWidth*ratio); //lwx
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}
					}
				}
				//处理最后一组音符
				if(!list.isEmpty() && list.size()!= 1){
					int startIndex = uiNoteList.indexOf(list.get(0));
					int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
					Polygon poly = new Polygon();
					int x = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
					int y = (int)(x*ratio) + 3*beamWidth + 3*beamGap;
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
					y = (int)(x*ratio) + 3*beamWidth + 3*beamGap;
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}//只有最后一个音符
				else if(!list.isEmpty()){
					int index = uiNoteList.indexOf(list.get(0));
					int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
					int y = (int)(x*ratio) + 3*beamWidth + 3*beamGap;
					Polygon poly = new Polygon();
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = x - noteWidth;
					y = y - (int)(noteWidth*ratio); //lwx
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}
				//第四条符杠，64分音符
			}
		}
		/********************************************符杠在音符下面*************************************************/
		//符杠在音符下面
		else if(upOrDown.equalsIgnoreCase("down")){
			//右边高
			if(highNode.equalsIgnoreCase("right") || highNode.equalsIgnoreCase("flat")){
				int width = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(0).getX();
				int height = getHeight();
				
				Polygon p = new Polygon();
				if(uiNoteList.get(0) instanceof Note){
					p.addPoint(0, height-beamWidth);
					p.addPoint(0, height);
				}else if(uiNoteList.get(0) instanceof ChordNote){
					int cNoteWidth = ((ChordNote) uiNoteList.get(0)).getNote(0).getWidth();
					if(uiNoteList.get(0).getWidth() == cNoteWidth){
						p.addPoint(0, height-beamWidth);
						p.addPoint(0, height);
					}else{
						p.addPoint(cNoteWidth, height-beamWidth);
						p.addPoint(cNoteWidth, height);
					}
				}
				if(uiNoteList.get(uiNoteList.size()-1) instanceof Note){
					p.addPoint(width, height-(int)(ratio*width));
					p.addPoint(width, height-(int)(ratio*width)-beamWidth);
				}else if(uiNoteList.get(uiNoteList.size()-1) instanceof ChordNote){
					int cNoteWidth = ((ChordNote) uiNoteList.get(uiNoteList.size()-1)).getNote(0).getWidth();
					if(uiNoteList.get(uiNoteList.size()-1).getWidth() == cNoteWidth){
						p.addPoint(width, height-(int)(ratio*width));
						p.addPoint(width, height-(int)(ratio*width)-beamWidth);
					}else{
						p.addPoint(width + cNoteWidth, height-(int)(ratio*width));
						p.addPoint(width + cNoteWidth, height-(int)(ratio*width)-beamWidth);
					}
				}
				
				g.fillPolygon(p); 
				//第一条符杠,8分符杠
				/***************************************************/
				ArrayList<AbstractNote> list = new ArrayList<AbstractNote>();
				for(int i = 0; i < uiNoteList.size(); i++){
					if(uiNoteList.get(i).getDuration() <= 16){
						list.add(uiNoteList.get(i));
					}else{
						//如果不止一个音符
						if(!list.isEmpty() && list.size()!= 1){
							int startIndex = uiNoteList.indexOf(list.get(0));
							int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
							Polygon poly = new Polygon();
							int xStart = 0;
							int yStart = 0;
							int xEnd = 0;
							int yEnd = 0;
							if(uiNoteList.get(startIndex) instanceof Note){
								Note startNote = (Note) uiNoteList.get(startIndex);
								xStart = startNote.getX() - uiNoteList.get(0).getX();
								yStart =  height - (int)(xStart*ratio) - 2*beamWidth - beamGap;
								poly.addPoint(xStart,yStart);
								poly.addPoint(xStart,yStart + beamWidth);
							}else if(uiNoteList.get(startIndex) instanceof ChordNote){
								ChordNote startNote = (ChordNote) uiNoteList.get(startIndex);						
								if(startNote.getWidth() == startNote.getNote(0).getWidth()){
									xStart = startNote.getX() - uiNoteList.get(0).getX();
									yStart =  height - (int)(xStart*ratio) - 2*beamWidth - beamGap;
									poly.addPoint(xStart,yStart);
									poly.addPoint(xStart,yStart + beamWidth);
								}else if(startNote.getWidth() == 2 * startNote.getNote(0).getWidth()){
									xStart = startNote.getX() + startNote.getNote(0).getWidth() - uiNoteList.get(0).getX();
									yStart =  height - (int)(xStart*ratio) - 2*beamWidth - beamGap;							
									
									poly.addPoint(xStart,yStart);
									poly.addPoint(xStart,yStart + beamWidth);
								}
							
							}
							if(uiNoteList.get(endIndex) instanceof Note){
								Note endNote = (Note) uiNoteList.get(endIndex);
								xEnd = endNote.getX() - uiNoteList.get(0).getX();
								yEnd =  height - (int)(xEnd*ratio) - 2*beamWidth - beamGap;
								poly.addPoint(xEnd ,yEnd + beamWidth);
								poly.addPoint(xEnd ,yEnd);
								
							}else if(uiNoteList.get(endIndex) instanceof ChordNote){
								ChordNote endNote = (ChordNote) uiNoteList.get(endIndex);						
								if(endNote.getWidth() == endNote.getNote(0).getWidth()){
									xEnd = endNote.getX() - uiNoteList.get(0).getX();
									yEnd =  height - (int)(xEnd*ratio) - 2*beamWidth - beamGap;
									poly.addPoint(xEnd ,yEnd + beamWidth);
									poly.addPoint(xEnd,yEnd);
									
								}else if(endNote.getWidth() == 2 * endNote.getNote(0).getWidth()){
									xEnd = endNote.getX() + endNote.getNote(0).getWidth() - uiNoteList.get(0).getX();
									yEnd =  height - (int)(xEnd*ratio) - 2*beamWidth - beamGap;
									poly.addPoint(xEnd,yEnd + beamWidth);
									poly.addPoint(xEnd,yEnd);
									
								}
							  
							}
							
							g.fillPolygon(poly);
							list.clear();							
						}//只有一个音符
						else if(!list.isEmpty()){
							int index = uiNoteList.indexOf(list.get(0));
							int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX() + noteWidth;
							int y = height - (int)(x*ratio) - 2*beamWidth - beamGap;
							Polygon poly = new Polygon();
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = x + noteWidth;
							y = y - (int)(noteWidth*ratio);
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}
					}
				}
				//处理最后一组音符
				if(!list.isEmpty() && list.size()!= 1){
					int startIndex = uiNoteList.indexOf(list.get(0));
					int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
					Polygon poly = new Polygon();
					int xStart = 0;
					int yStart = 0;
					int xEnd = 0;
					int yEnd = 0;
					if(uiNoteList.get(startIndex) instanceof Note){
						Note startNote = (Note) uiNoteList.get(startIndex);
						xStart = startNote.getX() - uiNoteList.get(0).getX();
						yStart =  height - (int)(xStart*ratio) - 2*beamWidth - beamGap;
						poly.addPoint(xStart,yStart);
						poly.addPoint(xStart,yStart + beamWidth);
					}else if(uiNoteList.get(startIndex) instanceof ChordNote){
						ChordNote startNote = (ChordNote) uiNoteList.get(startIndex);						
						if(startNote.getWidth() == startNote.getNote(0).getWidth()){
							xStart = startNote.getX() - uiNoteList.get(0).getX() ;
							yStart =  height - (int)(xStart*ratio) - 2*beamWidth - beamGap;
							poly.addPoint(xStart,yStart);
							poly.addPoint(xStart,yStart + beamWidth);
						}else if(startNote.getWidth() == 2 * startNote.getNote(0).getWidth()){
							xStart = startNote.getX() + startNote.getNote(0).getWidth() - uiNoteList.get(0).getX();
							yStart =  height - (int)(xStart*ratio) - 2*beamWidth - beamGap;							
							poly.addPoint(xStart,yStart);
							poly.addPoint(xStart,yStart + beamWidth);
						}
					
					}
					if(uiNoteList.get(endIndex) instanceof Note){
						Note endNote = (Note) uiNoteList.get(endIndex);
						xEnd = endNote.getX() - uiNoteList.get(0).getX();
						yEnd =  height - (int)(xEnd*ratio) - 2*beamWidth - beamGap;
						poly.addPoint(xEnd,yEnd + beamWidth);
						poly.addPoint(xEnd ,yEnd);
						
					}else if(uiNoteList.get(endIndex) instanceof ChordNote){
						ChordNote endNote = (ChordNote) uiNoteList.get(endIndex);						
						if(endNote.getWidth() == endNote.getNote(0).getWidth()){
							xEnd = endNote.getX() - uiNoteList.get(0).getX() ;
							yEnd =  height - (int)(xEnd*ratio) - 2*beamWidth - beamGap;
							poly.addPoint(xEnd,yEnd + beamWidth);
							poly.addPoint(xEnd,yEnd);
							
						}else if(endNote.getWidth() == 2 * endNote.getNote(0).getWidth()){
							xEnd = endNote.getX() + endNote.getNote(0).getWidth() - uiNoteList.get(0).getX();
							yEnd =  height - (int)(xEnd*ratio) - 2*beamWidth - beamGap;
							poly.addPoint(xEnd,yEnd + beamWidth);
							poly.addPoint(xEnd,yEnd);
							
						}
					  
					}
					
					g.fillPolygon(poly);
					list.clear();
				}//只有最后一个音符
				else if(!list.isEmpty()){
					int index = uiNoteList.indexOf(list.get(0));
					int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
					int y = height - (int)(x*ratio) - 2*beamWidth - beamGap;
					Polygon poly = new Polygon();
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = x - noteWidth;
					y = y + (int)(noteWidth*ratio);
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}
				//第二条符杠，16分音符
				/**************************************************************/
				for(int i = 0; i < uiNoteList.size(); i++){
					if(uiNoteList.get(i).getDuration() <= 256/32){
						list.add(uiNoteList.get(i));
					}else{
						//如果不止一个音符
						if(!list.isEmpty() && list.size()!= 1){
							int startIndex = uiNoteList.indexOf(list.get(0));
							int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
							Polygon poly = new Polygon();
							int xStart = 0;
							int yStart = 0;
							int xEnd = 0;
							int yEnd = 0;
							if(uiNoteList.get(startIndex) instanceof Note){
//								Note startNote = (Note) uiNoteList.get(startIndex);
								xStart = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
								yStart =  height - (int)(xStart*ratio) - 3*beamWidth - 2*beamGap;
								poly.addPoint(xStart,yStart);
								poly.addPoint(xStart,yStart + beamWidth);
							}else if(uiNoteList.get(startIndex) instanceof ChordNote){
								ChordNote startNote = (ChordNote) uiNoteList.get(startIndex);						
								if(startNote.getWidth() == startNote.getNote(0).getWidth()){
									xStart = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
									yStart =  height - (int)(xStart*ratio) - 3*beamWidth - 2*beamGap;
									poly.addPoint(xStart,yStart);
									poly.addPoint(xStart,yStart + beamWidth);
								}else if(startNote.getWidth() == 2 * startNote.getNote(0).getWidth()){
									xStart = uiNoteList.get(startIndex).getX() + startNote.getNote(0).getWidth()- uiNoteList.get(0).getX();
									yStart =  height - (int)(xStart*ratio) - 3*beamWidth - 2*beamGap;						
									
									poly.addPoint(xStart,yStart);
									poly.addPoint(xStart,yStart + beamWidth);
								}
							
							}
							if(uiNoteList.get(endIndex) instanceof Note){
//								Note endNote = (Note) uiNoteList.get(endIndex);
								xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
								yEnd =  height - (int)(xEnd*ratio) - 3*beamWidth - 2*beamGap;
								poly.addPoint(xEnd ,yEnd + beamWidth);
								poly.addPoint(xEnd ,yEnd);
								
							}else if(uiNoteList.get(endIndex) instanceof ChordNote){
								ChordNote endNote = (ChordNote) uiNoteList.get(endIndex);						
								if(endNote.getWidth() == endNote.getNote(0).getWidth()){
									xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
									yEnd =  height - (int)(xEnd*ratio) - 3*beamWidth - 2*beamGap;
									poly.addPoint(xEnd ,yEnd + beamWidth);
									poly.addPoint(xEnd,yEnd);
									
								}else if(endNote.getWidth() == 2 * endNote.getNote(0).getWidth()){
									xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX()+ endNote.getNote(0).getWidth();
									yEnd =  height - (int)(xEnd*ratio) - 3*beamWidth - 2*beamGap;
									poly.addPoint(xEnd,yEnd + beamWidth);
									poly.addPoint(xEnd,yEnd);
									
								}
							  
							}
//							int x = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
//							int y = height - (int)(x*ratio) - 3*beamWidth - 2*beamGap;
//							poly.addPoint(x, y);
//							poly.addPoint(x, y+beamWidth);
//							x = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
//							y = height - (int)(x*ratio) - 3*beamWidth - 2*beamGap;
//							poly.addPoint(x, y+beamWidth);
//							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}//只有一个音符
						else if(!list.isEmpty()){
							int index = uiNoteList.indexOf(list.get(0));
							int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX() + noteWidth;
							int y = height - (int)(x*ratio) - 3*beamWidth - 2*beamGap;
							Polygon poly = new Polygon();
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = x + noteWidth;
							y = y - (int)(noteWidth*ratio);
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}
					}
				}
				//处理最后一组音符
				if(!list.isEmpty() && list.size()!= 1){
					int startIndex = uiNoteList.indexOf(list.get(0));
					int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
					Polygon poly = new Polygon();
					int xStart = 0;
					int yStart = 0;
					int xEnd = 0;
					int yEnd = 0;
					if(uiNoteList.get(startIndex) instanceof Note){
//						Note startNote = (Note) uiNoteList.get(startIndex);
						xStart = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
						yStart =  height - (int)(xStart*ratio) - 3*beamWidth - 2*beamGap;
						poly.addPoint(xStart,yStart);
						poly.addPoint(xStart,yStart + beamWidth);
					}else if(uiNoteList.get(startIndex) instanceof ChordNote){
						ChordNote startNote = (ChordNote) uiNoteList.get(startIndex);						
						if(startNote.getWidth() == startNote.getNote(0).getWidth()){
							xStart = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
							yStart =  height - (int)(xStart*ratio) - 3*beamWidth - 2*beamGap;
							poly.addPoint(xStart,yStart);
							poly.addPoint(xStart,yStart + beamWidth);
						}else if(startNote.getWidth() == 2 * startNote.getNote(0).getWidth()){
							xStart = uiNoteList.get(startIndex).getX() + startNote.getNote(0).getWidth()- uiNoteList.get(0).getX();
							yStart =  height - (int)(xStart*ratio) - 3*beamWidth - 2*beamGap;						
							
							poly.addPoint(xStart,yStart);
							poly.addPoint(xStart,yStart + beamWidth);
						}
					
					}
					if(uiNoteList.get(endIndex) instanceof Note){
//						Note endNote = (Note) uiNoteList.get(endIndex);
						xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
						yEnd =  height - (int)(xEnd*ratio) - 3*beamWidth - 2*beamGap;
						poly.addPoint(xEnd ,yEnd + beamWidth);
						poly.addPoint(xEnd ,yEnd);
						
					}else if(uiNoteList.get(endIndex) instanceof ChordNote){
						ChordNote endNote = (ChordNote) uiNoteList.get(endIndex);						
						if(endNote.getWidth() == endNote.getNote(0).getWidth()){
							xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
							yEnd =  height - (int)(xEnd*ratio) - 3*beamWidth - 2*beamGap;
							poly.addPoint(xEnd ,yEnd + beamWidth);
							poly.addPoint(xEnd,yEnd);
							
						}else if(endNote.getWidth() == 2 * endNote.getNote(0).getWidth()){
							xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX()+ endNote.getNote(0).getWidth();
							yEnd =  height - (int)(xEnd*ratio) - 3*beamWidth - 2*beamGap;
							poly.addPoint(xEnd,yEnd + beamWidth);
							poly.addPoint(xEnd,yEnd);
							
						}
					  
					}
					g.fillPolygon(poly);
					list.clear();
				}//只有最后一个音符
				else if(!list.isEmpty()){
					int index = uiNoteList.indexOf(list.get(0));
					int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
					int y = height - (int)(x*ratio) - 3*beamWidth - 2*beamGap;
					Polygon poly = new Polygon();
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = x - noteWidth;
					y = y + (int)(noteWidth*ratio);
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}
				//第三条符杠,32分音符
				/*******************************************************/
				for(int i = 0; i < uiNoteList.size(); i++){
					if(uiNoteList.get(i).getDuration() <= 256/64){
						list.add(uiNoteList.get(i));
					}else{
						//如果不止一个音符
						if(!list.isEmpty() && list.size()!= 1){
							int startIndex = uiNoteList.indexOf(list.get(0));
							int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
							Polygon poly = new Polygon();
							int xStart = 0;
							int yStart = 0;
							int xEnd = 0;
							int yEnd = 0;
							if(uiNoteList.get(startIndex) instanceof Note){
//								Note startNote = (Note) uiNoteList.get(startIndex);
								xStart = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
								yStart =  height - (int)(xStart*ratio) - 4*beamWidth - 3*beamGap;
								poly.addPoint(xStart,yStart);
								poly.addPoint(xStart,yStart + beamWidth);
							}else if(uiNoteList.get(startIndex) instanceof ChordNote){
								ChordNote startNote = (ChordNote) uiNoteList.get(startIndex);						
								if(startNote.getWidth() == startNote.getNote(0).getWidth()){
									xStart = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
									yStart =  height - (int)(xStart*ratio) - 4*beamWidth - 3*beamGap;
									poly.addPoint(xStart,yStart);
									poly.addPoint(xStart,yStart + beamWidth);
								}else if(startNote.getWidth() == 2 * startNote.getNote(0).getWidth()){
									xStart = uiNoteList.get(startIndex).getX() + startNote.getNote(0).getWidth()- uiNoteList.get(0).getX();
									yStart =   height - (int)(xStart*ratio) - 4*beamWidth - 3*beamGap;						
									
									poly.addPoint(xStart,yStart);
									poly.addPoint(xStart,yStart + beamWidth);
								}
							
							}
							if(uiNoteList.get(endIndex) instanceof Note){
//								Note endNote = (Note) uiNoteList.get(endIndex);
								xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
								yEnd =  height - (int)(xEnd*ratio) - 4*beamWidth - 3*beamGap;
								poly.addPoint(xEnd ,yEnd + beamWidth);
								poly.addPoint(xEnd ,yEnd);
								
							}else if(uiNoteList.get(endIndex) instanceof ChordNote){
								ChordNote endNote = (ChordNote) uiNoteList.get(endIndex);						
								if(endNote.getWidth() == endNote.getNote(0).getWidth()){
									xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
									yEnd =  height - (int)(xEnd*ratio) - 4*beamWidth - 3*beamGap;
									poly.addPoint(xEnd ,yEnd + beamWidth);
									poly.addPoint(xEnd,yEnd);
									
								}else if(endNote.getWidth() == 2 * endNote.getNote(0).getWidth()){
									xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX()+ endNote.getNote(0).getWidth();
									yEnd =  height - (int)(xEnd*ratio) - 4*beamWidth - 3*beamGap;
									poly.addPoint(xEnd,yEnd + beamWidth);
									poly.addPoint(xEnd,yEnd);
									
								}
							  
							}
//							int x = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
//							int y = height - (int)(x*ratio) - 4*beamWidth - 3*beamGap;
//							poly.addPoint(x, y);
//							poly.addPoint(x, y+beamWidth);
//							x = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
//							y = height - (int)(x*ratio) - 4*beamWidth - 3*beamGap;
//							poly.addPoint(x, y+beamWidth);
//							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}//只有一个音符
						else if(!list.isEmpty()){
							int index = uiNoteList.indexOf(list.get(0));
							int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX() + noteWidth;
							int y = height - (int)(x*ratio) - 4*beamWidth - 3*beamGap;
							Polygon poly = new Polygon();
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = x + noteWidth;
							y = y - (int)(noteWidth*ratio);
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}
					}
				}
				//处理最后一组音符
				if(!list.isEmpty() && list.size()!= 1){
					int startIndex = uiNoteList.indexOf(list.get(0));
					int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
					Polygon poly = new Polygon();
					int xStart = 0;
					int yStart = 0;
					int xEnd = 0;
					int yEnd = 0;
					if(uiNoteList.get(startIndex) instanceof Note){
//						Note startNote = (Note) uiNoteList.get(startIndex);
						xStart = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
						yStart =  height - (int)(xStart*ratio) - 4*beamWidth - 3*beamGap;
						poly.addPoint(xStart,yStart);
						poly.addPoint(xStart,yStart + beamWidth);
					}else if(uiNoteList.get(startIndex) instanceof ChordNote){
						ChordNote startNote = (ChordNote) uiNoteList.get(startIndex);						
						if(startNote.getWidth() == startNote.getNote(0).getWidth()){
							xStart = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
							yStart =  height - (int)(xStart*ratio) - 4*beamWidth - 3*beamGap;
							poly.addPoint(xStart,yStart);
							poly.addPoint(xStart,yStart + beamWidth);
						}else if(startNote.getWidth() == 2 * startNote.getNote(0).getWidth()){
							xStart = uiNoteList.get(startIndex).getX() + startNote.getNote(0).getWidth()- uiNoteList.get(0).getX();
							yStart =   height - (int)(xStart*ratio) - 4*beamWidth - 3*beamGap;						
							
							poly.addPoint(xStart,yStart);
							poly.addPoint(xStart,yStart + beamWidth);
						}
					
					}
					if(uiNoteList.get(endIndex) instanceof Note){
//						Note endNote = (Note) uiNoteList.get(endIndex);
						xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
						yEnd =  height - (int)(xEnd*ratio) - 4*beamWidth - 3*beamGap;
						poly.addPoint(xEnd ,yEnd + beamWidth);
						poly.addPoint(xEnd ,yEnd);
						
					}else if(uiNoteList.get(endIndex) instanceof ChordNote){
						ChordNote endNote = (ChordNote) uiNoteList.get(endIndex);						
						if(endNote.getWidth() == endNote.getNote(0).getWidth()){
							xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
							yEnd =  height - (int)(xEnd*ratio) - 4*beamWidth - 3*beamGap;
							poly.addPoint(xEnd ,yEnd + beamWidth);
							poly.addPoint(xEnd,yEnd);
							
						}else if(endNote.getWidth() == 2 * endNote.getNote(0).getWidth()){
							xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX()+ endNote.getNote(0).getWidth();
							yEnd =  height - (int)(xEnd*ratio) - 4*beamWidth - 3*beamGap;
							poly.addPoint(xEnd,yEnd + beamWidth);
							poly.addPoint(xEnd,yEnd);
							
						}
					  
					}
					g.fillPolygon(poly);
					list.clear();
				}//只有最后一个音符
				else if(!list.isEmpty()){
					int index = uiNoteList.indexOf(list.get(0));
					int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX() + noteWidth;
					int y = height - (int)(x*ratio) - 4*beamWidth - 3*beamGap;
					Polygon poly = new Polygon();
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = x - noteWidth;
					y = y + (int)(noteWidth*ratio);
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}
				//第四条符杠，64分音符
			}
			//////////////////////////////////////////////////////////////////////////////////////////////////////////
			//左边高
			else if(highNode.equalsIgnoreCase("left")){
				int width = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(0).getX();
				int height = getHeight();
				Polygon p = new Polygon();
				if(uiNoteList.get(0) instanceof Note){
					p.addPoint(0,height-(int)(ratio*width)-beamWidth );
					p.addPoint(0, height-(int)(ratio*width));
				}else if(uiNoteList.get(0) instanceof ChordNote){
					int cNoteWidth = ((ChordNote) uiNoteList.get(0)).getNote(0).getWidth();
					if(uiNoteList.get(0).getWidth() == cNoteWidth){
						p.addPoint(0, height-(int)(ratio*width)-beamWidth);
						p.addPoint(0, height-(int)(ratio*width));
					}else{
						p.addPoint(cNoteWidth, height-(int)(ratio*width)-beamWidth);
						p.addPoint(cNoteWidth, height-(int)(ratio*width));
					}
				}
				if(uiNoteList.get(uiNoteList.size()-1) instanceof Note){
					p.addPoint(width, height);
					p.addPoint(width, height-beamWidth);
				}else if(uiNoteList.get(uiNoteList.size()-1) instanceof ChordNote){
					int cNoteWidth = ((ChordNote) uiNoteList.get(uiNoteList.size()-1)).getNote(0).getWidth();
					if(uiNoteList.get(uiNoteList.size()-1).getWidth() == cNoteWidth){
						p.addPoint(width, height);
						p.addPoint(width, height-beamWidth);
					}else{
						p.addPoint(width + cNoteWidth, height);
						p.addPoint(width + cNoteWidth, height-beamWidth);
					}
				}
//				p.addPoint(width, getHeight()-4);
//				p.addPoint(width, getHeight());
//				p.addPoint(0, getHeight()-(int)(width*ratio));
//				p.addPoint(0, getHeight()-(int)(width*ratio)-4);
				g.fillPolygon(p); 
				//第一条符杠,8分符杠
				/***************************************************/
				ArrayList<AbstractNote> list = new ArrayList<AbstractNote>();
				for(int i = 0; i < uiNoteList.size(); i++){
					if(uiNoteList.get(i).getDuration() <= 16){
						list.add(uiNoteList.get(i));
					}else{
						//如果不止一个音符
						if(!list.isEmpty() && list.size()!= 1){
							int startIndex = uiNoteList.indexOf(list.get(0));
							int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
							Polygon poly = new Polygon();	
							int xStart = 0;
							int yStart = 0;
							int xEnd = 0;
							int yEnd = 0;
							if(uiNoteList.get(startIndex) instanceof Note){
								int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
								Note startNote = (Note) uiNoteList.get(startIndex);
								xStart = startNote.getX() - uiNoteList.get(0).getX();
								yStart =  height - (int)(deltax*ratio) - 2*beamWidth - beamGap;
								poly.addPoint(xStart,yStart);
								poly.addPoint(xStart,yStart + beamWidth);
							}else if(uiNoteList.get(startIndex) instanceof ChordNote){
								ChordNote startNote = (ChordNote) uiNoteList.get(startIndex);						
								if(startNote.getWidth() == startNote.getNote(0).getWidth()){
									int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
									xStart = startNote.getX() - uiNoteList.get(0).getX();
									yStart =  height - (int)(deltax*ratio) - 2*beamWidth - beamGap;
									poly.addPoint(xStart,yStart);
									poly.addPoint(xStart,yStart + beamWidth);
								}else if(startNote.getWidth() == 2 * startNote.getNote(0).getWidth()){
									int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
									xStart = startNote.getX() + startNote.getNote(0).getWidth() - uiNoteList.get(0).getX();
									yStart =  height - (int)(deltax*ratio) - 2*beamWidth - beamGap;							
									
									poly.addPoint(xStart,yStart);
									poly.addPoint(xStart,yStart + beamWidth);
								}
							
							}
							if(uiNoteList.get(endIndex) instanceof Note){
								int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
								Note endNote = (Note) uiNoteList.get(endIndex);
								xEnd = endNote.getX() - uiNoteList.get(0).getX();
								yEnd =  height - (int)(deltax*ratio) - 2*beamWidth - beamGap;
								poly.addPoint(xEnd ,yEnd + beamWidth);
								poly.addPoint(xEnd ,yEnd);
								
							}else if(uiNoteList.get(endIndex) instanceof ChordNote){
								int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
								ChordNote endNote = (ChordNote) uiNoteList.get(endIndex);						
								if(endNote.getWidth() == endNote.getNote(0).getWidth()){
									xEnd = endNote.getX() - uiNoteList.get(0).getX();
									yEnd =  height - (int)(deltax*ratio) - 2*beamWidth - beamGap;
									poly.addPoint(xEnd ,yEnd + beamWidth);
									poly.addPoint(xEnd,yEnd);
									
								}else if(endNote.getWidth() == 2 * endNote.getNote(0).getWidth()){
									xEnd = endNote.getX() + endNote.getNote(0).getWidth() - uiNoteList.get(0).getX();
									yEnd =  height - (int)(deltax*ratio) - 2*beamWidth - beamGap;
									poly.addPoint(xEnd,yEnd + beamWidth);
									poly.addPoint(xEnd,yEnd);
									
								}
							  
							}							
//							int x = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
//							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
//							int y = getHeight() - (int)(deltax*ratio) - 2*beamWidth - beamGap;
//							poly.addPoint(x, y);
//							poly.addPoint(x, y+beamWidth);
//							x = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
//							deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
//							y = getHeight() - (int)(deltax*ratio) - 2*beamWidth - beamGap;
//							poly.addPoint(x, y+beamWidth);
//							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}//只有一个音符
						else if(!list.isEmpty()){
							int index = uiNoteList.indexOf(list.get(0));
							int x = 0;
							if(uiNoteList.get(0) instanceof Note){
								x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
							}else if(uiNoteList.get(0) instanceof ChordNote){
								int cNoteWidth = ((ChordNote) uiNoteList.get(0)).getNote(0).getWidth();
								if(uiNoteList.get(0).getWidth() == cNoteWidth){
									x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
								}else{
									x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX()+noteWidth;
								}
							}
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(index).getX();
							int y = getHeight() - (int)(deltax*ratio) - 2*beamWidth - beamGap;
							Polygon poly = new Polygon();
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = x + noteWidth;
							y = y + (int)(noteWidth*ratio); //lwx
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}
					}
				}
				//处理最后一组音符
				if(!list.isEmpty() && list.size()!= 1){
					int startIndex = uiNoteList.indexOf(list.get(0));
					int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
					Polygon poly = new Polygon();
					int xStart = 0;
					int yStart = 0;
					int xEnd = 0;
					int yEnd = 0;
					if(uiNoteList.get(startIndex) instanceof Note){
						int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
						Note startNote = (Note) uiNoteList.get(startIndex);
						xStart = startNote.getX() - uiNoteList.get(0).getX();
						yStart =  height - (int)(deltax*ratio) - 2*beamWidth - beamGap;
						poly.addPoint(xStart,yStart);
						poly.addPoint(xStart,yStart + beamWidth);
					}else if(uiNoteList.get(startIndex) instanceof ChordNote){
						ChordNote startNote = (ChordNote) uiNoteList.get(startIndex);						
						if(startNote.getWidth() == startNote.getNote(0).getWidth()){
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
							xStart = startNote.getX() - uiNoteList.get(0).getX();
							yStart =  height - (int)(deltax*ratio) - 2*beamWidth - beamGap;
							poly.addPoint(xStart,yStart);
							poly.addPoint(xStart,yStart + beamWidth);
						}else if(startNote.getWidth() == 2 * startNote.getNote(0).getWidth()){
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
							xStart = startNote.getX() + startNote.getNote(0).getWidth() - uiNoteList.get(0).getX();
							yStart =  height - (int)(deltax*ratio) - 2*beamWidth - beamGap;							
							
							poly.addPoint(xStart,yStart);
							poly.addPoint(xStart,yStart + beamWidth);
						}
					
					}
					if(uiNoteList.get(endIndex) instanceof Note){
						int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
						Note endNote = (Note) uiNoteList.get(endIndex);
						xEnd = endNote.getX() - uiNoteList.get(0).getX();
						yEnd =  height - (int)(deltax*ratio) - 2*beamWidth - beamGap;
						poly.addPoint(xEnd ,yEnd + beamWidth);
						poly.addPoint(xEnd ,yEnd);
						
					}else if(uiNoteList.get(endIndex) instanceof ChordNote){
						int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
						ChordNote endNote = (ChordNote) uiNoteList.get(endIndex);						
						if(endNote.getWidth() == endNote.getNote(0).getWidth()){
							xEnd = endNote.getX() - uiNoteList.get(0).getX();
							yEnd =  height - (int)(deltax*ratio) - 2*beamWidth - beamGap;
							poly.addPoint(xEnd ,yEnd + beamWidth);
							poly.addPoint(xEnd,yEnd);
							
						}else if(endNote.getWidth() == 2 * endNote.getNote(0).getWidth()){
							xEnd = endNote.getX() + endNote.getNote(0).getWidth() - uiNoteList.get(0).getX();
							yEnd =  height - (int)(deltax*ratio) - 2*beamWidth - beamGap;
							poly.addPoint(xEnd,yEnd + beamWidth);
							poly.addPoint(xEnd,yEnd);
							
						}
					  
					}	
					g.fillPolygon(poly);
					list.clear();
				}//只有最后一个音符
				else if(!list.isEmpty()){
					int index = uiNoteList.indexOf(list.get(0));
					int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
					int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(index).getX();
					int y = getHeight() - (int)(deltax*ratio) - 2*beamWidth - beamGap;
					Polygon poly = new Polygon();
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = x - noteWidth;
					y = y - (int)(noteWidth*ratio); //lwx
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}
				//第二条符杠，16分音符
				/**************************************************************/
				for(int i = 0; i < uiNoteList.size(); i++){
					if(uiNoteList.get(i).getDuration() <= 256/32){
						list.add(uiNoteList.get(i));
					}else{
						//如果不止一个音符
						if(!list.isEmpty() && list.size()!= 1){
							int startIndex = uiNoteList.indexOf(list.get(0));
							int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
							Polygon poly = new Polygon();
							int xStart = 0;
							int yStart = 0;
							int xEnd = 0;
							int yEnd = 0;
							if(uiNoteList.get(startIndex) instanceof Note){
//								Note startNote = (Note) uiNoteList.get(startIndex);
								xStart = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
								int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
								yStart =  height - (int)(deltax*ratio) - 3*beamWidth - 2*beamGap;
								poly.addPoint(xStart,yStart);
								poly.addPoint(xStart,yStart + beamWidth);
							}else if(uiNoteList.get(startIndex) instanceof ChordNote){
								ChordNote startNote = (ChordNote) uiNoteList.get(startIndex);						
								if(startNote.getWidth() == startNote.getNote(0).getWidth()){
									int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
									xStart = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
									yStart =  height - (int)(deltax*ratio) - 3*beamWidth - 2*beamGap;
									poly.addPoint(xStart,yStart);
									poly.addPoint(xStart,yStart + beamWidth);
								}else if(startNote.getWidth() == 2 * startNote.getNote(0).getWidth()){
									xStart = uiNoteList.get(startIndex).getX() + startNote.getNote(0).getWidth()- uiNoteList.get(0).getX();
									int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
									yStart =  height - (int)(deltax*ratio) - 3*beamWidth - 2*beamGap;						
									
									poly.addPoint(xStart,yStart);
									poly.addPoint(xStart,yStart + beamWidth);
								}
							
							}
							if(uiNoteList.get(endIndex) instanceof Note){
//								Note endNote = (Note) uiNoteList.get(endIndex);
								xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
								int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
								yEnd =  height - (int)(deltax*ratio) - 3*beamWidth - 2*beamGap;
								poly.addPoint(xEnd ,yEnd + beamWidth);
								poly.addPoint(xEnd ,yEnd);
								
							}else if(uiNoteList.get(endIndex) instanceof ChordNote){
								ChordNote endNote = (ChordNote) uiNoteList.get(endIndex);						
								if(endNote.getWidth() == endNote.getNote(0).getWidth()){
									xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
									int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
									yEnd =  height - (int)(deltax*ratio) - 3*beamWidth - 2*beamGap;
									poly.addPoint(xEnd ,yEnd + beamWidth);
									poly.addPoint(xEnd,yEnd);
									
								}else if(endNote.getWidth() == 2 * endNote.getNote(0).getWidth()){
									xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX()+ endNote.getNote(0).getWidth();
									int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
									yEnd =  height - (int)(deltax*ratio) - 3*beamWidth - 2*beamGap;
									poly.addPoint(xEnd,yEnd + beamWidth);
									poly.addPoint(xEnd,yEnd);
									
								}
							  
							}
//							int x = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
//							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
//							int y = getHeight() - (int)(deltax*ratio) - 3*beamWidth - 2*beamGap;
//							poly.addPoint(x, y);
//							poly.addPoint(x, y+beamWidth);
//							x = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
//							deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
//							y = getHeight() - (int)(deltax*ratio) - 3*beamWidth - 2*beamGap;
//							poly.addPoint(x, y+beamWidth);
//							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}//只有一个音符
						else if(!list.isEmpty()){
							int index = uiNoteList.indexOf(list.get(0));
							int x = 0;
							if(uiNoteList.get(0) instanceof Note){
								x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
							}else if(uiNoteList.get(0) instanceof ChordNote){
								int cNoteWidth = ((ChordNote) uiNoteList.get(0)).getNote(0).getWidth();
								if(uiNoteList.get(0).getWidth() == cNoteWidth){
									x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
								}else{
									x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX()+noteWidth;
								}
							}
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(index).getX();
							int y = getHeight() - (int)(deltax*ratio) - 3*beamWidth - 2*beamGap;
							Polygon poly = new Polygon();
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = x + noteWidth;
							y = y + (int)(noteWidth*ratio);//lwx
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}
					}
				}
				//处理最后一组音符
				if(!list.isEmpty() && list.size()!= 1){
					int startIndex = uiNoteList.indexOf(list.get(0));
					int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
					Polygon poly = new Polygon();
					int xStart = 0;
					int yStart = 0;
					int xEnd = 0;
					int yEnd = 0;
					if(uiNoteList.get(startIndex) instanceof Note){
//						Note startNote = (Note) uiNoteList.get(startIndex);
						xStart = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
						int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
						yStart =  height - (int)(deltax*ratio) - 3*beamWidth - 2*beamGap;
						poly.addPoint(xStart,yStart);
						poly.addPoint(xStart,yStart + beamWidth);
					}else if(uiNoteList.get(startIndex) instanceof ChordNote){
						ChordNote startNote = (ChordNote) uiNoteList.get(startIndex);						
						if(startNote.getWidth() == startNote.getNote(0).getWidth()){
							xStart = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
							yStart =  height - (int)(deltax*ratio) - 3*beamWidth - 2*beamGap;
							poly.addPoint(xStart,yStart);
							poly.addPoint(xStart,yStart + beamWidth);
						}else if(startNote.getWidth() == 2 * startNote.getNote(0).getWidth()){
							xStart = uiNoteList.get(startIndex).getX() + startNote.getNote(0).getWidth()- uiNoteList.get(0).getX();
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
							yStart =  height - (int)(deltax*ratio) - 3*beamWidth - 2*beamGap;						
							
							poly.addPoint(xStart,yStart);
							poly.addPoint(xStart,yStart + beamWidth);
						}
					
					}
					if(uiNoteList.get(endIndex) instanceof Note){
//						Note endNote = (Note) uiNoteList.get(endIndex);
						xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
						int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
						yEnd =  height - (int)(deltax*ratio) - 3*beamWidth - 2*beamGap;
						poly.addPoint(xEnd ,yEnd + beamWidth);
						poly.addPoint(xEnd ,yEnd);
						
					}else if(uiNoteList.get(endIndex) instanceof ChordNote){
						ChordNote endNote = (ChordNote) uiNoteList.get(endIndex);						
						if(endNote.getWidth() == endNote.getNote(0).getWidth()){
							xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
							yEnd =  height - (int)(deltax*ratio) - 3*beamWidth - 2*beamGap;
							poly.addPoint(xEnd ,yEnd + beamWidth);
							poly.addPoint(xEnd,yEnd);
							
						}else if(endNote.getWidth() == 2 * endNote.getNote(0).getWidth()){
							xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX()+ endNote.getNote(0).getWidth();
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
							yEnd =  height - (int)(deltax*ratio) - 3*beamWidth - 2*beamGap;
							poly.addPoint(xEnd,yEnd + beamWidth);
							poly.addPoint(xEnd,yEnd);
							
						}
					  
					}
					g.fillPolygon(poly);
					list.clear();
				}//只有最后一个音符
				else if(!list.isEmpty()){
					int index = uiNoteList.indexOf(list.get(0));
					int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
					int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(index).getX();
					int y = getHeight() - (int)(deltax*ratio) - 3*beamWidth - 2*beamGap;
					Polygon poly = new Polygon();
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = x - noteWidth;
					y = y - (int)(noteWidth*ratio); //lwx
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}
				//第三条符杠,32分音符
				/*******************************************************/
				for(int i = 0; i < uiNoteList.size(); i++){
					if(uiNoteList.get(i).getDuration() <= 256/64){
						list.add(uiNoteList.get(i));
					}else{
						//如果不止一个音符
						if(!list.isEmpty() && list.size()!= 1){
							int startIndex = uiNoteList.indexOf(list.get(0));
							int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
							Polygon poly = new Polygon();
							int xStart = 0;
							int yStart = 0;
							int xEnd = 0;
							int yEnd = 0;
							if(uiNoteList.get(startIndex) instanceof Note){
//								Note startNote = (Note) uiNoteList.get(startIndex);
								xStart = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
								int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
								yStart =  height - (int)(deltax*ratio) - 4*beamWidth - 3*beamGap;
								poly.addPoint(xStart,yStart);
								poly.addPoint(xStart,yStart + beamWidth);
							}else if(uiNoteList.get(startIndex) instanceof ChordNote){
								ChordNote startNote = (ChordNote) uiNoteList.get(startIndex);						
								if(startNote.getWidth() == startNote.getNote(0).getWidth()){
									xStart = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
									int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
									yStart =  height - (int)(deltax*ratio) - 4*beamWidth - 3*beamGap;
									poly.addPoint(xStart,yStart);
									poly.addPoint(xStart,yStart + beamWidth);
								}else if(startNote.getWidth() == 2 * startNote.getNote(0).getWidth()){
									xStart = uiNoteList.get(startIndex).getX() + startNote.getNote(0).getWidth()- uiNoteList.get(0).getX();
									int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
									yStart =   height - (int)(deltax*ratio) - 4*beamWidth - 3*beamGap;						
									
									poly.addPoint(xStart,yStart);
									poly.addPoint(xStart,yStart + beamWidth);
								}
							
							}
							if(uiNoteList.get(endIndex) instanceof Note){
//								Note endNote = (Note) uiNoteList.get(endIndex);
								xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
								int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
								yEnd =  height - (int)(deltax*ratio) - 4*beamWidth - 3*beamGap;
								poly.addPoint(xEnd ,yEnd + beamWidth);
								poly.addPoint(xEnd ,yEnd);
								
							}else if(uiNoteList.get(endIndex) instanceof ChordNote){
								ChordNote endNote = (ChordNote) uiNoteList.get(endIndex);						
								if(endNote.getWidth() == endNote.getNote(0).getWidth()){
									xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
									int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
									yEnd =  height - (int)(deltax*ratio) - 4*beamWidth - 3*beamGap;
									poly.addPoint(xEnd ,yEnd + beamWidth);
									poly.addPoint(xEnd,yEnd);
									
								}else if(endNote.getWidth() == 2 * endNote.getNote(0).getWidth()){
									xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX()+ endNote.getNote(0).getWidth();
									int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
									yEnd =  height - (int)(deltax*ratio) - 4*beamWidth - 3*beamGap;
									poly.addPoint(xEnd,yEnd + beamWidth);
									poly.addPoint(xEnd,yEnd);
									
								}
							  
							}
//							int x = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
//							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
//							int y = getHeight() - (int)(deltax*ratio) - 4*beamWidth - 3*beamGap;
//							poly.addPoint(x, y);
//							poly.addPoint(x, y+beamWidth);
//							x = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
//							deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
//							y = getHeight() - (int)(deltax*ratio) - 4*beamWidth - 3*beamGap;
//							poly.addPoint(x, y+beamWidth);
//							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}//只有一个音符
						else if(!list.isEmpty()){
							int index = uiNoteList.indexOf(list.get(0));
							int x = 0;
							if(uiNoteList.get(0) instanceof Note){
								x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
							}else if(uiNoteList.get(0) instanceof ChordNote){
								int cNoteWidth = ((ChordNote) uiNoteList.get(0)).getNote(0).getWidth();
								if(uiNoteList.get(0).getWidth() == cNoteWidth){
									x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
								}else{
									x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX()+noteWidth;
								}
							}
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(index).getX();
							int y = getHeight() - (int)(deltax*ratio) - 4*beamWidth - 3*beamGap;
							Polygon poly = new Polygon();
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = x + noteWidth;
							y = y + (int)(noteWidth*ratio); //lwx
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}
					}
				}
				//处理最后一组音符
				if(!list.isEmpty() && list.size()!= 1){
					int startIndex = uiNoteList.indexOf(list.get(0));
					int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
					Polygon poly = new Polygon();
					int xStart = 0;
					int yStart = 0;
					int xEnd = 0;
					int yEnd = 0;
					if(uiNoteList.get(startIndex) instanceof Note){
//						Note startNote = (Note) uiNoteList.get(startIndex);
						xStart = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
						int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
						yStart =  height - (int)(deltax*ratio) - 4*beamWidth - 3*beamGap;
						poly.addPoint(xStart,yStart);
						poly.addPoint(xStart,yStart + beamWidth);
					}else if(uiNoteList.get(startIndex) instanceof ChordNote){
						ChordNote startNote = (ChordNote) uiNoteList.get(startIndex);						
						if(startNote.getWidth() == startNote.getNote(0).getWidth()){
							xStart = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
							yStart =  height - (int)(deltax*ratio) - 4*beamWidth - 3*beamGap;
							poly.addPoint(xStart,yStart);
							poly.addPoint(xStart,yStart + beamWidth);
						}else if(startNote.getWidth() == 2 * startNote.getNote(0).getWidth()){
							xStart = uiNoteList.get(startIndex).getX() + startNote.getNote(0).getWidth()- uiNoteList.get(0).getX();
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
							yStart =   height - (int)(deltax*ratio) - 4*beamWidth - 3*beamGap;						
							
							poly.addPoint(xStart,yStart);
							poly.addPoint(xStart,yStart + beamWidth);
						}
					
					}
					if(uiNoteList.get(endIndex) instanceof Note){
//						Note endNote = (Note) uiNoteList.get(endIndex);
						xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
						int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
						yEnd =  height - (int)(deltax*ratio) - 4*beamWidth - 3*beamGap;
						poly.addPoint(xEnd ,yEnd + beamWidth);
						poly.addPoint(xEnd ,yEnd);
						
					}else if(uiNoteList.get(endIndex) instanceof ChordNote){
						ChordNote endNote = (ChordNote) uiNoteList.get(endIndex);						
						if(endNote.getWidth() == endNote.getNote(0).getWidth()){
							xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
							yEnd =  height - (int)(deltax*ratio) - 4*beamWidth - 3*beamGap;
							poly.addPoint(xEnd ,yEnd + beamWidth);
							poly.addPoint(xEnd,yEnd);
							
						}else if(endNote.getWidth() == 2 * endNote.getNote(0).getWidth()){
							xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX()+ endNote.getNote(0).getWidth();
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
							yEnd =  height - (int)(deltax*ratio) - 4*beamWidth - 3*beamGap;
							poly.addPoint(xEnd,yEnd + beamWidth);
							poly.addPoint(xEnd,yEnd);
							
						}
					  
					}
					g.fillPolygon(poly);
					list.clear();
				}//只有最后一个音符
				else if(!list.isEmpty()){
					int index = uiNoteList.indexOf(list.get(0));
					int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
					int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(index).getX();
					int y = getHeight() - (int)(deltax*ratio) - 4*beamWidth - 3*beamGap;
					Polygon poly = new Polygon();
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = x - noteWidth;
					y = y - (int)(noteWidth*ratio); //lwx
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}
				//第四条符杠，64分音符
			}
		}
	}
	
	/**
	 * 放置符杠
	 */
	public void locate(){
		int stemLength = NoteCanvas.STEM_LENGTH;
		int noteWidth = Note.NORMAL_HEAD_WIDTH;
		if(uiNoteList.get(0).getHighestNote() instanceof Grace){
			stemLength = Stem.GRACE_HEIGHT;
			noteWidth = Gracable.GRACE_WIDTH;
		}
		if(upOrDown.equalsIgnoreCase("up")){
			int x = uiNoteList.get(0).getX() + noteWidth;
			int y = MusicMath.getHighestNote(uiNoteList).getY() - stemLength;
			setLocation(x, y);
		}else if(upOrDown.equalsIgnoreCase("down")){
			int x = uiNoteList.get(0).getX();
			int y = MusicMath.getLowestNote(uiNoteList).getY() + stemLength - getHeight();
			setLocation(x, y);
		}
	}
	
	/**
	 * 返回指定位置的符杠所对应的音符时长
	 * @param index
	 * @return
	 */
	public static int getDurByBeamIndex(int index){
		switch(index){
		case 0:
			return 32;
		case 1:
			return 16;
		case 2:
			return 8;
		case 3:
			return 4;
		default:
			return -1;
		}
	}

}
