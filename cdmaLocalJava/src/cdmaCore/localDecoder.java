package cdmaCore;

public class localDecoder {
	static final int dataLength = 8;
	static final int blockNum = 60;
	int cdmaCodeLength = 4;
	int [][]cdmacodemat = {{1, 1, 1, 1}, {1, 1, -1, -1}, {1, -1, -1, 1}, {1, -1, 1, -1}};
	int[][] ImageMat;
	int blockSize_x;
	int blockSize_y;
	public localDecoder(int[][] InputMat) {
		ImageMat = InputMat;
		blockSize_x = ImageMat.length;
		blockSize_y = ImageMat[0].length;
		// TODO �Զ����ɵĹ��캯�����
	}
	String decode()
	{
		int [][] transferedDataMat = new int[blockSize_x][blockSize_y]
		return null;
	}
}
