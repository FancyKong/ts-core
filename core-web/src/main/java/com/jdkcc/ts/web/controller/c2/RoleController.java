package com.jdkcc.ts.web.controller.c2;

import com.jdkcc.ts.dal.MResponse;
import com.jdkcc.ts.dal.dto.RoleDTO;
import com.jdkcc.ts.dal.entity.Role;
import com.jdkcc.ts.dal.request.BasicSearchReq;
import com.jdkcc.ts.dal.request.role.RoleSaveReq;
import com.jdkcc.ts.dal.request.role.RoleUpdateReq;
import com.jdkcc.ts.service.impl2.RoleService;
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
 * Created by Cherish on 2017/1/6.
 */
@Controller
@RequestMapping("role")
public class RoleController extends ABaseController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView("admin/role/list");
        return mv;
    }

    /**
     * 返回新增页面
     */
    @GetMapping("/add")
    public ModelAndView addForm(){
        ModelAndView mv = new ModelAndView("admin/role/add");
        return mv;
    }

    /**
     * 返回修改信息页面
     */
    @GetMapping("/{roleId}/update")
    public ModelAndView updateForm(@PathVariable("roleId") Long roleId){
        ModelAndView mv = new ModelAndView("admin/role/edit");
        Role role = roleService.findById(roleId);
        mv.addObject(role);
        return mv;
    }

    /**
     * 分页查询
     * @param basicSearchReq 基本搜索条件
     * @return JSON
     * @date 2016年8月30日 下午5:30:18
     */
    @GetMapping("/page")
    @ResponseBody
    public MResponse toPage(BasicSearchReq basicSearchReq){

        try {
            Page<RoleDTO> page = roleService.findAll(basicSearchReq);

            return buildResponse(Boolean.TRUE, basicSearchReq.getDraw(), page);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取列表失败: {}", e.getMessage());
            return buildResponse(Boolean.FALSE, BUSY_MSG, null);
        }
    }

    /**
     * 删除
     * @param roleId ID
     * @return JSON
     */
    @DeleteMapping("/{roleId}/delete")
    @ResponseBody
    public MResponse delete(@PathVariable("roleId") Long roleId){

        try {
            roleService.delete(roleId);
            return buildResponse(Boolean.TRUE, "删除成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("删除失败:{}", e.getMessage());
            return buildResponse(Boolean.FALSE, "删除失败", null);
        }
    }

    /**
     * 更改信息
     * @param roleUpdateReq 更新信息
     * @return ModelAndView
     */
    @PostMapping("/update")
    public ModelAndView update(@Validated RoleUpdateReq roleUpdateReq, BindingResult bindingResult){

        ModelAndView mv = new ModelAndView("admin/role/edit");
        Map<String, Object> errorMap = new HashMap<>();
        mv.addObject("errorMap", errorMap);

        if(roleUpdateReq == null || roleUpdateReq.getId() == null){
            errorMap.put("msg", "数据错误");
            return mv;
        }

        if (bindingResult.hasErrors()) {
            errorMap.putAll(getErrors(bindingResult));
            mv.addObject("role", roleUpdateReq);

        }else {
            try {
                roleService.updateByReq(roleUpdateReq);

                mv.addObject("role", roleService.findById(roleUpdateReq.getId()));
                errorMap.put("msg", "修改成功");
            } catch (Exception e) {
                e.printStackTrace();
                errorMap.put("msg", "系统繁忙");
                log.error("修改错误:{}", e.getMessage());
            }
        }

        return mv;
    }

    /**
     * 保存新用户
     * @param roleSaveReq 保存的信息
     * @return ModelAndView
     */
    @PostMapping("/save")
    public ModelAndView save(@Validated RoleSaveReq roleSaveReq, BindingResult bindingResult){

        ModelAndView mv = new ModelAndView("admin/role/add");
        Map<String, Object> errorMap = new HashMap<>();
        mv.addObject("errorMap", errorMap);

        if (bindingResult.hasErrors()) {
            errorMap.putAll(getErrors(bindingResult));
            mv.addObject("role", roleSaveReq);

        }else {
            try {
                if (roleService.exist(roleSaveReq.getName())){
                    errorMap.put("msg", "该角色名已存在，请更换再试");
                    mv.addObject("role", roleSaveReq);
                }else {
                    roleService.saveByReq(roleSaveReq);
                    errorMap.put("msg", "添加成功");
                }

            } catch (Exception e) {
                e.printStackTrace();
                errorMap.put("msg", "系统繁忙");
                log.error("添加失败:{}", e.getMessage());
            }
        }

        return mv;
    }


}
