package com.liumapp.DNSBrood.config;

import com.liumapp.DNSQueen.queen.Queen;
import junit.framework.TestCase;

import java.io.IOException;

/**
 * Created by liumapp on 7/22/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
public class ZonesFileRefresherTest extends TestCase {

    public void testSaveOneZone() {
//        Queen queen = new Queen();
//        try {
//            queen.connect();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        queen.say("add_zones_ip_127.0.0.1:6.7.4.5_gmail.liumapp.com");
//        try {
//            System.out.println(queen.hear());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void testAdd () {
        Queen queen = new Queen();
        try {
            queen.connect();
            queen.say("add_zones_ip_LM09000:4.5.6.7_test2.liumapp.com");
            System.out.print(queen.hear());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testUpdate () {
        Queen queen = new Queen();
        try {
            queen.connect();
            queen.say("update_zones_ip_LM09000:4.5.6.8_test2.liumapp.com");
            System.out.print(queen.hear());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testSelect () {
        Queen queen = new Queen();
        try {
            queen.connect();
            queen.say("select_zones_ip_test2.liumapp.com");
            System.out.print(queen.hear());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testDel () {
        Queen queen = new Queen();
        try {
            queen.connect();
            queen.say("delete_zones_ip_LM0900:4.5.6.8_test2.liumapp.com");
            System.out.print(queen.hear());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 需要开启远程测试来执行
     */
    public void testMulDel () {
        Queen queen = new Queen();
        try {
            queen.connect();
            queen.say("delete_zones_ip_LM0900");
            System.out.print(queen.hear());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
