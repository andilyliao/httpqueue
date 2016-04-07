package org.client.publisher.intf;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.client.publisher.util.result.CommonRes;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by andilyliao on 16-4-6.
 */
public abstract class Publish {
    private static Logger log = Logger.getLogger(Publish.class);

    public String send(String url, String jsonString, String encoding) throws Exception {
        String body = "";
        //创建httpclient对象
        CloseableHttpClient client = HttpClients.createDefault();
        //创建post方式请求对象
        HttpPost httpPost;
        httpPost = new HttpPost(url);

        //设置参数到请求对象中
        httpPost.setEntity(new StringEntity(jsonString,encoding));
        log.debug("请求地址："+url);
        log.debug("请求参数："+jsonString);
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
        String url = "http://127.0.0.1:8844";
        String url1 = "http://127.0.0.1:8844";
//        String json = "{\"head\":{\"qn\":\"aaa\",\"ty\":0,\"m\":0,\"t\":"+10000+",\"h\":0}}";
        String json = "{\"head\":{\"qn\":\"aaa\",\"ty\":1,\"h\":0,\"tr\":0,\"s\":0,\"ts\":0},\"body\":\"aaa\"}";

        final CloseableHttpClient client = HttpClients.createDefault();
        final HttpPost httpPost;
        httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(json,"utf-8"));
        httpPost.setHeader("Content-type", "application/json");


        final CloseableHttpClient client1 = HttpClients.createDefault();
        final HttpPost httpPost1;
        httpPost1 = new HttpPost(url1);
        httpPost1.setEntity(new StringEntity(json,"utf-8"));
        httpPost1.setHeader("Content-type", "application/json");
        //执行请求操作，并拿到结果（同步阻塞）
        ExecutorService exec= Executors.newCachedThreadPool();
        final CountDownLatch latch=new CountDownLatch(10000);
        long start=System.currentTimeMillis();

        for(int i=0;i<10000;i++) {
            final int finalI = i;
            exec.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        CloseableHttpResponse response=null;
                        String body = "";
                        if(finalI%2==0) {
                            response= client.execute(httpPost);
                            HttpEntity entity = response.getEntity();
                            if (entity != null) {
                                //按指定编码转换结果实体为String类型
                                body = EntityUtils.toString(entity, "utf-8");
                            }
                            EntityUtils.consume(entity);
                            //释放链接
                            response.close();
//            System.out.println(body);
                            CommonRes cr = JSON.parseObject(body, CommonRes.class);
//            System.out.println("---------------------" + cr.getCode() + "   " + cr.getStatus());
                        }else{
                            response= client1.execute(httpPost1);
                            HttpEntity entity = response.getEntity();
                            if (entity != null) {
                                //按指定编码转换结果实体为String类型
                                body = EntityUtils.toString(entity, "utf-8");
                            }
                            EntityUtils.consume(entity);
                            //释放链接
                            response.close();
//            System.out.println(body);
                            CommonRes cr = JSON.parseObject(body, CommonRes.class);
//            System.out.println("---------------------" + cr.getCode() + "   " + cr.getStatus());
                        }
                        latch.countDown();
                        //获取结果实体


                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

        }
        System.out.println(System.currentTimeMillis()-start);
        latch.await();
        System.out.println(System.currentTimeMillis()-start);
    }
}
