package com.versionone.om.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.versionone.apiclient.APIException;
import com.versionone.apiclient.ConnectionException;
import com.versionone.apiclient.V1APIConnector;
import com.versionone.om.ApiClientInternals;
import com.versionone.om.AuthenticationException;
import com.versionone.om.V1Instance;

public class ApiClientInternalsTester extends BaseSDKTester {
	private final static int port = 4444;
	private final static String paramName = "test-param";
	private final static String paramValue = "test-value";
	private final static String paramName2 = "test-param 2";
	private final static String paramValue2 = "test-value 2";

	@After
	public void clearCookies() {
		V1Instance connection = new V1Instance(getApplicationPath(), getUsername(), getUsername());
		connection.getCookiesJar().deleteAllCookies();
		connection.getCustomHttpHeaders().clear();
		V1Instance connection2 = new V1Instance(getApplicationPath(), getUsername(), getUsername());
		connection2.getCookiesJar().deleteAllCookies();
		connection.getCustomHttpHeaders().clear();
	}

	@Test
	public void testConnectionsWithCustomsParameters()
			throws ConnectionException, APIException, SecurityException,
			NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException {

		V1Instance connection = new V1Instance(getApplicationPath(), getUsername(), getUsername());
		connection.getCustomHttpHeaders().put(paramName, paramValue);
		ApiClientInternals test = connection.getApiClient();

		Map<String, String> connections = test.getCustomHttpHeaders();

		Assert.assertEquals(1, test.getCustomHttpHeaders().size());
		Assert.assertEquals(paramValue, test.getCustomHttpHeaders().get(
				paramName));

		test.getV1Config();
		Assert.assertEquals(1, connections.size());
		connection.getCustomHttpHeaders().put(paramName2, paramValue2);
		Assert.assertEquals(2, connections.size());
		connection.getCustomHttpHeaders().remove(paramName2);
		Assert.assertEquals(1, connections.size());

		Class<?> classDeligatorDictionary = null;
		Class<?>[] classList = ApiClientInternals.class.getDeclaredClasses();
		for (int i = 0; i < classList.length; i++) {
			if (classList[i].getName().equals(
					"com.versionone.om.ApiClientInternals$DeligatorDictionary")) {
				classDeligatorDictionary = classList[i];
				break;
			}
		}
		if (classDeligatorDictionary == null) {
			Assert
					.fail("Not found ApiClientInternals.DeligatorDictionary class");
		}
		Field connectorsField = classDeligatorDictionary
				.getDeclaredField("connectors");
		connectorsField.setAccessible(true);
		List<V1APIConnector> insideConnection = (List<V1APIConnector>) connectorsField
				.get(connections);

		// exist only config connection
		verifyDataExist(paramName, paramValue, insideConnection);

		// add new connection
		test.getMetaModel();
		Assert.assertEquals(2, insideConnection.size());
		verifyDataExist(paramName, paramValue, insideConnection);

		connection.getCustomHttpHeaders().put(paramName2, paramValue2);
		verifyDataExist(paramName2, paramValue2, insideConnection);

		connection.getCustomHttpHeaders().remove(paramName);
		verifyDataExist(paramName2, paramValue2, insideConnection);

		connection.getCustomHttpHeaders().remove(paramName2);
		// list of params is empty
		try {
			verifyDataExist(paramName2, paramValue2, insideConnection);
			Assert.fail("List must be empty");
		} catch (AssertionError e) {
		}

		// add service connection
		test.getServices();
		Assert.assertEquals(3, insideConnection.size());
		// list of params is empty
		try {
			verifyDataExist(paramName2, paramValue2, insideConnection);
			Assert.fail("List must be still empty");
		} catch (AssertionError e) {
		}

		// call config one more time
		test.getV1Config();
		Assert.assertEquals(3, insideConnection.size());
		connection.getCustomHttpHeaders().put(paramName2, paramValue2);
		verifyDataExist(paramName2, paramValue2, insideConnection);

		test.getCustomHttpHeaders().clear();
		Assert.assertEquals(0, test.getCustomHttpHeaders().size());
		for (V1APIConnector connect : insideConnection) {
			Assert.assertEquals(0, connect.customHttpHeaders.size());
		}
		Assert.assertEquals(3, insideConnection.size());
	}

	private void verifyDataExist(String key, String value,
			List<V1APIConnector> connections) {
		for (V1APIConnector connection : connections) {
			Assert.assertEquals(value, connection.customHttpHeaders.get(key));
		}
	}

