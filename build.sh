#!/usr/bin/env bash

CurrentDir=`pwd`
find $CurrentDir -name "build" | xargs rm -rf                                                                                                                                 [±develop ●●●]
$CurrentDir/gradlew clean  build

