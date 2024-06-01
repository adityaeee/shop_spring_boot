# base image / parent image
FROM openjdk:17-alpine

WORKDIR /app

COPY /target/shop-0.0.1-SNAPSHOT.jar /app/shop.jar

COPY .env /app/.env

#ENV cat .env | xargs

EXPOSE 8080

CMD ["java", "-jar", "shop.jar"]
