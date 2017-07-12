#!/bin/sh
mkdir -p /usr/local/blackhole/
mkdir -p /usr/local/blackhole/lib
if [ ! -d /usr/local/blackhole/config ]
then
mkdir -p /usr/local/blackhole/config
cp ./config/* /usr/local/blackhole/config/
fi
cp ./blackhole.sh /usr/local/blackhole/
cp ./target/blackhole*.jar /usr/local/blackhole/blackhole.jar
rsync -avz --delete ./target/lib/ /usr/local/blackhole/lib/






