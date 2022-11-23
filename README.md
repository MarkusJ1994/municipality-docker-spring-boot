![](https://codebuild.us-east-1.amazonaws.com/badges?uuid=eyJlbmNyeXB0ZWREYXRhIjoiSG1mMENkUmFjT2RpZTZuMmxQY0d2T2ZKMUVvVGZXVVYvbFM4RDk4N3VybDZHbUlHbW82YnNMTThnbVhlYXVYV3NqRFRDZHpuby9mU2tlUHBhckRNMXlZPSIsIml2UGFyYW1ldGVyU3BlYyI6IldGdjF1OUtuRlVSSVh4eXIiLCJtYXRlcmlhbFNldFNlcmlhbCI6MX0%3D&branch=main)

To run this project as a docker container

- Build the code by running the following command in the root folder of the project `./gradlew build`
- Build image by running the following command in the root folder of the project where the Dockerfile
  resides: `docker build -t <image-name>` .
- Start container with the build image `docker run -d -p 8080:8080 --name <container-name> <image-name>`

To run locally as a Spring Boot application directly on the OS without docker

- In the root folder of the project, run `./gradlew bootRun`
