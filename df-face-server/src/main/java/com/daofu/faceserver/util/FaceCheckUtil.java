package com.daofu.faceserver.util;

import com.alibaba.fastjson.JSON;
import com.baidu.aip.face.AipFace;
import com.daofu.commons.propertie.CompUtils;
import com.daofu.commons.utils.GsonUtils;
import com.daofu.commons.utils.HttpClientUtils;
import com.daofu.faceserver.model.FaceOther;
import com.daofu.faceserver.model.baidu.FaceDetail;
import com.daofu.faceserver.model.baidu.Result;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author li-chuang
 * @date created in 2018/12/26 14:41
 * @description
 */
public class FaceCheckUtil {

    private static Logger logger = LoggerFactory.getLogger(FaceCheckUtil.class);

    /**
     * @description 检测照片是否存在人脸
     * @author lc
     * @date 2018/12/11 14:41
     * @param url
     * @return void
     */
    public static FaceOther getFaceByBaidu(String url){
        HashMap<String, String> map = new HashMap<>();
        map.put("face_field", "age,gender");
        String appId = CompUtils.getBaiduAppId();
        String apiKey = CompUtils.getBaiduAppKey();
        String secretKey = CompUtils.getBaiduSecretKey();
        // 获取人脸检测返回值
        JSONObject jsonObject = new AipFace(appId, apiKey, secretKey).detect(encodeBase64ByPath(url), "BASE64",  map);
        logger.info("人脸检测返回值：" + jsonObject);
        // 返回值为空
        if(jsonObject == null){
            return new FaceOther();
        }
        Result result = JSON.parseObject(jsonObject.toString(), Result.class);
        // 返回值为空或检测失败
        if(result == null || result.getErrorCode() != 0){
            return new FaceOther();
        }

        FaceOther faceOther = new FaceOther();
        FaceDetail faceDetail = result.getResult().getFaceDetail().get(0);
        if("male".equals(faceDetail.getGender().getType())){
            faceOther.setGender(0);
        } else {
            faceOther.setGender(1);
        }

        faceOther.setAge(faceDetail.getAge());
        faceOther.setPitch(faceDetail.getAngle().getPitch());
        faceOther.setYaw(faceDetail.getAngle().getYaw());
        faceOther.setRoll(faceDetail.getAngle().getRoll());
        return faceOther;
    }

    /**
     * 将文件转成base64 字符串
     *
     * @param path
     * @return *
     * @throws Exception
     */
    public static String encodeBase64ByPath(String path) {
        if (path.indexOf("aliyun")>=0) {
            return getAliyunFileStream(path);
        } else {
            return getLocalFileStream(path);
        }
    }

    /**
     * 将文件转成base64 字符串
     *
     * @param path
     * @param type(1:远程路径，其他:本地路径)
     * @return *
     * @throws Exception
     */
    public static String encodeBase64ByPathAndType(String path, int type) {
        if (type == 1) {
            return getAliyunFileStream(path);
        } else {
            return getLocalFileStream(path);
        }
    }

    public static String encodeBase64ByFile(MultipartFile file){
        InputStream inputFile = null;
        byte[] buffer = null;
        String res = "";
        try{
            inputFile = file.getInputStream();
            buffer = file.getBytes();
            inputFile.read(buffer);
            res = Base64Utils.encodeToString(buffer);
        } catch (Exception e){
            logger.error("文件encode失败", e);
        } finally {
            IOUtils.closeQuietly(inputFile);
        }
        return res;
    }

    /**
     * @description 获取本地文件流
     * @author lc
     * @date 2018/12/27 17:09
     * @param path
     * @return java.lang.String
     */
    private static String getLocalFileStream(String path){
        String filepath = CompUtils.getFileRootPath() + path.split("images/")[1];
        FileInputStream inputFile = null;
        byte[] buffer = null;
        String res = "";
        try{
            File file = new File(filepath);
            inputFile = new FileInputStream(file);
            buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            res = Base64Utils.encodeToString(buffer);
        } catch (Exception e){
            logger.error("文件encode失败", e);
        } finally {
            IOUtils.closeQuietly(inputFile);
        }
        return res;
    }

    /**
     * @description 获取阿里云文件流
     * @author lc
     * @date 2018/12/27 17:09
     * @param path
     * @return java.lang.String
     */
    private static String getAliyunFileStream(String path){
        HttpURLConnection urlConn = null;
        InputStream inputStream = null;
        String res = "";
        try {
            URL url = new URL(path);
            //创建url连接;
            urlConn = (HttpURLConnection)url.openConnection();
            //链接远程服务器;
            urlConn.connect();
            inputStream = urlConn.getInputStream();
            byte[] buffer = readInputStream(inputStream);
            res = Base64Utils.encodeToString(buffer);
        } catch (Exception e){
            logger.error("远程文件流获取失败：" +  e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return res;
    }

    private static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        // 创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        // 每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        // 使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            // 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        // 关闭输入流
        inStream.close();
        // 把outStream里的数据写入内存
        return outStream.toByteArray();
    }

}
