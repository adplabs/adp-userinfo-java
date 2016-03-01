/*
	---------------------------------------------------------------------------
	Copyright © 2015-2016 ADP, LLC.   
	
	Licensed under the Apache License, Version 2.0 (the “License”); 
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software 
	distributed under the License is distributed on an “AS IS” BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
	implied.  See the License for the specific language governing 
	permissions and limitations under the License.
	---------------------------------------------------------------------------
*/
package com.adp.marketplace.product.userinfo.utils;

import static org.junit.Assert.*;

import java.util.List;

import org.apache.http.NameValuePair;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.adp.marketplace.connection.configuration.AuthorizationCodeConfiguration;
import com.adp.marketplace.connection.configuration.ClientCredentialsConfiguration;
import com.adp.marketplace.connection.core.ADPAPIConnectionFactory;
import com.adp.marketplace.connection.core.AuthorizationCodeConnection;
import com.adp.marketplace.connection.core.ClientCredentialsConnection;
import com.adp.marketplace.connection.exception.ConnectionException;
import com.adp.marketplace.connection.vo.Token;

/**
 * @author tallaprs
 *
 */
public class UserInfoUtilsTest {

	
	UserInfoUtils INSTANCE = null;
	ADPAPIConnectionFactory connectFactoryInstance = null;
	AuthorizationCodeConfiguration authCodeConfiguration =  null;
	ClientCredentialsConfiguration clientCredentialsConfiguration =  null;
	
	/**
	 * @throws java.lang.Exception Exception thrown if this call fails
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception Exception thrown if this call fails
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception Exception thrown if this call fails
	 */
	@Before
	public void setUp() throws Exception {
		INSTANCE = UserInfoUtils.getInstance();
		connectFactoryInstance = ADPAPIConnectionFactory.getInstance();
		authCodeConfiguration =  new AuthorizationCodeConfiguration();
		clientCredentialsConfiguration = new ClientCredentialsConfiguration();
	}

	/**
	 * @throws java.lang.Exception Exception thrown if this call fails
	 */
	@After
	public void tearDown() throws Exception {
		INSTANCE = null;
		connectFactoryInstance = null;
		authCodeConfiguration =  null;
		clientCredentialsConfiguration =  null;
	}

	/**
	 * 
	 * verifies that a singleton instance is created
	 */
	@Test
	public void singletonInstanceCreated() {
		
		UserInfoUtils anotherInstance = UserInfoUtils.getInstance();
		
		assertNotNull(INSTANCE);
		assertNotNull(anotherInstance);
		
		assertTrue(INSTANCE instanceof UserInfoUtils);
		assertTrue(anotherInstance instanceof UserInfoUtils);
		
		assertSame(INSTANCE, anotherInstance);
		assertEquals(INSTANCE, anotherInstance);
	}

	/**
	 * verifies exception thrown on getNameValuePairs when AuthorizationCodeConnection 
	 * is null
	 */
	@Test 
	public void getNameValuePairsAuthorizationCodeConnectionNull() {
		
		try {
			
			AuthorizationCodeConnection authorizationCodeConnection = null;
			
			List<NameValuePair> nameValuePairs = INSTANCE.getNameValuePairs(authorizationCodeConnection);
			
		} catch (ConnectionException e) {
			assertNotNull(e);
			assertTrue(e instanceof ConnectionException);
			assertTrue(e.getMessage().equals("Connection is Null!"));
		}
	}
	
