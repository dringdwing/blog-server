package com.vector.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vector.domain.enums.UserRole;
import com.vector.mapper.UserRoleMapper;
import com.vector.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author makejava
 * @since 2022-09-25 10:44:10
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
