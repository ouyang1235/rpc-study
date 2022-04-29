package cn.ouyang.test.netty2.demo3.util;

import cn.ouyang.test.netty2.demo3.doamin.Constants;
import cn.ouyang.test.netty2.demo3.doamin.FileBurstData;
import cn.ouyang.test.netty2.demo3.doamin.FileBurstInstruct;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 文件读写工具
 */
public class FileUtil {

    public static FileBurstData readFile(String fileUrl,Integer readPosition) throws IOException {
        File file = new File(fileUrl);
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        randomAccessFile.seek(readPosition);
        byte[] bytes = new byte[1024 * 100];
        int readSize = randomAccessFile.read(bytes);
        if(readSize <= 0){
            randomAccessFile.close();
            return new FileBurstData(Constants.FileStatus.COMPLETE);
        }
        FileBurstData fileInfo = new FileBurstData();
        fileInfo.setFileUrl(fileUrl);
        fileInfo.setFileName(file.getName());
        fileInfo.setBeginPos(readPosition);
        fileInfo.setEndPos(readPosition+readSize);
        //不足100k需要拷贝去掉空字节
        if(readSize < 1024*100){
            byte[] read = new byte[readSize];
            System.arraycopy(bytes,0,read,0,readSize);
            fileInfo.setBytes(read);
            fileInfo.setStatus(Constants.FileStatus.END);
        }else{
            fileInfo.setBytes(bytes);
            fileInfo.setStatus(Constants.FileStatus.CENTER);
        }
        randomAccessFile.close();
        return fileInfo;
    }

    public static FileBurstInstruct writeFile(String baseUrl,FileBurstData fileBurstData) throws IOException{
        if(Constants.FileStatus.COMPLETE == fileBurstData.getStatus()){
            return new FileBurstInstruct(Constants.FileStatus.COMPLETE);
        }

        File file = new File(baseUrl + "/" + fileBurstData.getFileName());
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        randomAccessFile.seek(fileBurstData.getBeginPos());
        randomAccessFile.write(fileBurstData.getBytes());
        randomAccessFile.close();

        if(Constants.FileStatus.END == fileBurstData.getStatus()){
            return new FileBurstInstruct(Constants.FileStatus.COMPLETE);
        }

        FileBurstInstruct fileBurstInstruct = new FileBurstInstruct(Constants.FileStatus.CENTER);
        fileBurstInstruct.setClientFileUrl(fileBurstData.getFileUrl());
        fileBurstInstruct.setReadPosition(fileBurstData.getEndPos()+1);


        return fileBurstInstruct;
    }

}
