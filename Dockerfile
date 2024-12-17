FROM openjdk:17-jdk-slim

# 시간 설정
ARG DEBIAN_FRONTEND=noninteractive
ENV TZ=Asia/Seoul
RUN apt-get update
RUN apt-get install -y tzdata && \
    ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime && \
    apt-get install -y python3-pip && \
    pip3 install gtts && \
    mkdir -p /data/files && chmod 711 /data/files

# jar파일 설정
WORKDIR /usr/src/app
ENTRYPOINT ["java", "-jar", "app.jar"]