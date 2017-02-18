#!/bin/bash

# whether or not we're checking if the provided
# services are running. expects: y
check_services="$1"

if [ "$check_services" -eq "y"  ]; then
  echo "verifying correct services.."
  declare -a check_running=("redis-server" "mongod")


  for service in "${check_running[@]}"
  do
    if [ ps ax | grep -v grep | grep "$service" > /dev/null]; then
       echo "$service is running"
      else
       echo "$service not found.." && exit 1
  done
fi
