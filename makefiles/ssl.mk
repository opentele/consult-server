setup-ssl:
#make directory
	-mkdir server/src/main/resources/keystore
#remove
	-rm server/src/main/resources/keystore/consult.p12
#Convert the certificate and private key to PKCS 12. origin.cer and private_key are created by copying them into files (no need to store anywhere)
	openssl pkcs12 -export -in ~/Downloads/origin.cer -inkey ~/Downloads/private_key -name server.telesathi.com -out origin-PKCS-12.p12
#Import the PKCS 12 certificate by executing the following command
	keytool -importkeystore -deststorepass password -destkeystore server/src/main/resources/keystore/consult.p12 -srckeystore origin-PKCS-12.p12 -srcstoretype PKCS12

open-keystore-dir:
	open server/src/main/resources/keystore
