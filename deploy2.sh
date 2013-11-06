#!/bin/sh

TARGET_DIR=hokan_ng-engine-http/target/
DEPLOY_DIR=webapps/

cd $TARGET_DIR
ORIG_FILE=`ls -t1 *.war | head -1`
cd - >/dev/null

DEPLOY_FILE=`echo "$ORIG_FILE" | sed 's/hokan_ng-engine-http.*war/hokan_ng-core-engine.war/g'`

echo ""

echo "$ORIG_FILE -> $DEPLOY_FILE"

cp -v $TARGET_DIR$ORIG_FILE $DEPLOY_DIR$DEPLOY_FILE
