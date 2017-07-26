package com.liumapp.DNSBrood.answer;

import com.liumapp.DNSBrood.answer.provider.AnswerProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by liumapp on 7/14/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
@Component("postAnswerHandler")
public class PostAnswerHandler extends AbstractAnswerHandler implements InitializingBean {

    private List<AnswerProvider> answerProviders;

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        regitestProviders();
    }

    public void regitestProviders() {
        answerProviders = new LinkedList<AnswerProvider>();
    }

    @Override
    protected List<AnswerProvider> getaAnswerProviders() {
        return answerProviders;
    }

}
