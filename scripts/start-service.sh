#!/bin/bash
set -e
SCRIPTDIR=$(dirname ${BASH_SOURCE[0]} | xargs realpath)
PROJECTDIR=$(realpath $SCRIPTDIR/..)
ARGS=-Dspring-boot.run.profiles=live
cd $PROJECTDIR

function waitforHttp() {
  counter=0
  if [ -z "${@:2}" ]; then
    maxRetries=100
  else
    maxRetries=$2
  fi

  while [[ "$(curl --insecure -L -s -o /dev/null -w ''%{http_code}'' $1)" != "200" ]]; do
    if [[ "$counter" -ge "$maxRetries" ]]; then
      echo "Timeout waiting for " $1
      exit
    fi
    echo "waiting for $1 ($counter)"
    sleep 1
    counter=$((counter + 1))
  done
}

function start_application() {
  cd $1
  if [ -z "${@:2}" ]; then
    ARGS=""
  else
    ARGS="${@:2}"
  fi
  echo "Starting $1 $ARGS"
  export MAVEN_OPTS="-XX:+UseSerialGC -Xss256m -Xmx512m"
  mvn spring-boot:run $ARGS
  cd $PROJECTDIR

}

function build() {
    #BUILDCMD="mvn clean install -DskipTests"
    BUILDCMD="mvn clean install"
    echo "Build project without tests: $BUILDCMD "
    $BUILDCMD
}

function create_keystores() {
  if [ -z "$(ls -A $PROJECTDIR/keystores/*.jks)" ]; then
    echo "Keystore directory is empty, generating keystore"
    ./scripts/generate-keystore.sh
  fi
}

create_keystores
build
echo "Start mancala-project with pid and log files in $PROJECTDIR/target"

mkdir -p $PROJECTDIR/target
echo $$ >$PROJECTDIR/target/mancala.pid
truncate -s 0 $PROJECTDIR/target/mancala-api.log
truncate -s 0 $PROJECTDIR/target/mancala-web.log


start_application mancala-api $ARGS >$PROJECTDIR/target/mancala-api.log 2>&1 &
echo $! >$PROJECTDIR/target/mancala-api.pid

start_application mancala-web $ARGS >$PROJECTDIR/target/mancala-web.log 2>&1 &
echo $! >$PROJECTDIR/target/mancala-web.pid


echo "Wait until the mancala-api module is started (50 retries)"
waitforHttp "https://localhost:8080/actuator/health" 50

echo "Wait until the mancala-web is started (50 retries)"
waitforHttp "http://localhost:8082/actuator/health" 50


echo
echo "******************************************************"
echo "        All services are UP and Running! "
echo
echo "api is exposed on:https://127.0.0.1:8080/games/v1/mancala"
echo "web is exposed on:http://127.0.0.1:8082/games/v1/mancala/play"

echo "******************************************************"
