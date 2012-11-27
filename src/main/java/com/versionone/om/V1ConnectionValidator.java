/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import com.versionone.Oid;
import com.versionone.apiclient.*;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

class V1ConnectionValidator {
    private static final String MEMBER_CONNECT_PARAM = "loc.v1/?Member";
    private final String connectionURL;
    private final String username;
    private final String password;
    private final ProxySettings proxySettings;
    private final Map<String, String> customHttpHeaders = new HashMap<String, String>();

    /**
     * Create connection validation object.
     *
     * @param connectionURL URL of VersionOne server.
     * @param username      user name for authentication.
     * @param password      password for authentication.
     */
    public V1ConnectionValidator(String connectionURL, String username,
                                 String password) {
    	this(connectionURL, username, password, null);
    }

    /**
     * Create connection validation object.
     *
     * @param connectionURL	URL of VersionOne server.
     * @param username		user name for authentication.
     * @param password		password for authentication.
     * @param proxySettings	proxy settings for connection.
     */
    public V1ConnectionValidator(String connectionURL, String username,
								String password, ProxySettings proxySettings) {
        this.connectionURL = (connectionURL == null) ? "" : ((!connectionURL
                .endsWith("/")) ? connectionURL + "/" : connectionURL);

        this.username = username;
        this.password = password;
        this.proxySettings = proxySettings;
	}

	/**
     * Http headers list for sending to the VersionOne
     * @return http headers list
     */
    public Map<String, String> getCustomHttpHeaders() {
		return customHttpHeaders;
	}

    /**
     * Running all tests without version verification.
     *
     * @throws ConnectionException if any test is not passed.
     */
    public void test() throws ConnectionException {
        test(null);
    }

    /**
     * Running all tests.
     *
     * @param version version of VersionOne Release.
     * @throws ConnectionException if any test is not passed.
     */
    public void test(String version) throws ConnectionException {

        try {
            checkConnection();
            checkVersion(version);
            checkAuthentication();
        } catch (ConnectionException e) {
            throw e;
        } catch (Exception ex) {
            throw new ConnectionException("Connection Check: "
                    + ex.getMessage(), ex);
        }
    }

    /**
     * Check server connection and authentication.
     *
     * @throws ConnectionException - if no connection.
     */
    public void checkConnection() throws ConnectionException {
        IAPIConnector connector = populateHeaders(new V1APIConnector(getApplicationURL()
                + MEMBER_CONNECT_PARAM, username, password, ApiClientInternals.getProxyProvider(proxySettings)));

        try {
            connector.getData().close();
        } catch (Exception ex) {
            throw new ConnectionException("Application not found at the URL: "
                    + getApplicationURL(), ex);
        }
    }

    /**
     * Checking version of VersionOne.
     *
     * @param version number of version.
     * @throws ConnectionException if version lower than require.
     */
    public void checkVersion(String version) throws ConnectionException {
        MetaModel meta = createMetaModel();
        meta.getAssetType("Member");

        if ((version != null) && (version.length() > 0)
                && ((meta.getVersion() == null) || (meta.getVersion()
                .compareTo(new Version(version)) < 0))) {
            throw new ConnectionException(MessageFormat.format(
                    "VersionOne Release {0} or above is required (found {1}).",
                    version, meta.getVersion()));
        }
    }

    private MetaModel createMetaModel() {
        return new MetaModel(populateHeaders(new V1APIConnector(getApplicationURL()
                + "meta.v1/", username, password, ApiClientInternals.getProxyProvider(proxySettings))), false);
    }

    /**
     * Checking authentication.
     *
     * @throws ConnectionException if user name or password is not correct or
     *                             unable to retrieve logged in member.
     */
    public void checkAuthentication() throws ConnectionException {
        IServices services = new Services(createMetaModel(),
        	populateHeaders(new V1APIConnector(getApplicationURL() + "rest-1.v1/",
                        username, password, ApiClientInternals.getProxyProvider(proxySettings))));
        Oid loggedin;

        try {
            loggedin = services.getLoggedIn();
        } catch (Exception ex) {
            throw new ConnectionException(
                    "Unable to log in. Incorrect username or password.", ex);
        }

        if (loggedin.isNull()) {
            throw new ConnectionException(
                    "Unable to retrieve logged in member.");
        }
    }

    private String getApplicationURL() {
        return connectionURL;
    }

    private V1APIConnector populateHeaders(V1APIConnector connector) {
		for (String key : customHttpHeaders.keySet()) {
			connector.customHttpHeaders.put(key, customHttpHeaders.get(key));
		}

		return connector;
	}
}