# Java 21용 OpenJDK 이미지 사용
FROM eclipse-temurin:21-jdk

WORKDIR /app

# build/libs/ 안에 있는 jar 파일을 app.jar로 복사
COPY build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
