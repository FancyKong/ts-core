package com.jdkcc.ts.service.impl;

import com.jdkcc.ts.common.enums.ActiveEnum;
import com.jdkcc.ts.common.util.ObjectConvertUtil;
import com.jdkcc.ts.dal.entity.Customer;
import com.jdkcc.ts.dal.repository.CustomerDAO;
import com.jdkcc.ts.dal.repository.IBaseDAO;
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
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CustomerService extends ABaseService<Customer, Long> {

    private final CustomerDAO customerDAO;

    @Autowired
    public CustomerService(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Override
    protected IBaseDAO<Customer, Long> getEntityDAO() {
        return customerDAO;
    }

    public CustomerDTO findOne(Long CustomerId) {
        Customer customer = customerDAO.findOne(CustomerId);
        return customer == null ? null : ObjectConvertUtil.objectCopy(new CustomerDTO(), customer);
    }

    public Customer findByWxUserId(Long wxUserId){
        return customerDAO.findByWxUserId(wxUserId);
    }

    public Customer findByTelephone(String telephone){
        return customerDAO.findByTelephone(telephone);
    }

    public boolean exist(String telephone) {
        return customerDAO.findByTelephone(telephone) != null;
    }

    @Transactional
    public void delete(Long customerId) {
        Customer customer = customerDAO.findOne(customerId);
        if (customer == null) return;
        super.delete(customerId);
    }

    public Page<CustomerDTO> findAll(BasicSearchReq basicSearchReq, CustomerSearchReq customerSearchReq) {

        int pageNumber = basicSearchReq.getStartIndex() / basicSearchReq.getPageSize() + 1;
        PageRequest pageRequest = super.buildPageRequest(pageNumber, basicSearchReq.getPageSize());

        //除了分页条件没有特定搜索条件的，为了缓存count
        if (ObjectConvertUtil.objectFieldIsBlank(customerSearchReq)){
            List<Customer> customerList = customerDAO.listAllPaged(pageRequest);
            List<CustomerDTO> CustomerDTOList = customerList.stream().map(this::getCustomerDTO).collect(Collectors.toList());

            //为了计算总数使用缓存，加快搜索速度
            Long count = getCount();
            return new PageImpl<>(CustomerDTOList, pageRequest, count);
        }

        //有了其它搜索条件
        Page<Customer> customerPage = super.findAllBySearchParams(
                buildSearchParams(customerSearchReq), pageNumber, basicSearchReq.getPageSize());

        return customerPage.map(this::getCustomerDTO);
    }

    private CustomerDTO getCustomerDTO(Customer source) {
        CustomerDTO customerDTO = new CustomerDTO();
        ObjectConvertUtil.objectCopy(customerDTO, source);
        customerDTO.setActiveStr(ActiveEnum.getDesc(source.getActive()));
        return customerDTO;
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
    public void save(CustomerReq customerReq) {
        Customer customer = new Customer();
        ObjectConvertUtil.objectCopy(customer, customerReq);
        customer.setCreatedTime(new Date());
        customer.setModifiedTime(new Date());
        this.save(customer);
    }



}
