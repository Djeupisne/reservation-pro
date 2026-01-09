# Image Java Amazon Corretto
FROM amazoncorretto:17-alpine

# Répertoire de travail
WORKDIR /app

# Copier le JAR généré
COPY target/*.jar app.jar

# Exposer le port
EXPOSE 8080

# Commande de démarrage
ENTRYPOINT ["java", "-jar", "app.jar"]