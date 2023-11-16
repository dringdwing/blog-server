package com.vector.auth.controller;

import com.alibaba.fastjson.JSONObject;
import com.vector.auth.cache.AuthStateRedisCache;
import com.vector.domain.ResponseResult;
import com.vector.domain.entity.User;
import com.vector.domain.vo.UserInfoVo;
import com.vector.service.UserService;
import com.vector.utils.BeanCopyUtils;
import com.vector.utils.JwtUtil;
import com.vector.utils.RedisCache;
import com.xkcoding.http.config.HttpConfig;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.enums.scope.*;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.*;
import me.zhyd.oauth.utils.AuthScopeUtils;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2019/2/19 9:28
 * @since 1.8
 */
@Slf4j
@Controller
@RequestMapping("/oauth")
public class RestAuthController {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthStateRedisCache stateRedisCache;
    @Autowired
    private UserService userService;
    //本地授权成功重定向地址
    private String url = "http://localhost:8080";

    @RequestMapping("/render/{source}")
    @ResponseBody
    public ResponseResult renderAuth(@PathVariable("source") String source, HttpServletResponse response) throws IOException {
        log.info("进入render：" + source);
        AuthRequest authRequest = getAuthRequest(source);
        String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
        log.info(authorizeUrl);
        return ResponseResult.okResult(authorizeUrl);
    }

    /**
     * oauth平台中配置的授权回调地址，以本项目为例，在创建github授权应用时的回调地址应为：http://127.0.0.1:8443/oauth/callback/github
     */
    @RequestMapping("/callback/{source}")
    public void login(@PathVariable("source") String source, AuthCallback callback, HttpServletRequest request, HttpServletResponse res) throws IOException {
        log.info("进入callback：" + source + " callback params:" + JSONObject.toJSONString(callback));
        AuthRequest authRequest = getAuthRequest(source);
        AuthResponse<AuthUser> response = authRequest.login(callback);
        log.info(JSONObject.toJSONString(response));
        if (response.ok()) {
            AuthUser loginUser = response.getData();
            AuthToken token = loginUser.getToken();

            String jwt = JwtUtil.createJWT(loginUser.getUuid());
            System.out.println("jwt:" + jwt);
            // 把用户信息存到redis 后继访问的目标资源需要
            // 获取当前登录人的对象，在登录的时候将信息存redis，这样在认证过滤器就可以根据userId拼装的key获取对应的对象
            redisCache.setCacheObject("thirdbloglogin:", loginUser);
            // 把token 和 用户信息封装 返回
            // 将User转化为UserInfoVo
            User userInfo = new User();
            userInfo.setId((long) Integer.parseInt(loginUser.getUuid()));
            userInfo.setNickName(loginUser.getUsername());
            userInfo.setAvatar(loginUser.getAvatar());
            userInfo.setEmail(loginUser.getEmail());
            String encodePassword = passwordEncoder.encode(loginUser.getNickname());
            userInfo.setPassword(encodePassword);
            System.out.println("userInfouserInfouserInfouserInfouserInfouserInfo" + userInfo);
            //存入数据库
            boolean flag = userService.save(userInfo);
            UserInfoVo userInfoVo = null;
            if (flag) {
                userInfoVo = BeanCopyUtils.copyBean(userInfo, UserInfoVo.class);
            }
            // 将token和用户信息封装返回
            res.sendRedirect(url + "?token=" + jwt + "&userInfo=" + JSONObject.toJSONString(userInfoVo));
        }

    }

//    @RequestMapping("/revoke/{source}/{uuid}")
//    @ResponseBody
//    public ResponseResult revokeAuth(@PathVariable("source") String source, @PathVariable("uuid") String uuid) throws IOException {
//        AuthRequest authRequest = getAuthRequest(source.toLowerCase());
//
//        AuthUser user = userService.getByUuid(uuid);
//        if (null == user) {
//            return ResponseResult.errorResult(AppHttpCodeEnum.USERNAME_EXIST);
//        }
//        AuthResponse<AuthToken> response = null;
//        try {
//            response = authRequest.revoke(user.getToken());
//            if (response.ok()) {
//                userService.remove(user.getUuid());
//                return Response.success("用户 [" + user.getUsername() + "] 的 授权状态 已收回！");
//            }
//            return Response.error("用户 [" + user.getUsername() + "] 的 授权状态 收回失败！" + response.getMsg());
//        } catch (AuthException e) {
//            return Response.error(e.getErrorMsg());
//        }
//    }

//    @RequestMapping("/refresh/{source}/{uuid}")
//    @ResponseBody
//    public Object refreshAuth(@PathVariable("source") String source, @PathVariable("uuid") String uuid) {
//        AuthRequest authRequest = getAuthRequest(source.toLowerCase());
//
//        AuthUser user = userService.getByUuid(uuid);
//        if (null == user) {
//            return Response.error("用户不存在");
//        }
//        AuthResponse<AuthToken> response = null;
//        try {
//            response = authRequest.refresh(user.getToken());
//            if (response.ok()) {
//                user.setToken(response.getData());
//                userService.save(user);
//                return Response.success("用户 [" + user.getUsername() + "] 的 access token 已刷新！新的 accessToken: " + response.getData().getAccessToken());
//            }
//            return Response.error("用户 [" + user.getUsername() + "] 的 access token 刷新失败！" + response.getMsg());
//        } catch (AuthException e) {
//            return Response.error(e.getErrorMsg());
//        }
//    }