	/**
	 * verifies getNameValuePairs returns valid values for Authorization Code Connection
	 * 
	 */
	@Test 
	public void getNameValuePairsAuthorizationCodeConnection() {
		
		Token token = null;
		try {
			// build configuration with details
			authCodeConfiguration.setScope("openid");
			authCodeConfiguration.setClientID("5cab3a80-b3fd-415f-955f-4f868596ff43");
			authCodeConfiguration.setClientSecret("4a26db08-2885-4766-b6bb-ad8d0eac7c22");
			
			// below  fields need not be populated for the test here
			//authCodeConfiguration.setAuthorizationCode("authorization_code");
			//authCodeConfiguration.setState(0.0);
			//authCodeConfiguration.setTokenExpiration(0);
			
			//authCodeConfiguration.setApiRequestUrl("https://iat-api.adp.com/core/v1/userinfo");
			//authCodeConfiguration.setBaseAuthorizationUrl("https://iat-accounts.adp.com/auth/oauth/v2/authorize");
			//authCodeConfiguration.setRedirectUrl("http://localhost:8889/marketplace/callback");
			//authCodeConfiguration.setTokenServerUrl("https://iat-accounts.adp.com/auth/oauth/v2/token");
			//authCodeConfiguration.setDisconnectUrl(null);
			
			//authCodeConfiguration.setKeyPassword("adpadp10");
			//authCodeConfiguration.setStorePassword("adpadp10");
			//authCodeConfiguration.setSslCertPath("/src/main/resources/certs/keystore.jks");
			
			// build token
			token = new Token();
			token.setAccess_token("98f34d77-9b1f-490d-84c7-c9333ef929df");
			token.setExpires_in(14400);
			token.setId_token("dhdghdeggwfgmsns");
			token.setRefresh_token("");
			token.setScope("openid");
			token.setToken_type("Bearer");
			
			AuthorizationCodeConnection authorizationCodeConnection = 
					(AuthorizationCodeConnection) connectFactoryInstance.createConnection(authCodeConfiguration);
			
			authorizationCodeConnection.setToken(token);
			
			List<NameValuePair> nameValuePairs = INSTANCE.getNameValuePairs(authorizationCodeConnection);
		
			assertNotNull(nameValuePairs);
			assertTrue(nameValuePairs.size() == 5);
			
			for ( NameValuePair nameValuePair : nameValuePairs ) {
				 String name = nameValuePair.getName();
				 String value = nameValuePair.getValue();
				 
				 assertNotNull(name);
				 assertNotNull(value);
				 
				 assertTrue( name.contains("access_token") || 
						 name.contains("scope") ||
						 name.contains("grant_type") ||
						 name.contains("client_id") ||
						 name.contains("client_secret") );
				 if (  name.contains("scope") ) {
					 assertTrue(value.equals("openid") || value.equals("api") || value.equals("profile") );
				 }
				 if (  name.contains("grant_type") ) {
					 assertTrue(value.equals("authorization_code") );
				 }
			}
			
		} catch (ConnectionException e) {
			assertTrue(false);
		}
	}
	
	/**
	 * verifies exception thrown on getNameValuePairs when Client Credentials Connection 
	 * is null
	 */
	@Test 
	public void getNameValuePairsClientCredentialsConnectionNull() {
		
		Token token = null;
		try {
			
			ClientCredentialsConnection clientCredentialsConnection = null;
			List<NameValuePair> nameValuePairs = INSTANCE.getNameValuePairs(clientCredentialsConnection);
			
		} catch (ConnectionException e) {
			assertNotNull(e);
			assertTrue(e instanceof ConnectionException);
			assertTrue(e.getMessage().equals("Connection is Null!"));
		}
	}
	
	/**
	 * verifies getNameValuePairs returns valid values for Client Credetnials Connection
	 * 
	 */
	@Test 
	public void getNameValuePairsClientCredentialsConnection() {
		
		Token token = null;
		try {
			// build configuration 
			clientCredentialsConfiguration.setClientID("5cab3a80-b3fd-415f-955f-4f868596ff43");
			clientCredentialsConfiguration.setClientSecret("4a26db08-2885-4766-b6bb-ad8d0eac7c22");
			
			// build token
			token = new Token();
			token.setAccess_token("98f34d77-9b1f-490d-84c7-c9333ef929df");
			token.setScope("api");
			
			ClientCredentialsConnection clientCredentialsConnection = 
					(ClientCredentialsConnection) connectFactoryInstance.createConnection(clientCredentialsConfiguration);
			
			clientCredentialsConnection.setToken(token);
			
			List<NameValuePair> nameValuePairs = INSTANCE.getNameValuePairs(clientCredentialsConnection);
		
			assertNotNull(nameValuePairs);
			assertTrue(nameValuePairs.size() == 5);
			
			for ( NameValuePair nameValuePair : nameValuePairs ) {
				 String name = nameValuePair.getName();
				 String value = nameValuePair.getValue();
				 
				 assertNotNull(name);
				 assertNotNull(value);
				
				 assertTrue( name.contains("access_token") || 
						 name.contains("scope") ||
						 name.contains("grant_type") ||
						 name.contains("client_id") ||
						 name.contains("client_secret") );
				 
				 if (  name.contains("grant_type") ) {
					 assertTrue(value.equals("client_credentials") );
				 }
			}
			
		} catch (ConnectionException e) {
			assertTrue(false);
		}
	}
	
}
