#!/bin/bash

# Example args:
# run-server.sh "-Xmx2G -Xms1G -Dcom.mojang.eula.agree=true"

java "$1" -jar paper.jar

