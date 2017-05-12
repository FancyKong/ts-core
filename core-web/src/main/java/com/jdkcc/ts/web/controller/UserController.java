package com.jdkcc.ts.web.controller;

import com.google.common.base.Throwables;
import com.jdkcc.ts.dal.entity.User;
import com.jdkcc.ts.security.PasswordUtil;
import com.jdkcc.ts.security.SecurityUser;
import com.jdkcc.ts.service.dto.MResponse;
import com.jdkcc.ts.service.dto.request.BasicSearchReq;
import com.jdkcc.ts.service.dto.request.user.UserModifyPasswordReq;
import com.jdkcc.ts.service.dto.request.user.UserSaveReq;
import com.jdkcc.ts.service.dto.request.user.UserSearchReq;
import com.jdkcc.ts.service.dto.request.user.UserUpdateReq;
import com.jdkcc.ts.service.dto.response.UserDTO;
import com.jdkcc.ts.service.impl.UserService;
import org.apache.commons.lang3.StringUtils;
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
 * 用户控制器
 * Created by Cherish on 2017/1/6.
 */
@Controller
@RequestMapping("user")
public class UserController extends ABaseController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/list")
    public ModelAndView list(){
        ModelAndView mv = new ModelAndView("admin/user/list");
        return mv;
    }

    /**
     * 返回新增用户的页面
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/add")
    public ModelAndView addForm(){
        ModelAndView mv = new ModelAndView("admin/user/add");
        return mv;
    }

    /**
     * 返回修改用户信息的页面
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{userId}/update")
    public ModelAndView updateForm(@PathVariable("userId") Long userId){
        ModelAndView mv = new ModelAndView("admin/user/edit");
        User user = userService.findById(userId);
        mv.addObject("user", user);
        return mv;
    }

    /**
     * 用户修改密码的页面
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/modifyPassword")
    public ModelAndView modifyPassword(){
        ModelAndView mv = new ModelAndView("admin/user/modifyPassword");
        return mv;
    }

    /**
     * 分页查询用户
     * @param basicSearchReq 基本搜索条件
     * @return JSON
     * @date 2016年8月30日 下午5:30:18
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/page")
    @ResponseBody
    public MResponse toPage(BasicSearchReq basicSearchReq, UserSearchReq userSearchReq){
        try {
            Page<UserDTO> page = userService.findAll(userSearchReq, basicSearchReq);
            return buildResponse(Boolean.TRUE, basicSearchReq.getDraw(), page);
        } catch (Exception e) {
            log.error("获取用户列表失败: {}", Throwables.getStackTraceAsString(e));
            return buildResponse(Boolean.FALSE, BUSY_MSG, null);
        }
    }

    /**
     * 删除用户
     * @param userId 用户ID
     * @return JSON
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{userId}/delete")
    @ResponseBody
    public MResponse delete(@PathVariable("userId") Long userId){
        try {
            userService.delete(userId);
            return buildResponse(Boolean.TRUE, "删除成功", null);
        } catch (Exception e) {
            log.error("删除失败:{}", Throwables.getStackTraceAsString(e));
            return buildResponse(Boolean.FALSE, "删除失败", null);
        }
    }

    /**
     * 更改用户信息
     * @param userUpdateReq 更新信息
     * @return ModelAndView
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/update")
    public ModelAndView update(@Validated UserUpdateReq userUpdateReq, BindingResult bindingResult){
        log.info("【更改用户信息】 {}", userUpdateReq);
        ModelAndView mv = new ModelAndView("admin/user/edit");
        Map<String, Object> errorMap = new HashMap<>();
        mv.addObject("errorMap", errorMap);

        if(userUpdateReq == null || userUpdateReq.getId() == null){
            errorMap.put("msg", "数据错误");
            return mv;
        }
        if (bindingResult.hasErrors()) {
            errorMap.putAll(getErrors(bindingResult));
            mv.addObject("user", userUpdateReq);
            return mv;
        }
        try {
            userService.update(userUpdateReq);
            mv.addObject("user", userService.findById(userUpdateReq.getId()));
            errorMap.put("msg", "修改成功");
        } catch (Exception e) {
            errorMap.put("msg", "系统繁忙");
            log.error("修改用户错误:{}", Throwables.getStackTraceAsString(e));
        }
        return mv;
    }

    /**
     * 保存新用户
     * @param userSaveReq 保存的信息
     * @return ModelAndView
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/save")
    public ModelAndView save(@Validated UserSaveReq userSaveReq, BindingResult bindingResult){
        log.info("【保存新用户】 {}", userSaveReq);
        ModelAndView mv = new ModelAndView("admin/user/add");
        Map<String, Object> errorMap = new HashMap<>();
        mv.addObject("errorMap", errorMap);

        if (bindingResult.hasErrors()) {
            errorMap.putAll(getErrors(bindingResult));
            mv.addObject("user", userSaveReq);
            return mv;
        }
        try {
            if (userService.exist(userSaveReq.getUsername())){
                errorMap.put("msg", "该用户名已存在，请更换再试");
                mv.addObject("user", userSaveReq);
            }else {
                // 设置加密的密码
                userSaveReq.setPassword(PasswordUtil.sha1(userSaveReq.getPassword()));
                userService.save(userSaveReq);
                errorMap.put("msg", "添加成功");
            }
        } catch (Exception e) {
            errorMap.put("msg", "系统繁忙");
            log.error("添加用户失败:{}", Throwables.getStackTraceAsString(e));
        }
        return mv;
    }

    /**
     * 提交密码修改请求
     * @param modifyPasswordReq 新旧密码
     * @param bindingResult 表单验证
     * @return ModelAndView
     */
    @PostMapping("/modifyPassword")
    public ModelAndView modifyPassword(@Validated UserModifyPasswordReq modifyPasswordReq, BindingResult bindingResult) {
        log.debug("【修改密码】 {}", modifyPasswordReq);
        ModelAndView mv = new ModelAndView("admin/user/modifyPassword");
        Map<String, Object> errorMap = new HashMap<>();
        mv.addObject("errorMap", errorMap);

        // 表单验证是否通过
        if (bindingResult.hasErrors()) {
            errorMap.putAll(getErrors(bindingResult));
            return mv;
        }
        // 密码是否一致
        if (StringUtils.isBlank(modifyPasswordReq.getOldPassword())
                || StringUtils.isBlank(modifyPasswordReq.getPassword())
                || !StringUtils.equals(modifyPasswordReq.getPassword(),modifyPasswordReq.getRepeatPassword())){
            errorMap.put("msg", "两次输入的密码不一致");
            return mv;
        }

        try {
            User user = userService.findById(SecurityUser.id());
            log.info("【数据库里的用户信息】 {}", user);
            if (!StringUtils.equals(user.getPassword(), PasswordUtil.sha1(modifyPasswordReq.getOldPassword()))) {
                errorMap.put("msg", "原密码不匹配");
                return mv;
            }

            user.setPassword(PasswordUtil.sha1(modifyPasswordReq.getPassword()));
            userService.update(user);
            errorMap.put("msg", "密码修改成功");
        } catch (Exception e) {
            log.error("【修改密码失败】 {}", Throwables.getStackTraceAsString(e));
            errorMap.put("msg", BUSY_MSG);
        }
        return mv;
    }


}
