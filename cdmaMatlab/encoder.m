imageSize = 600;
dataLength = 8;
blockNum = 60;
blockSize = imageSize/blockNum;
cdmaCodeLength = 4;
cdmacodemat = [1 1 1 1; 
1 1 0 0;
1 0 0 1;
1 0 1 0];


transferedDataMat = zeros(blockNum);
encodedMat = uint8(zeros(imageSize));
encodedMatColor = uint8(zeros(imageSize,imageSize,3));
dataVectorLenghtMax = blockNum * blockNum / dataLength;
originDataVector = uint8(zeros(1,dataVectorLenghtMax));
originContent = 'haha';
originDataVector = uint8(originContent);
% transferedDataVector = zeros(1,dataLength * length(originDataVector));
transferedDataVector = zeros(1,blockNum*blockNum);

for i=1:length(originDataVector)
    for j=1:dataLength
        transferedDataVector((i-1)*dataLength+j) = bitget(originDataVector(i),dataLength+1-j);
    end
end
transferedDataVector((length(originDataVector)+1)*dataLength+1:blockNum*blockNum) = round(rand(1,blockNum*blockNum-((length(originDataVector)+1)*dataLength)));
transferedDataMat = reshape(transferedDataVector,[blockNum,blockNum])';

%%generate CDMA matrix
cdmaEncodingInput = reshape(transferedDataVector,[blockNum*blockNum/cdmaCodeLength,cdmaCodeLength]);
cdmaEncodingOutput = ones(blockNum*blockNum/4,size(cdmaEncodingInput,2));
for i=1:size(cdmaEncodingInput,1)
    for j=1:size(cdmaEncodingInput,2)
        cdmaEncodingOutput(i,j) = 0;
        for k=1:cdmaCodeLength
            cdmaEncodingOutput(i,j) = cdmaEncodingOutput(i,j) + bitand(1,bitxor(1,bitxor(cdmacodemat(k,j),cdmaEncodingInput(i,k))));
        end
    end
end

transferedDataMat(1:blockNum/2,1:blockNum/2) = reshape(cdmaEncodingOutput(:,1),[blockNum/2,blockNum/2]);
transferedDataMat(1:blockNum/2,blockNum/2+1:blockNum) = reshape(cdmaEncodingOutput(:,2),[blockNum/2,blockNum/2]);
transferedDataMat(blockNum/2+1:blockNum,1:blockNum/2) = reshape(cdmaEncodingOutput(:,3),[blockNum/2,blockNum/2]);
transferedDataMat(blockNum/2+1:blockNum,blockNum/2+1:blockNum) = reshape(cdmaEncodingOutput(:,4),[blockNum/2,blockNum/2]);



for i=1:blockNum
    for j=1:blockNum
        for p=1:blockSize
            for q=1:blockSize
                encodedMat((i-1)*blockSize+p,(j-1)*blockSize+q) = transferedDataMat(i,j)*255/cdmaCodeLength;
            end
        end
    end
end
% uint8 (round(reshape(V_result(1,:),[x1,y1,z1])))
imwrite(encodedMat, 'code.jpg');
