# Pull the open jdk image (java run-time)from the docker repository
FROM openjdk:8
 
 
# This port will be published to the host (AWS EC2 Instance), when the container is booting
EXPOSE 80
 
# Allows you to configure a container that will run as an executable (This tells docker how to start app after the image is started)
