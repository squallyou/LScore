package sjy.elwg.notation.musicBeans;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;

import sjy.elwg.notation.NoteCanvas;
import sjy.elwg.notation.musicBeans.symbolLines.NoteSymbol;
import sjy.elwg.notation.musicBeans.symbolLines.Tie;
import sjy.elwg.utility.MusicMath;

/**
 * 普通音符类
 * 乐理中所有的休止符、音符都属于音符类
 * 为了音符的位置处理方便，通常采用音符的位置音高. 但音符中保存有音符的真实音高RealPitch.
 * 音符的产生有两种情况：
 * 1. 用户编辑产生，生成的音符具有位置音高.但不具有真实音高,因为其真实音高的确定需要其所在小节的谱号等属性.
 *    当音符添加到某个小节时，即调用measure.addNote(note)时，音符会根据所添加的小节确定其真实音高.这也
 *    是将Measure的noteList封装起来的，不允许用户直接操作的原因，用户向小节添加音符只能通过measure.addNote()
 *    方法，而不能通过measure.getNoteList().add(note).
 * 2. 数据导入产生, 导入的MusicXML文件中包含音符的绝对音高，但音符构造函数仅允许位置音高，因此这时需要先
 *    将绝对音高转换为位置音高.
 * @author jingyuan.sun
 *
 */
public class Note extends AbstractNote implements Equalable{
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 7L;
	
	public final static int FULL_HEAD_WIDTH = 17;
	public final static int NORMAL_HEAD_WIDTH = 12;
	public final static int HEAD_HEIGHT = 16;
	public final static int REST1_WIDTH = 12;
	public final static int REST1_HEIGHT = 18;
	public final static int REST2_WIDTH = 12; 
	public final static int REST2_HEIGHT = 16;
	public final static int REST4_WIDTH = 10; 
	public final static int REST4_HEIGHT = 40;
	public final static int REST8_WIDTH = 10; 
	public final static int REST8_HEIGHT = 40;
	public final static int REST16_WIDTH = 15;
	public final static int REST16_HEIGHT = 40;
	public final static int REST32_WIDTH = 15;
	public final static int REST32_HEIGHT = 40;
	public final static int REST64_WIDTH = 15;
	public final static int REST64_HEIGHT = 40;
	public final static int REST128_WIDTH = 15;
	public final static int REST128_HEIGHT = 40;
	public final static int ORNAMENT_HEIGHT = 10;
	
	/**
	 * 音高,以音符在最下一根线上为0,每半个音阶增加1，则音符在第一根线上为8.
	 */
	protected int pitch;
	/**
	 * 是否休止符
	 */
	protected boolean isRest;
	/**
	 * 是否全音符.通常与isRest搭配使用来判断是否是全休止符.
	 */
	protected boolean isFull;
	/**
	 * 升降符号实体
	 */
	protected SharpOrFlat sharpOrFlat;

	/**
	 * 真实音高
	 */
	protected RealPitch realPitch;
	/**
	 * 当音符位置超出五线谱时，会有用于指示音符位置的短线.
	 * 该list保存当前音符的短信集合.
	 */
	private ArrayList<JPanel> positionLines = new ArrayList<JPanel>();
	/**
	 * 该音符所属的和弦音符,没有则为空
	 */
	private ChordNote chordNote;
	/**
	 * 附点实体
	 */
	private UIDot uiDot;
	/**
	 * 连音线. 包含与该音符相关的所有连音线，通常最多包含两个，一个以该音符作为起点，另一个以该音符作为终点.
	 */
	private ArrayList<Tie> ties = new ArrayList<Tie>();
	/**
	 * 是否被选中
	 */
	protected boolean selected;
	/**
	 * 是否被隐藏，主要应用于副声部休止符.
	 * 当且仅当该音符为休止符，且不属于小节的主声部，同时该属性为true时，音符会被隐藏.
	 * 但乐谱的逻辑操作时，该音符依然会被计算在内
	 */
	private boolean isHidden;
	/**
	 * 标注集合，所有对该音符进行的标注
	 */
	private ArrayList<Annotation> annotations = new ArrayList<Annotation>();
	
	/**
	 * 空构造函数.
	 * 注意：除导入XML文件之外，其他任何时候都应该避免使用空构造函数。
	 */
	public Note(){
		super(64);
		setOpaque(false);
		setLayout(null);
		adjSize();
		setFocusable(true);
	}
	
