# Variables
APP_NAME=Contact-Manager-0.0.1-SNAPSHOT
JAR_FILE=target/${APP_NAME}.jar
REMOTE_USER=ubuntu
REMOTE_HOST=ec2-52-200-57-227.compute-1.amazonaws.com
REMOTE_DIR=/home/ubuntu
SSH_KEY=/mnt/d/Movie/awskey.pem

# Default target
all: build deploy start

# Build the Spring Boot project
build:
	./mvnw clean package -DskipTests

# Deploy the JAR file to the EC2 instance
deploy: copy-to-ec2 chmod-executable

# Copy the JAR file to the EC2 instance
copy-to-ec2:
	scp -i ${SSH_KEY} ${JAR_FILE} ${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_DIR}/${APP_NAME}.jar

# Give executable permissions to the JAR file on the EC2 instance
chmod-executable:
	ssh -i ${SSH_KEY} ${REMOTE_USER}@${REMOTE_HOST} "chmod +x ${REMOTE_DIR}/${APP_NAME}.jar"

# Start the Spring Boot application on the EC2 instance
start:
	ssh -i ${SSH_KEY} ${REMOTE_USER}@${REMOTE_HOST} "nohup ${REMOTE_DIR}/${APP_NAME}.jar > ${REMOTE_DIR}/app.log 2>&1 &"

# Stop the Spring Boot application on the EC2 instance
stop:
	ssh -i ${SSH_KEY} ${REMOTE_USER}@${REMOTE_HOST} "pkill -f ${APP_NAME}"

# Clean the project
clean:
	./mvnw clean

.PHONY: all build deploy copy-to-ec2 chmod-executable start stop clean
