package com.kiwiframework.easycoding.base;

import javax.persistence.Column;
import java.util.Date;

/**
 * model类基类
 * 方便在AbstractService基类中对日期进行自动设置
 * @author xiongzhao
 * @date 18/12/4
 */
public class BaseModel {

    /**
     * 创建时间字段名称常量
     */
    public static final String CREATE_TIME = "createTime";

    /**
     * 更新时间字段名称常量
     */
    public static final String UPDATE_TIME = "updateTime";

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
