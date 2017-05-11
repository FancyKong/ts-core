package com.jdkcc.ts.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 安全框架配置
 * Created by Cherish on 2017/4/13.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();// csrf:Cross-site doQuery forgery跨站请求伪造
        http
            .authorizeRequests()
                .antMatchers("/","/login","/logout","/validateCode","/**/favicon.ico",
                        "/static/**", "/css/**", "/js/**", "/images/**", "/tools/**",
                        "/api/**","/imageDownload*").permitAll()//允许所有
                .anyRequest().hasRole("ADMIN")
                .and()
            .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/article")
                .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll();
    }

    /*@Bean
    UserDetailsService customUserService() {
        return new UserDetailsServiceImpl();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserService());
    }*/

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
            .withUser("cherish").password("cherish").roles("ADMIN")
            .and()
            .withUser("fancykong").password("fancykong").roles("ADMIN");
    }



}
