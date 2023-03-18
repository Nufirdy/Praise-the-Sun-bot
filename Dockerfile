FROM eclipse-temurin:11
COPY target/*.jar bot.jar
ENTRYPOINT ["java","-jar","/bot.jar"]