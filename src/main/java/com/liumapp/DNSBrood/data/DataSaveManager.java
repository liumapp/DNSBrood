package com.liumapp.DNSBrood.data;

import com.liumapp.DNSBrood.config.ZonesFileRefresher;
import com.liumapp.DNSBrood.config.ZonesPattern;
import com.liumapp.DNSQueen.worker.ready.StandReadyWorker;
import org.apache.commons.lang3.StringUtils;

import java.net.UnknownHostException;
import java.util.regex.Pattern;

/**
 * Created by liumapp on 7/19/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
public class DataSaveManager extends StandReadyWorker {


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