    /**
     * 根据具体的授权来源，获取授权请求工具类
     *
     * @param source
     * @return
     */
    private AuthRequest getAuthRequest(String source) {
        AuthRequest authRequest = null;
        switch (source.toLowerCase()) {
            case "dingtalk":
                authRequest = new AuthDingTalkRequest(AuthConfig.builder()
                        .clientId("dingcujhslmuqbezg3ow")
                        .clientSecret("qhoI6uphWyLw4xGeSA46mCFc7iL0LZ788B2iLYhtmRaJfatn5DcD8uar3kMAr6Mu")
                        .redirectUri("http://localhost:7777/oauth/callback/dingtalk")
                        .build());
                break;
            case "github":
                authRequest = new AuthGithubRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://localhost:8443/oauth/callback/github")
                        .scopes(AuthScopeUtils.getScopes(AuthGithubScope.values()))
                        // 针对国外平台配置代理
                        .httpConfig(HttpConfig.builder()
                                .timeout(15000)
                                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 10080)))
                                .build())
                        .build(), stateRedisCache);
                break;
            case "gitee":
                authRequest = new AuthGiteeRequest(AuthConfig.builder()
                        .clientId("13f45a461875d5050a8ea383a9cf88b55c2e85c9c8b2eaaefd13072b9fbc032f")
                        .clientSecret("3d35e94a501143206e4af09a3287c51f64e99b5a670cbf8cc76f4a42be0bd03a")
                        .redirectUri("http://localhost:7777/oauth/callback/gitee")
                        .scopes(AuthScopeUtils.getScopes(AuthGiteeScope.values()))
                        .build(), stateRedisCache);
                break;
            case "weibo":
                authRequest = new AuthWeiboRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://dblog-web.zhyd.me/oauth/callback/weibo")
                        .scopes(Arrays.asList(
                                AuthWeiboScope.EMAIL.getScope(),
                                AuthWeiboScope.FRIENDSHIPS_GROUPS_READ.getScope(),
                                AuthWeiboScope.STATUSES_TO_ME_READ.getScope()
                        ))
                        .build());
                break;
            case "coding":
                authRequest = new AuthCodingRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://dblog-web.zhyd.me/oauth/callback/coding")
                        .domainPrefix("")
                        .scopes(Arrays.asList(
                                AuthCodingScope.USER.getScope(),
                                AuthCodingScope.USER_EMAIL.getScope(),
                                AuthCodingScope.USER_PHONE.getScope()
                        ))
                        .build());
                break;
            case "oschina":
                authRequest = new AuthOschinaRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://localhost:8443/oauth/callback/oschina")
                        .build());
                break;
            case "alipay":
                // 支付宝在创建回调地址时，不允许使用localhost或者127.0.0.1，所以这儿的回调地址使用的局域网内的ip
                authRequest = new AuthAlipayRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .alipayPublicKey("")
                        .redirectUri("https://www.zhyd.me/oauth/callback/alipay")
                        .build());
                break;
            case "qq":
                authRequest = new AuthQqRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://localhost:8443/oauth/callback/qq")
                        .build());
                break;
            case "csdn":
                authRequest = new AuthCsdnRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://dblog-web.zhyd.me/oauth/callback/csdn")
                        .build());
                break;
            case "taobao":
                authRequest = new AuthTaobaoRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://dblog-web.zhyd.me/oauth/callback/taobao")
                        .build());
                break;
            case "google":
                authRequest = new AuthGoogleRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://localhost:8443/oauth/callback/google")
                        .scopes(AuthScopeUtils.getScopes(AuthGoogleScope.USER_EMAIL, AuthGoogleScope.USER_PROFILE, AuthGoogleScope.USER_OPENID))
                        // 针对国外平台配置代理
                        .httpConfig(HttpConfig.builder()
                                .timeout(15000)
                                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 10080)))
                                .build())
                        .build());
                break;
            case "douyin":
                authRequest = new AuthDouyinRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://dblog-web.zhyd.me/oauth/callback/douyin")
                        .build());
                break;
            case "microsoft":
                authRequest = new AuthMicrosoftRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://localhost:8443/oauth/callback/microsoft")
                        .scopes(Arrays.asList(
                                AuthMicrosoftScope.USER_READ.getScope(),
                                AuthMicrosoftScope.USER_READWRITE.getScope(),
                                AuthMicrosoftScope.USER_READBASIC_ALL.getScope(),
                                AuthMicrosoftScope.USER_READ_ALL.getScope(),
                                AuthMicrosoftScope.USER_READWRITE_ALL.getScope(),
                                AuthMicrosoftScope.USER_INVITE_ALL.getScope(),
                                AuthMicrosoftScope.USER_EXPORT_ALL.getScope(),
                                AuthMicrosoftScope.USER_MANAGEIDENTITIES_ALL.getScope(),
                                AuthMicrosoftScope.FILES_READ.getScope()
                        ))
                        .build());
                break;
            case "mi":
                authRequest = new AuthMiRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://dblog-web.zhyd.me/oauth/callback/mi")
                        .build());
                break;
            case "toutiao":
                authRequest = new AuthToutiaoRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://dblog-web.zhyd.me/oauth/callback/toutiao")
                        .build());
                break;
            case "pinterest":
                authRequest = new AuthPinterestRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("https://eadmin.innodev.com.cn/oauth/callback/pinterest")
                        // 针对国外平台配置代理
                        .httpConfig(HttpConfig.builder()
                                .timeout(15000)
                                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 10080)))
                                .build())
                        .build());
                break;
            case "renren":
                authRequest = new AuthRenrenRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://127.0.0.1:8443/oauth/callback/teambition")
                        .build());
                break;
            case "huawei":
                authRequest = new AuthHuaweiRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://127.0.0.1:8443/oauth/callback/huawei")
                        .scopes(Arrays.asList(
                                AuthHuaweiScope.BASE_PROFILE.getScope(),
                                AuthHuaweiScope.MOBILE_NUMBER.getScope(),
                                AuthHuaweiScope.ACCOUNTLIST.getScope(),
                                AuthHuaweiScope.SCOPE_DRIVE_FILE.getScope(),
                                AuthHuaweiScope.SCOPE_DRIVE_APPDATA.getScope()
                        ))
                        .build());
                break;
            case "gitlab":
                authRequest = new AuthGitlabRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://localhost:8443/oauth/callback/gitlab")
                        .scopes(AuthScopeUtils.getScopes(AuthGitlabScope.values()))
                        .build());
                break;

            case "wechat_mp":
                authRequest = new AuthWeChatMpRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("")
                        .build());
                break;
            case "aliyun":
                authRequest = new AuthAliyunRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://localhost:8443/oauth/callback/aliyun")
                        .build());
                break;
            default:
                break;
        }
        if (null == authRequest) {
            throw new AuthException("未获取到有效的Auth配置");
        }
        return authRequest;
    }
}
