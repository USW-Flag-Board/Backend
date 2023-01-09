#!/usr/bin/env bash

# 기본 변수
PROJECT_ROOT="/home/ubuntu/flag"
JAR_FILE="$PROJECT_ROOT/flag-webapp.jar"
TIME_NOW=$(date +%c)

# 로그 변수
APP_LOG="$PROJECT_ROOT/application.log"
ERROR_LOG="$PROJECT_ROOT/error.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

# build 파일 복사
echo "$TIME_NOW > $JAR_FILE 파일 복사" >> $DEPLOY_LOG
cp $PROJECT_ROOT/build/libs/*.jar $JAR_FILE

# 현재 구동 중인 애플리케이션 pid 확인
echo "> 현재 실행중인 애플리케이션 pid 확인" >> $DEPLOY_LOG
CURRENT_PID=$(lsof -i :8080)

# 프로세스가 켜져 있으면 재시작
if [ -z $CURRENT_PID ]; then
  echo "$TIME_NOW > 현재 실행중인 애플리케이션이 없습니다." >> $DEPLOY_LOG
else
  echo "$TIME_NOW > 실행중인 $CURRENT_PID 애플리케이션 종료하고 재시작합니다. " >> $DEPLOY_LOG
  kill -15 $CURRENT_PID
  # 종료될 때까지 대기
  sleep 5
fi

# jar 파일 실행
echo "$TIME_NOW > $JAR_FILE 파일 실행" >> $DEPLOY_LOG
nohup java -jar $JAR_FILE > $APP_LOG 2> $ERROR_LOG &