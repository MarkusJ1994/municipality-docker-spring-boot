To run this project as a docker container

- Build the code by running the following command in the root folder of the project `./gradlew build`
- Build image by running the following command in the root folder of the project where the Dockerfile
  resides: `docker build -t <image-name>` .
- Start container with the build image `docker run -d -p 8080:8080 --name <container-name> <image-name>`

To run locally as a Spring Boot application directly on the OS without docker

- In the root folder of the project, run `./gradlew bootRun`