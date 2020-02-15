#!/bin/sh
if [[ "" != "$(which docker-machine)" ]]; then
   docker-machine ip default
fi
