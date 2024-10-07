# syntax=docker/dockerfile:1.4

FROM gradle:jdk17-alpine AS build
ARG VERSION=1.0-SNAPSHOT
COPY . /home/gradle/project
WORKDIR /home/gradle/project

# Build the project and create the JAR file
RUN gradle clean build -Pversion=$VERSION

# Export the JAR file to the host machine
FROM scratch AS export-stage
ARG VERSION=1.0-SNAPSHOT
COPY --from=build /home/gradle/project/build/libs/auth-forge-spi-$VERSION.jar /auth-forge-spi-$VERSION.jar
