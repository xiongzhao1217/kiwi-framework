package com.kiwiframework.easycoding.base;

import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kiwiframework.easycoding.PageBean;
import com.kiwiframework.core.exception.AppException;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.List;

/**
 * 基于通用MyBatis Mapper插件的Service接口的实现
 * @author xiongzhao
 */
public abstract class AbstractService<T extends BaseModel> implements BaseService<T> {

    @Autowired
    protected BaseMapper<T> mapper;

    /**
     * 当前泛型真实类型的Class
     */
    private Class<T> modelClass;

    public AbstractService() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        modelClass = (Class<T>) pt.getActualTypeArguments()[0];
    }

    @Override
    public int insert(T model) {
        model.setCreateTime(new Date());
        return mapper.insert(model);
    }

    @Override
    public int insertSelective(T model) {
        model.setCreateTime(new Date());
        return mapper.insertSelective(model);
    }

    @Override
    public int insertBatch(List<T> models) {
        Date now = new Date();
        for (T model: models) {
            model.setCreateTime(now);
        }
        return mapper.insertList(models);
    }

    @Override
    public int deleteById(Object id) {
        return mapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteByIds(String ids) {
        return mapper.deleteByIds(ids);
    }

    @Override
    public int delete(T model) {
        return mapper.delete(model);
    }

    @Override
    public int update(T model) {
        model.setUpdateTime(new Date());
        return mapper.updateByPrimaryKey(model);
    }

    @Override
    public int updateSelective(T model) {
        model.setUpdateTime(new Date());
        return mapper.updateByPrimaryKeySelective(model);
    }

    @Override
    public int updateByConditionSelective(T model, Condition condition) {
        model.setUpdateTime(new Date());
        return mapper.updateByConditionSelective(model, condition);
    }

    @Override
    public T selectById(Object id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public T selectBy(String fieldName, Object value) throws TooManyResultsException {
        try {
            T model = modelClass.newInstance();
            Field field = modelClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(model, value);
            return mapper.selectOne(model);
        } catch (ReflectiveOperationException e) {
            throw new AppException(e.getMessage(), e);
        }
    }

    @Override
    public T selectOne(T model) {
        return mapper.selectOne(model);
    }

    @Override
    public List<T> findByIds(String ids) {
        return mapper.selectByIds(ids);
    }

    @Override
    public List<T> find(T model) {
        return mapper.select(model);
    }

    @Override
    public List<T> findByCondition(Condition condition) {
        return mapper.selectByCondition(condition);
    }

    @Override
    public List<T> findAll() {
        return mapper.selectAll();
    }

    @Override
    public int selectCount(T model) {
        return mapper.selectCount(model);
    }

    @Override
    public int selectCountByCondition(Condition condition) {
        return mapper.selectCountByCondition(condition);
    }

    @Override
    public PageInfo<T> findPageList(PageBean pageBean, final T model) {
        PageInfo<T> page = PageHelper.startPage(pageBean.getPageNum(), pageBean.getPageSize())
                .doSelectPageInfo(new ISelect() {
                    @Override
                    public void doSelect() {
                        find(model);
                    }
                });
        return page;
    }

    @Override
    public PageInfo<T> findPageListByCondition(PageBean pageBean, final Condition condition){
        PageInfo<T> page = PageHelper.startPage(pageBean.getPageNum(), pageBean.getPageSize()).doSelectPageInfo(new ISelect() {
            @Override
            public void doSelect() {
                findByCondition(condition);
            }
        });
        return page;
    }
}