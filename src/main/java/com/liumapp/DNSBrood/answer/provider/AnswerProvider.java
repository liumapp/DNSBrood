package com.liumapp.DNSBrood.answer.provider;

/**
 * Created by liumapp on 7/14/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
public interface AnswerProvider {
    /**
     *
     * @param query
     * @param type
     * @return
     */
    public String getAnswer(String query, int type);
}
