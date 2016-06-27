package cdmaLocalJava;

import java.awt.image.BufferedImage;
import java.io.File;


import javax.imageio.ImageIO;

public class DecoderImageReader {
	private static int height = 0;
	private static int width = 0;
	static int[][] readFile(String filePathName)
	{
		int[][] result;
		try{
            BufferedImage bufImg = ImageIO.read(new File(filePathName));
            height = bufImg.getHeight();
            width = bufImg.getWidth();
            result = new int[height][width];
            for (int i = 0; i < width; i++) {
                 for (int j = 0; j < height; j++) {
                	 //need to change if changing to colorful mode
                	 result[i][j] = bufImg.getRaster().getSample(j, i, 0);  //B
                 }
            }
            return result;
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	static int getHeight()
	{
		return height;
	}
	static int getWidth()
	{
		return width;
	}
}
