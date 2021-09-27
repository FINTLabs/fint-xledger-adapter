FROM gradle:6.9.1-jdk11 as builder
USER root
COPY . .
RUN gradle --no-daemon build

FROM gcr.io/distroless/java:11
ENV JAVA_TOOL_OPTIONS -XX:+ExitOnOutOfMemoryError
COPY --from=builder /home/gradle/build/deps/external/*.jar /data/
COPY --from=builder /home/gradle/build/deps/fint/*.jar /data/
COPY --from=builder /home/gradle/build/libs/fint-sse-adapter-skeleton-*.jar /data/fint-sse-adapter-skeleton.jar
CMD ["/data/fint-sse-adapter-skeleton.jar"]
