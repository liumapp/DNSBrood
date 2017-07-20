package com.liumapp.DNSBrood.data;

import com.liumapp.DNSBrood.config.ZonesFileRefresher;
import com.liumapp.DNSBrood.config.ZonesPattern;
import com.liumapp.DNSBrood.model.Zones;
import com.liumapp.DNSBrood.service.ZonesService;
import com.liumapp.DNSQueen.worker.ready.StandReadyWorker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.regex.Pattern;

/**
 * Created by liumapp on 7/19/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
public class DataSaveManager extends StandReadyWorker {

    @Autowired
    private ZonesService zonesService;


    /**
     * you should save zones data
     * @param whatWifeSays
     * @return
     */
    @Override
    public String doWhatYouShouldDo(String whatWifeSays) {

        if (StringUtils.startsWithIgnoreCase(whatWifeSays, ZonesFileRefresher.getAddZonesIp())) {
            String line = StringUtils.removeStart(whatWifeSays, ZonesFileRefresher.getAddZonesIp());
            try {
                ZonesPattern zonesPattern = ZonesPattern.parse(line);

                if (zonesPattern == null) {
                    return "PARSE ERROR";
                }

                List<Pattern> patterns = zonesPattern.getPatterns();

                Zones zone = new Zones();
                zone.setType("A");
                zone.setDomain(zonesPattern.getTexts().toString());
                zone.setValue(zonesPattern.getTargetIp());
                zone.setCreateTime(new Date().getTime());
                zone.setUpdateTime(new Date().getTime());
                zonesService.addZones(zone);

                return "SUCCESS, " + zonesPattern.getPatterns().size() + " patterns added.";

            } catch (UnknownHostException e) {

                return "ERROR " + e;

            }

        } else if (StringUtils.startsWithIgnoreCase(whatWifeSays, ZonesFileRefresher.getDeleteZonesIp())) {

            return " not now ";

        }

        return "unknow command";

    }



}
