package com.jdkcc.ts.web.controller;

import com.jdkcc.ts.dal.entity.User;
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

    @GetMapping
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView("admin/user/list");
        return mv;
    }

    /**
     * 返回新增用户的页面
     */
    @GetMapping("/add")
    public ModelAndView addForm(){
        ModelAndView mv = new ModelAndView("admin/user/add");
        return mv;
    }

    /**
     * 返回修改用户信息的页面
     */
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
    @GetMapping("/page")
    @ResponseBody
    public MResponse toPage(BasicSearchReq basicSearchReq, UserSearchReq userSearchReq){
        try {
            Page<UserDTO> page = userService.findAll(userSearchReq, basicSearchReq);
            return buildResponse(Boolean.TRUE, basicSearchReq.getDraw(), page);
        } catch (Exception e) {
            log.error("获取用户列表失败: {}", e.getMessage());
            return buildResponse(Boolean.FALSE, BUSY_MSG, null);
        }
    }

    /**
     * 删除用户
     * @param userId 用户ID
     * @return JSON
     */
    @DeleteMapping("/{userId}/delete")
    @ResponseBody
    public MResponse delete(@PathVariable("userId") Long userId){
        try {
            userService.delete(userId);
            return buildResponse(Boolean.TRUE, "删除成功", null);
        } catch (Exception e) {
            log.error("删除失败:{}", e.getMessage());
            return buildResponse(Boolean.FALSE, "删除失败", null);
        }
    }

    /**
     * 更改用户信息
     * @param userUpdateReq 更新信息
     * @return ModelAndView
     */
    @PostMapping("/update")
    public ModelAndView update(@Validated UserUpdateReq userUpdateReq, BindingResult bindingResult){

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
        }else {
            try {
                userService.update(userUpdateReq);
                mv.addObject("user", userService.findById(userUpdateReq.getId()));
                errorMap.put("msg", "修改成功");
            } catch (Exception e) {
                errorMap.put("msg", "系统繁忙");
                log.error("修改用户错误:{}", e.getMessage());
            }
        }
        return mv;
    }

    /**
     * 保存新用户
     * @param userSaveReq 保存的信息
     * @return ModelAndView
     */
    @PostMapping("/save")
    public ModelAndView save(@Validated UserSaveReq userSaveReq, BindingResult bindingResult){

        ModelAndView mv = new ModelAndView("admin/user/add");
        Map<String, Object> errorMap = new HashMap<>();
        mv.addObject("errorMap", errorMap);

        if (bindingResult.hasErrors()) {
            errorMap.putAll(getErrors(bindingResult));
            mv.addObject("user", userSaveReq);
        }else {
            try {
                if (userService.exist(userSaveReq.getUsername())){
                    errorMap.put("msg", "该用户名已存在，请更换再试");
                    mv.addObject("user", userSaveReq);
                }else {
                    userService.save(userSaveReq);
                    errorMap.put("msg", "添加成功");
                }
            } catch (Exception e) {
                errorMap.put("msg", "系统繁忙");
                log.error("添加用户失败:{}", e.getMessage());
            }
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

        //表单验证是否通过
        if (bindingResult.hasErrors()) {
            errorMap.putAll(getErrors(bindingResult));
        }else {
            if (StringUtils.isBlank(modifyPasswordReq.getPassword())
                    || StringUtils.isBlank(modifyPasswordReq.getRepeatPassword())
                    || !modifyPasswordReq.getPassword().equals(modifyPasswordReq.getRepeatPassword())){

                errorMap.put("msg", "两次输入的密码不一致");
                return mv;
            }

            try {
                //FIXME 逻辑

            } catch (Exception e) {
                log.error("修改密码失败:{}", e.getMessage());
                errorMap.put("msg", BUSY_MSG);
            }
        }
        return mv;
    }


}
