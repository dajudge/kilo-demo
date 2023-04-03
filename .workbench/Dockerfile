FROM ubuntu:jammy

RUN apt update && apt install -y curl sudo openssh-client

RUN curl -Ls "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl" > /usr/local/bin/kubectl && \
    chmod +x /usr/local/bin/kubectl

RUN apt install -y openjdk-11-jdk
