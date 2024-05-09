FROM azul/zulu-openjdk-alpine:21-latest
#FROM azul/zulu-openjdk-alpine:21-jre-headless-latest

ENV PW='rt4wbwyA'

RUN mkdir /app
RUN addgroup --system javauser && adduser -S -s /bin/false -G javauser javauser && chown -R javauser:javauser /app

WORKDIR /app
COPY build/libs/jarlyzer-*-all.jar /app/jarlyzer.jar

USER javauser
EXPOSE 4443
CMD ["java", "-Xms2048m", "-Xmx2048m", "-jar", "jarlyzer.jar"]
