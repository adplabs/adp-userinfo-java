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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.adp.marketplace.connection.configuration.AuthorizationCodeConfiguration;
import com.adp.marketplace.connection.configuration.ClientCredentialsConfiguration;
import com.adp.marketplace.connection.constants.Constants;
import com.adp.marketplace.connection.core.ADPAPIConnection;
import com.adp.marketplace.connection.core.AuthorizationCodeConnection;
import com.adp.marketplace.connection.core.ClientCredentialsConnection;
import com.adp.marketplace.connection.exception.ConnectionException;
import com.adp.marketplace.connection.utils.ConnectionUtils;
import com.adp.marketplace.connection.vo.Token;


/**
 * <p>
 * UserInfoUtils is a singleton utility class that provides convenience 
 * methods to build request and process response of connections
 * <p>
 * In case the connection or configuration is null or empty or invalid 
 * {@link ConnectionException} is thrown.
 * @author tallaprs
 *
 */
public class UserInfoUtils {
	
	private static UserInfoUtils INSTANCE = null;
	
	private static final Logger LOGGER = Logger.getLogger(UserInfoUtils.class.getName());
	
	/**
	 * constructor
	 */
	private UserInfoUtils() {}
	
	
	/**
	 * this method returns a singleton instance of UserInfoUtils class
	 * @return UserInfoUtils
	 */
	public static UserInfoUtils getInstance() {
		
		if ( INSTANCE == null ) {		
			synchronized (UserInfoUtils.class) {
	            if ( INSTANCE == null ) {
	                INSTANCE = new UserInfoUtils();
	            }
	        }
	    }
		
	    return INSTANCE;
	}
	
	/**
	 * Returns a list of name value pairs required to execute HttpRequest. 
	 * The list may vary for each type of authorization scheme
	 * 
	 * @param connection			 Connection object
	 * @return List					 List of Parameters to be mapped 
	 * @throws ConnectionException   ConnectionException in case of null or 
	 *                               other error conditions
	 */
	public List<NameValuePair> getNameValuePairs(ADPAPIConnection connection) 
			throws ConnectionException {
		
		List<NameValuePair> nameValuePairs = null;
		
		if ( connection == null ) {
			throw new ConnectionException("Connection is Null!");
		}
		
		if ( connection instanceof AuthorizationCodeConnection ) {
			nameValuePairs =  getNameValuePairs((AuthorizationCodeConnection) connection);
		} else if ( connection instanceof ClientCredentialsConnection ) {
			nameValuePairs =  getNameValuePairs((ClientCredentialsConnection) connection);
		}
		
		return nameValuePairs;
	}
	
	/**
	 * Returns a list of name value pairs required for HttpRequest 
	 * for AuthorizationCode Connection
	 * 
	 * @param connection
	 * @return
	 * @throws ConnectionException
	 */
	private List<NameValuePair> getNameValuePairs(AuthorizationCodeConnection 
			connection) throws ConnectionException {

		String scope = null;	
		List<NameValuePair> nameValuePairs = null;
		
		if ( connection == null ) {
			throw new ConnectionException("Connection is Null!");
		}
	
		Token token = connection.getToken();
		if ( token != null) { 
			
			AuthorizationCodeConfiguration configuration = 
				(AuthorizationCodeConfiguration) connection.getConnectionConfiguration();
		
			scope = configuration.getScope();
			
			nameValuePairs = new ArrayList<NameValuePair>();
			
			nameValuePairs.add(new BasicNameValuePair(Constants.ACCESS_TOKEN, token.getAccess_token()));				
			nameValuePairs.add(new BasicNameValuePair(Constants.SCOPE, scope));
			nameValuePairs.addAll(ConnectionUtils.getInstance().getClientCredentials(configuration));
		}
		
		return nameValuePairs;
	}
	
	/**
	 * Returns a list of name value pairs required for HttpRequest 
	 * for Client Credentials Connection
	 * 
	 * @param connection
	 * @return
	 * @throws ConnectionException
	 */
	private List<NameValuePair> getNameValuePairs(ClientCredentialsConnection connection) 
			throws ConnectionException {

		List<NameValuePair> nameValuePairs = null;
		
		if ( connection == null ) {
			throw new ConnectionException("Connection is Null!");
		}
	
		Token token = connection.getToken();
		if ( token != null ) {
			ClientCredentialsConfiguration configuration = 
				(ClientCredentialsConfiguration) connection.getConnectionConfiguration();

			String accessToken = token.getAccess_token();
			String scope = token.getScope();
			
			nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair(Constants.ACCESS_TOKEN, accessToken));				
			nameValuePairs.add(new BasicNameValuePair(Constants.SCOPE, scope));
			nameValuePairs.addAll(ConnectionUtils.getInstance().getClientCredentials(configuration));
		} 
		
		return nameValuePairs;
	}

}
