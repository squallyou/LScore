package sjy.elwg.notation.musicBeans.symbolLines;



import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.CubicCurve2D;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;

import sjy.elwg.notation.NoteCanvas;
import sjy.elwg.notation.musicBeans.AbstractNote;
import sjy.elwg.notation.musicBeans.MeasurePart;
import sjy.elwg.notation.musicBeans.Note;
import sjy.elwg.notation.musicBeans.NoteLine;
import sjy.elwg.notation.musicBeans.Page;
import sjy.elwg.utility.Controller;
import sjy.elwg.utility.MusicMath;

/**
 * 连句线
 * @author sjy
 *
 */
public class Slur extends SymbolLine{
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 5892844437040471688L;

	/**
	 * 高度
	 */
	public static final int SLUR_HEIGHT = 35;
	
	/**
	 * 连句号的弧度方向，有效值"up"和"down".
	 * "up"表示弧形是圆形的上面部分，或者放置在音符的上面所呈现的弧度方向。 "down"则相反.
	 */
	protected String upOrDown = "up";
	
	double ratio = 1.4;
	
	Point[] x = new Point[4];

	int i,j,k,flag = 0;
//	int dragX0;
//	int dragY0;
//	int dragX1;
//	int dragY1;
//	int dragX2;
//	int dragY2;
//	int dragX3;
//	int dragY3;
	

	/**
	 * 空构造函数
	 */
	public Slur(){
		super();
	}
	
	/**
	 * 构造函数
	 * 通常用于XML文件的导入时
	 * @param note 起始音符
	 */
    public Slur(AbstractNote note){
    	super();
    	startNote = note;
    	if(note != null)
    		note.getSymbolLines().add(this);
    	//determineUpOrDown();
    }
    
    /**
	 * 构造函数
	 * 通常用于XML文件的导入时
	 * @param note 起始音符
	 * @param direction 弧度方向
	 */
    public Slur(AbstractNote note, String str){
    	super();
    	startNote = note;
    	upOrDown = str;
    	repaint();
    	if(note != null)
    		note.getSymbolLines().add(this);
    	//determineUpOrDown();
    }
    
    /**
     * 根据起始音符与结束音符构造连句线
     * 通常用于用户手动操作生成符号
     * @param startNote
     * @param endNote
     */
    public Slur(AbstractNote startNote, AbstractNote endNote){
    	super();
    	this.startNote = startNote;
    	this.endNote = endNote;
    	startNote.getSymbolLines().add(this);
    	endNote.getSymbolLines().add(this);
    	//determineUpOrDown();
    	reShape();
    }

	@Override
	public SymbolLine getSymbolLineInstance() {
		// TODO Auto-generated method stub
		return new Slur(null, this.upOrDown);
	}
	
