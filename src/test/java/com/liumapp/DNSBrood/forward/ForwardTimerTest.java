package com.liumapp.DNSBrood.forward;

import org.junit.Test;

/**
 * Created by liumapp on 7/15/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
public class ForwardTimerTest {

    private ConnectionTimer forwardTimer = new ConnectionTimer();

    @Test
    public void testCheckForwardTimeForAddress() {
        System.out.println(forwardTimer
                .checkConnectTimeForAddress("173.194.72.106"));
    }

}
