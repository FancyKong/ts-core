package com.jdkcc.ts.web.controller;

import com.google.common.base.Throwables;
import com.jdkcc.ts.dal.entity.WarmSweet;
import com.jdkcc.ts.service.dto.MResponse;
import com.jdkcc.ts.service.dto.request.WarmSweetReq;
import com.jdkcc.ts.service.dto.request.BasicSearchReq;
import com.jdkcc.ts.service.dto.response.WarmSweetDTO;
import com.jdkcc.ts.service.impl.WarmSweetService;
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
 * 温馨墙控制器
 * Created by Cherish on 2017/1/6.
 */
@Controller
@RequestMapping("warmSweet")
public class WarmSweetController extends ABaseController {

    private final WarmSweetService warmSweetService;

    @Autowired
    public WarmSweetController(WarmSweetService warmSweetService) {
        this.warmSweetService = warmSweetService;
    }

    /**
     * 对外开放的查看温馨话语详情
     * @param warmSweetId 温馨话语ID
     * @return JSON
     */
    @GetMapping("/{warmSweetId}")
    @ResponseBody
    public MResponse findOne(@PathVariable Long warmSweetId){
        try {
            WarmSweetDTO warmSweet = warmSweetService.findOne(warmSweetId);
            return buildResponse(Boolean.TRUE, "查看温馨话语详情", warmSweet);
        } catch (Exception e) {
            log.error("获取列表失败:", Throwables.getStackTraceAsString(e));
            return buildResponse(Boolean.FALSE, BUSY_MSG, null);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/list")
    public ModelAndView list(){
        ModelAndView mv = new ModelAndView("admin/warmSweet/list");
        return mv;
    }

    /**
     * 返回新增页面
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/add")
    public ModelAndView addForm(){
        ModelAndView mv = new ModelAndView("admin/warmSweet/add");
        return mv;
    }

    /**
     * 返回修改信息页面
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{warmSweetId}/update")
    public ModelAndView updateForm(@PathVariable("warmSweetId") Long warmSweetId){
        ModelAndView mv = new ModelAndView("admin/warmSweet/edit");
        WarmSweet warmSweet = warmSweetService.findById(warmSweetId);
        mv.addObject(warmSweet);
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
    public MResponse toPage(BasicSearchReq basicSearchReq){
        try {
            Page<WarmSweetDTO> page = warmSweetService.findAll(basicSearchReq);
            return buildResponse(Boolean.TRUE, basicSearchReq.getDraw(), page);
        } catch (Exception e) {
            log.error("获取列表失败: {}", Throwables.getStackTraceAsString(e));
            return buildResponse(Boolean.FALSE, BUSY_MSG, null);
        }
    }

    /**
     * 删除
     * @param warmSweetId ID
     * @return JSON
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{warmSweetId}/delete")
    @ResponseBody
    public MResponse delete(@PathVariable("warmSweetId") Long warmSweetId){
        try {
            warmSweetService.delete(warmSweetId);
            return buildResponse(Boolean.TRUE, "删除成功", null);
        } catch (Exception e) {
            log.error("删除失败:{}", Throwables.getStackTraceAsString(e));
            return buildResponse(Boolean.FALSE, "删除失败", null);
        }
    }

    /**
     * 更改信息
     * @param warmSweetReq 更新信息
     * @return ModelAndView
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/update")
    public ModelAndView update(@Validated WarmSweetReq warmSweetReq, BindingResult bindingResult){
        log.info("【更新信息】{}", warmSweetReq);
        ModelAndView mv = new ModelAndView("admin/warmSweet/edit");
        Map<String, Object> errorMap = new HashMap<>();
        mv.addObject("errorMap", errorMap);

        if(warmSweetReq == null || warmSweetReq.getId() == null){
            errorMap.put("msg", "数据错误");
            return mv;
        }
        if (bindingResult.hasErrors()) {
            errorMap.putAll(getErrors(bindingResult));
            mv.addObject("warmSweet", warmSweetReq);
        }else {
            try {
                warmSweetService.update(warmSweetReq);
                mv.addObject("warmSweet", warmSweetService.findById(warmSweetReq.getId()));
                errorMap.put("msg", "修改成功");
            } catch (Exception e) {
                errorMap.put("msg", "系统繁忙");
                log.error("修改错误:{}", Throwables.getStackTraceAsString(e));
            }
        }
        return mv;
    }



}
