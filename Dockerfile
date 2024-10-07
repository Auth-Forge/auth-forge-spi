# syntax=docker/dockerfile:1.4

FROM gradle:jdk17-alpine AS build
ARG VERSION=1.0-SNAPSHOT
ARG GITHUB_ACTOR
ARG GITHUB_TOKEN

# Set environment variables for GitHub authentication
ENV GITHUB_ACTOR=$GITHUB_ACTOR
ENV GITHUB_TOKEN=$GITHUB_TOKEN

# Copy the project files into the container
COPY . /home/gradle/project
WORKDIR /home/gradle/project

# Build the project and publish the JAR to GitHub Packages
RUN gradle clean build publish -Pversion=$VERSION
