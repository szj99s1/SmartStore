package com.daofu.commons.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 内部用户模块返回给前端的消息
 * @author Created by lc on 2018-05-06 14:18
 *
 */
@Getter
@AllArgsConstructor
public enum CommonEnum implements MsgInterface {
    /**
     * 404
     */
    RESOURCES_NOT_FOUNT("404", "404"),
    /**
     * Ops failure msg enum.
     */
    FAILURE("-1", "操作失败"),
    /**
     * Ops success msg enum.
     */
    SUCCESS("0", "操作成功"),
    /**
     * Ops success msg enum.
     */
    GET_ERROR("50", "Get请求失败"),
    /**
     * Ops success msg enum.
     */
    POST_ERROR("51", "Post请求失败");

    private String code;

    private String msg;
}
