package com.liumapp.DNSBrood.forward;

import com.liumapp.DNSBrood.answer.container.DomainPatternsContainer;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liumapp on 7/15/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
@Component
public class DNSHostsContainer {

    private DomainPatternsContainer domainPatternsContainer;

    private int timeout = 3000;

    private int order;

    private Map<SocketAddress, Integer> requestTimes = new ConcurrentHashMap<SocketAddress, Integer>();

    private Logger logger = Logger.getLogger(getClass());

    public void clearHosts() {
        requestTimes = new ConcurrentHashMap<SocketAddress, Integer>();
        order = 0;
    }

    public void addHost(SocketAddress address) {
        requestTimes.put(address, order++);
        logger.info("add dns address " + address);
    }

    public int getOrder(SocketAddress socketAddress) {
        Integer order = requestTimes.get(socketAddress);
        return order == null ? 0 : order;
    }

    /**
     * @param timeout
     *            the timeout to set
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public List<SocketAddress> getAllAvaliableHosts(String domain) {
        String ip = domainPatternsContainer.getIp(domain);
        if (ip != null) {
            List<SocketAddress> socketAddresses = new ArrayList<SocketAddress>();
            socketAddresses.add(new InetSocketAddress(ip, 53));
            return socketAddresses;
        }
        List<SocketAddress> results = new ArrayList<SocketAddress>();
        Iterator<Map.Entry<SocketAddress, Integer>> iterator = requestTimes.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<SocketAddress, Integer> next = iterator.next();
            results.add(next.getKey());
        }
        return results;
    }

    public void setDomainPatternsContainer(DomainPatternsContainer domainPatternsContainer) {
        this.domainPatternsContainer = domainPatternsContainer;
    }

}