	public void paintComponent(Graphics gg){
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg;	
		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        	renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        	g.setRenderingHints(renderHints);
        
		if(startNote != null && endNote != null){
			
			//首尾音符均有符杆
			if(startNote.getStem() != null && endNote.getStem() != null){
				//首音符杆朝上，尾音符符杆朝上
				if(startNote.isStemUp() && endNote.isStemUp()){
					if(startNote.getLowestNote().getY() < endNote.getLowestNote().getY()){
						int y  = Math.abs(endNote.getLowestNote().getY() - startNote.getLowestNote().getY());
						x[0] = new Point(4, 4 );
						if( y < 20){
							x[1] = new Point(getWidth()/3, 20 );
							x[2] = new Point(getWidth()/3*2, 25 ); 
						}else{
							x[1] = new Point(getWidth()/3, 11/10 * y + 2 );
							x[2] = new Point(getWidth()/3*2, 13/10 * y + 2 );  
						}	
						x[3] = new Point(getWidth() - 4, 4 + y );
					}else{
						int y  = Math.abs(endNote.getLowestNote().getY() - startNote.getLowestNote().getY());
						x[0] = new Point(4, 4 + y );
						if( y < 10){
							x[1] = new Point(getWidth()/3, 20 );
							x[2] = new Point(getWidth()/3*2, 15 ); 
						}else{
							x[1] = new Point(getWidth()/3, 13/10 * y + 2 );
							x[2] = new Point(getWidth()/3*2, 11/10 * y + 2 );  
						}	
						x[3] = new Point(getWidth() - 2, 2);
					}
				}
				//首上，尾下
				else if(startNote.isStemUp() && !endNote.isStemUp()){
					//首高尾低
					if(startNote.getStem().getY() < endNote.getHighestNote().getY()){
						int y = Math.abs(endNote.getHighestNote().getY() - startNote.getStem().getY());
						x[0] = new Point(7, getHeight() - y );
						if( y < 20 ){
							x[1] = new Point(getWidth()/3, 10);
							x[2] = new Point(getWidth()/3*2, 15 ); 
						}else{
							x[1] = new Point(getWidth()/3, getHeight() - 12/10 * y );
							x[2] = new Point(getWidth()/3*2, getHeight() - 11/10 * y ); 
						}
						x[3] = new Point(getWidth() - 2, getHeight());
					}
					//首低尾高
					else{
						int y = Math.abs(endNote.getHighestNote().getY() - startNote.getStem().getY());
						x[0] = new Point(7, getHeight());
						if( y < 20 ){
							x[1] = new Point(getWidth()/3, 15);
							x[2] = new Point(getWidth()/3*2, 10 ); 
						}else{
							x[1] = new Point(getWidth()/3, getHeight() - 11/10 * y );
							x[2] = new Point(getWidth()/3*2, getHeight() - 12/10 * y ); 
						}
						x[3] = new Point(getWidth() - 2, getHeight() - y);			
					}
				}
				//首下，尾上
				else if(!startNote.isStemUp() && endNote.isStemUp()){
					//首高尾低
					if(startNote.getHighestNote().getY() < endNote.getStem().getY()){

						int y = Math.abs(startNote.getHighestNote().getY() - endNote.getStem().getY());
						x[0] = new Point(3, getHeight() - y );
						if( y < 20 ){
							x[1] = new Point(getWidth()/3, 10);
							x[2] = new Point(getWidth()/3*2, 15 ); 
						}else{
							x[1] = new Point(getWidth()/3, getHeight() - 12/10 * y );
							x[2] = new Point(getWidth()/3*2, getHeight() - 11/10 * y ); 
						}
						x[3] = new Point(getWidth() - 2, getHeight());
					
					}
					//首低尾高
					else{
						int y = Math.abs(startNote.getHighestNote().getY() - endNote.getStem().getY());
						x[0] = new Point(3, getHeight());
						if( y < 20 ){
							x[1] = new Point(getWidth()/3, 15);
							x[2] = new Point(getWidth()/3*2, 10 ); 
						}else{
							x[1] = new Point(getWidth()/3, getHeight() - 11/10 * y );
							x[2] = new Point(getWidth()/3*2, getHeight() - 12/10 * y ); 
						}
						x[3] = new Point(getWidth() - 2, getHeight() - y);	
					}
				}
				//首下，尾下
				else{
					//首高尾低
					if(startNote.getHighestNote().getY() < endNote.getHighestNote().getY()){
						int y = Math.abs(startNote.getHighestNote().getY() - endNote.getHighestNote().getY());
						x[0] = new Point(3, getHeight()- y);
						if( y < 20 ){
							x[1] = new Point(getWidth()/3, 10);
							x[2] = new Point(getWidth()/3*2, 15 ); 
						}else{
							x[1] = new Point(getWidth()/3, getHeight() - 12/10 * y );
							x[2] = new Point(getWidth()/3*2, getHeight() - 11/10 * y ); 
						}
						x[3] = new Point(getWidth() - 2, getHeight() );					
					}
					//首低尾高
					else{
						int y = Math.abs(startNote.getHighestNote().getY() - endNote.getHighestNote().getY());
						x[0] = new Point(3, getHeight());
						if( y < 20 ){
							x[1] = new Point(getWidth()/3, 15);
							x[2] = new Point(getWidth()/3*2, 10 ); 
						}else{
							x[1] = new Point(getWidth()/3, getHeight() - 11/10 * y );
							x[2] = new Point(getWidth()/3*2, getHeight() - 12/10 * y ); 
						}
						x[3] = new Point(getWidth() - 4, getHeight() - y );			
					}				
				}
			}
			//首音有符杆，尾音符无符杆
			else if(startNote.getStem() != null && endNote.getStem() == null){
				if(startNote.isStemUp()){
					if(startNote.getLowestNote().getY() < endNote.getY()){		
						int y  = Math.abs(startNote.getLowestNote().getY() - endNote.getY());
						x[0] = new Point(4, 4 );
						if( y < 20){
							x[1] = new Point(getWidth()/3, 15 );
							x[2] = new Point(getWidth()/3*2, 20 ); 
						}else{
							x[1] = new Point(getWidth()/3, 11/10 * y + 2 );
							x[2] = new Point(getWidth()/3*2, 13/10 * y + 2 );  
						}	
						x[3] = new Point(getWidth() - 1, 4 + y );	
					}else{

						int y  = Math.abs(startNote.getLowestNote().getY() - endNote.getY());
						x[0] = new Point(4, y );
						if( y < 10){
							x[1] = new Point(getWidth()/3, 15 );
							x[2] = new Point(getWidth()/3*2, 20 ); 
						}else{
							x[1] = new Point(getWidth()/3, 11/10 * y + 2 );
							x[2] = new Point(getWidth()/3*2, 13/10 * y + 2 );  
						}	
						x[3] = new Point(getWidth() - 4, 2);					
					}
				}else{
					if(startNote.getHighestNote().getY() > endNote.getY()){					
						int y = Math.abs(startNote.getHighestNote().getY() - endNote.getHighestNote().getY());
						x[0] = new Point(3, getHeight());
						if( y < 20 ){
						
							x[1] = new Point(getWidth()/3, 15);
							x[2] = new Point(getWidth()/3*2, 10 ); 
						}else{
						
							x[1] = new Point(getWidth()/3, getHeight() - 11/10 * y );
							x[2] = new Point(getWidth()/3*2, getHeight() - 12/10 * y ); 
						}
						x[3] = new Point(getWidth() - 4, getHeight() - y );							
					}
					else{
						int y = Math.abs(startNote.getHighestNote().getY() - endNote.getHighestNote().getY());
						x[0] = new Point(3, getHeight()- y);
						if( y < 20 ){
							x[1] = new Point(getWidth()/3, 10);
							x[2] = new Point(getWidth()/3*2, 15 ); 
						}else{
							x[1] = new Point(getWidth()/3, getHeight() - 12/10 * y );
							x[2] = new Point(getWidth()/3*2, getHeight() - 11/10 * y ); 
						}
						x[3] = new Point(getWidth() - 2, getHeight() );			
					}	
				}
			}
			//首音无符杆，尾音符有符杆
			else if(startNote.getStem() == null && endNote.getStem() != null){
				if(endNote.isStemUp()){
					if(endNote.getLowestNote().getY() < startNote.getY()){		
						int y  = Math.abs(endNote.getLowestNote().getY() - startNote.getY());
						x[0] = new Point(4, 4  + y);
						if( y < 20){
							x[1] = new Point(getWidth()/3, 15 );
							x[2] = new Point(getWidth()/3*2, 20 ); 
						}else{
							x[1] = new Point(getWidth()/3, 13/10 * y + 2 );
							x[2] = new Point(getWidth()/3*2, 10/10 * y + 2 );  
						}	
						x[3] = new Point(getWidth() - 1, 4  );	
					}else{
						int y  = Math.abs(endNote.getLowestNote().getY() - startNote.getY());
						x[0] = new Point(4,  2);
						if( y < 10){
							x[1] = new Point(getWidth()/3, 15 );
							x[2] = new Point(getWidth()/3*2, 20 ); 
						}else{
							x[1] = new Point(getWidth()/3, 13/10 * y + 2 );
							x[2] = new Point(getWidth()/3*2, 11/10 * y + 2 );  
						}	
						x[3] = new Point(getWidth() - 4, y );				
					}
				}else{
					if(endNote.getHighestNote().getY() > startNote.getY()){					
						int y = Math.abs(endNote.getHighestNote().getY() - startNote.getHighestNote().getY());
						x[0] = new Point(3, getHeight() - y);
						if( y < 20 ){
						
							x[1] = new Point(getWidth()/3, 15);
							x[2] = new Point(getWidth()/3*2, 10 ); 
						}else{
						
							x[1] = new Point(getWidth()/3, getHeight() - 12/10 * y );
							x[2] = new Point(getWidth()/3*2, getHeight() - 11/10 * y ); 
						}
						x[3] = new Point(getWidth() - 4, getHeight() );								
					}
					else{
						int y = Math.abs(endNote.getHighestNote().getY() - startNote.getHighestNote().getY());
						x[0] = new Point(3, getHeight());
						if( y < 20 ){
							x[1] = new Point(getWidth()/3, 10);
							x[2] = new Point(getWidth()/3*2, 15 ); 
						}else{
							x[1] = new Point(getWidth()/3, getHeight() - 11/10 * y );
							x[2] = new Point(getWidth()/3*2, getHeight() - 12/10 * y ); 
						}
						x[3] = new Point(getWidth() - 2, getHeight() - y );			
					}	
				}			
			}
			//首尾均无
			else{
				AbstractNote snote = startNote;
				AbstractNote enote = endNote;
				//如果首音符是休止符
				if(snote.isRest()){
					//尾音符是休止符
					if(enote.isRest()){
					int y = Math.abs(endNote.getY() - startNote.getY());
						if(endNote.getY() < startNote.getY()){
							x[0] = new Point(3, getHeight() - y);
							x[1] = new Point(getWidth()/3, 15);
							x[2] = new Point(getWidth()/3*2, 15 ); 
							x[3] = new Point(getWidth() - 4, getHeight() );		
						}else{
							x[0] = new Point(3, getHeight());
							x[1] = new Point(getWidth()/3, 15);
							x[2] = new Point(getWidth()/3*2, 15 ); 
							x[3] = new Point(getWidth() - 4, getHeight() - y );		
						}
					}else{
						if(enote.getHighestNote().getPitch() >= 4){
							int y = Math.abs(endNote.getHighestNote().getY() - startNote.getY());
							x[0] = new Point(3, getHeight());
							if( y < 20 ){
								x[1] = new Point(getWidth()/3, 10);
								x[2] = new Point(getWidth()/3*2, 15 ); 
							}else{
								x[1] = new Point(getWidth()/3, getHeight() - 11/10 * y );
								x[2] = new Point(getWidth()/3*2, getHeight() - 12/10 * y ); 
							}
							x[3] = new Point(getWidth() - 4, getHeight() - y );		
						}else{
							int y = Math.abs(endNote.getLowestNote().getY() - startNote.getY());
							x[0] = new Point(3, 2);
							if( y < 10){
								x[1] = new Point(getWidth()/3, 15 );
								x[2] = new Point(getWidth()/3*2, 20 ); 
							}else{
								x[1] = new Point(getWidth()/3, 13/10 * y + 2 );
								x[2] = new Point(getWidth()/3*2, 11/10 * y + 2 );  
							}	
							x[3] = new Point(getWidth() - 4,  y );		
						}
					}		
				}
				//首音符不是休止符
				else{
					if(snote.getHighestNote().getPitch() >= 4){					
						int y = Math.abs(endNote.getHighestNote().getY() - startNote.getHighestNote().getY());
						if(startNote.getHighestNote().getY() < endNote.getHighestNote().getY()){
							x[0] = new Point(3, getHeight() - y );
							if( y < 20 ){								
								x[1] = new Point(getWidth()/3, 15);
								x[2] = new Point(getWidth()/3*2, 10 ); 
							}else{							
								x[1] = new Point(getWidth()/3, getHeight() - 12/10 * y );
								x[2] = new Point(getWidth()/3*2, getHeight() - 11/10 * y ); 
							}
							x[3] = new Point(getWidth() - 4, getHeight() );	
						}else{
							x[0] = new Point(3, getHeight() );
							if( y < 20 ){								
								x[1] = new Point(getWidth()/3, 10);
								x[2] = new Point(getWidth()/3*2, 15 ); 
							}else{							
								x[1] = new Point(getWidth()/3, getHeight() - 11/10 * y );
								x[2] = new Point(getWidth()/3*2, getHeight() - 12/10 * y ); 
							}
							x[3] = new Point(getWidth() - 4, getHeight() - y );	
						}		
					}
					//spitch < 4
					else{
						int y = Math.abs(endNote.getLowestNote().getY() - startNote.getLowestNote().getY());
						if(startNote.getLowestNote().getY() >= endNote.getLowestNote().getY()){
							x[0] = new Point(3, y);
							if( y < 10){
								x[1] = new Point(getWidth()/3, 20 );
								x[2] = new Point(getWidth()/3*2, 15 ); 
							}else{
								x[1] = new Point(getWidth()/3, 11/10 * y + 2 );
								x[2] = new Point(getWidth()/3*2, 13/10 * y + 2 );  
							}	
							x[3] = new Point(getWidth() - 4, 2 );		
						}else{
							x[0] = new Point(3, 2);
							if( y < 10){
								x[1] = new Point(getWidth()/3, 20 );
								x[2] = new Point(getWidth()/3*2, 15 ); 
							}else{
								x[1] = new Point(getWidth()/3, 13/10 * y + 2 );
								x[2] = new Point(getWidth()/3*2, 11/10 * y + 2 );  
							}	
							x[3] = new Point(getWidth() - 4, y );		
						}		
					}
				}	
			}

		}else if(startNote != null && endNote == null){	
			if(startNote.isStemUp()){
				x[0] = new Point(3, 1 );									
				x[1] = new Point(getWidth()/3, 20);
				x[2] = new Point(getWidth()/3*2, 25 ); 
				x[3] = new Point(getWidth() - 4, 30);	
			}else{
				x[0] = new Point(3, getHeight() );									
				x[1] = new Point(getWidth()/3, 25);
				x[2] = new Point(getWidth()/3*2, 20 ); 
				x[3] = new Point(getWidth() - 4, 15);	
			}
		}else if(startNote == null && endNote != null){
			if(endNote.isStemUp()){
				x[0] = new Point(3, 30 );									
				x[1] = new Point(getWidth()/3, 25);
				x[2] = new Point(getWidth()/3*2, 20 ); 
				x[3] = new Point(getWidth() - 4, 2);	
			}else{
				x[0] = new Point(3, 15 );									
				x[1] = new Point(getWidth()/3, 20);
				x[2] = new Point(getWidth()/3*2, 25 ); 
				x[3] = new Point(getWidth() - 4, getHeight());	
			}
		}else{
			x[0] = new Point(4, getHeight());
			x[1] = new Point(getWidth()/3, 2 / 3 * getHeight());
			x[2] = new Point(getWidth()/3*2,3 / 5 * getHeight());  
			x[3] = new Point(getWidth()-2, getHeight());
		}

      
        if(status == EDIT_STATUS)
        	g.setColor(Color.green);
        else if(status == SELECT_STATUS)
        	g.setColor(Color.blue);
        else 
        	g.setColor(Color.black);
        
//        if (status == EDIT_STATUS){
//        	int[] ax = new int[4];
//			int[] ay = new int[4];
            if(upOrDown.equalsIgnoreCase("up")){
            
    		
            //	CubicCurve2D.Float curveUp = new CubicCurve2D.Float(x[0].x+dragX0, x[0].y+dragY0, x[1].x+dragX1, x[1].y+dragY1, x[2].x+dragX2, x[2].y+dragY2, x[3].x+dragX3, x[3].y+dragY3);
            	CubicCurve2D.Float curveUp = new CubicCurve2D.Float(x[0].x, x[0].y, x[1].x, x[1].y, x[2].x, x[2].y, x[3].x, x[3].y);
            	g.draw(curveUp);
//        		g.drawOval(x[0].x+dragX0, x[0].y+dragY0, 3, 3);
//            	g.drawOval(x[1].x+dragX1, x[1].y+dragY1 , 3, 3);
//            	g.drawOval(x[2].x+dragX0, x[2].y+dragY2, 3, 3);
            //	g.drawOval(x[3].x+dragX0-4, x[2].y+dragY3, 3, 3);
            }
            	
        	else if(upOrDown.equalsIgnoreCase("down")){
            
        	//	CubicCurve2D.Float curveDown = new CubicCurve2D.Float(x[0].x+dragX0, x[0].y+dragY0, x[1].x+dragX1, x[1].y+dragY1, x[2].x+dragX2, x[2].y+dragY2, x[3].x+dragX3, x[3].y+dragY3);
        		CubicCurve2D.Float curveDown = new CubicCurve2D.Float(x[0].x, x[0].y, x[1].x, x[1].y, x[2].x, x[2].y, x[3].x, x[3].y);
        		g.draw(curveDown);
//        		g.drawOval(x[0].x+dragX0, x[0].y+dragY0, 3, 3);
//            	g.drawOval(x[1].x+dragX1, x[1].y+dragY1, 3, 3);
//            	g.drawOval(x[2].x+dragX0, x[2].y+dragY2, 3, 3);
           // 	g.drawOval(x[3].x+dragX0-4, x[3].y+dragY3, 3, 3);
        	}

        
//        }else {
//            if(upOrDown.equalsIgnoreCase("down")){
//                
//            		CubicCurve2D.Float curveDown = new CubicCurve2D.Float(x[0].x+dragX0, x[0].y+dragY0, x[1].x+dragX1, x[1].y+dragY1, x[2].x+dragX2, x[2].y+dragY2, x[3].x+dragX3, x[3].y+dragY3);
//              
//            		g.draw(curveDown);
//                }
//                	
//            	else if(upOrDown.equalsIgnoreCase("up")){
//            		
//              
//            		CubicCurve2D.Float curveUp = new CubicCurve2D.Float(x[0].x+dragX0, x[1].y+dragY0, x[1].x+dragX1, x[0].y+dragY1, x[2].x+dragX2, x[3].y+dragY2, x[3].x+dragX3, x[2].y+dragY3);
//            		g.draw(curveUp);
//            	}
//            }

    		
	}
	 

