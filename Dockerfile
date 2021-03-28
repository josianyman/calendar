FROM openjdk:11 AS BUILD_IMAGE

# Create app home
ENV APP_HOME=/app/project/
RUN mkdir -p $APP_HOME
WORKDIR $APP_HOME

# Copy application and build it
COPY ./ ./

WORKDIR $APP_HOME

RUN ./gradlew -Dorg.gradle.parallel=false build -x test

# Create runtime environment
FROM openjdk:11-jre
WORKDIR /root/
COPY --from=BUILD_IMAGE /app/project/build/libs/project-boot.jar .
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/root/project-boot.jar"]