package com.vector.service.Impl;

import com.vector.domain.ResponseResult;
import com.vector.domain.entity.LoginUser;
import com.vector.domain.entity.User;
import com.vector.domain.vo.BlogUserLoginVo;
import com.vector.domain.vo.UserInfoVo;
import com.vector.service.BlogLoginService;
import com.vector.utils.BeanCopyUtils;
import com.vector.utils.JwtUtil;
import com.vector.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @description:
 * @Title: BlogLoginServiceImpl
 * @Package com.vector.service.Impl
 * @Author 芝士汉堡
 * @Date 2022/9/18 17:31
 */
@SuppressWarnings("all")
@Service
public class BlogLoginServiceImpl implements BlogLoginService {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public ResponseResult logout() {
        // 获取token解析获取userId
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 获取userId
        Long userId = loginUser.getUser().getId();
        // 删除redis中的用户信息
        redisCache.deleteObject("bloglogin:" + userId);
        redisCache.deleteObject("thirdbloglogin:");

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        // 判断是否认证通过
        if (!Optional.ofNullable(authenticate).isPresent()) {
            throw new RuntimeException("用户名或密码错误");
        }
        // 获取userid 生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        // 把用户信息存到redis 后继访问的目标资源需要
        // 获取当前登录人的对象，在登录的时候将信息存redis，这样在认证过滤器就可以根据userId拼装的key获取对应的对象
        redisCache.setCacheObject("bloglogin:" + userId, loginUser);
        // 把token 和 用户信息封装 返回
        // 将User转化为UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);

        BlogUserLoginVo vo = new BlogUserLoginVo(jwt, userInfoVo);
        return ResponseResult.okResult(vo);
    }
}
