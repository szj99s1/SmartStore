package com.daofu.commons.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.Valid;
import java.util.Optional;

/**
 * 分页
 * @author lc
 * @version 1.0
 **/
public class Paginator<T> {
    /**
     * 页码*
     */
    private Integer currPage;
    /**
     * 页尺
     */
    private Integer pageSize;
    /**
     * 总条数
     */
    private int total;
    /**
     * 查询参数/查询结果
     */
    @Valid
    private T data;

    public Paginator() {
    }

    public Paginator(Integer currPage, Integer pageSize, int total, T data) {
        this.currPage = currPage;
        this.pageSize = pageSize;
        this.total = total;
        this.data = data;
    }

    /**
     * 页码初始化
     */
    public int getCurrPage() {
        return Optional.ofNullable(currPage).orElseGet(() -> 1);
    }

    /**
     * 页尺初始化
     */
    public int getPageSize() {
        return Optional.ofNullable(pageSize).orElseGet(() -> 20);
    }

    /**
     * 分页起始参数
     */
    @JsonIgnore
    public int getOffset() {
        return this.getCurrPage() > 0 ? this.getPageSize() * (this.getCurrPage() - 1) : 0;
    }

    public void setCurrPage(Integer currPage) {
        this.currPage = currPage;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}