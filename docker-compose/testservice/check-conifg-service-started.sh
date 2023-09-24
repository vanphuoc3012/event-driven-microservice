#!/bin/bash

apt-get update -y

yes | apt-get install curl

curlResult=$(curl -s -o /dev/null -I -w "%{http_code}" http://config-server:8888/actuator/health)

echo "Result status code: " + $curlResult

files=$(ls ../cnb/process)

for f in $files
do
  echo $f
done

a=0
while [ "$curlResult" != "200" ];
 do
  echo $a
    if [ $a -eq 30 ]
      then
        echo "Failed to connect to config server!!"
        echo $a
        exit 1
    fi
  >&2 echo "Config server is not start up yet!!"
  sleep 2
  curlResult=$(curl -s -o /dev/null -I -w "%{http_code}" http://config-server:8888/actutor/health)
  a=$(($a+1))
done


../cnb/process/web