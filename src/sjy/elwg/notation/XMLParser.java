package sjy.elwg.notation;

import java.awt.Font;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import sjy.elwg.notation.musicBeans.AbstractNote;
import sjy.elwg.notation.musicBeans.Annotation;
import sjy.elwg.notation.musicBeans.Barline;
import sjy.elwg.notation.musicBeans.ChordGrace;
import sjy.elwg.notation.musicBeans.ChordNote;
import sjy.elwg.notation.musicBeans.FreeAddedText;
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
import sjy.elwg.notation.musicBeans.SharpOrFlat;
import sjy.elwg.notation.musicBeans.TempoText;
import sjy.elwg.notation.musicBeans.Time;
import sjy.elwg.notation.musicBeans.TremoloBeam;
import sjy.elwg.notation.musicBeans.Tuplet;
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
import sjy.elwg.notation.musicBeans.symbolLines.RepeatEnding;
import sjy.elwg.notation.musicBeans.symbolLines.RepeatLine;
import sjy.elwg.notation.musicBeans.symbolLines.Slur;
import sjy.elwg.notation.musicBeans.symbolLines.SymbolLine;
import sjy.elwg.notation.musicBeans.symbolLines.Tie;
import sjy.elwg.notation.musicBeans.symbolLines.Vibrato;
import sjy.elwg.utility.Controller;
import sjy.elwg.utility.MusicMath;
/**
 * XML文件解析器，用于MusicXML文件的解析、生成.
 * 以及软件组件配置文件的解析与生成
 * 
 * @author jingyuan.sun
 *
 */ 
public class XMLParser {
	/**
	 * 乐谱
	 */
	private Score score;
	/**
	 * 待写入或者读取的文件
	 */
	private File file;
	/**
	 * 输入流
	 */
	private InputStream inputStream;
	/**
	 * 控制器
	 */
	public Controller controller;
	
	
	/**
	 * 读xml文件的构造函数
	 * @param file
	 */
	public XMLParser(File file, Controller controller){
		this.file = file;
		this.controller = controller;
	}
	
	public XMLParser(InputStream in, Controller controller){
		this.inputStream = in;
		this.controller = controller;
	}
	
	/**
	 * 写xml文件的构造函数
	 * @param score
	 */
	public XMLParser(Score score){
		this.score = score;
	}
	
	public Score getScore() {
		return score;
	}

	public void setScore(Score score) {
		this.score = score;
	}
	
