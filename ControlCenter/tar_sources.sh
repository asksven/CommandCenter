#!/bin/bash

if [ -d ./SystemSettings ]; then 
  VERSION=`cat SystemSettings/AndroidManifest.xml | grep "versionName" | sed 's/"/ /g' | awk {'print $2'}`
  tar -czf SystemSettings_src_${VERSION}.tar.gz ./SystemSettings/ --exclude=./SystemSettings/bin/*
  tar -czf SystemSettings_${VERSION}.tar.gz ./SystemSettings/bin/SystemSettings.apk
else
  echo "you must be in your workspace dir to tar sources"
fi
