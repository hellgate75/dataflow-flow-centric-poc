#!/bin/sh
PING_SK_SRV_PORT="$(netstat -an|grep 7577)"
PING_DF_SRV_PORT="$(netstat -an|grep 9393)"
PING_CFG_SRV_PORT="$(netstat -an|grep 8899)"
PING_ZK_SRV_PORT="$(netstat -an|grep 2181)"
PING_AK_SRV_PORT="$(netstat -an|grep 9092)"
if [[ "" != "$PING_ZK_SRV_PORT" ]]; then
   echo "Flow Centric Apache Kafka's Zookeeper Server is ACTIVE"
   echo "$PING_ZK_SRV_PORT"
else
   echo "Flow Centric Apache Kafka's Zookeeper Server is OFFLINE"
fi
if [[ "" != "$PING_AK_SRV_PORT" ]]; then
   echo "Flow Centric Apache Kafka Server is ACTIVE"
   echo "$PING_AK_SRV_PORT"
else
   echo "Flow Centric Apache Kafka Server is OFFLINE"
fi
if [[ "" != "$PING_CFG_SRV_PORT" ]]; then
   echo "Flow Centric Spring Cloud Config Server is ACTIVE"
   echo "$PING_CFG_SRV_PORT"
else
   echo "Flow Centric Spring Cloud Config Server is OFFLINE"
fi
if [[ "" != "$PING_DF_SRV_PORT" ]]; then
   echo "Spring Cloud Data Flow Server is ACTIVE"
   echo "$PING_DF_SRV_PORT"
else
   echo "Spring Cloud Data Flow Server is OFFLINE"
fi
if [[ "" != "$PING_SK_SRV_PORT" ]]; then
   echo "Spring Cloud Skipper Server is ACTIVE"
   echo "$PING_SK_SRV_PORT"
else
   echo "Spring Cloud Skipper Server is OFFLINE"
fi

