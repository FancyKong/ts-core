package com.jdkcc.ts.service.impl2;

import com.jdkcc.ts.common.util.ObjectConvertUtil;
import com.jdkcc.ts.common.util.SHA;
import com.jdkcc.ts.dal.dto.UserDTO;
import com.jdkcc.ts.dal.entity.User;
import com.jdkcc.ts.dal.mapper.IBaseDAO;
import com.jdkcc.ts.dal.mapper.UserDAO;
import com.jdkcc.ts.dal.request.BasicSearchReq;
import com.jdkcc.ts.dal.request.user.UserSaveReq;
import com.jdkcc.ts.dal.request.user.UserSearchReq;
import com.jdkcc.ts.dal.request.user.UserUpdateReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Scope("prototype")//Shiro的配置影响到了动态代理，现在就这样吧，可以去看MShiroRealm
@Service
@Transactional(readOnly = true)
public class UserService extends ABaseService<User, Long> implements UserDetailsService {

    @Autowired
    private UserDAO userDAO;

    private static final String UNKNOW = "未知";
    private static final String AC = "激活/在职";
    private static final String UN = "冻结/离职";

    @Override
    protected IBaseDAO<User, Long> getEntityDAO() {
        return userDAO;
    }

    public User findByUsername(String username) {
        log.debug("username_{}没有缓存", username);
        return userDAO.findByUsername(username);
    }

    public boolean exist(String username) {
        return userDAO.findByUsername(username) != null;
    }

    public Long count() {
        log.debug("countAllUser没有缓存");
        return userDAO.count();
    }

    @Transactional(readOnly = false)
    public void delete(Long id) {
        // 并不是真正的删除，只是冻结账户
        User user = findById(id);
        user.setActive(0);
        update(user);
    }

    @Transactional
    public void updateByReq(UserUpdateReq userUpdateReq) {
        User user = findById(userUpdateReq.getId());
        ObjectConvertUtil.objectCopy(user, userUpdateReq);
        user.setModifiedTime(new Date());
        update(user);
    }

    @Transactional
    public void saveByReq(UserSaveReq userSaveReq) {

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

        List<User> users = userDAO.findAll(start, size);
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = this.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(),
                user.getActive() > 0,
                true, true, true,
                AuthorityUtils.createAuthorityList("ROLE_USER"));
    }


}
