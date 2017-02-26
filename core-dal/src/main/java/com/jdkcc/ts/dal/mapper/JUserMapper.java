/**
 * Bestpay.com.cn Inc.
 * Copyright (c) 2011-2016 All Rights Reserved.
 */
package com.jdkcc.ts.dal.mapper;

import com.jdkcc.ts.dal.entity.JUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Jiangjiaze
 * @version Id: JUserMapper.java, v 0.1 2016/9/27 0027 下午 3:34 FancyKong Exp $$
 */
@Mapper
@Repository
public interface JUserMapper {
    JUser selectById(long id);
    int insert(JUser user);
}
