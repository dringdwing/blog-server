package com.vector.controller;

import com.vector.domain.ResponseResult;
import com.vector.domain.entity.LoginUser;
import com.vector.domain.entity.Menu;
import com.vector.domain.entity.User;
import com.vector.domain.enums.AppHttpCodeEnum;
import com.vector.domain.vo.AdminUserInfoVo;
import com.vector.domain.vo.RoutersVo;
import com.vector.domain.vo.UserInfoVo;
import com.vector.exception.SystemException;
import com.vector.service.LoginService;
import com.vector.service.MenuService;
import com.vector.service.RoleService;
import com.vector.utils.BeanCopyUtils;
import com.vector.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description:
 * @Title: LoginController
 * @Package com.vector.controller
 * @Author 芝士汉堡
 * @Date 2022/9/22 9:19
 */
@RestController
@SuppressWarnings("all")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;


    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user) {
        if (!StringUtils.hasText(user.getUserName())) {
            //提示 必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }

    @PostMapping("/user/logout")
    public ResponseResult logout() {
        return loginService.logout();
    }

    @GetMapping("getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo() {
        // 获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        // 根据用户id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
        // 根据用户id查询角色信息
        List<String> roleKeyList  = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());
        // 获取用户信息
        User user = loginUser.getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);

        // 封装数据返回类型
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms, roleKeyList, userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }


    @GetMapping("getRouters")
    public ResponseResult<RoutersVo> getRouters() {
        Long userId = SecurityUtils.getUserId();
        // 查询结果是tree的形式
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);
        // 封装数据返回

        return ResponseResult.okResult(new RoutersVo(menus));

    }
}