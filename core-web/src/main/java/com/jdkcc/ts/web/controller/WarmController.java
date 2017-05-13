package com.jdkcc.ts.web.controller;

import com.google.common.base.Throwables;
import com.jdkcc.ts.dal.entity.Warm;
import com.jdkcc.ts.service.dto.MResponse;
import com.jdkcc.ts.service.dto.request.WarmReq;
import com.jdkcc.ts.service.dto.request.BasicSearchReq;
import com.jdkcc.ts.service.dto.response.WarmDTO;
import com.jdkcc.ts.service.impl.WarmService;
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
@RequestMapping("warm")
public class WarmController extends ABaseController {

    private final WarmService warmService;

    @Autowired
    public WarmController(WarmService warmService) {
        this.warmService = warmService;
    }

    /**
     * 对外开放的查看温馨话语详情
     * @param warmId 温馨话语ID
     * @return JSON
     */
    @GetMapping("/{warmId}")
    @ResponseBody
    public MResponse findOne(@PathVariable Long warmId){
        try {
            WarmDTO warmDTO = warmService.findOne(warmId);
            return buildResponse(Boolean.TRUE, "查看温馨话语详情", warmDTO);
        } catch (Exception e) {
            log.error("获取列表失败:", Throwables.getStackTraceAsString(e));
            return buildResponse(Boolean.FALSE, BUSY_MSG, null);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/list")
    public ModelAndView list(){
        ModelAndView mv = new ModelAndView("admin/warm/list");
        return mv;
    }

    /**
     * 返回修改页面
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{warmId}/update")
    public ModelAndView updateForm(@PathVariable("warmId") Long warmId){
        ModelAndView mv = new ModelAndView("admin/warm/edit");
        Warm warm = warmService.findById(warmId);
        mv.addObject(warm);
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
            Page<WarmDTO> page = warmService.findAll(basicSearchReq);
            return buildResponse(Boolean.TRUE, basicSearchReq.getDraw(), page);
        } catch (Exception e) {
            log.error("获取列表失败: {}", Throwables.getStackTraceAsString(e));
            return buildResponse(Boolean.FALSE, BUSY_MSG, null);
        }
    }

    /**
     * 删除
     * @param warmId ID
     * @return JSON
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{warmId}/delete")
    @ResponseBody
    public MResponse delete(@PathVariable("warmId") Long warmId){
        try {
            warmService.delete(warmId);
            return buildResponse(Boolean.TRUE, "删除成功", null);
        } catch (Exception e) {
            log.error("删除失败:{}", Throwables.getStackTraceAsString(e));
            return buildResponse(Boolean.FALSE, "删除失败", null);
        }
    }

    /**
     * 更改信息
     * @param warmReq 更新信息
     * @return ModelAndView
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/update")
    public ModelAndView update(@Validated WarmReq warmReq, BindingResult bindingResult){
        log.info("【更新信息】{}", warmReq);
        ModelAndView mv = new ModelAndView("admin/warm/edit");
        Map<String, Object> errorMap = new HashMap<>();
        mv.addObject("errorMap", errorMap);

        if(warmReq == null || warmReq.getId() == null){
            errorMap.put("msg", "数据错误");
            return mv;
        }
        if (bindingResult.hasErrors()) {
            errorMap.putAll(getErrors(bindingResult));
            mv.addObject("warm", warmReq);
        }else {
            try {
                warmService.update(warmReq);
                mv.addObject("warm", warmService.findById(warmReq.getId()));
                errorMap.put("msg", "修改成功");
            } catch (Exception e) {
                errorMap.put("msg", "系统繁忙");
                log.error("修改错误:{}", Throwables.getStackTraceAsString(e));
            }
        }
        return mv;
    }



}
