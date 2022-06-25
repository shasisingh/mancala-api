#!/bin/bash

echo "Bash version ${BASH_VERSION}..."

SCRIPTDIR=$(dirname ${BASH_SOURCE[0]} | xargs realpath)
PROJECTDIR=$(realpath $SCRIPTDIR/..)
PASSWORD=vt56@612

CURRENTDIR=$(pwd)

echo "PROJECT directory :$PROJECTDIR"

echo "Goto script directory $SCRIPTDIR"

cd "$SCRIPTDIR"

function create_keystore {
  echo "Creating a keystore for $1"
  keytool -genkey -alias "$1" -dname "cn=home-project, ou=my-project, o=test, c=NL" -keyalg RSA -keysize 2048 -keystore "$1"-keystore.p12 -ext SAN=dns:localhost,IP:127.0.0.1 -ext KeyUsage=digitalSignature,keyEncipherment,keyCertSign -storepass $PASSWORD -keypass $PASSWORD -validity 365 -noprompt -storetype PKCS12
}

function export_cert {
  echo "Exporting a certificate for $1"
  keytool -export -alias "$1" -file "$1".cer -keystore "$1"-keystore.p12 -storepass $PASSWORD -keypass $PASSWORD
}

function import_cert_into_truststore {
  echo "Importing $2 into truststore $1"
  keytool -import -alias "$2" -file "$2".cer -keystore "$1"-truststore.p12 -storepass $PASSWORD -keypass $PASSWORD -noprompt
}

function formatString() {
    VAR=""
    for i in {1..50}
    do
        VAR+="$1"
    done
    echo "$VAR"
}

function move_to_project_keystores {
  TARGETDIR=$PROJECTDIR/keystores/
  mkdir -p "$TARGETDIR"
  echo "move $1*-keystore.p12 and $1-truststore.p12 to $TARGETDIR"
  if ls ./$1*-keystore.p12 1>/dev/null 2>&1; then
    cp $1*-keystore.p12 $TARGETDIR
    rm $1*-keystore.p12
  fi
  if [ -f "./$1-truststore.p12" ]; then
    cp $1-truststore.p12 $TARGETDIR
    rm $1-truststore.p12
  fi
}


create_keystore mancala-api-web
create_keystore mancala-web-client
create_keystore mancala-web

export_cert mancala-api-web
export_cert mancala-web-client

import_cert_into_truststore mancala-api mancala-web-client
import_cert_into_truststore mancala-web mancala-api-web

move_to_project_keystores  mancala-api
move_to_project_keystores  mancala-web

rm *.cer

formatString "*"

echo "Return to directory $CURRENTDIR"

cd $CURRENTDIR
