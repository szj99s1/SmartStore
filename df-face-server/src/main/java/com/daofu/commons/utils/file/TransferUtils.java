package com.daofu.commons.utils.file;

import com.daofu.commons.exception.MyBussinessException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author li-chuang
 * @date created in 2018/12/10 16:15
 * @description
 */
public final class TransferUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransferUtils.class);

    private TransferUtils(){}

    /**
     * @description 通过path转换base64
     * @author lc
     * @date 2018/12/12 10:56
     * @param path
     * @return java.lang.String
     */
    public static String encodeBase64File(String path) {
        return encodeBase64File(new File(path));
    }

    /**
     * 将文件转成base64 字符串
     *
     * @param file
     * @return *
     * @throws Exception
     */
    public static String encodeBase64File(File file) {
        if(file == null){
            throw new MyBussinessException("文件地址错误");
        }
        FileInputStream inputFile = null;
        byte[] buffer;

        try{
            inputFile = new FileInputStream(file);
            buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
        } catch (FileNotFoundException ex){
            throw new MyBussinessException("文件不存在!");
        } catch (Exception e){
            throw new MyBussinessException("文件转换base64失败!");
        } finally {
            IOUtils.closeQuietly(inputFile);
        }
        return Base64Utils.encodeToString(buffer);
    }

    /**
     * @description 加上头部的base64
     * @author lc
     * @date 2018/12/12 16:32
     * @param path
     * @return java.lang.String
     */
    public static String concatBase64Head(String path){
        File file = new File(path);
        return "data:image/" + FilenameUtils.getExtension(file.getName()) + ";base64," + encodeBase64File(new File(path));
    }

    /**
     * 删除文件
     * @param filePathAndName
     *            String 文件路径及名称 如c:/fqf.txt
     *            String
     * @return boolean
     */
    public static void delFile(String filePathAndName) {
        try {
            new File(filePathAndName).delete();
        } catch (Exception e) {
            System.out.println("删除文件操作出错");
            e.printStackTrace();
            throw new MyBussinessException("删除文件操作出错");
        }
    }
}
