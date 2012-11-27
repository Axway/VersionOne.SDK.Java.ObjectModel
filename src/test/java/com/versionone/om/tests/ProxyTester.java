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

	private final static String proxyAddress = "http://proxy:3128";
	private final static String proxyUserName = "user";
	private final static String proxyPassword = "password";

	private final static String v1Path = "http://localhost/VersionOneTest/";
	private final static String v1UserName = "admin";
	private final static String v1Password = "admin";

	@Test
	public void getProjectListThroughScope() throws URISyntaxException {
		URI proxy = new URI(proxyAddress);
    	ProxySettings proxySettings = new ProxySettings(proxy, proxyUserName, proxyPassword);
		V1Instance instanceWithProxy = new V1Instance(v1Path, v1UserName, v1Password, proxySettings);
		V1Instance instanceWithoutProxy = new V1Instance(v1Path, v1UserName, v1Password);
		instanceWithProxy.validate();
		instanceWithoutProxy.validate();

		Collection<Project> projectProxy = instanceWithProxy.get().projects(null);
		Collection<Project> projectNoProxy = instanceWithoutProxy.get().projects(null);

		Assert.assertEquals(projectNoProxy.size(), projectProxy.size());
	}

}