	/**
	 * 从XML文件中把数据读入一个列表中，其中每一项为一个小节组.
	 * @return
	 */
	public void readFromXML(){
		//标题和作者
		String title = null;
		String composer = null;
		
		//乐谱类型
		int scoreType = Score.SCORE_NORMAL;
		//是否包含标题作者
		boolean hasTitle = true;
		
		//将MusicXML文件中的每一个声部读入一个单独的小节组序列. 多个小节组序列共同存放在parts变量中.
		ArrayList<ArrayList<MeasurePart>> parts = new ArrayList<ArrayList<MeasurePart>>(); 
		
		SAXReader reader = new SAXReader();
		Document document = null;
		try{
			reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			if(file != null && file.isFile())
				document = reader.read(file);
			else if(inputStream != null)
				document = reader.read(new BufferedInputStream(inputStream));
		}catch(Exception e){
			e.printStackTrace();
			return;
		}
		
		Element root = document.getRootElement();
		Iterator<?> rootIterator = root.elementIterator();
		
		//XML文件中用来表示四分音符时长的数字
		int div = 0;
		
		//当前音符
		AbstractNote currentNote = null;
		
		//提前出现的音符符号,它们属于某个音符，但在XML文件中比音符先出现，因此用于缓存这些符号
		ArrayList<NoteSymbol> preNoteSymbols = new ArrayList<NoteSymbol>();
		
		//提前出现的倚音
		ArrayList<AbstractNote> graces = new ArrayList<AbstractNote>();
		
		//提前出现的房子记号
		HashMap<Integer, AbstractLine> preRepeatEnding = new HashMap<Integer, AbstractLine>();
		
		HashMap<Integer, AbstractLine> preCreAndDims = new HashMap<Integer, AbstractLine>();
		HashMap<Integer, AbstractLine> preOctaves = new HashMap<Integer, AbstractLine>();
		HashMap<Integer, AbstractLine> preVibratos = new HashMap<Integer, AbstractLine>();
	//	HashMap<Integer, AbstractSymbolLine> prePedals = new HashMap<Integer, AbstractSymbolLine>();
		
		//XML文件中出现的连音号或各种线条符号集合.键、值分别为符号的id与符号本身
		HashMap<Integer, Deque<AbstractLine>> tieMap = new HashMap<Integer, Deque<AbstractLine>>();
		HashMap<Integer, AbstractLine> slurMap = new HashMap<Integer, AbstractLine>();
		HashMap<Integer, AbstractLine> octaveMap = new HashMap<Integer, AbstractLine>();
		HashMap<Integer, AbstractLine> wedgeMap = new HashMap<Integer, AbstractLine>();
		HashMap<Integer, AbstractLine> creOrDimMap = new HashMap<Integer,AbstractLine>();
		HashMap<Integer, AbstractLine> vibratoMap = new HashMap<Integer,AbstractLine>();
	//	HashMap<Integer, AbstractSymbolLine> pedalMap = new HashMap<Integer, AbstractSymbolLine>();
		
		
		//XML文件中出现的标注符号，键、值分别为符号id与符号本身
		HashMap<Integer, Annotation> anotationMap = new HashMap<Integer, Annotation>();
		
		//各个声部的名字，缩写，MIDI乐器. 键为XML文件中读取的声部ID，如"P1". 值为一个list,依次存放声部名字、缩写、MIDI乐器.
		//如果没有则为"none".
		HashMap<String, ArrayList<String>> partInfo = new HashMap<String, ArrayList<String>>();
		
		//前一个小节线的类型
		String preBarlineType = "regular";
		
		//是否刚刚碰到backup标签，backup的时长等于小节时长
		boolean justBackUpToFront = false;
		
		int oldStaff = 1; //上一个音符staff属性值
		
		//自由文本集合
		List<FreeAddedText> fatList = new ArrayList<FreeAddedText>();
		
		int debugNoteIndex = 0;
		
		while(rootIterator.hasNext()){
			
			Element rootEle = (Element)rootIterator.next();
			
			if(rootEle.getName().equalsIgnoreCase("work")){
				title = rootEle.elementText("work-title");
			}
			
			else if(rootEle.getName().equalsIgnoreCase("identification")){
				Iterator<Element> idIt = rootEle.elementIterator();
				while(idIt.hasNext()){
					Element ele = (Element)idIt.next();
					if(ele.getName().equalsIgnoreCase("creator") && ele.attributeValue("type").equalsIgnoreCase("composer")){
						composer = ele.getText();
					}
				}
			}
			
			else if(rootEle.getName().equalsIgnoreCase("score-type")){
				String type = rootEle.getText();
				if(type.equals("normal"))
					scoreType = Score.SCORE_NORMAL;
				else if(type.equals("unlimited"))
					scoreType = Score.SCORE_UNLMTED;
			}
			
			else if(rootEle.getName().equalsIgnoreCase("has-title")){
				String str = rootEle.getText();
				if(str.equals("true"))
					hasTitle = true;
				else if(str.equals("false"))
					hasTitle = false;
			}
			
			else if(rootEle.getName().equalsIgnoreCase("part-list")){
				Iterator<?> partListIt = rootEle.elementIterator();
				while(partListIt.hasNext()){
					Element partListEle = (Element)partListIt.next();
					if(partListEle.getName().equalsIgnoreCase("score-part")){
						String partID = partListEle.attributeValue("id");
						ArrayList<String> info = new ArrayList<String>();
						for(int i = 0; i < 3; i++){
							info.add(null);
						}
						Iterator<?> partIt = partListEle.elementIterator();
						while(partIt.hasNext()){
							//分别解析出声部名字、缩写、MIDI乐器
							Element partEle = (Element)partIt.next();
							if(partEle.getName().equalsIgnoreCase("part-name")){
								String str = partEle.getText() == null ? "none" : partEle.getText();
								info.set(0, str);
							}else if(partEle.getName().equalsIgnoreCase("part-abbreviation")){
								String str = partEle.getText() == null ? "none" : partEle.getText();
								info.set(1, str);
							}else if(partEle.getName().equalsIgnoreCase("midi-instrument")){
								String str = partEle.elementText("midi-program");
								if(str == null) str = "1";
								info.set(2, str);
							}
						}
						partInfo.put(partID, info);
					}
				}
			}
			
			if(rootEle.getName().equalsIgnoreCase("part")){
				//声部ID
				String partID = rootEle.attributeValue("id");
				
				//声部内各个谱表的谱号,调号，拍号
				ArrayList<String> tempClefs = new ArrayList<String>();
				ArrayList<Integer> tempKeys = new ArrayList<Integer>();
				for(int i = 0; i < 2; i++){
					tempClefs.add("g/2");
					tempKeys.add(0);
				}
//				int tempBeats = 4;
//				int tempBeatType = 4;
//				int tempBeat[] = new int[4];
				
				Time timeSignature = new Time(4,4);
				//boolean mixTimeSignature = false;
				
				//指示当前读到了第几个音符节点
				int noteNum = 0;
				//当前的tuplet
				Tuplet tuplet = null;
				
				ArrayList<MeasurePart> meaPartList = new ArrayList<MeasurePart>();
				parts.add(meaPartList);
				
				Iterator<?> partIt = rootEle.elementIterator();
				while(partIt.hasNext()){
					Element partEle = (Element)partIt.next();
					
					//小节标签
					if(partEle.getName().equalsIgnoreCase("measure")){
						justBackUpToFront = false;
						oldStaff = 1;
						
						int fwardDur = 0; //每个小节从起始的累积时长，遇到音符或者forward标签增加，遇到backup减少
						//当前voice
						int voice = 0;
						//当前小节组中小节id.
						int curMeaIndex = 0;
						//前一个生成的音符
						AbstractNote preNote = null;
						
						MeasurePart measurePart = new MeasurePart();
						meaPartList.add(measurePart);
						
						
						Measure measure = null;
						if(scoreType == Score.SCORE_NORMAL){	
							measure = new Measure(tempClefs.get(0), tempKeys.get(0), timeSignature);
//							Element measureIt = partEle.element("attributes");
//							if(measureIt != null){
//								
//								Element measureEle = measureIt.element("time");
//								//System.out.println(measureEle);
//								if(measureEle != null){
//									if (measureEle.elementText("beats").contains("+")){
//										mixTimeSignature = true;
//										measure = new Measure(tempClefs.get(0), tempKeys.get(0), tempBeat, tempBeatType);
//									}else{
//										
//										measure = new Measure(tempClefs.get(0), tempKeys.get(0), tempBeats, tempBeatType);
//									}
//									
//								}else{
//									measure = new Measure(tempClefs.get(0), tempKeys.get(0), tempBeats, tempBeatType);
//								}
//							}else{
//								measure = new Measure(tempClefs.get(0), tempKeys.get(0), tempBeats, tempBeatType);
//							}
							
//							if(tempBeat[0] != 0){
//								measure = new Measure(tempClefs.get(0), tempKeys.get(0), tempBeat, tempBeatType);
//							}else{
//								measure = new Measure(tempClefs.get(0), tempKeys.get(0), tempBeats, tempBeatType);
//							}
						}
						else if(scoreType == Score.SCORE_UNLMTED)
							measure = new Measure(tempClefs.get(0), tempKeys.get(0));
						measure.addMouseListener(controller.getCanvas());
						measurePart.addMeasure(measure);
						//写入乐器等信息
						measure.setPartName(partInfo.get(partID).get(0));
						measure.setPartAbrre(partInfo.get(partID).get(1));
						measure.setInstrument(partInfo.get(partID).get(2));
						
						Iterator<?> measureIt = partEle.elementIterator();
						while(measureIt.hasNext()){
							Element measureEle = (Element)measureIt.next();
							
							//小节属性标签
							if(measureEle.getName().equalsIgnoreCase("attributes")){
								Iterator<?> attrIt = measureEle.elementIterator();
								while(attrIt.hasNext()){
									Element attrEle = (Element)attrIt.next();
									if(attrEle.getName().equalsIgnoreCase("divisions")){
										div = Integer.parseInt(attrEle.getText());
									}
									else if(attrEle.getName().equalsIgnoreCase("key")){
										String fifths = attrEle.element("fifths").getText();
										for(int i = curMeaIndex; i < tempKeys.size(); i++){
											tempKeys.set(i, Integer.parseInt(fifths));
										}
										measure.setKeyValue(tempKeys.get(curMeaIndex));
									}
									else if(attrEle.getName().equalsIgnoreCase("time") && scoreType == Score.SCORE_NORMAL){
										int[] tempBeat = new int[4];
										int tempBeatType;
										int tempBeats;
										if(attrEle.elementText("beats").contains("+")){
											int length = attrEle.elementText("beats").split("[+]").length;
											
											for(int i = 0; i < length; i++){
												tempBeat[i] = Integer.parseInt(attrEle.elementText("beats").split("[+]")[i]);
											}
											
										//	timeSignature.set = tempBeat[0] + tempBeat[1] + tempBeat[2] + tempBeat[3];
											tempBeatType =Integer.parseInt(attrEle.elementText("beat-type"));
											measure.getTime().setBeats(tempBeat[0] + tempBeat[1] + tempBeat[2] + tempBeat[3]);
											measure.getTime().setBeat(tempBeat);
											
											
											measure.getTime().setBeatType(tempBeatType);										
											
										}else{
											tempBeats = Integer.parseInt(attrEle.elementText("beats"));
											tempBeatType =Integer.parseInt(attrEle.elementText("beat-type"));
											for(int i = 0; i < 4; i++){
												tempBeat[i] = 0;
											}
											measure.getTime().setBeat(tempBeat);
											measure.getTime().setBeats(tempBeats);
											measure.getTime().setBeatType(tempBeatType);
										}
										timeSignature = measure.getTime();
									}
									else if(attrEle.getName().equalsIgnoreCase("clef")){
										int attrIndex = attrEle.attributeValue("number") == null ?
												1 : Integer.parseInt(attrEle.attributeValue("number"));
										String sign = attrEle.elementText("sign");
										String line = attrEle.elementText("line");
										String clefOctaveChange = attrEle.elementText("clef-octave-change");
										if(clefOctaveChange != null){
											if(clefOctaveChange.equalsIgnoreCase("1")){
												String clf = sign.toLowerCase()+"1u"+"/"+line.toLowerCase();
												tempClefs.set(attrIndex-1, clf);
											}else if(clefOctaveChange.equalsIgnoreCase("2")){
												String clf = sign.toLowerCase()+"2u"+"/"+line.toLowerCase();
												tempClefs.set(attrIndex-1, clf);
											}else if(clefOctaveChange.equalsIgnoreCase("-1")){
												String clf = sign.toLowerCase()+"1d"+"/"+line.toLowerCase();
												tempClefs.set(attrIndex-1, clf);
											}else if(clefOctaveChange.equalsIgnoreCase("-2")){
												String clf = sign.toLowerCase()+"2d"+"/"+line.toLowerCase();
												tempClefs.set(attrIndex-1, clf);
											}
										}else {
											String clf = sign.toLowerCase();
											if(line != null){
												clf += "/" + line.toLowerCase();
											}
											tempClefs.set(attrIndex-1, clf);
										}
									}
									measure.setClefType(tempClefs.get(curMeaIndex));
								}
							}
							//音符标签
							else if(measureEle.getName().equalsIgnoreCase("note")){
								Note note = new Note();
								debugNoteIndex ++;
								//之前碰到了backup标签,那么该音符可能属于新的小节，或者新的声部
								if(justBackUpToFront){
									System.out.println("NoteIndex: " + debugNoteIndex);
									justBackUpToFront = false;
									int s = 1;
									try {
									s = Integer.parseInt(measureEle.element("staff").getText());
									} catch (Exception e) {System.out.println(e);}
									if(s == oldStaff){  //旧小节，新声部
										voice++;
									}
									else{  //新小节，生成新的小节
										
										System.out.println("AAA: " + s + ", " + oldStaff);
										curMeaIndex++;
										oldStaff = s;
										voice = 0;
										if(scoreType == Score.SCORE_NORMAL)
											measure = new Measure(tempClefs.get(curMeaIndex), tempKeys.get(curMeaIndex), 
													timeSignature.getBeats(), timeSignature.getBeatType());
										else if(scoreType == Score.SCORE_UNLMTED)
											measure = new Measure(tempClefs.get(curMeaIndex), tempKeys.get(curMeaIndex));
										measure.addMouseListener(controller.getCanvas());
										measurePart.addMeasure(measure);
										preNote = null;
									}
								}
								measure.addNote(note, voice);         //...直接添加音符
								note.addMouseListener(controller.getCanvas());
								note.addMouseMotionListener(controller.getCanvas());
								//当前音符
								currentNote = note;
								
								Iterator<?> noteIt = measureEle.elementIterator();
								//指示当前音符是否是和弦
//								boolean isChord = false;
								//读取的duration标签值
								int duration = 0;
								while(noteIt.hasNext()){
									Element noteEle = (Element)noteIt.next();
									
									if(noteEle.getName().equalsIgnoreCase("grace")){
										measure.removeNote(note);
										boolean hasSlash = noteEle.attribute("slash")!=null && noteEle.attribute("slash").getText().equals("yes");
										Grace grace = new Grace(hasSlash);
										graces.add(grace);
										grace.addMouseListener(controller.getCanvas());
										grace.addMouseMotionListener(controller.getCanvas());
										grace.addKeyListener(controller.getCanvas());
										note = grace;
										currentNote = grace;
									}
									else if(noteEle.getName().equalsIgnoreCase("chord")){
										if(currentNote instanceof Grace){
											graces.remove((Grace)currentNote);
											//前一个音符是普通音符
											if(preNote instanceof Note){
												graces.remove(preNote);
												ChordNote cnote = new ChordGrace((Grace)preNote, ((Grace)preNote).isHasSlash());
												cnote.addNote((Note)preNote);
												cnote.addNote(note);
												graces.add(cnote);
												currentNote = cnote;
											}
											//前一个音符已经是和弦
											else if(preNote instanceof ChordNote){
												ChordNote cnote = (ChordNote)preNote;
												cnote.addNote(note);
												currentNote = cnote;
											}
										}
										else{
											measure.removeNote(note);
											//前一个音符是普通音符
											if(preNote instanceof Note){
												measure.removeNote(preNote);
												ChordNote cnote = new ChordNote((Note)preNote);
												cnote.addNote((Note)preNote);
												cnote.addNote(note);
												measure.addNote(cnote, voice);
												currentNote = cnote;
											}
											//前一个音符已经是和弦
											else if(preNote instanceof ChordNote){
												ChordNote cnote = (ChordNote)preNote;
												cnote.addNote(note);
												currentNote = cnote;
											}
										}
									}
									else if(noteEle.getName().equalsIgnoreCase("pitch")){
										
										String tempStep = noteEle.elementText("step");
										int tempOctave = Integer.parseInt(noteEle.elementText("octave"));
										int alter = noteEle.elementText("alter") == null ? 
												0 : Integer.parseInt(noteEle.elementText("alter"));
										
										Note.RealPitch realpitch = note.new RealPitch(tempOctave, tempStep, alter);
										note.setRealPitch(realpitch);
										
										note.determinePitchByRealPitch(tempClefs.get(curMeaIndex));
									}
									else if(noteEle.getName().equalsIgnoreCase("duration")){
										duration = Integer.parseInt(noteEle.getText());
										if(currentNote instanceof Note){
											fwardDur += duration;
											System.out.println("forward: " + duration);
										}
										//判断是否全休止符
										if(note.isRest()){
											int meaDur = div * 4 * timeSignature.getBeats() / timeSignature.getBeatType();
											if(meaDur == duration){
												note.setFull(true);
												note.setDuration(256 * timeSignature.getBeats() / timeSignature.getBeatType());
											}
										}
									}
									else if(noteEle.getName().equalsIgnoreCase("staff")){
										oldStaff = Integer.parseInt(noteEle.getText());
									}
									else if(noteEle.getName().equalsIgnoreCase("rest")){
										note.setRest(true);
										//判断是否全休止符
										if(duration != 0){
											int meaDur = div * 4 * timeSignature.getBeats() / timeSignature.getBeatType();
											if(meaDur == duration){
												note.setFull(true);
												note.setDuration(256 * timeSignature.getBeats() / timeSignature.getBeatType());
											}
										}
									}
									else if(noteEle.getName().equalsIgnoreCase("type")){
										note.setDuration(MusicMath.noteDurationByType(noteEle.getText()));
									}
									else if(noteEle.getName().equalsIgnoreCase("dot")){
										if (currentNote instanceof Note)
											note.setDotNum(note.getDotNum()+1);
									}
									else if(noteEle.getName().equalsIgnoreCase("time-modification")){
										int actual = Integer.parseInt(noteEle.elementText("actual-notes"));
										int normal = Integer.parseInt(noteEle.elementText("normal-notes"));
										if(tuplet == null){
											tuplet = new Tuplet(currentNote, actual, normal);
										}else{
											tuplet.getNoteList().add(currentNote);
											currentNote.setTuplet(tuplet);
										}
									}
									else if(noteEle.getName().equalsIgnoreCase("accidental")){
										String acciStr = noteEle.getText();
										SharpOrFlat sof = new SharpOrFlat(acciStr);
										note.setSharpOrFlat(sof);
										sof.setNote(note);
										sof.addMouseListener(controller.getCanvas());
										sof.addMouseMotionListener(controller.getCanvas());
									}
									else if(noteEle.getName().equalsIgnoreCase("notations")){
										Iterator<?> notationIt = noteEle.elementIterator();
										while(notationIt.hasNext()){
											Element notationsEle = (Element)notationIt.next();
											if(notationsEle.getName().equalsIgnoreCase("tuplet")){
												if(notationsEle.attributeValue("type").equals("stop"))
													tuplet = null;
											}
											if(notationsEle.getName().equalsIgnoreCase("tied")){
												int number = notationsEle.attributeValue("number") == null ?
														curMeaIndex + 1 : Integer.parseInt(notationsEle.attributeValue("number"));
												if(notationsEle.attributeValue("type").equalsIgnoreCase("start")){
													Tie tie = new Tie(note);
													tie.addMouseListener(controller.getCanvas());
													tie.addMouseMotionListener(controller.getCanvas());
													Deque<AbstractLine> queue = tieMap.get(number) == null ? 
															new ArrayDeque<AbstractLine>() : tieMap.get(number);
													if(tieMap.get(number) == null){
														tieMap.put(number, queue);
													}
													queue.add(tie);
//													System.out.println("tie queue size: " + queue.size());
												}
												else if(notationsEle.attributeValue("type").equalsIgnoreCase("stop")){
													Deque<AbstractLine> queue = tieMap.get(number);
													Tie tie = (Tie)queue.poll();
													if(tie != null){
														tie.addMouseListener(controller.getCanvas());
														tie.addMouseMotionListener(controller.getCanvas());
														tie.setEndNote(note);
														note.addTie(tie);
													}
												}
											}else if(notationsEle.getName().equalsIgnoreCase("slur")){
												int number = notationsEle.attributeValue("number") == null ?
														curMeaIndex + 1 : Integer.parseInt(notationsEle.attributeValue("number"));
												if(notationsEle.attributeValue("type").equalsIgnoreCase("start")){
													Slur slur = new Slur(currentNote);
													slur.addMouseListener(controller.getCanvas());
													slur.addMouseMotionListener(controller.getCanvas());
													slurMap.put(number, slur);
												}
												else if(notationsEle.attributeValue("type").equalsIgnoreCase("stop")){
													Slur slur = (Slur)slurMap.get(number);
													if(slur != null){
														slur.setEndNote(currentNote);
														currentNote.getSymbolLines().add(slur);
														slurMap.remove(number);
													}
												}
											}else if(notationsEle.getName().equalsIgnoreCase("articulations")){
												Iterator<?> artiIt = notationsEle.elementIterator();
												while(artiIt.hasNext()){
													Element artiEle = (Element)artiIt.next();
													String name = artiEle.getName();
													String strOrnm = null;
													String strBreath = null;
													if(name.equals("strong-accent")){
														String direction = artiEle.attributeValue("type");
														strOrnm = (direction != null && direction.equals("up")) ? 
																"strongAccentUp" : "strongAccentDown";
													}
													else if(name.equals("staccatissimo")){
														strOrnm = "staccatissimoUp";
													}
													else if(name.equals("staccato") || name.equals("tenuto") ||
														    name.equals("accent") || name.equals("staccatissimoUp") ||
														    name.equals("staccatissimoDown") || name.equals("staccatissimoUp") ){
														strOrnm = name;
													}
													else if(name.equals("breath-mark")){
														strBreath = "breath";
													}
													if(strOrnm != null && !currentNote.hasOrnament(strOrnm)){
														Ornament or = new Ornament(strOrnm);
														or.addMouseListener(controller.getCanvas());
														or.addMouseMotionListener(controller.getCanvas());
														currentNote.addOrnament(or);
														or.setNote(currentNote);
													}
													if(strBreath != null){
														Breath breath = new Breath();
														currentNote.addBreath(breath);
														breath.addMouseListener(controller.getCanvas());
													}
												}
											}else if(notationsEle.getName().equalsIgnoreCase("ornaments")){
												Iterator<?> orIt = notationsEle.elementIterator();
												while(orIt.hasNext()){
													Element orEle = (Element)orIt.next();
													if(orEle.getName().equals("tremolo")){
														if(orEle.attribute("type").getText().equals("single")){
															String str = "tremoloBeam" + orEle.getText();
															TremoloBeam tb = new TremoloBeam(str);
															currentNote.setTremoloBeam(tb);
															tb.setNote(currentNote);
															tb.addMouseListener(controller.getCanvas());
														}
													}
													else if(orEle.getName().equals("turn") || orEle.getName().equals("inverted-turn") ||
															orEle.getName().equals("mordent") || orEle.getName().equals("inverted-mordent") ||
															orEle.getName().equals("trill-mark") || orEle.getName().equals("trill-natural") ||
															orEle.getName().equals("trill-sharp") || orEle.getName().equals("trill-flat")){
														GraceSymbol gs = new GraceSymbol(orEle.getName());
														currentNote.addGraceSymbol(gs);
														gs.addMouseListener(controller.getCanvas());
													}
												}
											}else if(notationsEle.getName().equalsIgnoreCase("fermata")){
//												if(notationsEle.attributeValue("type").equalsIgnoreCase("upright")){
													Ornament or = new Ornament("fermata");
													or.addMouseListener(controller.getCanvas());
													or.addMouseMotionListener(controller.getCanvas());
													currentNote.addOrnament(or);
													or.setNote(currentNote);
//												}
											}
										}
									}
									else if(noteEle.getName().equalsIgnoreCase("annotation")){
										int id = Integer.parseInt(noteEle.attributeValue("id"));
										String text = noteEle.elementText("text");
										Annotation annotation;
										if(anotationMap.get(id) == null){
											annotation = new Annotation(text);
											annotation.addMouseListener(controller.getCanvas());
											annotation.addMouseMotionListener(controller.getCanvas());
											annotation.getImagePanel().addMouseListener(controller.getCanvas());
											annotation.getImagePanel().addMouseMotionListener(controller.getCanvas());
											anotationMap.put(id, annotation);
										}else{
											annotation = anotationMap.get(id);
										}
										note.getAnnotations().add(annotation);
										annotation.getRelatedObjts().add(note);
									}
									else if(noteEle.getName().equalsIgnoreCase("beam")){
										int num = Integer.parseInt(noteEle.attributeValue("number"));
										if(num == 1){
											String beamType = noteEle.getText();
											if(beamType.equalsIgnoreCase("begin") || beamType.equalsIgnoreCase("continue"))
												currentNote.setBeamType(beamType);
										}else{
											continue;
										}
									}
									else if(noteEle.getName().equalsIgnoreCase("lyric")){
										int num = Integer.parseInt(noteEle.attributeValue("number"));
										while(currentNote.getLyricsNum() < num){
											currentNote.addLyrics(null);
										}
										String text = noteEle.elementText("text");
										Lyrics lyric = new Lyrics(text);
										lyric.addMouseListener(controller.getCanvas());
										lyric.addMouseMotionListener(controller.getCanvas());		
										lyric.viewMode();
										currentNote.setLyrics(num-1, lyric);
										lyric.setNote(currentNote);
									}
								}
								
								//该音符提前产生的音符符号
								if(preNoteSymbols.size() > 0){
									for(int i = 0, n = preNoteSymbols.size(); i < n; i++){
										NoteSymbol nsl = preNoteSymbols.get(i);
										nsl.addMouseListener(controller.getCanvas());
										nsl.addMouseMotionListener(controller.getCanvas());
										currentNote.addNoteSymbol(nsl);
									}
									preNoteSymbols.clear();
								}
								
								//属于该音符的提前产生的倚音
								if(!(currentNote instanceof Grace) && !(currentNote instanceof ChordGrace)
										&& !graces.isEmpty()){
									for(int i = 0; i < graces.size(); i++){
										currentNote.addLeftGrace(graces.get(i));
									}
									graces.clear();
								}
								
								//以该音符为截止音符，提前产生的线条符号
//								if(prePedals.size() > 0){
//									int staffIndex = measurePart.measureIndex(measure);
//									SymbolLine sbl = (SymbolLine)prePedals.get(staffIndex + 1);
//									if(sbl != null){
//										sbl.setEndNote(currentNote); 
//										currentNote.getSymbolLines().add(sbl);
//										prePedals.remove(staffIndex + 1);
//									}
//								}
								if(preOctaves.size() > 0){
									int staffIndex = measurePart.measureIndex(measure);
									SymbolLine sbl = (SymbolLine)preOctaves.get(staffIndex + 1);
									if(sbl != null){
										sbl.setEndNote(currentNote); 
										currentNote.getSymbolLines().add(sbl);
										preOctaves.remove(staffIndex + 1);
									}
								}
								if(preCreAndDims.size() > 0){
									int staffIndex = measurePart.measureIndex(measure);
									SymbolLine sbl = (SymbolLine)preCreAndDims.get(staffIndex + 1);
									if(sbl != null){
										sbl.setEndNote(currentNote); 
										currentNote.getSymbolLines().add(sbl);
										preCreAndDims.remove(staffIndex + 1);
									}
								}
								
								if(preVibratos.size() > 0){
									int staffIndex = measurePart.measureIndex(measure);
									SymbolLine sbl = (SymbolLine)preVibratos.get(staffIndex + 1);
									if(sbl != null){
										sbl.setEndNote(currentNote); 
										currentNote.getSymbolLines().add(sbl);
										preVibratos.remove(staffIndex + 1);
									}
								}
								
								if(preRepeatEnding.size() > 0){
									int staffIndex = measurePart.measureIndex(measure);
									RepeatLine rl = (RepeatLine)preRepeatEnding.get(staffIndex + 1);
									if(rl != null){
										rl.setEndMeasurePart(measurePart);
										measurePart.getRepeatLines().add(rl);
										preRepeatEnding.remove(staffIndex + 1);
									}
									
								}
								
								//以该音符为起点，提前产生的线条音符
								//以该音符为起点，提前产生的线条符号
//								if(pedalMap.size() > 0){
//									int staffIndex = measurePart.measureIndex(measure);
//									SymbolLine sbl = (SymbolLine)pedalMap.get(staffIndex + 1);
//									if(sbl != null && sbl.getStartNote() == null){
//										sbl.addMouseListener(controller.getCanvas());
//										sbl.addMouseMotionListener(controller.getCanvas());
//										sbl.setStartNote(currentNote);
//										currentNote.getSymbolLines().add(sbl);
//									}
//								}
								if(octaveMap.size() > 0){
									int staffIndex = measurePart.measureIndex(measure);
									SymbolLine sbl = (SymbolLine)octaveMap.get(staffIndex + 1);
									if(sbl != null && sbl.getStartNote() == null){
										sbl.addMouseListener(controller.getCanvas());
										sbl.addMouseMotionListener(controller.getCanvas());
										sbl.setStartNote(currentNote);
										currentNote.getSymbolLines().add(sbl);
									}
								}
								if(wedgeMap.size() > 0){
									int staffIndex = measurePart.measureIndex(measure);
									SymbolLine sbl = (SymbolLine)wedgeMap.get(staffIndex + 1);
									if(sbl != null && sbl.getStartNote() == null){
										sbl.addMouseListener(controller.getCanvas());
										sbl.addMouseMotionListener(controller.getCanvas());
										sbl.setStartNote(currentNote);
										currentNote.getSymbolLines().add(sbl);
									}
								}
								if(creOrDimMap.size() > 0){
									int staffIndex = measurePart.measureIndex(measure);
									SymbolLine sbl = (SymbolLine)creOrDimMap.get(staffIndex + 1);
									if(sbl != null && sbl.getStartNote() == null){
										sbl.addMouseListener(controller.getCanvas());
										sbl.addMouseMotionListener(controller.getCanvas());
										sbl.setStartNote(currentNote);
										currentNote.getSymbolLines().add(sbl);
									}	
								}
								if(vibratoMap.size() > 0){
									int staffIndex = measurePart.measureIndex(measure);
									SymbolLine sbl = (SymbolLine)vibratoMap.get(staffIndex + 1);
									if(sbl != null && sbl.getStartNote() == null){
										sbl.addMouseListener(controller.getCanvas());
										sbl.addMouseMotionListener(controller.getCanvas());
										sbl.setStartNote(currentNote);
										currentNote.getSymbolLines().add(sbl);
									}	
								}
								
								
								//生成附点实体
								currentNote.generateUIDot();
								
								preNote = currentNote;
								
								noteNum++;
							}
							
							//direction标签
							else if(measureEle.getName().equalsIgnoreCase("direction")){
								Element dirEle = measureEle.element("direction-type");
								Element stf = measureEle.element("staff");
								int staffNum = stf == null ? measurePart.measureIndex(measure) + 1 : 
									Integer.parseInt(stf.getText());
								Iterator<?> dirTypeIt = dirEle.elementIterator();
								while(dirTypeIt.hasNext()){
									Element dirTypeEle = (Element)dirTypeIt.next();
									
									//dynamic
									if(dirTypeEle.getName().equalsIgnoreCase("dynamics")){
									//	System.out.println(dirTypeEle);
										Iterator<?> dynaIt = dirTypeEle.elementIterator();
										while(dynaIt.hasNext()){
											Element dynaEle = (Element)dynaIt.next();
											NoteSymbol nsl = new Dynamic(dynaEle.getName());
											preNoteSymbols.add(nsl);
											nsl.addMouseListener(controller.getCanvas());
										}
									}
									else if(dirTypeEle.getName().equalsIgnoreCase("pedal")){
										
										
										String  s =dirTypeEle.attributeValue("type");
										NoteSymbol nsl = new Pedal(s);
										preNoteSymbols.add(nsl);
										nsl.addMouseListener(controller.getCanvas());
//										if(s.equalsIgnoreCase("start")){
//										//	System.out.println(s);
//											NoteSymbol nsl = new Pedal("start");
//											preNoteSymbols.add(nsl);
//											nsl.addMouseListener(controller.getCanvas());
//										}else if(s.equalsIgnoreCase("stop")){
//										//	System.out.println(s);
//											NoteSymbol nsl = new Pedal("end");
//											preNoteSymbols.add(nsl);
//											nsl.addMouseListener(controller.getCanvas());
//										}
									}
									//表情术语					
									else if(dirTypeEle.getName().equalsIgnoreCase("words")){
									
										
										//	System.out.println(dirTypeEle.getT);
//											if(!dirTypeEle.getText().equals("con brio")||!dirTypeEle.getText().equals("con spirito")){
//											//	System.out.println("has");
//												NoteSymbol nsl = new PerformanceSymbol(dirTypeEle.getText());
//												preNoteSymbols.add(nsl);
//												nsl.addMouseListener(controller.getCanvas());
//											
//										//	NoteSymbol nsl = new Dynamic(dynaEle.getName());
//									
//										}else if(dirTypeEle.getText().equals("con brio")){
//											NoteSymbol nsl = new PerformanceSymbol("conBrio");
//											preNoteSymbols.add(nsl);
//											nsl.addMouseListener(controller.getCanvas());
//										}else if(dirTypeEle.getText().equals("con spirito")){
//											NoteSymbol nsl = new PerformanceSymbol("conSpirito");
//											preNoteSymbols.add(nsl);
//											nsl.addMouseListener(controller.getCanvas());
//										}
											
										if(dirTypeEle.getText().equals("con brio")){
											NoteSymbol nsl = new PerformanceSymbol("conBrio");
											preNoteSymbols.add(nsl);
											nsl.addMouseListener(controller.getCanvas());
										}else if(dirTypeEle.getText().equals("con spirito")){
											NoteSymbol nsl = new PerformanceSymbol("conSpirito");
											preNoteSymbols.add(nsl);
											nsl.addMouseListener(controller.getCanvas());
										}else if(dirTypeEle.getText().equals("crescendo")){
											SymbolLine sbl = new Cresc(null);
											creOrDimMap.put(staffNum, sbl);
										}else if(dirTypeEle.getText().equals("diminuendo")){
											SymbolLine sbl = new Dimc(null);
											creOrDimMap.put(staffNum, sbl);
										}else if(dirTypeEle.getText().equals("affetuoso")||dirTypeEle.getText().equals("agitato")||dirTypeEle.getText().equals("animato")
												||dirTypeEle.getText().equals("brillante")||dirTypeEle.getText().equals("conBrio")||dirTypeEle.getText().equals("cantabile")
												||dirTypeEle.getText().equals("deciso")||dirTypeEle.getText().equals("dolce")||dirTypeEle.getText().equals("dolento")
												||dirTypeEle.getText().equals("espressivo")||dirTypeEle.getText().equals("energico")||dirTypeEle.getText().equals("furioso")
												||dirTypeEle.getText().equals("giocoso")||dirTypeEle.getText().equals("grave")||dirTypeEle.getText().equals("grazioso")
												||dirTypeEle.getText().equals("maestoso")||dirTypeEle.getText().equals("misterioso")||dirTypeEle.getText().equals("passionato")
												||dirTypeEle.getText().equals("sostenuto")||dirTypeEle.getText().equals("conSpirito")||dirTypeEle.getText().equals("tranquillo")){
											NoteSymbol nsl = new PerformanceSymbol(dirTypeEle.getText());
											preNoteSymbols.add(nsl);
											nsl.addMouseListener(controller.getCanvas());
										}
										else{
											TempoText nsl = new TempoText(dirTypeEle.getText());
											preNoteSymbols.add(nsl);
											nsl.addMouseListener(controller.getCanvas());
										}
									}
									//踏板记号
								//	else if(dirTypeEle.getName().equalsIgnoreCase(anotherString))
									//渐强、减弱 
									else if(dirTypeEle.getName().equalsIgnoreCase("wedge")){
										SymbolLine sbl = null;
										String type = dirTypeEle.attributeValue("type");
										if(type.equalsIgnoreCase("crescendo")){
											sbl = new Cre(null);
											wedgeMap.put(staffNum, sbl);
										}
										else if(type.equalsIgnoreCase("diminuendo")){
											sbl = new Dim(null);
											wedgeMap.put(staffNum, sbl);
										}
										else if(type.equalsIgnoreCase("stop")){
											sbl = (SymbolLine)wedgeMap.get(staffNum);
											wedgeMap.remove(staffNum);
											preCreAndDims.put(staffNum, sbl);
										}
									}
									else if(dirTypeEle.getName().equalsIgnoreCase("vibrato")){
										
										SymbolLine sbl = null;
										String type = dirTypeEle.attributeValue("type");
										if(type.equalsIgnoreCase("vibrato")){
											System.out.println("stange");
											sbl = new Vibrato(null);
											vibratoMap.put(staffNum, sbl);
										}else if(type.equalsIgnoreCase("stop")){
											sbl = (SymbolLine)vibratoMap.get(staffNum);
											vibratoMap.remove(staffNum);
											preVibratos.put(staffNum, sbl);
										}
									}
									//pedal
//									else if(dirTypeEle.getName().equalsIgnoreCase("pedal")){
//										SymbolLine sbl = null;
//										String type = dirTypeEle.attributeValue("type");
//										if(type.equalsIgnoreCase("start")){
//											sbl = new Pedal(null);
//											pedalMap.put(staffNum, sbl);
//										}
//										else if(type.equalsIgnoreCase("stop")){
//											sbl = (SymbolLine)pedalMap.get(staffNum);
//											pedalMap.remove(staffNum);
//											prePedals.put(staffNum, sbl);
//										}
//									}

									//高、低八度
									else if(dirTypeEle.getName().equalsIgnoreCase("octave-shift")){
										SymbolLine sbl = null;
										String type = dirTypeEle.attributeValue("type");
										if(type.equalsIgnoreCase("down")){
											sbl = new OctaveDown(null);
											octaveMap.put(staffNum, sbl);
										}
										else if(type.equalsIgnoreCase("up")){
											sbl = new OctaveUp(null);
											octaveMap.put(staffNum, sbl);
										}
										else if(type.equalsIgnoreCase("stop")){
											sbl = (SymbolLine)octaveMap.get(staffNum);
											octaveMap.remove(staffNum);
											preOctaves.put(staffNum, sbl);
										}
									}
									//反复记号
									else if(dirTypeEle.getName().equalsIgnoreCase("coda") || dirTypeEle.getName().equalsIgnoreCase("segno")){
										RepeatSymbol rs = new RepeatSymbol(dirTypeEle.getName());
										measurePart.addRepeatSymbol(rs);
										rs.addMouseListener(controller.getCanvas());
									}
									//文字，反复记号或者速度记号
									else if(dirTypeEle.getName().equalsIgnoreCase("words")){
										String words = dirTypeEle.getText();
										String name = null;
										if(words.equalsIgnoreCase("D.C. al Coda")){
											name = "dcCoda";
										}
										else if(words.equalsIgnoreCase("D.C. al Fine")){
											name = "dcFine";
										}
										else if(words.equalsIgnoreCase("D.S. al Fine")){
											name = "dsFine";
										}
										else if(words.equalsIgnoreCase("D.S. al Coda")){
											name = "dsCoda";
										}
										else if(words.equalsIgnoreCase("D.C.")){
											name = "dc";
										}
										else if(words.equalsIgnoreCase("D.S.")){
											name = "ds";
										}
										else if(words.equalsIgnoreCase("To Coda")){
											name = "toCoda";
										}
										if(name != null){
											RepeatSymbol rs = new RepeatSymbol(name);
											measurePart.addRepeatSymbol(rs);
											rs.addMouseListener(controller.getCanvas());
										}
										//name为空，即不是反复记号，则是速度记号
										else{
											TempoText tt = null;
											Element sound = measureEle.element("sound");
											if(sound != null && sound.attributeValue("tempo") != null){
												int tempo = Integer.parseInt(sound.attributeValue("tempo"));
												tt = new TempoText(words, tempo, true);
											}else{
												tt = new TempoText(words);
											}
											tt.addMouseListener(controller.getCanvas());
											preNoteSymbols.add(tt);
										}
									}
								}
							}
							
							else if(measureEle.getName().equalsIgnoreCase("barline")){
								Iterator barlineIt = measureEle.elementIterator();
								while(barlineIt.hasNext()){
									Element barlineEle = (Element)barlineIt.next();
									if(barlineEle.getName().equalsIgnoreCase("bar-style")){
										String type = null;
										Element repeat = measureEle.element("repeat");
										if(repeat != null){
											type = repeat.attributeValue("direction");
										}else{
											type = barlineEle.getText();
										}
										if(measureEle.attribute("location") != null && measureEle.attribute("location").getText().equals("right")){
											Barline barline = new Barline(type);
											measurePart.setBarline(barline);
											barline.setMeaPart(measurePart);
											barline.addMouseListener(controller.getCanvas());
											preBarlineType = type;
										}
										else if(measureEle.attribute("location").getText().equals("left")){
											int meaPartIndex = meaPartList.indexOf(measurePart);
											if(meaPartIndex <= 0)
												return;
											else{
												String newType = Barline.genBarlineStyle(preBarlineType, type);
												MeasurePart prePart = meaPartList.get(meaPartIndex-1);
												if(prePart.getBarline() != null){
													prePart.getBarline().setType(newType);
												}else{
													Barline barline = new Barline(newType);
													barline.addMouseListener(controller.getCanvas());
													prePart.setBarline(barline);
													barline.setMeaPart(prePart);
												}
												preBarlineType = "regular";
											}
										}
									}
									if(barlineEle.getName().equalsIgnoreCase("ending")){
										int number;
										
										if(measureEle.attribute("location") != null && measureEle.attribute("location").getText().equals("left")){
											//JTextField textField = new JTextField(number);
											if(barlineEle.attributeValue("number").contains(" ")){
												String numberString = barlineEle.attributeValue("number");
												String[] numberArray = null;
												int[] numbers = new int [3];
												numberArray = numberString.split(" ");
												for(int i = 0; i < numberArray.length; i++){
													numbers[i] = Integer.parseInt(numberArray[i]);
												}

												RepeatEnding repeatEnding = new RepeatEnding(measurePart,numbers);
												
												repeatEnding.setStartMeasurePart(measurePart);
												preRepeatEnding.put(0, repeatEnding);
												repeatEnding.addMouseListener(controller.getCanvas());
												repeatEnding.addKeyListener(controller.getCanvas());

											}else{
												number  = Integer.parseInt(barlineEle.attributeValue("number")) ;
												RepeatEnding repeatEnding = new RepeatEnding(measurePart,number);
												
												repeatEnding.setStartMeasurePart(measurePart);
												preRepeatEnding.put(0, repeatEnding);
												repeatEnding.addMouseListener(controller.getCanvas());
												repeatEnding.addKeyListener(controller.getCanvas());
											}
										//	RepeatEnding repeatEnding = new RepeatEnding();
										}else if(measureEle.attribute("location") != null && measureEle.attribute("location").getText().equals("right")){
											if(!preRepeatEnding.isEmpty()){
												RepeatEnding repeatEnding = (RepeatEnding) preRepeatEnding.get(0);
												repeatEnding.setEndMeasurePart(measurePart);
												preRepeatEnding.remove(0);
											}
										}
									}
								}
							}
							
							//anotation标签，即标注标签
							else if(measureEle.getName().equalsIgnoreCase("annotation")){
								int id = Integer.parseInt(measureEle.attributeValue("id"));
								String text = measureEle.elementText("text");
								Annotation annotation;
								if (anotationMap.get(id) == null) {
									annotation = new Annotation(text);
									annotation.addMouseListener(controller.getCanvas());
									annotation.addMouseMotionListener(controller.getCanvas());
									annotation.getImagePanel().addMouseListener(controller.getCanvas());
									annotation.getImagePanel().addMouseMotionListener(controller.getCanvas());
									anotationMap.put(id, annotation);
								} else {
									annotation = anotationMap.get(id);
								}
								measure.getAnnotations().add(annotation);
								annotation.getRelatedObjts().add(measure);
								System.out.println("MEASURE ANNO");
							}
							
							//backup标签
							else if(measureEle.getName().equalsIgnoreCase("backup")){
								int backDur = Integer.parseInt(measureEle.elementText("duration"));
								System.out.println("backup: " + backDur);
								fwardDur -= backDur;
								
								if(fwardDur == 0){   // div * 4 * measure.getTime().getBeats() / measure.getTime().getBeatType()
									justBackUpToFront = true;
								}else{
									voice++; //进入相同小节下一个声部
									int dur = (measure.getTime().getBeats() / measure.getTime().getBeatType())*256 - transDuration(div, backDur);
									this.addSpaceWithInvisibleNotes(dur, measure, voice);
								}
							}
							
							//forward标签
							else if(measureEle.getName().equalsIgnoreCase("forward")){
								int forwardDur = Integer.parseInt(measureEle.elementText("duration"));
								if(!justBackUpToFront){
									this.addSpaceWithInvisibleNotes(transDuration(div, forwardDur), measure, voice);
								}
								else{
									justBackUpToFront = false;
									voice++;
									this.addSpaceWithInvisibleNotes(forwardDur, measure, voice);
								}
								fwardDur += forwardDur;
								System.out.println("forward: " + forwardDur);
							}
						}
					}
				}
			}
			
			//自由文本
			else if(rootEle.getName().equals("added-text")){
				Iterator<?> it = rootEle.elementIterator();
				while(it.hasNext()){
					Element eleT = (Element)it.next();
					if(!eleT.getName().equals("text"))
						continue;
					Iterator<?> tit = eleT.elementIterator();
					FreeAddedText faText = new FreeAddedText();
					fatList.add(faText);
					while(tit.hasNext()){
						Element eleTT = (Element)tit.next();
						if(eleTT.getName().equals("content"))
							faText.setText(eleTT.getText());
						else if(eleTT.getName().equals("font")){
							String fname = eleTT.elementText("name");
							String fstyle = eleTT.elementText("style");
							String fsize = eleTT.elementText("size");
							faText.setFont(new Font(fname, Integer.parseInt(fstyle), Integer.parseInt(fsize)));
							faText.reSize();
							faText.revalidate();
						}
						else if(eleTT.getName().equals("x")){
							faText.setLocation(Integer.parseInt(eleTT.getText()), faText.getY());
						}
						else if(eleTT.getName().equals("y")){
							faText.setLocation(faText.getX(), Integer.parseInt(eleTT.getText()));
						}
						else if(eleTT.getName().equals("page-index")){
							faText.setPageIndex(Integer.parseInt(eleTT.getText()));
						}
					}
				}
			}
		}
		//合并声部
		ArrayList<MeasurePart> result = mergeParts(parts);
		
		for(int i = 0, n = result.size(); i < n; i++){
			MeasurePart mea = result.get(i);
			if(mea.getBarline() != null)
				continue;
			if(i == n-1){
				Barline barline = new Barline(mea,"light-heavy");
				barline.setMeaPart(mea);
			}else{
				Barline barline = new Barline(mea,"regular");
				barline.setMeaPart(mea);
			}
		}

		score = controller.makeScoreByMeasureParts(result, fatList);
		score.setScoreType(scoreType);
		score.setHasTitle(hasTitle);
		if(hasTitle){
			//设置标题与作者
			Page fpage = score.getPageList().get(0);
			fpage.generateTitleBox();
			fpage.getTitleBox().setTitle(title);
			fpage.getTitleBox().setComposer(composer);
		}

		System.out.println("导入开始，共有 " + score.getPageList().size() + " 页! FREE " + score.getAddedTexts().size()) ;
	}
	
