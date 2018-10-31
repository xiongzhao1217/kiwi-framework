package com.kiwiframework.easycoding;

/**
 * 分页bean
 * @author xiongzhao
 */
public class PageBean {

    /**
     * 默认页面
     */
    private static final Integer DEFAULT_PAGE_NUM = 1;

    /**
     * 默认页面大小
     */
    private static final Integer DEFAULT_PAGE_SIZE = 10;

    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 页面大小
     */
    private Integer pageSize;

    /**
     * 分页,如: create_time desc, 支持传null,支持多字段排序,支持[表别名.排序字段]排序
     */
    private String orderBy;

    public PageBean() {
        this.pageNum = DEFAULT_PAGE_NUM;
        this.pageSize = DEFAULT_PAGE_SIZE;
    }

    public PageBean(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public PageBean(Integer pageNum, Integer pageSize, String orderBy) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.orderBy = orderBy;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
