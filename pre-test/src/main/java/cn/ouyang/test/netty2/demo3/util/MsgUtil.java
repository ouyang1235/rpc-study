package cn.ouyang.test.netty2.demo3.util;

import cn.ouyang.test.netty2.demo3.doamin.*;

/**
 * 消息构建工具
 */
public class MsgUtil {

    public static FileTransferProtocol buildRequestTransferFile(String fileUrl, String fileName,Long fileSize){
        FileDescInfo fileDescInfo = new FileDescInfo();
        fileDescInfo.setFileName(fileName);
        fileDescInfo.setFileSize(fileSize);
        fileDescInfo.setFileUrl(fileUrl);

        FileTransferProtocol fileTransferProtocol = new FileTransferProtocol();
        fileTransferProtocol.setTransferObj(fileDescInfo);
        fileTransferProtocol.setTransferType(Constants.TransferType.REQUEST);
        return fileTransferProtocol;

    }

    public static FileTransferProtocol buildTransferData(FileBurstData data){
        FileTransferProtocol fileTransferProtocol = new FileTransferProtocol();
        fileTransferProtocol.setTransferObj(data);
        fileTransferProtocol.setTransferType(Constants.TransferType.DATA);
        return fileTransferProtocol;
    }

    public static FileTransferProtocol buildTransferInstruct(FileBurstInstruct fileBurstInstruct){
        FileTransferProtocol fileTransferProtocol = new FileTransferProtocol();
        fileTransferProtocol.setTransferObj(fileBurstInstruct);
        fileTransferProtocol.setTransferType(Constants.TransferType.INSTRUCT);
        return fileTransferProtocol;
    }

    public static FileTransferProtocol buildTransferInstruct(Integer status, String clientFileUrl, Integer readPosition){
        FileBurstInstruct fileBurstInstruct = new FileBurstInstruct();
        fileBurstInstruct.setReadPosition(readPosition);
        fileBurstInstruct.setClientFileUrl(clientFileUrl);
        fileBurstInstruct.setStatus(status);

        return buildTransferInstruct(fileBurstInstruct);
    }

}
