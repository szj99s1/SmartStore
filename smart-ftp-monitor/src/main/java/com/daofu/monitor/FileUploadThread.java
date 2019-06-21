package com.daofu.monitor;

import com.daofu.util.Configuration;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * @ClassName FileUploadThread
 * @Description TODO
 * @Author shenzj
 * @Date 2019/2/11 16:45
 **/
public class FileUploadThread extends Thread {
    private static Logger logger = Logger.getLogger(FileUploadThread.class);
    private File file;

    public FileUploadThread(File file) {
        this.file = file;
    }

    public void run() {
        String absolutePath = file.getAbsolutePath();
        long length = file.length();
        logger.info("文件上传[" + absolutePath + "]["+length+"]");
        try {
            Thread.sleep(50);
            long newLen = 0;
            for (int j=0;j<100;j++) {
                newLen = file.length();
                if ((newLen - length) > 0||newLen==0) {
                    logger.info("newLen["+newLen+"]");
                    length = newLen;
                    Thread.sleep(50);
                } else {
                   break;
                }
            }

            logger.info("end_length[" + length + "]");
            if (length > Configuration.getInt("file.max.size") || length == 0) {
                return;
            }
            String response = sendToFaceServer(file);
            if (response.indexOf("-1") >= 0) {
                //如果返回-1,最多重复发5次，每次间隔100m
                for (int i = 0; i < 5; i++) {
                    Thread.sleep(100);
                    response = sendToFaceServer(file);
                    if (response.indexOf("-1") >= 0) {
                        continue;
                    } else {
                        break;
                    }
                }
            }

        } catch (Exception e) {
            logger.error("FileUploadThread", e);
        }finally {
            movePic(file);
        }
    }

    private String sendToFaceServer(File file) {
        String responseStr = "";
        CloseableHttpClient httpClient = null;
        try {
            String serverUrl = Configuration.getString("face.server.url");
            HttpPost httpPost = new HttpPost(serverUrl);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            // 上传的文件
            builder.addBinaryBody("files", file);
            String[] filepaths = file.getAbsolutePath().split(File.separator);
            String fold = "";
            for (String filepath : filepaths) {
                if (filepath.length() == 32) {
                    fold = filepath;
                    break;
                }
            }
            //String fold = file.getParentFile().getName();
            builder.addTextBody("device_id", fold, ContentType.TEXT_PLAIN.withCharset("UTF-8"));
            HttpEntity httpEntity = builder.build();
            httpPost.setEntity(httpEntity);
            httpPost.setHeader("Connection", "close");
            httpClient = HttpClients.createDefault();

            HttpResponse response = httpClient.execute(httpPost);
            if (null == response || response.getStatusLine() == null) {
                logger.info("Post Request For Url[" + serverUrl + "] is not ok. Response is null");
            } else if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                logger.info("Post Request For Url[" + serverUrl + "] is not ok. Response Status Code is {" + response.getStatusLine().getStatusCode() + "}");
            }
            responseStr = EntityUtils.toString(response.getEntity());
            logger.info("Response[" + responseStr + "]");
        } catch (Exception e) {
            logger.error("sendToFaceServer", e);
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (Exception e) {
                    logger.error(e);
                }
            }
        }
        return responseStr;
    }


    private void movePic(File file){
        if(file!=null){
            String[] filepaths = file.getAbsolutePath().split(File.separator);
            String fold = "";
            for (String filepath : filepaths) {
                if (filepath.length() == 32) {
                    fold = filepath;
                    break;
                }
            }
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("YYYYMMdd");
            java.util.Date currentTime = new java.util.Date();//得到当前系统时间
            String yyyymmdd = formatter.format(currentTime); //将日期时间格式化
            File filepath = new File(Configuration.getString("pic.backup.path") + fold+"/"+yyyymmdd, file.getName());
            // 判断路径是否存在，如果不存在就创建一个
            if (!filepath.getParentFile().exists()) {
                filepath.getParentFile().mkdirs();
            }
            file.renameTo(filepath);
        }
    }
}
