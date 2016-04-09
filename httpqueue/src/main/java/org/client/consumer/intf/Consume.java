package org.client.consumer.intf;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.client.consumer.util.result.CommonRes;
import org.client.consumer.util.result.MsgRes;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by andilyliao on 16-4-7.
 */
public abstract class Consume {
    private static Logger log = Logger.getLogger(Consume.class);

    public String send(String url, String jsonString, String encoding) throws Exception {
        String body = "";
        //创建httpclient对象
        CloseableHttpClient client = HttpClients.createDefault();
        //创建post方式请求对象
        HttpPost httpPost;
        httpPost = new HttpPost(url);

        //设置参数到请求对象中
        httpPost.setEntity(new StringEntity(jsonString, encoding));
        log.debug("请求地址：" + url);
        log.debug("请求参数：" + jsonString);
        //设置header信息
        //指定报文头【Content-type】、【User-Agent】
//        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
//        httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        httpPost.setHeader("Content-type", "application/json");
        //执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = client.execute(httpPost);
        //获取结果实体
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            //按指定编码转换结果实体为String类型
            body = EntityUtils.toString(entity, encoding);
        }
        EntityUtils.consume(entity);
        //释放链接
        response.close();
        return body;
    }

    public static void main(String[] args) throws Exception {

        String url = "http://127.0.0.1:8845";
//        String json = "{\"head\":{\"qn\":\"mydirectqueue\",\"id\":\"uuid\",\"ty\":0,\"h\":0}}";
        String json ="{\"head\":{\"qn\":\"mydirectqueue\",\"id\":\"uuid\",\"ty\":1,\"h\":0,\"o\":0,\"s\":0}}";
        final CloseableHttpClient client = HttpClients.createDefault();
        final HttpPost httpPost;
        httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(json, "utf-8"));
        httpPost.setHeader("Content-type", "application/json");

        //执行请求操作，并拿到结果（同步阻塞）

        try {
            CloseableHttpResponse response = null;
            String body = "";

            response = client.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                //按指定编码转换结果实体为String类型
                body = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);
            //释放链接
            response.close();
            System.out.println(body);
            CommonRes cr = JSON.parseObject(body, CommonRes.class);
            System.out.println("---------------------" + cr.getCode() + "   " + cr.getStatus()+"   "+cr.getBody());
            MsgRes res=JSON.parseObject(cr.getBody(),MsgRes.class);
            System.out.println("=====================body is: "+res.getBody()+" is has data: "+res.getIsHasDate()+" offset is: "+res.getOffset()+" putset is: "+res.getPutset()+" seq is: "+res.getSeq()+" totle is: "+res.getTotleseq());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
