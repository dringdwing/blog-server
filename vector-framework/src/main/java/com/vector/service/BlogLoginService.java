package com.vector.service;

import com.vector.domain.ResponseResult;
import com.vector.domain.entity.User;

/**
 * @description:
 * @Title: BlogLoginService
 * @Package com.vector.service
 * @Author 芝士汉堡
 * @Date 2022/9/18 17:30
 */
public interface BlogLoginService {

    ResponseResult logout();

    ResponseResult login(User user);
}
