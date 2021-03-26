FROM ubuntu:20.04
EXPOSE 3100/tcp
EXPOSE 3200/tcp

RUN apt-get update &&\
    apt-get install curl gpg wget -y

RUN wget -nv https://download.opensuse.org/repositories/science:SimSpark/xUbuntu_20.04/Release.key -O Release.key &&\
    apt-key add - < Release.key && rm Release.key &&\
    sh -c "echo 'deb http://download.opensuse.org/repositories/science:/SimSpark/xUbuntu_20.04/ /' > /etc/apt/sources.list.d/science:SimSpark.list" &&\
    apt-get update -y &&\
    apt-get install rcssserver3d -y

ENTRYPOINT rcssserver3d
