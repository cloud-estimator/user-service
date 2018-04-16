FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
RUN apk update && apk upgrade && apk add netcat-openbsd && apk add curl
RUN cd /tmp/ && \
    curl -k -LO "http://download.oracle.com/otn-pub/java/jce/8/jce_policy-8.zip" -H 'Cookie: oraclelicense=accept-securebackup-cookie' && \
    unzip jce_policy-8.zip && \
    rm jce_policy-8.zip && \
    yes |cp -v /tmp/UnlimitedJCEPolicyJDK8/*.jar /usr/lib/jvm/java-1.8-openjdk/jre/lib/security/
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Deureka.client.serviceUrl.defaultZone=${EUREKASERVER_URI}","-Dspring.cloud.config.uri=${CONFIGSERVER_URI}", "-Dspring.profiles.active=${PROFILE}", "-jar","/app.jar"]