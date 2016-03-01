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
package com.adp.marketplace.product.userinfo.core;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.adp.marketplace.connection.configuration.AuthorizationCodeConfiguration;
import com.adp.marketplace.connection.configuration.ClientCredentialsConfiguration;
import com.adp.marketplace.connection.core.ADPAPIConnectionFactory;
import com.adp.marketplace.connection.core.AuthorizationCodeConnection;
import com.adp.marketplace.connection.core.ClientCredentialsConnection;
import com.adp.marketplace.connection.exception.ConnectionException;
import com.adp.marketplace.product.userinfo.exception.APIException;

/**
 * @author tallaprs
 *
 */
public class UserInfoHelperTest {

	/**
	 *  Tests UserInfoHelper instance is created with ClientCredentialsConnection
	 */
	@Test
	public void constructUserInfoHelperWithClientCredentials() {

		try {
		
			ClientCredentialsConfiguration  clientCredentialsConfiguration = new ClientCredentialsConfiguration();
	
			ADPAPIConnectionFactory INSTANCE = ADPAPIConnectionFactory.getInstance();
		
			ClientCredentialsConnection clientCredentialsConnection = 
				(ClientCredentialsConnection) INSTANCE.createConnection(clientCredentialsConfiguration);
			
			UserInfoHelper userInfoHelper = new UserInfoHelper(clientCredentialsConnection);
			assertNotNull(userInfoHelper);
			
		} catch (ConnectionException e) {
			assertTrue(false);
		}
	}
	
	/**
	 *  Tests UserInfoHelper instance is created with AuthorizationCodeConfiguration
	 */
	@Test
	public void constructUserInfoHelperWithAuthCodeConnection() {

		try {
		
			AuthorizationCodeConfiguration authCodeConfiguration = new AuthorizationCodeConfiguration();
	
			ADPAPIConnectionFactory INSTANCE = ADPAPIConnectionFactory.getInstance();
		
			AuthorizationCodeConnection authorizationCodeConnection = 
				(AuthorizationCodeConnection) INSTANCE.createConnection(authCodeConfiguration);
			
			UserInfoHelper userInfoHelper = new UserInfoHelper(authorizationCodeConnection);
			assertNotNull(userInfoHelper);
			
		} catch (ConnectionException e) {
			assertTrue(false);
		}
	}
	
	/**
	 * Test ConnectionException is thrown with passing null connection
	 */
	@Test
	public void connectionIsNull() {
	
		AuthorizationCodeConnection authorizationCodeConnection = null;
		
		try {
		UserInfoHelper userInfoHelper = new UserInfoHelper(authorizationCodeConnection);
		String userInfoResponse = userInfoHelper.getUserInfo();
		assertNull(userInfoResponse);
		
		} catch (Exception e) {
			assertNotNull(e);
			assertTrue(e instanceof APIException);
			assertTrue(e.getCause() instanceof ConnectionException);
			assertTrue(e.getCause().getMessage().contains("ADPAPIConnection is null!"));
		}
		
	}
	
	/**
	 * Test verifies if Client Credentials Connection and throws APIConnection with
	 * ConnectionException as cause and message as Unimplemented method
	 * 
	 */
	@Test
	public void ClientCredentialsConnectionUnimplemented() {
		
		try {
			
			ClientCredentialsConfiguration  clientCredentialsConfiguration = new ClientCredentialsConfiguration();
		
			ADPAPIConnectionFactory INSTANCE = ADPAPIConnectionFactory.getInstance();
	
			ClientCredentialsConnection clientCredentialsConnection = 
					(ClientCredentialsConnection) INSTANCE.createConnection(clientCredentialsConfiguration);
		
			UserInfoHelper userInfoHelper = new UserInfoHelper(clientCredentialsConnection);
			
			String userInfoResponse = userInfoHelper.getUserInfo();
			assertNull(userInfoResponse);
			
		} 	catch (APIException e) {
			assertNotNull(e);
			assertTrue(e instanceof APIException);
			assertTrue(e.getCause() instanceof ConnectionException);
			assertTrue(e.getCause().getMessage().contains("Unimplemented method - UserInfo API access not "
						+ "available for GrantType:ClientCredentials"));
		} 	catch (ConnectionException e) {
			assertNotNull(e);
		}
	}
	
	/**
	 * Test verifies if AuthorizationCode Connection is passed to UserInfoHelper but if 
	 * connection is Not Alive exception is thrown
	 * 
	 */
	@Test 
	public void AuthorizationCodeConnectionIsNotAlive() {
	
		try {
			AuthorizationCodeConfiguration authCodeConfiguration = new AuthorizationCodeConfiguration();
			
			ADPAPIConnectionFactory INSTANCE = ADPAPIConnectionFactory.getInstance();
		
			AuthorizationCodeConnection authorizationCodeConnection = 
				(AuthorizationCodeConnection) INSTANCE.createConnection(authCodeConfiguration);
			
			assertTrue ( authorizationCodeConnection.isConnectionIndicator() == false );
			UserInfoHelper userInfoHelper = new UserInfoHelper(authorizationCodeConnection);
			
			assertNotNull(userInfoHelper);
			String userInfoResponse = userInfoHelper.getUserInfo();
			assertNull(userInfoResponse);
			
		} catch (ConnectionException e) {
			assertTrue(false);
		} catch (Exception e) {
			assertNotNull(e);
			assertTrue(e instanceof APIException);
			assertTrue(e.getCause() instanceof ConnectionException);
			assertTrue(e.getCause().getMessage().contains("Connection Token is expired!"));
		}
	}

}
