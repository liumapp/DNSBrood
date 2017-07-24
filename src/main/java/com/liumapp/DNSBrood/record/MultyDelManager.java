package com.liumapp.DNSBrood.record;

import com.liumapp.DNSBrood.answer.provider.CustomAnswerPatternProvider;
import com.liumapp.DNSBrood.config.Configure;
import com.liumapp.DNSBrood.utils.RecordUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liumapp on 7/24/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
@Service
public class MultyDelManager implements Manager {

    @Autowired
    private Configure configure;

    @Autowired
    private CustomAnswerPatternProvider customAnswerPatternProvider;

    @Override
    public String handle(String whatQueenSays) {

        /**
         * remove all records
         */
        String ip = StringUtils.removeStart(whatQueenSays, configure.getDeleteZonesIp());

        if (RecordUtils.isValidIpv4Address(ip)) {
            customAnswerPatternProvider.getDomainPatterns().remove(ip);
            customAnswerPatternProvider.getDomainTexts().remove(ip);
            return "REMOVE SUCCESS";
        } else {
            return "ERROR, invalid ip " + ip;
        }
    }

}
