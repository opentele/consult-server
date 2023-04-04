login:
ifndef POSTMAN_API_KEY
	$(error ERROR: POSTMAN_API_KEY not provided.)
endif
	postman login --with-api-key $(POSTMAN_API_KEY)
