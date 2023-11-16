package com.vector.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @description:
 * @Title: UserInfoVo
 * @Package com.vector.domain.vo
 * @Author 芝士汉堡
 * @Date 2022/9/18 18:33
 */

@Data
@Accessors(chain = true)
public class UserInfoVo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatar;

    private String sex;

    private String email;


}