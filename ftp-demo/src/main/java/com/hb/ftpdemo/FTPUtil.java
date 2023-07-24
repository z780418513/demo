package com.hb.ftpdemo;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Ftp工具类
 *
 * @author zhaochengshui
 */

public class FTPUtil {

    /**
     * ftpClient对象
     */
    private FTPClient ftpClient;

    /**
     * 主机名或者ip地址
     */
    private  String host;

    /**
     * ftp服务器端口
     */
    private  int port;

    /**
     * ftp用户名
     */
    private  String username;

    /**
     * ftp密码
     */
    private  String password;

    /**
     * 字符集
     */
    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * 超时时间
     */
    private static final int DEFAULT_TIMEOUT = 60 * 1000;

    /**
     * ftp默认端口
     */
    private static final int DEFAULT_PORT = 21;

    /**
     * 初始化时ftp服务器路径
     */
    private volatile String ftpBasePath = "";


    /**
     * 构造函数
     *
     * @param host        主机名或者ip地址
     * @param username    ftp 用户名
     * @param password    ftp 密码
     * @param ftpBasePath 初始化时ftp服务器路径
     */
    private FTPUtil(String host, int port, String username, String password, String ftpBasePath) {
        this(host, port, username, password, DEFAULT_CHARSET, ftpBasePath);
        setTimeout();
    }

    /**
     * 构造函数
     *
     * @param host        主机名或者ip地址
     * @param port        ftp 端口
     * @param username    用户名
     * @param password    密码
     * @param ftpBasePath 初始化时ftp服务器路径
     */
    private FTPUtil(String host, int port, String username, String password, String charset, String ftpBasePath) {
        ftpClient = new FTPClient();
        ftpClient.setControlEncoding(charset);
        this.host = StringUtils.isBlank(host) ? "localhost" : host;
        this.port = (port <= 0) ? DEFAULT_PORT : port;
        this.username = StringUtils.isBlank(username) ? "anonymous" : username;
        this.password = password;
        this.ftpBasePath = ftpBasePath;
    }

    public FTPUtil() {

    }

    /**
     * 创建简单的ftp客户端
     *
     * @param host     主机名或者ip地址
     * @param port     ftp 端口
     * @param username 用户名
     * @param password 密码
     * @return FTPUtil
     */
    public static FTPUtil createSimpleFtpCli(String host, int port, String username, String password) {
        return new FTPUtil(host, port, username, password, DEFAULT_CHARSET, "");
    }

    /**
     * 创建默认的ftp客户端
     *
     * @param host        主机名或者ip地址
     * @param username    ftp用户名
     * @param password    ftp密码
     * @param ftpBasePath 初始化时ftp服务器路径
     * @return FTPUtil
     */
    public static FTPUtil createFtpCli(String host, int port, String username, String password, String ftpBasePath) {
        return new FTPUtil(host, port, username, password, ftpBasePath);
    }

    /**
     * 创建自定义属性的ftp客户端
     *
     * @param host        主机名或者ip地址
     * @param port        ftp端口
     * @param username    ftp用户名
     * @param password    ftp密码
     * @param charset     字符集
     * @param ftpBasePath 初始化时ftp服务器路径
     * @return FTPUtil
     */
    public static FTPUtil createFtpCli(String host, int port, String username, String password, String charset, String ftpBasePath) {
        return new FTPUtil(host, port, username, password, charset, ftpBasePath);
    }

    /**
     * 设置超时时间
     */
    private void setTimeout() {
        ftpClient.setDefaultTimeout(FTPUtil.DEFAULT_TIMEOUT);
        ftpClient.setConnectTimeout(FTPUtil.DEFAULT_TIMEOUT);
        ftpClient.setDataTimeout(FTPUtil.DEFAULT_TIMEOUT);
    }

    /**
     * 连接到ftp
     */
    public void connect() throws IOException {
        try {
            ftpClient.connect(host, port);
        } catch (UnknownHostException e) {
            throw new IOException("Can't find FTP server :" + host);
        }

        int reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            disconnect();
            throw new IOException("Can't connect to server :" + host);
        }

        if (!ftpClient.login(username, password)) {
            disconnect();
            throw new IOException("Can't login to server :" + host);
        }

        // set data transfer mode.
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        // Use passive mode to pass firewalls.
        ftpClient.enterLocalPassiveMode();

