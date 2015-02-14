#! /bin/bash

# Parse a support-core plugin -style txt file as specification for jenkins plugins to be installed
# in the reference directory, so user can define a derived Docker image with just :
#
# FROM jenkins
# COPY plugins.txt /plugins.txt
# RUN /usr/share/jenkins/plugins.sh /plugins.txt
#

REF=/usr/share/jenkins/ref/plugins
mkdir -p $REF

while read spec; do
    if [ "x$spec" != "x" ]; then
        if [ "${spec:0:1}" != "#" ]; then
            plugin=(${spec//:/ });
            if [ "${#plugin[@]}" -eq "2" ]; then
                echo "download plugin '${plugin[0]}-${plugin[1]}'"
                curl -L ${JENKINS_UC}/download/plugins/${plugin[0]}/${plugin[1]}/${plugin[0]}.hpi -o $REF/${plugin[0]}.hpi;
            else
                echo "download plugin '${plugin[0]}'"
                curl -L ${JENKINS_UC}/latest/${plugin[0]}.hpi -o $REF/${plugin[0]}.hpi;
            fi
        else
            echo ${spec}
        fi
    fi

done  < $1
