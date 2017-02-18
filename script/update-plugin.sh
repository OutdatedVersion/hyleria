#!/bin/bash

# Example args:
# update-plugin.sh ~/dev/mc/test-server/plugins/ Hyleria.jar ~/dev/projects/ultimate-uhc/spigot-plugin/target/Hyleria.jar


plugin_dir="$1"   # make sure you have the trailing /
plugin_name="$2"
update_file="$3"


if [ -f "$1$2" ]; then
 rm "$1$2"
 echo "removed outdated plugin"
fi


if [ -f "$update_file" ]; then
 cp "$update_file" "$plugin_dir"
else
 echo "missing update file.." && exit 1
fi
