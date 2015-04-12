package sjy.elwg.utility;

import java.awt.Font;
import java.io.FileInputStream;
import java.io.InputStream;

public class MusicFont {
	
	public static Font getMScore(){
		Font font = null;
		try{
			InputStream in = Class.forName("sjy.elwg.utility.MusicFont").getResourceAsStream("fonts/mscore-20.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, in);
		}catch(Exception e){
			try {
				InputStream in = new FileInputStream("fonts/mscore-20.ttf");
				font = Font.createFont(Font.TRUETYPE_FONT, in);
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
		return font;
	}
	
	public static Font getMusicSymbol(){
		Font font = null;
		try{
			InputStream in = Class.forName("sjy.elwg.utility.MusicFont").getResourceAsStream("fonts/MUSICAL.TTF");
			font = Font.createFont(Font.TRUETYPE_FONT, in);
		}catch(Exception e){
			try {
				InputStream in = new FileInputStream("fonts/MUSICAL.TTF");
				font = Font.createFont(Font.TRUETYPE_FONT, in);
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
		return font;
	}
	
	public static Font getMScore1(){
		Font font = null;
		try{
			InputStream in = Class.forName("sjy.elwg.utility.MusicFont").getResourceAsStream("fonts/mscore1-20.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, in);
		}catch(Exception e){
			try {
				InputStream in = new FileInputStream("fonts/mscore1-20.ttf");
				font = Font.createFont(Font.TRUETYPE_FONT, in);
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
		return font;
	}
	
	public static Font getFreeSerif(){
		Font font = null;
		try{
			InputStream in = Class.forName("sjy.elwg.utility.MusicFont").getResourceAsStream("fonts/FreeSerifMscore.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, in);
		}catch(Exception e){
			try {
				InputStream in = new FileInputStream("fonts/FreeSerifMscore.ttf");
				font = Font.createFont(Font.TRUETYPE_FONT, in);
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
		return font;
	}
	
	public static Font getGabriola(){
		Font font = null;
		try{
			InputStream in = Class.forName("sjy.elwg.utility.MusicFont").getResourceAsStream("fonts/GABRIOLA.TTF");
			font = Font.createFont(Font.TRUETYPE_FONT, in);
		}catch(Exception e){
			try {
				InputStream in = new FileInputStream("fonts/GABRIOLA.TTF");
				font = Font.createFont(Font.TRUETYPE_FONT, in);
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
		return font;
	}

}
