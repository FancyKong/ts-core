package com.jdkcc.ts.service.impl;

import com.jdkcc.ts.common.util.ObjectConvertUtil;
import com.jdkcc.ts.common.util.SHA;
import com.jdkcc.ts.dal.entity.Customer;
import com.jdkcc.ts.dal.mapper.CustomerMapper;
import com.jdkcc.ts.dal.mapper.IBaseMapper;
import com.jdkcc.ts.service.dto.request.BasicSearchReq;
import com.jdkcc.ts.service.dto.request.customer.CustomerReq;
import com.jdkcc.ts.service.dto.request.customer.CustomerSearchReq;
import com.jdkcc.ts.service.dto.response.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CustomerService extends ABaseService<Customer, Long> {

    private final CustomerMapper customerMapper;

    private static final String UNKNOW = "未知";
    private static final String AC = "激活";
    private static final String UN = "冻结";

    @Autowired
    public CustomerService(CustomerMapper customerMapper) {
        this.customerMapper = customerMapper;
    }

    @Override
    protected IBaseMapper<Customer, Long> getEntityDAO() {
        return customerMapper;
    }

    public CustomerDTO findOne(Long CustomerId) {
        Customer customer = customerMapper.findOne(CustomerId);
        return customer == null ? null : ObjectConvertUtil.objectCopy(new CustomerDTO(), customer);
    }

    public boolean exist(String telephone) {
        return customerMapper.findByTelephone(telephone) != null;
    }

    @Transactional
    public void delete(Long customerId) {
        Customer customer = customerMapper.findOne(customerId);
        if (customer == null) return;

        super.delete(customerId);
    }

    public Page<CustomerDTO> findAll(BasicSearchReq basicSearchReq, CustomerSearchReq customerSearchReq) {
        Integer start = basicSearchReq.getStartIndex();
        Integer size = basicSearchReq.getPageSize();

        int pageNumber =  start / size + 1;
        PageRequest pageRequest = new PageRequest(pageNumber, size);

        List<Customer> customerList = customerMapper.findAll( start, size);
        Long count = count();

        //有了其它搜索条件
        Page<Customer> userPage = new PageImpl<>(customerList, pageRequest, count);

        return userPage.map(source -> {
            CustomerDTO customerDTO = new CustomerDTO();
            ObjectConvertUtil.objectCopy(customerDTO, source);
            customerDTO.setActiveStr(source.getActive() == null ? UNKNOW : source.getActive() == 1 ? AC : UN);
            return customerDTO;
        });
    }

    @Transactional
    public void update(CustomerReq customerReq) {
        Customer customer = this.findById(customerReq.getId());
        if (customer == null) return;

        ObjectConvertUtil.objectCopy(customer, customerReq);
        customer.setModifiedTime(new Date());
        this.update(customer);

    }

    @Transactional
    public void insert(CustomerReq customerReq) {
        Customer customer = new Customer();
        ObjectConvertUtil.objectCopy(customer, customerReq);
        customer.setPassword(SHA.sha1(customer.getPassword()));
        customer.setCreatedTime(new Date());
        customer.setModifiedTime(new Date());

        this.insert(customer);
    }


}
