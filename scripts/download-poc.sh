#!/bin/sh
DIR="."
BASE_URL="https://ftorelli-software-compliance-repository.s3-eu-west-1.amazonaws.com/flow-centric/PoC"

function download() {
	curl -L "$BASE_URL/$1" -o "$2/$1"
}

function install() {
	unzip "$2/$1" -d "$2"

	rm -f "$2/$1"
}

function download_install() {
	download "$1" "$DIR"

	install "$1" "$DIR"
}

download_install "docker.zip"

download_install "utils.zip"

download_install "docker-src.zip"

download_install "scripts.zip"

mkdir -p $DIR/tmp
