package com.jdkcc.ts.web.controller;

import com.google.common.base.Throwables;
import com.jdkcc.ts.dal.entity.Garbage;
import com.jdkcc.ts.service.dto.MResponse;
import com.jdkcc.ts.service.dto.request.BasicSearchReq;
import com.jdkcc.ts.service.dto.request.GarbageReq;
import com.jdkcc.ts.service.dto.response.GarbageDTO;
import com.jdkcc.ts.service.impl.GarbageService;
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
 * 回收站控制器
 * Created by Cherish on 2017/1/6.
 */
@Controller
@RequestMapping("garbage")
public class GarbageController extends ABaseController {

    private final GarbageService garbageService;

    @Autowired
    public GarbageController(GarbageService garbageService) {
        this.garbageService = garbageService;
    }

    /**
     * 对外开放的查看
     * @param garbageId 温馨话语ID
     * @return JSON
     */
    @GetMapping("/{garbageId}")
    @ResponseBody
    public MResponse findOne(@PathVariable Long garbageId){
        try {
            GarbageDTO garbageDTO = garbageService.findOne(garbageId);
            return buildResponse(Boolean.TRUE, "查看详情", garbageDTO);
        } catch (Exception e) {
            log.error("获取列表失败:", Throwables.getStackTraceAsString(e));
            return buildResponse(Boolean.FALSE, BUSY_MSG, null);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/list")
    public ModelAndView list(){
        ModelAndView mv = new ModelAndView("admin/garbage/list");
        return mv;
    }

    /**
     * 返回新增页面
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/add")
    public ModelAndView addForm(){
        ModelAndView mv = new ModelAndView("admin/garbage/add");
        return mv;
    }

    /**
     * 返回修改信息页面
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{garbageId}/update")
    public ModelAndView updateForm(@PathVariable("garbageId") Long garbageId){
        ModelAndView mv = new ModelAndView("admin/garbage/edit");
        Garbage garbage = garbageService.findById(garbageId);
        mv.addObject(garbage);
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
            Page<GarbageDTO> page = garbageService.findAll(basicSearchReq);
            return buildResponse(Boolean.TRUE, basicSearchReq.getDraw(), page);
        } catch (Exception e) {
            log.error("获取列表失败: {}", Throwables.getStackTraceAsString(e));
            return buildResponse(Boolean.FALSE, BUSY_MSG, null);
        }
    }

    /**
     * 删除
     * @param garbageId ID
     * @return JSON
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{garbageId}/delete")
    @ResponseBody
    public MResponse delete(@PathVariable("garbageId") Long garbageId){
        try {
            garbageService.delete(garbageId);
            return buildResponse(Boolean.TRUE, "删除成功", null);
        } catch (Exception e) {
            log.error("删除失败:{}", Throwables.getStackTraceAsString(e));
            return buildResponse(Boolean.FALSE, "删除失败", null);
        }
    }

    /**
     * 更改信息
     * @param garbageReq 更新信息
     * @return ModelAndView
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/update")
    public ModelAndView update(@Validated GarbageReq garbageReq, BindingResult bindingResult){
        log.info("【更新信息】{}", garbageReq);
        ModelAndView mv = new ModelAndView("admin/garbage/edit");
        Map<String, Object> errorMap = new HashMap<>();
        mv.addObject("errorMap", errorMap);

        if(garbageReq == null || garbageReq.getId() == null){
            errorMap.put("msg", "数据错误");
            return mv;
        }
        if (bindingResult.hasErrors()) {
            errorMap.putAll(getErrors(bindingResult));
            mv.addObject("garbage", garbageReq);
        }else {
            try {
                garbageService.update(garbageReq);
                mv.addObject("garbage", garbageService.findById(garbageReq.getId()));
                errorMap.put("msg", "修改成功");
            } catch (Exception e) {
                errorMap.put("msg", "系统繁忙");
                log.error("修改错误:{}", Throwables.getStackTraceAsString(e));
            }
        }
        return mv;
    }



}
