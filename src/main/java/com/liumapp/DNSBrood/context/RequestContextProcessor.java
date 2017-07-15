package com.liumapp.DNSBrood.context;

import java.net.DatagramPacket;

/**
 * Created by liumapp on 7/15/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
public class RequestContextProcessor {

    public static void processRequest(DatagramPacket datagramPacket) {
        RequestContext.setClientIps(datagramPacket.getAddress().getHostAddress());
    }
}
