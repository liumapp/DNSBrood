#!/bin/sh
mkdir -p /usr/local/DNSBrood/
mkdir -p /usr/local/DNSBrood/lib
if [ ! -d /usr/local/DNSBrood/config ]
then
mkdir -p /usr/local/DNSBrood/config
cp ./config/* /usr/local/DNSBrood/config/
fi
cp ./dnsbrood.sh /usr/local/DNSBrood/
cp ./target/DNSBrood*.jar /usr/local/DNSBrood/DNSBrood.jar
rsync -avz --delete ./target/lib/ /usr/local/DNSBrood/lib/

