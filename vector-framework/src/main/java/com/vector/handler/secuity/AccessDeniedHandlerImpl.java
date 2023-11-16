package com.vector.handler.secuity;

import com.alibaba.fastjson.JSON;
import com.vector.domain.ResponseResult;
import com.vector.domain.enums.AppHttpCodeEnum;
import com.vector.utils.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @description: 授权失败处理器
 * @Title: AccessDeniedHandlerImpl
 * @Package com.vector.handler.secuity
 * @Author 芝士汉堡
 * @Date 2022/9/19 7:44
 */
@SuppressWarnings("all")
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        accessDeniedException.printStackTrace();
        ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
        //响应给前端
        WebUtils.renderString(response, JSON.toJSONString(result));
    }
}