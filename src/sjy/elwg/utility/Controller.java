package sjy.elwg.utility;


import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import sjy.elwg.notation.NoteCanvas;
import sjy.elwg.notation.action.AddChordAction;
import sjy.elwg.notation.action.ChangeMeaTimeAction;
import sjy.elwg.notation.action.ChangeMultiTimeAction;
import sjy.elwg.notation.action.ChangeRythmAction;
import sjy.elwg.notation.action.ClearMeasureAction;
import sjy.elwg.notation.action.DelNoteAction;
import sjy.elwg.notation.action.RemoveChordAction;
import sjy.elwg.notation.musicBeans.AbstractNote;
import sjy.elwg.notation.musicBeans.Annotation;
import sjy.elwg.notation.musicBeans.Barline;
import sjy.elwg.notation.musicBeans.Beam;
import sjy.elwg.notation.musicBeans.ChordGrace;
import sjy.elwg.notation.musicBeans.ChordNote;
import sjy.elwg.notation.musicBeans.FreeAddedText;
import sjy.elwg.notation.musicBeans.Gracable;
import sjy.elwg.notation.musicBeans.Grace;
import sjy.elwg.notation.musicBeans.GraceSymbol;
import sjy.elwg.notation.musicBeans.Lyrics;
import sjy.elwg.notation.musicBeans.Measure;
import sjy.elwg.notation.musicBeans.MeasurePart;
import sjy.elwg.notation.musicBeans.Note;
import sjy.elwg.notation.musicBeans.NoteLine;
import sjy.elwg.notation.musicBeans.Page;
import sjy.elwg.notation.musicBeans.RepeatSymbol;
import sjy.elwg.notation.musicBeans.Score;
import sjy.elwg.notation.musicBeans.Selectable;
import sjy.elwg.notation.musicBeans.SharpOrFlat;
import sjy.elwg.notation.musicBeans.Stem;
import sjy.elwg.notation.musicBeans.Tail;
import sjy.elwg.notation.musicBeans.TempoText;
import sjy.elwg.notation.musicBeans.Time;
import sjy.elwg.notation.musicBeans.TremoloBeam;
import sjy.elwg.notation.musicBeans.Tuplet;
import sjy.elwg.notation.musicBeans.UIClef;
import sjy.elwg.notation.musicBeans.UIDot;
import sjy.elwg.notation.musicBeans.UIKey;
import sjy.elwg.notation.musicBeans.UITime;
import sjy.elwg.notation.musicBeans.symbolLines.AbstractLine;
import sjy.elwg.notation.musicBeans.symbolLines.Breath;
import sjy.elwg.notation.musicBeans.symbolLines.Cre;
import sjy.elwg.notation.musicBeans.symbolLines.Cresc;
import sjy.elwg.notation.musicBeans.symbolLines.Dim;
import sjy.elwg.notation.musicBeans.symbolLines.Dimc;
import sjy.elwg.notation.musicBeans.symbolLines.Dynamic;
import sjy.elwg.notation.musicBeans.symbolLines.NoteSymbol;
import sjy.elwg.notation.musicBeans.symbolLines.OctaveDown;
import sjy.elwg.notation.musicBeans.symbolLines.OctaveUp;
import sjy.elwg.notation.musicBeans.symbolLines.Ornament;
import sjy.elwg.notation.musicBeans.symbolLines.Pedal;
import sjy.elwg.notation.musicBeans.symbolLines.PerformanceSymbol;
import sjy.elwg.notation.musicBeans.symbolLines.RepeatLine;
import sjy.elwg.notation.musicBeans.symbolLines.Slur;
import sjy.elwg.notation.musicBeans.symbolLines.SymbolLine;
import sjy.elwg.notation.musicBeans.symbolLines.Tie;
import sjy.elwg.notation.musicBeans.symbolLines.Vibrato;
/**
 * 画板控制器.辅助画板进行布局等操作
 * @author sjy
 *
 */
public class Controller {
	
	
	/**
	 * 画板
	 */
	private NoteCanvas canvas;
	
	/**
	 * 操作控制器
	 */
	private ActionHistoryController actionController = ActionHistoryController.getInstance();
	
	public Controller(NoteCanvas canvas){
		this.canvas = canvas;
	}
	
	/**
	 * 获得画板
	 * @return
	 */
	public NoteCanvas getCanvas() {
		return canvas;
	}

	/**
	 * 放置线条符号
	 * @param tie
	 */
	public static void locateLine(AbstractLine sbl){
		//连句线
		int draggedX = 0;
		int draggedY = 0;
		if(sbl instanceof SymbolLine){
			draggedX = ((SymbolLine)sbl).getDraggedX();
			draggedY = ((SymbolLine)sbl).getDraggedY();
		}
		if(sbl instanceof SymbolLine){
			SymbolLine sl = (SymbolLine)sbl;
			if(sl.getStartNote() != null){
				//如果有首音符，SymbolLine符号的放置，包括可能有尾音符或者无尾音符的情况
				AbstractNote startNote = sl.getStartNote();
//				Measure measure = startNote.getMeasure();
				if(sl instanceof Cre || sl instanceof Dim || sl instanceof Vibrato || sl instanceof Dimc || sl instanceof Cresc){
					sl.setLocation(startNote.getX()  + draggedX-2+12, startNote.getMeasure().getY() - sl.getHeight() + draggedY);
				}else if(sl instanceof OctaveUp){
					sl.setLocation(startNote.getX()  + draggedX-2, startNote.getMeasure().getY() - sl.getHeight() + draggedY);
				}

				else if(sl instanceof OctaveDown ){
					sl.setLocation(startNote.getX()  + draggedX-2, startNote.getY() + startNote.getHeight()  + draggedY);
				}
				if(sl instanceof Slur){
						//分有尾音符和无尾音符两种情况
						if(sl.getEndNote() != null){
							
							//首音符，尾音符均有符杆
							if(startNote.getStem() != null && sl.getEndNote().getStem() != null){
								//首音符杆向上，尾音符符干向下
								if(startNote.isStemUp() && !sl.getEndNote().isStemUp()){
									int y = Math.max(startNote.getStem().getY(), sl.getEndNote().getY());
									sl.setLocation(startNote.getX() + 2 + draggedX,  y - sl.getHeight() + draggedY );
								}
								//首音符符干向上，尾音符符干向上
								else if(startNote.isStemUp() && sl.getEndNote().isStemUp() ){
									int y = Math.min(startNote.getLowestNote().getY() + 10, sl.getEndNote().getLowestNote().getY() + 10);
									sl.setLocation(startNote.getX() + 2 + draggedX,  y  + draggedY );
								}
								
								//首音符符干向下，尾音符干向上
								else if(!startNote.isStemUp() && sl.getEndNote().isStemUp()){
									int y = Math.max(startNote.getY(), sl.getEndNote().getStem().getY());
									sl.setLocation(startNote.getX() + 2 + draggedX,  y  - sl.getHeight() + draggedY );
								}
								
								//首音符符干向下，尾音符干向下
								else if(!startNote.isStemUp() && !sl.getEndNote().isStemUp() ){
									int y = Math.max(startNote.getHighestNote().getY(), sl.getEndNote().getHighestNote().getY());
									sl.setLocation(startNote.getX() + 2 + draggedX,  y  - sl.getHeight() + draggedY );
								}						
							}
							//首有符杆，尾无
							else if(startNote.getStem() != null && sl.getEndNote().getStem() == null){			
//								int yS = startNote.getStem().getY() < startNote.getY() ? startNote.getStem().getY() : startNote.getY();
//								//取首音符符杆和符头的y坐标最低值和尾音符比较

								if(startNote.isStemUp()){
									int y= Math.min(sl.getEndNote().getY(),startNote.getLowestNote().getY());
									if( sl.getEndNote().getDuration() <= 64){
										sl.setLocation(startNote.getX() + 2 + draggedX, y + sl.getEndNote().getHeight()/2 + 5 + draggedY);	
									}else{
										sl.setLocation(startNote.getX() + 2 + draggedX, y + sl.getEndNote().getHeight() / 2 + draggedY);			
									}							
								}else{
									int y= Math.max(sl.getEndNote().getY() ,startNote.getHighestNote().getY());
									sl.setLocation(startNote.getX() + 2 + draggedX, y - sl.getHeight() + draggedY);		
								}
								
							}
							//首无，尾有
							else if(startNote.getStem() == null && sl.getEndNote().getStem() != null){	
								if(sl.getEndNote().isStemUp()){
									
									int y= Math.min(sl.getStartNote().getY(),sl.getEndNote().getLowestNote().getY());
//									if( sl.getEndNote().getDuration() <= 64){
//										sl.setLocation(startNote.getX() + 2 + draggedX, y + sl.getEndNote().getHeight()/2 + 5 + draggedY);	
//									}else{
//										sl.setLocation(startNote.getX() + 2 + draggedX, y + sl.getEndNote().getHeight() / 2 + draggedY);			
//									}	
//									
									sl.setLocation(startNote.getX() + 2 + draggedX, y + 10 + draggedY);
								}else{
									int y= Math.max(sl.getStartNote().getY(),sl.getEndNote().getHighestNote().getY());	
									sl.setLocation(startNote.getX() + 2 + draggedX, y - sl.getHeight() + draggedY);
								}
								
							}
							//首无，尾无
							else{
								AbstractNote eNote = (AbstractNote) sl.getEndNote();
								if(startNote.isRest()){
									if(eNote.isRest()){
										int y = Math.max(startNote.getY(), sl.getEndNote().getY());
										sl.setLocation(startNote.getX() + 2 + draggedX, y- sl.getHeight() + draggedY);
									}else{
										if(eNote.getHighestNote().getPitch() >= 4){
											int y = Math.max(startNote.getY(), sl.getEndNote().getHighestNote().getY());
											sl.setLocation(startNote.getX() + 2 + draggedX, y- sl.getHeight() + draggedY);
										}else{
											int y = Math.min(startNote.getY(), sl.getEndNote().getLowestNote().getY());
											sl.setLocation(startNote.getX() + 2 + draggedX, y + draggedY);
										}
									}
								}else{
									if(startNote.getHighestNote().getPitch() >= 4){
										int y = Math.max(startNote.getHighestNote().getY(), sl.getEndNote().getHighestNote().getY());
										sl.setLocation(startNote.getX() + 2 + draggedX, y- sl.getHeight() + draggedY);
									}else{
										int y = Math.min(startNote.getLowestNote().getY(), sl.getEndNote().getLowestNote().getY());
										sl.setLocation(startNote.getX() + 2 + draggedX, y + 12 + draggedY);
									}
								}
					
							}
							
						}
						//无尾音符
						else{
							//如果首音符有符杆
							if(startNote.getStem() != null){
								if(startNote.isStemUp()){
									sl.setLocation(startNote.getX() + 2 + draggedX, startNote.getY() + draggedY);
								}else{
									sl.setLocation(startNote.getX() + 2 + draggedX, startNote.getY()- sl.getHeight() + draggedY);
								}
							}
							//首音符无符杆
							else{
								if(startNote.getHighestNote().getPitch() >= 4){
									
									sl.setLocation(startNote.getX() + 2 + draggedX, startNote.getY()- sl.getHeight() + draggedY);
								}else{
									sl.setLocation(startNote.getX() + 2 + draggedX, startNote.getY() + draggedY);
								}
							}
							sl.setLocation(startNote.getX() + 2 + draggedX, startNote.getY() - sl.getHeight() + draggedY);
							}
//						}
					//slur朝下
//					else{
//					
//						if(sl.getEndNote() != null){
//							int y = startNote.getY() < sl.getEndNote().getY() ? startNote.getY() + startNote.getHeight() - 5: sl.getEndNote().getY() + sl.getEndNote().getHeight() - 5;
//							sl.setLocation(startNote.getX() + 4 + draggedX, y + draggedY);
//						}else{
//							sl.setLocation(startNote.getX() + 4 + draggedX, startNote.getY() + startNote.getHeight() - 5 + draggedY);
//						}
//						
//					}
				}
			}
			else if(sl.getEndNote() != null){
				
				AbstractNote endNote = sl.getEndNote();
//				Measure measure = endNote.getMeasure();
				if(sl instanceof Cre || sl instanceof Dim || sl instanceof OctaveUp || sl instanceof Vibrato|| sl instanceof Dimc || sl instanceof Cresc){
					sl.setLocation(endNote.getX() - sl.getWidth() + 3 + draggedX, endNote.getMeasure().getY() - sl.getHeight() + draggedY);
				}
				else if(sl instanceof OctaveDown ){
					sl.setLocation(endNote.getX() - sl.getWidth() + 3 + draggedX, endNote.getY() + endNote.getHeight() + Measure.MEASURE_HEIGHT + draggedY);
				}
				if(sl instanceof Slur){
					if(((Slur)sl).getUpOrDown().equalsIgnoreCase("up"))
						sl.setLocation(endNote.getX() - sl.getWidth() + 3 + draggedX, endNote.getY() - sl.getHeight() + draggedY);
					else
						sl.setLocation(endNote.getX() - sl.getWidth() + 8 + draggedX, endNote.getY() + endNote.getHeight()+draggedY);
				}
			}
			else{
				NoteLine line = (NoteLine)MusicMath.getNoteLineBySymbolLine(sl).get(0);
				int meaIndex = (Integer)MusicMath.getNoteLineBySymbolLine(sl).get(1);
				int notex = line.getMeaPartList().get(0).maxAttrWidth() + NoteCanvas.xStart;
				int measurey = line.getMeaPartList().get(0).getMeasure(meaIndex).getY();
				if(sl instanceof Cre || sl instanceof Dim || sl instanceof OctaveUp || sl instanceof Vibrato){
					sl.setLocation(notex + draggedX, measurey - sl.getHeight() + draggedY);
				}
				else if(sl instanceof OctaveDown){
					sl.setLocation(notex + draggedX, measurey + Measure.MEASURE_HEIGHT + draggedY);
				}
				if(sl instanceof Slur){
					if(((Slur)sl).getUpOrDown().equalsIgnoreCase("up"))
						sl.setLocation(notex + draggedX, measurey - 2 + draggedY);
					else
						sl.setLocation(notex + draggedX, measurey + Measure.MEASURE_HEIGHT + draggedY);
				}
			}
		}
		//连音线
		else if(sbl instanceof Tie){
			Tie tie = (Tie)sbl;
			if(tie.getStartNote() != null){
				Note startNote = tie.getStartNote();
				if(tie.getUpOrDown().equalsIgnoreCase("up"))
					tie.setLocation(startNote.getX() + 2, startNote.getY() - tie.getHeight()/2);
				else
					tie.setLocation(startNote.getX() + 3, startNote.getY() + startNote.getHeight()-9);
			}
			else if(tie.getEndNote() != null){
				Note endNote = tie.getEndNote();
				if(tie.getUpOrDown().equalsIgnoreCase("up"))
					tie.setLocation(endNote.getX() - tie.getWidth() + 3, endNote.getY() - tie.getHeight()/2);
				else
					tie.setLocation(endNote.getX() - tie.getWidth() + 8, endNote.getY() + endNote.getHeight()-9);
			}
		}
		//房子记号
		else if(sbl instanceof RepeatLine){
			RepeatLine repeatLine = (RepeatLine)sbl;
			if(repeatLine.getStartMeasurePart() != null){
				MeasurePart measurePart = repeatLine.getStartMeasurePart();
				repeatLine.setLocation(measurePart.getX(), measurePart.getY()-30);
				//repeatLine.setLocation(50, 50);
			}
			else if(repeatLine.getStartMeasurePart() == null && repeatLine.getEndMeasurePart() != null){
				MeasurePart measurePart = repeatLine.getEndMeasurePart();
				repeatLine.setLocation(NoteCanvas.xStart , measurePart.getY() - 30);
			}
		}
	}
	
