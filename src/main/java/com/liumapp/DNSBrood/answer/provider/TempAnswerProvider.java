package com.liumapp.DNSBrood.answer.provider;

import com.liumapp.DNSBrood.utils.DoubleKeyMap;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liumapp on 7/14/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
@Component
public class TempAnswerProvider implements AnswerProvider {

    private DoubleKeyMap<String, Integer, String> container;

    public TempAnswerProvider() {
        container = new DoubleKeyMap<String, Integer, String>(
                ConcurrentHashMap.class);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * us.codecraft.blackhole.answer.AnswerProvider#getAnswer(java.lang.String,
     * int)
     */
    @Override
    public String getAnswer(String query, int type) {
        return container.get(query, type);
    }

    public void add(String query, int type, String answer) {
        container.put(query, type, answer);
    }

}
