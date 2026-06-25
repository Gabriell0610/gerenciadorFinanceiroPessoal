# Estágio 1: Build da aplicação
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copia apenas o pom.xml primeiro para baixar as dependências (otimiza o cache do Docker)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código fonte e gera o .jar
COPY src ./src
RUN mvn clean package -DskipTests

# Estágio 2: Execução do container
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copia o .jar gerado no estágio anterior
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta padrão do Spring (mude se o Finance rodar em outra, ex: 8082)
EXPOSE 8080

# Executa a aplicação ativando o profile 'dev'
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "app.jar"]