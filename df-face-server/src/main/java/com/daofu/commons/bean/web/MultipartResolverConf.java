package com.daofu.commons.bean.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * @author li-chuang
 * @date created in 2018/12/10 13:10
 * @description
 */
@Configuration
public class MultipartResolverConf {

    /**
     * @description 图片上传限制
     * @author lc
     * @date 2018/12/13 11:15
     * @param defaultEncoding
     * @param maxUploadSize
     * @param maxInMemorySize
     * @return org.springframework.web.multipart.commons.CommonsMultipartResolver
     */
    @Bean
    public CommonsMultipartResolver multipartResolver(@Value("${multi.defaultEncoding}") String defaultEncoding,
                                                      @Value("${multi.maxUploadSize}") long maxUploadSize,
                                                      @Value("${multi.maxInMemorySize}") int maxInMemorySize) {
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        commonsMultipartResolver.setDefaultEncoding(defaultEncoding);
        commonsMultipartResolver.setMaxInMemorySize(maxInMemorySize);
        commonsMultipartResolver.setMaxUploadSize(maxUploadSize);
        return commonsMultipartResolver;
    }
}

