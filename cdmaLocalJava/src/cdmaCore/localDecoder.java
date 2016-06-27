package cdmaCore;

import com.sun.istack.internal.FragmentContentHandler;

public class LocalDecoder {
	static final int dataLength = 8;
	static final int blockNum = 60;
	int cdmaCodeLength = 4;
	int [][]cdmacodemat = {{1, 1, 1, 1}, {1, 1, -1, -1}, {1, -1, -1, 1}, {1, -1, 1, -1}};
	int[][] imageMat;
	int imageSize_x;
	int imageSize_y;
	int blockSize_x;
	int blockSize_y;
	public LocalDecoder(int[][] InputMat, int height, int width) {
		imageMat = InputMat;
		imageSize_x = height;
		imageSize_y = width;
		blockSize_x = imageSize_x/blockNum;
		blockSize_y = imageSize_y/blockNum;
		// TODO 自动生成的构造函数存根
	}
	public String decode()
	{
		double [][] transferedDataMat = new double[blockNum][blockNum];
		
		for (int i=0; i<blockNum; i++)
			for (int j=0; j<blockNum; j++)
			{
				transferedDataMat[i][j] = 0;
			}
		
		for (int i=0; i<blockNum; i++)
		{
			for (int j=0; j<blockNum; j++)
			{
				for (int p=0; p<blockSize_x; p++)
				{
					for (int q = 0; q < blockSize_y; q++) 
					{
						transferedDataMat[i][j] += imageMat[i*blockSize_x+p][j*blockSize_y+q];
					}
				}
				transferedDataMat[i][j] /= (blockSize_x*blockSize_y/cdmaCodeLength*255);
			}
		}
		double [][]cdmaDecodingInput = new double[cdmaCodeLength][blockNum*blockNum/cdmaCodeLength];
		matVectorize(transferedDataMat, 0, blockNum/2-1, 0, blockNum/2-1, cdmaDecodingInput[0], true);
		matVectorize(transferedDataMat, 0, blockNum/2-1, blockNum/2, blockNum-1, cdmaDecodingInput[1], true);
		matVectorize(transferedDataMat, blockNum/2, blockNum-1, 0, blockNum/2-1, cdmaDecodingInput[2], true);
		matVectorize(transferedDataMat, blockNum/2, blockNum-1, blockNum/2, blockNum-1, cdmaDecodingInput[3], true);
		
		double [][]cdmaDecodingOutput = new double[cdmaCodeLength][blockNum*blockNum/cdmaCodeLength];
		
		
		for (int i=0; i<cdmaCodeLength; i++)
			for (int j=0; j<blockNum*blockNum/cdmaCodeLength; j++)
			{
				cdmaDecodingInput[i][j] = cdmaDecodingInput[i][j]*2-4;
				cdmaDecodingOutput[i][j] = 0;
			}
		for (int i=0; i<cdmaCodeLength; i++)
			for (int j=0; j<blockNum*blockNum/cdmaCodeLength; j++)
			{
				for (int k=0; k<cdmaCodeLength; k++)
				{
					cdmaDecodingOutput[i][j] += cdmacodemat[i][k] * cdmaDecodingInput[k][j];  
				}
				cdmaDecodingOutput[i][j] /= cdmaCodeLength;
			}
		double []transferedDataVectorDouble = new double[blockNum*blockNum];
		 matVectorize(cdmaDecodingOutput, 0, cdmaCodeLength-1, 0, blockNum*blockNum/cdmaCodeLength-1,transferedDataVectorDouble, false);
		
		int []transferedDataVectorInt = new int[blockNum*blockNum];
		for (int i=0; i<blockNum*blockNum; i++)
		{
			transferedDataVectorInt[i] = Integer.parseInt(new java.text.DecimalFormat("0").format(transferedDataVectorDouble[i]));
			transferedDataVectorInt[i] = (transferedDataVectorInt[i]+1)/2;
		}
		
		char []originDataVector = new char[blockNum*blockNum/dataLength];
		StringBuffer resultSB = new StringBuffer();
		for (int i=0; i<blockNum*blockNum/dataLength; i++)
		{
			originDataVector[i] = 0;
			for (int j=0; j<dataLength; j++)
			{
				originDataVector[i] <<= 1;
				originDataVector[i] += (char)transferedDataVectorInt[i*dataLength+j];
			}
//			System.out.println((int)originDataVector[i]+"");
			if (originDataVector[i] == 0)
				break;
			resultSB.append(originDataVector[i]);
		}
		return resultSB.toString();
	}
	private void matVectorize(double[][] inputmat, int x_low, int x_up, int y_low, int y_up, double[] targetarr, boolean rowFirst) {
		double[] result = new double[(x_up-x_low+1)*(y_up-y_low+1)];
		int resultIndexcnt = 0;
		if (rowFirst){
			for (int j=y_low ; j<=y_up ; j++)
				for (int i=x_low ; i<=x_up ; i++)
				{
					result[resultIndexcnt++] = inputmat[i][j];
				}
		}
		else {
			for (int i=x_low ; i<=x_up ; i++)
				for (int j=y_low ; j<=y_up ; j++)
				{
					result[resultIndexcnt++] = inputmat[i][j];
				}
		}
		dataCopy(targetarr, result, (x_up-x_low+1)*(y_up-y_low+1));
	}
	private void dataCopy(double[] arr1, double[] arr2, int length) {
		for (int i=0; i<length; i++)
		{
			arr1[i] = arr2[i];
		}
	}
}
