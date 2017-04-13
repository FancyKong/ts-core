package com.jdkcc.ts.dal.mapper;

import com.jdkcc.ts.dal.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserDAO extends IBaseDAO<User, Long> {

    User findByUsername(String username);


}
