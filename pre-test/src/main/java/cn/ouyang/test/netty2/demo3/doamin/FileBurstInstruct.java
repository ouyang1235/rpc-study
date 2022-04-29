package cn.ouyang.test.netty2.demo3.doamin;

/**
 * 文件分片指令
 */
public class FileBurstInstruct {

    private  Integer status;
    private String clientFileUrl;
    private Integer readPosition;    //读取指令

    public Integer getStatus() {
        return status;
    }

    public FileBurstInstruct(Integer status) {
        this.status = status;
    }

    public FileBurstInstruct() {
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getClientFileUrl() {
        return clientFileUrl;
    }

    public void setClientFileUrl(String clientFileUrl) {
        this.clientFileUrl = clientFileUrl;
    }

    public Integer getReadPosition() {
        return readPosition;
    }

    public void setReadPosition(Integer readPosition) {
        this.readPosition = readPosition;
    }
}
