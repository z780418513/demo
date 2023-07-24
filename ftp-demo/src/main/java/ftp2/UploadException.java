package ftp2;

/**
 * @author zhaochengshui
 * @description 上传异常枚举类
 * @date 2023/7/10
 */
public class UploadException extends RuntimeException {

    public UploadException(String message) {
        super(message);
    }
}
