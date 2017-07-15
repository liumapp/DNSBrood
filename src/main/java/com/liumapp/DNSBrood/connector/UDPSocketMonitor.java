package com.liumapp.DNSBrood.connector;

import com.liumapp.DNSBrood.concurrent.ThreadPools;
import com.liumapp.DNSBrood.container.QueryProcesser;
import com.liumapp.DNSBrood.forward.Forwarder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;

/**
 * Created by liumapp on 7/15/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
public class UDPSocketMonitor extends Thread {

    private Logger log = Logger.getLogger(this.getClass());

    private InetAddress addr;
    private int port;
    private static final short udpLength = 512;
    private DatagramSocket socket;
    @Autowired
    private QueryProcesser queryProcesser;
    @Autowired
    private Forwarder forwarder;
    @Autowired
    private ThreadPools threadPools;

    public UDPSocketMonitor(String host, int port) {
        super();
        try {
            this.addr = Inet4Address.getByName(host);
            this.port = port;
            socket = new DatagramSocket(port, addr);
        } catch (IOException e) {
            System.err.println("Startup fail, 53 port is taken or has no privilege. Check if you are running in root, or another DNS server is running.");
            log.error("Startup fail, 53 port is taken or has no privilege", e);
            System.exit(-1);
        }

        this.setDaemon(true);
    }

    @Override
    public void run() {
        ExecutorService executorService = threadPools.getMainProcessExecutor();
        log.info("Starting UDP socket monitor on address "
                + this.getAddressAndPort());

        while (true) {
            try {

                byte[] in = new byte[udpLength];
                DatagramPacket indp = new DatagramPacket(in, in.length);
                indp.setLength(in.length);
                socket.receive(indp);
                executorService.execute(new UDPConnectionWorker(indp,
                        queryProcesser,
                        new UDPConnectionResponser(socket, indp), forwarder));
            } catch (SocketException e) {

                // This is usally thrown on shutdown
                log.debug("SocketException thrown from UDP socket on address "
                        + this.getAddressAndPort() + ", " + e);
                break;
            } catch (IOException e) {

                log.info("IOException thrown by UDP socket on address "
                        + this.getAddressAndPort() + ", " + e);
            }
        }
        log.info("UDP socket monitor on address " + getAddressAndPort()
                + " shutdown");
    }

    public String getAddressAndPort() {

        return addr.getHostAddress() + ":" + port;
    }

}
