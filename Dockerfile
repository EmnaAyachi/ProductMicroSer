# Use an official OpenJDK 8 runtime as a parent image
FROM openjdk:8-jre-slim

# Set the working directory


# Copy the JAR file into the container at /app
COPY target/ProduitTest-0.0.1-SNAPSHOT.jar ProduitTest-0.0.1-SNAPSHOT.jar

EXPOSE 8060

# Define the command to run your application
ENTRYPOINT ["java", "-jar", "ProduitTest-0.0.1-SNAPSHOT.jar"]