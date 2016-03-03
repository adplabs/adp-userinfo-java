## ADP User Info Library for Java

The ADP Client UserInfo Library is intended to simplify and aid the process of retrieving UserInfo from the ADP Marketplace API Gateway. The Library includes a sample application that can be run out-of-the-box to connect to the ADP Marketplace API test gateway.

There are two ways of installing and using this library:

  - Clone the repo from Github: This allows you to access the raw source code of the library as well as provides the ability to run the sample application and view the Library documentation
  - Maven dependency: When you are ready to use the library in your own application use this method to add as Maven dependency

### Version
1.0.0-Beta 

### Installation

**Clone from Github**

You can either use the links on Github or the command line git instructions below to clone the repo.

$ git clone https://github.com/adplabs/adp-userinfo-java.git

followed by either using command line or eclipse IDE to build the projects

$ cd adp-userinfo-java

$ mvn clean install -e

## Do a one time setup for client certificates 
 
NOTE: 

Certificates bundled in libraries will only work for ADP Sandbox environment

Refer ONE-TIME_CERTS_SETUP.md for jks key generation and cacerts import instructions

## Usage
### Library Organization 
ADPConnection library follows standard maven project structure


### Integrate Libraries
To integrate ADPConnection library to your Client Applications add ADPConnection.jar to your project classpath and use the library API's.

Client Applications that use these libraries would have to acquire certificates from ADP by submitting CSR signing request.

NOTE: 
Certificates bundled in libraries will only work for ADP Sandbox environment

Refer ONE-TIME_CERTS_SETUP.md for jks key generation and how to import to cacerts instructions


**How to Use ADPConnection Library and ADPUSerInfo Library ?**

Initialize Configuration based on Grant type **client_credentials** or **authorization_code**

Create Connection with initialized configuration

Invoke connect() on Connection to acquire a connection based on configuration

Invoke getAccessToken() on Connection to obtain Access Token 

Invoke getErrorResponse() on Connection to obtain error details in case Access Token is not returned

**Steps to get Authorization Code connection**

Initialize Authorization Code Configuration based on Grant type **authorization_code**

Create AuthorizationCodeConnection Connection with initialized configuration

Invoke connect() on Connection to get Access Token from authorizationCodeConnection

Invoke getAccessToken() on Connection to obtain Access Token 

Invoke getErrorResponse() on Connection to obtain error details in case Access Token is not returned

**ADPUSerInfo Library Usage**

Create a UserInfoHelper object initialized with AuthorizationCode Connection

Invoke getUserInfo() on UserInfoHelper to obtain UserInfo      
          
### Create Authorization Code Connection###
     
    // create authorization code configuration object    
    AuthorizationCodeConfiguration authorizationCodeConfiguration = new AuthorizationCodeConfiguration();
				
	// get authorization code configuration properties 
	Properties properties = CLIENT_UTILS_INSTANCE.getInstance().getConfigProperties();

	// populate authorization code configuration 
	CLIENT_UTILS_INSTANCE.mapPropertiesToAuthCodeConfiguration(properties,  authorizationCodeConfiguration);
				
	// get ADP connection using configuration above
	authorizationCodeConnection = (AuthorizationCodeConnection) 
			CONNECTION_FACTORY_INSTANCE.createConnection(authorizationCodeConfiguration); 			
		
	// get Url from authorization connection
	authorizationUrl = "redirect:" + authorizationCodeConnection.getAuthorizationUrl();

	// callback code snippet
	String requestCode = (String) request.getParameter("code");
	String callBackRequestError = (String) request.getParameter("error");
	
	AuthorizationCodeConfiguration config = ((AuthorizationCodeConfiguration) 
	authorizationCodeConnection.getConnectionConfiguration());

	// call to ADPConnection Library
	authorizationCodeConnection.connect();
					
	// at this time this connection must have token info
	Token token = authorizationCodeConnection.getToken();

	// alternate flow - no token in connection
	errorMessage =  authorizationCodeConnection.getErrorResponse();

