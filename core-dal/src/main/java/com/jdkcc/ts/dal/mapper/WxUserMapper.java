package com.jdkcc.ts.dal.mapper;

import com.jdkcc.ts.dal.entity.WxUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface WxUserMapper extends IBaseMapper<WxUser, Long> {

    WxUser findByOpenid(String openid);

}
