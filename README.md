# Run Locally 
- mvn clean install 
- mvn spring-boot:run

# Docker Information
- open docker desktop
- docker build -t senior-design-backend .
- docker run -p 8000:8000 senior-design-backend   (or open docker desktop and run the image)
- docker ps (verify what in docker is running)

# application.properties setup
mkdir /src/main/resources
nano /scr/main/resources/application.properties 
(paste in the application.properties file content. Save using ctrl + x, press y, enter) 