package org.httpqueue.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by andilyliao on 16-3-31.
 */
public class PropertiesStr {
    //生产者链接的地址
    public static int inBound;
    //消费者链接的地址
    public static int outBound;
    public void initProperties() throws IOException {
        Properties prop = new Properties();
        InputStream in = this.getClass().getResourceAsStream("/httpqueue.properties");
        prop.load(in);
        PropertiesStr.inBound=Integer.parseInt(prop.getProperty("InBound"));
        PropertiesStr.outBound=Integer.parseInt(prop.getProperty("OutBound"));
    }
}
