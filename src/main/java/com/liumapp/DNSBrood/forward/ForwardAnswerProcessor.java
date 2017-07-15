package com.liumapp.DNSBrood.forward;

import com.liumapp.DNSBrood.antipollution.BlackListManager;
import com.liumapp.DNSBrood.antipollution.SafeHostManager;
import com.liumapp.DNSBrood.cache.CacheManager;
import com.liumapp.DNSBrood.config.Configure;
import com.liumapp.DNSBrood.utils.RecordUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xbill.DNS.Message;
import org.xbill.DNS.Record;
import org.xbill.DNS.Section;
import org.xbill.DNS.Type;

import java.io.IOException;
import java.net.SocketAddress;

/**
 * Created by liumapp on 7/15/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
@Service
public class ForwardAnswerProcessor {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private BlackListManager blackListManager;

    @Autowired
    private DNSHostsContainer dnsHostsContainer;

    @Autowired
    private ConnectionTimer connectionTimer;

    @Autowired
    private SafeHostManager safeBoxService;

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private Configure configure;

    public void handleAnswer(byte[] answer, Message message, SocketAddress remoteAddress, ForwardAnswer forwardAnswer)
            throws IOException {
        // fake dns server return an answer, it must be dns pollution
        if (configure.getFakeDnsServer() != null
                && remoteAddress.equals(configure.getFakeDnsServer())) {
            addToBlacklist(message);
            String domain = StringUtils.removeEnd(message.getQuestion()
                    .getName().toString(), ".");
            safeBoxService.setPoisoned(domain);
            return;
        }
        if (logger.isTraceEnabled()) {
            logger.trace("get message from " + remoteAddress + "\n" + message);
        }
        if (forwardAnswer == null) {
            logger.info("Received messages for "
                    + message.getQuestion().getName().toString()
                    + " after timeout!");
            return;
        }
        if (configure.isEnableSafeBox()) {
            answer = removeFakeAddress(message, answer);
        }

        if (answer != null) {
            forwardAnswer.decrCountDown();
            int order = dnsHostsContainer.getOrder(remoteAddress);
            if (!RecordUtils.hasAnswer(message)) {
                forwardAnswer.setTempAnswer(message);
                if (forwardAnswer.getCountDown() <= 0) {
                    forwardAnswer.getResponser().response(answer);
                    forwardAnswer.setTempAnswer(null);
                }
            } else {
                if (forwardAnswer.confirmProcess(order)) {
                    forwardAnswer.getResponser().response(answer);
                    if (logger.isDebugEnabled()) {
                        logger.debug("response message " + message.getHeader().getID()
                                + " from " + remoteAddress + " to "
                                + forwardAnswer.getResponser().getInDataPacket().getPort());
                    }
                    cacheManager.setResponseToCache(message, answer);
                }
            }
        }
    }

    private void addToBlacklist(Message message) {
        for (Record answer : message.getSectionArray(Section.ANSWER)) {
            String address = StringUtils.removeEnd(answer.rdataToString(), ".");
            if (!blackListManager.inBlacklist(address)) {
                logger.info("detected dns poisoning, add address " + address
                        + " to blacklist");
                blackListManager.addToBlacklist(address);
            }
        }
    }

    private byte[] removeFakeAddress(Message message, byte[] bytes) {
        Record[] answers = message.getSectionArray(Section.ANSWER);
        boolean changed = false;
        for (Record answer : answers) {
            String address = StringUtils.removeEnd(answer.rdataToString(), ".");
            if ((answer.getType() == Type.A || answer.getType() == Type.AAAA)
                    && blackListManager.inBlacklist(address)) {
                if (!changed) {
                    // copy on write
                    message = (Message) message.clone();
                }
                message.removeRecord(answer, Section.ANSWER);
                changed = true;
            }
        }
        if (changed && message.getQuestion().getType() == Type.A
                && (message.getSectionArray(Section.ANSWER) == null || message
                .getSectionArray(Section.ANSWER).length == 0)
                && (message.getSectionArray(Section.ADDITIONAL) == null || message
                .getSectionArray(Section.ADDITIONAL).length == 0)
                && (message.getSectionArray(Section.AUTHORITY) == null || message
                .getSectionArray(Section.AUTHORITY).length == 0)) {
            logger.info("remove message " + message.getQuestion());
            return null;
        }
        if (changed) {
            return message.toWire();
        }
        return bytes;
    }


}
