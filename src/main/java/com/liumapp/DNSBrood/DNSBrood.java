package com.liumapp.DNSBrood;

import com.liumapp.DNSBrood.config.Configure;
import com.liumapp.DNSBrood.connector.UDPSocketMonitor;
import com.liumapp.DNSBrood.utils.SpringLocator;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Created by liumapp on 7/14/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
@Component
public class DNSBrood {

    private boolean isShutDown = false;

    private static Logger logger = Logger.getLogger(DNSBrood.class);

    private UDPSocketMonitor udpSocketMonitor;

    public void start() throws UnknownHostException, IOException {
        udpSocketMonitor = SpringLocator.getBean(UDPSocketMonitor.class);
        udpSocketMonitor.start();
    }

    private static void parseArgs(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption(new Option("d", true, "home path"));
        CommandLineParser commandLineParser = new PosixParser();
        CommandLine commandLine = commandLineParser.parse(options, args);
        readOptions(commandLine);
    }

    private static void readOptions(CommandLine commandLine) {
        if (commandLine.hasOption("d")) {
            String filename = commandLine.getOptionValue("d");
            Configure.FILE_PATH = filename;
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            parseArgs(args);
        } catch (ParseException e1) {
            logger.warn("parse args error");
        }
        SpringLocator.applicationContext = new ClassPathXmlApplicationContext(
                "classpath*:/spring/applicationContext*.xml");
        DNSBrood dnsBrood = SpringLocator.getBean(DNSBrood.class);
        try {
            dnsBrood.start();
        } catch (UnknownHostException e) {
            logger.warn("init failed ", e);
        } catch (IOException e) {
            logger.warn("init failed ", e);
        }
        while (!dnsBrood.isShutDown) {
            try {
                Thread.sleep(10000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
