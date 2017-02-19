#!/bin/bash

# Setup MongoDB - super small script, but I forget about this a lot so
#
# Example usage:
# mongodb.sh "hyleria" "local_tests" "Lh()BvHMs6gb"

db="$1"
name="$2"
password="$3"

mongo --eval 'db.createUser( { user: "$db", pwd: "$password", roles: [ { role: "readWrite", db: "$db" } ] } )'