	public void determineUpOrDown(){
		//若slur有首音符和尾音符
		if(startNote != null && endNote != null){
			//首音符和尾音符均有符杆
			if(startNote.getStem() != null && endNote.getStem() != null){
				//首音符和尾音符至少有一个符号朝下,则slur方向为up
				if(!startNote.isStemUp() || !endNote.isStemUp()){
					upOrDown = "up";
				}
				//首音符尾音符符杆均朝上
				else{
					upOrDown = "down";
				}
			}
			//首音符有符杆，尾音符无符杆
			else if(startNote.getStem() != null  && endNote.getStem() == null){
				//如果首音符的符干朝上
				if(startNote.isStemUp()){
					upOrDown = "down";
				}
				//首音符的符干朝下
				else{
					upOrDown = "up";
				}
			}
			//首音符无符杆，尾音符有符干
			else if(startNote.getStem() == null && endNote.getStem() != null){
				//如果首音符的音高大于等于4,slur方向超上
				if(startNote.getHighestNote().getPitch() >= 4){
					upOrDown = "up";
				}
				//如果首音符的音高小于4,slur方向超下
				else{
					upOrDown = "down";
				}
			}
			//首尾音符均无符干
			else if (startNote instanceof Note && endNote instanceof Note){
				//如果首音符的音高大于等于4,slur方向超上
		
				Note sNote = (Note) startNote;
				Note eNote = (Note) endNote;
				if(sNote.isRest()){
					if(eNote.isRest()){
						upOrDown = "up";
					}else{
						if(eNote.getHighestNote().getPitch() >= 4){
							upOrDown = "up";
						}else{
							upOrDown = "down";
						}
					}
				}else{
					if(sNote.getHighestNote().getPitch() >= 4){
						upOrDown = "up";
					}
					//如果首音符的音高小于4,slur方向超下
					else{
						upOrDown = "down";
					}
				}

			}
			else {
				upOrDown = "down";
			}
			repaint();
			reLocate();
		}
		//有首，无尾，通常为分段slur的第一段
		else if(startNote != null && endNote == null){
			//首音符有符杆
			if(startNote.getStem() != null){
				//符杆向上,slur朝下
				if(startNote.isStemUp()){
					upOrDown = "down";
				}
				//符杆向下，slur朝上
				else{
					upOrDown = "up";
				}
			}
			upOrDown = "up";
			repaint();
			reLocate();
		}
		//无首，无尾,slur的中间一段
		else if(startNote == null && endNote == null){
			AbstractLine preSl = preSymbolLine;
			if(preSl != null){
				upOrDown = ((Slur)preSl).getUpOrDown();
			}
			repaint();
			reLocate();
		}
		//无首，有尾,slur的最后一段
		else{
			AbstractLine preSl = preSymbolLine;
			if(preSl != null){
				upOrDown = ((Slur)preSl).getUpOrDown();
			}
			repaint();
			reLocate();
		}
	}
	
