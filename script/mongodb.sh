#!/bin/bash

# Setup MongoDB - super small script, but I forget about this a lot so
#
# Example usage:
# mongodb.sh "local_tests" "Lh()BvHMs6gb"

db="hyleria"
name="$1"
password="$2"

mongo --eval 'db.createUser( { user: "$db", pwd: "$password", roles: [ { role: "readWrite", db: "$db" } ] } )'
mongo --eval 'db.accounts.createIndex( { uuid: 1, name_lower: 1 } )' "$db"
