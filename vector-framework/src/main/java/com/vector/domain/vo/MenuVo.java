package com.vector.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.vector.domain.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @description: 菜单权限表(Menu)表实体类
 * @Title: MenuVo
 * @Package com.vector.domain.vo
 * @Author 芝士汉堡
 * @Date 2022/9/23 10:18
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MenuVo {
    //菜单ID
    private Long id;

    //菜单名称
    private String menuName;
    //父菜单ID
    private Long parentId;
    //显示顺序
    private Integer orderNum;
    //路由地址
    private String path;
    //组件路径
    private String component;
    //是否为外链（0是 1否）
    private Integer isFrame;
    //菜单类型（M目录 C菜单 F按钮）
    private String menuType;
    //菜单状态（0显示 1隐藏）
    private String visible;
    //菜单状态（0正常 1停用）
    private String status;
    //权限标识
    private String perms;
    //菜单图标
    private String icon;

    //备注
    private String remark;


    @TableField(exist = false)
    private List<MenuVo> children;

}

