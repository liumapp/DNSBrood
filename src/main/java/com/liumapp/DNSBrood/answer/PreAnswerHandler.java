package com.liumapp.DNSBrood.answer;

import com.liumapp.DNSBrood.answer.provider.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by liumapp on 7/14/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
@Component("preAnswerHandler")
public class PreAnswerHandler extends  AbstractAnswerHandler implements InitializingBean {

    private List<AnswerProvider> answerProviders;

    @Autowired
    private CustomTempAnswerProvider customTempAnswerProvider;

    @Autowired
    private CustomAnswerPatternProvider customAnswerPatternProvider;

    @Autowired
    private AnswerPatternProvider answerPatternProvider;

    @Autowired
    private TempAnswerProvider tempAnswerContainer;

    @Autowired
    private SafeHostAnswerProvider safeHostAnswerProvider;


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
        answerProviders.add(customTempAnswerProvider);
        answerProviders.add(customAnswerPatternProvider);
        answerProviders.add(tempAnswerContainer);
        answerProviders.add(answerPatternProvider);
        answerProviders.add(safeHostAnswerProvider);
    }

    @Override
    protected List<AnswerProvider> getaAnswerProviders() {
        return answerProviders;
    }
}
