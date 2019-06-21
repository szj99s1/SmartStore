package com.daofu.commons.exception;

import com.daofu.commons.utils.MsgInterface;
import lombok.Getter;

/**
 * @author Li-chuang
 */
public class MyBussinessException extends RuntimeException {

    private static final long serialVersionUID = 0L;

    public MyBussinessException(){
        super();
    }

    @Getter
    private MsgInterface msgInterface;

    public MyBussinessException(MsgInterface msgInterface){
        super(msgInterface.getMsg());
        this.msgInterface = msgInterface;
    }

    public MyBussinessException(String message) {
        super(message);
    }
}
