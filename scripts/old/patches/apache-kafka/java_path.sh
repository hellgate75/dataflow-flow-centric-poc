#!/bin/sh
TMP="$(which java)"
JP=""
IFS="/";for tkn in $TMP; do 
   if [[ "$tkn" != "java" ]] && [[ "$tkn" != "" ]]; then 
      JP="$JP/$tkn" 
   fi 
done
export JAVA_HOME="$JP"
echo "$JAVA_HOME"