	/**
	 * 普通带附点音符构造函数
	 * @param duration 时长
	 * @param pitch 音高
	 * @param dotNum 附点个数
	 */
	public Note(int duration, int pitch, int dotNum){
		super(duration);
		this.pitch = pitch;
		this.dotNum = dotNum;
		beamType = duration < 64 ? "default" : "none";
		setOpaque(false);
		setLayout(null);
		adjSize();
		repaint();
		setFocusable(true);
		generateUIDot();
	}
	
	/**
	 * 普通不带附点音符构造函数
	 * @param duration 时长
	 * @param pitch 音高
	 */
	public Note(int duration, int pitch){
		super(duration);
		this.pitch = pitch;
		beamType = duration < 64 ? "default" : "none";
		setOpaque(false);
		setLayout(null);
		adjSize();
		repaint();
		setFocusable(true);
	}
	
	/**
	 * 带附点休止符构造函数
	 * @param duration 时长
	 * @param dotNum 附点个数
	 * @param isFull 是否全休止符
	 */
	public Note(int duration, int dotNum, boolean isFull){
		super(duration);
		this.dotNum = dotNum;
		this.isFull = isFull;
		isRest = true;
		beamType = "none";
		setOpaque(false);
		setLayout(null);
		adjSize();
		repaint();
		setFocusable(true);
		generateUIDot();
	}
	
	/**
	 * 不带附点休止符构造函数
	 * @param duration 时长
	 * @param isFull 是否全休止符
	 */
	public Note(int duration, boolean isFull){
		super(duration);
		this.isFull = isFull;
		isRest = true;
		beamType = "none";
		setOpaque(false);
		setLayout(null);
		adjSize();
		repaint();
		setFocusable(true);
	}
	
	/**
	 * 设置时长.
	 * 对于时长的说明见其父类 AbstractNote
	 */
	public void setDuration(int duration){
		super.setDuration(duration);
		adjSize();
		repaint();
	}
	
	/**
	 * 获得位置音高
	 * @return
	 */
	public int getPitch() {
		return pitch;
	}

	/**
	 * 设置位置音高
	 * @param pitch
	 */
	public void setPitch(int pitch) {
		this.pitch = pitch;
	}

	/**
	 * 是否是休止符
	 * @return
	 */
	public boolean isRest() {
		return isRest;
	}

	/**
	 * 设置休止符
	 * @param isRest
	 */
	public void setRest(boolean isRest) {
		this.isRest = isRest;
		beamType = "none";
		adjSize();
		repaint();
	}

	/**
	 * 返回是否全休止符
	 * @return
	 */
	public boolean isFull() {
		return isFull;
	}

	/**
	 * 设置全休止符
	 * @param isFull
	 */
	public void setFull(boolean isFull) {
		this.isFull = isFull;
		adjSize();
		repaint();
	}

	/**
	 * 调整音符大小
	 */
	public void adjSize(){
		if(!isRest){
			if(duration == 256) 
				setSize(FULL_HEAD_WIDTH, HEAD_HEIGHT);
			else setSize(NORMAL_HEAD_WIDTH, HEAD_HEIGHT);
		}else{
			switch(duration)
			{
			case 256: setSize(REST1_WIDTH, REST1_HEIGHT); break;
			case 128: setSize(REST2_WIDTH, REST2_HEIGHT); break;
			case 64: setSize(REST4_WIDTH, REST4_HEIGHT); break;
			case 32: setSize(REST8_WIDTH, REST8_HEIGHT); break;
			case 16: setSize(REST16_WIDTH, REST16_HEIGHT); break;
			case 8: setSize(REST32_WIDTH, REST32_HEIGHT); break;
			case 4: setSize(REST64_WIDTH, REST64_HEIGHT); break;
			case 2: setSize(REST128_WIDTH, REST128_HEIGHT); break;
			default:
				setSize(REST1_WIDTH, REST1_HEIGHT);
				System.err.println("Invalid durDiv!");
			}
		}
		if(dotNum == 1){
			setSize(getWidth()+4, getHeight());
		}else if(dotNum == 2){
			setSize(getWidth()+6, getHeight());
		}
	}
	
