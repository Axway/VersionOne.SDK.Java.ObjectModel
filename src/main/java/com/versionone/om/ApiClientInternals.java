/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.versionone.apiclient.*;

/**
 * Allows access to the underlying API Client structures.
 */
public class ApiClientInternals {
	private static final String CONFIG_PARAM = "config.v1/";
	private static final String ATTACHMENT_PARAM = "attachment.img/";
	private static final String REST_PARAM = "rest-1.v1/";
	private static final String LOC_PARAM = "loc.v1/";
	private static final String META_PARAM = "meta.v1/";
	private final String applicationPath;
	private final String username;
	private final String password;
	private final ProxySettings proxySettings;
	private IMetaModel metaModel;
	private IServices services;
	private ILocalizer loc;
	private IAttachments attachments;
	private IV1Configuration v1Config;
	private final DeligatorDictionary customHttpHeaders = new DeligatorDictionary();

	ApiClientInternals(String applicationPath) {
		this(applicationPath, null, null);
	}

	ApiClientInternals(String applicationPath, String username, String password) {
		this(applicationPath, username, password, null);
	}

	public ApiClientInternals(String applicationPath, String username, String password, ProxySettings proxySettings) {
		this.applicationPath = applicationPath;
		this.username = username;
		this.password = password;
		this.proxySettings = proxySettings;
	}

	String getApplicationPath() {
		return applicationPath;
	}

	/**
	 * @return The underlying MetaModel provided by the API Client.
	 */
	public IMetaModel getMetaModel() {
		if (metaModel == null) {
			V1APIConnector connector = new V1APIConnector(applicationPath
					+ META_PARAM, getProxyProvider(proxySettings));
			customHttpHeaders.add(connector);
			metaModel = new MetaModel(connector);
		}
		return metaModel;
	}

	/**
	 * @return The underlying localizer provided by the API Client.
	 */
	public ILocalizer getLocalizer() {
		if (loc == null) {
			V1APIConnector connector = new V1APIConnector(applicationPath
					+ LOC_PARAM, getProxyProvider(proxySettings));
			customHttpHeaders.add(connector);
			loc = new Localizer(connector);
		}
		return loc;
	}

	/**
	 * @return The underlying Services provided by the API Client.
	 */
	public IServices getServices() {
		if (services == null) {
			V1APIConnector connector = new V1APIConnector(applicationPath
					+ REST_PARAM, username, password, getProxyProvider(proxySettings));
			customHttpHeaders.add(connector);
			services = new Services(getMetaModel(), connector);
		}
		return services;
	}

	/**
	 * @return The underlying Attachments provided by the API Client.
	 */
	public IAttachments getAttachments() {
		if (attachments == null) {
			V1APIConnector connector = new V1APIConnector(applicationPath
					+ ATTACHMENT_PARAM, username, password, getProxyProvider(proxySettings));
			customHttpHeaders.add(connector);
			attachments = new Attachments(connector);
		}
		return attachments;
	}

	/**
	 * @return The underlying V1Config provided by the API Client.
	 */
	public IV1Configuration getV1Config() {
		if (v1Config == null) {
			V1APIConnector connector = new V1APIConnector(applicationPath
					+ CONFIG_PARAM, getProxyProvider(proxySettings));
			customHttpHeaders.add(connector);
			v1Config = new V1Configuration(connector);
		}
		return v1Config;
	}

	/**
	 * Validates server connection and authentication.
	 *
	 * @throws ApplicationUnavailableException if no connection.
	 * @throws AuthenticationException if failed authentication.
	 */
	void validate() throws ApplicationUnavailableException,
			AuthenticationException {
		V1ConnectionValidator validator = new V1ConnectionValidator(
				applicationPath, username, password, proxySettings);

		for (String key : customHttpHeaders.keySet()) {
			validator.getCustomHttpHeaders().put(key,
					customHttpHeaders.get(key));
		}

		try {
			validator.checkConnection();
		} catch (ConnectionException e) {
			throw new ApplicationUnavailableException(
					"Unable to connect to VersionOne.", e);
		}

		try {
			validator.checkAuthentication();
		} catch (ConnectionException e) {
			throw new AuthenticationException("Invalid username or password.",
					e);
		}
	}

    public static ProxyProvider getProxyProvider(ProxySettings proxySettings) {
    	if (proxySettings == null) {
    		return null;
    	}
    	return new ProxyProvider(proxySettings.getAddress(), proxySettings.getUserName(), proxySettings.getPassword());
    }

	/**
	 * Http headers list for sending to the VersionOne
	 *
	 * @return headers parameters
	 */
	public Map<String, String> getCustomHttpHeaders() {
		return customHttpHeaders;
	}

	/**
	 * Cookies manager to work with cookies.
	 *
	 * @return cookies manager.
	 */
	public CookiesManager getCookiesJar() {
		return new CookiesManager(applicationPath, username, password);
	}

	private class DeligatorDictionary extends HashMap<String, String> {
		private List<V1APIConnector> connectors = new ArrayList<V1APIConnector>();

		public void add(V1APIConnector connector) {
			connector.customHttpHeaders.putAll(this);
			connectors.add(connector);
		}

		@Override
		public String put(String key, String value) {
			for (V1APIConnector connector : connectors) {
				connector.customHttpHeaders.put(key, value);
			}
			return super.put(key, value);
		}

		@Override
		public void putAll(Map<? extends String, ? extends String> m) {

			for (String key : m.keySet()) {
				put(key, m.get(key));
			}
			super.putAll(m);
		}

		@Override
		public void clear() {
			for (V1APIConnector connector : connectors) {
				connector.customHttpHeaders.clear();
			}
			super.clear();
		}

		@Override
		public String remove(Object key) {
			for (V1APIConnector connector : connectors) {
				connector.customHttpHeaders.remove(key);
			}
			return super.remove(key);
		}
	}

}
