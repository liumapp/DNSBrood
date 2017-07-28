package com.liumapp.DNSBrood.record.impl;

import com.liumapp.DNSBrood.answer.provider.CustomAnswerPatternProvider;
import com.liumapp.DNSBrood.config.Configure;
import com.liumapp.DNSBrood.record.Manager;
import com.liumapp.DNSBrood.utils.DoubleKeyMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liumapp on 7/24/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 * select_zones_ip_gmail.liumapp.com
 */
@Service("SelectZoneManager")
public class SelectZoneManager implements Manager {

    @Autowired
    private Configure configure;

    @Autowired
    private CustomAnswerPatternProvider customAnswerPatternProvider;

    @Override
    public String handle(String whatQueenSays) {

        String domain = StringUtils.removeStart(whatQueenSays , configure.getSelectZonesIp());

        DoubleKeyMap<String , String , String> domainTexts = customAnswerPatternProvider.getDomainTexts();

        /**
         * 直接从map中检查
         */
        String ip = domainTexts.get(domain , "ip");

        return ip;

    }
}
