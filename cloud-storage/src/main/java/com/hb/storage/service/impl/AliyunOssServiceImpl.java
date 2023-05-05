package com.hb.storage.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.utils.StringUtils;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectRequest;
import com.hb.storage.properties.OssProperties;
import com.hb.storage.service.AliyunOssService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author zhaochengshui
 * @description
 * @date 2022/9/22
 */
public class AliyunOssServiceImpl implements AliyunOssService {
    private static final Logger log = LoggerFactory.getLogger(AliyunOssServiceImpl.class);

    private final String endpoint;
    private final String accessKeyId;
    private final String accessKeySecret;
    private final String DEFAULT_BUCKET_NAME = "hb-admin-oss";
    private final CannedAccessControlList DEFAULT_CANNED_ACCESS = CannedAccessControlList.PublicRead;

    public AliyunOssServiceImpl(OssProperties ossProperties) {
        this.endpoint = ossProperties.getEndpoint();
        this.accessKeyId = ossProperties.getAccessKeyId();
        this.accessKeySecret = ossProperties.getAccessKeySecret();
    }

    /**
     * 创建Bucket
     *
     * @param bucketName Bucket名
     */
    @Override
    public boolean createBucket(String bucketName) {
        boolean insertFlag = true;
        if (StringUtils.isNullOrEmpty(bucketName)) {
            bucketName = DEFAULT_BUCKET_NAME;
        }
        OSS client = getOssClient();
        try {
            // 创建存储空间。
            client.createBucket(bucketName);
            // 默认公共读
            client.setBucketAcl(bucketName, DEFAULT_CANNED_ACCESS);
        } catch (OSSException oe) {
            log.error(oe.getMessage());
            insertFlag = false;
        } catch (ClientException ce) {
            log.error(ce.getMessage());
            insertFlag = false;
        } finally {
            if (client != null) {
                client.shutdown();
            }
        }
        return insertFlag;
    }

    @Override
    public boolean deleteBuket(String bucketName) {
        boolean deleteFlag = true;
        if (StringUtils.isNullOrEmpty(bucketName)) {
            deleteFlag = false;
            return deleteFlag;
        }
        OSS client = getOssClient();
        try {
            // 删除存储空间。
            client.deleteBucket(bucketName);
        } catch (OSSException oe) {
            log.error(oe.getMessage());
            deleteFlag = false;
        } catch (ClientException ce) {
            log.error(ce.getMessage());
            deleteFlag = false;
        } finally {
            if (client != null) {
                client.shutdown();
            }
        }
        return deleteFlag;
    }

    @Override
    public String upLoadFile(String filePath, String bucketName) {
        if (StringUtils.isNullOrEmpty(bucketName)) {
            bucketName = DEFAULT_BUCKET_NAME;
        }
        OSS client = getOssClient();

        String bucketPath = "";
        try {
            File file = new File(filePath);
            bucketPath = getBucketPath(file.getName());
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, bucketPath, file);
            // 上传文件。
            client.putObject(putObjectRequest);

        } catch (OSSException oe) {
            log.error(oe.getMessage());

        } catch (ClientException ce) {
            log.error(ce.getMessage());

        } finally {
            if (client != null) {
                client.shutdown();
            }
        }
        return bucketPath;
    }

    @Override
    public boolean deleteFile(String bucketPath, String bucketName) {
        if (StringUtils.isNullOrEmpty(bucketName)) {
            bucketName = DEFAULT_BUCKET_NAME;
        }
        OSS client = getOssClient();
        boolean deleteFlag = true;
        try {
            // 删除文件或目录。如果要删除目录，目录必须为空。
            client.deleteObject(bucketName, bucketPath);
        } catch (OSSException oe) {
            log.error(oe.getMessage());
            deleteFlag = false;
        } catch (ClientException ce) {
            log.error(ce.getMessage());
            deleteFlag = false;
        } finally {
            if (client != null) {
                client.shutdown();
            }
        }
        return deleteFlag;
    }

    @Override
    public InputStream downFile(String bucketPath, String bucketName) {
        if (StringUtils.isNullOrEmpty(bucketName)) {
            bucketName = DEFAULT_BUCKET_NAME;
        }
        OSS client = getOssClient();
        InputStream inputStream = null;
        try {
            // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
            OSSObject ossObject = client.getObject(bucketName, bucketPath);
            inputStream = ossObject.getObjectContent();
        } catch (OSSException oe) {
            log.error(oe.getMessage());
        } catch (ClientException ce) {
            log.error(ce.getMessage());
        } finally {
            if (client != null) {
                client.shutdown();
            }
        }
        return inputStream;
    }

    @Override
    public String upLoadFile(InputStream inputStream, String fileName, String bucketName) {
        if (StringUtils.isNullOrEmpty(bucketName)) {
            bucketName = DEFAULT_BUCKET_NAME;
        }
        OSS client = getOssClient();

        String downPath = "";
        try {
            String bucketPath = getBucketPath(fileName);
            String endpointPrefix = endpoint.split("//")[0];
            String endpointSuffix = endpoint.split("//")[1];
            // 下载路径
            downPath = endpointPrefix + "//" + bucketName + "." + endpointSuffix + "/" + bucketPath;
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, bucketPath, inputStream);
            // 上传文件。
            client.putObject(putObjectRequest);

        } catch (OSSException oe) {
            log.error(oe.getMessage());

        } catch (ClientException ce) {
            log.error(ce.getMessage());

        } finally {
            if (client != null) {
                client.shutdown();
            }
        }
        return downPath;
    }

    private OSS getOssClient() {
        // 创建OSSClient实例。
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }


    private String getFileType(File file) {
        String fileName = file.getName();
        return fileName.substring(fileName.lastIndexOf("."));
    }

    private String getBucketPath(String fileName) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
        String today = sdf.format(date);
        String uuid = UUID.randomUUID().toString();
        return today + uuid + "-" + fileName;
    }

}
