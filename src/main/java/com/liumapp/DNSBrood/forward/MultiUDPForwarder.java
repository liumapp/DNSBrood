package com.liumapp.DNSBrood.forward;

import com.liumapp.DNSBrood.config.Configure;
import com.liumapp.DNSBrood.connector.UDPConnectionResponser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xbill.DNS.Message;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.List;

/**
 * Created by liumapp on 7/15/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
@Component
public class MultiUDPForwarder implements Forwarder {

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private MultiUDPReceiver multiUDPReceiver;

    @Autowired
    private DNSHostsContainer dnsHostsContainer;

    @Autowired
    private Configure configure;

    /*
     * (non-Javadoc)
     *
     * @see us.codecraft.blackhole.forward.Forwarder#forward(byte[],
     * org.xbill.DNS.Message)
     */
    @Override
    public void forward(byte[] queryBytes, Message query,
                        UDPConnectionResponser responser) {
        // get address
        List<SocketAddress> allAvaliableHosts = dnsHostsContainer
                .getAllAvaliableHosts(query.getQuestion().getName().toString());
        forward(queryBytes, query, allAvaliableHosts, responser);
    }

    /*
     * (non-Javadoc)
     *
     * @see us.codecraft.blackhole.forward.Forwarder#forward(byte[],
     * org.xbill.DNS.Message, java.util.List)
     */
    @Override
    public void forward(byte[] queryBytes, Message query,
                        List<SocketAddress> hosts, UDPConnectionResponser responser) {
        if (logger.isDebugEnabled()) {
            logger.debug("forward query " + query.getQuestion().getName() + "_"
                    + query.getHeader().getID());
        }
        // send to all address

        int initCount = hosts.size();
        if (configure.getFakeDnsServer() != null) {
            // send fake dns query to detect dns poisoning
            hosts.add(0, configure.getFakeDnsServer());
        }
        ForwardAnswer forwardAnswer = new ForwardAnswer(query, responser, initCount);
        try {
            multiUDPReceiver.registerReceiver(query, forwardAnswer);
            try {
                for (SocketAddress host : hosts) {
                    send(queryBytes, host);
                    logger.debug("forward query "
                            + query.getQuestion().getName() + "_"
                            + query.getHeader().getID());
                }
            } catch (IOException e) {
                logger.warn("error", e);
            }
        } finally {
            multiUDPReceiver.delayRemoveAnswer(query, configure.getDnsTimeOut());
        }
    }

    private void send(byte[] queryBytes, SocketAddress host) throws IOException {
        DatagramChannel datagramChannel = multiUDPReceiver.getDatagramChannel();
        datagramChannel.send(ByteBuffer.wrap(queryBytes), host);
    }
}
