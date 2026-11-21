# syntax=docker/dockerfile:1

# ---------- Build stage ----------
# Build on the native runner platform: the jar is platform independent, so multi-arch builds
# only need to emulate the (small) runtime stage
FROM --platform=$BUILDPLATFORM eclipse-temurin:25-jdk AS build
WORKDIR /workspace

# Copy Gradle wrapper & build scripts first to leverage layer caching
COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
RUN chmod +x gradlew && ./gradlew --no-daemon dependencies > /dev/null

COPY src ./src
# Tests are executed in CI; skip them here to keep image builds fast
RUN ./gradlew --no-daemon bootJar -x test \
    && cp build/libs/*-SNAPSHOT.jar application.jar \
    && java -Djarmode=tools -jar application.jar extract --layers --launcher --destination extracted

# ---------- Runtime stage ----------
FROM eclipse-temurin:25-jre
WORKDIR /app

# curl is only used by the HEALTHCHECK below
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

RUN groupadd --system spring && useradd --system --gid spring --home-dir /app spring \
    && mkdir -p /app/images && chown -R spring:spring /app

# Copy layers from least to most frequently changing
COPY --from=build --chown=spring:spring /workspace/extracted/dependencies/ ./
COPY --from=build --chown=spring:spring /workspace/extracted/spring-boot-loader/ ./
COPY --from=build --chown=spring:spring /workspace/extracted/snapshot-dependencies/ ./
COPY --from=build --chown=spring:spring /workspace/extracted/application/ ./

USER spring

ENV IMAGE_FOLDER=/app/images \
    JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75.0 -Djava.awt.headless=true"

VOLUME ["/app/images"]
EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
    CMD curl -fsS http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
