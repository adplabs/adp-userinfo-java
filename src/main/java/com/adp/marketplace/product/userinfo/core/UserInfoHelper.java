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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Logger;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;

import com.adp.marketplace.connection.configuration.AuthorizationCodeConfiguration;
import com.adp.marketplace.connection.constants.Constants;
import com.adp.marketplace.connection.core.ADPAPIConnection;
import com.adp.marketplace.connection.core.AuthorizationCodeConnection;
import com.adp.marketplace.connection.core.ClientCredentialsConnection;
import com.adp.marketplace.connection.exception.ConnectionException;
import com.adp.marketplace.connection.utils.SSLUtils;
import com.adp.marketplace.product.userinfo.exception.APIException;
import com.adp.marketplace.product.userinfo.utils.UserInfoUtils;


/**
 * UserInfoHelper class is a helper class that invokes 
 * User Info API
 * 
 * @author tallaprs
 *
 */
public class UserInfoHelper  {

	private static final Logger LOGGER = Logger.getLogger(UserInfoHelper.class.getName());
	
	private String response;
	private ADPAPIConnection connection;
	
	/**
	 * constructor
	 * 
	 * @param apiConnection current connection
	 */
	public UserInfoHelper(ADPAPIConnection apiConnection) {
		this.connection = apiConnection;
	}
		
	/**
	 * Returns UserInfo for the connected entity
	 * 
	 * @return UserInfo
	 * @throws APIException  throws an exception with message
	 */
	public String getUserInfo() throws APIException {
	
		try {		
			this.response = invokeAPI();
		} catch (ConnectionException e) {
			throw new APIException(e);
		} 
		
		return response;
	}
	
	/**
	 * Process the actual call to User Info API
	 * 
	 * @return String
	 * @throws ConnectionException 
	 * @throws APIException 
	 * @throws UserInfoHelperException
	 */
	private String invokeAPI() throws ConnectionException, APIException {
			
		String response = null;
		StringBuilder stringBuilder = null;
		
		URI endPointURL = null;	
		
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse httpResponse = null;
		
		if ( connection != null ) {
			if ( connection instanceof ClientCredentialsConnection ) {
				throw new ConnectionException("Unimplemented method - UserInfo API access not "
						+ "available for GrantType:ClientCredentials");
			} else if ( !connection.isConnectionIndicator() ) {
				throw new ConnectionException("Connection Token is expired!");
			}
		} else {
			throw new ConnectionException("ADPAPIConnection is null!");
		}
		
		try {
			if ( connection instanceof AuthorizationCodeConnection) {
			
				AuthorizationCodeConnection authorizationCodeConnection = (AuthorizationCodeConnection) connection;
				AuthorizationCodeConfiguration authorizationCodeConfiguration = 
						(AuthorizationCodeConfiguration) authorizationCodeConnection.getConnectionConfiguration();
				
				//create a HTTP Client
				httpClient = SSLUtils.getInstance().getHttpsClient(authorizationCodeConfiguration);
									
				//create a GET request to retrieve data 
				HttpGet getRequest = new HttpGet();
				getRequest.addHeader("User-Agent", Constants.USERINFO_USER_AGENT);
					
				List<NameValuePair> nameValuePairs = UserInfoUtils.getInstance().getNameValuePairs((AuthorizationCodeConnection)connection);
					
				endPointURL = new URIBuilder(authorizationCodeConfiguration.getApiRequestUrl()).setParameters(nameValuePairs).build();
					
				// map end point url
				getRequest.setURI(endPointURL);
					
				//invoke the service for response
				httpResponse = (CloseableHttpResponse) httpClient.execute(getRequest);
					
				if ( httpResponse !=  null) {					
					stringBuilder = processAPIResponse(httpResponse);
				}
			} 
			
		} catch (ConnectionException e) {
			throw new APIException(e);
		} catch (IOException e) {
			throw new APIException(e);   
		} catch (URISyntaxException e) {
			throw new APIException(e);		    
		} catch (APIException e) {
			throw e;
		} finally {
	       	try {
	       		if ( httpResponse != null) {
	       			httpResponse.close();
	       		}
				if ( httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				throw new APIException(e);
			} finally {}
		}
		
		if ( stringBuilder != null) {
			response = stringBuilder.toString();
		}
		
		return response;
	}
	
	/**
	 * Returns a response after processing {@link CloseableHttpResponse} and throws exception
	 * 
	 * @param response 	       
	 * @return StringBuilder
	 * @throws IOException
	 * @throws APIException
	 */
	private StringBuilder processAPIResponse(CloseableHttpResponse response) throws IOException, APIException {
		
		StringBuilder stringBuilder = null;
		int value = -1;
		
		try {
			
			if ( response != null ) {
				
				stringBuilder = new StringBuilder();
				
				InputStreamReader inputStreamReader = new InputStreamReader(response.getEntity().getContent());
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
								
				// reads to the end of the stream 
		         while((value = bufferedReader.read()) != -1) {		        	 
		            // add character
		        	 stringBuilder.append((char)value);
		         }				
			}			
		} catch (IOException e) {
			throw new APIException("Error: Unable to get API response.");
		} finally {
			if ( response != null ) {
				response.close();
			}
		}

		return stringBuilder;
	}

}
