package com.jdkcc.ts.web.controller;

import com.jdkcc.ts.service.dto.MResponse;
import com.jdkcc.ts.service.dto.response.PermissionDTO;
import com.jdkcc.ts.dal.entity.Permission;
import com.jdkcc.ts.service.dto.request.BasicSearchReq;
import com.jdkcc.ts.service.dto.request.permission.PermissionSaveReq;
import com.jdkcc.ts.service.dto.request.permission.PermissionUpdateReq;
import com.jdkcc.ts.service.impl.PermissionService;
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
 *
 * Created by Cherish on 2017/1/6.
 */
@Controller
@RequestMapping("permission")
public class PermissionController extends ABaseController {

    private final PermissionService permissionService;

    @Autowired
    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView("admin/permission/list");
        return mv;
    }

    /**
     * 返回新增页面
     */
    @GetMapping("/add")
    public ModelAndView addForm(){
        ModelAndView mv = new ModelAndView("admin/permission/add");
        return mv;
    }

    /**
     * 返回修改信息页面
     */
    @GetMapping("/{permissionId}/update")
    public ModelAndView updateForm(@PathVariable("permissionId") Long permissionId){
        ModelAndView mv = new ModelAndView("admin/permission/edit");
        Permission permission = permissionService.findById(permissionId);
        mv.addObject(permission);
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
            Page<PermissionDTO> page = permissionService.findAll(basicSearchReq);

            return buildResponse(Boolean.TRUE, basicSearchReq.getDraw(), page);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取列表失败: {}", e.getMessage());
            return buildResponse(Boolean.FALSE, BUSY_MSG, null);
        }
    }

    /**
     * 删除
     * @param permissionId ID
     * @return JSON
     */
    @DeleteMapping("/{permissionId}/delete")
    @ResponseBody
    public MResponse delpermission(@PathVariable("permissionId") Long permissionId){

        try {
            permissionService.delete(permissionId);
            return buildResponse(Boolean.TRUE, "删除成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("删除失败:{}", e.getMessage());
            return buildResponse(Boolean.FALSE, "删除失败", null);
        }
    }

    /**
     * 更改信息
     * @param permissionUpdateReq 更新信息
     * @return ModelAndView
     */
    @PostMapping("/update")
    public ModelAndView updatepermission(@Validated PermissionUpdateReq permissionUpdateReq, BindingResult bindingResult){

        ModelAndView mv = new ModelAndView("admin/permission/edit");
        Map<String, Object> errorMap = new HashMap<>();
        mv.addObject("errorMap", errorMap);

        if(permissionUpdateReq == null || permissionUpdateReq.getId() == null){
            errorMap.put("msg", "数据错误");
            return mv;
        }

        if (bindingResult.hasErrors()) {
            errorMap.putAll(getErrors(bindingResult));
            mv.addObject("permission", permissionUpdateReq);

        }else {
            try {
                permissionService.update(permissionUpdateReq);

                mv.addObject("permission", permissionService.findById(permissionUpdateReq.getId()));
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
     * @param permissionSaveReq 保存的信息
     * @return ModelAndView
     */
    @PostMapping("/save")
    public ModelAndView savepermission(@Validated PermissionSaveReq permissionSaveReq, BindingResult bindingResult){

        ModelAndView mv = new ModelAndView("admin/permission/add");
        Map<String, Object> errorMap = new HashMap<>();
        mv.addObject("errorMap", errorMap);

        if (bindingResult.hasErrors()) {
            errorMap.putAll(getErrors(bindingResult));
            mv.addObject("permission", permissionSaveReq);

        }else {
            try {
                if (permissionService.exist(permissionSaveReq.getPermit())){
                    errorMap.put("msg", "该角色名已存在，请更换再试");
                    mv.addObject("permission", permissionSaveReq);
                }else {
                    permissionService.insert(permissionSaveReq);
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
