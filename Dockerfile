FROM eclipse-temurin:21-jdk-jammy

# Set noninteractive frontend for clean install
ENV DEBIAN_FRONTEND=noninteractive

# Optional: install Maven or Gradle
RUN apt-get update && apt-get install -y \
    curl unzip git nano && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# Create working directory
WORKDIR /code
