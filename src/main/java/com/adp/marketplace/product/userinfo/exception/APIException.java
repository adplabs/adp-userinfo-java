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
package com.adp.marketplace.product.userinfo.exception;


/**
 * 
 * Thrown to indicate when a connection is null or invalid
 * or has invalid configuration
 * 
 * @author tallaprs
 *
 */
public class APIException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	private int httpStatus;
	
	private String message;
	private String response;
	
	public APIException() {
        super();
    }

	public APIException(int httpStatus) {
      this.httpStatus = httpStatus;
    }
	
	public APIException(String message) {
        super(message);
        this.message = message;
	}
	
	public APIException(int httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}
	
	public APIException(String message, String response) {
		this.message = message;
        this.response = response;
	}
	
	public APIException(int httpStatus, String message, String response) {
		this.httpStatus = httpStatus;
		this.message = message;
		this.response = response;
	}
    
    public APIException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public APIException(Throwable cause) {
        super(cause);
    }

	public int getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}
    
}