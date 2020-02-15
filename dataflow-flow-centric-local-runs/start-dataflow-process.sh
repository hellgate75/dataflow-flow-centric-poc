#!/bin/sh
DIR="$(realpath -L "$(dirname "$0")")"
source $DIR/env
rm -Rf /opt/dataflow-flow-centric/log/dataflow-ms-flow-centric-process-poc
echo "check  spring logs with: tail -f /opt/dataflow-flow-centric/log/dataflow-ms-flow-centric-process-poc/dataflow-ms-flow-centric-process-poc-spring.log"
echo "check service logs with: tail -f /opt/dataflow-flow-centric/log/dataflow-ms-flow-centric-process-poc/dataflow-ms-flow-centric-process-poc-derbvice.log"
$DIR/__dataflow_process
