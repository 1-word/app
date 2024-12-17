FROM apfhd0257/word:jdk17
ARG JAR_FILE=build/libs/*.jar
WORKDIR /usr/src/app
COPY ${JAR_FILE} ./app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]