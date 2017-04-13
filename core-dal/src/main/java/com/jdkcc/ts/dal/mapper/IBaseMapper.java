package com.jdkcc.ts.dal.mapper;

import java.io.Serializable;
import java.util.List;

/**
 * 通用DAO接口
 */
public interface IBaseMapper<E, PK extends Serializable> {

    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
     * entity instance completely.
     *
     * @param entity
     * @return the saved entity
     */
    void insert(E entity);

    /**
     * Update a given entity.
     *
     * @param entity
     * @return the update entity
     */
    void update(E entity);

    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal null} if none found
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    E findOne(PK id);

    /**
     * Returns the number of entities available.
     *
     * @return the number of entities
     */
    long count();

    /**
     * Deletes the entity with the given id.
     *
     * @param id must not be {@literal null}.
     * @throws IllegalArgumentException in case the given {@code id} is {@literal null}
     */
    void delete(PK id);


    List<E> findAll(int start, int size);

}
