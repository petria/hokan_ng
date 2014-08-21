#!/bin/sh

TARGET_DIR=hokan_ng-core-io/target/
#DEPLOY_DIR=/home/petria/code/Java/git/liferay-portal-6.1.1-ce-ga2/tomcat-7.0.27/webapps/
DEPLOY_DIR=../webapps/

cd $TARGET_DIR
ORIG_FILE=`ls -t1 hokan_ng-core-io*.war | head -1`
cd - >/dev/null

#DEPLOY_FILE=`echo "$ORIG_FILE" | sed 's/hokan_ng-core-http.*.war/hokan_ng-core-http.war/g'`
DEPLOY_FILE=$ORIG_FILE

echo ""

echo "$ORIG_FILE -> $DEPLOY_FILE"

cp -v $TARGET_DIR$ORIG_FILE $DEPLOY_DIR$DEPLOY_FILE