	private void addSpaceWithInvisibleNotes(int dur, Measure measure, int voice){
		List<Note> list = MusicMath.splitDurIntoRests(dur);
		for(int i = 0; i < list.size(); i++){
			Note nn = list.get(i);
			nn.setVisible(false);
			measure.addNote(nn, voice);
			nn.addMouseListener(controller.getCanvas());
			nn.addMouseMotionListener(controller.getCanvas());
		}
	}
	
	/**
	 * 将XML里measure的div属性为指定值的时长dur换算为系统真实时长
	 * @param div
	 * @param dur
	 * @return
	 */
	public static int transDuration(int div, int dur){
		return dur * 64 / div;
	}
	
	/**
	 * 把几个单独的声部合并为一个声部，每个声部包含若干个小节组
	 * @param parts
	 * @return
	 */
	private ArrayList<MeasurePart> mergeParts(ArrayList<ArrayList<MeasurePart>> parts){
		ArrayList<MeasurePart> firstPart = parts.get(0);
		
		for(int i = 0, n = firstPart.size(); i < n; i++){
			MeasurePart measurePart = firstPart.get(i);
			for(int j = 1; j < parts.size(); j++){
				ArrayList<MeasurePart> part = parts.get(j);
				
				if(part.size() != firstPart.size()){
					System.err.println("不同声部的小节组个数不同，无法合并！");
					return null;
				}
				
				MeasurePart tempPart = part.get(i);
				for(int k = 0; k < tempPart.getMeasureNum(); k++){
					Measure measure = tempPart.getMeasure(k);
					measurePart.addMeasure(measure);
				}
				
			}
		}
		return firstPart;
	}
	
