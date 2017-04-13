package com.jdkcc.ts.service.impl2;

import com.jdkcc.ts.common.util.ObjectConvertUtil;
import com.jdkcc.ts.dal.dto.PermissionDTO;
import com.jdkcc.ts.dal.entity.Permission;
import com.jdkcc.ts.dal.mapper.IBaseDAO;
import com.jdkcc.ts.dal.mapper.PermissionDAO;
import com.jdkcc.ts.dal.request.BasicSearchReq;
import com.jdkcc.ts.dal.request.permission.PermissionSaveReq;
import com.jdkcc.ts.dal.request.permission.PermissionUpdateReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@Transactional(readOnly = true)
public class PermissionService extends ABaseService<Permission, Long> {

    @Autowired
    private PermissionDAO permissionDAO;

    @Override
    protected IBaseDAO<Permission, Long> getEntityDAO() {
        return permissionDAO;
    }

    public Page<PermissionDTO> findAll(BasicSearchReq basicSearchReq) {
        Integer start = basicSearchReq.getStartIndex();
        Integer size = basicSearchReq.getPageSize();

        int pageNumber =  start / size + 1;
        PageRequest pageRequest = new PageRequest(pageNumber, size);

        List<Permission> list = permissionDAO.findAll(start, size);
        Long count = count();

        Page<Permission> permissionPage = new PageImpl<>(list, pageRequest, count);

        return permissionPage.map(source -> {
            PermissionDTO permissionDTO = new PermissionDTO();
            ObjectConvertUtil.objectCopy(permissionDTO, source);
            return permissionDTO;
        });
    }

    @Transactional
    public void updateByReq(PermissionUpdateReq permissionUpdateReq) {

        Permission permission = this.findById(permissionUpdateReq.getId());
        ObjectConvertUtil.objectCopy(permission, permissionUpdateReq);
        this.update(permission);
    }

    public boolean exist(String permit) {
        return permissionDAO.findByPermit(permit) != null;
    }

    @Transactional
    public void saveByReq(PermissionSaveReq permissionSaveReq) {

        Permission permission = new Permission();
        ObjectConvertUtil.objectCopy(permission, permissionSaveReq);
        this.insert(permission);
    }

    public List<Permission> listByRoleId(Long roleId){
        return permissionDAO.findByRoleId(roleId);
    }



}
