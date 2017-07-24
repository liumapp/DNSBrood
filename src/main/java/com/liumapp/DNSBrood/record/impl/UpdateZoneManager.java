package com.liumapp.DNSBrood.record.impl;

import com.liumapp.DNSBrood.record.Manager;
import org.springframework.stereotype.Service;

/**
 * Created by liumapp on 7/24/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
@Service
public class UpdateZoneManager implements Manager {

    @Override
    public String handle(String whatQueenSays) {
        return "success";
    }

}
