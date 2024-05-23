# Variables
APP_NAME=Contact-Manager-0.0.1-SNAPSHOT
JAR_FILE=target/${APP_NAME}.jar
REMOTE_USER=ubuntu
REMOTE_HOST=ec2-54-225-173-208.compute-1.amazonaws.com
REMOTE_DIR=/home/ubuntu
SSH_KEY=awskey.pem

# Default target
all: build deploy start

# Ensure mvnw is executable before building the project
build: ensure-mvnw-executable
	./mvnw clean package -DskipTests || { echo 'Build failed'; exit 1; }

# Ensure mvnw script is executable
ensure-mvnw-executable:
	chmod +x mvnw

# Deploy the JAR file to the EC2 instance
deploy: copy-to-ec2 chmod-executable

# Copy the JAR file to the EC2 instance
copy-to-ec2:
	scp -i ${SSH_KEY} ${JAR_FILE} ${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_DIR}/${APP_NAME}.jar || { echo 'SCP failed'; exit 1; }

# Give executable permissions to the JAR file on the EC2 instance
chmod-executable:
	ssh -i ${SSH_KEY} ${REMOTE_USER}@${REMOTE_HOST} "chmod +x ${REMOTE_DIR}/${APP_NAME}.jar" || { echo 'chmod failed'; exit 1; }

# Start the Spring Boot application on the EC2 instance and show the log
start:
	ssh -i ${SSH_KEY} ${REMOTE_USER}@${REMOTE_HOST} "nohup java -jar ${REMOTE_DIR}/${APP_NAME}.jar > ${REMOTE_DIR}/app.log 2>&1 &" || { echo 'Start failed'; exit 1; }
	ssh -i ${SSH_KEY} ${REMOTE_USER}@${REMOTE_HOST} "tail -f ${REMOTE_DIR}/app.log"

# Stop the Spring Boot application on the EC2 instance
stop:
	ssh -i ${SSH_KEY} ${REMOTE_USER}@${REMOTE_HOST} "pkill -f ${APP_NAME}" || { echo 'Stop failed'; exit 1; }

# Clean the project
clean:
	./mvnw clean || { echo 'Clean failed'; exit 1; }

.PHONY: all build deploy copy-to-ec2 chmod-executable start stop clean ensure-mvnw-executable
