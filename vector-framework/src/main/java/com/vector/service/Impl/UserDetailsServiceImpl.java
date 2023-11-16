package com.vector.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.vector.constants.SystemConstants;
import com.vector.domain.entity.LoginUser;
import com.vector.domain.entity.User;
import com.vector.mapper.MenuMapper;
import com.vector.mapper.UserMapper;
import com.vector.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @description:
 * @Title: UserDetailsServiceImpl
 * @Package com.vector.service.Impl
 * @Author 芝士汉堡
 * @Date 2022/9/18 17:48
 */
// 注入容器后SpringSecurity使用UserDetailsServiceImpl进行用户校验
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, username);
        User user = userMapper.selectOne(queryWrapper);
        // 判断是否查到用户    没有抛出异常检查
        if (!Optional.ofNullable(user).isPresent()) {
            throw new RuntimeException("用户不存在");
        }
        // 返回用户信息
        // TODO 查询权限信息封装
        // 如果是后台管理用户才需要查询权限信息
        if (user.getType().equals(SystemConstants.ADMAIN)) {
            List<String> list = menuMapper.selectPermsByUserId(user.getId());
            return new LoginUser(user, list);
        }
        return new LoginUser(user, null);
    }
}
