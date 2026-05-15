LOCAL_APP_IP	?=	localhost
LOCAL_APP_PORT	?=	8080


readme deploy_local: localappip    :=$(LOCAL_APP_IP)
readme deploy_local: localappport  :=$(LOCAL_APP_PORT)


.PHONY: readme deploy_local rip_deploy_local rip_local

readme:
	@echo \
"\n**** Stop! You must run make with a specific target! ****\n\
\n\
To drop and recreate app-user-management, use one of these targets.\n\
Ensure you have a .env file to map secrets for app-user-management application.properties file.\n\
if you dont have an .env file you must make one in the root directory in order for springboot to work.\n\
For more help refer to the documentation in Github. \n\
\n\
	rip_deploy_local	Drop and recreate LOCAL app-user-management at $(localappip):$(localappport).\n\
\n\
	deploy_local		Deploy the app-user-mananagement/local container.\n\
\n\
	rip_local		Drop the app-user-mananagement/local container.\n"


# ------ deploy application ------

deploy_local:
	@echo Deploying LOCAL app-user-management at $(localappip):$(localappport). Are you sure? [Y/n]
	@read line; if [ ! $$line = "Y" ] && [ ! $$line = "y" ]; then echo Aborting...; exit 1; fi
	
	docker build -t app-user-mananagement/local .
	
	docker compose up



# ------ rip and deploy application ------
rip_deploy_local:
	@echo Ripping and redeploying LOCAL app-user-management at $(localappip):$(localappport). Are you sure? [Y/n]
	@read line; if [ ! $$line = "Y" ] && [ ! $$line = "y" ]; then echo Aborting...; exit 1; fi
	
	docker compose down
	
	docker build -t app-user-mananagement/local .
	
	docker compose up



# ------ rip application ------
rip_local:
	@echo Ripping and redeploying LOCAL app-user-management at $(localappip):$(localappport). Are you sure? [Y/n]
	@read line; if [ ! $$line = "Y" ] && [ ! $$line = "y" ]; then echo Aborting...; exit 1; fi
	
	docker compose down