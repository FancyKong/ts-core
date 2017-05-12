package com.jdkcc.ts.web.controller;

import com.google.common.base.Throwables;
import com.jdkcc.ts.dal.entity.Customer;
import com.jdkcc.ts.security.PasswordUtil;
import com.jdkcc.ts.service.dto.MResponse;
import com.jdkcc.ts.service.dto.request.BasicSearchReq;
import com.jdkcc.ts.service.dto.request.customer.CustomerReq;
import com.jdkcc.ts.service.dto.request.customer.CustomerSearchReq;
import com.jdkcc.ts.service.dto.response.CustomerDTO;
import com.jdkcc.ts.service.impl.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * 会员
 * Created by Cherish on 2017/1/6.
 */
@Controller
@RequestMapping("customer")
public class CustomerController extends ABaseController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/list")
    public ModelAndView list(){
        ModelAndView mv = new ModelAndView("admin/customer/list");
        return mv;
    }

    /**
     * 返回新增页面
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/add")
    public ModelAndView addForm(){
        ModelAndView mv = new ModelAndView("admin/customer/add");
        return mv;
    }

    /**
     * 返回修改信息页面
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{customerId}/update")
    public ModelAndView updateForm(@PathVariable("customerId") Long customerId){
        ModelAndView mv = new ModelAndView("admin/customer/edit");
        Customer customer = customerService.findById(customerId);
        mv.addObject(customer);
        return mv;
    }

    /**
     * 分页查询
     * @param basicSearchReq 基本搜索条件
     * @return JSON
     * @date 2016年8月30日 下午5:30:18
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/page")
    @ResponseBody
    public MResponse toPage(BasicSearchReq basicSearchReq, CustomerSearchReq customerSearchReq){

        try {
            Page<CustomerDTO> page = customerService.findAll(basicSearchReq, customerSearchReq);

            return buildResponse(Boolean.TRUE, basicSearchReq.getDraw(), page);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取列表失败: {}", Throwables.getStackTraceAsString(e));
            return buildResponse(Boolean.FALSE, BUSY_MSG, null);
        }
    }

    /**
     * 删除
     * @param customerId ID
     * @return JSON
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{customerId}/delete")
    @ResponseBody
    public MResponse delete(@PathVariable("customerId") Long customerId){

        try {
            customerService.delete(customerId);
            return buildResponse(Boolean.TRUE, "删除成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("删除失败:{}", Throwables.getStackTraceAsString(e));
            return buildResponse(Boolean.FALSE, "删除失败", null);
        }
    }

    /**
     * 更改信息
     * @param customerReq 更新信息
     * @return ModelAndView
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/update")
    public ModelAndView update(@Validated CustomerReq customerReq, BindingResult bindingResult){

        ModelAndView mv = new ModelAndView("admin/customer/edit");
        Map<String, Object> errorMap = new HashMap<>();
        mv.addObject("errorMap", errorMap);

        if(customerReq == null || customerReq.getId() == null){
            errorMap.put("msg", "数据错误");
            return mv;
        }

        if (bindingResult.hasErrors()) {
            errorMap.putAll(getErrors(bindingResult));
            mv.addObject("customer", customerReq);
            return mv;
        }

        try {
            customerService.update(customerReq);
            mv.addObject("customer", customerService.findById(customerReq.getId()));
            errorMap.put("msg", "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            errorMap.put("msg", "系统繁忙");
            log.error("【修改错误】 {}", Throwables.getStackTraceAsString(e));
        }
        return mv;
    }

    /**
     * 保存新用户
     * @param customerReq 保存的信息
     * @return ModelAndView
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/save")
    public ModelAndView save(@Validated CustomerReq customerReq, BindingResult bindingResult){

        ModelAndView mv = new ModelAndView("admin/customer/add");
        Map<String, Object> errorMap = new HashMap<>();
        mv.addObject("errorMap", errorMap);

        if (bindingResult.hasErrors()) {
            errorMap.putAll(getErrors(bindingResult));
            mv.addObject("customer", customerReq);
            return mv;
        }

        try {
            customerReq.setPassword(PasswordUtil.sha1(customerReq.getPassword()));
            customerService.save(customerReq);
            errorMap.put("msg", "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            errorMap.put("msg", "系统繁忙");
            log.error("【添加失败】 {}", Throwables.getStackTraceAsString(e));
        }
        return mv;
    }

    /**
     * 提交密码修改请求
     * @return ModelAndView
     */
    @PostMapping("/modifyPassword")
    public ModelAndView modifyPassword() {

        return null;
    }


}