	/**
	 * 放置无符杠音符的符柄和符尾
	 * @param note
	 */
	public static void locateNoteStemAndTail(AbstractNote note){
		int noteWidth = note.getHighestNote().getWidth()-1;
		//无符杠
		if(note.getBeam() == null){
			Stem stem = note.getStem();
			Tail tail = note.getTail();
			//普通音符
			if(note instanceof Note){
				Note nnote = (Note)note;
				//符柄在上
				if(nnote.getPitch() < 4){
					if(stem != null){
						stem.setLocation(nnote.getX() + noteWidth, nnote.getY() + nnote.getHeight()/2 - stem.getHeight() + 0);
					}
				}//符柄在下
				else{
					if(stem != null){
						stem.setLocation(nnote.getX(), nnote.getY() + nnote.getHeight()/2);
					}
				}
			}
			//和弦音符
			else if(note instanceof ChordNote){
				ChordNote cnote = (ChordNote)note;
				Note snote = cnote.getNote(0);
				int highPitch = cnote.getHighestNote().getPitch();
				int lowPitch = cnote.getLowestNote().getPitch();
				//符柄在上
				if(Math.abs(highPitch-4) < Math.abs(lowPitch-4)){
					if(stem != null){
						stem.setLocation(cnote.getX() + noteWidth, cnote.getLowestNote().getY() + snote.getHeight()/2 - stem.getHeight());
					}
				}//符柄在下
				else{
					if(stem != null){
						int notex = cnote.getWidth() > Note.NORMAL_HEAD_WIDTH ? 
								cnote.getX()+Note.NORMAL_HEAD_WIDTH : cnote.getX();
						stem.setLocation(notex, cnote.getHighestNote().getY() + snote.getHeight()/2 );
					}
				}
			}
			
			//符尾
			if(tail != null){
				if(stem.getY() > note.getY() - 1){
					tail.setLocation(stem.getX(), stem.getY()+stem.getHeight() - 2 - stem.getDefaultHeight());
				}
				else{
					tail.setLocation(stem.getX(), stem.getY() - 3 );
				}
			}
		}
	}
	
	public static void locateNoteStemAndTail(AbstractNote note, String upOrDown){
		int noteWidth = note.getHighestNote().getWidth()-1;
		//无符杠
		if(note.getBeam() == null){
			Stem stem = note.getStem();
			Tail tail = note.getTail();
			//普通音符
			if(note instanceof Note){
				Note nnote = (Note)note;
				//符柄在上
				if(upOrDown.equalsIgnoreCase("up")){
					if(stem != null){
						stem.setLocation(nnote.getX() + noteWidth, nnote.getY() + nnote.getHeight()/2 - stem.getHeight() + 0);
					}
				}//符柄在下
				else{
					if(stem != null){
						stem.setLocation(nnote.getX(), nnote.getY() + nnote.getHeight()/2);
					}
				}
			}
			//和弦音符
			else if(note instanceof ChordNote){
				ChordNote cnote = (ChordNote)note;
				Note snote = cnote.getNote(0);
				//符柄在上
				if(upOrDown.equalsIgnoreCase("up")){
					if(stem != null){
						stem.setLocation(cnote.getX() + noteWidth, cnote.getLowestNote().getY() + snote.getHeight()/2 - stem.getHeight());
					}
				}//符柄在下
				else{
					if(stem != null){
						int notex = cnote.getWidth() > Note.NORMAL_HEAD_WIDTH ? 
								cnote.getX()+Note.NORMAL_HEAD_WIDTH : cnote.getX();
						stem.setLocation(notex, cnote.getHighestNote().getY() + snote.getHeight()/2 );
					}
				}
			}
			
			//符尾
			if(tail != null){
				if(stem.getY() > note.getY() - 1){
					tail.setLocation(stem.getX(), stem.getY()+stem.getHeight() - 2 - stem.getDefaultHeight());
				}
				else{
					tail.setLocation(stem.getX(), stem.getY() - 3 );
				}
			}
		}
	}
	
