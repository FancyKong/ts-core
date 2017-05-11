package com.jdkcc.ts.service.impl;

import com.jdkcc.ts.common.enums.ActiveEnum;
import com.jdkcc.ts.common.util.ObjectConvertUtil;
import com.jdkcc.ts.common.util.SHA;
import com.jdkcc.ts.dal.entity.User;
import com.jdkcc.ts.dal.repository.IBaseDAO;
import com.jdkcc.ts.dal.repository.UserDAO;
import com.jdkcc.ts.service.dto.request.BasicSearchReq;
import com.jdkcc.ts.service.dto.request.user.UserSaveReq;
import com.jdkcc.ts.service.dto.request.user.UserSearchReq;
import com.jdkcc.ts.service.dto.request.user.UserUpdateReq;
import com.jdkcc.ts.service.dto.response.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserService extends ABaseService<User, Long> {

    private final UserDAO userDAO;

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    protected IBaseDAO<User, Long> getEntityDAO() {
        return userDAO;
    }

    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public boolean exist(String username) {
        return userDAO.findByUsername(username) != null;
    }

    public Long getCount() {
        return userDAO.count();
    }

    @Transactional(readOnly = false)
    public void delete(Long id) {
        // 并不是真正的删除，只是冻结账户
        User user = findById(id);
        if (user == null) return;

        user.setActive(0);
        this.update(user);
    }

    @Transactional
    public void update(UserUpdateReq userUpdateReq) {
        User user = findById(userUpdateReq.getId());
        if (user == null) return;

        ObjectConvertUtil.objectCopy(user, userUpdateReq);
        user.setModifiedTime(new Date());
        this.update(user);
    }

    @Transactional
    public User save(UserSaveReq userSaveReq) {
        if (exist(userSaveReq.getUsername())) {
            return null;
        }
        User user = new User();
        ObjectConvertUtil.objectCopy(user, userSaveReq);
        user.setCreatedTime(new Date());
        user.setModifiedTime(new Date());
        user.setPassword(SHA.sha1(user.getPassword()));
        return this.save(user);
    }

    public Page<UserDTO> findAll(UserSearchReq userSearchReq, BasicSearchReq basicSearchReq) {

        int pageNumber = basicSearchReq.getStartIndex() / basicSearchReq.getPageSize() + 1;
        PageRequest pageRequest = super.buildPageRequest(pageNumber, basicSearchReq.getPageSize());

        //除了分页条件没有特定搜索条件的，为了缓存count
        if (ObjectConvertUtil.objectFieldIsBlank(userSearchReq)){
            List<User> userList = userDAO.listAllPaged(pageRequest);
            List<UserDTO> userDTOList = userList.stream().map(source -> {
                UserDTO userDTO = new UserDTO();
                ObjectConvertUtil.objectCopy(userDTO, source);
                userDTO.setActiveStr(ActiveEnum.getDesc(source.getActive()));
                return userDTO;
            }).collect(Collectors.toList());

            //为了计算总数使用缓存，加快搜索速度
            Long count = getCount();
            return new PageImpl<>(userDTOList, pageRequest, count);
        }

        //有了其它搜索条件
        Page<User> userPage = super.findAllBySearchParams(
                buildSearchParams(userSearchReq), pageNumber, basicSearchReq.getPageSize());

        return userPage.map(source -> {
            UserDTO userDTO = new UserDTO();
            ObjectConvertUtil.objectCopy(userDTO, source);
            userDTO.setActiveStr(ActiveEnum.getDesc(source.getActive()));
            return userDTO;
        });

    }

}
