package com.jdkcc.ts.service.impl;

import com.jdkcc.ts.common.util.ObjectConvertUtil;
import com.jdkcc.ts.service.dto.response.PermissionDTO;
import com.jdkcc.ts.dal.entity.Permission;
import com.jdkcc.ts.dal.mapper.IBaseMapper;
import com.jdkcc.ts.dal.mapper.PermissionMapper;
import com.jdkcc.ts.service.dto.request.BasicSearchReq;
import com.jdkcc.ts.service.dto.request.permission.PermissionSaveReq;
import com.jdkcc.ts.service.dto.request.permission.PermissionUpdateReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
public class PermissionService extends ABaseService<Permission, Long> {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    protected IBaseMapper<Permission, Long> getEntityDAO() {
        return permissionMapper;
    }

    public Page<PermissionDTO> findAll(BasicSearchReq basicSearchReq) {
        Integer start = basicSearchReq.getStartIndex();
        Integer size = basicSearchReq.getPageSize();

        int pageNumber =  start / size + 1;
        PageRequest pageRequest = new PageRequest(pageNumber, size);

        List<Permission> list = permissionMapper.findAll(start, size);
        Long count = count();

        Page<Permission> permissionPage = new PageImpl<>(list, pageRequest, count);

        return permissionPage.map(source -> {
            PermissionDTO permissionDTO = new PermissionDTO();
            ObjectConvertUtil.objectCopy(permissionDTO, source);
            return permissionDTO;
        });
    }

    @Transactional
    public void update(PermissionUpdateReq permissionUpdateReq) {

        Permission permission = this.findById(permissionUpdateReq.getId());
        ObjectConvertUtil.objectCopy(permission, permissionUpdateReq);
        this.update(permission);
    }

    public boolean exist(String permit) {
        return permissionMapper.findByPermit(permit) != null;
    }

    @Transactional
    public void insert(PermissionSaveReq permissionSaveReq) {

        Permission permission = new Permission();
        ObjectConvertUtil.objectCopy(permission, permissionSaveReq);
        this.insert(permission);
    }

    public List<Permission> listByRoleId(Long roleId){
        return permissionMapper.findByRoleId(roleId);
    }



}
