package com.jdkcc.ts.web.controller;

import com.google.common.base.Throwables;
import com.jdkcc.ts.dal.entity.Grade;
import com.jdkcc.ts.service.dto.MResponse;
import com.jdkcc.ts.service.dto.request.BasicSearchReq;
import com.jdkcc.ts.service.dto.request.GradeReq;
import com.jdkcc.ts.service.dto.response.GradeDTO;
import com.jdkcc.ts.service.impl.GradeService;
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
 * 会员心理评分控制器
 * Created by Cherish on 2017/1/6.
 */
@Controller
@RequestMapping("grade")
public class GradeController extends ABaseController {

    private final GradeService gradeService;

    @Autowired
    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/list")
    public ModelAndView list(){
        ModelAndView mv = new ModelAndView("admin/grade/list");
        return mv;
    }

    /**
     * 返回修改页面
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{gradeId}/update")
    public ModelAndView updateForm(@PathVariable("gradeId") Long gradeId){
        ModelAndView mv = new ModelAndView("admin/grade/edit");
        Grade grade = gradeService.findById(gradeId);
        mv.addObject(grade);
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
            Page<GradeDTO> page = gradeService.findAll(basicSearchReq);
            return buildResponse(Boolean.TRUE, basicSearchReq.getDraw(), page);
        } catch (Exception e) {
            log.error("获取列表失败: {}", Throwables.getStackTraceAsString(e));
            return buildResponse(Boolean.FALSE, BUSY_MSG, null);
        }
    }

    /**
     * 删除
     * @param gradeId ID
     * @return JSON
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{gradeId}/delete")
    @ResponseBody
    public MResponse delete(@PathVariable("gradeId") Long gradeId){
        try {
            gradeService.delete(gradeId);
            return buildResponse(Boolean.TRUE, "删除成功", null);
        } catch (Exception e) {
            log.error("删除失败:{}", Throwables.getStackTraceAsString(e));
            return buildResponse(Boolean.FALSE, "删除失败", null);
        }
    }

    /**
     * 更改信息
     * @param gradeReq 更新信息
     * @return ModelAndView
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/update")
    public ModelAndView update(@Validated GradeReq gradeReq, BindingResult bindingResult){
        log.info("【更新信息】{}", gradeReq);
        ModelAndView mv = new ModelAndView("admin/grade/edit");
        Map<String, Object> errorMap = new HashMap<>();
        mv.addObject("errorMap", errorMap);

        if(gradeReq == null || gradeReq.getId() == null){
            errorMap.put("msg", "数据错误");
            return mv;
        }
        if (bindingResult.hasErrors()) {
            errorMap.putAll(getErrors(bindingResult));
            mv.addObject("grade", gradeReq);
        }else {
            try {
                gradeService.update(gradeReq);
                mv.addObject("grade", gradeService.findById(gradeReq.getId()));
                errorMap.put("msg", "修改成功");
            } catch (Exception e) {
                errorMap.put("msg", "系统繁忙");
                log.error("修改错误:{}", Throwables.getStackTraceAsString(e));
            }
        }
        return mv;
    }



}
