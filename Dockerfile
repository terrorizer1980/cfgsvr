FROM openjdk:11-jre
LABEL maintainer="Eliezer Efrain Chavez <eliezer.chavez@gmail.com>"

RUN apt-get update \
 && apt-get -y install locales openssh-client \
 && mkdir ~/.ssh && chmod 700 ~/.ssh \
 && rm -rf /var/lib/apt/lists/*

# Add the service itself
ARG jarFile
ADD target/${jarFile} /usr/share/config/server.jar

ENTRYPOINT ["java", "-jar", "/usr/share/config/server.jar"]
