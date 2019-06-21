//package com.daofu.commons.log;
//
//import com.daofu.commons.utils.GsonUtils;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//
///**
// * @author li-chuang
// * @date created in 2019/1/8 13:35
// * @description
// */
//@Aspect
//@Component
//public class LogAspect {
//
//    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);
//
//    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
//            "|| @annotation(org.springframework.web.bind.annotation.PostMapping) " +
//            "|| @annotation(org.springframework.web.bind.annotation.GetMapping) " +
//            "|| @annotation(org.springframework.web.bind.annotation.PutMapping) " +
//            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)")
//    public void point(){}
//
//    /**
//     * @description AOP执行的方法
//     * @author lc
//     * @date 2019/1/8 13:37
//     * @param pjp
//     * @return java.lang.Object
//     */
//    @Around("point()")
//    public Object validIdentityAndSecure(ProceedingJoinPoint pjp) throws Throwable {
//        long start = System.currentTimeMillis();
//        Object proceed;
//        try {
//            logger.info("方法参数：" + getParameter(pjp));
//        } catch (Exception e){
//            logger.error("日志打印失败：" + e.getMessage());
//        }
//
//        try {
//            proceed = pjp.proceed();
//        } catch (Exception e) {
//            throw e;
//        }
//
//        logger.info("返回输出：" + proceed + "   方法执行时间：" + (System.currentTimeMillis() - start) + "毫秒");
//        return proceed;
//    }
//
//    /**
//     * @description
//     * @author lc
//     * @date 2019/1/8 14:00
//     * @param pjp
//     * @return void
//     */
//    private String getParameter(ProceedingJoinPoint pjp){
//        Object[] args = pjp.getArgs();
//        for(Object object : args){
//            if(object instanceof HttpServletRequest || object instanceof HttpServletRequest){
//                continue;
//            } else {
//                return GsonUtils.toJson(object);
//            }
//        }
//        return "";
//    }
//}