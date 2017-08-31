FROM java:8-jre-alpine
VOLUME /tmp
ADD portfolio-1.0.jar app.jar
RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java","-Dspring.profiles.active=production","-Djava.security.egd=file:/dev/./urandom","-Xmx256m","-jar","/app.jar"]
