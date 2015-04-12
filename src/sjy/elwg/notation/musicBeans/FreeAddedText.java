package sjy.elwg.notation.musicBeans;

/**
 * 自由标注文本
 * 该文本属于用户对乐谱进行的自由文本标注。这些文字在逻辑上不属于任何音乐对象，因此它们“飘零”在乐谱
 * 上面。在保存时记录他们的绝对位置。
 * 
 * 对于某个或某些音乐对象的标注，见Annotation对象。
 * 
 * @author jingyuan.sun
 *
 */
public class FreeAddedText extends FreeTextArea{

	private static final long serialVersionUID = -8178873619209620520L;
	
	private int pageIndex; //所在的页码，从0开始计数

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	
}
