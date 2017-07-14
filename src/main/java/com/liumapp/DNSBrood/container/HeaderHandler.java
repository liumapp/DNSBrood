package com.liumapp.DNSBrood.container;

import org.springframework.stereotype.Component;
import org.xbill.DNS.Flags;
import org.xbill.DNS.Record;
import org.xbill.DNS.Section;

/**
 * Created by liumapp on 7/14/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
@Component
public class HeaderHandler implements Handler {

    /*
     * (non-Javadoc)
     *
     * @see us.codecraft.blackhole.server.Handler#handle(org.xbill.DNS.Message,
     * org.xbill.DNS.Message)
     */
    @Override
    public boolean handle(MessageWrapper request, MessageWrapper response) {
        response.getMessage().getHeader().setFlag(Flags.QR);
        if (request.getMessage().getHeader().getFlag(Flags.RD)) {
            response.getMessage().getHeader().setFlag(Flags.RD);
        }
        Record queryRecord = request.getMessage().getQuestion();
        response.getMessage().addRecord(queryRecord, Section.QUESTION);
        return true;
    }
}
