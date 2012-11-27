package com.versionone.om;

import java.net.URI;

/***
 * Setting for connection to proxy.
 */
public class ProxySettings {

	private final URI address;
	private final String userName;
	private final String password;

	/***
	 * Proxy settings
	 * @param address path and port for proxy.
	 * @param userName user name for proxy.
	 * @param password password for proxy.
	 */
	public ProxySettings(URI address, String userName, String password) {
		this.address = address;
		this.userName = userName;
		this.password = password;
	}

	/***
	 * @return Address for proxy (host and port)
	 */
	public URI getAddress() {
		return address;
	}

	/***
	 * @return User name for proxy.
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @return Password for connection to proxy.
	 */
	public String getPassword() {
		return password;
	}
}
