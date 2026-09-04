# 第一阶段：编译项目
FROM eclipse-temurin:17-jdk-noble AS builder

WORKDIR /app

COPY .mvn .mvn
COPY mvnw pom.xml ./

RUN chmod +x mvnw
RUN ./mvnw -B dependency:go-offline

COPY src src

RUN ./mvnw -B clean package -DskipTests


# 第二阶段：运行项目
FROM eclipse-temurin:17-jre-noble

WORKDIR /app

COPY --from=builder \
    /app/target/opsflow-0.0.1-SNAPSHOT.jar \
    app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]