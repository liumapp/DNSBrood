package com.liumapp.DNSBrood.answer.provider;

import com.liumapp.DNSBrood.antipollution.SafeHostManager;
import com.liumapp.DNSBrood.config.Configure;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xbill.DNS.Type;

/**
 * Created by liumapp on 7/14/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
@Component
public class SafeHostAnswerProvider implements AnswerProvider {

    @Autowired
    private SafeHostManager safeBoxService;

    @Autowired
    private Configure configure;

    /*
     * (non-Javadoc)
     *
     * @see
     * us.codecraft.blackhole.answer.AnswerProvider#getAnswer(java.lang.String,
     * int)
     */
    @Override
    public String getAnswer(String query, int type) {
        if (!configure.isEnableSafeBox()) {
            return null;
        }
        if (type == Type.A || type == Type.AAAA) {
            return safeBoxService.get(StringUtils.removeEnd(query, "."));
        }
        return null;
    }

}