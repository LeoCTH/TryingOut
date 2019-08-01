#!/bin/bash

./gradlew runServer --console plain | tee latest-server.log
grep -E -v -i "DEBUG|Entry" latest-server.log > latest-server-reduced.log
read -n 1