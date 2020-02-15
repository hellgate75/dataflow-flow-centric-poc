#!/bin/sh
CYGWIN_HOME="$(find /c/ -maxdepth 2 -name cygwin 2> /dev/null)"
if [[ "$CYGWIN_HOME" == "" ]]; then
   CYGWIN_HOME="$(find /c/ -maxdepth 2 -name cygwin32 2> /dev/null)"
   if [[ "$CYGWIN_HOME" == "" ]]; then
      CYGWIN_HOME="$(find /c/ -maxdepth 2 -name cygwin64 2> /dev/null)"
      if [[ "$CYGWIN_HOME" == "" ]]; then
         echo "Please install CYGWIN on C drive, possibly in 64-bit version"
      fi
   fi
fi
if [[ -e "$CYGWIN_HOME/bin" ]]; then
   IS_IN_PATH="$(echo "$PATH"|grep $CYGWIN_HOME/bin)"
   if [[ "$IS_IN_PATH" == "" ]]; then
      export PATH="$PATH:$CYGWIN_HOME/bin"
   fi
   if [[ "$BASE_DIR" == "" ]]; then
		BASE_DIR="$(pwd)"
   fi
   cp $BASE_DIR/xbin/nohup* $CYGWIN_HOME/bin/

else
   echo "Please re-install CYGWIN on C drive, possibly in 64-bit version. The bin folder is not accessible"
fi
