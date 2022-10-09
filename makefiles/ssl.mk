setup-ssl:
	-mkdir server/src/main/resources/keystore
	keytool -genkeypair -alias consult -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore server/src/main/resources/keystore/consult.p12 -validity 3650