### Get User Info ###
	   
	 // create a new UserInfoHelper using the current connection 
	 UserInfoHelper userInfoHelper = new UserInfoHelper(authorizationCodeConnection);
	
	 // retrieve user info using the current connection 
	 userInfoResponse = userInfoHelper.getUserInfo();

## Sample Client

A sample client is provided to demonstrate usage of the libraries. The sample client connects to a sandbox environment hosted by ADP, and comes pre-configured with the necessary credentials and certificates to connect to the sandbox server.

AuthCodeUserInfoSampleApp - Web Application - Web application that uses ADPUserInfo library, ADPConnection library to obtain a connection based on grant type Authorization Code 
                                           
## Client Setup

Find folder /client in the adp-connection-java library

Copy the client sample(s) to {Home}/workspace/

$ cp adp-userinfo-java/client/AuthCodeUserInfoSampleApp.zip .	
 
Unzip the sample application(s) in {Home}/workspace/ so that sample applications can be run as individual module
 	
$ tar -xvzf AuthCodeUserInfoSampleApp.zip

Finally, Import the sample application(s) to Eclipse to run the client application

To setup, build and run the sample clients, please refer to README.md corresponding to sample AuthCodeUserInfoSampleApp


## API Documentation

Documentation on the individual API calls

Library Documentation

file://{HOME}/workspace/ADPUserInfo/doc/index.html
  
To generate the documentation, please complete the following steps:

### Generate Docs Eclipse Editor

Select ADPUserInfoLibrary

Select Project from menu bar and click **Generate Javadoc**

A pop up window opens with few options 

**Configure JavaDoc Command** to point to your machines JAVA_HOME/Contents/Home/bin/javadoc

Ex:	/Library/Java/JavaVirtualMachines/${java_version.jdk}/Contents/Home/bin/javadoc

**Create Javadoc with members with visibility** Select public

**Use standard Doculet** Destination {HOME}/workspace/ADPConnection/doc	

Check 'Document title' checkbox 

Click Next **Provide Document Title** 'ADPUserInfo API Docs'

**Basic Options** Choose Default options

Click Next

**Configure Javadoc arguments** 

Check Overview -> Browse to point to {HOME}/adp-userinfo-java/doc/index.html

Verify JRE source compatibility 

Check Open generated index file in browser

Click Finish

Java Doc successfully generated in {HOME}/workspace/ADPUserInfo/doc	

**View Java Docs in a browser**

file://{HOME}/workspace/ADPUserInfo/doc/index.html

Additional documentation can also be found on the [ADP Developer Portal](https://developers.adp.com).


## Tests
Automated unit tests are available in the src/main/test folder.  

$ mvn test

### Eclipse 

Right Click on pom.xml 

Select Run As -> Maven Test

## Dependencies

ADPUserInfo library depends on the following libraries.

1.  commons-logging-1.2.jar
2.  commons-lang3-3.4.jar
3.  httpclient-4.5.1.jar
4.  httpcore-4.4.3.jar
5.  commons-codec-1.9.jar
6.  httpcore-osgi-4.4.4.jar
7.  httpcore-nio-4.4.4.jar
8.  gson-2.3.1.jar
9.  json-simple-1.1.1.jar
10. junit-4.12.jar
11. hamcrest-core-1.3.jar
12. slf4j-api-1.7.14.jar
13. ADPConnection-1.0.0.jar


## Contributing

To contribute to the library, please generate a pull request.  Before generating the pull request, please insure the following:

1. Appropriate unit tests have been updated or created on all public methods validating the success flows, error, exception conditions. 
   In  addition, verify and validate logic inside protected/private methods through tests written for public methods
2. Your code updates have been fully tested and compiled with no errors.
3. Update README and API documentation as appropriate.

## License
[Apache 2](http://www.apache.org/licenses/LICENSE-2.0)