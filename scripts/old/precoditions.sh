#!/bin/sh
CYGPRES="$(echo $PATH|grep cygwin)"
if [[ "$CYGPRES" == "" ]]; then
   echo "false" 
fi
echo "true"
