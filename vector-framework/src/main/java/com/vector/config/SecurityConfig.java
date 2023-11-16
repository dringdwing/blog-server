package com.vector.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @description:
 * @Title: SecurityConfig
 * @Package com.vector.domain.config
 * @Author 芝士汉堡
 * @Date 2022/9/18 17:38
 */
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 重写这个方法，不然会报错
     * @return
     * @throws Exception
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
