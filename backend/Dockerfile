# syntax=docker/dockerfile:1

# 빌드 단계에는 컴파일과 Gradle 실행에 필요한 JDK 이미지를 사용한다.
# Alpine 기반 이미지를 사용해 빌드 이미지의 다운로드 용량을 줄인다.
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

# Gradle 설정 파일은 애플리케이션 소스보다 변경 빈도가 낮다.
# 먼저 복사하면 소스만 변경됐을 때 의존성 관련 레이어를 재사용할 수 있다.
COPY gradle ./gradle
COPY gradlew build.gradle settings.gradle ./
RUN chmod +x gradlew

# 의존성 다운로드를 소스 빌드와 분리한다.
# BuildKit cache mount를 사용하면 /root/.gradle 캐시를 다음 빌드에서도
# 재사용할 수 있고, 이 캐시는 최종 이미지에는 포함되지 않는다.
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew dependencies --no-daemon

# 자주 변경되는 소스 코드는 의존성 다운로드 이후에 복사한다.
COPY src ./src

# GitHub Actions의 test job에서 ./gradlew test를 먼저 실행한다.
# 테스트를 통과한 커밋만 이미지 빌드 단계로 넘어오므로 여기서는 중복 실행을
# 피하고 실행 가능한 bootJar 산출물 생성에만 집중한다.
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew bootJar -x test --no-daemon

# 실행 단계에는 컴파일 도구가 없는 JRE 이미지만 사용한다.
# 멀티 스테이지 빌드로 Gradle, JDK, 소스 코드가 최종 이미지에 들어가지 않는다.
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# health check에서 실제 HTTP 요청을 보내기 위해 curl을 설치한다.
# --no-cache를 사용해 apk 인덱스를 이미지 레이어에 남기지 않는다.
RUN apk add --no-cache curl \
    && addgroup -S spring \
    && adduser -S spring -G spring

# Gradle에서 bootJar 이름을 app.jar로 고정했으므로 wildcard를 사용하지 않는다.
# non-root 사용자가 읽을 수 있도록 복사 시점에 소유권도 함께 지정한다.
COPY --from=builder --chown=spring:spring /app/build/libs/app.jar app.jar

# 컨테이너 탈취 시 피해 범위를 줄이기 위해 root가 아닌 사용자로 실행한다.
USER spring
EXPOSE 8090

# 컨테이너 프로세스가 살아 있는지만 확인하지 않고 실제 API 응답을 검사한다.
# Spring Boot 시작 시간을 고려해 첫 검사는 30초 후 시작한다.
HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
    CMD curl --fail --silent http://localhost:8090/v1/health || exit 1

# JVM은 JAVA_TOOL_OPTIONS 환경변수를 자동으로 읽는다.
# Compose에서 메모리, 시간대 등의 옵션을 환경별로 주입할 수 있다.
# 예: JAVA_TOOL_OPTIONS=-Xms256m -Xmx512m -Duser.timezone=Asia/Seoul
ENTRYPOINT ["java", "-jar", "app.jar"]
