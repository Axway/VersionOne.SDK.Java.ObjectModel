package com.versionone.om;

import java.util.Date;

import com.versionone.apiclient.ICookiesManager;

public class CookiesManager implements ICookiesManager {
	private ICookiesManager cookiesManager;

	public CookiesManager(String path, String userName, String password) {
		cookiesManager = com.versionone.apiclient.CookiesManager
				.getCookiesManager(path, userName, password);
	}

	public void addCookie(String name, String value, Date expires) {
		cookiesManager.addCookie(name, value, expires);
	}

	public void deleteCookie(String name) {
		cookiesManager.deleteCookie(name);
	}

	public String getCookie(String name) {
		return cookiesManager.getCookie(name);
	}

	public void deleteAllCookies() {
		cookiesManager.deleteAllCookies();
	}
}