	/**
	 * 根据乐谱生产XML文件
	 */
	public Document makeXML(){
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("score-partwise");
		
		Element identification = root.addElement("identification");
		Element encoding = identification.addElement("encoding");
		Element encodingData = encoding.addElement("encoding-date");
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String mDateTime = formatter.format(cal.getTime());
		encodingData.setText(mDateTime);
		
		Element scoreType = root.addElement("score-type");
		if(score.getScoreType() == Score.SCORE_NORMAL)
			scoreType.setText("normal");
		else if(score.getScoreType() == Score.SCORE_UNLMTED){
			scoreType.setText("unlimited");
		}
		
		Element hasTitle = root.addElement("has-title");
		if(score.isHasTitle() && score.getPageList().get(0).getTitleBox() != null)
			hasTitle.setText("true");
		else 
			hasTitle.setText("false");
		
		Element partList = root.addElement("part-list");
		NoteLine fline = score.getPageList().get(0).getNoteLines().get(0);
		MeasurePart fmeaPart = fline.getMeaPartList().get(0);
		//表示当前是第几个乐器 
		int instrNum = 0;
		//annotation序号
		int annNum = 1;
		for(int i = 0; i < fmeaPart.getMeasureNum(); i++){
			Measure measure = fmeaPart.getMeasure(i);
			if(measure.getInstrument() != null){
				instrNum++;
				Element part = partList.addElement("score-part");
				part.addAttribute("id", "P" + String.valueOf(instrNum));
				Element name = part.addElement("part-name");
				if(measure.getPartName() != null)
					name.setText(measure.getPartName());
				Element abbre = part.addElement("part-abbreviation");
				if(measure.getPartAbrre() != null)
					abbre.setText(measure.getPartAbrre());
				Element midiInstr = part.addElement("midi-instrument");
				Element channel = midiInstr.addElement("midi-channel");
				channel.setText(String.valueOf(instrNum));
				Element program = midiInstr.addElement("midi-program");
				program.setText(measure.getInstrument());
			}
		}
		
		//第一个乐器的最后一个小节在小节组中的位置. 主要用于对反复记号生成位置的判断
		int llindex = fline.getPartNum() > 1 ? fmeaPart.getMeaIndxByInstrIndx(2)-1 : fmeaPart.getMeasureNum()-1;
		
		//逐个乐器生成XML文档
		for(int i = 0, in = fline.getPartNum(); i < in; i++){
			
			Element part = root.addElement("part");
			part.addAttribute("id", "P"+String.valueOf(i+1));
			
			int meaIndex = fmeaPart.getMeaIndxByInstrIndx(i);
			
			//存储各个连句号的ID映射
			HashMap<Slur, String> slurMap = new HashMap<Slur, String>();
			
			for(int j = 0, jn = score.getPageList().size(); j < jn; j++){
				Page page = score.getPageList().get(j);
				
				for(int k = 0, kn = page.getNoteLines().size(); k < kn; k++){
					NoteLine line = page.getNoteLines().get(k);
					
					//下一个小节组是否有左小节线.类型是带有repeat的heavy-light
					boolean nxtBarlineForward = false;
					
					for(int p = 0, pn = line.getMeaPartList().size(); p < pn; p++){
						MeasurePart meaPart = line.getMeaPartList().get(p);
						Element eleMea = part.addElement("measure");
						
						MeaLoop:
						for(int m = meaIndex; m < meaPart.getMeasureNum(); m++){
							Measure measure = meaPart.getMeasure(m);
							
							//已经是一个新的乐器，则退出该乐器的循环
							if(measure.getInstrument() != null && meaIndex != m)
								break MeaLoop;
							
							//不是该乐器的第一个声部
							if(measure.getInstrument() == null){
								Element eleBackup = eleMea.addElement("backup");
								Element eleDur = eleBackup.addElement("duration");
								eleDur.setText(String.valueOf(MusicMath.getMeasureDuration(measure)));
							}
							
							Element eleAttr = eleMea.addElement("attributes");
							Element eleDiv = eleAttr.addElement("divisions");
							eleDiv.setText(String.valueOf(64));
							if(measure.getUiKey() != null){
								MeasurePart prePart = MusicMath.preMeasurePart(meaPart);
								if(prePart == null || 
										(prePart != null && prePart.getMeasure(m).getKeyValue() != measure.getKeyValue())){
									Element eleKey = eleAttr.addElement("key");
									Element eleFifths = eleKey.addElement("fifths");
									eleFifths.setText(String.valueOf(measure.getKeyValue()));
									Element eleMode = eleKey.addElement("mode");
									eleMode.setText("major");
								}
							}
							
							//是乐器的第一个声部
							if(measure.getInstrument() != null){
								if(measure.getUiTime() != null){
									MeasurePart prePart = MusicMath.preMeasurePart(meaPart);
									if(prePart == null || 
											(prePart != null && !prePart.getMeasure(m).getTime().equals(measure.getTime()))){
										Element eleTime = eleAttr.addElement("time");
										Element eleBeats = eleTime.addElement("beats");
										if(measure.getTime().getBeat()[0] == 0){
											eleBeats.setText(String.valueOf(measure.getTime().getBeats()));
										}else{
											
											eleBeats.setText(String.valueOf(measure.getTime().getBeat()[0]) + "+" +
													String.valueOf(measure.getTime().getBeat()[1]) + "+" +
													String.valueOf(measure.getTime().getBeat()[2]) + "+" +
													String.valueOf(measure.getTime().getBeat()[3])
											);
										}
									
										Element eleBT = eleTime.addElement("beat-type");
										eleBT.setText(String.valueOf(measure.getTime().getBeatType()));
									}
								}
								if(j == 0 && k == 0 && p ==0){
									int num = line.getStavesInPart(i);
									eleAttr.addElement("staves").setText(String.valueOf(num));
								}
							}
							
							if(measure.getUiClef() != null){
								MeasurePart prePart = MusicMath.preMeasurePart(meaPart);
								if(prePart == null || 
										(prePart != null && !prePart.getMeasure(m).getClefType().equals(measure.getClefType()))){
									Element eleClef = eleAttr.addElement("clef");
									eleClef.addAttribute("number", String.valueOf(m - meaIndex + 1));
									String sign = measure.getClefType().split("/")[0];
									String ln = measure.getClefType().split("/")[1];
									if(sign.length() == 1){
				
										eleClef.addElement("sign").setText(sign.toUpperCase());
										eleClef.addElement("line").setText(ln.toUpperCase());
									}else{
										String s1 = String.valueOf(sign.charAt(0));
										if(String.valueOf(sign.charAt(2)).equalsIgnoreCase("u")){
											String s2 = String.valueOf(sign.charAt(1));
											eleClef.addElement("clef-octave-change").setText(s2.toUpperCase());
										}else if(String.valueOf(sign.charAt(2)).equalsIgnoreCase("d")){
											String s2 = "-" + String.valueOf(sign.charAt(1));
											eleClef.addElement("clef-octave-change").setText(s2.toUpperCase());
										}
									//	if(sign.equalsIgnoreCase("
									//	eleClef.addElement("sign").setText(sign.charAt(0).toUpperCase());
										eleClef.addElement("sign").setText(s1.toUpperCase());
										eleClef.addElement("line").setText(ln.toUpperCase());
									}	
								}
							}
							
//							if(measure.getMeasurePart().getRepeatLines() != null){
//								Element eleBarline = eleMea.addElement("barline").addAttribute("location", "left");
//								Element eleRepeatEnding = eleBarline.addElement("ending");
//								int number = ((RepeatEnding) measure.getMeasurePart().getRepeatLines().get(0)).getNumber();
//								Sysstem.out.println("yes" + number);
////								eleRepeatEnding.addAttribute("number", value)
////								eleStyle.setText("heavy-light");
////								eleBarline.addElement("repeat").addAttribute("direction", "forward");
////								nxtBarlineForward = false;
//								
//							}
							//房子记号
							if(!meaPart.getRepeatLines().isEmpty()){
									for(int rr=0; rr < meaPart.getRepeatLines().size(); rr++){
										RepeatLine rl = meaPart.getRepeatLines().get(rr);
										int number = ((RepeatEnding)rl).getNumber();
										int[] numbers = ((RepeatEnding)rl).getNumbers();									
										
										String numberString= "";
										for(int n = 0; n < 2; n++){
											numberString += numbers[n];
										}
										
										if(rl.getStartMeasurePart() == meaPart){
											if(eleMea.elements("barline").isEmpty()){
												Element eleBarline = eleMea.addElement("barline").addAttribute("location", "left");
												Element eleRepatEnding = eleBarline.addElement("ending");
											if(number != 0){
												eleRepatEnding.addAttribute("number", String.valueOf(number));		
											}
											if(numbers[0] != 0 || numbers[1] != 0 || numbers[2] != 0){
												String mNumber = "";
												for(int x = 0; x < 3; x++){
													if(numbers[x] != 0){
														mNumber += String.valueOf(numbers[x] + " ");
													}
												}
												eleRepatEnding.addAttribute("number", mNumber);
											}
												eleRepatEnding.addAttribute("type", "start");
												
											}
										}
//										if(rl.getEndMeasurePart() == meaPart){
//												System.out.println("yes,has end");
//											Element eleBarline = eleMea.addElement("barline").addAttribute("location", "right");
//											Element eleRepatEnding = eleBarline.addElement("ending");
//											eleRepatEnding.addAttribute("number", String.valueOf(number));
//											eleRepatEnding.addAttribute("type", "stop");
//										}
										
									 //	System.out.println(eleMea);
									//	Element eleBarline = eleMea.addElement("barline").addAttribute("location", "left");
									}

							}
							
							/*
							 * 小节组第一个小节, 
							 * 放置在小节组左边的反复记号和左小节线
							 */
							if(m == 0){
								if(!meaPart.getRepeatSymbol().isEmpty()){
									for(int pp = 0; pp < meaPart.getRepeatSymbol().size(); pp++){
										NoteSymbol rs = meaPart.getRepeatSymbol().get(pp);
										String type = rs.getSymbolType();
										Element eleDirection = eleMea.addElement("direction");
										Element eleType = eleDirection.addElement("direction-type");
										if(type.equalsIgnoreCase("coda") || type.equalsIgnoreCase("segno")){
											eleType.addElement(type);
											eleDirection.addElement("sound").addAttribute(type, type);
										}
									}
								}
								if(nxtBarlineForward){
									Element eleBarline = eleMea.addElement("barline").addAttribute("location", "left");
									Element eleStyle = eleBarline.addElement("bar-style");
									eleStyle.setText("heavy-light");
									eleBarline.addElement("repeat").addAttribute("direction", "forward");
									nxtBarlineForward = false;
								}
							}

							if(measure.getAnnotations().size() > 0){
								for(int ai = 0; ai < measure.getAnnotations().size(); ai++){
									Annotation anno = measure.getAnnotations().get(ai);
									Element eleAnno = eleMea.addElement("annotation");
									eleAnno.addAttribute("id", String.valueOf(annNum++));
									eleAnno.addElement("text").setText(anno.getText());
								}
							}

							for(int v = 0, vn = measure.getVoiceNum(); v < vn; v++){
								for(int n = 0, nn = measure.getNoteNum(v); n < nn; n++){
									AbstractNote note = measure.getNote(n, v);
									
									ArrayList<Note> noteList = new ArrayList<Note>(); //........音符放入一个数组中
									if(note instanceof Note){
										Note nnote = (Note)note;
										if(nnote.isRest() && nnote.isHidden()){
											addForWardTag(eleMea, nnote);
										}else{
											noteList.add(nnote);
										}
									}
									else if(note instanceof ChordNote){
										for(int xx = 0; xx < ((ChordNote)note).getNoteNum(); xx++){
											noteList.add(((ChordNote)note).getNote(xx));
										}
									}
									
									for(int xx = 0; xx < noteList.size(); xx++){
										Note nnote = noteList.get(xx);
										
									    	Element eleNotation = null;
										Element eleArticulation = null;
										Element eleOrnament = null;
										
										Element eleNote = eleMea.addElement("note");
										if(xx != 0)
											eleNote.addElement("chord");
										if(nnote.isRest())
											eleNote.addElement("rest");
										else{
											Element elePitch = eleNote.addElement("pitch");
											elePitch.addElement("step").setText(nnote.getRealPitch().getStep());
											elePitch.addElement("octave").setText(String.valueOf(nnote.getRealPitch().getOctave()));
											elePitch.addElement("alter").setText(String.valueOf(nnote.getRealPitch().getAlter()));
										}
										int durr = Math.round(nnote.getRealDuration());
										if(nnote.isRest() && nnote.isFull())
											durr = MusicMath.getMeasureDuration(measure);
										eleNote.addElement("duration").setText(String.valueOf(durr));
										eleNote.addElement("voice").setText(String.valueOf(v+1));
										eleNote.addElement("staff").setText(String.valueOf(m-meaIndex+1));
										if(!nnote.isFull())
											eleNote.addElement("type").setText(MusicMath.noteTypeByDuration(nnote.getDuration()));
										if(nnote.getSharpOrFlat() != null)
											eleNote.addElement("accidental").setText(nnote.getSharpOrFlat().getType());
										if(nnote.getDotNum() != 0)
											for(int d = 0; d < nnote.getDotNum(); d++){
												eleNote.addElement("dot");
											}
										if(note.getTuplet() != null && xx == 0){
											Tuplet tup = note.getTuplet();
										    	Element eleTm = eleNote.addElement("time-modification");
											eleTm.addElement("actual-notes").setText(String.valueOf(tup.getModification()));
											eleTm.addElement("normal-notes").setText(String.valueOf(tup.getNormal()));
											
											if(tup.getNoteList().indexOf(note) == 0 || tup.getNoteList().indexOf(note) == tup.getNoteList().size()-1){
												if(eleNotation == null){
													eleNotation = eleNote.addElement("notations");
												}
												Element eleTup = eleNotation.addElement("tuplet");
												String type = tup.getNoteList().indexOf(note) == 0 ? "start" : "stop";
												eleTup.addAttribute("type", type);
											}
										}
										if(note.getBeam() != null){
											ArrayList<String> beams = MusicMath.getBeamTypes(note);
											for(int b = 0; b < beams.size(); b++){
												Element eleBeam = eleNote.addElement("beam");
												eleBeam.addAttribute("number", String.valueOf(b + 1));
												eleBeam.setText(beams.get(b));
											}
										}
										
										if(nnote.getTieNum() != 0){
											for(int t = 0; t < nnote.getTieNum(); t++){
												Tie tie = nnote.getTie(t);
												Element eleTie = eleNote.addElement("tie");
												if(tie.getStartNote() == nnote){
													eleTie.addAttribute("type", "start");
													if(eleNotation == null)
														eleNotation = eleNote.addElement("notations");
													eleNotation.addElement("tied").addAttribute("type", "start");
												}
												else if(tie.getEndNote() == nnote){
													eleTie.addAttribute("type", "stop");
													if(eleNotation == null)
														eleNotation = eleNote.addElement("notations");
													eleNotation.addElement("tied").addAttribute("type", "stop");
												}
											}
										}

										//音符注释
										if(nnote.getAnnotations().size() > 0){
											for(int ai = 0, an = nnote.getAnnotations().size(); ai < an; ai++){
												Annotation annott = nnote.getAnnotations().get(ai);
												Element eleAnno = eleNote.addElement("annotation");
												eleAnno.addAttribute("id", String.valueOf(annNum++));
												eleAnno.addElement("text").setText(annott.getText());
											}
										}
										
										//歌词
										if(note.getLyricsNum() > 0 && noteList.indexOf(nnote) == 0){
											int num = 1;
											for(int ni = 0; ni < note.getLyricsNum(); ni++){
												Lyrics lyr = note.getLyrics(ni);
												Element eleLyric = eleNote.addElement("lyric");
												eleLyric.addAttribute("number", String.valueOf(num++));
												eleLyric.addElement("text").setText(lyr.getText());
											}		
										}

										//线条符号
										if(!note.getSymbolLines().isEmpty() && xx == 0){
											for(SymbolLine sl : note.getSymbolLines()){
												if(sl instanceof Slur){
													Slur slur = (Slur)sl;
													if(eleNotation == null)
														eleNotation = eleNote.addElement("notations");
													Element eleSlur = eleNotation.addElement("slur");
													if(slur.getStartNote() == note){
														eleSlur.addAttribute("type", "start");
														eleSlur.addAttribute("number", String.valueOf(slurMap.size() + 1));
														slurMap.put(slur, String.valueOf(slurMap.size() + 1));
													}
													else if(slur.getEndNote() == note){
														eleSlur.addAttribute("type", "stop");
														eleSlur.addAttribute("number", slurMap.get(slur));
													}
												}

//												else if(sl instanceof Pedal){
//													int index = eleMea.elements().indexOf(eleNote);
//													Element eleDirect = DocumentHelper.createElement("direction");
//													Element eleType = eleDirect.addElement("direction-type");	
//													if(sl.getStartNote() == nnote){
//														eleType.addElement("pedal").addAttribute("type", "start");
//													}else if(sl.getEndNote() == nnote){
//														eleType.addElement("pedal").addAttribute("type", "stop");
//													}
//													eleDirect.addElement("staff").setText(String.valueOf(m - meaIndex + 1));
//													eleMea.elements().add(index, eleDirect);
//												}
												else if(sl instanceof OctaveUp || sl instanceof OctaveDown){
													int index = eleMea.elements().indexOf(eleNote);
													Element eleDirect = DocumentHelper.createElement("direction");
													Element eleType = eleDirect.addElement("direction-type");
													if(sl.getStartNote() == note){
														String name = sl instanceof OctaveUp ? "up" : "down";
														eleType.addElement("octave-shift").addAttribute("type", name);
													}
													else if(sl.getEndNote() == note){
														eleType.addElement("octave-shift").addAttribute("type", "stop");
													}
													eleDirect.addElement("staff").setText(String.valueOf(m - meaIndex + 1));
													eleMea.elements().add(index, eleDirect);
												}
												else if(sl instanceof Dim || sl instanceof Cre){
													int index = eleMea.elements().indexOf(eleNote);
													Element eleDirect = DocumentHelper.createElement("direction");
													Element eleType = eleDirect.addElement("direction-type");	
													
													if(sl.getStartNote() == note){
														String name = sl instanceof Cre ? "crescendo" : "diminuendo";
														eleType.addElement("wedge").addAttribute("type", name);
													}else if(sl.getEndNote() == note){
														eleType.addElement("wedge").addAttribute("type", "stop");
													}
													eleDirect.addElement("staff").setText(String.valueOf(m - meaIndex + 1));
													eleMea.elements().add(index, eleDirect);
												}
												else if(sl instanceof Vibrato){
													int index = eleMea.elements().indexOf(eleNote);
													Element eleDirect = DocumentHelper.createElement("direction");
													Element eleType = eleDirect.addElement("direction-type");	
													
													if(sl.getStartNote() == note){
														
														eleType.addElement("vibrato").addAttribute("type", "vibrato");
													}else if(sl.getEndNote() == note){
														eleType.addElement("vibrato").addAttribute("type", "stop");
													}
													eleDirect.addElement("staff").setText(String.valueOf(m - meaIndex + 1));
													eleMea.elements().add(index, eleDirect);
												
												}
											}
										}
										//演奏符号
										if(!note.getOrnaments().isEmpty() && xx == 0){
											
											if(eleNotation == null)
												eleNotation = eleNote.addElement("notations");
												if(note.getOrnaments() != null){
													for(int z =0 ; z < note.getOrnamentsNum(); z++){
														if(note.getOrnament(i).getSymbolType().equalsIgnoreCase("fermata")){
															eleNotation.addElement("fermata").addAttribute("type", "upright");
														}
													}
												}
											if(eleArticulation == null)
												eleArticulation = eleNotation.addElement("articulations");
											
											for(NoteSymbol ns : note.getOrnaments()){
												String type = ns.getSymbolType();
												String name = null;
												if(type.equalsIgnoreCase("staccato") || type.equalsIgnoreCase("Tenuto") || 
														type.equalsIgnoreCase("accent")){
													name = type;
													eleArticulation.addElement(name);
												}
												else if(type.equalsIgnoreCase("strongAccentUp") || type.equalsIgnoreCase("strongAccentDown")){
													String dic = type.split("strongAccent")[1].toLowerCase();
													eleArticulation.addElement("strong-accent").addAttribute("type", dic);
												}
												else if(type.equalsIgnoreCase("staccatissimoDown") || type.equalsIgnoreCase("staccatissimoUp")){
													eleArticulation.addElement("staccatissimo");
												}
												else if(type.equalsIgnoreCase("staccatoTenutoUp") || type.equalsIgnoreCase("staccatoTenutoDown")){
													eleArticulation.addElement("staccato");
													eleArticulation.addElement("tenuto");
												}
											}
										}
										
										if(note.getTremoloBeam() != null){
											if(eleNotation == null)
												eleNotation = eleNote.addElement("notations");
											if(eleOrnament == null)
												eleOrnament = eleNotation.addElement("ornaments");
											Element ele = eleOrnament.addElement("tremolo").addAttribute("type", "single");
											String content = note.getTremoloBeam().getSymbolType().split("tremoloBeam")[1];
											ele.setText(content);
										}
												
										//呼吸记号
										if(note.getBreath() != null && xx == 0){
											if(eleNotation == null)
												eleNotation = eleNote.addElement("notations");
											if(eleArticulation == null)
												eleArticulation = eleNotation.addElement("articulations");
											
											eleArticulation.addElement("breath-mark");
										}
										//装饰音
										if(note.getGraceSymbolNum() != 0 && xx == 0){
											
											if(eleNotation == null)
												eleNotation = eleNote.addElement("notations");
											if(eleOrnament == null)
												eleOrnament = eleNotation.addElement("ornaments");
											
											for(int pp = 0; pp < note.getGraceSymbolNum(); pp++){
												NoteSymbol ns = note.getGraceSymbols(pp);
												String type = ns.getSymbolType();
												if(type.equalsIgnoreCase("mordent") || type.equalsIgnoreCase("inverted-mordent") ||
														type.equalsIgnoreCase("turn") || type.equalsIgnoreCase("inverted-turn") ||
														type.equalsIgnoreCase("trill-mark") || type.equalsIgnoreCase("trill-natural") ||
														type.equalsIgnoreCase("trill-flat") || type.equalsIgnoreCase("trill-sharp"))
													{
													eleOrnament.addElement(type);
													}
											}
										}
										
										//倚音
										if(!note.getLeftGraces().isEmpty() && xx == 0){
											int nindex = eleMea.elements().indexOf(eleNote);
											for(int qq = note.getLeftGraces().size()-1; qq >= 0; qq--){
												AbstractNote gnote = note.getLeftGraces().get(qq);
												Grace grace = (Grace)gnote.getHighestNote();
												Element eleGraceNote = DocumentHelper.createElement("note");
												eleMea.elements().add(nindex, eleGraceNote);
												Element eleGrace1 = eleGraceNote.addElement("grace");
												if(grace.isHasSlash())
													eleGrace1.addAttribute("slash", "yes");
												Element eleDur = eleGraceNote.addElement("type");
												eleDur.setText(MusicMath.noteTypeByDuration(gnote.getDuration()));
												Element elePitch = eleGraceNote.addElement("pitch");
												Element eleStep = elePitch.addElement("step");
												eleStep.setText(grace.getRealPitch().getStep());
												Element eleOctave = elePitch.addElement("octave");
												eleOctave.setText(String.valueOf(grace.getRealPitch().getOctave()));
												Element eleAlter = elePitch.addElement("alter");
												eleAlter.setText(String.valueOf(grace.getRealPitch().getAlter()));
												if(gnote instanceof ChordGrace){
													ChordGrace cgrace = (ChordGrace)gnote;
													for(int qqq = 1; qqq <cgrace.getNoteNum(); qqq++){
														Grace sgrace = (Grace)cgrace.getNote(qqq);
														eleGraceNote = DocumentHelper.createElement("note");
														eleMea.elements().add(nindex+qqq, eleGraceNote);
														eleGrace1 = eleGraceNote.addElement("grace");
														if(grace.isHasSlash())
															eleGrace1.addAttribute("slash", "yes");
														eleDur = eleGraceNote.addElement("type");
														eleDur.setText(MusicMath.noteTypeByDuration(gnote.getDuration()));
														eleGraceNote.addElement("chord");
														elePitch = eleGraceNote.addElement("pitch");
														eleStep = elePitch.addElement("step");
														eleStep.setText(grace.getRealPitch().getStep());
														eleOctave = elePitch.addElement("octave");
														eleOctave.setText(String.valueOf(grace.getRealPitch().getOctave()));
														eleAlter = elePitch.addElement("alter");
														eleAlter.setText(String.valueOf(grace.getRealPitch().getAlter()));
													}
												}
											}
										}
										
										//力度记号
										if(note.getDynamicsNum() != 0 && xx == 0){
											int index = eleMea.elements().indexOf(eleNote);
											for(int pp = 0; pp < note.getDynamicsNum(); pp++){
												Element eleDirect = DocumentHelper.createElement("direction");
												Element eleType = eleDirect.addElement("direction-type");
												Element eleDyn = eleType.addElement("dynamics");
												String str = note.getDynamics(pp).getSymbolType();
												eleDyn.addElement(str);
												eleDirect.addElement("staff").setText(String.valueOf(m - meaIndex + 1));
												eleMea.elements().add(index, eleDirect);
											}
										}
										//踏板
										if(note.getPedal() != null){
											int index = eleMea.elements().indexOf(eleNote);
											
												Element eleDirect = DocumentHelper.createElement("direction");
												Element eleType = eleDirect.addElement("direction-type");
												Element eleDyn = eleType.addElement("Pedal");
												String str = note.getPedal().getSymbolType();
												eleDyn.setAttributeValue("type", str);
											//	eleDyn.addAttribute("type", str);
											//	eleDirect.addElement("staff").setText(String.valueOf(m - meaIndex + 1));
												eleMea.elements().add(index, eleDirect);
											
										}
										//表情术语
										if(note.getPerformanceSymbolsNum() != 0 && xx == 0){
											int index = eleMea.elements().indexOf(eleNote);
											for(int ps = 0; ps < note.getPerformanceSymbolsNum(); ps++){
												Element eleDirect = DocumentHelper.createElement("direction");
												Element eleType = eleDirect.addElement("direction-type");
												Element eleDyn = eleType.addElement("words");
												if(note.getPerformanceSymbols(ps).getSymbolType().equalsIgnoreCase("conBrio")){
													eleDyn.setText("con brio");
												}else if(note.getPerformanceSymbols(ps).getSymbolType().equalsIgnoreCase("conSpirito")){
													eleDyn.setText("con spirito");
												}
												else{
													String str = note.getPerformanceSymbols(ps).getSymbolType();
													eleDyn.setText(str);
												}
												
											//	eleDirect.addElement("staff").setText(String.valueOf(m - meaIndex + 1));
												eleMea.elements().add(index, eleDirect);
											}
										}
										
										//速度记号
										if(note.getTempoText() != null && xx == 0){
											TempoText tt = note.getTempoText();
											int index = eleMea.elements().indexOf(eleNote);
											Element eleDirect = DocumentHelper.createElement("direction");
											Element eleType = eleDirect.addElement("direction-type");
											Element eleDyn = eleType.addElement("words");
											eleDyn.setText(tt.getText());
											eleMea.elements().add(index, eleDirect);
										}
									
									}

//									for(int rr=0; rr < meaPart.getRepeatLines().size(); rr++){
//										RepeatLine rl = meaPart.getRepeatLines().get(rr);
//										int number = ((RepeatEnding)rl).getNumber();
//										int[] numbers = ((RepeatEnding)rl).getNumbers();									
//										
//										String numberString= "";
////										for(int n = 0; n < 2; n++){
////											numberString += numbers[n];
////										}
//										System.out.println(numbers[0]);
//										System.out.println(numbers[1]);
//										System.out.println(numbers[2]);
//										System.out.println(number);
//										
////
////										if(rl.getStartMeasurePart() == meaPart){
////											if(eleMea.elements("barline").isEmpty()){
////												Element eleBarline = eleMea.addElement("barline").addAttribute("location", "left");
////												Element eleRepatEnding = eleBarline.addElement("ending");
////												eleRepatEnding.addAttribute("number", String.valueOf(number));
////											
////												eleRepatEnding.addAttribute("type", "start");
////												
////											}
////										}
//										if(rl.getEndMeasurePart() == meaPart){
//												System.out.println("yes,has end");
//											Element eleBarline = eleMea.addElement("barline").addAttribute("location", "right");
//											Element eleRepatEnding = eleBarline.addElement("ending");
//											eleRepatEnding.addAttribute("number", String.valueOf(number));
//											eleRepatEnding.addAttribute("type", "stop");
//										}
//										
//									 //	System.out.println(eleMea);
//									//	Element eleBarline = eleMea.addElement("barline").addAttribute("location", "left");
//
//									
//											
//									
//									}

									if(!meaPart.getRepeatLines().isEmpty()){
							
										for(int rr=0; rr < meaPart.getRepeatLines().size(); rr++){
											RepeatLine rl = meaPart.getRepeatLines().get(rr);
											int number = ((RepeatEnding)rl).getNumber();
											int[] numbers = ((RepeatEnding)rl).getNumbers();									
											if(rl.getEndMeasurePart() == meaPart){
												Element eleBarline = eleMea.addElement("barline").addAttribute("location", "right");
												Element eleRepatEnding = eleBarline.addElement("ending");
												if(number != 0){
													eleRepatEnding.addAttribute("number", String.valueOf(number));
												}
												if(numbers[0] !=0 || numbers[1] != 0 || numbers[2] != 0){
													String mNumber = "";
													for(int x = 0; x < 3; x++){
														if(numbers[x] != 0){
															mNumber += String.valueOf(numbers[x]) + " ";
														}
													}
													eleRepatEnding.addAttribute("number", mNumber);
												}

												eleRepatEnding.addAttribute("type", "stop");
											}
										}
								}
									
									//第一个乐器，最后一个小节，小节最后一个音符. 反复记号和小节线
									if(i == 0 && m == llindex && n == nn - 1){
										if(!meaPart.getRepeatSymbol().isEmpty()){
											for(int pp = 0; pp < meaPart.getRepeatSymbol().size(); pp++){
												NoteSymbol rs = meaPart.getRepeatSymbol().get(pp);
												String type = rs.getSymbolType();
												Element eleDirection = eleMea.addElement("direction");
												Element eleType = eleDirection.addElement("direction-type");
												if(type.equalsIgnoreCase("dc") || type.equalsIgnoreCase("ds") || type.equalsIgnoreCase("fine") ||
														type.equalsIgnoreCase("toCoda") || type.equalsIgnoreCase("dcCoda") || type.equalsIgnoreCase("dsCoda") ||
														type.equalsIgnoreCase("dcFine") || type.equalsIgnoreCase("dsFine")){
													Element eleWord = eleType.addElement("words");
													eleWord.setText(RepeatSymbol.getContentbyType(type));
												}else if(type.equalsIgnoreCase("codaLetter")){
													Element eleWord = eleType.addElement("words");
													eleWord.setText("coda");
												}
											}
										}
										if(!meaPart.getBarline().getType().equalsIgnoreCase("regular")){
											Element eleBarline = eleMea.addElement("barline").addAttribute("location", "right");
											
											Element eleStyle = eleBarline.addElement("bar-style");
											String blType = meaPart.getBarline().getType();
											if(blType.equalsIgnoreCase("backward")){
												eleStyle.setText("light-heavy");
												eleBarline.addElement("repeat").addAttribute("direction", "backward");
											//	nxtBarlineForward = true;
											}
											
											else if(blType.equalsIgnoreCase("light-heavy") || blType.equalsIgnoreCase("light-light")|| blType.equalsIgnoreCase("heavy")){
												eleStyle.setText(blType);
											}
											else if(blType.equalsIgnoreCase("forward")){
												eleStyle.setText("heavy-light");
												eleBarline.addElement("repeat").addAttribute("direction", "forward");
											//	nxtBarlineForward = true;
											}
											else if(blType.equalsIgnoreCase("backward-forward")){
												eleStyle.setText("backward");
												nxtBarlineForward = true;
											}
										}

									}
								}
								if(v < vn - 1){    //不是小节的最后一个声部
									Element eleBackup = eleMea.addElement("backup");
									Element eleDur = eleBackup.addElement("duration");
									eleDur.setText(String.valueOf(MusicMath.getMeasureDuration(measure)));
								}
								
							}
						}
					}
				}
			}
		}
		
		//自由文本
		Element txtList = root.addElement("added-text");
		for(int i = 0, n = score.getAddedTexts().size(); i < n; i++){
			FreeAddedText faText = score.getAddedTexts().get(i);
			Element eleTxt = txtList.addElement("text");
			eleTxt.addElement("content").setText(faText.getText());
			Element eleFont = eleTxt.addElement("font");
			eleFont.addElement("name").setText(faText.getFont().getName());
			eleFont.addElement("style").setText(String.valueOf(faText.getFont().getStyle()));
			eleFont.addElement("size").setText(String.valueOf(faText.getFont().getSize()));
			Page page = (Page)faText.getParent();
			eleTxt.addElement("page-index").setText(String.valueOf(score.getPageList().indexOf(page)));
			eleTxt.addElement("x").setText(String.valueOf(faText.getX()));
			eleTxt.addElement("y").setText(String.valueOf(faText.getY()));
		}
		
		return document;
	}
	
