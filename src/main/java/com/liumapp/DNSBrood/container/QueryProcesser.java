package com.liumapp.DNSBrood.container;

import com.liumapp.DNSBrood.cache.CacheManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xbill.DNS.Message;

import java.io.IOException;

/**
 * Created by liumapp on 7/14/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
@Component
public class QueryProcesser {

    @Autowired
    private HandlerManager handlerManager;

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private CacheManager cacheManager;

    /**
     * dig's first entry
     * @param queryData
     * @return
     * @throws IOException
     */
    public byte[] process(byte[] queryData) throws IOException {

        Message query = new Message(queryData);

        if (logger.isDebugEnabled()) {
            logger.debug("get query "
                    + query.getQuestion().getName().toString());
        }

        MessageWrapper responseMessage = new MessageWrapper(new Message(query
                .getHeader().getID()));

        for (Handler handler : handlerManager.getPreHandlers()) {
            boolean handle = handler.handle(new MessageWrapper(query),
                    responseMessage);
            if (!handle) {
                break;
            }
        }

        byte[] response = null;

        if (responseMessage.hasRecord()) {
            response = responseMessage.getMessage().toWire();
            return response;
        }

        byte[] cache = cacheManager.getResponseFromCache(query);

        if (cache != null) {
            return cache;
        } else {
            for (Handler handler : handlerManager.getPostHandlers()) {
                boolean handle = handler.handle(new MessageWrapper(query),
                        responseMessage);
                if (!handle) {
                    break;
                }
            }
            if (responseMessage.hasRecord()) {
                response = responseMessage.getMessage().toWire();
                return response;
            } else {
                return null;
            }
        }

    }

}