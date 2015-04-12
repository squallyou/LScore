package sjy.elwg.notation;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.swing.JComponent;

import sjy.elwg.notation.musicBeans.Page;
import sjy.elwg.notation.musicBeans.Score;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 进行图片处理
 * @author sjy
 *
 */
@SuppressWarnings("restriction")
public class ImageHandler {
	
	/**
	 * 把一个JPanel保存为由file指定的图片中
	 * @param file
	 * @param panel
	 */
	@SuppressWarnings("restriction")
	public static void saveToPicture(File file, JComponent panel){
		BufferedImage image = new BufferedImage(Page.PAGE_WIDTH, Page.PAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		panel.paint(g);
		image.flush();  
		try {
			FileOutputStream fos = new FileOutputStream(file);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos);
			JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(image);
			param.setQuality(1.0f, true);
			encoder.encode(image, param);
			javax.imageio.ImageIO.write(image, "jpeg", fos);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 将乐谱保存为一系列图片，其中每页为一张图片
	 * @param file 保存的目标地址.如果file是文件，则首个图片保存在file文件里，其他图片保存在同目录下，
	 *             并以该文件名加上阿拉伯数字命名，如file1, file2等. 
	 *             如果file是文件夹，则将图片保存在该目录下.
	 * @param score 乐谱
	 */
	public static void saveScoreToPictures(File file, Score score){
		if(score.getPageList().size() == 0)
			return;
		if(!file.isDirectory()){
			String append = file.getName().split("\\.")[1];
			saveToPicture(file, score.getPageList().get(0));
			
			String path = file.getAbsolutePath();
			int lindex = path.lastIndexOf(".");
			String pre = path.substring(0, lindex);
			
			for(int i = 1, n = score.getPageList().size(); i < n; i++){
				Page page = score.getPageList().get(i);
				File newFile = new File(pre + String.valueOf(i+1) + "." + append);
				saveToPicture(newFile, page);
			}
		}
	}

}