	private void addForWardTag(Element eleMea, Note note){
		Element eleForWard = eleMea.addElement("forward");
		Element eleDur = eleForWard.addElement("duration");
		eleDur.setText(String.valueOf(note.getDurationWithDot()));
	}
	
	/**
	 * 将XML文档写入文件中
	 * @param document XML文档
	 * @param file 文件
	 */
	public void writeDocumentToFile(Document document, File file){
		try{
			FileOutputStream fos = new FileOutputStream(file);
			OutputFormat of = new OutputFormat("  ", true);
			XMLWriter writer = new XMLWriter(fos, of);
			writer.setIndentLevel(2);
			writer.write(document);
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 读取配置信息，保存在一个HashMap中
	 * @return
	 */
	public static HashMap<String, Integer> getConfigs(InputStream inputStream){
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		SAXReader reader = new SAXReader();
		Document document = null;
		try{
			if(inputStream != null)
			document = reader.read(inputStream);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
		Element root = document.getRootElement();
		Iterator rootIt = root.elementIterator();
		while(rootIt.hasNext()){
			Element rootEle = (Element)rootIt.next();
			if(rootEle.getName().equals("button-panel")){
				Iterator panelIt = rootEle.elementIterator();
				while(panelIt.hasNext()){
					Element panelEle = (Element)panelIt.next();
					map.put(panelEle.getName(), Integer.parseInt(panelEle.attributeValue("enabled")));
				}
			}
		}
		
		return map;
	}
	
	public static void main(String args[]){
		String a = "abcdef";
		String b = a.split("abcde")[1];
		System.out.println(b);
	}

}
