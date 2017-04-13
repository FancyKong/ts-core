package com.jdkcc.ts.service.impl2;

import com.jdkcc.ts.common.util.ObjectConvertUtil;
import com.jdkcc.ts.dal.dto.RoleDTO;
import com.jdkcc.ts.dal.dto.su.SuperPermissionDTO;
import com.jdkcc.ts.dal.dto.su.SuperRoleDTO;
import com.jdkcc.ts.dal.dto.su.SuperRolePermissionDTO;
import com.jdkcc.ts.dal.dto.su.SuperUserRoleDTO;
import com.jdkcc.ts.dal.entity.Permission;
import com.jdkcc.ts.dal.entity.Role;
import com.jdkcc.ts.dal.entity.User;
import com.jdkcc.ts.dal.mapper.PermissionDAO;
import com.jdkcc.ts.dal.mapper.RoleDAO;
import com.jdkcc.ts.dal.mapper.UserDAO;
import com.jdkcc.ts.dal.request.su.SuperRolePermissionReq;
import com.jdkcc.ts.dal.request.su.SuperUserRoleReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class SuperService {

    @Autowired
    private RoleDAO roleDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private PermissionDAO permissionDAO;

    public List<RoleDTO> listAllRole() {
        return roleDAO.findAll(0, Integer.MAX_VALUE)
                .stream().map(role -> {
            return ObjectConvertUtil.objectCopy(new RoleDTO(), role);
        }).collect(Collectors.toList());
    }

    public SuperUserRoleDTO findByUsername(String username) {
        User user = userDAO.findByUsername(username);
        if (user == null) return null;

        SuperUserRoleDTO superUserRoleDTO = new SuperUserRoleDTO();
        List<Role> userRoles = roleDAO.findByUserId(user.getId());
        Set<Long> collect = userRoles.stream().map(Role::getId).collect(Collectors.toSet());
        List<Role> all = roleDAO.findAll(0, Integer.MAX_VALUE);

        List<SuperRoleDTO> superRoleDTOS = all.stream().map(role -> {
            SuperRoleDTO superRoleDTO = new SuperRoleDTO();
            ObjectConvertUtil.objectCopy(superRoleDTO, role);
            if (collect.contains(role.getId())) {
                superRoleDTO.setHave(Boolean.TRUE);
            }else {
                superRoleDTO.setHave(Boolean.FALSE);
            }
            return superRoleDTO;
        }).collect(Collectors.toList());

        superUserRoleDTO.setUsername(username);
        superUserRoleDTO.setRoles(superRoleDTOS);
        return superUserRoleDTO;
    }

    @Transactional
    public void updateUserRole(SuperUserRoleReq superUserRoleReq) {

        String username = superUserRoleReq.getUsername();
        List<Long> roleIds = superUserRoleReq.getRoleIds();

        User user = userDAO.findByUsername(username);
        if (user != null) {
            List<Role> roles = roleIds.stream().map(roleId -> {
                Role role = new Role();
                role.setId(roleId);
                return role;
            }).collect(Collectors.toList());

            userDAO.update(user);
            //TODO user.setRoles(roles);
        }
    }

    public SuperRolePermissionDTO findByRolename(String rolename) {
        Role byName = roleDAO.findByName(rolename);
        if (byName == null) return null;

        List<Permission> rolePermissions = permissionDAO.findByRoleId(byName.getId());
        if (rolePermissions == null) return null;

        Set<Long> collect = rolePermissions.stream().map(Permission::getId).collect(Collectors.toSet());

        List<Permission> all = permissionDAO.findAll(0, Integer.MAX_VALUE);
        SuperRolePermissionDTO superRolePermissionDTO = new SuperRolePermissionDTO();
        List<SuperPermissionDTO> permissionDTOs = all.stream().map(permission -> {
            SuperPermissionDTO dto = new SuperPermissionDTO();
            ObjectConvertUtil.objectCopy(dto, permission);
            if (collect.contains(permission.getId())) {
                dto.setHave(Boolean.TRUE);
            }else {
                dto.setHave(Boolean.FALSE);
            }
            return dto;
        }).collect(Collectors.toList());

        superRolePermissionDTO.setRolename(rolename);
        superRolePermissionDTO.setPermissions(permissionDTOs);

        return superRolePermissionDTO;
    }

    @Transactional
    public void updateRolePermission(SuperRolePermissionReq superRolePermissionReq) {
        String rolename = superRolePermissionReq.getRolename();
        List<Long> permissionIds = superRolePermissionReq.getPermissionIds();

        Role role = roleDAO.findByName(rolename);
        if (role != null) {
            List<Permission> permissions = permissionIds.stream().map(pId -> {
                Permission permission = new Permission();
                permission.setId(pId);
                return permission;
            }).collect(Collectors.toList());

            roleDAO.update(role);
            //TODO role.setPermissions(permissions);
        }
    }


}
