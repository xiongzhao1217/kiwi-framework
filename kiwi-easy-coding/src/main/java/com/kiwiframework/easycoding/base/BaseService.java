package com.kiwiframework.easycoding.base;

import com.github.pagehelper.PageInfo;
import com.kiwiframework.easycoding.PageBean;
import org.apache.ibatis.exceptions.TooManyResultsException;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;

/**
 * Service 层 基础接口，其他Service 接口 请继承该接口
 * @author xiongzhao
 */
public interface BaseService<T> {

    /**
     * insert model
     * @param model
     * @return
     */
    int insert(T model);

    /**
     * insert model
     * @param model
     * @return
     */
    int insertSelective(T model);

    /**
     * batch insert
     * @param models
     * @return
     */
    int insertBatch(List<T> models);

    /**
     * delete by primary key
     * @param id
     * @return
     */
    int deleteById(Object id);

    /**
     * batch delete
     * eg：ids -> “1,2,3,4”
     * @param ids
     */
    int deleteByIds(String ids);

    /**
     * delete by model
     * 为避免多删误删,请保证调用者逻辑缜密,慎用
     * @param model
     * @return
     */
    int delete(T model);

    /**
     * update
     * 所有字段都更新
     * @param model
     */
    int update(T model);

    /**
     * update selective
     * 只更新非空字段
     * @param model
     */
    int updateSelective(T model);

    /**
     * update by condition
     * @param condition
     * @return
     */
    int updateByConditionSelective(T model, Condition condition);

    /**
     * selete by primary key
     * @param id
     * @return
     */
    T selectById(Object id);

    /**
     * 通过Model中某个成员变量名称（非数据表中column的名称）查找
     * value需符合unique约束,否则会抛出异常
     * @param fieldName
     * @param value
     * @return
     * @throws TooManyResultsException
     */
    T selectBy(String fieldName, Object value) throws TooManyResultsException;

    /**
     * select one
     * @param model
     * @return
     */
    T selectOne(T model);

    /**
     * 通过多个ID查找
     * eg：ids -> “1,2,3,4”
     * @param ids
     * @return
     */
    List<T> findByIds(String ids);

    /**
     * find by model
     * @param model
     * @return
     */
    List<T> find(T model);

    /**
     * 根据条件查找
     * @param condition
     * @return
     */
    List<T> findByCondition(Condition condition);

    /**
     * find all
     * @return
     */
    List<T> findAll();

    /**
     * select count
     * @param model
     * @return
     */
    int selectCount(T model);

    /**
     * select count by condition
     * @param condition
     * @return
     */
    int selectCountByCondition(Condition condition);

    /**
     * page query
     * @param model
     * @return
     */
    PageInfo<T> findPageList(PageBean pageBean, T model);

    /**
     * page query by condition
     * @param pageBean
     * @param condition
     * @return
     */
    PageInfo<T> findPageListByCondition(PageBean pageBean, Condition condition);
}
