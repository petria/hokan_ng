#!/bin/sh

TARGET_DIR=hokan_ng-core-http/target/
DEPLOY_DIR=webapps/

cd $TARGET_DIR
ORIG_FILE=`ls -t1 *.war | head -1`
cd - >/dev/null

DEPLOY_FILE=`echo "$ORIG_FILE" | sed 's/hokan_ng-core-http.*.war/hokan_ng-core-http.war/g'`

echo ""

echo "$ORIG_FILE -> $DEPLOY_FILE"

echo "cp $TARGET_DIR$ORIG_FILE $DEPLOY_DIR$DEPLOY_FILE"


cp $TARGET_DIR$ORIG_FILE $DEPLOY_DIR$DEPLOY_FILE
