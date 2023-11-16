package com.vector.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class RoleMenuTreeSelectVo {


    private List<Long> checkedKeys;
    /**
     * 菜单树
     */
    private List<MenuTreeVo> menus;

}
