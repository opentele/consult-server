# Deployment
run-server-prod:
	CONSULT_HTTPS_PORT=443 java -jar server-0.0.1-SNAPSHOT.jar

rebuild-db-prod:
	DB_SUPER_USER=postgres make rebuild-db
