package com.vector.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vector.domain.ResponseResult;
import com.vector.domain.entity.Role;

import java.util.List;

/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2022-09-22 10:15:04
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    void insertRole(Role role);

    List<Role> selectRoleAll();

    List<Long> selectRoleIdByUserId(Long userId);

    void updateRole(Role role);

    ResponseResult selectRolePage(Role role, Integer pageNum, Integer pageSize);
}
