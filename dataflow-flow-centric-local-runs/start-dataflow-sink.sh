#!/bin/sh
DIR="$(realpath -L "$(dirname "$0")")"
source $DIR/env
rm -Rf /opt/dataflow-flow-centric/log/dataflow-ms-flow-centric-sink-poc
echo "check  spring logs with: tail -f /opt/dataflow-flow-centric/log/dataflow-ms-flow-centric-sink-poc/dataflow-ms-flow-centric-sink-poc-spring.log"
echo "check service logs with: tail -f /opt/dataflow-flow-centric/log/dataflow-ms-flow-centric-sink-poc/dataflow-ms-flow-centric-sink-poc-derbvice.log"
$DIR/__dataflow_sink