	public void paintComponent(Graphics gg){
		super.paintComponent(gg);
		//被隐藏
		if(voice > 0 && isHidden && isRest){
			return;
		}
		Graphics2D g = (Graphics2D) gg;	
		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
	    	renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		g.setRenderingHints(renderHints);
		g.setFont(NoteCanvas.MCORE_FONT.deriveFont(35f));
		int voice = chordNote == null ? this.voice : chordNote.getVoice();
		if(selected && voice == 0) 
			g.setColor(Color.blue);
		else if(selected && voice == 1)
			g.setColor(Color.green);
		else 
			g.setColor(Color.black);
		if(!isRest){
			if(duration == 256){
				g.drawString("\uE12B", 0, 8);
			}else if(duration == 128){
				g.drawString("\uE12C", 0, 8);
			}else{
				g.drawString("\uE12D", 0, 8);
			}
		}else{
			if(isFull){
				g.drawString("\uE101", 0, 5);
			}else if(duration == 128){
				g.drawString("\uE100", 0, 4);
			}else if(duration == 64){
				g.drawString("\uE107", 0, 20);
			}else if(duration == 32){
				g.drawString("\uE109", 0, 20);
			}else if(duration == 16){
				g.drawString("\uE10A", 3, 20);
			}else if(duration == 8){
				g.drawString("\uE10B", 3, 20);
			}else if(duration == 4){
				g.drawString("\uE10C", 3, 20);
			}else if(duration == 2){
				g.drawString("\uE10D", 3, 20);
			}else{
				g.drawString("\uE101", 0, 5);
			}
		}
		/*if(dotNum > 0){
			if(dotNum == 1){
				g.fillPolygon(new int[]{getWidth()-3, getWidth()-2, getWidth()-1, getWidth()-1, getWidth()-2, getWidth()-3}, new int[]{getHeight()/2-2, getHeight()/2-2, getHeight()/2-2, getHeight()/2, getHeight()/2, getHeight()/2}, 6);
			}
			else if(dotNum == 2){
				g.fillPolygon(new int[]{getWidth()-6, getWidth()-5, getWidth()-4, getWidth()-4, getWidth()-5, getWidth()-6}, new int[]{getHeight()/2-2, getHeight()/2-2, getHeight()/2-2, getHeight()/2, getHeight()/2, getHeight()/2}, 6);
				g.fillPolygon(new int[]{getWidth()-3, getWidth()-2, getWidth()-1, getWidth()-1, getWidth()-2, getWidth()-3}, new int[]{getHeight()/2-2, getHeight()/2-2, getHeight()/2-2, getHeight()/2, getHeight()/2, getHeight()/2}, 6);
			}
		}*/
	}
	
	/**
	 * 在逻辑上删除和弦音符所有的符号
	 * @param withTup 是否删除连音号
	 */
	public void removeAllSymbols(boolean withTup){
		super.removeAllSymbols(withTup);
		
		if(sharpOrFlat != null){
			sharpOrFlat.setNote(null);
			sharpOrFlat = null;
		}

		
		if(!ties.isEmpty()){
			for(int i = 0; i < ties.size(); i++){
				Tie tie = ties.get(i);
				
				Note startNote = tie.getStartNote();
				Note endNote = tie.getEndNote();
				if(startNote != null){
					startNote.removeTie(tie);
					tie.setStartNote(null);
				}
				if(endNote != null){
					endNote.removeTie(tie);
					tie.setEndNote(null);
				}
				//前一个片段
				Tie preTie = (Tie)tie.getPreSymbolLine();
				if(preTie != null){
					Note snote = preTie.getStartNote();
					Note enote = preTie.getEndNote();
					if(snote != null){
						snote.removeTie(preTie);
						preTie.setStartNote(null);
					}
					if(enote != null){
						enote.removeTie(preTie);
						preTie.setEndNote(null);
					}
					preTie.setNextSymbolLine(null);
					tie.setPreSymbolLine(null);
				}
				//下一个片段
				Tie nxtTie = (Tie)tie.getNextSymbolLine();
				if(nxtTie != null){
					Note snote = nxtTie.getStartNote();
					Note enote = nxtTie.getEndNote();
					if(snote != null){
						snote.removeTie(nxtTie);
						nxtTie.setStartNote(null);
					}
					if(enote != null){
						enote.removeTie(nxtTie);
						nxtTie.setEndNote(null);
					}
					nxtTie.setPreSymbolLine(null);
					tie.setNextSymbolLine(null);
				}
			}
		}
	}
     
	
	@Override
	public void locateNote(int x) {
		// TODO Auto-generated method stub
		if(isRest){
//			setLocation(x, measure.getY() + 2*NoteCanvas.LINE_GAP - getHeight()/2);
			setLocation(x, measure.getY() + 2*NoteCanvas.LINE_GAP - 
					NoteCanvas.LINE_GAP*pitch/2 - getHeight()/2);
		}else{
			setLocation(x, measure.getY() + 4*NoteCanvas.LINE_GAP - 
					NoteCanvas.LINE_GAP*pitch/2 - getHeight()/2);
		}
		refreshPosLines();
	}
	
