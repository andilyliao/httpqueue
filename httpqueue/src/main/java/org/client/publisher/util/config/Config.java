package org.client.publisher.util.config;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by andilyliao on 16-4-6.
 */
public class Config extends HashMap<String,String>{
    private static Logger log = Logger.getLogger(Config.class);
    public static final String HTTPQUEUE_SERVER="urls";
    //服务地址
    public Config(){

    }
    public Config(String path) throws IOException {
        Properties prop = new Properties();
        InputStream in = this.getClass().getResourceAsStream(path);//TODO 修改成非classpath路径
        prop.load(in);
        String[] hostsandports=prop.getProperty(HTTPQUEUE_SERVER).split(",");
        log.debug(hostsandports.length);
        for(int i=0;i<hostsandports.length;i++){
            String onehostport=hostsandports[i];
            this.put(HTTPQUEUE_SERVER,onehostport);
        }
    }

}
