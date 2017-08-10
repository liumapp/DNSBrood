# DNSBrood
A DNS server based on dnsjava .

### how to use

* create database 'brood' in mysql with utf-8 charset , and then create table from /sqlchema/mysql.sql 

* copy the project in your /usr/local/src/dnsbrood .

* chmod -R 777 make.sh .

* run make.sh .

* go to /usr/local/DNSBrood . 

* run dnsbrood.sh start / stop / reload .

### Attention

* only support Linux/Unix System

* make sure your system have rsync

* you will need root permission to run dnsbrood.sh , because system port of 53 require it.

### Debug

Fow How to Debug System , plz view [DNSBrood解析DNS过程中进行Debug](http://www.liumapp.com/articles/2017/07/12/1499822853171.html).

