FROM jenkins:latest

# if we want to install via apt
USER root

ENV JENKINS_OPTS --httpPort=8888 --httpsPort=-1
RUN \
    DEBIAN_FRONTEND=noninteractive && \
    echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" | tee /etc/apt/sources.list.d/webupd8team-java.list && \
    echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" | tee -a /etc/apt/sources.list.d/webupd8team-java.list && \
    echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections && \
    apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys EEA14886 && \
    apt-get update -y && \
    apt-get install -y git oracle-java8-installer oracle-java8-set-default iptables && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

RUN \
    mkdir -p /home/jenkins && \
    chown -R jenkins: /home/jenkins

RUN usermod --home /home/jenkins jenkins

RUN \
    curl -L https://get.docker.com/builds/Linux/x86_64/docker-latest -o /usr/bin/docker && \
    chmod +x /usr/bin/docker && \
    addgroup --gid 999 docker && \
    adduser jenkins users && \
    adduser jenkins docker

COPY plugins.sh /usr/local/bin/plugins.sh
COPY plugins.txt /usr/share/jenkins/plugins.txt
RUN /usr/local/bin/plugins.sh /usr/share/jenkins/plugins.txt

COPY conf /usr/share/jenkins/ref

# drop back to the regular jenkins user - good practice
USER jenkins

RUN \
    git config --global user.email "jenkins@demo.invalid" && \
    git config --global user.name "jenkins"

# jenkins sshd port
EXPOSE 2222
