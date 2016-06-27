fileName = 'code.jpg';
folderName = '../data/test/lzq/';
filePathName = strcat(folderName,fileName);

ImageMat = double(imread(filePathName)); 

dataLength = 8;
blockNum = 60;
blockSize_x = size(ImageMat,1)/blockNum;
blockSize_y = size(ImageMat,2)/blockNum;

cdmaCodeLength = 4;
cdmacodemat = [1 1 1 1; 
1 1 0 0;
1 0 0 1;
1 0 1 0];

transferedDataMat = zeros(blockNum);

for i=1:blockNum
    for j=1:blockNum
        for p=1:blockSize_x
            for q=1:blockSize_y
                transferedDataMat(i,j) = transferedDataMat(i,j) + ImageMat((i-1)*blockSize+p,(j-1)*blockSize+q);
            end
        end
    end
end
transferedDataMat = transferedDataMat / (blockSize_x*blockSize_y)*cdmaCodeLength/255;
cdmaDecodingInput = zeros(blockNum*blockNum/4,cdmaCodeLength);

cdmaDecodingInput(:,1) = reshape(transferedDataMat(1:blockNum/2,1:blockNum/2),[blockNum*blockNum/4,1]);
cdmaDecodingInput(:,2) = reshape(transferedDataMat(1:blockNum/2,blockNum/2+1:blockNum),[blockNum*blockNum/4,1]);
cdmaDecodingInput(:,3) = reshape(transferedDataMat(blockNum/2+1:blockNum,1:blockNum/2),[blockNum*blockNum/4,1]);
cdmaDecodingInput(:,4) = reshape(transferedDataMat(blockNum/2+1:blockNum,blockNum/2+1:blockNum),[blockNum*blockNum/4,1]);

cdmaDecodingOutput = zeros(blockNum*blockNum/cdmaCodeLength,cdmaCodeLength);

cdmacodemat = cdmacodemat*2-1;
cdmaDecodingInput = cdmaDecodingInput*2-4;
% for i=1:size(cdmaDecodingOutput,1)
%     for j=1:size(cdmaDecodingOutput,2)
%         for k=1:cdmaCodeLength            
%             cdmaDecodingOutput(i,j) = cdmaDecodingOutput(i,j) + cdmacodemat(k,j)*cdmaDecodingInput(i,k);
%         end 
%     end
% end
cdmaDecodingOutput = cdmaDecodingInput * cdmacodemat;

cdmaDecodingOutput = cdmaDecodingOutput/4;
cdmaDecodingOutput = uint8(round(cdmaDecodingOutput+1)/2);
transferedDataVector = reshape(cdmaDecodingOutput,[1,size(cdmaDecodingOutput,1)*size(cdmaDecodingOutput,2)]);
originDataVector = zeros(1,blockNum*blockNum/dataLength);
for i=1:blockNum*blockNum/dataLength
    for j=1:dataLength
        originDataVector(i) = bitshift(originDataVector(i),1);
        originDataVector(i) = originDataVector(i) + transferedDataVector((i-1)*dataLength+j);
    end
    if originDataVector(i) == 0
        originDataVector = originDataVector(1:i);
        break;
    end
end
char(originDataVector)
