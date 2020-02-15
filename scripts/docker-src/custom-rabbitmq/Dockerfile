FROM rabbitmq:3.8-rc

RUN apt-get update && \
	apt-get install -y net-tools curl vim && \
    apt-get clean all && \
	rm -rf /var/lib/apt/lists/*

COPY ./custom-definitions.json /root

COPY ./sync-definitions.sh /root

COPY ./bootstrap-sync-definitions.sh /root

COPY ./wrapper.sh /usr/local/bin/wrapper.sh

RUN chmod 777 /root/*sync-definitions.sh

RUN chmod 777 /usr/local/bin/

RUN echo "sh -c /root/bootstrap-sync-definitions.sh" >> "$(which docker-entrypoint.sh)"

WORKDIR /var/lib/rabbitmq

RUN rabbitmq-plugins enable --offline rabbitmq_management

CMD ["wrapper.sh"]