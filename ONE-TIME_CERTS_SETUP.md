## One Time Setup: to run the Sample Applications bundled in ADP Libraries

Steps for creating new JKS or using existing JKS and Importing ADPInternal Certificates to cacerts 

## Assumptions:

Unix or Linux environment
Openssl
Keytool
   
   
## ADPLibraries: { ADPConnection, ADPUserInfo }
## ADP Sample Apps: { ADPCodeConnectionSampleApp, AuthCodeUserInfoSampleApp, ClientCredConnectionSampleApp}

Both ADP libraries and Sample Apps are bundled with below seven files in project modules:

	/src/main/resources/certs/ADPInternalChain.der
	/src/main/resources/certs/ADPInternalChain.pem
	/src/main/resources/certs/apiclient_iat_key.pem
	/src/main/resources/certs/apiclient_iat.key
	/src/main/resources/certs/apiclient_iat.pem
	/src/main/resources/certs/keystore.jks
	/src/main/resources/certs/keystore.pkcs12


## To use existing keystore .jks and .der file SKIP Steps 1 - 6 and run commands in provided Step 7 and Step 8  
## To generate new keystore .jks and .der files and to import internal ADP certificates to cacerts follow all of the steps below.

Look in /src/main/resources folders of ADPLibraries or ADP Samples for below two files

'ADPInternalChain.der'
'keystore.jks'

## 1. Make a new key file with .pem extension
   
Ex: Copy contents of apiclient_iat.key to apiclient_key.pem
 

## 2. Generate .pkcs12 file from provided pem and key files using openssl command

Ex: Given apiclient_iat.pem and apiclient_iat_key.pem generate keystore.pkcs12
 
$ openssl pkcs12 -export -out keystore.pkcs12 -in apiclient_iat.pem -inkey apiclient_iat_key.pem
 

## 3. Generate .jks file from .pkcs12 created in Step 2 using keytool utility
 
Ex: Given keystore.pkcs12 generate keystore.jks 
 
Note: if prompted for password use 'adpadp10' without single quotes
	- this password will be used in clientConfig.properties (keyPassword) to access Java Key Store by the sample     
      application.
    - if you give a different password for keystore then remember to configure clientConfig.properties with the 
      right password for keyPassword and storePassword
 
$ keytool -importkeystore -srckeystore keystore.pkcs12 -srcstoretype PKCS12 -deststoretype JKS -destkeystore  keystore.jks
  

## 4. To view available certificates from a keystore of type JKS run below command with the keystore password    
   (adpadp10) or use the password that was provided in Step 3
   
Ex: keytool -list -v -keystore keystore.jks -storepass adpadp10
   
$ keytool -list -v -keystore ${keystore.file} -storepass ${keystore.pass}

Verify that the below block is present along with a listing of Extensions
 
	---------------------------------------------------------------------------------------------
     Alias name: 1
     Creation date: Feb 18, 2016
     Entry type: PrivateKeyEntry
     Certificate chain length: 1
     Certificate[1]:
     Owner: EMAILADDRESS=william.nyquist@adp.com, CN=Labs API Client Certificate, OU=Innovation Labs, 
     O="Automatic Data Processing, Inc", L=Roseland, ST=New Jersey, C=US
     Issuer: CN=ADP Internal Issuing CA 01, DC=ES, DC=AD, DC=ADP, DC=com
     Serial number: 66bde4c0000000000aa6
     Valid from: Wed Feb 17 11:36:19 EST 2016 until: Fri Feb 16 11:36:19 EST 2018
     Certificate fingerprints:
	 MD5:  34:77:BD:84:92:97:EC:05:0B:6B:01:24:37:22:0B:61
	 SHA1: A9:B2:19:A1:4E:DA:DB:74:F9:3B:84:71:D5:7C:EC:ED:EB:13:65:6A
	 SHA256: 39:F2:FB:E1:71:B5:E4:FF:69:E5:5D:F1:4A:A2:9A:53:8B:1C:3D:97:37:DB:D9:F1:76:C9:66:9F:3B:EE:47:06
	 Signature algorithm name: SHA384withRSA
	 Version: 3
	 Extensions
   ------------------------------------------------------------------------------------------------
 
## 5. Convert root certificate to DER format, Given root certificate (ADPInternalChain.pem)
 
$ openssl x509 -in ADPInternalChain.pem -inform pem -out ADPInternalChain.der -outform der
 	

## 6. Validate the root certificate 
   
$ keytool -v -printcert -file ADPInternalChain.der


## 7. Import the root certificate into the JVM Trust Store (cacerts) using keytool utility
 
Note 'cacerts' can be found in your machines JDK installation under security.

Take a back up of original cacerts if you like.

Example Path of CACERTS on MAC OS: 

/Library/Java/JavaVirtualMachines/${jdk_version.jdk}/Contents/Home/jre/lib/security/ - has 'cacerts' and its default password is 'changeit'

Run below keytool command in current workspace  or provide relative path to ADPInternalChain.der if running in a different directory

$ keytool -importcert -alias ADPInternalChain -keystore $JAVA_HOME/jre/lib/security/cacerts -storepass changeit -file ADPInternalChain.der

When Prompted for 

Trust this certificate? [no]:  

Type 'yes' 
   
  
##8  Verify that the root certificate has been imported by searching on alias given in step 7.

Example:

$ keytool -keystore /Library/Java/JavaVirtualMachines/${jdk_version.jdk}/Contents/Home/jre/lib/security/cacerts -storepass changeit -list | grep adpinternalchain    

You may see output similar to below:
	adpinternalchain, Feb 20, 2016, trustedCertEntry,

Congrats! you have successfully completed one time set up!