	/**
	 * 放置小节组内的标记注释
	 * @param measurePart
	 */
	public static void locateAnnotations(MeasurePart measurePart){
		for(int i = 0, n = measurePart.getMeasureNum(); i < n; i++){
			Measure measure = measurePart.getMeasure(i);
			//小节标注
			if(!measure.getAnnotations().isEmpty()){
				for(int j = 0; j < measure.getAnnotations().size(); j++){
					Annotation an = measure.getAnnotations().get(j);
					if(an.getRelatedObjts().indexOf(measure) == 0)
						an.setLocation(measure.getX() + NoteCanvas.LINE_GAP + an.getDraggedX(), 
								measure.getY() - an.getHeight() - NoteCanvas.LINE_GAP + an.getDraggedY());
				}
			}
			//音符标注
			for(int v = 0; v < measure.getVoiceNum(); v++){
				for(int j = 0, jn = measure.getNoteNum(v); j < jn; j++){
					AbstractNote note = measure.getNote(j, v);
					if(note instanceof Note){
						Note nnote = (Note)note;
						for(Annotation an : nnote.getAnnotations()){
							if(an.getRelatedObjts().indexOf(note) == 0)
								an.setLocation(nnote.getX()+nnote.getWidth() + an.getDraggedX(),
										measure.getY() - an.getHeight() - NoteCanvas.LINE_GAP + an.getDraggedY());
						}
					}
					else if(note instanceof ChordNote){
						ChordNote cnote = (ChordNote)note;
						for(int k = 0; k < cnote.getNoteNum(); k++){
							Note nnote = cnote.getNote(k);
							for(Annotation an : nnote.getAnnotations()){
								if(an.getRelatedObjts().indexOf(note) == 0)
									an.setLocation(nnote.getX()+nnote.getWidth() + an.getDraggedX(),
									    measure.getY() - an.getHeight() - NoteCanvas.LINE_GAP + an.getDraggedY());
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 放置整个乐谱的标注注释
	 * !!!!!!(注意，该方法暂时废弃使用，各个对象的标注的放置方法在对象内部)
	 * @param score
	 */
	public static void locateAnnotations(Score score){
		for(Page page : score.getPageList()){
			for(NoteLine line : page.getNoteLines()){
				for(MeasurePart meaPart : line.getMeaPartList()){
					locateAnnotations(meaPart);
				}
			}
		}
	}
	
	/**
	 * 为音符序列画符杠时，符杠在上还是在下.
	 * @param notes
	 * @return "up"和"down"两个值
	 */
	public static String beamUpOrDown(ArrayList<AbstractNote> notes){
		String result = "up";
		int upNum = 0; 
		int downNum = 0;
		for(int i = 0; i < notes.size(); i++){
			if(notes.get(i) instanceof Note){
				Note nnote = (Note)notes.get(i);
				if(nnote.getPitch() < 4) upNum++;
				else if(nnote.getPitch() > 4) downNum++;
			}
			else if(notes.get(i) instanceof ChordNote){
				ChordNote cnote = (ChordNote)notes.get(i);
				for(int j = 0, n = cnote.getNoteNum(); j < n; j++){
					Note cnnote = cnote.getNote(j);
					if(cnnote.getPitch() < 4) upNum++;
					else if(cnnote.getPitch() > 4) downNum++;
				}
			}
		}
		if(upNum > downNum) result = "up";
		else result = "down";
		return result;
	}
	
	/**
	 * 画符杠时，符杠倾斜的方式. 有左高、右高、水平三个值.
	 * @return "flat","left","right"三个值
	 */
	public static String highBeamNode(ArrayList<AbstractNote> notes){
		String result = "flat";
		
		if(notes.size() == 2){
			if(notes.get(0).getHighestNote().getPitch() > notes.get(1).getHighestNote().getPitch()){
				result = "left";
			}else if(notes.get(0).getHighestNote().getPitch() < notes.get(1).getHighestNote().getPitch()){
				result = "right";
			}
		}
		else{
			boolean rise = true;
			boolean down = true;
			boolean flat = true;
			for(int i = 1, n = notes.size() - 1 ; i < n; i++){
				int curPitch = notes.get(i).getHighestNote().getPitch();
				int prePitch = notes.get(i-1).getHighestNote().getPitch();
				int nxtPitch = notes.get(i+1).getHighestNote().getPitch();
				if((curPitch > prePitch && curPitch > nxtPitch) ||
						(curPitch < prePitch && curPitch < nxtPitch)){
					result = "flat";
					return result;
				}
				if(curPitch == prePitch && curPitch == nxtPitch && flat){
					result = "flat";
				}else if(curPitch >= prePitch && curPitch <= nxtPitch && rise){
					result = "right";
					down = false;
					flat = false;
				}else if(curPitch <= prePitch && curPitch >= nxtPitch && down){
					result = "left";
					rise = false;
					flat = false;
				}else{
					result = "flat";
					return result;
				}
			}
		}
		
		if(!result.equalsIgnoreCase("flat")){
			int deltay = Math.abs(notes.get(0).getHighestNote().getY() - notes.get(notes.size()-1).getHighestNote().getY());
			int deltax = Math.abs(notes.get(notes.size()-1).getX() - notes.get(0).getX());
			double ratio = (double)deltay / deltax;
			if(ratio < 0.15)
				result = "flat";
		}
		
		return result;
	}
	
	/**
	 * 放置小节组中音符所需的最小宽度
	 * @param measurePart 目标小节组
	 * @return
	 */
	public static int shortestMeaPartWidth(MeasurePart measurePart){
		return shortestMeaPartWidth(measurePart, NoteCanvas.X_MIN_DIST);
	}
	
	/**
	 * 以给定音符间隔绘制时所需的最短小节组宽度
	 * @param measurePart 小节组
	 * @param dst 指定的间隔
	 * @return
	 */
	public static int shortestMeaPartWidth(MeasurePart measurePart, int ddist){
		if(measurePart.getMeasure(0).isUnlimited() && measurePart.getMeasure(0).totalNoteNum() == 0)
			return Note.NORMAL_HEAD_WIDTH;
		
		int x = measurePart.getX();
//		int y = measurePart.getY();
		int meaNum = measurePart.getMeasureNum();
		//最后一个音符与小节线之间的距离
		int lastDist = ddist + Note.NORMAL_HEAD_WIDTH;
		//各个小节前一个音符
		ArrayList<MeasurePart.NListWithMeaIndex> preNotes = new ArrayList<MeasurePart.NListWithMeaIndex>();
		for(int i = 0; i <meaNum; i++){
			preNotes.add(null);
		}
		
		//谱号，调号，拍号的x坐标.
		int clefx = x;
		int keyx = clefx;
		int timex = keyx;
		//音符开始位置x坐标.
		int notex = timex;
		for(int i = 0, n = measurePart.getMeasureNum(); i < n; i++){
			Measure measure = measurePart.getMeasure(i);
			if(measure.getUiClef() != null){
				clefx = x + UIClef.CLEF_GAP;
				keyx = clefx + measure.getUiClef().getWidth();
				timex = timex > keyx ? timex : keyx;
				notex = timex ;
			}
			if(measure.getUiKey() != null){
                keyx = keyx + UIKey.KEY_GAP;
                timex = timex > keyx ? timex : keyx + measure.getUiKey().getWidth();
				notex = timex;
			}
			if(measure.getUiTime() != null){
				notex = timex + measure.getUiTime().getWidth();
			}
		}
		
		List<List<MeasurePart.NListWithMeaIndex>> noteStamp = measurePart.getNotesByTimeSlot();  //..........!!!!!!!!
		
		//各个时间槽中音符的横坐标，构建时向其中添加一个初始值，即小节组的x值
		ArrayList<Integer> xlocations = new ArrayList<Integer>();
		xlocations.add(notex);
		//当前时间槽的横坐标
		int tempx = notex;
		//各个小节前一个音符的坐标
		int[] prex = new int[meaNum];
		for(int i = 0; i < noteStamp.size(); i++){
			List<MeasurePart.NListWithMeaIndex> tslot = noteStamp.get(i);
			//由同一小节的前后两个音符的间距所限定的当前时间槽内音符的最小x值
			int[] shortestDistX = new int[tslot.size()];
			for(int j = 0; j < tslot.size(); j++){
				MeasurePart.NListWithMeaIndex mslot = tslot.get(j);
				int meaIndex = mslot.getMeaIndex();
				int shortDist = MusicMath.shortestDist(preNotes.get(meaIndex), mslot, true);
				shortestDistX[j] = preNotes.get(meaIndex)==null ? 
						notex+shortDist : prex[meaIndex]+shortDist;
				preNotes.set(meaIndex, mslot);
			}
			int maxShortX = MusicMath.maxValue(shortestDistX);
			
			if(i == 0){
				if(maxShortX > xlocations.get(i) + ddist){
					tempx = maxShortX;
				}else{
					tempx = xlocations.get(i) + ddist;
				}
			}
			else{
				if(maxShortX > xlocations.get(i) + Note.NORMAL_HEAD_WIDTH + ddist){
					tempx = maxShortX;
				}else{
					tempx = xlocations.get(i) + Note.NORMAL_HEAD_WIDTH + ddist;
				}
			}
			
			for(int j = 0; j < tslot.size(); j++){
				int meaIndex = tslot.get(j).getMeaIndex();
				prex[meaIndex] = tempx;
			}
			xlocations.add(tempx);
		}
		//删掉xlocations中的首个初始值
		xlocations.remove(0);
		return tempx + lastDist - x;
	}
	
	/**
	 * 画符杠，指定符杠朝向
	 * @param comp
	 * @param slist
	 * @param upordown 符杠方向,"up" "down"
	 */
	private static void drawBeam(JComponent comp, List<AbstractNote> slist, String upordown){
		ArrayList<ArrayList<AbstractNote>> commonBeamNotes = MusicMath.pickNoteTeams(slist);
		//符杠
		for(int i = 0, n = commonBeamNotes.size(); i < n; i++){
			ArrayList<AbstractNote> cbNotes = commonBeamNotes.get(i);
			if(cbNotes.size() > 1){
				Beam beam = new Beam(cbNotes, upordown);
				beam.locate();
				comp.add(beam);
			}
		}
	}
	
	/**
	 * 画符杠，为指定符杠朝向
	 * @param comp
	 * @param slist
	 */
	private static void drawBeam(JComponent comp, List<AbstractNote> slist){
		ArrayList<ArrayList<AbstractNote>> commonBeamNotes = MusicMath.pickNoteTeams(slist);
		//符杠
		for(int i = 0, n = commonBeamNotes.size(); i < n; i++){
			ArrayList<AbstractNote> cbNotes = commonBeamNotes.get(i);
			if(cbNotes.size() > 1){
				Beam beam = new Beam(cbNotes);
				beam.locate();
				comp.add(beam);
			}
		}
	}
	
	/**
	 * 画符杆、符柄，指定方向
	 * 注意：该方法必须在drawBeam()之后调用
	 * @param comp
	 * @param slist
	 * @param upordown
	 */
	private static void drawStem(JComponent comp, List<AbstractNote> slist, String upordown){
		if(slist.size() == 0)
			return;
		
		boolean isGrace = false;
		if(slist.get(0) instanceof Grace || slist.get(0) instanceof ChordGrace)
			isGrace = true;
		int stemType = isGrace ? Stem.GRACE : Stem.NORMAL;
		int noteWidth = isGrace ? Gracable.GRACE_WIDTH : Note.NORMAL_HEAD_WIDTH;
		
		//符柄符尾
		for(int i = 0, n = slist.size(); i < n; i++){
			AbstractNote note = slist.get(i);
			if(note.getDuration() < 256){
				if(note instanceof Note){
					Note nnote = (Note)note;
					if(nnote.isRest())
						continue;
				}
			
				Stem stem = new Stem(stemType);
				note.setStem(stem);
				comp.add(stem);
				
				if(note instanceof Note && !(note instanceof Grace)){
					Note nnote = (Note)note;
					int pitch = nnote.getPitch();
					if(pitch >= 11 || pitch <= -3){
						int stemHeight = Math.abs(nnote.getPitch() - 4) * NoteCanvas.LINE_GAP / 2;
						stem.setSize(stem.getWidth(), stemHeight);
						stem.repaint();
					}
				}else if(note instanceof ChordNote && !(note instanceof ChordGrace)){
					ChordNote cnote = (ChordNote) note;
					int pitchLow = cnote.getLowestNote().getPitch();
					int pitchHigh = cnote.getHighestNote().getPitch();
					if(pitchLow >= 11){
						stem.setSize(stem.getWidth(),(pitchLow - 4) * NoteCanvas.LINE_GAP / 2);
						stem.repaint();
					}else if(pitchHigh <= -3){
						stem.setSize(stem.getWidth(),(4 - pitchHigh) * NoteCanvas.LINE_GAP / 2);
						stem.repaint();
					}
				}
				//如果具有符杠
				if(note.getBeam() != null){
					Beam beam = note.getBeam();
					int noteHeight = isGrace ? Gracable.GRACE_HEIGHT : Note.HEAD_HEIGHT;         //音符高度
					//符杠在上
					if(beam.getUpOrDown().equalsIgnoreCase("up")){
						double ratio = beam.getRatio();
						ratio = beam.getHighNode().equalsIgnoreCase("flat") ? 0.0 : ratio;
						int listSize = beam.getUiNoteList().size();
						int deltax;
						if(beam.getHighNode().equalsIgnoreCase("left"))
							deltax = note.getX() - beam.getUiNoteList().get(0).getX();
						else
							deltax = beam.getUiNoteList().get(listSize-1).getX() - note.getX();
						int deltay = (int)(deltax * ratio);
						int yy = 0;
						if(note instanceof ChordNote){
							ChordNote cnote = (ChordNote)note;
							yy = cnote.getLowestNote().getY() + noteHeight/2 - beam.getY() - deltay;
						}else
							yy = note.getY() + noteHeight/2 - beam.getY() - deltay;
						stem.setSize(stem.getWidth(), yy);
						stem.repaint();
						stem.setLocation(note.getX()+noteWidth-1, beam.getY() + deltay );
					}//符杠在下
					else if(beam.getUpOrDown().equalsIgnoreCase("down")){
						double ratio = beam.getRatio();
						ratio = beam.getHighNode().equalsIgnoreCase("flat") ? 0.0 : ratio;
						int listSize = beam.getUiNoteList().size();
						int deltax;
						if(beam.getHighNode().equalsIgnoreCase("left"))
						    deltax = beam.getUiNoteList().get(listSize-1).getX() - note.getX();
						else
							deltax = note.getX() - beam.getUiNoteList().get(0).getX();
						int deltay = (int)(deltax * ratio);
						int yy = 0;
						if(note instanceof ChordNote){
							ChordNote cnote = (ChordNote)note;
							yy = beam.getY() + beam.getHeight() - noteHeight/2 
							       - cnote.getHighestNote().getY() - deltay;
						}else
							yy = beam.getY() + beam.getHeight() - noteHeight/2 - note.getY() - deltay;
						stem.setSize(stem.getWidth(), yy);
						stem.repaint();
						if(note instanceof ChordNote){
							ChordNote cnote = (ChordNote)note;
							int x = cnote.getWidth();
							if(x == cnote.getNote(0).getWidth()){
								stem.setLocation(note.getX(), cnote.getHighestNote().getY() + noteHeight/2);
							}else if(x == 2 * cnote.getNote(0).getWidth()){
								stem.setLocation(note.getX()+note.getWidth()/2, cnote.getHighestNote().getY() + noteHeight/2);
							}
							
						}else
							stem.setLocation(note.getX(), note.getY() + noteHeight/2);
					}
				}//不具有符杠
				else{
					if(note.getStem() != null && note instanceof ChordNote){
						ChordNote cnote = (ChordNote)note;
						int deltay = cnote.getLowestNote().getY() - cnote.getHighestNote().getY();
						stem.setSize(stem.getWidth(), stem.getHeight() + deltay);
					}
					if(note.getDuration() < 64){
						Tail tail = new Tail(note.getDuration(), upordown, stemType);
						note.setTail(tail);
						comp.add(tail);
					}
					locateNoteStemAndTail(note, upordown);
				}
			}
		}
		if(isGrace){
			boolean hasSlash = slist.get(0) instanceof Grace ? ((Grace)slist.get(0)).isHasSlash() : ((ChordGrace)slist.get(0)).isHasSlash();
			Grace grace = (Grace)slist.get(0).getHighestNote();
			if(hasSlash && slist.get(0).getTremoloBeam() == null){
				TremoloBeam tm = new TremoloBeam("tremoloBeam1", Gracable.GRACE);
				slist.get(0).setTremoloBeam(tm);
				grace.getParent().add(tm);
			}
		}
	}
	
	/**
	 * 画符杆、符柄. 不指定方向
	 * 注意：该方法必须在调用drawBeam()方法之后调用.
	 * @param comp
	 * @param slist
	 */
	private static void drawStem(JComponent comp, List<AbstractNote> slist){
		if(slist.size() == 0)
			return;
		
		boolean isGrace = false;
		if(slist.get(0) instanceof Grace || slist.get(0) instanceof ChordGrace)
			isGrace = true;
		int stemType = isGrace ? Stem.GRACE : Stem.NORMAL;
		int noteWidth = isGrace ? Gracable.GRACE_WIDTH : Note.NORMAL_HEAD_WIDTH;
		
		//符柄符尾
		for(int i = 0, n = slist.size(); i < n; i++){
			AbstractNote note = slist.get(i);
			if(note.getDuration() < 256){
				if(note instanceof Note){
					Note nnote = (Note)note;
					if(nnote.isRest())
						continue;
				}
			
				Stem stem = new Stem(stemType);
				note.setStem(stem);
				comp.add(stem);
				
				if(note instanceof Note && !(note instanceof Grace)){
					Note nnote = (Note)note;
					int pitch = nnote.getPitch();
					if(pitch >= 11 || pitch <= -3){
						int stemHeight = Math.abs(nnote.getPitch() - 4) * NoteCanvas.LINE_GAP / 2;
						stem.setSize(stem.getWidth(), stemHeight);
						stem.repaint();
					}
				}else if(note instanceof ChordNote && !(note instanceof ChordGrace)){
					ChordNote cnote = (ChordNote) note;
					int pitchLow = cnote.getLowestNote().getPitch();
					int pitchHigh = cnote.getHighestNote().getPitch();
					if(pitchLow >= 11){
						stem.setSize(stem.getWidth(),(pitchLow - 4) * NoteCanvas.LINE_GAP / 2);
						stem.repaint();
					}else if(pitchHigh <= -3){
						stem.setSize(stem.getWidth(),(4 - pitchHigh) * NoteCanvas.LINE_GAP / 2);
						stem.repaint();
					}
				}
				//如果具有符杠
				if(note.getBeam() != null){
					Beam beam = note.getBeam();
					int noteHeight = isGrace ? Gracable.GRACE_HEIGHT : Note.HEAD_HEIGHT;         //音符高度
					//符杠在上
					if(beam.getUpOrDown().equalsIgnoreCase("up")){
						double ratio = beam.getRatio();
						ratio = beam.getHighNode().equalsIgnoreCase("flat") ? 0.0 : ratio;
						int listSize = beam.getUiNoteList().size();
						int deltax;
						if(beam.getHighNode().equalsIgnoreCase("left"))
							deltax = note.getX() - beam.getUiNoteList().get(0).getX();
						else
							deltax = beam.getUiNoteList().get(listSize-1).getX() - note.getX();
						int deltay = (int)(deltax * ratio);
						int yy = 0;
						if(note instanceof ChordNote){
							ChordNote cnote = (ChordNote)note;
							yy = cnote.getLowestNote().getY() + noteHeight/2 - beam.getY() - deltay;
						}else
							yy = note.getY() + noteHeight/2 - beam.getY() - deltay;
						stem.setSize(stem.getWidth(), yy);
						stem.repaint();
						stem.setLocation(note.getX()+noteWidth-1, beam.getY() + deltay );
					}//符杠在下
					else if(beam.getUpOrDown().equalsIgnoreCase("down")){
						double ratio = beam.getRatio();
						ratio = beam.getHighNode().equalsIgnoreCase("flat") ? 0.0 : ratio;
						int listSize = beam.getUiNoteList().size();
						int deltax;
						if(beam.getHighNode().equalsIgnoreCase("left"))
						    deltax = beam.getUiNoteList().get(listSize-1).getX() - note.getX();
						else
							deltax = note.getX() - beam.getUiNoteList().get(0).getX();
						int deltay = (int)(deltax * ratio);
						int yy = 0;
						if(note instanceof ChordNote){
							ChordNote cnote = (ChordNote)note;
							yy = beam.getY() + beam.getHeight() - noteHeight/2 
							       - cnote.getHighestNote().getY() - deltay;
						}else
							yy = beam.getY() + beam.getHeight() - noteHeight/2 - note.getY() - deltay;
						stem.setSize(stem.getWidth(), yy);
						stem.repaint();
						if(note instanceof ChordNote){
							ChordNote cnote = (ChordNote)note;
							int x = cnote.getWidth();
							if(x == cnote.getNote(0).getWidth()){
								stem.setLocation(note.getX(), cnote.getHighestNote().getY() + noteHeight/2);
							}else if(x == 2 * cnote.getNote(0).getWidth()){
								stem.setLocation(note.getX()+note.getWidth()/2, cnote.getHighestNote().getY() + noteHeight/2);
							}
							
						}else
							stem.setLocation(note.getX(), note.getY() + noteHeight/2);
					}
				}//不具有符杠
				else{
					if(note.getStem() != null && note instanceof ChordNote){
						ChordNote cnote = (ChordNote)note;
						int deltay = cnote.getLowestNote().getY() - cnote.getHighestNote().getY();
						stem.setSize(stem.getWidth(), stem.getHeight() + deltay);
					}
					if(note.getDuration() < 64){
						String upOrDown;
						if(note instanceof ChordNote){
							ChordNote cnote = (ChordNote)note;
							int highPitch = cnote.getHighestNote().getPitch();
							int lowPitch = cnote.getLowestNote().getPitch();
							if(Math.abs(highPitch-4) < Math.abs(lowPitch-4))
								upOrDown = "up";
							else 
								upOrDown = "down";
						}else{
							Note nnote = (Note)note;
							upOrDown = nnote.getPitch() >= 4 ? "down" : "up";
						}
						Tail tail = new Tail(note.getDuration(), upOrDown, stemType);
						note.setTail(tail);
						comp.add(tail);
					}
					locateNoteStemAndTail(note);
				}
			}
		}
		if(isGrace){
			boolean hasSlash = slist.get(0) instanceof Grace ? ((Grace)slist.get(0)).isHasSlash() : ((ChordGrace)slist.get(0)).isHasSlash();
			Grace grace = (Grace)slist.get(0).getHighestNote();
			if(hasSlash && slist.get(0).getTremoloBeam() == null){
				TremoloBeam tm = new TremoloBeam("tremoloBeam1", Gracable.GRACE);
				slist.get(0).setTremoloBeam(tm);
				grace.getParent().add(tm);
			}
		}
	}
	
	
	
	/**
	 * 为指定的音符序列画符杆和符杠，指定了符杠方向
	 * @param slist 音符序列
	 * @param comp 盛放符杆、符杠的容器
	 * @param upordown 符杠的朝向,有效值"up","down".
	 */
	public static void drawBeamAndStem(JComponent comp, List<AbstractNote> slist, String upordown){
		drawBeam(comp, slist, upordown);
		drawStem(comp, slist, upordown);
	}
	
	/**
	 * 为指定的音符序列画符杆和符杠，为指定符杠方向
	 * @param comp
	 * @param slist
	 */
	public static void drawBeamAndStem(JComponent comp, List<AbstractNote> slist){
		drawBeam(comp, slist);
		drawStem(comp, slist);
	}
	
	/**
	 * 删除音符序列中个音符的符杠、符杆、符柄
	 * @param slist
	 */
	public static void deleteBeamAndStem(List<AbstractNote> slist){
		for(int i = 0, n = slist.size(); i < n; i++){
			AbstractNote note = slist.get(i);
			if(note.getBeam() != null){
				Beam beam = note.getBeam();
				for(int j = 0, jn = beam.getUiNoteList().size(); j < jn; j++){
					AbstractNote nnote = beam.getUiNoteList().get(j);
					nnote.setBeam(null);
				}
				beam.getUiNoteList().clear();
				if(beam.getParent() != null)
					beam.getParent().remove(beam);
			}
			if(note.getStem() != null){
				Stem stem = note.getStem();
				note.setStem(null);
				if(stem.getParent() != null)
					stem.getParent().remove(stem);
			}
			if(note.getTail() != null){
				Tail tail = note.getTail();
				note.setTail(null);
				if(tail.getParent() != null)
					tail.getParent().remove(tail);
			}
		}
	}
	
	/**
	 * 为菜单添加监听器
	 * @param l
	 */
	public static void addMenuListener(JMenu menu, ActionListener l){
		for(int i = 0, n = menu.getMenuComponentCount(); i < n; i++){
			Component comp = menu.getMenuComponent(i);
			if(comp instanceof JMenu){
				addMenuListener(((JMenu)comp), l);
			}
			else if(comp instanceof JMenuItem){
				((JMenuItem)comp).addActionListener(l);
			}
		}
	}
	
	/**
	 * 把一个音符的符号转移到另一个音符
	 * 通常是将和弦音符转移到普通音符，或者反过来。
	 * @param cnote
	 * @param onlyNote
	 */
	public static void transferSymbols(AbstractNote cnote, AbstractNote onlyNote){
		
		for(int i = 0; i < cnote.getLyricsNum(); i++){
			onlyNote.addLyrics(cnote.getLyrics(i));
			cnote.getLyrics(i).setNote(onlyNote);
		}
		
		for(int i = 0; i < cnote.getOrnamentsNum(); i++){
			onlyNote.addOrnament(cnote.getOrnament(i));
			cnote.getOrnament(i).setNote(onlyNote);
		}
		
		for(int i = 0; i < cnote.getDynamicsNum(); i++){
			onlyNote.addDynamics(cnote.getDynamics(i));
			cnote.getDynamics(i).setNote(onlyNote);
		}
		
		for(int i = 0; i < cnote.getPerformanceSymbolsNum(); i++){
			onlyNote.addPerformanceSymbols(cnote.getPerformanceSymbols(i));
			cnote.getPerformanceSymbols(i).setNote(onlyNote);
		}
		
		for(int i = 0; i < cnote.getSymbolLines().size(); i++){
			SymbolLine sl = cnote.getSymbolLines().get(i);
			onlyNote.getSymbolLines().add(sl);
			if(sl.getStartNote() == cnote)
				sl.setStartNote(onlyNote);
			else if(sl.getEndNote() == cnote)
				sl.setEndNote(onlyNote);
		}
		if(cnote.getTremoloBeam() != null){
			onlyNote.setTremoloBeam(cnote.getTremoloBeam());
			cnote.getTremoloBeam().setNote(onlyNote);
		}
		
		if(cnote.getBreath() != null){
			onlyNote.setBreath(cnote.getBreath());
			cnote.getBreath().setNote(onlyNote);
		}
		
		if(cnote.getPedal() != null){
			onlyNote.setPedal(cnote.getPedal());
			cnote.getPedal().setNote(onlyNote);
		}
		
		if(cnote.getTempoText() != null){
			onlyNote.setTempoText(cnote.getTempoText());
			cnote.getTempoText().setNote(onlyNote);
		}
		
		for(int i = 0; i < cnote.getGraceSymbolNum(); i++){
			onlyNote.addGraceSymbol(cnote.getGraceSymbols(i));
		}
		onlyNote.setTremoloBeam(cnote.getTremoloBeam());

		onlyNote.setBreath(cnote.getBreath());
		
		if(cnote.getTuplet() != null){
			Tuplet tuplet = cnote.getTuplet();
			int index = tuplet.getNoteList().indexOf(cnote);
			onlyNote.setTuplet(tuplet);
			tuplet.getNoteList().set(index, onlyNote);
		}
		
		onlyNote.getLeftGraces().clear();
		onlyNote.getRightGraces().clear();
		for(int i = 0; i < cnote.getLeftGraces().size(); i++){
			AbstractNote note = cnote.getLeftGraces().get(i);
			onlyNote.addLeftGrace(note);
		}
		for(int i = 0;i < cnote.getRightGraces().size(); i++){
			AbstractNote note = cnote.getRightGraces().get(i);
			onlyNote.addRightGrace(note);
		}
		cnote.getLeftGraces().clear();
		cnote.getRightGraces().clear();
		
		//删除源音符的引用
		cnote.clearLyrics();
		cnote.clearDynamics();
		cnote.clearPerformanceSymbol();
		cnote.clearOrnaments();
		if(cnote instanceof ChordNote)
			((ChordNote)cnote).clearNoteList();
		cnote.getSymbolLines().clear();
		cnote.setTremoloBeam(null);


		cnote.setTempoText(null);
		cnote.setBreath(null);
		cnote.setTuplet(null);
	}
	
	/**
	 * 从行结构中移除某个小节组
	 * 注意：仅仅从当前行的结构中移除小节组，并未消除对象。
	 * @param measurePart 待移除小节组
	 * @return 如果移除小节组后导致下一行为空，返回true, 否则返回false.
	 */
	public boolean removeMeasurePartFromLine(MeasurePart measurePart){
		NoteLine line = measurePart.getNoteLine();
		line.getMeaPartList().remove(measurePart);
		if(line.getMeaPartList().isEmpty()){
			line.deleteMarkers();
			line.deleteBrackets();
			line.deleteFrontLine();
			Page page = line.getPage();
			page.getNoteLines().remove(line);
			if(page.getNoteLines().isEmpty()){
				page.getScore().getPageList().remove(page);
				page.setScore(null);
				canvas.remove(page);
			}
			//重设各行坐标
			for(int i = 0, n = page.getNoteLines().size(); i < n; i++){
				NoteLine tline = page.getNoteLines().get(i);
				tline.determineLocation();
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 从页面结构中删除某一行
	 * 注意：并未消除对象
	 * @param line
	 */
	public void removeNoteLineFromPage(NoteLine line){
		Page page = line.getPage();
		page.getNoteLines().remove(line);
		if(page.getNoteLines().isEmpty()){
			Score score = page.getScore();
			int pageIndex = score.getPageList().indexOf(page);
			score.getPageList().remove(page);
			canvas.remove(page);
			canvas.rearangePages(pageIndex);
		}
	}
	
	/**
	 * 在乐谱结尾新增加一行
	 * @param line 待增的行
	 */
	public void addLine(NoteLine line){
		Score score = canvas.getScore();
		int pageNum = score.getPageList().size();
		Page lastPage = score.getPageList().get(pageNum-1);
		lastPage.getNoteLines().add(line);
		line.setPage(lastPage);
		line.determineLocation();
	}
	
	/**
	 * 把小节组中各小节，以及小节中的音符、各种符号等UI对象添加到一个页面容器中.
	 * 如果这些UI对象之前已经在另一个容器中，则会被自动挪动到目标页面容器
	 * @param measurePart 所要挪动的小节组
	 * @param page 目标页面
	 */
	public void addUIEntityToPage(MeasurePart measurePart, Page page) {

		for (int i = 0, n = measurePart.getMeasureNum(); i < n; i++) {
			Measure measure = measurePart.getMeasure(i);
			page.add(measure);
			//谱号等小节符号
			if(measure.getUiClef() != null){
				page.add(measure.getUiClef());
			}
			if(measure.getUiKey() != null){
				page.add(measure.getUiKey());
			}
			if(measure.getUiTime() != null){
				page.add(measure.getUiTime());
			}
			//小节标记注释,添加到画板上而不是页面上
			if(!measure.getAnnotations().isEmpty()){
				for(Annotation an : measure.getAnnotations()){
					if(an.getParent() != page)
						page.add(an, JLayeredPane.DRAG_LAYER);
					System.out.println("AN!!!!!!!!");
				}
			}
			for(int v = 0; v < measure.getVoiceNum(); v++){
				for (int j = 0, nj = measure.getNoteNum(v); j < nj; j++) {
					AbstractNote note = measure.getNote(j, v);
					page.add(note);
					
					//符杠，符柄，符尾等
					if(note.getBeam() != null){
						Beam beam = note.getBeam();
						page.add(beam);
					}
					if(note.getStem() != null){
						Stem stem = note.getStem();
						page.add(stem);
					}
					if(note.getTail() != null){
						Tail tail = note.getTail();
						page.add(tail);
					}
					//附点
					if(note.getDotNum() != 0){
						if(note instanceof Note){
							UIDot dot = ((Note)note).getUiDot();
							page.add(dot);
						}else if(note instanceof ChordNote){
							ChordNote cnote = (ChordNote)note;
							for(int k = 0; k < cnote.getNoteNum(); k++){
								UIDot dot = cnote.getNote(k).getUiDot();
								page.add(dot);
							}
						}
					}
					//升降号
					if(note instanceof ChordNote){
						ChordNote cnote = (ChordNote)note;
						for(int k = 0, nk = cnote.getNoteNum(); k < nk; k++){
							Note nnote = cnote.getNote(k);
							if(nnote.getSharpOrFlat() != null){
								page.add(nnote.getSharpOrFlat());
							}
						}
					}else if(note instanceof Note){
						Note nnote = (Note)note;
						if(nnote.getSharpOrFlat() != null){
							page.add(nnote.getSharpOrFlat());
						}
					}
					//连音线
					if(note instanceof Note && ((Note)note).getTieNum() != 0){
						for(int k = 0; k < ((Note)note).getTieNum(); k++){
							Tie tie = ((Note)note).getTie(k);
							page.add(tie);
						}
					}else if(note instanceof ChordNote){
						for(int k = 0; k < ((ChordNote)note).getNoteNum(); k++){
							Note nnote = ((ChordNote)note).getNote(k);
							if(nnote.getTieNum() != 0){
								for(int m = 0; m < nnote.getTieNum(); m++){
									page.add(nnote.getTie(m));
								}
							}
						}
					}
					//连音号
					if(note.getTuplet() != null && note.getTuplet().getParent() != page){
						page.add(note.getTuplet());
					}
					//各种线条符号
					for(int k = 0; k < note.getSymbolLines().size(); k++){
						SymbolLine symbolLine = note.getSymbolLines().get(k);
						page.add(symbolLine);
					}
					//音符符号
					for(int k = 0, kn = note.getOrnamentsNum(); k < kn; k++){
						NoteSymbol nsl = note.getOrnament(k);
						page.add(nsl);
					}
					if( note.getPedal() != null){
						NoteSymbol nsl = note.getPedal();
						page.add(nsl);
					}
					for(int k = 0, kn = note.getDynamicsNum(); k < kn; k++){
						NoteSymbol nsl = note.getDynamics(k);
						page.add(nsl);
					}
					for(int k = 0, kn = note.getPerformanceSymbolsNum(); k < kn; k++){
						NoteSymbol nsl = note.getPerformanceSymbols(k);
						page.add(nsl);
					}
					for(int k = 0, kn = note.getGraceSymbolNum(); k < kn; k++){
						NoteSymbol nsl = note.getGraceSymbols(k);
						page.add(nsl);
					}
					for(int k = 0, kn = note.getLeftGraces().size(); k < kn; k++){
						AbstractNote grace = note.getLeftGraces().get(k);
						page.add(grace);
					}
					for(int k = 0, kn = note.getRightGraces().size(); k < kn; k++){
						AbstractNote grace = note.getRightGraces().get(k);
						page.add(grace);
					}
					if(note.getTremoloBeam() != null)
						page.add(note.getTremoloBeam());
					if(note.getTempoText() != null)
						page.add(note.getTempoText());
					if(note.getBreath() != null)
						page.add(note.getBreath());
					
					//歌词
					for(int k = 0, kn = note.getLyricsNum(); k < kn; k++){
						Lyrics lyric = note.getLyrics(k);
						if(lyric != null)
							page.add(lyric);
					}
					//音符标记注释
					if(note instanceof Note){
						Note nnote = (Note)note;
						if(!nnote.getAnnotations().isEmpty()){
							for(Annotation an : nnote.getAnnotations()){
								if(an.getParent() != page)
									page.add(an);
							}
						}
					}
					else if(note instanceof ChordNote){
						ChordNote cnote = (ChordNote)note;
						for(int k = 0, kn = cnote.getNoteNum(); k < kn; k++){
							Note nnote = cnote.getNote(k);
							if(!nnote.getAnnotations().isEmpty()){
								for(Annotation an : nnote.getAnnotations()){
									if(an.getParent() != page)
										page.add(an);
								}
							}
						}
					}
				}
			}
		}
		//小节线
		page.add(measurePart.getBarline());
		//小节组反复记号
		for(int i = 0, n = measurePart.getRepeatSymbol().size(); i < n; i++){
			NoteSymbol nsl = measurePart.getRepeatSymbol().get(i);
			page.add(nsl);
		}
		//小节重复线条记号（房子记号等）
		for(int i = 0; i < measurePart.getRepeatLines().size(); i++){
			RepeatLine repeatLine = measurePart.getRepeatLines().get(i);
			page.add(repeatLine);
		}
	}
	
	/**
	 * 读取小节组序列,从而生成乐谱.
	 * 该方法进行了初步的行与页的划分，并产生完整的乐谱. 页面具有UI对象实体，但未进行排列与布局.
	 * @param list
	 * @return
	 */
	public Score makeScoreByMeasureParts(ArrayList<MeasurePart> list, List<FreeAddedText> tlist){
		Score score = new Score();
		Page page = new Page();
		page.addMouseListener(canvas);
		
		score.getPageList().add(page);
		page.setScore(score);
		NoteLine line = new NoteLine();
		page.getNoteLines().add(line);
		line.setPage(page);
		//行中时间槽的个数
		int slotNum = 0;
		for(int i = 0, n = list.size(); i < n; i++){
			MeasurePart measurePart = list.get(i);
			
	        slotNum += measurePart.getNotesByTimeSlot().size();
	        //一行中小节组个数不能超过5个, 时间槽不超过36个.
			if(slotNum >= 36 || line.getMeaPartList().size() > 5){
				line = new NoteLine();
				line.getMeaPartList().add(measurePart);
				measurePart.setNoteLine(line);
				measurePart.getBarline().addMouseListener(canvas);
				measurePart.getBarline().addMouseMotionListener(canvas);
				
				if(page.getBlankHeight() <= line.getLineGap()*2 + line.getHeight()){
					page = new Page();
					page.addMouseListener(canvas);
					score.getPageList().add(page);
					page.setScore(score);
				}
				page.getNoteLines().add(line);
				line.setPage(page);
				slotNum = measurePart.getNotesByTimeSlot().size();
			}
			
			else{
				line.getMeaPartList().add(measurePart);
				measurePart.setNoteLine(line);
				measurePart.getBarline().addMouseListener(canvas);
				measurePart.getBarline().addMouseMotionListener(canvas);
			}
			
			//添加UI对象实体
			addUIEntityToPage(measurePart, page);
		}
		
		//调整页面大小
//		canvas.adjustSize(score);
		
		//为各行生成辅助符号（前线，括号，水平标识符）
		for(int i = 0; i < score.getPageList().size(); i++){
			Page p = score.getPageList().get(i);
			for(int j = 0; j < p.getNoteLines().size(); j++){
				NoteLine lline = p.getNoteLines().get(j);
				lline.generateMarkers();
				lline.addMarkerListener(canvas);
				lline.generateBrackets();
				lline.generateFrontBarlineLine();
			}
		}
		
		//添加自由文本
		for(int i = 0; i < tlist.size(); i++){
			FreeAddedText txt = tlist.get(i);
			txt.addMouseListener(canvas);
			score.addFreeText(txt);
		}
		return score;
	}
	
	/**
	 * 根据某行各小节的属性生成谱号，调号，拍号等UI实体.
	 * 该方法通常在redrawLine()方法中调用.
	 * @param line 行
	 */
	public void genMeaAttrInLine(NoteLine line){
		int scoreType = canvas.getScore().getScoreType();
		
		Page page = line.getPage();
		int meaNum = line.getMeaPartList().get(0).getMeasureNum();
		
		//前一个小节组节拍
		Time preTime = null;
		//前一个小节组各小节谱号
		ArrayList<String> preClefType = new ArrayList<String>();
		for(int i = 0; i < meaNum; i++){
			preClefType.add(null);
		}
		//前一个小节组各小节调号
		ArrayList<Integer> preKeyValue = new ArrayList<Integer>();
		for(int i = 0; i < meaNum; i++){
			preKeyValue.add(null);
		}
		for(int i = 0, n = line.getMeaPartList().size(); i < n; i++){
			MeasurePart measurePart = line.getMeaPartList().get(i);
			for(int j = 0, jn = measurePart.getMeasureNum(); j < jn; j++){
				Measure measure = measurePart.getMeasure(j);
				
				String clefType = measure.getClefType();
				Time time = measure.getTime();
				int keyValue = measure.getKeyValue();
				
				/*
				 * 谱号
				 */
				if(i == 0 || !clefType.equalsIgnoreCase(preClefType.get(j))){
					if(measure.getUiClef() == null){
						UIClef uiClef = new UIClef(clefType);
						page.add(uiClef);
						measure.setUiClef(uiClef);
					}else{
						measure.getUiClef().setClefType(clefType);
						measure.getUiClef().adjustSize();
						measure.getUiClef().repaint();
					}
				}
				//删掉不该有的谱号
				else if(measure.getUiClef() != null){
					UIClef uclef = measure.getUiClef();
					page.remove(uclef);
					measure.setUiClef(null);
				}
				
				/*
				 * 拍号，拍号只有在乐谱是普通类型时才判断，因为无限制节拍类型不存在谱号概念
				 */
				if(scoreType == Score.SCORE_NORMAL){
					if(i == 0){
						if(MusicMath.preMeasurePart(canvas.getScore(), measurePart) != null){
	                        MeasurePart prePart = MusicMath.preMeasurePart(canvas.getScore(), measurePart);
	                        //前一个拍号与当前不相等
	                        if(!prePart.getMeasure(j).getTime().equals(measure.getTime())){
	                        	if(measure.getUiTime() == null){
	                        		UITime uiTime = new UITime(time);
	        						page.add(uiTime);
	        						measure.setUiTime(uiTime);
	                        	}else{
	                        		measure.getUiTime().setBeats(time.getBeats());
	                        		measure.getUiTime().setBeatType(time.getBeatType());
	                        		measure.getUiTime().repaint();
	                        	}
	                        }
	                        else{
	                        	//把不该有的拍号删掉
	                        	if(measure.getUiTime() != null){
	                        		UITime utime = measure.getUiTime();
	                        		page.remove(utime);
	                        		measure.setUiTime(null);
	                        	}
	                        }
						}
						//是乐谱第一个小节组
						else{
							if(measure.getUiTime() == null){
	                    		UITime uiTime = new UITime(time);
	    						page.add(uiTime);
	    						measure.setUiTime(uiTime);
	                    	}else{
	                    		UITime uiTime = measure.getUiTime();
	                    		page.remove(uiTime);
	                    		measure.setUiTime(null);
	                    		UITime utime = new UITime(time);
	    						page.add(utime);
	    						measure.setUiTime(utime);
//	                    		measure.getUiTime().setBeat(time.getBeat());                 
//	                    		measure.getUiTime().setBeatType(time.getBeatType());
//	                    		measure.getUiTime().repaint();
	                    	}
						}
					}
					else{
						if(!time.equals(preTime)){
							if(measure.getUiTime() == null){
								UITime uiTime = new UITime(time);
								page.add(uiTime);
								measure.setUiTime(uiTime);
							}
							else{
								UITime utime = measure.getUiTime();
								utime.setBeats(time.getBeats());
								utime.setBeatType(time.getBeatType());
								utime.repaint();
							}
						}
						//删掉不该有的拍号
						else if(measure.getUiTime() != null){
							UITime utime = measure.getUiTime();
	                		page.remove(utime);
	                		measure.setUiTime(null);
						}
					}
				}
				
				/*
				 * 调号
				 */
				if(i == 0){
					MeasurePart prePart = MusicMath.preMeasurePart(canvas.getScore(), measurePart);
					//乐谱第一个小节组
					if(prePart == null){
						//删除不该有的调号
						if(keyValue == 0 && measure.getUiKey() != null){
							UIKey ukey = measure.getUiKey();
							page.remove(ukey);
							measure.setUiKey(null);
						}
						else if(keyValue != 0){
							if(measure.getUiKey() != null){
								UIKey ukey = measure.getUiKey();
								ukey.setKeyValue(keyValue);
								ukey.adjustSize();
								ukey.repaint();
							}
							else{
								UIKey ukey = new UIKey(keyValue, clefType);
								page.add(ukey);
								measure.setUiKey(ukey);
							}
						}
					}
					//与上一行最后一个不相等
					else if(prePart.getMeasure(j).getKeyValue() != keyValue){
						UIKey newKey = measure.getUiKey() == null ? 
								new UIKey(1, clefType) : measure.getUiKey();
						if(keyValue == 0){
							newKey.setKeyValue(prePart.getMeasure(j).getKeyValue());
							newKey.setRestoreNatural(true);
							newKey.adjustSize();
							newKey.repaint();
						}
						else{
							newKey.setKeyValue(keyValue);
							newKey.setRestoreNatural(false);
							newKey.adjustSize();
							newKey.repaint();
						}
						if(measure.getUiKey() == null){
							page.add(newKey);
							measure.setUiKey(newKey);
						}
					}
					//与上一行最后一个相等
					else if(prePart.getMeasure(j).getKeyValue() == keyValue){
						if(keyValue != 0){
							if(measure.getUiKey() != null){
								measure.getUiKey().setKeyValue(keyValue);
								measure.getUiKey().setRestoreNatural(false);
								measure.getUiKey().adjustSize();
								measure.getUiKey().repaint();
							}else{
								UIKey ukey = new UIKey(keyValue, clefType);
								page.add(ukey);
								measure.setUiKey(ukey);
							}
						}
						//删除不该有的调号
						else if (keyValue == 0 && measure.getUiKey() != null) {
							UIKey ukey = measure.getUiKey();
							page.remove(ukey);
							measure.setUiKey(null);
						}
					}
					//删掉不应该有的调号
					else if(measure.getUiKey() != null){
						UIKey ukey = measure.getUiKey();
						page.remove(ukey);
						measure.setUiKey(null);
					}
				}
				else{
					if(keyValue != preKeyValue.get(j)){
						UIKey newKey = measure.getUiKey() == null ? 
								new UIKey(1, clefType) : measure.getUiKey();
						if(keyValue == 0){
							newKey.setKeyValue(preKeyValue.get(j));
							newKey.setRestoreNatural(true);
							newKey.adjustSize();
							newKey.repaint();
						}
						else{
							newKey.setKeyValue(keyValue);
							newKey.setRestoreNatural(false);
							newKey.adjustSize();
							newKey.repaint();
						}
						if(measure.getUiKey() == null){
							page.add(newKey);
							measure.setUiKey(newKey);
						}
					}
					//删掉不应该有的调号
					else if(measure.getUiKey() != null){
						UIKey ukey = measure.getUiKey();
						page.remove(ukey);
						measure.setUiKey(null);
					}
				}
				
				//更新变量
				preClefType.set(j, clefType);
				if(j == jn - 1) 
					preTime = time;
				preKeyValue.set(j, keyValue);
			}
		}
		page.revalidate();
		page.updateUI();
	}
	
	/**
	 * 返回正在编辑的页序号
	 * @param x 鼠标在veil上的x坐标，也即画板的全局x坐标
	 * @param y 鼠标在veil上的Y坐标，也即画板的全局y坐标
	 * @return
	 */
	public int getEditingPageIndex(int x, int y){
		int pageNum = y / (Page.PAGE_HEIGHT + NoteCanvas.PAGE_GAP);
		return pageNum;
	}
	
	/**
	 * 返回正在编辑的行序号
	 * @param x 页内局部x坐标
	 * @param y 页内局部y坐标
	 * @return
	 */
	public int getEditingLineIndex(Page page, int x, int y){
		int lineNum = page.getNoteLines().size();
		NoteLine firstLine = page.getNoteLines().get(0);
		NoteLine lastLine = page.getNoteLines().get(lineNum-1);
		if(y <= firstLine.getY())
			return 0;
		if(y >= lastLine.getY())
			return lineNum-1;
		//从第一行开始一直到倒数第二行
		for(int i = 0, n =lineNum - 1; i < n; i++){
			NoteLine curLine = page.getNoteLines().get(i);
			NoteLine nxtLine = page.getNoteLines().get(i+1);
			if(curLine.getY() <= y && nxtLine.getY() >= y){
				int stdy = curLine.getY() + curLine.getHeight() + curLine.getLineGap()/2;
				if(y < stdy)
					return i;
				else 
					return i+1;
			}
		}
		return -1;
	}
	
	/**
	 * 返回正在编辑的小节组序号
	 * @param x 页内局部x坐标
	 * @param y 页内局部y坐标
	 * @return
	 */
	public int getEditingMeaPartIndex(NoteLine line, int x, int yy){
		int meaPartNum = line.getMeaPartList().size();
		MeasurePart firstMeaPart = line.getMeaPartList().get(0);
		MeasurePart lastMeaPart = line.getMeaPartList().get(meaPartNum-1);
		if(x <= firstMeaPart.getX())
			return 0;
		if(x >= lastMeaPart.getX())
			return meaPartNum-1;
		//从第一个小节组一直到倒数第二个
		for(int i = 0, n = meaPartNum-1; i < n; i++){
			MeasurePart curPart = line.getMeaPartList().get(i);
			MeasurePart nxtPart = line.getMeaPartList().get(i+1);
			if(x >= curPart.getX() && x <= nxtPart.getX())
				return i;
		}
		return -1;
	}
	
	/**
	 * 返回正在编辑的小节序号
	 * @param measurePart 小节组
	 * @param x 页内局部x坐标
	 * @param y 页内局部y坐标
	 * @return
	 */
	public int getEditingMeasureIndex(MeasurePart measurePart, int x, int y){
		NoteLine line = measurePart.getNoteLine();
		int meaNum = measurePart.getMeasureNum();
		Measure firstMeasure = measurePart.getMeasure(0);
		Measure lastMeasure = measurePart.getMeasure(meaNum-1);
		if(y <= firstMeasure.getY())
			return 0;
		if(y >= lastMeasure.getY())
			return meaNum-1;
		//从第一个小节一直到倒数第二个
		for(int i = 0, n = meaNum-1; i < n; i++){
			Measure curMeasure = measurePart.getMeasure(i);
			Measure nxtMeasure = measurePart.getMeasure(i+1);
			if(y >= curMeasure.getY() && y <= nxtMeasure.getY()){
				int stdy = curMeasure.getY() + curMeasure.getHeight() + line.getMeasureGaps().get(i)/2;
				if(y < stdy)
					return i;
				else 
					return i+1;
			}
		}
		return -1;
	}
	
	/**
	 * 返回小节内正在编辑的音符序号
	 * 
	 * 如果小节是正常小节，则被编辑音符一定是小节内的某个音符或者休止符，此时返回被编辑音符的序号
	 * 如果小节是无限制节拍小节，则被编辑音符可能是空的，即向小节末尾添加音符。这时返回的结果为最后一个音符序号+1.
	 * @param measure 小节
	 * @param x 页内局部x坐标
	 * @param y 页内局部y坐标
	 * @return
	 */
	public int getEditingNoteIndex(Measure measure, int x, int y, int voice){
		int noteNum = measure.getNoteNum(voice);
		if(noteNum == 0){
			return 0;
		}
		AbstractNote firstNote = measure.getNote(0, voice);
		AbstractNote lastNote = measure.getNote(noteNum-1, voice);
		if(x <= firstNote.getX())
			return 0;
		if(x >= lastNote.getX()){
			//正常小节
			if(!measure.isUnlimited())
				return noteNum-1;
			//无限制节拍小节
			else{
				if(x <= lastNote.getX() + lastNote.getWidth() + 2*NoteCanvas.LINE_GAP)
					return noteNum-1;
				else
					return noteNum;
			}
		}	
		for(int i = 0, n = noteNum-1; i < n; i++){
			AbstractNote curNote = measure.getNote(i, voice);
			AbstractNote nxtNote = measure.getNote(i+1, voice);
			if(x >= curNote.getX() && x <= nxtNote.getX()){
				int stdx = (curNote.getX() + nxtNote.getX()) / 2 + curNote.getWidth() / 2;
				if(x <= stdx)
					return i;
				else 
					return i+1;
			}
		}
		return -1;
	}
	
	/**
	 * 返回正在编辑的音符
	 * @param x 鼠标在veil上的x坐标，也即画板的全局x坐标
	 * @param y 鼠标在veil上的Y坐标，也即画板的全局y坐标
	 * @return
	 */
	public AbstractNote getEditingNote(int x, int y, int voice){
		int pageIndex = getEditingPageIndex(x, y);
		Page page = canvas.getScore().getPageList().get(pageIndex);
		//局部y坐标
		int yy = y - (Page.PAGE_HEIGHT + NoteCanvas.PAGE_GAP) * pageIndex;
		int lineIndex = getEditingLineIndex(page, x, yy);
		NoteLine line = page.getNoteLines().get(lineIndex);
		int meaPartIndex = getEditingMeaPartIndex(line, x, yy);
		MeasurePart meaPart = line.getMeaPartList().get(meaPartIndex);
		int meaIndex = getEditingMeasureIndex(meaPart, x, yy);
		Measure measure = meaPart.getMeasure(meaIndex);
		int noteIndex = getEditingNoteIndex(measure, x, yy, voice);
		if(noteIndex >= measure.getNoteNum(voice))
			return null;
		AbstractNote note = measure.getNote(noteIndex, voice);
		return note;
	}
	
	/**
	 * 在指定小节的指定音符处添加一个新的音符,所产生的逻辑与表现层的变化
	 * @param measure 指定小节
	 * @param noteIndex 指定音符序号
	 * @param newNote 新的音符
	 * 
	 * 当音符编辑成功时返回true, 否则返回false
	 * 
	 */
	
	public boolean changeMeasureRythm(Measure measure, int noteIndex, Note newNote, int voice){
		Page page = (Page)measure.getParent();
		
		/*
		 * 无限制小节
		 */
		if(measure.isUnlimited()){
			if(noteIndex >= measure.getNoteNum(voice)){
				measure.addNote(newNote, voice);
				page.add(newNote);
				newNote.addMouseListener(canvas);
				newNote.addMouseMotionListener(canvas);
				return true;
			}
			else{
				AbstractNote note = measure.getNote(noteIndex, voice);
				//当前音符的时长大于等于待添加音符
				if(note.getDurationWithDot() >= newNote.getDurationWithDot()){
					handleShortNote(measure, noteIndex, newNote, voice);
					return true;
				}
				//当前音符时长较短，且没有连音号
				else if(note.getTuplet() == null){
					measure.removeNote(note);
					page.deleteNote(note, false);
					note.removeAllSymbols(false);
					measure.addNote(noteIndex, newNote, voice);
					page.add(newNote);
					if(newNote.getUiDot() != null)
						page.add(newNote.getUiDot());
					newNote.addMouseListener(canvas);
					newNote.addMouseMotionListener(canvas);
					return true;
				}
			}
		}
		
		/*
		 * 普通小节，为第二声部添加第一个音符
		 */
		if(noteIndex == 0 && noteIndex >= measure.getNoteNum(voice) && voice > 0){
			if(newNote.getDurationWithDot() > MusicMath.getMeasureDuration(measure))
				return false;
			ChangeRythmAction action = new ChangeRythmAction(measure, voice, this);  
			actionController.pushAction(action);    //操作压栈
			measure.addNote(noteIndex, newNote, voice);
			page.add(newNote);
			action.addCurNote(newNote);
			newNote.addMouseListener(canvas);
			newNote.addMouseMotionListener(canvas);
			
			int rest = MusicMath.getMeasureDuration(measure) - newNote.getDurationWithDot();
			ArrayList<Note> splited = MusicMath.splitDurIntoRests(rest);
			int n = splited.size();
			for(int i = 0; i < splited.size(); i++){
				Note note = splited.get(n-1-i);
				measure.addNote(i + 1, note, voice);
				page.add(note);
				action.addCurNote(note);
				note.addMouseListener(canvas);
				note.addMouseMotionListener(canvas);
			}
			//新生成的音符默认为选择状态
			canvas.cancleAllSelected();
			newNote.beSelected();
			canvas.getSelectedObjects().add(newNote);
			return true;
		}
		
		AbstractNote curNote = measure.getNote(noteIndex, voice);
		//当前音符的时长大于等于待添加音符
		if(curNote.getDurationWithDot() >= newNote.getDurationWithDot()){
			handleShortNote(measure, noteIndex, newNote, voice);
			return true;
		}
		else{
			//当前音符不具有连音号
			if(curNote.getTuplet() == null){
				//可用的剩余时长
				int totalRestDur = 0;
				float tempDur = 0.0f;
				for(int i = noteIndex; i < measure.getNoteNum(voice); i++){
					AbstractNote note = measure.getNote(i, voice);
					tempDur += note.getRealDuration();
				}
				totalRestDur = Math.round(tempDur);
				
				//剩余时长足够
				if(totalRestDur >= newNote.getDurationWithDot()){
					ChangeRythmAction action = new ChangeRythmAction(measure, voice, this);  
					actionController.pushAction(action);    //操作压栈
					action.addPreNote(curNote);
					int curDur = curNote.getDurationWithDot();
					measure.removeNote(curNote);
					page.deleteNote(curNote, false);
					//curNote.removeAllSymbols(false);
					//合并后面音符直到足够盛下新音符
					while(curDur < newNote.getDurationWithDot()){
						AbstractNote nxtNote = measure.getNote(noteIndex, voice);
						if(nxtNote.getTuplet() != null){
							curDur += nxtNote.getTuplet().getRealDuration();
							measure.remove(nxtNote);
							page.deleteNote(nxtNote, true);
							//nxtNote.removeAllSymbols(true);
						}
						else{
							measure.removeNote(nxtNote);
							page.deleteNote(nxtNote, false);
							//nxtNote.removeAllSymbols(false);
							curDur += nxtNote.getDurationWithDot();
						}
						action.addPreNote(nxtNote);
					}
					//将合并后的多余量进行切分.
					int rest = curDur - newNote.getDurationWithDot();
					ArrayList<Note> splited = MusicMath.splitDurIntoRests(rest);
					//将新音符及切分后的音符添加入小节
					measure.addNote(noteIndex, newNote, voice);
					page.add(newNote);
					if(newNote.getUiDot() != null)
						page.add(newNote.getUiDot());
					newNote.addMouseListener(canvas);
					newNote.addMouseMotionListener(canvas);
					action.addCurNote(newNote); 
					int n = splited.size();
					for(int i = 0; i < splited.size(); i++){
						Note note = splited.get(n-1-i);
						measure.addNote(noteIndex + i + 1, note, voice);
						page.add(note);
						note.addMouseListener(canvas);
						note.addMouseMotionListener(canvas);
						action.addCurNote(note);
					}
					
					//新生成的音符默认为选择状态
					canvas.cancleAllSelected();
					newNote.beSelected();
					canvas.getSelectedObjects().add(newNote);
					
					return true;
				}
				else 
					return false;
			}
			
			//当前音符具有连音号
			else{
				Tuplet tup = curNote.getTuplet();
				int index = tup.getNoteList().indexOf(curNote);
				//该连音号内部的剩余时长
				int restDur = 0;
				for(int i = index; i < tup.getNoteList().size(); i++){
					restDur += tup.getNoteList().get(i).getDurationWithDot();
				}
				//时长足够
				if(restDur >= newNote.getDurationWithDot()){
					int curDur = curNote.getDurationWithDot();
					measure.removeNote(curNote);
					page.deleteNote(curNote, false);
					//curNote.removeAllSymbols(false);
					tup.getNoteList().remove(curNote);
					//合并后面音符直到足够盛下新音符
					while(curDur < newNote.getDurationWithDot()){
						AbstractNote nxtNote = measure.getNote(noteIndex, voice);
						measure.removeNote(nxtNote);
						page.deleteNote(nxtNote, false);
						//nxtNote.removeAllSymbols(false);
						tup.getNoteList().remove(nxtNote);
						curDur += nxtNote.getDurationWithDot();
					}
					//将合并后的多余量进行切分.
					int rest = curDur - newNote.getDurationWithDot();
					ArrayList<Note> splited = MusicMath.splitDurIntoRests(rest);
					//将新音符及切分后的音符添加入小节
					measure.addNote(noteIndex, newNote, voice);
					page.add(newNote);
					tup.getNoteList().add(index, newNote);
					newNote.setTuplet(tup);
					if(newNote.getUiDot() != null)
						page.add(newNote.getUiDot());
					newNote.addMouseListener(canvas);
					newNote.addMouseMotionListener(canvas);
					int n = splited.size();
					for(int i = 0; i < splited.size(); i++){
						Note note = splited.get(n-1-i);
						measure.addNote(noteIndex + i + 1, note, voice);
						page.add(note);
						tup.getNoteList().add(index + 1 + i, note);
						note.setTuplet(tup);
						note.addMouseListener(canvas);
						note.addMouseMotionListener(canvas);
					}
					
					//新生成的音符默认为选择状态
					canvas.cancleAllSelected();
					newNote.beSelected();
					canvas.getSelectedObjects().add(newNote);
					
					return true;
				}
				else 
					return false;
			}
		}
	}
	
	/**
	 * 如果编辑待编辑的音符或休止符时长大于等于待添加音符
	 * @param measure
	 * @param noteIndex
	 * @param newNote
	 */
	public void handleShortNote(Measure measure, int noteIndex, Note newNote, int voice){
		
		AbstractNote curNote = measure.getNote(noteIndex, voice);
		newNote.addMouseListener(canvas);
		newNote.addMouseMotionListener(canvas);
		//是否具有连音号
		boolean hasTup = curNote.getTuplet() == null ? false : true;
		
		//当前音符是休止符，且与新音符时长相等
		if(curNote.getDurationWithDot() == newNote.getDurationWithDot() && 
				curNote instanceof Note && ((Note)curNote).isRest()){
			ChangeRythmAction action = new ChangeRythmAction(measure, voice, this);  
			actionController.pushAction(action);    //操作压栈
			
			measure.removeNote(curNote);
			Page page = (Page)measure.getParent();
			page.deleteNote(curNote, false);
			//curNote.removeAllSymbols(false);
			action.addPreNote(curNote);
			measure.addNote(noteIndex, newNote, voice);
			page.add(newNote);
			action.addCurNote(newNote);
			if(newNote.getUiDot() != null)
				page.add(newNote.getUiDot());
			//如果有连音符
			if(curNote.getTuplet() != null){
				Tuplet tup = curNote.getTuplet();
				int index = tup.getNoteList().indexOf(curNote);
				tup.getNoteList().remove(curNote);
				tup.getNoteList().add(index, newNote);
				newNote.setTuplet(tup);
			}
		}
		
		//当前音符时长比新音符更长
		else if(curNote.getDurationWithDot() > newNote.getDurationWithDot()){
			ChangeRythmAction action = new ChangeRythmAction(measure, voice, this);  
			actionController.pushAction(action);    //操作压栈
			
			//切分多余的时长
			int rest = curNote.getDurationWithDot() - newNote.getDurationWithDot();
			ArrayList<Note> splited = MusicMath.splitDurIntoRests(rest);
			//删除原有音符
			Page page = (Page)measure.getParent();
			measure.removeNote(curNote);
			page.deleteNote(curNote, false);
			//curNote.removeAllSymbols(false);
			action.addPreNote(curNote);
			//添加新音符
			measure.addNote(noteIndex, newNote, voice);
			page.add(newNote);
			action.addCurNote(newNote);
			if(newNote.getUiDot() != null)
				page.add(newNote.getUiDot());
			//连音符
			if(hasTup){
				Tuplet tup = curNote.getTuplet();
				int index = tup.getNoteList().indexOf(curNote);
				tup.getNoteList().remove(curNote);
				tup.getNoteList().add(index, newNote);
				newNote.setTuplet(tup);
			}
			int n = splited.size();
			for(int i = 0; i < splited.size(); i++){
				measure.addNote(noteIndex + i + 1, splited.get(n-1-i), voice);
				page.add(splited.get(i));
				if(hasTup){
					Tuplet tup = newNote.getTuplet();
					int index = tup.getNoteList().indexOf(newNote);
					tup.getNoteList().add(index + i + 1, splited.get(n-1-i));
					splited.get(n-1-i).setTuplet(tup);
				}
				splited.get(n-1-i).addMouseListener(canvas);
				splited.get(n-1-i).addMouseMotionListener(canvas);
				action.addCurNote(splited.get(n-1-i));
			}
		}
		
		//当前音符非休止符，且与新音符时长相等
		else if((curNote instanceof ChordNote || (curNote instanceof Note && !((Note)curNote).isRest())) 
				&& curNote.getDurationWithDot() == newNote.getDurationWithDot()){
			
			//与现有音符重合，则返回
			if(curNote instanceof ChordNote && ((ChordNote)curNote).getNoteWithPitch(newNote.getPitch()) != null)
				return;
			else if(curNote instanceof Note && ((Note)curNote).getPitch() == newNote.getPitch())
				return;
			
			//没有重合，则构成和弦
			AddChordAction action = new AddChordAction(this);
			actionController.pushAction(action);
			action.setPreNote(curNote);
			action.setNote(newNote);
			ChordNote cnote = addChord(curNote, newNote, noteIndex, voice);
			action.setCurNote(cnote);
	    }
		//新生成的音符默认为选择状态
		canvas.cancleAllSelected();
		newNote.beSelected();
		canvas.getSelectedObjects().add(newNote);
		NoteLine line = measure.getMeasurePart().getNoteLine();
		canvas.redrawLine(line);
	}
	
	/**
	 * 添加和弦
	 * @param curNote
	 * @param newNote
	 */
	public ChordNote addChord(AbstractNote curNote, Note newNote, int noteIndex, int voice){
		boolean hasTup = curNote.getTuplet() == null ? false : true; //是否具有连音号
		Measure measure = curNote.getMeasure();
		if(measure == null){
			return null;
		}
		ChordNote result = null;
		Page page = (Page)measure.getParent();
		//之前不是和弦
		if(curNote instanceof Note){
			Note nnote = (Note)curNote;
			ChordNote cnote = new ChordNote(nnote);
			if(newNote.getPitch() != nnote.getPitch()){
				cnote.addNote(newNote);
			    cnote.addNote(nnote);
			    //用和弦替换掉原有音符
			    measure.removeNote(curNote);
			    page.deleteNote(curNote, false);
			    page.add(curNote);
			    page.add(newNote);
			    measure.addNote(noteIndex, cnote, voice);
			    if(hasTup){
			    	Tuplet tup = curNote.getTuplet();
			    	int index = tup.getNoteList().indexOf(curNote);
			    	tup.getNoteList().remove(curNote);
			    	tup.getNoteList().add(index, cnote);
			    	cnote.setTuplet(tup);
			    }
			}
			result = cnote;
		}
		//之前已经是和弦
		else if(curNote instanceof ChordNote){
			ChordNote cnote = (ChordNote)curNote;
			ArrayList<Integer> List = new ArrayList<Integer>();
            for(int i = 0; i < cnote.getNoteNum(); i++){
            	List.add(cnote.getNote(i).getPitch());
            }
            if(List.contains(newNote.getPitch())){
            	return cnote;
            }else{
            	cnote.addNote(newNote);
		        page.add(newNote);
			if(newNote.getUiDot() != null)
				page.add(newNote.getUiDot());
            }
            result = cnote;
	    }
		return result;
	}
	
	/**
	 * 将音符的符杠、符柄、符尾等实体删除
	 * @param note
	 */
	public void deleteNoteBeamStemTail(AbstractNote note){
		Page page = null;
		if(note instanceof Note){
			Note nnote = (Note)note;
			page = (Page)nnote.getParent();
		}
		else if(note instanceof ChordNote){
			ChordNote cnote = (ChordNote)note;
			page = (Page)cnote.getNote(0).getParent();
		}
		if(note.getStem() != null)
			page.remove(note.getStem());
		if(note.getBeam() != null)
			page.remove(note.getBeam());
		if(note.getTail() != null)
			page.remove(note.getTail());
	}

	
	/**
	 * 为音符设置附点
	 * @param note 音符
	 * @param dotNum 所设置的附点个数
	 * @return
	 */
	public boolean addDotForNote(Note note, int dotNum){
		Measure measure = note.getMeasure();
		int noteIndex = measure.noteIndex(note);
		Page page = (Page)note.getParent();
		AbstractNote anote = note.getChordNote() == null ? note : note.getChordNote();
		int voice = anote.getVoice();
		
		//无限制节拍小节,且没有连音号
		if(measure.isUnlimited() && note.getTuplet() == null){
			note.setDotNum(dotNum);
			note.generateUIDot();
			((JComponent)page).add(note.getUiDot());
			NoteLine line = measure.getMeasurePart().getNoteLine();
			while(canvas.redrawLine(line)){
				line = MusicMath.nxtLine(line);
			}
		}
		
		//原本附点个数
		int formerDotNum = note.getDotNum();
		if(formerDotNum == dotNum) 
			return false;
		
		//新的附点时长
		int dotDuration = MusicMath.dotDuration(note.getDuration(), dotNum);
		//老附点时长
		int oldDotDuration = MusicMath.dotDuration(note.getDuration(), formerDotNum);
		
		//该音符不具有连音号
		if(note.getTuplet() == null){
			int curDuration = oldDotDuration;
			float tempDur = 0.0f;
			for(int i = noteIndex + 1; i < measure.getNoteNum(voice); i++){
				AbstractNote nt = measure.getNote(i, voice);
				tempDur += nt.getRealDuration();
			}
			curDuration = Math.round(tempDur);
			
			//剩余的时长足够
			if(curDuration >= dotDuration){
				curDuration = oldDotDuration;
				note.setDotNum(dotNum);
				note.generateUIDot();
				if(note.getUiDot().getParent() != page)
					page.add(note.getUiDot());
				//合并音符，直到时长足够
				while(curDuration < dotDuration){
					AbstractNote tempNote = measure.getNote(noteIndex + 1, voice);
					//某音符具有连音号
					if(tempNote.getTuplet() != null){
						Tuplet tup = tempNote.getTuplet();
						curDuration += tup.getRealDuration();
						for(AbstractNote an : tup.getNoteList()){
							measure.removeNote(an);
						}
						page.deleteNote(tempNote, true);
						tempNote.removeAllSymbols(true);
					}
					//不具有连音号
					else{
						measure.removeNote(tempNote);
						page.deleteNote(tempNote, false);
						tempNote.removeAllSymbols(false);
						curDuration += tempNote.getDurationWithDot();
					}
				}
				//合并之后有多余，则将多余的进行切分
				if(curDuration > dotDuration){
					int rest = curDuration - dotDuration;
					ArrayList<Note> splited = MusicMath.splitDurIntoRests(rest);
					for(int i = 0, n = splited.size(); i < n; i++){
						Note nt = splited.get(n - 1 - i);
						measure.addNote(noteIndex + i + 1, nt, voice);
						page.add(nt);
						nt.addMouseListener(canvas);
					}
				}
				return true;
			}
		}
		
		//该音符具有连音号
		else{
			Tuplet tuplet = note.getTuplet();
			int index = tuplet.getNoteList().indexOf(note);
			int curDuration = oldDotDuration;
			for(int i = index + 1; i < tuplet.getNoteList().size(); i++){
				AbstractNote nt = tuplet.getNoteList().get(i);
				curDuration += nt.getDurationWithDot();
			}
			
			//剩余的时长足够
			if(curDuration >= dotDuration){
				curDuration = oldDotDuration;
				note.setDotNum(dotNum);
				note.generateUIDot();
				if(note.getUiDot().getParent() != page)
					page.add(note.getUiDot());
				//合并音符，直到时长足够
				while(curDuration < dotDuration){
					AbstractNote tempNote = measure.getNote(noteIndex + 1, voice);
					measure.removeNote(tempNote);
					page.deleteNote(tempNote, false);
					tempNote.removeAllSymbols(false);
					tuplet.getNoteList().remove(tempNote);
					curDuration += tempNote.getDurationWithDot();
				}
				//合并之后有多余，则将多余的进行切分
				if(curDuration > dotDuration){
					int rest = curDuration - dotDuration;
					ArrayList<Note> splited = MusicMath.splitDurIntoRests(rest);
					for(int i = 0; i < splited.size(); i++){
						Note nt = splited.get(i);
						measure.addNote(noteIndex + i + 1, nt, voice);
						page.add(nt);
						tuplet.getNoteList().add(index + i + 1, nt);
						nt.setTuplet(tuplet);
						nt.addMouseListener(canvas);
					}
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 删除音符的附点，删除成功则返回true，否则返回false.
	 * @param note
	 * @return
	 */
	public boolean removeDotFromNote(Note note){
		if(note.getUiDot() == null || note.getChordNote() != null)
			return false;
		
		Measure measure = note.getMeasure();
		Page page = measure.getMeasurePart().getNoteLine().getPage();
		AbstractNote anote = note.getChordNote() == null ? note : note.getChordNote();
		int voice = anote.getVoice();
		int noteIndex = measure.noteIndex(note);
		int dotDur = MusicMath.dotDuration(note.getDuration(), note.getDotNum());
		//删掉附点
		note.setDotNum(0);
		page.remove(note.getUiDot());
		note.setUiDot(null);
//		note.getDotNum();
		
		ArrayList<Note> splited = MusicMath.splitDurIntoRests(dotDur);
		for(int i = 0; i < splited.size(); i++){
			Note n = splited.get(i);
			n.addMouseListener(canvas);
			n.addMouseMotionListener(canvas);
			measure.addNote(noteIndex + i + 1, n, voice);
			page.add(n);
		}
		
		return true;
	}
	
	/**
	 * 删除乐谱上某个对象
	 * @param o
	 */
	public void deleteObject(Object o){
		//删除音符
		if(o instanceof Note){
			Note note = (Note)o;
			note.deletePosLines();
			
			//倚音
			if(note instanceof Grace){
				Grace grace = (Grace)note;
				ChordGrace cg = ((Grace)note).getChordNote() == null ? null : (ChordGrace)((Grace)note).getChordNote();
				if(cg == null){
					AbstractNote anote = ((Grace)note).getNote();
					anote.removeGrace((Grace)note);
					Page page = (Page)grace.getParent();
					page.deleteNote(grace, false);
					//grace.removeAllSymbols(false);
					NoteLine line = anote.getMeasure().getMeasurePart().getNoteLine();
					while(canvas.redrawLine(line)){
						line = MusicMath.nxtLine(line);
					}
				}else{
					AbstractNote bnote = cg.getNote();
					NoteLine line = cg.getMeasure().getMeasurePart().getNoteLine();
					
					cg.removeNote(grace);
					Page page = (Page)grace.getParent();
					page.deleteNote(grace, false);
					//grace.removeAllSymbols(false);
					if(cg.getNoteNum() < 2){
						Grace onlyGrace = (Grace)cg.getNote(0);
						boolean left = grace.getX() < bnote.getX();
						if(left){
							int index = bnote.getLeftGraces().indexOf(cg);
							bnote.getLeftGraces().remove(index);
							bnote.addLeftGrace(index, onlyGrace);
						}
						else{
							int index = bnote.getRightGraces().indexOf(cg);
							bnote.getRightGraces().remove(index);
							bnote.addRightGrace(index, onlyGrace);
						}
						cg.clearNoteList();
						//转移符号
						transferSymbols(cg, onlyGrace);
						page.deleteNote(cg, false);
					}
					
					while(canvas.redrawLine(line)){
						line = MusicMath.nxtLine(line);
					}
				}
				return;
			}
			
			Measure measure = note.getChordNote() == null ? note.getMeasure() : note.getChordNote().getMeasure();
			NoteLine line = measure.getMeasurePart().getNoteLine();
			Page page = line.getPage();
			
			//非和弦，非休止符
			if(note.getChordNote() == null && !note.isRest()){
				//普通乐谱
				if(canvas.getScore().getScoreType() == Score.SCORE_NORMAL){
					if(note.getDotNum() != 0){
//						int noteIndex = measure.noteIndex(note);
						ArrayList<Note> splited = MusicMath.splitDurIntoRests(note.getDurationWithDot()-note.getDuration());
						for(int i = 0; i < splited.size(); i++){
							Note n = splited.get(i);
							n.addMouseListener(canvas);
							n.addMouseMotionListener(canvas);
							//measure.addNote(noteIndex, n, note.getVoice());
							//page.add(n);
						}
					}
					DelNoteAction action = new DelNoteAction(note, this);
					actionController.pushAction(action);
					page.deleteNote(note, false);
					note.setRest(true);
					note.setPitch(0);
					//note.removeAllSymbols(false);
					page.add(note);
				}
				//无限制节拍乐谱
				else{
					page.deleteNote(note, false);
					//note.removeAllSymbols(false);
					measure.removeNote(note);
					canvas.redrawLine(line);
				}
			}//和弦
			else if(note.getChordNote() != null){
				RemoveChordAction action = new RemoveChordAction(this);
				actionController.pushAction(action);
				ChordNote cnote = note.getChordNote();
				action.setPreNote(cnote);
				action.setNote(note);
				AbstractNote afterNote = removeChord(cnote, note);
				action.setCurNote(afterNote);
			}
			//副声部休止符，隐藏
			else if(note.isRest()){
				note.setHidden(true);
				note.repaint();
			}
			canvas.redrawLine(line);
			JComponent pr = (JComponent)canvas.getParent();
			pr.revalidate();
			pr.updateUI();
			canvas.revalidate();
			canvas.updateUI();
		}
		else if(o instanceof SharpOrFlat){
			SharpOrFlat sof = (SharpOrFlat)o;
			Note note = sof.getNote();
			note.setSharpOrFlat(null);
			sof.setNote(null);
			sof.getParent().remove(sof);
			JComponent pr = (JComponent) canvas.getParent();
			Measure measure = note.getChordNote() == null ? note.getMeasure() : note.getChordNote().getMeasure();
	        	NoteLine line = measure.getMeasurePart().getNoteLine();
	        	while(canvas.redrawLine(line)){
	        		line = MusicMath.nxtLine(line);
	        	}
			pr.updateUI();
		}
		//删除线条符号
		else if(o instanceof SymbolLine){
			//当前片段
			SymbolLine cur = (SymbolLine)o;
			AbstractNote startNote = cur.getStartNote();
			AbstractNote endNote = cur.getEndNote();
			//删除音符关联
			if(startNote != null){
				cur.setStartNote(null);
				startNote.getSymbolLines().remove(cur);
			}
			if(endNote != null){
				cur.setEndNote(null);
				endNote.getSymbolLines().remove(cur);
			}
			//删除实体
			JComponent parent = (JComponent)cur.getParent();
			parent.remove(cur);
			
			//之前的片段
			SymbolLine pre = (SymbolLine)cur.getPreSymbolLine();
			while(pre != null){
				AbstractNote snote = pre.getStartNote();
				//删除音符关联
				if(snote != null){
					pre.setStartNote(null);
					snote.getSymbolLines().remove(pre);
				}
				//删除实体
				JComponent p = (JComponent)pre.getParent();
				p.remove(pre);
				pre.setNextSymbolLine(null);
				pre = (SymbolLine)pre.getPreSymbolLine();
			}
			
			//之后的片段
			SymbolLine nxt = (SymbolLine)cur.getNextSymbolLine();
			while(nxt != null){
				AbstractNote snote = nxt.getEndNote();
				//删除音符关联
				if(snote != null){
					nxt.setEndNote(null);
					snote.getSymbolLines().remove(nxt);
				}
				//删除实体
				JComponent p = (JComponent)nxt.getParent();
				p.remove(nxt);
				nxt.setPreSymbolLine(null);
				nxt = (SymbolLine)nxt.getNextSymbolLine();
			}
			
			JComponent pr = (JComponent)canvas.getParent();
			pr.revalidate();
			pr.updateUI();
			
			canvas.getSelectedObjects().remove(o);
		}
		//删除tie符号
		else if (o instanceof Tie){
			Tie tie = (Tie)o;
			AbstractNote startNote = tie.getStartNote();
			AbstractNote endNote = tie.getEndNote();
			//删除音符关联
			if(startNote != null){
				
			//	startNote.getTies().remove(tie);
				tie.setStartNote(null);
			}
			if(endNote != null){
				
			//	endNote.getTies().remove(tie);
				tie.setEndNote(null);
			}
			//删除实体
			JComponent parent = (JComponent)tie.getParent();
			parent.remove(tie);					
			JComponent pr = (JComponent)canvas.getParent();
			pr.revalidate();
			pr.updateUI();
			
			canvas.getSelectedObjects().remove(o);
			
		}
		//删除ornament符号
		else if(o instanceof Ornament){
			Ornament ornament = (Ornament)o;
			AbstractNote note = ornament.getNote();
			note.removeOrnament(ornament);
			note.locateNoteSymbols();
			ornament.getParent().remove(ornament);
			JComponent pr = (JComponent) canvas.getParent();
			pr.revalidate();
			pr.updateUI();
		}
		//删除graceSymbol
		else if(o instanceof GraceSymbol){
			GraceSymbol graceSymbol = (GraceSymbol)o;
			AbstractNote note = graceSymbol.getNote();
			if(note != null){
				note.removeGraceSymbol(graceSymbol);
				note.locateNoteSymbols();
			}
			graceSymbol.getParent().remove(graceSymbol);
			JComponent pr = (JComponent) canvas.getParent();
			pr.revalidate();
			pr.updateUI();
		}
		//删除颤音符号
		else if(o instanceof TremoloBeam){
			TremoloBeam tremoloBeam = (TremoloBeam)o;
			AbstractNote note = tremoloBeam.getNote();
			note.setTremoloBeam(null);
			tremoloBeam.setNote(null);
			tremoloBeam.getParent().remove(tremoloBeam);
			JComponent pr = (JComponent) canvas.getParent();
			pr.revalidate();
			pr.updateUI();
		}
		//删除Dynamic符号
		else if (o instanceof Dynamic) {
			Dynamic dynamic = (Dynamic) o;
			AbstractNote note = dynamic.getNote();
			note.removeDynamics(dynamic);
			note.locateNoteSymbols();
			dynamic.getParent().remove(dynamic);
			JComponent pr = (JComponent) canvas.getParent();
			pr.revalidate();
			pr.updateUI();
		} 
		//删除表情术语
		else if (o instanceof PerformanceSymbol) {
			PerformanceSymbol performanceSymbol = (PerformanceSymbol) o;
			AbstractNote note = performanceSymbol.getNote();
			note.removePerformanceSymbols(performanceSymbol);
			note.locateNoteSymbols();
			performanceSymbol.getParent().remove(performanceSymbol);
			JComponent pr = (JComponent) canvas.getParent();
			pr.revalidate();
			pr.updateUI();
		} 
		//删除重复记号
		else if(o instanceof RepeatSymbol){
			RepeatSymbol rs  = (RepeatSymbol) o;
			MeasurePart measurePart = rs.getMeasurePart();
			measurePart.removeRepeatSymbol(rs);
			rs.getParent().remove(rs);
			JComponent pr = (JComponent) canvas.getParent();
			pr.revalidate();
			pr.updateUI();
		}
		
		//删除房子记号
		else if(o instanceof RepeatLine){	
			//当前片段
			RepeatLine cur = (RepeatLine)o;
			MeasurePart startMeasurePart = cur.getStartMeasurePart();
			MeasurePart endMeasurePart = cur.getEndMeasurePart();
			
			//删除小节关联
			if(startMeasurePart != null){
				cur.setStartMeasurePart(null);
				startMeasurePart.getRepeatLines().remove(cur);
			}
			if(endMeasurePart != null){
				cur.setEndMeasurePart(null);
				endMeasurePart.getRepeatLines().remove(cur);
			}
			
			//删除实体
			JComponent parent = (JComponent)cur.getParent();
			parent.remove(cur);
			
			//之前的片段
			RepeatLine pre = (RepeatLine)cur.getPreSymbolLine();
			
			while(pre != null){
				MeasurePart sMeasurePart = pre.getStartMeasurePart();
				//删除小节关联
				if(sMeasurePart != null){
					pre.setStartMeasurePart(null);
					sMeasurePart.getRepeatLines().remove(pre);
				}
				//删除实体
				JComponent p = (JComponent)pre.getParent();
				p.remove(pre);
				pre.setNextSymbolLine(null);
				pre = (RepeatLine)pre.getPreSymbolLine();
				
			}
			
			//之后的片段
			RepeatLine nxt = (RepeatLine)cur.getNextSymbolLine();
			while(nxt != null){
				MeasurePart eMeasurePart = nxt.getEndMeasurePart();
				//删除小节关联
				if(eMeasurePart != null){
					nxt.setEndMeasurePart(null);
					eMeasurePart.getRepeatLines().remove(nxt);
				}
				
				//删除实体
				JComponent p = (JComponent)pre.getParent();
				p.remove(pre);
				pre.setNextSymbolLine(null);
				pre = (RepeatLine)pre.getPreSymbolLine();
				
			}
			
			JComponent pr = (JComponent)canvas.getParent();
			pr.revalidate();
			pr.updateUI();
			
			canvas.getSelectedObjects().remove(o);
		}
		
		else if(o instanceof Breath){
			Breath breath  = (Breath) o;
			AbstractNote note = breath.getNote();
			note.removeBreath();
		//	note.locateNoteSymbols();
		//	note.setBreath(null);
			breath.getParent().remove(breath);
			note.locateNoteSymbols();
			JComponent pr = (JComponent) canvas.getParent();
			pr.revalidate();
			pr.updateUI();
		}
		
		else if(o instanceof Pedal){
			Pedal pedal  = (Pedal) o;
			AbstractNote note = pedal.getNote();
			note.removeBreath();
		//	note.locateNoteSymbols();
		//	note.setBreath(null);
			pedal.getParent().remove(pedal);
			note.locateNoteSymbols();
			JComponent pr = (JComponent) canvas.getParent();
			pr.revalidate();
			pr.updateUI();
		}
		
		//删除歌词
		else if (o instanceof Lyrics) {
			Lyrics lyric = (Lyrics) o;
			AbstractNote note = lyric.getNote();
			note.removeLyrics(lyric);
			lyric.getParent().remove(lyric);
			note.locateNoteSymbols();
			JComponent pr = (JComponent) canvas.getParent();
			pr.revalidate();
			pr.updateUI();
		}
		//自由文本
		else if(o instanceof FreeAddedText){
			FreeAddedText txt = (FreeAddedText)o;
			canvas.getScore().removeFreeText(txt);
			txt.getParent().remove(txt);
			JComponent pr = (JComponent) canvas.getParent();
			pr.revalidate();
			pr.updateUI();
		}
		
		//清除小节内音符
		else if(o instanceof Measure){
			Measure measure = (Measure)o;
			ClearMeasureAction action = new ClearMeasureAction(this, measure);
			actionController.pushAction(action);
			clearMeasure(measure);
		}
		
		//删除注释
		else if(o instanceof Annotation.ImagePanel){
			Annotation.ImagePanel imgPanel = (Annotation.ImagePanel)o;
			Annotation ant = imgPanel.getAt();
			ant.getParent().remove(ant);
			for(Selectable s : ant.getRelatedObjts()){
				s.cancleSelected();
				if(s instanceof Measure){
					Measure mea = (Measure)s;
					mea.getAnnotations().remove(ant);
				}
				else if(s instanceof Note){
					Note note = (Note)s;
					note.getAnnotations().remove(ant);
				}
			}
			((JComponent)canvas.getParent()).updateUI();
			canvas.getVeil().setVisible(false);
		}
		
		//删除速度记号
		else if(o instanceof TempoText || o instanceof TempoText.EditField){
			TempoText tt = o instanceof TempoText? (TempoText)o : (TempoText)((TempoText.EditField)o).getParent();
			AbstractNote note = tt.getNote();
			note.setTempoText(null);
			tt.setNote(null);
			tt.getParent().remove(tt);
			canvas.repaint();
		}
	}
	
	/**
	 * 清空小节内的内容
	 * @param measure
	 */
	public void clearMeasure(Measure measure){
		NoteLine line = measure.getMeasurePart().getNoteLine();
		Page page = line.getPage();
		int voiceNum = measure.getVoiceNum();
		for(int i = 0; i < voiceNum; i++){
			while(measure.getNoteNum(i) > 0){
				AbstractNote note = measure.getNote(0, i);
				page.deleteNote(note, true);
				measure.removeNote(note);
//				note.removeAllSymbols(true);
			}
		}
		if(!measure.isUnlimited()){
			Note rest = new Note(MusicMath.getMeasureDuration(measure), true);
			measure.addNote(rest, 0);
			page.add(rest);
			rest.addMouseListener(canvas);
			rest.addMouseMotionListener(canvas);
		}
		canvas.redrawLine(line);
	}
	
	public AbstractNote removeChord(ChordNote cnote, Note note){
		if(note.getChordNote() != cnote){
			return null;
		}
		AbstractNote result = cnote;
		Measure measure = cnote.getMeasure();
		Page page = (Page)measure.getParent();
		cnote.removeNote(note);
		int voice = cnote.getVoice();
		if(cnote.getNoteNum() < 2){
			int noteIndex = measure.noteIndex(cnote);
			Note onlyNote = cnote.getNote(0);
			result = onlyNote;
			onlyNote.setChordNote(null);
			measure.removeNote(cnote);
			measure.addNote(noteIndex, onlyNote, voice);
			//转移符号
			transferSymbols(cnote, onlyNote);
			
			page.deleteNote(cnote, false);	
		}
		page.deleteNote(note, false);
		note.removeAllSymbols(false);		
		canvas.getSelectedObjects().remove(note);
		return result;
	}
	
	/**
	 * 在所指定的乐器之上添加一个新乐器
	 * @param instrIndex 指定的乐器序号
	 * @param newInstrument 新乐器的MIDI数值
	 * @param direction 添加的位置,有效值有:"up"和"down"两个
	 */
	public void addInstrument(int instrIndex, int newInstrument, String direction){
		Score score = canvas.getScore();
		MeasurePart fmeaPart = score.getPageList().get(0).getNoteLines().get(0).getMeaPartList().get(0);
		int meaIndex = -1;
		if(direction.equalsIgnoreCase("up"))
			meaIndex = fmeaPart.getMeaIndxByInstrIndx(instrIndex);
		else
			meaIndex = fmeaPart.getMeaIndxByInstrIndx(instrIndex + 1);
		//如果小节被添加位置是-1,则添加到最下面
		meaIndex = meaIndex == -1 ? fmeaPart.getMeasureNum() : meaIndex;
		
		for(int i = 0, in = score.getPageList().size(); i < in; i++){
			Page page = score.getPageList().get(i);
			for(int j = 0, jn = page.getNoteLines().size(); j < jn; j++){
				NoteLine line = page.getNoteLines().get(j);
				for(int k = 0, kn = line.getMeaPartList().size(); k < kn; k++){
					MeasurePart measurePart = line.getMeaPartList().get(k);
					//原本的小节
					Measure oldMeasure = measurePart.getMeasure(0);
					
					for(int p = 0 ; p < MusicMath.getPartNumOfInstr(newInstrument); p++){
						String clefType = MusicMath.getClefsOfInstrument(newInstrument).get(p);
						int keyValue = oldMeasure.getKeyValue();
						Time time = new Time(oldMeasure.getTime());
						//小节
						Measure newMeasure = new Measure(clefType, keyValue, time);
						measurePart.addMeasure(meaIndex + p, newMeasure);
						newMeasure.addMouseListener(canvas);
						if(p == 0)
							newMeasure.setInstrument(String.valueOf(newInstrument));
						//全休止符
						Note note = new Note(MusicMath.getMeasureDuration(newMeasure), true);
						newMeasure.addNote(note, 0);
						note.addMouseListener(canvas);
						//添加实体
						page.add(newMeasure);
						page.add(note);
					}
				}
				line.generateBrackets();
				line.generateMarkers();
			}
		}
	}
	
	/**
	 * 删除某个声部.
	 * @param meaIndex 声部序号，即一个小节在其小节组中的序号
 	 */
	public void removePart(int meaIndex){
		Score score = canvas.getScore();
		
		//只有一个声部，则不能删除
		MeasurePart fmeaPart = score.getPageList().get(0).getNoteLines().get(0).getMeaPartList().get(0);
		if(fmeaPart.getMeasureNum() == 1)
			return;
		
		for(int i = 0, in = score.getPageList().size(); i < in; i++){
			Page page = score.getPageList().get(i);
			for(int j = 0, jn = page.getNoteLines().size(); j < jn; j++){
				NoteLine line = page.getNoteLines().get(j);
				for(int k = 0, kn = line.getMeaPartList().size(); k < kn; k++){
					MeasurePart measurePart = line.getMeaPartList().get(k);
					Measure dmeasure = measurePart.getMeasure(meaIndex);
					Measure nxtMeasure = null;
					if(meaIndex < measurePart.getMeasureNum() - 1)
						nxtMeasure = measurePart.getMeasure(meaIndex + 1);
					
					//移除小节、及其音符、符号
					measurePart.removeMeasure(dmeasure);
					for(int m = 0; m < dmeasure.getVoiceNum(); m++){
						while(dmeasure.getNoteNum(m) > 0){
							AbstractNote note = dmeasure.getNote(0, m);
							if(note.getTuplet() != null){
								for(AbstractNote tnt : note.getTuplet().getNoteList()){
									dmeasure.removeNote(tnt);
								}
							}
							page.deleteNote(note, true);
							note.removeAllSymbols(true);
							dmeasure.removeNote(note);
						}
					}
					page.remove(dmeasure);
					dmeasure.deleteMeaAttr();
					
					//如果删除了一个多声部乐器的第一个声部
					if(nxtMeasure != null && dmeasure.getInstrument() != null){
						if(nxtMeasure.getInstrument() == null)
							nxtMeasure.setInstrument(dmeasure.getInstrument());
					}
				}
				line.generateBrackets();
				page.remove(line.getMarkers().get(meaIndex));
				line.getMarkers().remove(meaIndex);
			}
		}
	}
	
	/**
	 * 从指定小节组开始，乐谱之后的小节组的拍号都变为指定的拍号值
	 * @param meaPart 指定的起始小节组
	 * @param time 指定的新拍号
	 * @param return 更换拍号的最后一行，如果乐谱中没有包含指定小节组，则返回null
	 */
	public NoteLine changeTime(MeasurePart meaPart, Time time){
		//表示是否到达起始小节组
		boolean flag = false;
		//起始小节组的拍号
		Time oldTime = meaPart.getMeasure(0).getTime();
		Score score = canvas.getScore();
		ChangeMultiTimeAction action = new ChangeMultiTimeAction(meaPart, this);
		for(int i = 0, n = score.getPageList().size(); i < n; i++){
			Page page = score.getPageList().get(i);
			for(int j = 0, jn = page.getNoteLines().size(); j < jn; j++){
				NoteLine line = page.getNoteLines().get(j);
				for(int k = 0, kn = line.getMeaPartList().size(); k < kn; k++){
					MeasurePart measurePart = line.getMeaPartList().get(k);
					if(measurePart == meaPart)
						flag = true;
					if(flag){
						for(int m = 0, mn = measurePart.getMeasureNum(); m < mn; m++){
							Measure measure = measurePart.getMeasure(m);
							if(measure.getTime().equals(oldTime)){
								ChangeMeaTimeAction aaction = changeMeasureTime(measure, time);
								action.addTimeAction(aaction);
							}
							else
								return line;
						}
					}
				}
			}
		}
		if(action.singleActionSize() != 0){
			actionController.pushAction(action); //操作压栈
		}
		if(flag == false)
			return null;
		else{
			Page lastPage = score.getPageList().get(score.getPageList().size()-1);
			NoteLine lastLine = lastPage.getNoteLines().get(lastPage.getNoteLines().size()-1);
			return lastLine;
		}
	}
	
	/**
	 * 将某个小节的拍号设为指定值
	 * @param measure
	 * @param time
	 */
	public ChangeMeaTimeAction changeMeasureTime(Measure measure, Time time){
		//新老拍号相同
		if(measure.getTime().equals(time) && MusicMath.getMeasureDuration(measure) == MusicMath.getMeasureDuration(time))
			return null;
		
		ChangeMeaTimeAction action = new ChangeMeaTimeAction(this, measure, measure.getTime(), time);
		Page page = measure.getMeasurePart().getNoteLine().getPage();
		
		for(int v = 0; v < measure.getVoiceNum(); v++){
			List<AbstractNote> preNoteList = new ArrayList<AbstractNote>();
			List<AbstractNote> curNoteList = new ArrayList<AbstractNote>();
			action.addPreNoteList(preNoteList);
			action.addCurNoteList(curNoteList);
			
			//仅包含全休止符,仅仅修改该休止符的时长即可
			if(measure.getNoteNum(v) == 1){
				AbstractNote note = measure.getNote(0, v);
				if(note instanceof Note && ((Note)note).isRest() && ((Note)note).isFull()){
					page.deleteNote(note, false);
					measure.removeNote(note);
					Note nnote = new Note();
					nnote.setRest(true);
					nnote.setFull(true);
					nnote.setDuration(MusicMath.getMeasureDuration(time));
					measure.addNote(nnote, v);
					page.add(nnote);
//					note.setDuration(MusicMath.getMeasureDuration(time));
					preNoteList.add(note);
					curNoteList.add(nnote);
					continue;
				}
			}
			
			//新拍号更长
			if(MusicMath.getMeasureDuration(time) > MusicMath.getMeasureDuration(measure)){
				int rest = MusicMath.getMeasureDuration(time) - MusicMath.getMeasureDuration(measure);
				ArrayList<Note> splited = MusicMath.splitDurIntoRests(rest);
				for(int i = splited.size() - 1; i >= 0; i--){
					AbstractNote note = splited.get(i);
					measure.addNote(note, v);
					page.add(note);
					note.addMouseListener(canvas);
					note.addMouseMotionListener(canvas);
					curNoteList.add(note);
				}
			}
			
			//新拍号比旧拍号短
			else if(MusicMath.getMeasureDuration(time) < MusicMath.getMeasureDuration(measure)){
				int diff = MusicMath.getMeasureDuration(measure) - MusicMath.getMeasureDuration(time);
				//当前所去掉音符的时长总和
				int accum = 0;
				for(int i = measure.getNoteNum(v) - 1; i >= 0; i--){
					//去掉的时长已经足够
					if(accum >= diff){
						break;
					}
					
					AbstractNote note = measure.getNote(measure.getNoteNum(v)-1, v);
					//音符没有连音号
					if(note.getTuplet() == null){
						measure.removeNote(note);
						page.deleteNote(note, false);
//						note.removeAllSymbols(false);
						accum += note.getDurationWithDot();
						preNoteList.add(note);
					}
					//具有连音号
					else{
						Tuplet tup = note.getTuplet();
						accum += tup.getRealDuration();
						for(AbstractNote anote : tup.getNoteList()){
							measure.removeNote(anote);
							preNoteList.add(anote);
						}
						page.deleteNote(note, true);
//						note.removeAllSymbols(true);
					}
				}
				//实际去掉的比需要去掉的多，则将多余进行切分
				if(accum > diff){
					ArrayList<Note> splited = MusicMath.splitDurIntoRests(accum - diff);
					for(int j = 0, n = splited.size(); j < n; j++){
						Note nnote = splited.get(n - j - 1);
						measure.addNote(nnote, v);
						page.add(nnote);
						nnote.addMouseListener(canvas);
						nnote.addMouseMotionListener(canvas);
						curNoteList.add(nnote);
					}
				}
			}
		}
		measure.setTime(time);
		return action;
	}
	
	/**
	 * 从指定小节组开始，将各个小节组的指定位置的小节的谱号设置为指定值
	 * @param measurePart 起始小节组
	 * @param meaIndex 小节位置
	 * @param clefType 指定谱号
	 */
	public void changeClef(MeasurePart measurePart, int meaIndex, String clefType){
		Measure measure = measurePart.getMeasure(meaIndex);
		String oldClef = measure.getClefType();
		Score score = canvas.getScore();
		boolean flag = false;
		for(int i = 0, in = score.getPageList().size(); i < in; i++){
			Page page = score.getPageList().get(i);
			for(int j = 0, jn = page.getNoteLines().size(); j < jn; j++){
				NoteLine line = page.getNoteLines().get(j);
				for(int k = 0, kn = line.getMeaPartList().size(); k < kn; k++){
					MeasurePart meaPart = line.getMeaPartList().get(k);
					if(meaPart == measurePart)
						flag = true;
					if(flag){
						Measure mea = meaPart.getMeasure(meaIndex);
						//从指定小节组开始，一直到下一个谱号变化点为止
						if(!mea.getClefType().equalsIgnoreCase(oldClef)){
							return;
						}
						for(int v = 0; v < mea.getVoiceNum(); v++){
							for(int n = 0, nn = mea.getNoteNum(v); n < nn; n++){
								AbstractNote note = mea.getNote(n, v);
								if(note instanceof Note){
									Note nnote = (Note)note;
									if(!nnote.isRest())
										nnote.determinePitchByRealPitch(clefType);
								}
								else if(note instanceof ChordNote){
									ChordNote cnote = (ChordNote)note;
									for(int p = 0; p < cnote.getNoteNum(); p++){
										Note nt = cnote.getNote(p);
										nt.determinePitchByRealPitch(clefType);
									}
								}
							}
						}
						mea.setClefType(clefType);
					}
				}
			}
		}
	}
	
	/**
	 * 从指定的小节组开始，将每个小节组中指定位置的小节的调号值设置为指定值
	 * @param measurePart 起始小节组
	 * @param meaIndex 小节位置
	 * @param key 指定调号值
	 */
	public void changeKey(MeasurePart measurePart, int meaIndex, int key){
		Measure measure = measurePart.getMeasure(meaIndex);
		int oldKey = measure.getKeyValue();
		Score score = canvas.getScore();
		boolean flag = false;
		for(int i = 0, in = score.getPageList().size(); i < in; i++){
			Page page = score.getPageList().get(i);
			for(int j = 0, jn = page.getNoteLines().size(); j < jn; j++){
				NoteLine line = page.getNoteLines().get(j);
				for(int k = 0, kn = line.getMeaPartList().size(); k < kn; k++){
					MeasurePart meaPart = line.getMeaPartList().get(k);
					if(meaPart == measurePart)
						flag = true;
					if(flag){
						Measure mea = meaPart.getMeasure(meaIndex);
						//从指定小节组开始，一直到下一个调号变化点为止
						if(mea.getKeyValue() != oldKey){
							return;
						}
						mea.setKeyValue(key);
					}
				}
				
			}
		}
	}
	
	/**
	 * 向音符添加连音号
	 * @param note
	 * @param modification
	 * @param normal
	 */
	public void addTuplet(Note note, int modification, int normal){
		AbstractNote curNote = note.getChordNote() == null ? note : note.getChordNote();
		int voice = curNote.getVoice();
		
		//不允许嵌套连音
		if(curNote.getTuplet() != null)
			return;
		
		Measure measure = curNote.getMeasure();
		int curDur = note.getDurationWithDot();
		int index = measure.noteIndex(curNote);
		Page page = (Page)note.getParent();
		
		//如果是无限制节拍类型，直接添加
		if(canvas.getScore().getScoreType() == Score.SCORE_UNLMTED){
			//所生成的连音号序列
			Tuplet tuplet = new Tuplet(curNote, modification, normal);
			page.add(tuplet);
			//当前所插入的音符位置
			int curIndex = index + 1;
			for(int i = 1; i < modification; i++){
				Note rest = new Note(note.getDuration(), false);
				tuplet.getNoteList().add(rest);
				rest.setTuplet(tuplet);
				measure.addNote(curIndex++, rest, voice);
				page.add(rest);
				rest.addMouseListener(canvas);
				rest.addMouseMotionListener(canvas);
			}
			return;
		}
		
		int restDur = MusicMath.restDuration(measure, note);
		int durNeeded = note.getDuration() * normal;
		//小节时长不够
		if(restDur < durNeeded)
			return;
		
		//合并直到时长足够
		for(int i = index + 1; index + 1 < measure.getNoteNum(voice) && curDur < durNeeded; i++){
			AbstractNote anote = measure.getNote(index + 1, voice);
			if(curDur < durNeeded){
				if(anote.getTuplet() == null){
					page.deleteNote(anote, false);
					anote.removeAllSymbols(false);
					measure.removeNote(anote);
					curDur += anote.getDurationWithDot();
				}
				else{
					Tuplet tuplet = anote.getTuplet();
					for(AbstractNote an : tuplet.getNoteList()){
						measure.removeNote(an);
					}
					page.deleteNote(anote, true);
					anote.removeAllSymbols(true);
					curDur += tuplet.getRealDuration();
				}
			}
		}
		
		//所生成的连音号序列
		Tuplet tuplet = new Tuplet(curNote, modification, normal);
		page.add(tuplet);
		//当前所插入的音符位置
		int curIndex = index + 1;
		for(int i = 1; i < modification; i++){
			Note rest = new Note(note.getDuration(), false);
			tuplet.getNoteList().add(rest);
			rest.setTuplet(tuplet);
			measure.addNote(curIndex++, rest, voice);
			page.add(rest);
			rest.addMouseListener(canvas);
			rest.addMouseMotionListener(canvas);
		}
		
		//多余的时长切分为休止符
		if(curDur > durNeeded){
			int rest = curDur - durNeeded;
			ArrayList<Note> splited = MusicMath.splitDurIntoRests(rest);
			for(int i = splited.size() - 1; i >= 0; i--){
				Note n = splited.get(i);
				measure.addNote(curIndex++, n, voice);
				page.add(n);
				n.addMouseListener(canvas);
				n.addMouseMotionListener(canvas);
			}
		}	
	}
	
	/**
	 * 在指定的小节组后面追加小节组
	 * @param measurePart 指定小节组
	 * @param num 添加的小节组个数
	 * 新添加小节的时候，需要给新的小节添加乐器，否则导出的MusicXML文档将会出错
	 */
	public void addMeasure(MeasurePart measurePart, int num){
		int meaNum = measurePart.getMeasureNum();
		NoteLine line = measurePart.getNoteLine();
		int index = line.getMeaPartList().indexOf(measurePart);
		JComponent parent = (JComponent)measurePart.getMeasure(0).getParent();
		
		for(int i = 0; i < num ; i++){
			MeasurePart meaPart = new MeasurePart();
			for(int j = 0; j < meaNum; j++){
				Time time = null;
				Measure measure = null;
				String instrument = measurePart.getMeasure(j).getInstrument();
				//正常模式
				if(canvas.getScore().getScoreType() == Score.SCORE_NORMAL){
					time = new Time(measurePart.getMeasure(j).getTime());
					measure = new Measure(measurePart.getMeasure(j).getClefType(), 
							measurePart.getMeasure(j).getKeyValue(), time);
					Note note = new Note(MusicMath.getMeasureDuration(measure), true);
					note.addMouseListener(canvas);
					note.addMouseMotionListener(canvas);
					parent.add(note);
					measure.addNote(note, 0);
					measure.setInstrument(instrument);
				}
				//练习模式，小节无限制节拍
				else{
					measure = new Measure(measurePart.getMeasure(j).getClefType(), 
							measurePart.getMeasure(j).getKeyValue());
					measure.setInstrument(instrument);
				}
				measure.addMouseListener(canvas);			
				//实体
				parent.add(measure);
				//逻辑关系
				meaPart.addMeasure(measure);
			}
			Barline barline = new Barline(meaPart, "regular");
			parent.add(barline);
			barline.addMouseListener(canvas);
			barline.addMouseMotionListener(canvas);
			line.getMeaPartList().add(index + 1, meaPart);
			meaPart.setNoteLine(line);	
		}	
		//如果追加在该行末尾
		if(line.getMeaPartList().indexOf(measurePart) == line.getMeaPartList().size() - 1 - num){
			//获得measurePart的最后一个小节线的类型，将原来的最后一个小节线设为regular，添加后的最后一个小节线设为之前的状态
			String barlineType = measurePart.getBarline().getType();
			measurePart.getBarline().setType("regular");	
			line.getMeaPartList().get(line.getMeaPartList().size() - 1).getBarline().setType(barlineType);
		}
	}
	
	/**
	 * 彻底移除某个小节组,不保留内部逻辑关系
	 * @param meaPart
	 * @return 如果删除之后导致改行为空，则返回true, 否则返回false
	 */
	public boolean deleteMeasure(MeasurePart meaPart){
		return deleteMeasure(meaPart, true);
	}

	/**
	 * 彻底删除某个小节组
	 * @param meaPart 
	 * @param split 是否对小节组进行解体。如果解体，则删除小节组与小节，小节与音符之间的关联关系；否则只删除小节组在其外层对象中关联，保持其内部所有对象逻辑关系。
	 *
	 *
	 */
	public boolean deleteMeasure(MeasurePart meaPart, boolean split){
		Page page = meaPart.getNoteLine().getPage();
		for(int j = meaPart.getMeasureNum() - 1; j >= 0; j--){
			Measure measure = meaPart.getMeasure(j);
			page.remove(measure);
			if(split)
				meaPart.removeMeasure(measure);
			for(int i = 0, in = measure.getVoiceNum(); i < in; i++){
				for(int x = measure.getNoteNum(i) - 1; x >= 0 ; x--){
					AbstractNote note = measure.getNote(x, i);
					page.deleteNote(note, true);
					if(split)
						measure.removeNote(note);
				}
			}
			measure.deleteMeaAttr();
		}
		page.remove(meaPart.getBarline());
		if(split)
			meaPart.setBarline(null);
		return removeMeasurePartFromLine(meaPart);
	}

	/**
	 * 撤销
	 */
	public void undo(){
		actionController.undo();
	}
	
	/**
	 * 回撤
	 */
	public void redo(){
		actionController.redo();
	}
}
