package com.jdkcc.ts.web.util;


import com.jdkcc.ts.dal.entity.User;
import com.jdkcc.ts.dal.entity.WxUser;

public class SessionUtil {
	
	private static String SESSION_USER = "sessionUser";
	private static String SESSION_WXUSER = "sessionWxUSER";

	public static User getUser() {
		return (User) get(SESSION_USER);
	}

	public static void addUser(User user) {
		add(SESSION_USER, user);
	}

	public static Object get(String attrName) {
		return RequestHolder.getSession().getAttribute(attrName);
	}

	public static void add(String attrName, Object val) {
		RequestHolder.getSession().setAttribute(attrName, val);
	}

    public static WxUser getWxUser() {
        return (WxUser) get(SESSION_WXUSER);
    }

    public static void addWxUser(WxUser wxUser) {
        add(SESSION_WXUSER, wxUser);
    }


}