	@Override
	public void reSize(int x) {
		// TODO Auto-generated method stub
		setSize(x, SLUR_HEIGHT);
	}
	
	/**
	 * 放置连句线
	 */
	public void reLocate(){
		Controller.locateLine(this);
	}
	
	public void setSize(int width, int height){
		super.setSize(width, height);
		repaint();
	}

	/**
	 * 获得弧度方向
	 * @return
	 */
	public String getUpOrDown() {
		return upOrDown;
	}

	/**
	 * 设置弧度方向
	 * @param upOrDown
	 */
	public void setUpOrDown(String upOrDown) {
		this.upOrDown = upOrDown;
	}

//	public void mousePressed(MouseEvent e){
////		if(status == EDIT_STATUS){
////		
////		//	Graphics g = getGraphics();
////			int[] ax = new int[4];
////			int[] ay = new int[4];
////			int x0 = e.getX();
////			int y0 = e.getY();
////			Polygon a;
////			//判断是否有点被按下，通过创建四边形，看四边形内是否包含按下去的点
////			for(i=0; i<4; i++){
////				ax[0] = x[i].x-8;
////				ax[1] = x[i].x+8;
////				ax[2] = x[i].x+8;
////				ax[3] = x[i].x-8;
////				
////				ay[0] = x[i].y-8;
////				ay[1] = x[i].y-8;
////				ay[2] = x[i].y+8;
////				ay[3] = x[i].y+8;
////				
////				a = new Polygon(ax,ay,4);
////				if(a.contains(x0, y0)){
////					k = i;
////					flag = 1;
////				}
////			}
////			}else {
////				screenPoint.setLocation((int)e.getXOnScreen(), (int)e.getYOnScreen());
//		screenPoint.setLocation((int)e.getXOnScreen(), (int)e.getYOnScreen());
////			}
//		}
		
//		public void mouseDragged(MouseEvent e){
//			if(status == EDIT_STATUS){
//			Graphics g = getGraphics();
//		
//			if(flag == 1){
//				//System.out.println("dragged");
//				int x = e.getX();
//		    	int y = e.getY();
//		   // 	setSize(300,100);
//		    	if(k == 1){
//		    		dragX1 = x;
//		    		dragY1 = y;
//		    	}else if(k == 2){
//		    		dragX2 = x;
//		    		dragY2 = y;
//		    	}else if(k == 3){		    		
//		    		dragX3 = x;
//		    		dragY3 = y;
//		    	}else if(k == 0){		       		
//		    		dragX0 = x;
//		    		dragY0 = y;
//		    	}
//		 //   	int width = Max(x[0]+dragX0, x[1]+dragX1, x[2], d)
//				repaint();
//			}
//		}
//			else{
//			int x = e.getXOnScreen();
//        	int y = e.getYOnScreen();
//        	
//        	int deltax = x - (int)screenPoint.getX();
//        	int deltay = y - (int)screenPoint.getY();
//        	
//        	screenPoint.setLocation(x, y);
//        	draggedX += deltax;
//        	draggedY += deltay;
//        	
//        	int curX = getX();
//        	int curY = getY();
//        	setLocation(curX + deltax, curY + deltay);  	
//        	System.out.println((curX+deltax)+"  "+ (curY + deltay));
//        	
//		}
//			
//
//	}
		
