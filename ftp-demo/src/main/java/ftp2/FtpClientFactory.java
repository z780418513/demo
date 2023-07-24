package ftp2;


import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.Objects;

/**
 * FtpClient工厂类
 *
 * @author hanbaolaoba
 */
public class FtpClientFactory {

    /**
     * host
     */
    private final String host;
    /**
     * port
     */
    private final int port;
    /**
     * username
     */
    private final String username;
    /**
     * password
     */
    private final String password;
    /**
     * FtpClientProperties
     */
    @Nullable
    private FtpClientProperties properties;

    public FtpClientFactory(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }


    public FTPClient createClient() throws ClientCreateException {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(host, port);

            if (!ftpClient.login(username, password)) {
                throw new ClientCreateException("FTP登陆失败");
            }

            configClient(ftpClient, properties);

            return ftpClient;
        } catch (IOException e) {
            throw new ClientCreateException("FTP连接失败, Msg: " + e.getMessage());
        }
    }

    /**
     * 配置ftpClient
     *
     * @param ftpClient
     * @param properties
     */
    public void configClient(FTPClient ftpClient, @Nullable FtpClientProperties properties) throws IOException {
        if (Objects.nonNull(properties)){
            // TODO
        }
    }

    public void setProperties(FtpClientProperties properties) {
        this.properties = properties;
    }
}
