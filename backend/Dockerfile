#FORM 이미지빌드환경
#WORKDIR 디렉토리 폴더위치
#COPY 내컴퓨터에있는 파일을 해당위치로 복사
#RUN 의존성 다운로드
#ENTRYPOINT 명령어 실행하기 위해.

FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app


COPY gradle ./gradle
COPY gradlew build.gradle settings.gradle ./
RUN chmod +x gradlew

COPY src ./src
RUN ./gradlew bootJar -x test --no-daemon


FROM eclipse-temurin:17-jre
WORKDIR /app

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

COPY --from=builder /app/build/libs/*-SNAPSHOT.jar app.jar


EXPOSE 8090


ENTRYPOINT ["java", "-jar", "app.jar"]
