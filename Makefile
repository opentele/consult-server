.DEFAULT_GOAL := hello

hello:
	@echo --- Consult Server: make is working ---

help:
	@IFS=$$'\n' ; \
	help_lines=(`fgrep -h "##" $(MAKEFILE_LIST) | fgrep -v fgrep | sed -e 's/\\$$//'`); \
	for help_line in $${help_lines[@]}; do \
	    IFS=$$'#' ; \
	    help_split=($$help_line) ; \
	    help_command=`echo $${help_split[0]} | sed -e 's/^ *//' -e 's/ *$$//'` ; \
	    help_info=`echo $${help_split[2]} | sed -e 's/^ *//' -e 's/ *$$//'` ; \
	    printf "%-30s %s\n" $$help_command $$help_info ; \
	done

DB_SUPER_USER:=$(if $(DB_SUPER_USER),$(DB_SUPER_USER),$(shell id -un))
DB=consult
TEST_DB=consult_test
ADMIN_USER=consult

include makefiles/ssl.mk
include makefiles/prod.mk

define _build_db
	-PGPASSWORD=password psql -h localhost -U $(DB_SUPER_USER) -d postgres -c "create user $(ADMIN_USER) with password 'password' createrole";
	-PGPASSWORD=password psql -h localhost -U $(DB_SUPER_USER) -d postgres -c 'create database $1 with owner $(ADMIN_USER)';
	-PGPASSWORD=password psql -h localhost -U $(DB_SUPER_USER) -d $1 -c 'create extension if not exists "uuid-ossp"';
endef

define _drop_db
    -PGPASSWORD=password psql -h localhost -U postgres -c "SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE pg_stat_activity.datname = '$1' AND pid <> pg_backend_pid()"
    -PGPASSWORD=password psql -h localhost -U postgres -c 'drop database $1';
endef

define _run_server
	java -jar --enable-preview server/build/libs/server-0.0.1-SNAPSHOT.jar
endef


######## DATABASE LOCAL
rebuild-db: drop-db build-db

build-db:
	$(call _build_db,$(DB))

migrate-db:
	./gradlew migrateDb

drop-db:
	$(call _drop_db,$(DB))

create-test-db:
	$(call _build_db,$(TEST_DB))

build-test-db: create-test-db
	./gradlew migrateTestDb

drop-test-db:
	$(call _drop_db,$(TEST_DB))

rebuild-test-db: drop-test-db build-test-db

drop-roles:
	-psql -h localhost -U $(DB_SUPER_USER) -d postgres -c 'drop role $(ADMIN_USER)';

delete-all-data:
	psql -h localhost -U $(DB_SUPER_USER) -d $(DB) < core/src/main/resources/db/util/delete-all-data.sql
#######

####### BUILD, TEST, LOCAL RUN
build-server: ## Builds the jar file
	./gradlew clean build -x test

generate-schema: build-server
	> ./core/src/main/resources/db/migration/V1_1__AutoGeneratedSchema.sql
	CONSULT_HTTP_PORT=6057 CONSULT_HTTPS_PORT=6056 SPRING_GENERATE_SCHEMA_ACTION=create SPRING_FLYWAY_ENABLED=false $(call _run_server)

run-server-only:
	$(call _run_server)

run-server: build-db build-server run-server-only

run-server-without-background: build-server
	SCHEDULE_ROOM_CRON="0 0 0 30 2 *" java -jar --enable-preview server/build/libs/server-0.0.1-SNAPSHOT.jar

test-schema-generation: generate-schema rebuild-db migrate-db

test-server: drop-test-db build-test-db
	./gradlew clean build

test: test-server

open-test-results-core:
	open core/build/reports/tests/test/index.html

open-test-results-server:
	open server/build/reports/tests/test/index.html

set-super-admin-password:
	cat func-automation/superadmin.sql | PGPASSWORD=password psql -U consult -h localhost consult

get-super-admin-password:
	curl http://localhost:6054/api/test/open/passwordHash?password=$(CONSULT_SUPER_ADMIN_PASSWORD)

data-setup:
#	newman run func-automation/setup-data.json -e func-automation/postman-env.json --insecure
	postman collection run 695405-93304eb0-4cb4-40b0-ad48-278cc20b0895 -e func-automation/postman-env.json --insecure

data-setup-verbose:
	postman collection run 695405-93304eb0-4cb4-40b0-ad48-278cc20b0895 -e func-automation/postman-env.json --insecure --verbose
#######


####### SOURCE CONTROL
tag-release:
ifndef version
	$(error ERROR: version not provided.)
endif
	git tag -a v$(version) -m "version $(version)"
	git push origin --tags
#######


###### Use case specific
recreate-db-without-data: rebuild-db migrate-db set-super-admin-password
recreate-db: recreate-db-without-data data-setup
recreate-db-and-schema: generate-schema recreate-db

clean-session:
	rm SESSIONS.ser

# Production deployment
prod_scp_dest := consult-server:/root/server
deploy-scripts-to-prod:
	scp Makefile $(prod_scp_dest)
	scp -r makefiles $(prod_scp_dest)
	scp func-automation/superadmin.sql $(prod_scp_dest)/func-automation/superadmin.sql
	scp start-prod-server.sh $(prod_scp_dest)

deploy-to-prod: build-server deploy-scripts-to-prod
	scp server/build/libs/server-0.0.1-SNAPSHOT.jar $(prod_scp_dest)
