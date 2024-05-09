#!/bin/sh

#port=`lsof -i :8080 -t`

#kill -9 $port
git checkout server/deploy
git pull origin server/deploy
cp src/main/resources/devapplication.yml src/main/resources/application.yml
./gradlew build -x test
docker-compose up build -d
#nohup ./gradlew bootRun > gradlew.log 2>&1 &
