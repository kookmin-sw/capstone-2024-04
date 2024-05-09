#!/bin/sh

port=`lsof -i :8080 -t`

kill -9 $port
git checkout dev
git pull origin deploy
cp src/main/resources/devapplication.yml src/main/resources/application.yml
nohup ./gradlew bootRun > gradlew.log 2>&1 &
