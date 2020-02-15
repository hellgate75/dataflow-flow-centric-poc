FROM hellgate75/oracle-jdk8:1.8.241-x64
LABEL MAINTAINER Fabrizio Torelli <hellgate75@gmail.com> <https://www.linkedin.com/in/fabriziotorelli)

# H2 Database Environment Variables
#CONFIG_SERVER_CONFIG_FILE: enable read of custom file (false|true, default: false): file is present in shared volume as /var/spring-config-server/config/application.yaml
ENV USE_CUSTOM_CONFIG_FILE	false

#Create folders
RUN mkdir -p /opt/dataflow-flow-centric/dataflow-ms-flow-centric-config-server/bin
RUN mkdir -p /opt/dataflow-flow-centric/dataflow-ms-flow-centric-config-server/jar

# Copy required scripts
COPY ./start-server.sh /opt/dataflow-flow-centric/dataflow-ms-flow-centric-config-server/bin
COPY ./stop-server.sh /opt/dataflow-flow-centric/dataflow-ms-flow-centric-config-server/bin
COPY ./execute.sh /opt/dataflow-flow-centric/dataflow-ms-flow-centric-config-server/bin
COPY ./kill-all.sh /opt/dataflow-flow-centric/dataflow-ms-flow-centric-config-server/bin
COPY ./ms-env.sh /opt/dataflow-flow-centric/dataflow-ms-flow-centric-config-server/bin
COPY ./dataflow-ms-flow-centric-config-server.jar /opt/dataflow-flow-centric/dataflow-ms-flow-centric-config-server/jar

# Change shell script permissions
RUN chmod 777 /opt/dataflow-flow-centric/dataflow-ms-flow-centric-config-server/bin/ms-env.sh &&\
 chmod 777 /opt/dataflow-flow-centric/dataflow-ms-flow-centric-config-server/bin/start-server.sh &&\
 chmod 777 /opt/dataflow-flow-centric/dataflow-ms-flow-centric-config-server/bin/stop-server.sh &&\
 chmod 777 /opt/dataflow-flow-centric/dataflow-ms-flow-centric-config-server/bin/execute.sh &&\
 chmod 777 /opt/dataflow-flow-centric/dataflow-ms-flow-centric-config-server/bin/kill-all.sh &&\
 chmod 660 /opt/dataflow-flow-centric/dataflow-ms-flow-centric-config-server/jar/dataflow-ms-flow-centric-config-server.jar
ENV HOME=/opt/dataflow-flow-centric/dataflow-ms-flow-centric-config-server
ENV PATH=${PATH}:/opt/dataflow-flow-centric/dataflow-ms-flow-centric-config-server/bin
# Change of Working Directory
WORKDIR /opt/dataflow-flow-centric/dataflow-ms-flow-centric-config-server

# Docker Image parameters ...
VOLUME /opt/dataflow-flow-centric/log

EXPOSE 8888

ENTRYPOINT ["/bin/bash"]

CMD ["start-server.sh"]