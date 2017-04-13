package com.jdkcc.ts.service.impl;

import com.jdkcc.ts.common.util.ObjectConvertUtil;
import com.jdkcc.ts.common.util.SHA;
import com.jdkcc.ts.dal.entity.User;
import com.jdkcc.ts.dal.mapper.IBaseMapper;
import com.jdkcc.ts.dal.mapper.UserMapper;
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

@Service
@Transactional(readOnly = true)
public class UserService extends ABaseService<User, Long> {

    private final UserMapper userMapper;

    private static final String UNKNOW = "未知";
    private static final String AC = "激活/在职";
    private static final String UN = "冻结/离职";

    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    protected IBaseMapper<User, Long> getEntityDAO() {
        return userMapper;
    }

    public User findByUsername(String username) {
        log.debug("username_{}没有缓存", username);
        return userMapper.findByUsername(username);
    }

    public boolean exist(String username) {
        return userMapper.findByUsername(username) != null;
    }

    public Long count() {
        log.debug("countAllUser没有缓存");
        return userMapper.count();
    }

    @Transactional(readOnly = false)
    public void delete(Long id) {
        // 并不是真正的删除，只是冻结账户
        User user = findById(id);
        user.setActive(0);
        update(user);
    }

    @Transactional
    public void update(UserUpdateReq userUpdateReq) {
        User user = findById(userUpdateReq.getId());
        ObjectConvertUtil.objectCopy(user, userUpdateReq);
        user.setModifiedTime(new Date());
        update(user);
    }

    @Transactional
    public void insert(UserSaveReq userSaveReq) {

        if (exist(userSaveReq.getUsername())) {
            return;
        }

        User user = new User();
        ObjectConvertUtil.objectCopy(user, userSaveReq);
        user.setCreatedTime(new Date());
        user.setModifiedTime(new Date());
        user.setPassword(SHA.sha1(user.getPassword()));
        insert(user);
    }

    public Page<UserDTO> findAll(UserSearchReq userSearchReq, BasicSearchReq basicSearchReq) {
        Integer start = basicSearchReq.getStartIndex();
        Integer size = basicSearchReq.getPageSize();

        int pageNumber =  start / size + 1;
        PageRequest pageRequest = new PageRequest(pageNumber, size);

        List<User> users = userMapper.findAll(start, size);
        Long count = count();

        //有了其它搜索条件
        Page<User> userPage = new PageImpl<>(users, pageRequest, count);

        return userPage.map(source -> {
            UserDTO userDTO = new UserDTO();
            ObjectConvertUtil.objectCopy(userDTO, source);
            userDTO.setActiveStr(source.getActive() == null ? UNKNOW : source.getActive() == 1 ? AC : UN);
            return userDTO;
        });

    }


}
