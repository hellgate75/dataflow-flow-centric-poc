#!/bin/sh

PWD="$(pwd)"
FOLDER="$(dirname "$(realpath "$0")")"

function getBaseSysPath() {
	if [[ -e /c/ ]]; then
		echo "/c"
	else
		echo ""
	fi
}

function isWindows() {
	if [[ "" == "$(uname -a|grep MINGW)" ]]; then
		echo "no";
	fi
		echo "yes";
}

function makeQuestion() {
		echo -e "$1? [Yes/No]"
		if [[ "" != "$2" ]]; then
			echo -e "$2"
		fi
}

function askForYesOrNo() {
	while [ "$choice" == "" ]; do
		read choice
		if [[ "" == "$(echo $choice | grep -i Y)" ]] && \
		   [[ "" == "$(echo $choice | grep -i N)" ]]; then
		   choice=""
		fi
	done
	if [[ "" != "$(echo $choice | grep -i N)" ]]; then
	   echo "no"
	else
	   echo "yes"
	fi

}

echo "Start PoC procedure in progress..."

DOCKER="yes"
DOCKER_COMPOSE="yes"
DOCKER_MACHINE="yes"
if [[ "" = "$(which docker)" ]]; then
	DOCKER="no"
	echo ""
	echo "No docker binaries found ..."
	echo ""
fi

if [[ "" = "$(which docker-compose)" ]]; then
	echo ""
	DOCKER_COMPOSE="no"
	echo "No docker compose binaries found ..."
	echo ""
fi

if [[ "no" == "$DOCKER" ]] && [[ "" = "$(which docker-machine)" ]]; then
	echo ""
	DOCKER_MACHINE="no"
	echo "No docker machine binaries found ..."
	echo ""
fi
INSTALL="no"
if [[ "no" == "$DOCKER" ]]; then
	makeQuestion "You do not have installed any docker version, do you want to progress" "(In the case we have to install docker now)"
	INSTALL="$(askForYesOrNo)"
fi

if [[ "no" == "$INSTALL" ]] && [[ "no" == "$DOCKER" ]]; then
	echo ""
	echo "Cannot continue the PoC execution, the exit."
	echo ""
	echo "Installation procedure aborted!!"
	exit 1
fi

if [[ "yes" == "$INSTALL" ]]; then
	echo ""
	echo "docker installation start ..."
	echo ""
	if [[ "yes" != "$(isWindows)" ]]; then
		echo "We are sorry, we do not know this window shell or you are running this script over linux."
		echo "In the latter care, linux verion has not been implemented yet."
		exit 1
	fi
	if [[ -e $FOLDER/tmp ]]; then
		rm -Rf $FOLDER/tmp
	fi
	mkdir $FOLDER/tmp
	echo "Start downloading installation binaries ..."
	echo ""
	curl -L https://ftorelli-software-compliance-repository.s3-eu-west-1.amazonaws.com/flow-centric/PoC/docker.zip -o $FOLDER/tmp/docker.zip
	if [[ ! -e  $FOLDER/tmp/docker.zip ]]; then
	echo ""
		echo ""
		echo "Something went wrong with download of binaries. Please try again"
		echo ""
		echo "Installation procedure aborted!!"
		exit 2
	fi
	DEST_FOLDER="$(getBaseSysPath)"
	OUTDIR="$DEST_FOLDER"
	if [[ -e $FOLDER/tmp ]]; then
		rm -Rf $OUTDIR
	fi
	mkdir $OUTDIR
	cd $FOLDER/tmp
	unzip -q ./docker.zip -d $OUTDIR
	rm -Rf $FOLDER/tmp
	DOCKER_DIR="$OUTDIR/docker"
	cd $DOCKER_DIR
	VBOXMAN="yes"
	VBOXMAN_PATH="C:\\Program Files\\Oracle\\VirtualBox\\vboxmanage"
	if [[ "" == "$(ls "$VBOXMAN_PATH" 2> /dev/null )" ]]; then
			echo ""
			echo "Please install Oracle VirtualBox v. 6.1 in order to instal docker!!"
			echo "Try with experimental script: install-virtualbox.sh"
			echo ""
			makeQuestion "Do you want to install VirtualBox"
			INSTALL="$(askForYesOrNo)"
			if [[ "yes" == "$INSTALL" ]]; then
				sh $DOCKER_DIR/install-virtualbox.sh
			fi
			if [[ "" == "$(ls "$VBOXMAN_PATH" 2> /dev/null )" ]]; then
				VBOXMAN="no"
			fi
	fi
	if [[ "no" == "$VBOXMAN" ]]; then
		echo "Unable to install virtualbox in your system, so Poc cannot be perfomed."
		exit 3
	fi
	sh $DOCKER_DIR/install-docker.sh
	echo ""
	echo "Please add '$DOCKER_DIR' to your path for future use of the docker product"
	echo ""
	export PATH=$PATH:$DOCKER_DIR
	if [[ "" = "$(which docker)" ]]; then
		DOCKER="no"
		echo ""
		echo "No docker binaries found ..."
		echo ""
		echo "Installation procedure aborted!!"
		exit 2
	fi
	cd $FOLDER
fi

if [[ "" == "$(docker network ls|grep flowcentric)" ]]; then
	makeQuestion "Do you want to create the compose now" "(You can start re run this script at any time)"
	CREATE="$(askForYesOrNo)"
	if [[ "yes" == "$CREATE" ]]; then
		echo "PoC has not been created yet, now proceeding with creation"
		sh $FOLDER/create-compose.sh
	fi
else
	if [[ "" == "$(docker ps|grep rabbitmq|grep 3.8-rc-management-flow-centric)" ]]; then
		makeQuestion "Do you want to start the compose now" "(You can start and stop the architecture at any time using provided commands)"
		START="$(askForYesOrNo)"
		if [[ "yes" == "$START" ]]; then
			echo "PoC has  been created yet, but it's not running."
			echo "Now starting compose for you!!"
			sh $FOLDER/start-compose.sh
		else
			makeQuestion "Do you want to destroy the compose now" "(You can start re run this script at any time)"
			DESTROY="$(askForYesOrNo)"
			if [[ "yes" == "$DESTROY" ]]; then
				echo "PoC has  been destroyed now..."
				echo "Now destroying compose for you!!"
				sh $FOLDER/destroy-compose.sh
			fi
		fi
	else
		echo "PoC has  been created yet, and it's running."
		echo "Enjoy your experience!!"
	fi
fi

echo "Start PoC procedure complete!!"
