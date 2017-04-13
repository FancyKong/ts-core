package com.jdkcc.ts.dal.mapper;

import com.jdkcc.ts.dal.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RoleDAO extends IBaseDAO<Role, Long> {

    Role findByName(String name);

    List<Role> findByUserId(@Param("userId") Long userId);


}
