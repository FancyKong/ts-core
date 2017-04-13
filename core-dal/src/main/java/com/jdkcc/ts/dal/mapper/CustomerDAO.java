package com.jdkcc.ts.dal.mapper;

import com.jdkcc.ts.dal.entity.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CustomerDAO extends IBaseDAO<Customer, Long> {

    Customer findByTelephone(String telephone);

}
