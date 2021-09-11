FROM maven:3.8.2-amazoncorretto-16
COPY ./. /
run mvn test