package ftp2;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.NioUtil;
import cn.hutool.core.io.StreamProgress;
import cn.hutool.core.text.CharSequenceUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

import java.io.*;
import java.util.List;

/**
 * ftp工具类
 *
 * @author hanbaolaoba
 */
public class FtpUtil {
    private static final Logger logger = LoggerFactory.getLogger(FtpUtil.class);

    /**
     * ftpClient
     */
    private FTPClient ftpClient;

    public FtpUtil(FtpClientFactory ftpClientFactory) {
        this.ftpClient = initClientByFactory(ftpClientFactory);
    }

    private FTPClient initClientByFactory(FtpClientFactory ftpClientFactory) throws ClientCreateException {
        return ftpClientFactory.createClient();
    }

    /**
     * 上传文件
     *
     * @param remotePath     远程路径
     * @param localPath      本地路径
     * @param bufferSize     缓冲区大小
     * @param streamProgress 进度条监视器
     * @return true=上传成功
     * @throws UploadException 上传异常
     */
    public void upload(String remotePath, String localPath, int bufferSize, @Nullable StreamProgress streamProgress) throws UploadException {
        try {
            String fileDirectory = FileUtil.file(remotePath).getParentFile().getPath();
            if (!mkdirFileDirectory(fileDirectory)) {
                logger.warn("FTP Create File Fail, FileDirectory: {}", fileDirectory);
            }

            OutputStream os = ftpClient.storeFileStream(remotePath);

            FileInputStream fis = new FileInputStream(localPath);
            long total = FileUtil.file(localPath).length();

            IoUtil.copy(fis, os, bufferSize, total, streamProgress);
        } catch (Exception e) {
            throw new UploadException("FTP Upload fail, " + e.getMessage());
        }
    }

    public void uploadByNio(String remotePath, String localPath, int bufferSize, @Nullable StreamProgress streamProgress) throws UploadException {
        try {
            String fileDirectory = FileUtil.file(remotePath).getParentFile().getPath();
            if (!mkdirFileDirectory(fileDirectory)) {
                logger.warn("FTP Create File Fail, FileDirectory: {}", fileDirectory);
            }

            OutputStream os = ftpClient.storeFileStream(remotePath);

            FileInputStream fis = new FileInputStream(localPath);
            long total = FileUtil.file(localPath).length();

            NioUtil.copyByNIO(fis, os, bufferSize, total, streamProgress);
        } catch (Exception e) {
            throw new UploadException("FTP Upload fail, " + e.getMessage());
        }
    }

    /**
     * 下载文件
     *
     * @param remotePath     远程路径
     * @param localPath      本地路径
     * @param bufferSize     缓冲区大小
     * @param streamProgress 进度条监视器
     * @return true=上传成功
     * @throws DownloadException 下载异常
     */
    public void download(String remotePath, String localPath, int bufferSize, @Nullable StreamProgress streamProgress) throws DownloadException {
        try {
            File remoteFile = new File(remotePath);
            // check remote file
            long total = checkFileExist(remoteFile.getParent(), remoteFile.getName());
            // mkdir local file dir
            FileUtil.mkdir(new File(localPath).getParent());

            InputStream is = ftpClient.retrieveFileStream(remotePath);
            FileOutputStream fos = new FileOutputStream(localPath);

            IoUtil.copy(is, fos, bufferSize, total, streamProgress);
        } catch (Exception e) {
            throw new DownloadException("FTP Download fail, " + e.getMessage());
        }
    }


    /**
     * 创建目录
     *
     * @param fileDirectory 文件目录
     * @return true=创建成功
     * @throws IOException
     */
    private boolean mkdirFileDirectory(String fileDirectory) throws IOException {
        List<String> dirArray = CharSequenceUtil.split(fileDirectory, "/", true, true);
        if (dirArray != null && !dirArray.isEmpty()) {
            StringBuilder dir = new StringBuilder();
            for (String currentDir : dirArray) {
                dir.append("/").append(currentDir);
                int code = ftpClient.mkd(dir.toString());
                if (code <= 0) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * 检查FTP远程文件是否存在,存在就返回文件长度
     *
     * @param parentDir 远程文件父目录
     * @param fileName  文件名
     * @return 文件长度
     * @throws IOException
     */
    private long checkFileExist(String parentDir, String fileName) throws IOException {
        FTPFile[] fileInfoArray = ftpClient.listFiles(parentDir,
                file -> file != null && file.getName().equals(fileName));
        if (fileInfoArray == null || fileInfoArray.length == 0) {
            throw new FileNotFoundException("File " + fileName + " was not found on FTP server.");
        }

        return fileInfoArray[0].getSize();
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

}
