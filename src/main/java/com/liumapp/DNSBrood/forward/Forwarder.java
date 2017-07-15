package com.liumapp.DNSBrood.forward;

import com.liumapp.DNSBrood.connector.UDPConnectionResponser;
import org.xbill.DNS.Message;

import java.net.SocketAddress;
import java.util.List;

/**
 * Created by liumapp on 7/15/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
public interface Forwarder {

    /**
     * Forward query bytes to external DNS host(s) and get a valid DNS answer.
     *
     * @param queryBytes
     * @param query
     * @return
     */
    public void forward(final byte[] queryBytes, Message query,
                        UDPConnectionResponser responser);

    /**
     * Forward query bytes to external DNS host(s) and get a valid DNS answer.
     *
     * @param queryBytes
     * @param query
     * @return
     */
    public void forward(final byte[] queryBytes, Message query,
                        List<SocketAddress> hosts, UDPConnectionResponser responser);

}
