package com.vector.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vector.domain.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2022-09-22 10:15:02
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeyByUserId(@Param("id") Long id);

    List<Long> selectRoleIdByUserId(Long userId);
}
