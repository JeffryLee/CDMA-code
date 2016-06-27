package cdmaLocalJava;
import java.awt.Image;

import javax.imageio.ImageIO;

import cdmaCore.LocalDecoder;

import com.sun.org.apache.bcel.internal.classfile.Code;

import java.io.*;
public class Main {
	static Image image;
	static String fileName = "code.jpg";
	static String folderName = "../data/test/lzq/";
	static String filePathName;
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
//		String filepathString = "../cdmaMatlab/code.jpg";
		filePathName = folderName.concat(fileName);
		int[][] tmp = DecoderImageReader.readFile(filePathName);
		LocalDecoder localDecoder = new LocalDecoder(tmp,DecoderImageReader.getHeight(),DecoderImageReader.getWidth());
		System.out.println(localDecoder.decode());
	}
	

}
