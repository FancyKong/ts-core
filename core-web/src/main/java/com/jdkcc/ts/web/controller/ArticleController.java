package com.jdkcc.ts.web.controller;

import com.google.common.base.Throwables;
import com.jdkcc.ts.dal.entity.Article;
import com.jdkcc.ts.service.dto.MResponse;
import com.jdkcc.ts.service.dto.request.ArticleReq;
import com.jdkcc.ts.service.dto.request.BasicSearchReq;
import com.jdkcc.ts.service.dto.response.ArticleDTO;
import com.jdkcc.ts.service.impl.ArticleService;
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
 * 文章控制器
 * Created by Cherish on 2017/1/6.
 */
@Controller
@RequestMapping("article")
public class ArticleController extends ABaseController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    /**
     * 对外开放的查看文章详情
     * @param articleId 文章ID
     * @return JSON
     */
    @GetMapping("/{articleId}")
    @ResponseBody
    public MResponse findOne(@PathVariable Long articleId){
        try {
            ArticleDTO article = articleService.findOne(articleId);
            return buildResponse(Boolean.TRUE, "查看文章详情", article);
        } catch (Exception e) {
            log.error("获取列表失败:", Throwables.getStackTraceAsString(e));
            return buildResponse(Boolean.FALSE, BUSY_MSG, null);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/list")
    public ModelAndView list(){
        ModelAndView mv = new ModelAndView("admin/article/list");
        return mv;
    }

    /**
     * 返回新增页面
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/add")
    public ModelAndView addForm(){
        ModelAndView mv = new ModelAndView("admin/article/add");
        return mv;
    }

    /**
     * 返回修改信息页面
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{articleId}/update")
    public ModelAndView updateForm(@PathVariable("articleId") Long articleId){
        ModelAndView mv = new ModelAndView("admin/article/edit");
        Article article = articleService.findById(articleId);
        mv.addObject(article);
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
            Page<ArticleDTO> page = articleService.findAll(basicSearchReq);
            return buildResponse(Boolean.TRUE, basicSearchReq.getDraw(), page);
        } catch (Exception e) {
            log.error("获取列表失败: {}", Throwables.getStackTraceAsString(e));
            return buildResponse(Boolean.FALSE, BUSY_MSG, null);
        }
    }

    /**
     * 删除
     * @param articleId ID
     * @return JSON
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{articleId}/delete")
    @ResponseBody
    public MResponse delete(@PathVariable("articleId") Long articleId){
        try {
            articleService.delete(articleId);
            return buildResponse(Boolean.TRUE, "删除成功", null);
        } catch (Exception e) {
            log.error("删除失败:{}", Throwables.getStackTraceAsString(e));
            return buildResponse(Boolean.FALSE, "删除失败", null);
        }
    }

    /**
     * 更改信息
     * @param articleReq 更新信息
     * @return ModelAndView
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/update")
    public ModelAndView update(@Validated ArticleReq articleReq, BindingResult bindingResult){

        ModelAndView mv = new ModelAndView("admin/article/edit");
        Map<String, Object> errorMap = new HashMap<>();
        mv.addObject("errorMap", errorMap);

        if(articleReq == null || articleReq.getId() == null){
            errorMap.put("msg", "数据错误");
            return mv;
        }
        if (bindingResult.hasErrors()) {
            errorMap.putAll(getErrors(bindingResult));
            mv.addObject("article", articleReq);
        }else {
            try {
                articleService.update(articleReq);
                mv.addObject("article", articleService.findById(articleReq.getId()));
                errorMap.put("msg", "修改成功");
            } catch (Exception e) {
                errorMap.put("msg", "系统繁忙");
                log.error("修改错误:{}", Throwables.getStackTraceAsString(e));
            }
        }
        return mv;
    }

    /**
     * 保存新用户
     * @param articleReq 保存的信息
     * @return ModelAndView
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/save")
    public ModelAndView save(@Validated ArticleReq articleReq, BindingResult bindingResult){

        ModelAndView mv = new ModelAndView("admin/article/add");
        Map<String, Object> errorMap = new HashMap<>();
        mv.addObject("errorMap", errorMap);

        if (bindingResult.hasErrors()) {
            errorMap.putAll(getErrors(bindingResult));
            mv.addObject("article", articleReq);
        }else {
            try {
                articleService.save(articleReq);
                errorMap.put("msg", "添加成功");
            } catch (Exception e) {
                errorMap.put("msg", "系统繁忙");
                log.error("添加失败:{}", Throwables.getStackTraceAsString(e));
            }
        }
        return mv;
    }



}
