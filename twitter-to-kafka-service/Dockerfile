FROM eclipse-temurin:17-jdk
VOLUME /tmp
COPY target/*.jar app.jar
COPY ./check-config-server-started.sh /check-config-server-started.sh

RUN chmod +x /check-config-server-started.sh

ENTRYPOINT ["/check-config-server-started.sh"]
CMD ["java","-jar","/app.jar"]