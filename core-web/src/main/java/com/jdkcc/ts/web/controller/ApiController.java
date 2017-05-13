package com.jdkcc.ts.web.controller;

import com.google.common.base.Throwables;
import com.jdkcc.ts.dal.entity.WxUser;
import com.jdkcc.ts.service.impl.WxUserService;
import com.jdkcc.ts.web.util.SessionUtil;
import com.jdkcc.ts.wechat.weixin4j.OAuthInfo;
import com.jdkcc.ts.wechat.weixin4j.UserInfo;
import com.jdkcc.ts.wechat.weixin4j.WeixinConfig;
import com.jdkcc.ts.wechat.weixin4j.WeixinUtil;
import com.jdkcc.ts.wechat.weixinjs.Sign;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(value = "/api")
public class ApiController {

    private final WxUserService wxUserService;

    @Autowired
    public ApiController(WxUserService wxUserService) {
        this.wxUserService = wxUserService;
    }

    /**
	 * 提起授权
	 * @param response
	 * @date 2016年8月16日 下午1:33:28
	 */
	@GetMapping(value = "/toAuth")
	public void toAuth(HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		try {
			if (SessionUtil.getWxUser() != null) {
				response.sendRedirect(WeixinConfig.getValue("indexURL"));
			} else {
				response.sendRedirect(WeixinUtil.getLoginUrl());
			}
		} catch (IOException e) {
            log.error("【toAuth】 {}", Throwables.getStackTraceAsString(e));
        }
	}

	/**
	 * 授权回调接口，获取微信用户信息保存到数据库
	 * @param code
	 * @param session
	 * @param response
	 * @date 2016年8月16日 下午1:13:47
	 */
	@GetMapping("/authCallback")
	public void authCallback(String code, HttpSession session, HttpServletResponse response) {
        log.info("【授权回调】 code: {}", code);
        OAuthInfo oAuthInfo = WeixinUtil.getOAuthOpenid(code);
		WxUser wxUser = null;
		try {
			wxUser = wxUserService.findByOpenid(oAuthInfo.getOpenid());
		} catch (NoResultException e) {
			log.info("【授权回调】 openid: {} 没有对应的wxUser数据", oAuthInfo.getOpenid());
			log.error("【授权回调】 {}", Throwables.getStackTraceAsString(e));
		}

		try {
			if (wxUser == null || wxUser.getId() == null) {
				UserInfo userInfo = WeixinUtil.getUserInfo(oAuthInfo.getOpenid(), oAuthInfo.getAccessToken());
				wxUser = new WxUser();
				wxUser.setOpenid(oAuthInfo.getOpenid());
				if (userInfo != null) {
                    wxUser.setProvince(userInfo.getProvince());
                    wxUser.setCountry(userInfo.getCountry());
                    wxUser.setCity(userInfo.getCity());
                    wxUser.setHeadimgurl(userInfo.getHeadimgurl());
                    wxUser.setNickname(userInfo.getNickname());
                    wxUser.setSex(userInfo.getSex());
                    wxUser.setSubscribeTime(new Date());
				}
				log.info("【授权回调】 openid: {} 保存wxUser到数据库", oAuthInfo.getOpenid());
				wxUser = wxUserService.save(wxUser);
			}

			SessionUtil.addWxUser(wxUser);
            SessionUtil.add("nickname", wxUser.getNickname());
            SessionUtil.add("headimgurl", wxUser.getHeadimgurl());

            response.sendRedirect(WeixinConfig.getValue("indexURL"));
		} catch (Exception e) {
			log.error("【授权回调】 {}", Throwables.getStackTraceAsString(e));
		}
	}
	
	/**
	 * 获取JssdkInitData
	 * @return Map<String,String>
	 * @date 2016年8月17日 上午10:31:15
	 */
	@GetMapping(value = "/getJsSdkInitData")
	@ResponseBody
	public Map getJssdkInitData(String url,HttpServletRequest request) {
		Map<String, String> data =  null;

		if(StringUtils.isNotBlank(url))
			data = Sign.sign(request.getServletContext(), url);
		else
			data = Sign.sign(request.getServletContext(), WeixinConfig.getValue("indexURL"));

        log.debug("【getJsSdkInitData】 -> {}", data);
        return data;
	}



}
