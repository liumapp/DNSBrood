package com.liumapp.DNSBrood.container;

/**
 * Created by liumapp on 7/14/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
public interface Handler {

    /**
     * Process the request and generate the response.<br/>
     * If there are more than one handler, they are processed as a chain.<br/>
     * Unlike java servlet-api, the request is completely constructed before
     * handling(not a stream),and the response will be sent to client only if
     * all handlers process completed.
     *
     * @param request
     *            message from client
     * @param response
     *            message to client
     * @return true: pass the request to next handler<br>
     *         false: finish the entire handle process.
     */
    public boolean handle(MessageWrapper request, MessageWrapper response);

}
