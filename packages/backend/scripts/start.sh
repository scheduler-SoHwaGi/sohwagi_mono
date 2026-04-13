#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/app"
JAR_FILE="$PROJECT_ROOT/sohwagi-app.jar"
GC_LOG_PATH="$PROJECT_ROOT/gc.log"

APP_LOG="$PROJECT_ROOT/application.log"
ERROR_LOG="$PROJECT_ROOT/error.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)

FIREBASE_KEY_PATH="$PROJECT_ROOT/firebase-service-account.json"
VERTEX_KEY_PATH="$PROJECT_ROOT/vertexai-service-account.json"

if [ ! -f "$FIREBASE_KEY_PATH" ]; then
  echo "$TIME_NOW > ❌ Firebase key not found at $FIREBASE_KEY_PATH" >> $DEPLOY_LOG
  exit 1
fi
if [ ! -f "$VERTEX_KEY_PATH" ]; then
  echo "$TIME_NOW > ❌ Vertex AI key not found at $VERTEX_KEY_PATH" >> $DEPLOY_LOG
  exit 1
fi

export FirebaseKey="$FIREBASE_KEY_PATH"
export GOOGLE_APPLICATION_CREDENTIALS="$VERTEX_KEY_PATH"

echo "$TIME_NOW > ✅ FirebaseKey set to $FirebaseKey" >> $DEPLOY_LOG
echo "$TIME_NOW > ✅ GOOGLE_APPLICATION_CREDENTIALS set to $GOOGLE_APPLICATION_CREDENTIALS" >> $DEPLOY_LOG

# jar 파일 실행
nohup java -Xlog:gc*:file=$GC_LOG_PATH:time,uptimemillis:filecount=10,filesize=10m -Duser.timezone=Asia/Seoul -jar $JAR_FILE > $APP_LOG 2> $ERROR_LOG &

CURRENT_PID=$(pgrep -f $JAR_FILE)
echo "$TIME_NOW > 실행된 프로세스 PID: $CURRENT_PID" >> $DEPLOY_LOG
