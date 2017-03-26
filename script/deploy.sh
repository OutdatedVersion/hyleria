#!/bin/bash

#
# Upload the provided item to the remote location
# Maybe run a script to execute a bash file on the remote to publish a Redis message asking for a restart?
#
# Example: ./deploy.sh "host.example.com" "22" "ben" "~/.ssh/key.pem" 
#                      "/home/ben/dev/projects/thing/Archive.jar"
#                      "/home/mc/place"
#

HOST="$1"
PORT="$2"
USER="$3"
KEY="$4"
LOCAL_ITEM="$5"
REMOTE_PATH="$6"


echo "Uploading $LOCAL_ITEM to $HOST"
echo 
scp -P $PORT -i $KEY $LOCAL_ITEM $USER@$HOST:"$REMOTE_PATH"
