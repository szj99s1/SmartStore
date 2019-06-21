package com.daofu.commons.utils;

import com.daofu.commons.constant.CommonConstant;
import com.daofu.commons.exception.MyBussinessException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author li-chuang
 * @date created in 2018/12/11 10:03
 * @description
 */
public class HttpClientUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtils.class);

    private HttpClientUtils(){}

    /**
     * @description get请求
     * @author lc
     * @date 2018/12/14 17:23
     * @param url
     * @param map
     * @param contentType
     * @return java.lang.String
     */
    public static String doGet(String url, Map<String, String> map, String contentType){
        CloseableHttpClient httpClient = null;
        String entityStr = "";
        CloseableHttpResponse response = null;
        try {
            httpClient = HttpClients.createDefault();

            HttpGet httpGet = new HttpGet(new URIBuilder(url).setParameters(transferParam(map)).build());
            // 传输的类型
            httpGet.addHeader("Content-Type", contentType);
            httpGet.setHeader("Connection", "close");
            // 设置超时时间
            httpGet.setConfig(RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build());
            // 执行请求
            response = httpClient.execute(httpGet);
            if(response != null){
                // 获得响应的实体对象
                HttpEntity entity = response.getEntity();
                entityStr = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("get请求失败：", e);
            throw new MyBussinessException(CommonEnum.GET_ERROR);
        }finally {
            IOUtils.closeQuietly(httpClient);
        }
        return entityStr;
    }

    /**
     * @description post请求
     * @author lc
     * @date 2018/12/11 13:04
     * @param url
     * @param param
     * @return java.lang.String
     */
    public static String doPost(String url, String param, String contentType){
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try{
            httpClient = HttpClients.createDefault();
            httpPost = new HttpPost(url);
            // 传输的类型
            httpPost.addHeader("Content-Type", contentType);
            httpPost.setHeader("Connection", "close");
            // 转换参数
            if(contentType.equalsIgnoreCase(CommonConstant.APPLICATION_WWW)){
                List<NameValuePair> list = transferParam(GsonUtils.fromJson(param, HashMap.class));
                if(list.size() > 0){
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,"utf-8");
                    httpPost.setEntity(entity);
                }
            } else {
                StringEntity se = new StringEntity(param,"utf-8");
                httpPost.setEntity(se);
            }
            // 设置超时时间
            httpPost.setConfig(RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build());
            HttpResponse response = httpClient.execute(httpPost);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    result = EntityUtils.toString(resEntity, "utf-8");
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            LOGGER.error("post请求失败：", e);
            throw new MyBussinessException(CommonEnum.POST_ERROR);
        } finally {
            IOUtils.closeQuietly(httpClient);
        }
        return result;
    }

    /**
     * @description 转化map参数为NameValuePair
     * @author lc
     * @date 2018/12/11 13:00
     * @param map
     * @return java.util.List<org.apache.http.NameValuePair>
     */
    private static List<NameValuePair> transferParam(Map<String, String> map){
        //设置参数
        List<NameValuePair> list = new ArrayList<>();
        Iterator iterator = map.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String,String> elem = (Map.Entry<String, String>) iterator.next();
            list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));
        }
        return list;
    }

}
