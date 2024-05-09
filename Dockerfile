FROM azul/zulu-openjdk:21-latest

ENV PW='rt4wbwyA'

RUN mkdir /app
#RUN addgroup --system javauser && adduser -S /bin/false -G javauser javauser && chown -R javauser:javauser /app
RUN addgroup -S javauser && adduser -S javauser -G javauser && chown -R javauser:javauser /app

WORKDIR /app
COPY build/libs/jarlyzer-*-all.jar /app/jarlyzer.jar

USER javauser
EXPOSE 4443
CMD ["java", "-Xms2048m", "-Xmx1024m", "-jar", "jarlyzer.jar"]
