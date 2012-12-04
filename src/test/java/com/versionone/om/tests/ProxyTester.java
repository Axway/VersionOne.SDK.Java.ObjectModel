package com.versionone.om.tests;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.versionone.om.Project;
import com.versionone.om.ProxySettings;
import com.versionone.om.V1Instance;

@Ignore("This test requires proxy server.")
public class ProxyTester {

	//private final static String proxyAddress = "http://proxy:3128";
	//private final static String proxyUserName = "user";
	//private final static String proxyPassword = "password";

	//private final static String v1Path = "http://localhost/VersionOneTest/";
	//private final static String v1UserName = "admin";
	//private final static String v1Password = "admin";

	@Test
	public void getProjectListThroughScope() throws URISyntaxException {
		URI proxy = new URI(getProxyUrl());
    	ProxySettings proxySettings = new ProxySettings(proxy, getProxyUsername(), getProxyPassword());
		V1Instance instanceWithProxy = new V1Instance(getV1Url(), getV1Username() , getV1Password(), proxySettings);
		V1Instance instanceWithoutProxy = new V1Instance(getV1Url(), getV1Username() , getV1Password());
		instanceWithProxy.validate();
		instanceWithoutProxy.validate();

		Collection<Project> projectProxy = instanceWithProxy.get().projects(null);
		Collection<Project> projectNoProxy = instanceWithoutProxy.get().projects(null);

		Assert.assertEquals(projectNoProxy.size(), projectProxy.size());
	}
	
	private String stringFromPropertyOrEnvironment(String key){
		String prop = System.getProperty(key);
		if(prop == null)
			prop = System.getenv(key);
		if(prop == null)
			throw new IllegalArgumentException("Must provide a value for test variable " + key);
		return prop;
	}
	
	private String getProxyUrl() {
		return stringFromPropertyOrEnvironment("PROXY_URL");
	}
	
	private String getProxyUsername() {
		return stringFromPropertyOrEnvironment("PROXY_USER");
	}
	
	private String getProxyPassword() {
		return stringFromPropertyOrEnvironment("PROXY_PASS");
	}
	
	private String getV1Url() {
		try {
			return stringFromPropertyOrEnvironment("TEST_URL");
		}
		catch (IllegalArgumentException e) {
			return stringFromPropertyOrEnvironment("test.websiteurl");
		}
	}
	
	private String getV1Username() {
		return stringFromPropertyOrEnvironment("TEST_USER");
	}
	
	private String getV1Password() {
		return stringFromPropertyOrEnvironment("TEST_PASS");
	}

}
