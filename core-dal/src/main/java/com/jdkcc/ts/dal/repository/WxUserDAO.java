package com.jdkcc.ts.dal.repository;

import com.jdkcc.ts.dal.entity.WxUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WxUserDAO extends IBaseDAO<WxUser,Long> {

    WxUser findByOpenid(String openid);

    @Query("SELECT wx FROM WxUser AS wx ")
    List<WxUser> listAllPaged(Pageable pageable);



}
