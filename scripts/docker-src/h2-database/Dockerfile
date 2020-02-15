FROM hellgate75/oracle-jdk8:1.8.241-x64
LABEL MAINTAINER Fabrizio Torelli <hellgate75@gmail.com> <https://www.linkedin.com/in/fabriziotorelli)

# H2 Database Environment Variables
#H2_ENV: Environment folder for the database (default: D3)
ENV H2_ENV					D3
#H2_WEB_PORT: Web Module Port (none|<value>, default: 9090)
ENV H2_WEB_PORT				none
#H2_TCP_PORT: TCP Database Port (none|<value>, default: 65123)
ENV H2_TCP_PORT				none
#H2_PG_PORT: PG Module Port (none|<value>, default: 9023)
ENV H2_PG_PORT				none
#H2_ENABLE_WEB: Enable Web feature (1|0|true|false)
ENV H2_ENABLE_WEB			false
#H2_ENABLE_PG: Enable PG feature (1|0|true|false)
ENV H2_ENABLE_PG			false
#H2_ENABLE_INSECURE: Databases are created when accessed (1|0|true|false)
ENV H2_ENABLE_INSECURE		false
#H2_ENABLE_SECURE: Only existing databases may be opened (1|0|all servers)
#H2_ENABLE_SECURE Overrides H2_ENABLE_INSECURE
ENV H2_ENABLE_SECURE		false
#H2_BROWSER: Start a browser connecting to the web server (1|0|true|false)
ENV H2_BROWSER          	false
#H2_ENABLE_DAEMONS: Enable daemons sample(none|web,pg,tcp)
ENV H2_ENABLE_DAEMONS   	none
#H2_PROPS_FOLDER: Server properties (default: ~, disable: null)
ENV H2_PROPS_FOLDER			null
#H2_ENABLE_WEB_SSL: Use encrypted (HTTPS) connections (1|0|true|false)
ENV	H2_ENABLE_WEB_SSL		false
#H2_ENABLE_TCP_SSL: Use encrypted (SSL) connections
ENV H2_ENABLE_TCP_SSL		false
#H2_ENABLE_WEB_PASSWORD: Enable Password of DB Console administrator (1|0|true|false)
ENV H2_ENABLE_WEB_PASSWORD	false
#H2_WEB_PASSWORD: The Password of DB Console (none|<value>)
ENV H2_WEB_PASSWORD			none
#H2_ENABLE_TCP_PASSWORD: Enable Password for shutting down a TCP server (1|0|true|false)
ENV H2_ENABLE_TCP_PASSWORD	false
#H2_TCP_PASSWORD: The password for shutting down a TCP server (none|<value>)
ENV H2_TCP_PASSWORD			none
#H2_TRACE: Enable Print additional trace information (all servers) (1|0|true|false)
ENV H2_TRACE	        	false
#H2_SHUTDOWN_URL: Stop the TCP server; example: tcp://localhost
ENV H2_SHUTDOWN_URL	    	none
#H2_FORCE_SHUTDOWN: Do not wait until all connections are closed (1|0|true|false)
ENV H2_FORCE_SHUTDOWN		false
#H2_DATA_GIT_URL: Stop the TCP server; example: ssh://git@github/com/{user}/{repo_name}.git|{folder|/};ssh://git@github/com/{user}/{repo_name}.git|{folder|/};...
ENV H2_DATA_GIT_URL	    	none
# H2_DATA_GIT_REFRESH: Refresh the git-url database files at any start-up (false|true, default: false)
ENV H2_DATA_GIT_REFRESH    	false

# Install required system packages
RUN apt-get update && \
    apt-get install -y openssh-client openssh-known-hosts git && \
    apt-get clean all && \
	rm -rf /var/lib/apt/lists/*

# Create main temporary foders
RUN mkdir -p /root/downloads
RUN mkdir -p /root/patches
RUN mkdir -p /var/h2-database
RUN mkdir -p /var/h2-database/bin
RUN mkdir -p /var/h2-database/data

# Copy required scripts
COPY ./env.sh /root/downloads
COPY ./download-h2-database.sh /root/downloads
COPY ./install-h2-database.sh /root/downloads
COPY ./h2.sh /root/patches

# Change shell script permissions
RUN chmod +x /root/downloads/env.sh
RUN chmod +x /root/downloads/download-h2-database.sh
RUN chmod +x /root/downloads/install-h2-database.sh
RUN chmod 777 /root/patches/h2.sh
RUN chmod 666 /var/h2-database/data

# Change of Working Directory
WORKDIR /root/downloads

# Preparing ssh keys file in root folder for installation
COPY ./ssh.zip /root

# Perform H2 Database installation by custom scripts
RUN ./download-h2-database.sh
RUN ./install-h2-database.sh

# Copy bootstrap script
COPY ./start-h2.sh /var/h2-database/bin
RUN chmod 777 /var/h2-database/bin/start-h2.sh



# Remove unused installation shell scripts
RUN rm -f ./download-h2-database.sh
RUN rm -f ./install-h2-database.sh
RUN rm -f  ./env.sh

# Copy extra features script in binary folder
COPY ./check-repo-datafiles.sh /var/h2-database/bin
RUN chmod 777 /var/h2-database/bin/check-repo-datafiles.sh

# Removing temporary foders
RUN rm -Rf /root/downloads
RUN rm -Rf /root/patches

# Change of Working Directory
WORKDIR /var/h2-database

# Docker Image parameters ...
VOLUME /var/h2-database/data

EXPOSE 9090 65123 9023

ENTRYPOINT ["/bin/bash"]

CMD ["/var/h2-database/bin/start-h2.sh"]