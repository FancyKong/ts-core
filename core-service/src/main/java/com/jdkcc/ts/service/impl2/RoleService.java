package com.jdkcc.ts.service.impl2;

import com.jdkcc.ts.common.util.ObjectConvertUtil;
import com.jdkcc.ts.dal.dto.RoleDTO;
import com.jdkcc.ts.dal.entity.Role;
import com.jdkcc.ts.dal.mapper.IBaseDAO;
import com.jdkcc.ts.dal.mapper.RoleDAO;
import com.jdkcc.ts.dal.request.BasicSearchReq;
import com.jdkcc.ts.dal.request.role.RoleSaveReq;
import com.jdkcc.ts.dal.request.role.RoleUpdateReq;
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
public class RoleService extends ABaseService<Role, Long> {

    @Autowired
    private RoleDAO roleDAO;

    @Override
    protected IBaseDAO<Role, Long> getEntityDAO() {
        return roleDAO;
    }

    public Page<RoleDTO> findAll(BasicSearchReq basicSearchReq) {
        Integer start = basicSearchReq.getStartIndex();
        Integer size = basicSearchReq.getPageSize();

        int pageNumber =  start / size + 1;
        PageRequest pageRequest = new PageRequest(pageNumber, size);

        List<Role> roles = roleDAO.findAll(start, size);
        Long count = roleDAO.count();
        Page<Role> rolePage = new PageImpl<>(roles, pageRequest, count);

        return rolePage.map(source -> {
            RoleDTO roleDTO = new RoleDTO();
            ObjectConvertUtil.objectCopy(roleDTO, source);
            return roleDTO;
        });
    }

    @Transactional
    public void updateByReq(RoleUpdateReq roleUpdateReq) {
        Role role = this.findById(roleUpdateReq.getId());
        ObjectConvertUtil.objectCopy(role, roleUpdateReq);
        this.update(role);
    }

    public boolean exist(String name) {
        return roleDAO.findByName(name) != null;
    }

    @Transactional
    public void saveByReq(RoleSaveReq roleSaveReq) {
        Role role = new Role();
        ObjectConvertUtil.objectCopy(role, roleSaveReq);
        this.insert(role);
    }

    public List<Role> listByUserId(Long userId){
        return roleDAO.findByUserId(userId);
    }


}
