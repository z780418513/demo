package com.hb.storage.service;

import java.io.InputStream;

/**
 * @author zhaochengshui
 * @description 阿里云OSS服务类
 * @date 2022/9/22
 */
public interface AliyunOssService {

    /**
     * 创建Bucket
     *
     * @param bucketName Bucket名
     * @return true = 新增成功
     */
    boolean createBucket(String bucketName);


    /**
     * 删除Bucket
     *
     * @param bucketName Bucket名
     * @return true = 删除成功
     */
    boolean deleteBuket(String bucketName);

    /**
     * 上传文件
     *
     * @param filePath   本地文件地址
     * @param bucketName 指定bucket，传空或""为默认 hb-admin-oss
     * @return 上传路径bucketPath
     */
    @Deprecated
    String upLoadFile(String filePath, String bucketName);


    /**
     * 删除文件
     *
     * @param bucketPath bucket中的地址
     * @param bucketName 指定bucket，传空或""为默认 hb-admin-oss
     * @return true = 删除成功
     */
    boolean deleteFile(String bucketPath, String bucketName);


    /**
     * 下载文件到流
     *
     * @param bucketPath bucket中的地址
     * @param bucketName 指定bucket，传空或""为默认 hb-admin-oss
     * @return InputStream流
     */
    InputStream downFile(String bucketPath, String bucketName);

    /**
     * 上传文件流
     *
     * @param inputStream in
     * @param fileName    文件名
     * @param bucketName  指定bucket，传空或""为默认 hb-admin-oss
     * @return 文件下载地址
     */
    String upLoadFile(InputStream inputStream, String fileName, String bucketName);
}
