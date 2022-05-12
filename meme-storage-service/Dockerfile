FROM openjdk:11

COPY . /meme-storage-service
WORKDIR /meme-storage-service

RUN ./mvnw clean package -Dmaven.test.skip
WORKDIR /meme-storage-service/target

CMD java -jar meme-storage-service-0.0.1-SNAPSHOT.jar
