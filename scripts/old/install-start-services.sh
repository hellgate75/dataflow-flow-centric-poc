#!/bin/sh -e
BASE_DIR="$(pwd)"
download-scripts/download-binaries.sh
RES="$?"
if [[ "$RES" == "0" ]]; then
    echo "Binaries download completed successfully!!"
else
    echo "Binaries download interrupted by errors!!"
fi
installation-scripts/install-servers.sh
RES="$?"
if [[ "$RES" == "0" ]]; then
    echo "Installation completed successfully!!"
else
    echo "Installation interrupted by errors!!"
fi
./start-services.sh
RES="$?"
if [[ "$RES" == "0" ]]; then
    echo "Service Start-Up completed successfully!!"
else
    echo "Service Start-Up interrupted by errors!!"
fi
