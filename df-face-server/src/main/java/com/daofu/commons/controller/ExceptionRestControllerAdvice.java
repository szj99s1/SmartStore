package com.daofu.commons.controller;

import com.daofu.commons.exception.MyBussinessException;
import com.daofu.commons.utils.CommonEnum;
import com.daofu.commons.utils.JsonResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * @author Created by lc
 */
@RestControllerAdvice
@RestController
public class ExceptionRestControllerAdvice {

    @ExceptionHandler(value = Throwable.class)
    public Map<String, Object> error(Throwable exception) {
        LOGGER.error("error catched in ExceptionHandler: " + exception.getMessage(), exception);
        return JsonResultBuilder.fail(CommonEnum.FAILURE).toMap();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> bindMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return JsonResultBuilder.fail(exception.getBindingResult()).toMap();
    }

    @ExceptionHandler(value = MyBussinessException.class )
    public Map<String, Object> adviceMyBussinessException(MyBussinessException exception) {
        LOGGER.error("error catched in MyBussinessException: " + exception.getMessage());
        return JsonResultBuilder.fail(exception.getMsgInterface()).toMap();
    }

    @RequestMapping(value = "/404")
    public Map<String, Object> error404() {
        return JsonResultBuilder.fail(CommonEnum.RESOURCES_NOT_FOUNT).toMap();
    }

    private static Logger LOGGER = LoggerFactory.getLogger(ExceptionRestControllerAdvice.class);
}
