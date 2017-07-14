package com.liumapp.DNSBrood.context;

/**
 * Created by liumapp on 7/14/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
public class RequestContext {

    private static ThreadLocal<String> clientIps = new ThreadLocal<String>();

    public static String getClientIp() {
        return clientIps.get();
    }

    public static void setClientIps(String clientIp){
        clientIps.set(clientIp);
    }


}
