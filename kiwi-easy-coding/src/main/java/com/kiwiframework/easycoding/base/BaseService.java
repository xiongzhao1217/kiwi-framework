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
     *          要插入的对象
     * @return 数据库表变化的行数
     */
    int insert(T model);

    /**
     * insert model
     * @param model 要插入的对象
     * @return 数据库表变化的行数
     */
    int insertSelective(T model);

    /**
     * batch insert
     * @param models 对象列表
     * @return 数据库表变化的行数
     */
    int insertBatch(List<T> models);

    /**
     * delete by primary key
     * @param id 主键id
     * @return 数据库表变化的行数
     */
    int deleteById(Object id);

    /**
     * batch delete
     * eg：ids = “1,2,3,4”
     * @param ids 多个主键id,用英文逗号隔开
     * @return 数据库表变化的行数
     */
    int deleteByIds(String ids);

    /**
     * delete by model
     * 为避免多删误删,请保证调用者逻辑缜密,慎用
     * @param model 更新的对象
     * @return 数据库表变化的行数
     */
    int delete(T model);

    /**
     * update
     * 所有字段都更新
     * @param model 更新的对象
     * @return 数据库表变化的行数
     */
    int update(T model);

    /**
     * update selective
     * 只更新非空字段
     * @param model 更新的对象
     * @return 数据库表变化的行数
     */
    int updateSelective(T model);

    /**
     * update by condition
     * @param model 更新的对象
     * @param condition 更新条件
     * @return 数据库表变化的行数
     */
    int updateByConditionSelective(T model, Condition condition);

    /**
     * selete by primary key
     * @param id 主键id
     * @return 数据库表映射对象
     */
    T selectById(Object id);

    /**
     * 通过Model中某个成员变量名称（非数据表中column的名称）查找
     * value需符合unique约束,否则会抛出异常
     * @param fieldName 字段名称
     * @param value 字段值
     * @return 数据库表映射对象
     * @throws TooManyResultsException 查询结果不唯一会抛出这个异常
     */
    T selectBy(String fieldName, Object value) throws TooManyResultsException;

    /**
     * select one
     * @param model 查询条件
     * @return 数据库表映射对象
     */
    T selectOne(T model);

    /**
     * 通过多个ID查找
     * eg：ids = “1,2,3,4”
     * @param ids 主键id,多个用英文逗号隔开
     * @return 数据库表映射对象列表
     */
    List<T> findByIds(String ids);

    /**
     * find by model
     * @param model 查询条件
     * @return 数据库表映射对象列表
     */
    List<T> find(T model);

    /**
     * 根据条件查找
     * @param condition 查询条件
     * @return 数据库表映射对象列表
     */
    List<T> findByCondition(Condition condition);

    /**
     * find all
     * @return 数据库表映射对象列表
     */
    List<T> findAll();

    /**
     * select count
     * @param model 查询条件
     * @return 统计的行数
     */
    int selectCount(T model);

    /**
     * select count by condition
     * @param condition 查询条件
     * @return 统计的行数
     */
    int selectCountByCondition(Condition condition);

    /**
     * page query
     * @param pageBean 分页信息
     * @param model 查询条件
     * @return 分页查询的结果
     */
    PageInfo<T> findPageList(PageBean pageBean, T model);

    /**
     * page query by condition
     * @param pageBean 分页信息
     * @param condition 查询条件
     * @return 分页查询的结果
     */
    PageInfo<T> findPageListByCondition(PageBean pageBean, Condition condition);
}
