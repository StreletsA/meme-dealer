FROM openjdk:11

COPY . /telegram-publisher
WORKDIR /telegram-publisher

RUN ./mvnw clean package

WORKDIR /telegram-publisher/target

CMD java -jar telegram-publisher-1.0-SNAPSHOT.jar
