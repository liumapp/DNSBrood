package com.liumapp.DNSBrood.answer.provider;

import com.liumapp.DNSBrood.context.RequestContext;
import com.liumapp.DNSBrood.utils.DoubleKeyMap;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liumapp on 7/14/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
@Component
public class CustomTempAnswerProvider implements AnswerProvider {

    private Map<String,DoubleKeyMap<String, Integer, String>> container;

    public CustomTempAnswerProvider() {
        container = new ConcurrentHashMap<String, DoubleKeyMap<String, Integer, String>>();
    }

    /**
     *
     * @param query
     * @param type
     * @return
     */
    @Override
    public String getAnswer(String query, int type) {
        String ip = RequestContext.getClientIp();
        DoubleKeyMap<String, Integer, String> stringIntegerStringDoubleKeyMap = container.get(ip);
        if (stringIntegerStringDoubleKeyMap==null){
            return null;
        }
        return stringIntegerStringDoubleKeyMap.get(query, type);
    }

    public void add(String clientIp,String query, int type, String answer) {
        DoubleKeyMap<String, Integer, String> stringIntegerStringDoubleKeyMap = container.get(clientIp);
        if (stringIntegerStringDoubleKeyMap==null){
            stringIntegerStringDoubleKeyMap = new DoubleKeyMap<String, Integer, String>(ConcurrentHashMap.class);
            container.put(clientIp,stringIntegerStringDoubleKeyMap);
        }
        stringIntegerStringDoubleKeyMap.put(query, type, answer);
    }

}