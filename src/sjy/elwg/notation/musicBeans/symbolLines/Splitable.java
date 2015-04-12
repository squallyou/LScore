package sjy.elwg.notation.musicBeans.symbolLines;

import java.util.ArrayList;

/**
 * 可切分接口，
 * 实现了该接口的类具有切分功能，即将一个实体切分为几个相互关联的实体. 
 * 一个典型使用为乐谱中的线条符号,每个线条符号关联多个音符，当这些音符处于不同行的时候，每个符号会被切分为几个片段，分置于不同的行内.
 * @author jingyuan.sun
 *
 */
public interface Splitable {
	
	/**
	 * 进行切分
	 * @param num 切分的数目
	 * @return
	 */
	public ArrayList<AbstractLine> split(int num);

}
