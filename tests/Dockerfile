FROM eclipse-temurin:21-jdk

WORKDIR /bank
ENV TZ=Europe/Moscow
COPY ./gradle ./gradle
COPY ./tests ./tests
COPY ./gradlew ./
COPY ./build.gradle ./
COPY ./settings.gradle ./
COPY ./gradle.properties ./
COPY ./tests/entrypoint.sh ./entrypoint.sh

RUN chmod +x ./entrypoint.sh ./gradlew

CMD ["./entrypoint.sh"]
