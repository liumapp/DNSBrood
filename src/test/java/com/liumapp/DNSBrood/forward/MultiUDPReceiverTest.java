package com.liumapp.DNSBrood.forward;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by liumapp on 7/15/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
public class MultiUDPReceiverTest {

    private MultiUDPReceiver multireReceiver;

    @Before
    public void setUp() {
        multireReceiver = new MultiUDPReceiver();
    }

    @Test
    public void testReach() throws IOException {
        InetAddress inetAddress = InetAddress.getByName("203.161.230.171");
        System.out.println(inetAddress.isReachable(1000));
    }

}
