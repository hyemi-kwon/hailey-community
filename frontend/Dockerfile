# syntax=docker/dockerfile:1

# 실행 환경은 전체 Node 이미지보다 작은 Alpine 기반 이미지를 사용한다.
FROM node:22-alpine
WORKDIR /app

# 운영 의존성만 설치하도록 npm에 운영 환경임을 알린다.
ENV NODE_ENV=production

# 의존성 정의 파일은 애플리케이션 소스보다 변경 빈도가 낮다.
# 먼저 복사하면 JS/CSS/HTML만 수정됐을 때 npm ci 레이어를 재사용할 수 있다.
COPY --chown=node:node package*.json ./
# BuildKit cache mount로 npm 다운로드 캐시를 빌드 사이에 재사용한다.
# 캐시는 최종 이미지 레이어에 포함되지 않으므로 이미지 용량은 늘지 않는다.
RUN --mount=type=cache,target=/root/.npm \
    npm ci --omit=dev

# 자주 변경되는 소스는 의존성 설치 이후에 복사해 캐시 무효화 범위를 줄인다.
# 동시에 node 사용자 소유권을 부여해 실행 중 root 권한이 필요 없게 한다.
COPY --chown=node:node . .

# 컨테이너 탈취 시 피해 범위를 줄이기 위해 root가 아닌 공식 node 사용자로 실행한다.
USER node
EXPOSE 8080

# 프로세스가 실행 중인지만 보지 않고 실제 HTTP 응답 가능 여부를 검사한다.
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
    CMD wget --quiet --spider http://localhost:8080/config.js || exit 1

CMD ["node", "app.js"]
