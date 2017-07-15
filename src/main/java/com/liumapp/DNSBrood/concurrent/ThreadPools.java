package com.liumapp.DNSBrood.concurrent;

import com.liumapp.DNSBrood.config.Configure;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by liumapp on 7/15/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
@Component
public class ThreadPools implements InitializingBean {

    private ThreadPoolExecutor mainProcessExecutor;
    private ThreadPoolExecutor udpReceiverExecutor;
    private int threadNum = 0;
    @Autowired
    private Configure configure;
    private Log logger = LogFactory.getLog(getClass());

    public void resize() {
        if (threadNum != configure.getThreadNum()) {
            threadNum = configure.getThreadNum();
            logger.info("Thread num changed, resize to " + threadNum);
            if (threadNum < configure.getThreadNum()) {
                mainProcessExecutor.setMaximumPoolSize(threadNum);
                mainProcessExecutor.setCorePoolSize(threadNum);
                udpReceiverExecutor.setMaximumPoolSize(threadNum);
                udpReceiverExecutor.setCorePoolSize(threadNum);
            } else {
                mainProcessExecutor.setCorePoolSize(threadNum);
                mainProcessExecutor.setMaximumPoolSize(threadNum);
                udpReceiverExecutor.setCorePoolSize(threadNum);
                udpReceiverExecutor.setMaximumPoolSize(threadNum);
            }
        }
    }

    public ExecutorService getMainProcessExecutor() {
        return mainProcessExecutor;
    }

    public ExecutorService getUdpReceiverExecutor() {
        return udpReceiverExecutor;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        threadNum = configure.getThreadNum();
        mainProcessExecutor = new ThreadPoolExecutor(threadNum, threadNum, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        udpReceiverExecutor = new ThreadPoolExecutor(threadNum, threadNum, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    }
}
