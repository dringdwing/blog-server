package com.vector.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vector.domain.entity.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2022-09-22 10:08:51
 */
public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(@Param("id") Long id);

    List<Menu> selectRouterMenuTreeByUserId(@Param("userId") Long userId);

    boolean hasChild(Long menuId);

    List<Menu> selectMenuList(Menu menu);

    List<Long> selectMenuListByRoleId(Long roleId);
}
