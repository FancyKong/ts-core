package com.jdkcc.ts.web.controller;

import com.jdkcc.ts.service.wechat.weixin4j.WeixinConfig;
import com.jdkcc.ts.service.wechat.weixinjs.Sign;
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
	private WxUserService wxUserService;
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
		WxUser wxUser = null;
		try {
			wxUser = wxUserService.findByOpenid(oAuthInfo.getOpenid());
		} catch (NoResultException e) {
			log.info("openid:{}第一次登陆本系统,数据库没有对应的wxUser数据", oAuthInfo.getOpenid());
			e.printStackTrace();
		}

		try {
			if (wxUser == null || wxUser.getId() == null) {
				UserInfo userInfo = WeixinUtil.getUserInfo(oAuthInfo.getOpenid(), oAuthInfo.getAccessToken());
				wxUser = new WxUser();
				wxUser.setOpenid(oAuthInfo.getOpenid());
				if (userInfo != null) {
					wxUser.setCity(userInfo.getCity());
					wxUser.setHeadimgurl(userInfo.getHeadimgurl());
					wxUser.setNickname(userInfo.getNickname());
					wxUser.setSex(userInfo.getSex().shortValue());
					wxUser.setSubscribeTime(new Date());
				}
				log.info("openid:{} 保存wxUser到数据库", oAuthInfo.getOpenid());
				wxUser = wxUserService.insert(wxUser);
			}

			SessionUtil.addWxUser(wxUser);

			//如果还没有关联用户的，要去注册一个客户作为绑定
			if (customerService.findByWxUserId(wxUser.getId()) != null) {
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
