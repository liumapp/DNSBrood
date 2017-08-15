package com.liumapp.DNSBrood.record.impl;

import com.liumapp.DNSBrood.answer.provider.CustomAnswerPatternProvider;
import com.liumapp.DNSBrood.config.Configure;
import com.liumapp.DNSBrood.config.ZonesPattern;
import com.liumapp.DNSBrood.model.Zones;
import com.liumapp.DNSBrood.record.Manager;
import com.liumapp.DNSBrood.service.ZonesService;
import com.liumapp.DNSBrood.utils.DoubleKeyMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.util.Date;

/**
 * Created by liumapp on 7/24/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 * update_zones_ip_LM09000:4.5.6.8 gmail.liumapp.com
 */
@Service("UpdateZoneManager")
public class UpdateZoneManager implements Manager {

    @Autowired
    private Configure configure;

    @Autowired
    private CustomAnswerPatternProvider customAnswerPatternProvider;

    @Autowired
    private ZonesService zonesService;

    @Override
    public String handle(String whatQueenSays) {

        String line = StringUtils.removeStart(whatQueenSays , configure.getUpdateZonesIp());

        DoubleKeyMap<String , String , String> domainTexts = customAnswerPatternProvider.getDomainTexts();

        try {

            ZonesPattern zonesPattern = ZonesPattern.parse(line);

            if (zonesPattern == null) {
                return "ERROR:" + whatQueenSays + "cannot match any records";
            }

            String domain = zonesPattern.getTexts().get(0);

            String userNumber = domainTexts.get(domain,"userNumber");

            if (!userNumber.equals(zonesPattern.getUserNumber())) {
                return "ERROR:" + whatQueenSays + " has no access to control ";
            }

            Zones zone = zonesService.getVerifiedZone(domain , userNumber);

            /**
             * update the zones from dnsJava
             */
            if (domain == null || domain == "") {
                return "ERROR:" + whatQueenSays + "cannot find the domain from map";
            } else {
                domainTexts.put(domain , "ip" , zonesPattern.getTargetIp());
                domainTexts.put(domain , "userNumber" , zonesPattern.getUserNumber());
            }

            /**
             * update database
             */
            if (zone == null) {
                // add new zone
                Zones tmp = new Zones(zonesPattern.getUserNumber(),zonesPattern.getTexts().get(0) , zonesPattern.getTargetIp() , "A" , new Date().getTime() , new Date().getTime());
                zonesService.addZones(zone);
            } else {
                zone.setValue(zonesPattern.getTargetIp());
                zone.setUpdateTime(new Date().getTime());
                zonesService.updateZones(zone);
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return "success";

    }

}