		public int Max(int a,int b,int c,int d){
			int max;
			int max1 = Math.max(a, b);
			int max2 = Math.max(c, d);
			max = Math.max(max1, max2);
			return max;
		}
		
		public int Min(int a,int b,int c,int d){
			int min;
			int min1 = Math.min(a, b);
			int min2 = Math.min(c, d);
			min = Math.min(min1, min2);
			return min;
		}
		
		/**
		 * 根据起始音符与结束音符，调整大小与位置
		 */
		public void reShape(){
			determineUpOrDown();	
			/*
			 * 起始音符与结束音符均不为空
			 */
			if(startNote != null && endNote != null){
				
				NoteLine startLine = startNote.getMeasure().getMeasurePart().getNoteLine();
				NoteLine endLine = endNote.getMeasure().getMeasurePart().getNoteLine();
				
				//两音符在同一行
				if(startLine == endLine){	
					//int ys = Math.min(startNote.getY(), startNote.getStem().getY());
					//int ye = Math.min(endNote.getY(),endNote.getStem().getY());
					
					if(startNote.getStem() != null && endNote.getStem() != null){
						if(startNote.isStemUp() && !endNote.isStemUp()){
							int height =(int)(Math.abs(endNote.getHighestNote().getY() - startNote.getStem().getY()) * ratio);
							if(height > SLUR_HEIGHT){
								setSize(endNote.getX() - startNote.getX() + 10,height);					
							}else if(height <= SLUR_HEIGHT){
								reSize(endNote.getX() - startNote.getX());
							}
						}
						else if(startNote.isStemUp() && endNote.isStemUp()){
							int height = (int)(Math.abs(startNote.getLowestNote().getY() - endNote.getLowestNote().getY()) * ratio);
							if(height > SLUR_HEIGHT){
								setSize(endNote.getX() - startNote.getX() + 10,height);					
							}else if(height <= SLUR_HEIGHT){
								reSize(endNote.getX() - startNote.getX());
							}
						}
						else if(!startNote.isStemUp() && endNote.isStemUp()){
						
							int height = (int)(Math.abs(startNote.getHighestNote().getY() - endNote.getStem().getY()) * ratio);
							if(height > SLUR_HEIGHT){
								setSize(endNote.getX() - startNote.getX() + 10,height);					
							}else if(height <= SLUR_HEIGHT){
								reSize(endNote.getX() - startNote.getX());
							}
							
						}else{
							int height = (int)(Math.abs(startNote.getHighestNote().getY() - endNote.getHighestNote().getY()) * ratio);
							if(height > SLUR_HEIGHT){
								setSize(endNote.getX() - startNote.getX() + 10,height);					
							}else if(height <= SLUR_HEIGHT){
								reSize(endNote.getX() - startNote.getX());
							}
						}
					}
					
					else if(startNote.getStem() != null && endNote.getStem() == null){
					
						
						if(startNote.isStemUp()){
							int height = (int)(Math.abs(startNote.getLowestNote().getY() - endNote.getY()) * ratio);
							if(height > SLUR_HEIGHT){
								setSize(endNote.getX() - startNote.getX() + 10,height);					
							}else if(height <= SLUR_HEIGHT){
								reSize(endNote.getX() - startNote.getX());
							}
						}
						
						else{
							int height = (int)(Math.abs(startNote.getHighestNote().getY() - endNote.getY()) * ratio);
							if(height > SLUR_HEIGHT){
								setSize(endNote.getX() - startNote.getX() + 10,height);					
							}else if(height <= SLUR_HEIGHT){
								reSize(endNote.getX() - startNote.getX());
							}
						}
					}
					else if(startNote.getStem() == null && endNote.getStem() != null){

					
						
						if(endNote.isStemUp()){
							int height = (int)(Math.abs(endNote.getLowestNote().getY() - startNote.getY()) * ratio);
							if(height > SLUR_HEIGHT){
								setSize(endNote.getX() - startNote.getX() + 10,height);					
							}else if(height <= SLUR_HEIGHT){
								reSize(endNote.getX() - startNote.getX());
							}
						}
						
						else{
							int height = (int)(Math.abs(endNote.getHighestNote().getY() - startNote.getY()) * ratio);
							if(height > SLUR_HEIGHT){
								setSize(endNote.getX() - startNote.getX() + 10,height);					
							}else if(height <= SLUR_HEIGHT){
								reSize(endNote.getX() - startNote.getX());
							}
						}
					
					}
					else{
						int height = (int)(Math.abs(endNote.getY() - startNote.getY()) * ratio);
						if(height > SLUR_HEIGHT){
							setSize(endNote.getX() - startNote.getX() + 10, height);
						}else if(height <= SLUR_HEIGHT){
							reSize(endNote.getX() - startNote.getX());
						}
					}
					
		
					Page page = startLine.getPage();
					if(getParent() != page) 
						page.add(this);
					//如果之前有切分，将后面的部分删除
					SymbolLine nxtSlur = (SymbolLine)nextSymbolLine;
					nextSymbolLine = null;
					while(nxtSlur != null){
						nxtSlur.setPreSymbolLine(null);
						if(nxtSlur.getParent() != null)
							((JComponent)nxtSlur.getParent()).remove(nxtSlur);
						if(nxtSlur.getEndNote() != null){
							nxtSlur.getEndNote().getSymbolLines().remove(nxtSlur);
						} 
						
						nxtSlur = (SymbolLine)nxtSlur.getNextSymbolLine();
					}
					
					reLocate();
				}
				//两音符不在同一行，通常这是刚导入数据的情况，或者正在编辑符号.
				else{
					int lineDiff = MusicMath.NoteLineDiffs(startLine, endLine);
					ArrayList<AbstractLine> pieces = split(lineDiff + 1);
					NoteLine nxtLine = startLine;
					System.out.println("slur pieces size:" + pieces.size());
					for(int i = 0, n = pieces.size(); i < n; i++){
						Slur sl = (Slur)pieces.get(i);
						if(sl.getParent() != nxtLine.getPage())
							nxtLine.getPage().add(sl);
						nxtLine = MusicMath.nxtLine(nxtLine);
						sl.reShape();
					}
				}
			}
			
			/*
			 * 起始音符不为空，结束音符为空，是切分后的第一部分
			 */
			else if(startNote != null && endNote == null){
				
				//与之后的部分在同一行,则进行合并
				SymbolLine nxtTie = nextSymbolLine == null ? null : (SymbolLine)nextSymbolLine;
				NoteLine line = startNote.getMeasure().getMeasurePart().getNoteLine();
				if(nxtTie != null &&  nxtTie.getEndNote() != null){
					NoteLine nxtLine = nxtTie.getEndNote().getMeasure().getMeasurePart().getNoteLine();
					if(nxtLine == line){
						nextSymbolLine = null;
						nxtTie.setPreSymbolLine(null);
						line.getPage().remove(nxtTie);
						endNote = nxtTie.getEndNote();
						endNote.getSymbolLines().remove(nxtTie);
						endNote.getSymbolLines().add(this);
					}
				}
				
				reSize(NoteCanvas.lineWidth + NoteCanvas.xStart - startNote.getX());
				reLocate();
			}
			
			/*
			 * 起始音符为空，结束音符不为空，是切分后的最后一部分 
			 */
			else if(startNote == null && endNote != null){
				NoteLine line = endNote.getMeasure().getMeasurePart().getNoteLine();
				MeasurePart firstPart = line.getMeaPartList().get(0);
				reSize(endNote.getX() + endNote.getWidth()/2 - NoteCanvas.xStart - firstPart.maxAttrWidth());
				//如果之前有切分，将后面的部分删除
				SymbolLine nxtSlur = (SymbolLine)nextSymbolLine;
				nextSymbolLine = null;
				while(nxtSlur != null){
					nxtSlur.setPreSymbolLine(null);
					if(nxtSlur.getParent() != null)
						((JPanel)nxtSlur.getParent()).remove(nxtSlur);
					if(nxtSlur.getEndNote() != null){
						nxtSlur.getEndNote().getSymbolLines().remove(nxtSlur);
					}
					nxtSlur = (SymbolLine)nxtSlur.getNextSymbolLine();
				}
				reLocate();
			}
			
			/*
			 * 起始音符与结束音符均为空，是切分后的中间部分
			 */
			else{
				NoteLine curLine = (NoteLine)MusicMath.getNoteLineBySymbolLine(this).get(0);
				int attrWidth = curLine.getMeaPartList().get(0).maxAttrWidth(); 
				reSize(NoteCanvas.lineWidth - attrWidth);
				reLocate();
			}
		}
}

