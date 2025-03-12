# Usar uma imagem base do OpenJDK 17
FROM eclipse-temurin:17-jdk-jammy

# Definir o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copiar o arquivo JAR gerado pelo Maven para o contêiner
#COPY target/${project.artifactId}-${project.version}.jar app.jar
COPY target/users-1.0.0-SNAPSHOT.jar app.jar

# Expor a porta em que a aplicação Spring Boot roda
EXPOSE 8080

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]