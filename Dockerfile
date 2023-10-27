FROM maven:3.9.4-eclipse-temurin-17-alpine AS builder
WORKDIR /opt/goword

COPY . .
RUN mvn dependency:resolve
RUN mvn clean install -DskipTests
################################################################################
FROM eclipse-temurin:17.0.7_7-jre-alpine
WORKDIR /opt/goword
COPY --from=builder /opt/goword/target/goword-api-1.0.0.jar .

RUN mkdir -p ~/goword/images
RUN chmod 777 ~/goword/images

ENV PROFILE=stag
ENV PORT=9800
ENV DB_URL=
ENV DB_USER=
ENV DB_PASS=
ENV MAIL_HOST=
ENV MAILRUN mkdir /usr/share/goword && mkdir /usr/share/goword/images
RUN chmod 777 -R /usr/share/goword_PORT=
ENV MAIL_USER=
ENV MAIL_PASS=

EXPOSE 9800
CMD java -jar -Dspring.profiles.active=${PROFILE} /opt/goword/goword-api-1.0.0.jar