#!/bin/bash

./gradlew runClient --console plain | tee latest-client-new.log
grep -E -v -i "DEBUG|Entry" latest-client-new.log > latest-client-reduced.log
read -n 1