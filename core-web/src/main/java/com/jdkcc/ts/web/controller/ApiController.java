package com.jdkcc.ts.web.controller;

import com.jdkcc.ts.common.weixin4j.WeixinConfig;
import com.jdkcc.ts.common.weixinjs.Sign;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping(value = "/api")
public class ApiController {

/*
	private WeixinUserService weixinUserService;
	private CustomerService customerService;

	*//**
	 * 提起授权
	 * @param response
	 * @date 2016年8月16日 下午1:33:28
	 *//*
	@RequestMapping(value = "/toAuth")
	public void toAuth(HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		try {
			if (SessionUtil.getCustomer() != null) {
				response.sendRedirect(WeixinConfig.getValue("indexURL"));
			} else {
				response.sendRedirect(WeixinUtil.getLoginUrl());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	*//**
	 * 授权回调接口，获取微信用户信息保存到数据库
	 * @param code
	 * @param session
	 * @param response
	 * @date 2016年8月16日 下午1:13:47
	 *//*
	@RequestMapping("/authCallback")
	public void authCallback(String code, HttpSession session, HttpServletResponse response) {
		
		OAuthInfo oAuthInfo = WeixinUtil.getOAuthOpenid(code);
		WeixinUser weixinUser = null;
		try {
			weixinUser = weixinUserService.findByOpenid(oAuthInfo.getOpenid());
		} catch (NoResultException e) {
			log.info("openid:{}第一次登陆本系统,数据库没有对应的weixinUser数据", oAuthInfo.getOpenid());
			e.printStackTrace();
		}

		try {
			if (weixinUser == null || weixinUser.getId() == null) {
				UserInfo userInfo = WeixinUtil.getUserInfo(oAuthInfo.getOpenid(), oAuthInfo.getAccessToken());
				weixinUser = new WeixinUser();
				weixinUser.setOpenid(oAuthInfo.getOpenid());
				if (userInfo != null) {
					weixinUser.setCity(userInfo.getCity());
					weixinUser.setHeadimgurl(userInfo.getHeadimgurl());
					weixinUser.setNickname(userInfo.getNickname());
					weixinUser.setSex(userInfo.getSex().shortValue());
					weixinUser.setSubscribeTime(new Date());
				}
				log.info("openid:{} 保存weixinUser到数据库", oAuthInfo.getOpenid());
				weixinUser = weixinUserService.save(weixinUser);
			}

			SessionUtil.addWeixinUser(weixinUser);

			//如果还没有关联用户的，要去注册一个客户作为绑定
			if (customerService.findByWeixinUserId(weixinUser.getId()) != null) {
				response.sendRedirect(WeixinConfig.getValue("registerURL"));
			} else {
				response.sendRedirect(WeixinConfig.getValue("indexURL"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("openid: {} -> {}", oAuthInfo.getOpenid(), e.getMessage());
		}
	}*/
	
	/**
	 * 获取JssdkInitData
	 * @return Map<String,String>
	 * @date 2016年8月17日 上午10:31:15
	 */
	@RequestMapping(value = "/getJsSdkInitData")
	@ResponseBody
	public Map getJssdkInitData(String url) {
		Map<String, String> data =  null;

		// TODO 该保存全局
		if(StringUtils.isNotBlank(url))
			data = Sign.sign(/*request.getServletContext(),*/ url);
		else
			data = Sign.sign(/*request.getServletContext(), */WeixinConfig.getValue("indexURL"));
		
		return data;
	}



}
