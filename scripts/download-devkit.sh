#!/bin/sh
DIR="."
BASE_URL="https://ftorelli-software-compliance-repository.s3-eu-west-1.amazonaws.com/flow-centric/PoC"

function download() {
	curl -L "$BASE_URL/$1" -o "$2/$1"
}

function install() {
	unzip -o -q "$2/$1" -d "$2"

    if [[ "n" != "$3" ]]; then
		rm -f "$2/$1"
	fi
}

function download_install() {
	download "$1" "$DIR" "$2"

	install "$1" "$DIR" "$2"
}

if [ -e ./dev-kit.zip ]; then
     install "dev-kit" "$DIR" "n"
else
     download_install "dev-kit.zip"
fi


mkdir -p $DIR/tmp
