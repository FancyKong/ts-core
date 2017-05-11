package com.jdkcc.ts.service.impl;

import com.jdkcc.ts.dal.entity.User;
import com.jdkcc.ts.dal.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Cherish
 * @version 1.0
 * @date 2017/4/13 14:10
 */
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userDAO.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(),
                user.getActive() > 0,
                true, true, true,
                AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
    }

}
