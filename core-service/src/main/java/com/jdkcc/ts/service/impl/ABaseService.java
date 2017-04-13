package com.jdkcc.ts.service.impl;

import com.jdkcc.ts.common.util.Reflections;
import com.jdkcc.ts.dal.mapper.IBaseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * ABaseService
 * 基础Service 封装常用 CURD 及分页 查询
 * 子类继承后可以省略代码
 * @author Cherish
 */
@Component
public abstract class ABaseService<E, PK extends Serializable> {

    protected Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 数据库实体类的类类型
     * eg. Class<User>
     */
    protected Class<E> clazz;

    protected abstract IBaseMapper<E, PK> getEntityDAO();

    /**
     * 用于Service层子类使用的构造函数.
     * 通过子类的泛型定义取得对象类型Class.
     * eg. public class UserService extends ABaseService<User, Long>
     */
    public ABaseService() {
        this.clazz = Reflections.getClassGenericType(getClass());
    }

    @Transactional(readOnly = false)
    public void insert(E entity) {
        getEntityDAO().insert(entity);
    }

    @Transactional(readOnly = false)
    public void delete(PK id) {
        getEntityDAO().delete(id);
    }

    @Transactional(readOnly = false)
    public void update(E entity) {
        getEntityDAO().update(entity);
    }

    public E findById(PK id) {
        return getEntityDAO().findOne(id);
    }

    public Long count() {
        return getEntityDAO().count();
    }

    public List<E> findAll(int start, int size) {
        return getEntityDAO().findAll(start, size);
    }


}
