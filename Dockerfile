# 1. 빌드 스테이지 (Maven이 포함된 이미지를 사용해야 mvn 명령어가 작동합니다)
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# 의존성 캐싱: pom.xml을 먼저 복사하여 라이브러리를 미리 다운로드 (빌드 속도 향상)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 소스 복사 및 패키징
COPY src ./src
RUN mvn package -DskipTests

# 2. 실행 스테이지 (JRE만 포함하여 이미지 크기 최적화)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# 빌드 스테이지에서 생성된 jar 파일을 복사
# target/*.jar 보다는 명확한 파일명을 지정하거나 와일드카드를 주의해서 사용하세요.
COPY --from=build /app/target/*.jar app.jar

# 컨테이너 실행 명령
ENTRYPOINT ["java", "-jar", "app.jar"]