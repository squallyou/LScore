package sjy.elwg.notation.musicBeans.symbolLines;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import sjy.elwg.notation.musicBeans.MeasurePart;
import sjy.elwg.utility.Controller;
/**
 * 房子记号
 * @author tequila
 *
 */
public class RepeatEnding extends RepeatLine{

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -7896233893665648588L;
	
	/**
	 * 高度
	 */
	public static final int HEIGHT = 30;
	
	/**
	 * 重复记号的标号区域
	 */
	//private JTextField textField;
	
	private int number;
	
	private int[] numbers = new int[3];
	
	
	/**
	 * 构造函数
	 * @param measurePart
	 */
	public RepeatEnding(MeasurePart measurePart,int number){
		super();
		//setSize(100,100);
		startMeasurePart = measurePart;
		endMeasurePart = measurePart;
		this.setStartMeasurePart(measurePart);
		this.number = number;
		if(measurePart != null){
			measurePart.getRepeatLines().add(this);
		}	
		repaint();	
		System.out.println("r1");
	}
	
	public RepeatEnding(MeasurePart measurePart,int[] numbers){
		super();
		//setSize(100,100);
		startMeasurePart = measurePart;
		this.numbers = numbers;
		if(measurePart != null){
			measurePart.getRepeatLines().add(this);
		}	
		repaint();	
		System.out.println("r2");
	}
	
	public RepeatEnding(MeasurePart sMeasurePart, MeasurePart eMeasurePart,int number){
		super();
		startMeasurePart = sMeasurePart;
		endMeasurePart = eMeasurePart;
		this.number = number;
		if(sMeasurePart != null){
			sMeasurePart.getRepeatLines().add(this);
		}
		if(eMeasurePart != null){
			eMeasurePart.getRepeatLines().add(this);
		}
		repaint();
		reShape();
		System.out.println("r3");
	}
	
	public RepeatEnding(MeasurePart sMeasurePart, MeasurePart eMeasurePart,int[] numbers){
		super();
		startMeasurePart = sMeasurePart;
		endMeasurePart = eMeasurePart;
		this.numbers = numbers;
		if(sMeasurePart != null){
			sMeasurePart.getRepeatLines().add(this);
		}
		if(eMeasurePart != null){
			eMeasurePart.getRepeatLines().add(this);
		}

		repaint();
		reShape();
		System.out.println("r3");
	}
	
	@Override
	public void reSize(int x) {
		// TODO Auto-generated method stub
		setSize(x, HEIGHT);
	}
	
	public void paintComponent(Graphics gg){
		
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg;	
		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g.setRenderingHints(renderHints);
        
        if(status == EDIT_STATUS)
        	g.setColor(Color.green);
        else if(status == SELECT_STATUS)
        	g.setColor(Color.blue);
        else 
        	g.setColor(Color.black);
        String str = "";
        //单标号
     
        if(number != 0 ){
        	str = String.valueOf(number);
        	g.drawString(str+".", 7, 25);
        	System.out.println(str);
        }
        //多标号
        else if(numbers[0] != 0 || numbers[1] != 0 || numbers[2] != 0){

        	for(int i = 0; i < 3; i++){
        		if(numbers[i] != 0){
        		//	if(i < 2){
        				str += String.valueOf(numbers[i]) + ". ";
        		//	}else{
        			//	str += String.valueOf(numbers[i]);
        		//	}	
        		}  		
        	}
        //	str = String.valueOf(numbers[0]) + String.valueOf(numbers[1]) + String.valueOf(numbers[2]); 
        //		numbers[0] + numbers[1] + numbers[2];
        //	str = str1 + ". " + str2 + ". " + str3 + ". ";
        	g.drawString(str, 7, 25);
        }
        
      
        int x = this.getWidth();
        g.drawLine(0, 25, 0, 12);     
        g.drawLine(0, 12, x, 12);
        g.drawLine(x-1, 12, x-1, 25);
      


	}
	

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int[] getNumbers() {
		return numbers;
	}

	public void setNumbers(int[] numbers) {
		this.numbers = numbers;
	}

	@Override
	public RepeatLine getRepeatLineInstance() {
		// TODO Auto-generated method stub
		return new RepeatEnding(null ,1);
	}
	
	@Override
	public void reLocate() {
		// TODO Auto-generated method stub
		Controller.locateLine(this);
	}




	
	
}
