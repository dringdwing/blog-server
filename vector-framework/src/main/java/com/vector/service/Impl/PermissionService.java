package com.vector.service.Impl;

import com.vector.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @Title: PermissionService
 * @Package com.vector.service.Impl
 * @Author 芝士汉堡
 * @Date 2022/9/25 9:22
 */
@Service("ps")
public class PermissionService {

    /**
     * 判断当前用户是否具有permission
     * @param permission 要判断的权限
     * @return
     */
    public boolean hasPermission(String permission) {
        // 若果是超级管理员 直接返回true
        if (SecurityUtils.isAdmin()) {
            return true;
        }
        // 否则获取当前登录用户所具有的权限列表 如何判断是否存在permission
        List<String> permissions = SecurityUtils.getLoginUser().getPermissions();
        return permissions.contains(permission);
    }
}
