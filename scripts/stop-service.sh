#!/bin/bash

SCRIPTDIR=$(dirname ${BASH_SOURCE[0]} | xargs realpath)
PROJECTDIR=$(realpath $SCRIPTDIR/..)
TARGETDIR=$PROJECTDIR/target

echo "Stopping all processes"

kill -TERM -- -$(cat $TARGETDIR/mancala.pid) || true
