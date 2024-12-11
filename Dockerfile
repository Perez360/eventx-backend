FROM maven:3-jdk-11 AS build

ENV HOME=/usr/app

RUN mkdir -p $HOME

WORKDIR $HOME

COPY . $HOME

RUN --mount=type=cache,target=/root/.m2 mvn clean -U -B package -Dmaven.test.skip=true

## Stage Two: Deployment

FROM openjdk

RUN mkdir /app

WORKDIR /app

COPY --from=build /usr/app/target/eventx-backend-1.0.0-SNAPSHOT-fat.jar /app

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "eventx-backend-1.0.0-SNAPSHOT-fat.jar"]

