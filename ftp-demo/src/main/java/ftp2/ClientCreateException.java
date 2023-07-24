package ftp2;

/**
 * @author zhaochengshui
 * @description 客户端创建异常类
 * @date 2023/7/10
 */
public class ClientCreateException extends RuntimeException {

    public ClientCreateException(String message) {
        super(message);
    }
}
