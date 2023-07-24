package ftp2;

/**
 * @author zhaochengshui
 * @description 下载异常枚举类
 * @date 2023/7/10
 */
public class DownloadException extends RuntimeException {

    public DownloadException(String message) {
        super(message);
    }
}