	@Test
	public void testWorkingWithCookies() {
		Date expireDate = new Date();
		expireDate.setTime(new Date().getTime() + 1000000);
		String cookieName1 = "cookie_name2";
		String cookieValue1 = "cookie_value2";
		String[] domens = new String[]{"localhost", "127.0.0.1"};
		for(String domen : domens) {
			V1Instance connection = new V1Instance(getApplicationPath(), getUsername(), getUsername());

			try {
				//first and second request.
				//every request SDK makes 2 request to server. first checkConnection and second checkAuthentication.
				//checkAuthentication will throws exception
				connection.validate();
			} catch (AuthenticationException e) {
				// do nothing, we will have here exception during authorization,
				// it's ok
			}
			
			connection.getCookiesJar().addCookie(cookieName1, cookieValue1, expireDate);

			try {
				//third and fourth request.
				//every request SDK makes 2 request to server. first checkConnection and second checkAuthentication.
				//checkAuthentication will throws exception
				connection.validate();
			} catch (AuthenticationException e) {
				// do nothing, we will have here exception during authorization,
				// it's ok
			}
		}
	}

	private void testCookies(Map<String, String> headers, List<String> cookies) {
		String tmpHeadCookies = headers.get("Cookie");
		if (tmpHeadCookies == null && cookies != null) {
			Assert.fail("No cookies");
		} else if (tmpHeadCookies == null && cookies == null) {
			return;
		}

		String[] actualCookies = tmpHeadCookies.split("; ");
		if (actualCookies.length != cookies.size()) {
			Assert.fail("Incorrect numbers of cookies");
		}
		for (String actualCookie : actualCookies) {
			if (!cookies.contains(actualCookie)) {
				Assert.fail("Cookie '" + actualCookie + "' is not expected.");
			}
		}
	}

	@Test
	public void testConnectionsValidateWithCustomsParameters() {

		V1Instance connection = new V1Instance(getApplicationPath(), getUsername(), getUsername());
		connection.getCustomHttpHeaders().put(paramName, paramValue);
		try {
			connection.validate();
		} catch (AuthenticationException e) {
			// TODO: handle exception
		}
	}

	private void runServer(TestServer server) {
		new Thread(server).start();

		// waiting till server is started
		int count = 0;
		while (server.isNotRun && count < 50) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			count++;
		}
		// JIC
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private class TestServer extends Thread {
		public volatile boolean isNotRun = true;
		final String HTTP = "HTTP/1.0 ";
		private final List<String> cookies = new LinkedList<String>();
		private final int port;
		private final int requestNumbers;
		private ServerSocket serverSocket;
		private final List<Map<String, String>> headers;

		public TestServer(int port, List<String> cookies, int requestNumbers) {
			if (cookies != null) {
				this.cookies.addAll(cookies);
			}
			this.requestNumbers = requestNumbers;
			this.port = port;
			headers = new ArrayList<Map<String, String>>(requestNumbers);
		}

		public void addCookies(String name, String value) {
			this.cookies.add(name + "=" + value);
		}

		public void run() {
			serverSocket = null;
			Socket clientSocket = null;
			PrintWriter out = null;
			BufferedReader in = null;
			try {
				serverSocket = new ServerSocket(port);
			} catch (IOException e) {
				System.out.println("Could not listen on port: " + port + ". "
						+ e.getMessage());
				System.exit(-1);
			}

			isNotRun = false;
			try {
				for (int i = 0; i < requestNumbers; i++) {
					clientSocket = null;
					out = null;
					in = null;

					clientSocket = serverSocket.accept();
					out = new PrintWriter(clientSocket.getOutputStream(), true);

					in = new BufferedReader(new InputStreamReader(clientSocket
							.getInputStream()));
					headers.add(getHeaderParams(in));

					// out.println(HTTP + result);
					out.println(HTTP + "200 OK");
					if (cookies != null && cookies.size() > 0) {
						out.println("Set-Cookie:" + getCookies());
					}
					out.println("Content-Type:text/plain");
					out.println("<test>ok</test>");
					out.close();
				}
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			} finally {
				if (out != null) {
					out.close();
				}
				try {
					if (in != null) {
						in.close();
					}
					if (clientSocket != null) {
						clientSocket.close();
					}
				} catch (Exception ex) {
				}

				stopServer();
			}
		}

		public Map<String, String> getHeaders(int request) {
			return headers.get(request);
		}

		public void stopServer() {
			try {
				if (serverSocket != null) {
					serverSocket.close();
				}
			} catch (Exception ex) {
			}
		}

		private String getCookies() {
			String result = "";
			for (String cookie : cookies) {
				result += cookie + "; ";
			}

			if (result.length() > 0) {
				return result.substring(0, result.length() - 2);
			} else {
				return "";
			}
		}

		private Map<String, String> getHeaderParams(BufferedReader in)
				throws IOException {
			Map<String, String> headers = new HashMap<String, String>();
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				if (inputLine.equals("")) {
					break;
				} else if (!inputLine.contains(": ")) {
					continue;
				}
				String[] data = inputLine.split(": ");
				headers.put(data[0], data[1]);
			}
			return headers;
		}
	}
}
