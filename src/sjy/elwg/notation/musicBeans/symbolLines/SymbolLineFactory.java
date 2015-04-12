package sjy.elwg.notation.musicBeans.symbolLines;


/**
 * 线条符号工厂类
 * @author sjy
 *
 */
public class SymbolLineFactory {
	
	/**
	 * 根据名字创建线条符号对象
	 * @param name 
	 * @return
	 */
	public static SymbolLine generateSymbolLine(int name){
		if(name == AbstractSymbolLine.SLUR)
			return new Slur();
		else if(name == AbstractSymbolLine.CRE)
			return new Cre(null);
		else if(name == AbstractSymbolLine.DIM)
			return new Dim(null);
//		else if(name == AbstractSymbolLine.PEDAL)
//			return new Pedal(null);
		else if(name == AbstractSymbolLine.OCTAVE_DOWN)
			return new OctaveDown(null);
		else if(name == AbstractSymbolLine.OCTAVE_UP)
			return new OctaveUp(null);
		else if(name == AbstractSymbolLine.VIB)
			return new Vibrato(null);
		else if(name == AbstractSymbolLine.DIMC)
			return new Dimc(null);
		else if(name == AbstractSymbolLine.CRESC)
			return new Cresc(null);
		else 
			return null;
	}

}
