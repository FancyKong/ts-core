package com.jdkcc.ts.service.impl;

import com.jdkcc.ts.common.util.ObjectConvertUtil;
import com.jdkcc.ts.service.dto.response.RoleDTO;
import com.jdkcc.ts.dal.entity.Role;
import com.jdkcc.ts.dal.mapper.IBaseMapper;
import com.jdkcc.ts.dal.mapper.RoleMapper;
import com.jdkcc.ts.service.dto.request.BasicSearchReq;
import com.jdkcc.ts.service.dto.request.role.RoleSaveReq;
import com.jdkcc.ts.service.dto.request.role.RoleUpdateReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RoleService extends ABaseService<Role, Long> {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    protected IBaseMapper<Role, Long> getEntityDAO() {
        return roleMapper;
    }

    public Page<RoleDTO> findAll(BasicSearchReq basicSearchReq) {
        Integer start = basicSearchReq.getStartIndex();
        Integer size = basicSearchReq.getPageSize();

        int pageNumber =  start / size + 1;
        PageRequest pageRequest = new PageRequest(pageNumber, size);

        List<Role> roles = roleMapper.findAll(start, size);
        Long count = roleMapper.count();
        Page<Role> rolePage = new PageImpl<>(roles, pageRequest, count);

        return rolePage.map(source -> {
            RoleDTO roleDTO = new RoleDTO();
            ObjectConvertUtil.objectCopy(roleDTO, source);
            return roleDTO;
        });
    }

    @Transactional
    public void update(RoleUpdateReq roleUpdateReq) {
        Role role = this.findById(roleUpdateReq.getId());
        ObjectConvertUtil.objectCopy(role, roleUpdateReq);
        this.update(role);
    }

    public boolean exist(String name) {
        return roleMapper.findByName(name) != null;
    }

    @Transactional
    public void insert(RoleSaveReq roleSaveReq) {
        Role role = new Role();
        ObjectConvertUtil.objectCopy(role, roleSaveReq);
        this.insert(role);
    }

    public List<Role> listByUserId(Long userId){
        return roleMapper.findByUserId(userId);
    }


}
