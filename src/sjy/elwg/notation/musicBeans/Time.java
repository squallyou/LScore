package sjy.elwg.notation.musicBeans;

import java.util.ArrayList;

/**
 * 拍号实体，包括普通拍号和其他拍号，普通拍号指拍号面板上直接存在的按钮
 * 添加的拍号。其他拍号指通过拍号面板右边的其他拍号面板上输入拍数拍子而
 * 形成的拍号，其他拍号中可以生成混合拍号，如2+2+3/8拍子，也可以生成和普
 * 通拍号中的拍子类型一样，但是谱通拍号中没有的拍号，例如12/16拍。
 * @author sjy
 *
 */
public class Time {
	/**
	 * 普通拍号的拍数
	 */
	private int beats;
	/**
	 * 拍类型
	 */
	private int beatType;
	
	/**
	 * 其它拍号的拍数
	 */
	private int[] beat = new int[4];
	
	/**
	 * 普通拍号的构造函数
	 * @param beats
	 * @param beatType
	 */
	public Time(int beats, int beatType){
		this.beats = beats;
		this.beatType = beatType;
	}
	/**
	 * 其他拍号的构造函数
	 * @param beat
	 * @param beatType
	 */
	public Time(int[] beat, int beatType){
		this.beat = beat;		
		for(int i = 0; i < 4; i++){
			beats += beat[i];
		}
		this.beatType = beatType;
	}
	
	public Time(Time time){
		this.beat = time.getBeat();
		this.beats = time.getBeats();
		this.beatType = time.getBeatType();
	}
	
	
	

	/**
	 * 获得拍数
	 * @return
	 */
	public int getBeats() {
		return beats;
	}

	/**
	 * 设置拍数
	 * @param beats
	 */
	public void setBeats(int beats) {
		this.beats = beats;
	}

	/**
	 * 获得节拍类型
	 * @return
	 */
	public int getBeatType() {
		return beatType;
	}

	/**
	 * 设置节拍类型
	 * @param beatType
	 */
	public void setBeatType(int beatType) {
		this.beatType = beatType;
	}
	
	/**
	 * 重写Object的equals方法,对两个拍号逻辑比较，从而判断是否相等.
	 * 相等条件：beats和beatType均相等.
	 */
	public boolean equals(Object o){
		if(o == this)
			return true;
		if(!(o instanceof Time))
			return false;
		Time time = (Time)o;
		
		//两者均为普通拍号，比较beats和beatType即可
		if(beat[0] == 0 && time.getBeat()[0] == 0){
			return time.getBeats() == beats && time.getBeatType() == beatType;
		}
		//当前拍号为普通拍号，相比较的拍号为其他拍号，如果其他拍号的第一个拍子等于普通拍号的beats，则若其他拍子的第二、三、四拍子都为0，则两者相等
		else if(beat[0] == 0 && time.getBeat()[0] != 0){
			if(time.getBeat()[0] == beats && time.getBeatType() == beatType){
				if(time.getBeat()[1] == 0 && time.getBeat()[2] == 0 && time.getBeat()[3] == 0){
					return true;
				}else{
					return false;
				}
				
			}else{
				return false;
			}
		}
		//当前拍号为其他拍号，相比较的拍号为普通拍号，比较方法同上
		else if(beat[0] != 0 && time.getBeat()[0] == 0){
			if(beat[0] == time.getBeats() && beatType == time.getBeatType()){
				if(beat[1] == 0 && beat[2] == 0 && beat[3] == 0){
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}		
		}
		//两者都为其他拍号，比较每个拍子是否相等
		else if(beat[0] !=0 && time.getBeat()[0] != 0){

			ArrayList<Integer> arrayList1 = new ArrayList<Integer>();
			ArrayList<Integer> arrayList2 = new ArrayList<Integer>();
			for(int i = 0; i < 4; i++){
				if(beat[i] != 0){
					arrayList1.add(beat[i]);
				}
			}
			for(int i = 0; i < 4; i++){
				if(time.getBeat()[i] != 0){
					arrayList2.add(time.getBeat()[i]);
				}
			}
			if(arrayList1.equals(arrayList2)){
				return true;
			}else{
				return false;
			}
			
 		}else{
			return false;
		}	
	}
	
	/**
	 * 由于改写了equals方法，总是要改写散列值方法.
	 */
	public int hashCode(){
		int result = 17;
		result = 37 * result + beats;
		result = 37 * result + beatType;
		return result;
	}

	public int[] getBeat() {
		return beat;
	}

	public void setBeat(int[] beat) {
		this.beat = beat;
	}

}