	public int sharpOrFlatWidth(){
		return sharpOrFlat == null ? 0 : sharpOrFlat.getWidth();
	}
	
	public int dotWidth(){
		return uiDot == null ? 0 : uiDot.getWidth();
	}
	
	public boolean absHigherThan(AbstractNote note){
		if(note instanceof Note){
			Note nnote = (Note)note;
			return pitch > nnote.getPitch();
		}
		else if(note instanceof ChordNote){
			ChordNote cnote = (ChordNote)note;
			for(Note n : cnote.getNoteList()){
				if(pitch <= n.getPitch())
					return false;
			}
			return true;
		}
		return false;
	}
	
	public boolean absLowerThan(AbstractNote note){
		if(note instanceof Note){
			Note nnote = (Note)note;
			return pitch < nnote.getPitch();
		}
		else if(note instanceof ChordNote){
			ChordNote cnote = (ChordNote)note;
			for(Note n : cnote.getNoteList()){
				if(pitch >= n.getPitch())
					return false;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 根据音符的真实音高，计算位置音高
	 */
	public void determinePitchByRealPitch(String clefType){
		int pitch = MusicMath.getNotePitchByRealPitch(realPitch.getOctave(), realPitch.getStep(), clefType);
		this.pitch = pitch;
	}
	
	/**
	 * 根据音符位置音高以及所在小节的信息确定音符的实际音高
	 * 该方法必须在音符被添加进小节之后调用
	 */
	public void determineRealPitch(){
		Measure tmeasure = chordNote == null ? measure : chordNote.getMeasure();
		if(tmeasure == null)
			return;
		
		int octave = MusicMath.getOctave(this, tmeasure);
		int alter = MusicMath.getAlter(this, tmeasure);
		String step = MusicMath.getStep(this, tmeasure);
		
		if(realPitch == null){
			realPitch = new RealPitch(octave, step, alter);
		}else{
			realPitch.setAlter(alter);
			realPitch.setOctave(octave);
			realPitch.setStep(step);
		}
	}
	
	/**
	 * 根据小节或者自身升降号属性，重置音符真实音高中的Alter值
	 */
	public void resetAlter(){
		Measure tmeasure = chordNote == null ? measure : chordNote.getMeasure();
		int alter = MusicMath.getAlter(this, tmeasure);
		if(realPitch != null){
			realPitch.setAlter(alter);
		}
	}

	/**
	 * 获得音符的真实音高信息，包括八度、音阶及变调
	 * @return
	 */
	public RealPitch getRealPitch() {
		return realPitch;
	}

	/**
	 * 设置真实音高,包括八度、音阶及变调
	 * @param realPitch
	 */
	public void setRealPitch(RealPitch realPitch) {
		this.realPitch = realPitch;
	}

	/**
	 * 获得升降调号实体
	 * @return
	 */
	public SharpOrFlat getSharpOrFlat() {
		return sharpOrFlat;
	}

	/**
	 * 设置升降号调实体
	 * @param sharpOrFlat
	 */
	public void setSharpOrFlat(SharpOrFlat sharpOrFlat) {
		this.sharpOrFlat = sharpOrFlat;
	}
	
	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	/**
	 * 放置音符的各种符号
	 */
	@Override
	public void locateNoteSymbols() {
		// TODO Auto-generated method stub
		super.locateNoteSymbols();
		// 升降号
		if (sharpOrFlat != null) {
			sharpOrFlat.setLocation(getX() - sharpOrFlat.getWidth() + 2, getY()
					- sharpOrFlat.getHeight() / 3);
		}
		
		// 演奏记号与装饰音
		if(ornaments.size() != 0){
			//音符符杆的纵坐标
			int sy = getStem() == null ? getY() : getStem().getY();
			int uy1 = getY() - 2 - (8 - pitch) * 5;
			int dy1 = getY() + NoteCanvas.LINE_GAP + 2 + (pitch - 1) * 5;
			int uy2 = getY() - 3 - (8 - pitch) * 5;
			int dy2 = getY() + NoteCanvas.LINE_GAP + 1 + pitch * 5;
			int uy3 = getY() - 10;
			int dy3 = getY() + 10 + 2;
			for (int i = 0, n = ornaments.size(); i < n; i++) {
				NoteSymbol nsl = ornaments.get(i);
				int dragY = nsl.getDraggedY();
				// 当音高在2到6之间时，除了staccato以外的符号出现在五线谱上方或下方的固定位置
				if (pitch >= 2 && pitch <= 6) {
					// 当音高在3到5之间时，各个音符出现在五线谱上的位置的确定，staccato符号具有特殊性需要单独考虑
					//音符符杆的方向也会决定符号的放置
					if (pitch >= 3 && pitch <= 5) {
	                     //有staccato符号时，该符号应该放在五线谱内
						if (nsl.getSymbolType().equals("staccato")) {
							if (sy > getY()) {
								nsl.setLocation(getX(), this.getMeasure().getY() + dragY);
							} else if (sy < getY()) {
								nsl.setLocation(getX(), this.getMeasure().getY() + 30 + dragY);
							}
						}else if(nsl.getSymbolType().equals("staccatoTenutoUp")||nsl.getSymbolType().equals("staccatoTenutoDown")){
							if (sy > getY()) {
								nsl.setLocation(getX(), uy1 + dragY - 7);
								uy1 -= 10;
							} else if (sy < getY()) {
									nsl.setLocation(getX(), dy1  + dragY);
									dy1 += 10;
							}
						}else if(nsl.getSymbolType().equals("fermata")){
							if (sy > getY()) {
								nsl.setLocation(getX() - 3, uy1 + dragY - 9);
								uy1 -= 10;
							} else if (sy < getY()) {
									nsl.setLocation(getX() - 3, dy1  + dragY);
									dy1 += 10;
							}
						}else {
							if (sy > getY()) {
								nsl.setLocation(getX(), uy1 + dragY);
								uy1 -= 10;
							} else if (sy < getY()) {
									nsl.setLocation(getX(), dy1  + dragY);
									dy1 += 10;
							}
						}
					} else {
						//音高为2或者6时
						if(nsl.getSymbolType().equals("staccatoTenutoUp")||nsl.getSymbolType().equals("staccatoTenutoDown")){
							if (sy > getY()) {
								nsl.setLocation(getX(), uy2 + dragY - 7);
								uy2 -= 10;
							} else if (sy < getY()) {
									nsl.setLocation(getX(), dy2 + dragY);
									dy2 += 10;
							}
						}else if(nsl.getSymbolType().equals("fermata")){
							if (sy > getY()) {
								nsl.setLocation(getX() - 3, uy2 + dragY - 9);
								uy2 -= 10;
							} else if (sy < getY()) {
									nsl.setLocation(getX() - 3, dy2 + dragY);
									dy2 += 10;
							}	
						}else{
							if (sy > getY()) {
								nsl.setLocation(getX(), uy2 + dragY);
								uy2 -= 10;
							} else if (sy < getY()) {
									nsl.setLocation(getX(), dy2 + dragY);
									dy2 += 10;
							}
						}
					}
				}else{
					//音高小于2或者大于1时
					if(nsl.getSymbolType().equals("staccatoTenutoUp")||nsl.getSymbolType().equals("staccatoTenutoDown")){
						if (sy > getY()) {
							nsl.setLocation(getX(), uy3 - 7);
							uy3 -= 10;
						} else if (sy < getY()) {
							nsl.setLocation(getX(), dy3+ 2);
							dy3 += 10;							
						}
					}else if(nsl.getSymbolType().equals("fermata")){
						if (sy > getY()) {
							nsl.setLocation(getX() - 3, uy3 - 9);
							uy3 -= 10;
						} else if (sy < getY()) {
							nsl.setLocation(getX() - 3, dy3+ 2);
							dy3 += 10;							
						}
					}else{
						if (sy > getY()) {
							nsl.setLocation(getX(), uy3);
							uy3 -= 10;
						} else if (sy < getY()) {
							nsl.setLocation(getX(), dy3+ 2);
							dy3 += 10;							
						}
					} 
				}
			}
		}


		//力度记号
		if(dynamics.size() != 0){
			
			for(int i = 0, n = dynamics.size(); i < n; i++){
				NoteSymbol nsl = dynamics.get(i);
				int dragX = nsl.getDraggedX();
				int dragY = nsl.getDraggedY();
				if(getPitch() >= 10){
					nsl.setLocation(getX() + dragX -25,
							getY()- 15 + dragY);
				}else{
					nsl.setLocation(getX() + dragX - 10,
							measure.getY() - 30 + dragY);
				}
			}
		}
		//表情术语
		if(performanceSymbols.size() != 0){
			for(int i = 0, n = performanceSymbols.size(); i < n; i++){
				NoteSymbol nsl = performanceSymbols.get(i);
				int dragX = nsl.getDraggedX();
				int dragY = nsl.getDraggedY();
				if(getPitch() >= 10){
					nsl.setLocation(getX() + dragX -25,
							getY()- 15 + dragY);
				}else{
					nsl.setLocation(getX() + dragX - 10,
							measure.getY() - 30 + dragY);
				}
			}
		}
		//装饰音符号
		if(graceSymbols.size() != 0){	
			for(int i = 0, n = graceSymbols.size(); i < n; i++){
				NoteSymbol nsl = graceSymbols.get(i);
				int dragY = nsl.getDraggedY();
				String gType = nsl.getSymbolType();
				if(gType.equalsIgnoreCase("trill-sharp")||gType.equalsIgnoreCase("trill-flat")||gType.equalsIgnoreCase("trill-natural")){
					nsl.setLocation(getX()- 2, this.getMeasure().getY() - 30 + dragY);
				}else{
					nsl.setLocation(getX()- 2, this.getMeasure().getY() - 15 + dragY);
				}
			
//					} else if (getStem().getY()< getY()) {
//						nsl.setLocation(getX()- 2, this.getMeasure().getY() - 10 + dragY);
//					}
//				}else{
//					if (getStem().getY() > getY()) {
//						nsl.setLocation(getX()- 2, getY()-10 + dragY);
//					} else if (getStem().getY()< getY()) {
//						nsl.setLocation(getX()- 2, getY() + 10 + dragY);
//					}
//				}
			}
		}
		
		//呼吸记号
		if(breath != null){
			int dragX = breath.getDraggedX();
			int dragY = breath.getDraggedY();
			if(this.getY()<this.getMeasure().getY()){
				breath.setLocation(getX()+12+ dragX, getY()-25+ dragY);
			}else{
				breath.setLocation(getX()+12+ dragX,this.getMeasure().getY()-29+ dragY);
			}
		
	
		}
		
		if(pedal != null){
			int dragX = pedal.getDraggedX();
			int dragY = pedal.getDraggedY();
			if(this.getPitch() > 2){
				pedal.setLocation(getX()+ dragX, this.getMeasure().getY()+40+ dragY);
			}else{
				pedal.setLocation(getX()+ dragX,getY()+15+ dragY);
			}
		}
		//附点
		if(uiDot != null){
			if(pitch % 2 == 0)
				uiDot.setLocation(getX() + getWidth(), getY());
			else 
				uiDot.setLocation(getX() + getWidth(), getY() + NoteCanvas.LINE_GAP/2);
		}
		//歌词
		if(lyrics.size() != 0){
			Measure measure = chordNote == null ? this.measure : chordNote.getMeasure();		
			int yy = measure.getY() + Measure.MEASURE_HEIGHT + NoteLine.MEASURE_GAP/3;
			for(int i = 0; i < lyrics.size(); i++){
				if(lyrics.get(i) != null){
					int xx = getX() - (lyrics.get(i).getWidth() - getWidth()) / 2;
					lyrics.get(i).setLocation(xx + lyrics.get(i).draggedX, yy + lyrics.get(i).draggedY);
				}
				yy += Lyrics.LYRIC_FONT.getSize() + 2;
			}
		}
		//连音号
		if(tuplet != null){
			tuplet.locateTuplet();
		}
		//注释
		if(!annotations.isEmpty()){
			for(Annotation an : annotations){
				if(an.getRelatedObjts().get(0) == this){
					an.setLocation(getX() + getWidth() + an.getDraggedX(),
							getY() - an.getHeight() - NoteCanvas.LINE_GAP + an.getDraggedY());
				}
			}
		}
		//在符干上的颤音
		if (tremoloBeam != null) {
			if (this.getDuration() == 256) {
				tremoloBeam.setLocation(getX() + 3, getY() - 10);
			} else {
				if (getStem().getY() > getY()) {
					if (tremoloBeam.getSymbolType().equals("tremoloBeam1")) {
						tremoloBeam.setLocation(getX() - getWidth() / 2 + 2, getStem().getY() + getStem().getHeight() / 2);
					} else if (tremoloBeam.getSymbolType().equals("tremoloBeam2")) {
						tremoloBeam.setLocation(getX() - getWidth() / 2 + 2, getStem().getY() + getStem().getHeight() / 2 - 3);
					} else if (tremoloBeam.getSymbolType().equals("tremoloBeam3")) {
						tremoloBeam.setLocation(getX() - getWidth() / 2 + 2, getStem().getY() + getStem().getHeight() / 2 - 6);
					}
				} else if (getStem().getY() < getY()) {
					if (tremoloBeam.getSymbolType().equals("tremoloBeam1")) {tremoloBeam.setLocation(getX() + getWidth() / 2 + 1,
								getStem().getY() + getStem().getHeight() / 2);
					} else if (tremoloBeam.getSymbolType().equals("tremoloBeam2")) {
						tremoloBeam.setLocation(getX() + getWidth() / 2 + 1,
								getStem().getY() + getStem().getHeight() / 2 - 3);
					} else if (tremoloBeam.getSymbolType().equals("tremoloBeam3")) {
						tremoloBeam.setLocation(getX() + getWidth() / 2 + 1, getStem().getY() + getStem().getHeight() / 2 - 6);
					}
				}
			}
		}

	}
	
	/**
	 * 是否与另一个音符相同
	 */
	public boolean equalsWith(Object o){
		if(!(o instanceof Note)){
			System.out.println("不是音符");
			return false;
		}
		Note note = (Note)o;
		boolean basicSame1 = note.getPitch()==pitch && note.getDuration()==duration && note.isRest()==isRest &&
		      note.getDotNum()==dotNum && note.hasSharpOrFlat()==hasSharpOrFlat();
		if(basicSame1){
			boolean basicSame2 = false;
			if(note.hasSharpOrFlat()){
				basicSame2 = note.getSharpOrFlat().getType().equals(sharpOrFlat.getType());
				if(!basicSame2){
					System.out.println("升降号类型不一致");
					return false;
				}
			}
			return true;
		}
		System.out.println(note.getPitch() + " " + note.getDuration() + " " + note.isRest());
		System.out.println("basicSame1为false");
		return false;
	}
	

	/**
	 * 是否具有升降号
	 * @return
	 */
	public boolean hasSharpOrFlat(){
		return !(sharpOrFlat == null);
	}
	
	/**
	 * 刷新音符的附加线
	 */
	public void refreshPosLines(){
		Measure tmeasure = chordNote == null ? measure : chordNote.getMeasure();
		refreshPosLines(tmeasure);
	}
	
	/**
	 * 刷新音符附加线
	 * @param measure
	 */
	public void refreshPosLines(Measure measure){
		//相对指定小节的音高
		int pitch = Math.round((measure.getY() + Measure.MEASURE_HEIGHT - (getY() + getHeight()/2)) * 2 / NoteCanvas.LINE_GAP);
		
		if((pitch <= 8 && pitch >= 0 && positionLines.isEmpty()) || isRest)
			return;
		
		if(!positionLines.isEmpty()){
			for(JPanel line : positionLines){
				JComponent parent = (JComponent)line.getParent();
				if(parent != null)
					parent.remove(line);
			}
			positionLines.clear();
		}
		
		if(pitch < 0 || pitch > 8){
			int num = pitch > 8 ? (pitch - 8)/2 : (0 - pitch)/2 + 1;
			boolean up = pitch > 8 ? true : false;
			for(int i = 0; i < num; i++){
				JPanel line = new ShortLine();
				positionLines.add(line);
				if(getParent() != null){
					((JComponent)getParent()).add(line);
				}
				if(measure == null)
					return;
				if(up)
					line.setLocation(getX() - 2, measure.getY() - (i + 1) * NoteCanvas.LINE_GAP);
				else
					line.setLocation(getX() - 2, measure.getY() + Measure.MEASURE_HEIGHT + (i + 1) * NoteCanvas.LINE_GAP - 1);
			}
		}
		
		((JComponent)getParent()).revalidate();
		((JComponent)getParent()).updateUI();
	}
	
	/**
	 * 删掉音符的位置线条
	 */
	public void deletePosLines(){
		if(!positionLines.isEmpty()){
			JComponent parent = (JComponent)positionLines.get(0).getParent();
			
			for(JPanel line : positionLines){
				if(parent != null)
					parent.remove(line);
			}
			positionLines.clear();
			parent.revalidate();
		}
	}
	
	
	
	/********************************************************************************/
	
	/**
	 * 音高实体类，用于对于音符音高的客观描述.
	 * 包括八度，音阶，变调
	 * @author jingyuan.sun
	 *
	 */
	public class RealPitch{
		/**
		 * 音阶,有效值为：C, D, E, F, G, A, B
		 */
		String step;
		/**
		 * 变调,正数表示升，负数表示降
		 */
		int alter;
		/**
		 * 八度
		 */
		int octave;
		
		public RealPitch(int octave, String step, int alter){
			this.octave = octave;
			this.step = step;
			this.alter = alter;
		}
		
		public RealPitch(){
			octave = 4;
			step = "G";
			alter = 0;
		}

		/**
		 * 获得音阶
		 * @return
		 */
		public String getStep() {
			return step;
		}

		/**
		 * 设置音阶
		 * @param step
		 */
		public void setStep(String step) {
			this.step = step;
		}

		/**
		 * 获得变调
		 * @return
		 */
		public int getAlter() {
			return alter;
		}

		/**
		 * 设置变调
		 * @param alter
		 */
		public void setAlter(int alter) {
			this.alter = alter;
		}

		/**
		 * 获得八度
		 * @return
		 */
		public int getOctave() {
			return octave;
		}

		/**
		 * 设置八度
		 * @param octave
		 */
		public void setOctave(int octave) {
			this.octave = octave;
		}
	}
	
	/******************************************************************/
	
	/**
	 * 指示音符位置的短线类
	 */
	private class ShortLine extends JPanel{
		
		/**
		 * 序列号
		 */
		private static final long serialVersionUID = -3408943048696549781L;

		public ShortLine(){
			super();
			setSize(Note.this.getWidth() + 4, 2);
			setOpaque(false);
			repaint();
		}
		
		public void paintComponent(Graphics gg){
			gg.drawLine(0, 0, getWidth(), 0);
		}
	}

	@Override
	public Note getHighestNote() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public Note getLowestNote() {
		// TODO Auto-generated method stub
		return this;
	}
	
	public Note getNoteWithPitch(int pitch){
		if(!isRest && this.pitch == pitch)
			return this;
		return null;
	}
	
	/**
	 * 重写普通音符的获得小节方法
	 */
	public Measure getMeasure(){
		if(chordNote != null){
			return chordNote.getMeasure();
		}
		else{
			return super.getMeasure();
		}
	}

	/**
	 * 获得音符所在和弦音符,如果没有，则返回null
	 * @return
	 */
	public ChordNote getChordNote() {
		return chordNote;
	}

	/**
	 * 设置和弦
	 * @param chordNote
	 */
	public void setChordNote(ChordNote chordNote) {
		this.chordNote = chordNote;
	}
	
	/**
	 * 获得附点实体
	 * @return
	 */
	public UIDot getUiDot() {
		return uiDot;
	}
	
	/**
	 * 设置附点
	 * @param uiDot
	 */
	public void setUiDot(UIDot uiDot) {
		this.uiDot = uiDot;
	}

	/**
	 * 获得位置短线集合
	 * @return
	 */
	public ArrayList<JPanel> getPositionLines() {
		return positionLines;
	}

	/**
	 * 产生附点
	 */
	public void generateUIDot(){
		if(dotNum != 0){
			if(this.uiDot == null){
				uiDot = new UIDot(dotNum);
			}
			else{
				uiDot.setDotNum(dotNum);
				uiDot.adjustSize();
				uiDot.repaint();
			}
		}
	}

	/**
	 * 返回被选择
	 * @return
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * 设置被选择
	 * @param selected
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * 获得用户标注
	 * @return
	 */
	public ArrayList<Annotation> getAnnotations() {
		return annotations;
	}

	/************************ 对ties进行封装  **************************/
	/**
	 * 添加连音线
	 */
	public void addTie(Tie tie){
		if(!ties.contains(tie)){
			ties.add(tie);
		}
	}
	
	/**
	 * 删除连音线
	 * @param tie
	 */
	public void removeTie(Tie tie){
		ties.remove(tie);
	}
	
	/**
	 * 获得连音线数量(通常最大为2)
	 * @return
	 */
	public int getTieNum(){
		return ties.size();
	}
	
	/**
	 * 获得连音线
	 * @param index
	 * @return
	 */
	public Tie getTie(int index){
		return ties.get(index);
	}
	
	/*
	 * 连音线结束音判断。是的话播放时不发音
	 */
	public boolean isEndOfTie() {
		if (ties.size() == 0) return false;
		for (Tie t : ties) {
			if (t.getEndNote() == this) return true;
		}
		return false;
	}
	/*****************************************************************/
	
//	public Stem getStem(){
//		if(chordNote == null){
//			return super.getStem();
//		}
//		else{
//			return chordNote.getStem();
//		}
//	}

	public void beSelected() {
		// TODO Auto-generated method stub
		if(!selected){
			selected = true;
			repaint();
		}
	}

	public void cancleSelected() {
		// TODO Auto-generated method stub
		if(selected){
			selected = false;
			repaint();
		}
	}

}


