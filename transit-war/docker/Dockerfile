FROM tomcat:8-jre8
MAINTAINER Wei Yang <wei@weizilla.com>

RUN rm -rf /usr/local/tomcat/webapps/* 
EXPOSE 8080
COPY transit.war /usr/local/tomcat/webapps/
