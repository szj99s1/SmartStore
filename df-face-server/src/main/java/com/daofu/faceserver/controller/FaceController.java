package com.daofu.faceserver.controller;

import com.alibaba.fastjson.JSONObject;
import com.daofu.faceserver.base.ImageInfo;
import com.daofu.faceserver.model.*;
import com.daofu.faceserver.service.FaceService;
import com.daofu.faceserver.util.ImageUtil;
import com.daofu.faceserver.util.SystemTimeUtil;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FaceController
 * @Author shenzj
 * @Date 2019/1/22 16:20
 **/
@Controller
public class FaceController {
    private static Logger logger = LoggerFactory.getLogger(FaceController.class);

    @Autowired
    FaceService faceService;

    /**
     *功能描述  人脸检测
     * @param image 人脸图像字节BASE64编码字符串
     * @author shenzj
     * @date 2019/1/22
     */
    @RequestMapping(value = "/detectFaces", method = RequestMethod.POST)
    @ResponseBody
    public List<FaceDetailInfo> detectFaces(@RequestParam("image") String image) {
        logger.info("image["+(image.length() > 100 ? image.substring(0,100) : image)+"]");
        long start = System.currentTimeMillis();
        List<FaceDetailInfo> faceInfoList = null;
        InputStream inputStream = null;
        try {
            byte[] bytes = Base64Utils.decodeFromString(image.trim());
            inputStream = new ByteArrayInputStream(bytes);
            ImageInfo rgbData = ImageUtil.getRGBData(inputStream);
            if(rgbData == null){
                logger.error("图片为空！");
                return null;
            }
            faceInfoList = faceService.detectFaces(rgbData);
            logger.info("faceInfoList["+ JSONObject.toJSONString(faceInfoList));
        } catch (Exception e) {
            logger.error("detectFaces发生异常", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        logger.info("人脸检测耗时：" + (System.currentTimeMillis() - start) + "毫秒");
        return faceInfoList;
    }
    /**
     *功能描述  图片人脸匹配，获取分值
     * @author shenzj
     * @date 2019/1/31
     */
    @RequestMapping(value = "/compareFacesByImages", method = RequestMethod.POST)
    @ResponseBody
    public CompareResult compareFacessByImages(@RequestParam("image0") String image0, @RequestParam("image1") String image1){
        logger.info("image0["+(image0.length() > 100 ? image0.substring(0,100) : image0)+"]image1["
                +(image1.length() > 100 ? image1.substring(0,100) : image1)+"]");
        CompareResult result = null;
        InputStream inputStream0 = null;
        InputStream inputStream1 = null;
       try{
           byte[] bytes0 = Base64Utils.decodeFromString(image0.trim());
           inputStream0 = new ByteArrayInputStream(bytes0);
           ImageInfo imageInfo0 = ImageUtil.getRGBData(inputStream0);
           byte[] bytes1 = Base64Utils.decodeFromString(image1.trim());
           inputStream1 = new ByteArrayInputStream(bytes1);
           ImageInfo imageInfo1 = ImageUtil.getRGBData(inputStream1);
           result = faceService.compareFacesByImages(imageInfo0,imageInfo1);
           logger.info("result:"+ result);
       }catch(Exception e){
           logger.error("compareFacesByImages异常", e);
       }finally {
           IOUtils.closeQuietly(inputStream0);
           IOUtils.closeQuietly(inputStream1);
       }
        return result;
    }

    /**
     *功能描述 通过特征值匹配分值，特征值BASE64编码
     * @author shenzj
     * @date 2019/1/31
     */
    @RequestMapping(value = "/compareFacesByFeatures", method = RequestMethod.POST)
    @ResponseBody
    public CompareResult compareFacesByFeatures(@RequestParam("feature0") String feature0, @RequestParam("feature1") String feature1){
        if(feature0.length() < 100 || feature1.length() < 100){
            logger.error("特征值错误！" + feature0 + ", " + feature1);
            return new CompareResult();
        }
        CompareResult result = null;
        try{
            byte[] bytes0 = Base64Utils.decodeFromString(feature0.trim());
            byte[] bytes1 = Base64Utils.decodeFromString(feature1.trim());

            result = faceService.compareFacesByFeatures(bytes0,bytes1);
            logger.info("result["+ JSONObject.toJSONString(result));
        }catch(Exception e){
            logger.error("compareFacesByFeatures异常", e);
        }
        return result;
    }

    /**
     * @description 功能描述 批量通过特征值匹配分值，特征值BASE64编码
     * @author lc
     * @date 2019-03-13 13:22
     * @param condList
     * @return java.util.List<com.daofu.faceserver.model.CompareResult>
     */
    @RequestMapping(value = "/compareFacesByFeatureList", method = RequestMethod.POST)
    @ResponseBody
    public List<CompareResult> compareFacesByFeatureList(@Valid @RequestBody List<FeatureCompareCond> condList){
        CompareResult result;
        List<CompareResult> compareResultList = new ArrayList<>();
        try{
            for(FeatureCompareCond cond : condList){
                byte[] bytes0 = Base64Utils.decodeFromString(cond.getFeature0().trim());
                byte[] bytes1 = Base64Utils.decodeFromString(cond.getFeature1().trim());

                result = faceService.compareFacesByFeatures(bytes0, bytes1);
                result.setRecId(cond.getRecId());
                compareResultList.add(result);
            }
            logger.info("result:"+ compareResultList);
        }catch(Exception e){
            logger.error("compareFacesByFeatureList异常:", e);
        }
        return compareResultList;
    }

    /**
     *功能描述 获取人像特征值，BASE64编码
     * @param image 人脸图像字节BASE64编码字符串
     * @author shenzj
     * @date 2019/1/22
     */
    @RequestMapping(value = "/getFeatureByImage", method = RequestMethod.POST)
    @ResponseBody
    public FeatureData getFeatureByImage(@RequestParam("image") String image) {
        logger.info("image["+(image.length() > 100 ? image.substring(0,100) : image)+"]");
        long start = System.currentTimeMillis();
        FeatureData featureData = null;
        InputStream inputStream = null;
        try {
            inputStream = new ByteArrayInputStream(Base64Utils.decodeFromString(image.trim()));
            ImageInfo rgbData = ImageUtil.getRGBData(inputStream);
            featureData = faceService.getFeatureByImage(rgbData);
        } catch (Exception e) {
            logger.error("getFeatureByImage发生异常", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        logger.info("获取特征值耗时:" + (System.currentTimeMillis() - start) + "毫秒");
        return featureData;
    }

    /**
     *功能描述 获取人像特征值，BASE64编码
     * @author shenzj
     * @date 2019/1/22
     */
    @RequestMapping(value = "/compareFaceByFeatureList", method = RequestMethod.POST)
    @ResponseBody
    public FaceData compareFaceByFeatureList(@RequestBody FeatureBatchCond cond){
        try{
            byte[] bytes = Base64Utils.decodeFromString(cond.getFeature());
            List<FaceUserInfo> faceUserInfoList = Lists.newArrayList();
            cond.getFeatureList().forEach(x -> faceUserInfoList.add(new FaceUserInfo(x, Base64Utils.decodeFromString(x))));
            return faceService.compareFaceFeature(bytes, faceUserInfoList, cond.getCompareScore());
        } catch(Exception e){
            logger.error("compareFacesByFeatureList异常:", e);
        }
        return null;
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    @ResponseBody
    public String test(){
        String firsttime = SystemTimeUtil.dateToString(new Date());
        for(int i=0;i<5000;i++){
            String feature0="AAD6RAAAdEODl5C95w0mPZGRq7wkjxa8BAYmPWV+Vb2BcLq8rujJvYbYiD1fGEq92UrCvbEZW72XLY29lUWJOteFTzzPlzC9QCyDPT3wAj3lVie9wHu9PZ0IxD2W+489hkcUvdvIW73Aebi9NDlcuslpKT3mi3M9Yw0tPcjwFL1PEk+9EuHau/73WDx/cok9pJ+MvLhNiT118wu9hwI/u8GrF7ySMTC95gRnvNCVxb3v9tE897DgPXUlGbz6+ko95IY+PSBINL1j4/68vlqoPJnAlD17tjI9CiH+OlRHmTy2lQi+Y4MtPngpSj3DddG8vKfhveIa0Lxkjaa9sgWWvTTYUL2PMxU9+VYtvJIQLL1FDTc9QfUxvY8+GbpAfBS8IxVJO7u1PL2+fH698WXUPJHrCjyxnaw9wvUPvF5wpb2jhaQ8lHtBO4tgrr1uwrW8M54kPVPJG72zwvi7X615PdchsD3TvFG9cIhpvRrBcr3xCQW8kdcUvV7QjLycVSi96k6cvb/uLT6TeJG9e4YyPJ17u71ywEO9q3qoPO8uA739ni08bLmovPIRbDxH6Io9WTuCPXfPuTzp8IA9wUPuvErHubyQupu8whUZvcxywLykKEy9aWy/OMg9xT0vYUu9tikMPrqLGD0pyi+9lcvKvTYgTL2tjmq99VMvO86hO73BkHc7UgAuvXkKZbzYJYE9fSMOPR/7gjwpAE69Wu/duuWDuzsJJTa9aT3IPJnl4zwxtOg9+2pSvZp88jyDc0I+CverO3FL5byWZfG8l4BtvaOCozzydV69NIcQPUPhrjtecLG91iPhvXp96Dxc4nC9PtXhPJHdbjykSFu9kCwlPH/Ybj1jMYk9YeTOvUfM5DungIo7cCPaPZK3VT29bwk+mIbnuhZRoj2KQe28VxUgPYPZL71BLzy+gEacvS8V2jup9gg+iMyTvaP/t7y3da+9/0BLvHS7ND2zrGi9oY+APe/yFb099jq+YE9PvRUIm7wW9ui87UEMvbnoDDyuLG89G/NpvYrCFL0lsIk8WaD4veoArT1T8RQ+Rsz0O4q58jxYDjs9qKOavOU7qDwb83k9zYHcPf6iZ73/ffg82l6vPfelAzy3RqE9KdXAPZVA3739/Qo+e5jnPKwbn7xU7hk+pUKpvGQQujzq0K49tHqGPN1sEj3gxoI84Bf0PQmXOzzOK229iyaPvZLpu7zjRHW84txJvHpoUbzL6Ba+HJvGPT0Zrj105cG8asgPPj4ozLpyjJe9JnGpPKh/ADk+Phe92LirO767JzuU3lU9vQUrPRIjHD1fLl294uQfvMYB0z3RFNQ9zBdFPYcahz36N848110BPe03Wz2fTc+9KqgFvcXukjslkPM8";
            String feature1="AAD6RAAAdEOpkHA8wMZ5vccoMb5w5mm6++TjO3ZI47uBVKe8D7NLvQc+ej1DnG29Xf0FvhgTqbzqFYG8c0MmPC+jBj3ljTu9yERWvbvbWz3UFTW91o2tPSlU8j3Zbda8FnWZvWd0lr2jMYk8cP3dvEuIrD38EzW8CljUPMDVErvZyYO99xogvfhzBr1BL6s99iuLPQBXgD2fXxM8odOwPPdFFbxc03W9Em5gPE2kIL5Ulos98PMuPbUK3ruFyfY9REcNOp22zbzfrTG9sM8kPdqMXDraz78988Z2vEd0173eQrC9eKAFPuDSCjzf06K9L6aevTA0rz2m+Hm9/PdHvZl/vT24PD89k8+3vOtfj70cP2w8qhUMPTUfvjzZbyq9fun5u/c3Ar51LZE8oXnuu7by+DtsSYw9NdhdvSLacrwpNfm8ud0MvVNwq70k/Vq8kKN/PVQC+rwxKGC9nrfJPTYjkT1CWJm8FekXvkBvrD1HBJA8a08wvTt5Hjwem769yzGqvQdvoj1eW9C94hC9PGx28b3arx+927KyPcXS77yHxbs9iflEvAlsKLx2gb48dsZIPRB7dT3uy268IK1NvYwdIr05ZEI9QNHou9Rzaj1K+Xs9ObcAPZaZGD5CTKk91hG4PZqv7DxgjoS70NarvUWjlb0NcUM8uz3VPSv4tLzkEm674jOxvIFmxb0EkK08jkGmvWLAvj2dhCg9SVQOPDg/OLwODgE9cozEvOzMJD0qgFA9Ie2PPL6sxzzuFgg+HgwpvIaSB7138xO9kJoNvaEycDwkf+a86qAavQgQtj3j3Lk8Q07XvQWrPT1iihC8QGtYPAjvnrqkb1w9gEaSPbgvc721k589GnTVvSIf4zvCPXg9LJoKPYrsbT0ARKQ9sP+tvIrxdT1kBGG7LEnQPEswSb1trxy+5+d7vXHQWz0k9wI+jjQpvVWlRjsuX8O9WK/JvUaNgDuNJqu8EKGyPWfK6L1VEhG+xhKTvXAfe731cTC9Iyo6vT5UI7143Ye8FP2rvRp8mr2Z3DK8K7IjvvUzlb3IMsA9hHWvPSfsfLza/I88vy6bvRVthbwL4m48tX+SPdmLwjt7Z209HUG2PfCLtb3lI409VDyoPdAPv70nclg9SyRjva0SGj1dKU49Fb38vIdeJjzwIcE9zTrvvJ7jODt5TuQ8m3WnPdfAbTt/7J+9iFbjvYhzabzHA3w9vVXkvAaYDj0QYOa8h9XzPLus07qAdIM8ZsL6PWvhWLxOsZi8nt3lvOrlnzwF7XS8adI3vVJZyLwfnZw94rVVvSq3Hz1TvYW94bxZPSyfz7xK65I8fUSDPUeAmj3LQ+q97PxvPDthYT1jRC48tQ8JPJ1yzDymyH27";
            byte[] bytes0 = Base64Utils.decodeFromString(feature0.trim());
            byte[] bytes1 = Base64Utils.decodeFromString(feature1.trim());

            CompareResult result = faceService.compareFacesByFeatures(bytes0,bytes1);
            logger.info("featureData["+ JSONObject.toJSONString(result));
        }
        System.out.println("firsttime="+firsttime+"  end="+ SystemTimeUtil.dateToString(new Date()));
        return "ok";
    }

}
