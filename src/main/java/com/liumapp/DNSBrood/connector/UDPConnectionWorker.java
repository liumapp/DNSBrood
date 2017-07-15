package com.liumapp.DNSBrood.connector;

import com.liumapp.DNSBrood.container.QueryProcesser;
import com.liumapp.DNSBrood.context.RequestContextProcessor;
import com.liumapp.DNSBrood.forward.Forwarder;
import org.apache.log4j.Logger;
import org.xbill.DNS.Message;

import java.net.DatagramPacket;

/**
 * Created by liumapp on 7/15/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
public class UDPConnectionWorker implements Runnable {

    private static final Logger logger = Logger
            .getLogger(UDPConnectionWorker.class);

    private final UDPConnectionResponser responser;
    private final DatagramPacket inDataPacket;

    private QueryProcesser queryProcesser;
    private Forwarder forwarder;

    public UDPConnectionWorker(DatagramPacket inDataPacket,
                               QueryProcesser queryProcesser, UDPConnectionResponser responser,
                               Forwarder forwarder) {
        super();
        this.responser = responser;
        this.inDataPacket = inDataPacket;
        this.queryProcesser = queryProcesser;
        this.forwarder = forwarder;
    }

    public void run() {

        try {

            RequestContextProcessor.processRequest(inDataPacket);
            byte[] response = queryProcesser.process(inDataPacket.getData());
            if (response != null) {
                responser.response(response);
            } else {
                Message query = new Message(
                        inDataPacket.getData());
                forwarder.forward(query.toWire(), query, responser);
            }
        } catch (Throwable e) {

            logger.warn(
                    "Error processing UDP connection from "
                            + inDataPacket.getSocketAddress() + ", ", e);
        }
    }
}
