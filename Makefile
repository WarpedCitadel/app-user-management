LOCAL_APP_IP	?=	localhost
LOCAL_APP_PORT	?=	8080

IMAGE_NAME =app-user-management/local
CONTAINER_NAME =app-user-management

readme deploy_local: localappip    :=$(LOCAL_APP_IP)
readme deploy_local: localappport  :=$(LOCAL_APP_PORT)


.PHONY: readme deploy_local rip_deploy_local rip_local

readme:
	@echo \
"\n**** Stop! You must run make with a specific target! ****\n\
\n\
To drop and recreate app-user-management, use one of these targets.\n\
Ensure you have a .env file to map secrets for app-user-management application.properties file.\n\
If you do not have an .env file you must make one in the root directory in order for SpringBoot to work.\n\
Before using the make commands or docker commands verify if the docker daemon is active\n\
For more help refer to the documentation in Github. \n\
\n\
	rip_deploy_local	Stop the local docker container and rebuild the docker image and container on $(localappip):$(localappport).\n\
\n\
	deploy_local		Build the local docker image and build local docker container on $(localappip):$(localappport).\n\
\n\
	rip_local		Stop the local docker container and rebuild the docker image and container on $(localappip):$(localappport).\n"


# ------ deploy application ------

deploy_local:
	@echo Deploying LOCAL $(CONTAINER_NAME) at $(localappip):$(localappport). Are you sure? [Y/n]
	@read line; if [ ! $$line = "Y" ] && [ ! $$line = "y" ]; then echo Aborting...; exit 1; fi
	
	@echo "Building image $(IMAGE_NAME)..."
	docker build -t $(IMAGE_NAME) .
	
	@echo "Building container $(CONTAINER_NAME)..."
	docker compose up



# ------ rip and deploy application ------
rip_deploy_local:
	@echo Ripping and redeploying LOCAL $(CONTAINER_NAME) at $(localappip):$(localappport). Are you sure? [Y/n]
	@read line; if [ ! $$line = "Y" ] && [ ! $$line = "y" ]; then echo Aborting...; exit 1; fi
	
	@echo "Stopping $(CONTAINER_NAME)..."
	docker compose down
	
	@echo "Building image $(IMAGE_NAME)..."
	docker build -t $(IMAGE_NAME) .
	
	@echo "Building container app-user-management..."
	docker compose up



# ------ rip application ------
rip_local:
	@echo Ripping LOCAL $(CONTAINER_NAME) at $(localappip):$(localappport). Are you sure? [Y/n]
	@read line; if [ ! $$line = "Y" ] && [ ! $$line = "y" ]; then echo Aborting...; exit 1; fi
	
	@echo "Stopping and removing container $(CONTAINER_NAME)..."
	-docker stop $(CONTAINER_NAME)-wc_local-1
	-docker rm $(CONTAINER_NAME)-wc_local-1
	
	@echo "Removing image $(IMAGE_NAME)..."
	-docker rmi -f $(IMAGE_NAME)