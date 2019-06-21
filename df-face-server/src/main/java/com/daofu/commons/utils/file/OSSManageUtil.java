package com.daofu.commons.utils.file;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 对OSS服务器进行上传删除等的处理
 *
 * @author liux
 * @ClassName: OSSManageUtil
 * @Description:
 * @date 2017-5-3 上午10:47:00
 */
public class OSSManageUtil {
    private static Logger logger = LoggerFactory.getLogger(OSSManageUtil.class);
    private static OSSManageUtil ossManageUtil = new OSSManageUtil();
    private static OSSClient ossClient;
    private static OSSConfigure ossConfigure;

    public OSSManageUtil() {
        try {
            ossConfigure = new OSSConfigure();
            ossClient = new OSSClient(ossConfigure.getEndpoint(), ossConfigure.getAccessKeyId(), ossConfigure.getAccessKeySecret());
        } catch (Exception e) {
            logger.error("oss配置获取失败", e);
        }
    }

    public static OSSManageUtil getInstance() {
        if (ossManageUtil == null) {
            ossManageUtil = new OSSManageUtil();
        }
        return ossManageUtil;
    }

    /**
     * 上传OSS服务器文件 @Title: uploadFile
     *
     * @throws Exception 设定文件 @return String 返回类型 @throws
     */
    public static String uploadFile(InputStream fileContent, String remotePath, String fileName) throws Exception {
        // 加载配置文件，初始化OSSClient

        try {
            // 定义二级目录
            String remoteFilePath = remotePath.substring(0, remotePath.length()).replaceAll("\\\\", "/") + "/";
            // 创建上传Object的Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(fileContent.available());
            objectMetadata.setContentEncoding("utf-8");
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(contentType(fileName.substring(fileName.lastIndexOf("."))));
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            // 上传文件
            ossClient.putObject(ossConfigure.getBucketName(), remoteFilePath + fileName, fileContent, objectMetadata);

            return ossConfigure.getAccessUrl() + "/" + remoteFilePath + fileName;
        } finally {
            // 关闭io流
            fileContent.close();
        }

    }

    // 下载文件
    @SuppressWarnings("unused")
    public static void downloadFile(OSSConfigure ossConfigure, String key, String filename) throws OSSException, ClientException, IOException {
        // 初始化OSSClient
        OSSClient ossClient = new OSSClient(ossConfigure.getEndpoint(), ossConfigure.getAccessKeyId(), ossConfigure.getAccessKeySecret());
        OSSObject object = ossClient.getObject(ossConfigure.getBucketName(), key);
        // 获取ObjectMeta
        ObjectMetadata meta = object.getObjectMetadata();

        // 获取Object的输入流
        InputStream objectContent = object.getObjectContent();

        ObjectMetadata objectData = ossClient.getObject(new GetObjectRequest(ossConfigure.getBucketName(), key), new File(filename));
        // 关闭数据流
        objectContent.close();
    }

    /**
     * 根据key删除OSS服务器上的文件 @Title: deleteFile @Description: @param @param
     * ossConfigure @param @param filePath 设定文件 @return void 返回类型 @throws
     *
     * @throws IOException
     */
    public static void deleteFile(String filePath) throws IOException {
        // 加载配置文件，初始化OSSClient
/*		OSSConfigure ossConfigure = new OSSConfigure("oss.properties");
		OSSClient ossClient = new OSSClient(ossConfigure.getEndpoint(),
				ossConfigure.getAccessKeyId(),
				ossConfigure.getAccessKeySecret());*/
        filePath = filePath.substring(ossConfigure.getAccessUrl().length() + 1);
        ossClient.deleteObject(ossConfigure.getBucketName(), filePath);

    }

    /**
     * Description: 判断OSS服务文件上传时文件的contentType @Version1.0
     *
     * @param FilenameExtension 文件后缀
     * @return String
     */
    public static String contentType(String FilenameExtension) {
        FilenameExtension = FilenameExtension.toLowerCase();
        if (FilenameExtension.equals(".BMP") || FilenameExtension.equals(".bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equals(".GIF") || FilenameExtension.equals(".gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equals(".JPEG") || FilenameExtension.equals(".jpeg") || FilenameExtension.equals(".JPG") || FilenameExtension.equals(".jpg") || FilenameExtension.equals(".PNG") || FilenameExtension.equals(".png")) {
            return "image/jpeg";
        }
        if (FilenameExtension.equals(".HTML") || FilenameExtension.equals(".html")) {
            return "text/html";
        }
        if (FilenameExtension.equals(".TXT") || FilenameExtension.equals(".txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equals(".VSD") || FilenameExtension.equals(".vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equals(".PPTX") || FilenameExtension.equals(".pptx") || FilenameExtension.equals(".PPT") || FilenameExtension.equals(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equals(".DOCX") || FilenameExtension.equals(".docx") || FilenameExtension.equals(".DOC") || FilenameExtension.equals(".doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equals(".XML") || FilenameExtension.equals(".xml")) {
            return "text/xml";
        }
        if (FilenameExtension.equals(".apk") || FilenameExtension.equals(".APK")) {
            return "application/octet-stream";
        }
        if (FilenameExtension.equals(".mp2") || FilenameExtension.equals(".mp3") || FilenameExtension.equals(".mp4") || FilenameExtension.equals(".mpa") || FilenameExtension.equals(".mpe") || FilenameExtension.equals(".mpg") || FilenameExtension.equals(".mpeg")) {
            return "video/mpeg";
        }
        return "text/html";
    }
}
