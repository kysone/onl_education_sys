package com.zwf.ones.user.api;

import com.alibaba.fastjson.JSON;
import com.zwf.ones.model.user.UserInfo;
import com.zwf.ones.user.service.UserInfoService;
import com.zwf.ones.utils.JwtHelper;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;

@Controller//因为要做页面跳转所以这次用controller不用restcontroller
//跟前端路径保持一致
@RequestMapping("/api/user/wechat")
public class WechatController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private WxMpService wxMpService;

    @Value("${wechat.userInfoUrl}")
    private String userInfoUrl;
    //与前端保持一致
    //授权跳转
    @GetMapping("/authorize")
    //returnUrl与前端保持一致
    public String authorize(@RequestParam("returnUrl") String returnUrl, HttpServletRequest request) {
        String redirectURL = wxMpService.oauth2buildAuthorizationUrl(userInfoUrl,
                WxConsts.OAUTH2_SCOPE_USER_INFO,
                URLEncoder.encode(returnUrl.replace("guiguketan", "#")));//解释在下面
        return "redirect:" + redirectURL;
    }
    //该路径是配置文件里面规定的 # 授权回调获取用户信息接口地址
    //wechat.userInfoUrl: http://20c34ca8.r2.cpolar.top/api/user/wechat/userInfo
    @GetMapping("/userInfo")
    //code微信返回的临时票据  state为授权后跳转的地址
    public String userInfo(@RequestParam("code") String code,
                           @RequestParam("state") String returnUrl) throws Exception {
        //拿到code请求
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = this.wxMpService.oauth2getAccessToken(code);
        //获取openid
        String openId = wxMpOAuth2AccessToken.getOpenId();

        System.out.println("【微信网页授权】openId={}"+openId);
        //获取微信用户信息
        WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
        System.out.println("【微信网页授权】wxMpUser={}"+ JSON.toJSONString(wxMpUser));
        //获取微信用户信息添加到数据库（根据openid是否为空判断要不要添加）
        UserInfo userInfo = userInfoService.getByOpenid(openId);
        if(null == userInfo) {
            //表示为空添加到数据库中
            userInfo = new UserInfo();
            userInfo.setOpenId(openId);
            userInfo.setUnionId(wxMpUser.getUnionId());
            userInfo.setNickName(wxMpUser.getNickname());
            userInfo.setAvatar(wxMpUser.getHeadImgUrl());
            userInfo.setSex(wxMpUser.getSexId());
            userInfo.setProvince(wxMpUser.getProvince());
            userInfoService.save(userInfo);
        }
        //授权完成之后，跳转具体功能页面
        //生成token，按照一定规则生产字符串，可以包含用户信息 jwt实现
        String token = JwtHelper.createToken(userInfo.getId(), userInfo.getNickName());
        if(returnUrl.indexOf("?") == -1) {
            return "redirect:" + returnUrl + "?token=" + token;
        } else {
            return "redirect:" + returnUrl + "&token=" + token;
        }
    }
}
