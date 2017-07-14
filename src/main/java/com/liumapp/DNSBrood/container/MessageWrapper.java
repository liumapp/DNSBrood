package com.liumapp.DNSBrood.container;

import org.xbill.DNS.Message;

/**
 * Created by liumapp on 7/14/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 * DNS message wrapper
 */
public class MessageWrapper {

    private Message message;

    private boolean hasRecord;

    /**
     * @param message
     */
    public MessageWrapper(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("Message should not be null!");
        }
        this.hasRecord = false;
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public boolean hasRecord() {
        return hasRecord;
    }

    public void setHasRecord(boolean hasRecord) {
        this.hasRecord = hasRecord;
    }

    public void setMessage(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("Message should not be null!");
        }
        this.message = message;
    }

}
