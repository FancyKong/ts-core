package com.jdkcc.ts.dal.mapper;


import com.jdkcc.ts.dal.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PermissionDAO extends IBaseDAO<Permission, Long> {

    Permission findByPermit(String permit);

    List<Permission> findByRoleId(@Param("roleId") Long roleId);

}
