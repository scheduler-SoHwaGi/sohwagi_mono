FROM gradle:8.10.2-jdk17 AS build
WORKDIR /workspace
COPY . .
RUN gradle bootJar --no-daemon -x test

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/build/libs/*.jar /app/sohwagi-app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-Duser.timezone=Asia/Seoul", "-jar", "/app/sohwagi-app.jar"]
