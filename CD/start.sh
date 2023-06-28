#!/usr/bin/env bash

# 기본 변수
PROJECT_ROOT="/home/ubuntu/flag"
JAR_FILE="$PROJECT_ROOT/flag-webapp.jar"
TIME_NOW=$(date +%c)

# 로그 변수
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

# build 파일 복사
echo "$TIME_NOW > $JAR_FILE 파일 복사" >> $DEPLOY_LOG
cp $PROJECT_ROOT/build/libs/*.jar $JAR_FILE

# jar 파일 실행
echo "$TIME_NOW > $JAR_FILE 파일 실행" >> $DEPLOY_LOG
nohup java -jar $JAR_FILE &

# 실행한 애플리케이션 pid 출력
CURRENT_PID=$(pgrep -f $JAR_FILE)
echo "$TIME_NOW > 실행된 프로세스 pid는 $CURRENT_PID 입니다." >> $DEPLOY_LOG
