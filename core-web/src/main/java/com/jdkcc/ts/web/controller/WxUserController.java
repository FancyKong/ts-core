package com.jdkcc.ts.web.controller;

import com.google.common.base.Throwables;
import com.jdkcc.ts.dal.entity.WxUser;
import com.jdkcc.ts.service.dto.MResponse;
import com.jdkcc.ts.service.dto.request.BasicSearchReq;
import com.jdkcc.ts.service.dto.request.wxuser.WxUserSearchReq;
import com.jdkcc.ts.service.dto.response.WxUserDTO;
import com.jdkcc.ts.service.impl.WxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * 微信会员
 * Created by Cherish on 2017/1/6.
 */
@Controller
@RequestMapping("wx_user")
public class WxUserController extends ABaseController {

    private final WxUserService wxUserService;

    @Autowired
    public WxUserController(WxUserService customerService) {
        this.wxUserService = customerService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/list")
    public ModelAndView list(){
        ModelAndView mv = new ModelAndView("admin/wx_user/list");
        return mv;
    }

    /**
     * 返回修改信息页面
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}/update")
    public ModelAndView updateForm(@PathVariable("id") Long id){
        ModelAndView mv = new ModelAndView("admin/wx_user/edit");
        WxUser wxUser = wxUserService.findById(id);
        mv.addObject("wxUser", wxUser);
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
    public MResponse toPage(BasicSearchReq basicSearchReq, WxUserSearchReq wxUserSearchReq){
        try {
            Page<WxUserDTO> page = wxUserService.findAll(basicSearchReq, wxUserSearchReq);
            return buildResponse(Boolean.TRUE, basicSearchReq.getDraw(), page);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取列表失败: {}", Throwables.getStackTraceAsString(e));
            return buildResponse(Boolean.FALSE, BUSY_MSG, null);
        }
    }

    /**
     * 删除
     * @param id ID
     * @return JSON
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}/delete")
    @ResponseBody
    public MResponse delete(@PathVariable("id") Long id){
        try {
            wxUserService.delete(id);
            return buildResponse(Boolean.TRUE, "删除成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("删除失败:{}", Throwables.getStackTraceAsString(e));
            return buildResponse(Boolean.FALSE, "删除失败", null);
        }
    }

}