        initFtpBasePath();
    }

    /**
     * 连接ftp时保存刚登陆ftp时的路径
     */
    private void initFtpBasePath() throws IOException {
        if (StringUtils.isBlank(ftpBasePath)) {
            synchronized (this) {
                if (StringUtils.isBlank(ftpBasePath)) {
                    ftpBasePath = ftpClient.printWorkingDirectory();
                }
            }
        }
    }

    /**
     * ftp是否处于连接状态，是连接状态返回<tt>true</tt>
     *
     * @return boolean  是连接状态返回<tt>true</tt>
     */
    public boolean isConnected() {
        return ftpClient.isConnected();
    }

    /**
     * 上传文件到对应目录下
     *
     * @param fileName    文件名
     * @param inputStream 文件输入流
     * @param uploadDir   上传文件的父路径
     * @return java.lang.String
     */
    public String uploadFile(String fileName, InputStream inputStream, String uploadDir) throws IOException {
        changeWorkingDirectory(ftpBasePath);
        makeDirs(uploadDir);
        storeFile(fileName, inputStream);
        return uploadDir + "/" + fileName;
    }

    /**
     * 根据uploadFile返回的路径，从ftp下载文件到指定输出流中
     *
     * @param ftpRealFilePath 方法uploadFile返回的路径
     * @param outputStream    输出流
     */
    public void downloadFileFromDailyDir(String ftpRealFilePath, OutputStream outputStream) throws IOException {
        changeWorkingDirectory(ftpBasePath);
        ftpClient.retrieveFile(ftpRealFilePath, outputStream);
    }

    /**
     * 获取ftp上指定文件名到输出流中
     *
     * @param ftpFileName 文件在ftp上的路径  如绝对路径 /home/ftpuser/123.txt 或者相对路径 123.txt
     * @param out         输出流
     */
    public void retrieveFile(String ftpFileName, OutputStream out) throws IOException {
        try {
            FTPFile[] fileInfoArray = ftpClient.listFiles(ftpFileName);
            if (fileInfoArray == null || fileInfoArray.length == 0) {
                throw new FileNotFoundException("File '" + ftpFileName + "' was not found on FTP server.");
            }

            FTPFile fileInfo = fileInfoArray[0];
            if (fileInfo.getSize() > Integer.MAX_VALUE) {
                throw new IOException("File '" + ftpFileName + "' is too large.");
            }

            if (!ftpClient.retrieveFile(ftpFileName, out)) {
                throw new IOException("Error loading file '" + ftpFileName + "' from FTP server. Check FTP permissions and path.");
            }
            out.flush();
        } finally {
            closeStream(out);
        }
    }


    /**
     * 将输入流存储到指定的ftp路径下
     *
     * @param ftpFileName 文件在ftp上的路径 如绝对路径 /home/ftpuser/123.txt 或者相对路径 123.txt
     * @param in          输入流
     */
    public void storeFile(String ftpFileName, InputStream in) throws IOException {
        try {
            if (!ftpClient.storeFile(ftpFileName, in)) {
                throw new IOException("Can't upload file '" + ftpFileName + "' to FTP server. Check FTP permissions and path.");
            }
        } finally {
            closeStream(in);
        }
    }

    /**
     * 根据文件ftp路径名称删除文件
     *
     * @param ftpFileName 文件ftp路径名称
     */
    public void deleteFile(String ftpFileName) throws IOException {
        if (!ftpClient.deleteFile(ftpFileName)) {
            throw new IOException("Can't remove file '" + ftpFileName + "' from FTP server.");
        }
    }

    /**
     * 上传文件到ftp
     *
     * @param ftpFileName 上传到ftp文件路径名称
     * @param localFile   本地文件路径名称
     */
    public void upload(String ftpFileName, File localFile) throws IOException {
        if (!localFile.exists()) {
            throw new IOException("Can't upload '" + localFile.getAbsolutePath() + "'. This file doesn't exist.");
        }

        InputStream in = null;
        try {
            in = new BufferedInputStream(Files.newInputStream(localFile.toPath()));
            if (!ftpClient.storeFile(ftpFileName, in)) {
                throw new IOException("Can't upload file '" + ftpFileName + "' to FTP server. Check FTP permissions and path.");
            }
        } finally {
            closeStream(in);
        }
    }

    /**
     * 上传文件夹到ftp上
     *
     * @param remotePath ftp上文件夹路径名称
     * @param localPath  本地上传的文件夹路径名称
     */
    public void uploadDir(String remotePath, String localPath) throws IOException {
        localPath = localPath.replace("\\\\", "/");
        File file = new File(localPath);
        if (file.exists()) {
            if (!ftpClient.changeWorkingDirectory(remotePath)) {
                ftpClient.makeDirectory(remotePath);
                ftpClient.changeWorkingDirectory(remotePath);
            }
            File[] files = file.listFiles();
            if (null != files) {
                for (File f : files) {
                    if (f.isDirectory() && !f.getName().equals(".") && !f.getName().equals("..")) {
                        uploadDir(remotePath + "/" + f.getName(), f.getPath());
                    } else if (f.isFile()) {
                        upload(remotePath + "/" + f.getName(), f);
                    }
                }
            }
        }
    }

    /**
     * 下载ftp文件到本地上
     *
     * @param ftpFileName ftp文件路径名称
     * @param localFile   本地文件路径名称
     */
    public void download(String ftpFileName, File localFile) throws IOException {
        OutputStream out = null;
        try {
            FTPFile[] fileInfoArray = ftpClient.listFiles(ftpFileName);
            if (fileInfoArray == null || fileInfoArray.length == 0) {
                throw new FileNotFoundException("File " + ftpFileName + " was not found on FTP server.");
            }

            FTPFile fileInfo = fileInfoArray[0];
            if (fileInfo.getSize() > Integer.MAX_VALUE) {
                throw new IOException("File " + ftpFileName + " is too large.");
            }

            out = new BufferedOutputStream(Files.newOutputStream(localFile.toPath()));
            if (!ftpClient.retrieveFile(ftpFileName, out)) {
                throw new IOException("Error loading file " + ftpFileName + " from FTP server. Check FTP permissions and path.");
            }
            out.flush();
        } finally {
            closeStream(out);
        }
    }


    /**
     * 改变工作目录
     *
     * @param dir ftp服务器上目录
     * @return boolean 改变成功返回true
     */
    public boolean changeWorkingDirectory(String dir) {
        if (!ftpClient.isConnected()) {
            return false;
        }
        try {
            return ftpClient.changeWorkingDirectory(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 下载ftp服务器下文件夹到本地
     *
     * @param remotePath ftp上文件夹路径名称
     * @param localPath  本地上传的文件夹路径名称
     */
    public void downloadDir(String remotePath, String localPath) throws IOException {
        localPath = localPath.replace("\\\\", "/");
        File file = new File(localPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        FTPFile[] ftpFiles = ftpClient.listFiles(remotePath);
        for (int i = 0; ftpFiles != null && i < ftpFiles.length; i++) {
            FTPFile ftpFile = ftpFiles[i];
            if (ftpFile.isDirectory() && !".".equals(ftpFile.getName()) && !"..".equals(ftpFile.getName())) {
                downloadDir(remotePath + "/" + ftpFile.getName(), localPath + "/" + ftpFile.getName());
            } else {
                download(remotePath + "/" + ftpFile.getName(), new File(localPath + "/" + ftpFile.getName()));
            }
        }
    }


    /**
     * 列出ftp上文件目录下的文件
     *
     * @param filePath ftp上文件目录
     * @return java.util.List<java.lang.String>
     */
    public List<String> listFileNames(String filePath) throws IOException {
        FTPFile[] ftpFiles = ftpClient.listFiles(filePath);
        List<String> fileList = new ArrayList<>();
        if (ftpFiles != null) {
            for (int i = 0; i < ftpFiles.length; i++) {
                FTPFile ftpFile = ftpFiles[i];
                if (ftpFile.isFile()) {
                    fileList.add(ftpFile.getName());
                }
            }
        }

        return fileList;
    }

    /**
     * 发送ftp命令到ftp服务器中
     *
     * @param args ftp命令
     */
    public void sendSiteCommand(String args) throws IOException {
        if (!ftpClient.isConnected()) {
            ftpClient.sendSiteCommand(args);
        }
    }

    /**
     * 获取当前所处的工作目录
     *
     * @return java.lang.String 当前所处的工作目录
     */
    public String printWorkingDirectory() {
        if (!ftpClient.isConnected()) {
            return "";
        }
        try {
            return ftpClient.printWorkingDirectory();
        } catch (IOException e) {
            // do nothing
        }

        return "";
    }

    /**
     * 切换到当前工作目录的父目录下
     *
     * @return boolean 切换成功返回true
     */
    public boolean changeToParentDirectory() {

        if (!ftpClient.isConnected()) {
            return false;
        }

        try {
            return ftpClient.changeToParentDirectory();
        } catch (IOException e) {
            // do nothing
        }

        return false;
    }

    /**
     * 返回当前工作目录的上一级目录
     *
     * @return java.lang.String 当前工作目录的父目录
     */
    public String printParentDirectory() {
        if (!ftpClient.isConnected()) {
            return "";
        }

        String w = printWorkingDirectory();
        changeToParentDirectory();
        String p = printWorkingDirectory();
        changeWorkingDirectory(w);

        return p;
    }

    /**
     * 创建目录
     *
     * @param pathname 路径名
     * @return boolean 创建成功返回true
     */
    public boolean makeDirectory(String pathname) throws IOException {
        return ftpClient.makeDirectory(pathname);
    }

    /**
     * 创建多个目录
     *
     * @param pathname 路径名
     */
    public void makeDirs(String pathname) throws IOException {
        pathname = pathname.replace("\\\\", "/");
        String[] pathnameArray = pathname.split("/");
        for (String each : pathnameArray) {
            if (StringUtils.isNotEmpty(each)) {
                ftpClient.makeDirectory(each);
                ftpClient.changeWorkingDirectory(each);
            }
        }
    }

    /**
     * 文件是否存在
     *
     * @param pathname 文件路径名
     * @return true = 存在
     * @throws IOException
     */
    public boolean fileIsExist(String pathname) throws IOException {
        FTPFile[] files = ftpClient.listFiles(pathname);
        return files != null && files.length != 0;
    }

    /**
     * 关闭流
     *
     * @param stream 流
     */
    public static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException ex) {
                // do nothing
            }
        }
    }

    /**
     * 关闭ftp连接
     */
    public void disconnect() {
        if (null != ftpClient && ftpClient.isConnected()) {
            try {
                boolean logout = ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException ex) {
                // do nothing
            }
        }
    }

    /**
     * 上传文件 支持断点续传
     *
     * @param localFilePath  本地文件
     * @param remoteFilePath 远程文件
     * @throws Exception
     */
    public void upload(String localFilePath, String remoteFilePath) throws Exception {
        ftpClient.changeWorkingDirectory("/ccc/ddd");
        FTPFile[] files = ftpClient.listFiles(remoteFilePath);
        long beginSize = 0;
        long endSize = 0;
        // 文件已存在
        if (files.length == 1) {
            beginSize = files[0].getSize();
        }
        File file = new File(localFilePath);
        endSize = file.length();
        writeByUnit(remoteFilePath, file, beginSize, endSize);
    }

    /**
     * 包装ftpClient
     *
     * @param ftpClient ftpClient
     * @return FTPUtil
     */
    public static FTPUtil warpFtpClient(FTPClient ftpClient) {
        FTPUtil ftpUtil = new FTPUtil();
        ftpUtil.setFtpClient(ftpClient);
        return ftpUtil;
    }

    /**
     * 上传文件到服务器,新上传和断点续传
     *
     * @param remoteFile 远程文件名，在上传之前已经将服务器工作目录做了改变
     * @param localFile  本地文件File句柄，绝对路径
     * @return
     * @throws IOException
     */

    private void writeByUnit(String remoteFile, File localFile, long beginSize, long endSize) throws Exception {
        long localSize = localFile.length();
        //等待写入的文件大小
        long writeSize = endSize - beginSize;
        if (writeSize <= 0) {
//            throw new CreateException(1, "文件指针参数出错");
        }

        //获取百分单位是 1-100
        try (RandomAccessFile raf = new RandomAccessFile(localFile, "r");
             OutputStream out = ftpClient.appendFileStream(new String(remoteFile.getBytes("GBK"), StandardCharsets.ISO_8859_1));) {
            //把文件指针移动到 开始位置
            ftpClient.setRestartOffset(beginSize);
            raf.seek(beginSize);

            //定义最小移动单位是 1024字节 也就是1kb
            byte[] bytes = new byte[1024 * 10];
            int c;
            double finishSize = 0;
            double finishPercent = 0;
            long timeMillis = System.currentTimeMillis();

            //存在一个bug 当分布移动的时候  可能会出现下载重复的问题 后期需要修改
            while ((c = raf.read(bytes)) != -1) {
                out.write(bytes, 0, c);
                finishSize += c;
                if (finishSize > writeSize) {
                    finishPercent = 1;
                    System.out.println(">>>>>完成进度AAA:" + finishPercent);
                    break;
                }

                if ((finishSize / localSize) - finishPercent > 0.01) {
                    finishPercent = finishSize / writeSize;
                    System.out.println(">>>>>完成进度BBB:" + finishPercent);
                }

            }
            out.flush();
            long cost = System.currentTimeMillis() - timeMillis;
            System.out.println("cost = " + cost);
        }
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}
