FROM dockerfile/java:oracle-java8
MAINTAINER email@daniel-zwicker.de

# Set the env variable DEBIAN_FRONTEND to noninteractive
ENV DEBIAN_FRONTEND noninteractive

######### Set locale to UTF-8 ###################
RUN locale-gen en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LC_ALL en_US.UTF-8
RUN echo LANG=\"en_US.UTF-8\" > /etc/default/locale
RUN echo "Europe/Berlin" > /etc/timezone

# add our user and group first to make sure their IDs get assigned consistently, regardless of whatever dependencies get added
RUN \
    groupadd --gid 3000 deployer && \
    groupadd --gid 2000 wildfly && \
    useradd --system --uid 2000 --gid wildfly --groups deployer wildfly

# Install needed packages
RUN \
 apt-get update && \
 apt-get -y install curl && \
 apt-get clean && \
 rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

# Install wildfly
ENV WILDFLY_VERSION 8.2.0.Final
ENV WILDFLY_HOME /opt/wildfly
ENV JBOSS_HOME $WILDFLY_HOME/wildfly-current

RUN \
 mkdir -p  $WILDFLY_HOME && \
 cd $WILDFLY_HOME && \
 curl http://download.jboss.org/wildfly/$WILDFLY_VERSION/wildfly-$WILDFLY_VERSION.tar.gz \
 | tar zx && \
 ln -s $WILDFLY_HOME/wildfly-$WILDFLY_VERSION $JBOSS_HOME && \
 rm -rf $JBOSS_HOME/appclient && \
 rm -rf $JBOSS_HOME/docs && \
 rm -rf $JBOSS_HOME/domain && \
 $JBOSS_HOME/bin/add-user.sh admin sehr_geheim --silent && \
 chown -R wildfly:staff $WILDFLY_HOME && \
 chmod -R g+rws $WILDFLY_HOME

RUN \
    apt-get -y purge curl && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

ADD wildfly.sh $WILDFLY_HOME/wildfly.sh

EXPOSE 8080 9990
CMD ["/opt/wildfly/wildfly.sh"]
