#!/bin/sh

./gradlew build
docker build -t flightexporter .
